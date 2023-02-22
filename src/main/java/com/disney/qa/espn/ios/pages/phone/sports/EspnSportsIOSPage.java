package com.disney.qa.espn.ios.pages.phone.sports;

import com.disney.qa.espn.ios.pages.sports.EspnSportsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnSportsIOSPage.class)
public class EspnSportsIOSPage extends EspnSportsIOSPageBase {
    public EspnSportsIOSPage(WebDriver driver) {
        super(driver);
    }
}
