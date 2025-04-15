package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.*;

import java.lang.invoke.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWatchlistIOSPageBase extends DisneyPlusApplePageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusWatchlistIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "emptyView")
    private ExtendedWebElement watchlistEmpty;

    public void waitForWatchlistPageToOpen() {
        LOGGER.info("Waiting for watchlist page to load");
        fluentWait(getDriver(), TWENTY_FIVE_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Watchlist page is not opened")
                .until(it -> headerViewTitleLabel.isPresent(THREE_SEC_TIMEOUT));
    }

    public boolean isWatchlistTitlePresent(String contentTitle) {
        return getTypeCellLabelContains(contentTitle).isPresent();
    }

    public void tapWatchlistContent(String contentTitle) {
        getTypeCellLabelContains(contentTitle).click();
    }

    public boolean isWatchlistScreenDisplayed() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.WATCHLIST_SCREEN.getText()))
                .isElementPresent();
    }

    public boolean isWatchlistEmptyBackgroundDisplayed() {
        return watchlistEmpty.isPresent();
    }

    public boolean areWatchlistTitlesDisplayed(String... titles) {
        List<String> items = Arrays.asList(titles);
        List<Boolean> validations = new ArrayList<>();
        items.forEach(title -> {
            ExtendedWebElement watchlistItem = getTypeCellLabelContains(title);
            validations.add(watchlistItem.isElementPresent());
        });
        return !validations.contains(false);
    }

    public boolean areWatchlistTitlesProperlyOrdered(String... titles) {
        List<String> items = Arrays.asList(titles);
        List<ExtendedWebElement> entryCells = getCellsWithLabels();
        List<Boolean> validations = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            try {
                String entryCellTextOnlyTitle = entryCells.get(i).getText().split(",")[0];
                validations.add(entryCellTextOnlyTitle.equals(titles[i]));
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return !validations.contains(false);
    }
}
