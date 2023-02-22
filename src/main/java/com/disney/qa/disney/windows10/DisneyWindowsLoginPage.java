package com.disney.qa.disney.windows10;

import com.disney.qa.common.utils.UniversalUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsLoginPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "TextBox")
    private ExtendedWebElement emailTextField;
    @ExtendedFindBy(accessibilityId = "EmailContinueButton")
    private ExtendedWebElement emailContinueButton;

    public DisneyWindowsLoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return emailContinueButton.isElementPresent();
    }

    public void enterEmail(String email) {
        emailTextField.isElementPresent();
        emailTextField.type(email);
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void clickContinueBtn() {
        emailContinueButton.isElementPresent();
        emailContinueButton.click();
    }
}
