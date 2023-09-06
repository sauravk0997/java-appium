package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOriginalsIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    private final ExtendedWebElement originalsPageLoad = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.ORIGINALS_PAGE_LOAD.getText()));

    //FUNCTIONS

    public DisneyPlusOriginalsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isOriginalPageLoadPresent() {
        return originalsPageLoad.isElementPresent();
    }
}
