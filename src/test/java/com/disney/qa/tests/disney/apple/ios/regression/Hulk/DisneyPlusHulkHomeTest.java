package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.explore.response.Item;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.*;
import com.disney.qa.disney.apple.pages.common.*;
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
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusHulkHomeTest extends DisneyBaseTest {

    String UNENTITLED_SERIES_ID = "entity-7840bf30-f440-48d4-bf81-55d8cb24457a";
    String PLATFORM = "apple";
    String ENVIRONMENT = "PROD";
    String AVAILABLE_WITH_HULU = "Available with Hulu Subscription";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";
    private static final String BACK_BUTTON_NOT_PRESENT = "Back button is not present";
    private static final String HULU_BRAND_LOGO_NOT_EXPANDED = "Hulu brand logo is not expanded";
    private static final String HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE = "Hulu tile is not visible on home page";

    @DataProvider(name = "huluUnavailableDeepLinks")
    public Object[][] huluUnavailableDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_hulu_unavailable_deeplink")},
                {R.TESTDATA.get("disney_prod_hulu_unavailable_language_deeplink")}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74564"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluBrandTileOnHome() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(
                DisneyPlusBrandIOSPageBase.Brand.HULU)).isPresent(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        Assert.assertTrue(huluPage.isOpened(), "Hulu page didn't open after navigating via brand tile");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74829"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluBrandPage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(
                DisneyPlusBrandIOSPageBase.Brand.HULU)).isPresent(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), HULU_BRAND_LOGO_NOT_EXPANDED);
        sa.assertTrue(huluPage.getBackButton().isPresent(), BACK_BUTTON_NOT_PRESENT);
        sa.assertTrue(huluPage.isArtworkBackgroundPresent(), "Artwork images is not present");

        huluPage.swipeInHuluBrandPage(Direction.UP);
        sa.assertTrue(huluPage.isHuluBrandImageCollapsed(), "Hulu brand logo is not collapsed");
        sa.assertTrue(huluPage.getBackButton().isPresent(), BACK_BUTTON_NOT_PRESENT);

        huluPage.swipeInHuluBrandPage(Direction.DOWN);
        sa.assertTrue(huluPage.isHuluBrandImageExpanded(), HULU_BRAND_LOGO_NOT_EXPANDED);

        huluPage.tapBackButton();
        Assert.assertTrue(homePage.isHuluTileVisible(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));
        sa.assertTrue(huluPage.validateScrollingInHuluCollection(), "User cannot scroll horizontally");
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network logos are not present");
        verifyNetworkLogoValues(sa, huluPage);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74642"})
    @Test(groups = {TestGroup.HOME, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluPageContent() throws URISyntaxException, JsonProcessingException {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(
                DisneyPlusBrandIOSPageBase.Brand.HULU)).isPresent(), HULU_TILE_NOT_VISIBLE_ON_HOME_PAGE);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));

        //To get the collections details of Hulu from API
        ArrayList<Container> collections = getHuluAPIPage(HULU_PAGE.getEntityId());
        //Click any title from collection
        try {
            String titleFromCollection = collections.get(0).getItems().get(0).getVisuals().getTitle();
            huluPage.getTypeCellLabelContains(titleFromCollection).click();
            Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), "Detail page did not open");
            Assert.assertTrue(detailsPage.getMediaTitle().equals(titleFromCollection),
                    titleFromCollection + " Content was not opened");
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75209"})
    @Test(description = "New URL Structure - Hulu Hub - Not Entitled For Hulu - Error Message", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US}, dataProvider = "huluUnavailableDeepLinks", enabled = false)
    public void verifyHulkDeepLinkNewURLStructureNotEntitledHulu(String deepLink) throws URISyntaxException, JsonProcessingException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(deepLink);

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77868"})
    @Test(groups = {TestGroup.HULU_HUB, US})
    public void verifyHulkUpsellStandaloneUserInEligible() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int swipeCount = 5;
        String email = "robert.walters+6740c5ea@disneyplustesting.com";

        loginForHuluHub(email);
        homePage.tapHuluBrandTile();

        //Verify user can play some Hulu content

        String titleAvailableToPlay = "Hulu Original Series, Select for details on this title.";
        homePage.getTypeCellLabelContains(titleAvailableToPlay).click();
        Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayOrContinue();
        videoPlayer.verifyVideoPlaying(sa);
        videoPlayer.clickBackButton();

        //Go back to the Hulu page
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.tapBackButton();

        //Swipe to the "Unlock to stream more collection"
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.UNLOCK_TO_STREAM_MORE_HULU,
                Direction.UP,
                swipeCount);

        homePage.getTypeCellLabelContains(AVAILABLE_WITH_HULU).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.getUpgradeNowButton().click();

        //Verify that user is on the ineligible interstitial screen
        sa.assertTrue(detailsPage.isOnlyAvailableWithHuluHeaderPresent(), "Ineligible Screen Header is not present");
        sa.assertTrue(detailsPage.isIneligibleScreenBodyPresent(), "Ineligible Screen Body is not present");
        sa.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(), "Ineligible Screen cta is not present");

        sa.assertAll();
    }

    private void verifyNetworkLogoValues(SoftAssert sa, DisneyPlusHuluIOSPageBase huluPage) {
        try {
            // Items from index 5 indicates the list of Network Logos from the Hulu Brand page
            ArrayList<Item> logoCollection = getHuluAPIPage(HULU_PAGE.getEntityId()).get(5).getItems();
            for (Item item : logoCollection) {
                String logo_title = item.getVisuals().getTitle();
                sa.assertTrue(huluPage.isNetworkLogoPresent(logo_title), String.format("%s Network logo is not present", logo_title));
            }
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
