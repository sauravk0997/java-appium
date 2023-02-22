package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class DisneyPlusLoginPageBase extends DisneyPlusCommonPageBase{

    @FindBy(id = "backButton")
    private ExtendedWebElement backArrow;

    @FindBy(id = "continueLoadingButton")
    protected ExtendedWebElement continueLoadingButton;

    @FindBy(id = "dateOfBirthContainer")
    protected ExtendedWebElement dateOfBirthContainer;

    @FindBy(id = "loginEmailTitle")
    private ExtendedWebElement emailTitle;

    @FindBy(id = "forgotPasswordButton")
    protected ExtendedWebElement forgotPasswordButton;

    @FindBy(id = "loginEmailContainer")
    private ExtendedWebElement loginPageContainer;

    @FindBy(id = "titleTextView")
    private ExtendedWebElement loginPageTitleTextView;

    @FindBy(id = "inputErrorTextView")
    private ExtendedWebElement inputErrorTextView;

    @FindBy(id = "onboardingDisneyLogo")
    private ExtendedWebElement onboardingLogo;

    @FindBy(id = "optInCheckbox")
    private ExtendedWebElement optInCheckboxes;

    @FindBy(id = "passwordLayout")
    private ExtendedWebElement passwordScreen;

    @FindBy(id = "passwordTitle")
    private ExtendedWebElement passwordTitle;

    @FindBy(id = "standardButtonContainer")
    private ExtendedWebElement proceedButton;

    @FindBy(id = "inputShowPwdImageView")
    protected ExtendedWebElement showPasswordButton;

    @FindBy(id = "signUpButton")
    private ExtendedWebElement signUpButton;

    @FindBy(id = "signUpEmailLayout")
    private ExtendedWebElement signUpScreen;

    @FindBy(id = "positiveButton")
    private ExtendedWebElement legalPositiveBtn;

    @FindBy(id = "inputFieldConstraintLayout")
    private ExtendedWebElement passwordField;

    public DisneyPlusLoginPageBase(WebDriver driver) {
        super(driver);
    }

    public void clickAgreeAndContinueButton() {
        continueLoadingButton.click();
    }

    public void clickForgotPassword() {
        forgotPasswordButton.click();
    }

    public void clickSignupButton() {
        signUpButton.click();
    }

    public void clickLegalPositiveButton() {
        legalPositiveBtn.click();
    }

    public void dismissError() {
        confirmButton.click();
    }

    public DisneyPlusLoginPageBase enterNewEmail(String newEmail){
        editTextByClass.click();
        editTextByClass.type(newEmail);

        return initPage(DisneyPlusLoginPageBase.class);
    }

    public List<ExtendedWebElement> getCheckboxes() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(optInCheckboxes.getBy()), 30);
        return findExtendedWebElements(optInCheckboxes.getBy());
    }

    public String getEmailTitleText() {
        if (emailTitle.isElementPresent()) {
            return emailTitle.getText();
        } else {
            return "";
        }
    }

    public String getInputErrorText() {
        return getElementText(errorTextView);
    }

    public String getPasswordTitleText() {
        if (passwordTitle.isElementPresent()){
            return passwordTitle.getText();
        } else {
            return "";
        }
    }

    public boolean isBackButtonDisplayed() {
        return backArrow.isElementPresent();
    }

    public boolean isEmailErrorDisplayed() {
        return confirmButton.isElementPresent(DELAY);
    }

    public boolean isDOBContainerDisplayed() {
        return dateOfBirthContainer.isVisible();
    }

    public boolean isEmailTitleDisplayed() {
        return emailTitle.isElementPresent();
    }

    public boolean isForgotPasswordDisplayed() {
        return forgotPasswordButton.isElementPresent();
    }

    public boolean isInputErrorVisible() {
        return errorTextView.isVisible();
    }

    public boolean isLogoDisplayed() {
        return onboardingLogo.isElementPresent();
    }

    public boolean isInputErrorTextViewDisplayed() { return inputErrorTextView.isElementPresent(); }

    @Override
    public boolean isOpened() {
        return loginPageContainer.isElementPresent() && editTextByClass.isElementPresent();
    }

    public boolean isProceedButtonDisplayed() {
        return proceedButton.isElementPresent();
    }

    public boolean isProceedButtonDisplayed(String text) {
        return proceedButton.isElementPresent() && proceedButton.getText().equals(text);
    }

    public boolean isProfileSelectDisplayed() {
        return loginPageTitleTextView.isElementPresent();
    }

    public boolean isShowPasswordButtonDisplayed() {
        return showPasswordButton.isElementPresent();
    }

    public boolean isSignUpButtonDisplayed() {
        return signUpButton.isElementPresent();
    }

    public boolean isSignUpScreenDisplayed() {
        return signUpScreen.isElementPresent();
    }

    public boolean isPasswordScreenOpened() {
        return passwordScreen.isElementPresent();
    }

    public void waitForPasswordFieldToBeDisplayed() {
        fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, "Password field is not displayed").until(it -> passwordField.isElementPresent(ONE_SEC_TIMEOUT));
    }

    public DisneyPlusDiscoverPageBase logInWithPassword(String password) {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(editTextByClass.getBy()), DELAY);
        waitForLoading();
        editTextByClass.click();
        editTextByClass.type(password);
        new AndroidUtilsExtended().hideKeyboard();
        proceedButton.click();

        return initPage(DisneyPlusDiscoverPageBase.class);
    }

    public void pressBackCarrotBtn() {
        backArrow.click();
    }

    public DisneyPlusLoginPageBase proceedToPasswordMode(String email) {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(editTextByClass.getBy()), DELAY);
        editTextByClass.click();
        editTextByClass.type(email);
        new AndroidUtilsExtended().hideKeyboard();
        proceedButton.click();
        waitForLoading();
        return initPage(DisneyPlusLoginPageBase.class);
    }

    public DisneyPlusLoginPageBase registerNewEmail(String email) {
        waitForLoading();
        editTextByClass.click();
        editTextByClass.type(email);
        new AndroidUtilsExtended().pressBack();
        if (getCheckboxes().size() > 1) {
            getCheckboxes().forEach(ExtendedWebElement::click);
        }
        continueLoadingButton.click();

        return initPage(DisneyPlusLoginPageBase.class);
    }

    public DisneyPlusPaywallPageBase registerPassword() {
        return registerPassword(cryptoTool.decrypt(R.TESTDATA.get("disney_qa_web_d23password")));
    }

    public DisneyPlusPaywallPageBase registerPassword(String value) {
        waitForLoading();
        editTextByClass.click();
        editTextByClass.type(value);
        new AndroidUtilsExtended().pressBack();
        continueLoadingButton.click();

        return initPage(DisneyPlusPaywallPageBase.class);
    }
}
