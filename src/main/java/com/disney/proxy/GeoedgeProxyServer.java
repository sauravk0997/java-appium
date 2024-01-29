package com.disney.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.proxy.ZebrunnerProxyBuilder;
import com.zebrunner.carina.webdriver.proxy.mode.UpstreamMode;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * todo remove usage of RestTemplateBuilder class
 */
public class GeoedgeProxyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PROXY_URL = "http://%s:%s";
    private static final LazyInitializer<ArrayList<HashMap<String, String>>> YAML_PROXY_SERVERS = new LazyInitializer<>() {
        @Override
        protected ArrayList<HashMap<String, String>> initialize() {
            try (InputStream countryStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("geoedge/proxy-servers.yaml")) {
                return new Yaml().load(countryStream);
            } catch (IOException e) {
                LOGGER.error("Cannot read proxy-server.yaml. Message: {}", e.getMessage(), e);
                return ExceptionUtils.rethrow(e);
            }
        }
    };
    private static final Map<String, LazyInitializer<Optional<String>>> COUNTRY_PROXY_LAZY_INITIALIZER = new ConcurrentHashMap<>();

    private GeoedgeProxyServer() {
        //hide
    }

    /**
     * Get GeoEdge proxy ip using GeoEdge API. If something went wrong with API, use yaml configuration instead
     *
     * @param countryCode country code, for example {@code LI}
     * @return {@link Optional} with the proxy ip if it exists for this country, {@link Optional#empty()} otherwise
     */
    public static Optional<String> getGeoEdgeIp(String countryCode) {
        try {
            COUNTRY_PROXY_LAZY_INITIALIZER.putIfAbsent(countryCode, new LazyInitializer<>() {
                @SneakyThrows(ConcurrentException.class)
                @Override
                protected Optional<String> initialize() {
                    String proxyIp = null;
                    try {
                        proxyIp = Optional.ofNullable(Objects.requireNonNull(getGeoEdgeProxyServerJson())
                                        .findParent("response")
                                        .findPath(countryCode)
                                        .findPath("servers")
                                        .findPath("host")
                                        .asText())
                                .filter(StringUtils::isNotBlank)
                                .orElse(null);
                        LOGGER.info("Using GeoEdge Proxy API IP. Country: {}, IP: {}", countryCode, proxyIp);
                    } catch (HttpClientErrorException hex) {
                        logHttpError(hex);
                    } catch (Exception e) {
                        LOGGER.error("Error in requesting GeoEdge API IP, falling back to yaml local stored Ip. Message: {}", e.getMessage(), e);
                    }

                    if (proxyIp == null) {
                        //Scans through the proxy-servers.yaml for the country in question and returns it's IP.
                        proxyIp = YAML_PROXY_SERVERS.get()
                                .stream()
                                .filter(server -> StringUtils.equalsIgnoreCase(countryCode, server.get("code")))
                                .findFirst()
                                .map(map -> map.get("ip"))
                                .filter(StringUtils::isNotBlank)
                                .orElse(null);
                    }
                    return Optional.ofNullable(proxyIp);
                }
            });
            return COUNTRY_PROXY_LAZY_INITIALIZER.get(countryCode)
                    .get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    /**
     * Get {@link ZebrunnerProxyBuilder} with the GeoEdge proxy configuration
     *
     * @param countryCode searches for a proxy matching the country code in question.
     * @return returns {@link ZebrunnerProxyBuilder} configured to the correct Geoedge proxy server.
     * @throws Exception is returned if the country in question can't be found.
     */
    @java.lang.SuppressWarnings("squid:S00112")
    public static ZebrunnerProxyBuilder getGeoedgeProxy(String countryCode) {
        ZebrunnerProxyBuilder builder = new ZebrunnerProxyBuilder();
        LOGGER.info("Proxy Country IP {}", countryCode);
        Optional<String> geoEdgeProxyIp = getGeoEdgeIp(countryCode);
        if (geoEdgeProxyIp.isEmpty()) {
            LOGGER.warn("There are no GeoEdge proxy for '{}' country, so superproxy will be used instead.", countryCode);
            builder.addMode(new UpstreamMode(String.format(PROXY_URL, R.TESTDATA.get("bright_data_super_proxy"), 22225)));
            String password = R.TESTDATA.get("bright_data_residential_pass");
            String username = R.TESTDATA.get("bright_data_residential_un") + countryCode.toLowerCase();
            builder.upstreamAuth(username, password);
        } else if (!geoEdgeProxyIp.get().contains(":")) {
            LOGGER.info("Setting upstream proxy for country: {} {}", countryCode, geoEdgeProxyIp.get());
            builder.addMode(new UpstreamMode(String.format(PROXY_URL, geoEdgeProxyIp.get(), 443)));
            String password = R.TESTDATA.getDecrypted("browserup_pw");
            builder.upstreamAuth(R.TESTDATA.get("browserup_un"), password);
            LOGGER.info("GeoEdge Host Set to: {}", R.TESTDATA.get("browserup_un"));
        } else {
            String[] splitHost = geoEdgeProxyIp.get().split(":");
            builder.addMode(new UpstreamMode(String.format(PROXY_URL, splitHost[0], splitHost[1])));
            LOGGER.info("GeoEdge Host Set to: {}", splitHost[0]);
        }

        return builder;
    }

    private static JsonNode getGeoEdgeProxyServerJson() throws URISyntaxException {
        JsonNode proxyBody = null;
        URI uri = new URI(R.TESTDATA.get("geoedge_proxy_uri"));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + R.TESTDATA.getDecrypted("geoedge_auth_token"));
        try {
            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
            proxyBody = RestTemplateBuilder
                    .newInstance()
                    .withSpecificJsonMessageConverter()
                    .withUtf8EncodingMessageConverter()
                    .build()
                    .exchange(request, JsonNode.class).getBody();
        } catch (HttpClientErrorException hex) {
            logHttpError(hex);
        }
        LOGGER.debug("GeoEdge Proxies {}", proxyBody);
        return proxyBody;
    }

    private static void logHttpError(HttpClientErrorException hex) {
        LOGGER.error("HTTP Status Code Returned: {}", hex.getStatusCode());
        LOGGER.error("HTTP Response Body with Error: {}", hex.getResponseBodyAsString());
        LOGGER.error("HTTP Headers: {}", hex.getResponseHeaders());
    }
}
