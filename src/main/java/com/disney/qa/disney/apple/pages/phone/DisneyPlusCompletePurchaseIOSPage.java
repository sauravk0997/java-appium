package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusCompletePurchaseIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusCompletePurchaseIOSPageBase.class)
public class DisneyPlusCompletePurchaseIOSPage extends DisneyPlusCompletePurchaseIOSPageBase {
    public DisneyPlusCompletePurchaseIOSPage(WebDriver driver) {
        super(driver);
    }
}
