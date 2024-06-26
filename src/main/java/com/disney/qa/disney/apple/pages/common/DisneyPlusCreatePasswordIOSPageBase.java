package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCreatePasswordIOSPageBase extends DisneyPlusApplePageBase{

    private static final String CONSENT_TEXT_HEADER = "Yes, I would like to receive updates, special offers and other information from Disney+ and The Walt Disney Family of Companies.";

    private static final String CONSENT_SUBTEXT = "By clicking “Agree & Continue,” you agree to the Disney Terms of Use and Disney+ Subscriber Agreement, and acknowledge you have read our Privacy Policy and US State Privacy Rights Notice.";

    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    protected ExtendedWebElement signUpBtn;

    protected ExtendedWebElement emailInUseText = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FAT_FINGER_EMAIL.getText()));

    @ExtendedFindBy(accessibilityId = "buttonShowHidePassword")
    private ExtendedWebElement showHideEyeIcon;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeSecureTextField'$]/XCUIElementTypeOther/**/XCUIElementTypeImage[1]")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeSecureTextField'$]/XCUIElementTypeOther/**/XCUIElementTypeImage[3]")
    private ExtendedWebElement myDisneyLogo;

    public DisneyPlusCreatePasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return headlineHeader.isPresent();
    }

    public boolean isHidePasswordIconPresent() {
        return showHideEyeIcon.isElementPresent();
    }

    public boolean isDisneyLogoDisplayed() {
        return dPlusLogo.isPresent();
    }

    public boolean isMyDisneyLogoDisplayed() {
        return myDisneyLogo.isPresent();
    }

    public boolean isPasswordBodyTextDisplayed() {
        String[] passwordBodyText=getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CREATE_PASSWORD_BODY.getText()).split("\n");
        return getTextViewByName(passwordBodyText[0]).isElementPresent();
    }

    public boolean isPasswordBodySubTextDisplayed(String email) {
        String[] passwordBodyText=getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CREATE_PASSWORD_BODY.getText()).split("\n");
        String passwordBodySubText = getDictionary().formatPlaceholderString(passwordBodyText[2], Map.of("email", email,"link_1" , "(edit)"));
        return getTextViewByName(passwordBodySubText).isElementPresent();
    }

    public void clickShowHidePassword() {
        showHideEyeIcon.click();
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
        ExtendedWebElement passwordStrengthHeader = getStaticTextByLabel(getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PASSWORD_REQS.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2"))));
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

    public boolean isMarketingTextDisplayed() {
        return getTextViewByName(CONSENT_TEXT_HEADER).isElementPresent();
    }

    public boolean isConsentLegalTextDisplayed() {
        return getTextViewByName(CONSENT_SUBTEXT).isElementPresent();
    }

}
