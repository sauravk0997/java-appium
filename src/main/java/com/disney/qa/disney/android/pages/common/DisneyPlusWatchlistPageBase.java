package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWatchlistPageBase extends DisneyPlusCommonPageBase{

    @FindBy(id = "watchlistEmptyState")
    private ExtendedWebElement emptyWatchlistState;

    public DisneyPlusWatchlistPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return title.isElementPresent()
                && title.getText().equals(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_TITLE.getText()));
    }

    /**
     * Waits for posters to be loaded in on the page, then populates them into a List of elements to check if the title
     * is included.
     */

    public boolean isMediaPresent(String media) {
        try {
            waitUntil(ExpectedConditions.visibilityOfElementLocated(shelfItem.getBy()), 30);
        } catch (TimeoutException te) {
            return false;
        }

        List<ExtendedWebElement> posters = findExtendedWebElements(shelfItem.getBy());
        return posters.contains(shelfItem.format(media));
    }
}
