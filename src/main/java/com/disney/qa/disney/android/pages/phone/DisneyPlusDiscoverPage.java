package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusDiscoverPageBase.class)
public class DisneyPlusDiscoverPage extends DisneyPlusDiscoverPageBase {

    public DisneyPlusDiscoverPage(WebDriver driver) {
        super(driver);
    }
}
