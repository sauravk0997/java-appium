package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOneTrustConsentBannerIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusOneTrustConsentBannerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "bannerTitle")
    protected ExtendedWebElement bannerTitle;

    @ExtendedFindBy(accessibilityId = "bannerDescriptions")
    protected ExtendedWebElement bannerDescriptions;

    @ExtendedFindBy(accessibilityId = "bannerDPDTitle")
    protected ExtendedWebElement bannerDPDTitle;

    @ExtendedFindBy(accessibilityId = "bannerDPDDescriptions")
    protected ExtendedWebElement bannerDPDDescriptions;

    @ExtendedFindBy(accessibilityId = "bannerListOfVendors")
    protected ExtendedWebElement bannerListOfVendors;

    @ExtendedFindBy(accessibilityId = "bannerButtonStackFirstItem")
    protected ExtendedWebElement bannerAllowAllButton;

    @ExtendedFindBy(accessibilityId = "bannerRejectAllButton")
    protected ExtendedWebElement rejectAllButton;

    @ExtendedFindBy(accessibilityId = "bannerPrivacySettingsButton")
    protected ExtendedWebElement bannerPrivacySettingsButton;

    @ExtendedFindBy(accessibilityId = "pcDescription")
    protected ExtendedWebElement preferenceCenterDescription;

    @ExtendedFindBy(accessibilityId = "pcConfirmMyChoiceButton")
    protected ExtendedWebElement pcConfirmMyChoiceButton;

    @Override
    public boolean isOpened() {
        return bannerDPDDescriptions.isPresent();
    }

    public boolean isBannerTitlePresent() {
        return bannerTitle.isPresent();
    }

    public boolean isBannerDescriptionsPresent() {
        return bannerDescriptions.isPresent();
    }

    public boolean isBannerDPDTitlePresent() {
        return bannerDPDTitle.isPresent();
    }

    public boolean isBannerDPDDescriptionsPresent() {
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

    public void tapConfirmMyChoiceButton() {
        pcConfirmMyChoiceButton.click();
    }

}
