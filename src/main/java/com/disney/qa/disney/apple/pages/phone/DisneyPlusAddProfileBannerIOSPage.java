package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileBannerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusAddProfileBannerIOSPageBase.class)
public class DisneyPlusAddProfileBannerIOSPage extends DisneyPlusAddProfileBannerIOSPageBase {
    //Functions
    public DisneyPlusAddProfileBannerIOSPage(WebDriver driver) {
        super(driver);
    }
}
