package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusWatchlistPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusWatchlistPageBase.class)
public class DisneyPlustTabletWatchlistPage extends DisneyPlusWatchlistPageBase {
    public DisneyPlustTabletWatchlistPage(WebDriver driver) {
        super(driver);
    }
}
