package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;

public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String RECOMMENDED_FOR_YOU = "Recommended For You";
    private static final String DISNEY_PLUS = "Disney Plus";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62276"})
    @Test(description = "Home - Deeplink", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyHomeDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_home_deeplink"), 10);
        homePage.clickOpenButton();
        Assert.assertTrue(homePage.isOpened(), "Home page did not open via deeplink.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67371"})
    @Test(description = "Home - Home Screen UI Elements", groups = {"Home", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67377"})
    @Test(description = "Home - Recommended for You", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyHomeRecommendedForYou() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        Assert.assertTrue(homePage.isOpened(), "Home page did not open.");
        sa.assertTrue(homePage.isRecommendedForYouContainerPresent(), "Recommended For You header was not found");

        String firstCellTitle = homePage.getFirstCellTitleFromRecommendedForYouContainer();
        homePage.swipeInRecommendedForYouContainer(Direction.LEFT);
        sa.assertFalse(homePage.getStaticCellByLabel(firstCellTitle).isPresent(), "User was not able to swipe in the Recommend for Your container");
        homePage.swipeInRecommendedForYouContainer(Direction.RIGHT);
        sa.assertTrue(homePage.getStaticCellByLabel(firstCellTitle).isPresent(), "User was not able to swipe back at beginning of the Recommend for Your container");

        homePage.getStaticCellByLabel(firstCellTitle).click();
        sa.assertTrue(detailsPage.isOpened(), "Detais Page was not opened");
        sa.assertTrue(detailsPage.getMediaTitle().equals(firstCellTitle), "Different details page opened");
        detailsPage.clickCloseButton();
        sa.assertTrue(homePage.isOpened(), "Home page did not open.");
        sa.assertTrue(homePage.isRecommendedForYouContainerPresent(), "Recommended For You header was not found");
        sa.assertTrue(homePage.getStaticCellByLabel(firstCellTitle).isPresent(), "Same position was not retained in Recommend for Your container after coming back from detail page");
        sa.assertAll();
    }
}
