package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.explore.response.Visuals;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121758"})
    @Test(groups = {TestGroup.HOME, TestGroup.ESPN, US})
    public void verifyESPNTileUS() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTileToBrandTile();
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)).isPresent(),
                ESPN_BRAND_TILE_NOT_PRESENT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121942"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyESPNBrandTiles() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)).isPresent(),
                ESPN_BRAND_TILE_NOT_PRESENT);

        homePage.moveDownFromHeroTileToBrandTile();
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
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();

        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU)).isPresent(),
                "Hulu brand tile was not present on home page screen");
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)).isPresent(),
                ESPN_BRAND_TILE_NOT_PRESENT);

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU));
        Assert.assertTrue(
                brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU)),
                "Hulu Hub page did not open");
        brandPage.clickBack();

        homePage.waitForPresenceOfAnElement(
                homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)));
        homePage.clickUp();
        homePage.moveDownFromHeroTileToBrandTile();
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
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU));

        //Validate in-eligible for upsell user still has some content to watch
        String titleAvailableToPlay = "Hulu Original Series";
        int waitTimeout = 15;
        homePage.moveDownUntilCollectionContentIsFocused(
                getCollectionName(CollectionConstant.Collection.ENJOY_THESE_SERIES_FROM_HULU), 5);
        homePage.moveRightUntilElementIsFocused(brandPage.getTypeCellLabelContains(titleAvailableToPlay), 15);
        Assert.assertTrue(brandPage.getTypeCellLabelContains(titleAvailableToPlay).isPresent(),
                "In-Eligible user for upsell couldn't see any playable Hulu content");
        brandPage.clickSelect();
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.waitUntilElementIsFocused(detailsPage.getPlayOrContinueButton(), waitTimeout);
        detailsPage.clickSelect();
        Assert.assertTrue(videoPlayer.waitForVideoToStart().isOpened(), "Video player did not open");
        videoPlayer.clickBack();

        //Go back to the Hulu page
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.clickBack();

        //Move to the "Unlock to Stream More Hulu" collection
        brandPage.waitForLoaderToDisappear(FIFTEEN_SEC_TIMEOUT);
        brandPage.moveLeftUntilFirstCellIsFocused(getCollectionName(CollectionConstant.Collection.ENJOY_THESE_SERIES_FROM_HULU), 15);
        brandPage.moveDownUntilCollectionContentIsFocused(
                getCollectionName(CollectionConstant.Collection.UNLOCK_TO_STREAM_MORE_HULU), 5);
        brandPage.moveRightUntilElementIsFocused(brandPage.getTypeCellLabelContains
                (titleAvailableToPlay), 5);
        brandPage.clickSelect();
        detailsPage.waitUntilElementIsFocused(detailsPage.getUnlockButton(), waitTimeout);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(),
                "Unlock button was not present");
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
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTileToBrandTile();
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

        logIn(getUnifiedAccount());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118739"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyLiveModalEpisodicInfo() {
        int maxQuantityOfExpectedChannels = 6;
        String episodicInfoLabelFormat = "Season %s Episode %s %s";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());

        String streamsCollectionName =
                getCollectionName(STREAMS_NON_STOP_PLAYLISTS);

        logIn(getUnifiedAccount());
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
        Assert.assertTrue(liveEventModal.getWatchLiveButton().isElementPresent(), "Watch Live CTA is not present");
        Assert.assertTrue(liveEventModal.getDetailsButton().isElementPresent(), "Details CTA is not present");
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

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilCollectionContentIsFocused(streamsCollectionName, 12);
        Item channelItemWithEpisodicInfo = getFirstChannelItemThatHasEpisodicInfo(maxQuantityOfExpectedChannels);
        homePage.moveRightUntilElementIsFocused(
                homePage.getCellElementFromContainer(STREAMS_NON_STOP_PLAYLISTS,
                        channelItemWithEpisodicInfo.getVisuals().getTitle()),
                maxQuantityOfExpectedChannels);
        homePage.clickSelect();

        Assert.assertTrue(liveEventModal.isOpened(), LIVE_MODAL_NOT_DISPLAYED);

        sa.assertTrue(liveEventModal.getWatchLiveButton().isElementPresent(), "Watch Live CTA is not present");
        sa.assertTrue(liveEventModal.getDetailsButton().isElementPresent(), "Details CTA is not present");
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

        logIn(getUnifiedAccount());
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

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        for (int i = 2; i < collections.size(); i++) {
            String containerId = collections.get(i).getId();
            String containerName = collections.get(i).getVisuals().getName();
            if (containerId.isEmpty() && containerId == null && containerName.isEmpty() && containerName == null) {
                throw new SkipException("Skipping test, failed to get collection name or id from the api ");
            }

            LOGGER.info("Container Name returned: {} ", containerName);
            homePage.moveDownUntilCollectionContentIsFocused(containerId, 5);
            sa.assertTrue(homePage.isFocused(homePage.getFirstCellFromCollection(containerId)),
                    "User is not able to swipe to container " + containerName);
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
        logIn(getUnifiedAccount());
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
