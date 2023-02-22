package com.disney.qa.disney.windows10;

import com.disney.qa.common.utils.UniversalUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsPasswordPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "PasswordBox")
    private ExtendedWebElement passwordField;
    @ExtendedFindBy(accessibilityId = "PasswordRevealToggleButton")
    private ExtendedWebElement passwordShowButton;
    @ExtendedFindBy(accessibilityId = "PasswordContinueButton")
    private ExtendedWebElement loginButton;
    @ExtendedFindBy(accessibilityId = "ForgotPasswordButton")
    private ExtendedWebElement forgotPasswordButton;

    public DisneyWindowsPasswordPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return loginButton.isElementPresent();
    }

    public void enterPassword(String password) {
        passwordField.isElementPresent();
        passwordField.type(password);
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void clickLoginBtn() {
        loginButton.isElementPresent();
        loginButton.click();
        UniversalUtils.captureAndUpload(getCastedDriver());
    }
}
