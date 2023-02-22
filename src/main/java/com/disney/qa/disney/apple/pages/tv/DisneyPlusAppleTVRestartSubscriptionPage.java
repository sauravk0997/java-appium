package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusRestartSubscriptionIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.RESTARTSUBSCRIPTION_CTA;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.RESTARTSUBSCRIPTION_LOGOUT;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.RESTART_SUB_COPY;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.RESTART_SUB_COPY_2;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusRestartSubscriptionIOSPageBase.class)
public class DisneyPlusAppleTVRestartSubscriptionPage extends DisneyPlusRestartSubscriptionIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Restart Subscription\"`]")
    private ExtendedWebElement restartSubscriptionButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Are you sure you want to log out?\"`]")
    private ExtendedWebElement logoutPageHeader;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == 'paywallSubscribeRestart'`]")
    private ExtendedWebElement paywallSubscribeRestart;

    @ExtendedFindBy(accessibilityId = "restoreButton")
    private ExtendedWebElement restartSubscriptionLocalizedButton;

    public DisneyPlusAppleTVRestartSubscriptionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = paywallSubscribeRestart.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public List<String> getRestartSubscriptionScreenDictionaryTexts() {
        return Stream.of(RESTART_SUB_COPY, RESTART_SUB_COPY_2, RESTARTSUBSCRIPTION_CTA, RESTARTSUBSCRIPTION_LOGOUT)
                .map(item -> {
                    if (item == RESTARTSUBSCRIPTION_CTA || item == RESTARTSUBSCRIPTION_LOGOUT) {
                        return getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, item.getText()).toUpperCase();
                    }
                    return getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, item.getText());
                }).collect(Collectors.toList());
    }

    public boolean isLogoutPageOpen() {
        boolean isPresent = logoutPageHeader.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void clickLocalizedRestartSubBtn() {
        restartSubscriptionLocalizedButton.click();
    }
}
