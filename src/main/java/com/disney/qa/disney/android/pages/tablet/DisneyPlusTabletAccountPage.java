package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.phone.DisneyPlusAccountPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusAccountPage.class)
public class DisneyPlusTabletAccountPage extends DisneyPlusAccountPage{
    public DisneyPlusTabletAccountPage(WebDriver driver) {
        super(driver);
    }
}
