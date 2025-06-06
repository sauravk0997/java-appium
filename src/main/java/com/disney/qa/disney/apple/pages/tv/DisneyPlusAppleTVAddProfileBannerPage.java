package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileBannerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusAddProfileBannerIOSPageBase.class)
public class DisneyPlusAppleTVAddProfileBannerPage extends DisneyPlusAddProfileBannerIOSPageBase {

    public DisneyPlusAppleTVAddProfileBannerPage(WebDriver driver) {
        super(driver);
    }
}
