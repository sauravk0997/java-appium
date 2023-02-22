package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.api.nhl.pojo.Game;
import com.disney.qa.api.nhl.pojo.Schedule;
import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpServerErrorException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by mk on 11/28/15.
 */
public class NhlScheduleTest extends BaseNhlApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String SCHEDULE_DATE_RESPONSE_DATE_FILE = "nhl/api/schedule/schedule_date_2015_11_15.json";

    public static final String SCHEDULE_EXPAND_TEAM_RESPONSE_DATE_FILE = "nhl/api/schedule/schedule_expand_teams_date_2015_04_01.json";

    public static final String SCHEDULE_EXPAND_LINESCORE_RESPONSE_DATE_FILE = "nhl/api/schedule/schedule_expand_linescore_date_2015_04_01.json";

    public static final String SCHEDULE_EXPAND_SCORINGPLAYS_AND_PERSONS_RESPONSE_DATE_FILE = "nhl/api/schedule/schedule_expand_scoringplays_and_persons_date_2015_04_01.json";

    public static final String SCHEDULE_EXPAND_DECISIONS_AND_PERSONS_RESPONSE_DATE_FILE = "nhl/api/schedule/schedule_expand_decisions_and_persons_date_2015_04_01.json";

    public static final String SCHEDULE_EXPAND_VENUE_RESPONSE_DATE_FILE_HISTORICAL = "nhl/api/schedule/schedule_expand_venue_date_2015_04_01.json";

    public static final String SCHEDULE_EXPAND_VENUE_RESPONSE_DATE_FILE = "nhl/api/schedule/schedule_expand_venue_date_2017_04_01.json";

    public static final String SCHEDULE_EXPAND_GAME_SERIES_DATE_FILE = "nhl/api/schedule/schedule_expand_games_series_date_2014_04_19.json";

    public static final String SCHEDULE_NO_GAMES_DATE_DATA_FILE = "nhl/api/schedule/schedule_date_2016_09_02.json";

    public static final String ONE_DAY_DATE = "10/15/2015";

    public static final String ONE_DAY_DATE_NO_GAMES = "07/01/2015";

    public static final String DATE_RANGE_START_DATE = "04/01/2015";

    public static final String DATE_RANGE_END_DATE = "04/07/2015";

    public static final String TEAM_ID = "01";

    public static final String SEASON_2015_2016 = "20152016";

    public static final String FIELDS_FILTER_1 = "dates,games,date,gamePk,gameDate";

    public static final String FIELDS_FILTER_2 = "dates,games,date,gamePk";

    public static final String FIELDS_FILTER_3 = "dates,games,date";

    public static final String GAME_STATUS = "/api/v1/gameStatus";

    /**
     * Compare Stats API response with snapshot for 10/15/2015
     * Snapshot updated 01/07/2016
     * Snapshot updated 09/13/2016
     * Snapshot updated 10/07/2016 - tricodes were added
     * Snapshot updated 11/04/2016 - tricodes were removed
     * <p/>
     * TODO to ask: parameter '"wait": 10' was added - to clarify
     * "startTimeTBD" was added
     * @throws JSONException 
     */
	@Test
	public void compareStatsApiResponseWithSnapshot() throws IOException, JSONException {
		String responseSnapshot = fileUtils.getResourceFileAsString(SCHEDULE_DATE_RESPONSE_DATE_FILE);
		String actualResponse = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "11/15/2015"),
				String.class);

		JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.LENIENT,
				Lists.newArrayList("id", "link", "wait"));

		JSONAssert.assertEquals(responseSnapshot, actualResponse, jsonCustomIgnoreComparator);
	}

    /**
     * In this test http://www.nhl.com/feed/nhl/league/schedule/expanded/schedule.json feed endpoint is requested.
     * Also possible http://www.nhl.com/feed/nhl/league/schedule.json
     * TODO [minor] specify, which endpoint to use
     */
    @QTestCases(id = "42928")
    @Test(enabled = false)
    public void verifyOneDayGames() {
        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", ONE_DAY_DATE));
        //LOGGER.info(String.format("Games of date '%s', Stats API response: %s", ONE_DAY_DATE, statsSchedule.toString()));

        Schedule feedSchedule = nhlFeedContentService.getSchedule(ImmutableMap.of("date", ONE_DAY_DATE));
        //LOGGER.info(String.format("Games of date '%s', Feed response: %s", ONE_DAY_DATE, feedSchedule.toString()));

        addAccentToMontrealCanadiensName(feedSchedule);

        assertSchedules(statsSchedule, feedSchedule);
    }

    @QTestCases(id = "42932")
    @Test(enabled = false)
    public void verifyOneDayAbsentGames() {
        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", ONE_DAY_DATE_NO_GAMES));
        //LOGGER.info(String.format("Games of date '%s', Stats API response: %s", ONE_DAY_DATE_NO_GAMES, statsSchedule.toString()));

        Schedule feedSchedule = nhlFeedContentService.getSchedule(ImmutableMap.of("date", ONE_DAY_DATE_NO_GAMES));
        //LOGGER.info(String.format("Games of date '%s', Feed response: %s", ONE_DAY_DATE_NO_GAMES, feedSchedule.toString()));

        assertSchedules(statsSchedule, feedSchedule);
    }

    @QTestCases(id = "42929")
    @Test(enabled = false)
    public void verifyDateRangeGames() {
        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("startDate", DATE_RANGE_START_DATE, "endDate", DATE_RANGE_END_DATE));
        //LOGGER.info(String.format("Games of date range from '%s' to '%s', Stats API response: %s", DATE_RANGE_START_DATE, DATE_RANGE_END_DATE, statsSchedule.toString()));

        Schedule feedSchedule = nhlFeedContentService.getSchedule(
                ImmutableMap.of("startDate", DATE_RANGE_START_DATE, "endDate", DATE_RANGE_END_DATE));
        //LOGGER.info(String.format("Games of date range from '%s' to '%s', Feed response: %s", DATE_RANGE_START_DATE, DATE_RANGE_END_DATE, statsSchedule.toString()));

        addAccentToMontrealCanadiensName(feedSchedule);

        assertSchedules(statsSchedule, feedSchedule);
    }

    /**
     * TODO verify dates in response - not just amount
     */
    @QTestCases(id = "42945")
    @Test
    public void verifyDateRangeExtended() {
        JsonNode games0 = getGamesOfDateRange(ImmutableMap.of("teamId", "53", "startDate", "2016-06-01", "endDate", "2016-06-30"));
        JsonNode games6 = getGamesOfDateRange(ImmutableMap.of("startDate", "05/29/2016", "endDate", "07/02/2016", "fields", "dates,date,totalItems,games,gamePk,gameType", "platform", "xbox-360"));
        JsonNode games166 = getGamesOfDateRange(ImmutableMap.of("startDate", "2016-09-01", "endDate", "2016-10-20"));
        JsonNode games31 = getGamesOfDateRange(ImmutableMap.of("startDate", "09/29/2016", "endDate", "10/02/2016"));
        JsonNode games1030 = getGamesOfDateRange(ImmutableMap.of("startDate", "2016-02-01", "endDate", "2016-11-30"));
        JsonNode games2192 = getGamesOfDateRange(ImmutableMap.of("startDate", "2015-01-01", "endDate", "2016-07-01"));
        JsonNode games01 = getGamesOfDateRange(ImmutableMap.of("startDate", "2016-06-30", "endDate", "2016-08-30"));
        JsonNode games47 = getGamesOfDateRange(ImmutableMap.of("startDate", "2016-09-01", "endDate", "2016-09-30"));

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(games0.size(), 0, "Incorrect number of games in: 2016-06-01 - 2016-06-30");
        softAssert.assertEquals(games6.size(), 6, "Incorrect number of games in: 05/29/2016 - 07/02/2016");
        softAssert.assertEquals(games166.size(), 166, "Incorrect number of games in: 2016-09-01 - 2016-10-20");
        softAssert.assertEquals(games31.size(), 31, "Incorrect number of games in: 09/29/2016 - 10/02/2016");
        softAssert.assertEquals(games1030.size(), 1030, "Incorrect number of games in: 2016-02-01 - 2016-11-30");
        softAssert.assertEquals(games2192.size(), 2192, "Incorrect number of games in: 2015-01-01 - 2016-07-01");
        softAssert.assertEquals(games01.size(), 0, "Incorrect number of games in: 2016-06-30 - 2016-08-30");
        softAssert.assertEquals(games47.size(), 47, "Incorrect number of games in: 2016-09-01 - 2016-09-30");

        softAssert.assertAll();
    }

    @QTestCases(id = "42930")
    @Test(enabled = false)
    public void verifyDateRangeParticularTeamGames() {
        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("startDate", DATE_RANGE_START_DATE, "endDate", DATE_RANGE_END_DATE, "teamId", TEAM_ID));
        //LOGGER.info(String.format("Games of date range from '%s' to '%s', team: {ID: %s}, Stats API response: %s", DATE_RANGE_START_DATE, DATE_RANGE_END_DATE, TEAM_ID, statsSchedule.toString()));

        Schedule feedSchedule = nhlFeedContentService.getSchedule(
                ImmutableMap.of("startDate", DATE_RANGE_START_DATE, "endDate", DATE_RANGE_END_DATE, "teamId", TEAM_ID));
        //LOGGER.info(String.format("Games of date range from '%s' to '%s', team: {ID: %s}, Feed response: %s", DATE_RANGE_START_DATE, DATE_RANGE_END_DATE, TEAM_ID, statsSchedule.toString()));

        addAccentToMontrealCanadiensName(feedSchedule);

        assertSchedules(statsSchedule, feedSchedule);
    }

	/**
	 * TODO [normal] ask about sorting order in current day endpoint - it's
	 * incorrect there Related tickets: https://jira.mlbam.com/browse/SDAPINHL-711,
	 * SDAPINHL-697
	 */
    @QTestCases(id = "42931")
	@Test(enabled = false)
	public void verifyCurrentDayGames() {
		JsonNode statsScheduleNode = nhlStatsApiContentService.getSchedule(Collections.EMPTY_MAP, JsonNode.class);
		JsonNode feedScheduleNode = nhlFeedContentService.getSchedule(ImmutableMap.of("gameType", "1,2,3,4,8,6,7,9"), JsonNode.class);
		Schedule feedSchedule = nhlFeedContentService.getSchedule(ImmutableMap.of("gameType", "1,2,3,4,8,6,7,9"));
		// LOGGER.info(String.format("Games of current date, Feed response: %s", feedSchedule.toString()));
		addAccentToMontrealCanadiensName(feedSchedule);

		JsonNode statsScheduleGamePkNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(statsScheduleNode)
				.read("$..gamePk");
		JsonNode feedScheduleGameIdNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(feedScheduleNode)
				.read("$..gameId");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(statsScheduleGamePkNodes.size() > 0, "statsScheduleGamePkNodes = 0!");
		softAssert.assertTrue(feedScheduleGameIdNodes.size() > 0, "feedScheduleGameIdNodes = 0!");
		softAssert.assertTrue(statsScheduleGamePkNodes.size() == feedScheduleGameIdNodes.size(),
				"Stats API response is not equal to Feed response!");
		softAssert.assertAll();
	}

    /**
     * TODO [major] null pointer here - see mail message for Jan 21 2016
     */
    @Test(enabled = false)
    public void verifySeasonGames() {
        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(ImmutableMap.of("season", SEASON_2015_2016));
        // response contains more than thousand items - too huge log message
        //LOGGER.info(String.format("Games of season '%s', Stats API response: %s", SEASON_2015_2016, statsSchedule.toString()));

        Schedule feedSchedule = nhlFeedContentService.getSchedule(ImmutableMap.of("season", SEASON_2015_2016));
        //LOGGER.info(String.format("Games season '%s', Feed response: %s", SEASON_2015_2016, statsSchedule.toString()));

        assertSchedules(statsSchedule, feedSchedule);
    }

    @Test
    public void verifyFieldsFilter() {
        verifyFieldFilter(FIELDS_FILTER_1);
    }

    @Test()
    public void verifyScheduleExpandTeams() throws IOException, JSONException {
        verifyResponseWithJsonAssert(SCHEDULE_EXPAND_TEAM_RESPONSE_DATE_FILE,
                ImmutableMap.of("date", "04/01/2015", "expand", "schedule.teams"), null);
    }

    @Test()
    public void verifyScheduleExpandLineScore() throws IOException, JSONException {
        verifyResponseWithJsonAssert(SCHEDULE_EXPAND_LINESCORE_RESPONSE_DATE_FILE,
                ImmutableMap.of("date", "04/01/2015", "expand", "schedule.linescore"), null);
    }

    @Test()
    public void verifyScheduleExpandScoringPlaysAndPersons() throws IOException, JSONException {
        verifyResponseWithJsonAssert(SCHEDULE_EXPAND_SCORINGPLAYS_AND_PERSONS_RESPONSE_DATE_FILE,
                ImmutableMap.of("date", "04/01/2017", "expand", "schedule.scoringplays,scoringplays.person,person.names"), null);
    }

    /**
     * to ignore: "currentTeam", "primaryNumber", "active", "alternateCaptain", "rosterStatus"
     */
    @Test()
    public void verifyScheduleExpandDecisionsAndPersons() throws IOException, JSONException {
        verifyResponseWithJsonAssert(SCHEDULE_EXPAND_DECISIONS_AND_PERSONS_RESPONSE_DATE_FILE,
                ImmutableMap.of("date", "04/01/2015", "expand", "schedule.decisions,decisions.person,person.names"), null);
    }

    @QTestCases(id = "42951")
    @MethodOwner(owner = "cboyle")
    @Test()
    public void verifyScheduleExpandVenueHistorical() throws IOException, JSONException {
        verifyResponseWithJsonAssert(SCHEDULE_EXPAND_VENUE_RESPONSE_DATE_FILE_HISTORICAL,
                ImmutableMap.of("date", "04/01/2015", "expand", "schedule.venue"), null);
    }

    @QTestCases(id = "42952")
    @MethodOwner(owner = "cboyle")
    @Test()
    public void verifyScheduleExpandVenue() throws IOException, JSONException {
        verifyResponseWithJsonAssert(SCHEDULE_EXPAND_VENUE_RESPONSE_DATE_FILE,
                ImmutableMap.of("date", "04/01/2017", "expand", "schedule.venue"), null);
    }

	/**
	 * https://www.pivotaltracker.com/projects/1427108/stories/117188357
	 */
    @QTestCases(id = "42954")
	@Test()
	public void verifyScheduleExpandGameSeries() throws IOException, JSONException {
		verifyResponseWithJsonAssert(SCHEDULE_EXPAND_GAME_SERIES_DATE_FILE, ImmutableMap.of("expand",
				"schedule.game.seriesSummary,seriesSummary.series,series.round", "date", "04/19/2014"), null);
	}

    /**
     * TODO [normal] Will be added soon - verification method update is required
     */
    public void verifyFieldsFilter2() {
        verifyFieldFilter(FIELDS_FILTER_2);
    }

    /**
     * TODO [normal] Will be added soon - verification method update is required
     */
    public void verifyFieldsFilter3() {
        verifyFieldFilter(FIELDS_FILTER_3);
    }

    /**
     * sometimes "null" response in reports
     */
    @Test
    public void verifyScheduleMetadataNode() {
        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("date", "01/06/2016", "expand", "schedule.metadata"));

        for (Game game : statsSchedule.getGameList()) {
            if (game.getMetaData().isManuallyScored() || game.getMetaData().isSplitSquad()) {
                Assert.fail(String.format("Incorrect metadata values: isManuallyScored - %s, isSplitSquad - %s",
                        game.getMetaData().isManuallyScored(), game.getMetaData().isSplitSquad()));
            }
        }
    }

    @Test
    public void verifyDatesArrayIsEmptyWhenNoGames() throws IOException, JSONException {
        verifyResponseWithJsonAssert(SCHEDULE_NO_GAMES_DATE_DATA_FILE, ImmutableMap.of("date", "2016-09-02",
                "expand", "schedule.linescore,schedule.teams,schedule.game.content.media.epg", "platform", "roku", "site", "site=en_nhl"),
                null, JSONCompareMode.STRICT);
    }

    /**
     * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-3066
     */
    @QTestCases(id = "42935")
    @Test
    public void verifyRadioBroadcastsInformation() {

        String scheduleResponseWithRadioBroadcasts = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2017-10-20", "hydrate", "radioBroadcasts"), String.class);
        String scheduleResponseWithoutRadioBroadcasts = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2016-10-20"), String.class);

        List<String> radioBroadcastsName = JsonPath.read(scheduleResponseWithRadioBroadcasts, "$..radioBroadcasts..name");
        List<String> radioBroadcastsType = JsonPath.read(scheduleResponseWithRadioBroadcasts, "$..radioBroadcasts..type");

        List<String> emptyRadioBroadcasts = JsonPath.read(scheduleResponseWithoutRadioBroadcasts, "$..radioBroadcasts[*]");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(radioBroadcastsName.size(), 11, String.format("Expected radioBroadcasts name field to have size of 11 %s", radioBroadcastsName.size()));
        softAssert.assertEquals(radioBroadcastsType.size(), 11, String.format("Expected radioBroadcasts type field to have size of 11 %s", radioBroadcastsName.size()));
        softAssert.assertEquals(emptyRadioBroadcasts.size(), 0, "'radioBroadcasts' is present, but should not be");

        softAssert.assertAll();
    }

    @QTestCases(id = "42936")
    @Test
    public void verifyScheduleCanTakeMultipleSeasons() {
        String scheduleResponse = nhlStatsApiContentService.getSchedule(ImmutableMap.of("teamId", "2", "season", "20152016,20162017"), String.class);

        List<String> seasons = JsonPath.read(scheduleResponse, "$..season");

        Assert.assertTrue(seasons.contains("20152016") && seasons.contains("20162017"), "Cannot find either '20152016' or '20162017' in response");
    }

    @QTestCases(id = "42937")
    @Test
    public void verifyHydratedTeamRosterSeason() {
        String rawResponseActual = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("date", "2001-12-01", "hydrate", "team(roster)"), String.class);
        String rawResponseExpected = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "20012002"), String.class, "3/roster");

        JsonNode rosterActual = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual).
                read("$..games[?(@.gamePk == '2001020384')]..away..roster.roster");
        JsonNode rosterExpected = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseExpected).
                read("$..roster");

        Assert.assertEquals(rosterActual, rosterExpected, "Hydrated team roster doesn't reflect the date correctly");
    }

    @QTestCases(id = "42938")
    @Test
    public void verifyHydratedTeamStatsSeason() {
        String rawResponseActual = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("date", "2001-12-01", "hydrate", "team(stats(splits=statsSingleSeason))"), String.class);
        String rawResponseExpected = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "20012002"), String.class, "3/stats");

        JsonNode statsActual = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual).
                read("$..games[?(@.gamePk == '2001020384')]..away..stat");
        JsonNode statsExpected = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseExpected).
                read("$..stat");

        Assert.assertEquals(statsActual, statsExpected, "Hydrated team stats don't reflect the date correctly");
    }

    @QTestCases(id = "42939")
    @Test(description = "JIRA# = QAA-1474", enabled = false)
    public void verifyEventStatusHydrate() {
        String rawResponse1 = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("startDate", "10/1/2016", "endDate", "11/1/2016", "hydrate", "event.status"),
                String.class, "events");

        String rawResponse2 = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("eventIds", "601476,601477", "hydrate", "event.status"),
                String.class, "events");

        String jsonPathExpression = "$..eventStatus[?(@.abstractGameState && @.codedGameState && @.detailedState && @.statusCode)]";

        JsonNode stats1 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1).read(jsonPathExpression);
        JsonNode stats2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2).read(jsonPathExpression);

        SoftAssert softAssert = new SoftAssert();

        String errorMessage = "Statuses are absent for all or several events";

        softAssert.assertEquals(stats1.size(), 408, errorMessage);
        softAssert.assertEquals(stats2.size(), 2, errorMessage);

        softAssert.assertAll();
    }

    /**
     * TODO add additional verifications from JIRA ticket
     */
    @QTestCases(id = "42940")
    @Test
    public void verifyRosterSeasonForGamePkParameter() {
        JsonNode rawResponseActual = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("gamePk", "2013020111", "hydrate", "team(roster)"), JsonNode.class);
        JsonNode rawResponseExpected = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "20132014"), JsonNode.class, "22/roster");

        JsonNode rosterActual = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual).
                read("$..team[?(@.id == 22)]..roster.roster");
        JsonNode rosterExpected = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseExpected).
                read("$..roster");

        Assert.assertEquals(rosterActual, rosterExpected, "Rosters are not the same");
    }

    /**
     * SDMLS-34 - Clarify whether it should be verified against NHL or Arena
     */
    @QTestCases(id = "42942")
    @Test
    public void verifyBroadcastsHydrate() {
        String rawResponse = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("startDate", "10/1/2016", "endDate", "11/1/2016", "hydrate", "broadcasts"), String.class);

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<String> id = JsonPath.read(document, "$..broadcasts[*].id");
        List<String> name = JsonPath.read(document, "$..broadcasts[*].name");
        List<String> type = JsonPath.read(document, "$..broadcasts[*].type");
        List<String> site = JsonPath.read(document, "$..broadcasts[*].site");
        List<String> language = JsonPath.read(document, "$..broadcasts[*].language");

        List<String> broadcasts = JsonPath.read(document, "$..broadcasts[*]");

        int expectedBroadcastsValue = broadcasts.size();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(id.size(), expectedBroadcastsValue,
                String.format("Unexpected amount of broadcasts ids: %s, expected: %s", id.size(), expectedBroadcastsValue));
        softAssert.assertEquals(name.size(), expectedBroadcastsValue,
                String.format("Unexpected amount of broadcasts names: %s, expected: %s", name.size(), expectedBroadcastsValue));
        softAssert.assertEquals(type.size(), expectedBroadcastsValue,
                String.format("Unexpected amount of broadcasts types: %s, expected: %s", type.size(), expectedBroadcastsValue));
        softAssert.assertEquals(site.size(), expectedBroadcastsValue,
                String.format("Unexpected amount of broadcasts sites: %s, expected: %s", site.size(), expectedBroadcastsValue));
        softAssert.assertEquals(language.size(), expectedBroadcastsValue,
                String.format("Unexpected amount of broadcasts languages: %s, expected: %s", language.size(), expectedBroadcastsValue));

        softAssert.assertAll();
    }

    /**
     * New modified Jira: https://jira.bamtechmedia.com/browse/QAA-2986
     */
    @QTestCases(id = "42942")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyGameStatus() {
        String rawResponse = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2016-10-06"), String.class);
        String gameStatus = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + GAME_STATUS, String.class).getBody().toString();

        List<String> detailStatuses = (ArrayList) JsonPath.read(rawResponse, "$..games[?(@.gamePk=='2016010089')].status.detailedState");
        List<String> validResponses = (ArrayList) JsonPath.read(gameStatus, "$..detailedState");

        LOGGER.info("Checking Detailed Status: " + detailStatuses);
        LOGGER.info("Checking Valid Responses: " + validResponses);

        Assert.assertTrue(validResponses.contains(detailStatuses.get(0)), String.format("Expected valid status responses to match 'detailState' node: %s Actual: %s", validResponses, detailStatuses.get(0)));
    }

    /**
     * TODO perform more deep validation: verify schema for each of nodes: linescore, decisions, scoringplays
     */
    @QTestCases(id = "42943")
    @Test
    public void verifyLineScoreDecisionsScoringPlaysExpandsByDateRange() {
    	JsonNode rawResponseLinescore = nhlStatsApiContentService.getSchedule(ImmutableMap.of(
                "teamId", "24", "gameType", "PR,R,P,WCOH_EXH,WCOH_PRELIM,WCOH_FINAL", "startDate", "2014-10-08", "endDate", "2017-04-17",
                "expand", "schedule.teams,schedule.linescore"),
    			JsonNode.class);

    	JsonNode rawResponseDecisions = nhlStatsApiContentService.getSchedule(ImmutableMap.of(
                "teamId", "24", "gameType", "PR,R,P,WCOH_EXH,WCOH_PRELIM,WCOH_FINAL", "startDate", "2014-10-08", "endDate", "2017-04-17",
                "expand", "schedule.teams,schedule.decisions"),
    			JsonNode.class);

    	JsonNode rawResponseScoringPlays = nhlStatsApiContentService.getSchedule(ImmutableMap.of(
                "teamId", "24", "gameType", "PR,R,P,WCOH_EXH,WCOH_PRELIM,WCOH_FINAL", "startDate", "2014-10-08", "endDate", "2017-04-17",
                "expand", "schedule.teams,schedule.scoringplays"),
    			JsonNode.class);

        verifyLineScoreDecisionsScoringPlaysExpand(rawResponseLinescore, rawResponseDecisions, rawResponseScoringPlays);
    }

    /**
     * TODO perform more deep validation: verify schema for each of nodes: linescore, decisions, scoringplays
     */
    @QTestCases(id = "42944")
    @Test
    public void verifyLineScoreDecisionsScoringPlaysExpandsBySeason() {
    	JsonNode rawResponseLinescore = nhlStatsApiContentService.getSchedule(ImmutableMap.of(
                "teamId", "24", "seasons", "20162017", "expand", "schedule.teams,schedule.linescore"),
    			JsonNode.class);

    	JsonNode rawResponseDecisions = nhlStatsApiContentService.getSchedule(ImmutableMap.of(
                "teamId", "24", "seasons", "20152016,20162017", "expand", "schedule.teams,schedule.decisions"),
    			JsonNode.class);

    	JsonNode rawResponseScoringPlays = nhlStatsApiContentService.getSchedule(ImmutableMap.of(
                "teamId", "24", "seasons", "20152016,20162017", "expand", "schedule.teams,schedule.scoringplays"),
    			JsonNode.class);

        verifyLineScoreDecisionsScoringPlaysExpand(rawResponseLinescore, rawResponseDecisions, rawResponseScoringPlays);
    }

    @DataProvider(name = "invalidNhlRequests")
    public Object[][] invalidNhlRequestsDataProvider() {
        return new Object[][]{
                {"TUID: performers", "/api/v1/performers/5171"},
                //{"TUID: venues", "/api/v1/venues"} currently a valid URL
        };
    }

    @Test(dataProvider = "invalidNhlRequests", expectedExceptions = HttpServerErrorException.class, expectedExceptionsMessageRegExp = "500 Internal Server Error")
    public void verifyErrorsWhenArenaDataRequestedFromNhl(String TUID, String request) {
        restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, String.class);
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-817
     */
    @QTestCases(id = "42946")
    @Test
    public void verifyMetadataDescriptionProperty() {
        String rawResponse = nhlStatsApiContentService.getSchedule(ImmutableMap.of("gamePk", "2016020559", "hydrate", "metadata"), String.class);
        List<String> description = JsonPath.read(rawResponse, "$..metadata.description");
        Assert.assertEquals(description.get(0), "Centennial Classic");
    }

    protected void verifyFieldFilter(String fieldsFilter) {
        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("date", ONE_DAY_DATE, "fields", fieldsFilter));

        Boolean validFilterResponse = true;

        for (Game game : statsSchedule.getGameList()) {
            if (!(game.getHomeTeam() == null && game.getAwayTeam() == null && game.getGameId() != null && game.getDate() != null)) {
                validFilterResponse = false;
            }
        }

        Assert.assertTrue(validFilterResponse, String.format("Fields filter '%s' is not working properly", fieldsFilter));
    }

    /*
     * JIRA Ticket: SDAPINHL-1125 - https://jira.mlbam.com/browse/SDAPINHL-1125 - QAA-3464
     * */
    @QTestCases(id = "42953")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyDataForMinnesotaWild(){
        JsonNode rawGameData = nhlStatsApiContentService.getSchedule(ImmutableMap.of("startDate", "04/10/2017", "endDate", "04/25/2017"), JsonNode.class, "games");
        JsonNode rawEventData = nhlStatsApiContentService.getSchedule(ImmutableMap.of("startDate", "04/10/2017", "endDate", "04/25/2017"), JsonNode.class, "events");

        JsonNode minnesotaWildData = JsonPath.using(jsonPathJacksonConfiguration).parse(rawGameData).read("$..teams..team[?(@.name == 'Minnesota Wild')]");
        JsonNode eventNameData = JsonPath.using(jsonPathJacksonConfiguration).parse(rawEventData).read("$..events[*].name");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(minnesotaWildData.size() > 0, String.format("Expected data for Minnesota Wild to be returned: %s", minnesotaWildData));
        softAssert.assertTrue(!eventNameData.isMissingNode(), String.format("Expected data for name node under event: %s", eventNameData));

        softAssert.assertAll();
    }

     /**
      * JIRA Ticket: SDAPINHL-1046 - hydrating the linescore does not work with timecode correctly
      */
    @Test
    public void verifyHydratingLinescoreDoesNotWorkWithTimecodeCorrectly(){
        String rawResponse1 = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2017-02-28", "timecode", "20170301_013842", "hydrate", "linescore"), String.class);
        List<String> hydratingLineScore = JsonPath.read(rawResponse1, "$..abstractGameState");
        Assert.assertTrue(!hydratingLineScore.contains("Final"), "Game is not returned in the Final state");
    }

    @QTestCases(id = "42948")
	@Test
	public void verifyScheduleTeamHydrate() {
		JsonNode rawResponse = nhlStatsApiContentService.getSchedule(ImmutableMap.of("date", "2001-12-31", "hydrate",
				"team(leaders(categories=[gaa,assists,goals],gameTypes=R))"), JsonNode.class);

		JsonNode leaderCategoryGaa = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..teamLeaders[?(@.leaderCategory == 'gaa')]");
		JsonNode leaderCategoryAssists = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..teamLeaders[?(@.leaderCategory == 'assists')]");
		JsonNode leaderCategoryGoals = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..teamLeaders[?(@.leaderCategory == 'goals')]");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(leaderCategoryGaa.size() == 20,
				"Incorrect number of teamLeaders with leaderCategory: 'gaa'!");
		softAssert.assertTrue(leaderCategoryAssists.size() == 20,
				"Incorrect number of teamLeaders with leaderCategory: 'assists'!");
		softAssert.assertTrue(leaderCategoryGoals.size() == 20,
				"Incorrect number of teamLeaders with leaderCategory: 'goals'!");
		softAssert.assertAll();
	}

    @QTestCases(id = "42949")
	@Test(enabled = false)
	public void verifyHydrateEventForGameInformation() throws IOException, JSONException, ProcessingException {
		String rawResponse1 = nhlStatsApiContentService.getSchedule(
				ImmutableMap.of("eventIds", "1010001621", "hydrate", "event(game)"), String.class, "events");
		String rawResponse2 = nhlStatsApiContentService
				.getSchedule(ImmutableMap.of("date", "2016-10-28", "hydrate", "event(game)"), String.class, "events");

		String jsonSchema = fileUtils.getResourceFileAsString("nhl/api/schedule/schema/game_schema.json");
		String responseSnapshot1 = fileUtils
				.getResourceFileAsString("nhl/api/schedule/schedule_hydrate_game_eventIds_1010001621.json");
		String responseSnapshot2 = fileUtils
				.getResourceFileAsString("nhl/api/schedule/schedule_hydrate_game_date_2016-10-28.json");

		SoftAssert softAssert = new SoftAssert();
		jsonValidator.validateJsonAgainstSchema(jsonSchema, rawResponse1, "games", softAssert);
		jsonValidator.validateJsonAgainstSchema(jsonSchema, rawResponse2, "games", softAssert);
		softAssert.assertAll();

		JSONAssert.assertEquals(responseSnapshot1, rawResponse1, JSONCompareMode.STRICT);
		JSONAssert.assertEquals(responseSnapshot2, rawResponse2, JSONCompareMode.STRICT);
	}

	/**
	 * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1061
	 */
    @QTestCases(id = "42950")
	@Test()
	public void verifyHydrateAndExpandVenue() throws JSONException {

		String rawResponse1 = nhlStatsApiContentService.getSchedule(ImmutableMap.of("hydrate", "venue"), String.class);
		String rawResponse2 = nhlStatsApiContentService.getSchedule(ImmutableMap.of("expand", "schedule.venue"),
				String.class);

		JSONAssert.assertEquals(rawResponse1, rawResponse2, JSONCompareMode.STRICT);
	}
	
	/**
	 * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1115
	 */
    @QTestCases(id = "42955")
	@Test()
	public void verifySeasonParameterFromStartEndDate() {
		JsonNode rawResponse = restTemplate.getForEntity(
				NhlParameters.getNhlStatsApiHost()
						+ "/api/v1/schedule?teamId=3&gameType=PR,R,P,WCOH_EXH,WCOH_PRELIM,WCOH_FINAL,A&startDate=2016-09-08&endDate=2017-06-20&leaderGameTypes=R&expand=schedule.teams,schedule.linescore,schedule.metadata",
				JsonNode.class).getBody();
		JsonNode datesNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..dates[*]");

		if (datesNodes.size() > 0) {
			for (JsonNode date : datesNodes) {
				JsonNode seasonNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(date)
						.read("$..[?(@.season == '20162017')]");
				Assert.assertTrue(seasonNodes.size() > 0, "Season is not '20162017'!");
			}
		} else {
			Assert.fail("dates nodes are not present!");
		}
	}
	
	/**
	 * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1278
	 */
    @QTestCases(id = "42956")
	@Test()
	public void verifyPreseasonGamesInScheduleRequestAcrossSeasons() {
		JsonNode rawResponse = nhlStatsApiContentService.getSchedule(ImmutableMap.of("gameType",
				"PR,R,P,WCOH_EXH,WCOH_PRELIM,WCOH_FINAL,A", "startDate", "2015-10-07", "endDate", "2018-04-17"),
				JsonNode.class);

		JsonNode gameTypeNodes_PR = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.gameType == 'PR')]");
		JsonNode gameTypeNodes_R = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.gameType == 'R')]");
		JsonNode gameTypeNodes_P = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.gameType == 'P')]");
		JsonNode gameTypeNodes_WCOH_EXH = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.gameType == 'WCOH_EXH')]");
		JsonNode gameTypeNodes_WCOH_PRELIM = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.gameType == 'WCOH_PRELIM')]");
		JsonNode gameTypeNodes_WCOH_FINAL = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.gameType == 'WCOH_FINAL')]");
		JsonNode gameTypeNodes_A = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.gameType == 'A')]");
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(gameTypeNodes_PR.size() > 0, "Games with gameType: 'PR' are not returned!");
		softAssert.assertTrue(gameTypeNodes_R.size() > 0, "Games with gameType: 'R' are not returned!");
		softAssert.assertTrue(gameTypeNodes_P.size() > 0, "Games with gameType: 'P' are not returned!");
		softAssert.assertTrue(gameTypeNodes_WCOH_EXH.size() > 0, "Games with gameType: 'WCOH_EXH' are not returned!");
		softAssert.assertTrue(gameTypeNodes_WCOH_PRELIM.size() > 0, "Games with gameType: 'WCOH_PRELIM' are not returned!");
		softAssert.assertTrue(gameTypeNodes_WCOH_FINAL.size() > 0, "Games with gameType: 'WCOH_FINAL' are not returned!");
		softAssert.assertTrue(gameTypeNodes_A.size() > 0, "Games with gameType: 'A' are not returned!");
		
		verifyGamesSeasons(gameTypeNodes_WCOH_EXH, "20162017", softAssert);
		verifyGamesSeasons(gameTypeNodes_WCOH_PRELIM, "20162017", softAssert);
		verifyGamesSeasons(gameTypeNodes_WCOH_FINAL, "20162017", softAssert);

		softAssert.assertAll();
	}

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-990
     */
    @QTestCases(id = "42957")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyGameTypeA(){
        JsonNode rawResponse = nhlStatsApiContentService.getSchedule(ImmutableMap.of("startDate", "2012-01-28", "endDate", "2012-02-11", "site", "en_nhl",
                "expand", "schedule.teams,schedule.venue,schedule.metadata,schedule.linescore,schedule.decisions,schedule.game.content.media.epg"), JsonNode.class);

        JsonNode gameType = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..games..gameType");

        Assert.assertTrue(gameType.toString().contains("A"), String.format("Expected 'All-Star Games' to populate: %s", gameType.get(0)));
    }

	
	protected void verifyGamesSeasons(JsonNode jsonNode, String season, SoftAssert softAssert) {
		if (jsonNode.size() > 0) {
			for (JsonNode game : jsonNode) {
				JsonNode seasonNode = JsonPath.using(jsonPathJacksonConfiguration).parse(game)
						.read("$..[?(@.season== '" + season + "')]");
				softAssert.assertTrue(seasonNode.size() > 0, "game is not part of the '" + season + "' season!");
			}
		} else {
			softAssert.fail("Game list is empty!");
		}
	}

    protected void assertSchedules(Schedule statsSchedule, Schedule feedSchedule) {
        Assert.assertTrue(statsSchedule.equals(feedSchedule),
                String.format("Stats API response is not equal to Feed response.\nStats API: %s\nFeed: %s",
                        statsSchedule.toString(), feedSchedule.toString()));
    }

    protected void verifyResponseWithJsonAssert(String responseDataFile, Map parameters, String pathSegment, JSONCompareMode jsonCompareMode) throws IOException, JSONException {
        JSONComparator jsonCustomIgnoreComparator =
                new JsonIgnoreComparator(jsonCompareMode,
                        Lists.newArrayList("currentAge", "currentTeam", "weight", "alternateCaptain", "rosterStatus", "primaryNumber", "primaryPosition", "copyright"),
                        Lists.newArrayList("PPG PAINTS Arena"),
                        Lists.newArrayList("Final"));

        String expectedResponse = fileUtils.getResourceFileAsString(responseDataFile);
        String actualResponse = nhlStatsApiContentService.getSchedule(parameters, String.class, pathSegment);

        JSONAssert.assertEquals(expectedResponse, actualResponse, jsonCustomIgnoreComparator);
    }

    protected void verifyResponseWithJsonAssert(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        verifyResponseWithJsonAssert(responseDataFile, parameters, pathSegment, JSONCompareMode.LENIENT);
    }

    protected void addAccentToMontrealCanadiensName(Schedule schedule) {
        // Stats API decided to use english spelling with "é"
        for (Game game : schedule.getGameList()) {
            if ("Montreal Canadiens".equals(game.getHomeTeam().getName())) {
                game.getHomeTeam().setName("Montréal Canadiens");
            }
            if ("Montreal Canadiens".equals(game.getAwayTeam().getName())) {
                game.getAwayTeam().setName("Montréal Canadiens");
            }

        }
    }

	protected void verifyLineScoreDecisionsScoringPlaysExpand(JsonNode rawResponseLinescore,
			JsonNode rawResponseDecisions, JsonNode rawResponseScoringPlays) {

		JsonNode gamePksLinescore = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseLinescore)
				.read("$..gamePk");
		JsonNode lineScores = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseLinescore)
				.read("$..linescore");
		JsonNode gamePksDecisions = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseDecisions)
				.read("$..gamePk");
		JsonNode decisions = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseDecisions)
				.read("$..decisions");
		JsonNode gamePksScoringPlays = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseScoringPlays)
				.read("$..gamePk");
		JsonNode scoringPlays = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseScoringPlays)
				.read("$..scoringPlays");

		SoftAssert softAssert = new SoftAssert();

		softAssert.assertEquals(lineScores.size(), gamePksLinescore.size(),
				String.format("Amount of linescores is not as expected: expected: %s, actual: %s",
						gamePksLinescore.size(), lineScores.size()));
		softAssert.assertEquals(decisions.size(), gamePksDecisions.size(),
				String.format("Amount of decisions is not as expected: expected: %s, actual: %s",
						gamePksDecisions.size(), decisions.size()));
		softAssert.assertEquals(scoringPlays.size(), gamePksScoringPlays.size(),
				String.format("Amount of scoringplays is not as expected: expected: %s, actual: %s",
						gamePksScoringPlays.size(), scoringPlays.size()));

		softAssert.assertAll();
	}

    protected JsonNode getGamesOfDateRange(Map parameters) {
        JsonNode rawResponseActual = nhlStatsApiContentService.getSchedule(parameters, JsonNode.class);
        return JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual).read("$..games[*]");
    }
}
