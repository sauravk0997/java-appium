package com.disney.qa.common.jarvis.apple;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.qaprosoft.carina.core.foundation.utils.appletv.IRemoteControllerAppleTV;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class JarvisAppleTV extends JarvisAppleBase {

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name=\"%s\"]/ancestor::XCUIElementTypeCell")
    private ExtendedWebElement config;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"SAVE\"`]")
    private ExtendedWebElement saveButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"done\"`]")
    private ExtendedWebElement doneButton;

    public JarvisAppleTV(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isOpened = jarvisHeader.isElementPresent();
        UniversalUtils.captureAndUpload(getDriver());
        return isOpened;
    }

    public void clickItemWhileMovingDown(ExtendedWebElement element) {
        DisneyPlusApplePageBase.fluentWait(
                getDriver(),
                (long) LONG_TIMEOUT * 5, 0,
                "Unable to find Config " + element.getBy())
                .until(it -> {
                    if (element.isVisible(ONE_SEC_TIMEOUT)) {
                        return true;
                    }
                    moveDown(5, 0);
                    return false;
                });
        UniversalUtils.captureAndUpload(getDriver());
        element.click();
    }

    public void clickConfig(String appConfig) {
        clickItemWhileMovingDown(config.format(appConfig));
    }

    public void focusDoneButtonKeyboardEntry() {
        boolean isClearBtnPresent = keyboardClear.isElementPresent(SHORT_TIMEOUT);
        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 0, "Unable to focus continue button on email Entry")
                .until(it -> {
                    if (isClearBtnPresent) {
                        clickRight();
                    } else {
                        clickDown();
                    }
                    return isFocused(doneButton);
                });
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void setOverride(String override) {
        textEntry.isElementPresent();
        fluentWait(getDriver(), DELAY, 0, "Text field is not focused")
                .until(it -> isFocused(textEntry));
        clickSelect();
        textEntry.type(override);
        focusDoneButtonKeyboardEntry();
        clickSelect();
        UniversalUtils.captureAndUpload(getDriver());
        saveButton.click();
        keyPressTimes(IRemoteControllerAppleTV::clickMenu, 1, 1);
    }

    public void setDictionaryKey(String version) {
        Arrays.stream(JarvisAppleTV.DictionaryResourceKeys.values()).forEach(item -> {
            clickSelect();
            textEntry.type(version);
            focusDoneButtonKeyboardEntry();
            keyPressTimes(IRemoteControllerAppleTV::clickSelect, 1, 1);
            isAIDElementPresentWithScreenshot(item.getText());
            clickDown();
        });

    }

    public enum Configs {
        APP_CONFIG("App Config"),
        DICTIONARY_DEBUG_MODE("Dictionary Debug Mode"),
        DICTIONARY_VERSIONS("Dictionary Versions"),
        EDIT_CONFIG("Edit Config"),
        GLOBALIZATION_VERSION("globalizationVersion"),
        LOCALIZATION("localization"),
        RESOURCE_KEY("resourceKey");

        String key;

        Configs(String key) {
            this.key = key;
        }

        public String getText() {
            return key;
        }
    }

    public enum DictionaryResourceKeys {
        ACCESSIBILITY("accessibility"),
        APPLICATION("application"),
        DECORATIONS("decorations"),
        IDENTITY("identity"),
        MEDIA("media"),
        PAYWALL("paywall"),
        PCON("pcon"),
        RATINGS("ratings"),
        SDK_ERRORS("sdk-errors"),
        SUBSCRIPTIONS("subscriptions"),
        WELCH("welch");

        String key;

        DictionaryResourceKeys(String key) {
            this.key = key;
        }

        public String getText() {
            return key;
        }
    }
}