package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusAccountPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusAccountPageBase.class)
public class DisneyPlusAccountPage extends DisneyPlusAccountPageBase {
    public DisneyPlusAccountPage(WebDriver driver) {
        super(driver);
    }
}
