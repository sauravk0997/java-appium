package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class AppStorePageBase extends DisneyPlusApplePageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "AppStore.productPage")
    private ExtendedWebElement appStoreScreen;
    @ExtendedFindBy(accessibilityId = "AppStore.shelfItemSubComponent.title")
    private ExtendedWebElement appStoreScreenTitle;

    //FUNCTIONS
    public AppStorePageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isAppStoreAppOpen() {
        return appStoreScreen.isPresent();
    }

    public String getAppStoreAppScreenTitle() {
        return appStoreScreenTitle.getText();
    }
}