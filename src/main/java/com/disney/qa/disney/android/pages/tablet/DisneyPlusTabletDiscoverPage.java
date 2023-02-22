package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusDiscoverPageBase.class)
public class DisneyPlusTabletDiscoverPage extends DisneyPlusDiscoverPageBase {

    public DisneyPlusTabletDiscoverPage(WebDriver driver) {
        super(driver);
    }
}
