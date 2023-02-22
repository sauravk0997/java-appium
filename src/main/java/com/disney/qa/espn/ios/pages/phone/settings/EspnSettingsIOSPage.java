package com.disney.qa.espn.ios.pages.phone.settings;

import com.disney.qa.espn.ios.pages.settings.EspnSettingsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnSettingsIOSPageBase.class)
public class EspnSettingsIOSPage extends EspnSettingsIOSPageBase {
    public EspnSettingsIOSPage(WebDriver driver) {
        super(driver);
    }
}
