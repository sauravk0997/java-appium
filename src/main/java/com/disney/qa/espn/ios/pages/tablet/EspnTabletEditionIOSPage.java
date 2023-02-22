package com.disney.qa.espn.ios.pages.tablet;


import com.disney.qa.espn.ios.pages.common.EspnEditionIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.EspnEditionIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnEditionIOSPageBase.class)
public class EspnTabletEditionIOSPage extends EspnEditionIOSPage{
    public EspnTabletEditionIOSPage(WebDriver driver) {
        super(driver);
    }
}
