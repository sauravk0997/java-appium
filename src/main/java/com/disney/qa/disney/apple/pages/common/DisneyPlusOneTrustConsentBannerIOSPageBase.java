package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOneTrustConsentBannerIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusOneTrustConsentBannerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "bannerDPDDescriptions")
    protected ExtendedWebElement bannerDPDDescriptions;

    @ExtendedFindBy(accessibilityId = "bannerListOfVendors")
    protected ExtendedWebElement bannerListOfVendors;

    @ExtendedFindBy(accessibilityId = "bannerAllowAllButton")
    protected ExtendedWebElement bannerAllowAllButton;

    @ExtendedFindBy(accessibilityId = "bannerRejectAllButton")
    protected ExtendedWebElement rejectAllButton;

    @ExtendedFindBy(accessibilityId = "bannerPrivacySettingsButton")
    protected ExtendedWebElement bannerPrivacySettingsButton;

    @ExtendedFindBy(accessibilityId = "pcDescription")
    protected ExtendedWebElement preferenceCenterDescription;

    @Override
    public boolean isOpened() {
        return bannerDPDDescriptions.isPresent();
    }

    public boolean isListOfVendorsLinkPresent() {
        return bannerListOfVendors.isPresent();
    }

    public boolean isAllowAllButtonPresent() {
        return bannerAllowAllButton.isPresent();
    }

    public boolean isRejectAllButtonPresent() {
        return rejectAllButton.isPresent();
    }

    public boolean isCustomizedChoicesButtonPresent() {
        return bannerPrivacySettingsButton.isPresent();
    }

    public boolean isPrivacyPreferenceCenterOpen() {
        return preferenceCenterDescription.isPresent();
    }

    public void tapAcceptAllButton() {
        bannerAllowAllButton.click();
    }

    public void tapRejectAllButton() {
        rejectAllButton.click();
    }

    public void tapCustomizedChoices() {
        bannerPrivacySettingsButton.click();
    }

}
