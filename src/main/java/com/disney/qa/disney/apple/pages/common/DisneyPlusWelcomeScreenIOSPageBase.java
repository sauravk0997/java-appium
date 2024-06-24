package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
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

    @ExtendedFindBy(accessibilityId = "Don’t Allow")
    private ExtendedWebElement dontAllowbtn;

    //FUNCTIONS

    public DisneyPlusWelcomeScreenIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return signUpButton.isPresent();
    }

    public boolean isBackgroundDisplayed() {
        return backgroundImage.isElementPresent();
    }

    public boolean isDisneyPlusLogoDisplayed() {
        return disneyPlusLogo.isPresent();
    }

    public boolean isMainTextDisplayed() {
        return staticTextLabelContains.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE, DictionaryKeys.WELCOME_UNAUTHENTICATED_TITLE.getText())).isPresent();
    }

    //TODO: Investigate why this dictionary key is not found QAA-12657
    public boolean isSubCopyPresent() {
        String subscribeText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE, DictionaryKeys.WELCOME_UNAUTHENTICATED_SUBCOPY.getText());
        String subscribeText2 = getDictionary().formatPlaceholderString(subscribeText, Map.of("PRICE_0", "---", "TIME_UNIT_0", "---"));
        return staticTextByLabel.format(subscribeText2).isPresent();
    }

    public boolean isSubCopyDirectTextPresent() {
        String subCopyDirectText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE, DictionaryKeys.WELCOME_UNAUTHENTICATED_SUBCOPY.getText());
        return staticTextNameContains.format(subCopyDirectText).isPresent();
    }

    public ExtendedWebElement getSubCopyDirectText(){
        return staticTextNameContains.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE, DictionaryKeys.WELCOME_UNAUTHENTICATED_SUBCOPY.getText()));
    }

    public boolean isSignUpButtonDisplayed() {
        return signUpButton.isPresent();
    }

    public boolean isLogInButtonDisplayed() {
        return loginButton.isPresent();
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
        clickDontAllowBtn();
        loginButton.click();
    }

    public void clickSignUpButton() {
        clickDontAllowBtn();
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

    public void clickDontAllowBtn() {
        dontAllowbtn.clickIfPresent();
    }
}
