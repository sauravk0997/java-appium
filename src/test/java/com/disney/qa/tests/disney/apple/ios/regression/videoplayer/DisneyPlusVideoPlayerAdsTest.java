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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.DEUTSCH;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.BTN_PLAY;

public class DisneyPlusVideoPlayerAdsTest extends DisneyBaseTest {

    @DataProvider(name = "content")
    public Object[][] content() {
        return new Object[][]{{MS_MARVEL},
                {SPIDERMAN_THREE}
        };
    }
    //Test constants
    private static final String SPIDERMAN_THREE = "SpiderMan 3";
    private static final String MS_MARVEL = "Ms. Marvel";
    private static final String THE_MARVELS = "The Marvels";
    private static final double SCRUB_PERCENTAGE_THIRTY = 30;
    private static final double SCRUB_PERCENTAGE_SIXTY = 60;
    private static final String FRANCAIS = "Français";
    private static final String AD_BADGE_NOT_PRESENT_ERROR_MESSAGE = "Ad badge was not present";
    private static final String NOT_RETURNED_DETAILS_PAGE_ERROR_MESSAGE = "Unable to return to details page";
    private static final String DURING_SECOND_AD_POD = "During second ad pod,";
    private static final String DURING_PRE_ROLL = "During pre-roll,";
    private static final String VALIDATING_EXIT_PLAYER = "{} validating exit player for {}";
    private static final String PLAYER_DID_NOT_OPEN_ERROR_MESSAGE = "Player view did not open.";

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
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(SHORT_TIMEOUT), "Ad badge label was not found during first ad.");
        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.skipPromoIfPresent();
        videoPlayer.scrubPlaybackWithAdsPercentage(SCRUB_PERCENTAGE_THIRTY);
        sa.assertFalse(videoPlayer.isAdBadgeLabelPresent(SHORT_TIMEOUT), "Ad badge label was found after scrubbing forward past new ad pod.");
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72272"})
    @Test(description = "VOD Player - Ads - Leave Player during Ad", dataProvider = "content", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyLeavePlayerDuringAd(String content) {
        String errorFormat = "%s %s";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyAccount basicAccount = createV2Account(BUNDLE_BASIC);
        setAppToHomeScreen(basicAccount);
        homePage.getSearchNav().click();
        LOGGER.info(VALIDATING_EXIT_PLAYER, DURING_PRE_ROLL);
        if (searchPage.getClearText().isPresent(SHORT_TIMEOUT)) {
            searchPage.clearText();
        }
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.getPlayerView().isPresent(SHORT_TIMEOUT), PLAYER_DID_NOT_OPEN_ERROR_MESSAGE);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), String.format(errorFormat, DURING_PRE_ROLL, AD_BADGE_NOT_PRESENT_ERROR_MESSAGE));
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), String.format(errorFormat, DURING_PRE_ROLL, NOT_RETURNED_DETAILS_PAGE_ERROR_MESSAGE));

        LOGGER.info(VALIDATING_EXIT_PLAYER, DURING_SECOND_AD_POD);
        detailsPage.clickPlayOrContinue();
        sa.assertTrue(videoPlayer.getPlayerView().isPresent(SHORT_TIMEOUT), PLAYER_DID_NOT_OPEN_ERROR_MESSAGE);
        videoPlayer.waitForAdToCompleteIfPresent(5);
        videoPlayer.skipPromoIfPresent();
        if (content.equalsIgnoreCase(MS_MARVEL)) {
            videoPlayer.waitForAdGracePeriodToEnd(videoPlayer.getRemainingTime());
        } else {
            videoPlayer.waitForAdGracePeriodToEnd(videoPlayer.getRemainingTimeThreeIntegers());
        }
        videoPlayer.scrubPlaybackWithAdsPercentage(SCRUB_PERCENTAGE_THIRTY);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), String.format(errorFormat, DURING_SECOND_AD_POD, AD_BADGE_NOT_PRESENT_ERROR_MESSAGE));
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), String.format(errorFormat, DURING_SECOND_AD_POD, NOT_RETURNED_DETAILS_PAGE_ERROR_MESSAGE));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72177"})
    @Test(description = "Ariel Ads Video Player > Scrub forward after grace period", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyPlayerScrubForwardAfterAdGracePeriod() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL, sa);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(5), "Ad badge label was not found during first ad.");
        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.skipPromoIfPresent();
        videoPlayer.waitForAdGracePeriodToEnd(videoPlayer.getRemainingTime());
        videoPlayer.scrubPlaybackWithAdsPercentage(SCRUB_PERCENTAGE_THIRTY);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found after scrubbing forward after an ad grace period");

        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.scrubPlaybackWithAdsPercentage(SCRUB_PERCENTAGE_SIXTY);
        sa.assertFalse(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was found after scrubbing during grace period");
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
