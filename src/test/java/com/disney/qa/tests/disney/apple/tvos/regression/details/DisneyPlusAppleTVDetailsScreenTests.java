package com.disney.qa.tests.disney.apple.tvos.regression.details;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.alice.AliceUtilities;
import com.disney.qa.api.client.requests.CreateUnifiedAccountProfileRequest;
import com.disney.qa.api.client.responses.profile.Profile;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.explore.response.Set;
import com.disney.qa.api.explore.response.Visuals;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.*;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.stream.*;

import static com.disney.alice.labels.AliceLabels.DESCRIPTION;
import static com.disney.qa.api.disney.DisneyEntityIds.*;
import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.CollectionConstant.getCollectionName;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;
import static com.disney.qa.tests.disney.apple.ios.regression.details.DisneyPlusDetailsTest.LIVE;
import static com.disney.qa.tests.disney.apple.ios.regression.details.DisneyPlusDetailsTest.UPCOMING;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVDetailsScreenTests extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String HOME_PAGE_ERROR_MESSAGE = "Home page did not open";
    private static final String SEARCH_PAGE_ERROR_MESSAGE = "Search page did not open";
    private static final String DETAILS_PAGE_ERROR_MESSAGE = "Details page did not open";
    private static final String WATCHLIST_SCREEN_ERROR_MESSAGE = "Watchlist page did not open";
    private static final String WATCHLIST_ICON_NOT_PRESENT = "Watchlist plus icon is not displayed";
    private static final String BADGE_LABEL_NOT_PRESENT = "Badge label is not present";
    private static final String TITLE_NOT_PRESENT = "Title is not present";
    private static final String DESCRIPTION_NOT_PRESENT = "Description is not present";
    private static final String WATCHLIST_NOT_PRESENT = "Watchlist button is not present";
    private static final String BACKGROUND_IMAGE_NOT_PRESENT = "Background image is not present";
    private static final String ASSET_NOT_FOUND_IN_WATCHLIST = "The asset was not found in the watchlist";
    private static final String LIVE_MODAL_NOT_OPEN = "Live event modal did not open";
    private static final String SUGGESTED = "SUGGESTED";
    private static boolean eventDescription = false;
    private static boolean networkAttribution = false;


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66656"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, US})
    public void verifyMovieDetailsExtraTab() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1, 1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getExtrasTab(), 6);
        detailsPage.moveDown(1, 1);
        String extraTitle = detailsPage.getExtraEpisodeTitle();
        detailsPage.clickSelect();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(videoPlayer.getTitleLabel().contains(extraTitle),
                "Playback is not initiated for the extra content");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66642"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SMOKE, US})
    public void verifyMovieDetailsPageAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(), getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlock(DisneyEntityIds.END_GAME.getEntityId()));

        ExploreContent movieApiContent = getMovieApi(END_GAME.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String description = movieApiContent.getDescription().getBrief();
        String ratingsValue = movieApiContent.getRating();
        List<String> tabs = Stream.of("SUGGESTED", "EXTRAS", "DETAILS").collect(Collectors.toList());
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        sa.assertTrue(watchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);

        watchListPage.clickSelect();
        sa.assertTrue(detailsPage.isOpened(), "Movies details page did not launch");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Logo isn't present in its expected position");
        sa.assertTrue(detailsPage.isContentSummaryView(), "Content summary view is not displayed.");
        sa.assertTrue(detailsPage.isBriefDescriptionPresent(description), "description is not present");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(detailsPage.isPlayButtonDisplayed(), "Play button isn't present in its expected position");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Trailer button isn't present in its expected position");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button isn't present in its expected position");

        AliceUtilities aliceUtilities = new AliceUtilities(getDriver());
        aliceUtilities.isUltronTextPresent("HD 5.1 CC", DESCRIPTION.getText());
        aliceUtilities.isUltronTextPresent(ratingsValue, DESCRIPTION.getText());
        tabs.forEach(item -> sa.assertTrue(detailsPage.getDynamicRowButtonLabel(item, 1).isPresent(SHORT_TIMEOUT),
                "The following tab isn't present " + item));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64740"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void addAndRemoveAssetFromWatchlist() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.clickWatchlistButton();

        disneyPlusAppleTVDetailsPage.clickMenuTimes(2, 1);
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);
        sa.assertTrue(disneyPlusAppleTVWatchListPage.getTypeCellLabelContains(END_GAME.getTitle()).isElementPresent(), "The following asset was not found in watchlist " + END_GAME.getTitle());

        disneyPlusAppleTVWatchListPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.clickWatchlistButton();

        sa.assertEquals(disneyPlusAppleTVDetailsPage.getWatchlistButtonText(),
                "Add the current title to your Watchlist", "Add To Watchlist Text is not displayed");

        disneyPlusAppleTVDetailsPage.clickMenuTimes(1, 1);
        collapseGlobalNav();

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);

        sa.assertFalse(disneyPlusAppleTVWatchListPage.getDynamicCellByLabel(END_GAME.getTitle()).isElementPresent(), "The following asset was  found in watchlist " + END_GAME.getTitle());

        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66646"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, US})
    public void verifyMovieDetailsVODBookmarkRefresh() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        String continueWatchingCollection = CollectionConstant
                .getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING);
        int maxCount = 20;
        String continueButton = "CONTINUE";

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        //Populate continue watching collection
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_moana_2_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();

        // Forward video and get remaining time
        commonPage.clickRight(6, 1, 1);
        videoPlayer.waitForVideoToStart();
        commonPage.clickDown(1);
        commonPage.clickSelect();
        String remainingTime = videoPlayer.getRemainingTimeInDetailsFormatString();
        LOGGER.info("remainingTime {}", remainingTime);
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));

        //Navigate to continue watching collection
        commonPage.moveDownUntilCollectionContentIsFocused(continueWatchingCollection, maxCount);
        commonPage.clickSelect();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);
        Assert.assertTrue(detailsPage.getTypeButtonContainsLabel(continueButton).isPresent(),
                "Continue button was not present on details page");
        Assert.assertTrue(detailsPage.getContinueWatchingTimeRemaining().getText().contains(remainingTime),
                "Correct remaining time is not reflecting in progress bar on details page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90976"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SMOKE, US})
    public void trailerCompletionTakesUserToDetailsPage() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage disneyPlusAppleTVVideoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.getTrailerButton().click();
        sa.assertTrue(disneyPlusAppleTVVideoPlayerPage.isOpened(), "Video player page did not launch");

        disneyPlusAppleTVVideoPlayerPage.waitUntilDetailsPageIsLoadedFromTrailer(200, 20);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-101242"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, US})
    public void verifyMovieDetailsAppearanceMetadata() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        String continueWatchingCollection = CollectionConstant
                .getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING);
        SoftAssert sa = new SoftAssert();
        int maxCount = 20;
        String continueButton = "CONTINUE";
        String restartButton = "RESTART";
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        ExploreContent movieApiContent = getMovieApi(DisneyEntityIds.PREY.getEntityId(),
                DisneyPlusBrandIOSPageBase.Brand.HULU);
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        //Populate continue watching collection
        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_movie_prey_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        commonPage.clickRight(4, 1, 1);
        videoPlayer.waitForVideoToStart();
        commonPage.clickDown(1);
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));

        //Navigate to continue watching collection
        homePage.waitForHomePageToOpen();
        commonPage.moveDownUntilCollectionContentIsFocused(continueWatchingCollection, maxCount);
        commonPage.clickSelect();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);

        sa.assertTrue(detailsPage.getTypeButtonByLabel(restartButton).isPresent(),
                "Restart button is not displayed on details page");
        sa.assertTrue(detailsPage.getTypeButtonContainsLabel(continueButton).isPresent(),
                "Continue button was not present on details page");
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(), WATCHLIST_NOT_PRESENT);
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "metadata label is not displayed");
        sa.assertTrue(detailsPage.getProgressContainer().isPresent(),
                "Progress container view is not present for the movies");
        if (movieApiContent != null) {
            sa.assertEquals(detailsPage.getContentDescriptionText(), movieApiContent.getDescription().getBrief(),
                    "Description didn't match with api description value");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112611"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, US})
    public void verifyNetworkAttributionWithBundleUserAccount() {
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        SoftAssert sa = new SoftAssert();
        verifyServiceAttribution(ONLY_MURDERS_IN_THE_BUILDING, sa);
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
        verifyServiceAttribution(BILL_BURR, sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66644"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, US})
    public void verifyUpAndDownArrowFocusesPlayButton() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.typeInSearchField(END_GAME.getTitle());
        searchPage.clickSearchResult(END_GAME.getTitle());

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getPlayButton()),
                "Play button was not focused when details page opened");
        detailsPage.moveDown(1,1);
        detailsPage.moveUp(1,1);
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getPlayButton()),
                "Play button was not focused after navigating up from details tab");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121917"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void verifyESPNLeaguePage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVCollectionPage collectionPage = new DisneyPlusAppleTVCollectionPage(getDriver());
        String leagueName = "National Hockey League";
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_nhl_league_deeplink"));
        Assert.assertTrue(collectionPage.isOpened(leagueName), "Expected League page did not open");
        Assert.assertTrue(brandPage.getBrandLandingView().isPresent(), "A logo is not present on the league page");
        Assert.assertTrue(brandPage.getBrandFeaturedImage().isPresent(), "Artwork background is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102800"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void verifyVODReplayAppearance() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusEspnIOSPageBase espnPage = new DisneyPlusEspnIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        String rugby = "Rugby";
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilDisneyOriginalBrandIsFocused(20);
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN))
                        .isPresent(), ESPN_BRAND_TILE_NOT_PRESENT);
        homePage.moveDownFromHeroTile();
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN));
        Assert.assertTrue(brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)),
                ESPN_PAGE_DID_NOT_OPEN);

        // Navigate to Sports collection
        homePage.moveDownUntilCollectionContentIsFocused(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.ESPN_SPORTS), 10);
        homePage.moveRightUntilElementIsFocused(detailsPage.getTypeCellLabelContains(rugby), 30);
        detailsPage.getTypeCellLabelContains(rugby).click();
        Assert.assertTrue(espnPage.isPageTitlePresent(rugby),
                SPORT_PAGE_DID_NOT_OPEN);

        // Navigate to a Replay and validate the page
        CollectionConstant.Collection replaysCollection = CollectionConstant.Collection.SPORT_REPLAYS;
        homePage.moveDownUntilCollectionContentIsFocused(CollectionConstant.getCollectionName(replaysCollection), 10);
        String replayTitle = detailsPage.getAllCollectionCells(replaysCollection).get(0).getText();
        if (replayTitle == null) {
            throw new IndexOutOfBoundsException("No replay events found");
        }
        detailsPage.getTypeCellLabelContains(replayTitle).click();
        detailsPage.waitForDetailsPageToOpen();
        // Validate logo and play button
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        // Validate other UI elements
        sa.assertTrue(detailsPage.getMetaDataLabel().isPresent(), "Metadata text is not present");
        sa.assertTrue(detailsPage.getAiringBadgeLabel().isPresent(), BADGE_LABEL_NOT_PRESENT);
        sa.assertTrue(detailsPage.getDetailsTitleLabel().isPresent(), TITLE_NOT_PRESENT);
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), DESCRIPTION_NOT_PRESENT);
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(), WATCHLIST_NOT_PRESENT);
        sa.assertTrue(detailsPage.getBackgroundImage().isPresent(), BACKGROUND_IMAGE_NOT_PRESENT);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102798"})
    @Test(groups = {TestGroup.VIDEO_PLAYER,  US})
    public void verifyLiveEventAppearance() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        String errorMessage = "Skipping test, no live events are available";
        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTile();
        String liveAndUpcomingEventsCollection =
                getCollectionName(CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING);
                // Navigate to a live event
                Set espnLiveEvent = getExploreAPISet(liveAndUpcomingEventsCollection, 5);
        if (espnLiveEvent == null) {
            throw new SkipException(errorMessage);
        }
        try {
            String titleEvent = espnLiveEvent.getItems().get(0).getVisuals().getTitle();
            LOGGER.info("Event title: {}", titleEvent);
            homePage.moveDownUntilCollectionContentIsFocused(liveAndUpcomingEventsCollection, 10);
            // Verify airing badge to validate if there is a live event occurring
            String airingBadge = collectionPage.getAiringBadgeOfFirstCellElementFromCollection(
                    liveAndUpcomingEventsCollection).getText();
            LOGGER.info("Airing badge: {}", airingBadge);
            if (airingBadge.equals(UPCOMING)) {
                throw new SkipException(errorMessage);
            }
            // Open live event
            detailsPage.getTypeCellLabelContains(titleEvent).click();
        } catch(SkipException e) {
            throw new SkipException(e.getMessage());
        } catch(Exception e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertTrue(liveEventModal.isOpened(), LIVE_MODAL_NOT_OPEN);
        liveEventModal.getDetailsButton().click();
        // Validate logo and play button
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        // Validate other UI elements
        sa.assertTrue(detailsPage.getAiringBadgeLabel().isPresent(), BADGE_LABEL_NOT_PRESENT);
        sa.assertTrue(detailsPage.getDetailsTitleLabel().isPresent(), TITLE_NOT_PRESENT);
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), DESCRIPTION_NOT_PRESENT);
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(), WATCHLIST_NOT_PRESENT);
        sa.assertTrue(detailsPage.getBackgroundImage().isPresent(), BACKGROUND_IMAGE_NOT_PRESENT);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102799"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyUpcomingEventAppearance() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();

        // Navigate to the first event from Live and Upcoming shelf
        Set espnLiveEvent =
                getExploreAPISet(getCollectionName(CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING), 10);
        if (espnLiveEvent == null) {
            throw new SkipException("Skipping test, no upcoming events are available");
        }
        try {
            String firstEvent = espnLiveEvent.getItems().get(0).getVisuals().getTitle();
            LOGGER.info("Event title: {}", firstEvent);
            homePage.moveDownUntilElementIsFocused(detailsPage.getTypeCellLabelContains(firstEvent), 10);
            // Explore the espnLiveEvent Set to find an upcoming event and open it
            String upcomingTitle = navigateToUpcomingEvent(espnLiveEvent);
            detailsPage.getTypeCellLabelContains(upcomingTitle).click();
        } catch (Exception e) {
            Assert.fail("No events are available" + e.getMessage());
        }

        // Validate details page, logo and play button
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        // Validate other UI elements
        sa.assertTrue(detailsPage.isLogoPresent(), "Logo image is not present");
        sa.assertTrue(detailsPage.getAiringBadgeLabel().isPresent(), BADGE_LABEL_NOT_PRESENT);
        sa.assertTrue(detailsPage.getDetailsTitleLabel().isPresent(), TITLE_NOT_PRESENT);
        if (eventDescription && networkAttribution) {
            sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), DESCRIPTION_NOT_PRESENT);
            sa.assertTrue(detailsPage.getStaticTextByLabelContains(ESPN_SUBSCRIPTION_MESSAGE).isPresent(),
                    ENTITLEMENT_ATTRIBUTION_IS_NOT_PRESENT);
        }
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(), WATCHLIST_NOT_PRESENT);
        sa.assertTrue(detailsPage.getBackgroundImage().isPresent(), BACKGROUND_IMAGE_NOT_PRESENT);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67716"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SMOKE, US})
    public void verifyNavigationFromWatchlistToDetailsPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(), getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlock(DisneyEntityIds.END_GAME.getEntityId()));
        ExploreContent movieApiContent = getMovieApi(END_GAME.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String description = movieApiContent.getDescription().getBrief();

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);

        watchListPage.clickSelect();
        Assert.assertTrue(detailsPage.isOpened(), "Movies details page did not launch");
        Assert.assertTrue(detailsPage.isBriefDescriptionPresent(description), "description is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102803"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyUpcomingEventWatchlist() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        SoftAssert sa = new SoftAssert();
        String upcomingTitle = "";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        // Navigate to the first event from Live and Upcoming shelf
        Set espnLiveEvent =
                getExploreAPISet(getCollectionName(CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING), 10);
        if (espnLiveEvent == null) {
            throw new SkipException("Skipping test, no upcoming events are available");
        }
        try {
            String firstEvent = espnLiveEvent.getItems().get(0).getVisuals().getTitle();
            LOGGER.info("Event title: {}", firstEvent);
            homePage.moveDownUntilElementIsFocused(detailsPage.getTypeCellLabelContains(firstEvent),10);
            // Explore the espnLiveEvent Set to find an upcoming event and open it
            upcomingTitle = navigateToUpcomingEvent(espnLiveEvent);
            detailsPage.getTypeCellLabelContains(upcomingTitle).click();
        } catch(Exception e) {
            Assert.fail("No events are available" + e.getMessage());
        }

        // Validate details page and add item to the watchlist
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(), WATCHLIST_NOT_PRESENT);
        sa.assertTrue(detailsPage.getAddToWatchlistText().isElementPresent(),
                "Item is not available to be added to the watchlist");
        detailsPage.getWatchlistButton().click();
        sa.assertTrue(detailsPage.getRemoveFromWatchListButton().isElementPresent(),
                "Item was not added to the watchlist");

        //Navigate to watchlist
        detailsPage.clickMenuTimes(1,1);
        homePage.waitForPresenceOfAnElement(homePage.getGlobalNav());
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);
        detailsPage.waitForPresenceOfAnElement(detailsPage.getTypeCellLabelContains(upcomingTitle));
        sa.assertTrue(detailsPage.getTypeCellLabelContains(upcomingTitle).isPresent(),
                ASSET_NOT_FOUND_IN_WATCHLIST);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68876"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SMOKE, US})
    public void verifyHomeTitleSelectionRedirectsToDetailsPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModalPage = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        ExtendedWebElement liveCell = homePage.getTypeCellLabelContains("live");

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        homePage.moveDownUntilCollectionContentIsFocused(
                getCollectionName(CollectionConstant.Collection.STREAMS_NON_STOP_PLAYLISTS), 10);
        String[] firstNewlyAddedLongTitle = homePage.getFirstCellTitleFromContainer(
                CollectionConstant.Collection.STREAMS_NON_STOP_PLAYLISTS).split(",");
        String firstNewlyAddedTitleName = "";
        try {
            if (liveCell != null && homePage.isFocused(liveCell)) {
                    firstNewlyAddedTitleName = firstNewlyAddedLongTitle[1].trim();
            } else {
                firstNewlyAddedTitleName = firstNewlyAddedLongTitle[0].trim();
            }
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }

        if (homePage.isFocused(liveCell)) {
            homePage.clickSelect();
            liveEventModalPage.waitForPresenceOfAnElement(liveEventModalPage.getWatchLiveButton());
            liveEventModalPage.moveDown(1, 1);
            Assert.assertTrue(liveEventModalPage.isFocused(liveEventModalPage.getDetailsButton()),
                    "Modal details button is not focused");
        }
        homePage.clickSelect();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1,1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getDetailsTab(), 6);
        Assert.assertEquals(detailsPage.getDetailsTabTitle(), firstNewlyAddedTitleName,
                "Current details page title doesn't match API fetched title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102804"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyLiveEventWatchlist() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());

        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();

        // Navigate to the first event from Live and Upcoming shelf
        String titleEvent = navigateToLiveEvent();

        Assert.assertTrue(liveEventModal.isOpened(), LIVE_MODAL_NOT_OPEN);
        liveEventModal.getDetailsButton().click();
        // Validate details page and add item to the watchlist
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getWatchlistButton().isPresent(), WATCHLIST_NOT_PRESENT);
        sa.assertTrue(detailsPage.getAddToWatchlistText().isElementPresent(),
                "Item is not available to be added to the watchlist");
        detailsPage.getWatchlistButton().click();
        sa.assertTrue(detailsPage.getRemoveFromWatchListButton().isElementPresent(),
                "Item was not added to the watchlist");

        //Navigate to watchlist
        detailsPage.clickMenuTimes(1, 1);

        homePage.waitForPresenceOfAnElement(homePage.getGlobalNav());
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);
        detailsPage.waitForPresenceOfAnElement(detailsPage.getTypeCellLabelContains(titleEvent));
        sa.assertTrue(detailsPage.getTypeCellLabelContains(titleEvent).isPresent(),
                ASSET_NOT_FOUND_IN_WATCHLIST);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102805"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyWatchLiveEvent() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();

        // Navigate to the first event from Live and Upcoming shelf
        String titleEvent = navigateToLiveEvent();

        Assert.assertTrue(liveEventModal.isOpened(), LIVE_MODAL_NOT_OPEN);
        liveEventModal.getDetailsButton().click();
        // Validate details page and add item to the watchlist
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickWatchButton();
        if (liveEventModal.isOpened()) {
            liveEventModal.clickWatchLiveButton();
        }
        videoPlayerPage.waitForPresenceOfAnElement(videoPlayerPage.getPlayerView());
        Assert.assertTrue(videoPlayerPage.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(titleEvent.contains(videoPlayerPage.getTitleLabel()),
                "Video title does not match with the expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66648"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, US})
    public void verifyMoviesDetailsTabMetadata() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1, 1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getDetailsTab(), 6);
        sa.assertTrue(detailsPage.isDetailsTabTitlePresent(), "Details Tab title not present");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present");
        sa.assertTrue(detailsPage.isDurationDisplayed(), "Detail Tab duration not present");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Detail Tab genre is not present");
        sa.assertTrue(detailsPage.isRatingPresent(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Detail Tab formats not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertEquals(detailsPage.getQuantityOfActors(), 6, "Expected quantity of actors is incorrect");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-114007"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void verifyHuluStereotypeAdvisoryDetailsTab() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        ExploreContent seriesApiContent =
                getSeriesApi(R.TESTDATA.get("disney_prod_lion_king_timon_and_pumbaa_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);

        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_content_timon_and_pumbaa_deeplink"));
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.moveDown(1, 1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getDetailsTab(), 6);
        Assert.assertTrue(detailsPage.getStaticTextByLabelContains(seriesApiContent.getTitle()).isPresent(),
                "Expected series title is not present");
        Assert.assertTrue(detailsPage.getContentAdvisoryText().isPresent(),
                "Content Advisory section is not present");
        Assert.assertTrue(detailsPage.getContentAdvisoryText().getText().contains(
                detailsPage.retrieveContentAdvisory(seriesApiContent)),
                "Content Advisory text is not present as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64730"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.MOVIES, US})
    public void verifyMovieDetailsPageSuggestedTab() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1, 1);
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getSuggestedTab()), "Suggested tab was not focussed");
        detailsPage.moveDown(1, 1);

        ArrayList<Container> movieDetailsPageContainers = getDisneyAPIPage(THE_AVENGERS.getEntityId(), false);
        List<Item> movieFirstThreeSuggestedContainers = movieDetailsPageContainers.stream()
                .filter(container -> container.getVisuals().getName().equals(SUGGESTED))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("suggested container not present in API response"))
                .getItems().subList(0, 3);

        movieFirstThreeSuggestedContainers.subList(0, 2).forEach(i -> {
            Assert.assertTrue(detailsPage.isFocused(detailsPage.getTypeCellLabelContains(i.getVisuals().getTitle())),
                    "Suggested title was not focussed");
            detailsPage.moveRight(1, 1);
        });
        detailsPage.clickSelect();
        detailsPage.waitForDetailsPageToOpen();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.moveDown(1, 1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getDetailsTab(), 6);
        Assert.assertEquals(detailsPage.getDetailsTabTitle(),
                movieFirstThreeSuggestedContainers.get(2).getVisuals().getTitle(),
                "Current details page title doesn't match API fetched title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64736"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.WATCHLIST, TestGroup.MOVIES, US})
    public void verifyMovieDetailsPageWatchListPlusIcon() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), WATCHLIST_NOT_PRESENT);
        Assert.assertTrue(detailsPage.getAddToWatchlistText().isPresent(),
                WATCHLIST_ICON_NOT_PRESENT);
        detailsPage.getWatchlistButton().click();
        Assert.assertTrue(detailsPage.getRemoveFromWatchListButton().isPresent(),
                "Checkmark icon - to remove the content from the watchlist is not displayed");
        // Click again and verify plus icon
        detailsPage.getWatchlistButton().click();
        Assert.assertTrue(detailsPage.getAddToWatchlistText().isPresent(),
                WATCHLIST_ICON_NOT_PRESENT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121956"})
    @Test(groups = {TestGroup.ESPN, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNUnavailableDetailsPagePCONError() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        Profile secondaryProfile = getUnifiedAccount().getProfile(SECONDARY_PROFILE);
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                secondaryProfile.getProfileId(),
                secondaryProfile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                secondaryProfile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(1));

        logIn(getUnifiedAccount(), SECONDARY_PROFILE);
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_nhl_replay_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getEspnPlusGenericErrorText().isPresent(),
                "Inline generic error message is not present");
        Assert.assertFalse(detailsPage.getDetailsTab().isPresent(FIVE_SEC_TIMEOUT),
                "Details tab is present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-113262"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void verifyTrailerPlaybackDisregardsBookmark() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        // Play movie trailer, fast-forward 20 seconds, pause it before playback finishes and close video player
        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_trailer_playback_deeplink"));
        videoPlayer.waitForVideoToStart(TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT);
        commonPage.clickRight(2, 2, 1);
        videoPlayer.clickPlay();
        videoPlayer.clickMenuTimes(1, 1);

        // Reload movie details page and validate trailer has a bookmark under the Extras tab view
        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1, 1);
        Assert.assertTrue(detailsPage.isExtrasTabPresent(), EXTRAS_TAB_NOT_DISPLAYED);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getExtrasTab(), 6);
        detailsPage.moveDown(1, 1);
        Assert.assertTrue(detailsPage.getContentImageViewProgressBar().isElementPresent(),
                "Movie trailer image preview doesn't has a progress bar present");

        // Play trailer, pause it and validate current elapsed time is in the initial seconds of playback
        detailsPage.clickSelect();
        videoPlayer.waitForVideoToStart(TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT);
        videoPlayer.clickPlay();
        int elapsedPlaybackTime = videoPlayer.getCurrentTime();
        ValueRange playbackStartRange = ValueRange.of(0, 15);
        Assert.assertTrue(playbackStartRange.isValidIntValue(elapsedPlaybackTime),
                String.format("Current elapsed time (%d seconds) is not between the expected range (%d-%d seconds)" +
                                "of the beginning of the playback", elapsedPlaybackTime,
                        playbackStartRange.getMinimum(), playbackStartRange.getMaximum()));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121954"})
    @Test(groups = {TestGroup.ESPN, TestGroup.DETAILS_PAGE, JP_ENG})
    public void verifyESPNUnavailableDetailsPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_nfl_turning_point_deeplink"));

        Assert.assertTrue(homePage.isViewAlertPresent(), "Alert was not present");
        Assert.assertTrue(homePage.getContentUnavailableErrorMessageElement().isElementPresent(),
                "Content Unavailable error message was not present");
        Assert.assertTrue(homePage.getOkButton().isElementPresent(),
                "OK button text was not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121918"})
    @Test(groups = {TestGroup.ESPN, TestGroup.DETAILS_PAGE, US})
    public void verifyESPNSportPage() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusEspnIOSPageBase espnPage = new DisneyPlusEspnIOSPageBase(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilDisneyOriginalBrandIsFocused(20);
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN))
                .isPresent(), "ESPN brand tile was not present on home page screen");
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN));

        Assert.assertTrue(brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)),
                ESPN_PAGE_DID_NOT_OPEN);

        // Navigate to Sports collection and select the first Sport title
        homePage.moveDownUntilCollectionContentIsFocused(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.ESPN_SPORTS), 15);

        String sportTitle = getContainerTitlesFromApi(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.ESPN_SPORTS), 5).get(0);
        LOGGER.info("Navigating to sport {}", sportTitle);
        if(!sportTitle.isEmpty()) {
            homePage.moveRightUntilElementIsFocused(detailsPage.getTypeCellLabelContains(sportTitle), 20);
        } else {
            throw new SkipException("There are no sports available");
        }

        detailsPage.getTypeCellLabelContains(sportTitle).click();
        Assert.assertTrue(espnPage.isPageTitlePresent(sportTitle),
                SPORT_PAGE_DID_NOT_OPEN);
        Assert.assertFalse(detailsPage.getBrandLandingView().isElementPresent(THREE_SEC_TIMEOUT),
                "A logo image is present in sports screen");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121702"})
    @Test(groups = {TestGroup.ESPN, TestGroup.UPSELL, US})
    public void verifyUpsellScreenForLiveUpcomingEvent() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTile();

        String liveAndUpcomingEventsCollection = getCollectionName(CollectionConstant.Collection.ESPN_EXPLORE_MORE);
        // Navigate to a live or upcoming event
        Set espnEvent = getExploreAPISet(liveAndUpcomingEventsCollection, 5);
        if (espnEvent == null) {
            throw new SkipException("Skipping test, no live events are available");
        }
        try {
            String titleEvent = espnEvent.getItems().get(0).getVisuals().getTitle();
            LOGGER.info("Event title: {}", titleEvent);
            homePage.moveDownUntilCollectionContentIsFocused(liveAndUpcomingEventsCollection, 20);
            homePage.getTypeCellLabelContains(titleEvent).click();
        } catch (Exception e) {
            Assert.fail("No events are available " + e.getMessage());
        }
        if (liveEventModal.isOpened()) {
            liveEventModal.getDetailsButton().click();
        }

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        detailsPage.clickSelect();
        Assert.assertTrue(detailsPage.isOnlyAvailableWithESPNHeaderPresent(),
                "Upsell roadblock screen header is not present");
        Assert.assertTrue(detailsPage.isIneligibleScreenBodyPresent(),
                "Upsell roadblock screen body is not present");
        Assert.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(),
                "Upsell roadblock screen cta is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121520"})
    @Test(groups = {TestGroup.ESPN, TestGroup.UPSELL, US})
    public void verifyUpsellDetailsPageForLiveUpcomingEvent() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        SoftAssert sa = new SoftAssert();
        String titleEvent = null;
        String titleDescription = null;
        String airingBadge = null;
        List<String> audioVideoApiBadge = new ArrayList<>();

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTile();

        String liveAndUpcomingEventsCollection = getCollectionName(CollectionConstant.Collection.ESPN_EXPLORE_MORE);
        // Navigate to a live or upcoming event
        Set espnEvent = getExploreAPISet(liveAndUpcomingEventsCollection, 5);
        if (espnEvent == null) {
            throw new SkipException("Skipping test, no live events are available");
        }
        try {
            Visuals visualsResponse = espnEvent.getItems().get(0).getVisuals();
            titleEvent = visualsResponse.getTitle();
            titleDescription = visualsResponse.getDescription().getBrief();

            if (visualsResponse.getMetastringParts().getAudioVisual() != null) {
                visualsResponse.getMetastringParts().getAudioVisual().getFlags()
                        .forEach(flag -> audioVideoApiBadge.add(flag.getTts()));
            }

            LOGGER.info("Event title: {}", titleEvent);
            homePage.moveDownUntilCollectionContentIsFocused(liveAndUpcomingEventsCollection, 20);
            airingBadge = collectionPage.getAiringBadgeOfFirstCellElementFromCollection(
                    liveAndUpcomingEventsCollection).getText();
            homePage.getTypeCellLabelContains(titleEvent).click();
        } catch (Exception e) {
            Assert.fail("No events are available " + e.getMessage());
        }

        if (liveEventModal.isOpened()) {
            liveEventModal.getDetailsButton().click();
        }

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        sa.assertFalse(detailsPage.getPlayButton().isPresent(THREE_SEC_TIMEOUT), "Play button is displayed");
        sa.assertFalse(detailsPage.isWatchButtonPresent(), "Watch button is displayed");
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(THREE_SEC_TIMEOUT), "Watchlist button is displayed");

        sa.assertTrue(detailsPage.getStaticTextByLabel(titleEvent).isPresent(), "Content title is not displayed");
        sa.assertTrue(detailsPage.getStaticTextByLabel(titleDescription).isPresent(),
                "Content title description is not displayed");
        if (audioVideoApiBadge.size() > 0) {
            (audioVideoApiBadge).forEach(badge ->
                    sa.assertTrue(detailsPage.getStaticTextByLabelContains(badge).isPresent(),
                            String.format("Audio video badge %s is not present on details page featured area", badge)));
        }
        if (airingBadge.equals(LIVE)) {
            sa.assertTrue(detailsPage.isProgressBarPresent(), PROGRESS_BAR_NOT_DISPLAYED);
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121165"})
    @Test(groups = {TestGroup.ESPN, TestGroup.DETAILS_PAGE, US})
    public void verifyESPNEntitlementAttributionSupport() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        // Open an entitlement-gated ESPN+ content
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_documentary_american_son_deeplink"));

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getStaticTextByLabelContains(ESPN_SUBSCRIPTION_MESSAGE).isPresent(),
                ENTITLEMENT_ATTRIBUTION_IS_NOT_PRESENT);

        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getStaticTextByLabelContains(ESPN_SUBSCRIPTION_MESSAGE).isPresent(),
                ENTITLEMENT_ATTRIBUTION_IS_NOT_PRESENT);
    }

    private String navigateToLiveEvent() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        String errorMessage = "No live events found";
        String titleEvent = "";
        Set espnEvents =
                getExploreAPISet(getCollectionName(CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING), 5);
        if (espnEvents == null) {
            throw new SkipException(errorMessage);
        }
        try {
            titleEvent = espnEvents.getItems().get(0).getActions().stream()
                    .filter(item -> item.getActions().get(0).getContentType().equals("live"))
                    .findFirst()
                    .orElseThrow(() -> new SkipException(errorMessage))
                    .getVisuals().getTitle();
            LOGGER.info("Title event: {}", titleEvent);
            homePage.moveDownUntilElementIsFocused(detailsPage.getTypeCellLabelContains(titleEvent), 10);
            // Open live event
            detailsPage.getTypeCellLabelContains(titleEvent).click();
        } catch (Exception e) {
            throw new SkipException(errorMessage + e.getMessage());
        }
        return titleEvent;
    }

    private String navigateToUpcomingEvent(Set event) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        Item upcomingEvent = new Item();
        String eventTitle = "";
        for (int i = 0; i < event.getItems().size(); i++) {
            if (!event.getItems().get(i).getVisuals().getPrompt().contains("Started")) {
                upcomingEvent = event.getItems().get(i);
                break;
            }
        }
        eventTitle = upcomingEvent.getVisuals().getTitle();
        if (Stream.of(
                upcomingEvent.getVisuals().getDescription().getMedium(),
                upcomingEvent.getVisuals().getNetworkAttribution()).noneMatch(Objects::isNull)) {
            eventDescription = true;
            networkAttribution = true;
        }

        LOGGER.info("Upcoming event {}", eventTitle);
        homePage.moveRightUntilElementIsFocused(homePage.getTypeCellLabelContains(eventTitle), 15);
        return eventTitle;
    }

    private void verifyServiceAttribution(String content, SoftAssert sa) {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());

        Assert.assertTrue(home.isOpened(), HOME_PAGE_ERROR_MESSAGE);
        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.waitForPresenceOfAnElement(searchPage.getSearchField());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.clearSearchBar();
        searchPage.typeInSearchField(content);
        searchPage.clickSearchResult(content);
        sa.assertTrue(detailsPage.getServiceAttribution().isPresent(),
                "Service attribution was not found on Hulu series detail page");
    }
}
