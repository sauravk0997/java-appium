package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.api.nhl.pojo.stats.games.Games;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NhlGameTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected Gson gson = new Gson();

    /**
     * Contact parson for issues in this test - Rob Engel (developer)
     */
    @Test(description = "JIRA# QAA-1289", enabled = false)
    public void verifyLiveDataForPastGame() throws IOException, JSONException {
        verifyResponseWithJsonAssert("nhl/api/games/game_feed_live_gamepk_413849_mlb_host.json", null, "413849/feed/live");
    }

    @Test
    public void verifyPlatformParameter() {
        Games games = nhlStatsApiContentService.getGames(ImmutableMap.of("platform", "android-phone"), Games.class, "2015030225/content");
        Assert.assertTrue((null != games.getMedia()) && (null != games.getEditorial()) && (null != games.getHighlights()),
                String.format("Several parameter have null values: media - '%s', editorial - '%s', highlights - '%s'",
                        games.getMedia(), games.getEditorial(), games.getHighlights()));
    }

	@QTestCases(id = "42879")
    @Test
    public void verifyPowerPlayGoalsIsPresent() {
        verifyPowerPlayGoals(ImmutableMap.of("site", "en_nhl"));
    }

	@QTestCases(id = "42880")
    @Test
    public void verifyPowerPlayGoalsIsPresentWithTimeCode() {
        verifyPowerPlayGoals(ImmutableMap.of("site", "en_nhl", "timecode", "20160410_045510"));
    }

    /**
     * TODO add one more test instance for 'string' values
     */
	@QTestCases(id = "42882")
    @Test
    public void verifyGoalieShotsSaves() {
        String rawResponse = nhlStatsApiContentService.getGames(ImmutableMap.of("site", "en_nhl"), String.class, "2015021228/feed/live");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<Map> goalieStats = JsonPath.read(document, "$..goalieStats");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(goalieStats.get(0).get("savePercentage").toString(), "89.28571428571429");
        softAssert.assertEquals(goalieStats.get(0).get("powerPlaySavePercentage").toString(), "100.0");
        softAssert.assertEquals(goalieStats.get(0).get("evenStrengthSavePercentage").toString(), "88.0");

        softAssert.assertEquals(goalieStats.get(1).get("savePercentage").toString(), "92.10526315789474");
        softAssert.assertEquals(goalieStats.get(1).get("powerPlaySavePercentage").toString(), "90.0");
        softAssert.assertEquals(goalieStats.get(1).get("shortHandedSavePercentage").toString(), "100.0");
        softAssert.assertEquals(goalieStats.get(1).get("evenStrengthSavePercentage").toString(), "92.5925925925926");

        softAssert.assertAll();
    }

    /**
     * Jackson's JsonNode can not be used as arrays with different element's structure can not be processed
     */
	@QTestCases(id = "42883")
    @Test
    public void verifyGoalieInGoalEvent() {
        String rawResponse = nhlStatsApiContentService.getGames(null, String.class, "2016020180/feed/live");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<Object> goalies = JsonPath.read(document, "$..allPlays[?(@.result.event == 'Goal')].players[?(@.playerType == 'Goalie')]");

        Assert.assertEquals(goalies.size(), 4, "Goalies are not displayed for all goals");
    }


	@QTestCases(id = "42884")
    @Test
    public void verifyGoaliePulledProperty() {
        String rawResponse = nhlStatsApiContentService.getGames(ImmutableMap.of("timecode", "20161110_032200"), String.class, "2016020194/feed/live");

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawResponse);

        List<Boolean> awayGoaliePulled = JsonPath.read(document, "$..liveData.linescore.teams.away.goaliePulled");
        List<Boolean> homeGoaliePulled = JsonPath.read(document, "$..liveData.linescore.teams.home.goaliePulled");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(awayGoaliePulled.get(0), String.format("'away.goaliePulled' value is incorrect: %s", awayGoaliePulled.get(0)));
        softAssert.assertFalse(homeGoaliePulled.get(0), String.format("'home.goaliePulled' value is incorrect: %s", homeGoaliePulled.get(0)));

        softAssert.assertAll();
    }

	@QTestCases(id = "42885")
    @Test
    public void verifyTeamAbbreviationsInLineScore() {
        String rawResponse1 = nhlStatsApiContentService.getGames(Collections.EMPTY_MAP, String.class, "2016020476/feed/live");

        List<String> abbreviation1 = JsonPath.read(rawResponse1, "$..liveData.linescore.teams.home.team.abbreviation");
        List<String> abbreviation2 = JsonPath.read(rawResponse1, "$..liveData.linescore.teams.away.team.abbreviation");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(abbreviation1.get(0), "PHI", String.format("Incorrect abbreviation: %s", abbreviation1.get(0)));
        softAssert.assertEquals(abbreviation2.get(0), "NSH", String.format("Incorrect abbreviation: %s", abbreviation2.get(0)));

        softAssert.assertAll();
    }

    /**
     * TODO find case when penalty box is has active:false
     * JIRA ticket: https://jira.mlbam.com/browse/SDAPINHL-173
     */
	@QTestCases(id = "42886")
    @Test
    public void verifyActivePropertyForPenalty() {
        String rawResponse = nhlStatsApiContentService.getGames(ImmutableMap.of("timecode", "20170218_194014"), String.class, "2016020858/feed/live");
        List<Boolean> penaltyBox = JsonPath.read(rawResponse, "$..penaltyBox[?(@.id == 8475844)].active");
        Assert.assertEquals(penaltyBox.get(0), Boolean.TRUE, String.format("Incorrect value of 'active' property: %s", penaltyBox.get(0)));
    }

    protected void verifyResponseWithJsonAssert(String responseDataFile, Map parameters, String pathSegment) throws IOException, JSONException {
        String gamesSnapshot = fileUtils.getResourceFileAsString(responseDataFile);
        String gamesSchedule = mlbNhlStatsApiContentService.getGames(parameters, String.class, pathSegment);

        JSONAssert.assertEquals(gamesSnapshot, gamesSchedule, false);
    }

    /**
     * Jira ticket: https://jira.mlbam.com/browse/SDAPINHL-1082
	 * Doing a quick smoke test to make sure the boolean field exist when 'situationTimeRemaining' and 'situationTimeElapsed' exists.
     */
	@QTestCases(id = "42893")
	@MethodOwner(owner = "shashem")
    @Test
	public void addIndicatorForRealSituations(){
		String rawPowerPlay = nhlStatsApiContentService.getGames(ImmutableMap.of("skipCache", "true"), String.class, "2016021024/linescore");
		List<String> powerPlayInfo = JsonPath.read(rawPowerPlay, "$..powerPlayInfo");

		LOGGER.info("PowerPlayInfo = " + powerPlayInfo);

		Assert.assertTrue(powerPlayInfo.toString().contains("situationTimeRemaining")
				&& powerPlayInfo.toString().contains("situationTimeElapsed")
				&& powerPlayInfo.toString().contains("inSituation"), "Member fields are present with the boolean field");

	}

	@QTestCases(id = "42887")
	@Test
	public void verifyDiffPatch() {
		SoftAssert softAssert = new SoftAssert();

		String rawResponse1 = nhlStatsApiContentService.getGames(ImmutableMap.of("endTimecode", "7834738_78374398"),
				String.class, "0/feed/live/diffPatch");
		String rawResponse2 = nhlStatsApiContentService.getGames(ImmutableMap.of("endTimecode", "7834738_78374398"),
				String.class, "3/feed/live/diffPatch");

		softAssert.assertEquals(rawResponse1, "[]");
		softAssert.assertEquals(rawResponse2, "[]");

		String message1 = getDiffPatchErrorMessage("2/feed/live/");
		String message2 = getDiffPatchErrorMessage("0/feed/live/");

		softAssert.assertEquals(message1, "Game data couldn't be found",
				String.format("Incorrect message: %s", message1));
		softAssert.assertEquals(message2, "Game data couldn't be found",
				String.format("Incorrect message: %s", message2));

		softAssert.assertAll();
	}

	@QTestCases(id = "42888")
	@Test
	public void verifyTriCodeForTeam() {
		JsonNode rawResponse1 = nhlStatsApiContentService.getGames(null, JsonNode.class, "2016020132/feed/live");
		JsonNode rawResponse2 = nhlStatsApiContentService.getTeams(null, JsonNode.class, "5");

		// for path json["gameData"]["teams"]["away" and "home"]["triCode"]
		JsonNode gameData1 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..teams.away.triCode");
		JsonNode gameData2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..teams.home.triCode");
		
		// for path json["liveData"]["boxscore"]["teams"]["away" and "home"]["team"]["triCode"]
		JsonNode gameData3 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..liveData.boxscore.teams.away.team.triCode");
		JsonNode gameData4 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..liveData.boxscore.teams.home.team.triCode");
		
		// for path json["liveData"]["linescore"]["teams"]["away" and "home"]["team"]["triCode"]
		JsonNode gameData5 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..liveData.linescore.teams.away.team.triCode");
		JsonNode gameData6 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..liveData.linescore.teams.home.team.triCode");
		
		// for path json["teams"][0]["triCode"] 
		JsonNode gameData7 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
				.read("$..teams[0].triCode");
		
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(gameData1.size() == 1, "TriCode field for away game is not present!");
		softAssert.assertTrue(gameData2.size() == 1, "TriCode field for home game is not present!");
		softAssert.assertTrue(gameData3.size() == 1, "TriCode field for away game is not present!");
		softAssert.assertTrue(gameData4.size() == 1, "TriCode field for home game is not present!");
		softAssert.assertTrue(gameData5.size() == 1, "TriCode field for away game is not present!");
		softAssert.assertTrue(gameData6.size() == 1, "TriCode field for home game is not present!");
		softAssert.assertTrue(gameData7.size() == 0, "TriCode field is present as not expected!");
		softAssert.assertAll();
	}

	@QTestCases(id = "42889")
	@Test
	public void verifyInvalidUnknownGamePKMessage() {
		String message = getDiffPatchErrorMessage("1811020328/feed/live");
		Assert.assertEquals(message, "Game data couldn't be found", String.format("Incorrect message: %s", message));
	}

	/**
	 * https://jira.mlbam.com/browse/SDAPINHL-890
	 */
	@QTestCases(id = "42890")
	@Test
	public void verifyGoalieSubstitutionEvent() {
		JsonNode rawResponse = nhlStatsApiContentService.getGames(ImmutableMap.of("site", "en_nhl", "timecode", "20170125_000000"), JsonNode.class,
				"2016020642/feed/live");

		JsonNode goalieSubstitutionNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(
				"$..allPlays[?(@.result.description == 'Goalie Substitution' && @.players[0].playerType == 'PlayerOn' && @.players[1].playerType == 'PlayerOff')]");

		Assert.assertTrue(goalieSubstitutionNode.size() > 0, "Incorrect number of 'Goalie Substitution'!");
	}
	
	@DataProvider(name = "powerPlayInfo")
	public Object[][] powerPlayInfoDataProvider() {
		return new Object[][] {
				{"TUID: timecode_5_4", "/api/v1/game/2016021013/feed/live?timecode=20170313_000059", "5", "4", true },
				{"TUID: timecode_4_3", "/api/v1/game/2016021046/feed/live?timecode=20170317_045230", "4", "3", true },
				{"TUID: timecode_6_5", "/api/v1/game/2016021013/feed/live?timecode=20170313_013913", "6", "5", false },
				{"TUID: timecode_3_3", "/api/v1/game/2016021046/feed/live?timecode=20170317_045041", "3", "3", false } };
	}
	
	/**
	 * https://jira.mlbam.com/browse/QAA-1796
	 */
	@QTestCases(id = "42891")
	@Test(dataProvider = "powerPlayInfo" )
	public void verifyPowerPlayInfo(String TUID, String request, String numSkaters1, String numSkaters2,
			boolean powerPlayInfoPresent) {
		JsonNode rawResponse = restTemplate.getForEntity(NhlParameters.getNhlStatsApiHost() + request, JsonNode.class)
				.getBody();

		JsonNode numSkatersNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..[?(@.numSkaters == " + numSkaters1 + " || @.numSkaters == " + numSkaters2 + " ) ]");
		JsonNode powerPlayInfoNodes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..powerPlayInfo");

		Assert.assertTrue(numSkatersNodes.size() == 2, "Required 'numSkaters' nodes: numSkaters1=" + numSkaters1
				+ " numSkaters2=" + numSkaters2 + " are not present!");

		if (powerPlayInfoPresent) {
			Assert.assertTrue(powerPlayInfoNodes.size() > 0, "'powerPlayInfo' node is not present!");
		} else {
			Assert.assertTrue(powerPlayInfoNodes.size() == 0, "'powerPlayInfo' node is present as not expected!");
		}
	}

	/**
	 * https://jira.mlbam.com/browse/SDAPINHL-1069
     */
	@QTestCases(id = "42892")
	@MethodOwner(owner = "cboyle")
	@Test()
	public void verifySeasonOnLiveEndPoint() {
		JsonNode rawGame = nhlStatsApiContentService.getGames(ImmutableMap.of("site", "en_nhl"), JsonNode.class,
				"2016021194/feed/live");

		JsonNode seasonNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawGame)
				.read("$.gameData.game");
		
		String seasonText = seasonNode.findPath("season").asText();

		LOGGER.info("seasonNode = " + seasonText);

		Assert.assertTrue(seasonText.contains("20162017"), "'game.season' node is not present");
	}
	
	protected String getDiffPatchErrorMessage(String pathSegment) {
		String rawResponse = nhlStatsApiContentService.getGames(null, String.class, pathSegment);
		JsonNode message = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..message");
		return message.get(0).asText();
	}
	
	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1219
	 */
	@QTestCases(id = "42894")
	@Test
	public void verifyPowerPlayInfoNodeIfPowerPlaySpansPeriods() {
		JsonNode rawResponse = nhlStatsApiContentService.getGames(null, JsonNode.class, "2016030414/feed/live");
		JsonNode rawResponse2 = nhlStatsApiContentService.getGames(ImmutableMap.of("timecode", "20170606_012549"),
				JsonNode.class, "2016030414/feed/live");

		JsonNode inSituationNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..powerPlayInfo.inSituation");
		JsonNode inSituationNode2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
				.read("$..powerPlayInfo.inSituation");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(inSituationNode.size() > 0,
				"'powerPlayInfo.inSituation' node is not present in response!");
		softAssert.assertTrue(inSituationNode2.size() > 0,
				"'powerPlayInfo.inSituation' node is not present in response!");
		softAssert.assertAll();
	}
	
	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1232
	 */
	@QTestCases(id = "42896")
	@Test
	public void verifyIntermissionInfoNode() {
		if (!R.CONFIG.get("env").equals("PROD")) {
			JsonNode rawResponse = nhlStatsApiContentService.getGames(ImmutableMap.of("site", "en_nhl"), JsonNode.class,
					"2016030414/feed/live");

			JsonNode intermissionInfoNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
					.read("$..intermissionInfo");
			JsonNode intermissionTimeRemainingNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
					.read("$..intermissionInfo.intermissionTimeRemaining");
			JsonNode intermissionTimeElapsedNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
					.read("$..intermissionInfo.intermissionTimeElapsed");
			JsonNode inIntermissionNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
					.read("$..intermissionInfo.inIntermission");

			SoftAssert softAssert = new SoftAssert();
			softAssert.assertTrue(intermissionInfoNode.size() > 0,
					"'intermissionInfoNode' node is not present in response!");
			softAssert.assertTrue(intermissionTimeRemainingNode.size() > 0,
					"'intermissionInfo.intermissionTimeRemaining' node is not present in response!");
			softAssert.assertTrue(intermissionTimeElapsedNode.size() > 0,
					"'intermissionInfo.intermissionTimeElapsed' node is not present in response!");
			softAssert.assertTrue(inIntermissionNode.size() > 0,
					"'intermissionInfo.inIntermission' node is not present in response!");
			softAssert.assertAll();
		}
	}

	@DataProvider
	public Iterator<Object[]> retrieveLanguageList() throws IOException, URISyntaxException {

		List<Object[]> languageList = new ArrayList<>();

		ArrayNode listofLanguages = nhlStatsApiContentService
				.getProvidedEndpoint(ImmutableMap.of(), ArrayNode.class, "languages");

		for (JsonNode node : listofLanguages) {
			String language = node.findValue("locale").asText().substring(0,2) + "_nhl";

			if (!language.contains("en_") && !language.contains("es_")) {
				LOGGER.info("Adding Language: " + language);
				languageList.add(new Object[]{
						String.format("Language: (%s)", language),
						language
				});
			}
		}
		return languageList.iterator();
	}

	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-756
	 */
	@QTestCases(id = "42897")
	@MethodOwner(owner = "cboyle")
	@Test(dataProvider = "retrieveLanguageList")
	public void verifyLocalizedEventTypeReturned(String TUID, String language) throws IOException, URISyntaxException {
		SoftAssert softAssert = new SoftAssert();

		String gameId = "2016020328";

		JsonNode rawResponseEnglish = getLiveFeedForLanguage("en_nhl", gameId);
		JsonNode rawResponseNonEnglish = getLiveFeedForLanguage(language, gameId);

		List<String> dictionaryEnglish = getGameCenterDictionaryItems(R.CONFIG.get("env").toLowerCase(), "en");
		List<String> dictionaryNonEnglish = getGameCenterDictionaryItems(R.CONFIG.get("env").toLowerCase(), language.substring(0,2));

		LOGGER.info(String.format("Dictionary Size (English: %s) (Non-English: %s)"
				, dictionaryEnglish.size(), dictionaryNonEnglish.size()));

		ArrayNode resultsNonEnglish = jsonContext().parse(rawResponseNonEnglish).read("$.liveData[*].allPlays[*].result");
		ArrayNode resultsEnglish = jsonContext().parse(rawResponseEnglish).read("$.liveData[*].allPlays[*].result");

		for (int i = 0; i < resultsNonEnglish.size(); i++) {
			JsonNode nodeNonEnglish = resultsNonEnglish.get(i);

			boolean matchedNonEnglish = dictionaryNonEnglish.contains(nodeNonEnglish.findPath("event").asText());
			boolean matchedEnglish = dictionaryEnglish.contains(nodeNonEnglish.findPath("event").asText());

			String resultsPrinted = String.format("Event: %s Event Code: %s, Event Id: %s, Description: %s, Non-English: %s, English: %s",
					nodeNonEnglish.findPath("event"),
					nodeNonEnglish.findPath("eventCode"),
					nodeNonEnglish.findPath("eventTypeId"),
					nodeNonEnglish.findPath("description"),
					matchedNonEnglish,
					matchedEnglish);
			LOGGER.info(resultsPrinted);

			softAssert.assertTrue(matchedNonEnglish || matchedEnglish, "\n" + resultsPrinted);

			if (matchedNonEnglish) {
				JsonNode nodeEnglish = resultsEnglish.get(i);
				String resultsDescription = String.format("Event Code: %s, Event Id: %s, Desc (%s): %s Desc (en): %s "
						, nodeNonEnglish.findPath("event")
						, nodeNonEnglish.findPath("eventCode")
						, language
						, nodeNonEnglish.findPath("description")
						, nodeEnglish.findPath("description"));

				LOGGER.info(resultsDescription);

				softAssert.assertTrue(nodeNonEnglish.get("description").equals(nodeEnglish.get("description"))
						, resultsDescription);
			}
		}
		softAssert.assertAll();
	}

    protected void verifyPowerPlayGoals(Map parameters) {
        String gamesResponse = nhlStatsApiContentService.getGames(parameters, String.class, "2015021228/feed/live");

        JsonElement gamesObject = gson.fromJson(gamesResponse, JsonElement.class);

        String powerPlayGoalsHome = getPowerPlayGoals(gamesObject, "home");
        String powerPlayGoalsAway = getPowerPlayGoals(gamesObject, "away");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(!Strings.isNullOrEmpty(powerPlayGoalsHome), "'powerPlayGoalsHome' is not present in response or its value equals to null");
        softAssert.assertTrue(!Strings.isNullOrEmpty(powerPlayGoalsAway), "'powerPlayGoalsAway' is not present in response or its value equals to null");
        softAssert.assertAll();
    }

    private JsonNode getLiveFeedForLanguage(String language, String gameId) {
		return nhlStatsApiContentService.getGames(ImmutableMap.of("site", language), JsonNode.class,
				gameId + "/feed/live");
	}

	private List<String> getGameCenterDictionaryItems(String environment, String language) throws IOException, URISyntaxException {
		URI uri = new URI(
				String.format("https://s3.amazonaws.com/cms-nhl-app-content-%s/feed/sportsdata/data/keyValue/v1/nhlDictionary/%s-ALL/default-v1.json"
						, getProperS3Env(environment)
						, language));

		JsonNode rawDictionary = jsonContext().parse(uri.toURL().openStream()).read("$");

		List<String> dictionaryValues = new ArrayList<String>();

		Iterator<String> dictionary = rawDictionary.fieldNames();
		while (dictionary.hasNext()) {
			String node = dictionary.next();

			if (node.contains("gamecenter")) {
				LOGGER.info(String.format("Dictionary Item (%s): %s Value: %s", language, node, rawDictionary.get(node)));
				dictionaryValues.add(rawDictionary.get(node).asText());
			}
		}

		return dictionaryValues;
	}

	private String getProperS3Env(String env){
		if(env.equalsIgnoreCase("CMS")){
			return "prod";
		}
		return env;
	}
	
    /**
     * TODO implement this using JSON Path: https://github.com/jayway/JsonPath
     */
    protected String getPowerPlayGoals(JsonElement gamesObject, String teamType) {
        return gamesObject.getAsJsonObject().get("liveData").getAsJsonObject().
                get("boxscore").getAsJsonObject().get("teams").getAsJsonObject().
                get(teamType).getAsJsonObject().get("teamStats").getAsJsonObject().
                get("teamSkaterStats").getAsJsonObject().get("powerPlayGoals").toString();
    }
}
