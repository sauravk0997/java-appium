package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.phone.DisneyPlusCreatePasswordPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusCreatePasswordPage.class)
public class DisneyPlusTabletCreatePasswordPage extends DisneyPlusCreatePasswordPage {
    public DisneyPlusTabletCreatePasswordPage(WebDriver driver) {
        super(driver);
    }
}
