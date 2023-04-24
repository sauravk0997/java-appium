package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
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
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void waitForWelcomePageToLoad() {
        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 2, "Welcome Screen was not loaded")
                .until(it -> loginButton.isElementPresent());
    }

    public boolean isSignUpFocused() {
        boolean isFocused = isFocused(signUpButton);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isFocused;
    }

    public boolean isLoginBtnFocused() {
        boolean isFocused = isFocused(loginButton);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isFocused;
    }

    @Override
    public void clickLogInButton() {
        loginButton.isElementPresent();
        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 2, "Sign Up button was not focused")
                .until(it -> isFocused(signUpButton));
        UniversalUtils.captureAndUpload(getCastedDriver());

        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 1, "Login Button was not focused")
                .until(it -> {
                    clickDown();
                    return isFocused(loginButton);
                });
        UniversalUtils.captureAndUpload(getCastedDriver());
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
