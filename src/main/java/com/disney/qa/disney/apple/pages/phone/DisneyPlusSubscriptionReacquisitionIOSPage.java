package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusSubscriptionReacquisitionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusSubscriptionReacquisitionIOSPageBase.class)
public class DisneyPlusSubscriptionReacquisitionIOSPage extends DisneyPlusSubscriptionReacquisitionIOSPageBase {

    public DisneyPlusSubscriptionReacquisitionIOSPage(WebDriver driver) {
        super(driver);
    }
}
