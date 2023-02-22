package com.disney.qa.tests.disney.web.appex.watchlist;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.headermenu.DisneyPlusWatchlistPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageCarouselPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.disney.qa.disney.web.entities.Contents;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.DisneyGlobalUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusWatchlistTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20035", "XWEBQAS-18503", "XWEBQAS-18509"})
    @Test(description = "Verify Click Through to Detail Page", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX", "JP"})
    public void verifyWatchListContentLoads() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1496"));
        skipTestByEnv(QA);
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageCarouselPage homepageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageCarouselPage.
                clickMainCarouselItem(2, 2);
        homepageSearchPage
                .clickToAddSearchResult()
                .waitForSeconds(2);
        homepageSearchPage
                .clickOnWatchList();

        analyticPause();

        softAssert.assertTrue(homepageSearchPage.getSearchResultAssetInWatchlist().isElementPresent(),
                "Expected watchlist to contain added search result");

        homepageSearchPage
                .clickOnSearchResultAsset();

        softAssert.assertTrue(detailsPage.isDetailsPagePlayBtnOrTrailerBtnPresent(),
                "Expected - Details play button to be present");

        // Sends the logs to the validator and then adds the results to the soft assert
        analyticPause();
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20037"})
    @Test(description = "Verify Removal Of Watchlist Item", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX", "JP"})
    public void verifyEmptyWatchList() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1497"));
        skipTestByEnv(QA);
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageCarouselPage homepageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        DisneyPlusWatchlistPage watchListPage = new DisneyPlusWatchlistPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageCarouselPage
                .clickMainCarouselItem(0, 2);
        homepageSearchPage.
                clickToAddSearchResult();

        softAssert.assertTrue(homepageSearchPage.getRemoveFromWatchListButton().isElementPresent(),
                "Expected 'Remove from watchlist' button to be present on page");

        homepageSearchPage
                .clickOnWatchList();
        detailsPage
                .waitForDplusTileVisibleByIndex(0);

        softAssert.assertTrue(watchListPage.isDplusTilePresentByIndex(0),
                "Expected Watchlist to be populated");

        watchListPage
                .getWatchlistContentList();
        analyticPause();
        watchListPage
                .clickDplusTileByIndex(0);
        analyticPause();
        homepageSearchPage
                .clickToRemoveWatchlistItem();

        softAssert.assertTrue(homepageSearchPage.getAddToWatchListButton().isElementPresent(),
                "Expected 'Add to watchlist' button to be present on page");

        homepageSearchPage
                .clickOnWatchList();
        detailsPage
                .waitForDplusTileNotVisibleByIndex(0);

        softAssert.assertFalse(watchListPage.isDplusTilePresentByIndex(0),
                "Expected Watchlist to be empty");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18507"})
    @Test(description = "Verify Order By Recently Added", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, "US", "MX", "JP"})
    public void verifyWatchListOrder() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1498"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusWatchlistPage watchListPage = new DisneyPlusWatchlistPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        watchListPage
                .searchAndAddContent(Contents.getWatchlistContent());
        homepageSearchPage
                .clickOnWatchList();
        analyticPause();
        detailsPage
                .waitForDplusTileVisibleByIndex(2);
        analyticPause();

        softAssert.assertTrue(watchListPage.getFirstTileName().contains(Contents.FREE_GUY),
                "Expected - Watchlist should maintain recently added order");
        analyticPause();
        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "Verify Homepage Watchlist", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, "US", "MX", "JP"}, enabled = false)
    public void verifyHomepageWatchlist() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1499"));
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusWatchlistPage watchListPage = new DisneyPlusWatchlistPage(getDriver());
        DisneyPlusHomePageCarouselPage homepageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageCarouselPage
                .clickMainCarouselItem(0,1);
        homepageSearchPage
                .clickToAddSearchResult()
                .clickOnWatchList();

        softAssert.assertTrue(watchListPage.isDplusTilePresentByIndex(1),
                "Expected Watchlist to be not empty");

        homepageCarouselPage
                .clickOnHomeMenuOption();

        softAssert.assertTrue(homepageCarouselPage.isWatchListCategoryOnHomepagePresent(WebConstant.WATCHLIST),
                "Expected Watchlist Category on Homepage");
        softAssert.assertTrue(homepageCarouselPage.watchlistCategoryOnHomepageNotEmpty(),
                "Expected assets under Watchlist Category on Homepage");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    //These test are part of this ticket: https://jira.bamtechmedia.com/browse/QAA-5875
    @Test(description = "Search Results", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_SMOKE, TestGroup.STAR_APPEX, TestGroup.DISNEY_APPEX, "US", "MX", "JP"})
    public void verifySearchBarContent() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1500"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert, PageUrl.SEARCH);

        softAssert.assertEquals(homepageSearchPage.getSearchBarDefaultText(),
                homepageSearchPage.getExpectedSearchBarDefaultText(
                        "Search by title or team",
                        "Search by title, character, or genre"),
                "Expected default content to exist on Searchbar");

        homepageSearchPage
                .enterMovieOnSearchBar()
                .clickToAddSearchResult()
                .clickOnWatchList();
        analyticPause();

        softAssert.assertTrue(homepageSearchPage.getSearchResultAssetInWatchlist().isElementPresent(),
                "Expected watchlist to contain added search result");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20037", "XWEBQAS-18505"})
    @Test(description = "Verify Content", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX,
            TestGroup.DISNEY_SMOKE, TestGroup.LIONSGATE_APPEX, "US", "MX", "JP"})
    public void verifyWatchlistContent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1501"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusWatchlistPage watchListPage = new DisneyPlusWatchlistPage(getDriver());
        DisneyPlusBaseTilesPage baseTilesPage = new DisneyPlusBaseTilesPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V2_ORDER));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        analyticPause();
        if ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) {
            verifyAddToWatchlist(Contents.POWER, softAssert, homepageSearchPage);
            softAssert
                    .assertTrue(baseTilesPage.isTileImageBadgingVisible(baseTilesPage.getWatchlistTileImg(0), "lionsgateplus"),
                            "Expected to see LionsGate+ network attribution on Watchlist tile");
        }

        verifyAddToWatchlist(Contents.THE_SIMPSONS, softAssert, homepageSearchPage);

        homepageSearchPage
                .removeWatchlistItems(watchListPage.getNumberOfWatchListTitles());
        analyticPause();

        softAssert
                .assertTrue(homepageSearchPage.verifyWatchlistIsEmpty(),
                "Expected empty watchlist icon to be present");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20037", "XWEBQAS-18505", "XWEBQAS-31397", "XWEBQAS-31429", "XWEBQAS-31425"})
    @Test(description = "Verify Watchlist for LionsGate entitlement", groups = {TestGroup.STAR_APPEX, TestGroup.LIONSGATE_APPEX, "MX"})
    public void verifyWatchlistContentForLionsGateEntitlement() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusWatchlistPage watchListPage = new DisneyPlusWatchlistPage(getDriver());
        DisneyPlusBaseTilesPage baseTilesPage = new DisneyPlusBaseTilesPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        verifyAddToWatchlist(Contents.POWER, softAssert, homepageSearchPage);
        softAssert
                .assertTrue(baseTilesPage.isTileImageBadgingVisible(baseTilesPage.getWatchlistTileImg(0), "lionsgateplus"),
                        "Expected to see LionsGate+ network attribution on Watchlist tile");

        verifyAddToWatchlist(Contents.THE_SIMPSONS, softAssert, homepageSearchPage);

        homepageSearchPage
                .removeWatchlistItems(watchListPage.getNumberOfWatchListTitles());
        softAssert
                .assertTrue(homepageSearchPage.verifyWatchlistIsEmpty(),
                "Expected empty watchlist icon to be present");

        softAssert.assertAll();
    }

    private void verifyAddToWatchlist(String title, SoftAssert softAssert, DisneyPlusHomePageSearchPage homepageSearchPage) {
        LOGGER.info("Verify title is added to watchlist");
        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterTextOnSearchBar(title);
        homepageSearchPage
                .clickToAddSearchResult()
                .clickOnWatchList()
                .waitForSeconds(2);
        analyticPause();
        homepageSearchPage
                .verifyUrlText(softAssert,PageUrl.WATCHLIST);
        softAssert
                .assertTrue(homepageSearchPage.isTitlePresentInWatchlist(title),
                        "Expected watchlist to contain the title");
    }
}
