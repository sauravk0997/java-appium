package com.disney.qa.espn.ios.pages.phone.home;

import com.disney.qa.espn.ios.pages.home.EspnHomeIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnHomeIOSPageBase.class)
public class EspnHomeIOSPage extends EspnHomeIOSPageBase {
    public EspnHomeIOSPage(WebDriver driver) {
        super(driver);
    }
}
