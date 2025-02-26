package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.*;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusAppleTVHomeTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121503"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULK, US})
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
    @Test(groups = {TestGroup.HOME, TestGroup.HULK, US})
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
    @Test(groups = {TestGroup.HOME, TestGroup.HULK, US})
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
    @Test(groups = {TestGroup.HOME, TestGroup.HULK, US})
    public void verifyRecommendationsIncludeHuluTitlesForStandaloneUser() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        logIn(getAccount());
        homePage.waitForHomePageToOpen();

        List<Item> availableHuluTitlesForStandaloneUserFromApi = getAvailableHuluTitlesForStandaloneUserFromApi();
        List<Item> trendingTitlesFromApi = getExploreAPIItemsFromSet
                (CollectionConstant.getCollectionName(CollectionConstant.Collection.TRENDING), 30);
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
                CollectionConstant.getCollectionName(CollectionConstant.Collection.TRENDING), 15);
        homePage.moveRightUntilElementIsFocused(huluTitleCell, 30);
        Assert.assertTrue(huluTitleCell.isElementPresent(),
                "Hulu title cell was not present under Trending collection UI");
    }
}
