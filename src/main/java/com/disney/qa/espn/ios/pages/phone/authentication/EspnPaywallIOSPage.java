package com.disney.qa.espn.ios.pages.phone.authentication;

import com.disney.qa.espn.ios.pages.authentication.EspnPaywallIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnPaywallIOSPageBase.class)
public class EspnPaywallIOSPage extends EspnPaywallIOSPageBase {
    public EspnPaywallIOSPage(WebDriver driver) {
        super(driver);
    }
}
