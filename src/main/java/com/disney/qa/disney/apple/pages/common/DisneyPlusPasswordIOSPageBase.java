package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.Map;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

/*
 * Enter Password Page
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPasswordIOSPageBase extends DisneyPlusApplePageBase {

    private static final String LOGIN_BUTTON = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN.getText());
    private ExtendedWebElement forgotPasswordLink = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, FORGOT_PASSWORD.getText()));

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeSecureTextField'$]/XCUIElementTypeOther/XCUIElementTypeImage")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeSecureTextField'$]/XCUIElementTypeOther/**/XCUIElementTypeImage[1]")
    private ExtendedWebElement myDisneyLogo;

    @ExtendedFindBy(accessibilityId = "passwordStrengthHeader")
    protected ExtendedWebElement passwordHintText;

    @ExtendedFindBy(accessibilityId = "CONTINUE")
    protected ExtendedWebElement continueButton;

    @ExtendedFindBy(accessibilityId = "labelErrorMessage")
    protected ExtendedWebElement labelError;

    private static final String havingTroubleLoggingText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
            MY_DISNEY_ENTER_YOUR_PASSWORD_OTP_BTN.getText());

    public DisneyPlusPasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = headlineHeader.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isPasswordPagePresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_PASSWORD_HEADER.getText())).isElementPresent();
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

    public boolean isPasswordEntryFieldDisplayed() {
        return passwordFieldHint.isPresent();
    }

    public boolean isShowPasswordIconDisplayed() {
        boolean isPresent = showHidePasswordIndicator.isPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent && showHidePasswordIndicator.getAttribute("label").equals("hidePasswordDisneyAuth");
    }

    public boolean isPasswordHintTextDisplayed() {
        return passwordHintText.isPresent();
    }

    public boolean isLoginButtonDisplayed() {
        return getLoginButton().isPresent();
    }

    public boolean isForgotPasswordLinkDisplayed() {
        return forgotPasswordLink.isPresent();
    }

    public void clickForgotPasswordLink() {
        forgotPasswordLink.click();
    }

    public void clickR21ForgotPasswordLink() {
        forgotPasswordBtn.click();
    }

    public void typePassword(String password) {
        passwordFieldHint.type(password);
    }

    public void enterLogInPassword(String password) {
      secureTextEntryField.type(password);
    }

    public ExtendedWebElement getLoginButton() {
        return getDynamicAccessibilityId(LOGIN_BUTTON);
    }

    public void submitPasswordForLogin(String userPassword) {
        //To hide the keyboard, passing \n at the end of password value
        enterLogInPassword(userPassword + "\n");
        Assert.assertTrue(waitUntil(ExpectedConditions.invisibilityOfElementLocated(getLoginButton().getBy()), DEFAULT_EXPLICIT_TIMEOUT),
                "Login button is visible after entering password.");
    }

    public void submitPasswordWhileLoggedIn(String userPassword) {
        typePassword(userPassword);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        if (continueButton.isElementPresent()) {
            continueButton.click();
        } else {
            clickNearElement(getTypeButtonByLabel("CONTINUE"), 0.5, 30);
        }
    }

    public String getPasswordText() {
        return getStaticTextByLabelContains(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_PASSWORD_HINT.getText()))
                .getText();
    }

    public String getHidePasswordText() {
        return secureTextEntryField.getText();
    }

    public String getShowPasswordText(){
        return textEntryField.getText();
    }

    public void clickShowPasswordIcon() {
        showHidePasswordIndicator.click();
    }

    public void clickHidePasswordIcon() {
        showPasswordIndicator.click();
    }

    public String getErrorMessageString() {
        return labelError.getText();
    }

    public boolean isAuthPasswordKidsProfileBodyDisplayed() {
        return textViewByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.AUTH_PASSWORD_KIDS_PROFILE_OFF_BODY.getText())).isPresent();
    }

    public boolean isMyDisneyLogoDisplayed() {
        return myDisneyLogo.isPresent();
    }

    public boolean isStep2LabelDisplayed() {
        String step2Label = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_STEPPER_TEXT.getText()), Map.of("current_step", "2"));
        return getStaticTextByLabel(step2Label).isPresent();
    }

    public boolean isEnterYourPasswordBodyPresent(String accountEmail) {
        String enterYourPasswordBody = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_PASSWORD_BODY.getText()), Map.of("email", accountEmail, "link_1", "(edit)"));
        return getDynamicAccessibilityId(enterYourPasswordBody).isPresent();
    }

    public boolean isCaseSensitiveHintPresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_ENTER_YOUR_PASSWORD_HINT2.getText())).isPresent();
    }

    public boolean isForgotPasswordButtonPresent() {
        return getDynamicAccessibilityId(havingTroubleLoggingText).isPresent();
    }

    public void clickHavingTroubleLoggingButton() {
        dynamicBtnFindByLabel.format(havingTroubleLoggingText).click();
    }
}
