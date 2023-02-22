package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusSignUpPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusSignUpPageBase.class)
public class DisneyPlusSignUpPage extends DisneyPlusSignUpPageBase {

    public DisneyPlusSignUpPage(WebDriver driver) {
        super(driver);
    }
}

