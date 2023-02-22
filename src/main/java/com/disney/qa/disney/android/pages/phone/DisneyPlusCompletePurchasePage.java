package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusCompletePurchasePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusCompletePurchasePageBase.class)
public class DisneyPlusCompletePurchasePage extends DisneyPlusCompletePurchasePageBase {
    public DisneyPlusCompletePurchasePage(WebDriver driver) {
        super(driver);
    }
}
