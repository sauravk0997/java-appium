package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.support.JsonIgnoreComparator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * TODO [major] add one more check:
 * player data (current season - confirm fields, not values)
 * http://www.nhl.com/feed/nhl/playerdata/expanded/playercard.json?id=8471675
 * <p/>
 * Comments:
 * <p/>
 * Below are sample calls for the NHL feeds we hit from the api. All of the feeds besides the full game data will require an auth token generated using the api key.  For most of these feeds, it should be enough to compare the feed response to a stored one .  The one exception is the player card feed, which only returns data for the current season, so those values will change with every game the player is in.  For that case,  if it's possible we'd like to confirm the field names have remained consistent and not be concerned with the changes to the values.
 */
public class NhlFeedConsistencyTest extends BaseNhlApiTest {

    @Test
    public void verifyScheduleSingleDay() throws IOException, JSONException {
        String scheduleResponse = nhlFeedContentService.getSchedule(ImmutableMap.of("date", "03/04/2015"), String.class);
        verifyResponseWithJsonAssert("nhl/api/feed/schedule_date_03_04_2015.json", scheduleResponse);
    }

    @Test
    public void verifyScheduleSeason() throws IOException, JSONException {
        String scheduleResponse = nhlFeedContentService.getSchedule(ImmutableMap.of("season", "20142015"), String.class);
        verifyResponseWithJsonAssert("nhl/api/feed/schedule_season_20142015.json", scheduleResponse);
    }

    @Test
    public void verifyScheduleDateRange() throws IOException, JSONException {
        String scheduleResponse = nhlFeedContentService.getSchedule(ImmutableMap.of("startDate", "01/01/2015", "endDate", "04/01/2015"), String.class);
        verifyResponseWithJsonAssert("nhl/api/feed/schedule_season_date_range.json", scheduleResponse);
    }

    @Test
    public void verifyGamePlayByPlay() throws IOException, JSONException {
        String gamesResponse = nhlFeedContentService.getGames(ImmutableMap.of("id", "2014021083"), String.class, "playbyplay.json");
        verifyResponseWithJsonAssert("nhl/api/feed/games_play_by_play.json", gamesResponse);
    }

    @Test
    public void verifyGameBoxScore() throws IOException, JSONException {
        String gamesResponse = nhlFeedContentService.getGames(ImmutableMap.of("id", "2014021083"), String.class, "boxscore.json");
        verifyResponseWithJsonAssert("nhl/api/feed/games_boxscore.json", gamesResponse);
    }

    @Test
    public void verifyTeamLeaders() throws IOException, JSONException {
        String gamesResponse = nhlFeedContentService.getGames(ImmutableMap.of("teamId", "1", "season", "20142015", "gameType", "2"), String.class, "statsleaders.json");
        verifyResponseWithJsonAssert("nhl/api/feed/games_team_leaders_id_1_season_20142015.json", gamesResponse);
    }

    @Test
    public void verifyStandings() throws IOException, JSONException {
        String standingsResponse = nhlFeedContentService.getStandings(ImmutableMap.of("season", "20142015"), String.class);
        verifyResponseWithJsonAssert("nhl/api/feed/standings_20142015.json", standingsResponse);
    }

    @Test
    public void verifyFullGameData() throws IOException, JSONException {
        String gamesResponse = restTemplate.getForEntity("http://live.nhl.com/GameData/20152016/2015020300/gc/gcsb-pid.json", String.class).getBody();
        verifyResponseWithJsonAssert("nhl/api/feed/full_game_data.json", gamesResponse);
    }

    protected void verifyResponseWithJsonAssert(String responseDataFile, String actualResponse) throws IOException, JSONException {
        String expectedResponse = fileUtils.getResourceFileAsString(responseDataFile);

        JSONComparator jsonCustomIgnoreComparator =
                new JsonIgnoreComparator(JSONCompareMode.LENIENT, Lists.newArrayList("timestamp", "updated", "sweater",
                        // ticket URLs
                        "attUrl", "bstUrl", "cmtUrl", "ctUrl", "mbstUrl", "mtUrl", "tUrl", "amtUrl"));

        JSONAssert.assertEquals(expectedResponse, actualResponse, jsonCustomIgnoreComparator);
    }
}
