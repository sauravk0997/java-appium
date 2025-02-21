package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
/**
 * Landing page when the user logs in with a minor account
 */

public class DisneyPlusAccountIsMinorIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusAccountIsMinorIOSPageBase(WebDriver driver) { super(driver); }

    private ExtendedWebElement helpCenterButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.BTN_HELP_CENTER);

    private ExtendedWebElement dismissButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_DISMISS_BTN);

    @Override
    public boolean isOpened() { return helpCenterButton.isElementPresent(); }

    public ExtendedWebElement getHelpCenterButton() { return helpCenterButton; }

    public ExtendedWebElement getDismissButton() { return dismissButton; }

    public void clickHelpCenterButton() {
        helpCenterButton.click();
    }

    public void clickDismissButton() {
        dismissButton.click();
    }
}
