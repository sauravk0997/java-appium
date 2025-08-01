package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustConsentBannerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOneTrustConsentBannerIOSPageBase.class)
public class DisneyPlusAppleTVOneTrustConsentBannerIOSPage extends DisneyPlusOneTrustConsentBannerIOSPageBase {

    @ExtendedFindBy(accessibilityId = "bannerAllowAllButton")
    private ExtendedWebElement tvOsBannerAllowAllButton;

    public DisneyPlusAppleTVOneTrustConsentBannerIOSPage (WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return tvOsBannerAllowAllButton.isPresent();
    }

    @Override
    public ExtendedWebElement getAcceptAllButton() {
        return tvOsBannerAllowAllButton;
    }

    @Override
    public boolean isAllowAllButtonPresent() {
        return tvOsBannerAllowAllButton.isPresent();
    }

    @Override
    public void tapAcceptAllButton() {
        tvOsBannerAllowAllButton.click();
    }
}