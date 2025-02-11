package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.config.DisneyParameters;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.remote.MobilePlatform;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.*;
import java.time.temporal.ValueRange;
import java.util.List;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.DEUTSCH;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.BTN_PLAY;

import static com.disney.qa.api.disney.DisneyEntityIds.MARVELS;

public class DisneyPlusVideoPlayerAdsTest extends DisneyBaseTest {
    //Test constants
    private static final String SPIDERMAN_THREE = "Spider-Man™ 3";
    private static final String MS_MARVEL = "Ms. Marvel";
    private static final String THE_MARVELS = "The Marvels";
    private static final double SCRUB_PERCENTAGE_THIRTY = 30;
    private static final double SCRUB_PERCENTAGE_TEN = 10;
    private static final double SCRUB_PERCENTAGE_SIXTY = 60;
    private static final int UI_LATENCY = 25;
    private static final String FRANCAIS = "Français";
    private static final String DURING_SECOND_AD_POD = "During second ad pod,";
    private static final String DURING_PRE_ROLL = "During pre-roll,";
    private static final String PLAYER_DID_NOT_OPEN_ERROR_MESSAGE = "Player view did not open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN_ERROR_MESSAGE = "Details page did not open";
    private static final String CONTENT_TIME_CHANGED_ERROR_MESSAGE = "Content time remaining did not remain the same";
    private static final String AD_BADGE_NOT_PRESENT_ERROR_MESSAGE = "Ad badge was not present";
    private static final String NOT_RETURNED_DETAILS_PAGE_ERROR_MESSAGE = "Unable to return to details page";
    private static final String AD_IS_NOT_PRESENT_MESSAGE = "Ad badge is not present";
    private static final String AD_IS_PRESENT_MESSAGE = "Ad is present";
    private static final String AD_POD_PRESENT_MESSAGE = "Ad pod is present in timeline";
    private static final String SEEK_BAR_NOT_VISIBLE_MESSAGE = "Seek bar is not visible";
    private static final String AD_POD_NOT_PRESENT_MESSAGE = "Ad pod not present in timeline";


    @DataProvider(name = "tapAction")
    public Object[][] tapAction() {
        return new Object[][]{{DisneyPlusVideoPlayerIOSPageBase.PlayerControl.FAST_FORWARD},
                {DisneyPlusVideoPlayerIOSPageBase.PlayerControl.REWIND}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72216"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyAdDurationTimer() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
        int remainingTimeBeforeAd = videoPlayer.getRemainingTime();
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        sa.assertTrue(videoPlayer.isAdTimeDurationPresent(), "Ad time duration wasn't shown when video controls were not present");
        videoPlayer.waitUntil(ExpectedConditions.invisibilityOfElementLocated(videoPlayer.getSeekbar().getBy()), SHORT_TIMEOUT);
        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.skipPromoIfPresent();
        int remainingTimeAfterAd = videoPlayer.getRemainingTime();
        int playDuration = (remainingTimeBeforeAd - remainingTimeAfterAd);
        ValueRange range = ValueRange.of(0, UI_LATENCY);
        sa.assertTrue(range.isValidIntValue(playDuration),
                "Remaining time before ad" + remainingTimeBeforeAd +
                        " is not greater than remaining time after ad" + remainingTimeAfterAd);
        videoPlayer.waitForAdGracePeriodToEnd(videoPlayer.getRemainingTimeThreeIntegers());
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_SIXTY);
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        sa.assertTrue(videoPlayer.isAdTimeDurationPresentWithVideoControls(),
                "Ad time duration wasn't shown when video controls were present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72851"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAdsPlayerAudioSubtitleButton() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusAudioSubtitleIOSPageBase audioSubtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
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
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlayerScrubForwardDuringAdGracePeriod() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found during first ad.");
        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.skipPromoIfPresent();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_THIRTY);
        sa.assertTrue(videoPlayer.isAdBadgeLabelNotPresent(), "Ad badge label was found after scrubbing forward past new ad pod.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73630"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRestartButtonInActiveWhilePlayingAd() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SPIDERMAN_THREE);
        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getPlayerView());
        Assert.assertTrue(videoPlayer.waitUntil(ExpectedConditions.invisibilityOfElementLocated(
                videoPlayer.getSeekbar().getBy()), SHORT_TIMEOUT), "Seek bar visible");
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(FIFTEEN_SEC_TIMEOUT), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        sa.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.RESTART),
                "Restart button is not visible on ad player overlay");
        sa.assertTrue(videoPlayer.getRestartButtonStatus().equals(FALSE),
                "Restart button is clickable and not disabled on ad player overlay");
        videoPlayer.waitForAdToCompleteIfPresent(5);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_TEN);
        sa.assertTrue(videoPlayer.getRestartButtonStatus().equals(TRUE),
                "Restart button is not enabled on video player");
        int remainingTimeBeforeRestartClick = videoPlayer.getRemainingTime();
        videoPlayer.clickRestartButton();
        videoPlayer.waitForVideoToStart();
        int remainingTimeAfterRestartClick = videoPlayer.getRemainingTime();
        sa.assertTrue(remainingTimeBeforeRestartClick < remainingTimeAfterRestartClick,
                "Remaining time after restart click" + remainingTimeAfterRestartClick +
                        " is not greater than remaining time before restart click" + remainingTimeBeforeRestartClick);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72212"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
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
        loginAndStartPlayback(MS_MARVEL);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
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
        sa.assertTrue(videoPlayer.getStaticTextByLabel(adInFrench).isPresent(), "Ad Badge is not displayed in French language");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72175"})
    @Test(dataProvider = "tapAction", groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlayerNoSkippingDuringAd(DisneyPlusVideoPlayerIOSPageBase.PlayerControl control) {
        int timeout = 20;
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        loginAndStartPlayback(SPIDERMAN_THREE);

        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getAdBadge());
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(timeout), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        int adTimeRemainingBeforeControlAction = videoPlayer.getAdRemainingTimeInSeconds();
        int contentTimeRemaining = videoPlayer.getRemainingTime();
        videoPlayer.tapPlayerScreen(control, 2);
        Assert.assertTrue(videoPlayer.getAdRemainingTimeInSeconds() < adTimeRemainingBeforeControlAction,
                "Fast forward/Rewind action is functional during an ad");
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(SHORT_TIMEOUT), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        Assert.assertEquals(videoPlayer.getRemainingTime(), contentTimeRemaining, CONTENT_TIME_CHANGED_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72272"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLeavePlayerDuringAd() {
        String errorFormat = "%s %s";
        SoftAssert sa = new SoftAssert();
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        loginAndStartPlayback(SPIDERMAN_THREE);
        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getPlayerView());
        sa.assertTrue(videoPlayer.getPlayerView().isPresent(TEN_SEC_TIMEOUT), PLAYER_DID_NOT_OPEN_ERROR_MESSAGE);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(FIFTEEN_SEC_TIMEOUT),
                String.format(errorFormat, DURING_PRE_ROLL, AD_BADGE_NOT_PRESENT_ERROR_MESSAGE));
        videoPlayer.clickBackButton();
        detailsPage.waitForDetailsPageToOpen();
        sa.assertTrue(detailsPage.isOpened(),
                String.format(errorFormat, DURING_PRE_ROLL, NOT_RETURNED_DETAILS_PAGE_ERROR_MESSAGE));

        detailsPage.clickPlayOrContinue();
        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getPlayerView());
        sa.assertTrue(videoPlayer.getPlayerView().isPresent(TEN_SEC_TIMEOUT), PLAYER_DID_NOT_OPEN_ERROR_MESSAGE);
        videoPlayer.waitForAdToCompleteIfPresent(5);
        videoPlayer.skipPromoIfPresent();
        videoPlayer.waitForAdGracePeriodToEnd(videoPlayer.getRemainingTimeThreeIntegers());
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_SIXTY);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(FIFTEEN_SEC_TIMEOUT),
                String.format(errorFormat, DURING_SECOND_AD_POD, AD_BADGE_NOT_PRESENT_ERROR_MESSAGE));
        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getPlayerView());
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(),
                String.format(errorFormat, DURING_SECOND_AD_POD, NOT_RETURNED_DETAILS_PAGE_ERROR_MESSAGE));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72177"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlayerScrubForwardAfterAdGracePeriod() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found during first ad.");
        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.skipPromoIfPresent();
        videoPlayer.waitForAdGracePeriodToEnd(videoPlayer.getRemainingTime());
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_THIRTY);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found after scrubbing forward " +
                "after an ad grace period");

        videoPlayer.waitForAdToCompleteIfPresent(6);
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_SIXTY);
        sa.assertTrue(videoPlayer.isAdBadgeLabelNotPresent(),
                "Ad badge label was found after scrubbing during grace period");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72476"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContentDurationBeforeAndAfterAd() {
        int timeout = 15;
        String durationNotmatchedErrorMessage = "Duration of video is not representing total length of main content";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY);
        setAppToHomeScreen(basicAccount);
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(THE_MARVELS);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        ExploreContent movieApiContent = getMovieApi(MARVELS.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String contentTimeFromAPI = detailsPage.getHourMinFormatForDuration(movieApiContent.getDurationMs());
        sa.assertTrue(detailsPage.getMetaDataLabel().getText().contains(contentTimeFromAPI), "Expected runtime for ad-supportrd content was not found on detail page");

        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.getPlayerView().isPresent(SHORT_TIMEOUT), PLAYER_DID_NOT_OPEN_ERROR_MESSAGE);
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(timeout), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        sa.assertTrue(videoPlayer.getRemainingTimeInStringWithHourAndMinutes().equals(contentTimeFromAPI), durationNotmatchedErrorMessage);
        videoPlayer.waitForAdToCompleteIfPresent(5);
        sa.assertTrue(videoPlayer.getRemainingTimeInStringWithHourAndMinutes().equals(contentTimeFromAPI), durationNotmatchedErrorMessage);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72188"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAdContentTimeDisplayUI() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
        sa.assertTrue(videoPlayer.getPlayerView().isPresent(SHORT_TIMEOUT), PLAYER_DID_NOT_OPEN_ERROR_MESSAGE);
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(FIFTEEN_SEC_TIMEOUT), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        videoPlayer.clickPauseButton();
        sa.assertTrue(videoPlayer.isAdTimeDurationPresent(), "Ad remaining time was not found");
        verifyAdRemainingTimeFormat(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72834"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION,US})
    public void verifyContentRatingDisplayedAfterPreRoll() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        loginAndStartPlayback(MS_MARVEL);

        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getAdBadge());
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(), AD_BADGE_NOT_PRESENT_ERROR_MESSAGE);
        videoPlayer.waitForAdToCompleteIfPresent(2);
        Assert.assertTrue(videoPlayer.isRatingPresent(DictionaryKeys.RATING_TVPG_TV_PG),  "rating was" +
                " not shown for " + MS_MARVEL);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72187"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAdPodOnTimeline() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        loginAndStartPlayback(MS_MARVEL);
        videoPlayer.displayVideoController();
        Assert.assertTrue(videoPlayer.isAdPodPresent(), "Ad pod is not found");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76660"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyVideoPlayerBehaviorAfterBackgroundingAppForAdsUser() {
        int polling = 5;
        int backgroundDuration = 20;
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        loginAndStartPlayback(THE_MARVELS);
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_TEN);
        videoPlayer.waitForVideoToStart();
        videoPlayer.waitForAdToCompleteIfPresent(polling);
        videoPlayer.waitForVideoToStart();
        lockDevice(Duration.ofSeconds(backgroundDuration));
        Assert.assertTrue(videoPlayer.isOpened(), "User did not land on video player after foregrounding the app");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72214"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyVideoPlayerPausingWhilePlayingAd() {
        int uiLatency = 20;
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        loginAndStartPlayback(THE_MARVELS);
        videoPlayer.waitForVideoToStart(10, 1);

        int adDurationBeforePause = videoPlayer.getAdRemainingTimeInSeconds();
        videoPlayer.clickPauseButton();
        int adDurationAfterPause = videoPlayer.getAdRemainingTimeInSeconds();
        int adDurationDelta = (adDurationBeforePause - adDurationAfterPause);
        ValueRange range = ValueRange.of(0, uiLatency);
        Assert.assertTrue(range.isValidIntValue(adDurationDelta),
                "Ad badge countdown didn't pause");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73082"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMovingBackwardsAdPodNotForceAdPlay() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        String adBoundaryPresentMessage = "Ad boundary is present";
        loginAndStartPlayback(MS_MARVEL);
        // Validate and wait for Ad to complete
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(), AD_IS_NOT_PRESENT_MESSAGE);
        videoPlayer.waitForAdToCompleteIfPresent(6);
        // Rewind to the beginning and validate Ad should not be playing
        videoPlayer.scrubToPlaybackPercentage(0);
        Assert.assertFalse(videoPlayer.isCrossingAdBoundaryMessagePresent(), adBoundaryPresentMessage);
        Assert.assertTrue(videoPlayer.isAdBadgeLabelNotPresent(), AD_IS_PRESENT_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72568"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeeksOverAdsOutsideGracePeriod() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int nextAdScrubPercentage = 65;
        loginAndStartPlayback(MS_MARVEL);
        // Validate and wait for Ad to complete
        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getAdBadge());
        Assert.assertTrue(videoPlayer.isAdBadgeLabelPresent(6), AD_IS_NOT_PRESENT_MESSAGE);
        videoPlayer.waitForAdToCompleteIfPresent(6);
        // Wait to be outside grace period
        pause(FORTY_FIVE_SEC_TIMEOUT);
        // Scrub to an Ad area
        videoPlayer.scrubToPlaybackPercentage(nextAdScrubPercentage);
        // Wait for ad to start and verify Ad and Ad pod in timeline
        sa.assertTrue(videoPlayer.isAdPodPresent(), AD_POD_NOT_PRESENT_MESSAGE);
        sa.assertTrue(videoPlayer.isSeekbarVisible(), SEEK_BAR_NOT_VISIBLE_MESSAGE);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(6), AD_IS_NOT_PRESENT_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72273"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.VIDEO_PLAYER_ADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMovingBackwardsAdPodNotForceAdPlayInsideGracePeriod() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(MS_MARVEL);
        // Validate and wait for Ad to complete
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), AD_IS_NOT_PRESENT_MESSAGE);
        videoPlayer.waitForAdToCompleteIfPresent(6);
        // Need to be inside grace period
        videoPlayer.waitForVideoToStart();
        pause(FIFTEEN_SEC_TIMEOUT);
        // Rewind to zero percentage and validate ad is not present
        videoPlayer.scrubToPlaybackPercentage(0);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isAdBadgeLabelNotPresent(), AD_IS_PRESENT_MESSAGE);
        sa.assertFalse(videoPlayer.isAdPodPresent(), AD_POD_PRESENT_MESSAGE);
        sa.assertAll();
    }

    private void loginAndStartPlayback(String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY);
        setAppToHomeScreen(basicAccount);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(content);
        searchPage.getDynamicAccessibilityId(content).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN_ERROR_MESSAGE);
        detailsPage.clickPlayButton();
    }

    private void verifyAdRemainingTimeFormat(SoftAssert sa) {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        int adTime = videoPlayer.getAdRemainingTimeInSeconds();
        String adTimeInString = videoPlayer.getAdRemainingTimeInString();
        if (adTime < 60) {
            sa.assertTrue(adTimeInString.startsWith("0:"), "Ad remaining time should start with 0");
        } else {
            sa.assertTrue(adTimeInString.startsWith("1:"), "Ad remaining time should start with 1");
        }
        sa.assertTrue(videoPlayer.validateTimeFormat(videoPlayer.getAdRemainingTime().getText()), "Ad remaining time is not visible in M:SS Format");
    }
}
