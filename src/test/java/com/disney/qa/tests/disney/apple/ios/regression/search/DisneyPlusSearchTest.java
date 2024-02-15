package com.disney.qa.tests.disney.apple.ios.regression.search;

import com.disney.alice.AliceDriver;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class DisneyPlusSearchTest extends DisneyBaseTest {

    private static final String BLUEY = "Bluey";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";

    @DataProvider(name = "contentName")
    public Object[][] disneyPlanTypes() {
        return new Object[][]{
                {MOVIES}, {SERIES}
        };
    }

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
    public void clearRecentSearches() {
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
        results = searchPage.getDisplayedTitles();
        results.get(0).click();

        //verify selected recent search item opened
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        sa.assertTrue(detailsPage.getMediaTitle().equals(media), "selected recent search item was not opened");
        sa.assertAll();
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62540"})
    @Test(description = "Search - Recent Searches - Show 10 Results Max with the Ability to Scroll Up and Down", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void verifyRecentSearchShowsMaxTenResults() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        //Add 11 search result in recent search list
        IntStream.range(0, getMedia().size()).forEach(i -> {
            if (searchPage.getClearText().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(getMedia().get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
            detailsPage.getBackArrow().click();
        });

        searchPage.clearText();
        searchPage.getSearchBar().click();

        //Verify that the after searching 11 content, only last latest 10 visible in list and the first one is not visible
        sa.assertFalse(searchPage.getStaticTextByLabel(getMedia().get(0)).isPresent(), "First content is displayed");
        for(int j = getMedia().size()-1; j>0; j--){
            sa.assertTrue(searchPage.getStaticTextByLabel(getMedia().get(j)).isPresent(), "recent search content was not displayed in recent search results");
            if(j==getMedia().size()/2){
                searchPage.swipeInRecentSearchResults(Direction.UP);
                //After Swipe also verify that the first content is not visible
                sa.assertFalse(searchPage.getStaticTextByLabel(getMedia().get(0)).isPresent(), "First content is displayed");
            }
        }
        sa.assertAll();
    }

    protected ArrayList<String> getMedia() {
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add("Bluey");
        contentList.add("Turning Red");
        contentList.add("Presto");
        contentList.add("Percy Jackson and the Olympians");
        contentList.add("Dancing with the Stars");
        contentList.add("The Incredible Hulk");
        contentList.add("The Jungle Book");
        contentList.add("Guardians of the Galaxy");
        contentList.add("Jungle Cruise");
        contentList.add("Fantastic Four");
        contentList.add("Iron Man");
        return contentList;
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61829"})
    @Test(description = "Search - Content Type Landing Pages - Scroll Behavior & Dropdown Behavior", groups = {"Search", TestGroup.PRE_CONFIGURATION}, dataProvider = "contentName")
    public void verifyScrollAndDropdownForSearchContentLandingPage(String contentName) {
        String filterValue = "Comedy";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        if(contentName.equalsIgnoreCase("movies")){
            searchPage.clickMoviesTab();
        }else{
            searchPage.clickSeriesTab();
        }

        //Verify Page header is present
        sa.assertTrue(searchPage.getStaticTextByLabel(contentName).isPresent(), "Page header '" +contentName + "' was not found");

        if(R.CONFIG.get(DEVICE_TYPE).equals(TABLET)){
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
            scrollDown();
            //verify after scrolling down also, Page header and Filter header tabbar is present
            sa.assertTrue(searchPage.getStaticTextByLabel(contentName).isPresent(), "Page header '" +contentName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
            //Verify after selecting any filter value also, Page header and Filter header tabbar is present
            searchPage.getTypeButtonByLabel(filterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(contentName).isPresent(), "Page header '" +contentName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
        }else{
            sa.assertTrue(searchPage.isContentPageFilterDropDownPresent(), "Content Page Filter Dropdown was not found");
            scrollDown();
            //verify after scrolling down also, Filter dropdown is present
            sa.assertTrue(searchPage.isContentPageFilterDropDownAtMiddleTopPresent(), "Content Page Filter Dropdown not present after scroll");
            //Verify after selecting any filter value also, it navigate to top and Filter dropdown is present
            searchPage.clickContentPageFilterDropDownAtMiddleTop();
            searchPage.getStaticTextByLabel(filterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(contentName).isPresent(), "Page header '" +contentName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterDropDownPresent(), "Content Page Filter Dropdown was not found");
        }
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61725"})
    @Test(description = "Search - Originals Landing Page - UI Elements", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void verifyOriginalsLandingPageUI() {
        int containerPosition = 0;
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyAccount testAccount = getAccount();
        setAppToHomeScreen(testAccount);

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.clickOriginalsTab();

        //Verify Original page opened
        sa.assertTrue(originalsPage.isOriginalPageLoadPresent(), "Original content page was not opened");
        //Verify Back button is present or not
        sa.assertTrue(originalsPage.getBackArrow().isPresent(), "Back button was not found");

        //To get the collections details of Original from API
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(getLocalizationUtils().getLocale())
                .audience("false")
                .language(getLocalizationUtils().getUserLanguage())
                .slug(DisneyStandardCollection.ORIGINALS.getSlug())
                .contentClass(DisneyStandardCollection.ORIGINALS.getContentClass())
                .account(testAccount)
                .build();
        ContentCollection contentCollection = getSearchApi().getCollection(collectionRequest);
        List<DisneyCollectionSet> setInfo = contentCollection.getCollectionSetsInfo();

        ExtendedWebElement collectionName;
        for (DisneyCollectionSet set : setInfo) {
            collectionName = searchPage.getTypeOtherByLabel(set.getContent());
            swipe(collectionName);
            //Verify that collection is present in page
            sa.assertTrue(collectionName.isPresent(), collectionName + " content was not found");

            //To get the all movie/series title under collection from API
            SetRequest setRequest = SetRequest.builder()
                    .region(getLocalizationUtils().getLocale())
                    .language(getLanguage())
                    .setId(set.getRefId())
                    .refType(set.getRefType())
                    .account(testAccount).build();
            String titleFromCollection = getSearchApi().getAllSetPages(setRequest).getTitles().get(0);

            originalsPage.swipeInCollectionContainer(originalsPage.getDynamicCellByLabel(titleFromCollection), containerPosition++);
            Assert.assertTrue(originalsPage.getDynamicCellByLabel(titleFromCollection).isPresent(), titleFromCollection + " was not present for " + set.getContent() + " collection");
            //verify that correct titles of that collection opened in app, verify with 2 or 3 titles
            originalsPage.getDynamicCellByLabel(titleFromCollection).click();
            sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
            sa.assertTrue(detailsPage.getMediaTitle().equals(titleFromCollection), titleFromCollection + " Content was not opened");
            detailsPage.clickCloseButton();
        }
        sa.assertAll();
    }
}