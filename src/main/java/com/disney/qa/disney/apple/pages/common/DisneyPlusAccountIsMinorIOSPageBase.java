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

    private ExtendedWebElement notEligibleHeader = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ACCOUNT_BLOCK_HEADER);

    private ExtendedWebElement helpCenterButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.BTN_HELP_CENTER);

    private ExtendedWebElement dismissButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_DISMISS_BTN);

    @Override
    public boolean isOpened() { return notEligibleHeader.isElementPresent(); }

    public ExtendedWebElement getNotEligibleHeader() { return notEligibleHeader; }

    public ExtendedWebElement getHelpCenterButton() { return helpCenterButton; }

    public ExtendedWebElement getDismissButton() { return dismissButton; }

    public void clickHelpCenterButton() {
        helpCenterButton.click();
    }

    public void clickDismissButton() {
        dismissButton.click();
    }

    public ExtendedWebElement getNotEligibleSubText() {
        String subscribeText = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ACCOUNT_BLOCK_BODY.getText()), Map.of("link_1", "here"));
        return staticTextByLabel.format(subscribeText);
    }
}