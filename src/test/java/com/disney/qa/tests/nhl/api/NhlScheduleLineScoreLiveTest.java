package com.disney.qa.tests.nhl.api;


import com.disney.qa.api.nhl.NhlContentService;
import com.disney.qa.api.nhl.NhlContentServiceBuilder;
import com.disney.qa.api.nhl.pojo.stats.schedule.Game;
import com.disney.qa.api.nhl.pojo.stats.schedule.Linescore;
import com.disney.qa.api.nhl.pojo.stats.schedule.Schedule;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NhlScheduleLineScoreLiveTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected Gson gson = new Gson();

    protected NhlContentService nhlFeedContentServiceHostF;

    @Test
    public void verifyScheduleLineScoreLiveTest() throws ParseException {
        nhlFeedContentServiceHostF = NhlContentServiceBuilder.newInstance().withAllParametersForFeedHostF(restTemplate).build();

        SoftAssert softAssert = new SoftAssert();

        // looks like issue for 2016-02-25 - 'currentPeriod' = 0
        Schedule schedule = nhlStatsApiContentService.getSchedule(
                ImmutableMap.of("expand", "schedule.linescore"), Schedule.class);

        if (schedule.getDates().size() != 0) {
            verifySchedule(schedule, softAssert);
        } else {
            LOGGER.info("There are no games today");
        }
    }

    protected void verifySchedule(Schedule schedule, SoftAssert softAssert) throws ParseException {
        for (Game game : schedule.getDates().get(0).getGames()) {

            String gameResponse = nhlStatsApiContentService.getGames(
                    null, String.class, String.format("%s/feed/live", game.getGamePk()));
            JsonElement gameObject = gson.fromJson(gameResponse, JsonElement.class);

            Linescore actualLinescore = game.getLinescore();
            JsonElement expectedLineScore = gameObject.getAsJsonObject().get("liveData").getAsJsonObject().get("linescore");

            int actualCurrentPeriod = actualLinescore.getCurrentPeriod().intValue();
            int expectedCurrentPeriod = expectedLineScore.getAsJsonObject().get("currentPeriod").getAsInt();
            verifyCurrentPeriod(softAssert, actualCurrentPeriod, expectedCurrentPeriod, game.getGamePk().toString(), "Stats 'feed/live'");

            String actualCurrentPeriodTimeRemaining = actualLinescore.getCurrentPeriodTimeRemaining();
            String expectedCurrentPeriodTimeRemaining;
            if (expectedLineScore.getAsJsonObject().get("currentPeriodTimeRemaining") != null) {
                expectedCurrentPeriodTimeRemaining = expectedLineScore.getAsJsonObject().get("currentPeriodTimeRemaining").getAsString();
                verifyCurrentPeriodTimeRemaining(softAssert, actualCurrentPeriodTimeRemaining, expectedCurrentPeriodTimeRemaining, game.getGamePk().toString(), 0, "Stats 'feed/live'");
            }

            String boxScoreResponse = nhlFeedContentServiceHostF.getBoxScore(ImmutableMap.of("id", game.getGamePk().toString()), String.class);
            JsonElement boxScoreObject = gson.fromJson(boxScoreResponse, JsonElement.class);

            // if 'period' property is absent, game is not started
            int expectedCurrentPeriodFeed = 0;
            if (boxScoreObject.getAsJsonObject().get("period") != null) {
                expectedCurrentPeriodFeed = boxScoreObject.getAsJsonObject().get("period").getAsInt();
                String expectedCurrentPeriodTimeRemainingFeed = boxScoreObject.getAsJsonObject().get("timeRemaining").getAsString();

                verifyCurrentPeriod(softAssert, actualCurrentPeriod, expectedCurrentPeriodFeed, game.getGamePk().toString(), "Feed");
                verifyCurrentPeriodTimeRemaining(softAssert, actualCurrentPeriodTimeRemaining, expectedCurrentPeriodTimeRemainingFeed, game.getGamePk().toString(), expectedCurrentPeriodFeed, "Feed");
            }

            LOGGER.info("\n");
        }

        softAssert.assertAll();
    }

    protected void verifyCurrentPeriod(SoftAssert softAssert, int actualCurrentPeriod, int expectedCurrentPeriod, String gameId, String sourceForComparing) {
        LOGGER.info(String.format("Actual 'currentPeriod' value for [game: %s]: %s", gameId, actualCurrentPeriod));
        LOGGER.info(String.format("Expected 'currentPeriod' value for [game: %s]: %s", gameId, expectedCurrentPeriod));

        softAssert.assertTrue(actualCurrentPeriod == expectedCurrentPeriod,
                String.format("\n'currentPeriod' is not the same for game [gamePk: %s]. Actual: %s, expected: %s, source: %s",
                        gameId, actualCurrentPeriod, expectedCurrentPeriod, sourceForComparing));
    }

    protected void verifyCurrentPeriodTimeRemaining(
            SoftAssert softAssert, String actualCurrentPeriodTimeRemaining, String expectedCurrentPeriodTimeRemaining, String gameId, int currentPeriod, String sourceForComparing) throws ParseException {
        LOGGER.info(String.format("Actual 'currentPeriodTimeRemaining' value for [game: %s]: %s", gameId, actualCurrentPeriodTimeRemaining));
        LOGGER.info(String.format("Expected 'currentPeriodTimeRemaining' value for [game: %s]: %s", gameId, expectedCurrentPeriodTimeRemaining));

        SimpleDateFormat format = new SimpleDateFormat("mm:ss");

        if (actualCurrentPeriodTimeRemaining == null) {
            softAssert.fail(String.format("\nactual 'currentPeriodTimeRemaining' is absent in response for game [game: %s]", gameId));
            return;
        }

        if ("FINAL".equals(expectedCurrentPeriodTimeRemaining.toUpperCase()) || "END".equals(expectedCurrentPeriodTimeRemaining.toUpperCase())) {
            try {
                Date actualTime = format.parse(actualCurrentPeriodTimeRemaining);
                int timeDifference = new DateTime(actualTime).getMinuteOfHour();

                softAssert.assertTrue(timeDifference <= 5,
                        String.format("\ndifference in 'currentPeriodTimeRemaining' values for game [game: %s] is more than 5 minutes: %s, source: %s", gameId, timeDifference, sourceForComparing));
            } catch (ParseException e) {
                softAssert.assertTrue(actualCurrentPeriodTimeRemaining.toUpperCase().equals(expectedCurrentPeriodTimeRemaining.toUpperCase()),
                        String.format("\n'currentPeriodTimeRemaining' is not the same for game [game: %s]. Actual: %s, expected: %s, source: %s",
                                gameId, actualCurrentPeriodTimeRemaining, expectedCurrentPeriodTimeRemaining, sourceForComparing));
            }
        } else {
            // TODO remove duplicated code
            Date actualTime = null;
            try {
                actualTime = format.parse(actualCurrentPeriodTimeRemaining);
            } catch (ParseException e) {
                softAssert.assertTrue(actualCurrentPeriodTimeRemaining.equals(expectedCurrentPeriodTimeRemaining),
                        String.format("\n'currentPeriodTimeRemaining' is not the same for game [game: %s]. Actual: %s, expected: %s, source: %s",
                                gameId, actualCurrentPeriodTimeRemaining, expectedCurrentPeriodTimeRemaining, sourceForComparing));
            }

            Date expectedTime = format.parse(expectedCurrentPeriodTimeRemaining);

            int timeDifference = Math.abs(Minutes.minutesBetween(new DateTime(actualTime), new DateTime(expectedTime)).getMinutes());

            if ("Feed".equals(sourceForComparing) &&
                    currentPeriod == 5 && "05:00".equals(expectedCurrentPeriodTimeRemaining) && gameId.toCharArray()[5] == '2') {
                softAssert.assertTrue("00:00".equals(actualCurrentPeriodTimeRemaining),
                        String.format("\n'timeRemaining' should be turned to '00:00', but actual value is: %s. Feed time remaining: %s, source",
                                actualCurrentPeriodTimeRemaining, expectedCurrentPeriodTimeRemaining, sourceForComparing));

                return;
            }

            LOGGER.info(String.format("Time difference in remaining time for game [game: %s] is: %s", gameId, timeDifference));

            softAssert.assertTrue(timeDifference <= 5,
                    String.format("\ndifference in 'currentPeriodTimeRemaining' values for game [game: %s] is more than 5 minutes: %s, source: %s", gameId, timeDifference, sourceForComparing));
        }
    }
}
