package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusBrandIOSPageBase.class)
public class DisneyPlusAppleTVBrandsPage extends DisneyPlusBrandIOSPageBase {
    public DisneyPlusAppleTVBrandsPage(WebDriver driver) {
        super(driver);
    }
}