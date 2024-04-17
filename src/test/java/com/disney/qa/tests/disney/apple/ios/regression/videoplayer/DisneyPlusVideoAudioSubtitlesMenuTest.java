package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.DEUTSCH;
import static com.disney.qa.tests.disney.apple.ios.regression.videoplayer.DisneyPlusVideoUpNextTest.SHORT_SERIES;

public class DisneyPlusVideoAudioSubtitlesMenuTest extends DisneyBaseTest {
    private static final String AUDIO_SUBTITLE_MENU_DID_NOT_OPEN = "Audio subtitle menu didn't open";
    private static final String VIDEO_NOT_PAUSED = "Video was not paused";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player didn't open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62343"})
    @Test(description = " Verify Menu, Languages and UI", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
    @Maintainer("gkrishna1")
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

        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("Off"), "Selected subtitle language is not as expected");

        subtitlePage.tapCloseButton();
        disneyPlusVideoPlayerIOSPageBase.isOpened();

        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("Off"), "Selected subtitle language is not as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62347"})
    @Test(description = "Verify Menu, Languages and UI(change options)", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
    @Maintainer("gkrishna1")
    public void verifySubtitleMenuLanguageChangesPersist() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        initiatePlaybackFor(SHORT_SERIES);
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();

        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("Off"), "Selected subtitle language is not as expected");
        subtitlePage.chooseAudioLanguage("Italiano");
        subtitlePage.chooseSubtitlesLanguage("English [CC]");
        subtitlePage.tapCloseButton();

        disneyPlusVideoPlayerIOSPageBase.isOpened();
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("Italiano"), "Checkmark was not present for the selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("English"), "Selected subtitle language is not as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69558"})
    @Test(description = "Audio & Subtitles Menu - Chinese Language Support", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
    @Maintainer("gkrishna1")
    public void verifyAudioAndSubtitleMenuChineseSupport() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_mulan_2020_deeplink"), 10);
        homePage.clickOpenButton();
        detailsPage.clickPlayButton().isOpened();
        videoPlayer.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (普通话)"), "Audio language 'Mandarin cmn' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (简体)"), "Subtitle language 'Chinese(Simplified) zh-Hans' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (粵語)"), "Subtitle language 'Chinese (Hong Kong) zh-HK' was not present");
        sa.assertTrue(subtitlePage.isLanguagePresent("Chinese (繁體)"), "Subtitle language 'Chinese(Traditional) zh-Hant' was not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68452"})
    @Test(description = "Video Player Controls - Audio & Subtitles Menu - Backgrounding the App from the player", groups = {"Video Player", TestGroup.PRE_CONFIGURATION}, enabled = false)
    @Maintainer("gkrishna1")
    public void verifyAudioAndSubtitleMenuBackgroundingApp() {
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_mulan_2020_deeplink"), 10);
        homePage.clickOpenButton();
        sa.assertTrue(detailsPage.clickPlayButton().isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        lockDevice(Duration.ofSeconds(5));
        handleAlert();
        sa.assertTrue(subtitlePage.isOpened(), AUDIO_SUBTITLE_MENU_DID_NOT_OPEN);
        subtitlePage.chooseAudioLanguage(DEUTSCH);
        subtitlePage.chooseSubtitlesLanguage(DEUTSCH);
        subtitlePage.tapCloseButton();
        sa.assertTrue(videoPlayer.verifyVideoPaused(), VIDEO_NOT_PAUSED);
        videoPlayer.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs(DEUTSCH), "Checkmark was not present for the selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs(DEUTSCH), "Selected subtitle language is not as expected");
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
}
