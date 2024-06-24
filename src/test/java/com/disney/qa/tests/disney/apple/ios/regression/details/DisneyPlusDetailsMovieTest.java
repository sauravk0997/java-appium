package com.disney.qa.tests.disney.apple.ios.regression.details;

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
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;


public class DisneyPlusDetailsMovieTest extends DisneyBaseTest {
    //Test constants
    private static final String HOCUS_POCUS = "Hocus Pocus";
    private static final String  ALL_METADATA_MOVIE = "Turning Red";
    private static final String WORLDS_BEST = "World's Best";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68448"})
    @Test(description = "Movies Detail Page > User taps checkmark to remove watchlist", groups = {TestGroup.WATCHLIST, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyRemoveMovieFromWatchlist() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69961"})
    @Test(description = "Movies Details Screen - If no Extra content, do not show section", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieNoExtras() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.searchForMedia(DisneyMovies.HOLIDAY_MAGIC.getName());
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();
        sa.assertFalse(disneyPlusHomeIOSPageBase.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_EXTRAS.getText())).isPresent());
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67891"})
    @Test(description = "Movie Details: Verify Details Tab Metadata", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyMoviesDetailsTabMetadata() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //Navigate to All Metadata Movie
        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69961"})
    @Test(description = "Movies Details - Verify UI Elements", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieDetailsUIElements() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //Navigate to all metadata movie
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ALL_METADATA_MOVIE);
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
        sa.assertTrue(detailsPage.isMovieDownloadButtonDisplayed(), "Details page download button not present");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1), "Metadata year does not contain details tab year.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67527"})
    @Test(description = "Movies Details - Deeplink", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieDetailsDeeplink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_movie_detail_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Cars"),
                "Cars Movie Details page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74597"})
    @Test(description = "Asset Detail Page > User taps Share Button", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieDetailsShare() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found.");
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel(String.format("%s | Disney+", HOCUS_POCUS)).isPresent(), String.format("'%s | Disney+' title was not found on share actions.", HOCUS_POCUS));
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Copy").isPresent(), "Share action 'Copy' was not found.");

        detailsPage.clickOnCopyShareLink();
        detailsPage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search page did not open");

        String url = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_hocus_pocus_share_link");

        sa.assertTrue(expectedUrl.contains(url), String.format("Share link for movie %s is not the expected", HOCUS_POCUS));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72420"})
    @Test(description = "Movies Detail Page > User taps on Suggested tab", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyMoviesSuggestedTab() {
        DisneyPlusHomeIOSPageBase home = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        home.clickSearchIcon();
        search.searchForMedia(WORLDS_BEST);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found on details page");
        details.compareSuggestedTitleToMediaTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72417"})
    @Test(description = "Movies Details verify playback behavior", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyMoviesPlayBehavior() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.clickMoviesTab();
        searchPage.selectRandomTitle();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyVideoPlayingFromBeginning(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69960"})
    @Test(description = "Movie Details verify resume UI", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieResumeUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        detailsPage.getTrailerButton().click();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");

        videoPlayer.clickBackButton();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyThreeIntegerVideoPlaying(sa);
        videoPlayer.scrubToPlaybackPercentage(30);

        //Validate resume UI
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present after exiting video player.");
        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button is not present.");
        sa.assertTrue(detailsPage.getProgressBar().isPresent(), "Progress bar is not present.");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().isPresent(), "Continue watching time remaining is not present.");

        //Validate detail page UI is same after change to Continue state
        //share, back arrow
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button is not present.");
        sa.assertTrue(detailsPage.getBackArrow().isPresent(), "Back arrow button is not present.");

        //Validate Dolby Vision present / not present on certain devices
        detailsPage.isDolbyVisionPresentOrNot(sa);

        //Media features
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Subtitles / CC").isPresent(), "`Subtitles / CC` audio quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Audio Description").isPresent(), "`Audio Description` audio quality is not found.");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero banner image is not present.");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Details page logo image is not present.");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Details page content description is not present.");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Details page metadata label is not present.");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button is not present.");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Details page trailer button is not present.");
        sa.assertTrue(detailsPage.isMovieDownloadButtonDisplayed(), "Details page download button is not present.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1), "Metadata year does not contain details tab year.");

        //Tabs
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab is not present.");
        sa.assertTrue(detailsPage.getExtrasTab().isPresent(), "Extras tab is not present.");
        sa.assertTrue(detailsPage.getSuggestedTab().isPresent(), "Suggested tab is not present.");
        sa.assertAll();
    }
}
