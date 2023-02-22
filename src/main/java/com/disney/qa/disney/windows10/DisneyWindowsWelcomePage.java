package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsWelcomePage extends DisneyWindowsCommonPage {

    @ExtendedFindBy(accessibilityId = "SignUpButton")
    private ExtendedWebElement signUpNowBtn;
    @ExtendedFindBy(accessibilityId = "LogInButton")
    private ExtendedWebElement loginBtn;

    public DisneyWindowsWelcomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return signUpNowBtn.isElementPresent();
    }

    public void clickLogin() {
        loginBtn.isElementPresent();
        loginBtn.click();
    }
}