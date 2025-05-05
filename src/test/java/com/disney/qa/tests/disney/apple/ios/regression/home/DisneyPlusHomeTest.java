package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.hatter.api.alice.AliceApiManager;
import com.disney.hatter.api.alice.model.ImagesRequestS3;
import com.disney.hatter.api.alice.model.ImagesResponse360;
import com.disney.hatter.core.utils.FileUtil;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

import static com.disney.qa.api.disney.DisneyEntityIds.*;
import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.CollectionConstant.Collection.STUDIOS_AND_NETWORKS;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.RatingConstant.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";
    private static final String HOME_PAGE_DID_NOT_OPEN = "Home page did not open";
    private static final String HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE = "Hulu tile is not visible on home page";
    private static final String BACK_BUTTON_NOT_PRESENT = "Back button is not present";
    private static final String HULU_BRAND_LOGO_NOT_EXPANDED = "Hulu brand logo is not expanded";

    private static final AliceApiManager ALICE_API_MANAGER = new AliceApiManager(MULTIVERSE_STAGING_ENDPOINT);

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67371"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHomeUIElements() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        //Validate top of home
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found");
        homePage.swipeUpTillCollectionCompletelyVisible(CollectionConstant.Collection.NEWLY_ADDED, 5);
        sa.assertTrue(homePage.getTypeOtherContainsName(CollectionConstant.getCollectionTitle
                        (CollectionConstant.Collection.NEWLY_ADDED)).isPresent(),
                "'Newly Added' collection was not found");
        homePage.swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.NEWLY_ADDED);
        BufferedImage collectionLastTileInView = getElementImage(
                homePage.getCollection(CollectionConstant.Collection.NEWLY_ADDED));
        homePage.swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.NEWLY_ADDED);
        BufferedImage collectionFirstTileInView = getElementImage(
                homePage.getCollection(CollectionConstant.Collection.NEWLY_ADDED));
        sa.assertTrue(areImagesDifferent(
                        collectionFirstTileInView,
                        collectionLastTileInView),
                "Collection first tile in view and last tile in view images are the same");

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
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(),
                "`Disney Plus` image was not found after return to top of home");
        homePage.swipeUpTillCollectionCompletelyVisible(CollectionConstant.Collection.NEWLY_ADDED, 5);
        sa.assertTrue(homePage.getTypeOtherContainsName(CollectionConstant.getCollectionTitle
                        (CollectionConstant.Collection.NEWLY_ADDED)).isPresent(),
                "'Newly Added' collection was not found");

        //Validate images are different
        sa.assertTrue(areImagesDifferent(topOfHome, closeToBottomOfHome),
                "Top of home image is the same as bottom of home image");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67377"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRecommendedForYouContainer() {
        int limit = 30;
        int verticalSwipeCount = 5;
        int horizontalSwipeCount = 30;
        int swipeDuration = 100;
        String recommendedContainerNotFound = "Recommended For You container was not found";
        String recommendedHeaderNotFound = "Recommended For You Header was not found";
        CollectionConstant.Collection collection = CollectionConstant.Collection.RECOMMENDED_FOR_YOU;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
        homePage.swipeTillCollectionTappable(collection, Direction.UP, verticalSwipeCount);
        Assert.assertTrue(homePage.isCollectionPresent(collection), recommendedContainerNotFound);
        Assert.assertTrue(homePage.isCollectionTitlePresent(collection), recommendedHeaderNotFound);

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
        Assert.assertEquals(firstCellTitle, recommendationTitlesFromApi.get(0),
                "UI title value not matched with API title value");

        ExtendedWebElement recommendedForYouCollection = homePage.getCollection(collection);
        homePage.swipePageTillElementPresent(lastTitle, horizontalSwipeCount, recommendedForYouCollection,
                Direction.LEFT, swipeDuration);
        Assert.assertTrue(lastTitle.isPresent(),
                "User is not able to swipe through end of container");

        homePage.swipePageTillElementPresent(firstTitle, horizontalSwipeCount, recommendedForYouCollection,
                Direction.RIGHT, swipeDuration);
        Assert.assertTrue(firstTitle.isPresent(),
                "User is not able to swipe to the beginning of container");

        firstTitle.click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertEquals(detailsPage.getMediaTitle(), firstCellTitle, "Content title not matched");
        detailsPage.clickCloseButton();
        sa.assertTrue(homePage.isCollectionPresent(collection), recommendedContainerNotFound);
        sa.assertTrue(homePage.isCollectionTitlePresent(collection), recommendedHeaderNotFound);
        sa.assertTrue(firstTitle.isPresent(),
                "Same position was not retained in Recommend for Your container after coming back from detail page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69549"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, DE})
    public void verifyRatingRestrictionTravelingMessage() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), JP);
        initialSetup();
        handleAlert();
        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomePage.clickLogInButton();
        login(getUnifiedAccount());
        handleGenericPopup(5,1);
        homePage.waitForPresenceOfAnElement(homePage.getTravelAlertTitle());
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
        setAppToHomeScreen(getUnifiedAccount());
        goToFirstCollectionTitle(homePage);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickCloseButton();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67505"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.HOME, US})
    public void verifyHeroAutoRotationOnHomeScreen() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
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
        setAppToHomeScreen(getUnifiedAccount());

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
        Assert.assertFalse(continueWatchingTitlesFromApi == null,
                "No items for 'Continue Watching' collection were fetched from Explore API");

        Item firstAPICollectionItem = continueWatchingTitlesFromApi.get(0);
        String firstAPICollectionItemTitle = firstAPICollectionItem.getVisuals().getTitle();
        if (firstAPICollectionItemTitle == null) {
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
        if (firstAPICollectionItemSeasonNumber == null ||
                firstAPICollectionItemEpisodeNumber == null ||
                firstAPICollectionItemEpisodeTitle == null ) {
            throw new SkipException("First API Collection item did not have all episode metadata to validate");
        }
        sa.assertTrue(
                homePage.isFirstCellFromCollectionEpisodeMetadataPresent(continueWatchingCollectionName,
                        firstAPICollectionItemSeasonNumber,
                        firstAPICollectionItemEpisodeNumber,
                        firstAPICollectionItemEpisodeTitle),
                "First element under 'Continue Watching' did not have Episode metadata");

        String firstAPICollectionItemPrompt = firstAPICollectionItem.getVisuals().getPrompt();
        if (firstAPICollectionItemPrompt == null) {
            throw new SkipException("First API Collection item did not have a prompt to validate");
        }
        sa.assertTrue(homePage.isFirstCellFromCollectionStaticTextPresent(
                        continueWatchingCollectionName, firstAPICollectionItemPrompt),
                "First element under 'Continue Watching' did not have Remaining time text");


        String lastAPICollectionItemTitle = continueWatchingTitlesFromApi.get(continueWatchingTitlesFromApi.size() - 1)
                .getVisuals().getTitle();
        if (lastAPICollectionItemTitle == null) {
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
    public void verifyPlaybackForEpisodesInSetsByTappingOnMetadata() {
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_collection_treehouse_of_horror"));
        collectionPage.waitForCollectionPageToOpen(
                CollectionConstant.getCollectionTitle(CollectionConstant.Collection.TREEHOUSE_OF_HORROR));

        collectionPage.swipeTillCollectionTappable(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V,
                Direction.UP, 5);
        Assert.assertTrue(collectionPage.isCollectionPresent(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V),
                "Treehouse of Horror I-V container not found");

        collectionPage.getFirstCellFromCollectionEpisodeMetadataElement(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V))
                .click();

        Assert.assertTrue(videoPlayer.isOpened(), "Video Player did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68155"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContinueWatchingContainerNotBeingDisplayed() {
        int swipeCount = 5;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        boolean isContinueWatchingContainerVisible = homePage.isCollectionVisibleAfterSwiping(
                CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        Assert.assertFalse(isContinueWatchingContainerVisible,
                String.format("Continue Watching container was visible after %s swipes", swipeCount));

        // Add a secondary profile and select it
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        chooseAvatar.clickSkipButton();
        addProfile.enterProfileName(SECONDARY_PROFILE);
        addProfile.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        addProfile.chooseGender();
        addProfile.clickSaveProfileButton();
        addProfile.clickSecondaryButton();
        moreMenu.getProfileAvatar(SECONDARY_PROFILE).click();
        homePage.waitForHomePageToOpen();

        boolean isContinueWatchingContainerVisibleOnSecondaryProfile = homePage.isCollectionVisibleAfterSwiping(
                CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        Assert.assertFalse(isContinueWatchingContainerVisibleOnSecondaryProfile,
                String.format(
                        "Continue Watching container was visible after %s swipes on secondary profile", swipeCount));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68177"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContinueWatchingContainerNotPresentAfterContentComplete() {
        int swipeCount = 5;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusUpNextIOSPageBase upNextPage = initPage(DisneyPlusUpNextIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        // Populate Continue Watching assets
        addContentInContinueWatching(R.TESTDATA.get("disney_prod_the_avengers_deeplink"), 10);

        homePage.waitForHomePageToOpen();
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        Assert.assertTrue(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container not found");
        Assert.assertTrue(homePage.getCellElementFromContainer(CollectionConstant.Collection.CONTINUE_WATCHING,
                THE_AVENGERS.getTitle()).isPresent(), "Title not found in Continue watching container");

        homePage.getCellElementFromContainer(CollectionConstant.Collection.CONTINUE_WATCHING,
                THE_AVENGERS.getTitle()).click();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(THE_AVENGERS.getTitle()),
                "Title didn't play from continue watching shelf");
        videoPlayer.scrubToPlaybackPercentage(95);
        upNextPage.waitForUpNextUIToAppear();
        videoPlayer.clickBackButton();
        homePage.waitForElementToDisappear(
                homePage.getCollection(CollectionConstant.Collection.CONTINUE_WATCHING), FIFTEEN_SEC_TIMEOUT);
        Assert.assertFalse(
                homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING, FIVE_SEC_TIMEOUT),
                "Continue Watching Container found after content completed");
        Assert.assertFalse(homePage.getCellElementFromContainer(
                CollectionConstant.Collection.CONTINUE_WATCHING, THE_AVENGERS.getTitle()).isPresent(FIVE_SEC_TIMEOUT),
                "Title found in Continue watching container");

        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        ExploreContent movieApiContent = getMovieApi(THE_AVENGERS.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String contentTimeFromAPI = detailsPage.getHourMinFormatForDuration(movieApiContent.getDurationMs());

        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.getPlayerView().isPresent(SHORT_TIMEOUT), "Video player did not open");
        Assert.assertTrue(videoPlayer.getRemainingTimeInStringWithHourAndMinutes().equals(contentTimeFromAPI),
                "Video is not playing from beginning");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68165"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContinueWatchingContainerScrollForOneItem() {
        int swipeCount = 5;
        int titlesLimit = 1;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        // Populate Continue Watching assets
        addContentInContinueWatching(R.TESTDATA.get("disney_prod_the_avengers_deeplink"), 10);

        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        Assert.assertTrue(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container not found");

        List<Item> continueWatchingTitlesFromApi =
                getItemsFromCollection(CollectionConstant.Collection.CONTINUE_WATCHING, titlesLimit);

        Item firstAPICollectionItem = continueWatchingTitlesFromApi.get(0);
        String firstAPICollectionItemTitle = firstAPICollectionItem.getVisuals().getTitle();
        if (firstAPICollectionItemTitle == null) {
            throw new SkipException("First API Collection item did not have a title");
        }

        ExtendedWebElement continueWatchingElement = homePage.getCellElementFromContainer(
                CollectionConstant.Collection.CONTINUE_WATCHING, firstAPICollectionItemTitle);
        Point initialLocation = continueWatchingElement.getLocation();
        homePage.swipeInContainer(
                homePage.getCollection(CollectionConstant.Collection.CONTINUE_WATCHING),
                Direction.LEFT, 2, 900
        );
        homePage.swipeInContainer(
                homePage.getCollection(CollectionConstant.Collection.CONTINUE_WATCHING),
                Direction.RIGHT, 2, 900
        );
        Point newLocation = continueWatchingElement.getLocation();
        Assert.assertEquals(initialLocation, newLocation,
                "'Continue Watching' title moved from position");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68159"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContinueWatchingItemSelection() {
        int swipeCount = 5;
        int thresholdInMins = 1;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        // Populate Continue Watching assets
        addContentInContinueWatching(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"), 30);

        homePage.waitForHomePageToOpen();
        CollectionConstant.Collection continueWatching = CollectionConstant.Collection.CONTINUE_WATCHING;
        homePage.swipeTillCollectionTappable(continueWatching, Direction.UP, swipeCount);
        Assert.assertTrue(homePage.isCollectionPresent(continueWatching), "Continue Watching Container not found");

        String continueWatchingCollectionName = CollectionConstant.getCollectionName(continueWatching);
        int homePageRemainingTimeInMinutes =
                homePage.getFirstCellRemainingTimeInMinutesFromCollection(continueWatchingCollectionName);
        ExtendedWebElement firstElement = homePage.getFirstCellFromCollection(continueWatchingCollectionName);
        firstElement.click();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickPauseButton();
        int videoPlayerRemainingTimeInMinutes = videoPlayer.getRemainingTimeInMinutes();

        Assert.assertTrue(
                Math.abs(homePageRemainingTimeInMinutes - videoPlayerRemainingTimeInMinutes) <= thresholdInMins,
                "Playback did not start from user's most recent bookmark");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75464"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackForEpisodesInSetsByTappingOnArtwork() {
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_collection_treehouse_of_horror"));
        collectionPage.waitForCollectionPageToOpen(
                CollectionConstant.getCollectionTitle(CollectionConstant.Collection.TREEHOUSE_OF_HORROR));

        collectionPage.swipeTillCollectionTappable(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V,
                Direction.UP, 5);
        Assert.assertTrue(collectionPage.isCollectionPresent(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V),
                "Treehouse of Horror I-V container not found");

        collectionPage.getFirstCellFromCollectionAssetImage(
                        CollectionConstant.getCollectionName(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V))
                .click();

        Assert.assertTrue(videoPlayer.isOpened(), "Video Player did not open");

        videoPlayer.clickBackButton();
        collectionPage.waitForCollectionPageToOpen(
                CollectionConstant.getCollectionTitle(CollectionConstant.Collection.TREEHOUSE_OF_HORROR));
        Assert.assertTrue(collectionPage.isCollectionPresent(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67511"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEpisodeInSetUIElements() {
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String collectionName = CollectionConstant.getCollectionName(
                CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V);
        setAppToHomeScreen(getUnifiedAccount());

        Visuals firstEpisodeFromCollectionVisuals = getItemsFromCollection(
                CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V, 1).get(0).getVisuals();

        String firstEpisodeFromCollectionSeriesTitle = firstEpisodeFromCollectionVisuals.getTitle();
        String firstEpisodeFromCollectionTitle = firstEpisodeFromCollectionVisuals.getEpisodeTitle();
        String firstEpisodeFromCollectionSeasonNumber = firstEpisodeFromCollectionVisuals.getSeasonNumber();
        String firstEpisodeFromCollectionEpisodeNumber = firstEpisodeFromCollectionVisuals.getEpisodeNumber();

        if (firstEpisodeFromCollectionSeriesTitle == null ||
                firstEpisodeFromCollectionSeasonNumber == null ||
                firstEpisodeFromCollectionEpisodeNumber == null ||
                firstEpisodeFromCollectionTitle  == null ) {
            throw new SkipException("At least one Episode metadata from API was set to null");
        }

        launchDeeplink(R.TESTDATA.get("disney_prod_collection_treehouse_of_horror"));
        collectionPage.waitForCollectionPageToOpen(
                CollectionConstant.getCollectionTitle(CollectionConstant.Collection.TREEHOUSE_OF_HORROR));

        collectionPage.swipeTillCollectionTappable(CollectionConstant.Collection.TREEHOUSE_OF_HORROR_I_TO_V,
                Direction.UP, 5);

        sa.assertTrue(
                collectionPage.isFirstCellFromCollectionStaticTextPresent(collectionName,
                        firstEpisodeFromCollectionSeriesTitle),
                "First element of the collection did not have series title"
        );

        sa.assertTrue(
                collectionPage.isFirstCellFromCollectionEpisodeMetadataPresent(collectionName,
                        firstEpisodeFromCollectionSeasonNumber,
                        firstEpisodeFromCollectionEpisodeNumber,
                        firstEpisodeFromCollectionTitle),
                "First element of the collection did not have episode metadata");

        sa.assertTrue(collectionPage.isFirstCellFromCollectionAssetImagePresent(collectionName),
                "First element of the collection did not have Asset image");
        sa.assertTrue(collectionPage.isFirstCellFromCollectionPlayIconPresent(collectionName),
                "First element  of the collection did not have Play icon");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77869"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, US})
    public void verifyStandaloneUserSubBrandTile() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        //Open Hulu brand page
        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        Assert. assertTrue(brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU)),
                "After tapping the hulu tile user did not land on Hulu screen");
        huluPage.getBackButton().click();
        Assert. assertTrue(homePage.isOpened(), "Home page didn't open after closing the Hulu page");

        //Open ESPN brand page
        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.ESPN));
        Assert. assertTrue(brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.ESPN)),
                "After tapping the ESPN tile user did not land on ESPN screen");
        homePage.getBackButton().click();
        Assert. assertTrue(homePage.isOpened(), "Home page didn't open after closing the ESPN page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69662"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, SG})
    public void verifyStarBrandTile() {
        int totalExpectedBrands = 6;
        Container brandCollection;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PREMIUM_MONTHLY_SINGAPORE)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), SINGAPORE);

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), SINGAPORE);
        setAppToHomeScreen(getUnifiedAccount());
        handleAlert();
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        try {
            brandCollection = getDisneyAPIPage(HOME_PAGE.getEntityId(),
                    getLocalizationUtils().getLocale(),
                    getLocalizationUtils().getUserLanguage()).get(1);
        } catch (Exception e) {
            throw new SkipException("Skipping test, failed to get brand collection details from the api " + e.getMessage());
        }

        int totalBrandTile = brandCollection.getItems().size();
        swipe(homePage.getHomePageMainElement());
        Assert.assertEquals(totalBrandTile, totalExpectedBrands, "Total number of brand does not match with expected");

        IntStream.range(0, getExpectedBrand().size() - 1).forEach(i -> {
            Assert.assertTrue(homePage.getBrandCells().get(i).getText()
                            .contains(getExpectedBrand().get(i)),
                    getExpectedBrand().get(i) + " tile is not in order");
            Assert.assertTrue(homePage.getBrandCells().get(i).getText()
                            .contains(brandCollection.getItems().get(i).getVisuals().getTitle()),
                    brandCollection.getItems().get(i).getVisuals().getTitle() + " title is not matching with UI");
        });
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68163"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContinueWatchingWhenBookmarkLessThanOneMin() {
        int swipeCount = 5;
        int expectedRemainingTimeInSec = 70;
        String lessThanOneMinMessage = "Less than 1m remaining";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
        homePage.waitForHomePageToOpen();
        addContentInContinueWatchingWithExpectedRemainingTime(
                R.TESTDATA.get("disney_prod_series_party_animals_first_episode_playback_deeplink"),
                expectedRemainingTimeInSec);
        homePage.waitForHomePageToOpen();
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        sa.assertTrue(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container not found for Adult profile");
        sa.assertTrue(homePage.isFirstCellFromCollectionStaticTextPresent(
                        CollectionConstant.getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING),
                        lessThanOneMinMessage),
                lessThanOneMinMessage + " message not displayed on tile for Adult profile");

        // Verify for KIDS profile
        homePage.clickMoreTab();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        homePage.waitForHomePageToOpen();
        addContentInContinueWatchingWithExpectedRemainingTime(
                R.TESTDATA.get("disney_prod_series_party_animals_first_episode_playback_deeplink"),
                expectedRemainingTimeInSec);

        whoIsWatching.clickProfile(KIDS_PROFILE);
        homePage.waitForHomePageToOpen();
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.CONTINUE_WATCHING, Direction.UP, swipeCount);
        sa.assertTrue(homePage.isCollectionPresent(CollectionConstant.Collection.CONTINUE_WATCHING),
                "Continue Watching Container not found for Kid profile");
        sa.assertTrue(homePage.isFirstCellFromCollectionStaticTextPresent(
                        CollectionConstant.getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING),
                        lessThanOneMinMessage),
                lessThanOneMinMessage + " message not displayed on tile for Kid profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74564"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluBrandTileOnHome() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(
                DisneyPlusBrandIOSPageBase.Brand.HULU)).isPresent(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        Assert.assertTrue(huluPage.isOpened(), "Hulu page didn't open after navigating via brand tile");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74829"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluBrandPage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);

        setAccount(getUnifiedAccountApi()
                .createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(
                DisneyPlusBrandIOSPageBase.Brand.HULU)).isPresent(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), HULU_BRAND_LOGO_NOT_EXPANDED);
        sa.assertTrue(huluPage.getBackButton().isPresent(), BACK_BUTTON_NOT_PRESENT);
        sa.assertTrue(huluPage.isArtworkBackgroundPresent(), ARTWORK_IMAGE_NOT_DISPLAYED);
        huluPage.swipeInHuluBrandPage(Direction.UP);
        sa.assertTrue(huluPage.isHuluBrandImageCollapsed(), "Hulu brand logo is not collapsed");
        huluPage.swipeInHuluBrandPage(Direction.DOWN);
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), HULU_BRAND_LOGO_NOT_EXPANDED);
        huluPage.tapBackButton();
        Assert.assertTrue(homePage.isHuluTileVisible(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        huluPage.waitForLoaderToDisappear(TEN_SEC_TIMEOUT);
        sa.assertTrue(huluPage.validateScrollingInHuluCollection(CollectionConstant.Collection.HULU_ORIGINALS),
                "Unable to validate Scrolling in Hulu Collection");
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), STUDIOS_AND_NETWORKS_NOT_DISPLAYED);
        verifyNetworkLogoValues(sa, huluPage);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74642"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluPageContent() throws URISyntaxException, JsonProcessingException {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(
                DisneyPlusBrandIOSPageBase.Brand.HULU)).isPresent(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));

        //To get the collections details of Hulu from API
        ArrayList<Container> collections = getHuluAPIPage(HULU_PAGE.getEntityId());
        //Click any title from collection
        try {
            String titleFromCollection = collections.get(0).getItems().get(0).getVisuals().getTitle();
            huluPage.getTypeCellLabelContains(titleFromCollection).click();
            Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), "Detail page did not open");
            Assert.assertTrue(detailsPage.getMediaTitle().equals(titleFromCollection),
                    titleFromCollection + " Content was not opened");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78296"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU, US})
    public void verifyRecommendationsIncludeHuluTitlesForStandaloneUser() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());

        List<Item> trendingTitlesFromApi = getExploreAPIItemsFromSet
                (CollectionConstant.getCollectionName(CollectionConstant.Collection.TRENDING), 30);
        if (trendingTitlesFromApi.isEmpty()) {
            throw new NoSuchElementException("Failed to get Trending collection titles from Explore API");
        }

        Optional<Item> matchingTitle = trendingTitlesFromApi.stream()
                .filter(trendingTitle -> getAvailableHuluTitlesForStandaloneUserFromApi().stream()
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

        homePage.swipeUpTillCollectionCompletelyVisible(CollectionConstant.Collection.TRENDING, 10);
        homePage.swipeInContainerTillElementIsPresent(
                homePage.getCollection(CollectionConstant.Collection.TRENDING),
                huluTitleCell,
                20,
                Direction.LEFT);
        Assert.assertTrue(huluTitleCell.isElementPresent(),
                "Hulu title cell was not present under Trending collection UI");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74637"})
    @Test(groups = {TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyHuluCollectionPagesNetworkPageUI() {
        List<String> networkLogos = new ArrayList<String>(
                Arrays.asList("A&E", "ABC", "ABC News", "Adult Swim", "Andscape", "Aniplex", "BBC Studios",
                        "Cartoon Network", "CBS", "Discovery", "Disney XD", "FOX", "Freeform", "FX", "FYI", "HGTV",
                        "Hulu Original Series", "Lifetime", "Lionsgate", "LMN", "Magnolia", "Moonbug Entertainment ",
                        "MTV", "National Geographic", "Nickelodeon", "Saban Films", "Samuel Goldwyn Films",
                        "Searchlight Pictures", "Paramount+", "Sony Pictures Television", "The HISTORY Channel",
                        "TLC", "TV Land", "Twentieth Century Studios", "Vertical Entertainment", "Warner Bros"));
        double imageSimilarityPercentageThreshold = 90.0;

        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand logo is not expanded");
        sa.assertTrue(huluPage.isBackButtonPresent(), "Back button is not present");
        sa.assertTrue(huluPage.isArtworkBackgroundPresent(), "Artwork images is not present");
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), STUDIOS_AND_NETWORKS_NOT_DISPLAYED);

        networkLogos.forEach(item -> {
            sa.assertTrue(huluPage.isNetworkLogoPresent(item), String.format("%s Network logo is not present", item));
            huluPage.clickOnNetworkLogo(item);
            sa.assertTrue(homePage.isNetworkLogoImageVisible(item), "Network logo page are not present");
            pause(3);
            String s3BucketPath = buildS3BucketPath(String.format("%s.png", item.replace(' ', '_')), "hulu-network-logos");
            File srcFile = homePage.getNetworkLogoImage(item).getElement().getScreenshotAs(OutputType.FILE);
            ImagesRequestS3 imagesComparisonRequest = new ImagesRequestS3(srcFile.getName(), FileUtil.encodeBase64File(srcFile), s3BucketPath);
            ImagesResponse360 imagesResponse360 = ALICE_API_MANAGER.compareImages360S3(imagesComparisonRequest);
            JSONObject jsonResponse = new JSONObject(imagesResponse360.getData().toString());
            LOGGER.info("Raw JSON response: {}", jsonResponse);
            double imageSimilarityPercentage = imagesResponse360.getSummary().getImageSimilarityPercentage();

            LOGGER.info("Similarity Percentage is: {}", imageSimilarityPercentage);

            sa.assertTrue(
                    imageSimilarityPercentage >= imageSimilarityPercentageThreshold,
                    String.format("Similarity Percentage score was %,.2f or lower in %s Network logo {%,.2f}.", imageSimilarityPercentageThreshold, item, imageSimilarityPercentage));
            huluPage.clickOnNetworkBackButton();
            sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), STUDIOS_AND_NETWORKS_NOT_DISPLAYED);
        });

        sa.assertAll();
    }

    private void addContentInContinueWatching(String url, int scrubPercentage) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        launchDeeplink(url);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(scrubPercentage);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        detailsPage.waitForDetailsPageToOpen();
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
    }

    private void addContentInContinueWatchingWithExpectedRemainingTime(String url, int expectedRemainingTime) {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        launchDeeplink(url);
        videoPlayer.waitForVideoToStart();
        videoPlayer.waitUntilRemainingTimeLessThan(SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, expectedRemainingTime);
        videoPlayer.clickBackButton();
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
    }

    private List<Item> getItemsFromCollection(CollectionConstant.Collection collection, int titlesLimit) {
        List<Item> continueWatchingTitlesFromApi = getExploreAPIItemsFromSet
                (CollectionConstant.getCollectionName(collection), titlesLimit);
        Assert.assertNotNull(continueWatchingTitlesFromApi,
                String.format("No items for '%s' collection were fetched from Explore API", collection.name()));
        return continueWatchingTitlesFromApi;
    }

    protected ArrayList<String> getExpectedBrand() {
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add("Disney");
        contentList.add("Pixar");
        contentList.add("Marvel");
        contentList.add("Star Wars");
        contentList.add("National Geographic");
        contentList.add("STAR");
        return contentList;
    }

    private void verifyNetworkLogoValues(SoftAssert sa, DisneyPlusHuluIOSPageBase huluPage) {
        try {
            String collection = CollectionConstant.getCollectionTitle(STUDIOS_AND_NETWORKS);
            ArrayList<Item> logoCollection = getHuluAPIPage(HULU_PAGE.getEntityId()).stream()
                    .filter(container -> container.getVisuals().getName().equals(collection))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(collection + "container not present in API response"))
                    .getItems();
            for (Item item : logoCollection) {
                String logoTitle = item.getVisuals().getTitle();
                sa.assertTrue(huluPage.isNetworkLogoPresent(logoTitle),
                        String.format("%s Network logo is not present", logoTitle));
            }
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void goToFirstCollectionTitle(DisneyPlusHomeIOSPageBase homePage) {
        String collectionID, contentTitle;
        ArrayList<Container> collections = getDisneyAPIPage(HOME_PAGE.getEntityId(),
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage());
        if (collections.size() < 3) {
            throw new ArrayIndexOutOfBoundsException("Explore API did not return any collection titles");
        }
        collectionID = collections.get(2).getId();
        contentTitle = collections.get(2).getItems().get(0).getVisuals().getTitle();
        if(contentTitle == null) {
            throw new SkipException("Collection title returned null from Explore API, skipping test");
        }
        ExtendedWebElement contentTitleElement = homePage.getElementTypeCellByLabel(contentTitle);
        swipe(homePage.getDynamicAccessibilityId(collectionID));
        if (contentTitleElement.isPresent()) {
            contentTitleElement.click();
        } else {
            swipeInContainerTillElementIsPresent(homePage.getCollection(collectionID), contentTitleElement,
                    30,
                    Direction.LEFT);
            contentTitleElement.click();
        }
    }
}
