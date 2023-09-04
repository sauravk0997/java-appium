package com.disney.qa.common.utils.ios_settings;

import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = IOSSettingsMenuBase.class)
public class IOSSettingsMenuTablet extends IOSSettingsMenuBase{

    public IOSSettingsMenuTablet(WebDriver driver) {
        super(driver);
    }
}
