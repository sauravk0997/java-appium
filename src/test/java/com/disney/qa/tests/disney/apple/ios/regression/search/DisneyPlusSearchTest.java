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
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DisneyPlusSearchTest extends DisneyBaseTest {

    private static final String BLUEY = "Bluey";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";

    @DataProvider(name = "collectionNames")
    public Object[][] collections() {
        return new Object[][]{
                {MOVIES}, {SERIES}
        };
    }

    @Maintainer("dconyers")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67343"})
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68282"})
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68290"})
    @Test(description = "Search - Recent Searches - Clear Recent Search by clicking on the X Icon", groups = {"Search", TestGroup.PRE_CONFIGURATION }, enabled = false)
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68282"})
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
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68280"})
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
                searchPage.getClearText().click();
            }
            searchPage.searchForMedia(getMedia().get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
            detailsPage.getBackArrow().click();
        });

        searchPage.getClearText().click();
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67956"})
    @Test(description = "Search - Content Type Landing Pages - Scroll Behavior & Dropdown Behavior", groups = {"Search", TestGroup.PRE_CONFIGURATION}, dataProvider = "collectionNames", enabled = false)
    public void verifyScrollAndDropdownForSearchContentLandingPage(@NotNull String collectionName) {
        String filterValue = "Comedy";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        if(collectionName.equalsIgnoreCase("movies")){
            searchPage.clickMoviesTab();
        }else{
            searchPage.clickSeriesTab();
        }

        //Verify Page header is present
        sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" +collectionName + "' was not found");

        if(R.CONFIG.get(DEVICE_TYPE).equals(TABLET)){
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
            scrollDown();
            //verify after scrolling down also, Page header and Filter header tabbar is present
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" +collectionName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
            //Verify after selecting any filter value also, Page header and Filter header tabbar is present
            searchPage.getTypeButtonByLabel(filterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" +collectionName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
        }else{
            sa.assertTrue(searchPage.isContentPageFilterDropDownPresent(), "Content Page Filter Dropdown was not found");
            scrollDown();
            //verify after scrolling down also, Filter dropdown is present
            sa.assertTrue(searchPage.isContentPageFilterDropDownAtMiddleTopPresent(), "Content Page Filter Dropdown not present after scroll");
            //Verify after selecting any filter value also, it navigate to top and Filter dropdown is present
            searchPage.clickContentPageFilterDropDownAtMiddleTop();
            searchPage.getStaticTextByLabel(filterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" +collectionName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterDropDownPresent(), "Content Page Filter Dropdown was not found");
        }
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67958"})
    @Test(description = "Search - Originals Landing Page - UI Elements", groups = {"Search", TestGroup.PRE_CONFIGURATION }, enabled = false)
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
        //Verify Back button is present
        sa.assertTrue(originalsPage.getBackArrow().isPresent(), "Back button was not found");

        //To get the collections details of Originals from API
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
            Assert.assertTrue(originalsPage.getDynamicCellByLabel(titleFromCollection).isPresent(), titleFromCollection + " was not found for " + set.getContent() + " collection");
            //verify that correct titles of that collection opened in app, verify with 1 titles
            originalsPage.getDynamicCellByLabel(titleFromCollection).click();
            sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
            sa.assertTrue(detailsPage.getMediaTitle().equals(titleFromCollection), titleFromCollection + " Content was not opened");
            detailsPage.clickCloseButton();
        }
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67950"})
    @Test(description = "Search - Content Type Landing Pages - Swipe Behavior", groups = {"Search", TestGroup.PRE_CONFIGURATION}, dataProvider = "collectionNames", enabled = false)
    public void verifySwipeBehaviorForContentLandingPage(String collectionName) {
        String comedyFilterValue = "Comedy";
        String kidsFilterValue = "Kids";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        searchPage.clickOriginalsTab();
        sa.assertTrue(originalsPage.isOriginalPageLoadPresent(), "Originals Page did not open.");
        sa.assertTrue(searchPage.getBackButtonOnContentPage().isPresent(), "Back button is not present.");
        searchPage.getBackButtonOnContentPage().click();

        if (collectionName.equalsIgnoreCase("movies")) {
            searchPage.clickMoviesTab();
        } else {
            searchPage.clickSeriesTab();
        }
        sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
        sa.assertTrue(searchPage.getBackButtonOnContentPage().isPresent(), "Back button is not present.");

        List<ExtendedWebElement> featuredFilterResults = searchPage.getDisplayedTitles();
        String tenthFeaturedResult = featuredFilterResults.get(10).getText();
        scrollDown();

        if(R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getBackButtonOnContentPage().isPresent(), "Back button is not present.");

            searchPage.swipeContentPageFilter(Direction.LEFT);
            searchPage.getTypeButtonByLabel(kidsFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getBackButtonOnContentPage().isPresent(), "Back button is not present.");

            List<ExtendedWebElement> kidsResults = searchPage.getDisplayedTitles();
            String firstKidsResult = kidsResults.get(0).getText();

            searchPage.swipeContentPageFilter(Direction.RIGHT);
            searchPage.getTypeButtonByLabel(comedyFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getBackButtonOnContentPage().isPresent(), "Back button is not present.");

            List<ExtendedWebElement> comedyResults = searchPage.getDisplayedTitles();
            sa.assertFalse(comedyResults.get(0).getText().equalsIgnoreCase(firstKidsResult), "Displayed titles are not different.");
            sa.assertFalse(comedyResults.get(20).getText().equalsIgnoreCase(tenthFeaturedResult), "Displayed titles are not different.");
        } else {
            sa.assertFalse(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getBackButtonOnContentPage().isPresent(), "Back button is not present.");

            searchPage.clickContentPageFilterDropDownAtMiddleTop();
            searchPage.swipeItemPicker(Direction.UP);
            searchPage.getStaticTextByLabel(kidsFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getBackButtonOnContentPage().isPresent(), "Back button is not present.");

            List<ExtendedWebElement> kidsResults = searchPage.getDisplayedTitles();
            String firstComedyResult = kidsResults.get(0).getText();

            scrollDown();
            searchPage.clickContentPageFilterDropDownAtMiddleTop();
            searchPage.swipeItemPicker(Direction.DOWN);
            searchPage.getStaticTextByLabel(comedyFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was  not found");

            List<ExtendedWebElement> comedyResults = searchPage.getDisplayedTitles();
            sa.assertFalse(comedyResults.get(0).getText().equalsIgnoreCase(firstComedyResult), "Displayed titles are not different.");
            sa.assertFalse(comedyResults.get(10).getText().equalsIgnoreCase(tenthFeaturedResult), "Displayed titles are not different.");
        }
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67303"})
    @Test(description = "Search - Search Results Contains Rating And Released Year details (Handset Only)", groups = {"Search", TestGroup.PRE_CONFIGURATION })
    public void verifySearchResultContainsRatingAndYearDetails() {
        if(R.CONFIG.get(DEVICE_TYPE).equals(PHONE)){
            String media = "M";
            String movie = "The Marvels";
            String series = "The Simpsons";
            String seriesID = "3ZoBZ52QHb4x";
            SoftAssert sa = new SoftAssert();
            DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
            DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
            setAppToHomeScreen(getAccount());
            homePage.clickSearchIcon();
            Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

            //User made search with one letter
            String contentTitle = getFirstSearchResults(media);
            sa.assertTrue(contentTitle.startsWith(media), "Result dosent start with letter " + media);
            //Verify search result have Rating and released year details also
            validateRatingAndReleasedYearDetails(sa, contentTitle, getMovieRatingDetails(contentTitle), getMovieReleasedYearDetails(contentTitle));

            //User made search with movie name
            contentTitle = getFirstSearchResults(movie);
            sa.assertTrue(contentTitle.equals(movie), movie + " was not displayed in search results");
            //Verify search result have Rating and released year details also
            validateRatingAndReleasedYearDetails(sa, contentTitle, getMovieRatingDetails(contentTitle), getMovieReleasedYearDetails(contentTitle));

            //User made search with series name
            contentTitle = getFirstSearchResults(series);
            sa.assertTrue(contentTitle.equals(series), series + " was not displayed in search results");
            String rating = getSearchApi().getSeries(seriesID, getAccount().getCountryCode(), getAccount().getProfileLang()).getSeriesRatingsValue();
            String releasedYear = String.valueOf(getSearchApi().getSeries(seriesID, getAccount().getCountryCode(), getAccount().getProfileLang()).getReleaseYear());
            //Verify search result have Rating and released year details also
            validateRatingAndReleasedYearDetails(sa, contentTitle, rating, releasedYear);
            sa.assertAll();
        }
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

    private String getFirstSearchResults(String title){
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        searchPage.searchForMedia(title);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        //Verify correct result are displayed after search
        Assert.assertTrue(results.size()>0, "Search result not displayed");
        return results.get(0).getText().split(",")[0];
    }

    private void validateRatingAndReleasedYearDetails(SoftAssert sa, String title, String rating, String releasedYear){
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        sa.assertTrue(searchPage.getRatingAndYearDetailsFromSearchResults(title).contains(rating), "Rating details was not found in search results for " + title);
        sa.assertTrue(searchPage.getRatingAndYearDetailsFromSearchResults(title).contains(releasedYear), "Released year details was not found in search results " + title);
    }

    public String getMovieRatingDetails(String title){
        return getSearchApi().getMovie(title, getAccount()).getContentRatingsValue();
    }

    public String getMovieReleasedYearDetails(String title){
        return getSearchApi().getMovie(title, getAccount()).getReleaseDate();
    }
}