package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

public class DisneyPlusHulkSearchTest extends DisneyBaseTest {
    static final String DISNEY_CONTENT = "Percy Jackson";
    static final String HULU_CONTENT = "Only Murders in the Building";

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74554"})
    @Test(description = "Search Hulu Content", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchHuluContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67307"})
    @Test(description = "Search > Empty Page State", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchEmptyPage() {
        String inValidTitleForAdult = "Demolition";
        String inValidTitleForKids = "Robocop";
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(getAccount(), KIDS_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA, true, true);
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        verifyNoResultFoundMessage(sa, inValidTitleForAdult);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(KIDS_PROFILE);
        verifyNoResultFoundMessage(sa, inValidTitleForKids);
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75718"})
    @Test(description = "Search > Empty Page State- Hide Restricted Title for TV-14 and Kids", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchEmptyPageHideRestrictedTitleForTV14AndKids() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().editContentRatingProfileSetting(getAccount(), "MPAAAndTVPG", "TV-14");
        getAccountApi().addProfile(getAccount(), KIDS_PROFILE, KIDS_DOB, getAccount().getProfileLang(), null, true, true);
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Only murders in the building");
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertTrue(searchPage.isPCONRestrictedTitlePresent(), "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Only murders in the building"), "No results found message was not as expected for TV-14 profile");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(KIDS_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Only murders in the building");
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertTrue(searchPage.isKIDSPCONRestrictedTitlePresent(), "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Only murders in the building"), "No results found message was not as expected for kids profile");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75718"})
    @Test(description = "Search > Empty Page State- Max maturity rating", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifySearchEmptyPageMaxMaturityRating() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().editContentRatingProfileSetting(getAccount(), "MPAAAndTVPG", "TV-MA");
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Demolition");
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertFalse(searchPage.isPCONRestrictedTitlePresent(), "PCON restricted title message present for TV-MA profile");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent("Demolition"), "No results found message was not as expected for TV-MA profile");
        sa.assertAll();
    }
    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68282"})
    @Test(description = "Search > Mobile clients displayRecent Searches on search box focus", groups = {"Hulk", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySearchDisplayRecentSearches() {
        String media1 = "Luca";
        String media2 = "Bluey";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().editContentRatingProfileSetting(getAccount(), "MPAAAndTVPG", "TV-MA");
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();

        searchPage.searchForMedia(media1);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page didn't open after tapping on search list");
        sa.assertTrue(detailsPage.getMediaTitle().equalsIgnoreCase(media1), String.format("Details page for %s didn't open", media1));
        homePage.getSearchNav().click();

        searchPage.searchForMedia(media2);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page didn't open after tapping on search list");
        sa.assertTrue(detailsPage.getMediaTitle().equalsIgnoreCase(media2), String.format("Details page for %s didn't open", media2));
        pause(2);
        homePage.getSearchNav().click();
        searchPage.isOpened();
        searchPage.clearText();

        sa.assertTrue(searchPage.isRecentSearchDisplayed(), "recent search was not displayed");
        sa.assertTrue(searchPage.isTitlePresent(media1), "recently searched title was not displayed under recent search");
        sa.assertTrue(searchPage.isTitlePresent(media2), "recently searched title was not displayed under recent search");
        searchPage.tapRecentSearchClearButton();
        sa.assertFalse(searchPage.isTitlePresent(media2), "recently searched title was not displayed under recent search");
        sa.assertTrue(searchPage.isTitlePresent(media1), "recently searched title was not displayed under recent search");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69570"})
    @Test(description = "Search > Limited Availability Messaging", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyLimitedAvailabilityMessaging() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().editContentRatingProfileSetting(getAccount(), "MPAAAndTVPG", "TV-14");
        getAccountApi().addProfile(getAccount(), KIDS_PROFILE, KIDS_DOB, getAccount().getProfileLang(), R.TESTDATA.get("disney_darth_maul_avatar_id"), true, true);
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("The chronicles of Narnia");
        //dismisses the keyboard
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        searchPage.swipeUp(1000);
        sa.assertTrue(searchPage.isPCONRestrictedTitlePresent(), "PCON restricted title message wasn't present for TV-14 profile");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(KIDS_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia("Bluey");
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertTrue(searchPage.isKIDSPCONRestrictedTitlePresent(), "PCON restricted title message was not as expected");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74651", "XMOBQA-68442"})
    @Test(description = "Watchlist Page Support Service-Driven Empty State", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyEmptyWatchlistAndAddToWatchlist() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        homePage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        //verify empty watch list
        sa.assertTrue(moreMenu.isWatchlistHeaderDisplayed(),
                "'Watchlist' header was not displayed");
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
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.getTypeCellLabelContains(DISNEY_CONTENT).isPresent(), "D+ Media title was not added to the watchlist");
        sa.assertTrue(moreMenu.getTypeCellLabelContains(HULU_CONTENT).isPresent(),"Hulu Media title was not added to the watchlist");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74651"})
    @Test(description = "Watchlist - Adding & Removing Hulu Content from the Watchlist", groups = {"Hulk", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyWatchlistAddAndRemoveItem() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
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
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.getTypeCellLabelContains(HULU_CONTENT).isPresent(),"Hulu media title was not added to the watchlist");
        moreMenu.clickBackArrowFromWatchlist();
        //Remove title from the watchlist
        homePage.clickSearchIcon();
        detailsPage.getRemoveFromWatchListButton().click();
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "add to watchlist button wasn't displayed");
        homePage.clickMoreTab();
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        //verify empty watch list
        sa.assertTrue(moreMenu.isWatchlistHeaderDisplayed(),
                "'Watchlist' header was not displayed");
        sa.assertTrue(moreMenu.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not displayed");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74557"})
    @Test(description = "Search Hulu Content", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyMaxLimitSearchQuery() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String searchLimitQuery = "automationsearchlongqueryformaximumSixtyfourcharacterlimittest!!";
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
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

    private void verifyNoResultFoundMessage(SoftAssert sa, String title) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia(title);
        searchPage.getKeyboardSearchButton().clickIfPresent();
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(title), "'No results' error message was not as expected");
    }
}
