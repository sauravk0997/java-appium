package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.List;

import static com.disney.qa.common.constant.RatingConstant.SINGAPORE;
import static com.disney.qa.tests.disney.apple.ios.regression.ratings.DisneyPlusRatingsBase.SINGAPORE_LANG;

public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String RECOMMENDED_FOR_YOU = "Recommended For You";
    private static final String DISNEY_PLUS = "Disney Plus";
    private static final String HOME_PAGE_ERROR = "Home page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67371"})
    @Test(description = "Home - Home Screen UI Elements", groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION})
    public void verifyHomeUIElements() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        //Validate top of home
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found.");
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(), "'Recommend For You' collection was not found.");
        homePage.swipeLeftInCollectionNumOfTimes(5, CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
        BufferedImage recommendedForYouLastTileInView = getElementImage(homePage.getCollection(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));
        homePage.swipeRightInCollectionNumOfTimes(5, CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
        BufferedImage recommendedForYouFirstTileInView = getElementImage(homePage.getCollection(CollectionConstant.Collection.RECOMMENDED_FOR_YOU));
        sa.assertTrue(areImagesDifferent(recommendedForYouFirstTileInView, recommendedForYouLastTileInView), "Recommended For You first tile in view and last tile in view images are the same.");

        BufferedImage topOfHome = getCurrentScreenView();

        //Capture bottom of home
        swipeInContainer(null, Direction.UP, 5, 500);
        BufferedImage closeToBottomOfHome = getCurrentScreenView();

        //Validate back at top of home
        swipePageTillElementPresent(homePage.getImageLabelContains(DISNEY_PLUS), 10, null, Direction.DOWN, 300);
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(), "'Recommend For You' collection was not found.");
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found after return to top of home.");

        //Validate images are different
        sa.assertTrue(areImagesDifferent(topOfHome, closeToBottomOfHome),
                "Top of home image is the same as bottom of home image.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67377"})
    @Test(description = "Home - Recommended for You", groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION})
    public void verifyRecommendedForYouContainer() {
        int limit = 30;
        String recommendedContainerNotFound = "Recommended For You container was not found";
        String recommendedHeaderNotFound = "Recommended For You Header was not found";
        CollectionConstant.Collection collection = CollectionConstant.Collection.RECOMMENDED_FOR_YOU;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyAccount account = getAccount();
        setAppToHomeScreen(account);

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_ERROR);
        sa.assertTrue(homePage.isCollectionPresent(collection), recommendedContainerNotFound);
        sa.assertTrue(homePage.isCollectionTitlePresent(collection), recommendedHeaderNotFound);

        List<String> recommendationTitlesFromApi = getContainerTitlesFromApi
                (CollectionConstant.getCollectionName(collection), limit);

        int size = recommendationTitlesFromApi.size();
        String firstCellTitle = homePage.getFirstCellTitleFromContainer(collection).split(",")[0];
        ExtendedWebElement firstTitle = homePage.getCellElementFromContainer(collection, recommendationTitlesFromApi.get(0));
        ExtendedWebElement lastTitle = homePage.getCellElementFromContainer(collection, recommendationTitlesFromApi.get(size-1));
        Assert.assertTrue(firstCellTitle.equals(recommendationTitlesFromApi.get(0)), "UI title value not matched with API title value");

        homePage.swipeInContainerTillElementIsPresent(homePage.getCollection(collection), lastTitle, 30, Direction.LEFT );
        Assert.assertTrue(lastTitle.isPresent(), "User is not able to swipe through end of container");

        homePage.swipeInContainerTillElementIsPresent(homePage.getCollection(collection), firstTitle, 30, Direction.RIGHT);
        Assert.assertTrue(firstTitle.isPresent(), "User is not able to swipe to the begining of container");

        firstTitle.click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page was not opened");
        sa.assertTrue(detailsPage.getMediaTitle().equals(firstCellTitle), "Content title not matched");
        detailsPage.clickCloseButton();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_ERROR);
        sa.assertTrue(homePage.isCollectionPresent(collection), recommendedContainerNotFound);
        sa.assertTrue(homePage.isCollectionTitlePresent(collection), recommendedHeaderNotFound);
        sa.assertTrue(firstTitle.isPresent(), "Same position was not retained in Recommend for Your container after coming back from detail page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69549"})
    @Test(groups = {TestGroup.HOME})
    public void verifyRatingRestrictionTravelingMessage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_STARHUB_SG_STANDALONE, SINGAPORE, SINGAPORE_LANG));
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());

        Assert.assertTrue(homePage.isTravelAlertTitlePresent(), "Travel alert title was not present");
        Assert.assertTrue(homePage.isTravelAlertBodyPresent(), "Travel alert body was not present");
        Assert.assertTrue(homePage.getTravelAlertOk().isPresent(), "Travel alert ok button was not present");
        homePage.getTravelAlertOk().click();
        Assert.assertFalse(homePage.isTravelAlertTitlePresent(), "Travel alert was not dismissed.");
    }
}
