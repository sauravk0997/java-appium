package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.FORGOT_PASSWORD;

/*
 * Enter Password Page
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPasswordIOSPageBase extends DisneyPlusApplePageBase {

    private ExtendedWebElement forgotPasswordLink = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, FORGOT_PASSWORD.getText()));

    @FindBy(xpath = "//XCUIElementTypeButton[@name='buttonBack']/../following-sibling::*/*/XCUIElementTypeImage")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(accessibilityId = "passwordStrengthHeader")
    protected ExtendedWebElement passwordHintText;

    @ExtendedFindBy(accessibilityId = "primaryButton")
    protected ExtendedWebElement logInButton;

    @ExtendedFindBy(accessibilityId = "CONTINUE")
    protected ExtendedWebElement continueButton;

    @ExtendedFindBy(accessibilityId = "labelErrorMessage")
    protected ExtendedWebElement labelError;

    public DisneyPlusPasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = headlineHeader.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isCreatePasswordTextFieldFocused() {
        return isFocused(passwordEntryField);
    }

    public boolean isBackArrowDisplayed() {
        return backArrow.isPresent();
    }

    public boolean isDisneyLogoDisplayed() {
        return dPlusLogo.isPresent();
    }

    public boolean isHeaderTextDisplayed() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ENTER_YOUR_PASSWORD.getText())).isPresent();
    }

    public boolean isConfirmWithPasswordTitleDisplayed() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_PASSWORD_TITLE.getText())).isPresent();
    }

    public boolean isPasswordFieldDisplayed() {
        return passwordEntryField.isPresent();
    }

    public boolean isShowPasswordIconDisplayed() {
        boolean isPresent = showHidePasswordIndicator.isPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent && showHidePasswordIndicator.getAttribute("label").equals("show");
    }

    public boolean isHidePasswordIconDisplayed() {
        boolean isPresent = showHidePasswordIndicator.isPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent && showHidePasswordIndicator.getAttribute("label").equals("hide");
    }

    public boolean isPasswordHintTextDisplayed() {
        return passwordHintText.isPresent();
    }

    public boolean isLoginButtonDisplayed() {
        return logInButton.isPresent();
    }

    public boolean isForgotPasswordLinkDisplayed() {
        return forgotPasswordLink.isPresent();
    }

    public void clickForgotPasswordLink() {
        forgotPasswordLink.click();
    }

    public void typePassword(String password) {
        passwordEntryField.type(password);
    }

    public void submitPasswordForLogin(String userPassword) {
        typePassword(userPassword);
        UniversalUtils.captureAndUpload(getCastedDriver());
        new IOSUtils().hideKeyboard();
    }

    public void submitPasswordWhileLoggedIn(String userPassword) {
        typePassword(userPassword);
        UniversalUtils.captureAndUpload(getCastedDriver());
        if (continueButton.isElementPresent()) {
            continueButton.click();
        } else {
            new IOSUtils().clickNearElement(getTypeButtonByLabel("CONTINUE"), 0.5, 30);
        }
    }

    public String getPasswordText() {
        return passwordEntryField.getText();
    }

    public void clickShowPasswordIcon() {
        showHidePasswordIndicator.click();
    }

    public void clickHidePasswordIcon() {
        showHidePasswordIndicator.click();
    }

    public String getErrorMessageString() {
        return labelError.getText();
    }
}