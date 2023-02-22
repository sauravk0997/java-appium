package com.disney.qa.espn.ios.pages.phone.watch;

import com.disney.qa.espn.ios.pages.watch.EspnWatchOriginalsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnWatchOriginalsIOSPageBase.class)
public class EspnWatchOriginalsIOSPage extends EspnWatchOriginalsIOSPageBase {
    public EspnWatchOriginalsIOSPage(WebDriver driver) {
        super(driver);
    }
}
