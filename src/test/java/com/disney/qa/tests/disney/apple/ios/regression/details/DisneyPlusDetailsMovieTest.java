package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.search.assets.DisneyMovies;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;


public class DisneyPlusDetailsMovieTest extends DisneyBaseTest {
    //Test constants
    private static final String  DETAILS_TAB_METADATA_MOVIE = "Hocus Pocus";
    private static final String  ALL_METADATA_MOVIE = "Turning Red";
    private static final String DEVICE_TYPE = "capabilities.deviceType";

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62395"})
    @Test(description = "Movies Details Screen - Play vs Continue", groups = {"Details"})
    public void verifyMoviePlayVsContinue() {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.clickMoviesTab();
        List<ExtendedWebElement> movies = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        movies.get(0).click();
        sa.assertTrue(disneyPlusDetailsIOSPageBase.doesPlayButtonExist(), "Expected - play button should exists on details page");
        disneyPlusDetailsIOSPageBase.clickPlayButton().isOpened();
        //Wait for content to load
        pause(5);
        disneyPlusVideoPlayerIOSPageBase.clickBackButton().isOpened();
        sa.assertTrue(disneyPlusDetailsIOSPageBase.doesContinueButtonExist(), "'CONTINUE' button doesn't exist on details page");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61849"})
    @Test(description = "Movies Detail Page > User taps checkmark to remove watchlist", groups = {"Details"})
    public void verifyRemoveMovieFromWatchlist() {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.clickMoviesTab();
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62393"})
    @Test(description = "Movies Details Screen - If no Extra content, do not show section", groups = {"Details"})
    public void verifyMovieNoExtras() {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.searchForMedia(DisneyMovies.HOLIDAY_MAGIC.getName());
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();
        sa.assertFalse(disneyPlusHomeIOSPageBase.getTypeButtonByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_EXTRAS.getText())).isElementPresent());
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71794"})
    @Test(description = "Movie Details: Verify Details Tab Metadata", groups = {"Details"})
    public void verifyMoviesDetailsTabMetadata() {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        //Navigate to All Metadata Movie
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_MOVIE);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickDetailsTab();
        detailsPage.swipeTillActorsElementPresent();

        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present");
        sa.assertTrue(detailsPage.isDurationDisplayed(), "Detail Tab duration not present");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Detail Tab genre is not present");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Detail Tab formats not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62391"})
    @Test(description = "Movies Details - Verify UI Elements", groups = {"Details"})
    public void verifyMovieDetailsUIElements() {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        //Navigate to all metadata movie
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ALL_METADATA_MOVIE);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        //Currently banner is not detected on iPad by Alice driver (QCE-684).
        if("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.BANNER.getText());
        } else {
            LOGGER.info((R.CONFIG.get(DEVICE_TYPE)) + " device type detected, will not validate banner image");
        }

        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Details page logo image not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Details page content description not present");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Details page metadata label not present");
        sa.assertTrue(detailsPage.isPlayButtonDisplayed(), "Details page play button not present");
        sa.assertTrue(detailsPage.isGroupWatchButtonDisplayed(), "Details page group watch button not present");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button not present");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Details page trailer button not displayed");
        sa.assertTrue(detailsPage.isDownloadButtonDisplayed(), "Details page download button not present");
        sa.assertTrue(detailsPage.doesMetadataYearContainDetailsTabYear(), "Metadata year does not contain details tab year.");

        sa.assertAll();
    }
}