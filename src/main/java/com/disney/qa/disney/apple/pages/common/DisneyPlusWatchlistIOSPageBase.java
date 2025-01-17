package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import org.openqa.selenium.WebDriver;
import org.slf4j.*;

import java.lang.invoke.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWatchlistIOSPageBase extends DisneyPlusApplePageBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusWatchlistIOSPageBase(WebDriver driver) {
        super(driver);
    }

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
}
