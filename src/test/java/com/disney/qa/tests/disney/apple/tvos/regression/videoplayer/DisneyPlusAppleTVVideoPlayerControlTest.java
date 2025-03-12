package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.DisneyAbstractPage;
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

import java.lang.invoke.MethodHandles;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.IConstantHelper.*;

public class DisneyPlusAppleTVVideoPlayerControlTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67528"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVideoPlayerControls() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM));
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67546"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVideoPlayerControlsScrubThumbnail() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        logIn(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayerTVPage.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayerTVPage.waitForVideoToStart();

        // Pause video with remote button
        home.clickPlay();
        int remainingTimeWhilePaused = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeWhilePaused {}", remainingTimeWhilePaused);
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.ONE_HUNDRED_TWENTY_SEC_TIMEOUT);

        // Click fast-forward on the remote and get the thumbnail position in time after the assertion
        clickRight(3, 1, 1);
        Assert.assertTrue(videoPlayerTVPage.getThumbnailView().isPresent(), "Thumbnail preview did not appear");
        int positionOfThumbnail = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        commonPage.clickDown(1);
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);

        // Make duration appear and get time that should be lower than the previous time
        commonPage.clickDown(1);
        int remainingTime = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeAfterPause {}", remainingTime);
        Assert.assertTrue(remainingTime < remainingTimeWhilePaused, "Video did not fast forward");
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);

        // Make duration appear with remote button and validate video is paused
        commonPage.clickDown(1);
        int timeAfterFF = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("timeAfterFF {}", timeAfterFF);
        Assert.assertEquals(remainingTime, timeAfterFF, "Video was not paused");
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);

        // Get time before play and compare with thumbnail time appearance
        commonPage.clickDown(1);
        int timeBeforeTapPlay = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        Assert.assertEquals(positionOfThumbnail, timeBeforeTapPlay, "Play will not start at the thumbnail position");
        // Play video with remote button
        home.clickPlay();
        home.waitForElementToDisappear(videoPlayerTVPage.getTimeRemainingLabel(), DisneyAbstractPage.SIXTY_SEC_TIMEOUT);
        commonPage.clickDown(1);
        int remainingTimeAfterPlay = videoPlayerTVPage.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeAfterPlay {}", remainingTimeAfterPlay);
        Assert.assertTrue(remainingTimeWhilePaused > remainingTimeAfterPlay, "Video was not playing");

    }

    public void clickRight(int times, int timeout, int duration) {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        IntStream.range(0, times).forEach(i -> {
            commonPage.clickRight(duration);
            pause(timeout);
        });
    }


}
