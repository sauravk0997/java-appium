package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusSignUpPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusSignUpPageBase.class)
public class DisneyPlusTabletSignUpPage extends DisneyPlusSignUpPageBase {

    public DisneyPlusTabletSignUpPage(WebDriver driver) {super(driver);
    }
}
