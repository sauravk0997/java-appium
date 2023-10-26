package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustConsentBannerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusOneTrustConsentBannerIOSPageBase.class)
public class DisneyPlusOneTrustConsentBannerIOSPage extends DisneyPlusOneTrustConsentBannerIOSPageBase {
    public DisneyPlusOneTrustConsentBannerIOSPage (WebDriver driver) {
        super(driver);
    }
}
