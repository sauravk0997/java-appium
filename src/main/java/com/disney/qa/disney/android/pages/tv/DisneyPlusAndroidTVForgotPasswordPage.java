package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVForgotPasswordPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVForgotPasswordPageBase.class)
public class DisneyPlusAndroidTVForgotPasswordPage extends DisneyPlusAndroidTVForgotPasswordPageBase {

    public DisneyPlusAndroidTVForgotPasswordPage(WebDriver driver) {
        super(driver);
    }
}