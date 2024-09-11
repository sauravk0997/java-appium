package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.search.assets.DisneyMovies;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

public class DisneyPlusDetailsMovieTest extends DisneyBaseTest {
    //Test constants
    private static final String HOCUS_POCUS = "Hocus Pocus";
    private static final String ALL_METADATA_MOVIE = "Turning Red";
    private static final String WORLDS_BEST = "World's Best";
    private static final String AUDIO_VIDEO_BADGE = "Audio_Video_Badge";
    private static final String RATING = "Rating";
    private static final String CONTENT_DESCRIPTION = "Content_Description";
    private static final String CONTENT_PROMO_TITLE = "Content_Promo_Title";
    private static final String CONTENT_TITLE = "Content_Title";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";
    private static final String SEARCH_PAGE_DID_NOT_OPEN = "Search page did not open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68448"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION}, enabled = false)
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
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        List<ExtendedWebElement> watchlist = disneyPlusMoreMenuIOSPageBase.getDisplayedTitles();
        watchlist.get(0).click();
        disneyPlusDetailsIOSPageBase.clickRemoveFromWatchlistButton();
        pause(10); //wait for refresh rate
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not properly displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69961"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieNoExtras() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.searchForMedia(DisneyMovies.HOLIDAY_MAGIC.getName());
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();
        sa.assertFalse(disneyPlusHomeIOSPageBase.getTypeButtonByLabel(
                        getLocalizationUtils()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                        DictionaryKeys.NAV_EXTRAS.getText()))
                .isPresent());
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67891"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
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
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0,
                        detailsPage.getReleaseDate(), 1),
                "Metadata year does not contain details tab year");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74597"})
    @Test(description = "Asset Detail Page > User taps Share Button", groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieDetailsShare() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found");
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel(
                        String.format("%s | Disney+", HOCUS_POCUS)).isPresent(),
                String.format("'%s | Disney+' title was not found on share actions", HOCUS_POCUS));
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Copy").isPresent(),
                "Share action 'Copy' was not found");

        detailsPage.clickOnCopyShareLink();
        detailsPage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        String url = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_hocus_pocus_share_link");

        sa.assertTrue(expectedUrl.contains(url), String.format("Share link for movie %s is not the expected", HOCUS_POCUS));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72420"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE})
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
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        videoPlayer.clickBackButton();
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyThreeIntegerVideoPlaying(sa);
        videoPlayer.scrubToPlaybackPercentage(30);

        //Validate resume UI
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present after exiting video player");
        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button is not present");
        sa.assertTrue(detailsPage.getProgressBar().isPresent(), "Progress bar is not present");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().isPresent(),
                "Continue watching time remaining is not present");

        //Validate detail page UI is same after change to Continue state
        //share, back arrow
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button is not present");
        sa.assertTrue(detailsPage.getBackArrow().isPresent(), "Back arrow button is not present");

        //Validate Dolby Vision present / not present on certain devices
        detailsPage.isDolbyVisionPresentOrNot(sa);

        //Media features
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not found");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Subtitles / CC").isPresent(),
                "`Subtitles / CC` audio quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Audio Description").isPresent(),
                "`Audio Description` audio quality is not found");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero banner image is not present");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Details page logo image is not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Details page content description is not present");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Details page metadata label is not present");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button is not present");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Details page trailer button is not present");
        sa.assertTrue(detailsPage.isMovieDownloadButtonDisplayed(), "Details page download button is not present");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1),
                "Metadata year does not contain details tab year");

        //Tabs
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab is not present");
        sa.assertTrue(detailsPage.getExtrasTab().isPresent(), "Extras tab is not present");
        sa.assertTrue(detailsPage.getSuggestedTab().isPresent(), "Suggested tab is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72543"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
    public void verifyComingSoonMovieBehavior() {
        String httpPrefix = "https://";
        SoftAssert sa = new SoftAssert();
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //TODO: Replace entity-id, deeplink from API when https://jira.disneystreaming.com/browse/QP-3247 is ready
        String entityID = R.TESTDATA.get("disney_prod_movie_deadpool_wolverine_entity_id");
        String deeplink = R.TESTDATA.get("disney_prod_movie_deadpool_wolverine_deeplink");
        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);

        Map<String, Object> exploreAPIMetaData = getMoviesMetaDataFromAPI(visualsResponse);
        sa.assertEquals(detailsPage.getPromoLabelText(), exploreAPIMetaData.get(CONTENT_PROMO_TITLE),
                "Promo title didn't match with api promo title");
        //Subscriber can play trailer (if available)
        String contentTitle = detailsPage.getContentTitle();
        detailsPage.getTrailerActionButton().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart().verifyVideoPlaying(sa);
        videoPlayer.clickBackButton().isOpened();

        //Subscriber can add title to Watchlist
        detailsPage.clickWatchlistButton();
        detailsPage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
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
        sa.assertTrue(detailsPage.getTypeOtherByLabel(String.format("%s | Disney+", contentTitle)).isPresent(),
                String.format("'%s | Disney+' title was not found on share actions", contentTitle));
        detailsPage.clickOnCopyShareLink();
        detailsPage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.getSearchBar().click();
        String url = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_movie_deadpool_wolverine_deeplink");
        sa.assertTrue(expectedUrl.contains(url.replace(httpPrefix, "")),
                String.format("Share link for movie %s is not the expected", contentTitle));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69963"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
    public void verifyComingSoonMovieUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //TODO: Replace entity-id, deeplink from API when https://jira.disneystreaming.com/browse/QP-3247 is ready
        String entityID = R.TESTDATA.get("disney_prod_movie_deadpool_wolverine_entity_id");
        String deeplink = R.TESTDATA.get("disney_prod_movie_deadpool_wolverine_deeplink");
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getMoviesMetaDataFromAPI(visualsResponse);

        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        sa.assertEquals(detailsPage.getPromoLabelText(), exploreAPIData.get(CONTENT_PROMO_TITLE),
                "Promo title didn't match with api promo title");
        sa.assertEquals(detailsPage.getMediaTitle(), exploreAPIData.get(CONTENT_TITLE),
                "Content title didn't match with api content title");
        sa.assertEquals(detailsPage.getContentDescriptionText(), exploreAPIData.get(CONTENT_DESCRIPTION),
                "Description didn't match with api description value");

        //Audio/Video/Format Quality
        ((List<String>) exploreAPIData.get(AUDIO_VIDEO_BADGE)).forEach(badge ->
                sa.assertTrue(detailsPage.getStaticTextByLabelContains(badge).isPresent(),
                        String.format("Audio video badge %s is not present on details page", badge)));

        //Featured Metadata
        String metadataString = detailsPage.getMetaDataLabel().getText();
        getMetaDataLabelValuesFromAPI(visualsResponse).forEach(value -> sa.assertTrue(metadataString.contains(value),
                String.format("%s value was not present on Metadata label", value)));

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75417"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileDetailsPageMovieDownload() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        String THE_TIGGER_MOVIE = "The Tigger Movie";

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
        searchPage.searchForMedia(THE_TIGGER_MOVIE);
        searchPage.getDynamicAccessibilityId(THE_TIGGER_MOVIE).click();
        detailsPage.startDownload();
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.DOWNLOADS));
        Assert.assertTrue(detailsPage.getStaticTextByLabel(THE_TIGGER_MOVIE).isPresent(), "Movie title is not present in downloads");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67781"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileMovieDetailsPage() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang())
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);

        String entityID = R.TESTDATA.get("disney_prod_movie_the_tigger_movie_entity_id");
        String deeplink = R.TESTDATA.get("disney_prod_movie_the_tigger_movie_deeplink");
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getMoviesMetaDataFromAPI(visualsResponse);

        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        //Verify main details page UI elements
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero banner image not present");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Details page logo image not present");
        sa.assertTrue(detailsPage.getBackArrow().isPresent(), "Back button not found on details page");
        sa.assertTrue(detailsPage.isKidThemeBackgroudUIDisplayed(), "UI on detail page is not in kid mode theme");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Details page metadata label not present");
        sa.assertEquals(detailsPage.getMediaTitle(), exploreAPIData.get(CONTENT_TITLE),
                "Content title didn't match with api content title");
        sa.assertEquals(detailsPage.getContentDescriptionText(), exploreAPIData.get(CONTENT_DESCRIPTION),
                "Description didn't match with api description value");

        //Verify if ratings value matches with api, if api has returned any value
        if (exploreAPIData.containsKey(RATING)) {
            sa.assertTrue(detailsPage.getStaticTextByLabelContains(exploreAPIData.get(RATING).toString()).isPresent(),
                    "Rating value is not present on details page featured area");
        }

        //Audio/Video/Format Quality
        ((List<String>) exploreAPIData.get(AUDIO_VIDEO_BADGE)).forEach(badge ->
                sa.assertTrue(detailsPage.getStaticTextByLabelContains(badge).isPresent(),
                        String.format("Audio video badge %s is not present on details page", badge)));

        //Featured Metadata
        String metadataString = detailsPage.getMetaDataLabel().getText();
        getMetaDataLabelValuesFromAPI(visualsResponse).forEach(value -> sa.assertTrue(metadataString.contains(value),
                String.format("%s value was not present on Metadata label", value)));

        //Verify if CTA buttons are present
        sa.assertTrue(detailsPage.isPlayButtonDisplayed(), "Details page play button not present");
        sa.assertTrue(detailsPage.getTrailerActionButton().isPresent(),
                "Trailer button is not present on detail page");
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(),
                "Watchlist button is not present on detail page");
        sa.assertTrue(detailsPage.isMovieDownloadButtonDisplayed(),
                "Details page download button not present");

        //Verify if tabs are present
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(),
                "Details tab is not present on detail page");
        sa.assertTrue(detailsPage.getExtrasTab().isPresent(),
                "Extra tab is not present on detail page");
        sa.assertTrue(detailsPage.getSuggestedTab().isPresent(),
                "Suggested tab is not present on detail page");
        sa.assertAll();
    }

    private Map<String, Object> getMoviesMetaDataFromAPI(Visuals visualsResponse) {
        Map<String, Object> exploreAPIMetaData = new HashMap<>();

        exploreAPIMetaData.put(CONTENT_TITLE, visualsResponse.getTitle());
        exploreAPIMetaData.put(CONTENT_DESCRIPTION, visualsResponse.getDescription().getBrief());
        if (visualsResponse.getPromoLabel() != null) {
            exploreAPIMetaData.put(CONTENT_PROMO_TITLE, visualsResponse.getPromoLabel().getHeader());
        }

        //Audio visual badge
        if (visualsResponse.getMetastringParts().getAudioVisual().getFlags() != null) {
            List<String> audioVideoApiBadge = new ArrayList<>();
            visualsResponse.getMetastringParts().getAudioVisual().getFlags().forEach(flag -> audioVideoApiBadge.add(flag.getTts()));
            exploreAPIMetaData.put(AUDIO_VIDEO_BADGE, audioVideoApiBadge);
        }

        //Rating
        exploreAPIMetaData.put(RATING, visualsResponse.getMetastringParts().getRatingInfo().getRating().getText());

        return exploreAPIMetaData;
    }

    private ArrayList<String> getMetaDataLabelValuesFromAPI(Visuals visualsResponse) {
        ArrayList<String> metadataArray = new ArrayList();
        if (visualsResponse.getMetastringParts().getReleaseYearRange() != null) {
            metadataArray.add(visualsResponse.getMetastringParts().getReleaseYearRange().getStartYear());
        }

        if (visualsResponse.getMetastringParts().getRuntime() != null) {
            metadataArray.add(convertMinutesIntoStringWithHourAndMinutes(
                    (visualsResponse.getMetastringParts().getRuntime().getRuntimeMs() / 1000) / 60));
        }

        if (visualsResponse.getMetastringParts().getGenres() != null) {
            var genreList = visualsResponse.getMetastringParts().getGenres().getValues();
            //get only first two values of genre
            genreList.subList(0, 2).forEach(genre -> metadataArray.add(genre));
        }
        return metadataArray;
    }
}
