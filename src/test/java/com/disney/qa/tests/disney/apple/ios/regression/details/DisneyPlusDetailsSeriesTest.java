package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.ScreenOrientation;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.disney.qa.api.disney.DisneyEntityIds.SERIES_EXTRA;
import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusDetailsSeriesTest extends DisneyBaseTest {

    //Test constants
    private static final String DETAILS_TAB_METADATA_SERIES = "Loki";
    private static final String ALL_METADATA_SERIES = "High School Musical: The Musical: The Series";
    private static final String MORE_THAN_TWENTY_EPISODES_SERIES = "Phineas and Ferb";
    private static final String SECRET_INVASION = "Secret Invasion";
    private static final String FOUR_EVER = "4Ever";
    String TANGLED_THE_SERIES = "Tangled: The Series - Short Cuts";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";
    private static final String SEARCH_PAGE_DID_NOT_OPEN = "Search page did not open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";
    private static final String DOWNLOADS_PAGE_DID_NOT_OPEN = "Downloads page did not open";
    private static final String AUDIO_VIDEO_BADGE = "Audio_Video_Badge";
    private static final String RATING = "Rating";
    private static final String RELEASE_YEAR_DETAILS = "Release_Year";
    private static final String CONTENT_DESCRIPTION = "Content_Description";
    private static final String CONTENT_PROMO_TITLE = "Content_Promo_Title";
    private static final String CONTENT_TITLE = "Content_Title";
    private static final String DISNEY_JUNIOR_ARIEL = "Disney Junior Ariel";
    private static final String PLAY = "Play";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67401"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
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
        List<ExtendedWebElement> seasons = disneyPlusDetailsIOSPageBase.getSeasonsFromPicker();
        seasons.get(1).click();

        sa.assertTrue(disneyPlusDetailsIOSPageBase.isSeasonButtonDisplayed("2"), "Season has not changed to Season 2");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71632"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyAddAndRemoveSeriesFromWatchlist() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.clickSeriesTab();
        searchPage.selectRandomTitle();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        String contentTitle = detailsPage.getMediaTitle();
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(), "Add to watchList button not displayed");
        detailsPage.addToWatchlist();
        sa.assertTrue(detailsPage.getRemoveFromWatchListButton().isPresent(),
                "Remove from watchlist button not displayed after adding content");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(moreMenuPage.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertTrue(moreMenuPage.getTypeCellLabelContains(contentTitle).isPresent(),
                "Series title was not added to the watchlist");

        //Remove from watchList
        moreMenuPage.getTypeCellLabelContains(contentTitle).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickRemoveFromWatchlistButton();
        detailsPage.waitForWatchlistButtonToAppear();
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(),
                "Add to watchList button not displayed after removing content");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(moreMenuPage.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertFalse(moreMenuPage.getTypeCellLabelContains(contentTitle).isPresent(),
                "Series title was not removed from watchlist");
        sa.assertTrue(moreMenuPage.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not properly displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67985"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeriesDetailsShare() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_SERIES);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found");
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel(String.format("%s | Disney+", DETAILS_TAB_METADATA_SERIES)).isPresent(), String.format("'%s | Disney+' title was not found on share actions.", DETAILS_TAB_METADATA_SERIES));
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Copy").isPresent(), "Share action 'Copy' was not found");

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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeriesExtrasTab() throws URISyntaxException, JsonProcessingException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        //Get duration from explore api
        ExploreContent series = getExploreApi().getSeries(getDisneyExploreSearchRequest().setEntityId(SERIES_EXTRA.getEntityId()).setProfileId(getAccount().getProfileId()));
        int seriesExtrasDuration = 0;
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(FOUR_EVER);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab was not found");

        detailsPage.clickExtrasTab();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
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
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75166"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
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
        sa.assertTrue(detailsPage.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.scrubToPlaybackPercentage(30);

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed.");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present after exiting video player");

        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
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
        sa.assertTrue(detailsPage.isSeriesDownloadButtonPresent("1", "1"), "Season button 1 button is was found");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72545"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
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
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        videoPlayer.clickBackButton();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyVideoPlaying(sa);
        videoPlayer.validateResumeTimeRemaining(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71700"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
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
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        detailsPage.clickPlayButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not present");
        videoPlayer.scrubToPlaybackPercentage(30);

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed");
        sa.assertTrue(detailsPage.getBackButton().isPresent(), "Back button is not present");
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not present");
        sa.assertTrue(detailsPage.getMediaTitle().contains("Loki"), "Prey media title not present.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not present");
        detailsPage.isDolbyVisionPresentOrNot(sa);
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not present");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Subtitles / CC").isPresent(), "`Subtitles / CC` accessibility badge not present");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Audio Description").isPresent(), "`Audio Description` accessibility badge is not present");

        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1),
                "Release date from metadata label does not match release date from details tab");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(2, detailsPage.getGenre(), 1),
                "Genre Thriller from metadata label does not match Genre Thriller from details tab");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(3, detailsPage.getGenre(), 2),
                "Genre Drama from metadata label does not match Genre Drama from details tab");
        sa.assertTrue(detailsPage.getRating().isPresent(), "Rating not present");
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Progress bar is not present");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present");

        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button is not present");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button not present");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Trailer button not present");

        sa.assertTrue(detailsPage.getEpisodeTitle("1", "1").isPresent(), "Episode Title not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Content Description not present");

        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getEpisodesTab().isPresent(), "Episodes tab not present");
        sa.assertTrue(detailsPage.isSuggestedTabPresent(), "Suggested tab not present");
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab not present");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72418"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
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
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyVideoPlayingFromBeginning(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67981"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
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
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.scrubToPlaybackPercentage(5);

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Series image is not present");
        sa.assertTrue(detailsPage.getEpisodeTitle("1", "1").isPresent(), "Episode Title not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Content Description not present");

        detailsPage.clickContinueButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        videoPlayer.scrubToPlaybackPercentage(99);
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.tapBackButton();
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        sa.assertTrue(detailsPage.getEpisodeTitle("1", "2").isPresent(), "Episode Title not present");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1),
                "Release date from metadata label does not match release date from details tab");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(2, detailsPage.getGenre(), 1),
                "Genre Thriller from metadata label does not match Genre Thriller from details tab");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(3, detailsPage.getGenre(), 2),
                "Genre Drama from metadata label does not match Genre Drama from details tab");
        detailsPage.clickDetailsTab();
        detailsPage.swipeTillActorsElementPresent();

        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72419"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyComingSoonSeriesBehavior() {
        String httpPrefix = "https://";
        SoftAssert sa = new SoftAssert();
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //TODO: Replace entity-id, deeplink from API when https://jira.disney.com/browse/QP-3247 is ready
        String entityID = R.TESTDATA.get("disney_prod_series_goosebumps_the_vanishing_entity_id");
        String deeplink = R.TESTDATA.get("disney_prod_series_goosebumps_the_vanishing_deeplink");

        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);

        sa.assertEquals(detailsPage.getPromoLabelText(), visualsResponse.getPromoLabel().getHeader(),
                "Promo title didn't match with api promo title");

        //Subscriber can play trailer (if available)
        String contentTitle = detailsPage.getContentTitle();
        detailsPage.getTrailerActionButton().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart().verifyVideoPlaying(sa);
        Assert.assertTrue(videoPlayer.clickBackButton().isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        //Subscriber can add title to Watchlist
        detailsPage.clickWatchlistButton();
        detailsPage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(contentTitle),
                "Titles were not added to the Watchlist");
        moreMenu.clickHomeIcon();

        //Subscriber can interact with tabs
        detailsPage.getSuggestedTab().click();
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(),
                "Content title is missing from the extra tab");
        detailsPage.clickDetailsTab();
        sa.assertTrue(detailsPage.getDetailsTabTitle().contains(contentTitle), DETAILS_PAGE_DID_NOT_OPEN);

        //Subscriber can share link to title over social media
        detailsPage.getShareBtn().click();
        String expectedLabel = String.format("%s | Disney+", contentTitle);
        sa.assertTrue(detailsPage.getTypeOtherByLabel(expectedLabel).isPresent(),
                String.format("'%s' title was not found on share actions", expectedLabel));

        detailsPage.clickOnCopyShareLink();
        detailsPage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.getSearchBar().click();
        String url = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_series_goosebumps_the_vanishing_deeplink");
        sa.assertTrue(expectedUrl.contains(url.replace(httpPrefix, "")),
                String.format("Share link for coming soon series %s is not as expected", contentTitle));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71701"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyComingSoonSeriesUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //TODO: Replace entity-id, deeplink from API when https://jira.disneystreaming.com/browse/QP-3247 is ready
        String entityID = R.TESTDATA.get("disney_prod_series_goosebumps_the_vanishing_entity_id");
        String deeplink = R.TESTDATA.get("disney_prod_series_goosebumps_the_vanishing_deeplink");
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getContentMetadataFromAPI(visualsResponse);

        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        sa.assertEquals(detailsPage.getPromoLabelText(), exploreAPIData.get(CONTENT_PROMO_TITLE),
                "Promo title didn't match with api promo title");
        sa.assertEquals(detailsPage.getMediaTitle(), exploreAPIData.get(CONTENT_TITLE),
                "Content title didn't match with api content title");
        sa.assertEquals(detailsPage.getContentDescriptionText(), exploreAPIData.get(CONTENT_DESCRIPTION),
                "Description didn't match with api description value");

        //Featured Metadata
        String metadataString = detailsPage.getMetaDataLabel().getText();
        getGenreMetadataLabels(visualsResponse).forEach(value -> sa.assertTrue(metadataString.contains(value),
                String.format("%s value was not present on Metadata label", value)));

        //Verify if "Audio/Video/Format Quality" value matches with api, if api has returned any value
        if (exploreAPIData.containsKey(AUDIO_VIDEO_BADGE)) {
            ((List<String>) exploreAPIData.get(AUDIO_VIDEO_BADGE)).forEach(badge ->
                    sa.assertTrue(detailsPage.getStaticTextByLabelContains(badge).isPresent(),
                            String.format("Audio video badge %s is not present on details page featured area for " +
                                    "coming soon content", badge)));
        }
        //Verify if ratings value matches with api, if api has returned any value
        if (exploreAPIData.containsKey(RATING)) {
            sa.assertTrue(detailsPage.getStaticTextByLabel(exploreAPIData.get(RATING).toString()).isPresent(),
                    "Rating value is not present on details page featured area for coming soon content");
        }

        //Verify if CTA buttons are present
        sa.assertTrue(detailsPage.getTrailerActionButton().isPresent(),
                "Trailer button is not present on coming soon content");
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(),
                "Watchlist button is not present on coming soon content");
        sa.assertTrue(detailsPage.getShareBtn().isPresent(),
                "Share button is not present on coming soon content");

        //Verify if tabs are present
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(),
                "Details tab is not present on coming soon content");
        sa.assertTrue(detailsPage.getExtrasTab().isPresent(),
                "Extra tab is not present on coming soon content");
        sa.assertTrue(detailsPage.getSuggestedTab().isPresent(),
                "Suggested tab is not present on coming soon content");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75416"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileDetailsPageWatchListButton() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        SoftAssert sa = new SoftAssert();

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang())
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DISNEY_JUNIOR_ARIEL);
        searchPage.getDynamicAccessibilityId(DISNEY_JUNIOR_ARIEL).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button not displayed");
        detailsPage.clickWatchlistButton();
        sa.assertTrue(detailsPage.getRemoveFromWatchListButton().isPresent(),
                "remove from watchlist button wasn't displayed");
        detailsPage.clickMoreTab();
        detailsPage.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertTrue(detailsPage.getTypeCellLabelContains(DISNEY_JUNIOR_ARIEL).isPresent(),
                "Title was not added to the watchlist");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75418"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileDetailsPageTrailerButton() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang())
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DISNEY_JUNIOR_ARIEL);
        searchPage.getDynamicAccessibilityId(DISNEY_JUNIOR_ARIEL).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Trailer button not displayed");
        detailsPage.getTrailerButton().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForTrailerToEnd(75, 5);
        Assert.assertTrue(detailsPage.isOpened(), "After trailer ended, not returned to Details page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76972"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileDetailsPageSeriesDownload() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang())
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DISNEY_JUNIOR_ARIEL);
        searchPage.getDynamicAccessibilityId(DISNEY_JUNIOR_ARIEL).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(detailsPage.getDownloadAllSeasonButton().isPresent(), "Download button is not present");

        //Start season download
        detailsPage.downloadAllOfSeason();
        sa.assertTrue(detailsPage.isAlertTitleDisplayed(), "Download alert title not found");
        sa.assertTrue(detailsPage.isDownloadSeasonButtonDisplayed("1"),
                "Download Season One button not found");
        sa.assertTrue(detailsPage.isAlertDismissBtnPresent(), "Dismiss button not found");
        detailsPage.clickAlertDismissBtn();
        Assert.assertFalse(detailsPage.isAlertTitleDisplayed(), "Download Alert was not dismissed");

        //verify pause and remove download
        detailsPage.downloadAllOfSeason();
        detailsPage.clickAlertConfirm();
        sa.assertTrue(detailsPage.isStopOrPauseDownloadIconDisplayed(),
                "Download not started, Stop or Pause Download button not displayed");
        detailsPage.clickStopOrPauseDownload();
        sa.assertTrue(detailsPage.isPauseDownloadButtonDisplayed(), "Pause Download button not displayed on alert");
        sa.assertTrue(detailsPage.isRemoveDownloadButtonDisplayed(), "Remove Download button not displayed on alert");
        sa.assertTrue(detailsPage.isDownloadInProgressStatusDisplayed(),
                "Download in Progress status not displayed on alert");
        sa.assertTrue(detailsPage.isAlertDismissBtnPresent(), "Dismiss button not found on alert");
        detailsPage.clickAlertDismissBtn();
        sa.assertFalse(detailsPage.isAlertTitleDisplayed(), "Pause or Remove Alert was not dismissed");

        navigateToTab((DisneyPlusApplePageBase.FooterTabs.DOWNLOADS));
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(detailsPage.getStaticTextByLabel(DISNEY_JUNIOR_ARIEL).isPresent(),
                "Downloaded Series was not present in downloads page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76971"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileDetailsPageSeriesEpisodeDownload() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang())
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DISNEY_JUNIOR_ARIEL);
        searchPage.getDynamicAccessibilityId(DISNEY_JUNIOR_ARIEL).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(detailsPage.isSeriesDownloadButtonPresent("1", "1"),
                "Series download button is not present");

        detailsPage.getEpisodeToDownload("1","1").click();
        sa.assertTrue(detailsPage.isStopOrPauseDownloadIconDisplayed(),
                "Download not started, Stop or Pause Download button not displayed");
        detailsPage.clickStopOrPauseDownload();
        sa.assertTrue(detailsPage.isPauseDownloadButtonDisplayed(), "Pause Download button not displayed on alert");
        sa.assertTrue(detailsPage.isRemoveDownloadButtonDisplayed(), "Remove Download button not displayed on alert");
        sa.assertTrue(detailsPage.isDownloadInProgressStatusDisplayed(),
                "Download in Progress status not displayed on alert");
        sa.assertTrue(detailsPage.isAlertDismissBtnPresent(), "Dismiss button not found on alert");
        detailsPage.clickAlertDismissBtn();
        Assert.assertFalse(detailsPage.isAlertTitleDisplayed(), "Pause or Remove Alert was not dismissed");

        navigateToTab((DisneyPlusApplePageBase.FooterTabs.DOWNLOADS));
        Assert.assertTrue(detailsPage.getStaticTextByLabel(DISNEY_JUNIOR_ARIEL).isPresent(),
                "Downloaded Series was not present in downloads page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66706"})
    @Test(groups = {TestGroup.SMOKE, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyTapPlayRemoveDismissOnDownloadsScreen() {
        String episodeOneTitle = "Check Mate";
        int pollingInSeconds = 5;
        int timeoutInSeconds = 90;
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        SoftAssert sa = new SoftAssert();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_tangled_short_deeplink"));
        detailsPage.isOpened();
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeUp(2500);
        }
        detailsPage.getEpisodeToDownload("1", "1").click();
        detailsPage.waitForOneEpisodeDownloadToComplete(timeoutInSeconds, pollingInSeconds);
        detailsPage.getHuluSeriesDownloadCompleteButton().click();

        //Verify download complete modal UI
        sa.assertTrue(detailsPage.getStaticTextByLabel(episodeOneTitle).isPresent(),
                "Episode title was not displayed on download modal");
        sa.assertTrue(detailsPage.isDownloadModalPlayButtonDisplayed(),
                "Play button was not displayed on download modal");
        sa.assertTrue(detailsPage.isDownloadModalRenewButtonDisplayed(),
                "Renew button was not displayed on download modal");
        sa.assertTrue(detailsPage.isDownloadModalRemoveButtonDisplayed(),
                "Remove button was not displayed on download modal");
        sa.assertTrue(detailsPage.isAlertDismissBtnPresent(),
                "Dismiss button was not displayed on download modal");

        //Verify play button alert
        detailsPage.getDownloadModalPlayButton().click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.getSubTitleLabel().contains(episodeOneTitle),
                "Playback of Download Content didn't begin");
        videoPlayer.clickBackButton();

        //Verify dismiss button to close the modal
        detailsPage.getHuluSeriesDownloadCompleteButton().click();
        detailsPage.clickAlertDismissBtn();
        Assert.assertFalse(detailsPage.isViewAlertPresent(),
                "Download alert modal was not closed after clicking the dismiss button");

        //Verify remove download button
        detailsPage.getHuluSeriesDownloadCompleteButton().click();
        detailsPage.getSystemAlertDestructiveButton().click();
        Assert.assertTrue(detailsPage.getHuluSeriesDownloadCompleteButton().isElementNotPresent(SHORT_TIMEOUT),
                "Content is not removed from the Device");
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.DOWNLOADS));
        Assert.assertTrue(detailsPage.getStaticTextByLabel(TANGLED_THE_SERIES).isElementNotPresent(SHORT_TIMEOUT),
                "Series content title is present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75415"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileSeriesDetailsPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang())
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DISNEY_JUNIOR_ARIEL);
        searchPage.getDynamicAccessibilityId(DISNEY_JUNIOR_ARIEL).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        String entityID = R.TESTDATA.get("disney_prod_series_disney_junior_ariel_entity_id");
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getContentMetadataFromAPI(visualsResponse);

        ExploreContent seriesApiContent = getSeriesApi(entityID, DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String firstEpisodeTitle =
                seriesApiContent.getSeasons().get(0).getItems().get(0).getVisuals().getEpisodeTitle();

        //Verify main details page UI elements
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero banner image not present");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Details page logo image not present");
        sa.assertTrue(detailsPage.isKidThemeBackgroudUIDisplayed(), "UI on detail page is not in kid mode theme");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Details page content description not present");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Details page metadata label not present");
        sa.assertTrue(detailsPage.isPlayButtonDisplayed(), "Details page play button not present");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button not present");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Details page trailer button not displayed");
        sa.assertTrue(detailsPage.getMetaDataLabel().getText().contains("Season"), "Season label not displayed on " +
                "metadata label");

        //Verify if "Genre" value matches with api, if api has returned any value
        String metadataString = detailsPage.getMetaDataLabel().getText();
        getGenreMetadataLabels(visualsResponse).forEach(value -> sa.assertTrue(metadataString.contains(value),
                String.format("%s value was not present on Metadata label", value)));
        //Verify if "Audio/Video/Format Quality" value matches with api, if api has returned any value
        if (exploreAPIData.containsKey(AUDIO_VIDEO_BADGE)) {
            ((List<String>) exploreAPIData.get(AUDIO_VIDEO_BADGE)).forEach(badge ->
                    sa.assertTrue(detailsPage.getStaticTextByLabelContains(badge).isPresent(),
                            String.format("Audio video badge %s is not present on details page featured area", badge)));
        }
        //Verify if ratings value matches with api, if api has returned any value
        if (exploreAPIData.containsKey(RATING)) {
            sa.assertTrue(detailsPage.getStaticTextByLabelContains(exploreAPIData.get(RATING).toString()).isPresent(),
                    "Rating value is not present on details page featured area");
        }
        //Verify if release year value matches with api, if api has returned any value
        if (exploreAPIData.containsKey(RELEASE_YEAR_DETAILS)) {
            sa.assertTrue(detailsPage.getStaticTextByLabelContains(exploreAPIData.get(RELEASE_YEAR_DETAILS).toString()).isPresent(),
                    "Release year value is not present on details page featured area");
        }

        detailsPage.getEpisodesTab().click();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getSeasonSelectorButton().isPresent(), "Season selector button not found on Episodes tab");
        sa.assertTrue(detailsPage.getDownloadAllSeasonButton().isPresent(), "Series download icon was not found");
        sa.assertTrue(detailsPage.getEpisodeTitleFromEpisodsTab("1", firstEpisodeTitle).isPresent(),
                "Episode Number and Name was not found");
        sa.assertTrue(detailsPage.isContentImageViewPresent(), "Content Image View not found on Episode container");
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Play Icon not found on Episodes container");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "Episode title was not found");
        sa.assertTrue(detailsPage.isDurationTimeLabelPresent(), "Episode duration was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "Episode description was not found");
        sa.assertTrue(detailsPage.isSeriesDownloadButtonPresent("1","1"), "Episode download icon was not found");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66736"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadScreenForSeriesViewUI() {
        String season1 = "Season 1";
        String season2 = "Season 2";
        String one = "1";
        String titleErrorMessage = "title not found";
        String sizeIdentifier = "MB";
        int pollingInSeconds = 6;
        int timeoutInSeconds = 120;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DETAILS_TAB_METADATA_SERIES);
        searchPage.getDynamicAccessibilityId(DETAILS_TAB_METADATA_SERIES).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        //Get season1 episode details from API
        ExploreContent seriesApiContent = getSeriesApi(R.TESTDATA.get("disney_prod_loki_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        Visuals seasonDetails = seriesApiContent.getSeasons().get(0).getItems().get(0).getVisuals();

        //Download season 1 & 2
        detailsPage.downloadAllOfSeason();
        detailsPage.clickAlertConfirm();
        detailsPage.getSeasonSelectorButton().click();
        detailsPage.getStaticTextByLabel(season2).click();
        detailsPage.downloadAllOfSeason();
        detailsPage.clickAlertConfirm();
        detailsPage.getSeasonSelectorButton().click();
        detailsPage.getStaticTextByLabel(season1).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(timeoutInSeconds, pollingInSeconds);

        //Navigate to Download page
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        downloads.clickSeriesMoreInfoButton();

        //verify Download detail view
        sa.assertTrue(downloads.getBackArrow().isPresent(), "Back button not present");
        sa.assertTrue(downloads.getStaticTextByLabelContains(DETAILS_TAB_METADATA_SERIES).isPresent(),
                DETAILS_TAB_METADATA_SERIES + " title was not found on downloads screen");
        sa.assertTrue(downloads.getEditButton().isPresent(), "Edit button not found on download screen");
        sa.assertTrue(downloads.getStaticTextByLabel(season1).isPresent(),
                season1 + " " + titleErrorMessage);
        sa.assertTrue(downloads.getStaticTextByLabel(seasonDetails.getEpisodeTitle()).isPresent(),
                "Episode " + titleErrorMessage);
        sa.assertTrue(downloads.getStaticTextByLabelContains(seasonDetails.getMetastringParts().getRatingInfo().getRating().getText()).isPresent(),
                "Episode rating detail was not found");
        sa.assertTrue(downloads.isEpisodeNumberDisplayed(seasonDetails.getEpisodeNumber()),
                "Episode Number was not found");
        sa.assertTrue(downloads.getStaticTextByLabelContains(sizeIdentifier).isPresent(),
                "Size of episode was not found");
        long durationFromApi = TimeUnit.MILLISECONDS.toMinutes(seasonDetails.getDurationMs());
        sa.assertTrue(downloads.getStaticTextByLabelContains(String.valueOf(durationFromApi)).isPresent(),
                "Duration of episode was not found");
        sa.assertTrue(downloads.getDownloadCompleteButton().isPresent(),
                "Download state button was not found");
        sa.assertTrue(downloads.getTypeButtonContainsLabel(PLAY).isPresent(),
                "Episode artwork and play button was not found");
        downloads.getStaticTextByLabel(seasonDetails.getEpisodeTitle()).click();
        LOGGER.info("Description:- " +seasonDetails.getDescription().getBrief());
        sa.assertTrue(downloads.getEpisodeDescription(one, one)
                .getText().equals(seasonDetails.getDescription().getFull()),
                "Episode description detail was not found after episode expanded");

        downloads.getTypeButtonByLabel(season1).click();
        sa.assertFalse(downloads.isEpisodeCellDisplayed(one, one), season1 + " not collapsed");
        sa.assertTrue(downloads.getStaticTextByLabel(season2).isPresent(),
                season2 + " " + titleErrorMessage);
        sa.assertTrue(downloads.getStaticTextByLabel(season1).isPresent(),
                season1 + " " + titleErrorMessage);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75573"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadedEpisodePlayback() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        int seasonNumber = 1;
        int episodeNumber = 1;
        String episodeTitle;
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));

        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }
        detailsPage.getEpisodeToDownload(Integer.toString(seasonNumber), Integer.toString(episodeNumber)).click();
        ExploreContent seriesApiContent = getSeriesApi(R.TESTDATA.get("disney_prod_series_bluey_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        try {
            episodeTitle = seriesApiContent.getSeasons().get(seasonNumber - 1).getItems().get(episodeNumber - 1)
                    .getVisuals().getEpisodeTitle();
        } catch (Exception e) {
            throw new SkipException("Skipping test because of unexpected exception getting episode title");
        }
        if (episodeTitle == null) {
            throw new SkipException("No episode title found for the desired series episode in Explore API");
        }
        detailsPage.waitForOneEpisodeDownloadToComplete(SIXTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);
        detailsPage.getHuluSeriesDownloadCompleteButton().click();
        detailsPage.getDownloadModalPlayButton().click();

        Assert.assertTrue(videoPlayer.isOpened(),
                "Video player did not open after choosing a downloaded episode");
        videoPlayer.waitForVideoToStart();
        String playerSubtitle = videoPlayer.getSubTitleLabel();
        Assert.assertTrue(playerSubtitle.contains(episodeTitle),
                "Video player title does not match with expected title: " + episodeTitle);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75577"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.DOWNLOADS, TestGroup.SERIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadModalWhenEpisodeDownloadIsInProgress() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String firstEpisodeTitle = "Glorious Purpose";
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }
        detailsPage.getFirstEpisodeDownloadButton().click();
        Assert.assertTrue(detailsPage.isStopOrPauseDownloadIconDisplayed(),
                "Download not started, Stop or Pause Download button not displayed");
        detailsPage.clickStopOrPauseDownload();

        sa.assertTrue(detailsPage.getStaticTextByLabel(firstEpisodeTitle).isElementPresent(),
                "Content Title was not displayed on Download modal");
        sa.assertTrue(detailsPage.isPauseDownloadButtonDisplayed(),
                "Pause Download button not displayed on Download modal");
        sa.assertTrue(detailsPage.isRemoveDownloadButtonDisplayed(),
                "Remove Download button not displayed on Download modal");
        sa.assertTrue(detailsPage.isDownloadInProgressStatusDisplayed(),
                "Download in Progress status not displayed on Download modal");
        sa.assertTrue(detailsPage.isAlertDismissBtnPresent(), "Dismiss button not found on Download modal");

        detailsPage.getPauseDownloadButton().click();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                "Download Modal was still visible");
        Assert.assertTrue(detailsPage.getFirstEpisodeDownloadButton().isElementPresent(),
                "Download was not paused. Download icon was not present");
        detailsPage.getFirstEpisodeDownloadButton().click();
        Assert.assertTrue(detailsPage.isDownloadPausedInDownloadModal(),
                "Download state did not change to Paused");

        detailsPage.getRemoveDownloadButton().click();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                "Download Modal was still visible");
        Assert.assertTrue(detailsPage.getFirstEpisodeDownloadButton().isElementPresent(),
                "Download was not removed. Download icon was not present");
        Assert.assertFalse(detailsPage.getStopOrPauseDownloadIcon().isElementPresent(THREE_SEC_TIMEOUT),
                "Download was not removed. Stop/Pause Download icon was still displayed");

        detailsPage.getFirstEpisodeDownloadButton().click();
        Assert.assertTrue(detailsPage.isStopOrPauseDownloadIconDisplayed(),
                "Download not started, Stop or Pause Download button not displayed");
        detailsPage.clickStopOrPauseDownload();
        Assert.assertTrue(detailsPage.getViewAlert().isElementPresent(),
                "Download modal was not present");
        detailsPage.clickAlertDismissBtn();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                "Download Modal was still visible");
        Assert.assertTrue(detailsPage.isStopOrPauseDownloadIconDisplayed(),
                "Download is not in progress. Stop/Pause Download icon was not displayed");

        sa.assertAll();
    }

    private Map<String, Object> getContentMetadataFromAPI(Visuals visualsResponse) {
        Map<String, Object> exploreAPIMetadata = new HashMap<>();

        exploreAPIMetadata.put(CONTENT_TITLE, visualsResponse.getTitle());
        exploreAPIMetadata.put(CONTENT_DESCRIPTION, visualsResponse.getDescription().getBrief());
        if (visualsResponse.getPromoLabel() != null) {
            exploreAPIMetadata.put(CONTENT_PROMO_TITLE, visualsResponse.getPromoLabel().getHeader());
        }

        //Audio visual badge
        if (visualsResponse.getMetastringParts().getAudioVisual() != null) {
            List<String> audioVideoApiBadge = new ArrayList<>();
            visualsResponse.getMetastringParts().getAudioVisual().getFlags()
                    .forEach(flag -> audioVideoApiBadge.add(flag.getTts()));
            exploreAPIMetadata.put(AUDIO_VIDEO_BADGE, audioVideoApiBadge);
        }

        //Rating
        if (visualsResponse.getMetastringParts().getRatingInfo() != null) {
            exploreAPIMetadata.put(RATING, visualsResponse.getMetastringParts().getRatingInfo().getRating().getText());
        }

        //Release Year
        if (visualsResponse.getMetastringParts().getReleaseYearRange() != null) {
            exploreAPIMetadata.put(RELEASE_YEAR_DETAILS,
                    visualsResponse.getMetastringParts().getReleaseYearRange().getStartYear());
        }
        return exploreAPIMetadata;
    }

    private List<String> getGenreMetadataLabels(Visuals visualsResponse) {
        List<String> metadataArray = new ArrayList();
        List<String> genreList = visualsResponse.getMetastringParts().getGenres().getValues();
        //get only first two values of genre
        if (genreList.size() > 2) {
            genreList = genreList.subList(0, 2);
        }
        genreList.forEach(genre -> metadataArray.add(genre));
        return metadataArray;
    }
}
