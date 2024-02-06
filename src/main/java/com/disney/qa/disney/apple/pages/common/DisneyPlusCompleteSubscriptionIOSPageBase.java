package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
/**
 * Page for when the user logs into the D+ application with an account that has never had an active subscription
 */
public class DisneyPlusCompleteSubscriptionIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusCompleteSubscriptionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "dismissButton")
    private ExtendedWebElement dimissBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == 'paywallLandingComplete'`]/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeImage")
    private ExtendedWebElement heroImage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther")
    private ExtendedWebElement disneyPlusLogo;

    private ExtendedWebElement primaryText = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMPLETE_SUB_TITLE);
    private ExtendedWebElement secondaryText = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.COMPLETE_SUB_COPY);
    private ExtendedWebElement completeSubscriptionButton = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMPLETE_SUBSCRIPTION_BTN);
    //Logout button
    public ExtendedWebElement getDismissButton() {
        return dimissBtn;
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

    public ExtendedWebElement getCompleteSubscriptionButton() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return completeSubscriptionButton;
    }
}
