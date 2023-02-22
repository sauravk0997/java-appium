package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.globalnav.StarPlusAndroidTVESPNPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = StarPlusAndroidTVESPNPageBase.class)
public class StarPlusAndroidTVESPNPage extends StarPlusAndroidTVESPNPageBase {

    public StarPlusAndroidTVESPNPage(WebDriver driver) {
        super(driver);
    }
}
