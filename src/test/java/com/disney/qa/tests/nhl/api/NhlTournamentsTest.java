package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.pojo.stats.tournaments.Round;
import com.disney.qa.api.nhl.pojo.stats.tournaments.Series;
import com.disney.qa.api.nhl.pojo.stats.tournaments.Tournaments;
import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.R;
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

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Finalize https://www.pivotaltracker.com/n/projects/1427108/stories/117189055 - Done
 * TODO Review after 1.9.0: https://www.pivotaltracker.com/projects/1427108/stories/118402183
 */
public class NhlTournamentsTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String TOURNAMENTS_PLAYOFF_SEASON_1993_1994 = "nhl/api/tournaments/playoffs_19931994.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_1993_1994_FR = "nhl/api/tournaments/playoffs_19931994_fr.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_1993_1994_EXPANDS = "nhl/api/tournaments/playoffs_19931994_expands.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_1993_1994_EXPANDS_FR = "nhl/api/tournaments/playoffs_19931994_expands_fr.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_EXPANDS_PROD = "nhl/api/tournaments/playoffs_20132014_expands_prod.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2014_2015_EXPANDS_PROD = "nhl/api/tournaments/playoffs_20142015_expands_prod.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_1_EXPANDS_PROD = "nhl/api/tournaments/playoffs_20132014_round_1_expands_prod.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_1_EXPANDS_QA = "nhl/api/tournaments/playoffs_20132014_round_1_expands_qa.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_3_EXPANDS_PROD = "nhl/api/tournaments/playoffs_20132014_round_3_expands_prod.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_3_EXPANDS_QA = "nhl/api/tournaments/playoffs_20132014_round_3_expands_qa.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_SERIES_CODES_C_EXPANDS = "nhl/api/tournaments/playoffs_20132014_series_codes_c_expands.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_SERIES_CODES_F_EXPANDS_PROD = "nhl/api/tournaments/playoffs_20132014_series_codes_f_expands_prod.json";

    public static final String TOURNAMENTS_PLAYOFF_SEASON_2013_2014_SERIES_CODES_F_EXPANDS_QA = "nhl/api/tournaments/playoffs_20132014_series_codes_f_expands_qa.json";

    private String environment = R.CONFIG.get("env");
    public static final String ENV_QA = "QA";
    public static final String ENV_DEV = "DEV";

    @Test
    public void verifyPreviousSeasonsTournamentConsistency() throws IOException, JSONException {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))) {
            verifyTournamentsPlayoffs(ImmutableMap.of("season", "19931994"), TOURNAMENTS_PLAYOFF_SEASON_1993_1994);
        } else {
            skipExecution("Skipping 'Playoff Season 1993 1994' in QA and DEV");
        }
    }

    @Test
    public void verifyPreviousSeasonsTournamentConsistencyFr() throws IOException, JSONException {
        verifyTournamentsPlayoffs(ImmutableMap.of("season", "19931994", "language", "fr"), TOURNAMENTS_PLAYOFF_SEASON_1993_1994_FR);
    }

    @Test
    public void verifyPreviousSeasonsTournamentConsistencyExpands() throws IOException, JSONException {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))) {
            verifyTournamentsPlayoffs(
                    ImmutableMap.of("season", "19931994", "expand", "round.series,series.schedule,schedule.game.seriesSummary"),
                    TOURNAMENTS_PLAYOFF_SEASON_1993_1994_EXPANDS);
        } else {
            skipExecution("Skipping 'Playoff Season 1993 1994 Expand' in QA and DEV");
        }
    }

    @Test()
    public void verifyPreviousSeasonsTournamentConsistencyExpandsFr() throws IOException, JSONException {
        verifyTournamentsPlayoffs(
                ImmutableMap.of("season", "19931994", "language", "fr", "expand", "round.series,series.schedule,schedule.game.seriesSummary"),
                TOURNAMENTS_PLAYOFF_SEASON_1993_1994_EXPANDS_FR);
    }

    @Test()
    public void verifyTournamentsPlayoffExpands20132014() throws IOException, JSONException {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))) {
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "season", "20132014"),
                    TOURNAMENTS_PLAYOFF_SEASON_2013_2014_EXPANDS_PROD);
        } else {
            skipExecution("Skipping 'Playoff Season 2013 2014 Expand' in QA and DEV");
        }
    }

    /**
     * SDAPINHL-454 was at QA, DEV
     */
    @Test()
    public void verifyTournamentsPlayoffExpands20142015() throws IOException, JSONException {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))) {
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "season", "20142015"),
                    TOURNAMENTS_PLAYOFF_SEASON_2014_2015_EXPANDS_PROD);
        } else {
            skipExecution("Skipping 'Playoff Season 2014 2015 Expand' in QA and DEV");
        }
    }

    @Test()
    public void verifyTournamentsPlayoffExpands20132014Rounds1() throws IOException, JSONException {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))) {
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "rounds", "1", "season", "20132014"),
                    TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_1_EXPANDS_QA);
        } else
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "rounds", "1", "season", "20132014"),
                    TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_1_EXPANDS_PROD);
    }

    @Test()
    public void verifyTournamentsPlayoffExpands20132014Rounds3() throws IOException, JSONException {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))) {
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "rounds", "3", "season", "20132014"),
                    TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_3_EXPANDS_QA);
        } else
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "rounds", "3", "season", "20132014"),
                    TOURNAMENTS_PLAYOFF_SEASON_2013_2014_ROUND_3_EXPANDS_PROD);
    }

    /**
     * additional 'link' field is ok
     */
    @Test()
    public void verifyTournamentsPlayoffExpands20132014SeriesCodesC() throws IOException, JSONException {
        verifyTournamentsPlayoffs(
                ImmutableMap.of("expand", "round.series,series.schedule", "seriesCodes", "C", "season", "20132014"),
                TOURNAMENTS_PLAYOFF_SEASON_2013_2014_SERIES_CODES_C_EXPANDS);
    }

    @Test()
    public void verifyTournamentsPlayoffExpands20132014SeriesCodesF() throws IOException, JSONException {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))) {
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "seriesCodes", "F", "season", "20132014"),
                    TOURNAMENTS_PLAYOFF_SEASON_2013_2014_SERIES_CODES_F_EXPANDS_QA);
        } else
            verifyTournamentsPlayoffsExpand(
                    ImmutableMap.of("expand", "round.series,series.schedule", "seriesCodes", "F", "season", "20132014"),
                    TOURNAMENTS_PLAYOFF_SEASON_2013_2014_SERIES_CODES_F_EXPANDS_PROD);
    }


    @Test
    public void verifyRoundSeriesExpandParameter() {
        if (!(environment.equalsIgnoreCase(ENV_QA) || environment.equalsIgnoreCase(ENV_DEV))){
            Tournaments tournaments = nhlStatsApiContentService.getTournaments(ImmutableMap.of("expand", "round.series", "season", "20152016"), Tournaments.class, "playoffs");
            for (Round round : tournaments.getRounds()) {
                for (Series series : round.getSeries()) {
                    Assert.assertNotNull(series.getCurrentGame().getSeriesSummary().getSeriesStatus(),
                            String.format("'seriesStatus' is empty for game '%s'", series.getCurrentGame().getSeriesSummary().getGamePk()));
                }
            }
        } else {
            skipExecution("Skipping 'Round Series Expand Parameter' in QA and DEV");
        }
    }

    @Test
    public void verifyRoundSeriesExpandParameterFr() {
        String expandValue = "round.series";
        Tournaments tournaments = nhlStatsApiContentService.getTournaments(ImmutableMap.of("expand", expandValue, "season", "20132014", "site", "fr_nhl"), Tournaments.class, "playoffs");
        for (Round round : tournaments.getRounds()) {
            for (Series series : round.getSeries()) {
                Assert.assertNotNull(series.getCurrentGame().getSeriesSummary().getSeriesStatus(),
                        String.format("'seriesStatus' is empty for game '%s'", series.getCurrentGame().getSeriesSummary().getGamePk()));
            }
        }
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1163
     */
    @QTestCases(id = "43000")
    @Test
    public void verifyTournamentWorldCupEndpoint() {
        JsonNode rawResponse = nhlStatsApiContentService.getTournaments(
                ImmutableMap.of("season", "20162017", "expand", "round.series&series.seriesSummary"), JsonNode.class,
                "worldCup");
        JsonNode seriesNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..round.series");
        JsonNode seriesSummaryNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..series.seriesSummary");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(seriesNodes.size() >= 0, "series node is not present!");
        softAssert.assertTrue(seriesSummaryNodes.size() >= 0, "series node is not present!");
        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1141
     */
    @QTestCases(id = "43001")
    @Test
    public void verifyTournamentRoundsAndSeriesCodesFiltering() {
        JsonNode rawResponse1 = nhlStatsApiContentService.getTournaments(
                ImmutableMap.of("season", "20162017", "expand", "round.series&rounds=2"), JsonNode.class, "playoffs");
        JsonNode rawResponse2 = nhlStatsApiContentService.getTournaments(
                ImmutableMap.of("season", "20162017", "expand", "round.series&seriesCodes=A,O"), JsonNode.class,
                "playoffs");

        JsonNode specificRoundNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
                .read("$..round[?(@.number)].*");
        JsonNode specificSeriesNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
                .read("$..seriesCode");

        SoftAssert softAssert = new SoftAssert();
        // specificRoundNodes verification
        if (specificRoundNodes.size() > 0) {
            for (JsonNode specificRound : specificRoundNodes) {
                softAssert.assertTrue(specificRound.toString().equals("2"),
                        specificRound.toString() + " round is not equals '2'!");
            }
        } else {
            softAssert.fail("'round' nodes are not present!");
        }

        // specificSeriesNodes verification
        if (specificSeriesNodes.size() > 0) {
            for (JsonNode specificSeries : specificSeriesNodes) {
                String series = specificSeries.textValue();
                if (!(series.equals("A") || series.equals("O"))) {
                    softAssert.fail(series + " series is not equals 'A'||'O'!");
                }
            }
        } else {
            softAssert.fail("'round' nodes are not present!");
        }

        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1083
     */
    @QTestCases(id = "42999")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyTournamentSeries() throws IOException {
        String rawResponse = nhlStatsApiContentService.getTournaments(
                ImmutableMap.of("expand", "round.series,series.schedule,schedule.game.seriesSummary,schedule.teams,schedule.broadcasts", "season", "20162017"), String.class,
                "playoffs");
        String cmsDataSnapshot = fileUtils.getResourceFileAsString("nhl/api/tournaments/series20162017.json");

        SoftAssert softAssert = new SoftAssert();

         Iterator apiDatas = apiData(rawResponse).iterator();
         Iterator cmsDatas = cmsData(cmsDataSnapshot).iterator();
        while(apiDatas.hasNext()){
            String allDataApi = apiDatas.next().toString();
            String allDataCms = cmsDatas.next().toString();
            if(!allDataApi.contains("") && !allDataCms.contains("")){
                softAssert.assertTrue(cmsDatas.equals(allDataCms),
                        String.format("Api data: %s, Cms data: %s", allDataApi, allDataCms));
            }
        }
        softAssert.assertAll();
    }

    private List apiData(String rawResponse) {
        ArrayNode roundsNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$.rounds");
        List<Object> nodesApi = new ArrayList<>();

        for(JsonNode round : roundsNodes){
            ArrayNode seriesNode = jsonContext().parse(round).read("$.series");
            JsonNode roundNumber = jsonContext().parse(round).read("$.number");
            JsonNode roundName = jsonContext().parse(round).read("$.names.name");
            JsonNode roundShortName = jsonContext().parse(round).read("$.names.shortName");
            nodesApi.add(roundName);
            nodesApi.add(roundNumber);
            nodesApi.add(roundShortName);

            for(JsonNode series : seriesNode) {
                JsonNode seriesCode = jsonContext().parse(series).read("$.seriesCode");
                JsonNode matchupName = jsonContext().parse(series).read("$..names.matchupName");
                JsonNode matchupShortN = jsonContext().parse(series).read("$..names.matchupShortName");
                JsonNode teamA = jsonContext().parse(series).read("$..names.teamAbbreviationA");
                JsonNode teamB = jsonContext().parse(series).read("$..names.teamAbbreviationB");
                JsonNode seriesSummary = jsonContext().parse(series).read("$..currentGame.seriesSummary.seriesStatus");
                JsonNode statusShort = jsonContext().parse(series).read("$..currentGame.seriesSummary.seriesStatusShort");

                nodesApi.add(seriesCode);
                nodesApi.add(matchupName);
                nodesApi.add(matchupShortN);
                nodesApi.add(teamA);
                nodesApi.add(teamB);
                nodesApi.add(seriesSummary);
                nodesApi.add(statusShort);
                LOGGER.info("Series Nodes: " + nodesApi);
            }
        }
        return nodesApi;
    }

    private List cmsData(String cmsData) {
        ArrayNode seriesNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(cmsData)
                .read("$..rounds[*]");
        List nodesCms = new ArrayList<>();

        for (JsonNode rounds : seriesNodes) {
            ArrayNode seriesRoundsCms = jsonContext().parse(rounds).read("$.series");
            JsonNode roundName = jsonContext().parse(rounds).read("$.name");
            JsonNode roundShortName = jsonContext().parse(rounds).read("$.shortName");
            JsonNode roundNumber = jsonContext().parse(rounds).read("$.roundNumber");
            nodesCms.add(roundName);
            nodesCms.add(roundShortName);
            nodesCms.add(roundNumber);

            for (JsonNode series : seriesRoundsCms) {
                JsonNode seriesCode = jsonContext().parse(series).read("$.series");
                JsonNode matchupName = jsonContext().parse(series).read("$..matchup");
                JsonNode matchupShortN = jsonContext().parse(series).read("$..matchupShort");
                JsonNode teamA = jsonContext().parse(series).read("$..teamA");
                JsonNode teamB = jsonContext().parse(series).read("$..teamB");
                JsonNode seriesStatus = jsonContext().parse(series).read("$..seriesStatus");
                JsonNode statusShort = jsonContext().parse(series).read("$..seriesStatusShort");

                nodesCms.add(seriesCode);
                nodesCms.add(matchupName);
                nodesCms.add(matchupShortN);
                nodesCms.add(teamA);
                nodesCms.add(teamB);
                nodesCms.add(seriesStatus);
                nodesCms.add(statusShort);
                LOGGER.info("Series nodes for CMS: " + nodesCms);
            }
        }
        return nodesCms;
    }

	protected void verifyTournamentsPlayoffs(Map<String, String> parameters, String responseDataFile)
			throws IOException, JSONException {
		String responseSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
		String actualResponse = nhlStatsApiContentService.getTournaments(parameters, String.class, "playoffs");
		JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.LENIENT,
				Lists.newArrayList("type", "matchupName", "shortName", "timeStamp"));

		JSONAssert.assertEquals(responseSnapshot, actualResponse, jsonCustomIgnoreComparator);
	}

    protected void verifyTournamentsPlayoffsExpand(Map<String, String> parameters, String responseDataFile)
            throws IOException, JSONException {
        SoftAssert softAssert = new SoftAssert();

        String responseSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String actualResponse = nhlStatsApiContentService.getTournaments(parameters, String.class, "playoffs");

        JsonNode venues = JsonPath.using(jsonPathJacksonConfiguration).parse(actualResponse).
                read("$..venue");
        if (!venues.isNull()) {
            for (JsonNode venue : venues) {
                softAssert.assertTrue(venue.toString().contains("name"), String.format("Name field on 'venue' node is missing : %s", venue.findPath("name")));
                softAssert.assertTrue(!venue.findPath("name").toString().isEmpty(), String.format("Name field on 'venue' node is empty : %s", venue.findPath("name")));
            }
        } else {
            Assert.fail("Expected 'venue' node to be present" + venues);
        }
        JSONComparator jsonCustomIgnoreComparator = new JsonIgnoreComparator(JSONCompareMode.LENIENT,
                    Lists.newArrayList("type", "matchupName", "shortName", "venue"));

        JSONAssert.assertEquals(responseSnapshot, actualResponse, jsonCustomIgnoreComparator);

        softAssert.assertAll();
    }
}
