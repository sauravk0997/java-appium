package com.disney.qa.espn.ios.pages.phone.watch;

import com.disney.qa.espn.ios.pages.watch.EspnWatchIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnWatchIOSPageBase.class)
public class EspnWatchIOSPage extends EspnWatchIOSPageBase {
    public EspnWatchIOSPage(WebDriver driver) {
        super(driver);
    }
}
