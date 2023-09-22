package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWelcomeScreenIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    protected ExtendedWebElement signUpButton;

    @ExtendedFindBy(accessibilityId = "loginButton")
    protected ExtendedWebElement loginButton;

    @ExtendedFindBy(accessibilityId = "dismissButton")
    private ExtendedWebElement logOutButton;

    @ExtendedFindBy(accessibilityId = "customButton")
    private ExtendedWebElement completeSubscriptionButton;

    @ExtendedFindBy(accessibilityId = "Cancel")
    private ExtendedWebElement paywallCancelButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"monthly\"`]")
    private ExtendedWebElement monthlySubButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"yearly\"`]")
    private ExtendedWebElement yearlySubButton;

    @ExtendedFindBy(accessibilityId = "restoreButton")
    private ExtendedWebElement restoreButton;

    @ExtendedFindBy(accessibilityId = "imageLogo")
    private ExtendedWebElement backgroundImage;

    @ExtendedFindBy(accessibilityId = "Disney Plus Logo")
    private ExtendedWebElement disneyPlusLogo;

    private ExtendedWebElement welcomeTagline = findByAccessibilityId(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.WELCOME_BASE_TEXT);

    //FUNCTIONS

    public DisneyPlusWelcomeScreenIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SIGN_UP_BTN.getText())).isElementPresent();
    }

    public boolean isBackgroundDisplayed() {
        return backgroundImage.isElementPresent();
    }

    public boolean isDisneyPlusLogoDisplayed() {
        return disneyPlusLogo.isElementPresent();
    }

    public boolean isMainTextDisplayed() {
        return welcomeTagline.isElementPresent();
    }

    public boolean isSubCtaPresent() {
        String subscribeText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.WELCOME_SUB_TEXT.getText());
        subscribeText = getDictionary().formatPlaceholderString(subscribeText, Map.of("PRICE_0", "---", "TIME_UNIT_0", "---", "PRICE_1", "---", "TIME_UNIT_1", "---"));
        return staticTextByLabel.format(subscribeText).isElementPresent();
    }

    public boolean isSignUpButtonDisplayed() {
        return signUpButton.isElementPresent();
    }

    public boolean isLogInButtonDisplayed() {
        return loginButton.isElementPresent();
    }

    public boolean isLogOutButtonDisplayed() {
        return logOutButton.isElementPresent();
    }

    public boolean isCompleteSubscriptionButtonDisplayed() {
        return completeSubscriptionButton.isElementPresent();
    }

    public boolean isCancelButtonDisplayed() {
        return paywallCancelButton.isElementPresent();
    }

    public boolean isMonthlySubButtonDisplayed() {
        return monthlySubButton.isElementPresent();
    }

    public boolean isYearlySubButtonDisplayed() {
        return yearlySubButton.isElementPresent();
    }

    public boolean isRestoreButtonDisplayed() {
        return restoreButton.isElementPresent();
    }

    public void clickLogInButton() {
        getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN_BTN.getText())).click();
    }

    public void clickSignUpButton() {
        signUpButton.click();
    }

    public void clickCompleteSubscriptionButton() {
        completeSubscriptionButton.click();
    }

    public void logOutFromUnentitledAccount() {
        paywallCancelButton.click();
        systemAlertDefaultBtn.click();
        logOutButton.click();
        systemAlertDefaultBtn.click();
    }

    public By getSignUpButtonBy() {
        return signUpButton.getBy();
    }

    public ExtendedWebElement getSignupButton() {
        return signUpButton;
    }

}
