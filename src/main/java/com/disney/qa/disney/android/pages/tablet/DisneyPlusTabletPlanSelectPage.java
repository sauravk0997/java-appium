package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusPlanSelectPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusPlanSelectPageBase.class)
public class DisneyPlusTabletPlanSelectPage extends DisneyPlusPlanSelectPageBase {

    public DisneyPlusTabletPlanSelectPage(WebDriver driver) {
        super(driver);
    }
}
