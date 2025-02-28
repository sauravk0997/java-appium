package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.config.*;
import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.utils.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import static com.disney.qa.common.constant.IConstantHelper.*;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.temporal.*;
import java.util.*;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusDetailsMovieTest extends DisneyBaseTest {
    //Test constants
    private static final String HOCUS_POCUS = "Hocus Pocus";
    private static final String WORLDS_BEST = "World's Best";
    private static final String AUDIO_VIDEO_BADGE = "Audio_Video_Badge";
    private static final String RATING = "Rating";
    private static final String CONTENT_DESCRIPTION = "Content_Description";
    private static final String CONTENT_PROMO_TITLE = "Content_Promo_Title";
    private static final String CONTENT_TITLE = "Content_Title";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";
    private static final String DOWNLOAD_MODAL_STILL_VISIBLE = "Download Modal was still visible";
    private static final String RELEASE_YEAR_DETAILS = "Release_Year";


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68448"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyAddAndRemoveMovieFromWatchlist() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.clickMoviesTab();
        searchPage.selectRandomTitle();
        String contentTitle = detailsPage.getMediaTitle();

        //Add to watchlist
        detailsPage.addToWatchlist();
        Assert.assertTrue(detailsPage.getRemoveFromWatchListButton().isPresent(),
                "remove from watchlist button wasn't displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        Assert.assertTrue(moreMenu.isOpened(), MORE_MENU_NOT_DISPLAYED);

        moreMenu.getDynamicCellByLabel(
                moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(watchlistPage.isWatchlistTitlePresent(contentTitle), "D+ Media title was not " +
                "added to the watchlist");
        //Remove from watchlist
        watchlistPage.tapWatchlistContent(contentTitle);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickRemoveFromWatchlistButton();
        detailsPage.waitForWatchlistButtonToAppear();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        Assert.assertTrue(moreMenu.isOpened(), MORE_MENU_NOT_DISPLAYED);
        moreMenu.getDynamicCellByLabel(
                moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_NOT_DISPLAYED);
        sa.assertTrue(moreMenu.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not properly displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67891"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMoviesDetailsTabMetadata() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //Navigate to All Metadata Movie
        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
        searchPage.getDynamicAccessibilityId(HOCUS_POCUS).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickDetailsTab();
        detailsPage.swipeTillActorsElementPresent();

        sa.assertTrue(detailsPage.isDetailsTabTitlePresent(), "Details Tab title not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present");
        sa.assertTrue(detailsPage.isDurationDisplayed(), "Detail Tab duration not present");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Detail Tab genre is not present");
        sa.assertTrue(detailsPage.isRatingPresent(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Detail Tab formats not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69961"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMovieDetailsUIElements() {
        String contentTitle = DisneyEntityIds.IRONMAN.getTitle();
        String entityID = DisneyEntityIds.IRONMAN.getEntityId();

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getMoviesMetaDataFromAPI(visualsResponse);

        //Navigate to all metadata movie
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(contentTitle);
        searchPage.getDynamicAccessibilityId(contentTitle).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        //Verify main details page UI elements
        sa.assertTrue(detailsPage.getBackButton().isPresent(), "Close button not present");
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not present");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero banner image not present");
        sa.assertEquals(detailsPage.getMediaTitle(), visualsResponse.getTitle(), "Content title mismatch");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Details page logo image not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Details page content description not present");

        //Verify if "Genre" value matches with api, if api has returned any value
        String metadataString = detailsPage.getMetaDataLabel().getText();
        getGenreMetadataLabels(visualsResponse).forEach(value -> sa.assertTrue(metadataString.contains(value),
                String.format("%s value was not present on Metadata label", value)));

        //Verify if "Audio/Video/Format Quality" value matches with api, if api has returned any value
        if (exploreAPIData.containsKey(AUDIO_VIDEO_BADGE)) {
            ((List<String>) exploreAPIData.get(AUDIO_VIDEO_BADGE)).forEach(badge -> {
                if (badge.equalsIgnoreCase(DOLBY_VISION)) {
                    detailsPage.isDolbyVisionPresentOrNot(sa);
                } else {
                    sa.assertTrue(detailsPage.getStaticTextByLabelContains(badge).isPresent(),
                            String.format("Audio video badge %s is not present on details page featured area", badge));
                }
            });
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

        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Details page metadata label not present");
        sa.assertTrue(detailsPage.isPlayButtonDisplayed(), "Details page play button not present");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button not present");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Details page trailer button not displayed");
        sa.assertTrue(detailsPage.isMovieDownloadButtonDisplayed(), "Details page download button not present");

        //Tabs
        sa.assertTrue(detailsPage.getSuggestedTab().isPresent(), "Details tab is not present");
        sa.assertTrue(detailsPage.getExtrasTab().isPresent(), "Extras tab is not present");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Suggested tab is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74597"})
    @Test(description = "Asset Detail Page > User taps Share Button", groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
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
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        String url = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_hocus_pocus_share_link");

        sa.assertTrue(expectedUrl.contains(url), String.format("Share link for movie %s is not the expected", HOCUS_POCUS));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72420"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72544"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyMovieResumeStateBehavior() {
        int UI_LATENCY_IN_SEC = 60;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
        searchPage.getDynamicAccessibilityId(HOCUS_POCUS).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        //Play Trailer
        detailsPage.getTrailerActionButton().click();
        videoPlayer.waitForVideoToStart();
        videoPlayer.verifyVideoPlaying(sa);
        videoPlayer.clickBackButton();

        //Resume state
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(30);
        videoPlayer.clickBackButton();

        //Validate resume state
        Assert.assertTrue(detailsPage.isContinueButtonPresent(),
                "Continue button is not present after exiting video player");
        int detailsPageRemainingTime = detailsPage.getRemainingTimeInSeconds(detailsPage.getRemainingTimeText());
        detailsPage.clickContinueButton().waitForVideoToStart();

        int videoPlayerRemainingTime = videoPlayer.getRemainingHourAndMinInSeconds();
        int playDuration = (detailsPageRemainingTime - videoPlayerRemainingTime);
        ValueRange range = ValueRange.of(0, UI_LATENCY_IN_SEC);
        Assert.assertTrue(range.isValidIntValue(playDuration),"video didn't start from the bookmark");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72543"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyComingSoonMovieBehavior() {
        String httpPrefix = "https://";
        SoftAssert sa = new SoftAssert();
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //TODO: Replace entity-id, deeplink from API when https://jira.disneystreaming.com/browse/QP-3247 is ready
        String entityID = R.TESTDATA.get("disney_prod_movie_moana_2_entity_id");
        String deeplink = R.TESTDATA.get("disney_prod_movie_moana_2_deeplink");
        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
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
        sa.assertTrue(detailsPage.getDetailsTabTitle().contains(contentTitle), DETAILS_PAGE_NOT_DISPLAYED);

        //Subscriber can share link to title over social media
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel(String.format("%s | Disney+", contentTitle)).isPresent(),
                String.format("'%s | Disney+' title was not found on share actions", contentTitle));
        detailsPage.clickOnCopyShareLink();
        detailsPage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.getSearchBar().click();
        String url = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_movie_moana_2_deeplink");
        sa.assertTrue(expectedUrl.contains(url.replace(httpPrefix, "")),
                String.format("Share link for movie %s is not the expected", contentTitle));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69963"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyComingSoonMovieUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //TODO: Replace entity-id, deeplink from API when https://jira.disneystreaming.com/browse/QP-3247 is ready
        String entityID = R.TESTDATA.get("disney_prod_movie_moana_2_entity_id");
        String deeplink = R.TESTDATA.get("disney_prod_movie_moana_2_deeplink");
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getMoviesMetaDataFromAPI(visualsResponse);

        launchDeeplink(deeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

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
    @Test(groups = {TestGroup.PROFILES, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
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
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72423"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMoviesExtrasTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
        searchPage.getDynamicAccessibilityId(HOCUS_POCUS).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab was not found");

        detailsPage.clickExtrasTab();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Extras tab play icon was not found");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "First extras title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "First extras description was not found");
        detailsPage.getPlayIcon().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.clickBackButton();
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67387"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.DOWNLOADS, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadedMoviePlayback() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_content_dumbo_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.getMovieDownloadButton().click();
        String movieTitle = getExploreAPIPageVisuals(R.TESTDATA.get("disney_prod_movie_dumbo_entity_id")).getTitle();
        if (movieTitle == null) {
            throw new SkipException("No movie title was found in Explore API");
        }
        detailsPage.waitForMovieDownloadComplete(THREE_HUNDRED_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);
        detailsPage.getMovieDownloadCompleteButton().click();
        detailsPage.getDownloadModalPlayButton().click();

        Assert.assertTrue(videoPlayer.isOpened(),
                "Video player did not open after choosing a downloaded movie");
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(movieTitle),
                "Video player title does not match with expected title: " + movieTitle);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67389"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.DOWNLOADS, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadModalWhenMovieDownloadIsInProgress() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String movieTitle = "Doctor Strange";
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.getMovieDownloadButton().click();
        Assert.assertTrue(detailsPage.getDownloadStartedButton().isElementPresent(),
                "Download not started, Stop or Pause Download button not displayed");
        detailsPage.getDownloadStartedButton().click();

        sa.assertTrue(detailsPage.getStaticTextByLabel(movieTitle).isElementPresent(),
                "Content Title was not displayed on Download modal");
        sa.assertTrue(detailsPage.isMoviePauseDownloadButtonDisplayed(),
                "Pause Download button not displayed on Download modal");
        sa.assertTrue(detailsPage.isRemoveDownloadButtonDisplayed(),
                "Remove Download button not displayed on Download modal");
        sa.assertTrue(detailsPage.isDownloadInProgressStatusDisplayed(),
                "Download in Progress status not displayed on Download modal");
        sa.assertTrue(detailsPage.isAlertDismissBtnPresent(), "Dismiss button not found on Download modal");

        detailsPage.getPauseDownloadButton().click();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                DOWNLOAD_MODAL_STILL_VISIBLE);
        Assert.assertTrue(detailsPage.getMovieDownloadButton().isElementPresent(),
                "Download was not paused. Download icon was not present");
        detailsPage.getMovieDownloadButton().click();
        Assert.assertTrue(detailsPage.isDownloadPausedInDownloadModal(),
                "Download state did not change to Paused");

        detailsPage.getRemoveDownloadButton().click();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                DOWNLOAD_MODAL_STILL_VISIBLE);
        Assert.assertTrue(detailsPage.getMovieDownloadButton().isElementPresent(),
                "Download was not removed. Download icon was not present");
        Assert.assertFalse(detailsPage.getDownloadStartedButton().isElementPresent(THREE_SEC_TIMEOUT),
                "Download was not removed. Stop/Pause Download icon was still displayed");

        detailsPage.getMovieDownloadButton().click();
        Assert.assertTrue(detailsPage.getDownloadStartedButton().isElementPresent(),
                "Download not started, Stop or Pause Download button not displayed");
        detailsPage.getDownloadStartedButton().click();
        Assert.assertTrue(detailsPage.getViewAlert().isElementPresent(),
                "Download modal was not present");
        detailsPage.clickAlertDismissBtn();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                DOWNLOAD_MODAL_STILL_VISIBLE);
        Assert.assertTrue(detailsPage.getDownloadStartedButton().isElementPresent(),
                "Download is not in progress. Stop/Pause Download icon was not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67395"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.DOWNLOADS, TestGroup.MOVIES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadModalWhenMovieDownloadIsPaused() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        String movieTitle = "Doctor Strange";
        String movieDeeplink = R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink");
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //Open movie detail page, download movie, pause download and re-open download modal
        launchDeeplink(movieDeeplink);
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.getMovieDownloadButton().click();
        Assert.assertTrue(detailsPage.getDownloadStartedButton().isElementPresent(),
                "Download not started, Stop or Pause Download button not displayed");
        detailsPage.getDownloadStartedButton().click();
        Assert.assertTrue(detailsPage.isMoviePauseDownloadButtonDisplayed(),
                "Pause Download button was not displayed on Download modal");
        detailsPage.getPauseDownloadButton().click();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                DOWNLOAD_MODAL_STILL_VISIBLE);
        detailsPage.getMovieDownloadButton().click();

        //Validate download modal elements presence when download is paused
        sa.assertTrue(detailsPage.getStaticTextByLabel(movieTitle).isElementPresent(),
                "Content Title was not displayed on Download modal");
        sa.assertTrue(detailsPage.isDownloadPausedInDownloadModal(),
                "Download Paused status not displayed on Download modal");
        sa.assertTrue(detailsPage.getResumeDownloadButton().isDisplayed(),
                "Resume Download button was not displayed on Download modal");
        sa.assertTrue(detailsPage.isRemoveDownloadButtonDisplayed(),
                "Remove Download button was not displayed on Download modal");
        sa.assertTrue(detailsPage.isAlertDismissBtnPresent(), "Dismiss button not found on Download modal");

        //Validate Dismiss button behavior when download is paused
        detailsPage.clickAlertDismissBtn();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                DOWNLOAD_MODAL_STILL_VISIBLE);
        Assert.assertTrue(detailsPage.getMovieDownloadButton().isElementPresent(),
                "Download is not paused. Download icon was not displayed");
        detailsPage.getMovieDownloadButton().click();
        Assert.assertTrue(detailsPage.isDownloadPausedInDownloadModal(),
                "Download did not remained paused. Download Paused status not displayed on Download modal");

        //Validate Resume Download button does resume the download when the download was paused
        detailsPage.getResumeDownloadButton().click();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                DOWNLOAD_MODAL_STILL_VISIBLE);
        Assert.assertTrue(detailsPage.getDownloadStartedButton().isElementPresent(),
                "Download was not resumed. Stop/Pause Download icon was not present");
        detailsPage.getDownloadStartedButton().click();
        Assert.assertTrue(detailsPage.isDownloadInProgressStatusDisplayed(),
                "Download state did not change to Download in progress");

        //Pause the download again, open modal and validate Remove Download button behavior when download is paused
        detailsPage.getPauseDownloadButton().click();
        detailsPage.getMovieDownloadButton().click();
        detailsPage.getRemoveDownloadButton().click();
        Assert.assertFalse(detailsPage.getViewAlert().isElementPresent(THREE_SEC_TIMEOUT),
                DOWNLOAD_MODAL_STILL_VISIBLE);
        Assert.assertFalse(detailsPage.getStopOrPauseDownloadIcon().isElementPresent(THREE_SEC_TIMEOUT),
                "Download was not removed. Stop/Pause Download icon was displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloadsPage.getEmptyDownloadImage().isPresent(),
                "Downloads Image is not present");
        Assert.assertTrue(downloadsPage.isDownloadsEmptyHeaderPresent(),
                "Downloads empty header is not present");

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
        if (visualsResponse.getMetastringParts().getRatingInfo() != null) {
            exploreAPIMetaData.put(RATING, visualsResponse.getMetastringParts().getRatingInfo().getRating().getText());
        }

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
