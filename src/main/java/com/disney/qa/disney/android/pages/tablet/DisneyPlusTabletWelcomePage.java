package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusWelcomePageBase.class)
public class DisneyPlusTabletWelcomePage extends DisneyPlusWelcomePageBase {

    public DisneyPlusTabletWelcomePage(WebDriver driver) {
        super(driver);
    }
}
