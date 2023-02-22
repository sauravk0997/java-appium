package com.disney.qa.disney.web.appex.userflows;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLoginPage extends DisneyPlusBasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusLoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "logo")
    private ExtendedWebElement disneyLogoId;

    @FindBy(xpath = "//*[contains(text(),'Log in with your email')]")
    private ExtendedWebElement loginWithEmailText;

    @FindBy(id = "email")
    private ExtendedWebElement emailInput;

    @FindBy(xpath = "//*[@data-testid='login-continue-button']")
    private ExtendedWebElement loginContinueButton;

    @FindBy(xpath = "//*[@data-testid='signup-continue-button']")
    private ExtendedWebElement signupContinueButton;

    @FindBy(xpath = "//p[contains(text(),'New to Disney')]")
    private ExtendedWebElement newToDisneyParagraph;

    @FindBy(xpath = "//*[@data-testid='login-page-signup-cta']")
    private ExtendedWebElement signUpCtaLink;

    @FindBy(xpath = "//*[@data-testid='password-continue-login']")
    private ExtendedWebElement dPlusBasePasswordContinueLoginBtn;

    @FindBy(xpath = "//div[@id='email__error' and contains(text(),'Unknown email')]")
    private ExtendedWebElement unknownEmailError;

    @FindBy(xpath = "//div[@id='email__error' and contains(text(),'Error Code 6')]")
    private ExtendedWebElement errorCodeSix;

    @FindBy(xpath = "//h4[contains(@class,'text')]")
    private ExtendedWebElement accountNotFoundMessage;

    @FindBy(xpath = "//button[@data-testid='modal-primary-button']")
    private ExtendedWebElement tryAgainButton;

    @FindBy(xpath = "//*[contains(@class,'icon--checkmark-white')]")
    private ExtendedWebElement marketingOptInCheckbox;

    public boolean verifyPage() {
        LOGGER.info("Verify Login page loaded");
        return loginWithEmailText.isVisible();
    }

    public boolean isMarketingOptInVisible() {
        LOGGER.info("Is marketing opt-in checkbox visible");
        return marketingOptInCheckbox.isVisible();
    }

    public boolean isDisneyLogoIdPresent() {
        LOGGER.info("Verify Disney Logo is present");
        return disneyLogoId.isElementPresent();
    }

    public boolean isDplusLoginEmailTextPresent() {
        LOGGER.info("Verify Log in with your email is present");
        return loginWithEmailText.isElementPresent();
    }

    public boolean isDplusNewToDisneyParagraphPresent() {
        LOGGER.info("Verify New to Disney+ is present");
        return newToDisneyParagraph.isElementPresent();
    }

    public boolean isDplusSignUpLinkPresent() {
        LOGGER.info("Verify sign up link is present");
        return signUpCtaLink.isElementPresent();
    }

    public boolean isEmailGhostFillPresent() {
        LOGGER.info("Checking if ghost fill is present on the email text box");
        return getDplusBaseEmailAttribute("value").isEmpty()
                && getDplusBaseEmailAttribute("placeholder").equals("Email");
    }

    public boolean isLoginButtonPresent() {
        LOGGER.info("Verify Disney Login Button is present");
        waitFor(dPlusBasePasswordContinueLoginBtn);
        return dPlusBasePasswordContinueLoginBtn.isElementPresent();
    }

    public boolean isEmailNotFoundMessagePresent() {
        LOGGER.info("Verify couldn't find an account is present");
        return accountNotFoundMessage.isElementPresent();
    }

    public boolean isUnknownEmailErrorPresent() {
        LOGGER.info("Verify unknown error message is present");
        return unknownEmailError.isElementPresent();
    }

    public boolean isErrorCodeSixPresent() {
        LOGGER.info("Verify error code six is present");
        return errorCodeSix.isElementPresent();
    }

    public String getDplusBaseEmailAttribute(String attribute) {
        LOGGER.info("Getting the attribute: {} of the email field", attribute);
        return getDplusBaseEmailFieldId().getAttribute(attribute);
    }

    public DisneyPlusLoginPage clickTryAgainButton() {
        LOGGER.info("Click try again button");
        tryAgainButton.click();
        return this;
    }

    public DisneyPlusLoginPage clickDPlusBaseLoginButton() {
        LOGGER.info("Click Login Button");
        getDplusBaseLoginBtn().click();
        waitFor(loginContinueButton, 2);
        return this;
    }

    public DisneyPlusLoginPage clickSignUpLoginContinueButton() {
        LOGGER.info("Click Continue Button");
        getSignUpLoginContinueBtn().clickByJs();
        return this;
    }

    public DisneyPlusLoginPage enterEmail(String email) {
        LOGGER.info("Typing the email: {} in the email field", email);
        isDplusBaseEmailFieldIdPresent();
        analyticPause();
        emailInput.type(email);
        return this;
    }

    public DisneyPlusLoginPage clickLoginContinueButton() {
        LOGGER.info("Click login continue button");
        loginContinueButton.click();
        return this;
    }

    public DisneyPlusLoginPage clickSignupContinueButton() {
        LOGGER.info("Click signup continue button");
        signupContinueButton.click();
        return this;
    }

    public DisneyPlusLoginPage clickLoginSubmitButton() {
        LOGGER.info("Click login continue button");
        dPlusBasePasswordContinueLoginBtn.click();
        return this;
    }

    public void signUpWithCreds(ThreadLocal<DisneyAccount> disneyAccount) {
        String email = disneyAccount.get().getEmail();
        String password = disneyAccount.get().getUserPass();
        LOGGER.info("Signing up with valid credentials");
        typeDplusBaseEmailFieldId(email);
        clickSignUpLoginContinueButton();
        typeDplusBasePasswordFieldId(password);
        clickLoginSubmitButton();
        disneyAccount.set(setAndGetUIGeneratedAccount(email, password));
    }

    public void loginWithCreds(String email, String password) {
        LOGGER.info("Login with valid credentials");
        typeDplusBaseEmailFieldId(email);
        clickLoginContinueButton();
        typeDplusBasePasswordFieldId(password);
        clickLoginSubmitButton();
    }
}
