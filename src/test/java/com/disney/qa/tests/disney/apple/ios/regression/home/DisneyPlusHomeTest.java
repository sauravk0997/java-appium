package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.config.DisneyConfiguration;
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
    private static final String COLLECTIONS = "Collections";

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

        //Validate Recommended For You is present at beginning of home.
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeUp(2000);
        }
        sa.assertTrue(homePage.getTypeOtherContainsLabel(RECOMMENDED_FOR_YOU).isPresent(),
                "'Recommend For You' collection was not found.");
        BufferedImage topOfHome = getCurrentScreenView();

        //Get bottom of image with first `collections` tile
        BufferedImage bottomOfHomeWithFirstCollectionsTile = getCurrentScreenView();

        //Validate Collections is present after swiping to end of home.
        swipePageTillElementPresent(homePage.getTypeOtherContainsLabel(COLLECTIONS), 5,null, Direction.UP, 500);
        sa.assertTrue(homePage.getTypeOtherContainsLabel(COLLECTIONS).isPresent(), "'Collections' container was not found.");

        //Get bottom of home with last 'collections' tile
        BufferedImage bottomOfHomeWithLastCollectionsTile = getCurrentScreenView();

        //Validate swiping through Collections
        sa.assertTrue(homePage.isAllCollectionsPresent(), "`All Collections` was not found after swiping to end of Collection");
        sa.assertTrue(homePage.isBlackStoriesCollectionPresent(),
                "`Black Stories Collection` was not found after swiping to beginning of Collection");

        //Validate Recommended For You is present after swiping back to top of home.
        swipePageTillElementPresent(homePage.getTypeOtherContainsLabel(RECOMMENDED_FOR_YOU), 5,null, Direction.DOWN, 500);
        sa.assertTrue(homePage.getTypeOtherContainsLabel(RECOMMENDED_FOR_YOU).isPresent(),
                "'Recommend For You' collection was not found.");

        //Validate images are different
        sa.assertTrue(areImagesDifferent(bottomOfHomeWithFirstCollectionsTile, bottomOfHomeWithLastCollectionsTile),
                "Bottom of image with first `collections` tile is the same bottom of home with last 'collections' tile");
        sa.assertTrue(areImagesDifferent(topOfHome, bottomOfHomeWithLastCollectionsTile),
                "Top of home image is the same as bottom of home image.");
        sa.assertAll();
    }
}
