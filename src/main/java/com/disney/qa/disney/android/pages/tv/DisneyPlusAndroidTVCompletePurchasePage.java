package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVCompletePurchasePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVCompletePurchasePageBase.class)
public class DisneyPlusAndroidTVCompletePurchasePage extends DisneyPlusAndroidTVCompletePurchasePageBase {
    public DisneyPlusAndroidTVCompletePurchasePage(WebDriver driver) {
        super(driver);
    }
}
