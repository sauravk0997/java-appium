package com.disney.qa.espn.ios.pages.tablet.watch;

import com.disney.qa.espn.ios.pages.phone.watch.EspnWatchOriginalsIOSPage;
import com.disney.qa.espn.ios.pages.watch.EspnWatchOriginalsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnWatchOriginalsIOSPageBase.class)
public class EspnTabletWatchOriginalsIOSPage extends EspnWatchOriginalsIOSPage {
    public EspnTabletWatchOriginalsIOSPage(WebDriver driver) {
        super(driver);
    }
}
