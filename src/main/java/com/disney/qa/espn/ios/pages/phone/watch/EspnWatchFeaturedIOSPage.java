package com.disney.qa.espn.ios.pages.phone.watch;

import com.disney.qa.espn.ios.pages.watch.EspnWatchFeaturedIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnWatchFeaturedIOSPageBase.class)
public class EspnWatchFeaturedIOSPage extends EspnWatchFeaturedIOSPageBase {
    public EspnWatchFeaturedIOSPage(WebDriver driver) {
        super(driver);
    }
}
