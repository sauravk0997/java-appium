package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
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

import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.constant.IConstantHelper.US;


public class DisneyPlusAppleTVVideoPlayerControlTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67528"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVideoPlayerControls() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM));
        logIn(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), "Video player did not launch");
        videoPlayer.waitForVideoToStart();

        // Pause video with remote button
        home.clickPlay();
        int remainingTimeWhilePaused = videoPlayerTVPage.getRemainingDurationTime();
        LOGGER.info("remainingTimeBeforePause {}", remainingTimeWhilePaused);
        home.waitForElementToDisappear(videoPlayer.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);

        // Make duration appear and get time that should match the previous time
        commonPage.clickDown(1);
        int remainingTime = videoPlayerTVPage.getRemainingDurationTime();
        LOGGER.info("remainingTimeAfterPause {}", remainingTime);
        Assert.assertEquals(remainingTime, remainingTimeWhilePaused, "Video was not paused");

        // Play video with remote button
        home.clickPlay();
        home.waitForElementToDisappear(videoPlayer.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);
        commonPage.clickDown(1);
        int remainingTimeAfterPlay = videoPlayerTVPage.getRemainingDurationTime();
        LOGGER.info("remainingTimeAfterPlay {}", remainingTimeAfterPlay);
        Assert.assertTrue(remainingTimeWhilePaused > remainingTimeAfterPlay, "Video was not playing");
    }
}
