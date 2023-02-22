package com.disney.qa.espn.ios.pages.tablet.settings;

import com.disney.qa.espn.ios.pages.phone.settings.EspnSettingsIOSPage;
import com.disney.qa.espn.ios.pages.settings.EspnSettingsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnSettingsIOSPageBase.class)
public class EspnTabletSettingsIOSPage extends EspnSettingsIOSPage {
    public EspnTabletSettingsIOSPage(WebDriver driver) {
        super(driver);
    }
}
