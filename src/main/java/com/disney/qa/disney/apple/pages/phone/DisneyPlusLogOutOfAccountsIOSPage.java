package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusLogOutOfDevicesIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusLogOutOfDevicesIOSPageBase.class)
public class DisneyPlusLogOutOfAccountsIOSPage extends DisneyPlusLogOutOfDevicesIOSPageBase {

    public DisneyPlusLogOutOfAccountsIOSPage(WebDriver driver) {
        super(driver);
    }
}
