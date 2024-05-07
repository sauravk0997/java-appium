package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.config.DisneyParameters;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.remote.MobilePlatform;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.DEUTSCH;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.BTN_PLAY;

public class DisneyPlusVideoPlayerAdsTest extends DisneyBaseTest {
    //Test constants
    private static final String SPIDERMAN_THREE = "SpiderMan 3";
    private static final String MS_MARVEL = "Ms. Marvel";
    private static final String THE_MARVELS = "The Marvels";
    private static final double SCRUB_PERCENTAGE_THIRTY = 30;
    private static final String FRANCAIS = "Français";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72851"})
    @Test(description = "Ariel Ads Video Player > In Ad, Audio Subtitle button displayed/clickable", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyAdsPlayerAudioSubtitleButton() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusAudioSubtitleIOSPageBase audioSubtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL, sa);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found during first ad.");
        sa.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.AUDIO_SUBTITLE_BUTTON),
                "Audio Subtitle button was not found.");

        videoPlayer.tapAudioSubtitleMenu();
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
        loginAndStartPlayback(MS_MARVEL, sa);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(5), "Ad badge label was not found during first ad.");
        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.skipPromoIfPresent();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_THIRTY);
        sa.assertFalse(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was found after scrubbing forward past new ad pod.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73630"})
    @Test(description = "VOD Player - Ads - Restart - Restart Button inactive during Pre-Roll Ad", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyRestartButtonInActiveWhilePlayingAd() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SPIDERMAN_THREE, sa);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad is not playing");
        sa.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.RESTART), "Restart button is not visible on ad player overlay");
        sa.assertTrue(videoPlayer.getRestartButtonStatus().equals(FALSE), "Restart button is clickable and not disabled on ad player overlay");
        videoPlayer.waitForAdToComplete(videoPlayer.getAdRemainingTimeInSeconds(), 5);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_THIRTY);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72212"})
    @Test(description = "VOD Player - Ads - Display of Ad Badge", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyLocalizedAdBadgeWhilePlayingAd() {
        String frenchLanguageCode = "fr";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAppLanguageIOSPageBase appLanguage = initPage(DisneyPlusAppLanguageIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(THE_MARVELS, sa);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad Badge is not displayed");
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresentWhenControlDisplay(), "Ad Badge is not displayed when controls are visible");

        videoPlayer.clickBackButton();
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getAccount().getFirstName());
        editProfile.clickAppLanguage();
        sa.assertTrue(appLanguage.isOpened(), "App Language screen is not opened");
        appLanguage.selectLanguage(FRANCAIS);

        getLocalizationUtils().setLanguageCode(frenchLanguageCode);
        DisneyLocalizationUtils disneyLocalizationUtils = new DisneyLocalizationUtils(getCountry(), getLocalizationUtils().getUserLanguage(), MobilePlatform.IOS,
                DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                DISNEY);
        disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());
        String doneInFrench = disneyLocalizationUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT_PROFILE_DONE_BUTTON.getText());
        String playInFrench = disneyLocalizationUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_PLAY.getText());
        String adInFrench = disneyLocalizationUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.AD_BADGE_LABEL.getText());

        editProfile.clickElementAtLocation(editProfile.getStaticTextByLabel(doneInFrench), 50, 50);
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(THE_MARVELS);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.getStaticTextByLabel(playInFrench).click();
        sa.assertTrue(videoPlayer.getDynamicAccessibilityId(adInFrench).isPresent(), "Ad Badge is not displayed in French language");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72175"})
    @Test(description = "VOD Player - Ads - No Skip Forward or Backward allowed", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyPlayerNoSkippingDuringAd() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL, sa);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(SHORT_TIMEOUT), "Ad badge label was not found");
        int adTimeRemainingMinus30 = videoPlayer.getAdTimeRemaining() - 30;
        videoPlayer.displayVideoControllerForAdPlayer();
        videoPlayer.tapPlayerScreen(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.FAST_FORWARD, 3);
        sa.assertTrue(videoPlayer.getAdTimeRemaining() > adTimeRemainingMinus30,
                "Fast forward action is not functional during an ad");

        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(SHORT_TIMEOUT), "Ad badge label was not found");
        int adTimeRemainingPlus30 = videoPlayer.getAdTimeRemaining() + 30;
        videoPlayer.displayVideoControllerForAdPlayer();
        videoPlayer.tapPlayerScreen(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.REWIND, 3);
        sa.assertTrue(videoPlayer.getAdTimeRemaining() < adTimeRemainingPlus30,
                "Rewind action is not functional during an ad");
        sa.assertAll();
    }

    private void loginAndStartPlayback(String content, SoftAssert sa) {
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
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        detailsPage.clickPlayButton();
    }
}
