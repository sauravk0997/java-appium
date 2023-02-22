package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVOriginalsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVOriginalsPageBase.class)
public class DisneyPlusAndroidTVOriginalsPage extends DisneyPlusAndroidTVOriginalsPageBase {

    public DisneyPlusAndroidTVOriginalsPage(WebDriver driver) {
        super(driver);
    }
}
