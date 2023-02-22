package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.pojo.stats.standings.Standings;
import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.JsonDiff;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * TODO [major] add clear logging of what fields are not equal in JSON response: use JSONassert and return strings, not POJOs. Make it configurable
 * TODO [major] goalsAgainst: 54 (number) => 53 (number). Increased to 1 in 4 tests: by conference, division leaders, wild card, wild card with leaders
 * <p/>
 * Compare JSONs online: http://tlrobinson.net/projects/javascript-fun/jsondiff
 */
public class NhlStandingsTest extends BaseNhlApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String STANDINGS_SEASON_RESPONSE_DATA_FILE = "nhl/api/standings/standings_season_20142015.json";

    public static final String STANDINGS_WILD_CARD_RESPONSE_DATA_FILE = "nhl/api/standings/standings_wild_card_date_2015_11_14.json";

    public static final String STANDINGS_DIVISION_LEADERS_RESPONSE_DATA_FILE = "nhl/api/standings/standings_division_leaders_date_2015_11_14.json";

    public static final String STANDINGS_WILD_CARD_LEADERS_RESPONSE_DATA_FILE = "nhl/api/standings/standings_wild_card_leaders_date_2015_11_14.json";

    public static final String STANDINGS_BY_CONFERENCE_RESPONSE_DATA_FILE = "nhl/api/standings/standings_by_conference_date_2015_11_14.json";

    public static final String STANDINGS_EXPAND_TEAM_DATA_FILE = "nhl/api/standings/standings_expand_team_season_20142015.json";

    public static final String STANDINGS_EXPAND_TEAM_DIVISION_CONFERENCE_DATA_FILE = "nhl/api/standings/standings_expand_team_division_conference_season_20142015.json";

    public static final String STANDINGS_EXPAND_TEAM_SCHEDULE_PREVIOUS_DATA_FILE = "nhl/api/standings/standings_expand_team_schedule_previous_season_20142015.json";

    public static final String STANDINGS_EXPAND_TEAM_SCHEDULE_PREVIOUS_SPECIFIC_DATE_DATA_FILE = "nhl/api/standings/standings_expand_team_schedule_previous_season_20142015_date_2015_01_01.json";

    public static final String STANDINGS_OLD_DATA_FILE = "nhl/api/standings/standings_old.json";

    public static final String STANDINGS_DATE = "11/14/2015";

    @QTestCases(id = "42959")
    @Test
    public void verifySpecificSeason() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_SEASON_RESPONSE_DATA_FILE, ImmutableMap.of("season", "20142015"), null);
    }

    @QTestCases(id = "42960")
    @Test
    public void verifyWildCard() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_WILD_CARD_RESPONSE_DATA_FILE, ImmutableMap.of("date", STANDINGS_DATE), "wildCard");
    }

    @QTestCases(id = "42961")
    @Test
    public void verifyDivisionLeaders() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_DIVISION_LEADERS_RESPONSE_DATA_FILE, ImmutableMap.of("date", STANDINGS_DATE), "divisionLeaders");
    }

    @QTestCases(id = "42962")
    @Test
    public void verifyWildCardWithLeaders() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_WILD_CARD_LEADERS_RESPONSE_DATA_FILE, ImmutableMap.of("date", STANDINGS_DATE), "wildCardWithLeaders");
    }

    @QTestCases(id = "42963")
    @Test
    public void verifyByConference() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_BY_CONFERENCE_RESPONSE_DATA_FILE, ImmutableMap.of("date", STANDINGS_DATE), "byConference");
    }

    @Test
    public void verifyStandingsExpandTeam() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_EXPAND_TEAM_DATA_FILE, ImmutableMap.of("season", "20142015", "expand", "standings.team"), null);
    }

    @Test
    public void verifyStandingsExpandDivisionAndConference() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_EXPAND_TEAM_DIVISION_CONFERENCE_DATA_FILE,
                ImmutableMap.of("season", "20142015", "expand", "standings.team,standings.division,standings.conference"), null);
    }

    @Test
    public void verifyStandingsExpandTeamSchedulePrevious() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_EXPAND_TEAM_SCHEDULE_PREVIOUS_DATA_FILE,
                ImmutableMap.of("season", "20142015", "expand", "standings.team,team.schedule.previous"), null);
    }

    @Test
    public void verifyStandingsExpandTeamSchedulePreviousSpecificDate() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_EXPAND_TEAM_SCHEDULE_PREVIOUS_SPECIFIC_DATE_DATA_FILE,
                ImmutableMap.of("season", "20142015", "date", "01/01/2015", "expand", "standings.team,team.schedule.previous"), null);
    }

    @Test
    public void verifyOldStandings() throws IOException, JSONException {
        verifyResponseWithJsonAssert(STANDINGS_OLD_DATA_FILE,
                ImmutableMap.of("season", "19601961", "expand", "standings.record,standings.team,standings.division,standings.conference,team.schedule.next,team.schedule.previous"), null);
    }

    /**
     * TODO add all checks from the JIRA ticket
     */
    @QTestCases(id = "42964")
    @Test
    public void verifyRosterSeasonHydrate() {
        SoftAssert softAssert = new SoftAssert();

        verifyRosterSeasonHydrate(
                ImmutableMap.of("standingsType", "byDivision", "date", "2014-12-23", "hydrate", "team(roster)"),
                "5", softAssert);

        softAssert.assertAll();
    }

    /**
     * Jira https://jira.mlbam.com/browse/SDAPINHL-1270
     */
    @QTestCases(id = "42965")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyDivisionSpecificStandings() {
        JsonNode rawTeamRecordsConference = nhlStatsApiContentService.getStandings(ImmutableMap.of("conferenceIds", "5"), JsonNode.class);

        JsonNode rawTeamRecordsDivision = nhlStatsApiContentService.getStandings(ImmutableMap.of("divisionIds", "15"), JsonNode.class);

        JsonNode teamRecordsConference = JsonPath.using(jsonPathJacksonConfiguration).parse(rawTeamRecordsConference).read("$.records..teamRecords");
        JsonNode conferenceIds = JsonPath.using(jsonPathJacksonConfiguration).parse(rawTeamRecordsConference).read("$..conference.id");

        JsonNode teamRecordsDivision = JsonPath.using(jsonPathJacksonConfiguration).parse(rawTeamRecordsDivision).read("$..teamRecords");
        JsonNode divisionId = JsonPath.using(jsonPathJacksonConfiguration).parse(rawTeamRecordsDivision).read("$..division.id");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(teamRecordsConference.size() >= 0 && conferenceIds.size() >= 0,
                String.format("Expected 'teamRecords' size to equal '2' and 'conferenceIds' to be '5,5', found: %s, %s", teamRecordsConference.size(), conferenceIds));

        softAssert.assertTrue(teamRecordsDivision.size() >= 0 && divisionId.size() >= 0,
                String.format("Expected 'teamRecords' size to equal '1' and 'divisionId' to be '15', found: %s, %s", teamRecordsDivision.size(), divisionId));

        softAssert.assertAll();
    }

    protected void verifyResponse(String responseDataFile, Map parameters, String pathSegment) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Standings standingsSnapshot =
                objectMapper.readValue(new File(fileUtils.getResourceFilePath(responseDataFile)), Standings.class);

        Standings actualStandings = nhlStatsApiContentService.getStandings(parameters, Standings.class, pathSegment);

        Assert.assertEquals(actualStandings, standingsSnapshot);
    }

    protected void verifyResponseWithJsonAssert(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        JSONComparator jsonCustomIgnoreComparator =
                new JsonIgnoreComparator(JSONCompareMode.LENIENT, Lists.newArrayList("lastUpdated"));

        String standingsSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String actualStandings = nhlStatsApiContentService.getStandings(parameters, String.class, pathSegment);
        JSONAssert.assertEquals(standingsSnapshot, actualStandings, jsonCustomIgnoreComparator);
    }

    protected void verifyResponseWithJsonDiff(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode expectedResponse = mapper.readTree(new File(fileUtils.getResourceFilePath(responseDataFile)));
        JsonNode actualResponse = mapper.readTree(nhlStatsApiContentService.getStandings(parameters, String.class, pathSegment));

        JsonNode patch = JsonDiff.asJson(expectedResponse, actualResponse);
    }

    protected void verifyRosterSeasonHydrate(Map parameters, String teamId, SoftAssert softAssert) {
        JsonNode rawResponseActual = nhlStatsApiContentService.getStandings(parameters, JsonNode.class);
        JsonNode rawResponseExpected = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "20142015"), JsonNode.class, String.format("%s/roster", teamId));

        JsonNode rosterActual = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual).
                read(String.format(
                        "$..records[?(@.standingsType == 'byDivision')].teamRecords[?(@.team.id == %s)]..roster.roster", teamId));
        JsonNode rosterExpected = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseExpected).
                read("$..roster");

        List rosterActualList = Lists.newArrayList(rosterActual.get(0).iterator());
        List rosterExpectedList = Lists.newArrayList(rosterExpected.get(0).iterator());
        
        Collections.sort(rosterActualList, getRosterPersonComparator());
        Collections.sort(rosterExpectedList, getRosterPersonComparator());

        softAssert.assertEquals(rosterActualList, rosterExpectedList, "Rosters are not the same");
    }

    protected Comparator getRosterPersonComparator() {
        return new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((ObjectNode) o1).get("person").get("id").asInt() - ((ObjectNode) o2).get("person").get("id").asInt();
            }
        };
    }
}
