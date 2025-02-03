package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.*;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.*;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89521"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifyHomeScreenLayout() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        logInWithoutHomeCheck(getAccount());
        collapseGlobalNav();

        Assert.assertTrue(homePage.isOpened(),
                "Home page did not launch for single profile user after logging in");

        //stop hero carousel
        homePage.moveRight(2, 2);
        homePage.clickDown();

        List<Container> homeCollections = getCollectionsHome();

        verifyBrandDetails(homeCollections.get(1).getId(), homePage, sa);

        homePage.moveDown(1, 1);
        homePage.moveLeft(4, 1);
        homePage.moveRight(2, 1);

        verifyHomeCollectionsAndContent(homeCollections, sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121503"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU_HUB, US})
    public void verifyStandaloneESPNAndHuluBrandTiles() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());

        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY);
        logIn(basicAccount);

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
                "Hulu Hub page did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121502"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU_HUB, US})
    public void verifyHulkUpsellStandaloneUserInEligible() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();

        String lockedHuluContentCollectionName =
                CollectionConstant.getCollectionName(CollectionConstant.Collection.UNLOCK_TO_STREAM_MORE_HULU);
        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM);
        logIn(basicAccount);

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
    @Test(groups = {TestGroup.HOME, TestGroup.HULU_HUB, US})
    public void verifyHuluHubPageUI() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        SoftAssert sa = new SoftAssert();

        DisneyAccount bundleAccount = createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE);
        logIn(bundleAccount);

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
        sa.assertTrue(brandPage.getBrandLogoImage().isPresent(),
                "Hulu logo was not present");
        sa.assertTrue(brandPage.getBrandFeaturedImage().isPresent(),
                "Hulu background artwork was not present");

        brandPage.moveDownUntilCollectionContentIsFocused(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.STUDIOS_AND_NETWORKS), 10);
        Assert.assertTrue(brandPage.getCollection(CollectionConstant.Collection.STUDIOS_AND_NETWORKS).isPresent(),
                "Studios and Networks collection was not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-122533"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULU_HUB_2, US})
    public void verifyRecommendationsIncludeHuluTitlesForStandaloneUser() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        DisneyAccount account = getAccount();
        account.setEmail("alekhya.rallapalli+p2.standalone2@disney.com");
        account.setUserPass("Test123!");
        logIn(account);

        homePage.waitForHomePageToOpen();

        ExtendedWebElement huluTitleCell = homePage.getCellElementFromContainer(
                CollectionConstant.Collection.TRENDING, "Hulu Original Series");

        homePage.moveDownUntilCollectionContentIsFocused(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.TRENDING), 15);
        homePage.moveRightUntilElementIsFocused(huluTitleCell, 30);
        Assert.assertTrue(huluTitleCell.isElementPresent(),
                "Hulu title cell was not present under Trending collection");
    }

    private List<Container> getCollectionsHome() {
            return getDisneyAPIPage(HOME_PAGE.getEntityId(),
                    getLocalizationUtils().getLocale(),
                    getLocalizationUtils().getUserLanguage());
    }

    private void verifyBrandDetails(String brandCollectionID, DisneyPlusAppleTVHomePage homePage, SoftAssert sa) {
        List<Item> brandsCollection = getExploreAPIItemsFromSet(brandCollectionID, 10);
        brandsCollection.forEach(item -> {
            sa.assertTrue(homePage.isFocused(homePage.getTypeCellNameContains(item.getVisuals().getTitle())),
                    "The following brand tile was not focused: " + item);
            homePage.moveRight(1, 1);
        });
    }

    private void verifyHomeCollectionsAndContent(List<Container> homeCollections, SoftAssert sa) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        String homeShelf = "Home";
        String watchlistShelf = "My Watchlist";

        //This removes first 2 collections from the home collection
        homeCollections.subList(0, 2).clear();

        LOGGER.info("Home collection size: {}", homeCollections.size());

        homeCollections.forEach(homeCollectionId -> {
            //Verify shelf title
            String shelfTitle = homeCollectionId.getVisuals().getName();
            if(!Arrays.asList(homeShelf,watchlistShelf).contains(shelfTitle)) {
                sa.assertTrue(homePage.getStaticTextByLabelContains(shelfTitle).isPresent(SHORT_TIMEOUT),
                        "Shelf title not found: " + shelfTitle);
            }
            if (!homeCollectionId.getItems().isEmpty()) {
                String firstContentTitle = homeCollectionId.getItems().get(0).getVisuals().getTitle();
                LOGGER.info("Content Title: {} for Shelf: {}", firstContentTitle, shelfTitle);
                //Verify content title
                sa.assertTrue(homePage.getTypeCellNameContains(firstContentTitle).isPresent(SHORT_TIMEOUT),
                        "Content title not found: " + firstContentTitle);
            }
            homePage.moveDown(1, 1);

            if (homePage.getTypeOtherContainsName("airingBadgeContainerView").isPresent(SHORT_TIMEOUT)) {
                homePage.moveDown(1, 1);
            }
        });
    }
}
