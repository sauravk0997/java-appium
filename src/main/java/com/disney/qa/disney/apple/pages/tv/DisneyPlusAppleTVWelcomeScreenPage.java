package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusWelcomeScreenIOSPageBase.class)
public class DisneyPlusAppleTVWelcomeScreenPage extends DisneyPlusWelcomeScreenIOSPageBase {

    public DisneyPlusAppleTVWelcomeScreenPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return getLoginButton().isPresent();
    }

    public ExtendedWebElement getLoginButton() {
        return loginButton;
    }

    public void waitForWelcomePageToLoad() {
        fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 2, "Welcome Screen was not loaded")
                .until(it -> getLoginButton().isPresent());
    }

    public boolean isLoginBtnFocused() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused(getLoginButton());
    }

    @Override
    public void clickLogInButton() {
        fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 1, "Login Button was not focused")
                .until(it -> isFocused(getLoginButton()));
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        clickSelect();
    }
}
