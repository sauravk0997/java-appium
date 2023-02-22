package com.disney.qa.common.jarvis.apple;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class JarvisAppleBase extends DisneyPlusApplePageBase {

    protected IOSUtils util = new IOSUtils();

    public static final String JARVIS_BUNDLE = "com.bamtech.jarvis";
    public static final String JARVIS_ENTERPRISE_BUNDLE = "com.bamtech.jarvis.enterprise";
    public static final String JARVIS_IAP_BUNDLE = "com.disney.disneyplus.jarvis";
    private static final String VERSION = "version";
    private static final String SAVE = "SAVE";
    public static final String JARVIS = "jarvis";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == '%s'`]")
    protected ExtendedWebElement button;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"DISNEY+\" or label == \"STAR+\" or label == \"HULU\"`]")
    protected ExtendedWebElement placeholderApp;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == '%s'`]")
    protected ExtendedWebElement textItem;

    @ExtendedFindBy(iosPredicate = "type == 'XCUIElementTypeTextField'")
    protected ExtendedWebElement textEntry;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeNavigationBar[`name == \"Select App\"`]")
    protected ExtendedWebElement jarvisHeader;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeNavigationBar[`name == \"Disney+\"`]")
    protected ExtendedWebElement jarvisDisneyHeader;

    @FindBy(xpath = "//*[@label='Dictionary Debug Mode']/../following-sibling::*/*")
    protected ExtendedWebElement dictionaryDebugModeValue;

    private static Map<String, Object> overridesToChange = new HashMap<>();

    private static final Map<String, String> appNames = Map.of(
            "tvOSDisney", "Disney+",
            "iOSDisney", "DISNEY+"
    );

    public enum AppName {
        TVOS_DISNEY("tvOSDisney"),
        IOS_DISNEY("iOSDisney");

        String name;

        AppName(String appName) {
            this.name = appName;
        }

        public String getText() {
            return this.name;
        }
    }

    protected JarvisAppleBase(WebDriver driver) {
        super(driver);
    }

    public void selectApp(AppName appName) {
        button.format(appNames.get(appName.getText())).click();
    }

    public boolean isAppPresent(AppName appName) {
        return button.format(appNames.get(appName.getText())).isElementPresent();
    }

    public void clickPlaceholderJarvisApp() {
        placeholderApp.click();
    }

    public void clickBackButton() {
        button.format("Back").click();
    }

    public void openAppConfigOverrides() {
        scrollToItem("App Config").click();
        textItem.format("Edit Config").click();
    }

    public void activateOverrides() {
        new TreeSet<>(overridesToChange.keySet()).forEach(item -> {
            scrollToItem(item).click();
            if (textItem.format(VERSION).isElementPresent(SHORT_TIMEOUT)) {
                textItem.format(VERSION).click();
                textEntry.type(overridesToChange.get(item).toString());
                button.format(SAVE).click();
                navigateBack();
                pause(1);
            } else {
                textEntry.type(overridesToChange.get(item).toString());
                button.format(SAVE).click();
            }
            navigateBack();
        });
        clearOverrides();
    }

    public void openOverrideSection(String override) {
        scrollToItem(override).click();
    }

    public void resetApp() {
        scrollToItem("Reset App").click();
        new IOSUtils().acceptAlert();
    }

    public Map<String, Object> getOverrides() {
        return overridesToChange;
    }

    public void clearOverrides() {
        overridesToChange.clear();
    }

    public ExtendedWebElement scrollToItem(String item) {
        ExtendedWebElement override = textItem.format(item);
        new IOSUtils().swipe(override);
        return override;
    }

    private interface Operation {
        void setOverrideValue(Object value);
    }

    public enum Config_Localization implements Operation {
        ACCESSIBILITY {
            public void setOverrideValue(Object value) {
                overridesToChange.put("a11y", value.toString());
            }
        },
        APPLICATION {
            public void setOverrideValue(Object value) {
                overridesToChange.put("app", value.toString());
            }
        },
        DECORATIONS {
            public void setOverrideValue(Object value) {
                overridesToChange.put("decorations", value.toString());
            }
        },
        IDENTITY {
            public void setOverrideValue(Object value) {
                overridesToChange.put("identity", value.toString());
            }
        },
        MEDIA {
            public void setOverrideValue(Object value) {
                overridesToChange.put("media", value.toString());
            }
        },
        PAYWALL {
            public void setOverrideValue(Object value) {
                overridesToChange.put("paywall", value.toString());
            }
        },
        PCON {
            public void setOverrideValue(Object value) {
                overridesToChange.put("pcon", value.toString());
            }
        },
        PLATFORM {
            public void setOverrideValue(Object value) {
                overridesToChange.put("platform", value.toString());
            }
        },
        RATINGS {
            public void setOverrideValue(Object value) {
                overridesToChange.put("ratings", value.toString());
            }
        },
        SDK_ERRORS {
            public void setOverrideValue(Object value) {
                overridesToChange.put("sdk-errors", value.toString());
            }
        },
        SUBSCRIPTIONS {
            public void setOverrideValue(Object value) {
                overridesToChange.put("subscriptions", value.toString());
            }
        },
        WELCH {
            public void setOverrideValue(Object value) {
                overridesToChange.put("welch", value.toString());
            }
        };

        public static final String SECTION = "localization";
    }

    public enum Config_GlobalizationVersion implements Operation {
        GLOBALIZATION_VERSION {
            public void setOverrideValue(Object value) {
                overridesToChange.put("globalizationVersion", value.toString());
            }
        }
    }


    public void returnToDisneyMenu() {
        int maxPresses = 0;
        while(!jarvisDisneyHeader.isElementPresent(1) && maxPresses < 4) {
            maxPresses++;
            navigateBack();
        }
    }

    public String getDebugDisplayOverrideValue() {
        returnToDisneyMenu();
        new IOSUtils().swipePageTillElementTappable(dictionaryDebugModeValue, 3, null, IMobileUtils.Direction.UP, 1000);
        return dictionaryDebugModeValue.getText();
    }

    public void clickDebugDisplayOverride() {
        dictionaryDebugModeValue.click();
    }
}
