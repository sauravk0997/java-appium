package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.Configuration;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.LOGIN_BTN;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.WELCOME_SUB_TEXT;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusWelcomeScreenIOSPageBase.class)
public class DisneyPlusAppleTVWelcomeScreenPage extends DisneyPlusWelcomeScreenIOSPageBase {

    public DisneyPlusAppleTVWelcomeScreenPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = loginButton.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void waitForWelcomePageToLoad() {
        fluentWait(getDriver(), Configuration.getLong(Configuration.Parameter.EXPLICIT_TIMEOUT), 2, "Welcome Screen was not loaded")
                .until(it -> loginButton.isElementPresent());
    }

    public boolean isSignUpFocused() {
        boolean isFocused = isFocused(signUpButton);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }

    public boolean isLoginBtnFocused() {
        boolean isFocused = isFocused(loginButton);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }

    @Override
    public void clickLogInButton() {
        loginButton.isElementPresent();
        fluentWait(getDriver(), Configuration.getLong(Configuration.Parameter.EXPLICIT_TIMEOUT), 2, "Sign Up button was not focused")
                //TODO: TVOS-3456 focus not found on sign up button, temp fix use of isElementPresent
                .until(it -> signUpButton.isElementPresent());
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        fluentWait(getDriver(), Configuration.getLong(Configuration.Parameter.EXPLICIT_TIMEOUT), 1, "Login Button was not focused")
                .until(it -> {
                    clickDown();
                    return isFocused(loginButton);
                });
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        clickSelect();
    }

    @Override
    public void clickSignUpButton() {
        signUpButton.click();
    }

    public ExtendedWebElement getSignUpButton() {
        return xpathNameOrName.format(getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL,
                                DictionaryKeys.SIGN_UP_BTN.getText()),
                DictionaryKeys.SIGN_UP_BTN.getText());
    }

    public ExtendedWebElement getLoginButton() {
        return xpathNameOrName.format(getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL,
                                LOGIN_BTN.getText()),
                DictionaryKeys.LOGIN_BTN.getText());
    }

    public boolean isWelcomeSubTextPresent() {
        String subTextLabel = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, WELCOME_SUB_TEXT.getText()),
                Map.of("PRICE_0", "---", "TIME_UNIT_0", "---", "PRICE_1", "---", "TIME_UNIT_1", "---"));
        return getDynamicAccessibilityId(subTextLabel).isElementPresent();
    }
}
