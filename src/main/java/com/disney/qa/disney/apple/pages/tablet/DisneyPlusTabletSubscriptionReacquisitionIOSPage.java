package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusSubscriptionReacquisitionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusSubscriptionReacquisitionIOSPageBase.class)
public class DisneyPlusTabletSubscriptionReacquisitionIOSPage extends DisneyPlusSubscriptionReacquisitionIOSPageBase {

    public DisneyPlusTabletSubscriptionReacquisitionIOSPage(WebDriver driver) {
        super(driver);
    }
}
