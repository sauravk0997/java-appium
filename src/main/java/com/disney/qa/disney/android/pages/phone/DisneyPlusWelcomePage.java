package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusWelcomePageBase.class)
public class DisneyPlusWelcomePage extends DisneyPlusWelcomePageBase {

    public DisneyPlusWelcomePage(WebDriver driver) {
        super(driver);
    }
}
