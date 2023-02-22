package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVSeriesPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVSeriesPageBase.class)
public class DisneyPlusAndroidTVSeriesPage extends DisneyPlusAndroidTVSeriesPageBase {

    public DisneyPlusAndroidTVSeriesPage(WebDriver driver) {
        super(driver);
    }
}
