package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCollectionIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.*;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.net.*;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;
import static com.disney.qa.api.disney.DisneyEntityIds.THE_AVENGERS;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.RatingConstant.SINGAPORE;

public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String RECOMMENDED_FOR_YOU = "Recommended For You";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";
    private static final String HOME_PAGE_DID_NOT_OPEN = "Home page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67371"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHomeUIElements() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //Validate top of home
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found");
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(),
                "'Recommend For You' collection was not found");
        homePage.swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
        BufferedImage recommendedForYouLastTileInView = getElementImage(
                homePage.getCollection(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));
        homePage.swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
        BufferedImage recommendedForYouFirstTileInView = getElementImage(
                homePage.getCollection(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));
        sa.assertTrue(areImagesDifferent(
                        recommendedForYouFirstTileInView,
                        recommendedForYouLastTileInView),
                "Recommended For You first tile in view and last tile in view images are the same");

        BufferedImage topOfHome = getCurrentScreenView();

        //Capture bottom of home
        swipeInContainer(null, Direction.UP, 5, 500);
        BufferedImage closeToBottomOfHome = getCurrentScreenView();

        //Validate back at top of home
        swipePageTillElementPresent(homePage.getImageLabelContains(DISNEY_PLUS),
                10,
                null,
                Direction.DOWN,
                300);
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(),
                "'Recommend For You' collection was not found");
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(),
                "`Disney Plus` image was not found after return to top of home");

        //Validate images are different
        sa.assertTrue(areImagesDifferent(topOfHome, closeToBottomOfHome),
                "Top of home image is the same as bottom of home image");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67377"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRecommendedForYouContainer() {
        int limit = 30;
        int swipeCount = 5;
        String recommendedContainerNotFound = "Recommended For You container was not found";
        String recommendedHeaderNotFound = "Recommended For You Header was not found";
        CollectionConstant.Collection collection = CollectionConstant.Collection.RECOMMENDED_FOR_YOU;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyAccount account = getAccount();
        setAppToHomeScreen(account);

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
        homePage.swipeTillCollectionTappable(collection, Direction.UP, swipeCount);
        sa.assertTrue(homePage.isCollectionPresent(collection), recommendedContainerNotFound);
        sa.assertTrue(homePage.isCollectionTitlePresent(collection), recommendedHeaderNotFound);

        List<String> recommendationTitlesFromApi = getContainerTitlesFromApi
                (CollectionConstant.getCollectionName(collection), limit);

        int size = recommendationTitlesFromApi.size();
        String firstCellTitle = homePage.getFirstCellTitleFromContainer(collection).split(",")[0];
        ExtendedWebElement firstTitle = homePage.getCellElementFromContainer(
                collection,
                recommendationTitlesFromApi.get(0));
        ExtendedWebElement lastTitle = homePage.getCellElementFromContainer(
                collection,
                recommendationTitlesFromApi.get(size - 1));
        Assert.assertTrue(firstCellTitle.equals(recommendationTitlesFromApi.get(0)),
                "UI title value not matched with API title value");

        homePage.swipeInContainerTillElementIsPresent(homePage.getCollection(collection),
                lastTitle,
                30,
                Direction.LEFT);
        Assert.assertTrue(lastTitle.isPresent(),
                "User is not able to swipe through end of container");

        homePage.swipeInContainerTillElementIsPresent(homePage.getCollection(collection),
                firstTitle,
                30,
                Direction.RIGHT);
        Assert.assertTrue(firstTitle.isPresent(),
                "User is not able to swipe to the beginning of container");

        firstTitle.click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(detailsPage.getMediaTitle().equals(firstCellTitle),
                "Content title not matched");
        detailsPage.clickCloseButton();
        sa.assertTrue(homePage.isCollectionPresent(collection), recommendedContainerNotFound);
        sa.assertTrue(homePage.isCollectionTitlePresent(collection), recommendedHeaderNotFound);
        sa.assertTrue(firstTitle.isPresent(),
                "Same position was not retained in Recommend for Your container after coming back from detail page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69549"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyRatingRestrictionTravelingMessage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_STARHUB_SG_STANDALONE, SINGAPORE, ENGLISH_LANG));
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());

        Assert.assertTrue(homePage.isTravelAlertTitlePresent(), "Travel alert title was not present");
        Assert.assertTrue(homePage.isTravelAlertBodyPresent(), "Travel alert body was not present");
        Assert.assertTrue(homePage.getTravelAlertOk().isPresent(), "Travel alert ok button was not present");
        homePage.getTravelAlertOk().click();
        Assert.assertFalse(homePage.isTravelAlertTitlePresent(), "Travel alert was not dismissed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67375"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.HOME, TestGroup.SMOKE, US})
    public void verifyUserTapsOnHomeContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        goToFirstCollectionTitle(homePage);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickCloseButton();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67505"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.HOME, US})
    public void verifyHeroAutoRotationOnHomeScreen() throws URISyntaxException, JsonProcessingException {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
        ArrayList<Container> collections = getDisneyAPIPage(HOME_PAGE.getEntityId());
        String heroCarouselId = "";
        try{
            heroCarouselId = collections.get(0).getId();
        } catch (Exception e){
            throw new SkipException("Skipping test, hero carousel collection id not found:- " +  e.getMessage());
        }
        Assert.assertTrue(homePage.isHeroCarouselDisplayed(heroCarouselId), "Hero Carousel is not displayed");

        String currentHeroTitle = homePage.getCurrentHeroCarouselTitle(heroCarouselId);
        sa.assertTrue(homePage.isHeroCarouselAutoRotating(currentHeroTitle, heroCarouselId),
                "Hero Carousel did not auto rotate after 5 seconds");

        swipeInContainer(homePage.getHeroCarouselContainer(heroCarouselId), Direction.LEFT, 500);

        currentHeroTitle = homePage.getCurrentHeroCarouselTitle(heroCarouselId);
        Assert.assertTrue(homePage.isHeroCarouselDisplayed(heroCarouselId), "Hero Carousel is not displayed");
        sa.assertFalse(homePage.isHeroCarouselAutoRotating(currentHeroTitle, heroCarouselId),
                "Hero Carousel auto rotate after 5 seconds");

        homePage.swipeUp(900);
        sa.assertFalse(homePage.isHeroCarouselDisplayed(heroCarouselId), "Hero Carousel is displayed after swipe Down");
        homePage.swipeDown(900);
        sa.assertTrue(homePage.isHeroCarouselDisplayed(heroCarouselId), "Hero Carousel is not displayed after swipe up");

        currentHeroTitle = homePage.getCurrentHeroCarouselTitle(heroCarouselId);
        sa.assertTrue(homePage.isHeroCarouselAutoRotating(currentHeroTitle, heroCarouselId),
                "Hero Carousel did not auto rotate after 5 seconds");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68157"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContinueWatchingContainer() {
        int swipeCount = 5;
        int titlesLimit = 4;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        // Populate Continue Watching assets
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        detailsPage.clickPlayButton(DisneyAbstractPage.TEN_SEC_TIMEOUT);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();

        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        detailsPage.clickPlayButton(DisneyAbstractPage.TEN_SEC_TIMEOUT);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();

        launchDeeplink(R.TESTDATA.get("disney_prod_mulan_2020_deeplink"));
        detailsPage.clickPlayButton(DisneyAbstractPage.TEN_SEC_TIMEOUT);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));
        detailsPage.clickPlayButton(DisneyAbstractPage.TEN_SEC_TIMEOUT);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
        homePage.waitForHomePageToOpen();
        String continueWatchingCollectionName = CollectionConstant
                .getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING);
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        Assert.assertTrue(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container not found");

        List<Item> continueWatchingTitlesFromApi = getExploreAPIItemsFromSet
                (CollectionConstant.getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING), titlesLimit);
        Assert.assertFalse(continueWatchingTitlesFromApi.isEmpty(),
                "No items for 'Continue Watching' collection were fetched from Explore API");

        Item firstAPICollectionItem = continueWatchingTitlesFromApi.get(0);
        String firstAPICollectionItemTitle = firstAPICollectionItem.getVisuals().getTitle();
        if (firstAPICollectionItemTitle.isEmpty()) {
            throw new SkipException("First API Collection item did not have a title");
        }
        String firstCellTitle = homePage.getFirstCellTitleFromContainer(CollectionConstant.Collection.CONTINUE_WATCHING)
                .split(",")[0];
        sa.assertEquals(firstCellTitle, firstAPICollectionItemTitle,
                "First element under 'Continue Watching' did not have same Title from the API");
        sa.assertTrue(homePage.isFirstCellFromCollectionAssetImagePresent(continueWatchingCollectionName),
                "First element under 'Continue Watching' did not have Asset image");
        sa.assertTrue(homePage.isFirstCellFromCollectionProgressBarPresent(continueWatchingCollectionName),
                "First element under 'Continue Watching' did not have Progress bar");
        sa.assertTrue(homePage.isFirstCellFromCollectionPlayIconPresent(continueWatchingCollectionName),
                "First element under 'Continue Watching' did not have Play icon");
        String firstAPICollectionItemSeasonNumber = firstAPICollectionItem.getVisuals().getSeasonNumber();
        String firstAPICollectionItemEpisodeNumber = firstAPICollectionItem.getVisuals().getEpisodeNumber();
        String firstAPICollectionItemEpisodeTitle = firstAPICollectionItem.getVisuals().getEpisodeTitle();
        if (firstAPICollectionItemSeasonNumber.isEmpty() ||
                firstAPICollectionItemEpisodeNumber.isEmpty() ||
                firstAPICollectionItemEpisodeTitle.isEmpty() ) {
            throw new SkipException("First API Collection item did not have episode metadata to validate");
        }
        sa.assertTrue(
                homePage.isFirstCellFromCollectionEpisodeMetadataPresent(continueWatchingCollectionName,
                        firstAPICollectionItemSeasonNumber,
                        firstAPICollectionItemEpisodeNumber,
                        firstAPICollectionItemEpisodeTitle),
                "First element under 'Continue Watching' did not have Episode metadata");

        String firstAPICollectionItemPrompt = firstAPICollectionItem.getVisuals().getPrompt();
        if (firstAPICollectionItemPrompt.isEmpty()) {
            throw new SkipException("First API Collection item did not have a prompt to validate");
        }
        sa.assertTrue(homePage.isFirstCellFromCollectionRemainingTimePresent(
                        continueWatchingCollectionName, firstAPICollectionItemPrompt),
                "First element under 'Continue Watching' did not have Remaining time text");


        String lastAPICollectionItemTitle = continueWatchingTitlesFromApi.get(continueWatchingTitlesFromApi.size() - 1)
                .getVisuals().getTitle();
        if (lastAPICollectionItemTitle.isEmpty()) {
            throw new SkipException("Last API Collection item did not have a title");
        }
        ExtendedWebElement lastElement = homePage.getCellElementFromContainer(
                CollectionConstant.Collection.CONTINUE_WATCHING, lastAPICollectionItemTitle);
        homePage.swipeInContainerTillElementIsPresent(
                homePage.getCollection(CollectionConstant.Collection.CONTINUE_WATCHING),
                lastElement,
                10,
                Direction.LEFT);
        sa.assertTrue(lastElement.isPresent(),
                "Last element under 'Continue Watching' was not visible after swiping left in the container");

        ExtendedWebElement firstElement = homePage.getCellElementFromContainer(
                CollectionConstant.Collection.CONTINUE_WATCHING, firstAPICollectionItemTitle);
        homePage.swipeInContainerTillElementIsPresent(
                homePage.getCollection(CollectionConstant.Collection.CONTINUE_WATCHING),
                firstElement,
                10,
                Direction.RIGHT);
        sa.assertTrue(firstElement.isPresent(),
                "First element under 'Continue Watching' was not visible after swiping right in the container");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75465"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackForEpisodesInSets() {
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_collection_treehouse_of_horror"));
        collectionPage.waitForCollectionPageToOpen("The Simpsons Treehouse of Horror");

        collectionPage.swipeTillCollectionTappable(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V,
                Direction.UP, 5);
        Assert.assertTrue(collectionPage.isCollectionPresent(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V),
                "Treehouse of Horror I-V container not found");

        collectionPage.getFirstCellFromCollectionEpisodeMetadataElement(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V))
                .click();

        Assert.assertTrue(videoPlayer.isOpened(), "Video Player did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68177"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContinueWatchingContainerDeletedAfterContentComplete() throws URISyntaxException, JsonProcessingException {
        int swipeCount = 5;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        // Populate Continue Watching assets
        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(10);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();

        homePage.waitForHomePageToOpen();
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        Assert.assertTrue(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container not found");
        Assert.assertTrue(homePage.getCellElementFromContainer(CollectionConstant.Collection.CONTINUE_WATCHING,
                THE_AVENGERS.getTitle()).isPresent(), "Title not found in Continue watching container");

        homePage.clickCollectionTile(CollectionConstant.Collection.CONTINUE_WATCHING, 1);
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(THE_AVENGERS.getTitle()),
                "Expected content title not playing");
        videoPlayer.scrubToPlaybackPercentage(99);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        Assert.assertFalse(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container found after content completed");
        Assert.assertFalse(homePage.getCellElementFromContainer(CollectionConstant.Collection.CONTINUE_WATCHING,
                THE_AVENGERS.getTitle()).isPresent(), "Title found in Continue watching container");

        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        ExploreContent movieApiContent = getDisneyApiMovie(THE_AVENGERS.getEntityId());
        String contentTimeFromAPI = detailsPage.getHourMinFormatForDuration(movieApiContent.getDurationMs());

        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getRemainingTimeInStringWithHourAndMinutes().equals(contentTimeFromAPI),
                "Video is not playing from begining");
    }

    private void goToFirstCollectionTitle(DisneyPlusHomeIOSPageBase homePage) {
        String collectionID, contentTitle;
        try {
            ArrayList<Container> collections = getDisneyAPIPage(HOME_PAGE.getEntityId(),
                    getLocalizationUtils().getLocale(),
                    getLocalizationUtils().getUserLanguage());
            collectionID = collections.get(2).getId();
            contentTitle = collections.get(2).getItems().get(0).getVisuals().getTitle();
            swipe(homePage.getDynamicAccessibilityId(collectionID));
            homePage.getElementTypeCellByLabel(contentTitle).click();
        } catch (URISyntaxException | JsonProcessingException | IndexOutOfBoundsException e) {
            throw new RuntimeException(String.format("Not able to get the Home page data from the api, exception occurred: %s", e));
        }
    }
}
