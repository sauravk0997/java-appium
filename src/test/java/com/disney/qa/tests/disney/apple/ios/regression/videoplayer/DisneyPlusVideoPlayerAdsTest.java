package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.DEUTSCH;

public class DisneyPlusVideoPlayerAdsTest extends DisneyBaseTest {
    //Test constants
    private static final String SPIDERMAN_THREE = "SpiderMan 3";
    private static final double PLAYER_PERCENTAGE_FOR_RANDOM_MOVING = 10;
    private static final String MS_MARVEL = "Ms. Marvel";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72851"})
    @Test(description = "Ariel Ads Video Player > In Ad, Audio Subtitle button displayed/clickable", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyAdsPlayerAudioSubtitleButton() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusAudioSubtitleIOSPageBase audioSubtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found during first ad.");
        sa.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.AUDIO_SUBTITLE_BUTTON),
                "Audio Subtitle button was not found.");

        videoPlayer.tapAudioSubTitleMenu();
        sa.assertTrue(audioSubtitlePage.isOpened(), "Audio / Subtitle menu was not opened during first ad.");
        audioSubtitlePage.chooseAudioLanguage(DEUTSCH);
        audioSubtitlePage.chooseSubtitlesLanguage(DEUTSCH);
        sa.assertTrue(audioSubtitlePage.verifySelectedAudioIs(DEUTSCH), DEUTSCH + " audio was not selected.");
        sa.assertTrue(audioSubtitlePage.verifySelectedSubtitleLangIs(DEUTSCH), DEUTSCH + " subtitle was not selected.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72879"})
    @Test(description = "Ariel Ads Video Player > Able to scrub forward during Grace period", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyPlayerScrubForwardDuringAdGracePeriod() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found during first ad.");
        System.out.println(getDriver().getPageSource());
        int adTimeRemaining = videoPlayer.getAdRemainingTimeInSeconds();
        videoPlayer.waitForAdToComplete(adTimeRemaining, 5);
        videoPlayer.scrubToPlaybackPercentage(50);
        sa.assertFalse(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was found after scrubbing forward past new ad pod.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73630"})
    @Test(description = "VOD Player - Ads - Restart - Restart Button inactive during Pre-Roll Ad", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyRestartButtonInActiveWhilePlayingAd() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SPIDERMAN_THREE);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad is not playing");
        sa.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.RESTART), "Restart button is not visible on ad player overlay");
        sa.assertTrue(videoPlayer.getRestartButtonStatus().equals(FALSE), "Restart button is clickable and not disabled on ad player overlay");
        videoPlayer.waitForAdToComplete(videoPlayer.getAdRemainingTimeInSeconds(), 5);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_RANDOM_MOVING);
        sa.assertTrue(videoPlayer.getRestartButtonStatus().equals(TRUE), "Restart button is not enabled on video player");
        int remainingTimeBeforeRestartClick = videoPlayer.getRemainingTime();
        videoPlayer.clickRestartButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad not started again after clicking restart button");
        int remainingTimeAfterRestartClick = videoPlayer.getRemainingTime();
        sa.assertTrue(remainingTimeBeforeRestartClick < remainingTimeAfterRestartClick,
                "Remaining time after restart click" + remainingTimeAfterRestartClick +
                        " is not greater than remaining time before restart click" + remainingTimeBeforeRestartClick);
        sa.assertAll();
    }

    private void loginAndStartPlayback(String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyAccount basicAccount = createV2Account(BUNDLE_BASIC);
        setAppToHomeScreen(basicAccount);
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
    }
}
