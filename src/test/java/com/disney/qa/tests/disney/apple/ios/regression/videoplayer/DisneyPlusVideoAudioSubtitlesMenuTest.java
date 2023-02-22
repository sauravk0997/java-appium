package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.tests.disney.apple.ios.regression.videoplayer.DisneyPlusVideoUpNextTest.SHORT_SERIES;

public class DisneyPlusVideoAudioSubtitlesMenuTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62343"})
    @Test(description = " Verify Menu, Languages and UI", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifySubtitleMenuLanguageUI() {
        setGlobalVariables();
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        initiatePlaybackFor(SHORT_SERIES);
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();

        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.isAudioHeadingPresent(), "Audio heading is not present");
        sa.assertTrue(subtitlePage.isSubtitleHeadingPresent(), "Subtitle Heading is not present");

        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("subtitleOff"),"Selected subtitle language is not as expected");

        subtitlePage.tapCloseButton();
        disneyPlusVideoPlayerIOSPageBase.isOpened();

        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("subtitleOff"), "Selected subtitle language is not as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62347"})
    @Test(description = "Verify Menu, Languages and UI(change options)", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifySubtitleMenuLanguageChangesPersist() {
        setGlobalVariables();
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        initiatePlaybackFor(SHORT_SERIES);
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();

        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("subtitleOff"),"Selected subtitle language is not as expected");
        subtitlePage.chooseAudioLanguage("Italiano");
        subtitlePage.chooseSubtitlesLanguage("English [CC]");
        subtitlePage.tapCloseButton();

        disneyPlusVideoPlayerIOSPageBase.isOpened();
        disneyPlusVideoPlayerIOSPageBase.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("Italiano"), "Checkmark was not present for the selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("English [CC]"), "Selected subtitle language is not as expected");

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
