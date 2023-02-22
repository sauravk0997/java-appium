package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusMaturityPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusMaturityPageBase.class)
public class DisneyPlusMaturityPage extends DisneyPlusMaturityPageBase {
    public DisneyPlusMaturityPage(WebDriver driver) {
        super(driver);
    }
}
