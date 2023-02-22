package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusMaturityPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusMaturityPageBase.class)
public class DisneyPlusTabletMaturityPage extends DisneyPlusMaturityPageBase {
    public DisneyPlusTabletMaturityPage(WebDriver driver) {
        super(driver);
    }
}
