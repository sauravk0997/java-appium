package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAppSettingsIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusAppSettingsIOSPageBase.class)

public class DisneyPlusAppSettingsIOSPage extends DisneyPlusAppSettingsIOSPageBase {
    public DisneyPlusAppSettingsIOSPage(WebDriver driver) {
        super(driver);
    }
}
