package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusLoginPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusLoginPageBase.class)
public class DisneyPlusTabletLoginPage extends DisneyPlusLoginPageBase{

    public DisneyPlusTabletLoginPage(WebDriver driver) {
       super(driver);
    }
}
