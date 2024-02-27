package com.disney.qa.tests.disney.apple.ios.regression.home;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
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

import static com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest.SUB_VERSION;

public class DisneyPlusHomeTest extends DisneyBaseTest {
    private static final String RECOMMENDED_FOR_YOU = "Recommended For You";
    private static final String NEW_TO_DISNEY = "New To Disney";
    private static final String COLLECTIONS = "Collections";
    private static final String DISNEY_PLUS = "Disney Plus";
    private static final String PERSONALIZED_COLLECTION = "PersonalizedCollection";

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
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        setAppToHomeScreen(entitledUser);

        //Validate top of home
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found.");
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(), "'Recommend For You' collection was not found.");

        BufferedImage topOfHome = getCurrentScreenView();

        //Validate bottom of home
        swipePageTillElementPresent(homePage.getTypeOtherContainsName(COLLECTIONS), 10,null, Direction.UP, 300);
        sa.assertTrue(homePage.getTypeOtherContainsName(COLLECTIONS).isPresent(), "'Collections' container was not found.");
        sa.assertFalse(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "Disney Plus image was found after swiping up.");

        BufferedImage bottomOfHomeWithFirstCollectionsTile = getCurrentScreenView();
        sa.assertTrue(homePage.isAllCollectionsPresent(), "`All Collections` was not found after swiping to end of Collection");

        BufferedImage bottomOfHomeWithLastCollectionsTile = getCurrentScreenView();
        sa.assertTrue(homePage.isBlackStoriesCollectionPresent(),
                "`Black Stories Collection` was not found after swiping to beginning of Collection");

        //Validate back at top of home
        swipePageTillElementPresent(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU), 10,null, Direction.DOWN, 300);
        sa.assertTrue(homePage.getTypeOtherContainsName(RECOMMENDED_FOR_YOU).isPresent(), "'Recommend For You' collection was not found.");
        sa.assertTrue(homePage.getImageLabelContains(DISNEY_PLUS).isPresent(), "`Disney Plus` image was not found after return to top of home.");

        //Validate images are different
        sa.assertTrue(areImagesDifferent(bottomOfHomeWithFirstCollectionsTile, bottomOfHomeWithLastCollectionsTile),
                "Bottom of image with first `collections` tile is the same bottom of home with last 'collections' tile");
        sa.assertTrue(areImagesDifferent(topOfHome, bottomOfHomeWithFirstCollectionsTile),
                "Top of home image is the same as bottom of home image.");
        sa.assertAll();
    }
}
