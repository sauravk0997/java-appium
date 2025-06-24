package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVSubscriptionReacquisitionPage extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name == 'paywallLandingComplete'`][2]")
    private ExtendedWebElement disneyPlusLogo;

    public DisneyPlusAppleTVSubscriptionReacquisitionPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getDisneyPlusLogo() {
        return disneyPlusLogo;
    }

    public ExtendedWebElement getTitleTextElement() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE_ONBOARDING,
                DictionaryKeys.CO_ACCOUNT_NO_ENTITLEMENT_HEADER.getText()));
    }

    public ExtendedWebElement getDescriptionTextElement() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE_ONBOARDING,
                DictionaryKeys.CO_ACCOUNT_NO_ENTITLEMENT.getText()));
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
