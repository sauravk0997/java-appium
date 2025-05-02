package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.common.constant.CollectionConstant;
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
import java.util.stream.Stream;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;
import static com.disney.qa.common.constant.CollectionConstant.Collection.STREAMS_NON_STOP_PLAYLISTS;
import static com.disney.qa.common.constant.CollectionConstant.getCollectionName;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BASIC_MONTHLY;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {

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
                "ESPN brand tile was not present on home page screen");

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

        String lockedHuluContentCollectionName =
                getCollectionName(CollectionConstant.Collection.UNLOCK_TO_STREAM_MORE_HULU);

        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.HULU));

        //Validate in-eligible for upsell user still has some content to watch
        String titleAvailableToPlay = "Hulu Original Series, Select for details on this title.";
        Assert.assertTrue(brandPage.getTypeCellLabelContains(titleAvailableToPlay).isPresent(),
                "In-Eligible user for upsell couldn't see any playable Hulu content");
        brandPage.clickDown();
        brandPage.clickSelect();
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.waitUntilElementIsFocused(detailsPage.getPlayOrContinueButton(), 15);
        detailsPage.clickSelect();
        Assert.assertTrue(videoPlayer.waitForVideoToStart().isOpened(), "Video player did not open");
        videoPlayer.clickBack();

        //Go back to the Hulu page
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.clickBack();

        //Move to the "Unlock to Stream More Hulu" collection
        brandPage.waitForLoaderToDisappear(15);
        brandPage.moveDownUntilCollectionContentIsFocused(lockedHuluContentCollectionName, 3);
        brandPage.clickSelect();
        detailsPage.waitUntilElementIsFocused(detailsPage.getUpgradeNowButton(), 15);
        Assert.assertTrue(detailsPage.getUpgradeNowButton().isPresent(),
                "Upgrade Now button was not present");
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
                "ESPN brand tile was not present on home page screen");

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
                (getCollectionName(CollectionConstant.Collection.TRENDING), 30);
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
        int maxQuantityOfExpectedChannels = 10;
        String episodicInfoLabelFormat = "Season %s Episode %s %s";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());

        String streamsCollectionName =
                getCollectionName(STREAMS_NON_STOP_PLAYLISTS);

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilCollectionContentIsFocused(streamsCollectionName, 6);

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
        int maxQuantityOfExpectedChannels = 10;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        SoftAssert sa = new SoftAssert();

        String streamsCollectionName =
                CollectionConstant.getCollectionName(CollectionConstant.Collection.STREAMS_NON_STOP_PLAYLISTS);

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        homePage.moveDownUntilCollectionContentIsFocused(streamsCollectionName, 6);
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
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();
        ArrayList<Container> collections = getDisneyAPIPage(HOME_PAGE.getEntityId());
        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        for (int i = 2; i < collections.size(); i++) {
            String containerId = collections.get(i).getId();
            String containerName = collections.get(i).getVisuals().getName();
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
