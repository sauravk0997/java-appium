package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusPasswordIOSPageBase.class)
public class DisneyPlusAppleTVPasswordPage extends DisneyPlusPasswordIOSPageBase {

    @ExtendedFindBy(accessibilityId = "loginButton")
    protected ExtendedWebElement logInButtonPasswordScreen;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Create a password\"`]")
    private ExtendedWebElement createPasswordTitle;
    @ExtendedFindBy(accessibilityId = "progressBar")
    private ExtendedWebElement passwordStrengthMeter;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"progressBar\"`]/XCUIElementTypeOther")
    private ExtendedWebElement passwordStrengthMeterColor;
    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    private ExtendedWebElement createPasswordTextField;
    @ExtendedFindBy(accessibilityId = "buttonSignUp")
    private ExtendedWebElement signUpBtn;
    @ExtendedFindBy(accessibilityId = "buttonForgotPassword")
    private ExtendedWebElement forgotPasswordBtn;
    @ExtendedFindBy(accessibilityId = "buttonShowHidePassword")
    private ExtendedWebElement hideShowPasswordBtn;
    @ExtendedFindBy(iosPredicate = "type == \"XCUIElementTypeTextView\"")
    protected ExtendedWebElement passwordOnScreenField;
    @ExtendedFindBy(accessibilityId = "hidePasswordDisneyAuth")
    private ExtendedWebElement hidePasswordDisneyAuth;
    @ExtendedFindBy(accessibilityId = "showPasswordDisneyAuth")
    private ExtendedWebElement showPasswordDisneyAuth;

    private ExtendedWebElement havingTroubleLogginInBtn = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_LOGIN_HELP.getText()));

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

    public int getPasswordStrengthMeterWidth() {
        return passwordStrengthMeterColor.getSize().getWidth();
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

    public boolean isForgotPasswordBtnFocused() {
        boolean isFocused = isFocused(forgotPasswordBtn);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }

    public boolean isLogInBtnFocused() {
       return isFocused(getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
               MY_DISNEY_LOGIN_BTN.getText())));
    }

    public boolean isSignUpBtnFocused() {
        boolean isFocused = isFocused(primaryButton);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }

    public void createNewPasswordEntry(String password) {
        clickPassword();
        enterPasswordCreatePassword(password);
        moveToContinueOrDoneBtnKeyboardEntry();
        clickSelect();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        primaryButton.click();
    }

    public void enterPassword(String password) {
        typeTextView.type(password);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void enterPasswordCreatePassword(String password) {
        createPasswordTextField.type(password);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void clickSignUp() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        primaryButton.click();
    }

    public boolean isCreatePasswordScreenOpen() {
        boolean isPresent = getStaticTextByLabelContains(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, MY_DISNEY_CREATE_PASSWORD_HEADER.getText())).isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isEnterYourPasswordHeaderPresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_PASSWORD_HEADER.getText())).isPresent();
    }

    public boolean isEnterYourPasswordHintPresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_PASSWORD_HINT.getText())).isPresent();
    }

    public boolean isLearnMoreAboutMyDisneyButtonPresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_LEARN_MORE_BTN.getText())).isPresent();
    }

    public boolean isLoginNavigationButtonPresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).isPresent();
    }

    public String getShowHidePasswordBtnState() {
        String text = hideShowPasswordBtn.getText();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return text;
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
        Assert.assertTrue(isFocused(getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText()))),
                "Login button is not focused");
        getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).click();
    }

    public void logInWithPasswordLocalized(String password) {
        clickPassword();
        enterPassword(password);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        clickSelect();
        isOpened();
        Assert.assertTrue(isFocused(getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText()))),
                "Login button is not focused");
        getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).click();
    }

    public void submitSandboxPassword(String password) {
        clickPassword();
        enterPassword(password);
    }

    public boolean isContinueBtnOnCreatePasswordPresent() {
        boolean isPresent = primaryButton.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isContinueButtonFocused() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused(primaryButton);
    }

    public void clickHavingTroubleLogginInBtn() {
        havingTroubleLogginInBtn.click();
    }

    public boolean isHavingTroubleLogginInBtnFocused() {
        return isFocused(havingTroubleLogginInBtn);
    }

    public boolean isCreateNewPasswordScreenOpen() {
        return staticTextByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_NEW_PASSWORD.getText())).isPresent();
    }

    public boolean isStrengthBarPresent() {
        return passwordStrengthMeter.isElementPresent();
    }

    public List<List<String>> getStrengthMeterVerificationLists() {
        var dictionaryList = new LinkedList<String>();
        String widthListName = getDevice().getName().contains("4K") ? "disney_password_meter_width_4K" : "disney_password_meter_width";
        Stream.of(MY_DISNEY_CREATE_PASSWORD_STRENGTH_FAIR, MY_DISNEY_CREATE_PASSWORD_STRENGTH_FAIR, MY_DISNEY_CREATE_PASSWORD_STRENGTH_FAIR, MY_DISNEY_CREATE_PASSWORD_STRENGTH_GOOD, MY_DISNEY_CREATE_PASSWORD_STRENGTH_GREAT, MY_DISNEY_CREATE_PASSWORD_STRENGTH_GREAT)
                .collect(Collectors.toList()).forEach(item -> dictionaryList.add(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, item.getText())));
        return Stream.of(dictionaryList,
                        Stream.of(R.TESTDATA.get("disney_password_list").split(",")).collect(Collectors.toList()),
                        Stream.of(R.TESTDATA.get(widthListName).split(",")).collect(Collectors.toList()),
                        Stream.of(R.TESTDATA.get("disney_password_meter_colors").split(",")).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public void clickHideShowPasswordBtn() {
        hideShowPasswordBtn.click();
    }

    public String getPasswordHintText() {
        return passwordHintText.getText();
    }

    public void clickContinueBtn() {
        primaryButton.click();
    }

    public void enterNewPassword(String text) {
        getDynamicAccessibilityId(getDictionary()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_NEW_PASSWORD.getText()))
                .type(text);
    }

    /**
     * This method is used when user has already entered a password and wants to replace the existing password
     *
     * @param currentPassword - currently entered password
     * @param newPassword     - password to be entered
     */
    public void clearAndEnterNewPassword(String currentPassword, String newPassword) {
        try {
            staticTypeTextViewValueDoubleQuotes.format(currentPassword).getElement().clear();
        } catch (StaleElementReferenceException e) {
            enterNewPassword(newPassword);
        }
    }

    public void waitForPasswordPageToDisappear() {
        fluentWait(getDriver(), FIVE_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Element is not present").until(it -> getSecureTextEntryField().isElementNotPresent(ONE_SEC_TIMEOUT));
    }

    public ExtendedWebElement getSecureTextEntryField() {
        return secureTextEntryField;
    }

    @Override
    public boolean isPasswordFieldDisplayed() {
        return passwordOnScreenField.isPresent();
    }

    public boolean isInvalidCredentialsDisplayed() {
        String invalidCredentialsError = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());
        return getDynamicAccessibilityId(invalidCredentialsError).isPresent();
    }
}
