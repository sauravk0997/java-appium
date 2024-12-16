package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class AppStorePageBase extends DisneyPlusApplePageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "AppStore.productPage")
    private ExtendedWebElement appStoreScreen;
    @ExtendedFindBy(accessibilityId = "AppStore.shelfItemSubComponent.title")
    private ExtendedWebElement appStoreScreenTitle;
    @ExtendedFindBy(accessibilityId = "AppStore.onboarding.continueButton")
    private ExtendedWebElement onboardingContinueButton;
    @ExtendedFindBy(accessibilityId = "AppStore.onboarding.turnOffButton")
    private ExtendedWebElement turnOffPersonalizedAdsButton;

    //FUNCTIONS
    public AppStorePageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isAppStoreAppOpen() {
        return appStoreScreen.isPresent();
    }

    public String getAppStoreAppScreenTitle() {
        return appStoreScreenTitle.getText();
    }

    public void dismissOnboardingScreenIfPresent() {
        if (onboardingContinueButton.isPresent(TEN_SEC_TIMEOUT)) {
            onboardingContinueButton.click();
        }
    }

    public void dismissPersonalisedAdsScreenIfPresent() {
        if (turnOffPersonalizedAdsButton.isPresent(TEN_SEC_TIMEOUT)) {
            turnOffPersonalizedAdsButton.click();
        }
    }
}