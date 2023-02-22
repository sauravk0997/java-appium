package com.disney.qa.espn.ios.pages.tablet.watch;

import com.disney.qa.espn.ios.pages.phone.watch.EspnWatchFeaturedIOSPage;
import com.disney.qa.espn.ios.pages.watch.EspnWatchFeaturedIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnWatchFeaturedIOSPageBase.class)
public class EspnTabletWatchFeaturedIOSPage extends EspnWatchFeaturedIOSPage {
    public EspnTabletWatchFeaturedIOSPage(WebDriver driver) {
        super(driver);
    }
}
