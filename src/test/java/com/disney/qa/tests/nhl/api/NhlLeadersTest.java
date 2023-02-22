package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlObjectConverter;
import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.api.nhl.pojo.Leaders;
import com.disney.qa.api.nhl.pojo.stats.leaders.Leader;
import com.disney.qa.api.nhl.pojo.stats.leaders.LeagueLeader;
import com.disney.qa.api.nhl.pojo.stats.teams.Teams;
import com.disney.qa.tests.nhl.api.testdata.NhlLeadersTestData;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;


/**
 * Hi Mikita
 * This is because the NHL feeds automatically switch to playoffs once the playoffs start.  To get around that we pass in the game type.  So please add the param
 * gameType=2
 * <p/>
 * This is also valid for feed consistency test
 */
public class NhlLeadersTest extends BaseNhlApiTest {

    public static final String ERROR_MESSAGE_PATTERN = "Stats API response is not equal to Feed response.\nStats API: %s\nFeed: %s";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @QTestCases(id = "42900")
    @Test(enabled = false, description = "JIRA# QAA-1478")
    public void verifyPointsGoalsAssistsGaaComparingWithFeed() {
        NhlObjectConverter objectConverter = new NhlObjectConverter();

        Leaders actualLeaders = objectConverter.toLeaders(nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", "points,goals,assists,gaa"),
                com.disney.qa.api.nhl.pojo.stats.leaders.Leaders.class));

        Leaders expectedLeaders = objectConverter.toLeaders(nhlFeedContentService.getLeaders(
                ImmutableMap.of("gameType", "2"), com.disney.qa.api.nhl.pojo.feed.leaders.Leaders.class));

        assertLeaders(actualLeaders, expectedLeaders);
    }


    /**
     * TODO [minor] parameterize the test: request all commands instead of one
     * https://jira.mlbam.com/browse/SDAPINHL-1031
     */
    @Test(enabled = false)
    public void verifyTeamLeadersOneTeamComparingWithFeed() {
        NhlObjectConverter objectConverter = new NhlObjectConverter();

        Teams actualTeams = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("expand", "team.leaders", "leaderCategories", "goals,assists"), Teams.class, "1");

        Leaders actualLeaders = objectConverter.toLeaders(wrapLeagueLeadersList(actualTeams.getTeams().get(0).getLeagueLeaders()));

        Leaders expectedLeaders = objectConverter.toLeaders(nhlFeedContentService.getLeaders(
                ImmutableMap.of("teamId", "1", "gameType", "2"), com.disney.qa.api.nhl.pojo.feed.leaders.Leaders.class));

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualLeaders.getLeagueLeaders("goals"), expectedLeaders.getLeagueLeaders("goals"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders.toString(), expectedLeaders.toString()));
        softAssert.assertEquals(actualLeaders.getLeagueLeaders("assists"), expectedLeaders.getLeagueLeaders("assists"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders.toString(), expectedLeaders.toString()));
        softAssert.assertAll();
    }

    /**
     * https://jira.mlbam.com/browse/SDAPINHL-1031
     */
    @Test(enabled = false)
    public void verifyTeamLeadersTwoTeamsComparingWithFeed() {
        NhlObjectConverter objectConverter = new NhlObjectConverter();

        Teams actualTeams = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("expand", "team.leaders", "leaderCategories", "goals,assists", "teamId", "1,2"), Teams.class);

        Leaders actualLeaders1 = objectConverter.toLeaders(wrapLeagueLeadersList(actualTeams.getTeams().get(0).getLeagueLeaders()));
        Leaders actualLeaders2 = objectConverter.toLeaders(wrapLeagueLeadersList(actualTeams.getTeams().get(1).getLeagueLeaders()));

        Leaders expectedLeaders1 = objectConverter.toLeaders(nhlFeedContentService.getLeaders(
                ImmutableMap.of("teamId", "1", "gameType", "2"), com.disney.qa.api.nhl.pojo.feed.leaders.Leaders.class));
        Leaders expectedLeaders2 = objectConverter.toLeaders(nhlFeedContentService.getLeaders(
                ImmutableMap.of("teamId", "2", "gameType", "2"), com.disney.qa.api.nhl.pojo.feed.leaders.Leaders.class));

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actualLeaders1.getLeagueLeaders("goals"), expectedLeaders1.getLeagueLeaders("goals"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders1.toString(), expectedLeaders1.toString()));
        softAssert.assertEquals(actualLeaders2.getLeagueLeaders("goals"), expectedLeaders2.getLeagueLeaders("goals"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders2.toString(), expectedLeaders2.toString()));

        softAssert.assertEquals(actualLeaders1.getLeagueLeaders("assists"), expectedLeaders1.getLeagueLeaders("assists"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders1.toString(), expectedLeaders1.toString()));
        softAssert.assertEquals(actualLeaders2.getLeagueLeaders("assists"), expectedLeaders2.getLeagueLeaders("assists"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders2.toString(), expectedLeaders2.toString()));

        softAssert.assertAll();
    }

    /**
     * TODO [minor] parameterize the test - request all commands instead of two
     * https://jira.mlbam.com/browse/SDAPINHL-1031
     */
    @Test(enabled = false)
    public void verifyTeamLeaderAllTeamsComparingWithFeed() {
        NhlObjectConverter objectConverter = new NhlObjectConverter();

        Teams actualTeams = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("expand", "team.leaders", "leaderCategories", "goals,assists"), Teams.class);

        Leaders actualLeaders1 = objectConverter.toLeaders(wrapLeagueLeadersList(actualTeams.getTeams().get(0).getLeagueLeaders()));
        Leaders actualLeaders2 = objectConverter.toLeaders(wrapLeagueLeadersList(actualTeams.getTeams().get(1).getLeagueLeaders()));

        Leaders expectedLeaders1 = objectConverter.toLeaders(nhlFeedContentService.getLeaders(
                ImmutableMap.of("teamId", "1", "gameType", "2"), com.disney.qa.api.nhl.pojo.feed.leaders.Leaders.class));
        Leaders expectedLeaders2 = objectConverter.toLeaders(nhlFeedContentService.getLeaders(
                ImmutableMap.of("teamId", "2", "gameType", "2"), com.disney.qa.api.nhl.pojo.feed.leaders.Leaders.class));

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(actualLeaders1.getLeagueLeaders("goals"), expectedLeaders1.getLeagueLeaders("goals"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders1.toString(), expectedLeaders1.toString()));
        softAssert.assertEquals(actualLeaders2.getLeagueLeaders("goals"), expectedLeaders2.getLeagueLeaders("goals"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders2.toString(), expectedLeaders2.toString()));

        softAssert.assertEquals(actualLeaders1.getLeagueLeaders("assists"), expectedLeaders1.getLeagueLeaders("assists"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders1.toString(), expectedLeaders1.toString()));
        softAssert.assertEquals(actualLeaders2.getLeagueLeaders("assists"), expectedLeaders2.getLeagueLeaders("assists"),
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders2.toString(), expectedLeaders2.toString()));

        softAssert.assertAll();
    }

    @Test
    public void verifyLeadersLimitsGaa() {
        com.disney.qa.api.nhl.pojo.stats.leaders.Leaders leaders = nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", "gaa"), com.disney.qa.api.nhl.pojo.stats.leaders.Leaders.class);

        Assert.assertNotNull(leaders.getLeagueLeaders().get(0).getAdditionalProperties().get("limits"), "'limits' property is not present in response");
    }

    @Test
    public void verifyLeadersLimitsSavePct() {
        com.disney.qa.api.nhl.pojo.stats.leaders.Leaders leaders = nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", "savePct"), com.disney.qa.api.nhl.pojo.stats.leaders.Leaders.class);

        Assert.assertNotNull(leaders.getLeagueLeaders().get(0).getAdditionalProperties().get("limits"), "'limits' property is not present in response");
    }

    @Test
    public void verifyLeadersLimitsGoals() {
        com.disney.qa.api.nhl.pojo.stats.leaders.Leaders leaders = nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", "goals"), com.disney.qa.api.nhl.pojo.stats.leaders.Leaders.class);

        Assert.assertNull(leaders.getLeagueLeaders().get(0).getAdditionalProperties().get("limits"), "'limits' property should not be present in response");
    }

    @Test
    public void verifyLeadersLimitsWins() {
        com.disney.qa.api.nhl.pojo.stats.leaders.Leaders leaders = nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", "wins"), com.disney.qa.api.nhl.pojo.stats.leaders.Leaders.class);

        Assert.assertNull(leaders.getLeagueLeaders().get(0).getAdditionalProperties().get("limits"), "'limits' property should not be present in response");
    }

    @Test
    public void verifyLeadersExpandPerson() {
        com.disney.qa.api.nhl.pojo.stats.leaders.Leaders leaders = nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", "points", "expand", "leaders.person,person.names"),
                com.disney.qa.api.nhl.pojo.stats.leaders.Leaders.class);

        for (Leader leader : leaders.getLeagueLeaders().get(0).getLeaders()) {
            // 1. presence of slug mans that 'person.names' expand is working
            // 2. presence of other fields except of id, fullName, link means that 'leaders.person' expand is working
            if (null == ((Map) leader.getPerson().getAdditionalProperties().get("otherNames")).get("slug") ||
                    null == leader.getPerson().getAdditionalProperties().get("birthDate") ||
                    null == leader.getPerson().getAdditionalProperties().get("nationality")) {
                Assert.fail("'leaders.person' or 'person.names' value of 'expand' parameter are working not as expected");
            }
        }
    }

    @DataProvider(name = "additionalLeaders20132014")
    public Object[][] additionalLeadersDataProvider() {
        return new Object[][]{
                {"TUID: vSobotka", "faceOffPct", "Vladimir Sobotka"},
                {"TUID: cSchneider", "otLosses", "Cory Schneider"},
                {"TUID: rMiller", "losses", "Ryan Miller"},
                {"TUID: vHedman", "shortHandedAssists", "Victor Hedman"},
                {"TUID: sCrosby", "pointsPerGame", "Sidney Crosby"},
                {"TUID: playPoionts_Backstrom", "powerPlayPoints", "Nicklas Backstrom"},
                {"TUID: tBozak", "shootingPctg", "Tyler Bozak"},
                {"TUID: mMartin", "hits", "Matt Martin"},
                {"TUID: tJohnson", "shortHandedPoints", "Tyler Johnson"},
                {"TUID: tSestito", "penaltyMinutes", "Tom Sestito"},
                {"TUID: aOvechkin", "shots", "Alex Ovechkin"},
                {"TUID: playAssists_Backstrom", "powerPlayAssists", "Nicklas Backstrom"},
                {"TUID: mPacioretty", "gameWinningGoals", "Max Pacioretty"}
        };
    }

    /**
     * TODO add second verification point (see JIRA): /api/v1/teams/21/leaders?leaderCategories={category}
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-813
     */
    @QTestCases(id = "42901")
    @Test(dataProvider = "additionalLeaders20132014")
    public void verifyAdditionalLeaders(String TUID, String leaderCategories, String leaderName) {
        String rawResponse = nhlStatsApiContentService.getLeaders(
                ImmutableMap.of("leaderCategories", leaderCategories, "season", "20132014"), String.class);

        List<String> leaders = JsonPath.read(rawResponse, "$..leaders[*].person.fullName");

        Assert.assertEquals(leaders.get(0), leaderName);
    }
    
    /**
     * TODO think about more deep check - verify whole response or create excel with more data
     * <p>
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-862
     * WIKI page: https://wiki.mlbam.com/display/SD/All+Time+Stats+Leaders+Endpoint+Upgrade
     * Source feed: http://f.nhl.com/feed/nhl/gamedata/expanded/alltimestatsleaders.json
     */
    @QTestCases(id = "42902")
    @Test(dataProviderClass = NhlLeadersTestData.class, dataProvider = "allTimeStatsLeadersRequests")
    public void verifyAllTimeStatsLeadersEndpointUpgrade(String TUID, String request,
                                                         String jsonPathExpressionName, String jsonPathExpressionValue,
                                                         String expectedName, String expectedValue) {
        LOGGER.info(String.format("Requesting endpoint: %s%s", NhlParameters.getNhlStatsApiHost(), request));
        
        String rawResponse = restTemplate.getForObject(NhlParameters.getNhlStatsApiHost() + request, String.class);

        SoftAssert softAssert = new SoftAssert();

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);
        
        List<String> actualNames = JsonPath.read(document, jsonPathExpressionName);
        List<String> actualValues = JsonPath.read(document, jsonPathExpressionValue);

        softAssert.assertEquals(actualNames.get(0), expectedName,
                String.format("Actual value: %s, expected: %s", actualNames.get(0), expectedName));
        softAssert.assertTrue(Integer.valueOf(actualValues.get(0)) >= Integer.valueOf(expectedValue),
                String.format("Actual value: %s, expected: %s", actualValues.get(0), expectedValue));

        softAssert.assertAll();
    }

    @QTestCases(id = "42903")
	@Test()
	public void verifyAllTimeSeasonLeadersStat() {
		JsonNode rawResponse2 = nhlStatsApiContentService
				.getLeaders(
						ImmutableMap.of("leaderCategories", "powerPlayGoals", "limit", "25", "hydrate",
								"team,person(stats(splits=statsSingleSeason))", "depth", "AllTimeSeason"),
						JsonNode.class);

		JsonNode personListNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2).read("$..person");
		JsonNode statsListNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
				.read("$..person..stat");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(personListNode.size() == statsListNode.size(),
				"'stat' node is not present for each player!");
		softAssert.assertTrue(personListNode.size() == 25, "Incorrect number of players!");
		softAssert.assertAll();
	}
	
	/**
	 * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-1035
	 */
    @QTestCases(id = "42904")
	@Test()
	public void verifyLeadersTeamExpand() {
		nhlStatsApiContentService.getLeaders(ImmutableMap.of("leaderCategories", "points,goals,assists,wins",
				"leaderGameTypes", "R", "expand", "leaders.person,leaders.team", "season", "20162017"), String.class);

	}

    protected void assertLeaders(Leaders actualLeaders, Leaders expectedLeaders) {
        Assert.assertEquals(actualLeaders, expectedLeaders,
                String.format(ERROR_MESSAGE_PATTERN, actualLeaders.toString(), expectedLeaders.toString()));
    }

    /**
     * TODO [minor] Currently it's a workaround. Refactor to pass list in object converter instead of class-wrapper
     */
    protected com.disney.qa.api.nhl.pojo.stats.leaders.Leaders wrapLeagueLeadersList(List<LeagueLeader> leagueLeaders) {
        com.disney.qa.api.nhl.pojo.stats.leaders.Leaders leaders = new com.disney.qa.api.nhl.pojo.stats.leaders.Leaders();
        leaders.setLeagueLeaders(leagueLeaders);
        return leaders;
    }
}
