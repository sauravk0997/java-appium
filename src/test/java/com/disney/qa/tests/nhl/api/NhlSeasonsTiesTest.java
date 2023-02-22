package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.pojo.stats.seasons.Season;
import com.disney.qa.api.nhl.pojo.stats.seasons.Seasons;
import com.disney.qa.api.nhl.pojo.stats.standings.DivisionRecord;
import com.disney.qa.api.nhl.pojo.stats.standings.OverallRecord;
import com.disney.qa.api.nhl.pojo.stats.standings.Record;
import com.disney.qa.api.nhl.pojo.stats.standings.Standings;
import com.disney.qa.api.nhl.pojo.stats.standings.TeamRecord;
import com.google.common.collect.ImmutableMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NhlSeasonsTiesTest extends BaseNhlApiTest {

    @DataProvider(name = "seasonsProvider")
    public Iterator<Object[]> seasonsProvider() {

        Seasons seasons = nhlStatsApiContentService.getSeasons(Collections.EMPTY_MAP, Seasons.class);

        List<Object[]> seasonsData = new ArrayList<>();

        for (Season season : seasons.getSeasons()) {
            seasonsData.add(new Object[]{
                    String.format("TUID:Season: %s, ties in use: %s", season.getSeasonId(), season.getTiesInUse()), season});
        }

        return seasonsData.iterator();
    }

    @Test(dataProvider = "seasonsProvider")
    public void verifyTies(String TUID, Season season) {
        Standings standings = nhlStatsApiContentService.getStandings(
                ImmutableMap.of("season", season.getSeasonId(), "expand", "standings.record"),
                Standings.class);

        SoftAssert softAssert = new SoftAssert();

        for (Record record : standings.getRecords()) {
            for (TeamRecord teamRecord : record.getTeamRecords()) {

                verifyTies(softAssert, teamRecord.getLeagueRecord().getTies(), season.getTiesInUse(),
                        teamRecord.getLeagueRecord().getType(), "leagueRecord");

                for (OverallRecord overallRecord : teamRecord.getRecords().getOverallRecords()) {
                    verifyTies(softAssert, overallRecord.getTies(), season.getTiesInUse(), overallRecord.getType(),
                            String.format("overallRecord, %s", overallRecord.getType()));
                }

                for (DivisionRecord divisionRecord : teamRecord.getRecords().getDivisionRecords()) {
                    verifyTies(softAssert, divisionRecord.getTies(), season.getTiesInUse(), divisionRecord.getType(),
                            String.format("divisionRecord, %s", divisionRecord.getType()));
                }
            }
        }

        softAssert.assertAll();
    }

    protected void verifyTies(SoftAssert softAssert, Integer ties, boolean tiesInUse, String type, String details) {
        if (tiesInUse && !"shootOuts".equals(type)) {
            softAssert.assertNotNull(ties, String.format("Ties are absent, details: %s", details));
        } else {
            softAssert.assertNull(ties, String.format("Ties are present, details: %s", details));
        }
    }
}
