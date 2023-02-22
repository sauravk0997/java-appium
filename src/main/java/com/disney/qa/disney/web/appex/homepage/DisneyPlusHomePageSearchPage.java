package com.disney.qa.disney.web.appex.homepage;

import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.util.disney.DisneyGlobalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHomePageSearchPage extends DisneyPlusBaseTilesPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(id = "search-input")
    private ExtendedWebElement defaultSearchBar;

    @FindBy(xpath = "//*[@data-testid='navigation-item-1-SEARCH']")
    private ExtendedWebElement searchMenuOption;

    @FindBy(xpath = "//*[@data-testid='search-result-0']")
    private ExtendedWebElement firstSearchResult;

    @FindBy(xpath = "//*[@data-testid='add-to-watchlist-button']")
    private ExtendedWebElement addSearchResult;

    @FindBy(xpath = "//*[contains(@aria-label,'%s')]")
    private ExtendedWebElement searchResultOnWatchlistContains;

    @FindBy(xpath = "//div[@aria-label='%s']")
    private ExtendedWebElement searchResultOnWatchlist;

    @FindBy(xpath = "//*[@data-testid='remove-from-watchlist-button']")
    private ExtendedWebElement removeWatchlistItem;

    @FindBy(xpath = "//*[text() = '%s']")
    private ExtendedWebElement genericEqualsText;

    @FindBy(xpath = "//div[@class='gv2-asset']/a/div")
    private List<ExtendedWebElement> tileName;

    @FindBy(xpath = "//button[@aria-label='Clear search field'][@type='reset']")
    private ExtendedWebElement clearBtn;

    @FindBy(xpath = "//img[@alt='%s']")
    private ExtendedWebElement tileImage;

    @FindBy(xpath = "//*[@data-testid='navigation-item-2-WATCHLIST']")
    private ExtendedWebElement watchlistMenuOption;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-0-0']")
    private ExtendedWebElement searchResultAssetInWatchlist;

    @FindBy(xpath = "//*[@class='icon--empty-watchlist']")
    private ExtendedWebElement emptyWatchListIcon;

    @FindBy(xpath = "//*[@data-testid='row-collection']/div")
    private List<ExtendedWebElement> searchCollection;

    @FindBy(xpath = "//div[@class='gv2-asset']")
    private List<ExtendedWebElement> searchCollectionList;

    @FindBy(xpath = "//div[@data-testid='search-results']/div/div[2]/section/h3")
    private ExtendedWebElement noSearchResultsFound;

    @FindBy(xpath = "//*[@data-testid='search-result-%s']/div")
    private ExtendedWebElement searchResultTiles;

    DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

    public DisneyPlusHomePageSearchPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getSearchResultAssetInWatchlist() {
        return searchResultAssetInWatchlist;
    }

    public ExtendedWebElement getAddToWatchListButton() {
        return addSearchResult;
    }

    public ExtendedWebElement getRemoveFromWatchListButton() {
        return removeWatchlistItem;
    }


    public List<String> getSearchResultTitles() {
        List searchTitles = new ArrayList<String>();
        for (int tileIndex = 0; tileIndex < searchCollectionList.size(); tileIndex++)
            searchTitles.add(searchResultTiles.format(tileIndex).getAttribute(WebConstant.ARIA_LABEL));
        return searchTitles;
    }

    public DisneyPlusHomePageSearchPage clickOnSearchBar() {
        LOGGER.info(getCurrentEnvironment());
        waitFor(searchMenuOption);
        searchMenuOption.click();
        return this;
    }

    public String getSearchBarDefaultText() {
        LOGGER.info("Verify search bar contain default text");
        searchMenuOption.click();
        return defaultSearchBar.getAttribute("placeholder");
    }

    public String getExpectedSearchBarDefaultText(String starSearch, String disneySearch) {
        return ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) ? starSearch : disneySearch;
    }

    public void enterTextOnSearchBar(String textToSearch) {
        LOGGER.info("Enter text on search bar");
        defaultSearchBar.type(textToSearch);
        isTilePresentByContentName(textToSearch);
        waitFor(firstSearchResult);
        firstSearchResult.clickByJs();
    }

    public boolean isSearchResultPresent() {
        return firstSearchResult.isElementPresent();
    }

    public DisneyPlusHomePageSearchPage searchFor(String textToSearch) {
        LOGGER.info("Searching for {}", textToSearch);
        defaultSearchBar.type(textToSearch);
        return this;
    }

    public DisneyPlusHomePageSearchPage clickToAddSearchResult() {
        LOGGER.info("Add result from search bar");
        getAddToWatchListButton().isElementPresent();
        pause(2);
        getAddToWatchListButton().clickByJs();
        if (commercePage.getModalPrimaryBtn().isElementPresent(3)) {
            commercePage.clickModalPrimaryBtn();
            refresh();
            analyticPause();
        }
        return this;
    }

    public DisneyPlusHomePageSearchPage clickOnWatchList() {
        LOGGER.info("Clicked on 'WatchList' menu");
        waitFor(watchlistMenuOption);
        watchlistMenuOption.clickByJs();
        return this;
    }

    public boolean verifySearchResultOnWatchlist(String resultOnWatchlist) {
        LOGGER.info("Verify added result exist on watchlist");
        return searchResultOnWatchlistContains.format(resultOnWatchlist).isElementPresent();
    }

    public DisneyPlusBaseDetailsPage clickOnWatchlistItem(String watchlistItem) {
        LOGGER.info("Click on Watchlist item");
        waitFor(searchResultOnWatchlist.format(watchlistItem));
        searchResultOnWatchlist.format(watchlistItem).click();
        return new DisneyPlusBaseDetailsPage(getDriver());
    }

    public DisneyPlusBaseDetailsPage clickOnWatchlistItemContains(String watchlistItem) {
        LOGGER.info("Click on Watchlist item");
        waitFor(searchResultOnWatchlistContains);
        searchResultOnWatchlistContains.format(watchlistItem).click();
        return new DisneyPlusBaseDetailsPage(getDriver());
    }

    public DisneyPlusHomePageSearchPage clickToRemoveWatchlistItem() {
        LOGGER.info("Click to remove Watchlist item");
        waitFor(getRemoveFromWatchListButton());
        getRemoveFromWatchListButton().clickByJs();
        if (commercePage.getModalPrimaryBtn().isElementPresent()) {
            commercePage.clickModalPrimaryBtn();
            refresh();
        }
        pause(5); // Needed to ensure we actually remove it
        return this;
    }

    public boolean verifyWatchlistIsEmpty() {
        LOGGER.info("Verify Watchlist is empty");
        return emptyWatchListIcon.isElementPresent();
    }

    public List<ExtendedWebElement> getContainerList() {
        return tileName;
    }

    public DisneyPlusHomePageSearchPage clearSearch() {
        clearBtn.click();
        return this;
    }

    public boolean isEmpty(JsonNode dictionary) {
        return getDictionaryElement(dictionary, DisneyWebKeys.NO_SEARCH_RESULTS.getText()).isElementPresent()
                && getDictionaryElement(dictionary, DisneyWebKeys.NO_SEARCH_RESULTS_COPY.getText()).isElementPresent();
    }

    public double getTileAspectRatio(String tileName) {
        return Double.valueOf(new DecimalFormat("#.#")
                .format(((double) tileImage.format(tileName).getSize().getWidth())
                        / ((double) tileImage.format(tileName).getSize().getHeight())));
    }

    public DisneyPlusHomePageSearchPage clickOnSearchResultAsset() {
        getSearchResultAssetInWatchlist().click();
        return this;
    }

    public DisneyPlusHomePageSearchPage enterMovieOnSearchBar() {
        String[] moviesList = getMoviesList();
        for (String movie : moviesList) {
            LOGGER.info("Enter movie name to search: {}", movie);
            defaultSearchBar.type(movie);
            if (firstSearchResult.isElementPresent(5)) {
                firstSearchResult.click();
                break;
            } else {
                defaultSearchBar.sendKeys(Keys.DELETE);
                defaultSearchBar.type((movie));
            }
        }
        return this;
    }

    private String[] getMoviesList() {
        String[] movies;

        if ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) {
            movies = new String[]{
                    "Hitchcock",
                    "Pearl Harbor",
                    "Predator",
                    "Alien",
                    "Taken",
                    "Big Hero 6",
                    "A Bug's Life"
            };
        } else {
            movies = new String[]{
                    "Frozen",
                    "Mulan",
                    "Black Panther",
                    "Finding Nemo",
                    "Iron Man"
            };
        }

        return movies;
    }

    public void enterSeriesOnSearchBar() {
        String[] seriesList = getSeriesList();
        for (String series : seriesList) {
            LOGGER.info("Enter series name to search: {}", series);
            defaultSearchBar.type(series);
            if (firstSearchResult.isElementPresent(5)) {
                firstSearchResult.click();
                break;
            } else {
                defaultSearchBar.sendKeys(Keys.DELETE);
                defaultSearchBar.type((series));
            }
        }
    }

    private String[] getSeriesList() {
        String[] series;

        if ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) {
            series = new String[]{
                    "Dollface",
                    "Family Guy",
                    "Lost",
                    "Modern Family",
                    "Criminal Minds",
                    "God"
            };
        } else {
            series = new String[]{
                    "The Simpsons"
            };
        }

        return series;
    }

    @Override
    public boolean isOpened() {
        int timeout = LONG_TIMEOUT * 5;
        if (defaultSearchBar.isElementPresent(timeout)) {
            LOGGER.info("Search page is opened");
            return true;
        }
        return false;
    }

    public boolean verifyDefaultSearchBarIsVisible() {
        LOGGER.info("Verify search bar is visible");
        return defaultSearchBar.isVisible();
    }

    public DisneyPlusHomePageSearchPage homePageSearchContentLazyLoad() {
        LOGGER.info("Scroll down to the bottom of the page");
        waitForSeconds(5);
        int numOfCollectionsBeforeScroll = searchCollection.size();
        if (numOfCollectionsBeforeScroll > 0) {
            util.scrollToBottom();
            waitForSeconds(2);
            int numOfCollectionsAfterScroll = searchCollection.size();
            while (numOfCollectionsAfterScroll > numOfCollectionsBeforeScroll) {
                numOfCollectionsBeforeScroll = numOfCollectionsAfterScroll;
                util.scrollToBottom();
                waitForSeconds(2);
                numOfCollectionsAfterScroll = searchCollection.size();
                if (numOfCollectionsAfterScroll == numOfCollectionsBeforeScroll)
                    break;
            }
        }
        return this;
    }

    public String getSearchBarText() {
        LOGGER.info("Get the text available in the search bar");
        return defaultSearchBar.getAttribute("value");
    }

    public boolean verifyNoSearchResultsFoundTextIsVisible() {
        LOGGER.info("Verify the no search results found text is visible");
        return noSearchResultsFound.isVisible();
    }

    public boolean isSearchResultsContainingChar(String charToSearch) {
        LOGGER.info("Verify search results names contains char {}", charToSearch);
        boolean containsChar = false;
        if (!getContainerList().isEmpty()) {
            for (ExtendedWebElement result : getContainerList()) {
                containsChar = result.getAttribute("alt").contains(charToSearch.toUpperCase()) ||
                        result.getAttribute("alt").contains(charToSearch.toLowerCase());
                if (!containsChar) {
                    break;
                }
            }
        }
        return containsChar;
    }

    public boolean verifySearchListContainsTitle(String title) {
        LOGGER.info("Verify if search title {} is available in search results", title);
        ArrayList<String> titleList = new ArrayList(getSearchResultTitles());
        for (String searchResultTitle : titleList) {
            if (searchResultTitle.equalsIgnoreCase(title)) {
                LOGGER.info("Title {} is available in search results", title);
                return true;
            }
        }
        LOGGER.info("Title {} is not available in search results", title);
        return false;
    }

    public boolean isAddToWatchlistBtnPresent() {
        LOGGER.info("Verify if Add to watchlist button is present");
        return getAddToWatchListButton().isElementPresent();
    }

    public String getTileTitle(int tileIndex) {
        LOGGER.info("Get the title in the watchlist based for tile {}", tileIndex);
        return getdPlusTileByIndex(Integer.toString(tileIndex)).getAttribute(WebConstant.ARIA_LABEL);
    }

    public DisneyPlusHomePageSearchPage removeWatchlistItems(int numOfTitles) {
        LOGGER.info("Remove {} titles from watchlist", numOfTitles);
        int tileIndex = 0;
        while (tileIndex < numOfTitles) {
            clickDplusTileByIndex(tileIndex);
            clickToRemoveWatchlistItem();
            tileIndex++;
            clickOnWatchList();
        }
        return this;
    }

    public List<String> getWatchlistTitles() {
        List watchlistTitles = new ArrayList<String>();
        for (int tileIndex = 0; tileIndex < tileName.size(); tileIndex++)
            watchlistTitles.add(getdPlusTileByIndex(Integer.toString(tileIndex)).getAttribute(WebConstant.ARIA_LABEL));
        return watchlistTitles;
    }

    public boolean isTitlePresentInWatchlist(String title) {
        LOGGER.info("verify if title {} is present in watchlist", title);
        for(String tileTitle: getWatchlistTitles()) {
            if(tileTitle.equalsIgnoreCase(title))
                return true;
        }
        return false;
    }

    public DisneyPlusHomePageSearchPage clickOnFirstSearchResult() {
        LOGGER.info("Click on the first search result");
        firstSearchResult.click();
        return this;
    }

    @Override
    public boolean verifyTileImageBadging(String logo) {
        LOGGER.info("Verify the tile image badging for Recommended for you collection in Home Page");
        waitForSeconds(2);
        int j=0;
        while (j < getRecommendedForYouCollectionList().size() / 5) {
            for (int i = 0; i < 5; i++) {
                if (!isTileImageBadgingVisible(getRecommendedForYouTileImg(i), logo) &&
                        isTileImageBadgingVisible(getRecommendedForYouTileHoverImg(i), logo))
                    return false;
                }
                j++;
                if(j!= getRecommendedForYouCollectionList().size() / 5)
                    clickOnRecommendedForYouRightArrow();
            }
        return true;
    }
}
