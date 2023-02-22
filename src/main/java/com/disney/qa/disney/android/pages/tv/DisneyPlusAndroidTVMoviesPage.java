package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVMoviesPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVMoviesPageBase.class)
public class DisneyPlusAndroidTVMoviesPage extends DisneyPlusAndroidTVMoviesPageBase {
    public DisneyPlusAndroidTVMoviesPage(WebDriver driver) {
        super(driver);
    }
}
