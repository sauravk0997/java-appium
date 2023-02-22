package com.disney.qa.espn.ios.pages.phone;

import com.disney.qa.espn.ios.pages.common.EspnEditionIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnEditionIOSPageBase.class)
public class EspnEditionIOSPage extends EspnEditionIOSPageBase{
    public EspnEditionIOSPage(WebDriver driver) {
        super(driver);
    }
}
