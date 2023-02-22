package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVWatchlistPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVWatchlistPageBase.class)
public class DisneyPlusAndroidTVWatchlistPage extends DisneyPlusAndroidTVWatchlistPageBase {
    public DisneyPlusAndroidTVWatchlistPage(WebDriver driver) {
        super(driver);
    }
}
