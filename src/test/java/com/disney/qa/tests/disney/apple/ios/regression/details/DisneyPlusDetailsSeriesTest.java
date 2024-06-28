package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.ScreenOrientation;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DisneyPlusDetailsSeriesTest extends DisneyBaseTest {

    //Test constants
    private static final String DETAILS_TAB_METADATA_SERIES = "Loki";
    private static final String MOON_KNIGHT = "Moon Knight";
    private static final String ALL_METADATA_SERIES = "High School Musical: The Musical: The Series";
    private static final String MORE_THAN_TWENTY_EPISODES_SERIES = "Phineas and Ferb";
    private static final String SECRET_INVASION = "Secret Invasion";
    private static final String FOUR_EVER = "4Ever";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67401"})
    @Test(description = "Series Detail: attempt download season with more than 20 episodes", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73712"})
    @Test(description = "Series Detail Page > User Taps Seasons", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71632"})
    @Test(description = "Series Detail Page > User taps checkmark to remove watchlist", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION}, enabled = false)
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67985"})
    @Test(description = "Series Details: Verify Details Tab Metadata", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71699"})
    @Test(description = "Series Details - Verify UI Elements", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1), "Metadata year does not contain details tab year.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67707"})
    @Test(description = "Asset Detail Page > User taps Share Button", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifySeriesDetailsShare() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found.");
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel(String.format("%s | Disney+", DETAILS_TAB_METADATA_SERIES)).isPresent(), String.format("'%s | Disney+' title was not found on share actions.", DETAILS_TAB_METADATA_SERIES));
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Copy").isPresent(), "Share action 'Copy' was not found.");

        detailsPage.clickOnCopyShareLink();
        detailsPage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search page did not open");

        String url = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_loki_share_link");
        //There is limitation of char that can be displayed in search bar
        sa.assertTrue(expectedUrl.contains(url), "String.format(\"Share link for movie %s is not the expected\", DETAILS_TAB_METADATA_SERIES)");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72421"})
    @Test(description = "Series Detail Page > User taps on Suggested tab", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifySeriesSuggestedTab() {
        DisneyPlusHomeIOSPageBase home = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        home.clickSearchIcon();
        search.searchForMedia(SECRET_INVASION);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found on details page");
        details.compareSuggestedTitleToMediaTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72422"})
    @Test(description = "Series Details verify extras tab", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifySeriesExtrasTab() throws URISyntaxException, JsonProcessingException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        //Get duration from explore api
        ExploreContent series = getExploreApi().getSeries(getExploreSearchRequest().setEntityId(SERIES_EXTRA_ENTITY_ID).setProfileId(getAccount().getProfileId()));
        int seriesExtrasDuration = 0;
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(FOUR_EVER);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab was not found.");

        detailsPage.clickExtrasTab();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Extras tab play icon was not found");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "First extras title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "First extras description was not found");
        try {
            seriesExtrasDuration = series.getContainers().get(2).getItems().get(0).getVisuals().getDurationMs();
        } catch (IndexOutOfBoundsException e) {
            sa.assertTrue(false, "Series extra duration was returned null from the api");
        }
        long durationFromApi = TimeUnit.MILLISECONDS.toMinutes(seriesExtrasDuration);
        //Get actual duration from trailer cell
        String actualDuration = detailsPage.getFirstDurationLabel().getText().split("m")[0];
        sa.assertTrue(Long.toString(durationFromApi).equalsIgnoreCase(actualDuration),
                "Series extra duration is not the same value as actual value returned on details page.");
        detailsPage.getPlayIcon().click();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75166"})
    @Test(description = "Details Page - Resume State - Series - Episodes Tab", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyResumeStateSeriesEpisodesTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        detailsPage.clickPlayButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not opened.");
        videoPlayer.scrubToPlaybackPercentage(30);

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed.");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present after exiting video player.");

        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getEpisodesTab().isPresent(), "Episodes tab not present on Details page");
        detailsPage.getEpisodesTab().click();
        sa.assertTrue(detailsPage.getSeasonSelectorButton().isPresent(), "Season selector button not found on Episodes tab");
        detailsPage.getSeasonSelectorButton().click();
        sa.assertTrue(detailsPage.getItemPickerClose().isPresent(), "Season Item picker close CTA not found");
        sa.assertTrue(detailsPage.getSeasonItemPicker().isPresent(), "Season Item not found");
        detailsPage.getItemPickerClose().click();
        sa.assertTrue(detailsPage.getDownloadAllSeasonButton().isPresent(), "Download all session not found on Episodes tab");
        sa.assertTrue(detailsPage.isContentImageViewPresent(), "Content Image View not found on Episode container");
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Play Icon not found on Episodes container");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "Episode title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "Episode description was not found");
        sa.assertTrue(detailsPage.isDurationTimeLabelPresent(), "Episode duration was not found");
        sa.assertTrue(detailsPage.isSeriesDownloadButtonPresent("1", "1"), "Season button 1 button is was found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72545"})
    @Test(description = "Series Details verify resume behavior", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySeriesResumeBehavior() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.searchForMedia(SECRET_INVASION);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        detailsPage.getTrailerButton().click();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");

        videoPlayer.clickBackButton();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyVideoPlaying(sa);
        videoPlayer.validateResumeTimeRemaining(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71700"})
    @Test(description = "Details Page - Resume State - Series", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyResumeStateSeries() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        detailsPage.clickPlayButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not present.");
        videoPlayer.scrubToPlaybackPercentage(30);

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed.");
        sa.assertTrue(detailsPage.getBackButton().isPresent(), "Back button is not present");
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not present.");
        sa.assertTrue(detailsPage.getMediaTitle().contains("Loki"), "Prey media title not present.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not present.");
        detailsPage.isDolbyVisionPresentOrNot(sa);
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not present.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Subtitles / CC").isPresent(), "`Subtitles / CC` accessibility badge not present.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Audio Description").isPresent(), "`Audio Description` accessibility badge is not present.");

        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1),
                "Release date from metadata label does not match release date from details tab.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(2, detailsPage.getGenre(), 1),
                "Genre Thriller from metadata label does not match Genre Thriller from details tab.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(3, detailsPage.getGenre(), 2),
                "Genre Drama from metadata label does not match Genre Drama from details tab.");
        sa.assertTrue(detailsPage.getRating().isPresent(), "Rating not present.");
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Progress bar is not present.");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present.");

        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button is not present");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button not present.");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Trailer button not present.");

        sa.assertTrue(detailsPage.getEpisodeTitle("1", "1").isPresent(), "Episode Title not present.");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Content Description not present.");

        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getEpisodesTab().isPresent(), "Episodes tab not present");
        sa.assertTrue(detailsPage.isSuggestedTabPresent(), "Suggested tab not present.");
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab not present");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72418"})
    @Test(description = "Series Details verify playback behavior", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifySeriesPlayBehavior() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.clickSeriesTab();
        searchPage.selectRandomTitle();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyVideoPlayingFromBeginning(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67981"})
    @Test(description = "Series Details Page - Featured Episode Metadata", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySeriesDetailsPageFeaturedEpisodeMetadata() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        detailsPage.clickPlayButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not present.");
        videoPlayer.scrubToPlaybackPercentage(5);

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed.");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present.");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Series image is not present.");
        sa.assertTrue(detailsPage.getEpisodeTitle("1", "1").isPresent(), "Episode Title not present.");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Content Description not present.");

        detailsPage.clickContinueButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not present.");

        videoPlayer.scrubToPlaybackPercentage(99);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed.");
        detailsPage.tapBackButton();
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        sa.assertTrue(detailsPage.getEpisodeTitle("1", "2").isPresent(), "Episode Title not present.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1),
                "Release date from metadata label does not match release date from details tab.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(2, detailsPage.getGenre(), 1),
                "Genre Thriller from metadata label does not match Genre Thriller from details tab.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(3, detailsPage.getGenre(), 2),
                "Genre Drama from metadata label does not match Genre Drama from details tab.");
        detailsPage.clickDetailsTab();
        detailsPage.swipeTillActorsElementPresent();

        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }
}
