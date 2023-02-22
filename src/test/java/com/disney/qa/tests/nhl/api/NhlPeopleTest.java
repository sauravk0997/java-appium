package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
 * Created by mk on 1/26/16.
 */
public class NhlPeopleTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String PEOPLE_RETIRED_SKATER_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_skater_id_8466378_season_20142015.json";

    public static final String PEOPLE_RETIRED_SKATER_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_skater_id_8466378_playoff_20142015.json";

    public static final String PEOPLE_RETIRED_GOALIE_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_goalie_id_8455710_season_20142015.json";

    public static final String PEOPLE_RETIRED_GOALIE_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_goalie_id_8455710_playoff_20152016.json";

    public static final String PEOPLE_ACTIVE_SKATER_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_skater_id_8465058_season_20142015.json";

    public static final String PEOPLE_ACTIVE_SKATER_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_skater_id_8465058_playoff_20142015.json";

    public static final String PEOPLE_ACTIVE_GOALIE_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_goalie_id_8470645_season_20142015.json";

    public static final String PEOPLE_ACTIVE_GOALIE_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE = "nhl/api/people/people_goalie_id_8470645_playoff_20142015.json";

    public static final String PEOPLE_WIN_LOSS_TIES_SEASON_20142015_1_DATA_FILE = "nhl/api/people/people_win_loss_ties_season_20142015_1.json";

    public static final String PEOPLE_WIN_LOSS_TIES_SEASON_20142015_2_DATA_FILE = "nhl/api/people/people_win_loss_ties_season_20142015_2.json";

    public static final String TEAM_EXPAND_SINGLE_SEASON_DATA_FILE = "nhl/api/teams/teams_expand_season_20142015.json";

    public static final String TEAM_EXPAND_PLAYOFF_DATA_FILE = "nhl/api/teams/teams_expand_playoff_20142015.json";

    public static final String PEOPLE_NUMERIC_VALUES_DATA_FILE = "nhl/api/people/people_numeric_values_id_8470645.json";

    public static final String PEOPLE_STRING_VALUES_DATA_FILE = "nhl/api/people/people_string_values_id_8470645.json";

    public static final String PEOPLE_REGULAR_SEASON_STAT_RANKINGS_FILE = "nhl/api/people/people_regular_season_stat_rankings.json";

    public static final String PEOPLE_PLAYOFF_STAT_RANKINGS_FILE = "nhl/api/people/people_playoff_stat_rankings.json";

    public static final String PEOPLE_YEAR_BY_YEAR_PLAYOFFS_FILE = "nhl/api/people/people_year_by_year_playoffs.json";

    public static final String PEOPLE_YEAR_BY_YEAR_FILE = "nhl/api/people/people_year_by_year.json";

    public static final String RETIRED_SKATER_ID = "8466378";

    public static final String RETIRED_GOALIE_ID = "8455710";

    public static final String ACTIVE_SKATER_ID = "8465058"; // 'Chicago Blackhawks', play off 2014-2015

    public static final String ACTIVE_GOALIE_ID = "8470645"; // 'Chicago Blackhawks', play off 2014-2015

    public static final String WIN_LOSS_TIES_UPDATED_PLAYER = "8473461";

    public static final String DRAFT_PICKS = "nhl/api/people/people_draft_2004.json";

    /**
     * team tricode was removed
     */
	@Test(enabled = false)
	public void verifyRetiredSkaterAllRegularSeasonStatsOptions() throws IOException, JSONException {
		verifyPeopleResponseWithJsonAssert(PEOPLE_RETIRED_SKATER_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE,
				ImmutableMap.of("season", "20142015", "expand", "person.stats,person.social", "stats",
						"yearByYear,careerRegularSeason,gameLog,homeAndAway,winLoss,vsTeam,vsDivision,vsConference,onPaceRegularSeason,byMonth,byDayOfWeek,regularSeasonStatRankings,goalsByGameSituation,statsSingleSeason"),
				RETIRED_SKATER_ID);
	}

    /**
     * possible issues: additional "season": "20072008" in QA, DEV
     */
    @Test()
    public void verifyRetiredSkaterAllPlayoffStatOptions() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert(PEOPLE_RETIRED_SKATER_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "person.stats,person.social",
                        "stats", "yearByYearPlayoffs,careerPlayoffs,playoffGameLog,homeAndAwayPlayoffs,winLossPlayoffs,vsTeamPlayoffs,vsDivisionPlayoffs,vsConferencePlayoffs,byMonthPlayoffs,byDayOfWeekPlayoffs,goalsByGameSituationPlayoffs,statsSingleSeasonPlayoffs"),
                RETIRED_SKATER_ID);
    }

    /**
     * 1. Additional row "19971998" (olympic games) is in expected result for QA, DEV. Issue was closed as no longer valid: QAA-1186
     * 2. possible issue: "goalAgainstAverage": 2.241587 (DEV,QA) vs 2.24159 (PROD)
     */
    @Test
    public void verifyRetiredGoalieAllRegularSeasonStatsOptions() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert(PEOPLE_RETIRED_GOALIE_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "person.stats,person.social",
                        "stats", "yearByYear,careerRegularSeason,gameLog,homeAndAway,winLoss,vsTeam,vsDivision,vsConference,onPaceRegularSeason,byMonth,byDayOfWeek,regularSeasonStatRankings,goalsByGameSituation,statsSingleSeason"),
                RETIRED_GOALIE_ID);
    }

    /**
     * following issue is possible: "goalAgainstAverage": 2.019341 vs  "goalAgainstAverage": 2.01934
     * TODO: 12/15/2016 "ot" value is different in QA and PROD: in QA all values are set to 0
     */
    @Test
    public void verifyRetiredGoalieAllPlayoffStatOptions() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert(PEOPLE_RETIRED_GOALIE_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "person.stats,person.social",
                        "stats", "yearByYearPlayoffs,careerPlayoffs,playoffGameLog,homeAndAwayPlayoffs,winLossPlayoffs,vsTeamPlayoffs,vsDivisionPlayoffs,vsConferencePlayoffs,byMonthPlayoffs,byDayOfWeekPlayoffs,goalsByGameSituationPlayoffs,statsSingleSeasonPlayoffs"),
                RETIRED_GOALIE_ID);
    }

    /**
     * This test will fail in case of player will change the team.
     */
    @Test
    public void verifyActiveSkaterAllRegularSeasonStatsOptions() throws IOException, JSONException {
        verifyPeopleResponseActivePlayerWithJsonAssert(PEOPLE_ACTIVE_SKATER_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "person.stats,person.social",
                        "stats", "yearByYear,careerRegularSeason,gameLog,homeAndAway,winLoss,vsTeam,vsDivision,vsConference,onPaceRegularSeason,byMonth,byDayOfWeek,regularSeasonStatRankings,goalsByGameSituation,statsSingleSeason"),
                ACTIVE_SKATER_ID);
    }

    @Test
    public void verifyActiveSkaterAllPlayoffStatOptions() throws IOException, JSONException {
        verifyPeopleResponseActivePlayerWithJsonAssert(PEOPLE_ACTIVE_SKATER_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "person.stats,person.social",
                        "stats", "yearByYearPlayoffs,careerPlayoffs,playoffGameLog,homeAndAwayPlayoffs,winLossPlayoffs,vsTeamPlayoffs,vsDivisionPlayoffs,vsConferencePlayoffs,byMonthPlayoffs,byDayOfWeekPlayoffs,goalsByGameSituationPlayoffs,statsSingleSeasonPlayoffs"),
                ACTIVE_SKATER_ID);
    }

    @Test
    public void verifyActiveGoalieAllRegularSeasonStatsOptions() throws IOException, JSONException {
        verifyPeopleResponseActivePlayerWithJsonAssert(PEOPLE_ACTIVE_GOALIE_ALL_REGULAR_SEASON_STATS_OPTIONS_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "person.stats,person.social",
                        "stats", "yearByYear,careerRegularSeason,gameLog,homeAndAway,winLoss,vsTeam,vsDivision,vsConference,onPaceRegularSeason,byMonth,byDayOfWeek,regularSeasonStatRankings,goalsByGameSituation,statsSingleSeason"),
                ACTIVE_GOALIE_ID);
    }

    @Test
    public void verifyActiveGoalieAllPlayoffStatOptions() throws IOException, JSONException {
        verifyPeopleResponseActivePlayerWithJsonAssert(PEOPLE_ACTIVE_GOALIE_ALL_PLAYOFF_STATS_OPTIONS_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "person.stats,person.social",
                        "stats", "yearByYearPlayoffs,careerPlayoffs,playoffGameLog,homeAndAwayPlayoffs,winLossPlayoffs,vsTeamPlayoffs,vsDivisionPlayoffs,vsConferencePlayoffs,byMonthPlayoffs,byDayOfWeekPlayoffs,goalsByGameSituationPlayoffs,statsSingleSeasonPlayoffs"),
                ACTIVE_GOALIE_ID);
    }

    /**
     * 1. "jerseyNumber" fields appeared, but not in JSON file yet
     * 2. "currentTeam", "active", "captain" probably also should be ignored
     */
    @Test
    public void verifyRosterExpandsSingleSeason() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAM_EXPAND_SINGLE_SEASON_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "team.roster,roster.person,person.stats,team.social,person.social",
                        "stats", "statsSingleSeason"),
                "2");
    }

    /**
     * 1. "jerseyNumber" fields appeared, but not in JSON file yet
     * 2. "currentTeam", "active", "captain" probably also should be ignored
     */
    @Test
    public void verifyRosterExpandsPlayOff() throws IOException, JSONException {
        verifyTeamsResponseWithJsonAssert(TEAM_EXPAND_PLAYOFF_DATA_FILE,
                ImmutableMap.of(
                        "season", "20142015",
                        "expand", "team.roster,roster.person,person.stats,team.social,person.social",
                        "stats", "statsSingleSeasonPlayoffs"),
                "2");
    }

    @Test
    public void verifyWinLossTiesForGoalieByMonthByDayOfWeek() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert(PEOPLE_WIN_LOSS_TIES_SEASON_20142015_1_DATA_FILE,
                ImmutableMap.of("stats", "byMonth,byDayOfWeek", "season", "20142015"),
                String.format("%s/stats", WIN_LOSS_TIES_UPDATED_PLAYER));
    }

    @Test
    public void verifyWinLossTiesForGoalieVsConferenceVsDivision() throws IOException, JSONException {
        // snapshot against prod here
        verifyPeopleResponseWithJsonAssert(PEOPLE_WIN_LOSS_TIES_SEASON_20142015_2_DATA_FILE,
                ImmutableMap.of("stats", "vsConference,vsDivision", "season", "20142015"),
                String.format("%s/stats", WIN_LOSS_TIES_UPDATED_PLAYER));
    }

    /**
     * possible issues: "goalAgainstAverage" ... see above
     */
    @QTestCases(id = "42910")
    @Test(description = "JIRA# QAA-1450", enabled = false)
    public void verifyStatsNumericValues() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert(PEOPLE_NUMERIC_VALUES_DATA_FILE,
                ImmutableMap.of("season", "20152016", "expand", "person.stats,person.social", "stats", "careerRegularSeason"),
                ACTIVE_GOALIE_ID);
    }

    @QTestCases(id = "42911")
    @Test(description = "JIRA# QAA-1450", enabled = false)
    public void verifyStatsStringValues() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert(PEOPLE_STRING_VALUES_DATA_FILE,
                ImmutableMap.of("season", "20152016", "expand", "person.stats,person.social", "stats", "careerRegularSeason",
                        "statsAsStrings", "True"),
                ACTIVE_GOALIE_ID);
    }

    @QTestCases(id = "42912")
    @Test(description = "JIRA# QAA-1057", enabled = false)
    public void verifyStatsStringValuesWithHeader() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssertWithHeaders(PEOPLE_STRING_VALUES_DATA_FILE,
                ImmutableMap.of("X-statsAsStrings", "True"),
                ImmutableMap.of("season", "20152016", "expand", "person.stats,person.social", "stats", "careerRegularSeason"),
                ACTIVE_GOALIE_ID);
    }

    protected void verifyPeopleResponseWithJsonAssert(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssertWithHeaders(responseDataFile, null, parameters, pathSegment);
    }

    @QTestCases(id = "42905")
    @Test
    public void verifyPlayerDraftInformationWithPersonIds() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert("nhl/api/people/people_with_draft.json",
                ImmutableMap.of("personIds", "8455710,8476409,8479664", "hydrate", "draft"), null);
    }

    @QTestCases(id = "42907")
    @Test
    public void verifyPlayerDraftInformationWithTeamHydrateWithPersonsIds() throws IOException, JSONException {
        verifyPeopleResponseWithJsonAssert("nhl/api/people/people_with_draft_team_hydrate.json",
                ImmutableMap.of("personIds", "8455710,8476409,8479664", "hydrate", "draft(team)"), null);
    }

    @QTestCases(id = "42908")
    @Test
    public void verifyPlayerDraftInformation() throws IOException, JSONException {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "draft"), String.class, "8470594");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<String> draftTeamId = JsonPath.read(document, "$..draft[*].team.id");
        List<String> draftTeamName = JsonPath.read(document, "$..draft[*].team.name");
        List<String> draftTeamLink = JsonPath.read(document, "$..draft[*].team.link");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(draftTeamId.size() != 0, "'draft' -> 'team' -> 'id' is absent");
        softAssert.assertTrue(draftTeamName.size() != 0, "'draft' -> 'team' -> 'name' is absent");
        softAssert.assertTrue(draftTeamLink.size() != 0, "'draft' -> 'team' -> 'link' is absent");
        softAssert.assertAll();
    }

    /**
     * Jira https://jira.bamtechmedia.com/browse/QAA-3177
     */
    @QTestCases(id = "42907")
    @Test
    public void verifyPlayerDraftInformationWithTeamHydrate() throws IOException, JSONException, ProcessingException {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "draft(team)"), String.class, "8477492");
        String jsonSchema = fileUtils.getResourceFileAsString("nhl/api/people/schema/team_hydrate_schema.json");

        SoftAssert softAssert = new SoftAssert();

        jsonValidator.validateJsonAgainstSchema(jsonSchema, rawResponse, "team", softAssert);

        softAssert.assertAll();
    }

    @QTestCases(id = "42909")
    @Test
    public void verifyPlayerDraftInformationWithTeamRosterHydrate() throws IOException, JSONException, ProcessingException {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "draft(team(roster))"), String.class, "8470594");
        String jsonSchema = fileUtils.getResourceFileAsString("nhl/api/people/schema/team_roster_hydrate_schema.json");

        SoftAssert softAssert = new SoftAssert();

        jsonValidator.validateJsonAgainstSchema(jsonSchema, rawResponse, "roster", softAssert);

        softAssert.assertAll();
    }

    @QTestCases(id = "42913")
    @Test
    public void verifyPlayerPositionInformationLocalised() {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("site", "ru_nhl"), String.class, "8471675");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<String> code = JsonPath.read(document, "$..primaryPosition.code");
        List<String> name = JsonPath.read(document, "$..primaryPosition.name");
        List<String> type = JsonPath.read(document, "$..primaryPosition.type");
        List<String> abbreviation = JsonPath.read(document, "$..primaryPosition.abbreviation");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(code.get(0), "C", String.format("Unexpected code value: %s", code.get(0)));
        softAssert.assertEquals(name.get(0), "Центрфорвард", String.format("Unexpected name value: %s", name.get(0)));
        softAssert.assertEquals(type.get(0), "Форвард", String.format("Unexpected type value: %s", type.get(0)));
        softAssert.assertEquals(abbreviation.get(0), "Ц", String.format("Unexpected abbreviation value: %s", abbreviation.get(0)));

        softAssert.assertAll();
    }

    /**
     * Extend test to verify more data here, not only name
     */
    @QTestCases(id = "42914")
    @Test
    public void verifyRussianPlayerPage() {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("site", "ru_nhl"), String.class, "8478402");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<String> fullName = JsonPath.read(document, "$..people[0].fullName");

        Assert.assertEquals(fullName.get(0), "Коннор Макдэвид", String.format("Full name is incorrect: %s", fullName.get(0)));
    }

    @QTestCases(id = "42915")
    @Test
    public void verifyTeamSocialData() {
        JsonNode rawResponseActual1 = nhlStatsApiContentService.getPeople(
                ImmutableMap.of("hydrate", "name,currentTeam(social),social"), JsonNode.class, "8470966");
        JsonNode rawResponseActual2 = nhlStatsApiContentService.getPeople(
                ImmutableMap.of("hydrate", "name,currentTeam(social),social"), JsonNode.class, "8446014");

        JsonNode social = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual1).read("$..currentTeam.social");
        JsonNode currentTeam = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual2).read("$..currentTeam");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(social.get(0).size() != 0, "Social data is empty");
        softAssert.assertTrue(currentTeam.size() == 0, "'currentTeam' is present, but should not be");

        softAssert.assertAll();
    }

    /**
     * Failed in prod: https://jira.mlbam.com/browse/SDAPINHL-747
     */
    @QTestCases(id = "42916")
    @Test
    public void verifyAdditionalBioHydrate() throws IOException, ProcessingException {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "additionalBio"), String.class, "8473541");
        String jsonSchema = fileUtils.getResourceFileAsString("nhl/api/people/schema/people_additional_bio_schema.json");

        SoftAssert softAssert = new SoftAssert();

        jsonValidator.validateJsonAgainstSchema(jsonSchema, rawResponse, "additionalBio", softAssert);

        softAssert.assertAll();
    }

    @DataProvider(name = "homeTowns")
    public Object[][] homeTownDataProvider() {
        return new Object[][]{
                {"TUID: sp_ontario", "8444992", "South Porcupine, Ontario", true},
                {"TUID: t_ontario", "8444989", "Toronto, Ontario", true},
                {"TUID: a_ontario", "8445181", "Apsley, Ontario", true},
                {"TUID: playerId_8444995", "8444995", "", true},
                {"TUID: playerId_8445157", "8445157", "", false},
        };
    }

    /**
     * JIRA Ticket: https://jira.mlbam.com/browse/SDAPINHL-673
     */
    @QTestCases(id = "42918")
    @Test(dataProvider = "homeTowns")
    public void verifyHomeTown(String TUID, String playerId, String homeTown, boolean isBioRequested) {
        String rawResponse;
        if (isBioRequested) {
            rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("personIds", playerId, "hydrate", "additionalBio"), String.class);
        } else {
            rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("personIds", playerId), String.class);
        }

        List<String> homeTowns = JsonPath.read(rawResponse, String.format("$..people[?(@.id == %s)].additionalBio.homeTown", playerId));

        if (!"8444995".equals(playerId) && !"8445157".equals(playerId)) {
            Assert.assertEquals(homeTowns.get(0), homeTown);
        } else {
            Assert.assertEquals(homeTowns.size(), 0);
        }
    }

    /**
     * TODO add second case: api/v1/teams/16/roster?language=ru
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-777
     */
    @QTestCases(id = "42917")
    @Test
    public void verifyRussianLocalisationForLeadersAndTeamRoster() throws IOException, JSONException {
        String rawResponse = nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", "goals,assists", "hydrate", "person", "site", "ru_nhl", "season", "20132014"),
                String.class);
        String responseSnapshot = fileUtils.getResourceFileAsString("nhl/api/leaders/leaders_ru_person_hydrate_20132014.json");

        JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.STRICT, Lists.newArrayList("currentAge", "currentTeam", "weight", "captain"));

        JSONAssert.assertEquals(responseSnapshot, rawResponse, jsonCustomIgnoreComparator);
    }

    /**
     * TODO find player with top100=true
     * JIRA tickets: https://jira.mlbam.com/browse/SDAPINHL-855, https://jira.mlbam.com/browse/SDAPINHL-856
     */
    @QTestCases(id = "42919")
    @Test
    public void verifyTop100Property() {
        String rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "achievement"), String.class, "8447400");
        List<Boolean> top100 = JsonPath.read(rawResponse, "$..achievement.top100");
        List<Boolean> inHof = JsonPath.read(rawResponse, "$..achievement.inHOF");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(inHof.size()>0,"inHOF property is not exist");
        softAssert.assertTrue(top100.size()>0,"top100 property is not exist");

        softAssert.assertAll();
    }
    
	@DataProvider(name = "slugsDataProvider")
	public Object[][] slugsDataProvider() {
		return new Object[][] {
                {"TUID: lang_ru_hl", "ru_nhl", "александр-овечкин-8471214"},
                {"TUID: lang_en_nhl", "en_nhl", "alex-ovechkin-8471214"},
		};
	}

	/**
	 * https://jira.mlbam.com/browse/SDAPINHL-1020
	 */
    @QTestCases(id = "42920")
	@Test(dataProvider = "slugsDataProvider")
	public void verifySlugs(String TUID, String lang, String slug) {
		String rawResponse = nhlStatsApiContentService.getPeople(
				ImmutableMap.of("hydrate", "name,currentTeam(social),social,additionalBio,draft(team)", "site", lang),
				String.class, "8471214");

		List<String> slugsStringList = JsonPath.read(rawResponse, "$..slug");

		Assert.assertEquals(slugsStringList.get(0), slug, "'" + lang + "' slug is incorrect!");
	}

    /**
     * https://jira.mlbam.com/browse/SDAPINHL-1090
     */
    @QTestCases(id = "42921")
    @MethodOwner(owner = "cboyle")
    @Test
    public void verifySortGameLogsByDate() {

        JsonNode rawGameLogs = nhlStatsApiContentService
                .getPeople(ImmutableMap.of("stats", "gameLog", "expand", "stats.team", "site", "en_nhl"), JsonNode.class, "8475772/stats");

        ArrayNode gameDates = JsonPath.using(jsonPathJacksonConfiguration).parse(rawGameLogs)
                .read("$.stats[*].splits[*].date");

        List<String> dateList = new ArrayList<>();
        List<String> dateListSorted = new ArrayList<>();

        for (JsonNode node : gameDates) {
            dateList.add(node.asText());
            dateListSorted.add(node.asText());
        }

        LOGGER.info("dateList: " + dateList);

        Collections.sort(dateListSorted, Collections.<String>reverseOrder());

        LOGGER.info("dateListSorted: " + dateListSorted);

        Assert.assertEquals(dateList, dateListSorted
                , "Expected the returned list of games to be in order by date, found they were not.  Please see log.");
    }

    /*
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1192
     */
    @QTestCases(id = "42923")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyHydrateDraftInformationIntoPersonObject() throws IOException, JSONException {
        String rawDraft = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "draft"), String.class, "8471214");
        String rawDraftPicks = fileUtils.getResourceFileAsString("nhl/api/people/people_draft_2004.json");

        String draftInfo = JsonPath.using(jsonPathJacksonConfiguration).parse(rawDraft).read("$.people[*].draft").toString();
        String draftPicks = JsonPath.read(rawDraftPicks, "$.drafts..picks").toString();

        LOGGER.info(draftInfo + " matches " + draftPicks);

        JSONAssert.assertEquals(draftInfo, draftPicks, JSONCompareMode.STRICT_ORDER);
    }

    /*
     *Jira ticket: https://jira.mlbam.com/browse/SDAPINHL-828
     * */
    @QTestCases(id = "42922")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyDataToIndicateSeasonStatLeaders()throws IOException, JSONException {
        String rawRegularSeasonStatRankings = nhlStatsApiContentService.getPeople(ImmutableMap.of("stats", "regularSeasonStatRankings", "season", "19901991"), String.class, "8447400/stats");
        String statRegularSnapshot = fileUtils.getResourceFileAsString(PEOPLE_REGULAR_SEASON_STAT_RANKINGS_FILE);

        String rawPlayoffStatRankings = nhlStatsApiContentService.getPeople(ImmutableMap.of("stats", "playoffStatRankings", "season", "19901991"), String.class, "8447400/stats");
        String statPlayoffSnapshot = fileUtils.getResourceFileAsString(PEOPLE_PLAYOFF_STAT_RANKINGS_FILE);

        String rawYearByYearPlayoffs = nhlStatsApiContentService.getPeople(ImmutableMap.of("stats", "yearByYearPlayoffs"), String.class, "8447400/stats");
        String yearByYearPlayoffsSnapshot = fileUtils.getResourceFileAsString(PEOPLE_YEAR_BY_YEAR_PLAYOFFS_FILE);

        String rawYearByYear = nhlStatsApiContentService.getPeople(ImmutableMap.of("stats", "yearByYear"), String.class, "8447400/stats");
        String yearByYearSnapshot = fileUtils.getResourceFileAsString(PEOPLE_YEAR_BY_YEAR_FILE);

        JSONAssert.assertEquals(statRegularSnapshot, rawRegularSeasonStatRankings, JSONCompareMode.LENIENT);
        JSONAssert.assertEquals(statPlayoffSnapshot, rawPlayoffStatRankings, JSONCompareMode.LENIENT);
        JSONAssert.assertEquals(yearByYearPlayoffsSnapshot, rawYearByYearPlayoffs, JSONCompareMode.LENIENT);
        JSONAssert.assertEquals(yearByYearSnapshot, rawYearByYear, JSONCompareMode.LENIENT);
    }

    /*
    * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1280
    */
    @QTestCases(id = "42924")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyStatNodeIsPresent() {
        JsonNode rawStat = nhlStatsApiContentService.getPeople(ImmutableMap.of("stats", "goalsByGameSituation", "expand", "stats.team", "season", "20162017", "site","en_nhl"),
                JsonNode.class, "8470794/stats");

        JsonNode stat = JsonPath.using(jsonPathJacksonConfiguration).parse(rawStat).read("$..stat");

        Assert.assertTrue(stat.size() > 0 && !stat.isMissingNode(), "Expected 'stat' node to populate and be present " + stat.size() + " " + stat.isMissingNode());
    }

    /*
    * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1021
    */
    @QTestCases(id = "42925")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyRomanEnglishPlayerName() {
        JsonNode rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("hydrate", "name", "site", "ru_nhl"), JsonNode.class, "8474020");

        JsonNode fullName = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$.people[0].fullName");

        Assert.assertEquals(fullName.asText(), "Нико Саччетти", String.format("FullName is in incorrect form: %s", fullName));
    }

    /*
    * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1290
    */
    @QTestCases(id = "42926")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyNumbersOfTeamName() {
        JsonNode rawResponse = nhlStatsApiContentService.getPeople(ImmutableMap.of("stats", "yearByYear"), JsonNode.class, "8477240/stats");

        JsonNode teamName = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..splits[?(@.season == 20162017)].team.name");

        Assert.assertTrue(teamName.size() == 2, String.format("Incorrect numbers of team name: %s", teamName.size()));
    }



    /**
     * TODO [minor] think about how to move this method to base test
     */
	protected void verifyPeopleResponseWithJsonAssertWithHeaders(String responseDataFile, Map headers, Map parameters,
			String pathSegment) throws IOException, JSONException {
		JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.NON_EXTENSIBLE,
				Lists.newArrayList("copyright" ,"ot", "gamesStarted", "ties", "currentAge", "goalsLeadingByOne", "goalsTrailingByOne", "goalsWhenTied", "faceOffPct", "alternateCaptain"));

		String responseSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
		String actualResponse = nhlStatsApiContentService.getPeople(headers, parameters, String.class, pathSegment);

		JSONAssert.assertEquals(responseSnapshot, actualResponse, jsonCustomIgnoreComparator);
	}

    protected void verifyPeopleResponseActivePlayerWithJsonAssert(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        JSONComparator jsonCustomIgnoreComparator =
                new JsonIgnoreComparator(JSONCompareMode.LENIENT, Lists.newArrayList("stats", "currentAge"));

        String responseSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String actualResponse = nhlStatsApiContentService.getPeople(parameters, String.class, pathSegment);

        JSONAssert.assertEquals(responseSnapshot, actualResponse, jsonCustomIgnoreComparator);
    }

    /**
     * Parameter 'currentAge' of players is changing
     */
    protected void verifyTeamsResponseWithJsonAssert(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        JSONComparator jsonCustomIgnoreComparator =
                new JsonIgnoreComparator(JSONCompareMode.LENIENT,
                        Lists.newArrayList("currentAge", "weight", "rosterStatus", "primaryNumber", "primaryPosition", "position", "captain", "alternateCaptain", "id" , "link", "name"));

        String responseSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String actualResponse = nhlStatsApiContentService.getTeams(parameters, String.class, pathSegment);

        JSONAssert.assertEquals(responseSnapshot, actualResponse, jsonCustomIgnoreComparator);
    }
}