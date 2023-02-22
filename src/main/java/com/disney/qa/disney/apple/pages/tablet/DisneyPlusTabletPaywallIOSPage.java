package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAccountIOSPageBase.class)
public class DisneyPlusTabletPaywallIOSPage extends DisneyPlusAccountIOSPageBase {

    public DisneyPlusTabletPaywallIOSPage(WebDriver driver) {
        super(driver);
    }
}
