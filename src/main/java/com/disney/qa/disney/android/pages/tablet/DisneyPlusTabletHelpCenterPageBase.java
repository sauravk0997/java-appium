package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusHelpCenterPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusHelpCenterPageBase.class)
public class DisneyPlusTabletHelpCenterPageBase extends DisneyPlusHelpCenterPageBase {
    public DisneyPlusTabletHelpCenterPageBase(WebDriver driver) {
        super(driver);
    }
}
