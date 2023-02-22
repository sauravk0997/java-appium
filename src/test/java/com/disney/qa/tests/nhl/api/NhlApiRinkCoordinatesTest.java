package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlGameStates;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by boyle on 2/22/17.
 */
public class NhlApiRinkCoordinatesTest extends NhlBaseDailyApiSetup {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test(dataProvider = "previousDayGamesList", description = "Check Game for Valid Rink Coordinates")
    public void checkCoordinates(String TUID, String gamePk) throws IOException, URISyntaxException {
        SoftAssert softAssert = new SoftAssert();

        LOGGER.info("Checking GamePk: " + gamePk);
        checkGame(softAssert, gamePk);

        softAssert.assertAll();
    }

    private void checkGame(SoftAssert softAssert, String gamePk) throws IOException, URISyntaxException {

        URI uri = new URI(String.format("https://statsapi.web.nhl.com/api/v1/game/%s/feed/live", gamePk));

        LOGGER.info("Game URL: " + uri.toString());

        JsonNode allPlays = jsonContext().parse(uri.toURL().openStream()).read("$.liveData.plays.allPlays[*]");

        Set<String> gameStates = new HashSet<>();
        gameStates.add(NhlGameStates.GOAL.getGameState());
        gameStates.add(NhlGameStates.SHOT.getGameState());
        gameStates.add(NhlGameStates.MISSED_SHOT.getGameState());
        gameStates.add(NhlGameStates.BLOCKED_SHOT.getGameState());
        gameStates.add(NhlGameStates.TAKEAWAY.getGameState());
        gameStates.add(NhlGameStates.GIVEAWAY.getGameState());
        gameStates.add(NhlGameStates.FACEOFF.getGameState());
        gameStates.add(NhlGameStates.PENALTY.getGameState());
        gameStates.add(NhlGameStates.HIT.getGameState());

        for (JsonNode play : allPlays) {
            JsonNode event = jsonContext().parse(play).read("$.result.event");
            JsonNode eventId = jsonContext().parse(play).read("$.about.eventId");
            if (gameStates.contains(event.asText())) {
                JsonNode coordinates = jsonContext().parse(play).read("$.coordinates");
                LOGGER.info(String.format("Event ID: %s Event: %s Coordinates %s", eventId, event, coordinates));
                JsonNode coordinateX = jsonContext().parse(coordinates).read("$.x");
                JsonNode coordinateY = jsonContext().parse(coordinates).read("$.y");

                boolean blnCoordX = coordinateX.asDouble() <= -100 || coordinateX.asDouble() >= 100;
                boolean blnCoordY = coordinateY.asDouble() <= -42.5 || coordinateY.asDouble() >= 42.5;

                String msgAssert = prepareValidationMessage(blnCoordX, blnCoordY, coordinateX, coordinateY);

                softAssert.assertFalse(blnCoordX || blnCoordY,
                        String.format("Expected Valid Coordinates for Event (%s) Event Id (%s), Found:%s\n", event, eventId, msgAssert));
            }
        }
    }

    private String prepareValidationMessage(boolean blnCoordX, boolean blnCoordY, JsonNode coordinateX, JsonNode coordinateY) {
        String msgCoordX = "";
        String msgCoordY = "";

        if (blnCoordX) {
            msgCoordX = String.format(" X Coordinate is Invalid (%s)", coordinateX);
        }

        if (blnCoordY) {
            msgCoordY = String.format(" Y Coordinate is Invalid (%s)", coordinateY);
        }

        return String.format("%s%s", msgCoordX, msgCoordY);
    }
}
