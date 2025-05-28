package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSubscriptionReacquisitionIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeImage[`name == 'paywallLandingComplete'`][2]")
    private ExtendedWebElement disneyPlusLogo;

    public DisneyPlusSubscriptionReacquisitionIOSPageBase(WebDriver driver) {
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
}
