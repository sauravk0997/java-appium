package com.disney.qa.tests.disney.web.appex.search;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.Contents;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusNewSearchTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18255"})
    @Test(description = "Search - Sticks to top during scroll", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SEARCH, "US"})
    public void verifySearchFieldSticksToTop() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageSearchPage
                .clickOnSearchBar()
                .homePageSearchContentLazyLoad();
        softAssert
                .assertTrue(homepageSearchPage.verifyDefaultSearchBarIsVisible(),
                        "Expected search field to be visible after scroll");
        appExUtil
                .scrollToTop()
                .waitForSeconds(1);
        softAssert
                .assertTrue(homepageSearchPage.verifyDefaultSearchBarIsVisible(),
                        "Expected search field to be visible after scroll");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18263"})
    @Test(description = "Search-Test search clear feature", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SEARCH, "US"})
    public void testClearSearch() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageSearchPage
                .clickOnSearchBar()
                .searchFor(Contents.MANDALORIAN)
                .waitForSeconds(2);
        homepageSearchPage
                .clearSearch();

        softAssert
                .assertEquals(homepageSearchPage.getSearchBarDefaultText(),
                        "Search by title, character, or genre",
                        "Expected default content to exist on Search bar");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18280"})
    @Test(description = "Search - Verify search & search results retained", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SEARCH, "US"})
    public void testSearchResultRetains() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homepageSearchPage
                .clickOnSearchBar()
                .enterMovieOnSearchBar()
                .clickOnSearchBar();

        softAssert
                .assertTrue(homepageSearchPage.isSearchResultPresent(),
                        "Expected Search result to retain");
        softAssert
                .assertFalse(homepageSearchPage.getSearchBarText().isEmpty(),
                        "Search bar should still display the searched text");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18257"})
    @Test(description = "Search - Verify output on single char input", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SEARCH, "US"})
    public void verifySearchResultOnSingleCharInput(){
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homePageSearchPage
                .clickOnSearchBar()
                .searchFor("S")
                .waitForSeconds(2);
        softAssert.assertFalse(homePageSearchPage.getContainerList().isEmpty(),
                "Expected initial container shouldn't be available");
        softAssert.assertTrue(homePageSearchPage.isSearchResultsContainingChar("S"),
                "All the results names should contain the entered character");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18277"})
    @Test(description = "Search - Verify tile aspect ratio", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SEARCH, "US"})
    public void testTileAspectRatio() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homePageSearchPage
                .clickOnSearchBar()
                .searchFor(Contents.THE_SIMPSONS);
        softAssert.assertEquals(homePageSearchPage.getTileAspectRatio(Contents.THE_SIMPSONS), 1.8,
                "Expected Aspect ratio to be 16:9");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18267"})
    @Test(description = "Search - Non disney search returns results", groups = {TestGroup.DISNEY_APPEX, TestGroup.WEB_HOME, TestGroup.SEARCH, "US"})
    public void testNonDisneyContentSearch() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        homePageSearchPage
                .clickOnSearchBar()
                .searchFor(Contents.NON_DISNEY_TITLE);
        softAssert
                .assertFalse(homePageSearchPage.verifySearchListContainsTitle(Contents.NON_DISNEY_TITLE), "Expected to not see non-disney title in the search results");

        softAssert.assertAll();
    }
}
