package com.disney.qa.espn.ios.pages.phone.authentication;

import com.disney.qa.espn.ios.pages.authentication.EspnPaywallLogInIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnPaywallLogInIOSPageBase.class)
public class EspnPaywallLogInIOSPage extends EspnPaywallLogInIOSPageBase {
    public EspnPaywallLogInIOSPage(WebDriver driver) {
        super(driver);
    }
}
