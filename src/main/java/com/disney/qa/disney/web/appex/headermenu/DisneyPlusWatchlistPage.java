package com.disney.qa.disney.web.appex.headermenu;

import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWatchlistPage extends DisneyPlusBaseTilesPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@id='watchlist-collection']//a")
    private List<ExtendedWebElement> watchlistContentTileList;

    @FindBy(xpath = "//*[@data-testid='asset-wrapper-0-0']/div")
    private ExtendedWebElement watchlistContentTileLabel;

    public DisneyPlusWatchlistPage(WebDriver driver) {
        super(driver);
    }

    public List<ExtendedWebElement> getWatchlistContentList() {
        int x = 0;
        while (watchlistContentTileList.isEmpty()) {
            LOGGER.info("Loading watchlist tiles...");
            pause(5);
            x++;
            if (x == 5) {
                break;
            }
        }
        return watchlistContentTileList;
    }

    public void getWatchlistContent(int index) {
        waitFor(watchlistContentTileList.get(index));
        watchlistContentTileList.get(index).click();
    }

    public void getWatchlistContentName(int index) {
        watchlistContentTileList.get(index);
    }

    public String getFirstTileName() {
        return watchlistContentTileLabel.getAttribute(WebConstant.ARIA_LABEL);
    }

    public void searchAndAddContent(String... content) {
        DisneyPlusHomePageSearchPage dHomePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        for (String currentContent : content) {
            waitForPageToFinishLoading();
            dHomePageSearchPage.clickOnSearchBar();
            dHomePageSearchPage.enterTextOnSearchBar(currentContent);
            LOGGER.info("Searching for '" + currentContent + "' movie");
            pause(3);
            analyticPause();
            dHomePageSearchPage.clickToAddSearchResult();
            LOGGER.info("{} added to watchlist", currentContent);
            analyticPause();
        }
    }

    public int getNumberOfWatchListTitles() {
        LOGGER.info("Get the number of watchlist titles");
        return watchlistContentTileList.size();
    }
}
