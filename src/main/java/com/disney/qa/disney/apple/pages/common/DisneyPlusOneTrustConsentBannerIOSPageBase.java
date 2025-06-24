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

    @ExtendedFindBy(accessibilityId = "bannerButtonStackFirstItem")
    protected ExtendedWebElement bannerAllowAllButton;

    @ExtendedFindBy(accessibilityId = "bannerRejectAllButton")
    protected ExtendedWebElement rejectAllButton;

    @ExtendedFindBy(accessibilityId = "bannerPrivacySettingsButton")
    protected ExtendedWebElement bannerPrivacySettingsButton;

    @Override
    public boolean isOpened() {
        return bannerDPDDescriptions.isPresent();
    }

    public ExtendedWebElement getAcceptAllButton() {
        return bannerAllowAllButton;
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

    public void tapAcceptAllButton() {
        bannerAllowAllButton.click();
    }
}
