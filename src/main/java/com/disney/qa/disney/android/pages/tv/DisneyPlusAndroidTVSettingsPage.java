package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVSettingsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVSettingsPageBase.class)
public class DisneyPlusAndroidTVSettingsPage extends DisneyPlusAndroidTVSettingsPageBase {
    public DisneyPlusAndroidTVSettingsPage(WebDriver driver) {
        super(driver);
    }
}
