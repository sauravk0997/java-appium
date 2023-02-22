package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.phone.DisneyPlusCompletePurchasePage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusCompletePurchasePage.class)
public class DisneyPlusTabletCompletePurchasePage extends DisneyPlusCompletePurchasePage {
    public DisneyPlusTabletCompletePurchasePage(WebDriver driver) {
        super(driver);
    }
}
