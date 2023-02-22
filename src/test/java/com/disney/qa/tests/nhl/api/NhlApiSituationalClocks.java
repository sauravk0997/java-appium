package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlSituationalMapping;
import com.disney.qa.api.nhl.pojo.SituationalClockGame;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.DocumentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by boyle on 4/13/17.
 */
public class NhlApiSituationalClocks extends NhlBaseDailyApiSetup {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    NhlSituationalMapping situationalMapping = new NhlSituationalMapping();

    @Test(dataProvider = "previousDayGamesList", description = "Check Game for Valid Situational Clock Data")
    public void verifySituationalClocks(String TUID, String gamePk) {
        SoftAssert softAssert = new SoftAssert();

        ArrayNode gameTimeStamps = nhlStatsApiContentService
                .getGames(ImmutableMap.of("", ""), ArrayNode.class, gamePk + "/feed/live/timestamps");

        for (JsonNode gameTimeCode : gameTimeStamps) {
            JsonNode specificGameTime = nhlStatsApiContentService
                    .getGames(ImmutableMap.of("timecode", gameTimeCode.asText()), JsonNode.class, gamePk + "/feed/live");

            SituationalClockGame gameSituation = createSituationalObject(specificGameTime);

            if (!"Preview".equalsIgnoreCase(gameSituation.getGameState())) {
                LOGGER.info(String.format("Game State (%s) Game Type (%s) InSituation (%s) Period (%s) Skaters Away (%s) Home (%s)"
                        , gameSituation.getGameState(), gameSituation.getGameType(), gameSituation.getInSituation()
                        , gameSituation.getPeriod(), gameSituation.getSkatersAway(), gameSituation.getSkatersHome()));
                if (gameSituation.getSkatersAway() != 0) {
                    checkSituationStatus(softAssert, gameSituation, gameTimeCode.asText());
                }

            }
        }

        softAssert.assertAll();
    }

    private SituationalClockGame createSituationalObject(JsonNode node) {

        DocumentContext dc = jsonContext().parse(node);

        SituationalClockGame gameSituation = new SituationalClockGame();
        gameSituation.setGameState(node.findPath("abstractGameState").asText());
        gameSituation.setGameType(dc.read("$.gameData.game.type").toString());
        gameSituation.setInSituation(node.findPath("inSituation").asBoolean());
        gameSituation.setPeriod(node.findPath("currentPeriod").asInt());
        gameSituation.setSkatersAway(dc.read("$.liveData.linescore.teams.away.numSkaters").toString());
        gameSituation.setSkatersHome(dc.read("$.liveData.linescore.teams.home.numSkaters").toString());

        return gameSituation;
    }

    private void checkSituationStatus(SoftAssert softAssert, SituationalClockGame sg, String gameTimeCode) {
        List<String> mappings;

        String scenario = String.format("%s%s", sg.getSkatersAway(), sg.getSkatersHome());

        if (sg.getInSituation()) {
            LOGGER.info("Situation is True.");
            mappings = checkMappings(sg, "T");
        } else {
            LOGGER.info("Situation is False.");
            mappings = checkMappings(sg, "F");
        }

        boolean foundMapping = false;
        for (String mapping : mappings) {
            if (mapping.equalsIgnoreCase(scenario)) {
                foundMapping = true;
                break;
            }
        }
        softAssert.assertTrue(foundMapping,
                String.format("\nExpected to find appropriate mapping for (%s) at Game Type (%s) Game Period (%s) Situation (%s) Time Code (%s)",
                        scenario, sg.getGameType(), sg.getPeriod(), sg.getInSituation(), gameTimeCode));
    }

    private List<String> checkMappings(SituationalClockGame sg, String situation) {
        return situationalMapping.getMapping(String.format("%s%s%s", sg.getPeriod(), sg.getGameType().replace("\"", ""), situation));
    }
}
