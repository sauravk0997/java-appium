package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusMediaPageBase.class)
public class DisneyPlusMediaPage extends DisneyPlusMediaPageBase{

    public DisneyPlusMediaPage(WebDriver driver){
        super(driver);
    }
}
