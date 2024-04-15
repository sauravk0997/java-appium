package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusVideoPlayerArielAdsTest extends DisneyBaseTest {

@Maintainer("csolmaz")
@TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72851"})
@Test(description = "Ariel Ads Video Player > In Ad, Audio Subtitle button displayed/clickable", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
public void verifyArielAdsPlayerAudioSubtitleButton() {
    DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
    DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
    DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
    DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    DisneyPlusAudioSubtitleIOSPageBase audioSubtitlePage = new DisneyPlusAudioSubtitleIOSPageBase(getDriver());
    SoftAssert sa = new SoftAssert();
    setAccount(getAccountApi().createAccount(getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_BASIC),
            getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2));
    setAppToHomeScreen(getAccount());
    homePage.clickSearchIcon();
    homePage.getSearchNav().click();
    searchPage.searchForMedia("Ms. Marvel");
    List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
    results.get(0).click();
    detailsPage.clickPlayButton().isOpened();
    videoPlayer.displayVideoController();
    sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad badge label was not found during first ad.");
    sa.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.AUDIO_SUBTITLE_BUTTON), "Audio Subtitle button was not found.");

    videoPlayer.tapAudioSubTitleMenu();
    sa.assertTrue(audioSubtitlePage.isOpened(), "Audio / Subtitle menu was not opened during first ad.");
    sa.assertAll();
}
}
