package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusSearchPageBase.class)
public class DisneyPlusSearchPage extends DisneyPlusSearchPageBase {

    public DisneyPlusSearchPage(WebDriver driver){
        super(driver);
    }
}
