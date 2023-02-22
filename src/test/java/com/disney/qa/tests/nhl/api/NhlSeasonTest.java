package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class NhlSeasonTest extends BaseNhlApiTest {

    @Test
    public void verifyActiveTeamsForSpecificSeasonOneSeason() {
        String seasonRawResponse = nhlStatsApiContentService.getSeasons(
                ImmutableMap.of("hydrate", "teams"), String.class, "19181919");
        String teamRawResponse = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "19181919"), String.class);

        List<String> teamsActual = JsonPath.read(seasonRawResponse, "$..teams[*].name");
        List<String> teamsExpected = JsonPath.read(teamRawResponse, "$..teams[*].name");

        Assert.assertEquals(teamsActual, teamsExpected, "Incorrect teams for season 1918-1919");
    }

    @Test
    public void verifyActiveTeamsForSpecificSeasonTwoSeasonsTeamDetails() {
        JsonNode seasonRawResponse = nhlStatsApiContentService.getSeasons(
                ImmutableMap.of("season", "19181919,20102011", "hydrate", "teams(team)"), JsonNode.class);
        JsonNode teamRawResponse1 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "19181919"), JsonNode.class);
        JsonNode teamRawResponse2 = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "20102011"), JsonNode.class);

        JsonNode teams19181919actual = JsonPath.using(jsonPathJacksonConfiguration).parse(seasonRawResponse).
                read("$..seasons[?(@.seasonId=='19181919')].teams");
        JsonNode teams20102011actual = JsonPath.using(jsonPathJacksonConfiguration).parse(seasonRawResponse).
                read("$..seasons[?(@.seasonId=='20102011')].teams");

        JsonNode teams19181919expected = JsonPath.using(jsonPathJacksonConfiguration).parse(teamRawResponse1).
                read("$..teams");
        JsonNode teams20102011expected = JsonPath.using(jsonPathJacksonConfiguration).parse(teamRawResponse2).
                read("$..teams");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(teams19181919actual, teams19181919expected, "Incorrect teams for season 1918-1919");
        softAssert.assertEquals(teams20102011actual, teams20102011expected, "Incorrect teams for season 2010-2011");

        softAssert.assertAll();
    }

    @Test
    public void verifyActiveTeamsForSpecificSeasonTeamWithRoster() {
        JsonNode seasonRawResponse = nhlStatsApiContentService.getSeasons(
                ImmutableMap.of("season", "19871988", "hydrate", "teams(team(roster))"), JsonNode.class);
        JsonNode teamRawResponse = nhlStatsApiContentService.getTeams(
                ImmutableMap.of("season", "19871988", "hydrate", "roster"), JsonNode.class);

        JsonNode teamsActual = JsonPath.using(jsonPathJacksonConfiguration).parse(seasonRawResponse).
                read("$..teams");
        JsonNode teamsExpected = JsonPath.using(jsonPathJacksonConfiguration).parse(teamRawResponse).
                read("$..teams");
        
        Assert.assertEquals(teamsActual, teamsExpected, "Incorrect teams for season 1987-1988");
    }
}
