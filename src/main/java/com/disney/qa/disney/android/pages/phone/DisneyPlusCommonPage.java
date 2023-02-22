package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusCommonPageBase.class)
public class DisneyPlusCommonPage extends DisneyPlusCommonPageBase{

    public DisneyPlusCommonPage(WebDriver driver) {
        super(driver);
    }
}
