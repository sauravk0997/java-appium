package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisneyPlusHulkHomeTest extends DisneyBaseTest {

    private List<String> networkLogos = new ArrayList<String>(
            Arrays.asList("A&E", "ABC", "ABC News", "Adult Swim", "Andscape", "Aniplex", "BBC Studios",
                    "Cartoon Network", "CBS", "Discovery", "Disney XD", "FOX", "Freeform", "FX", "FYI", "HGTV",
                    "Hulu Original Series", "Lifetime", "Lionsgate", "LMN", "Magnolia", "Moonbug Entertainment ",
                    "MTV", "National Geographic", "Nickelodeon", "Saban Films", "Samuel Goldwyn Films",
                    "Searchlight Pictures", "Paramount+", "Sony Pictures Television", "The HISTORY Channel",
                    "TLC", "TV Land", "Twentieth Century Studios", "Vertical Entertainment", "Warner Bros"));

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74645"})
    @Test(description = "Brand Row Set includes Hulu Tile", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHuluBrandTileOnHome() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        sa.assertTrue(homePage.isHuluTileVisible(), "Hulu tile is not visible on home page");
        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.isOpened(), "Hulu page didn't open after navigating via brand tile");
        sa.assertAll();
    }

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74814", "XMOBQA-74829"})
    @Test(description = "Validate of the UI and functional items of the Hulu brand page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHuluBrandPage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        sa.assertTrue(homePage.isHuluTileVisible(), "Hulu tile is not visible on home page");
        homePage.tapHuluBrandTile();

        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand logo is not expanded");
        sa.assertTrue(huluPage.isBackButtonPresent(), "Back button is not present");
        sa.assertTrue(huluPage.isArtworkBackgroundPresent(), "Artwork images is not present");

        huluPage.swipeInHuluBrandPage(Direction.UP);
        sa.assertTrue(huluPage.isHuluBrandImageCollapsed(), "Hulu brand logo is not collapsed");
        sa.assertTrue(huluPage.isBackButtonPresent(), "Back button is not present");

        huluPage.swipeInHuluBrandPage(Direction.DOWN);
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand logo is not expanded");

        huluPage.clickOnCollectionBackButton();
        sa.assertTrue(homePage.isHuluTileVisible(), "Hulu tile is not visible on home page");

        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.validateScrollingInHuluCollection(), "User cannot scroll horizontally");
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network logos are not present");

        networkLogos.forEach(item ->
                sa.assertTrue(huluPage.isNetworkLogoPresent(item), String.format("%s Network logo is not present", item)));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75264"})
    @Test(description = "New URL Structure - Hulu Hub - Network Page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkDeepLinkNewURLStructure() throws IOException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_hulu_network_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.isNetworkLogoImageVisible(), "Network logo page are not present");
        // Get Network logo by deeplink access
        BufferedImage networkLogoImageSelected = getElementImage(homePage.getNetworkLogoImage());
        homePage.clickHomeIcon();

        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network and studios section are not present");
        huluPage.clickOnNetworkLogo("ABC");

        sa.assertTrue(homePage.isNetworkLogoImageVisible(), "Network logo page are not present");
        // Get Network logo by app navigation
        BufferedImage networkLogoImage = getElementImage(homePage.getNetworkLogoImage());

        sa.assertTrue(areImagesTheSame(networkLogoImageSelected, networkLogoImage, 10),
                "The user doesn't land on the given Network page");

        sa.assertAll();
    }
}
