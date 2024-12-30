package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.common.constant.IConstantHelper.*;

public class DisneyPlusHulkSearchTest extends DisneyBaseTest {
    static final String DISNEY_CONTENT = "Percy Jackson";
    static final String HULU_CONTENT = "Only Murders in the Building";
    private static final String SEARCH_PAGE_DID_NOT_OPEN = "Search page did not open";
    private static final String WATCHLIST_PAGE_DID_NOT_OPEN = "'Watchlist' page did not open";
    static final String UNLOCK = "Unlock";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74554"})
    @Test(description = "Search Hulu Content", groups = {TestGroup.SEARCH, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchHuluContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Naruto");
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page didn't open after selecting the search result");
        pause(3);
        detailsPage.clickDetailsTab();
        sa.assertTrue(detailsPage.getDetailsTabTitle().contains("Naruto"), "Details page for 'Naruto' didn't open");
        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69570"})
    @Test(description = "Search > Empty Page State- Hide Restricted Title for TV-14 and Kids", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchEmptyPageHideRestrictedTitleForTV14AndKids() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().editContentRatingProfileSetting(getAccount(), "MPAAAndTVPG", "TV-14");
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(KIDS_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(null).kidsModeEnabled(true).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Only murders in the building");
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertTrue(searchPage.isPCONRestrictedErrorMessagePresent(),
                "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Only murders in the building"),
                "No results found message was not as expected for TV-14 profile");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(KIDS_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Only murders in the building");
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertTrue(searchPage.isKIDSPCONRestrictedTitlePresent(), "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Only murders in the building"), "No results found message was not as expected for kids profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67307"})
    @Test(description = "Search > Empty Page State- Max maturity rating", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchEmptyPageMaxMaturityRating() {
        String searchQuery = "robocop";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().editContentRatingProfileSetting(getAccount(), "MPAAAndTVPG", "TV-MA");
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        homePage.getSearchNav().click();
        searchPage.searchForMedia(searchQuery);
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertFalse(searchPage.isPCONRestrictedErrorMessagePresent(),
                "PCON restricted title message present for TV-MA profile");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(searchQuery),
                "No results found message was not as expected for TV-MA profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68442"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEmptyWatchlistAndAddToWatchlist() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        //verify empty watch list
        sa.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_DID_NOT_OPEN);
        sa.assertTrue(moreMenu.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not displayed");
        moreMenu.clickBackArrowFromWatchlist();
        //add D+ Title to watch list
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(DISNEY_CONTENT);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.waitForWatchlistButtonToAppear();
        detailsPage.addToWatchlist();
        //add Hulu Title to watch list
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(HULU_CONTENT);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.waitForWatchlistButtonToAppear();
        detailsPage.addToWatchlist();
        //Verify watchlist is populated with the added titles
        homePage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertTrue(moreMenu.getTypeCellLabelContains(DISNEY_CONTENT).isPresent(), "D+ Media title was not added to the watchlist");
        sa.assertTrue(moreMenu.getTypeCellLabelContains(HULU_CONTENT).isPresent(),"Hulu Media title was not added to the watchlist");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74651"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyWatchlistAddAndRemoveItem() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        //Add Hulu title to watch list
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(HULU_CONTENT);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.waitForWatchlistButtonToAppear();
        detailsPage.addToWatchlist();
        sa.assertTrue(detailsPage.getRemoveFromWatchListButton().isPresent(), "remove from watchlist button wasn't displayed");

        //Verify watchlist is populated with the added titles
        homePage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertTrue(moreMenu.getTypeCellLabelContains(HULU_CONTENT).isPresent(),"Hulu media title was not added to the watchlist");
        moreMenu.clickBackArrowFromWatchlist();
        //Remove title from the watchlist
        homePage.clickSearchIcon();
        detailsPage.getRemoveFromWatchListButton().click();
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "add to watchlist button wasn't displayed");
        homePage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        //verify empty watch list
        sa.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_DID_NOT_OPEN);
        sa.assertTrue(moreMenu.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74557"})
    @Test(description = "Search Hulu Content", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMaxLimitSearchQuery() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String searchLimitQuery = "automationsearchlongqueryformaximumSixtyfourcharacterlimittest!!";
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        homePage.getSearchNav().click();
        searchPage.searchForMedia(searchLimitQuery);
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(searchLimitQuery), "No results found message was not as expected");
        sa.assertEquals(searchLimitQuery, searchPage.getSearchBarText(),
              "Maximum number of characters is not allowed in search field.");
        searchPage.searchForMedia(searchLimitQuery+"D");
        sa.assertEquals(searchLimitQuery, searchPage.getSearchBarText(),
                "character after 64 char is accepted in search bar");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(searchLimitQuery), "No results found message was not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77872"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifySearchHuluContentForStandaloneUser() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY);
        setAppToHomeScreen(basicAccount);

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.searchForMedia(HULU_CONTENT);
        Assert.assertTrue(searchPage.getDynamicAccessibilityId(HULU_CONTENT).isPresent(),
                "Hulu Content not found in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(HULU_CONTENT).getText().contains(HULU),
                "Hulu brand name not found in content in search result");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77875"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifyEntitleAndNonEntitleHuluContentForNonBundleUser() {
        String entitleHuluContent = "Solar Opposites";
        String notEntitleHuluContent = "Only Murders in the Building";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY);
        setAppToHomeScreen(basicAccount);

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        searchPage.searchForMedia(entitleHuluContent);
        Assert.assertTrue(searchPage.getDynamicAccessibilityId(entitleHuluContent).isPresent(),
                "Hulu Content not found in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(entitleHuluContent).getText().contains(HULU),
                "Hulu brand name not found in content in search result");
        Assert.assertFalse(searchPage.getTypeCellLabelContains(entitleHuluContent).getText().contains(UNLOCK),
                "Unlock 'upsell message' found in search result");

        searchPage.getClearTextBtn().click();
        searchPage.searchForMedia(notEntitleHuluContent);
        Assert.assertTrue(searchPage.getDynamicAccessibilityId(notEntitleHuluContent).isPresent(),
                "Hulu Content not found in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(notEntitleHuluContent).getText().contains(HULU),
                "Hulu brand name not found in content in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(notEntitleHuluContent).getText().contains(UNLOCK),
                "Unlock 'upsell message' not found in search result");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77874"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, US})
    public void verifySearchHuluContentForBundleUser() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE));
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        searchPage.searchForMedia(HULU_CONTENT);
        Assert.assertTrue(searchPage.getDynamicAccessibilityId(HULU_CONTENT).isPresent(),
                "Hulu Content not found in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(HULU_CONTENT).getText().contains(HULU),
                "Hulu brand name not found in content in search result");
        Assert.assertFalse(searchPage.getTypeCellLabelContains(HULU_CONTENT).getText().contains(UNLOCK),
                "Unlock 'upsell message' found in search result");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77873"})
    @Test(groups = {TestGroup.HULU_HUB, TestGroup.SEARCH, CA})
    public void verifySearchHuluContentForStandaloneUserInEligible() {
        String unavailableContentInCA = "Normal People";

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                CA,
                getLocalizationUtils().getUserLanguage()));
        handleAlert();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.searchForMedia(unavailableContentInCA);
        Assert.assertTrue(searchPage.isNoResultsFoundMessagePresent(unavailableContentInCA),
                String.format("No results found message was not displayed for, '%s'", unavailableContentInCA));
    }
}
