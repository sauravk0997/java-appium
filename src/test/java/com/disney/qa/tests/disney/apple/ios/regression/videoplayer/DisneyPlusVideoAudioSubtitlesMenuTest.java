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
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.IConstantHelper.US;
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
    private static final String LOKI = "Loki";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67865"})
    @Test(description = "Verify Menu, Languages and UI", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifySubtitleMenuLanguageUI() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        initiatePlaybackFor(SHORT_SERIES);
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubtitleMenu();

        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.isAudioHeadingPresent(), "Audio heading is not present");
        sa.assertTrue(subtitlePage.isSubtitleHeadingPresent(), "Subtitle Heading is not present");
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ENGLISH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(OFF), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + OFF);

        verifyAudioLanguageIsInOrder(sa);
        verifyAudioSubtitleIsInOrder(sa);

        subtitlePage.tapCloseButton();
        disneyPlusVideoPlayerIOSPageBase.isOpened();
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubtitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ENGLISH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(OFF), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + OFF);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67869"})
    @Test(description = "Verify Menu, Languages and UI(change options)", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifySubtitleMenuLanguageChangesPersist() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        initiatePlaybackFor(LOKI);
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubtitleMenu();

        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ENGLISH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(OFF), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + OFF);
        subtitlePage.chooseAudioLanguage(ITALIANO);
        subtitlePage.chooseSubtitlesLanguage(ENGLISH_CC);
        subtitlePage.tapCloseButton();

        disneyPlusVideoPlayerIOSPageBase.isOpened();
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubtitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ITALIANO), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(ENGLISH), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69558"})
    @Test(description = "Audio & Subtitles Menu - Chinese Language Support", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitleMenuChineseSupport() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayerAudioSubtitleMenu(MULAN_DEEPLINK, sa);
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (普通话)"), "Audio language 'Mandarin cmn' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (简体)"), "Subtitle language 'Chinese(Simplified) zh-Hans' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (粵語)"), "Subtitle language 'Chinese (Hong Kong) zh-HK' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (繁體)"), "Subtitle language 'Chinese(Traditional) zh-Hant' was not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68452"})
    @Test(description = "Video Player Controls - Audio & Subtitles Menu - Backgrounding the App from the player", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitleMenuBackgroundingApp() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayerAudioSubtitleMenu(MULAN_DEEPLINK, sa);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        lockDevice(Duration.ofSeconds(5));
        handleAlert();
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        subtitlePage.chooseAudioLanguage(DEUTSCH);
        subtitlePage.chooseSubtitlesLanguage(DEUTSCH);
        subtitlePage.tapCloseButton();
        sa.assertTrue(videoPlayer.verifyVideoPaused(), VIDEO_NOT_PAUSED);
        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(DEUTSCH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(DEUTSCH), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + DEUTSCH);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67853"})
    @Test(description = "Video Player Controls - Audio & Subtitles Menu - English Audio Descriptive Language Track", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitleMenuEnglishAudioDescriptiveLanguageTrack() {
        DisneyPlusAudioSubtitleIOSPageBase audioSubtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayerAudioSubtitleMenu(MULAN_DEEPLINK, sa);
        sa.assertTrue(audioSubtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        audioSubtitlePage.chooseAudioLanguage(ENGLISH_AUDIO_DESCRIPTION);
        audioSubtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayOrContinue();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(audioSubtitlePage.verifySelectedAudioIs(ENGLISH_AUDIO_DESCRIPTION), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        audioSubtitlePage.chooseAudioLanguage(DEUTSCH);
        audioSubtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayOrContinue();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(audioSubtitlePage.verifySelectedAudioIs(DEUTSCH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67847"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitlesPreferredLanguage() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String choctawLang = "Chahta anumpa (Choctaw)";
        String spanishLang = "Español";
        SoftAssert sa = new SoftAssert();

        // Open content and select Deutsch audio and language
        loginAndDeeplinkToPlayerAudioSubtitleMenu(MULAN_DEEPLINK, sa);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        subtitlePage.chooseAudioLanguage(DEUTSCH);
        subtitlePage.chooseSubtitlesLanguage(DEUTSCH);
        subtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        // Open another content and verify that Deutsch audio and language is selected
        launchDeeplinkAndOpenAudioSubtitleMenu(R.TESTDATA.get("disney_prod_movie_deadpool_rated_r_deeplink"), sa);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(DEUTSCH), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(DEUTSCH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        subtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        // Open content that do not have Deutsch audio and language and verify English is selected
        launchDeeplinkAndOpenAudioSubtitleMenu(R.TESTDATA.get("disney_prod_content_playback_temple_of_inca"), sa);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(ENGLISH), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(ENGLISH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        subtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();

        // Open content that do not have English audio and verify default is selected
        launchDeeplinkAndOpenAudioSubtitleMenu(R.TESTDATA.get("disney_prod_content_playback_spanish_content"), sa);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(spanishLang), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED);
        subtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();

        // Open content that contains Choctaw
        launchDeeplinkAndOpenAudioSubtitleMenu(R.TESTDATA.get("disney_prod_content_playback_echo"), sa);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        subtitlePage.chooseAudioLanguage(choctawLang);
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(choctawLang), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED);
        sa.assertAll();
    }

    private void launchDeeplinkAndOpenAudioSubtitleMenu(String deeplink, SoftAssert sa){
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        launchDeeplink(deeplink);
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
        videoPlayer.tapAudioSubtitleMenu();
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

    private void loginAndDeeplinkToPlayerAudioSubtitleMenu(String deeplink, SoftAssert sa) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(deeplink);
        sa.assertTrue(detailsPage.clickPlayButton().isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
        videoPlayer.tapAudioSubtitleMenu();
    }

    public void verifyAudioLanguageIsInOrder(SoftAssert sa){
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        List<String> audioLanguageValue = subtitlePage.getAudioLanguagesFromUI();
        IntStream.range(0, getAudioLanguage().size()).forEach(i -> sa.assertTrue(getAudioLanguage().get(i).equals(audioLanguageValue.get(i))));
    }

    public void verifyAudioSubtitleIsInOrder(SoftAssert sa){
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        List<String> audioSubtitleValue = subtitlePage.getAudioSubtitlesFromUI();
        IntStream.range(0, getSubTitlesLanguage().size()).forEach(i -> sa.assertTrue(getSubTitlesLanguage().get(i).equals(audioSubtitleValue.get(i))));
    }

    private List<String> getAudioLanguage() {
        List<String> contentList = List.of("Chinese (中文)", "Chinese (粵語)", "Dansk", "Deutsch", "English",
                "English [Audio Description]");
        return contentList;
    }

    private List<String> getSubTitlesLanguage() {
        List<String> contentList = List.of("Chinese (简体)", "Chinese (粵語)", "Chinese (繁體)", "Dansk", "Deutsch");
        return contentList;
    }
}
