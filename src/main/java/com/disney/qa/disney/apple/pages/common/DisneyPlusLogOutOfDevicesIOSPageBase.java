package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLogOutOfDevicesIOSPageBase extends DisneyPlusApplePageBase{

    @FindBy(id = "headlineHeader")
    ExtendedWebElement header;

    public DisneyPlusLogOutOfDevicesIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return header.isElementPresent();
    }

    public void clickCancelButton() {
        dismissBtn.click();
    }
}
