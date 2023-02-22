package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusBrandIOSPageBase.class)
public class DisneyPlusBrandsIOSPage extends DisneyPlusBrandIOSPageBase {
    public DisneyPlusBrandsIOSPage(WebDriver driver) {
        super(driver);
    }
}
