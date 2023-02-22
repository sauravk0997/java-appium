package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusBrandPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusBrandPageBase.class)
public class DisneyPlusBrandPage extends DisneyPlusBrandPageBase{

    public DisneyPlusBrandPage(WebDriver driver){
        super(driver);
    }
}
