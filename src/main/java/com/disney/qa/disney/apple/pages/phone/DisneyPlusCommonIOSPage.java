package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusCommonIOSPage extends DisneyPlusApplePageBase {
    public DisneyPlusCommonIOSPage(WebDriver driver) {
        super(driver);
    }
}
