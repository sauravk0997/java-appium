package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVCommonPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVVideoPlayerPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.temporal.ValueRange;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusAppleTVVideoPlayerControlTest extends DisneyPlusAppleTVBaseTest {

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

        logIn(getAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        videoPlayer.waitForVideoToStart();

        commonPage.clickDown(1);
        int remainingTimeBeforeForward = videoPlayer.getRemainingTimeInSeconds();
        commonPage.clickRight(actionTimes, 1, 1);
        commonPage.clickDown(1);
        int remainingTimeAfterForward = videoPlayer.getRemainingTimeInSeconds();
        Assert.assertTrue((remainingTimeBeforeForward - remainingTimeAfterForward) > expectedSkippedSeconds,
                String.format("The difference between the remaining time before forward skip (%d seconds) " +
                        "and the remaining time after the forward skip (%d seconds) is not greater than %d seconds",
                        remainingTimeBeforeForward, remainingTimeAfterForward, expectedSkippedSeconds));

        videoPlayer.waitForElementToDisappear(videoPlayer.getSeekbar(), FIVE_SEC_TIMEOUT);
        commonPage.clickDown(1);
        int remainingTimeBeforeRewind = videoPlayer.getRemainingTimeInSeconds();
        commonPage.clickLeft(actionTimes, 1, 1);
        commonPage.clickDown(1);
        int remainingTimeAfterRewind = videoPlayer.getRemainingTimeInSeconds();
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
