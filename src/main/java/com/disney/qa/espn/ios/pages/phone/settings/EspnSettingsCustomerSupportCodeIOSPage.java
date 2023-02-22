package com.disney.qa.espn.ios.pages.phone.settings;

import com.disney.qa.espn.ios.pages.settings.EspnSettingsCustomerSupportCodeIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnSettingsCustomerSupportCodeIOSPageBase.class)
public class EspnSettingsCustomerSupportCodeIOSPage extends EspnSettingsCustomerSupportCodeIOSPageBase {
    public EspnSettingsCustomerSupportCodeIOSPage(WebDriver driver) {
        super(driver);
    }
}
