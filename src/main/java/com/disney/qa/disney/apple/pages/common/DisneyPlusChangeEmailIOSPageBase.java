package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChangeEmailIOSPageBase extends DisneyPlusApplePageBase{

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView/**XCUIElementTypeImage[1]")
    private ExtendedWebElement myDisneyLogo;

    public DisneyPlusChangeEmailIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_HEADER.getText())).isElementPresent();
    }

    public boolean isCurrentEmailShown(String email) {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_CURRENT.getText()).replace("{email}", email))
                .isElementPresent();
    }

    public boolean isNewEmailHeaderPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_HINT.getText())).isElementPresent();
    }

    public void enterNewEmailAddress(String value) {
        textEntryField.type(value);
    }

    public void submitNewEmailAddress(String value) {
        enterNewEmailAddress(value + "\n");
        getKeyboardDoneButton().clickIfPresent(SHORT_TIMEOUT);
        getSaveAndContinueButton().click();
    }

    public boolean isLearnMoreAboutMyDisney() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_LEARN_MORE_BTN.getText())).isElementPresent();
    }

    public boolean isConfirmationPageOpen() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_SUCCESS_HEADER.getText())).isPresent();
    }

    public void clickBackToDisneyBtn() {
        getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_BACK_TO_SERVICE_BTN.getText())).click();
    }

    public void clickLogoutBtn() {
        getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.LOG_OUT_LABEL.getText())).click();
    }

    @Override
    public void clickCancelBtn() {
        getCancelButton().click();
    }

    public boolean isChangeEmailFormatErrorDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_FORMAT_ERROR.getText())).isPresent();
    }

    public boolean isAlreadyInUseEmailErrorMessageDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_IN_USE_ERROR.getText())).isPresent();
    }

    public ExtendedWebElement getMyDisneyLogo() {
        return myDisneyLogo;
    }

    public ExtendedWebElement getCancelButton() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CANCEL_BTN.getText()));
    }

    public ExtendedWebElement getSaveAndContinueButton() {
        return getTypeButtonByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_SAVE_CONTINUE_BTN.getText()));
    }

    public boolean isLogoutOfAllDevicesTextPresent() {
        return getStaticTextByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_LOGOUT_ALL_CHECKBOX.getText())).isPresent();
    }

    public boolean isNewEmailShownOnSuccessPage(String email) {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_SUCCESS_NEW_EMAIL.getText()).replace("{email}", email))
                .isElementPresent();
    }
}
