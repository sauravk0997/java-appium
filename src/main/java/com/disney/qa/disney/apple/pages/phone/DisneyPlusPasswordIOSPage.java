package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusPasswordIOSPageBase.class)
public class DisneyPlusPasswordIOSPage extends DisneyPlusPasswordIOSPageBase {
    public DisneyPlusPasswordIOSPage(WebDriver driver) {
        super(driver);
    }
}