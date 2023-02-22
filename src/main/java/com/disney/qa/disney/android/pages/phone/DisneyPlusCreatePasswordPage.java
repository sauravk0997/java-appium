package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusCreatePasswordPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusCreatePasswordPageBase.class)
public class DisneyPlusCreatePasswordPage extends DisneyPlusCreatePasswordPageBase {
    public DisneyPlusCreatePasswordPage(WebDriver driver) {
        super(driver);
    }
}
