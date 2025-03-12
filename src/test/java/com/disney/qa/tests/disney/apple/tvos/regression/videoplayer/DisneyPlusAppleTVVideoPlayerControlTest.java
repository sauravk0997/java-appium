package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVCommonPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVVideoPlayerPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.temporal.ValueRange;
import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.IConstantHelper.VIDEO_PLAYER_NOT_DISPLAYED;

public class DisneyPlusAppleTVVideoPlayerControlTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67528"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVideoPlayerControls() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        setAccount(getUnifiedAccount());
        logIn(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        Assert.assertTrue(videoPlayerTVPage.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayerTVPage.waitForVideoToStart();

        // Pause video with remote button
        home.clickPlay();
        int remainingTimeWhilePaused = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeBeforePause {}", remainingTimeWhilePaused);
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);

        // Make duration appear and get time that should match the previous time
        commonPage.clickDown(1);
        int remainingTime = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeAfterPause {}", remainingTime);
        Assert.assertEquals(remainingTime, remainingTimeWhilePaused, "Video was not paused");

        // Play video with remote button
        home.clickPlay();
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);
        commonPage.clickDown(1);
        int remainingTimeAfterPlay = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeAfterPlay {}", remainingTimeAfterPlay);
        Assert.assertTrue(remainingTimeWhilePaused > remainingTimeAfterPlay, "Video was not playing");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67538"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRewindAndForwardActionsWhilePlaying() {
        int actionTimes = 3;
        int secondsSkippedPerAction = 10;
        int expectedSkippedSeconds = actionTimes * secondsSkippedPerAction;
        int uiLatencyInSeconds = 10;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        videoPlayer.waitForVideoToStart();

        commonPage.clickDown(1);
        int remainingTimeBeforeForward = videoPlayer.getRemainingTimeThreeIntegers();
        commonPage.clickRight(actionTimes, 1, 1);
        commonPage.clickDown(1);
        int remainingTimeAfterForward = videoPlayer.getRemainingTimeThreeIntegers();
        Assert.assertTrue((remainingTimeBeforeForward - remainingTimeAfterForward) > expectedSkippedSeconds,
                String.format("The difference between the remaining time before forward skip (%d seconds) " +
                                "and the remaining time after the forward skip (%d seconds) is not greater than %d seconds",
                        remainingTimeBeforeForward, remainingTimeAfterForward, expectedSkippedSeconds));

        videoPlayer.waitForElementToDisappear(videoPlayer.getSeekbar(), FIVE_SEC_TIMEOUT);
        commonPage.clickDown(1);
        int remainingTimeBeforeRewind = videoPlayer.getRemainingTimeThreeIntegers();
        commonPage.clickLeft(actionTimes, 1, 1);
        commonPage.clickDown(1);
        int remainingTimeAfterRewind = videoPlayer.getRemainingTimeThreeIntegers();
        // Validate time difference using a range, to take into account the possible elapsed seconds of playback
        // between each rewind
        ValueRange acceptableDeltaRange =
                ValueRange.of(expectedSkippedSeconds - uiLatencyInSeconds, expectedSkippedSeconds);
        Assert.assertTrue(acceptableDeltaRange.isValidIntValue(remainingTimeAfterRewind - remainingTimeBeforeRewind),
                String.format("The difference between the remaining time after rewind skip (%d seconds) and " +
                                "the remaining time before the rewind skip (%d seconds) is not between %d-%d seconds",
                        remainingTimeAfterRewind, remainingTimeBeforeRewind,
                        acceptableDeltaRange.getMinimum(), acceptableDeltaRange.getMaximum()));
    }
}
