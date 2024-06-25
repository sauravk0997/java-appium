package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCreatePasswordIOSPageBase extends DisneyPlusApplePageBase{

    protected ExtendedWebElement createPasswordHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PASSWORD_SCREEN_TITLE.getText()));

    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    protected ExtendedWebElement signUpBtn;

    protected ExtendedWebElement emailInUseText = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FAT_FINGER_EMAIL.getText()));

    @ExtendedFindBy(accessibilityId = "buttonShowHidePassword")
    private ExtendedWebElement ShowHideIconBtn;

    public DisneyPlusCreatePasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return createPasswordHeader.isPresent();
    }

    public boolean isHidePasswordIconPresent() {
        return ShowHideIconBtn.isElementPresent();
    }

    public void clickShowHidePassword() {
        ShowHideIconBtn.click();
    }

    public boolean isPasswordEntryFieldPresent() {
        return passwordEntryField.isElementPresent();
    }

    public String getPasswordEntryText() {
        return passwordEntryField.getText();
    }

    public void tapSignUpButton() {
        staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SIGN_UP.getText())).click(SHORT_TIMEOUT);
    }

    public void enterPasswordValue(String value) {
        passwordEntryField.type(value);
    }

    public void submitPasswordValue(String value) {
        enterPasswordValue(value);
        clickElementAtLocation(passwordEntryField, 0, 50);
        dismissKeyboardForPhone();
        clickPrimaryButton();
    }

    public boolean isPasswordStrengthHeaderPresent() {
        ExtendedWebElement passwordStrengthHeader = getStaticTextByLabel(getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PASSWORD_REQS_ENHANCED.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2"))));
        return passwordStrengthHeader.isElementPresent();
    }

    public boolean isEmailInUseDisplayed(String email) {
        return emailInUseText.isElementPresent() && getDynamicAccessibilityId(email).isElementPresent();
    }

    public boolean isInvalidPasswordErrorDisplayed() {
        ExtendedWebElement passwordLengthError = getStaticTextByLabel(getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2"))));
        return passwordLengthError.isElementPresent();
    }

    public boolean isEmptyPasswordErrorDisplayed() {
        ExtendedWebElement passwordEmptyError = getStaticTextByLabel((getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PASSWORD_CREATION_ERROR_EMPTY.getText())));
        return passwordEmptyError.isElementPresent();
    }

    public boolean isCreateNewPasswordPageOpened() {
        String createNewPasswordPageHeader = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CREATE_PASSWORD_HEADER.getText());
        return getStaticTextByLabelContains(createNewPasswordPageHeader).isElementPresent();
    }

}
