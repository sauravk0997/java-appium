package com.disney.qa.espn.ios.pages.phone.listen;

import com.disney.qa.espn.ios.pages.listen.EspnListenIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnListenIOSPageBase.class)
public class EspnListenIOSPage extends EspnListenIOSPageBase {
    public EspnListenIOSPage(WebDriver driver) {
        super(driver);
    }
}
