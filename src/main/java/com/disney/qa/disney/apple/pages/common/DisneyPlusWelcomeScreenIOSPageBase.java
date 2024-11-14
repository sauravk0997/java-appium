package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

    private ExtendedWebElement forceUpdateTitle = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.APPLICATION,
            DictionaryKeys.UPDATE_APP_TITLE.getText()));

    //FUNCTIONS

    public DisneyPlusWelcomeScreenIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return loginButton.isPresent();
    }

    public boolean isBackgroundDisplayed() {
        return backgroundImage.isElementPresent();
    }

    public boolean isDisneyPlusLogoDisplayed() {
        return disneyPlusLogo.isPresent();
    }

    public boolean isMainTextDisplayed() {
        return staticTextLabelContains.format(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.WELCOME_UNAUTHENTICATED_TITLE.getText())).isPresent();
    }

    public boolean isSubCopyDirectTextPresent() {
        String subCopyDirectText = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                DictionaryKeys.WELCOME_UNAUTHENTICATED_SUBCOPY.getText());
        return staticTextNameContains.format(subCopyDirectText).isPresent();
    }

    public ExtendedWebElement getSubCopyDirectText(){
        return staticTextNameContains.format(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                        DictionaryKeys.WELCOME_UNAUTHENTICATED_SUBCOPY.getText()));
    }

    public boolean isLogInButtonDisplayed() {
        return loginButton.isPresent();
    }

    public boolean isCompleteSubscriptionButtonDisplayed() {
        return completeSubscriptionButton.isElementPresent();
    }

    public void clickLogInButton() {
        handleSystemAlert(AlertButtonCommand.DISMISS, 2);
        loginButton.click();
    }

    public void clickSignUpButton() {
        handleSystemAlert(AlertButtonCommand.DISMISS, 2);
        signUpButton.click();
    }

    public void clickCompleteSubscriptionButton() {
        completeSubscriptionButton.click();
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

    public void clickForceUpdateTitle() {
        forceUpdateTitle.click();
    }
    public boolean isForceAppUpdateTitlePresent() {
        return forceUpdateTitle.isPresent();
    }

    public boolean isForceAppUpdateMessagePresent() {
        return getTextViewByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.UPDATE_APP_BODY.getText())).isPresent();
    }

    public boolean isForceAppUpdateButtonPresent() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.BTN_UPDATE_APP.getText())).isPresent();
    }
}
