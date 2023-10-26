package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustConsentBannerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusOneTrustConsentBannerIOSPageBase.class)
public class DisneyPlusTabletOneTrustConsentBannerIOSPage extends DisneyPlusOneTrustConsentBannerIOSPageBase {
    public DisneyPlusTabletOneTrustConsentBannerIOSPage (WebDriver driver) {
        super(driver);
    }
}