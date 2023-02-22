package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusRestartSubscriptionIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusRestartSubscriptionIOSPageBase.class)
public class DisneyPlusTabletRestartSubscriptionIOSPage extends DisneyPlusRestartSubscriptionIOSPageBase {
    public DisneyPlusTabletRestartSubscriptionIOSPage(WebDriver driver) {
        super(driver);
    }
}
