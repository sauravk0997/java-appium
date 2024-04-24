package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static com.disney.qa.tests.disney.apple.ios.regression.videoplayer.DisneyPlusVideoUpNextTest.SHORT_SERIES;

public class DisneyPlusVideoAudioSubtitlesMenuTest extends DisneyBaseTest {
    private static final String AUDIO_SUBTITLE_MENU_DID_NOT_OPEN = "Audio subtitle menu didn't open";
    private static final String VIDEO_NOT_PAUSED = "Video was not paused";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player didn't open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page didn't open";
    private static final String CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG = "Checkmark was not present for the selected lang";
    private static final String SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED = "Selected subtitle language is not as expected ";
    private static final String MULAN_DEEPLINK = R.TESTDATA.get("disney_prod_mulan_2020_deeplink");

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67865"})
    @Test(description = "Verify Menu, Languages and UI", groups = {"Video Player, PlayerAudioSubtitles", TestGroup.PRE_CONFIGURATION})
    public void verifySubtitleMenuLanguageUI() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        initiatePlaybackFor(SHORT_SERIES);
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();

        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.isAudioHeadingPresent(), "Audio heading is not present");
        sa.assertTrue(subtitlePage.isSubtitleHeadingPresent(), "Subtitle Heading is not present");

        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ENGLISH), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(OFF), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + OFF);

        subtitlePage.tapCloseButton();
        disneyPlusVideoPlayerIOSPageBase.isOpened();

        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ENGLISH), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(OFF), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + OFF);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67869"})
    @Test(description = "Verify Menu, Languages and UI(change options)", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
    public void verifySubtitleMenuLanguageChangesPersist() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        initiatePlaybackFor(SHORT_SERIES);
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();

        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ENGLISH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(OFF), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + OFF);
        subtitlePage.chooseAudioLanguage(ITALIANO);
        subtitlePage.chooseSubtitlesLanguage(ENGLISH_CC);
        subtitlePage.tapCloseButton();

        disneyPlusVideoPlayerIOSPageBase.isOpened();
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ITALIANO), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(ENGLISH), "Selected subtitle language is not as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69558"})
    @Test(description = "Audio & Subtitles Menu - Chinese Language Support", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
    public void verifyAudioAndSubtitleMenuChineseSupport() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayer(MULAN_DEEPLINK, sa);
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (普通话)"), "Audio language 'Mandarin cmn' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (简体)"), "Subtitle language 'Chinese(Simplified) zh-Hans' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (粵語)"), "Subtitle language 'Chinese (Hong Kong) zh-HK' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (繁體)"), "Subtitle language 'Chinese(Traditional) zh-Hant' was not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68452"})
    @Test(description = "Video Player Controls - Audio & Subtitles Menu - Backgrounding the App from the player", groups = {"Video Player", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAudioAndSubtitleMenuBackgroundingApp() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayer(MULAN_DEEPLINK, sa);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        lockDevice(Duration.ofSeconds(5));
        handleAlert();
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        subtitlePage.chooseAudioLanguage(DEUTSCH);
        subtitlePage.chooseSubtitlesLanguage(DEUTSCH);
        subtitlePage.tapCloseButton();
        sa.assertTrue(videoPlayer.verifyVideoPaused(), VIDEO_NOT_PAUSED);
        videoPlayer.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(DEUTSCH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(DEUTSCH), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + DEUTSCH);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67853"})
    @Test(description = "Video Player Controls - Audio & Subtitles Menu - English Audio Descriptive Language Track", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
    public void verifyAudioAndSubtitleMenuEnglishAudioDescriptiveLanguageTrack() {
        DisneyPlusAudioSubtitleIOSPageBase audioSubtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayer(MULAN_DEEPLINK, sa);
        sa.assertTrue(audioSubtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        audioSubtitlePage.chooseAudioLanguage(ENGLISH_AUDIO_DESCRIPTION);
        audioSubtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayOrContinue();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.tapAudioSubTitleMenu();
        sa.assertTrue(audioSubtitlePage.verifySelectedAudioIs(ENGLISH_AUDIO_DESCRIPTION), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        audioSubtitlePage.chooseAudioLanguage(DEUTSCH);
        audioSubtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayOrContinue();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.tapAudioSubTitleMenu();
        sa.assertTrue(audioSubtitlePage.verifySelectedAudioIs(DEUTSCH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertAll();
    }

    private void initiatePlaybackFor(String content) {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusHomeIOSPageBase.getSearchNav().click();
        disneyPlusSearchIOSPageBase.searchForMedia(content);
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();
        disneyPlusDetailsIOSPageBase.clickPlayButton().isOpened();
    }

    private void loginAndDeeplinkToPlayer(String deeplink, SoftAssert sa) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, deeplink, 10);
        homePage.clickOpenButton();
        sa.assertTrue(detailsPage.clickPlayButton().isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.tapAudioSubTitleMenu();
    }
}
