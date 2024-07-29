package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.HULU_PAGE;
import static com.disney.qa.common.constant.IConstantHelper.CONTENT_ENTITLEMENT_DISNEY;
import static com.disney.qa.common.constant.IConstantHelper.CONTENT_ENTITLEMENT_HULU;

public class DisneyPlusHulkHomeTest extends DisneyBaseTest {

    String UNENTITLED_SERIES_ID = "entity-7840bf30-f440-48d4-bf81-55d8cb24457a";
    String PLATFORM = "apple";
    String ENVIRONMENT = "PROD";

    private List<String> networkLogos = new ArrayList<String>(
            Arrays.asList("A&E", "ABC", "ABC News", "Andscape", "Disney XD", "FOX", "Freeform", "FX", "FYI",
                    "Hulu Original Series", "Lifetime", "LMN", "MTV", "National Geographic", "Nickelodeon",
                    "Searchlight Pictures", "The HISTORY Channel", "Twentieth Century Studios"));

    @DataProvider(name = "huluUnavailableDeepLinks")
    public Object[][] huluUnavailableDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_hulu_unavailable_deeplink")},
                {R.TESTDATA.get("disney_prod_hulu_unavailable_language_deeplink")}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74564"})
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74829"})
    @Test(description = "Validate of the UI and functional items of the Hulu brand page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHuluBrandPage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        homePage.waitForPresenceOfAnElement(homePage.getElementTypeCellByLabel("Hulu"));
        Assert.assertTrue(homePage.isHuluTileVisible(), "Hulu tile is not visible on home page");
        homePage.tapHuluBrandTile();

        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand logo is not expanded");
        sa.assertTrue(huluPage.isBackButtonOnScreen(), "Back button is not present"); // corregir
        sa.assertTrue(huluPage.isArtworkBackgroundPresent(), "Artwork images is not present");

        huluPage.swipeInHuluBrandPage(Direction.UP);
        sa.assertTrue(huluPage.isHuluBrandImageCollapsed(), "Hulu brand logo is not collapsed");
        sa.assertTrue(huluPage.isBackButtonOnScreen(), "Back button is not present"); // corregir

        huluPage.swipeInHuluBrandPage(Direction.DOWN);
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand logo is not expanded");

        huluPage.tapBackButton();
        sa.assertTrue(homePage.isHuluTileVisible(), "Hulu tile is not visible on home page");

        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.validateScrollingInHuluCollection(), "User cannot scroll horizontally");
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network logos are not present");

        networkLogos.forEach(item ->
                sa.assertTrue(huluPage.isNetworkLogoPresent(item), String.format("%s Network logo is not present", item)));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74642"})
    @Test(description = "Hulk - Hulu Hub Page - Collections", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHuluPageContent() throws URISyntaxException, JsonProcessingException {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        homePage.waitForPresenceOfAnElement(homePage.getElementTypeCellByLabel("Hulu"));
        Assert.assertTrue(homePage.isHuluTileVisible(), "Hulu tile is not visible on home page");
        homePage.tapHuluBrandTile();

        //To get the collections details of Hulu from API
        ArrayList<Container> collections = getHuluAPIPage(HULU_PAGE.getEntityId());
        //Click any title from collection
        try {
            String titleFromCollection = collections.get(0).getItems().get(0).getVisuals().getTitle();
            huluPage.getTypeCellLabelContains(titleFromCollection).click();
            Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), "Detail page did not open");
            Assert.assertTrue(detailsPage.getMediaTitle().equals(titleFromCollection), titleFromCollection + " Content was not opened");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75209"})
    @Test(description = "New URL Structure - Hulu Hub - Not Entitled For Hulu - Error Message", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION}, dataProvider = "huluUnavailableDeepLinks", enabled = false)
    public void verifyHulkDeepLinkNewURLStructureNotEntitledHulu(String deepLink) throws URISyntaxException, JsonProcessingException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, deepLink, 10);
        homePage.clickOpenButton();

        sa.assertTrue(detailsPage.isContentAvailableWithHuluSubscriptionPresent(getAccount(), ENVIRONMENT, PLATFORM, UNENTITLED_SERIES_ID),
                "\"This content requires a Hulu subscription.\" message is displayed");
        sa.assertFalse(detailsPage.getExtrasTab().isPresent(SHORT_TIMEOUT), "Extra tab is found.");
        sa.assertFalse(detailsPage.getSuggestedTab().isPresent(SHORT_TIMEOUT), "Suggested tab is found.");
        sa.assertFalse(detailsPage.getDetailsTab().isPresent(SHORT_TIMEOUT), "Details tab is found.");
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(SHORT_TIMEOUT), "Watchlist CTA found.");
        sa.assertFalse(detailsPage.getTrailerButton().isPresent(SHORT_TIMEOUT), "Trailer CTA found.");
        sa.assertFalse(detailsPage.getPlayButton().isPresent(SHORT_TIMEOUT), "Play CTA found.");

        sa.assertAll();
    }
}
