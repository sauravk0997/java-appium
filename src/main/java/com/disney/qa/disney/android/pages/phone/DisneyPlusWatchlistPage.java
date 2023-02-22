package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusWatchlistPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusWatchlistPageBase.class)
public class DisneyPlusWatchlistPage extends DisneyPlusWatchlistPageBase {
    public DisneyPlusWatchlistPage(WebDriver driver) {
        super(driver);
    }
}
