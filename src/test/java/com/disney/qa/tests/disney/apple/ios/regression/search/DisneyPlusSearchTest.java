package com.disney.qa.tests.disney.apple.ios.regression.search;

import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DisneyPlusSearchTest extends DisneyBaseTest {

    private static final String BLUEY = "Bluey";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";
    private static final String SEARCH_PAGE_DID_NOT_OPEN = "Search page did not open";
    private static final String DETAIL_PAGE_DID_NOT_OPEN = "Detail page did not open";
    private static final String BACK_BUTTON_ERROR_MESSAGE = "Back button is not present";

    @DataProvider(name = "collectionNames")
    public Object[][] collections() {
        return new Object[][]{
                {MOVIES}, {SERIES}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68278"})
    @Test(description = "'Recent Searches' is not shown when user has made no Recent Searches", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE})
    public void verifyRecentSearchWhenNoSearchMade() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        searchPage.getSearchBar().click();
        sa.assertFalse(searchPage.isRecentSearchDisplayed(), "recent search was displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68290"})
    @Test(description = "Search - Recent Searches - Clear Recent Search by clicking on the X Icon", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void clearRecentSearches() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        //User made search
        searchPage.searchForMedia(BLUEY);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAIL_PAGE_DID_NOT_OPEN);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68282"})
    @Test(description = "Search - Recent Searches - Selecting a Recent Search initiates that Search", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION})
    public void verifyRecentSearchInitiatesValidSearch() {
        String media = "Turning Red";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        //User made different search
        searchPage.searchForMedia(BLUEY);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAIL_PAGE_DID_NOT_OPEN);
        detailsPage.getBackArrow().click();
        searchPage.searchForMedia(media);
        results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAIL_PAGE_DID_NOT_OPEN);
        detailsPage.getBackArrow().click();

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();

        //user selects a Recent Search from the Recent Searches list
        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.getSearchBar().click();
        sa.assertTrue(searchPage.isRecentSearchDisplayed(), "recent search was not displayed");
        searchPage.tapTitleUnderRecentSearch(media);
        results = searchPage.getDisplayedTitles();
        results.get(0).click();

        //verify selected recent search item opened
        sa.assertTrue(detailsPage.isOpened(), DETAIL_PAGE_DID_NOT_OPEN);
        sa.assertTrue(detailsPage.getMediaTitle().equals(media), "selected recent search item was not opened");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68280"})
    @Test(description = "Search - Recent Searches - Show 10 Results Max with the Ability to Scroll Up and Down", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION})
    public void verifyRecentSearchShowsMaxTenResults() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        //Add 11 search result in recent search list
        IntStream.range(0, getMedia().size()).forEach(i -> {

            if (searchPage.getClearTextBtn().isPresent(SHORT_TIMEOUT)) {
                searchPage.getClearTextBtn().click();
            }
            searchPage.searchForMedia(getMedia().get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            sa.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAIL_PAGE_DID_NOT_OPEN);
            detailsPage.getBackArrow().click();
        });

        searchPage.getClearTextBtn().click();
        searchPage.getSearchBar().click();

        //Verify that the after searching 11 content, only last latest 10 visible in list and the first one is not visible
        sa.assertFalse(searchPage.getStaticTextByLabel(getMedia().get(0)).isPresent(), "First content is displayed");
        for (int j = getMedia().size() - 1; j > 0; j--) {
            sa.assertTrue(searchPage.getStaticTextByLabel(getMedia().get(j)).isPresent(), "recent search content was not displayed in recent search results");
            if (j == getMedia().size() / 2) {
                searchPage.swipeInRecentSearchResults(Direction.UP);
                //After Swipe also verify that the first content is not visible
                sa.assertFalse(searchPage.getStaticTextByLabel(getMedia().get(0)).isPresent(), "First content is displayed");
            }
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67956"})
    @Test(description = "Search - Content Type Landing Pages - Scroll Behavior & Dropdown Behavior", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION}, dataProvider = "collectionNames", enabled = false)
    public void verifyScrollAndDropdownForSearchContentLandingPage(@NotNull String collectionName) {
        String filterValue = "Comedy";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        if (collectionName.equalsIgnoreCase("movies")) {
            searchPage.clickMoviesTab();
        } else {
            searchPage.clickSeriesTab();
        }

        //Verify Page header is present
        sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");

        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
            scrollDown();
            //verify after scrolling down also, Page header and Filter header tabbar is present
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
            //Verify after selecting any filter value also, Page header and Filter header tabbar is present
            searchPage.getTypeButtonByLabel(filterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterHeaderPresent(), "Content Page Filter Header was not found");
        } else {
            sa.assertTrue(searchPage.isContentPageFilterDropDownPresent(), "Content Page Filter Dropdown was not found");
            scrollDown();
            //verify after scrolling down also, Filter dropdown is present
            sa.assertTrue(searchPage.isContentPageFilterDropDownAtMiddleTopPresent(), "Content Page Filter Dropdown not present after scroll");
            //Verify after selecting any filter value also, it navigate to top and Filter dropdown is present
            searchPage.clickContentPageFilterDropDownAtMiddleTop();
            searchPage.getStaticTextByLabel(filterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.isContentPageFilterDropDownPresent(), "Content Page Filter Dropdown was not found");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67958"})
    @Test(description = "Search - Originals Landing Page - UI Elements", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION})
    public void verifyOriginalsLandingPageUI() throws URISyntaxException, JsonProcessingException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB);
        setAppToHomeScreen(basicAccount);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.clickOriginalsTab();

        //Verify Original page opened
        sa.assertTrue(originalsPage.isOriginalPageLoadPresent(), "Original content page was not opened");
        //Verify Back button is present
        sa.assertTrue(originalsPage.getBackButton().isElementPresent(), BACK_BUTTON_ERROR_MESSAGE);

        //To get the collections details of Originals from API
        ArrayList<Container> collections = getDisneyAPIPage(DisneyEntityIds.ORIGINALS_PAGE.getEntityId());
        collections.forEach(item -> {
            ExtendedWebElement collectionName = searchPage.getTypeOtherByLabel(item.getVisuals().getName());
            swipePageTillElementPresent(collectionName, 2, null, Direction.UP, 500);
            //Verify that collection is present in page
            if (collectionName.isPresent()) {
                //To get the all movie/series title under collection from API
                if (item.getItems().size() > 0) {
                    String titleFromCollection = item.getItems().get(0).getVisuals().getTitle();
                    swipePageTillElementPresent(originalsPage.getCollection(item.getId()), 2, null, Direction.UP, 500);
                    originalsPage.swipeInCollectionContainer(originalsPage.getTypeCellLabelContains(titleFromCollection), item.getId());
                    sa.assertTrue(originalsPage.getTypeCellLabelContains(titleFromCollection).isPresent(), titleFromCollection + " was not found for " + collectionName.getText() + " collection");
                    //verify that correct titles of that collection opened in app, verify with 1 titles
                    originalsPage.getTypeCellLabelContains(titleFromCollection).click();
                    sa.assertTrue(detailsPage.isOpened(), DETAIL_PAGE_DID_NOT_OPEN);
                    sa.assertTrue(detailsPage.getMediaTitle().equals(titleFromCollection), titleFromCollection + " Content was not opened");
                    detailsPage.clickCloseButton();
                } else {
                    sa.assertTrue(item.getItems().size() > 0, "API returned empty collection: " + item.getVisuals().getName());
                }
            } else {
                sa.assertTrue(collectionName.isPresent(), String.format("%s collection was not found", item.getVisuals().getName()));
            }
        });
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67950"})
    @Test(description = "Search - Content Type Landing Pages - UI Elements & Filtering", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION}, dataProvider = "collectionNames", enabled = false)
    public void verifySwipeBehaviorForContentLandingPage(String collectionName) {
        String comedyFilterValue = "Comedy";
        String kidsFilterValue = "Kids";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        searchPage.clickOriginalsTab();
        sa.assertTrue(originalsPage.isOriginalPageLoadPresent(), "Originals Page did not open.");
        sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);
        searchPage.getNavBackArrow().click();

        if (collectionName.equalsIgnoreCase("movies")) {
            searchPage.clickMoviesTab();
        } else {
            searchPage.clickSeriesTab();
        }
        sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
        sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);

        List<ExtendedWebElement> featuredFilterResults = searchPage.getDisplayedTitles();
        String tenthFeaturedResult = featuredFilterResults.get(10).getText();
        scrollDown();

        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);

            searchPage.swipeContentPageFilter(Direction.LEFT);
            searchPage.getTypeButtonByLabel(kidsFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);

            List<ExtendedWebElement> kidsResults = searchPage.getDisplayedTitles();
            String firstKidsResult = kidsResults.get(0).getText();

            searchPage.swipeContentPageFilter(Direction.RIGHT);
            searchPage.getTypeButtonByLabel(comedyFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);

            List<ExtendedWebElement> comedyResults = searchPage.getDisplayedTitles();
            sa.assertFalse(comedyResults.get(0).getText().equalsIgnoreCase(firstKidsResult), "Displayed titles are not different.");
            sa.assertFalse(comedyResults.get(20).getText().equalsIgnoreCase(tenthFeaturedResult), "Displayed titles are not different.");
        } else {
            sa.assertFalse(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);

            searchPage.clickContentPageFilterDropDownAtMiddleTop();
            searchPage.swipeItemPicker(Direction.UP);
            searchPage.getStaticTextByLabel(kidsFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67303"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION})
    public void verifySearchResultContainsRatingAndYearDetails() throws URISyntaxException, JsonProcessingException {
            String media = "M";
            String movie = "The Marvels";
            String series = "The Simpsons";

            SoftAssert sa = new SoftAssert();
            DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
            DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

            ExploreContent seriesApiContent = getDisneyApiSeries(DisneyEntityIds.SERIES.getEntityId());
            ExploreContent movieApiContent = getDisneyApiMovie(DisneyEntityIds.MARVELS.getEntityId());

            setAppToHomeScreen(getAccount());
            homePage.clickSearchIcon();
            Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

            //User made search with one letter
            String contentTitle = getFirstSearchResults(media);
            sa.assertTrue(contentTitle.startsWith(media), "Result dosent start with letter " + media);

            //User made search with movie name
            contentTitle = getFirstSearchResults(movie);
            sa.assertTrue(contentTitle.equals(movie), movie + " was not displayed in search results");
            //Verify search result have Rating and released year details also
            validateRatingAndReleasedYearDetails(sa, contentTitle, getApiMovieRatingDetails(movieApiContent),
                    getApiContentReleasedYearDetails(movieApiContent));

            //User made search with series name
            contentTitle = getFirstSearchResults(series);
            sa.assertTrue(contentTitle.equals(series), series + " was not displayed in search results");
            //Verify search result have Rating and released year details also
            validateRatingAndReleasedYearDetails(sa, contentTitle, getApiSeriesRatingDetails(seriesApiContent),
                    getApiContentReleasedYearDetails(seriesApiContent));
            sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67379"})
    @Test(description = "Search - Explore - Editorials & Collections", groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION})
    public void verifySearchExploreEditorialsAndCollections() {
        String collectionPageDidNotOpen = "User did not land on the collection page";
        String collectionLogoNotExpanded = "Collection brand logo is not expanded";
        int swipeAttempt = 2;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandIOSPageBase = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(searchPage.isExploreTitleDisplayed(SHORT_TIMEOUT), "Explore title is not displayed");

        searchPage.clickFirstCollection();
        Assert.assertTrue(brandIOSPageBase.isOpened(), collectionPageDidNotOpen);
        sa.assertTrue(brandIOSPageBase.isCollectionBrandImageExpanded(), collectionLogoNotExpanded);
        sa.assertTrue(brandIOSPageBase.getBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);
        sa.assertTrue(brandIOSPageBase.isArtworkBackgroundPresent(), "Artwork images is not present");
        sa.assertTrue(brandIOSPageBase.isCollectionTitleDisplayed(), "Collection title not displayed");

        sa.assertTrue(brandIOSPageBase.isCollectionImageCollapsedFromSwipe(Direction.UP, swipeAttempt), "Image not collapsed after swipe");
        sa.assertTrue(brandIOSPageBase.getBackArrow().isPresent(), BACK_BUTTON_ERROR_MESSAGE);
        sa.assertTrue(brandIOSPageBase.isCollectionBrandImageCollapsed(), "Collection brand logo is not collapsed");
        brandIOSPageBase.swipeInCollectionTillImageExpand(Direction.DOWN, swipeAttempt);
        sa.assertTrue(brandIOSPageBase.isCollectionBrandImageExpanded(), collectionLogoNotExpanded);
        brandIOSPageBase.getBackArrow().click();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        searchPage.clickFirstCollection();
        sa.assertTrue(brandIOSPageBase.isOpened(), collectionPageDidNotOpen);
        brandIOSPageBase.clickFirstCarouselPoster();
        sa.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAIL_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player didn't open");
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAIL_PAGE_DID_NOT_OPEN);
        pressByElement(detailsPage.getBackArrow(), 1);
        sa.assertTrue(brandIOSPageBase.isOpened(), collectionPageDidNotOpen);
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

    private String getFirstSearchResults(String title) {
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        searchPage.searchForMedia(title);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        //Verify correct result are displayed after search
        Assert.assertTrue(results.size() > 0, "Search result not displayed");
        return results.get(0).getText().split(",")[0];
    }

    private void validateRatingAndReleasedYearDetails(SoftAssert sa, String title, String rating, String releasedYear) {
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        sa.assertTrue(searchPage.getRatingAndYearDetailsFromSearchResults(title).contains(rating), "Rating details was not found in search results for " + title);
        sa.assertTrue(searchPage.getRatingAndYearDetailsFromSearchResults(title).contains(releasedYear), "Released year details was not found in search results " + title);
    }
}