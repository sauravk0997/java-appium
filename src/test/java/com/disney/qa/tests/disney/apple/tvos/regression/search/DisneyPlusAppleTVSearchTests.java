package com.disney.qa.tests.disney.apple.tvos.regression.search;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.CreateUnifiedAccountProfileRequest;
import com.disney.qa.api.disney.*;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang.RandomStringUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.*;

import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.DAREDEVIL_BORN_AGAIN;
import static com.disney.qa.api.disney.DisneyEntityIds.END_GAME;
import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING;
import static com.disney.qa.common.constant.CollectionConstant.getCollectionName;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVSearchTests extends DisneyPlusAppleTVBaseTest {

    private static final String UNENTITLED_HULU_CONTENT = "Only Murders in the Building";
    private static final String ENTITLED_HULU_CONTENT = "Solar Opposites";
    private static final String UNENTITLED_ESPN_CONTENT = "The Championships, Wimbledon";
    private static final String UNLOCK = "Unlock";
    private static final String AVAILABLE_WITH_ESPN_SUBSCRIPTION = "Available with ESPN+ Subscription";
    private static final String HULU_CONTENT_ERROR_MESSAGE = "Hulu content is not present";
    private static final String HULU_CONTENT_NOT_AVAILABLE_IN_CANADA = "Normal People";
    private static final String A_CHARACTER = "a";
    private static final String B_CHARACTER = "b";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67362"})
    @Test(groups = {TestGroup.SEARCH, US})
    public void verifySearchGlobalMenuLanding() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());

        logIn(getUnifiedAccount());
        Assert.assertTrue(home.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.typeInSearchField(UNENTITLED_HULU_CONTENT);
        Assert.assertEquals(searchPage.getSearchBar().getAttribute("value"), UNENTITLED_HULU_CONTENT,
                "Search text was not displayed in the search field");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121506"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, US})
    public void verifyHuluHubSearchContentWithStandaloneAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());

        logIn(getUnifiedAccount());
        Assert.assertTrue(home.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.typeInSearchField(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(UNENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121508"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, US})
    public void verifyHuluHubSearchContentWithBundleUserAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        Assert.assertTrue(home.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.typeInSearchField(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(UNENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
        searchPage.clickSearchResult(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertFalse(detailsPage.getUpgradeNowButton().isPresent(), "Upsell message is present");
        Assert.assertTrue(detailsPage.isPlayButtonDisplayed(), "Play button is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121507"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, CA})
    public void verifyHuluHubSearchContentInNonEligibleCountry() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusOneTrustConsentBannerIOSPageBase bannerIOSPageBase =
                new DisneyPlusOneTrustConsentBannerIOSPageBase(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));

        handleAlert();
        logIn(getUnifiedAccount());
        if (bannerIOSPageBase.isAllowAllButtonPresent()) {
            bannerIOSPageBase.tapAcceptAllButton();
        }
        Assert.assertTrue(home.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.typeInSearchField(HULU_CONTENT_NOT_AVAILABLE_IN_CANADA);
        Assert.assertTrue(searchPage.isNoResultsFoundMessagePresent(HULU_CONTENT_NOT_AVAILABLE_IN_CANADA),
                "No results found message was not as expected for non eligible country Canada");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121509"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULU, US})
    public void verifyHuluHubSearchContentWithNonBundleUserAccount() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount());
        Assert.assertTrue(home.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.waitForPresenceOfAnElement(searchPage.getSearchField());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        // Look for entitled Hulu content
        searchPage.typeInSearchField(ENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(ENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
        Assert.assertFalse(searchPage.getTypeCellLabelContains(UNLOCK).isPresent(),
                "Unlock 'upsell message' found in search result");

        searchPage.clearSearchBar();

        // Look for unentitled Hulu content
        searchPage.typeInSearchField(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(UNENTITLED_HULU_CONTENT).isPresent(), HULU_CONTENT_ERROR_MESSAGE);
        Assert.assertTrue(searchPage.getTypeCellLabelContains(UNLOCK).isPresent(),
                "Unlock 'upsell message' not found in search result");
        searchPage.clickSearchResult(UNENTITLED_HULU_CONTENT);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        detailsPage.getUnlockButton().click();
        Assert.assertTrue(detailsPage.isOnlyAvailableWithHuluHeaderPresent(), "Hulu ineligible screen header is not present");
        Assert.assertTrue(detailsPage.isIneligibleScreenBodyPresent(), "Hulu ineligible screen description is not present");
        Assert.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(), "Ineligible CTA button is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-100354"})
    @Test(groups = {TestGroup.SEARCH, US})
    public void verifySearchNavigation() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        Assert.assertTrue(home.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.typeInSearchField(END_GAME.getTitle());
        searchPage.waitForPresenceOfAnElement(searchPage.getSearchResults(END_GAME.getTitle()).get(0));
        searchPage.keyPressTimes(searchPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        Assert.assertTrue(searchPage.isFocused(searchPage.getSearchResults(END_GAME.getTitle()).get(0)),
                "First Top Left of the tile is not focused");
        searchPage.navigateToKeyboardFromResult();
        Assert.assertTrue(searchPage.isFocused(searchPage.getKeyboardByPredicate()),
                "Keyboard not focused");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67380"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.SMOKE, US})
    public void verifySearchInputField() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        homePage.clickSelect();
        homePage.moveRight(1, 1);
        homePage.clickSelect();
        Assert.assertTrue(searchPage.getSearchBar().getAttribute("value").equals("ab"),
                "Search inputs were not recorded correctly");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67280"})
    @Test(groups = {TestGroup.SMOKE, TestGroup.SEARCH, TestGroup.MOVIES, US})
    public void verifySearchMoviesNavigation() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage =
                new DisneyPlusMediaCollectionIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());

        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(MOVIES.getText());
        Assert.assertTrue(mediaCollectionPage.getMoviesCollectionLabel().isPresent(),
                "Unable to navigate to Movies collection page");

        //verify that able to navigate to the tabs
        List<String> tabNames = getMovieTabCollection();
        if(tabNames.isEmpty()) {
           throw new SkipException("Not able to get the tab collections from the API");
        }
        if(tabNames.size() > 6) {
            tabNames = tabNames.subList(0, 6);
        }
        tabNames.forEach(tab -> {
            sa.assertTrue(commonPage.isFocused(commonPage.getTypeButtonByLabel(tab)),
                    "Unable to navigate to the movies tabs");
            commonPage.moveRight(1, 1);
        });

        //verify that able to navigate the collections
        commonPage.moveDown(1, 1);
        String currentFocussedCellTitle = commonPage.getFocusedCell().getText();
        commonPage.moveLeft(2, 1);
        String nextFocussedCellTitle = commonPage.getFocusedCell().getText();
        Assert.assertNotEquals(currentFocussedCellTitle, nextFocussedCellTitle,
                "Not able to traverse horizontally in movies collection");

        commonPage.moveDown(1, 1);
        String nextRowFocussedCellTitle = commonPage.getFocusedCell().getText();
        Assert.assertNotEquals(nextRowFocussedCellTitle, nextFocussedCellTitle,
                "Not able to traverse down in movies collection");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112662"})
    @Test(groups = {TestGroup.SEARCH, US})
    public void verifySearchPageEmptyState() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());

        logIn(getUnifiedAccount());

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        Assert.assertFalse(searchPage.getNoResultsFoundText().isPresent(FIVE_SEC_TIMEOUT),
                "'No results found' message is displayed");
        searchPage.typeInSearchField(RandomStringUtils.randomAlphabetic(61));
        Assert.assertTrue(searchPage.getNoResultsFoundText().isPresent(),
                "'No results found' message is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112731"})
    @Test(groups = {TestGroup.SEARCH, US})
    public void verifySearchKeyboard() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());

        logIn(getUnifiedAccount());

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        Assert.assertTrue(searchPage.getKeyboardByPredicate().isPresent(), "Keyboard is not present");
        searchPage.clickSelect();
        Assert.assertEquals(searchPage.getSearchBarText(), A_CHARACTER,
                String.format("Current search query wasn't '%s'", A_CHARACTER));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112649"})
    @Test(groups = {TestGroup.SEARCH, US})
    public void verifySearchQueryInputBehavior() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());

        logIn(getUnifiedAccount());

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.typeInSearchField(A_CHARACTER);
        List<ExtendedWebElement> firstQuerySearchResults = searchPage.getAllSearchResults();
        searchPage.typeInSearchField(B_CHARACTER);
        List<ExtendedWebElement> secondQuerySearchResults = searchPage.getAllSearchResults();
        Assert.assertNotEquals(firstQuerySearchResults, secondQuerySearchResults,
                "Search results didn't changed after updating the search query");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-123196"})
    @Test(groups = {TestGroup.HOME, US})
    public void verifySearchNavigationForUpComingContent() {
        int maxQuantityOfExpectedChannels = 6;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        Item firstUpcomingEvent = getUpcomingEventFromAPI(maxQuantityOfExpectedChannels);
        String contentTitle = firstUpcomingEvent.getVisuals().getTitle();
        LOGGER.info("Upcoming event title:- " + contentTitle );

        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.typeInSearchField(contentTitle);
        Assert.assertTrue(searchPage.waitForSearchResultToVisible(), "search result is not visible");
        searchPage.clickSearchResult(contentTitle);

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1,1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getDetailsTab(), 6);
        Assert.assertEquals(detailsPage.getDetailsTabTitle(), contentTitle,
                "Expected upcoming event detail page not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112624"})
    @Test(groups = {TestGroup.SEARCH, US})
    public void verifySearchEmptyPageHideRestrictedTitleForTV14AndKids() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        // Update main profile to have TV_14 content rating
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_TV14);

        // Add secondary profile with kid DOB
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        // Access main profile
        logInWithoutHomeCheck(getUnifiedAccount());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.clickProfile(DEFAULT_PROFILE);
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        // Go to search page
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        // Search a title that is above current content rating. Expect to see 'content hidden' message and no results
        searchPage.typeInSearchField(DAREDEVIL_BORN_AGAIN.getTitle());
        Assert.assertTrue(searchPage.isPCONRestrictedErrorMessagePresent(),
                "PCON restricted title message was not as expected");
        Assert.assertTrue(searchPage.isNoResultsFoundMessagePresent(DAREDEVIL_BORN_AGAIN.getTitle()),
                "No results found message was not as expected for TV-14 profile");

        // Switch to kids secondary profile and go to search page
        homePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.clickProfile(KIDS_PROFILE);
        Assert.assertTrue(homePage.isKidsHomePageOpen(), HOME_PAGE_NOT_DISPLAYED);
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        // Search a title that is above kids content rating. Expect to see 'content hidden' kids message and no results
        searchPage.typeInSearchField(DAREDEVIL_BORN_AGAIN.getTitle());
        Assert.assertTrue(searchPage.isKIDSPCONRestrictedTitlePresent(),
                "PCON restricted title message was not as expected for kids profile");
        Assert.assertTrue(searchPage.isNoResultsFoundMessagePresent(DAREDEVIL_BORN_AGAIN.getTitle()),
                "No results found message was not as expected for kids profile");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121201"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.ESPN, US})
    public void verifySportsSearchForEligibleCountry() {
        String sport = "Soccer";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.typeInSearchField(sport);
        Assert.assertTrue(searchPage.getTypeCellLabelContains(ESPN_PLUS).isElementPresent(),
                "An ESPN+ title was not found when searching for Sports content");
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(sport).isElementPresent(),
                "Search did not show titles related to the given ESPN sport search");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-121743"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.ESPN, US})
    public void verifyUpsellDetailPageForESPNContent() {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount());
        Assert.assertTrue(home.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        home.moveDownFromHeroTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.waitForPresenceOfAnElement(searchPage.getSearchField());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        // Look for unentitled ESPN content
        searchPage.typeInSearchField(UNENTITLED_ESPN_CONTENT);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(UNENTITLED_ESPN_CONTENT).isPresent(), "ESPN content is not found on search result");
        Assert.assertTrue(searchPage.getTypeCellLabelContains(UNLOCK).isPresent(),
                "Unlock 'upsell message' not found in search result");
        searchPage.clickSearchResult(UNENTITLED_ESPN_CONTENT);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getStaticTextByLabel(AVAILABLE_WITH_ESPN_SUBSCRIPTION).isPresent(),
                AVAILABLE_WITH_ESPN_SUBSCRIPTION + " upsell message is not displayed");
    }

    private Item getUpcomingEventFromAPI(int titlesLimit) {
        List<Item> upcomingSetItemFromApi = getExploreAPIItemsFromSet(
                getCollectionName(ESPN_PLUS_LIVE_AND_UPCOMING), titlesLimit);
        Assert.assertNotNull(upcomingSetItemFromApi,
                String.format("No items for '%s' collection were fetched from Explore API",
                        ESPN_PLUS_LIVE_AND_UPCOMING));
        if (upcomingSetItemFromApi.isEmpty()) {
            throw new NoSuchElementException("Failed to fetch a upcoming content details from API");
        }

        for (Item upcomingEventFromApi : upcomingSetItemFromApi) {
            if (upcomingEventFromApi.getVisuals().getPrompt().contains("AM") ||
                    upcomingEventFromApi.getVisuals().getPrompt().contains("PM")) {
                return upcomingEventFromApi;
            }
        }
        throw new NoSuchElementException("Failed to fetch an upcoming content details from API");
    }

    private List<String> getMovieTabCollection() {
        List<Container> pageContainers = getDisneyAPIPage(DisneyEntityIds.MOVIES.getEntityId());
        List<String> tabNames = new ArrayList<>();
        if(pageContainers.isEmpty()) {
            throw new SkipException("Not able to get the Movies collection data from the API");
        }
        pageContainers.forEach(container -> tabNames.add(container.getVisuals().getName()));
        return tabNames;
    }
}
