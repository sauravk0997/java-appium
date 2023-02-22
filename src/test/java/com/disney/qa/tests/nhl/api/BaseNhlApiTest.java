package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlContentService;
import com.disney.qa.api.nhl.NhlContentServiceBuilder;
import com.disney.qa.api.nhl.NhlDictionaryService;
import com.disney.qa.api.nhl.NhlDictionaryServiceBuilder;
import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.api.nhl.statsapi.NhlStatsApiDictionaryProvider;
import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.disney.qa.common.validator.JsonValidator;
import com.disney.qa.tests.BaseAPITest;
import com.disney.util.FileUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@SuppressWarnings("squid:S2187")
public class BaseNhlApiTest extends BaseAPITest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    protected RestTemplate restTemplate;

    protected NhlContentService nhlStatsApiContentService;

    protected NhlDictionaryService nhlStatsApiDictionaryService;

    protected NhlContentService mlbNhlStatsApiContentService;

    protected NhlContentService nhlFeedContentService;

    protected Configuration jsonPathJacksonConfiguration = Configuration.builder().
            mappingProvider(new JacksonMappingProvider()).
            jsonProvider(new JacksonJsonNodeJsonProvider()).
            build();

    protected FileUtils fileUtils = new FileUtils();
    
    protected JsonValidator jsonValidator = new JsonValidator();

    protected boolean assertDictionaryTest;

    protected String dictionaryError = "Error with dictionary key '%s': ";

    /**
     * Setting application version to automation report
     */
    @BeforeSuite
    public void setApplicationVersion() {
        String versionResponse = getNhlApiRevisionString(buildRestTemplateWithBasicAuthentication());

        if (null != versionResponse) {
            R.CONFIG.put("app_version", String.format("Version %s, Revision %s",
                    getApplicationTag(versionResponse), getApplicationRevision(versionResponse)));
        }
    }

    @BeforeClass
    public void setupNhlContentManager() {
        restTemplate = RestTemplateBuilder.newInstance().withDisabledSslChecking().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();

        nhlStatsApiContentService = NhlContentServiceBuilder.newInstance().withAllParametersForStatsApi(restTemplate).build();
        nhlFeedContentService = NhlContentServiceBuilder.newInstance().withAllParametersForFeed(restTemplate).build();
        nhlStatsApiDictionaryService = NhlDictionaryServiceBuilder.newInstance().withAllParametersForStatsApi(restTemplate).build();
        mlbNhlStatsApiContentService = NhlContentServiceBuilder.newInstance().withAllParametersForStatsApiHostMlb(restTemplate).build();
    }

    protected RestTemplate buildRestTemplateWithBasicAuthentication() {
        return RestTemplateBuilder.newInstance().withUtf8EncodingMessageConverter().withBasicAuthentication(
                NhlParameters.NHL_BASIC_AUTHENTICATION_LOGIN.getValue(),
                NhlParameters.NHL_BASIC_AUTHENTICATION_PASSWORD.getDecryptedValue()).build();
    }

    protected String getNhlApiRevisionString(RestTemplate restTemplate) {
        String revisionString = null;
        try {
            revisionString = restTemplate.getForEntity(
                    UriComponentsBuilder.fromHttpUrl(NhlParameters.getNhlStatsApiHost()).path("revision.html").build().toUri(),
                    String.class).getBody();
        } catch (Exception e) {
            LOGGER.warn(String.format("Can't get application version: %s", e.getMessage()), e);
        }
        return revisionString;
    }

    protected String getApplicationTag(String versionResponse) {
        Document document = Jsoup.parse(versionResponse);
        return document.select("a").first().text();
    }

    protected String getApplicationRevision(String versionResponse) {
        Document document = Jsoup.parse(versionResponse);
        return document.select("a").get(1).text();
    }

    /**
     * TODO 'jerseyNumber' node and 'PPG PAINTS Arena' venue are added as temporary solution for outdated QA DB
     */
    protected void verifyTeamsResponseWithJsonAssert(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        JSONComparator jsonCustomIgnoreComparator =
                new JsonIgnoreComparator(JSONCompareMode.LENIENT,
                        Lists.newArrayList("jerseyNumber", "copyright"),
                        Lists.newArrayList("PPG PAINTS Arena"));

        String teamsSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String actualTeams = nhlStatsApiContentService.getTeams(parameters, String.class, pathSegment);

        JSONAssert.assertEquals(teamsSnapshot, actualTeams, jsonCustomIgnoreComparator);
    }

    public void scanJson(SoftAssert softAssert, JsonNode rawNode, String id, String slug) {

        ArrayNode hydrateTeamResult = JsonPath.using(jsonPathJacksonConfiguration).parse(rawNode).read("$.teams[*]");

        for (JsonNode node : hydrateTeamResult) {
            if (id.equalsIgnoreCase(node.findPath("id").asText())) {
                LOGGER.info("Found JSON Node, checking SlugName.");
                LOGGER.info(node.toString());
                softAssert.assertTrue(
                        checkJsonNodeField(node.findPath("slug").asText(), slug),
                        String.format("Expected SlugName (%s), found (%s)", slug, node.findPath("slug)")));
            }
        }
    }

    public boolean checkJsonNodeField(String jsonField, String field) {

        if (field.equalsIgnoreCase(jsonField)) {
            return true;
        }
        return false;
    }

    protected void checkIfCyrillicExists(SoftAssert softAssert, String stringLine) {

        softAssert.assertTrue(Pattern.matches(".*\\p{InCyrillic}.*", stringLine),
                String.format("Expected Cyrillic Characters to Exist for String (%s)", stringLine));
    }

    protected ParseContext jsonContext() {
        return JsonPath.using(jsonPathJsonConfig);
    }

    private Configuration jsonPathJsonConfig = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider()).options(Option.DEFAULT_PATH_LEAF_TO_NULL)
            .build();

    protected String safeStatus(RequestEntity<String> checkUrl, Class<?> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpStatus statusCode = restTemplate.exchange(checkUrl, responseType).getStatusCode();
            return statusCode.toString();
        } catch (HttpClientErrorException ex) {
            LOGGER.debug("Status Code Error", ex);
            return ex.getStatusCode().toString();
        } catch (Exception ex1) {
            LOGGER.debug("Status Code Error #2", ex1);
            return ex1.getMessage();
        }
    }

    protected String getHttpStatus(String link) {
        String url = NhlParameters.getNhlStatsApiHost() + link;
        LOGGER.info("url: " + url);
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).build();
        return safeStatus(new RequestEntity<>(HttpMethod.GET, uriComponents.toUri()), String.class);
    }

    protected void verifyNodeIsPresent(SoftAssert softAssert, String nodeName, String rawResponse) {
        softAssert.assertTrue(rawResponse.contains(nodeName), String.format("Expected node '%s' is missing", nodeName));
    }

    protected void skipTest(String missingKey){
        skipExecution(String.format("Skipping test. Acceptance file contains no matches to Key Value '%s'", missingKey));
    }

    protected String expectedDictionaryResponse(NhlStatsApiDictionaryProvider.DictionaryLanguage dictionaryLanguage) throws IOException {
        return fileUtils.getResourceFileAsString(String.format("nhl/api/dictionaries/full_dictionary_%s.json", dictionaryLanguage.getLanguage()));
    }

    protected String actualDictionaryResponse(NhlStatsApiDictionaryProvider.DictionaryLanguage dictionaryLanguage) {
        return nhlStatsApiDictionaryService.getDictionaryResponse(dictionaryLanguage);
    }

    //Method returns all valid keys in passed Json file based on desired prefix value (ex. series.status)
    //To be used against the provided acceptance files located in src/test/resources/nhl/api/dictionaries
    protected List<String> getJsonKeys(String jsonFile, String keyToTest) throws JSONException {
        Iterator keys = new JSONObject(jsonFile).keys();
        List<String> keyValues = new ArrayList<>();

        while (keys.hasNext()){
            String currentKey = (String) keys.next();
            if(currentKey.contains(keyToTest)){
                keyValues.add(currentKey);
            }
        }

        Collections.sort(keyValues);
        return keyValues;
    }

    //Will assert a match against all dictionary keys in the provided list with no filter applied
    public SoftAssert assertKeys(NhlStatsApiDictionaryProvider.DictionaryLanguage language, List<String> keys) throws IOException {
        return assertKeys(language, keys, Lists.newArrayList(""));
    }

    //Will assert a match against all dictionary keys in the provided list with a group of filters applied
    public SoftAssert assertKeys(NhlStatsApiDictionaryProvider.DictionaryLanguage language, List<String> keys, List<String> filters) throws IOException {
        SoftAssert sa = new SoftAssert();
        for(String currentKey : keys){
            if(doesKeyMatchCriteria(currentKey, filters)){
                sa.assertEquals(nhlStatsApiDictionaryService.getJsonKeyValue(actualDictionaryResponse(language), currentKey),
                        nhlStatsApiDictionaryService.getJsonKeyValue(expectedDictionaryResponse(language), currentKey),
                        String.format(dictionaryError, currentKey));
            }
        }
        return sa;
    }

    /** Sets dictionary assertion boolean to true upon any match being found.
     * Returns a local boolean for keys where there are multiple entries that
     * are not in scope for a specific test, but the list of keys still contains
     * at least 1 valid value to test.
     *
     * ex. period1Abbrev = true, period1Full = false;
     */
    private boolean doesKeyMatchCriteria(String key, List<String> filters){
        boolean valid = false;
        for(String criterion : filters){
            if(key.contains(criterion)){
                assertDictionaryTest = true;
                valid = true;
            }
        }
        return valid;
    }

    //Method is for asserting Dictionary tests and applying a dynamic message during a skip exception
    protected void performDictionaryAssertion(SoftAssert sa, String keyCheck){
        if(assertDictionaryTest){
            sa.assertAll();
        } else {
            skipTest(keyCheck);
        }
    }
}
