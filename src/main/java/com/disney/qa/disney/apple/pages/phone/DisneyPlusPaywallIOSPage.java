package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPaywallIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusPaywallIOSPageBase.class)
public class DisneyPlusPaywallIOSPage extends DisneyPlusPaywallIOSPageBase {

    public DisneyPlusPaywallIOSPage(WebDriver driver) {
        super(driver);
    }
}
