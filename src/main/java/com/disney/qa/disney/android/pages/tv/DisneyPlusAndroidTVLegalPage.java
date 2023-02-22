package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVLegalPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVLegalPageBase.class)
public class DisneyPlusAndroidTVLegalPage extends DisneyPlusAndroidTVLegalPageBase
{
    public DisneyPlusAndroidTVLegalPage(WebDriver driver) {
        super(driver);
    }
}
