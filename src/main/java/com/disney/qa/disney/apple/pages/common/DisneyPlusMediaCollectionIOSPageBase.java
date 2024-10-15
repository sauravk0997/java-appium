package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusMediaCollectionIOSPageBase extends DisneyPlusApplePageBase {

    @FindBy(id = "segmentedControl")
    private ExtendedWebElement categoryScroller;

    @FindBy(id = "selectorButton")
    private ExtendedWebElement mediaCategoryDropdown;

    private final ExtendedWebElement seriesHeader = xpathNameOrName.format(iapiHelper.getLocalizationUtils().
                    getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.NAV_SERIES_TITLE.getText()),
            DictionaryKeys.NAV_SERIES_TITLE.getText());

    private final ExtendedWebElement moviesHeader = xpathNameOrName.format(iapiHelper.getLocalizationUtils().
                    getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.NAV_MOVIES_TITLE.getText()),
            DictionaryKeys.NAV_MOVIES_TITLE.getText());

    public DisneyPlusMediaCollectionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getMediaCategoryDropdown() {
        return mediaCategoryDropdown;
    }

    public ExtendedWebElement getCategoryScroller() {
        return categoryScroller;
    }

    public ExtendedWebElement getSeriesHeader() {
        return seriesHeader;
    }

    public ExtendedWebElement getMoviesHeader() {
        return moviesHeader;
    }
}
