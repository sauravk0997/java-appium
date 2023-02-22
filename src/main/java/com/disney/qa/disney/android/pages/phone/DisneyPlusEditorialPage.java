package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusEditorialPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusEditorialPageBase.class)
public class DisneyPlusEditorialPage extends DisneyPlusEditorialPageBase {

    public DisneyPlusEditorialPage(WebDriver driver){
        super(driver);
    }
}
