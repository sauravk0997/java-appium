package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusChangePasswordIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusChangePasswordIOSPageBase.class)
public class DisneyPlusChangePasswordIOSPage extends DisneyPlusChangePasswordIOSPageBase {

    public DisneyPlusChangePasswordIOSPage(WebDriver driver) {
        super(driver);
    }
}
