package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusOneTimePasscodePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusOneTimePasscodePageBase.class)
    public class DisneyPlusOneTimePasscodePage extends DisneyPlusOneTimePasscodePageBase {
    public DisneyPlusOneTimePasscodePage(WebDriver driver) {
        super(driver);
    }
}
