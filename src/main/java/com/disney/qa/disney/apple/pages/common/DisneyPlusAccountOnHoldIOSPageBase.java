package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
/**
 * Page for when the user's account is on a billing hold
 */
public class DisneyPlusAccountOnHoldIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusAccountOnHoldIOSPageBase(WebDriver driver) {
        super(driver);
    }

    private ExtendedWebElement accountHoldTitle = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_HOLD_TITLE);
    private ExtendedWebElement accountHoldSubText = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.ACCOUNT_HOLD_BAMTECH_DISNEY);
    private ExtendedWebElement updatePaymentButton = getTypeButtonByLabel(iapiHelper.getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_ACCOUNT_HOLD_UPDATE_PAYMENT.getText()));
    private ExtendedWebElement refreshButton = getTypeButtonByLabel(iapiHelper.getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_REFRESH.getText()));

    public ExtendedWebElement getLogoutButton() { return dismissBtn; }

    public ExtendedWebElement getAccountHoldTitle() { return accountHoldTitle; }

    public ExtendedWebElement getAccountHoldSubText() { return accountHoldSubText; }

    public ExtendedWebElement getUpdatePaymentButton() { return updatePaymentButton; }

    public ExtendedWebElement getRefreshButton() {return refreshButton; }
}
