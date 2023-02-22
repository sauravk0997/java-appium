package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAppSettingsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAppSettingsIOSPageBase.class)
public class DisneyPlusTabletAppSettingsIOSPage extends DisneyPlusAppSettingsIOSPageBase {
    public DisneyPlusTabletAppSettingsIOSPage(WebDriver driver) {
        super(driver);
    }
}
