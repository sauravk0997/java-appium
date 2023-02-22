package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.google.common.collect.Lists;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO [major] ask how to deal with this test - too much changes: https://www.pivotaltracker.com/projects/1427108/stories/112527621
 */
public class NhlTfsGameTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String GAME_ID = "2015020739";

    /**
     * TODO move credentials to configuration
     */
    protected RestTemplate restTemplateBaseAuth =
            RestTemplateBuilder.newInstance().withBasicAuthentication("jeremy.braff@mlb.com", "1234").build();

    @DataProvider(name = "gameProvider")
    public Object[][] gameProvider() {
        return new Object[][]{
                {"feed/live", "nhl/api/games/game_feed_live_id_2015020739.json", null},
                {"linescore", "nhl/api/games/game_linescore_id_2015020739.json", null},
                {"boxscore", "nhl/api/games/game_boxscore_id_2015020739.json", null},
                {"playByPlay", "nhl/api/games/game_play_by_play_id_2015020739.json", null},
                {"feed/live", "nhl/api/games/game_feed_live_id_2015020739_fr.json", new HashMap<String, String>() {{
                    put("language", "fr");
                }}},
                {"linescore", "nhl/api/games/game_linescore_id_2015020739_fr.json", new HashMap<String, String>() {{
                    put("language", "fr");
                }}},
                {"boxscore", "nhl/api/games/game_boxscore_id_2015020739_fr.json", new HashMap<String, String>() {{
                    put("language", "fr");
                }}},
                {"playByPlay", "nhl/api/games/game_play_by_play_id_2015020739_fr.json", new HashMap<String, String>() {{
                    put("language", "fr");
                }}},
        };
    }

    @BeforeClass(dependsOnMethods = "setupNhlContentManager")
    public void updateFormatOfGameEndpoint() {
        URI uri = UriComponentsBuilder.fromHttpUrl(NhlParameters.getNhlStatsApiHost()).path("api/v1/admin/tfs").build().toUri();

        MultiValueMap postData = new LinkedMultiValueMap();
        postData.add("gamePk", GAME_ID);

        RequestEntity<MultiValueMap> requestEntity = new RequestEntity(postData, null, HttpMethod.POST, uri);

        LOGGER.info(String.format("Updating the format of the game endpoint, sending request: %s", requestEntity));

        String response = restTemplateBaseAuth.exchange(requestEntity, String.class).getBody();

        LOGGER.info(String.format("Response: %s", response));

        Assert.assertTrue(response.contains("Updated") && response.contains(GAME_ID));
    }


    // TODO refactor to use 'getGame' method
    @Test(dataProvider = "gameProvider", enabled = false)
    public void verifyGameResponse(String urlPath, String responseDataFile, Map<String, String> urlParameters) throws IOException, JSONException {
        // TODO investigate why 'pathSegment' works properly, and 'path' - not inserting '/' before segment
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(
                NhlParameters.getNhlStatsApiHost()).pathSegment("api/v1/game", GAME_ID, urlPath);

        if (urlParameters != null) {
            MultiValueMap parameters = new LinkedMultiValueMap();

            for (Map.Entry<String, String> entry : urlParameters.entrySet()) {
                parameters.add(entry.getKey(), entry.getValue());
            }

            uriComponentsBuilder.queryParams(parameters);
        }

        URI uri = uriComponentsBuilder.build().toUri();

        LOGGER.info(String.format("Requesting URL: %s", uri));

        JSONComparator jsonCustomIgnoreComparator =
                new JsonIgnoreComparator(JSONCompareMode.LENIENT, Lists.newArrayList("timeStamp", "currentAge"));

        String actualResponse = restTemplate.getForEntity(uri, String.class).getBody();
        String expectedResponse = fileUtils.getResourceFileAsString(responseDataFile);

        JSONAssert.assertEquals(expectedResponse, actualResponse, jsonCustomIgnoreComparator);
    }
}
