package com.disney.qa.api.disney.windows10;

import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Base64;
import java.util.Iterator;

public class DisneyWindowsAppLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String HEADER_TEMPLATE = "Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"\r\nContent-Type: %s\r\n\r\n";
    private static final String GITHUB_BASE_URL = "https://github.bamtech.co/api/v3";
    private static final String REPOS = "/repos";
    private static final String TOKEN = "token ";
    private static final String RELEASE_ENDPOINT = "/releases";
    private static final String ASSET_RELEASE_ENDPOINT = "/releases/assets/";
    private static final String GITHUB_USER_TOKEN = "NWQyZDY2Y2ZmNGVkYjRlMWQ3MDY5MTE0ZDNkYTVmZTMxMDY5NDZkMQ==";
    private static final String SESSION_REQUEST = "Session - Request: {}";
    private static final String SESSION_RESPONSE = "Session - Response: {}";
    private static final String REPO_NAME = "/fed-ce-dmgz/dmgz-uwp-app";
    private static final String DEVICE_PORTAL_HOST = R.TESTDATA.get("device_portal_ip");
    private static final String DEVICE_PORTAL_PORT = "50080";
    private static final String MSIX_EXTENSION = ".msix";
    private static final String CER_EXTENSION = ".cer";
    private static final String ZIP_EXTENSION = ".zip";
    private static final String X_CSRF_TOKEN = "X-CSRF-Token";
    private static final String DEVICE_PORTAL_UN_PW = "y";
    private static final String J_TOKEN_NAME = "name";
    private static final String DEVICE_PORTAL_URL_APP = "http://%s:%s/api/app/packagemanager/package?package=%s";
    private static final String DEVICE_PORTAL_URL = "http://%s:%s/";
    private static final String DEVICE_PORTAL_CERT = "api/app/packagemanager/certificate";
    private static final String NEW_LINE = "\r\n--";
    private static final String TO_REMOVE_TEST = "_Test.zip";
    protected RestTemplate restTemplate;
    private String releaseName = "";
    private String csrfToken;

    public DisneyWindowsAppLoader() {
        restTemplate = RestTemplateBuilder
                .newInstance()
                .withBasicAuthentication(DEVICE_PORTAL_UN_PW, DEVICE_PORTAL_UN_PW)
                .withSpecificJsonMessageConverter()
                .withUtf8EncodingMessageConverter()
                .withDisabledSslChecking()
                .build();
        csrfToken = getCSRFToken();
    }

    public String getReleaseName() {
        return releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    public void uninstallApp() {
        ResponseEntity<String> responseEntity;
        RequestEntity<String> request;
        String appName = "Disney.37853FC22B2CE_1.19.10.0_x64__6rarf9sa4v8jt";
        String version = StringUtils.substringBetween(getReleaseName(), "_", "_x");
        LOGGER.info("pattern {}", version);
        try {
            URI uri = new URI(String.format(DEVICE_PORTAL_URL_APP, DEVICE_PORTAL_HOST, DEVICE_PORTAL_PORT, StringUtils.replace(appName, "_1.19.10.0_x64", "_" + version + "_x64")));
            HttpHeaders headers = new HttpHeaders();
            headers.add(X_CSRF_TOKEN, csrfToken);
            request = new RequestEntity<>(headers, HttpMethod.DELETE, uri);
            LOGGER.info(SESSION_REQUEST, request);
            responseEntity = restTemplate.exchange(request, String.class);
            LOGGER.info(SESSION_RESPONSE, responseEntity);
        } catch (Exception e) {
            LOGGER.error("Failed to uninstall app: {}", e.getMessage());
        }
    }

    public String getCSRFToken() {
        ResponseEntity<String> responseEntity;
        RequestEntity<String> request;
        try {
            URI uri = new URI(String.format(DEVICE_PORTAL_URL, DEVICE_PORTAL_HOST, DEVICE_PORTAL_PORT));
            request = new RequestEntity<>(HttpMethod.POST, uri);
            LOGGER.info(SESSION_REQUEST, request);
            responseEntity = restTemplate.exchange(request, String.class);
            String token = responseEntity.getHeaders().get("Set-Cookie").get(0);
            csrfToken = token.replace("CSRF-Token=", "");
            LOGGER.info(SESSION_RESPONSE, responseEntity);
        } catch (Exception e) {
            LOGGER.error("Failed to get CSRF token: {}", e.getMessage());
        }
        return csrfToken;
    }

    public String getLatestRelease() {
        byte[] actualByte = Base64.getDecoder().decode(GITHUB_USER_TOKEN);
        String decodedToken = new String(actualByte);
        String releaseId = null;

        try {
            URI uri = new URI(GITHUB_BASE_URL + REPOS + REPO_NAME + RELEASE_ENDPOINT);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, TOKEN + decodedToken);
            headers.add(HttpHeaders.ACCEPT, "application/vnd.github.v3.raw");

            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
            LOGGER.debug("Github releases - Request: {}", request);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            LOGGER.debug("Github releases - Response: {}", response);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.getBody());
            for (Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
                JsonNode node1 = it.next();
                if (!node1.findValue(J_TOKEN_NAME).asText().contains("Test")) {
                    var assets = node1.findValues("assets");
                    ArrayNode elements = (ArrayNode) assets.get(0);
                    for (JsonNode assetNode : elements
                    ) {
                        if (assetNode.findValue(J_TOKEN_NAME).asText().contains("zip") && assetNode.findValue(J_TOKEN_NAME).asText().contains("x64")) {
                            releaseId = assetNode.findValue("id").asText();
                            setReleaseName(assetNode.findValue(J_TOKEN_NAME).asText());
                            LOGGER.info("release id for the latest release is {}", releaseId);
                            LOGGER.info("releaseName for the latest release is {}", getReleaseName());
                            return releaseId;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Unable to retrieve latest release id from GitHub: {}", ex.getMessage());
        }
        return releaseId;
    }

    public Path downloadBuildFromGitHub() throws IOException {
        byte[] actualByte = Base64.getDecoder().decode(GITHUB_USER_TOKEN);
        String decodedToken = new String(actualByte);
        Path disneyDirectory = Files.createTempDirectory("Disney");
        Path builds = Files.createTempFile("builds", ZIP_EXTENSION);
        try (ZipFile zipFile = new ZipFile(builds.toString())) {
            URI uri = new URI(GITHUB_BASE_URL + REPOS + REPO_NAME + ASSET_RELEASE_ENDPOINT + getLatestRelease());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, TOKEN + decodedToken);
            headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
            Files.write(builds, response.getBody());
            LOGGER.info("Status code for Github download call: {}", response.getStatusCode());
            zipFile.extractAll(disneyDirectory.toString());
            LOGGER.info("Downloaded zip file here: {}", builds);
            LOGGER.info("Extracted zip file here: {}", disneyDirectory);
        } catch (Exception e) {
            LOGGER.error("Failed to download build from GitHub: {}", e.getMessage());
        }
        return disneyDirectory;
    }

    private byte[] setByteOutStream(String bundleName, Path bundle, String boundary) throws IOException {
        String buildName = getReleaseName().replace(ZIP_EXTENSION, "");
        byte[] boundaryBytes = (NEW_LINE + boundary + "\r\n").getBytes();
        String header = String.format(HEADER_TEMPLATE, bundleName, bundleName, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        byte[] headerBytes = header.getBytes();
        byte[] file = Files.readAllBytes(Path.of(bundle + File.separator + buildName + File.separator + bundleName));
        byte[] trailer = (NEW_LINE + boundary + "--\r\n").getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(boundaryBytes);
            outputStream.write(headerBytes);
            outputStream.write(file);
            outputStream.write(trailer);
        } catch (IOException e) {
            LOGGER.error("Exception during writing body bytes to output stream: {}", e.getMessage());
        }

        byte[] body = outputStream.toByteArray();
        outputStream.close();
        return body;
    }

    public void installApp(Path bundle) throws URISyntaxException, IOException {
        String testRemovedFromBuildName = getReleaseName().replace(TO_REMOVE_TEST, "");
        String msixAppName = testRemovedFromBuildName + MSIX_EXTENSION;
        String boundary = getEpoch();
        byte[] body = setByteOutStream(msixAppName, bundle, boundary);
        URI uri = new URI(String.format(DEVICE_PORTAL_URL_APP, DEVICE_PORTAL_HOST, DEVICE_PORTAL_PORT, msixAppName));
        ResponseEntity<String> response = setUpApiCall(body, uri, boundary);
        LOGGER.info("App installation response is: {}", response);
    }

    public void installAppCert(Path bundle) throws URISyntaxException, IOException {
        String testRemovedFromBuildName = getReleaseName().replace(TO_REMOVE_TEST, "");
        String cerAppName = testRemovedFromBuildName + CER_EXTENSION;
        String boundary = getEpoch();
        byte[] body = setByteOutStream(cerAppName, bundle, boundary);
        URI uri = new URI(String.format(DEVICE_PORTAL_URL, DEVICE_PORTAL_HOST, DEVICE_PORTAL_PORT) + DEVICE_PORTAL_CERT);
        ResponseEntity<String> response = setUpApiCall(body, uri, boundary);
        LOGGER.info("App Certificate installation response is: {}", response);
    }

    private String getEpoch() {
        return "------------" + Instant.now().getEpochSecond();
    }

    private ResponseEntity<String> setUpApiCall(byte[] body, URI uri, String boundary) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=" + boundary);
        headers.set(X_CSRF_TOKEN, csrfToken);
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(body, headers);
        LOGGER.info(SESSION_REQUEST, requestEntity);
        return restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);
    }
}