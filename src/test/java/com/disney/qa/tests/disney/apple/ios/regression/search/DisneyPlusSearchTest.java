package com.disney.qa.tests.disney.apple.ios.regression.search;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.constant.RatingConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.RatingConstant.FRANCE;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusSearchTest extends DisneyBaseTest {

    private static final String BLUEY = "Bluey";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";
    private static final String ESPN_LEAGUE = "Spanish LALIGA";
    private static final String UNLOCK = "Unlock";

    private static final String RECENT_SEARCH_NOT_FOUND_ERROR_MESSAGE = "recent search was not displayed";
    private static final String RECENT_SEARCH_FOUND_ERROR_MESSAGE = "recent search was displayed";
    private static final String CONTENT_NOT_FOUND_IN_RECENT_SEARCH_ERROR_MESSAGE = "content was not displayed in " +
            "recent search results";
    private static final String PCON_HEADER_ERROR_NOT_FOUND = "PCON restricted title message was not present";
    private static final String PCON_ERROR_MESSAGE_NOT_FOUND = "PCON restricted error message was not present";
    private static final String SEARCH_PAGE_DID_NOT_OPEN = "Search page did not open";

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
        setAppToHomeScreen(getUnifiedAccount());

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
        setAppToHomeScreen(getUnifiedAccount());

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
        setAppToHomeScreen(getUnifiedAccount());

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
        setAppToHomeScreen(getUnifiedAccount());
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
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        //User made different search
        searchPage.searchForMedia(BLUEY);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getBackArrow().click();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(media);
        results = searchPage.getDisplayedTitles();
        results.get(0).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getBackArrow().click();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();

        //user selects a Recent Search from the Recent Searches list
        //Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.getSearchBar().click();
        sa.assertTrue(searchPage.isRecentSearchDisplayed(), RECENT_SEARCH_NOT_FOUND_ERROR_MESSAGE);
        searchPage.tapTitleUnderRecentSearch(media);
        results = searchPage.getDisplayedTitles();
        results.get(0).click();

        //verify selected recent search item opened
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getMediaTitle().equals(media), "selected recent search item was not opened");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68280"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRecentSearchShowsMaxTenResults() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

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
        setAppToHomeScreen(getUnifiedAccount());

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

        setAppToHomeScreen(getUnifiedAccount());
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

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        setAppToHomeScreen(getUnifiedAccount(), KIDS_PROFILE);
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
        setAppToHomeScreen(getUnifiedAccount());

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

        setAppToHomeScreen(getUnifiedAccount());
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
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(searchPage.isExploreTitleDisplayed(SHORT_TIMEOUT), "Explore title is not displayed");

        searchPage.clickSecondCollection();
        Assert.assertTrue(brandPage.isOpened(), collectionPageDidNotOpen);

        sa.assertTrue(brandPage.isCollectionBrandImageExpanded(), collectionLogoNotExpanded);
        sa.assertTrue(brandPage.getBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(brandPage.isArtworkBackgroundPresent(), "Artwork images is not present");
        sa.assertTrue(brandPage.isCollectionTitleDisplayed(), "Collection title not displayed");
        sa.assertTrue(brandPage.isCollectionImageCollapsedFromSwipe(Direction.UP, swipeAttempt),
                "Image not collapsed after swipe");
        sa.assertTrue(brandPage.getBackArrow().isPresent(), BACK_BUTTON_NOT_DISPLAYED);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            LOGGER.info("Device is Handset. Skipping Collapsed Scrolling Assert on iPad");
            sa.assertTrue(brandPage.isCollectionBrandImageCollapsed(), "Collection brand logo is not collapsed");
        }

        brandPage.swipeInCollectionTillImageExpand(Direction.DOWN, swipeAttempt);
        sa.assertTrue(brandPage.isCollectionBrandImageExpanded(), collectionLogoNotExpanded);
        brandPage.getBackArrow().click();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.clickSecondCollection();
        Assert.assertTrue(brandPage.isOpened(), collectionPageDidNotOpen);

        //Click First Content Tile on Collection Page
        homePage.clickDynamicCollectionOrContent(2, 1);
        detailsPage.waitForDetailsPageToOpen();
        Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player didn't open");
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);

        clickElementAtLocation(detailsPage.getBackArrow(), 50, 50);
        Assert.assertTrue(brandPage.isOpened(), collectionPageDidNotOpen);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75718"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHideRestrictedTitlesInSearchResults() {
        String contentTitle = "Deadpool & Wolverine";

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_TV14);

        setAppToHomeScreen(getUnifiedAccount());

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
        setAppToHomeScreen(getUnifiedAccount());

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
        setAppToHomeScreen(getUnifiedAccount());

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

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(false)
                .build());
        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
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
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RatingConstant.Rating.TV_Y.getContentRating());

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75501"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyOriginalsLandingPageContentMaturityRatingRestriction() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage = initPage(DisneyPlusMediaCollectionIOSPageBase.class);

        SoftAssert sa = new SoftAssert();
        int apiTitlesSearchLimit = 400;

        //Assign TV-Y content maturity rating
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RatingConstant.Rating.TV_Y.getContentRating());
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.clickOriginalsTab();
        Assert.assertTrue(originalsPage.isOpened(), "Original content page was not opened");

        //Compare default content displayed in the UI against Explore API originals for TV-Y ratings
        String selectedCategory = mediaCollectionPage.getSelectedCategoryFilterNameForOriginalsAndBrands();
        String setId = getSetIdFromApi(DisneyEntityIds.ORIGINALS_PAGE.getEntityId(), selectedCategory);
        List<String> filteredListOfTitlesByRating = getContainerTitlesWithGivenRatingFromApi(
                setId, apiTitlesSearchLimit, RatingConstant.Rating.TV_Y.getContentRating());

        if (!filteredListOfTitlesByRating.isEmpty()) {
            filteredListOfTitlesByRating.forEach(item -> {
                sa.assertTrue(originalsPage.getTypeCellLabelContains(item).isPresent(), "Title from Api not found in UI " + item);
            });
        } else {
            LOGGER.info("Originals Collection Api results are empty");
            sa.assertTrue(searchPage.isPCONRestrictedErrorHeaderPresent(),
                    PCON_HEADER_ERROR_NOT_FOUND);
            sa.assertTrue(searchPage.isPCONRestrictedErrorMessagePresent(),
                    PCON_ERROR_MESSAGE_NOT_FOUND);
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69557"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyBrandPageContentMaturityRatingRestriction() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage = initPage(DisneyPlusMediaCollectionIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = new DisneyPlusBrandIOSPageBase(getDriver());

        SoftAssert sa = new SoftAssert();
        int apiTitlesSearchLimit = 400;

        // Edit to get TV-Y maturity rating content
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RatingConstant.Rating.TV_Y.getContentRating());
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickDisneyTile();
        Assert.assertTrue(brandPage.isOpened(), "Disney brand page was not opened");

        // Get Container ID
        List<Container> collections = getDisneyAPIPage(DisneyEntityIds.DISNEY_PAGE.getEntityId());
        if (collections.isEmpty()) {
            throw new IllegalArgumentException("No collections found for brand");
        }

        //Compare default content displayed in the UI against Explore API Disney brand page for TV-Y rating
        String selectedCategory = mediaCollectionPage.getSelectedCategoryFilterNameForOriginalsAndBrands();
        String setId = getSetIdFromApi(DisneyEntityIds.DISNEY_PAGE.getEntityId(), selectedCategory);
        List<String> filteredListOfTitlesByRating = getContainerTitlesWithGivenRatingFromApi(
                setId, apiTitlesSearchLimit, RatingConstant.Rating.TV_Y.getContentRating());

        if (!filteredListOfTitlesByRating.isEmpty()) {
            String finalHeroCarouselId = collections.get(0).getId();
            filteredListOfTitlesByRating.forEach(item -> {
                if (brandPage.getTypeCellLabelContains(item).isElementNotPresent(THREE_SEC_TIMEOUT)) {
                    swipeInContainer(homePage.getHeroCarouselContainer(finalHeroCarouselId), Direction.LEFT, 500);
                }
                sa.assertTrue(brandPage.getTypeCellLabelContains(item).isPresent(), "Title from Api not found in UI " + item);
            });
        } else {
            LOGGER.info("Originals Collection Api results are empty");
            sa.assertTrue(searchPage.isPCONRestrictedErrorHeaderPresent(),
                    PCON_HEADER_ERROR_NOT_FOUND);
            sa.assertTrue(searchPage.isPCONRestrictedErrorMessagePresent(),
                    PCON_ERROR_MESSAGE_NOT_FOUND);
        }

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77662"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNSportsUpcomingEventBadges() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.searchForMedia(ESPN_LEAGUE);

        ExtendedWebElement firstUpcomingEventCell = searchPage.getFirstUpcomingEventCell();
        searchPage.hideKeyboard();
        searchPage.swipe(firstUpcomingEventCell, 20);
        Assert.assertTrue(firstUpcomingEventCell.isElementPresent(),
                "No upcoming events found on search page");
        Assert.assertTrue(searchPage.getUpcomingBadgeForGivenSearchResult(firstUpcomingEventCell).isElementPresent(),
                "Upcoming badge was not present for upcoming event search result");
        Assert.assertTrue(searchPage.getUnlockBadgeForGivenSearchResult(firstUpcomingEventCell).isElementPresent(),
                "Unlock badge was not present for upcoming event search result");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74554"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchHuluContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());

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
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchEmptyPageHideRestrictedTitleForTV14AndKids() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_TV14);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        setAppToHomeScreen(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        homePage.getSearchNav().click();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertTrue(searchPage.isPCONRestrictedErrorMessagePresent(),
                "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(ONLY_MURDERS_IN_THE_BUILDING),
                "No results found message was not as expected for TV-14 profile");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(KIDS_PROFILE);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        homePage.getSearchNav().click();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getTypeButtonByLabel("search").clickIfPresent();
        pause(2);
        sa.assertTrue(searchPage.isKIDSPCONRestrictedTitlePresent(), "PCON restricted title message was not as expected");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(ONLY_MURDERS_IN_THE_BUILDING), "No results found message was not as expected " +
                "for kids profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67307"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifySearchEmptyPageMaxMaturityRating() {
        String searchQuery = "robocop";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_MATURE);
        setAppToHomeScreen(getUnifiedAccount());

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74557"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMaxLimitSearchQuery() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String searchLimitQuery = "automationsearchlongqueryformaximumSixtyfourcharacterlimittest!!";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        homePage.getSearchNav().click();
        searchPage.searchForMedia(searchLimitQuery);
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(searchLimitQuery), "No results found message was not as expected");
        sa.assertEquals(searchLimitQuery, searchPage.getSearchBarText(),
                "Maximum number of characters is not allowed in search field.");
        searchPage.searchForMedia(searchLimitQuery + "D");
        sa.assertEquals(searchLimitQuery, searchPage.getSearchBarText(),
                "character after 64 char is accepted in search bar");
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(searchLimitQuery), "No results found message was not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77872"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, US})
    public void verifySearchHuluContentForStandaloneUser() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        Assert.assertTrue(searchPage.getDynamicAccessibilityId(ONLY_MURDERS_IN_THE_BUILDING).isPresent(),
                "Hulu Content not found in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(ONLY_MURDERS_IN_THE_BUILDING).getText().contains(HULU),
                "Hulu brand name not found in content in search result");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77875"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, US})
    public void verifyEntitleAndNonEntitleHuluContentForNonBundleUser() {
        String entitleHuluContent = "Solar Opposites";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        Assert.assertTrue(searchPage.getDynamicAccessibilityId(ONLY_MURDERS_IN_THE_BUILDING).isPresent(),
                "Hulu Content not found in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(ONLY_MURDERS_IN_THE_BUILDING).getText().contains(HULU),
                "Hulu brand name not found in content in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(ONLY_MURDERS_IN_THE_BUILDING).getText().contains(UNLOCK),
                "Unlock 'upsell message' not found in search result");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77874"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, US})
    public void verifySearchHuluContentForBundleUser() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);

        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        Assert.assertTrue(searchPage.getDynamicAccessibilityId(ONLY_MURDERS_IN_THE_BUILDING).isPresent(),
                "Hulu Content not found in search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(ONLY_MURDERS_IN_THE_BUILDING).getText().contains(HULU),
                "Hulu brand name not found in content in search result");
        Assert.assertFalse(searchPage.getTypeCellLabelContains(ONLY_MURDERS_IN_THE_BUILDING).getText().contains(UNLOCK),
                "Unlock 'upsell message' found in search result");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77873"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, CA})
    public void verifySearchHuluContentForStandaloneUserInEligible() {
        String unavailableContentInCA = "Normal People";

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PREMIUM_MONTHLY_CANADA)));
        handleAlert();
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_DID_NOT_OPEN);
        searchPage.searchForMedia(unavailableContentInCA);
        Assert.assertTrue(searchPage.isNoResultsFoundMessagePresent(unavailableContentInCA),
                String.format("No results found message was not displayed for, '%s'", unavailableContentInCA));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77673"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, FRANCE})
    public void verifySportsSearchForNonEligibleCountry() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage =
                initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM)));

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        setAppToHomeScreen(getUnifiedAccount());
        if (oneTrustPage.isOpened())
            oneTrustPage.tapAcceptAllButton();
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(ESPN_LEAGUE);
        searchPage.getKeyboardSearchButton().click();
        Assert.assertFalse(searchPage.getTypeCellLabelContains(ESPN_PLUS).isElementPresent(TEN_SEC_TIMEOUT),
                "An ESPN+ title was found when searching for Sports content");
        Assert.assertFalse(searchPage.getStaticTextByLabel(ESPN_LEAGUE)
                        .isElementPresent(TEN_SEC_TIMEOUT),
                "Search did show titles related to the given ESPN League search");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77669"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifySportsSearchForEligibleCountry() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(ESPN_LEAGUE);
        searchPage.getKeyboardSearchButton().click();
        Assert.assertTrue(searchPage.getTypeCellLabelContains(ESPN_PLUS).isElementPresent(),
                "An ESPN+ title was not found when searching for Sports content");
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(ESPN_LEAGUE).isElementPresent(),
                "Search did not show titles related to the given ESPN League search");
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
