package com.disney.qa.espn.ios.pages.tablet.watch;

import com.disney.qa.espn.ios.pages.phone.watch.EspnWatchVideoPlayerIOSPage;
import com.disney.qa.espn.ios.pages.watch.EspnWatchVideoPlayerIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnWatchVideoPlayerIOSPageBase.class)
public class EspnTabletWatchVideoPlayerIOSPage extends EspnWatchVideoPlayerIOSPage {
    public EspnTabletWatchVideoPlayerIOSPage(WebDriver driver) {
        super(driver);
    }
}
