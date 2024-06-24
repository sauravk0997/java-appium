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

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

/*
 * Enter Password Page
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPasswordIOSPageBase extends DisneyPlusApplePageBase {

    private static final String LOGIN_BUTTON = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN.getText());
    private ExtendedWebElement forgotPasswordLink = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, FORGOT_PASSWORD.getText()));
    private ExtendedWebElement r21ForgotPasswordLink = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, RATING_R21_FORGOT_PASSSWORD_LINK.getText()));

    @FindBy(xpath = "//XCUIElementTypeButton[@name='buttonBack']/../following-sibling::*/*/XCUIElementTypeImage")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(accessibilityId = "passwordStrengthHeader")
    protected ExtendedWebElement passwordHintText;

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
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isPasswordPagePresent() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_PASSWORD_HEADER.getText())).isElementPresent();
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
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent && showHidePasswordIndicator.getAttribute("label").equals("hidePasswordDisneyAuth");
    }

    public boolean isHidePasswordIconDisplayed() {
        boolean isPresent = showHidePasswordIndicator.isPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent && showHidePasswordIndicator.getAttribute("label").equals("hide");
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
        r21ForgotPasswordLink.click();
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
}
