package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSearchPageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "searchAndExploreRecyclerView")
    private ExtendedWebElement searchPageContainer;

    @FindBy(id = "searchBtn")
    private ExtendedWebElement searchButton;

    @FindBy(id = "searchContainer")
    private ExtendedWebElement searchContainer;

    @FindBy(id = "searchBarBackground")
    private ExtendedWebElement searchBarBackground;

    @FindBy(id = "search_bar")
    private ExtendedWebElement searchBar;

    @FindBy(id = "search_src_text")
    private ExtendedWebElement searchTextArea;

    @FindBy(id = "thumbnailImage")
    private ExtendedWebElement searchResultThumb;

    @FindBy(id = "recentSearchTextView")
    private ExtendedWebElement recentSearchView;

    @FindBy(id  = "com.disney.disneyplus:id/title")
    private ExtendedWebElement searchResultTitle;

    @FindBy(id = "com.disney.disneyplus:id/originalsLogo")
    private ExtendedWebElement originalsLogo;

    @FindBy(id = "landingPageTextView")
    private ExtendedWebElement landingPageTextView;

    @FindBy(id = "contentRestrictedTitle")
    private ExtendedWebElement contentRestrictedTitle;

    @FindBy(id = "shelf_item_layout")
    private ExtendedWebElement searchShelfItemLayout;

    public void clickContentTypeFilter(int contentTypePosition) {
        List<WebElement> shelfItemLayout = getDriver().findElements(By.id("shelf_item_layout"));
        try {
            shelfItemLayout.get(contentTypePosition).click();
        } catch (NullPointerException nullPointerException) {
            LOGGER.error("clickContentTypeFilter():", nullPointerException);
            Assert.fail("Position " + contentTypePosition + " is null and unable to click Content Type");
        }
    }

    public DisneyPlusSearchPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return searchPageContainer.isElementPresent();
    }

    public boolean isSearchBoxDisplayed() {
        return searchBar.isElementPresent();
    }

    public boolean isSearchContainerDisplayed() {
        return searchContainer.isElementPresent();
    }

    public boolean isSearchBarBackgroundDisplayed() {
        return searchBarBackground.isElementPresent();
    }

    public boolean isSearchButtonDisplayed() {
        return searchButton.isElementPresent();
    }

    public String getSearchBarText() {
        return searchTextArea.getText();
    }

    public enum ScreenTitles {
        MOVIES("nav_movies_title"),
        EXPLORE("explore_label"),
        SERIES("nav_series_title"),
        SEARCH_BAR_PLACEHOLDER("search_placeholder");

        private String screenTitle;

        ScreenTitles(String screenTitle) {
            this.screenTitle = screenTitle;
        }

        public String getText() {
            return screenTitle;
        }
    }

    public String selectMediaItem() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(shelfItem.getBy()), DELAY);
        String title = null;

        List<String> titles = new ArrayList<>();
        findExtendedWebElements(shelfItem.getBy()).forEach(poster -> titles.add(poster.getAttribute("content-desc")));

        for (int i = 0; i < titles.size(); i++) {
            clickOnContentDescExact(titles.get(i));
            DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
            if (mediaPageBase.isOpened() && mediaPageBase.isWatchlistAvailable()) {
                title = mediaPageBase.getMediaTitle();
                LOGGER.info("Using media: '{}' for Watchlist test", title);
                break;
            } else {
                new AndroidUtilsExtended().pressBack();
            }
        }
        return title;
    }

    public void clickSearchBar() {
        searchBar.click();
    }

    public void searchForMedia(String param) {
        LOGGER.info("Searching for media: '{}'", param);
        searchBar.click();
        searchTextArea.type(param);
        new AndroidUtilsExtended().hideKeyboard();
    }

    public boolean isRecentSearchDisplayed() {
        return recentSearchView.isElementPresent();
    }

    public void useRecentSearchItem() {
        searchBar.click();
        recentSearchView.click();
    }

    //Leverages of Carina's text clearning to remove previously entered text
    public void clearSearchText() {
        searchBar.click();
        searchTextArea.type("");
    }

    public void openSearchResult(String content) {
        LOGGER.info("Opening search result '{}'", content);
        waitUntil(ExpectedConditions.visibilityOfElementLocated(searchResultTitle.getBy()), LONG_TIMEOUT);

        List<ExtendedWebElement> mediaTitles = findExtendedWebElements(searchResultTitle.getBy());
        for(ExtendedWebElement title : mediaTitles){
            if(title.getText().equals(content)){
                title.click();
                break;
            }
        }
    }

    public String getFirstSearchResultTitle() {
        return searchResultTitle.getText();
    }

    public DisneyPlusMediaPageBase openFirstSearchResultItem(){
        waitUntil(ExpectedConditions.visibilityOfElementLocated(searchResultThumb.getBy()), DELAY);
        searchResultThumb.click();
        return initPage(DisneyPlusMediaPageBase.class);
    }

    public DisneyPlusMediaPageBase openFirstCategoryItem(){
        shelfItem.click();
        return initPage(DisneyPlusMediaPageBase.class);
    }

    public boolean isOriginalsHeaderPresent() {
        return originalsLogo.isElementPresent();
    }

    public String getLandingPageText() {
        return landingPageTextView.getText();
    }

    public List<String> getVisibleSearchResultTitles() {
        List<String> listOfSearchResults = new LinkedList<>();
        findExtendedWebElements(searchResultTitle.getBy()).forEach(result -> listOfSearchResults.add(result.getText()));
        return listOfSearchResults;
    }

    public String getRestrictedSearchResultText() { return contentRestrictedTitle.getText(); }
}
