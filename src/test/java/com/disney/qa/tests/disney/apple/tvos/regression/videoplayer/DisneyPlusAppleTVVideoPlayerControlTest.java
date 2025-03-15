package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVCommonPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
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

import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;

public class DisneyPlusAppleTVVideoPlayerControlTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String VIDEO_NOT_PAUSED = "Video was not paused";
    private static final String VIDEO_NOT_PLAYING = "Video was not playing";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67528"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVideoPlayerControls() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        logIn(getUnifiedAccount());

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
        Assert.assertEquals(remainingTime, remainingTimeWhilePaused, VIDEO_NOT_PAUSED);

        // Play video with remote button
        home.clickPlay();
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);
        commonPage.clickDown(1);
        int remainingTimeAfterPlay = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeAfterPlay {}", remainingTimeAfterPlay);
        Assert.assertTrue(remainingTimeWhilePaused > remainingTimeAfterPlay, VIDEO_NOT_PLAYING);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67546"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVideoPlayerControlsScrubThumbnail() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        logIn(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayerTVPage.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayerTVPage.waitForVideoToStart();

        // Pause video with remote button
        home.clickPlay();
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.ONE_HUNDRED_TWENTY_SEC_TIMEOUT);

        // Click fast-forward on the remote and get the thumbnail position in time
        commonPage.clickRight(3, 1, 1);
        Assert.assertTrue(videoPlayerTVPage.getThumbnailView().isPresent(), "Thumbnail preview did not appear");
        int thumbnailTimeline = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("thumbnailTimeline {}", thumbnailTimeline);
        commonPage.clickUp(2);

        // Play video with remote button and verify it started to play
        home.clickPlay();
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);
        commonPage.clickDown(1);
        int remainingTimeAfterPlay = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeAfterPlay {}", remainingTimeAfterPlay);
        Assert.assertTrue(thumbnailTimeline > remainingTimeAfterPlay, VIDEO_NOT_PLAYING);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67554"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeekingThumbnailRectangleBehavior() {
        int numberOfAttemptsToReachBeginning = 5;
        int numberOfAttemptsToReachEnd = 40;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickPlay();

        //Move left to the start of the playback and check thumbnail is aligned with the beginning of the seek bar
        commonPage.clickLeftTillBeginningOfPlaybackIsReached(
                videoPlayer.getSeekbar(), numberOfAttemptsToReachBeginning, 1, 1);
        Assert.assertTrue(videoPlayer.isThumbnailAlignedWithTheSeekBar(),
                "Thumbnail rectangle wasn't aligned with the beginning of the seek bar");
        int firstThumbnailLeftXCoordinate = videoPlayer.getThumbnailView().getLocation().getX();

        //Fast-Forward 10 times and validate thumbnail is no longer aligned with the beginning of the seek bar
        commonPage.clickRight(10, 1, 1);
        int secondThumbnailLeftXCoordinate = videoPlayer.getThumbnailView().getLocation().getX();
        Assert.assertTrue(secondThumbnailLeftXCoordinate > firstThumbnailLeftXCoordinate,
                "Thumbnail rectangle didn't detach from the left side of the screen as the playback was moved forward");

        //Fast-Forward 3 times and validate thumbnail has moved along through the content seek bar
        commonPage.clickRight(10, 1, 1);
        int thirdThumbnailLeftXCoordinate = videoPlayer.getThumbnailView().getLocation().getX();
        Assert.assertTrue(thirdThumbnailLeftXCoordinate > secondThumbnailLeftXCoordinate,
                "Thumbnail rectangle didn't move along to the right as the playback was moved forward");

        //Fast-Forward until the end of the playback and validate the thumbnail is "docked" at the end of the seekbar
        commonPage.clickRightTillEndOfPlaybackIsReached(
                videoPlayer.getSeekbar(), numberOfAttemptsToReachEnd, 1, 1);
        Assert.assertTrue(videoPlayer.isThumbnailAlignedWithTheEndOfTheSeekBar(),
                "Thumbnail rectangle wasn't aligned with the end of the seek bar");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121848"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVideoPlayerControlsUIVOD() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        int times = 3;
        int secondsSkippedPerAction = 10;
        int expectedSkippedSeconds = times * secondsSkippedPerAction;
        int uiLatencyInSeconds = 10;
        // Total time of the movie
        int totalDurationTime = 7526;
        logIn(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        Assert.assertTrue(videoPlayerTVPage.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayerTVPage.waitForVideoToStart();


        // Pause and forward video validations
        commonPage.clickDown(1);
        int remainingTimeBeforeFF = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeBeforeFF {}", remainingTimeBeforeFF);
        commonPage.clickRight(3, 1, 1);
        commonPage.clickPlay();
        commonPage.clickDown(1);
        int timeAfterFF = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("timeAfterFF {}", timeAfterFF);
        Assert.assertTrue(remainingTimeBeforeFF > timeAfterFF, "Forward did not work as expected");
        home.waitForElementToDisappear(videoPlayerTVPage.getSeekbar(), DisneyAbstractPage.ONE_HUNDRED_TWENTY_SEC_TIMEOUT);

        // Actions required to properly restart the video because the button disappears quickly from screen
        commonPage.clickUp(3);
        Assert.assertTrue(videoPlayerTVPage.getRestartBtn().isPresent(), RESTART_BTN_NOT_DISPLAYED);
        commonPage.clickUp(3);
        commonPage.clickLeft();
        commonPage.clickRight();
        videoPlayerTVPage.getRestartBtn().click();
        home.waitForPresenceOfAnElement(videoPlayerTVPage.getTitleVideoLabel());
        home.waitForElementToDisappear(videoPlayerTVPage.getTitleVideoLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);
        commonPage.clickPlay();
        commonPage.clickDown(1);
        int timeAfterRestart = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("timeAfterRestart {}, totalDuration {}", timeAfterRestart, totalDurationTime);
        int duration = totalDurationTime - timeAfterRestart;
        ValueRange range = ValueRange.of(0, uiLatencyInSeconds);
        Assert.assertTrue(range.isValidIntValue(duration),"Video did not restarted");

        // Actions to validate forward and rewind
        commonPage.clickPlay();
        commonPage.clickRight(6, 1, 1);
        commonPage.clickDown(1);
        int remainingTimeBeforeRewind = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        commonPage.clickLeft(3, 1, 1);
        commonPage.clickDown(1);
        int remainingTimeAfterRewind = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        ValueRange acceptableDeltaRange =
                ValueRange.of(expectedSkippedSeconds - uiLatencyInSeconds, expectedSkippedSeconds);
        Assert.assertTrue(acceptableDeltaRange.isValidIntValue(remainingTimeAfterRewind - remainingTimeBeforeRewind),
                "Rewind did not work as expected");
    }
}
