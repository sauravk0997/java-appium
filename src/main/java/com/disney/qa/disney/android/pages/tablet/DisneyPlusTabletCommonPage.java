package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusCommonPageBase.class)
public class DisneyPlusTabletCommonPage extends DisneyPlusCommonPageBase{

    public DisneyPlusTabletCommonPage(WebDriver driver) {
        super(driver);
    }
}
