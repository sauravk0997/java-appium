package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.phone.DisneyPlusOneTimePasscodePage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusOneTimePasscodePage.class)
    public class DisneyPlusTabletOneTimePasscodePage extends DisneyPlusOneTimePasscodePage {
    public DisneyPlusTabletOneTimePasscodePage(WebDriver driver) {
        super(driver);
    }
}
