package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusContactCustomerServicePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusContactCustomerServicePageBase.class)
public class DisneyPlusTabletContactCustomerServicePage extends DisneyPlusContactCustomerServicePageBase {
    public DisneyPlusTabletContactCustomerServicePage(WebDriver driver) {
        super(driver);
    }
}
