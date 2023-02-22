package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.pojo.Schedule;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

/**
 * TODO [minor] add more parameters from XLS: team name or abbreviation to logger
 * TODO [minor] rename/refactor XLS 'abbreviation' sheet
 * TODO [minor] rename 'id' in XLS to 'teamID' for more clear reporting in email
 * RESOLVED [major] review feed pojo classes
 * <p>
 * Auth generator: MLBAMCoreFeeds.html
 * <p>
 * Thanks Mikita
 * This looks great.  Adding additional checks for the other fields would probably be highest priority for us right now.
 * Emily and I will give you some additional functionality for the schedule to test next.
 * For example, we allow the ability to grab more info about the teams in the schedule, and we need check to make sure that works correctly.
 */
public class NhlScheduleSampleTest extends BaseNhlApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String SEASON_2015_2016_START_DATE = "10/07/2015";

    public static final String SEASON_2015_2016_END_DATE = "06/15/2016";

    @Test(dataProvider = "SingleDataProvider")
    @XlsDataSourceParameters(sheet = "abbreviation", dsArgs = "id", dsUid = "id")
    public void scheduleTest(String teamId) {
        Schedule feedSchedule = nhlFeedContentService.getSchedule(
                Integer.valueOf(teamId), SEASON_2015_2016_START_DATE, SEASON_2015_2016_END_DATE);
        LOGGER.info(String.format("Amount of games in Feed response for team {ID: %s}: %s", teamId, feedSchedule.getGameList().size()));

        Schedule statsSchedule = nhlStatsApiContentService.getSchedule(
                Integer.valueOf(teamId), SEASON_2015_2016_START_DATE, SEASON_2015_2016_END_DATE);
        LOGGER.info(String.format("Amount of games in Stats API response for team {ID: %s}: %s", teamId, statsSchedule.getGameList().size()));

        Assert.assertEquals(statsSchedule.getGameList().size(), feedSchedule.getGameList().size(),
                String.format("Amount of games in Feed and Stats API responses for team {ID: %s} are different. Stats API: %s, Feed: %s",
                        teamId, statsSchedule.getGameList().size(), feedSchedule.getGameList().size()));
    }
}
