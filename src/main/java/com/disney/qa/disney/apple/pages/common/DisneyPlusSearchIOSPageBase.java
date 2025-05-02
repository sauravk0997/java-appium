package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSearchIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //LOCATORS

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCollectionView[$type = 'XCUIElementTypeStaticText' AND label = 'RECENT SEARCHES'$]")
    protected ExtendedWebElement recentSearchResultsView;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$type = 'XCUIElementTypeStaticText' AND name = '%s'$]")
    protected ExtendedWebElement ratingAndYearDetailsOfContent;
    @ExtendedFindBy(accessibilityId = "magnifyingglass")
    private ExtendedWebElement magnifyingGlassImage;
    @FindBy(id = "Search")
    private ExtendedWebElement keyboardSearchButton;
    @ExtendedFindBy(iosPredicate = "type == 'XCUIElementTypeSearchField'")
    private ExtendedWebElement searchBar;
    @ExtendedFindBy(accessibilityId = "iconSearchCancelLightActive")
    private ExtendedWebElement cancelButtonRecentSearch;
    @ExtendedFindBy(accessibilityId = "selectorButton")
    private ExtendedWebElement contentPageFilterDropDown;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$type = 'XCUIElementTypeButton'  AND label == 'iconNavBack24LightActive'$]/XCUIElementTypeOther/XCUIElementTypeButton[3]")
    private ExtendedWebElement contentPageFilterDropDownAtMiddleTop;
    @ExtendedFindBy(accessibilityId = "segmentedControl")
    private ExtendedWebElement contentPageFilterHeader;
    @ExtendedFindBy(accessibilityId = "itemPickerView")
    private ExtendedWebElement itemPickerView;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS '%s'`][3]")
    private ExtendedWebElement thirdCollectionTitle;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$label CONTAINS '%s'$]")
    private ExtendedWebElement searchResults;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS '%s' AND label CONTAINS '%s'`]")
    private ExtendedWebElement searchResultCellwithTitleAndRatingValues;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$label CONTAINS[c] 'Upcoming'$][1]")
    private ExtendedWebElement firstUpcomingEventCell;
    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeStaticText[`name == 'airingBadgeLabel' AND label =[c] 'Upcoming'`]")
    private ExtendedWebElement upcomingBadge;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == selectableTitle`]")
    private ExtendedWebElement selectableTitle;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == 'Unlock'`]")
    private ExtendedWebElement unlockBadge;
    private ExtendedWebElement moviesTile = staticCellByLabel.format(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.NAV_MOVIES_TITLE.getText()));
    private ExtendedWebElement originalsTile = staticCellByLabel.format(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.NAV_ORIGINALS_TITLE.getText()));
    private ExtendedWebElement seriesTile = staticCellByLabel.format(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.NAV_SERIES_TITLE.getText()));
    private String ratingImage = "current_rating_value_image";

    public DisneyPlusSearchIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return searchBar.isPresent();
    }

    public void clickMoviesTab() {
        moviesTile.click();
    }

    public void clickOriginalsTab() {
        originalsTile.click();
    }

    public void clickSeriesTab() {
        seriesTile.click();
    }

    public List<ExtendedWebElement> getDisplayedTitles() {
        int tries = 0;
        List<ExtendedWebElement> titles;
        do {
            pause(2);
            titles = findExtendedWebElements(cell.getBy());
            titles.subList(0, 4).clear();
            LOGGER.info("Titles: {}", titles);
            tries++;
        } while (tries < 3 && titles.isEmpty());
        if (titles.isEmpty()) {
            throw new SkipException("Skipping test, no titles were found.");
        } else {
            return titles;
        }
    }

    public void searchForMedia(String query) {
        searchBar.type(query);
    }

    public void selectRandomTitle() {
        List<ExtendedWebElement> titleList = getDisplayedTitles();
        LOGGER.info("Random title selected.");
        titleList.get(new SecureRandom().nextInt(titleList.size() - 1)).click();
    }

    public ExtendedWebElement getKeyboardSearchButton() {
        return keyboardSearchButton;
    }

    public ExtendedWebElement getSearchBar() {
        return searchBar;
    }

    public ExtendedWebElement getOriginalsTile() {
        return originalsTile;
    }

    public ExtendedWebElement getMovieTile() {
        return moviesTile;
    }

    public ExtendedWebElement getSeriesTile() {
        return seriesTile;
    }

    public ExtendedWebElement getMagnifyingGlassImage() {
        return magnifyingGlassImage;
    }

    public ExtendedWebElement getFirstUpcomingEventCell() {
        return firstUpcomingEventCell;
    }

    public ExtendedWebElement getUpcomingBadgeForGivenSearchResult(ExtendedWebElement resultCell) {
        return resultCell.findExtendedWebElement(upcomingBadge.getBy());
    }

    public ExtendedWebElement getUnlockBadgeForGivenSearchResult(ExtendedWebElement resultCell) {
        return resultCell.findExtendedWebElement(unlockBadge.getBy());
    }

    public void clearText() {
        LOGGER.info("Clearing text in search bar");
        getClearTextBtn().click();
    }

    public boolean isRecentSearchDisplayed() {
        return headerViewTitleLabel.getText().equalsIgnoreCase("RECENT SEARCHES");
    }

    public void tapTitleUnderRecentSearch(String title) {
        staticTextByLabel.format(title).click();
    }

    public void tapRecentSearchClearButton() {
        cancelButtonRecentSearch.click();
    }

    public boolean isNoResultsFoundMessagePresent(String title) {
        String noResultError = "No results found for \"" + title + "\"";
        return getDynamicAccessibilityId(noResultError).isPresent();
    }

    public boolean isPCONRestrictedErrorMessagePresent() {
        String dictVal = getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.BROWSE_CONTENT_HIDDEN_BODY.getText())
                .replace(ratingImage, " ")
                .replace("{ }", "");
        return getStaticTextByLabel(dictVal).isPresent();
    }

    public boolean isPCONRestrictedErrorHeaderPresent() {
        String dictVal = getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.BROWSE_CONTENT_HIDDEN_HEADER.getText());
        return getStaticTextByLabel(dictVal).isPresent();
    }

    public boolean isKIDSPCONRestrictedTitlePresent() {
        String dictVal = getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.BROWSE_CONTENT_HIDDEN_BODY_KIDS.getText())
                .replace(ratingImage, " ")
                .replace("{ }", "");
        return getDynamicAccessibilityId(dictVal).isPresent();
    }

    public boolean isContentPageFilterDropDownPresent() {
        return contentPageFilterDropDown.isPresent();
    }

    public void clickContentPageFilterDropDown() {
        contentPageFilterDropDown.click();
    }

    public boolean isContentPageFilterSelectableTitlePresent() {
        return selectableTitle.isPresent();
    }

    public void clickContentPageFilterDropDownAtMiddleTop() {
        contentPageFilterDropDownAtMiddleTop.click();
    }

    public boolean isContentPageFilterHeaderPresent() {
        return contentPageFilterHeader.isPresent();
    }

    public void swipeContentPageFilter(Direction direction) {
        //To be used with tablet only
        swipeInContainer(contentPageFilterHeader, direction, 500);
    }

    public boolean isContentPageFilterDropDownAtMiddleTopPresent() {
        return contentPageFilterDropDownAtMiddleTop.isPresent();
    }

    public void swipeItemPicker(Direction direction) {
        //To be used with handset only
        swipeInContainer(itemPickerView, direction, 500);
    }

    public void swipeInRecentSearchResults(Direction direction) {
        swipeInContainer(recentSearchResultsView, direction, 2, 1500);
    }

    public String getClipboardContentBySearchInput() {
        if (getClearTextBtn().isPresent(THREE_SEC_TIMEOUT)) {
            clearText();
        }
        searchBar.click();
        getStaticTextByLabel("Paste").click();
        return searchBar.getText();
    }

    public String getSearchBarText() {
        return searchBar.getText();
    }

    public String getRatingAndYearDetailsFromSearchResults(String title) {
        return ratingAndYearDetailsOfContent.format(title).getText();
    }

    public String getPlaceholderText() {
        return getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.SEARCH_PLACEHOLDER.getText());
    }

    public boolean isExploreTitleDisplayed(int timeOut) {
        return getDynamicAccessibilityId((getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.NAV_EXPLORE_TITLE.getText()))).isPresent(timeOut);
    }

    public void clickThirdCollection() {
        thirdCollectionTitle.format(
                getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DictionaryKeys.CONTENT_TILE_INTERACT.getText())).click();
    }

    public boolean isRatingPresentInSearchResults(String rating) {
        LOGGER.info("Verifying Ratings in search results");
        return searchResults.format(rating).isPresent();
    }

    public ExtendedWebElement getTitleContainer(String titleName, String titleRating) {
        return searchResultCellwithTitleAndRatingValues.format(escapeSingleQuotes(titleName), titleRating);
    }
}
