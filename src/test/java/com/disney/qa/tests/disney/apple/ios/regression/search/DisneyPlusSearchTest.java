package com.disney.qa.tests.disney.apple.ios.regression.search;

import com.disney.alice.AliceDriver;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusSearchTest extends DisneyBaseTest {

    private static final String BLUEY = "Bluey";

    @Maintainer("dconyers")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61083"})
    @Test(description = "Search: Navigate to search page and verify search icon", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void verifySearchTabIcon() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, "search_button_selected");
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74517"})
    @Test(description = "'Recent Searches' is not shown when user has made no Recent Searches", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void verifyRecentSearchWhenNoSearchMade() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        searchPage.getSearchBar().click();
        sa.assertFalse(searchPage.isRecentSearchDisplayed(), "recent search was displayed");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62552"})
    @Test(description = "Search - Recent Searches - Clear Recent Search by clicking on the X Icon", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void clearRecentSearchs() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search page did not open");

        //User made search
        searchPage.searchForMedia(BLUEY);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        detailsPage.getBackArrow().click();
        //Empty string to clear the keys
        searchPage.searchForMedia("");
        searchPage.getCancelButton().click();
        homePage.clickSearchIcon();
        searchPage.getSearchBar().click();
        sa.assertTrue(searchPage.isRecentSearchDisplayed(), "recent search was not displayed");
        searchPage.getCancelButton().click();
        homePage.clickHomeIcon();

        //click x button to clear recent searches
        homePage.clickSearchIcon();
        searchPage.getSearchBar().click();
        searchPage.tapRecentSearchClearButton();
        searchPage.getSearchBar().click();
        sa.assertFalse(searchPage.isRecentSearchDisplayed(), "recent search was displayed");
        searchPage.getCancelButton().click();

        //next time user select search bar, recent search is no longer displayed
        homePage.clickHomeIcon();
        homePage.clickSearchIcon();
        searchPage.getSearchBar().click();
        sa.assertFalse(searchPage.isRecentSearchDisplayed(), "recent search was displayed");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62542"})
    @Test(description = "Search - Recent Searches - Selecting a Recent Search initiates that Search", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void verifyRecentSearchInitiatesValidSearch() {
        String media = "Turning Red";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search page did not open");

        //User made different search
        searchPage.searchForMedia(BLUEY);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        detailsPage.getBackArrow().click();
        searchPage.searchForMedia(media);
        results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        detailsPage.getBackArrow().click();

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();

        //user selects a Recent Search from the Recent Searches list
        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.getSearchBar().click();
        sa.assertTrue(searchPage.isRecentSearchDisplayed(), "recent search was not displayed");
        searchPage.tapTitleUnderRecentSearch(media);

        //verify search is initiated using the selected Recent Search
        sa.assertTrue(searchPage.getStaticTextByLabel(media).isPresent(), "search was not initiated using the selected Recent Search");

        //verify selected recent search item opened
        results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        sa.assertTrue(detailsPage.getMediaTitle().equals(media), "selected recent search item was not opened");
    }
}