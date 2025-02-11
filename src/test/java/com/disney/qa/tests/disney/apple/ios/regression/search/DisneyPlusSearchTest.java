package com.disney.qa.tests.disney.apple.ios.regression.search;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.RatingConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.DisneyAbstractPage.THREE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

public class DisneyPlusSearchTest extends DisneyBaseTest {

    private static final String BLUEY = "Bluey";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";

    private static final String RECENT_SEARCH_NOT_FOUND_ERROR_MESSAGE = "recent search was not displayed";
    private static final String RECENT_SEARCH_FOUND_ERROR_MESSAGE = "recent search was displayed";
    private static final String CONTENT_NOT_FOUND_IN_RECENT_SEARCH_ERROR_MESSAGE = "content was not displayed in " +
            "recent search results";

    @DataProvider(name = "collectionNames")
    public Object[][] collections() {
        return new Object[][]{
                {MOVIES}, {SERIES}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68278"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyRecentSearchWhenNoSearchMade() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.getSearchBar().click();
        sa.assertFalse(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_FOUND_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67325"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, TestGroup.SEARCH, US})
    public void verifyMaintainSearchQuery() {
        String content = "The Simpsons";
        List<String> firstResultList = new ArrayList<>();
        List<String> secondResultList = new ArrayList<>();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        searchPage.getCancelButton();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(content);
        searchPage.getDisplayedTitles().forEach(searchResult -> firstResultList.add(searchResult.getText()));

        searchPage.getDynamicAccessibilityId(content).click();
        Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getBackArrow().click();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.getDisplayedTitles()
                .subList(0, firstResultList.size())
                .forEach(searchResult -> secondResultList.add(searchResult.getText()));

        Assert.assertTrue(firstResultList.containsAll(secondResultList),
                "Search Result list doesn't match with the previous navigation");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68290"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void clearRecentSearches() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        //User made search
        searchPage.searchForMedia(BLUEY);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getBackArrow().click();
        //Empty string to clear the keys
        searchPage.searchForMedia("");
        searchPage.getCancelButton().click();
        homePage.clickSearchIcon();
        searchPage.getSearchBar().click();
        sa.assertTrue(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_NOT_FOUND_ERROR_MESSAGE);
        searchPage.getCancelButton().click();
        homePage.clickHomeIcon();

        //click x button to clear recent searches
        homePage.clickSearchIcon();
        searchPage.getSearchBar().click();
        searchPage.tapRecentSearchClearButton();
        searchPage.getSearchBar().click();
        sa.assertFalse(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_FOUND_ERROR_MESSAGE);
        searchPage.getCancelButton().click();

        //next time user select search bar, recent search is no longer displayed
        homePage.clickHomeIcon();
        homePage.clickSearchIcon();
        searchPage.getSearchBar().click();
        sa.assertFalse(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_FOUND_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67319"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifySearchBarUI() {
        String title = "Simpson";
        String placeholderError = "Placeholder text is not as expected";
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        searchPage.clickSearchIcon();

        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        Assert.assertEquals(searchPage.getSearchBar().getText(),
                searchPage.getPlaceholderText(),
                placeholderError);
        Assert.assertTrue(searchPage.getMagnifyingGlassImage().isPresent(),
                "Magnifying glass image was not present on search page");

        searchPage.getSearchBar().click();
        Assert.assertTrue(searchPage.getCancelButton().isPresent(), "Cancel button was not present on search page");
        Assert.assertTrue(searchPage.getMagnifyingGlassImage().isPresent(),
                "Magnifying glass image was not present on search page");
        Assert.assertTrue(searchPage.getKeyboardSearchButton().isPresent(),
                "Keyboard was not shown when entering the search term");
        Assert.assertEquals(searchPage.getSearchBar().getText(),
                searchPage.getPlaceholderText(),
                placeholderError);
        searchPage.searchForMedia(title);
        Assert.assertNotEquals(searchPage.getSearchBar().getText(),
                searchPage.getPlaceholderText(),
                placeholderError);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68282"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRecentSearchInitiatesValidSearch() {
        String media = "Turning Red";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        //User made different search
        searchPage.searchForMedia(BLUEY);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getBackArrow().click();
        searchPage.searchForMedia(media);
        results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getBackArrow().click();

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();

        //user selects a Recent Search from the Recent Searches list
        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.getSearchBar().click();
        sa.assertTrue(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_NOT_FOUND_ERROR_MESSAGE);
        searchPage.tapTitleUnderRecentSearch(media);
        results = searchPage.getDisplayedTitles();
        results.get(0).click();

        //verify selected recent search item opened
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getMediaTitle().equals(media), "selected recent search item was not opened");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68280"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRecentSearchShowsMaxTenResults() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        //Add 11 search result in recent search list
        addContentInSearchResults(getMedia());

        searchPage.getClearTextBtn().click();
        searchPage.getSearchBar().click();

        //Verify that the after searching 11 content, only last latest 10 visible in list and the first one is not visible
        sa.assertFalse(searchPage.getStaticTextByLabel(getMedia().get(0)).isPresent(), "First content is displayed");
        for (int j = getMedia().size() - 1; j > 0; j--) {
            sa.assertTrue(searchPage.getStaticTextByLabel(getMedia().get(j)).isPresent(), CONTENT_NOT_FOUND_IN_RECENT_SEARCH_ERROR_MESSAGE);
            if (j == getMedia().size() / 2) {
                searchPage.swipeInRecentSearchResults(Direction.UP);
                //After Swipe also verify that the first content is not visible
                sa.assertFalse(searchPage.getStaticTextByLabel(getMedia().get(0)).isPresent(), "First content is displayed");
            }
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67956"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US}, dataProvider = "collectionNames", enabled = false)
    public void verifyScrollAndDropdownForSearchContentLandingPage(@NotNull String collectionName) {
        String filterValue = "Comedy";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

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
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyOriginalsLandingPageUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB);

        setAppToHomeScreen(basicAccount);
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.clickOriginalsTab();
        Assert.assertTrue(originalsPage.isOpened(), ORIGINALS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(originalsPage.getBackButton().isElementPresent(), BACK_BUTTON_NOT_DISPLAYED);

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
                    sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75482"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyOriginalsLandingPageUIForKids() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        setAppToHomeScreen(getAccount(), KIDS_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.clickOriginalsTab();
        Assert.assertTrue(originalsPage.isOpened(), ORIGINALS_PAGE_NOT_DISPLAYED);
        //Verify Back button is present
        sa.assertTrue(originalsPage.getBackButton().isElementPresent(), BACK_BUTTON_NOT_DISPLAYED);

        //To get the collections details of Originals from API
        ArrayList<Container> collections = getDisneyAPIPage(DisneyEntityIds.ORIGINALS_PAGE.getEntityId(), true);
        collections.forEach(item -> {
            ExtendedWebElement collectionName = searchPage.getTypeOtherByLabel(item.getVisuals().getName());
            swipe(collectionName, Direction.UP, 2, 500);
            //Verify that collection is present in page
            if (collectionName.isPresent()) {
                //To get the all movie/series title under collection from API
                if (item.getItems().size() > 0) {
                    String titleFromCollection = item.getItems().get(0).getVisuals().getTitle();
                    swipe(originalsPage.getCollection(item.getId()), Direction.UP, 2, 500);
                    originalsPage.swipeInCollectionContainer(originalsPage.getTypeCellLabelContains(titleFromCollection), item.getId());
                    sa.assertTrue(originalsPage.getTypeCellLabelContains(titleFromCollection).isPresent(),
                            titleFromCollection + " was not found for " + collectionName.getText() + " collection");
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
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US}, dataProvider = "collectionNames", enabled = false)
    public void verifySwipeBehaviorForContentLandingPage(String collectionName) {
        String comedyFilterValue = "Comedy";
        String kidsFilterValue = "Kids";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.clickOriginalsTab();
        sa.assertTrue(originalsPage.isOpened(), ORIGINALS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        searchPage.getNavBackArrow().click();

        if (collectionName.equalsIgnoreCase("movies")) {
            searchPage.clickMoviesTab();
        } else {
            searchPage.clickSeriesTab();
        }
        sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
        sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);

        List<ExtendedWebElement> featuredFilterResults = searchPage.getDisplayedTitles();
        String tenthFeaturedResult = featuredFilterResults.get(10).getText();
        scrollDown();

        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);

            searchPage.swipeContentPageFilter(Direction.LEFT);
            searchPage.getTypeButtonByLabel(kidsFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);

            List<ExtendedWebElement> kidsResults = searchPage.getDisplayedTitles();
            String firstKidsResult = kidsResults.get(0).getText();

            searchPage.swipeContentPageFilter(Direction.RIGHT);
            searchPage.getTypeButtonByLabel(comedyFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);

            List<ExtendedWebElement> comedyResults = searchPage.getDisplayedTitles();
            sa.assertFalse(comedyResults.get(0).getText().equalsIgnoreCase(firstKidsResult), "Displayed titles are not different.");
            sa.assertFalse(comedyResults.get(20).getText().equalsIgnoreCase(tenthFeaturedResult), "Displayed titles are not different.");
        } else {
            sa.assertFalse(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);

            searchPage.clickContentPageFilterDropDownAtMiddleTop();
            searchPage.swipeItemPicker(Direction.UP);
            searchPage.getStaticTextByLabel(kidsFilterValue).click();
            sa.assertTrue(searchPage.getStaticTextByLabel(collectionName).isPresent(), "Page header '" + collectionName + "' was not found");
            sa.assertTrue(searchPage.getNavBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);

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
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchResultContainsRatingAndYearDetails() {
            String media = "M";
            String movie = "The Marvels";
            String series = "The Simpsons";

            SoftAssert sa = new SoftAssert();
            DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
            DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

            ExploreContent seriesApiContent = getSeriesApi(DisneyEntityIds.SERIES.getEntityId(),
                    DisneyPlusBrandIOSPageBase.Brand.DISNEY);
            ExploreContent movieApiContent = getMovieApi(DisneyEntityIds.MARVELS.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);

            setAppToHomeScreen(getAccount());
            homePage.clickSearchIcon();
            Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

            //User made search with one letter
            String contentTitle = getFirstSearchResults(media);
            sa.assertTrue(contentTitle.startsWith(media), "Result doesnt start with letter " + media);

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
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
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
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(searchPage.isExploreTitleDisplayed(SHORT_TIMEOUT), "Explore title is not displayed");

        searchPage.clickFirstCollection();
        Assert.assertTrue(brandIOSPageBase.isOpened(), collectionPageDidNotOpen);
        sa.assertTrue(brandIOSPageBase.isCollectionBrandImageExpanded(), collectionLogoNotExpanded);
        sa.assertTrue(brandIOSPageBase.getBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(brandIOSPageBase.isArtworkBackgroundPresent(), "Artwork images is not present");
        sa.assertTrue(brandIOSPageBase.isCollectionTitleDisplayed(), "Collection title not displayed");

        sa.assertTrue(brandIOSPageBase.isCollectionImageCollapsedFromSwipe(Direction.UP, swipeAttempt),
                "Image not collapsed after swipe");
        sa.assertTrue(brandIOSPageBase.getBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(brandIOSPageBase.isCollectionBrandImageCollapsed(), "Collection brand logo is not collapsed");
        brandIOSPageBase.swipeInCollectionTillImageExpand(Direction.DOWN, swipeAttempt);
        sa.assertTrue(brandIOSPageBase.isCollectionBrandImageExpanded(), collectionLogoNotExpanded);
        brandIOSPageBase.getBackArrow().click();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.clickFirstCollection();
        sa.assertTrue(brandIOSPageBase.isOpened(), collectionPageDidNotOpen);
        brandIOSPageBase.clickFirstNoLiveEvent();
        sa.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player didn't open");
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);
        clickElementAtLocation(detailsPage.getBackArrow(), 50, 50);
        sa.assertTrue(brandIOSPageBase.isOpened(), collectionPageDidNotOpen);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75718"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHideRestrictedTitlesInSearchResults() {
        String contentTitle = "Deadpool & Wolverine";

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getAccountApi().editContentRatingProfileSetting(getAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_TV14);

        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        homePage.getSearchNav().click();
        searchPage.searchForMedia(contentTitle);
        sa.assertTrue(searchPage.isPCONRestrictedErrorMessagePresent(),
                "PCON restricted title message not present for TV-MA profile");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(contentTitle),
                "No results found message was not as expected for TV-MA profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67313"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchExploreUIElements() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        sa.assertTrue(searchPage.getOriginalsTile().isPresent(), "Originals tile not found");
        sa.assertTrue(searchPage.getMovieTile().isPresent(), "Movies tile not found");
        sa.assertTrue(searchPage.getSeriesTile().isPresent(), "Series tile not found");
        sa.assertTrue(searchPage.isExploreTitleDisplayed(SHORT_TIMEOUT), "Explore title is not displayed");
        sa.assertTrue(searchPage.isCollectionTitleDisplayed(), "Collection title not displayed");

        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            searchPage.swipeUp(1500);
            sa.assertFalse(searchPage.getOriginalsTile().isPresent(), "Originals tile found after scrolling");
            sa.assertFalse(searchPage.getMovieTile().isPresent(), "Movies tile found after scrolling");
            sa.assertFalse(searchPage.getSeriesTile().isPresent(), "Series tile found after scrolling");
            sa.assertFalse(searchPage.isExploreTitleDisplayed(SHORT_TIMEOUT),
                    "Explore title is displayed after scrolling");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67317"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyBackBehaviorForContentLandingPage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(searchPage.isExploreTitleDisplayed(SHORT_TIMEOUT), "Explore title is not displayed");

        sa.assertTrue(searchPage.getOriginalsTile().isPresent(), "Originals tile not found");
        sa.assertTrue(searchPage.getMovieTile().isPresent(), "Movies tile not found");
        sa.assertTrue(searchPage.getSeriesTile().isPresent(), "Series tile not found");

        searchPage.clickOriginalsTab();
        Assert.assertTrue(originalsPage.isOpened(), ORIGINALS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(searchPage.getBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        searchPage.getBackArrow().click();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.clickMoviesTab();
        sa.assertTrue(searchPage.getStaticTextByLabel(MOVIES).isPresent(), "Movies page did not open");
        sa.assertTrue(searchPage.getBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        searchPage.getBackArrow().click();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.clickSeriesTab();
        sa.assertTrue(searchPage.getStaticTextByLabel(SERIES).isPresent(), "Series Page did not open");
        sa.assertTrue(searchPage.getBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        searchPage.getBackArrow().click();
        sa.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68286"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRecentSearchPerProfile() {
        //Creating a lists of shows to search
        List<String> listOfShowsPrimaryProfile = Arrays.asList("Toy Story", "Bluey", "Doctor Strange");
        List<String> listOfShowsSecondaryProfile = Arrays.asList("The Little Mermaid", "Planes", "Big City Greens");

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder()
                .disneyAccount(getAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getAccount().getProfileLang())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(false)
                .build());

        setAppToHomeScreen(getAccount(), DEFAULT_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        addContentInSearchResults(listOfShowsPrimaryProfile);

        searchPage.getClearTextBtn().click();
        searchPage.getSearchBar().click();
        Assert.assertTrue(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_NOT_FOUND_ERROR_MESSAGE);

        for (int j = listOfShowsPrimaryProfile.size() - 1; j > 0; j--) {
            sa.assertTrue(searchPage.getStaticTextByLabel(listOfShowsPrimaryProfile.get(j)).isPresent(),
                    listOfShowsPrimaryProfile.get(j) + " " + CONTENT_NOT_FOUND_IN_RECENT_SEARCH_ERROR_MESSAGE);
        }

        searchPage.getCancelButton().click();
        searchPage.clickMoreTab();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);

        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        addContentInSearchResults(listOfShowsSecondaryProfile);
        searchPage.getClearTextBtn().click();
        searchPage.getSearchBar().click();
        Assert.assertTrue(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_NOT_FOUND_ERROR_MESSAGE);

        for (int j = listOfShowsSecondaryProfile.size() - 1; j > 0; j--) {
            sa.assertTrue(searchPage.getStaticTextByLabel(listOfShowsSecondaryProfile.get(j)).isPresent(),
                    listOfShowsSecondaryProfile.get(j) + " " + CONTENT_NOT_FOUND_IN_RECENT_SEARCH_ERROR_MESSAGE);
            sa.assertFalse(searchPage.getStaticTextByLabel(listOfShowsPrimaryProfile.get(j)).isPresent(),
                    listOfShowsPrimaryProfile.get(j) + " - Default profile content was displayed in recent " +
                            "search results");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75502"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMoviesLandingPageContentMaturityRatingRestriction() {
        int apiTitlesSearchLimit = 400;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage = initPage(DisneyPlusMediaCollectionIOSPageBase.class);

        //Assign TV-Y content maturity rating
        getAccountApi().editContentRatingProfileSetting(getAccount(),
                getLocalizationUtils().getRatingSystem(),
                RatingConstant.Rating.TV_Y.getContentRating());

        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.clickMoviesTab();
        mediaCollectionPage.waitForPresenceOfAnElement(mediaCollectionPage.getMoviesHeader());

        //Compare default movies displayed in the UI against Explore API movies for TV-Y rating
        String selectedCategory = mediaCollectionPage.getSelectedCategoryFilterName();
        String setId = getSetIdFromApi(DisneyEntityIds.MOVIES.getEntityId(), selectedCategory);
        List<String> filteredListOfTitlesByRating = getContainerTitlesWithGivenRatingFromApi(
                setId, apiTitlesSearchLimit, RatingConstant.Rating.TV_Y.getContentRating());
        Assert.assertTrue(mediaCollectionPage.getCollectionTitles().stream()
                        .anyMatch(filteredListOfTitlesByRating::contains),
                "API fetched titles don't contain UI titles");
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69557"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyBrandPageContentMaturityRatingRestriction() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage = initPage(DisneyPlusMediaCollectionIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = new DisneyPlusBrandIOSPageBase(getDriver());

        SoftAssert sa = new SoftAssert();
        // Edit to get TV-Y maturity rating content
        getAccountApi().editContentRatingProfileSetting(getAccount(),
                getLocalizationUtils().getRatingSystem(),
                RatingConstant.Rating.TV_Y.getContentRating());
        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();
        homePage.clickDisneyTile();
        Assert.assertTrue(brandPage.isOpened(), "Disney brand page was not opened");

        // Get Container ID
        ArrayList<Container> collections = getDisneyAPIPage(DisneyEntityIds.DISNEY_PAGE.getEntityId());
        String heroCarouselId = "";
        try{
            heroCarouselId = collections.get(0).getId();
        } catch (Exception e){
            throw new SkipException("Skipping test, hero carousel collection id not found:- " +  e.getMessage());
        }

        //Compare default content displayed in the UI against Explore API Disney brand page for TV-Y rating
        String selectedCategory = mediaCollectionPage.getSelectedCategoryFilterNameForOriginalsAndBrands();
        LOGGER.info("selectedCategory {}", selectedCategory);

        String setId = getSetIdFromApi(DisneyEntityIds.DISNEY_PAGE.getEntityId(), selectedCategory);
        LOGGER.info("setId {}", setId);

        List<String> filteredListOfTitlesByRating = getContainerTitlesWithGivenRatingFromApi(setId, 500,
                RatingConstant.Rating.TV_Y.getContentRating());
        LOGGER.info("filteredListOfTitlesByRating {}", filteredListOfTitlesByRating);

        if(!filteredListOfTitlesByRating.isEmpty()) {
            String finalHeroCarouselId = heroCarouselId;
            filteredListOfTitlesByRating.forEach(item -> {
                if (brandPage.getTypeCellLabelContains(item).isElementNotPresent(THREE_SEC_TIMEOUT)) {
                    swipeInContainer(homePage.getHeroCarouselContainer(finalHeroCarouselId), Direction.LEFT, 500);
                }
                sa.assertTrue(brandPage.getTypeCellLabelContains(item).isPresent(), "Title from Api not found in UI " + item);
            });
        } else {
            LOGGER.info("Originals Collection Api results are empty");
            sa.assertTrue(searchPage.isPCONRestrictedErrorHeaderPresent(),
                    "PCON restricted title message was not present");
            sa.assertTrue(searchPage.isPCONRestrictedErrorMessagePresent(),
                    "PCON restricted title message was not present");
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

    private void addContentInSearchResults(List<String> contentList) {
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        IntStream.range(0, contentList.size()).forEach(i -> {
            if (searchPage.getClearTextBtn().isPresent(SHORT_TIMEOUT)) {
                searchPage.getClearTextBtn().click();
            }
            searchPage.searchForMedia(contentList.get(i));
            searchPage.getDynamicAccessibilityId(contentList.get(i)).click();
            Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);
            detailsPage.getBackArrow().click();
        });
    }
}
