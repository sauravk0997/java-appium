package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.common.constant.*;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;

public class DisneyPlusBrandsTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67373"})
    @Test(groups = {TestGroup.HOME, TestGroup.PRE_CONFIGURATION})
    public void verifyBrandUI() {
        DisneyPlusBrandIOSPageBase.Brand brand = DisneyPlusBrandIOSPageBase.Brand.DISNEY;
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        sa.assertTrue(homePage.getBrandCell(brandPage.getBrand(brand)).isPresent(),
                "The following brand tile was not present: " + brandPage.getBrand(brand));

        homePage.clickOnBrandCell(brandPage.getBrand(brand));
        sa.assertTrue(brandPage.isOpened(),
                brandPage.getBrand(brand) + "Brand page did not open");
        sa.assertTrue(brandPage.getBrandLogoImage().isPresent(),
                brandPage.getBrand(brand) + "Brand logo image is not present");
        sa.assertTrue(brandPage.getBrandFeaturedImage().isPresent(),
                brandPage.getBrand(brand) + "Brand featured image is not present");

        //Capture top of brand page
        BufferedImage topOfBrandPage = getCurrentScreenView();

        brandPage.validateSwipeNavigation(CollectionConstant.Collection.BRANDS_DISNEY_ORIGINALS, sa);

        //Capture end of brand page
        swipeInContainer(null, Direction.UP, 5, 500);
        BufferedImage closeToEndOfBrandPage = getCurrentScreenView();
        sa.assertTrue(brandPage.areImagesDifferent(topOfBrandPage, closeToEndOfBrandPage),
                "Top of brand page and close to end of brand page are the same");
        sa.assertFalse(brandPage.getBrandLogoImage().isPresent(),
                "Brand logo image was not suppressed after scrolling down page");
        sa.assertTrue(brandPage.getBackButton().isPresent(),
                "Back button was not found when at bottom of brand page");

        brandPage.swipePageTillElementPresent(brandPage.getBrandLogoImage(),
                5, null, Direction.DOWN, 500);
        sa.assertTrue(brandPage.getBrandLogoImage().isPresent(),
                brandPage.getBrand(brand) + "Brand logo image is not present");
        brandPage.tapBackButton();
        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(),
                "Home page didn't open after clicking the back button on the Brand page");
        sa.assertAll();
    }
}
