package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
/**
 * Page for when the user's entitlement expires
 */
public class DisneyPlusRestartSubscriptionIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusRestartSubscriptionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == 'paywallLandingRestart'`]/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeImage")
    private ExtendedWebElement heroImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == 'paywallLandingRestart'`]/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeImage")
    private ExtendedWebElement disneyPlusLogo;

    // After clicking Log Out button once. Not the same Identifier as "dismissBtn".
    private ExtendedWebElement logOutConfirmationButton = dynamicBtnFindByName.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_LOGOUT.getText()));
    private ExtendedWebElement logOutCancelButton = dynamicBtnFindByName.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CANCEL_BTN_CAPS.getText()));

    private ExtendedWebElement primaryText = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.RESTART_SUB_COPY);
    private ExtendedWebElement secondaryText = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.RESTART_SUB_COPY_2);
    private ExtendedWebElement restartSubscriptionButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.RESTART_SUBSCRIPTION);

    public ExtendedWebElement getLogoutButton() {
        return dismissBtn;
    }

    public ExtendedWebElement getHeroImage() {
        return heroImage;
    }

    public ExtendedWebElement getDisneyPlusLogo() {
        return disneyPlusLogo;
    }

    public ExtendedWebElement getPrimaryText() {
        return primaryText;
    }

    public ExtendedWebElement getSecondaryText() {
        return secondaryText;
    }

    public ExtendedWebElement getRestartSubscriptionButton() {
        return restartSubscriptionButton;
    }

    public void clickRestartSubscriptionButton() {
        restartSubscriptionButton.click();
    }

    public ExtendedWebElement getLogOutConfirmationButton() { return logOutConfirmationButton; }

    public ExtendedWebElement getLogOutCancelButton() { return logOutCancelButton; }
}
