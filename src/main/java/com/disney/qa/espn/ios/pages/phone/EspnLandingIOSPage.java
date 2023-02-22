package com.disney.qa.espn.ios.pages.phone;

import com.disney.qa.espn.ios.pages.common.EspnLandingIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnLandingIOSPageBase.class)
public class EspnLandingIOSPage extends EspnLandingIOSPageBase {
    public EspnLandingIOSPage(WebDriver driver) {
        super(driver);
    }
}
