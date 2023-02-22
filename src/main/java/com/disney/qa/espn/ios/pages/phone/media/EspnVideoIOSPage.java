package com.disney.qa.espn.ios.pages.phone.media;

import com.disney.qa.espn.ios.pages.media.EspnVideoIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnVideoIOSPageBase.class)
public class EspnVideoIOSPage extends EspnVideoIOSPageBase {
    public EspnVideoIOSPage(WebDriver driver) {
        super(driver);
    }
}
