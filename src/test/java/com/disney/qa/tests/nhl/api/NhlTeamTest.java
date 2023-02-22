package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.atbat.AtBatApiContentProvider;
import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.api.nhl.pojo.stats.teams.Team;
import com.disney.qa.api.nhl.pojo.stats.teams.Teams;
import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Next thing to do will be teams.
 * <p/>
 * We will have the following endpoints
 * <p/>
 * qa-statsapi.web.nhl.com/api/v1/teams?season=20112012&fields=teams,id
 */
public class NhlTeamTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String TEAMS_SEASON_RESPONSE_DATA_FILE = "nhl/api/teams/teams_season_20112012.json";

    public static final String TEAMS_SEASON_SPECIFIC_TEAM_RESPONSE_DATA_FILE = "nhl/api/teams/teams_season_20112012_team_id_4.json";

    public static final String TEAMS_EXPAND_ROSTER_RESPONSE_DATA_FILE = "nhl/api/teams/teams_expand_roster_season_19941995.json";

    public static final String TEAMS_EXPAND_ROSTER_SPECIFIC_TEAM_RESPONSE_DATA_FILE = "nhl/api/teams/teams_expand_roster_season_19941995_team_id_4.json";

    public static final String TEAMS_EXPAND_ROSTER_PERSON_RESPONSE_DATA_FILE = "nhl/api/teams/teams_expand_roster_person_season_19992000.json";

    public static final String TEAMS_EXPAND_LEADERS_RESPONSE_DATA_FILE = "nhl/api/teams/teams_expand_leaders_20132014.json";

    public static final String TEAMS_BY_LEAGUE_ID = "nhl/api/teams/teams_by_league_id_394.json";

    public static final String TEAMS_BY_LEAGUE_ID_AND_SEASON = "nhl/api/teams/teams_by_league_id_394_season_20162017.json";

    public static final String TEAMS_BY_LEAGUE_ID_AND_TEAM_ID = "nhl/api/teams/teams_by_league_id_394_team_id_1_2_3_4.json";

    public static final String TEAM_ID_4 = "4";

    public static final String TEAM_ID_6 = "6";

    private AtBatApiContentProvider contentProvider = new AtBatApiContentProvider();

    @Test
    public void verifySpecificSeason() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_SEASON_RESPONSE_DATA_FILE, ImmutableMap.of("season", "20112012"), null);
    }

    @Test
    public void verifySpecificSeasonAndSpecificTeam() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_SEASON_SPECIFIC_TEAM_RESPONSE_DATA_FILE, ImmutableMap.of("season", "20112012"), TEAM_ID_4);
    }

    @Test
    public void verifyExpandTeamRoster() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_EXPAND_ROSTER_RESPONSE_DATA_FILE, ImmutableMap.of("expand", "team.roster", "season", "19941995"), null);
    }

    @Test
    public void verifyExpandTeamRosterAndSpecificTeam() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_EXPAND_ROSTER_SPECIFIC_TEAM_RESPONSE_DATA_FILE, ImmutableMap.of("expand", "team.roster", "season", "19941995"), TEAM_ID_4);
    }

    @Test
    public void verifyExpandRosterPerson() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_EXPAND_ROSTER_PERSON_RESPONSE_DATA_FILE, ImmutableMap.of("expand", "roster.person", "season", "19992000"), "10/roster");
    }

	@Test
	public void verifyBadRequest() {
		String url = NhlParameters.getNhlStatsApiHost() + "/api/v1/teams?teamId=fsd";
		LOGGER.info("url1: " + url);

		UriComponents uriComponents1 = UriComponentsBuilder.fromHttpUrl(url).build();

		String httpStatus = safeStatus(new RequestEntity<>(HttpMethod.GET, uriComponents1.toUri()), String.class);

		Assert.assertTrue(httpStatus.equals("400"),
				String.format("Expected '400' status code for endpoint, instead found %S", httpStatus));
	}

    @Test
    public void verifyFullRoster() {
        Teams teams = nhlStatsApiContentService
                .getTeams(ImmutableMap.of("expand", "team.roster", "rosterType", "fullRoster"), Teams.class);
        SoftAssert softAssert = new SoftAssert();

        for (Team team : teams.getTeams()) {
            if (team.getRoster() != null) {
                softAssert.assertTrue(team.getRoster().getRoster().size() > 0,
                        String.format("There are less than 55 players in team %s - %s totally", team.getName(),
                                team.getRoster().getRoster().size()));
            }
        }
        softAssert.assertAll();
    }

    @Test
    public void verifyRoster() {
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", "team.roster"), Teams.class);

        SoftAssert softAssert = new SoftAssert();

        for (Team team : teams.getTeams()) {
            if (team.getRoster() != null) {
                softAssert.assertTrue(team.getRoster().getRoster().size() >= 18 && team.getRoster().getRoster().size() <= 90,
                        String.format("There are less than 18 or more than 90 players in team %s - %s totally", team.getName(),
                                team.getRoster().getRoster().size()));
            }
        }
        softAssert.assertAll();
    }

    /**
     * https://jira.mlbam.com/browse/SDAPINHL-1067
     */
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyLineCombinationDataDepthChart(){
        JsonNode rawRosterPersonTeam3 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "roster(rosterType=depthChart)"), JsonNode.class, "3");
        JsonNode rawRosterPersonTeam4 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "roster(rosterType=depthChart)"), JsonNode.class, "4");

        JsonNode rosterPersonTeam3 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawRosterPersonTeam3)
                .read("$..roster.roster..person");
        JsonNode rosterPersonTeam4 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawRosterPersonTeam4)
                .read("$..roster.roster..person");

        JsonNode rosterLineNumberTeam3 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawRosterPersonTeam3).read("$..roster.roster..lineNumber");
        JsonNode rosterLineNumberTeam4 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawRosterPersonTeam4).read("$..roster.roster..lineNumber");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(rosterPersonTeam3.size() > 2, "More than 2 person for roster");
        softAssert.assertTrue(rosterLineNumberTeam3.size() == rosterPersonTeam3.size(), "LineNumber exist for every person in roster");
        softAssert.assertTrue(rosterPersonTeam4.size() > 2, "More than 2 person for roster");
        softAssert.assertTrue(rosterLineNumberTeam4.size() == rosterPersonTeam4.size(), "LineNumber exist for person in roster");

        softAssert.assertAll();
    }

    /**
     * Jira ticket: https://jira.mlbam.com/browse/SDAPINHL-1130
     */
    @QTestCases(id = "42987")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyStatHydrateReturnTeamStats(){
        JsonNode rawTeams = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "stats(splits=statsSingleSeason)"), JsonNode.class);

        JsonNode teams = JsonPath.using(jsonPathJacksonConfiguration).parse(rawTeams).read("$.teams[*]");

        Assert.assertTrue(teams.toString().contains("teamStats"), "'teamStats' nodes are present");
    }

    /**
     * https://jira.mlbam.com/browse/SDAPINHL-1067
     */
    @Test
    public void verifyLimitOffsetAdditionalTiesMetadataTest() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_EXPAND_LEADERS_RESPONSE_DATA_FILE,
                ImmutableMap.of("expand", "team.leaders", "leaderCategories", "goals,points", "season", "20132014"),
                "4");

    }

    /**
     * https://jira.mlbam.com/browse/SDAPINHL-334
     */
    @Test
    public void verifyTeamsByLeagueId() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_BY_LEAGUE_ID, ImmutableMap.of("leagueIds", "394"), null);
    }

    @Test
    public void verifyTeamsByLeagueIdWithSeason() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_BY_LEAGUE_ID_AND_SEASON, ImmutableMap.of("leagueIds", "394", "season", "20162017"), null);
    }

    @Test
    public void verifyTeamsByLeagueIdAndTeamId() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAMS_BY_LEAGUE_ID_AND_TEAM_ID, ImmutableMap.of("leagueIds", "394", "teamId", "1,2,3,4"), null);
    }

    @Test
    public void verifyTeamContentSectionsExpandParameter() {
        String expandValue = "team.content.sections";
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", expandValue), Teams.class, TEAM_ID_6);
        Assert.assertNotNull(((Map) teams.getTeams().get(0).getAdditionalProperties().get("content")).get("sections"),
                String.format("Output for the following 'expand' parameter is empty: %s", expandValue));
    }

    @Test
    public void verifyTeamDevicePropertiesExpandParameter() {
        String expandValue = "team.deviceProperties";
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", expandValue), Teams.class, TEAM_ID_6);
        Assert.assertNotNull(teams.getTeams().get(0).getAdditionalProperties().get("deviceProperties"),
                String.format("Output for the following 'expand' parameter is empty: %s", expandValue));
    }

    /**
     * Related tickets: https://jira.mlbam.com/browse/SDAPINHL-575,  SDAPINHL-402
     */
    @Test
    public void verifyTeamDevicePropertiesExpandParameterAndroid() {
        String expandValue = "team.deviceProperties";
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", expandValue, "platform", "android-phone"), Teams.class, TEAM_ID_6);
        Assert.assertNotNull(teams.getTeams().get(0).getAdditionalProperties().get("deviceProperties"),
                String.format("Output for the following 'expand' parameter is empty: %s", expandValue));
    }

    @Test
    public void verifyTeamContentHomeAllExpandParameter() {
        String expandValue = "team.content.home.all";
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", expandValue), Teams.class, TEAM_ID_6);
        Assert.assertNotNull(((Map) teams.getTeams().get(0).getAdditionalProperties().get("content")).get("home"),
                String.format("Output for the following 'expand' parameter is empty: %s", expandValue));
    }

    @Test(enabled = false)
    public void verifyTeamSocialExpandParameter() {
        String expandValue = "team.social";
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", expandValue), Teams.class, TEAM_ID_6);
        Assert.assertNotNull(teams.getTeams().get(0).getAdditionalProperties().get("social"),
                String.format("Output for the following 'expand' parameter is empty: %s", expandValue));
    }

    @Test
    public void verifyTeamPlayoffsExpandParameterFr() {
        String expandValue = "team.playoffs";
        Teams teams = nhlStatsApiContentService.getTeams(ImmutableMap.of(
                "expand", expandValue), Teams.class);

        for (Team team : teams.getTeams()) {
            if (null == ((Map) team.getAdditionalProperties().get("playoffInfo")).get("inPlayoffs")) {
                Assert.fail(String.format("Team '%s' doesn't contain 'inPlayoffs' field", team.getName()));
            }
        }
    }

    @Test(description = "JIRA# QAA-1299", enabled = false)
    public void verifyFutureSeasonLeaders() {
        String futureSeasonLeaders = nhlStatsApiContentService.getTeams(ImmutableMap.of(
                "teamId", TEAM_ID_6, "season", "20162017", "leaderGameTypes", "", "expand", "team.leaders,leaders.team&leaderCategories=points,goals,assists"
        ), String.class);
        String currentSeasonLeaders = nhlStatsApiContentService.getTeams(ImmutableMap.of(
                "teamId", TEAM_ID_6, "season", "20152016", "leaderGameTypes", "", "expand", "team.leaders,leaders.team&leaderCategories=points,goals,assists"
        ), String.class);
        Assert.assertEquals(futureSeasonLeaders, currentSeasonLeaders, "Future leaders are not equal to current season leaders");
    }

    /**
     * To clarify, is it OK to validate schema here, not values
     */
    @QTestCases(id = "42968")
    @Test
    public void verifyTeamStats() throws IOException, JSONException, ProcessingException {
        String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("gameType", "regularSeason"), String.class, "12/stats");
        String jsonSchema = fileUtils.getResourceFileAsString("nhl/api/teams/schema/team_stats_schema.json");

        SoftAssert softAssert = new SoftAssert();

        jsonValidator.validateJsonAgainstSchema(jsonSchema, rawResponse, softAssert);

        softAssert.assertAll();
    }

    @Test(enabled = false)
    public void verifyTeamStatsForBoxScore() {

    }

    @QTestCases(id = "42969")
    @Test
    public void verifyFranchiseHistoryForTeam() {
        JsonNode rawResponseActual = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "franchise(roster(season=19171918,person(stats(splits=[yearByYear]))))"),
                JsonNode.class, "10");
        JsonNode rawResponseExpected = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "roster(person(stats(splits=[yearByYear])))&season=19171918"),
                JsonNode.class, "57");

        // TODO it's better to use JSON Path there, see example in NhlScheduleTest, verifyHydratedTeamRosterSeason
        JsonNode actualRoster = rawResponseActual.get("teams").get(0).get("franchise").get("roster");
        JsonNode expectedRoster = rawResponseExpected.get("teams").get(0).get("roster");

        Assert.assertEquals(actualRoster, expectedRoster, "Actual and expected rosters are not equal");
    }

    @QTestCases(id = "42970")
    @Test
    public void verifyContentSectionsHydrateIsNotNull() {
        String rawResponse = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("teamId", "4", "leaderGameTypes", "R", "expand", "team.playoffs,team.content.sections"),
                String.class);

        JsonNode content = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..content");

        if (content.toString().contains("sections")) {
            Assert.assertFalse(content.toString().contains("null"), "'content' node shouldn't contain null sections value");
        } else {
            Assert.assertEquals(content.toString(), "[{}]");
        }
    }

    @QTestCases(id = "42971")
    @Test
    public void verifySeasonDivision() {
        String rawResponse1 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "division"), String.class, "3");
        String rawResponse2 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "division", "season", "20012002"), String.class, "3");
        String rawResponse3 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "division", "season", "19881989"), String.class, "3");
        String rawResponse4 = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2001-11-06", "hydrate", "team(division)"), String.class);

        List<String> division1 = JsonPath.read(rawResponse1, "$..division.name");
        List<String> division2 = JsonPath.read(rawResponse2, "$..division.name");
        List<String> division3 = JsonPath.read(rawResponse3, "$..division.name");
        List<String> division4 = JsonPath.read(rawResponse4, "$..team[?(@.id==3)].division.name");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(division1.get(0), "Metropolitan", String.format("Incorrect division: %s", division1));
        softAssert.assertEquals(division2.get(0), "Atlantic", String.format("Incorrect division: %s", division2));
        softAssert.assertEquals(division3.get(0), "Patrick", String.format("Incorrect division: %s", division3));
        softAssert.assertEquals(division4.get(0), "Atlantic", String.format("Incorrect division: %s", division4));

        softAssert.assertAll();
    }

    @QTestCases(id = "42972")
    @Test
    public void verifySeasonConference() {
        String rawResponse1 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "conference"), String.class, "3");
        String rawResponse2 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "conference", "season", "20012002"), String.class, "3");
        String rawResponse3 = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "conference", "season", "19881989"), String.class, "3");
        String rawResponse4 = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2001-11-06", "hydrate", "team(conference)"), String.class);

        List<String> conference1 = JsonPath.read(rawResponse1, "$..conference.name");
        List<String> conference2 = JsonPath.read(rawResponse2, "$..conference.name");
        List<String> abbreviation2 = JsonPath.read(rawResponse2, "$..conference.abbreviation");
        List<String> conference3 = JsonPath.read(rawResponse3, "$..conference.name");
        List<String> conference4 = JsonPath.read(rawResponse4, "$..team[?(@.id==3)].conference.name");
        List<String> abbreviation4 = JsonPath.read(rawResponse4, "$..team[?(@.id==3)].conference.abbreviation");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(conference1.get(0), "Eastern", String.format("Incorrect conference in first response: %s", conference1));
        softAssert.assertEquals(conference2.get(0), "Eastern", String.format("Incorrect conference in second response: %s", conference2));
        softAssert.assertEquals(abbreviation2.get(0), "XVE", String.format("Incorrect conference abbreviation in second response: %s", abbreviation2));
        softAssert.assertEquals(conference3.get(0), "Prince of Wales", String.format("Incorrect conference in third response: %s", conference3));
        softAssert.assertEquals(conference4.get(0), "Eastern", String.format("Incorrect conference in 4th response: %s ", conference4));
        softAssert.assertEquals(abbreviation4.get(0), "XVE", String.format("Incorrect conference abbreviation in 4th response: %s", abbreviation4));

        softAssert.assertAll();
    }

    /**
     * Add more verifications
     * Comments:
     * 1. case with "game(content(all))" is not fully clear - donn't see any difference with/without it
     */
    @QTestCases(id = "42973")
    @Test
    public void verifyNestedHydratesForPreviousNextSchedule() {

        JsonNode rawResponse1 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "previousSchedule(team)"), JsonNode.class, "21");
        JsonNode rawResponse2 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "previousSchedule(tickets)"), JsonNode.class, "21");
        JsonNode rawResponse3 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "previousSchedule(linescore)"), JsonNode.class, "21");
        JsonNode rawResponse4 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "previousSchedule"), JsonNode.class, "21");


        JsonNode teamData1 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1).
                read("$..team.abbreviation");
        JsonNode teamData2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2).
                read("$..tickets");
        JsonNode teamData3 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse3).
                read("$..linescore");
        JsonNode teamData4 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse4).
                read("$..previousSchedule");


        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(teamData1.size() != 0, "Hydrate for team is absent or incorrect");
        softAssert.assertTrue(teamData2.size() != 0, "Hydrate for tickets is absent or incorrect");
        softAssert.assertTrue(teamData3.size() != 0, "Hydrate for linescore is absent or incorrect");
        softAssert.assertTrue(teamData4.size() != 0, "Hydrate for linescore is absent or incorrect");

        softAssert.assertAll();
    }

    @QTestCases(id = "42974")
    @Test
    public void verifyCache60Seconds() {
        ResponseEntity response = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + "/api/v1/teams/21", String.class);
        Assert.assertEquals(response.getHeaders().get("Cache-Control").get(0), "max-age=60, public",
                String.format("'Cache-Control' header has unexpected value: %s",
                        response.getHeaders().get("Cache-Control").get(0)));
    }

    @QTestCases(id = "42975")
    @Test
    public void verifyRadioBroadcastsShowingUp() {
        JsonNode rawResponse1 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "previousSchedule(date=2016-11-11,limit=2)"), JsonNode.class, "21");
        JsonNode rawResponse2 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "previousSchedule(date=2016-11-11,limit=2,radioBroadcasts)"), JsonNode.class,
                "21");

        JsonNode teamData1 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
                .read("$..radioBroadcasts");
        JsonNode teamData2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
                .read("$..radioBroadcasts");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(teamData1.size() == 0, "RadioBroadcasts are present as not expected!");
        softAssert.assertTrue(teamData2.size() > 0, "RadioBroadcasts are not present!");
        softAssert.assertAll();
    }

    @QTestCases(id = "42976")
    @Test
    public void verifyTeamsEndpointNotReturningError() {
        JsonNode rawResponse1 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("site", "en_nhl", "teamId", "20,4", "expand",
                        "team.roster,team.stats,roster.person,person.stats", "stats", "statsSingleSeason"),
                JsonNode.class);

        JsonNode teamData1 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
                .read("$..teams[?(@.id == 20)]");
        JsonNode teamData2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
                .read("$..teams[?(@.id == 4)]");
        JsonNode rosterData2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1).read("$..roster");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(teamData1.size() == 1, "Team with id = 20 is not present!");
        softAssert.assertTrue(teamData2.size() == 1, "Team with id = 4 is not present!");
        softAssert.assertTrue(rosterData2.size() > 0, "'roster' node is not present!");
        softAssert.assertAll();
    }

    @DataProvider(name = "teamData")
    public Object[][] teamStatData() {
        return new Object[][] {
                {"TUID: hydrate_stats", "/api/v1/teams?gameType=P&hydrate=stats&season=20162017"},
                {"TUID: expand_team", "/api/v1/teams?gameType=P&expand=team.stats&season=20162017"},
        };
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1206
     */
    @QTestCases(id = "42989")
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "teamData")
    public void verifyTeamStatsReturnData(String TUID, String request) throws IOException, JSONException {

        String rawTeam = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, String.class)
                .getBody();

        String snapShotTeam = fileUtils.getResourceFileAsString("nhl/api/teams/team_stats_season_20162017.json");

        JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.LENIENT,
                Lists.newArrayList("copyright"));
        JSONAssert.assertEquals(snapShotTeam, rawTeam, jsonCustomIgnoreComparator);
    }

    @QTestCases(id = "42977")
    @Test
    public void verifyPointPercentageRanking() {
        JsonNode rawResponse1 = nhlStatsApiContentService
                .getTeams(ImmutableMap.of("season", "20142015", "hydrate", "stats(splits=statsSingleSeason)"), JsonNode.class);

        JsonNode pointPercentageRankingData = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
                .read("$..splits[0]..ptPctg");

        Assert.assertTrue(pointPercentageRankingData.size() == 30,
                "Point Percentage in Team stats not displaying correctly!");
    }

    @DataProvider(name = "seasonRanges")
    public Object[][] seasonRangeEndpointsDataProvider() {
        return new Object[][]{
                {"/api/v1/teams/history?hydrate=seasonRange"},
                {"/api/v1/teams/11?hydrate=seasonRange"},
                {"/api/v1/teams?hydrate=seasonRange"},
                {"/api/v1/schedule?&hydrate=team(seasonRange)"}
        };
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-716
     */
    @Test(dataProvider = "seasonRanges", enabled = false)
    public void verifySeasonRange() {
        String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "seasonRange"), String.class, "history");

    }

    @DataProvider(name = "teamsRoster")
    public Object[][] invalidNhlRequzestsDataProvider() {
        return new Object[][] {
                {"TUID: language_ru_season", "/api/v1/teams/16/roster?language=ru&season=20152016", "nhl/api/teams/teams_roster_season_20152016_ru.json"},
                {"TUID: season_20152016", "/api/v1/teams/16/roster?season=20152016", "nhl/api/teams/teams_roster_season_20152016_en.json"},
        };
    }

	/**
	 * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-985
	 */
    @QTestCases(id = "42978")
	@Test(dataProvider = "teamsRoster")
	public void verifyFullNamesWithRussianCharacters(String TUID, String request, String file) throws IOException, JSONException {

		String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, String.class)
				.getBody();

		String responseSnapshot = fileUtils.getResourceFileAsString(file);

		JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.LENIENT,
				Lists.newArrayList("jerseyNumber"));

		JSONAssert.assertEquals(responseSnapshot, rawResponse, jsonCustomIgnoreComparator);
	}

    @DataProvider(name = "teamsLogos")
    public Object[][] teamsLogosDataProvider() {
        return new Object[][] { { "TUID: 1", "/api/v1/teams/8?hydrate=logos", "nhl/api/teams/teams_logos_teamId_8.json" },
                { "TUID: 2", "/api/v1/teams/8?hydrate=logos(season=19871988)",
                        "nhl/api/teams/teams_logos_teamId_8_season_19871988.json" },
                { "TUID: 3", "/api/v1/teams/8?hydrate=logos(platform=ios-phone)",
                        "nhl/api/teams/teams_logos_teamId_8_platform_ios_phone.json" },
                { "TUID: 4", "/api/v1/teams/8?hydrate=logos(type=svg)", "nhl/api/teams/teams_logos_teamId_8_type_svg.json" },
                { "TUID: 5", "/api/v1/teams/8?hydrate=logos(size=250)", "nhl/api/teams/teams_logos_teamId_8_size_250.json" },
                { "TUID: 6", "/api/v1/teams/history?hydrate=logos(season=19241925)",
                        "nhl/api/teams/teams_logos_teamId_8_history_season_19241925.json" } };

    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-831
     */
    @QTestCases(id = "42979")
    @Test(dataProvider = "teamsLogos")
    public void verifyHockeyTeamLogos(String TUID, String request, String file) throws IOException, JSONException {

        String rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, String.class)
                .getBody();

        String responseSnapshot = fileUtils.getResourceFileAsString(file);

        JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.LENIENT,
                Lists.newArrayList("copyright"), Lists.newArrayList("PPG PAINTS Arena"));
        JSONAssert.assertEquals(responseSnapshot, rawResponse, jsonCustomIgnoreComparator);
    }

    @DataProvider(name = "objectNotFound")
    public Object[][] objectNotFoundTeamEndpointsDataProvider() {
        return new Object[][] {
                {"TUID: team_99", "/api/v1/teams/99"},
                {"TUID: team_roster", "/api/v1/teams/99/roster"},
                {"TUID: team_leader", "/api/v1/teams/99/leaders?leaderCategories=assists"},
                {"TUID: team_hydrate_leaders", "/api/v1/teams/99?hydrate=leaders(categories=assists)"}
        };
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1014
     */
    @QTestCases(id = "42980")
    @Test(dataProvider = "objectNotFound")
    public void verifyDefaultHockeyTeamObjectNotFound(String TUID, String request) {
        String message = "";
        try {
            restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, String.class);
        } catch (HttpClientErrorException e) {
            List<String> messages = JsonPath.read(e.getResponseBodyAsString(), "$..message");
            message = messages.get(0);
        }

        Assert.assertEquals(message, "Object not found", String.format("Incorrect message: %s", message));
    }

    @DataProvider(name = "emptyResponse")
    public Object[][] emptyResponseTeamEndpointsDataProvider() {
        return new Object[][] {
                {"TUID: team_stats", "/api/v1/teams/99/stats"},
                {"TUID: team_stats_leaders", "/api/v1/stats/leaders?teamId=99&leaderCategories=assists"}
        };
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1014
     */
    @QTestCases(id = "42981")
    @Test(dataProvider = "emptyResponse")
    public void verifyDefaultHockeyTeamEmptyResponse(String TUID, String request) {
        JsonNode rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, JsonNode.class)
                .getBody();
        JsonNode teamNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..team");
        Assert.assertTrue(teamNodes.size() == 0, "Default Hockey Team is not removed!");
    }

    @DataProvider(name = "fullMinusOne")
    public Object[][] fullMinusOneTeamEndpointsDataProvider() {
        return new Object[][] {
                {"TUID: teams", "/api/v1/teams"},
                {"TUID: teams_history", "/api/v1/teams/history"}
        };
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1014
     */
    @QTestCases(id = "42982")
    @Test(dataProvider = "fullMinusOne")
    public void verifyDefaultHockeyTeamFullMinusOne(String TUID, String request) {
        JsonNode rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, JsonNode.class)
                .getBody();
        JsonNode teamNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..[?(@.id == 99)]");
        Assert.assertTrue(teamNodes.size() == 0, "Default Hockey Team is not removed!");
    }

    @MethodOwner(owner = "cboyle")
    @Test(dataProvider = "DataProvider")
    @XlsDataSourceParameters(sheet = "abbreviation", dsUid = "TUID, Name")
    public void verifyTeamSlugNamesExist(Map<String, String> data) {

        SoftAssert softAssert = new SoftAssert();

        String teamId = data.get("id");
        String teamSlug = data.get("slugName");

        JsonNode rawTeamsHydrateName = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "name"), JsonNode.class);
        scanJson(softAssert, rawTeamsHydrateName, teamId, teamSlug);

        JsonNode rawTeamsExpandTeamName = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", "team.name"), JsonNode.class);
        scanJson(softAssert, rawTeamsExpandTeamName, teamId, teamSlug);

        JsonNode rawTeamsIdHydrateName = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "name"), JsonNode.class, data.get("id"));
        scanJson(softAssert, rawTeamsIdHydrateName, teamId, teamSlug);

        JsonNode rawTeamsIdExpandTeamName = nhlStatsApiContentService.getTeams(ImmutableMap.of("expand", "team.name"), JsonNode.class, data.get("id"));
        scanJson(softAssert, rawTeamsIdExpandTeamName, teamId, teamSlug);

        softAssert.assertAll();
    }

    @DataProvider(name = "teamLanguages")
    public Iterator<Object[]> languageDataProvider() {
        List<Object[]> languageList = new ArrayList<>();

        Map<String, Integer> leagueTeamIds = new HashMap<>();
        leagueTeamIds.put("NHL", 8);
        leagueTeamIds.put("WCOH", 7401);

        Iterator leagueIterator = leagueTeamIds.entrySet().iterator();
        while (leagueIterator.hasNext()) {
            Map.Entry league = (Map.Entry) leagueIterator.next();
            String leagueName = league.getKey().toString();
            String teamId = league.getValue().toString();

            LOGGER.info(String.format("KEY (%s) VALUE (%s)", league.getKey(), league.getValue()));
            addToLanguageList(languageList, leagueName, teamId, "en_nhl");
            addToLanguageList(languageList, leagueName, teamId,  "fr_nhl");
            addToLanguageList(languageList, leagueName, teamId,  "cs_nhl");
            addToLanguageList(languageList, leagueName, teamId,  "sv_nhl");
            addToLanguageList(languageList, leagueName, teamId,  "de_nhl");
            addToLanguageList(languageList, leagueName, teamId,  "ru_nhl");
            addToLanguageList(languageList, leagueName, teamId,  "fi_nhl");
            addToLanguageList(languageList, leagueName, teamId,  "sk_nhl");
        }

        return languageList.iterator();
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-847
     */
    @QTestCases(id = "42983")
    @MethodOwner(owner = "cboyle")
    @Test(dataProvider = "teamLanguages")
    public void verifyTeamRosterPlayerValidations(String TUID, String teamId, String language) {
        SoftAssert softAssert = new SoftAssert();

        JsonNode rawTeamRosterPlayer = nhlStatsApiContentService
                .getTeams(ImmutableMap.of("expand", "roster.person", "season", "20162017", "site", language), JsonNode.class, teamId + "/roster");

        ArrayNode playerNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawTeamRosterPlayer).read("$.roster[*].person");

        for (JsonNode playerNode : playerNodes) {
            LOGGER.info("Full Name: " + playerNode.findValue("fullName"));
            String playerId = playerNode.findValue("id").asText();
            String fullName = playerNode.findValue("fullName").asText();
            String firstName = playerNode.findValue("firstName").asText();
            String lastName = playerNode.findValue("lastName").asText();

            softAssert.assertTrue(fullName.length() > 0,
                    String.format("Expected 'fullName' node for Player Id (%s) to be populated", playerId));
            softAssert.assertTrue(firstName.length() > 0,
                    String.format("Expected 'firstName' node for Player Id (%s) to be populated", playerId));
            softAssert.assertTrue(lastName.length() > 0,
                    String.format("Expected 'lastName' node for Player Id (%s) to be populated", playerId));
            if ("ru_nhl".equalsIgnoreCase(language)) {
                checkIfCyrillicExists(softAssert, fullName);
                checkIfCyrillicExists(softAssert, firstName);
                checkIfCyrillicExists(softAssert, lastName);
            }
        }

        softAssert.assertTrue(playerNodes.size() >= 23, String.format("Expected at least 23 active 'players', found %s instead", playerNodes.size()));
        softAssert.assertAll();
    }

	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1078
	 */
    @QTestCases(id = "42984")
	@Test
	public void verifyHydratingStatsWithoutSplitsParameter() {
		JsonNode rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("site", "en_nhl", "teamId", "1,2",
				"hydrate", "roster(person(stats(splits=statsSingleSeason)))"), JsonNode.class);
		JsonNode statsNodesList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..person.stats");

		String rawResponse2 = nhlStatsApiContentService.getTeams(
				ImmutableMap.of("site", "en_nhl", "teamId", "1,2", "hydrate", "roster(person(stats))"), String.class);
		JsonNode messageNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2).read("$..message");
		String message = messageNode.get(0).asText();

		Assert.assertEquals(message,
				"the 'stats' hydration requires a 'splits' parameter e.g. stats(splits=statsSingleSeason). See api/v1/statTypes for all possible values; ",
				String.format("Incorrect message: %s", message));

		Assert.assertTrue(statsNodesList.size() > 0, "'stats' node is not present!");
	}

    /**
     * JIRA https://jira.mlbam.com/browse/SDAPINHL-1076
     */
    @QTestCases(id = "42985")
    @Test
    public void verifyAddSaveShootingStatsRanks() {
        String rawResponse = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "20142015", "hydrate", "stats(splits=statsSingleSeason)"), String.class);

        List<Double> shootingPctgList = JsonPath.read(rawResponse, "$..stat.shootingPctg");
        List<Double> savePctgList = JsonPath.read(rawResponse, "$..stat.savePctg");
        List<String> savePctRankList = JsonPath.read(rawResponse, "$..stat.savePctRank");
        List<String> shootingPctRankList = JsonPath.read(rawResponse, "$..stat.shootingPctRank");

        verifyStringsRepresentingOrdinals(savePctRankList);
        verifyStringsRepresentingOrdinals(shootingPctRankList);

        Assert.assertTrue(shootingPctgList.size() == 30, "Incorrect number of 'shootingPctgList' nodes!");
        Assert.assertTrue(savePctgList.size() == 30, "Incorrect number of 'savePctgList' nodes!");
    }

    /**
     * JIRA https://jira.mlbam.com/browse/SDAPINHL-1106
     */
    @QTestCases(id = "42986")
    @Test
    public void verifyTeamLeadersLimitParameter() {
        String rawResponse = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "leaders(person,categories=[goals,points,assists],limit=1)"), String.class,
                "5");
        ArrayNode limitNodesListValues = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..limit");
        if (limitNodesListValues.size() > 0) {
            for (JsonNode limitNode : limitNodesListValues) {
                String value = limitNode.asText();
                if (!value.equals("1")) {
                    Assert.fail("'limit' node value is not equals '1'!");
                }
            }
        } else {
            Assert.fail("'limit' nodes are not present!");
        }
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1149
     */
    @QTestCases(id = "42988")
    @Test
    public void verifyRosterQueryOnlyConsidersRegularSeasonGames() {
        JsonNode rawResponse = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("expand", "team.roster,roster.person", "season", "19421943"), JsonNode.class, "6");

        JsonNode statsNodesList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..person[?(@.fullName== 'Bill Anderson')]");
        Assert.assertTrue(statsNodesList.size() > 0, "Bill Anderson is not returned!");
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1222
     */
    @Test
    public void verifyHydrateTeamPlayoffStatsNotMissingTypeElement() {
        JsonNode rawResponse = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("gameType", "P", "hydrate", "stats", "season", "20162017"), JsonNode.class, "3");

        JsonNode statsNodesList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..teamStats..type[?(@.displayName == 'statsSingleSeasonPlayoffs')]");
        Assert.assertTrue(statsNodesList.size() > 0, "'type' element is not returned!");
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1224
     */
    @QTestCases(id = "42990")
    @Test
    public void verifyMadePlayoffsAttribute() {
        JsonNode rawResponse = nhlStatsApiContentService
                .getTeams(ImmutableMap.of("hydrate", "playoffs(season=20142015)"), JsonNode.class);
        JsonNode playOffInfoNodesList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..playoffInfo");

        SoftAssert softAssert = new SoftAssert();
        if (playOffInfoNodesList.size() > 0) {
            for (JsonNode playOffInfoNode : playOffInfoNodesList) {
                JsonNode madePlayoffsNodesList = JsonPath.using(jsonPathJacksonConfiguration).parse(playOffInfoNode)
                        .read("$..[?(@.madePlayoffs== true || @.madePlayoffs== false)]");
                softAssert.assertTrue(madePlayoffsNodesList.size() > 0, "madePlayoffs is not present!");
            }
        } else {
            softAssert.fail("'playoffInfo' nodes are not present!");
        }

        softAssert.assertAll();
    }

    @DataProvider(name = "teamsHistoryLogos")
    public Object[][] teamsHistoryHydrateLogos() {
        return new Object[][] {
                {"TUID: chromecast", "/api/v1/teams/history?hydrate=logos(platform=chromecast)"},
                {"TUID: chromecast_season", "/api/v1/teams/history?hydrate=logos(platform=chromecast,season=19261927)"}
        };
    }
    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1227
     */
    @QTestCases(id = "42991")
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "teamsHistoryLogos")
    public void verifyTeamHistoryHydrateLogos (String TUID, String request) {
        JsonNode rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, JsonNode.class)
                .getBody();

        JsonNode teams = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$.teams");
        JsonNode teamsLogos = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$.teams[*].logos");
        JsonNode teamsIds = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$.teams[*].id");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(!teams.findPath("logos").isMissingNode(),
                String.format("Expected 'logos' node to exist, instead found %s", teams.findPath("stages").toString()));

        softAssert.assertTrue(((teamsLogos.size() == teamsIds.size() && teamsLogos.size() > 0)),
                String.format("Expected 'teamsLogos' and 'teamsId' to be equal in size: %s %s", teamsLogos.size(), teamsIds.size()));

        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1272
     */
    @QTestCases(id = "42992")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyLogosSizeRokuDimension() {
        JsonNode rawLogos = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("hydrate", "logos(size=128,platform=roku)", "fields", "teams,division,name,logos,light,url"), JsonNode.class);
        JsonNode rawRokuDimension = nhlStatsApiContentService.getProvidedEndpoint(ImmutableMap.of(), JsonNode.class, "imageSizes");

        JsonNode logos = JsonPath.using(jsonPathJacksonConfiguration).parse(rawLogos).read("$.teams[*].logos");
        JsonNode rokuDimension = JsonPath.using(jsonPathJacksonConfiguration).parse(rawRokuDimension).read("$.ROKU..dimension");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(!logos.isMissingNode(), "Expected 'logos' nodes to be present" + logos);
        softAssert.assertTrue(rokuDimension.toString().contains("128x128"), "Expected 'imageSize' for ROKU to contain '128x128' " + rokuDimension.get(1));

        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1143
     */
    @QTestCases(id = "42993")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyHydrateCoaches(){
        JsonNode rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("hydrate", "coaches"), JsonNode.class);

        JsonNode coaches = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..coaches");

        SoftAssert softAssert = new SoftAssert();

        if(coaches.size() > 0){
            for(JsonNode coachNode : coaches){
                softAssert.assertTrue(!coachNode.isMissingNode(), String.format("Expected 'coaches' node to populate: %s", coachNode));
            }

            softAssert.assertAll();
        }
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/QAA-2092
     */
    @QTestCases(id = "42994")
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "DataProvider", description = "Verify: Team Page", enabled = false)
    @XlsDataSourceParameters(sheet = "abbreviation_with_nhl", dsUid = "TUID, Name")
    public void verifyTeamPageVTPs (Map<String, String> data) {

        String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("teamId", data.get("id"), "expand", "team.playoffs,team.content.sections"), String.class);
        JsonNode typeVideos = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..content.sections.itemsList[?(@.type == 'video')]");

        SoftAssert softAssert = new SoftAssert();

        for(JsonNode typeVideo : typeVideos){
            ArrayNode topicIds = JsonPath.using(jsonPathJacksonConfiguration).parse(typeVideo).read("$..topicId");

            MultiValueMap<String, String> listQueryParams = new LinkedMultiValueMap<>();
            listQueryParams.add("page", "1");
            listQueryParams.add("expand", "playbacks.HTTP_CLOUD_TABLET_60,partner");

            for(JsonNode topicId : topicIds){
                RequestEntity<String> checkUrls = contentProvider.buildRequestEntity(
                        "https",
                        "search-api.svc.nhl.com",
                        "/svc/search/v2/nhl_global_en/topic/" + topicId,
                        listQueryParams,
                        HttpMethod.GET
                );

                String httpStatus = safeStatus(checkUrls, String.class);
                softAssert.assertTrue("200".equalsIgnoreCase(httpStatus),
                        String.format("HTTP Status (%s) for URL (%s)", httpStatus, checkUrls.getUrl()));
            }
        }
        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/QAA-2091
     */
    @QTestCases(id = "42995")
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "DataProvider", description = "Verify: Team Page", enabled = false)
    @XlsDataSourceParameters(sheet = "abbreviation_with_nhl", dsUid = "TUID, Name")
    public void verifyTeamPageATPs (Map<String, String> data) {

        String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("teamId", data.get("id"), "expand", "team.playoffs,team.content.sections"), String.class);
        JsonNode typeArticles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..content.sections.itemsList[?(@.type == 'article')]");

        SoftAssert softAssert = new SoftAssert();

        for(JsonNode typeArticle : typeArticles){
            ArrayNode topicIds = JsonPath.using(jsonPathJacksonConfiguration).parse(typeArticle).read("$..topicId");

            MultiValueMap<String, String> listQueryParams = new LinkedMultiValueMap<>();
            listQueryParams.add("page", "1");
            listQueryParams.add("expand", "partner");

            for(JsonNode topicId : topicIds){
                RequestEntity<String> checkUrls = contentProvider.buildRequestEntity(
                        "https",
                        "search-api.svc.nhl.com",
                        "/svc/search/v2/nhl_global_en/topic/" + topicId,
                        listQueryParams,
                        HttpMethod.GET
                );

                String httpStatus = safeStatus(checkUrls, String.class);
                softAssert.assertTrue("200".equalsIgnoreCase(httpStatus),
                        String.format("HTTP Status (%s) for URL (%s)", httpStatus, checkUrls.getUrl()));
            }
        }
        softAssert.assertAll();
    }

    private void addToLanguageList(List languageList, String leagueName, String teamId, String language) {
        languageList.add(
                new Object[]{
                        String.format("TUID: League (%s) Language: (%s) ", leagueName, language), teamId, language});
    }

    private void verifyStringsRepresentingOrdinals(List<String> values) {
        SoftAssert softAssert = new SoftAssert();
        for (String value : values) {
            softAssert.assertTrue(value.matches("[0-9]*(st|nd|rd|th)"),
                    "Value :" + value + " is not StringsRepresentingOrdinals!");
        }
        softAssert.assertAll();
    }
}