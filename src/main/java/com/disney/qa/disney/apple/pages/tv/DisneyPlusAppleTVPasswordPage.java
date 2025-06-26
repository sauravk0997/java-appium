package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.*;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusPasswordIOSPageBase.class)
public class DisneyPlusAppleTVPasswordPage extends DisneyPlusPasswordIOSPageBase {

    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    private ExtendedWebElement createPasswordTextField;
    @ExtendedFindBy(accessibilityId = "buttonShowHidePassword")
    private ExtendedWebElement hideShowPasswordBtn;
    @ExtendedFindBy(iosPredicate = "type == \"XCUIElementTypeTextView\"")
    protected ExtendedWebElement passwordOnScreenField;
    @ExtendedFindBy(accessibilityId = "hidePasswordDisneyAuth")
    private ExtendedWebElement hidePasswordDisneyAuth;
    @ExtendedFindBy(accessibilityId = "showPasswordDisneyAuth")
    private ExtendedWebElement showPasswordDisneyAuth;

    private ExtendedWebElement havingTroubleLogginInBtn = getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_LOGIN_HELP.getText()));

    public DisneyPlusAppleTVPasswordPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return secureTextEntryField.isPresent();
    }

    public ExtendedWebElement getHidePassword() {
        return hidePasswordDisneyAuth;
    }

    public ExtendedWebElement getShowPassword() {
        return showPasswordDisneyAuth;
    }

    public void clickPassword() {
        secureTextEntryField.click();
    }

    public String getPasswordFieldText() {
        return passwordEntryField.getText();
    }

    public boolean isPasswordFieldFocused() {
        return isFocused(secureTextEntryField);
    }

    public boolean isLogInBtnFocused() {
       return isFocused(getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
               MY_DISNEY_LOGIN_BTN.getText())));
    }

    public void enterPasswordToCompleteAuth(String password) {
        clickPassword();
        enterPasswordOnAuthPasswordScreen(password);
        moveToContinueOrDoneBtnKeyboardEntry();
        clickSelect();
        primaryButton.click();
    }

    public void enterPassword(String password) {
        typeTextView.type(password);
    }

    public void enterPasswordOnAuthPasswordScreen(String password) {
        createPasswordTextField.type(password);
    }

    public ExtendedWebElement getEnterYourPasswordHeader() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_PASSWORD_HEADER.getText()));
    }

    public boolean isEnterYourPasswordHeaderPresent() {
        return getEnterYourPasswordHeader().isPresent();
    }

    public boolean isEnterYourPasswordHintPresent() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_PASSWORD_HINT.getText())).isPresent();
    }

    public boolean isLearnMoreAboutMyDisneyButtonPresent() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_LEARN_MORE_BTN.getText())).isPresent();
    }

    public boolean isLoginNavigationButtonPresent() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).isPresent();
    }

    public boolean isShowHidePasswordEyeIconPresent() {
        return hideShowPasswordBtn.isPresent();
    }

    public void passwordEntry(String password) {
        clickPassword();
        enterPassword(password);
        moveToContinueOrDoneBtnKeyboardEntry();
        clickSelect();
    }

    public void logInWithPassword(String password) {
        passwordEntry(password);
        Assert.assertTrue(isOpened(), "Password entry page did not open");
        Assert.assertTrue(isFocused(getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText()))),
                "Login button is not focused");
        getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).click();
    }

    public void logInWithPasswordLocalized(String password) {
        clickPassword();
        enterPassword(password);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        clickSelect();
        isOpened();
        Assert.assertTrue(isFocused(getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText()))),
                "Login button is not focused");
        getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).click();
    }

    public boolean isContinueButtonPresent() {
        boolean isPresent = primaryButton.isElementPresent();
        return isPresent;
    }

    public void clickHavingTroubleLogginInBtn() {
        havingTroubleLogginInBtn.click();
    }

    public boolean isHavingTroubleLogginInBtnFocused() {
        return isFocused(havingTroubleLogginInBtn);
    }

    public void clickContinueBtn() {
        primaryButton.click();
    }

    public ExtendedWebElement getSecureTextEntryField() {
        return secureTextEntryField;
    }

    @Override
    public boolean isPasswordFieldDisplayed() {
        return passwordOnScreenField.isPresent();
    }

    public boolean isInvalidCredentialsDisplayed() {
        String invalidCredentialsError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());
        return getDynamicAccessibilityId(invalidCredentialsError).isPresent();
    }

    @Override
    public boolean isEnterYourPasswordBodyPresent(String accountEmail) {
        String enterYourPasswordBody = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_PASSWORD_BODY.getText()), Map.of("email", accountEmail, "link_1", ""));
        return getDynamicAccessibilityId(enterYourPasswordBody.trim()).isPresent();
    }

    public ExtendedWebElement getAuthEnterPasswordProfileBody() {
        return getTextViewByLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        AUTH_PASSWORD_ADD_PROFILE_BODY.getText()));
    }

    public String getAuthEnterPasswordFieldHintText() {
        return getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        AUTH_PASSWORD_ADD_PROFILE_FIELD_HINT.getText());
    }

    public ExtendedWebElement getAuthEnterPasswordForgotPassword() {
        return getStaticTextByLabel(getAppleTVLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        AUTH_PASSWORD_ADD_PROFILE_FORGOT_PASSWORD.getText()));
    }
}
