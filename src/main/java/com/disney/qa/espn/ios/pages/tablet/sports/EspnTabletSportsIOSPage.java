package com.disney.qa.espn.ios.pages.tablet.sports;

import com.disney.qa.espn.ios.pages.phone.sports.EspnSportsIOSPage;
import com.disney.qa.espn.ios.pages.sports.EspnSportsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnSportsIOSPageBase.class)
public class EspnTabletSportsIOSPage extends EspnSportsIOSPage {
    public EspnTabletSportsIOSPage(WebDriver driver) {
        super(driver);
    }
}
