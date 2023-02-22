package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusAccountPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusContactCustomerServicePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusAccountPageBase.class)
public class DisneyPlusContactCustomerServicePage extends DisneyPlusContactCustomerServicePageBase {
    public DisneyPlusContactCustomerServicePage(WebDriver driver) {
        super(driver);
    }
}
