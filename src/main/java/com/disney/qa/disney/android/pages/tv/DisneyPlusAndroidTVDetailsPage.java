package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVDetailsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVDetailsPageBase.class)
public class DisneyPlusAndroidTVDetailsPage extends DisneyPlusAndroidTVDetailsPageBase {

    public DisneyPlusAndroidTVDetailsPage(WebDriver driver) {
        super(driver);
    }
}
