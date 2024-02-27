package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusHomeTest extends DisneyBaseTest {

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
