package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPinIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusPinIOSPageBase.class)
public class DisneyPlusPinIOSPage extends DisneyPlusPinIOSPageBase {
    public DisneyPlusPinIOSPage(WebDriver driver) {
        super(driver);
    }
}
