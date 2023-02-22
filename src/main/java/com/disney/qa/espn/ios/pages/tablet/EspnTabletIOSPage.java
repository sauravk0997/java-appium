package com.disney.qa.espn.ios.pages.tablet;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.EspnIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnIOSPageBase.class)
public class EspnTabletIOSPage extends EspnIOSPage {
    public EspnTabletIOSPage(WebDriver driver) {
        super(driver);
    }
}