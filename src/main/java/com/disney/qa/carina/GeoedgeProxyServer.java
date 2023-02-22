package com.disney.qa.carina;

import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.disney.DisneyCountryData;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.proxy.auth.AuthType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GeoedgeProxyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String COUNTRY = "country";
    private static String proxyIp = "";
    private static boolean isIpSet = false;
    protected final RestTemplate restTemplate = RestTemplateBuilder
            .newInstance()
            .withSpecificJsonMessageConverter()
            .withUtf8EncodingMessageConverter()
            .build();
    AllowListManager allowList = new AllowListManager();
    private Yaml yaml = new Yaml();
    private InputStream countryStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("geoedge/proxy-servers.yaml");
    private ArrayList<Object> countryList = yaml.load(countryStream);
    private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
    private boolean useGeoEdgeProxyIpRequest = R.CONFIG.getBoolean("proxy_server_geoedge");
    private int proxyPortForThread = 0;

    public static void setGeoEdgeProxyIp(String countryIp) {
        proxyIp = countryIp;
        setIp(true);
    }

    public static void setIp(boolean setIP) {
        isIpSet = setIP;
    }

    private boolean isIpSet() {
        return isIpSet;
    }

    /**
     * Retrieve a new GeoEdge proxy, attempts to stop any proxy that is currently running prior.
     *
     * @param country searches for a proxy matching the country in question.
     * @return returns BrowserMobProxy connected to the correct Geoedge proxy server.
     * @throws Exception is returned if the country in question can't be found.
     */
    @java.lang.SuppressWarnings("squid:S00112")
    public BrowserMobProxy getGeoedgeProxy(String country) {
        DisneyCountryData disneyCountryData = new DisneyCountryData();
        this.proxyPortForThread = ProxyPool.getProxy().getPort(); //get port is available only before stop!!!
        ProxyPool.stopProxy();

        BrowserMobProxy proxy = ProxyPool.createProxy();
        proxy.setTrustAllServers(true);
        proxy.setMitmDisabled(false);
        proxy.setConnectTimeout(180, TimeUnit.SECONDS);

        //Returns null because it finds the country but not the field to return
        String isGeoEdgeUnsupported =
                disneyCountryData.searchAndReturnCountryData(country, COUNTRY, "isGeoEdgeUnsupportedRegion");
        boolean isBrightData = isGeoEdgeUnsupported != null;
        String countryIp = getGeoEdgeProxyIp(country);
        if (isBrightData) {
            proxy.setChainedProxy(new InetSocketAddress(R.TESTDATA.get("bright_data_super_proxy"), 22225));
            String password = R.TESTDATA.get("bright_data_residential_pass");
            String username = R.TESTDATA.get("bright_data_residential_un") +
                    disneyCountryData.searchAndReturnCountryData(country, COUNTRY, "code").toLowerCase();
            proxy.chainedProxyAuthorization(username, password, AuthType.BASIC);
        } else if (!countryIp.contains(":")) {
            if (!country.equalsIgnoreCase("united states")) {
                LOGGER.info("Setting upstream proxy for country: {} {}" , country, countryIp);
                proxy.setChainedProxy(new InetSocketAddress(countryIp, 443));
                String password = cryptoTool.decrypt(R.TESTDATA.get("browsermob_pw"));
                proxy.chainedProxyAuthorization(R.TESTDATA.get("browsermob_un"), password, AuthType.BASIC);
                allowList.addIpToAllowList(countryIp);
                LOGGER.info("GeoEdge Host Set to: {}", proxy.getChainedProxy().getHostName());
            }
        } else {
            String[] splitHost = countryIp.split(":");
            proxy.setChainedProxy(new InetSocketAddress(splitHost[0], Integer.parseInt(splitHost[1])));
            allowList.addIpToAllowList(splitHost[0]);
            LOGGER.info("GeoEdge Host Set to: {}", proxy.getChainedProxy().getHostName());
        }

        return proxy;
    }

    /**
     * Scans through the proxy-servers.yaml for the country in question and returns it's IP.
     *
     * @param country searches for the country passed.
     * @return brings back a string of the IP of the country found.
     * @throws Exception is returned if the country in question can't be found.
     */
    @java.lang.SuppressWarnings("squid:S00112")
    public String retrieveProxyCountryHost(String country) {

        for (Object item : countryList) {
            Map<String, String> server = (HashMap<String, String>) item;
            LOGGER.debug("Current Country Being Checked: {}", server.get(COUNTRY));
            if (country.equalsIgnoreCase(server.get(COUNTRY))) {
                return server.get("ip");
            }
        }

        throw new RuntimeException(String.format("Country (%s) provided does not exist in list of known proxies!", country));
    }

    /**
     * When utilizing Selenoid the default proxy host oof 127.0.0.1 will not work due to the docker container
     * checking inside itself for the running proxy, whereas the running proxy will be running local to the host
     * server it's on instead.  What the below attempts to do is to pull back that particular IP for use and set it.
     */
    public void setProxyHostForSelenoid() {
        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) en.nextElement();
                Enumeration ee = ni.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress ia = (InetAddress) ee.nextElement();
                    if (!ia.isLoopbackAddress() && !ia.isLinkLocalAddress()
                            && ia.isSiteLocalAddress() && !ia.getCanonicalHostName().contains("192.")) {
                        LOGGER.debug("Checking Canonical Host Name: " + ia.getCanonicalHostName());
                        LOGGER.debug("Checking Host Address: " + ia.getHostAddress());
                        LOGGER.debug("Link Local: " + ia.isLinkLocalAddress());
                        LOGGER.debug("Local Address: " + ia.isSiteLocalAddress());
                        LOGGER.debug("Is Reachable: " + ia.isReachable(1));
                        R.CONFIG.put("browsermob_host", ia.getHostAddress());
                        allowList.addIpToAllowList(ia.getHostAddress());
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Proxy Setup Error Recorded: " + ex.getMessage(), ex);
        }
    }

    /**
     * This will check the proxy port that was started when we called browsermob_proxy = true in the @BeforeClass.  It will
     * then pull that back for use for Geoedge Proxying.
     */
    @java.lang.SuppressWarnings("squid:S00112")
    @Deprecated
    public int getAndSetDynamicProxyPort() {

        String currentPort = Configuration.get(Configuration.Parameter.PROXY_PORT);

        LOGGER.info(String.format("Checking Proxy Info: %s:%s", Configuration.get(Configuration.Parameter.PROXY_HOST), currentPort));

        R.CONFIG.put("browsermob_port", currentPort);
        String bmPort = R.CONFIG.get("browsermob_port");

        if (bmPort.isEmpty()) {
            throw new RuntimeException("Browser Mob Port Value wasn't properly set for Proxy, aborting.");
        }
        return Integer.parseInt(bmPort);
    }

    /**
     * This will grab the port value of the stored proxy on the current thread.
     *
     * @return brings back BrowserMobProxy port that was set on the current thread.
     */
    public int getProxyPortForThread() {
        return this.proxyPortForThread;
    }

    public JsonNode getGeoEdgeProxyServerJson() throws URISyntaxException {
        JsonNode proxyBody = null;
        URI uri = new URI(R.TESTDATA.get("geoedge_proxy_uri"));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + cryptoTool.decrypt(R.TESTDATA.get("geoedge_auth_token")));
        try {
            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
            proxyBody = restTemplate.exchange(request, JsonNode.class).getBody();
        } catch (HttpClientErrorException hex) {
            logHttpError(hex);
        }
        LOGGER.debug("GeoEdge Proxies {}", proxyBody);
        return proxyBody;
    }

    public String getGeoEdgeProxyIp(String country) {
        String geoEdgeIpAddress = null;
        DisneyCountryData countryData = new DisneyCountryData();
        String yamlHostIp = retrieveProxyCountryHost(country);
        country = countryData.searchAndReturnCountryData(country, COUNTRY, "code");
        if (!isIpSet()) {
            try {
                if (!useGeoEdgeProxyIpRequest) {
                    LOGGER.info("Falling back to local proxy-server IP. Country: {}, IP: {}", country, yamlHostIp);
                    return yamlHostIp;
                } else {
                    geoEdgeIpAddress = getGeoEdgeProxyServerJson().findParent("response").findPath(country).findPath("servers").findPath("host").asText();
                    setGeoEdgeProxyIp(geoEdgeIpAddress);
                    setIp(true);
                    LOGGER.info("Using GeoEdge Proxy API IP. Country: {}, IP: {}", country, geoEdgeIpAddress);
                }
            } catch (URISyntaxException e) {
                e.getMessage();
            } catch (HttpClientErrorException hex) {
                logHttpError(hex);
            } catch (Exception e) {
                LOGGER.info("Error in requesting GeoEdge API IP, falling back to yaml local stored Ip");
                return yamlHostIp;
            }
        } else {
            return proxyIp;
        }
        return geoEdgeIpAddress;
    }

    private void logHttpError(HttpClientErrorException hex) {
        LOGGER.error("HTTP Status Code Returned: {}", hex.getStatusCode());
        LOGGER.error("HTTP Response Body with Error: {}", hex.getResponseBodyAsString());
        LOGGER.error("HTTP Headers: {}", hex.getResponseHeaders());
    }
}
