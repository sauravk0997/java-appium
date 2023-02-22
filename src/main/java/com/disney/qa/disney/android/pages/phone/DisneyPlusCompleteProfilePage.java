package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusCompleteProfilePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusCompleteProfilePageBase.class)
public class DisneyPlusCompleteProfilePage extends DisneyPlusCompleteProfilePageBase {
    public DisneyPlusCompleteProfilePage(WebDriver driver) {
        super(driver);
    }
}
