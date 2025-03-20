package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Listeners;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static java.lang.String.format;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusVideoAudioSubtitlesMenuTest extends DisneyBaseTest {
    private static final String AUDIO_SUBTITLE_MENU_DID_NOT_OPEN = "Audio subtitle menu didn't open";
    private static final String VIDEO_NOT_PAUSED = "Video was not paused";
    private static final String CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG = "Checkmark was not present for the selected lang";
    private static final String SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED = "Selected subtitle language is not as expected ";
    private static final String MULAN_DEEPLINK = R.TESTDATA.get("disney_prod_mulan_2020_deeplink");
    private static final String LOKI = "Loki";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67865"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifySubtitleMenuLanguageUI() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        initiatePlaybackFor(SERIES_BLUEY);
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
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifySubtitleMenuLanguageChangesPersist() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
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
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitleMenuChineseSupport() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayerAudioSubtitleMenu(MULAN_DEEPLINK);
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (普通话)"), "Audio language 'Mandarin cmn' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (简体)"), "Subtitle language 'Chinese(Simplified) zh-Hans' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (粵語)"), "Subtitle language 'Chinese (Hong Kong) zh-HK' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (繁體)"), "Subtitle language 'Chinese(Traditional) zh-Hant' was not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68452"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitleMenuBackgroundingApp() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayerAudioSubtitleMenu(MULAN_DEEPLINK);
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);

        lockDevice(Duration.ofSeconds(5));
        handleAlert();
        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getPlayerView());
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);

        subtitlePage.chooseAudioLanguage(DEUTSCH);
        subtitlePage.chooseSubtitlesLanguage(DEUTSCH);
        subtitlePage.tapCloseButton();
        sa.assertTrue(videoPlayer.verifyVideoPaused(), VIDEO_NOT_PAUSED);

        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(DEUTSCH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(DEUTSCH),
                SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED + DEUTSCH);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67853"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitleMenuEnglishAudioDescriptiveLanguageTrack() {
        DisneyPlusAudioSubtitleIOSPageBase audioSubtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginAndDeeplinkToPlayerAudioSubtitleMenu(MULAN_DEEPLINK);
        sa.assertTrue(audioSubtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        audioSubtitlePage.chooseAudioLanguage(ENGLISH_AUDIO_DESCRIPTION);
        audioSubtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayOrContinue();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(audioSubtitlePage.verifySelectedAudioIs(ENGLISH_AUDIO_DESCRIPTION), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        audioSubtitlePage.chooseAudioLanguage(DEUTSCH);
        audioSubtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayOrContinue();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(audioSubtitlePage.verifySelectedAudioIs(DEUTSCH), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67847"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAudioAndSubtitlesPreferredLanguage() {
        String choctawLang = "Chahta anumpa (Choctaw)";
        String spanishLang = "Español";
        setAppToHomeScreen(getUnifiedAccount());

        // Open content and select Deutsch audio and language, this will be the preferred language
        changeAudioLanguage(R.TESTDATA.get("disney_prod_content_mulan_playback_deeplink"), DEUTSCH, DEUTSCH);

        // Open another content and verify that preferred Deutsch audio is selected
        verifyAudioLanguage(R.TESTDATA.get("disney_prod_movie_deadpool_rated_r_deeplink"), DEUTSCH);

        // Open content with no preferred language and verify English audio is selected
        verifyAudioLanguage(R.TESTDATA.get("disney_prod_content_brave_playback_deeplink"), ENGLISH);

        // Open content that do not have English audio and verify default is selected
        verifyAudioLanguage(R.TESTDATA.get("disney_prod_content_spanish_playback_deeplink"), spanishLang);

        // Open content that contains Choctaw and change it to that language
        changeAudioLanguage(R.TESTDATA.get("disney_prod_content_choctaw_playback_deeplink"), choctawLang, DEUTSCH);
    }

    private void changeAudioLanguage(String deeplink, String language, String subtitles) {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        launchDeeplink(deeplink);
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        videoPlayer.tapAudioSubtitleMenu();
        Assert.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        subtitlePage.chooseAudioLanguage(language);
        subtitlePage.chooseSubtitlesLanguage(subtitles);
        Assert.assertTrue(subtitlePage.verifySelectedAudioIs(language), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED);
        Assert.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(subtitles), CHECKMARK_NOT_PRESENT_FOR_SELECTED_LANG);
        subtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
    }

    private void verifyAudioLanguage(String deeplink, String audioLanguage) {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        launchDeeplink(deeplink);
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        videoPlayer.tapAudioSubtitleMenu();
        Assert.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        Assert.assertTrue(subtitlePage.verifySelectedAudioIs(audioLanguage), SELECTED_SUBTITLE_LANG_NOT_AS_EXPECTED);
        subtitlePage.tapCloseButton();
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
    }

    private void initiatePlaybackFor(String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.clickPlayButton(TEN_SEC_TIMEOUT);
        videoPlayer.waitForPresenceOfAnElement(videoPlayer.getPlayerView());
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
    }

    private void loginAndDeeplinkToPlayerAudioSubtitleMenu(String deeplink) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.clickPlayButton().isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        videoPlayer.tapAudioSubtitleMenu();
    }

    public void verifyAudioLanguageIsInOrder(SoftAssert sa){
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        List<String> audioLanguageValue = subtitlePage.getAudioLanguagesFromUI();
        IntStream.range(0, getAudioLanguage().size()).forEach(i -> sa.assertTrue(getAudioLanguage().get(i)
                        .equals(audioLanguageValue.get(i)),
                format("Expected audio language %s mismatched with audio language on UI %s.",
                        getAudioLanguage().get(i), audioLanguageValue.get(i))));
    }

    public void verifyAudioSubtitleIsInOrder(SoftAssert sa){
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        List<String> audioSubtitleValue = subtitlePage.getAudioSubtitlesFromUI();
        IntStream.range(0, getSubTitlesLanguage().size()).forEach(i -> sa.assertTrue(getSubTitlesLanguage().get(i)
                .equals(audioSubtitleValue.get(i)),
                format("Expected audio subtitle %s mismatched with audio subtitle on UI %s.",
                        getSubTitlesLanguage().get(i), audioSubtitleValue.get(i))));
    }

    private List<String> getAudioLanguage() {
        List<String> contentList = List.of("Chinese (中文)", "Chinese (粵語)", "Dansk", "Deutsch", "English", "English [Audio Description]");
        return contentList;
    }

    private List<String> getSubTitlesLanguage() {
        List<String> contentList = List.of("Chinese (粵語)", "Chinese (繁體)", "English [CC]",
                "Español (Latinoamérica) [CC]", "Magyar");
        return contentList;
    }
}
