package com.disney.qa.espn.ios.pages.tablet.settings;

import com.disney.qa.espn.ios.pages.phone.settings.EspnSettingsCustomerSupportCodeIOSPage;
import com.disney.qa.espn.ios.pages.settings.EspnSettingsCustomerSupportCodeIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnSettingsCustomerSupportCodeIOSPageBase.class)
public class EspnTabletSettingsCustomerSupportCodeIOSPage extends EspnSettingsCustomerSupportCodeIOSPage {
    public EspnTabletSettingsCustomerSupportCodeIOSPage(WebDriver driver) {
        super(driver);
    }
}
