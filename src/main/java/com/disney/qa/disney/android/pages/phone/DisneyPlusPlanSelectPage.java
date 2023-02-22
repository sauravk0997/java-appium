package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusPlanSelectPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusPlanSelectPageBase.class)
public class DisneyPlusPlanSelectPage extends DisneyPlusPlanSelectPageBase {
    public DisneyPlusPlanSelectPage(WebDriver driver) {
        super(driver);
    }
}
