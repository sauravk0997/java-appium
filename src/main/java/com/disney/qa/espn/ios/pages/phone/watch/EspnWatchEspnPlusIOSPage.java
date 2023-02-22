package com.disney.qa.espn.ios.pages.phone.watch;

import com.disney.qa.espn.ios.pages.watch.EspnWatchEspnPlusIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnWatchEspnPlusIOSPageBase.class)
public class EspnWatchEspnPlusIOSPage extends EspnWatchEspnPlusIOSPageBase {
    public EspnWatchEspnPlusIOSPage(WebDriver driver) {
        super(driver);
    }
}
