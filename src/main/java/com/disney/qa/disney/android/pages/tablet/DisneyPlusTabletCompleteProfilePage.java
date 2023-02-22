package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.phone.DisneyPlusCompleteProfilePage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusCompleteProfilePage.class)
public class DisneyPlusTabletCompleteProfilePage extends DisneyPlusCompleteProfilePage {
    public DisneyPlusTabletCompleteProfilePage(WebDriver driver) {
        super(driver);
    }
}
