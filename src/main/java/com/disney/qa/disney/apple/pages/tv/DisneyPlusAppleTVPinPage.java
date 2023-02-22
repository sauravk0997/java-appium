package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPinIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusPinIOSPageBase.class)
public class DisneyPlusAppleTVPinPage extends DisneyPlusPinIOSPageBase {
    public DisneyPlusAppleTVPinPage(WebDriver driver) {
        super(driver);
    }
}
