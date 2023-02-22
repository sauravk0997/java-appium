package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusLoginPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusLoginPageBase.class)
public class DisneyPlusLoginPage extends DisneyPlusLoginPageBase{

    public DisneyPlusLoginPage(WebDriver driver) {
       super(driver);
    }
}
