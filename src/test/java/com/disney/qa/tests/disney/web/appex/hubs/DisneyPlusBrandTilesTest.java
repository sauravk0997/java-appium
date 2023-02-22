package com.disney.qa.tests.disney.web.appex.hubs;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.headermenu.DisneyPlusSeriesPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusBrandPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageCarouselPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.disney.qa.disney.web.entities.Contents;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusBrandTilesTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-30884", "XWEBQAS-30883", "XWEBQAS-30882", "XWEBQAS-30881", "XWEBQAS-30880"})
    @Test(description = "Verify Brand tiles for all Brands in Home page and the autoplay video", groups = {TestGroup.DISNEY_APPEX, TestGroup.HUBS})
    public void verifyBrandTilesAndAutoPlayVideo() {
        skipTestByEnv(QA);
        
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageCarouselPage homePageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        softAssert
                .assertTrue(homePageCarouselPage.verifyBrandTileVideo(homePageCarouselPage.getNumberOfTilesInContainer()),
                        "Expected to see background video for brand tiles and hub pages");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31395", "XWEBQAS-31394", "XWEBQAS-31397", "XWEBQAS-31433", "XWEBQAS-31429", "XWEBQAS-31425"})
    @Test(description = "Verify Lionsgate brand collection page", groups = {TestGroup.LIONSGATE_APPEX, TestGroup.STAR_APPEX, "MX"})
    public void verifyLionsGateBrandPage() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageCarouselPage homePageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        DisneyPlusBrandPage disneyPlusBrandPage = new DisneyPlusBrandPage(getDriver());
        DisneyPlusBaseTilesPage baseTilesPage = new DisneyPlusBaseTilesPage(getDriver());
        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        DisneyPlusHomePageSearchPage homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homePageSearchPage
                .clickOnSearchBar()
                .searchFor(Contents.POWER);
        softAssert
                .assertTrue(baseTilesPage.isTileImageBadgingVisible(baseTilesPage.getSearchResultTileImg(0), "lionsgateplus"));

        homePageCarouselPage
                .clickOnHomeMenuOption();
        //Need to scroll down to load the page and able to see LionsGate collection
        homePageCarouselPage
                .scrollToViewLionsGateCollection();
        softAssert
                .assertTrue(seriesPage.isBrandImageVisible("lionsgate"),
                        "Expected to see LionsGate brand image in home page");
        softAssert
                .assertTrue(baseTilesPage.verifyTileImageBadging( "lionsgateplus"),
                        "Expected to see tile image badging for all LionsGate tiles in Home Page");

        disneyPlusBrandPage
                .clickOnLionsGateTile(0)
                .waitForSeconds(2);
        softAssert
                .assertTrue(seriesPage.isBrandImageVisible("Lions Gate Plus"),
                        "Expected to see LionsGate brand page with LionsGate+ network attribution");

        verifyBrandPageForLionsGate(homePageCarouselPage, disneyPlusBrandPage, softAssert);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31397", "XWEBQAS-31434"})
    @Test(description = "Verify Lionsgate brand collection page for unentitled user", groups = {TestGroup.LIONSGATE_APPEX, TestGroup.STAR_APPEX, "MX"})
    public void verifyLionsGateBrandPageForUnentitledUser() {
        skipTestByEnv(QA);
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageCarouselPage homePageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        DisneyPlusBrandPage disneyPlusBrandPage = new DisneyPlusBrandPage(getDriver());
        DisneyPlusBaseTilesPage baseTilesPage = new DisneyPlusBaseTilesPage(getDriver());
        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        DisneyPlusHomePageSearchPage homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        SeleniumUtils utils = new SeleniumUtils(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V2_ORDER));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

      while(!baseTilesPage.isRecommendedForYouCollectionVisible())
            utils.scrollToBottom();
        softAssert
                .assertFalse(homePageSearchPage.verifyTileImageBadging("lionsgateplus"),
                        "Expected to not see LionsGate content in Recommended for you collection");

        //Need to scroll down to load the page and able to see LionsGate collection
        homePageCarouselPage
                .scrollToViewLionsGateCollection();
        softAssert
                .assertTrue(seriesPage.isBrandImageVisible("lionsgate"),
                        "Expected to see LionsGate brand image in home page");
        softAssert
                .assertTrue(baseTilesPage.verifyTileImageBadging("lionsgateplus"),
                        "Expected to see tile image badging for all LionsGate tiles in Home Page");

        disneyPlusBrandPage
                .clickOnLionsGateTile(0)
                .waitForSeconds(2);
        softAssert
                .assertFalse(seriesPage.isBrandImageVisible("Lions Gate Plus"),
                        "Expected to not see LionsGate brand page with LionsGate+ network attribution");

        verifyBrandPageForLionsGate(homePageCarouselPage, disneyPlusBrandPage, softAssert);

        softAssert.assertAll();
    }

    private void verifyBrandPageForLionsGate(DisneyPlusHomePageCarouselPage homePageCarouselPage, DisneyPlusBrandPage disneyPlusBrandPage, SoftAssert softAssert) {
        LOGGER.info("Verify brand page for LionsGate");
        homePageCarouselPage
                .clickOnHomeMenuOption();
        waitForSeconds(1);
        homePageCarouselPage
                .scrollToViewLionsGateCollection();
        homePageCarouselPage
                .clickOnViewMoreButton()
                .waitForSeconds(1);
        softAssert
                .assertTrue(disneyPlusBrandPage.getCurrentUrl().contains(PageUrl.LIONSGATE),
                        "Expected to see LionsGate brand page");
        softAssert
                .assertTrue(disneyPlusBrandPage.isLionsGateImageVisible(),
                        "Expected to see LionsGate image in the brand page");
        softAssert.assertTrue(disneyPlusBrandPage.verifyTileImage(),
                "Expected to see tile image on all tiles in the LionsGate brand page");
        softAssert
                .assertTrue(disneyPlusBrandPage.verifyTileImageBadging("lionsgateplus"),
                        "Expected to see tile image badging for second collection");
    }
}
