package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsAccountPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "ChangeEmailButton")
    private ExtendedWebElement email;
    @ExtendedFindBy(accessibilityId = "ChangePasswordButton")
    private ExtendedWebElement password;
    @FindBy(name = "Log out of all devices")
    private ExtendedWebElement logOutAllDevices;
    @FindBy(name = "Account")
    private ExtendedWebElement accountTitle;

    public DisneyWindowsAccountPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return email.isElementPresent();
    }
}
