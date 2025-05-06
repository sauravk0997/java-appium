package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWelcomeScreenIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    protected ExtendedWebElement signUpButton;

    @ExtendedFindBy(accessibilityId = "loginButton")
    protected ExtendedWebElement loginButton;

    private ExtendedWebElement forceUpdateTitle = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.UPDATE_APP_TITLE.getText()));

    //FUNCTIONS

    public DisneyPlusWelcomeScreenIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return loginButton.isPresent(FIVE_SEC_TIMEOUT);
    }

    public boolean isMainTextDisplayed() {
        return staticTextLabelContains.format(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE_ONBOARDING,
                        DictionaryKeys.WELCOME_UNAUTHENTICATED_TITLE.getText())).isPresent();
    }

    public boolean isSubCopyDirectTextPresent() {
        String subCopyDirectText = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE_ONBOARDING,
                        DictionaryKeys.WELCOME_UNAUTHENTICATED_SUBCOPY_BOOKWORM.getText());
        return staticTextNameContains.format(subCopyDirectText).isPresent();
    }

    public boolean isLogInButtonDisplayed() {
        return loginButton.isPresent();
    }

    public void clickLogInButton() {
        handleSystemAlert(AlertButtonCommand.DISMISS, 2);
        loginButton.click();
    }

    public void clickForceUpdateTitle() {
        forceUpdateTitle.click();
    }

    public boolean isForceAppUpdateTitlePresent() {
        return fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT,
                "Force Update page did not Open ")
                .until(it -> forceUpdateTitle.isPresent());
    }

    public boolean isForceAppUpdateMessagePresent() {
        return getTextViewByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.UPDATE_APP_BODY.getText())).isPresent();
    }

    public boolean isForceAppUpdateButtonPresent() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                    DictionaryKeys.BTN_UPDATE_APP.getText()).toUpperCase()).isPresent();
    }
}
