package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisneyPlusDetailsSeriesTest extends DisneyBaseTest {

    //Test constants
    private static final String DETAILS_TAB_METADATA_SERIES = "Loki";
    private static final String ORIGINALS_METADATA_SERIES = "Moon Knight";
    private static final String ALL_METADATA_SERIES = "High School Musical: The Musical: The Series";
    private static final String ASPECT_RATIO_SERIES = "The Simpsons";
    private static final String MORE_THAN_TWENTY_EPISODES_SERIES = "Phineas and Ferb";
    private static final String DEVICE_TYPE = "capabilities.deviceType";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62441"})
    @Test(description = "Series Detail: attempt download season with more than 20 episodes", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifyDownloadMessageForSeasonMoreThanTwentyEpisodes() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusApplePageBase disneyPlusApplePageBase = initPage(DisneyPlusApplePageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.searchForMedia(MORE_THAN_TWENTY_EPISODES_SERIES);
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();
        pause(5);
        disneyPlusDetailsIOSPageBase.downloadAllOfSeason();

        sa.assertTrue(disneyPlusDetailsIOSPageBase.isAlertTitleDisplayed(), "Download alert title not found");
        sa.assertTrue(disneyPlusDetailsIOSPageBase.isTwentyDownloadsTextDisplayed(), "Download alert text not found.");
        sa.assertTrue(disneyPlusApplePageBase.isAlertDefaultBtnPresent(), "Download All Of Season One button not found");
        sa.assertTrue(disneyPlusApplePageBase.isAlertDismissBtnPresent(), "Dismiss button not found");
        sa.assertAll();
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62439"})
    @Test(description = "Series Detail Page > User Taps Seasons", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifySeriesSeasonPicker() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //search series
        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.searchForMedia("Jessie");
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();

        disneyPlusDetailsIOSPageBase.clickSeasonsButton("1");
        List <ExtendedWebElement> seasons = disneyPlusDetailsIOSPageBase.getSeasonsFromPicker();
        seasons.get(1).click();

        sa.assertTrue(disneyPlusDetailsIOSPageBase.isSeasonButtonDisplayed("2"), "Season has not changed to Season 2");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62435"})
    @Test(description = "Series Details Screen - Play vs Continue", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifySeriesPlayVsContinue() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.clickSeriesTab();
        disneyPlusSearchIOSPageBase.selectRandomTitle();
        //waiting for group watch pop-up to dismiss
        pause(3);
        sa.assertTrue(disneyPlusDetailsIOSPageBase.doesPlayButtonExist(), "Play button doesn't exist on details page.");
        disneyPlusDetailsIOSPageBase.clickPlayButton();
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isOpened(), "Video player was not opened.");
        pause(15);
        disneyPlusVideoPlayerIOSPageBase.clickBackButton();
        sa.assertTrue(disneyPlusDetailsIOSPageBase.isOpened(), "Video player was not closed.");
        sa.assertTrue(disneyPlusDetailsIOSPageBase.doesContinueButtonExist(), "Continue button doesn't exist on details page.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61849"})
    @Test(description = "Series Detail Page > User taps checkmark to remove watchlist", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifyRemoveSeriesFromWatchlist() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.clickSeriesTab();
        disneyPlusSearchIOSPageBase.selectRandomTitle();
        disneyPlusDetailsIOSPageBase.addToWatchlist();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        List<ExtendedWebElement> watchlist = disneyPlusMoreMenuIOSPageBase.getDisplayedTitles();
        watchlist.get(0).click();
        disneyPlusDetailsIOSPageBase.clickRemoveFromWatchlistButton();
        pause(10); //wait for refresh rate
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isWatchlistEmptyBackgroundDisplayed(), "Empty Watchlist text/logo was not properly displayed");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71792"})
    @Test(description = "Series Details: Verify Details Tab Metadata", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifySeriesDetailsTabMetadata() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        setAppToHomeScreen(getAccount());

        //Navigate to All Metadata Series
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickDetailsTab();
        detailsPage.swipeTillActorsElementPresent();

        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Detail Tab genre is not present");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Detail Tab formats not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62431"})
    @Test(description = "Series Details - Verify UI Elements", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifySeriesDetailsUIElements() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //Navigate to All Metadata Series
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ALL_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        //Verify main details page UI elements
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero banner image not present");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Details page logo image not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Details page content description not present");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Details page metadata label not present");
        sa.assertTrue(detailsPage.isPlayButtonDisplayed(), "Details page play button not present");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button not present");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Details page trailer button not displayed");
        sa.assertTrue(detailsPage.doesOneOrMoreSeasonDisplayed(), "One or more season not displayed.");
        sa.assertTrue(detailsPage.doesMetadataYearContainDetailsTabYear(), "Metadata year does not contain details tab year.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61879"})
    @Test(description = "Series Details - Verify Aspect Ratio Text", groups = {"Details", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAspectRatioText() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.searchForMedia(ASPECT_RATIO_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickDetailsTab();
        detailsPage.clickInfoView();
        detailsPage.clickToggleView();
        sa.assertTrue(detailsPage.isOriginalFormatTextPresent(), "Original format text not present");
        sa.assertTrue(detailsPage.isInformationTitleLabelPresent(), "'Remastered Aspect Ratio' info view title not found");
        sa.assertTrue(detailsPage.isInformationDescriptionPresent(), "Info toggle description label not found.");

        detailsPage.clickToggleView();
        sa.assertTrue(detailsPage.isWidescreenFormatTextPresent(), "Widescreen format text not present");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72938"})
    @Test(description = "Series Details - Verify Aspect Ratio Video", groups = {"Details", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAspectRatioVideo() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        Map<String, Integer> params = new HashMap<>();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.searchForMedia(ASPECT_RATIO_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        //Get original dimension
        detailsPage.clickDetailsTab();
        detailsPage.clickToggleView();
        detailsPage.clickPlayButton();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        params.put("originalX", videoPlayer.getX());
        params.put("originalY", videoPlayer.getY());
        videoPlayer.clickBackButton();

        //Get widescreen dimension
        detailsPage.clickDetailsTab();
        detailsPage.clickToggleView();
        detailsPage.clickContinueButton();
        params.put("widescreenX", videoPlayer.getX());
        params.put("widescreenY", videoPlayer.getY());
        sa.assertFalse(params.get("originalX").equals(params.get("widescreenX")),
                "Expected: Original Format X is different from Widescreen Format X");
        sa.assertFalse(params.get("originalY").equals(params.get("widescreenY")),
                "Expected: Original Format Y is different from Widescreen Format Y");
        sa.assertAll();
    }

    @Maintainer("dconyers")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71793"})
    @Test(description = "Originals Details: Verify Details Tab Metadata", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifyOriginalsDetailsTabMetadata() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        SoftAssert sa = new SoftAssert();
        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        setAppToHomeScreen(getAccount());

        //Navigate to Originals Series
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ORIGINALS_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickDetailsTab();

        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab release date not present");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Detail Tab genre is not present");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Detail Tab formats not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62282"})
    @Test(description = "Series Details - Deeplink", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifySeriesDetailsDeeplink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_series_detail_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Avengers Assemble"),
                "Avengers Assemble Details page did not open via deeplink.");
    }
}
