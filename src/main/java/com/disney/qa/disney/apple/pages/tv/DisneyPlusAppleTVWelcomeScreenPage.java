package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.zebrunner.carina.utils.Configuration;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.WELCOME_SUB_TEXT;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusWelcomeScreenIOSPageBase.class)
public class DisneyPlusAppleTVWelcomeScreenPage extends DisneyPlusWelcomeScreenIOSPageBase {

    @ExtendedFindBy(accessibilityId = "Sign Up Now")
    protected ExtendedWebElement signUpButton;

    @ExtendedFindBy(accessibilityId = "Login")
    protected ExtendedWebElement loginButton;

    public DisneyPlusAppleTVWelcomeScreenPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        System.out.println(getDriver().getPageSource());
        System.out.println(signUpButton.isPresent());
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return signUpButton.isPresent();
    }

    public void waitForWelcomePageToLoad() {
        fluentWait(getDriver(), Configuration.getLong(Configuration.Parameter.EXPLICIT_TIMEOUT), 2, "Welcome Screen was not loaded")
                .until(it -> loginButton.isElementPresent());
    }

    public boolean isSignUpFocused() {
        return isFocused(signUpButton);
    }

    public boolean isLoginBtnFocused() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused(loginButton);
    }

    @Override
    public void clickLogInButton() {
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
        return signUpButton;
    }

    public ExtendedWebElement getLoginButton() {
        return loginButton;
    }

    public boolean isWelcomeSubTextPresent() {
        String subTextLabel = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, WELCOME_SUB_TEXT.getText()),
                Map.of("PRICE_0", "---", "TIME_UNIT_0", "---", "PRICE_1", "---", "TIME_UNIT_1", "---"));
        return getDynamicAccessibilityId(subTextLabel).isPresent();
    }
}
