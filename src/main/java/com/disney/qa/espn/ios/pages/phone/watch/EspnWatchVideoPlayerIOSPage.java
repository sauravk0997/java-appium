package com.disney.qa.espn.ios.pages.phone.watch;

import com.disney.qa.espn.ios.pages.watch.EspnWatchVideoPlayerIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnWatchVideoPlayerIOSPageBase.class)
public class EspnWatchVideoPlayerIOSPage extends EspnWatchVideoPlayerIOSPageBase {
    public EspnWatchVideoPlayerIOSPage(WebDriver driver) {
        super(driver);
    }
}
