package com.disney.qa.tests.disney.web.appex.search;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusSearchTest extends DisneyPlusBaseTest {

    DisneyPlusHomePageSearchPage homePageSearchPage;
    DisneyAccount entitledUser;
    JsonNode dictionary;

    @BeforeMethod
    public void setup() {
        homePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        entitledUser = getAccountApi().createAccount("Yearly", locale, language, "V1");
        dictionary = homePageSearchPage.getFullDictionary(entitledUser.getProfileLang());
        homePageSearchPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT,
                countryData.searchAndReturnCountryData(locale, "code", "country"));
        homePageSearchPage.dBaseUniversalLogin(entitledUser.getEmail() , entitledUser.getUserPass());
    }

    //Feature is inconsistent, verify validity
    @QTestCases(id = "43056")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18265"})
    @Test(description = "Search - Verify misspelled search returns result", enabled = false, groups = {TestGroup.DISNEY_APPEX})
    public void testMisspelledSearch() {
        SoftAssert softAssert = new SoftAssert();

        homePageSearchPage.clickOnSearchBar();

        homePageSearchPage.searchFor("Mondolorian");

        //add verification

        softAssert.assertAll();
    }

    //Feature is inconsistent, verify validity
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18267"})
    @Test(description = "Search - Non disney search returns results", groups = {TestGroup.DISNEY_APPEX})
    public void testNonDisneyContentSearch() {
        SoftAssert softAssert = new SoftAssert();

        homePageSearchPage.clickOnSearchBar();

        homePageSearchPage.searchFor("Dark Knight");

        softAssert.assertTrue(!homePageSearchPage.isEmpty(dictionary),
                "Result list shouldn't be empty");

        softAssert.assertAll();
    }

    @QTestCases(id = "45394,72755")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18271"})
    @Test(description = "Search - Search random letters", groups = {TestGroup.DISNEY_APPEX})
    public void testIncomprehensibleSearch() {
        SoftAssert softAssert = new SoftAssert();

        homePageSearchPage.clickOnSearchBar();

        homePageSearchPage.searchFor("asdf");

        softAssert.assertTrue(homePageSearchPage.isEmpty(dictionary),
                "Expected 'No results found' message to be displayed");

        softAssert.assertAll();
    }
}
