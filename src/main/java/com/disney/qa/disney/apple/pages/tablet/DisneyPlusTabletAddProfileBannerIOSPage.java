package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.*;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAddProfileBannerIOSPageBase.class)
public class DisneyPlusTabletAddProfileBannerIOSPage extends DisneyPlusAddProfileBannerIOSPageBase {

    //Functions
    public DisneyPlusTabletAddProfileBannerIOSPage(WebDriver driver) {
        super(driver);
    }
}
