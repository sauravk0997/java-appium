package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusChangeEmailIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusChangeEmailIOSPageBase.class)
public class DisneyPlusAppleTVChangeEmailPage extends DisneyPlusChangeEmailIOSPageBase {
    public DisneyPlusAppleTVChangeEmailPage(WebDriver driver) {super(driver);}

    @ExtendedFindBy(accessibilityId = "textFieldEmail")
    private ExtendedWebElement emailTextField;

    @Override
    public boolean isOpened() {
        boolean isPresent = emailTextField.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void clickEmailField() { emailTextField.click(); }

    public void enterEmail(String email) {
        emailTextField.type(email);
    }

    public void clickSave() {
        primaryButton.click();
    }
}
