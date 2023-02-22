package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusLogOutOfDevicesIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusLogOutOfDevicesIOSPageBase.class)
public class DisneyPlusTabletLogOutOfAccountsIOSPage extends DisneyPlusLogOutOfDevicesIOSPageBase {

    public DisneyPlusTabletLogOutOfAccountsIOSPage(WebDriver driver) {
        super(driver);
    }
}
