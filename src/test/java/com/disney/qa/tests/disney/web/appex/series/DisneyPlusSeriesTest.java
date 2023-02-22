package com.disney.qa.tests.disney.web.appex.series;

import java.lang.invoke.MethodHandles;
import java.util.List;

import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.headermenu.DisneyPlusMoviesPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageCarouselPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.appex.parentalcontrols.DisneyPlusSetProfilePinPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAddProfilePage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.entities.*;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.disney.qa.disney.web.appex.headermenu.DisneyPlusSeriesPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;


public class DisneyPlusSeriesTest extends DisneyPlusBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String BROWSER = R.CONFIG.get("browser");

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20460"})
    @Test(description = "Scroll/Filter", priority = 1, groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX,TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifySeriesPageScroll() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1521"));
        SoftAssert softAssert = new SoftAssert();
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        seriesPage.setIgnoreCookie(true);
        seriesPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        seriesPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        seriesPage.clickOnSeriesMenuOption();

        DisneyPlusBaseTilesPage tilesPage = new DisneyPlusBaseTilesPage(getDriver());
        List<String> tileSizeBeforeScroll = tilesPage.getTileAssetAttributes(100, "alt");

        LOGGER.info(String.format("Size of content before scroll: %s", tileSizeBeforeScroll.size()));

        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.scrollToBottom();

        pause(5);

        List<String> tileSizeAfterScroll = tilesPage.getTileAssetAttributes(100, "alt");

        LOGGER.info(String.format("Size of tile content after scroll: %s", tileSizeAfterScroll.size()));

        softAssert.assertTrue(tileSizeAfterScroll.size() >= tileSizeBeforeScroll.size(),
                String.format("Movies failed to populate after page scroll, original size of tiles grabbed: %s, tiles grabbed afters scroll: %s", tileSizeBeforeScroll.size(), tileSizeAfterScroll.size()));

        seriesPage.clickSeriesFilter();
        softAssert.assertTrue(seriesPage.getSeriesFilterOptionInitial().isElementPresent(),
                "Check for default filter");

        seriesPage.selectSeriesFilter();
        softAssert.assertTrue(seriesPage.isDisneyPlusMovieIndexPresent(4),
                "Data available after the filter change");
        softAssert.assertTrue(seriesPage.isSeriesAssetPresent(),
                "Expected asset to be present after filter change");

        seriesPage.clickOnTileByIndex(1);
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        softAssert.assertTrue(homepageSearchPage.getAddToWatchListButton().isElementPresent(),
                "Watchlist button should be present on the asset details page");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19439"})
    @Test(description = "Logged Out Details Page", priority = 2, groups = {TestGroup.STAR_SMOKE, TestGroup.STAR_APPEX, TestGroup.DISNEY_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifySeriesPageNotLoggedIn() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1522"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        seriesPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        seriesPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        seriesPage.clickOnSeriesMenuOption();
        analyticPause();
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.enterSeriesOnSearchBar();
        String seriesUrl = seriesPage.getCurrentUrl();
        analyticPause();
        seriesPage.getActiveProfile();
        seriesPage.clickOnLogout();
        seriesPage.openDetailsPageUrl(seriesUrl);
        analyticPause();
        softAssert.assertTrue((seriesPage.getSignUpNowCta().isElementPresent()),
                "Sign up now CTA should be present");
        seriesPage.getDriver().navigate().refresh();
        analyticPause();
        softAssert.assertTrue((seriesPage.getSignUpNowCta().isElementPresent()),
                "Sign up now CTA should be present after refresh");

        //Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18358","XWEBQAS-31153"})
    @Test(description = "Series Details Page", priority =3, groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, "AG","AI","AR","AT","AU","AW","BB","BE","BL","BM","BO","BQ","BR","BS","BZ","CA","CH","CL","CO","CR","CW","DE","DK","DM","DO","EC","ES","FI","FK","FR","GB","GD","GF","GG","GP","GS","GT","GY","HN","HT","IE","IM","IS","IT","JE","JM","JP","KR","KN","KY","LC","LU","MC","MF","MQ","MS","MU","MX","NC","NI","NL","NO","NZ","PA","PE","PR","PT","PY","RE","SE","SG","SR","SV","TC","TT","US","UY","VC","VE","VG","VI","WF","YT"})
    public void verifySeriesDetails() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1523"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        seriesPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        seriesPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        seriesPage.clickOnSeriesMenuOption();
        analyticPause();
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.enterSeriesOnSearchBar();
        pause(5); //avoid clicking 'QA build'

        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.scrollToBottom();

        analyticPause();

        seriesPage.verifyEpisodesTab();
        softAssert.assertTrue(seriesPage.isSeriesAssetPresent(),
                "Episode asset should display");

        seriesPage.verifySuggestedTab();
        softAssert.assertTrue(seriesPage.isSeriesAssetPresent(),
                "Suggested asset should display");

        seriesPage.verifyExtrasTab();
        softAssert.assertTrue(seriesPage.isSeriesAssetPresent(),
                "Extras asset should display");

        seriesPage.verifyDetailsTab();
        softAssert.assertTrue(seriesPage.getSeriesDetailHeadingTitle().isElementPresent(),
                "Details heading title should display");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31290", "XWEBQAS-31342", "XWEBQAS-31339"})
    @Test(description = "Shop Disney - Verify Shop Tab", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SHOP_DISNEY, "US"})
    public void verifyShopDisneyTab() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageSearchPage
                .clickOnSearchBar()
                .enterTextOnSearchBar(Contents.FROZEN_FEVER);
        softAssert
                .assertTrue(seriesPage.isSeriesDetailShopMenuVisible(),
                        "Shop tab is not visible");
        seriesPage
                .clickDisneyShopMenu()
                .waitForSeconds(2);
        softAssert.assertTrue(seriesPage.isDisneyShopPictureVisible(),
                "Shop Disney picture is not visible on shop Disney tab");
        softAssert
                .assertTrue(seriesPage.isDisneyShopHeadingVisible(),
                        "Header is not visible on shop Disney tab");
        softAssert
                .assertTrue(seriesPage.isDisneyShopParagraphVisible(),
                        "Paragraph is not visible on shop Disney tab");
        softAssert
                .assertTrue(seriesPage.isDisneyShopLogoVisible(),
                        "Disney logo is not visible on shop Disney tab");
        softAssert
                .assertTrue(seriesPage.isDisneyShopLegalVisible(),
                        "Legal disclaimer is not visible on shop Disney tab");
        seriesPage
                .clickDisneyShopLogo()
                .switchWindow();
        softAssert
                .assertTrue(seriesPage.getCurrentUrl().contains(PageUrl.SHOP_DISNEY),
                        "Current URL doesn't contain " + PageUrl.SHOP_DISNEY);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31340"})
    @Test(description = "Shop Disney - Verify Shop Disney Profiles", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SHOP_DISNEY, TestGroup.ARIEL_APPEX, "US"})
    public void verifyShopDisneyProfiles() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.PROFILE1, language, null, false);
        getAccountApi().addProfile(account, ProfileConstant.JUNIOR, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        addProfilePage.selectProfile(ProfileConstant.MAIN_TEST);

        homepageSearchPage
                .clickOnSearchBar()
                .enterTextOnSearchBar(Contents.FROZEN_FEVER);
        /*softAssert
                .assertTrue(seriesPage.isDetailsPromoLabelVisible(),
                        "Details promo label is visible");*/
        softAssert
                .assertTrue(seriesPage.isSeriesDetailShopMenuVisible(),
                        "Shop tab is visible");
        waitForSeconds(2);
        addProfilePage.hoverOnProfile();
        addProfilePage.selectProfile(ProfileConstant.PROFILE1);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();
        waitForSeconds(2);
        /*softAssert
                .assertFalse(seriesPage.isDetailsPromoLabelVisible(),
                        "Details promo label is visible");*/
        softAssert
                .assertFalse(seriesPage.isSeriesDetailShopMenuVisible(),
                        "Shop tab is visible");
        addProfilePage.hoverOnProfile();
        addProfilePage.selectProfile(ProfileConstant.JUNIOR);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        addProfilePage
                .clickOnAgreeBtn();
        waitForSeconds(2);
        /*softAssert
                .assertFalse(seriesPage.isDetailsPromoLabelVisible(),
                        "Details promo label is visible");*/
        softAssert
                .assertFalse(seriesPage.isSeriesDetailShopMenuVisible(),
                        "Shop tab is visible");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31211"})
    @Test(description = "Verify GroupWatch not available for account with Ads", groups = {TestGroup.DISNEY_APPEX, TestGroup.GROUPWATCH, "US"})
    public void verifyGroupWatchForAccountWithAds() {
        skipTestByEnv(QA);

        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusMoviesPage moviesPage = new DisneyPlusMoviesPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount(STANDALONE_MONTHLY_ADS, locale, language, SUB_VERSION_V2_ORDER));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String baseURL = homepageSearchPage.getHomeUrl();
        homepageSearchPage
                .clickOnSearchBar()
                .enterTextOnSearchBar(Contents.TURNING_RED);
        softAssert
                .assertFalse(moviesPage.isGroupWatchBtnVisible(), "Expected to not see GroupWatch button");

        homepageSearchPage
                .openURL(baseURL.concat(PageUrl.GROUPWATCH));
        softAssert
                .assertTrue(moviesPage.getGroupWatchPopUpText().equals("This GroupWatch is not available"),
                        "Expected to see GroupWatch not available text in pop up");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31396", "XWEBQAS-31397", "XWEBQAS-31398"})
    @Test(description = "Verify LionsGate content details page for unentitled users", groups = {TestGroup.STAR_APPEX, TestGroup.LIONSGATE_APPEX, "MX"})
    public void verifyDetailsPageForLionsGateUnentitledUser() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusBaseDetailsPage baseDetailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        DisneyPlusHomePageCarouselPage homePageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V2_ORDER));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        homePageCarouselPage
                .scrollToViewLionsGateCollection();
        softAssert
                .assertTrue(seriesPage.isBrandImageVisible("lionsgate"),
                        "Expected to see LionsGate brand image in home page");
        softAssert
                .assertTrue(baseDetailsPage.verifyTileImageBadging("lionsgateplus"),
                        "Expected to see tile image badging for all LionsGate tiles in Home Page");

        homepageSearchPage
                .clickOnSearchBar()
                .searchFor(Contents.POWER);
        softAssert
                .assertTrue(baseDetailsPage.isTileImageBadgingVisible(baseDetailsPage.getSearchResultTileImg(0), "lionsgateplus"));

        homepageSearchPage
                .clickOnFirstSearchResult()
                .waitForSeconds(1);
        softAssert
                .assertTrue(homepageSearchPage.isAddToWatchlistBtnPresent(), "Expected to see Add to watchlist button");
        softAssert
                .assertFalse(commercePage.isPremierAccessCtaPresent(), "Expected to not see the Premier access CTA");
        softAssert
                .assertTrue(seriesPage.isLionsGateNetworkLogoVisible(), "Expected to see LionsGate+ network attribution");
        softAssert
                .assertTrue(seriesPage.getUnentitledHeadlineText("LIONSGATE").contains(WebConstant.LIONSGATE_UNENTITLED_TEXT_HEADLINE),
                        "Expected to see LionsGate unentitled text headline");
        softAssert
                .assertTrue(seriesPage.getUnentitledText("LIONSGATE").contains(WebConstant.LIONSGATE_UNENTITLED_TEXT),
                        "Expected to see LionsGate unentitled text");
        softAssert
                .assertFalse(baseDetailsPage.isDetailsPagePlayBtnPresent(), "Expected to not see Play button");
        softAssert
                .assertFalse(seriesPage.isEpisodesTabVisible(), "Expected to not see episodes tab");
        softAssert
                .assertFalse(seriesPage.isExtrasTabVisible(), "Expected to not see extras tab");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31399", "XWEBQAS-31394", "XWEBQAS-31397", "XWEBQAS-31429", "XWEBQAS-31425"})
    @Test(description = "Verify LionsGate content details page for entitled users", groups = {TestGroup.STAR_APPEX, TestGroup.LIONSGATE_APPEX, "MX"})
    public void verifyDetailsPageForLionsGateEntitledUser() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusSeriesPage seriesPage = new DisneyPlusSeriesPage(getDriver());
        DisneyPlusBaseDetailsPage baseDetailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageSearchPage
                .clickOnSearchBar()
                .enterTextOnSearchBar(Contents.POWER);
        waitForSeconds(1);
        softAssert
                .assertFalse(seriesPage.isPremierAccessLogoPresent(),
                        "Expected to not see Premier Access logo");
        softAssert
                .assertFalse(seriesPage.isTextPresent("You have Premier Access to this movie."),
                        "Expected to not see the Premier access headline");
        softAssert
                .assertTrue(baseDetailsPage.isDetailsPagePlayBtnPresent(), "Expected to see Play button");
        softAssert
                .assertTrue(homepageSearchPage.isAddToWatchlistBtnPresent(),
                        "Expected to see Add to Watchlist button");
        softAssert
                .assertTrue(seriesPage.isBrandImageVisible("Lions Gate Plus"),
                        "Expected to see LionsGate plus logo");

        seriesPage
                .verifyEpisodesTab();
        softAssert
                .assertTrue(seriesPage.isSeriesAssetPresent(), "Expected to see Episodes tab content");

        if(BROWSER.equalsIgnoreCase("chrome")) { //https://jira.disneystreaming.com/browse/WEBQAA-766: Task for Firefox issue
            seriesPage
                    .verifyDetailsTab();
            softAssert
                    .assertTrue(seriesPage.getSeriesDetailHeadingTitle().isElementPresent(),
                            "Details heading title should display");
        }

        softAssert.assertAll();
    }
}

