package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.pojo.IntermissionClockGame;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.DocumentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class NhlApiIntermissionClocks extends NhlBaseDailyApiSetup {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private int previousIntermissionTimeRemaining;
	private int previousIntermissionTimeElapsed;
	private int previousPeriod;
	private int previousGameTimeCode;

	@BeforeMethod
	public void setup() {
		previousIntermissionTimeRemaining = 0;
		previousIntermissionTimeElapsed = 0;
		previousPeriod = 1;
		previousGameTimeCode = 0;
	}

	@Test(dataProvider = "currentDayGamesList", description = "Check Game for Valid Intermission Clock Data")
	public void verifyIntermissionClocks(String TUID, String gamePk) {
		SoftAssert softAssert = new SoftAssert();

		ArrayNode gameTimeStamps = nhlStatsApiContentService.getGames(ImmutableMap.of("", ""), ArrayNode.class,
				gamePk + "/feed/live/timestamps");

		for (JsonNode gameTimeCode : gameTimeStamps) {
			JsonNode specificGameTime = nhlStatsApiContentService.getGames(
					ImmutableMap.of("timecode", gameTimeCode.asText()), JsonNode.class, gamePk + "/feed/live");

			IntermissionClockGame gameIntermission = createIntermissionObject(specificGameTime);

			if (!"Preview".equalsIgnoreCase(gameIntermission.getGameState())) {
				LOGGER.info(String.format(
						"Game State - (%s), Game Type - (%s), Intermission - (%s), Period - (%s), Intermission duration - (%s) minutes.",
						gameIntermission.getGameState(), gameIntermission.getGameType(),
						gameIntermission.isIntermission(), gameIntermission.getPeriod(),
						getIntermissionDuration(gameIntermission.getIntermissionTimeRemaining(),
								gameIntermission.getIntermissionTimeElapsed())));
				checkIntermissionClockStatus(softAssert, gameIntermission);

				int currentGameTimeCode = Integer
						.parseInt(gameTimeCode.asText().substring(gameTimeCode.asText().indexOf('_') + 1));
				if (previousGameTimeCode < currentGameTimeCode) {
					checkIntermissionValues(softAssert, gameIntermission, gameTimeCode.asText());
					previousGameTimeCode = currentGameTimeCode;
				}
			}
		}
		softAssert.assertAll();
	}

	private IntermissionClockGame createIntermissionObject(JsonNode node) {

		DocumentContext dc = jsonContext().parse(node);

		IntermissionClockGame gameIntermission = new IntermissionClockGame();
		gameIntermission.setGameState(node.findPath("abstractGameState").asText());
		gameIntermission.setGameType(dc.read("$.gameData.game.type").toString().replace("\"", ""));
		gameIntermission.setPeriod(node.findPath("currentPeriod").asInt());
		gameIntermission.setIntermission(node.findPath("inIntermission").asBoolean());
		gameIntermission.setIntermissionTimeRemaining(node.findPath("intermissionTimeRemaining").asInt());
		gameIntermission.setIntermissionTimeElapsed(node.findPath("intermissionTimeElapsed").asInt());
		gameIntermission.setCurrentPeriodTimeRemaining(node.findPath("currentPeriodTimeRemaining").asText());

		return gameIntermission;
	}

	private void checkIntermissionClockStatus(SoftAssert softAssert, IntermissionClockGame intermissionClock) {

		String gameType = intermissionClock.getGameType();

		switch (gameType) {
		case "PR":
			checkIntermissionDuration(softAssert, intermissionClock, 5);
			break;
		case "R":
			checkIntermissionDuration(softAssert, intermissionClock, 5);
			break;
		case "P":
			checkIntermissionDuration(softAssert, intermissionClock, 15);
			break;
		default:
			LOGGER.info("Unsupported game type: " + gameType);
			break;
		}
	}

	private int getIntermissionDuration(int timeRemaining, int timeElapsed) {
		return (timeRemaining + timeElapsed) / 60;
	}

	private void checkIntermissionDuration(SoftAssert softAssert, IntermissionClockGame intermissionClock,
			int expectedDuration3rd) {
		int period = intermissionClock.getPeriod();
		int actualDuration = getIntermissionDuration(intermissionClock.getIntermissionTimeRemaining(),
				intermissionClock.getIntermissionTimeElapsed());

		if (intermissionClock.isIntermission()) {
			softAssert.assertTrue("END".equals(intermissionClock.getCurrentPeriodTimeRemaining()),
					"Incorrect currentPeriodTimeRemaining field value during intermission!");

			if (period == 1 || period == 2) {
				softAssert.assertTrue(actualDuration == 18, "Incorrect intermission duration for: 1st or 2nd period !");
			} else {
				softAssert.assertTrue(actualDuration == expectedDuration3rd,
						"Incorrect intermission duration for 3rd period!");
			}
		}
	}

	private void checkIntermissionValues(SoftAssert softAssert, IntermissionClockGame intermissionClock, String timecode) {
		if (intermissionClock.isIntermission()) {
			int intermissionTimeRemaining = intermissionClock.getIntermissionTimeRemaining();
			int intermissionTimeElapsed = intermissionClock.getIntermissionTimeElapsed();
			int period = intermissionClock.getPeriod();

			LOGGER.info(String.format("intermissionTimeRemaining - (%s), intermissionTimeElapsed - (%s)",
					intermissionTimeRemaining, intermissionTimeElapsed));

			if (previousPeriod < period) {
				previousIntermissionTimeRemaining = 0;
				previousIntermissionTimeElapsed = 0;
				previousPeriod = period;
			}

			if (previousIntermissionTimeRemaining == 0) {
				previousIntermissionTimeRemaining = intermissionTimeRemaining;
				previousIntermissionTimeElapsed = intermissionTimeElapsed;
			} else {
				softAssert.assertTrue(previousIntermissionTimeRemaining >= intermissionTimeRemaining,
						String.format("Incorrect value of IntermissionTimeRemaining! Prior Time: (%s) Current Time: (%s) Time Code: (%s)"
								, previousIntermissionTimeRemaining, intermissionTimeRemaining, timecode));
				previousIntermissionTimeRemaining = intermissionTimeRemaining;

				softAssert.assertTrue(previousIntermissionTimeElapsed <= intermissionTimeElapsed,
						String.format("Incorrect value of IntermissionTimeElapsed! Prior Time: (%s) Current Time: (%s) Time Code: (%s)"
								, previousIntermissionTimeElapsed, intermissionTimeElapsed, timecode));
				previousIntermissionTimeElapsed = intermissionTimeElapsed;
			}
		}
	}
}
