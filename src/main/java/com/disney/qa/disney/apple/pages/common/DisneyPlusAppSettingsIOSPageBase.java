package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAppSettingsIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusAppSettingsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    private ExtendedWebElement deleteAllDownloadsButton = getStaticTextByLabel(
            getLocalizationUtils()
                    .getDictionaryItem(
                            DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.BTN_DELETE_ALL_DOWNLOADS.getText()));

    public ExtendedWebElement getDeleteAllDownloadsButton() {
        return deleteAllDownloadsButton;
    }
}
