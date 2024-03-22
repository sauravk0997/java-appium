package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;

public class DisneyPlusBrandsTest extends DisneyBaseTest {
    @BeforeTest(alwaysRun = true)
    private void setUp() {
        initialSetup("US", "en");
        handleAlert();
        setAppToHomeScreen(getAccount());
        initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
    }

    @DataProvider(name = "brands")
    public Object[][] brandTypes() {
        return new Object[][]{
                {DisneyPlusBrandIOSPageBase.Brand.DISNEY},
                {DisneyPlusBrandIOSPageBase.Brand.PIXAR},
                {DisneyPlusBrandIOSPageBase.Brand.MARVEL},
                {DisneyPlusBrandIOSPageBase.Brand.STAR_WARS},
                {DisneyPlusBrandIOSPageBase.Brand.NATIONAL_GEOGRAPHIC}
        };
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67373"})
    @Test(dataProvider = "brands", description = "Home - Brands UI", groups = {"Home", TestGroup.PRE_CONFIGURATION})
    public void verifyBrandUI(DisneyPlusBrandIOSPageBase.Brand brand) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);

        if (!homePage.isOpened()) {
            brandPage.getBackButton().click();
        }
        sa.assertTrue(homePage.getDynamicCellByLabel(brandPage.getBrand(brand)).isPresent(),
                "The following brand tile was not present: " + brandPage.getBrand(brand));

        homePage.clickBrandTile(brand);
        sa.assertTrue(brandPage.isOpened(), brandPage.getBrand(brand) + "Brand page did not open.");
        sa.assertTrue(brandPage.getBrandLogoImage().isPresent(), brandPage.getBrand(brand) + "Brand logo image is not present.");
        sa.assertTrue(brandPage.getBrandFeaturedImage().isPresent(), brandPage.getBrand(brand) + "Brand featured image is not present");

        //Capture top of brand page
        BufferedImage topOfBrandPage = getCurrentScreenView();

        brandPage.validateBrand(brand, sa);

        //Capture end of brand page
        swipeInContainer(null, Direction.UP, 5, 500);
        BufferedImage closeToEndOfBrandPage = getCurrentScreenView();
        sa.assertTrue(brandPage.areImagesDifferent(topOfBrandPage, closeToEndOfBrandPage), "Top of brand page and close to end of brand page are the same.");
        sa.assertFalse(brandPage.getBrandLogoImage().isPresent(), "Brand logo image was not suppressed after scrolling down page.");
        sa.assertTrue(brandPage.getBackButton().isPresent(), "Back button was not found when at bottom of brand page");

        brandPage.swipePageTillElementPresent(brandPage.getBrandLogoImage(), 5, null, Direction.DOWN, 500);
        sa.assertTrue(brandPage.getBrandLogoImage().isPresent(), brandPage.getBrand(brand) + "Brand logo image is not present.");
        brandPage.getBackButton().click();
        sa.assertTrue(homePage.isHomePageLoadPresent(), "Brand page was not closed and returned to home page.");;
        sa.assertAll();
    }
}
