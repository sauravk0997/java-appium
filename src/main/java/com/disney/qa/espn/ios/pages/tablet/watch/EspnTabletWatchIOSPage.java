package com.disney.qa.espn.ios.pages.tablet.watch;

import com.disney.qa.espn.ios.pages.phone.watch.EspnWatchIOSPage;
import com.disney.qa.espn.ios.pages.watch.EspnWatchIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnWatchIOSPageBase.class)
public class EspnTabletWatchIOSPage extends EspnWatchIOSPage {
    public EspnTabletWatchIOSPage(WebDriver driver) {
        super(driver);
    }
}
