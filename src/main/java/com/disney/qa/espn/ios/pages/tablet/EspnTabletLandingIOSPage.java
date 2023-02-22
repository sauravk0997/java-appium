package com.disney.qa.espn.ios.pages.tablet;

import com.disney.qa.espn.ios.pages.common.EspnLandingIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.EspnLandingIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnLandingIOSPageBase.class)
public class EspnTabletLandingIOSPage extends EspnLandingIOSPage {
    public EspnTabletLandingIOSPage(WebDriver driver) {
        super(driver);
    }
}
