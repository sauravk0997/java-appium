package com.disney.charles;

import com.disney.exceptions.charles.CharlesProxyException;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.disney.DisneyCountryData;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class CharlesProxy {

    private static final String CHARLES_ENDPOINT = "http://control.charles";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestTemplate restTemplate;
    private Process process;

    private boolean isStarted;
    private final String charlesPath;
    private final int port;


    public CharlesProxy(int port, String path) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", port));
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(proxy);
        restTemplate = new RestTemplate(requestFactory);
        isStarted = false;
        this.port = port;
        charlesPath = path != null ? path : "/tools/charles/bin/charles ";
    }

    public void startProxy(String country, String deviceIP) {
        String charlesConfig = System.getProperty("user.dir") + "/" + getCharlesConfig(country, deviceIP, port);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-l", "-c", charlesPath + charlesConfig + " -headless");
        try {
            process = processBuilder.start();
            LOGGER.info("Starting Charles proxy...");
            Thread.sleep(15000);
            LOGGER.info("Charles proxy started...");
            isStarted = true;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new CharlesProxyException(e);
        }
    }

    public void stopProxy(){
        LOGGER.info("Stopping Charles proxy...");
        process.destroy();
        LOGGER.info("Charles Proxy stopped...");
        isStarted = false;
    }

    public void startCapture() {
        if (isStarted) {
            try {
                ResponseEntity<String> httpResponse = restTemplate.getForEntity(
                        new URI(CHARLES_ENDPOINT + CharlesControlAPIPath.RECORDING_START.getPath()),
                        String.class
                );
                LOGGER.info("Network Capture {}...",
                        httpResponse.getStatusCode() == HttpStatus.OK ? "started" : "did not start");
            } catch (URISyntaxException e) {
                throw new CharlesProxyException(e);
            }
        }
    }

    public void stopCapture() {
        if (isStarted) {
            try {
                ResponseEntity<String> httpResponse = restTemplate.getForEntity(
                        new URI(CHARLES_ENDPOINT + CharlesControlAPIPath.RECORDING_STOP.getPath()),
                        String.class
                );
                LOGGER.info("Network traffic capture {}...",
                        httpResponse.getStatusCode() == HttpStatus.OK ? "stopped" : "was not stopped");
            } catch (URISyntaxException e) {
                throw new CharlesProxyException(e);
            }
        }
    }

    public void downloadSessionCapture(String captureName) {
        try {
            File file = restTemplate.execute(
                    CHARLES_ENDPOINT + CharlesControlAPIPath.SESSION_DOWNLOAD.getPath(),
                    HttpMethod.GET,
                    null,
                    clientHttpResponse -> {
                        String uuid = UUID.randomUUID().toString();
                        File tmp = File.createTempFile(uuid, "tmp");
                        StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(tmp));
                        return tmp;
                    });
            FileUtils.copyFile(file, new File(captureName));
        } catch (Exception e) {
            throw new CharlesProxyException(e);
        }
    }

    private String getCharlesConfig(String country, String deviceIP, int port) {
        DisneyCountryData disneyCountryData = new DisneyCountryData();
        String isGeoEdgeUnsupported =
                disneyCountryData.searchAndReturnCountryData(country, "country", "isGeoEdgeUnsupportedRegion");
        boolean isBrightData = StringUtils.isNotBlank(isGeoEdgeUnsupported);
        String uuid = UUID.randomUUID().toString();
        String configFile = uuid + "-charles.config";
        String tmpFile = "tmp/" + configFile;
        String defaultConfig = "./com/disney/charles/config/";
        CharlesConfig charlesConfig = new CharlesConfig(
                defaultConfig + (isBrightData ? "charles-bright-data.config" : "charles.config"),
                tmpFile
        );

        if (isBrightData) {
            String countryCode = disneyCountryData.searchAndReturnCountryData(country, "country", "code");
            charlesConfig.updateUserName(R.TESTDATA.get("bright_data_residential_un") + countryCode.toLowerCase());
        } else {
            charlesConfig.updateProxyHost(new GeoedgeProxyServer().retrieveProxyCountryHost(country));
        }
        charlesConfig.updateIp(deviceIP);
        charlesConfig.updatePort(port);
        charlesConfig.generateConfigFile(configFile);
        return configFile;
    }
}
