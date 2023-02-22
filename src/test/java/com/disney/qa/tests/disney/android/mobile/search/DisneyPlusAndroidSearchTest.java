package com.disney.qa.tests.disney.android.mobile.search;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAndroidSearchTest extends BaseDisneyTest {

    private static final String SEARCH_ITEM = "Disney";
    private static final String INVALID_SEARCH_ITEM =
            "adfadfadsfpdaofhadhfohdafoudahfapdjnfdalnfadslfnadjlfadnfljncnm12343$$***((**s;lfdg/d,asdlkg;ngpadgklan;" +
                    ".dmas,nv;dlkna,dmfpasd;lfkn,madsflpod;klng,mqpoigk2j4mi0dw";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67325"})
    @Test(description = "Maintain Search Scroll Position", groups = {"Search"})
    public void testMaintainSearchScrollPosition() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        sa.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        commonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));

        sa.assertTrue(searchPageBase.isOpened(),
                "Search page not displayed");

        searchPageBase.clickSearchBar();

        sa.assertTrue(androidUtils.get().isKeyboardShown(),
                "Clicking the search box did not prompt the software keyboard");

        searchPageBase.searchForMedia(SEARCH_ITEM);
        searchPageBase.swipeUpOnScreen(3);

        String title = searchPageBase.getVisibleSearchResultTitles().get(1);

        searchPageBase.openSearchResult(title);

        sa.assertTrue(initPage(DisneyPlusMediaPageBase.class).getMediaTitle().equals(title),
                "Tap search result did not open expected Detail page");

        androidUtils.get().pressBack();

        String assertTitle = searchPageBase.getVisibleSearchResultTitles().get(1);

        sa.assertEquals(title, assertTitle);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67307"})
    @Test(description = "Search Queries With No Results", groups = {"Search"})
    public void testSearchQueriesWithNoResults() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);

        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        sa.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        commonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));

        sa.assertTrue(searchPageBase.isOpened(),
                "Search page not displayed");

        searchPageBase.clickSearchBar();

        sa.assertTrue(androidUtils.get().isKeyboardShown(),
                "Clicking the search box did not prompt the software keyboard");

        searchPageBase.searchForMedia(INVALID_SEARCH_ITEM);
        String expectedValue = languageUtils.get().replaceValuePlaceholders(
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SEARCH_NO_RESULTS.getText()),
                INVALID_SEARCH_ITEM.substring(0, 25) + "…");

        sa.assertEquals(searchPageBase.getRestrictedSearchResultText(), expectedValue,
                "No search results match dictionary key");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67319"})
    @Test(description = "Search Bar UX", groups = {"Search"})
    public void testSearchExploreContentTypeNav() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);

        String isSearchOpenedErrorMessage = "Search Page Is Not Open";
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);
        commonPageBase.dismissTravelingDialog();
        commonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));

        sa.assertTrue(searchPageBase.isOpened(), isSearchOpenedErrorMessage);

        searchPageBase.clickContentTypeFilter(0);

        sa.assertTrue(searchPageBase.isOriginalsHeaderPresent(), "Originals Content Type Page is not open");
        searchPageBase.clickBackButton();

        sa.assertTrue(searchPageBase.isOpened(), isSearchOpenedErrorMessage);

        searchPageBase.clickContentTypeFilter(1);

        sa.assertEquals(initPage(DisneyPlusSearchPageBase.class).getLandingPageText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusSearchPageBase.ScreenTitles.MOVIES.getText()),
                "Movies Content Type Page is not opened");
        searchPageBase.clickBackButton();

        sa.assertTrue(searchPageBase.isOpened(), isSearchOpenedErrorMessage);

        searchPageBase.clickContentTypeFilter(2);

        sa.assertEquals(initPage(DisneyPlusSearchPageBase.class).getLandingPageText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusSearchPageBase.ScreenTitles.SERIES.getText()),
                "Series Content Type Page is not opened");
        searchPageBase.clickBackButton();

        sa.assertTrue(searchPageBase.isOpened(), isSearchOpenedErrorMessage);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67319"})
    @Test(description = "Search Bar UX", groups = {"Search"})
    public void testSearchBarUI() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        String searchBoxErrorMessage = "Search Box is not displayed";
        String searchBoxBtn = "Search Button is not displayed";
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);
        commonPageBase.dismissTravelingDialog();

        commonPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));

        sa.assertTrue(searchPageBase.isSearchContainerDisplayed(), "Search Container is not displayed");
        sa.assertTrue(searchPageBase.isSearchBoxDisplayed(), searchBoxErrorMessage);
        sa.assertTrue(searchPageBase.isSearchBarBackgroundDisplayed(), "Search Bar Background is not displayed");
        sa.assertTrue(searchPageBase.isSearchButtonDisplayed(), searchBoxBtn);

        searchPageBase.clickSearchBar();

        sa.assertTrue(searchPageBase.isSearchBoxDisplayed(), searchBoxErrorMessage);
        sa.assertTrue(searchPageBase.isSearchButtonDisplayed(), searchBoxBtn);

        sa.assertTrue(androidUtils.get().isKeyboardShown(),
                "Clicking the search box did not prompt the software keyboard");

        sa.assertEquals(searchPageBase.getSearchBarText(), languageUtils.get().getDictionaryItem
                        (DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SEARCH_CALL_TO_ACTION.getText()),
                "Search Bar Title not displayed");

        sa.assertAll();
    }
}
