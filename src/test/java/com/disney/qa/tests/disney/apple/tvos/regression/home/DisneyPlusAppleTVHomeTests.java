package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.explore.response.Visuals;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCollectionIOSPageBase;
import com.disney.qa.common.constant.DisneyUnifiedOfferPlan;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;
import static com.disney.qa.common.DisneyAbstractPage.FIFTEEN_SEC_TIMEOUT;
import static com.disney.qa.common.constant.CollectionConstant.Collection.STREAMS_NON_STOP_PLAYLISTS;
import static com.disney.qa.common.constant.CollectionConstant.getCollectionName;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BASIC_MONTHLY;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {

    private static final String WATCH_LIVE_BUTTON_NOT_DISPLAYED = "Watch Live CTA is not present";
    private static final String DETAILS_BUTTON_NOT_DISPLAYED = "Details CTA is not present";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121758"})
    @Test(groups = {TestGroup.HOME, TestGroup.ESPN, US})
    public void verifyESPNTileUS() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilDisneyOriginalBrandIsFocused(20);
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)).isPresent(),
                ESPN_BRAND_TILE_NOT_PRESENT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121942"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyESPNBrandTiles() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilDisneyOriginalBrandIsFocused(20);
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)).isPresent(),
                ESPN_BRAND_TILE_NOT_PRESENT);

        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN));
        Assert.assertTrue(
                brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)),
                ESPN_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121503"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, US})
    public void verifyStandaloneESPNAndHuluBrandTiles() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilDisneyOriginalBrandIsFocused(20);
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU)).isPresent(),
                "Hulu brand tile was not present on home page screen");
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)).isPresent(),
                ESPN_BRAND_TILE_NOT_PRESENT);

        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU));
        Assert.assertTrue(
                brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU)),
                "Hulu Hub page did not open");
        brandPage.clickBack();

        homePage.waitForPresenceOfAnElement(
                homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)));
        homePage.clickUp();
        homePage.moveDownFromHeroTile();
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN));
        Assert.assertTrue(
                brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)),
                ESPN_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121502"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, US})
    public void verifyHuluUpsellStandaloneUserInEligible() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilDisneyOriginalBrandIsFocused(20);
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU));

        //Validate in-eligible for upsell user still has some content to watch
        String titleAvailableToPlay = "Hulu Original Series, Select for details on this title.";
        String huluSubscriptionTitle = "Hulu Original Series,  Available with Hulu Subscription,";
        int swipeCount = FIFTEEN_SEC_TIMEOUT;
        homePage.moveDownUntilCollectionContentIsFocused(
                getCollectionName(CollectionConstant.Collection.ENJOY_THESE_SERIES_FROM_HULU), swipeCount);
        homePage.moveRightUntilElementIsFocused(brandPage.getTypeCellLabelContains(titleAvailableToPlay), swipeCount);
        Assert.assertTrue(brandPage.getTypeCellLabelContains(titleAvailableToPlay).isPresent(),
                "In-Eligible user for upsell couldn't see any playable Hulu content");
        brandPage.clickSelect();
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.waitUntilElementIsFocused(detailsPage.getPlayOrContinueButton(), FIFTEEN_SEC_TIMEOUT);
        detailsPage.clickSelect();
        Assert.assertTrue(videoPlayer.waitForVideoToStart().isOpened(), "Video player did not open");
        videoPlayer.clickBack();

        //Go back to the Hulu page
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.clickBack();

        //Move to the "Unlock to Stream More Hulu" collection
        brandPage.waitForLoaderToDisappear(FIFTEEN_SEC_TIMEOUT);
        brandPage.moveLeftUntilElementIsFocused(collectionPage.getFirstCellFromCollection(getCollectionName(
                CollectionConstant.Collection.ENJOY_THESE_SERIES_FROM_HULU)), swipeCount);
        brandPage.moveDownUntilCollectionContentIsFocused(
                getCollectionName(CollectionConstant.Collection.UNLOCK_TO_STREAM_MORE_HULU), swipeCount);
        brandPage.moveRightUntilElementIsFocused(brandPage.getTypeCellLabelContains
                (huluSubscriptionTitle), swipeCount);
        brandPage.clickSelect();
        detailsPage.waitUntilElementIsFocused(detailsPage.getUnlockButton(), FIFTEEN_SEC_TIMEOUT);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(),
                UNLOCK_BUTTON_NOT_DISPLAYED);
        detailsPage.clickSelect();

        //Verify that user is on the ineligible interstitial screen
        sa.assertTrue(detailsPage.isOnlyAvailableWithHuluHeaderPresent(),
                "Ineligible Screen Header is not present");
        sa.assertTrue(detailsPage.isIneligibleScreenBodyPresent(),
                "Ineligible Screen Body is not present");
        sa.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(),
                "Ineligible Screen cta is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-120609"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, US})
    public void verifyHuluHubPageUI() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilDisneyOriginalBrandIsFocused(20);
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU)).isPresent(),
                "Hulu brand tile was not present on home page screen");
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)).isPresent(),
                ESPN_BRAND_TILE_NOT_PRESENT);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU));
        Assert.assertTrue(
                brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU)),
                "Hulu Hub page did not open");
        sa.assertTrue(brandPage.getBrandLogoImage().isPresent(),
                "Hulu logo was not present");
        sa.assertTrue(brandPage.getBrandFeaturedImage().isPresent(),
                "Hulu background artwork was not present");

        brandPage.moveDownUntilCollectionContentIsFocused(
                getCollectionName(CollectionConstant.Collection.STUDIOS_AND_NETWORKS), 10);
        Assert.assertTrue(brandPage.getCollection(CollectionConstant.Collection.STUDIOS_AND_NETWORKS).isPresent(),
                "Studios and Networks collection was not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-122533"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, US})
    public void verifyRecommendationsIncludeHuluTitlesForStandaloneUser() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        List<Item> availableHuluTitlesForStandaloneUserFromApi = getAvailableHuluTitlesForStandaloneUserFromApi();
        List<Item> trendingTitlesFromApi = getExploreAPIItemsFromSet
                (getCollectionName(CollectionConstant.Collection.TRENDING), 20);
        if (trendingTitlesFromApi.isEmpty()) {
            throw new NoSuchElementException("Failed to get Trending collection titles from Explore API");
        }

        Optional<Item> matchingTitle = trendingTitlesFromApi.stream()
                .filter(trendingTitle -> availableHuluTitlesForStandaloneUserFromApi.stream()
                        .anyMatch(availableHuluTitle ->
                                availableHuluTitle.getVisuals().getTitle().equals(trendingTitle.getVisuals().getTitle())
                        ))
                .findFirst();
        if (matchingTitle.isEmpty()) {
            throw new NoSuchElementException("Failed to find a title in Trending collection that matches " +
                    "the available Hulu titles using Explore API");
        }

        ExtendedWebElement huluTitleCell = homePage.getCellElementFromContainer(
                CollectionConstant.Collection.TRENDING, matchingTitle.get().getVisuals().getTitle());

        homePage.moveDownUntilCollectionContentIsFocused(
                getCollectionName(CollectionConstant.Collection.TRENDING), 15);
        homePage.moveRightUntilElementIsFocused(huluTitleCell, 30);
        Assert.assertTrue(huluTitleCell.isElementPresent(),
                "Hulu title cell was not present under Trending collection UI");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-119342"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyLiveModalLinearChannelTile() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        String streamsCollectionName = getCollectionName(STREAMS_NON_STOP_PLAYLISTS);
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.moveDownUntilCollectionContentIsFocused(streamsCollectionName, 12);
        String firstProgramTitle = homePage.getFirstCellTitleFromContainer(STREAMS_NON_STOP_PLAYLISTS).split(",")[1]
                .trim();
        homePage.clickSelect();
        Assert.assertTrue(liveEventModal.isOpened(), LIVE_MODAL_NOT_DISPLAYED);
        Assert.assertTrue(liveEventModal.getDetailsSection().isElementPresent(), "Details section is not present");
        Assert.assertEquals(liveEventModal.getProgramTitle(), firstProgramTitle,
                "Live modal is not displayed for expected content");
        Assert.assertTrue(liveEventModal.getWatchLiveButton().isElementPresent(), WATCH_LIVE_BUTTON_NOT_DISPLAYED);
        Assert.assertTrue(liveEventModal.getDetailsButton().isElementPresent(), DETAILS_BUTTON_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118739"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyLiveModalEpisodicInfo() {
        int maxQuantityOfExpectedChannels = 6;
        String episodicInfoLabelFormat = "Season %s Episode %s %s";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());

        String streamsCollectionName =
                getCollectionName(STREAMS_NON_STOP_PLAYLISTS);

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilCollectionContentIsFocused(streamsCollectionName, 12);

        Item channelItemWithEpisodicInfo = getFirstChannelItemThatHasEpisodicInfo(maxQuantityOfExpectedChannels);
        homePage.moveRightUntilElementIsFocused(
                homePage.getCellElementFromContainer(STREAMS_NON_STOP_PLAYLISTS,
                        channelItemWithEpisodicInfo.getVisuals().getTitle()),
                maxQuantityOfExpectedChannels);
        String seasonNumber = "";
        String episodeNumber = "";
        String episodeTitle = "";
        try {
            seasonNumber = channelItemWithEpisodicInfo.getVisuals().getSeasonNumber();
            episodeNumber = channelItemWithEpisodicInfo.getVisuals().getEpisodeNumber();
            episodeTitle = channelItemWithEpisodicInfo.getVisuals().getEpisodeTitle();
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        LOGGER.info("Episodic Info from Explore API: Season number '{}', Episode number '{}', Episode title '{}'",
                seasonNumber, episodeNumber, episodeTitle);
        homePage.clickSelect();

        Assert.assertTrue(liveEventModal.isOpened(), LIVE_MODAL_NOT_DISPLAYED);
        Assert.assertTrue(liveEventModal.getWatchLiveButton().isElementPresent(), WATCH_LIVE_BUTTON_NOT_DISPLAYED);
        Assert.assertTrue(liveEventModal.getDetailsButton().isElementPresent(), DETAILS_BUTTON_NOT_DISPLAYED);
        Assert.assertEquals(liveEventModal.getSubtitleLabel().getAttribute(LABEL),
                String.format(episodicInfoLabelFormat, seasonNumber, episodeNumber, episodeTitle),
                "Episodic Info label doesn't match expected format or element has more than just episodic info");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118738"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyLiveModalWithEpisodicArtworkForSeriesLiveEvent() {
        int maxQuantityOfExpectedChannels = 6;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        SoftAssert sa = new SoftAssert();

        String streamsCollectionName =
                CollectionConstant.getCollectionName(CollectionConstant.Collection.STREAMS_NON_STOP_PLAYLISTS);

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilCollectionContentIsFocused(streamsCollectionName, 12);
        Item channelItemWithEpisodicInfo = getFirstChannelItemThatHasEpisodicInfo(maxQuantityOfExpectedChannels);
        homePage.moveRightUntilElementIsFocused(
                homePage.getCellElementFromContainer(STREAMS_NON_STOP_PLAYLISTS,
                        channelItemWithEpisodicInfo.getVisuals().getTitle()),
                maxQuantityOfExpectedChannels);
        homePage.clickSelect();

        Assert.assertTrue(liveEventModal.isOpened(), LIVE_MODAL_NOT_DISPLAYED);

        sa.assertTrue(liveEventModal.getWatchLiveButton().isElementPresent(), WATCH_LIVE_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(liveEventModal.getDetailsButton().isElementPresent(), DETAILS_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(liveEventModal.getThumbnailView().isElementPresent(), "Episodic artwork is not present");
        sa.assertEquals(liveEventModal.getThumbnailAspectRatio(), 1.78,
                "Thumbnail aspect ratio wasn't the standard (1.78)");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118742"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyStandardPromptEpisodicSeries() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();

        String streamsNonStopPlaylists =
                CollectionConstant.getCollectionName(CollectionConstant.Collection.STREAMS_NON_STOP_PLAYLISTS);

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilCollectionContentIsFocused(streamsNonStopPlaylists, 10);

        try {
            // Get first series item
            Item channelItemWithEpisodicInfo = getFirstChannelItemThatHasEpisodicInfo(10);
            homePage.moveRightUntilElementIsFocused(
                    homePage.getCellElementFromContainer(STREAMS_NON_STOP_PLAYLISTS,
                            channelItemWithEpisodicInfo.getVisuals().getTitle()), 10);

            String rating = channelItemWithEpisodicInfo.getVisuals().getMetastringParts().getRatingInfo().getRating().getText();
            String seasonNumber = channelItemWithEpisodicInfo.getVisuals().getSeasonNumber();
            String episodeNumber = channelItemWithEpisodicInfo.getVisuals().getEpisodeNumber();
            String episodeTitle = channelItemWithEpisodicInfo.getVisuals().getEpisodeTitle();
            if (Stream.of(rating, seasonNumber, episodeNumber, episodeTitle).noneMatch(Objects::isNull)) {
                String metadataEpisode = String.format("S%s:E%s %s", seasonNumber, episodeNumber, episodeTitle);
                LOGGER.info("Metadata episode {}", metadataEpisode);
                sa.assertTrue(homePage.getTypeCellLabelContains(rating).isPresent(), "Rating is not present in cell episode");
                sa.assertTrue(homePage.getStaticTextByLabelContains(metadataEpisode).isPresent(),
                        "Episode metadata is not present");
            } else {
                throw new SkipException("Series episodes metadata expected is not available");
            }

            // Verify genre info and if exists assert that is not present
            if (channelItemWithEpisodicInfo.getVisuals().getMetastringParts().getGenres() != null) {
                sa.assertFalse(homePage.getStaticTextByLabel(
                                channelItemWithEpisodicInfo.getVisuals().getMetastringParts().getGenres().getLabel()).isPresent(),
                        "Genre is present");
            }
        } catch(SkipException e) {
            throw new SkipException("Series episode stream expected is not available" + e.getMessage());
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67224"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyHomeScreenUI() {
        ArrayList<Container> collections;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();
        try {
            collections = getDisneyAPIPage(HOME_PAGE.getEntityId());
        } catch (Exception e) {
            throw new SkipException("Skipping test, failed to get collection details from the api " + e.getMessage());
        }

        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        for (int i = 2; i < collections.size(); i++) {
            String containerId = collections.get(i).getId();
            String containerName = collections.get(i).getVisuals().getName();
            if (containerId.isEmpty() && containerId == null && containerName.isEmpty() && containerName == null) {
                throw new SkipException("Skipping test, failed to get collection name or id from the api ");
            }

            LOGGER.info("Container Name returned: {} ", containerName);
            sa.assertTrue(homePage.moveDownUntilCollectionIsPresent(containerId, 5),
                    String.format("%s collection is not present", containerName));
        }

        String topContainerID = collections.get(2).getId();

        homePage.moveUpUntilElementIsFocused(homePage.getFirstCellFromCollection(topContainerID), collections.size());
        sa.assertTrue(homePage.isFocused(homePage.getFirstCellFromCollection(topContainerID)),
                "User is not swiped up to top");

        BufferedImage collectionTileBeforeRightSwipe = getElementImage(homePage.getCollection(topContainerID));
        homePage.moveRight(7, 1);
        BufferedImage collectionTileAfterRightSwipe = getElementImage(homePage.getCollection(topContainerID));
        sa.assertTrue(areImagesDifferent(collectionTileBeforeRightSwipe, collectionTileAfterRightSwipe),
                "Collection tile in view and after right swipe are the same");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67200"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyHomeBrandTiles() {
        Container brandCollection;
        int totalBrandTile;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        try {
            brandCollection = getDisneyAPIPage(DisneyEntityIds.HOME_PAGE.getEntityId(),
                    getLocalizationUtils().getLocale(),
                    getLocalizationUtils().getUserLanguage()).get(1);
            totalBrandTile = brandCollection.getItems().size();
        } catch (Exception e) {
            throw new SkipException("Skipping test, failed to get brand collection details from the api " + e.getMessage());
        }

        IntStream.range(0, totalBrandTile - 1).forEach(i -> {
            Visuals brandVisuals = brandCollection.getItems().get(i).getVisuals();
            if (brandVisuals == null || brandVisuals.getTitle().isEmpty()) {
                throw new SkipException("Skipping test, failed to get brand collection details from the api");
            }
            Assert.assertTrue(homePage.getBrandCells().get(i).getText()
                            .contains(brandVisuals.getTitle()),
                    brandVisuals.getTitle() + " title is not matching with UI");
        });
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-115140"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyContinueWatchingContainer() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        CollectionConstant.Collection continueWatchingCollection = CollectionConstant.Collection.CONTINUE_WATCHING;
        String continueWatchingCollectionId =
                CollectionConstant.getCollectionName(continueWatchingCollection);
        int maxCount = 20;
        int titlesLimit = 4;

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_only_murders_in_the_building_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        commonPage.clickRight(5, 1, 1);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickDown();
        videoPlayer.clickBack();

        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        homePage.moveDownUntilCollectionContentIsFocused(continueWatchingCollectionId, maxCount);
        Assert.assertTrue(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container not found");

        List<Item> continueWatchingTitlesFromApi = getExploreAPIItemsFromSet
                (CollectionConstant.getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING), titlesLimit);
        Assert.assertFalse(continueWatchingTitlesFromApi == null,
                "No items for 'Continue Watching' collection were fetched from Explore API");

        Item firstAPICollectionItem = continueWatchingTitlesFromApi.get(0);
        String firstAPICollectionItemTitle = firstAPICollectionItem.getVisuals().getTitle();
        if (firstAPICollectionItemTitle == null) {
            throw new SkipException("First API Collection item did not have a title");
        }
        String firstCellTitle = homePage.getFirstCellTitleFromContainer(continueWatchingCollection).split(",")[0];

        sa.assertEquals(firstCellTitle, firstAPICollectionItemTitle,
                "First element under 'Continue Watching' did not have same Title from the API");
        sa.assertTrue(homePage.isFirstCellFromCollectionAssetImagePresent(continueWatchingCollectionId),
                "First element under 'Continue Watching' did not have Asset image");
        sa.assertTrue(homePage.isFirstCellFromCollectionProgressBarPresent(continueWatchingCollectionId),
                "First element under 'Continue Watching' did not have Progress bar");

        String firstAPICollectionItemSeasonNumber = firstAPICollectionItem.getVisuals().getSeasonNumber();
        String firstAPICollectionItemEpisodeNumber = firstAPICollectionItem.getVisuals().getEpisodeNumber();
        String firstAPICollectionItemEpisodeTitle = firstAPICollectionItem.getVisuals().getEpisodeTitle();
        if (firstAPICollectionItemSeasonNumber == null ||
                firstAPICollectionItemEpisodeNumber == null ||
                firstAPICollectionItemEpisodeTitle == null) {
            throw new SkipException("First API Collection item did not have all episode metadata to validate");
        }
        sa.assertTrue(
                homePage.isFirstCellFromCollectionEpisodeMetadataPresent(continueWatchingCollectionId,
                        firstAPICollectionItemSeasonNumber,
                        firstAPICollectionItemEpisodeNumber,
                        firstAPICollectionItemEpisodeTitle),
                "First element under 'Continue Watching' did not have Episode metadata");

        String firstAPICollectionItemPrompt = firstAPICollectionItem.getVisuals().getPrompt();
        if (firstAPICollectionItemPrompt == null) {
            throw new SkipException("First API Collection item did not have a prompt to validate");
        }
        sa.assertTrue(homePage.isFirstCellFromCollectionStaticTextPresent(
                        continueWatchingCollectionId, firstAPICollectionItemPrompt),
                "First element under 'Continue Watching' did not have Remaining time text");

        homePage.clickSelect();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1, 1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getDetailsTab(), 6);
        Assert.assertEquals(detailsPage.getDetailsTabTitle(), firstAPICollectionItemTitle,
                "Detail page not displayed for expected continue watching content");
        sa.assertAll();
    }

        private Item getFirstChannelItemThatHasEpisodicInfo(int titlesLimit) {
        List<Item> liveChannelsFromApi = getExploreAPIItemsFromSet(
                getCollectionName(STREAMS_NON_STOP_PLAYLISTS), titlesLimit);
        Assert.assertNotNull(liveChannelsFromApi,
                String.format("No items for '%s' collection were fetched from Explore API",
                        STREAMS_NON_STOP_PLAYLISTS));
        for (Item liveChannelFromApi : liveChannelsFromApi) {
            if (liveChannelFromApi.getVisuals().getEpisodeNumber() != null) {
                return liveChannelFromApi;
            }
        }
        throw new NoSuchElementException("Failed to fetch a live channel that has Episodic info");
    }
}
