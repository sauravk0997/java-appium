package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusRestartSubscriptionIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusRestartSubscriptionIOSPageBase.class)
public class DisneyPlusAppleTVRestartSubscriptionPage extends DisneyPlusRestartSubscriptionIOSPageBase {

    @ExtendedFindBy(accessibilityId = "restoreButton")
    private ExtendedWebElement restartSubscriptionLocalizedButton;

    private String restartSubscription = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.RESTART_SUBSCRIPTION.getText());

    public DisneyPlusAppleTVRestartSubscriptionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = getDynamicAccessibilityId(restartSubscription).isPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public List<String> getRestartSubscriptionScreenDictionaryTexts() {
        return Stream.of(RESTART_SUB_COPY, RESTART_SUB_COPY_2, RESTARTSUBSCRIPTION_CTA, RESTARTSUBSCRIPTION_LOGOUT)
                .map(item -> {
                    if (item == RESTARTSUBSCRIPTION_CTA || item == RESTARTSUBSCRIPTION_LOGOUT) {
                        return getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, item.getText()).toUpperCase();
                    }
                    return getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, item.getText());
                }).collect(Collectors.toList());
    }

    public ExtendedWebElement getLogoutHeader() {
        String label = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LOG_OUT_CONFIRMATION_TITLE.getText());
        String cellFormatLocator = "type == 'XCUIElementTypeAlert' and label == '%s'";
        return findExtendedWebElement(AppiumBy.iOSNsPredicateString(String.format(cellFormatLocator, label)));
    }

    public boolean isLogoutPageOpen() {
        boolean isPresent = getLogoutHeader().isPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void clickLocalizedRestartSubBtn() {
        restartSubscriptionLocalizedButton.click();
    }
}
