package com.disney.qa.disney.web.appex.search;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSearchPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SEARCH = "SEARCH";

    @FindBy(xpath = "//p[contains(text(),'%s')]")
    private ExtendedWebElement paragraphContainsText;

    @FindBy(css = "#search-input")
    private ExtendedWebElement searchBar;

    @FindBy(xpath = "//div[@aria-label='%s']")
    private ExtendedWebElement divAriaLabel;

    public DisneyPlusSearchPage(WebDriver driver) {
        super(driver);
    }

    public boolean isSearchBarPresent() {
        return searchBar.isElementPresent();
    }

    public void clickSearch() {
        paragraphContainsText.format(SEARCH).click();
    }

    public void searchForAsset(String searchQuery) {
        LOGGER.info(String.format("Search Query: %s", searchQuery));
        searchBar.type(searchQuery);
    }

    public boolean clickAssetIfPresent(String asset) {
        boolean isAssetPresent = false;
        LOGGER.info(String.format("Searching for Asset: %s", asset));
        if (divAriaLabel.format(asset).isElementPresent()) {
            LOGGER.info(String.format("Asset: %s Found... Opening Content", asset));
            divAriaLabel.format(asset).click();
            isAssetPresent = true;
        }
        return isAssetPresent;
    }
}
