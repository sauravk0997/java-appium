package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSubscriptionReacquisitionIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusSubscriptionReacquisitionIOSPageBase.class)
public class DisneyPlusAppleTVSubscriptionReacquisitionPage extends DisneyPlusSubscriptionReacquisitionIOSPageBase {

    public DisneyPlusAppleTVSubscriptionReacquisitionPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getJoinNowButton() {
        return dynamicBtnFindByLabel.format(
                getAppleTVLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE_ONBOARDING,
                        DictionaryKeys.DEVICE_BTN_JOIN_NOW.getText()));
    }

    public ExtendedWebElement getLogOutButton() {
        return dynamicBtnFindByLabel.format(
                getAppleTVLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE_ONBOARDING,
                        DictionaryKeys.DEVICE_BTN_LOG_OUT.getText()));
    }


}
