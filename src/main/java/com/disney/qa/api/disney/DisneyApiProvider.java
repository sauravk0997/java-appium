package com.disney.qa.api.disney;

import com.disney.exceptions.ApiDetectiveUnblockException;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.util.disney.DisneyGlobalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidCriteriaException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.IDriverPool;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.SkipException;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisneyApiProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected final RestTemplate restTemplate = RestTemplateBuilder
            .newInstance()
            .withSpecificJsonMessageConverter()
            .withUtf8EncodingMessageConverter()
            .build();

    private final Configuration jsonPathJsonConfig = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider()).options(Option.DEFAULT_PATH_LEAF_TO_NULL)
            .build();

    private final DisneyApiCommon disneyApiCommon = new DisneyApiCommon();

    private static final boolean IS_TEST_USER = true;
    private static final String DELIMITER = "/";
    private static final String BAMTECH_PLATFORM_NAME = "web";
    private static final String BAMTECH_VERSION_NUMBER = "9";
    private static final String BEARER = "bearer ";
    private static final String TEST_ACCOUNT_PREFIX = "testguid+";
    private static final String TEST_ACCOUNT_SUFFIX = "@gsuite.disneyplustesting.com";
    private static final String EMAIL_FIELD = "email";
    private static final String PASS_FIELD = "password";
    private static final String ID_TOKEN_FIELD = "id_token";
    private static final String ATTRIBUTE_FIELD = "attributes";
    private static final String IS_TEST = "isTest";
    private static final String METADATA_FIELD = "metadata";
    private static final String SESSION_REQUEST = "Session - Request: {}";
    private static final String SESSION_RESPONSE = "Session - Response: {}";
    private static final String REFERER = "Referer";
    private static final String REFERER_KEY = "TEST-7F1JdjhfVzhr90Tq6AGC3p2v";

    private static final String API_DETECTIVE = String.format("%s/metadata", DisneyParameters.getApiDetective());
    private static final String EDGE_ACCOUNTS_GRANT = String.format("%s/accounts/grant", DisneyParameters.getEdgeHost());
    private static final String EDGE_DEVICES = String.format("%s/devices", DisneyParameters.getEdgeHost());
    private static final String EDGE_IDP_LOGIN = String.format("%s/idp/login", DisneyParameters.getEdgeHost());
    private static final String EDGE_TOKEN = String.format("%s/token", DisneyParameters.getEdgeHost());
    private static final String DICTIONARIES_QUERY = "/Dictionaries";
    private static final String REMOTE_APP_CONFIG = "/1.0/remote-app-config";

    private static final String CONTENT_BAMGRID_HOST = DisneyParameters.getContentBamgridHost();
    private static final String COLLECTION_PATH = String.format("%s%s", DisneyContentParameters.getServiceContentFork(), DisneyContentParameters.getStandardCollectionPath());
    private static final String FULL_CONTENT_SET_ENDPOINT = String.format("%s%s", DisneyContentParameters.getServiceContentFork(), DisneyContentParameters.getRegionalCuratedSetPath());
    private static final List<String> overrideEnvironments = Collections.unmodifiableList(
            Arrays.asList("PROD", "BETA", "PREVIEW", "EDITORIAL"));

    //Search API parameters
    private static final String VARIABLES = "variables";
    private static final String PREFERRED_LANGUAGE = "preferredLanguage";
    private static final String PLATFORM = "platform";

    //Error String
    private static final String API_ERROR = "Request failed with the following exception: {}";

    private String overridenPartner = "default";

    private ParseContext jsonContext() {
        return JsonPath.using(jsonPathJsonConfig);
    }

    /**
     * Retrieves the correct partner for the api calls
     *
     * @return - partner as a string.
     */
    public String getPartner() {
        String partner = overridenPartner;
        if (partner.equalsIgnoreCase("default")) {
            partner = DisneyGlobalUtils.getProject();
        }
        if (partner.equalsIgnoreCase("STA") || partner.equalsIgnoreCase("star") ) {
            partner = "star";
        } else if (partner.equalsIgnoreCase("ESP") || partner.equalsIgnoreCase("espn")) {
            partner = "espn";
        } else {
            partner = "disney";
        }

        return partner;
    }

    /**
     * Overrides the correct partner for the api calls
     */
    public void setPartner(String partner) {
        overridenPartner = partner.toLowerCase();
    }

    /**
     * Retrieves the Client API Key that can be used for the initial device grant call.
     *
     * @return - brings back the api key necessary to run the initial /device call.
     * @throws URISyntaxException
     * @throws IOException
     */
    private String retrieveClientApiKey() throws URISyntaxException, IOException {
        String partnerApiKey = String.format("%s/partners/%s/platforms/microsoft/api-keys/1.0.0", DisneyParameters.getApiHost(), getPartner());

        URI uri = new URI(partnerApiKey);

        LOGGER.debug("Retrieve Client API Key URL Being Passed: {}", uri);

        ArrayNode apiKeyNode = jsonContext().parse(uri.toURL().openStream()).read("$[*].client_api_key");
        return apiKeyNode.get(0).asText();
    }

    /**
     * Creates an initial device grant.
     *
     * @return - brings back the jwt token that can be used in later calls.
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    private String retrieveDeviceGrant() throws IOException, URISyntaxException, JSONException {

        URI uri = new URI(EDGE_DEVICES);

        HttpHeaders headers = new HttpHeaders();
        headers.add(DisneyHttpHeaders.BAMTECH_VERSION, BAMTECH_VERSION_NUMBER);
        headers.add(DisneyHttpHeaders.BAMTECH_PLATFORM, BAMTECH_PLATFORM_NAME);
        headers.add(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
        headers.add(DisneyHttpHeaders.BAMTECH_PARTNER, getPartner());
        checkEnvironmentForOverride(headers);
        headers.add(HttpHeaders.AUTHORIZATION, retrieveClientApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject deviceObject = new JSONObject();
        deviceObject.put("deviceFamily", "roku");
        deviceObject.put("applicationRuntime", "roku");
        deviceObject.put("deviceProfile", "roku");

        JSONObject attributesObject = new JSONObject();
        attributesObject.put("type", "test-automation");

        deviceObject.put(ATTRIBUTE_FIELD, attributesObject);

        JSONObject metaDataObject = new JSONObject();
        metaDataObject.put(IS_TEST, IS_TEST_USER);

        deviceObject.put(METADATA_FIELD, metaDataObject);

        RequestEntity<String> request = new RequestEntity<>(deviceObject.toString(), headers, HttpMethod.POST, uri);

        LOGGER.debug("Devices - Request: {}", request);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        LOGGER.debug("Devices - Response: {}", response);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());

        return node.findValue("assertion").asText();
    }

    private String retrieveFromSessionUsingDeviceToken(String assertion) throws IOException, URISyntaxException {

        return retrieveFromSession(assertion, DisneyTokenParameters.DEVICE_TOKEN_TYPE.getTokenType());
    }

    /**
     * Retrieves the grant necessary to be able to entitle an account.
     *
     * @param idToken takes a jwt token.
     * @return - brings back a jwt token that has had the grant applied to it.
     * @throws URISyntaxException
     * @throws IOException
     * @throws JSONException
     */
    private String retrieveDeviceGrant(String idToken) throws URISyntaxException, IOException, JSONException {

        URI uri = new URI(EDGE_ACCOUNTS_GRANT);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, BEARER + retrieveFromSessionUsingDeviceToken(retrieveDeviceGrant()));
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        httpHeaders.add(DisneyHttpHeaders.BAMTECH_PARTNER, getPartner());

        JSONObject grantObject = new JSONObject();
        grantObject.put(ID_TOKEN_FIELD, idToken);

        RequestEntity<String> request = new RequestEntity<>(grantObject.toString(), httpHeaders, HttpMethod.POST, uri);

        LOGGER.debug("Grant - Request: {}", request);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        LOGGER.debug("Grant - Response: {}", response);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());

        return node.findValue("assertion").asText();
    }

    private String retrieveFromSessionUsingAccountToken(String assertion) throws IOException, URISyntaxException {

        return retrieveFromSession(assertion, DisneyTokenParameters.ACCOUNT_TOKEN_TYPE.getTokenType());
    }

    /**
     * unblocks Jenkins nightly servers
     */
    public void unblockJenkinsServers(String countryIp) {
        Map<String, Boolean> result = new HashMap<>();
        ResponseEntity<String> responseEntity = null;
        RequestEntity<String> request = null;
        try {
            if (!countryIp.isEmpty()) {
                DisneyParameters.elasticIpAddresses.add(countryIp);
            }
        } catch(NullPointerException e) {
            LOGGER.info("No proxy value returned for {}, skipping Jenkins Server unblock", R.CONFIG.get("locale"));
        }
        for (String item : DisneyParameters.elasticIpAddresses) {
            try {
                URI uri = new URI(API_DETECTIVE + DELIMITER + item);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                checkEnvironmentForOverride(headers);
                checkEnvironmentForLocationOverride(headers);
                request = new RequestEntity<>(headers, HttpMethod.DELETE, uri);
                LOGGER.info(SESSION_REQUEST, request);
                responseEntity = restTemplate.exchange(request, String.class);
                LOGGER.info(SESSION_RESPONSE, responseEntity);
                result.put(item, responseEntity.getStatusCode().is2xxSuccessful());
            } catch (HttpClientErrorException e1) {
                LOGGER.debug("IP doesn't exist on service side, ignoring..");
            } catch (Exception e2) {
                result.forEach((k, v) -> LOGGER.info(String.format("%s ip had the following 2xxsuccess: %s", k, v)));
                throw new ApiDetectiveUnblockException("Failed to delete IPs from api-detective ");
            }
        }
    }

    /**
     * Retrieves Session Information for the particular account or device.
     *
     * @param assertion - takes in a jwt token.
     * @param tokenType - takes in a token type of Account or Device.
     * @return - returns the access_token for session.
     * @throws IOException
     * @throws URISyntaxException
     */
    private String retrieveFromSession(String assertion, String tokenType) throws IOException, URISyntaxException {
        URI uri = new URI(EDGE_TOKEN);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add(DisneyHttpHeaders.BAMTECH_PLATFORM, BAMTECH_PLATFORM_NAME);
        headers.add(DisneyHttpHeaders.BAMTECH_VERSION, BAMTECH_VERSION_NUMBER);
        headers.add(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
        headers.add(HttpHeaders.AUTHORIZATION, BEARER + retrieveClientApiKey());
        checkEnvironmentForOverride(headers);
        checkEnvironmentForLocationOverride(headers);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
        formData.add("subject_token", assertion);
        formData.add("subject_token_type", tokenType);
        formData.add(PLATFORM, BAMTECH_PLATFORM_NAME);
        formData.add("latitude", "52.3680");
        formData.add("longitude", "4.9036");
        formData.add("setCookie", "false");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        LOGGER.debug("Token - URI: {}", uri);
        LOGGER.debug("Token - Request (Headers): {}", request.getHeaders());
        LOGGER.debug("Token - Request (Body): {}", request.getBody());

        ResponseEntity<String> response = restTemplate.postForEntity(
                uri, request, String.class);

        LOGGER.debug("Token - Response: {}", response.getBody());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());

        return node.findValue("access_token").asText();
    }

    /**
     * Allows a user to login with an account and get a valid id token in response
     *
     * @param emailAddress takes an email address that will be logged in.
     * @param pass         takes a valid password for the email address.
     * @param token        takes in the appropriate authorization token from the session.
     * @return brings back a valid token to use for 5 minutes.
     * @throws URISyntaxException
     * @throws JSONException
     */
    private String idpLogin(String emailAddress, String pass, String token, DisneyAccount account) throws IOException, URISyntaxException, JSONException {

        URI uri = new URI(EDGE_IDP_LOGIN);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headers.add(HttpHeaders.AUTHORIZATION, BEARER + token);
        headers.add(DisneyHttpHeaders.BAMTECH_VERSION, BAMTECH_VERSION_NUMBER);
        headers.add(DisneyHttpHeaders.BAMTECH_PLATFORM, BAMTECH_PLATFORM_NAME);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject emailObject = new JSONObject();
        emailObject.put(EMAIL_FIELD, emailAddress);
        emailObject.put(PASS_FIELD, pass);

        RequestEntity<String> request = new RequestEntity<>(emailObject.toString(), headers, HttpMethod.POST, uri);

        LOGGER.debug("What is Request: {}", request);
        LOGGER.debug("Check Request: {}", request.getBody());
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        LOGGER.debug("Check Response: {}", response.getBody());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());

        LOGGER.debug("Done with Login...");

        String tokenRetrieved = node.findValue(ID_TOKEN_FIELD).asText();

        retrieveIdentityPointFromToken(account, tokenRetrieved);

        return tokenRetrieved;
    }

    private void retrieveIdentityPointFromToken(DisneyAccount account, String authToken) throws IOException {

        String[] authTokenBody = authToken.split("\\.");
        String base64EncodedBody = authTokenBody[1];

        Base64 base64Url = new Base64(true);

        String body = new String(base64Url.decode(base64EncodedBody));
        LOGGER.debug("Auth Token Body: {}", body);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(body);

        account.setIdentityPointId(node.findValue("sub").asText());

        LOGGER.debug("Checking IdentityPoint: {}", account.getIdentityPointId());
    }

    private void logHttpError(HttpClientErrorException hex) {
        LOGGER.error("HTTP Status Code Returned: {}", hex.getStatusCode());
        LOGGER.error("HTTP Response Body with Error: {}", hex.getResponseBodyAsString());
        LOGGER.error("HTTP Headers: {}", hex.getResponseHeaders());
    }

    private String generateUniqueUserId() {
        String uniqueId = "";
        try {
            uniqueId = disneyApiCommon.getCurrentSystemNanoTime();
        } catch (Exception ex) {
            LOGGER.error("Error Generating Unique UserId: ", ex);
        }
        return TEST_ACCOUNT_PREFIX + uniqueId;
    }

    public String getUniqueUserEmail() {
        return generateUniqueUserId() + TEST_ACCOUNT_SUFFIX;
    }

    private void checkEnvironmentForOverride(HttpHeaders headers) {
        String partner = getPartner();
        for (String environment : overrideEnvironments) {
            if (R.CONFIG.get("env").toUpperCase().contains(environment)) {
                if (partner.equalsIgnoreCase("disney")) {
                    headers.add(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
                } else if (partner.equalsIgnoreCase("espn")) {
                    headers.add(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
                } else if (partner.equalsIgnoreCase("star")) {
                    headers.add(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY_STAR);
                }
                break;
            }
        }
    }

    private void checkEnvironmentForLocationOverride(HttpHeaders headers) {
        String partner = getPartner();
        for (String environment : overrideEnvironments) {
            if (R.CONFIG.get("env").toUpperCase().contains(environment)) {
                if (partner.equalsIgnoreCase("disney")) {
                    headers.add(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
                } else if (partner.equalsIgnoreCase("espn")) {
                    headers.add(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
                } else if (partner.equalsIgnoreCase("star")) {
                    headers.add(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
                }
                break;
            }
        }
    }

    //Attempts to get driver platform (iOS/Android). If null, default to web.
    public String getPlatform() {
        try {
            var deviceType = IDriverPool.currentDevice.get().getDeviceType();

            switch (deviceType) {
                case APPLE_TV:
                    return DeviceType.Type.APPLE_TV.getType().replace('_', '-');
                default:
                    return StringUtils.substringBefore(IDriverPool.currentDevice.get().getDeviceType().getType(), "_");
            }
        } catch (NullPointerException npe) {
            return "web";
        }
    }

    //Endpoint contains all searchApi related content not stored in collectionBySlug or setById nodes.
    private String getCoreApiHost() {
        return DisneyParameters.getSearchApiHost(getPlatform()) + "core/";
    }

    public JsonNode getDictionaryBody(String language, String resourceKey) {
        return getDictionaryBody(language, resourceKey, false);
    }

    public JsonNode getDictionaryBody(String language, String resourceKey, boolean unpinned) {
        JsonNode dictionaryBody = null;
        try {
            URI uri = new URI(getCoreApiHost() + DICTIONARIES_QUERY);
            HttpHeaders headers = new HttpHeaders();
            String token = retrieveFromSession(retrieveDeviceGrant(), DisneyTokenParameters.DEVICE_TOKEN_TYPE.getTokenType());
            headers.add(HttpHeaders.AUTHORIZATION, BEARER + token);
            String platform = getPlatform();
            String version;
            if(unpinned) {
                version = "0.0";
            } else {
                JsonNode config = getAppConfig(platform);
                version = new DisneyContentApiChecker().getDictionaryVersion(config, resourceKey, platform);
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject resource = new JSONObject();

            resource.put("resourceKey", resourceKey);
            if (!version.isEmpty()) {
                resource.put("version", version);
            }

            jsonObject.put(PREFERRED_LANGUAGE, new JSONArray().put(language));
            jsonObject.put("dictionary", new JSONArray().put(resource));
            jsonObject.put(PLATFORM, platform);

            LOGGER.debug("OBJECT: \n{}", jsonObject);

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString()).queryParam(VARIABLES, jsonObject).build();

            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, builder.toUri());
            dictionaryBody = restTemplate.exchange(request, JsonNode.class).getBody();
        } catch (HttpClientErrorException hex) {
            logHttpError(hex);
        } catch (Exception e) {
            throw new SkipException(API_ERROR, e);
        }
        return dictionaryBody;
    }

    public JsonNode getFullDictionaryBody(String language) {
        JsonNode dictionaryBody = null;
        try {
            URI uri = new URI(getCoreApiHost() + DICTIONARIES_QUERY);
            HttpHeaders headers = new HttpHeaders();
            String token = retrieveFromSession(retrieveDeviceGrant(), DisneyTokenParameters.DEVICE_TOKEN_TYPE.getTokenType());
            headers.add(HttpHeaders.AUTHORIZATION, BEARER + token);
            String platform = getPlatform();
            JsonNode config = getAppConfig(platform);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PREFERRED_LANGUAGE, new JSONArray().put(language));
            JSONArray jsonArray = new JSONArray();

            for (String key : DisneyParameters.resourceKeys) {
                JSONObject resource = new JSONObject();
                resource.put("resourceKey", key);
                String version = new DisneyContentApiChecker().getDictionaryVersion(config, key, platform);
                if (!version.isEmpty()) {
                    resource.put("version", version);
                }
                jsonArray.put(resource);
            }
            jsonObject.put("dictionary", jsonArray);
            jsonObject.put(PLATFORM, platform);

            LOGGER.debug("OBJECT: \n{}", jsonObject);
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString()).queryParam(VARIABLES, jsonObject).build();
            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, builder.toUri());
            dictionaryBody = restTemplate.exchange(request, JsonNode.class).getBody();
            LOGGER.debug("DICTIONARY URL: {}", uri.toURL());
        } catch (HttpClientErrorException hex) {
            logHttpError(hex);
        } catch (Exception e) {
            throw new SkipException(API_ERROR + e);
        }
        return dictionaryBody;
    }

    /**
     * Returns the collection body for a given slug (home or a brand)
     *
     * @param account      - Account needed for auth token
     * @param language     - Language to return the data in
     * @param locale       - Locale the data is being returned for
     * @param contentClass - literally "home" or "brand" depending on what is needed
     * @param slug         - "home" or the brand being parsed
     * @param isHome       - Boolen value to determine if we need to pass verion 5.0 or 5.1 (source under investogation)
     * @return
     */
    //TODO: Find the source for 5.0 vs. 5.1 and parameterize COLLECTION_PATH to use the value
    public JsonNode getCollectionBody(DisneyAccount account, String language, String locale, String contentClass, String slug, boolean isHome) {
        try {
            HttpHeaders headers = new HttpHeaders();
            String formattedCollection = isHome ? String.format(COLLECTION_PATH, "5.1") : String.format(COLLECTION_PATH, "5.0");
            String formattedSlug = String.format(DisneyContentParameters.getRegionSlugPath(), locale, language, contentClass, slug);
            URI uri = new URI(CONTENT_BAMGRID_HOST + formattedCollection + formattedSlug);

            headers.add(HttpHeaders.AUTHORIZATION, BEARER + getAccToken(account));
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString()).build();
            RequestEntity<JsonNode> request = new RequestEntity<>(headers, HttpMethod.GET, builder.toUri());
            return restTemplate.exchange(request, JsonNode.class).getBody();
        } catch (Exception e) {
            Assert.fail(API_ERROR + e);
        }
        return null;
    }

    public JsonNode getCuratedSetBySetId(DisneyAccount account, String language, String locale, String setId) {
        int tries = 0;
        while (tries < 3) {
            try {
                HttpHeaders headers = new HttpHeaders();
                String curatedSetPath = String.format(FULL_CONTENT_SET_ENDPOINT, locale, language, setId);
                URI uri = new URI(CONTENT_BAMGRID_HOST + curatedSetPath);

                headers.add(HttpHeaders.AUTHORIZATION, BEARER + getAccToken(account));
                UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString()).build();
                RequestEntity<JsonNode> request = new RequestEntity<>(headers, HttpMethod.GET, builder.toUri());
                return restTemplate.exchange(request, JsonNode.class).getBody();
            } catch (Exception e) {
                LOGGER.error("API Error {}", API_ERROR + e);
                tries++;
            }
        }
        Assert.fail("Could not retrieve the required data.");
        return null;
    }

    public String getOneIdSiteConfigurations(String language) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("langPref", StringUtils.substringBefore(language, "_"));
        map.add("countryCode", StringUtils.substringAfter(language, "_"));
        try {
            URI uri = new URI(String.format("%s/configuration/site", DisneyParameters.getOneIdProductClient()));
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(uri.toURL().toString())
                    .queryParams(map)
                    .build();
            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
            LOGGER.info("Request Uri: {}", builder.toUri());
            return restTemplate.exchange(builder.toUri(), HttpMethod.GET, request, String.class).getBody();
        } catch (Exception e) {
            throw new SkipException("Unable to retrieve Site configurations, failed with error: {}", e);
        }
    }

    public String getOneIdDocument(String documentCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        try {
            URI uri = new URI(String.format("%s/document/%s", DisneyParameters.getOneIdProductClient(), documentCode));
            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
            return restTemplate.exchange(request, String.class).getBody();
        } catch (Exception e) {
            throw new SkipException("Unable to retrieve Site configurations, failed with error: " + e);
        }
    }

    protected JsonNode getAppConfig(String platform) {
        JsonNode response = null;
        String version;
        String apiPath = null;
        String env = DisneyParameters.getEnv().toLowerCase();
        HttpHeaders headers = new HttpHeaders();
        String path = "config.json";

        /*
         * The env value is what is populated in Jenkins at runtime. Android does not generate builds
         * tagged as Prod, QA, or Beta, so this field is invalid. Instead, Android references the
         * capabilities.custom_env field parameter. iOS snips Prod or QA from its environment. Web
         * simply passes PROD or QA.
         *
         * Android-TV does not have any Jenkins jobs to see what will be used in env at this time,
         * and will need to be revisited once those jobs are created.
         */

        switch (platform) {
            case "ios":
                version = new MobileUtilsExtended().getInstalledAppVersion();
                platform = "/" + platform + "/";
                apiPath = DisneyParameters.getAppConfigsHost() + env + platform + version + File.separator + path;
                break;
            case "apple-tv":
                version = new MobileUtilsExtended().getInstalledAppVersion();
                platform = "/tvos/";
                apiPath = DisneyParameters.getAppConfigsHost() + env + platform + version + File.separator + path;
                break;
            default:
                throw new InvalidCriteriaException("Platform " + platform + " is not supported.");
        }

        try {
            LOGGER.debug("Pulling config from: {}", apiPath);
            URI uri = new URI(apiPath);
            RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, uri);
            response = restTemplate.exchange(request, JsonNode.class).getBody();
        } catch (Exception e) {
            LOGGER.error("Caught exception: {}", e.getMessage(), e);
        }
        return response;
    }

    private String getAccToken(DisneyAccount account) {
        try {
            String token = retrieveFromSession(retrieveDeviceGrant(), DisneyTokenParameters.DEVICE_TOKEN_TYPE.getTokenType());
            return retrieveFromSessionUsingAccountToken(retrieveDeviceGrant(idpLogin(account.getEmail(), account.getUserPass(), token, account)));
        } catch (HttpClientErrorException hex) {
            logHttpError(hex);
            throw new SkipException(API_ERROR + hex);
        } catch (Exception exception) {
            throw new SkipException(API_ERROR + exception);
        }
    }
}
