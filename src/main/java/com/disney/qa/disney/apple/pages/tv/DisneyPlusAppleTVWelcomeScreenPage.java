package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.WELCOME_SUB_TEXT;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusWelcomeScreenIOSPageBase.class)
public class DisneyPlusAppleTVWelcomeScreenPage extends DisneyPlusWelcomeScreenIOSPageBase {

    public DisneyPlusAppleTVWelcomeScreenPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return getSignupButton().isPresent();
    }

    @Override
    public ExtendedWebElement getSignupButton() {
        return dynamicBtnFindByName.format("buttonSignUp");
    }

    public ExtendedWebElement getLoginButton() {
        return loginButton;
    }

    public void waitForWelcomePageToLoad() {
        fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 2, "Welcome Screen was not loaded")
                .until(it -> getLoginButton().isPresent());
    }

    public boolean isSignUpFocused() {
        return isFocused(getSignupButton());
    }

    public boolean isLoginBtnFocused() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused(getLoginButton());
    }

    @Override
    public void clickLogInButton() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 1, "Login Button was not focused")
                .until(it -> {
                    return isFocused(loginButton);
                });
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        clickSelect();
    }

    @Override
    public void clickSignUpButton() {
        getSignupButton().click();
    }

    public boolean isWelcomeSubTextPresent() {
        String subTextLabel = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, WELCOME_SUB_TEXT.getText()),
                Map.of("PRICE_0", "---", "TIME_UNIT_0", "---", "PRICE_1", "---", "TIME_UNIT_1", "---"));
        return getDynamicAccessibilityId(subTextLabel).isPresent();
    }
}
