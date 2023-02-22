package com.disney.qa.espn.ios.pages.tablet.listen;

import com.disney.qa.espn.ios.pages.listen.EspnListenIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.listen.EspnListenIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnListenIOSPageBase.class)
public class EspnTabletListenIOSPage extends EspnListenIOSPage {
    public EspnTabletListenIOSPage(WebDriver driver) {
        super(driver);
    }
}
