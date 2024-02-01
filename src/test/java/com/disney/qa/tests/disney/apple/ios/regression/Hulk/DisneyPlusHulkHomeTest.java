package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
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

    @DataProvider(name = "huluDeepLinks")
    public Object[][] huluDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_hulu_abc_network_deeplink")},
                {R.TESTDATA.get("disney_prod_hulu_abc_network_language_deeplink")}
        };
    }

    @DataProvider(name = "huluNetworkDeepLinks")
    public Object[][] huluNetworkDeepLinks() {
        return new Object[][]{{"A&E", R.TESTDATA.get("disney_prod_hulu_a&e_network_deeplink")},
                {"ABC", R.TESTDATA.get("disney_prod_hulu_abc_network_deeplink")},
                {"ABC News", R.TESTDATA.get("disney_prod_hulu_abc_news_network_deeplink")},
                {"Adult Swim", R.TESTDATA.get("disney_prod_hulu_adult_swim_network_deeplink")},
                {"Andscape", R.TESTDATA.get("disney_prod_hulu_andscape_network_deeplink")},
                {"Aniplex", R.TESTDATA.get("disney_prod_hulu_aniplex_network_deeplink")},
                {"BBC Studios", R.TESTDATA.get("disney_prod_hulu_bbc_network_deeplink")},
                {"Cartoon Network", R.TESTDATA.get("disney_prod_hulu_cn_network_deeplink")},
                {"CBS", R.TESTDATA.get("disney_prod_hulu_cbs_network_deeplink")},
                {"Discovery", R.TESTDATA.get("disney_prod_hulu_discovery_network_deeplink")},
                {"Disney XD", R.TESTDATA.get("disney_prod_hulu_disney_xd_network_deeplink")},
                {"FOX", R.TESTDATA.get("disney_prod_hulu_fox_network_deeplink")},
                {"Freeform", R.TESTDATA.get("disney_prod_hulu_freeform_network_deeplink")},
                {"FX", R.TESTDATA.get("disney_prod_hulu_fx_network_deeplink")},
                {"FYI", R.TESTDATA.get("disney_prod_hulu_fyi_network_deeplink")},
                {"HGTV", R.TESTDATA.get("disney_prod_hulu_hgtv_network_deeplink")},
                {"Hulu Original Series", R.TESTDATA.get("disney_prod_hulu_hulu_originals_network_deeplink")},
                {"Lifetime", R.TESTDATA.get("disney_prod_hulu_lifetime_network_deeplink")},
                {"Lionsgate", R.TESTDATA.get("disney_prod_hulu_lionsgate_network_deeplink")},
                {"LMN", R.TESTDATA.get("disney_prod_hulu_lmn_network_deeplink")},
                {"Magnolia", R.TESTDATA.get("disney_prod_hulu_magnolia_network_deeplink")},
                {"Moonbug Entertainment ", R.TESTDATA.get("disney_prod_hulu_moonbug_network_deeplink")},
                {"MTV", R.TESTDATA.get("disney_prod_hulu_mtv_network_deeplink")},
                {"National Geographic", R.TESTDATA.get("disney_prod_hulu_national_geographic_network_deeplink")},
                {"Nickelodeon", R.TESTDATA.get("disney_prod_hulu_nickelodeon_network_deeplink")},
                {"Saban Films", R.TESTDATA.get("disney_prod_hulu_saban_films_network_deeplink")},
                {"Samuel Goldwyn Films", R.TESTDATA.get("disney_prod_hulu_samuel_goldwyn_network_deeplink")},
                {"Searchlight Pictures", R.TESTDATA.get("disney_prod_hulu_searchlight_network_deeplink")},
                {"Paramount+", R.TESTDATA.get("disney_prod_hulu_paramount_network_deeplink")},
                {"Sony Pictures Television", R.TESTDATA.get("disney_prod_hulu_sony_network_deeplink")},
                {"The HISTORY Channel", R.TESTDATA.get("disney_prod_hulu_history_network_deeplink")},
                {"TLC", R.TESTDATA.get("disney_prod_hulu_tlc_network_deeplink")},
                {"TV Land", R.TESTDATA.get("disney_prod_hulu_tvland_network_deeplink")},
                {"Twentieth Century Studios", R.TESTDATA.get("disney_prod_hulu_twenty_century_network_deeplink")},
                {"Vertical Entertainment", R.TESTDATA.get("disney_prod_hulu_vertical_network_deeplink")},
                {"Warner Bros", R.TESTDATA.get("disney_prod_hulu_wb_network_deeplink")},
        };
    }

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
    @Test(description = "New URL Structure - Hulu Hub - Network Page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION}, dataProvider = "huluDeepLinks")
    public void verifyHulkDeepLinkNewURLStructure(String deepLink) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, deepLink, 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.isNetworkLogoImageVisible(), "Network logo page are not present");
        pause(5);
        // Get Network logo by deeplink access
        BufferedImage networkLogoImageSelected = getElementImage(homePage.getNetworkLogoImage());
        homePage.clickHomeIcon();

        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network and studios section are not present");
        huluPage.clickOnNetworkLogo("ABC");

        sa.assertTrue(homePage.isNetworkLogoImageVisible(), "Network logo page are not present");
        pause(5);
        // Get Network logo by app navigation
        BufferedImage networkLogoImage = getElementImage(homePage.getNetworkLogoImage());

        sa.assertTrue(areImagesTheSame(networkLogoImageSelected, networkLogoImage, 10),
                "The user doesn't land on the given Network page");

        sa.assertAll();
    }

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74598"})
    @Test(description = "Validate of the UI and functional items of the Collection and Network page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION}, dataProvider = "huluNetworkDeepLinks")
    public void verifyHulkCollectionPagesNetworkPageUI(String network, String deepLink) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, deepLink, 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.isNetworkLogoImageVisible(), "Network logo page are not present");
        pause(3);
        BufferedImage networkLogoImageSelected = getElementImage(homePage.getNetworkLogoImage());
        homePage.clickHomeIcon();

        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand logo is not expanded");
        sa.assertTrue(huluPage.isBackButtonPresent(), "Back button is not present");
        sa.assertTrue(huluPage.isArtworkBackgroundPresent(), "Artwork images is not present");
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network and studios section are not present");

        sa.assertTrue(huluPage.isNetworkLogoPresent(network), String.format("%s Network logo is not present", network));
        huluPage.clickOnNetworkLogo(network);

        sa.assertTrue(homePage.isNetworkLogoImageVisible(), "Network logo page are not present");
        pause(3);
        BufferedImage networkLogoImage = getElementImage(homePage.getNetworkLogoImage());

        sa.assertTrue(areImagesTheSame(networkLogoImageSelected, networkLogoImage, 10),
                String.format("%s Network logo is not the expected", network));
        sa.assertTrue(huluPage.isNetworkBackButtonPresent(), "Back button is not present");
        huluPage.clickOnNetworkBackButton();

        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network and studios section are not present");
        sa.assertAll();
    }
}
