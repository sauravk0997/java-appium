package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusCompleteSubscriptionIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusCompleteSubscriptionIOSPageBase.class)
public class DisneyPlusTabletCompleteSubscriptionIOSPage extends DisneyPlusCompleteSubscriptionIOSPageBase {
    public DisneyPlusTabletCompleteSubscriptionIOSPage(WebDriver driver) {
        super(driver);
    }
}
