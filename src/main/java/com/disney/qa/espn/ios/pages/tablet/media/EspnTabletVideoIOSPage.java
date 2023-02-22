package com.disney.qa.espn.ios.pages.tablet.media;

import com.disney.qa.espn.ios.pages.media.EspnVideoIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.media.EspnVideoIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;


@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnVideoIOSPageBase.class)
public class EspnTabletVideoIOSPage extends EspnVideoIOSPage{
    public EspnTabletVideoIOSPage(WebDriver driver) {
        super(driver);
    }
}
