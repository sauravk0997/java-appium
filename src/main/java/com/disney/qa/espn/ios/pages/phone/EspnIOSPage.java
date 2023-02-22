package com.disney.qa.espn.ios.pages.phone;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnIOSPageBase.class)
public class EspnIOSPage extends EspnIOSPageBase {
    public EspnIOSPage(WebDriver driver) {
        super(driver);
    }
}
