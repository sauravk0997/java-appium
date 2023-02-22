package com.disney.qa.common.utils.ios_settings;

import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = IOSSettingsMenuBase.class)
public class IOSSettingsMenuHandset extends IOSSettingsMenuBase {
    public IOSSettingsMenuHandset(WebDriver driver) {
        super(driver);
    }
}
