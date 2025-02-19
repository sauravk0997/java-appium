package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
/**
 * Landing page when the user logs in with a minor account
 */

public class DisneyPlusAccountIsMinorIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusAccountIsMinorIOSPageBase(WebDriver driver) { super(driver); }

    private ExtendedWebElement notEligibleHeader = getStaticTextByName("Sorry, you're not eligible to use this service.");

    private ExtendedWebElement helpCenterButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.BTN_HELP_CENTER);

    private ExtendedWebElement dismissButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_DISMISS_BTN);

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == \"%s\"`]/*[2]")
    protected ExtendedWebElement blockedDescription;

    @Override
    public boolean isOpened() { return notEligibleHeader.isElementPresent(); }

    public ExtendedWebElement getBlockedDescription(String name) {
        return blockedDescription.format(name);
    }

    public ExtendedWebElement getHelpCenterButton() { return helpCenterButton; }

    public ExtendedWebElement getDismissButton() { return dismissButton; }

    public void clickHelpCenterButton() {
        helpCenterButton.click();
    }

    public void clickDismissButton() {
        dismissButton.click();
    }

}
