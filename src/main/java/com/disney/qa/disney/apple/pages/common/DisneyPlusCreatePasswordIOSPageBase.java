package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCreatePasswordIOSPageBase extends DisneyPlusApplePageBase{

    protected ExtendedWebElement createPasswordHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CREATE_PASSWORD_SCREEN_TITLE.getText()));

    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    protected ExtendedWebElement signUpBtn;

    protected ExtendedWebElement emailInUseText = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FAT_FINGER_EMAIL.getText()));

    public DisneyPlusCreatePasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return createPasswordHeader.isElementPresent();
    }

    public boolean isHidePasswordIconPresent() {
        return showHidePasswordIndicator.isElementPresent();
    }

    public void clickShowHidePassword() {
        showHidePasswordIndicator.click();
    }

    public boolean isPasswordEntryFieldPresent() {
        return passwordEntryField.isElementPresent();
    }

    public String getPasswordEntryText() {
        return passwordEntryField.getText();
    }

    public void enterPasswordValue(String value) {
        passwordEntryField.type(value);
    }

    public void submitPasswordValue(String value) {
        enterPasswordValue(value);
        new IOSUtils().clickElementAtLocation(passwordEntryField, 0, 50);
        clickPrimaryButton();
    }

    public boolean isPasswordStrengthHeaderPresent() {
        ExtendedWebElement passwordStrengthHeader = getStaticTextByLabel(getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PASSWORD_REQS_ENHANCED.getText()), "6", "2"));
        return passwordStrengthHeader.isElementPresent();
    }

    public boolean isEmailInUseDisplayed(String email) {
        return emailInUseText.isElementPresent() && getDynamicAccessibilityId(email).isElementPresent();
    }

    public boolean isInvalidPasswordErrorDisplayed() {
        ExtendedWebElement passwordLengthError = getStaticTextByLabel(getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), "6", "2"));
        return passwordLengthError.isElementPresent();
    }

}
