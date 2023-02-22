
package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.SearchPageRequest;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.content.ContentSearch;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.DIS;


public class DisneyPlusAndroidTVSearchTest extends DisneyPlusAndroidTVBaseTest {

    @Test
    public void generalSearchVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67366", "XCDQA-67372", "XCDQA-67374", "XCDQA-67378", "XCDQA-67380"));
        SoftAssert sa = new SoftAssert();
        String searchText = "a";

        loginAndOpenSearchPage(sa);

        // XCDQA-67366 - Default focus is on "a"
        // XCDQA-67380 - Confirm search string appears in search text field
        // XCDQA-67378 - Search results automatically appear after entering a search query
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isKeyFocused("a"), "Default focus should be on 'a'.");
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchText);
        sa.assertEquals(disneyPlusAndroidTVSearchPage.get().getSearchBoxText(), searchText, "Search box should contain search string.");
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isSearchResultVisible(), "Search results should appear.");

        // XCDQA-67372 - Left boundary check from "a" on keyboard.
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isKeyFocused("a"), "Focus should be on 'a'.");
        disneyPlusAndroidTVCommonPage.get().pressLeft(1);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isNavSelectionFocused(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH));

        // XCDQA-67374 - Right boundary check from "z" on keyboard.
        while(!disneyPlusAndroidTVSearchPage.get().isKeyFocused("g")) {
            disneyPlusAndroidTVCommonPage.get().pressRight(1);
        }
        while(!disneyPlusAndroidTVSearchPage.get().isKeyFocused("z")) {
            disneyPlusAndroidTVCommonPage.get().pressDown(1);
        }

        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isASearchTileFocused(), "Focus should be on a Search tile.");

        sa.assertAll();
    }

    @Test
    public void searchLayout() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67362", "XCDQA-67364", "XCDQA-67368", "XCDQA-67370"));
        SoftAssert sa = new SoftAssert();
        AndroidUtilsExtended AndroidUtil = new AndroidUtilsExtended();

        // XCDQA-67362 - Selecting Search global nav opens Search page
        loginAndOpenSearchPage(sa);
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isOpened(), SEARCH_PAGE_LOAD_ERROR);

        // XCDQA-67368 - Confirm custom keyboard is displayed and no device keyboard is present
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isSearchKeyboardPresent(), "Search keyboard should be present.");
        sa.assertFalse(AndroidUtil.isKeyboardShown(), "Is device keyboard present");

        // XCDQA-67364 - Search Page layout details
        List<ExtendedWebElement> allVisibleTiles = disneyPlusAndroidTVSearchPage.get().getShelfSetElements();
        int keyboardX = disneyPlusAndroidTVSearchPage.get().getKeyboardTopLeftCoordinate().x;
        int searchResultsX = disneyPlusAndroidTVSearchPage.get().getSearchResultsTopLeftCoordinate().x;

        // Fire TV devices do not have the mic button.
        if (!AndroidTVUtils.isAmazon()) {
            sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isMicrophoneIconVisible(), "Microphone icon should be visible.");
        }
        sa.assertEquals(disneyPlusAndroidTVSearchPage.get().getSearchBoxText(),
                "Search by title, character, or genre", "Search box should be: ");
        sa.assertTrue(keyboardX < searchResultsX, "X coordinate for keyboard " + keyboardX + " should be less than X coordinate for results " + searchResultsX);
        sa.assertTrue(allVisibleTiles.get(2).getLocation().y != allVisibleTiles.get(3).getLocation().y, "Search tile rows should be 3 tiles long.");

        // XCDQA-67370 - Keyboard layout details
        List<String> keyboardKeys = (disneyPlusAndroidTVSearchPage.get().getKeyboardKeys());
        sa.assertEquals(keyboardKeys.get(0), "a", "First keyboard key: ");
        sa.assertEquals(keyboardKeys.get(25), "z", "Twenty sixth keyboard key: ");
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isBackSpaceVisible(), "Back Space key should be visible.");
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isSpaceBarVisible(), "Space Bar key should be visible.");
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isKeyboardDividerVisible(), "A divider between letters and numbers should be visible.");

        // Letters should be lowercase
        for (int i = 0; i < 26; i++) {
            sa.assertTrue(keyboardKeys.get(i).matches("[a-z]"), "Keyboard key " + keyboardKeys.get(i) + " should be lowercase");
        }

        // Numbers should start on a new line, not in the same row as letters
        while(!disneyPlusAndroidTVSearchPage.get().isKeyFocused("v")) {
            disneyPlusAndroidTVCommonPage.get().pressDown(1);
        }
        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isKeyFocused("Number 1"), "Focus should be on '1'.");

        sa.assertAll();
    }

    @Test
    public void searchRestrictedNoResults() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-97184"));
        SoftAssert sa = new SoftAssert();
        loginAndOpenSearchPage(sa);

        // NSFW gibberish gets no results
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox("pr0n");

        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isContentRestrictedTitleVisible(), "Expected restricted content message to be visible...");
        sa.assertFalse(disneyPlusAndroidTVSearchPage.get().isSearchResultVisible(), "Expected no search results...");

        sa.assertAll();
    }

    @Test
    public void searchRestrictedResults() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-97185"));

        // Rated TV-MOM SAID NO
        String searchTitle = "Luke Cage";

        SoftAssert sa = new SoftAssert();
        loginAndOpenSearchPage(sa);

        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTitle);
        disneyPlusAndroidTVSearchPage.get().focusFirstSearchedItem();
        disneyPlusAndroidTVSearchPage.get().downUntilElementVisible(disneyPlusAndroidTVSearchPage.get().getContentRestrictedTextElement());

        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isContentRestrictedTextVisible(), "Expected restricted content message to be visible...");
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isSearchResultVisible(), "Expected search results to be visible...");

        sa.assertAll();
    }

    @Test
    public void autoCompletePriority() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-96053"));
        SoftAssert sa = new SoftAssert();

        loginAndOpenSearchPage(sa);

        // Movie - partial match should be first film in series
        validateSearchPriority("Froze", "Frozen", sa);

        // Series - series title has priority
        validateSearchPriority("Obi-Wan", "Obi-Wan Kenobi", sa);

        sa.assertAll();
    }

    @Test
    public void autoCompleteSuggestion() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66170", "XCDQA-66164"));
        String searchTarget = "Star";
        SoftAssert sa = new SoftAssert();

        loginAndOpenSearchPage(sa);

        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTarget);
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isAutoCompleteSuggestionVisible(),
                "Auto complete suggestion should be visible.");

        // Get text of second auto complete suggestion
        // click() does not register on the first element...possibly due to occlusion by the search keyboard.
        String suggestion = disneyPlusAndroidTVSearchPage.get().getAutoCompleteSuggestionText(1);
        disneyPlusAndroidTVSearchPage.get().selectAutoCompleteSuggestion(1);

        UniversalUtils.captureAndUpload(getCastedDriver());

        // Verify search box text and auto complete suggestion match
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().verifySuggestionContainsSearchText(suggestion),
                "Autocomplete suggestion should match search criteria.");

        // First item in search should match suggestion after selection.
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().verifyShelfElementText(suggestion, 0),
                "Should find text: " + suggestion + " in first shelf item content-desc");

        // Query back-end using auto complete suggestion, verify results contain on-screen elements.
        validateVisibleSearchResults(suggestion, sa);

        sa.assertAll();
    }

    @Test
    public void autoCompleteResultsAndCriteria() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66166", "XCDQA-66162"));
        String searchTarget = "Li";
        SoftAssert sa = new SoftAssert();

        loginAndOpenSearchPage(sa);

        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTarget);

        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isAutoCompleteSuggestionVisible(),
                "After entering search text, auto complete suggestion not visible.");

        // Enumerate results and verify text matches rules as defined in the test case.
        // TLDR: If a word in the suggestion contains the search string, it must start with the search string.
        for (ExtendedWebElement e : disneyPlusAndroidTVSearchPage.get().getAutoCompleteSuggestionElements()) {
            String suggestion = e.getText();
            String[] words = suggestion.split("\\s");
            for(String s : words) {
                s = s.toLowerCase();
                if(s.contains(searchTarget)) {
                sa.assertTrue(s.startsWith(searchTarget),
                        "Unexpected match in suggestion. Search term:" + searchTarget + " matched: " + s );
                }
            }
        }
        sa.assertAll();
    }

    @Test
    public void searchExploreResults() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67354", "XCDQA-67358", "XCDQA-67360"));
        SoftAssert sa = new SoftAssert();
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());

        loginAndOpenSearchPage(sa);

        // Get a list of sets from the collection api for the Explore section of the Search page
        String searchRow = DisneyStandardCollection.EXPLORE.getName();
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(country).language(language)
                .slug(DisneyStandardCollection.EXPLORE.getSlug()).contentClass(DisneyStandardCollection.EXPLORE.getContentClass()).build();

        ContentCollection collection = getSearchApi().getCollection(collectionRequest);
        DisneyCollectionSet set = collection.getCollectionSetByName(searchRow);
        SetRequest setRequest = SetRequest.builder().region(country).language(language).setId(set.getRefId()).refType(set.getRefType()).build();
        List<String> setTitles = getSearchApi().getSet(setRequest).getTitles();

        // Check that there are 3 tiles in all visible rows (current tile size fits 4 rows)
        List<ExtendedWebElement> allVisibleTiles = disneyPlusAndroidTVSearchPage.get().getShelfSetElements();
        sa.assertTrue(allVisibleTiles.get(0).getLocation().y == allVisibleTiles.get(1).getLocation().y
                && allVisibleTiles.get(1).getLocation().y == allVisibleTiles.get(2).getLocation().y, "Three tiles should be in the first row");

        // Check titles and aspect ratio of Search tiles (don't check the last 3 as they are cut offscreen)
        for (int i = 0; i < allVisibleTiles.size() - 3; i++) {
            sa.assertEquals(setTitles.get(i), utils.getContentDescription(allVisibleTiles.get(i)), "Dictionary titles should match visible titles.");
            double aspectRatio = (double) allVisibleTiles.get(i).getSize().getWidth() / allVisibleTiles.get(i).getSize().getHeight();

            double minAspectRatio = 1.77;
            double maxAspectRatio = 1.85;
            sa.assertTrue(aspectRatio >= minAspectRatio && aspectRatio <= maxAspectRatio, String.format("Aspect ratio %f for tile %s should be between %f and %f",
                    aspectRatio, disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().getContentDescription(allVisibleTiles.get(i)),
                    minAspectRatio, maxAspectRatio));
        }
        sa.assertAll();
    }

    @Test
    public void searchEditBoxPinning() {
       setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67338"));
        String searchTarget = "star";
        SoftAssert sa = new SoftAssert();

        loginAndOpenSearchPage(sa);
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTarget);
        UniversalUtils.captureAndUpload(getCastedDriver());

        disneyPlusAndroidTVSearchPage.get().focusFirstSearchedItem();
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isSearchEditBoxVisible(),
                "Search edit box should remain pinned to top of screen after focusing first result.");

        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isSearchEditBoxVisible(),
                "Search edit box should remain pinned to top of screen after focusing first result.");

        disneyPlusAndroidTVCommonPage.get().pressDown(1);
        sa.assertTrue(!disneyPlusAndroidTVSearchPage.get().isSearchEditBoxVisible(),
                "Search edit box should not remain pinned to top of screen after scrolling to 3rd row.");
        sa.assertAll();
    }

    @Test
    public void validateNoSearchResultsNSFW(){
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66160"));
        String searchTarget = "fuck";
        SoftAssert sa = new SoftAssert();

        loginAndOpenSearchPage(sa);

        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTarget);

        // Enter in NSFW text and verifying no search results are returned.
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isContentRestrictedTitleVisible(),
                "After entering NSFW search text, no results found should display.");

        String actualText = disneyPlusAndroidTVSearchPage.get().getContentRestrictedTitleElement().getText();
        sa.assertTrue(actualText.contains(searchTarget), "Search text should be present in no result message");

        sa.assertAll();
    }

    private void validateVisibleSearchResults(String query, SoftAssert sa) {
        DisneyAccount account = entitledUser.get();
        DisneySearchApi searchAPI = disneySearchApi.get();
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());

        SearchPageRequest searchPageRequest = SearchPageRequest.builder()
                .region(account.getCountryCode())
                .language(account.getProfileLang())
                .account(account)
                .query(query)
                .build();

        ContentSearch searchResult = searchAPI.getContentSearchPageResults(searchPageRequest);
        List<String> apiResultTitles = searchResult.getContentTitlesFull();
        List<ExtendedWebElement> displayedTiles = disneyPlusAndroidTVSearchPage.get().getShelfSetElements();

        for (int i = 0; i < displayedTiles.size(); i++) {
            String displayedTitle = utils.getContentDescription(displayedTiles.get(i));

            // Verify expected results are displayed.
            sa.assertTrue(apiResultTitles.contains(displayedTitle),
                    "Search query result should contain displayed item: " + displayedTiles.get(i));

            // Verify order of results.
            sa.assertEquals(displayedTitle, apiResultTitles.get(i),
                    String.format("Items out of order at index %s ", i));
        }
    }

    private void validateSearchPriority(String searchTarget, String searchExpected, SoftAssert sa) {
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox(searchTarget);

        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().verifyShelfElementText(searchExpected, 0),
                "Did not find text: " + searchExpected + " in shelf items content-desc");
        resetSearch(sa);
    }

    private void resetSearch(SoftAssert sa) {
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isOpened(), SEARCH_PAGE_LOAD_ERROR);
    }

    private void loginAndOpenSearchPage(SoftAssert sa) {
        disneyAccountApi.get().addProfile(entitledUser.get(), "test", language, null, false);
        loginWithoutHomeCheck(entitledUser.get());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVProfilePageBase.get().selectDefaultProfileAfterFocused();
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        // Goto Search page
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(),
                AndroidKey.DPAD_UP, String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isOpened(), SEARCH_PAGE_LOAD_ERROR);
    }


}
