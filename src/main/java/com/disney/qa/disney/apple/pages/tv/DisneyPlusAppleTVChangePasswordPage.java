package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusChangePasswordIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusChangePasswordIOSPageBase.class)
public class DisneyPlusAppleTVChangePasswordPage extends DisneyPlusChangePasswordIOSPageBase {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public DisneyPlusAppleTVChangePasswordPage(WebDriver driver) {super(driver);}

    @ExtendedFindBy(accessibilityId = "secureTextFieldPassword")
    private ExtendedWebElement passwordTextField;

    @Override
    public boolean isOpened() {
        boolean isPresent = passwordTextField.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void clickPasswordField() { passwordTextField.click(); }

    public void enterPassword(String password) {
        passwordTextField.type(password);
    }

    public void clickSave() {
        LOGGER.info("Print out:" + getDriver().getPageSource());
        waitForPresenceOfAnElement(primaryButton);
        primaryButton.click();
    }
}
