package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustConsentBannerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOneTrustConsentBannerIOSPageBase.class)
public class DisneyPlusAppleTVOneTrustConsentBannerIOSPage extends DisneyPlusOneTrustConsentBannerIOSPageBase {
    public DisneyPlusAppleTVOneTrustConsentBannerIOSPage (WebDriver driver) {
        super(driver);
    }
}