package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusSearchIOSPageBase.class)
public class DisneyPlusAppleTVSearchPage extends DisneyPlusSearchIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @ExtendedFindBy(accessibilityId = "searchBar")
    private ExtendedWebElement searchField;

    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeOther[`name == 'searchResults'`]/**/XCUIElementTypeCell[`label CONTAINS '%s'`]")
    private ExtendedWebElement searchResultsContainers;

    @ExtendedFindBy(iosClassChain =
            "**/XCUIElementTypeOther[`name == 'searchResults'`]/**/XCUIElementTypeCollectionView/XCUIElementTypeCell")
    private ExtendedWebElement allSearchResultsContainers;

    public DisneyPlusAppleTVSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = searchField.isElementPresent();
        return isPresent;
    }

    public void typeInSearchField(String text) {
        searchField.type(text);
    }

    public ExtendedWebElement getSearchResultsContainers(String label) {
        return searchResultsContainers.format(label);
    }

    public void clickSearchResult(String assetName) {
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        getSearchResults(assetName).get(0).click();
    }

    public List<ExtendedWebElement> getSearchResults(String assetName) {
        List<ExtendedWebElement> searchResults = findExtendedWebElements(getSearchResultsContainers(assetName).getBy());
        if (!searchResults.isEmpty()) {
            return searchResults;
        } else {
            throw new IllegalArgumentException("No search results found");
        }
    }

    public List<ExtendedWebElement> getAllSearchResults() {
        fluentWait(getDriver(), TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT,
                "No search results found")
                .until(it -> !findExtendedWebElements(allSearchResultsContainers.getBy()).isEmpty());
        return findExtendedWebElements(allSearchResultsContainers.getBy());
    }

    public void clickLocalizedSearchResult(String assetName) {
        if (localizedKeyboard.getSize().getWidth() > 1000) {
            LOGGER.info("Detected horizontal keyboard, clicking down to search results..");
            keyPressTimes(IRemoteControllerAppleTV::clickDown, 1, 1);
        } else {
            LOGGER.info("Detected vertical keyboard, clicking right to search results..");
            keyPressTimes(IRemoteControllerAppleTV::clickRight, 6, 1);
        }
        getTypeCellLabelContains(assetName).clickIfPresent();
    }

    public void clearSearchBar() {
        moveDown(5, 1);
        moveRight(2, 1);
        keyPressTimes(IRemoteControllerAppleTV::clickSelect, 1, 5);
    }

    public ExtendedWebElement getSearchField() {
        return searchField;
    }

    public void navigateToKeyboardFromResult() {
        if (!localizedKeyboard.isPresent())
            moveUp(2, 1);

        if (localizedKeyboard.getSize().getWidth() > 1000) {
            LOGGER.info("Detected horizontal keyboard, clicking Up");
            keyPressTimes(IRemoteControllerAppleTV::clickUp, 2, 1);
        } else {
            LOGGER.info("Detected vertical keyboard, clicking Left");
            keyPressTimes(IRemoteControllerAppleTV::clickLeft, 2, 1);
        }
    }
}
