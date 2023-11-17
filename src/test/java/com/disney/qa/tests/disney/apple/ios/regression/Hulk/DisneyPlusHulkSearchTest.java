package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusHulkSearchTest extends DisneyBaseTest {

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74646"})
    @Test(description = "Search Hulu Content", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchHuluContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
//        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
//        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        setAppToHulkHomeScreen(ADULT_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Naruto");
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page didn't open after selecting the search result");
        pause(5);
        sa.assertTrue(detailsPage.getStaticTextByName("Naruto").isPresent(), "Details page for 'Naruto' didn't open");
        sa.assertAll();

    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74811"})
    @Test(description = "Search > Empty Page State", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchEmptyPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
//        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
//        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        setAppToHulkHomeScreen(ADULT_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Demolition");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Demolition"), "'No results' error message was not as expected");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74878"})
    @Test(description = "Search > Empty Page State- Hide Restricted Title for TV-14 and Kids", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchEmptyPageHideRestrictedTitleForTV14AndKids() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
//        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
//        disneyAccountApi.get().editContentRatingProfileSetting(disneyAccount.get(), "MPAAAndTVPG", "TV-14");
//        disneyAccountApi.get().addProfile(disneyAccount.get(), KIDS_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), null, true, true);
//        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        setAppToHulkHomeScreen(TV_14_PROFILE);
        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Only murders in the building");
        sa.assertTrue(searchPage.isPCONRestrictedTitlePresent(), "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Only murders in the building"), "No results found message was not as expected for TV-14 profile");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Only murders in the building");
        sa.assertTrue(searchPage.isKIDSPCONRestrictedTitlePresent(), "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Only murders in the building"), "No results found message was not as expected for kids profile");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74879"})
    @Test(description = "Search > Empty Page State- Max maturity rating", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchEmptyPageMaxMaturityRating() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
//        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
//        disneyAccountApi.get().editContentRatingProfileSetting(disneyAccount.get(), "MPAAAndTVPG", "TV-MA");
//        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        setAppToHulkHomeScreen(ADULT_PROFILE);

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Demolition");
        sa.assertFalse(searchPage.isPCONRestrictedTitlePresent(), "PCON restricted title message present for TV-MA profile");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Demolition"), "No results found message was not as expected for TV-14 profile");
        sa.assertAll();
    }
}
