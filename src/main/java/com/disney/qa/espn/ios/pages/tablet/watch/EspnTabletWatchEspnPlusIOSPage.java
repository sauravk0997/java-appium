package com.disney.qa.espn.ios.pages.tablet.watch;

import com.disney.qa.espn.ios.pages.phone.watch.EspnWatchEspnPlusIOSPage;
import com.disney.qa.espn.ios.pages.watch.EspnWatchEspnPlusIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnWatchEspnPlusIOSPageBase.class)
public class EspnTabletWatchEspnPlusIOSPage extends EspnWatchEspnPlusIOSPage {
    public EspnTabletWatchEspnPlusIOSPage(WebDriver driver) {
        super(driver);
    }
}
