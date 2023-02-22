package com.disney.qa.disney.android.pages.tv;


import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVWelchPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusAndroidTVWelchPageBase.class)
public class DisneyPlusAndroidTVWelchPage extends DisneyPlusAndroidTVWelchPageBase {
    public DisneyPlusAndroidTVWelchPage(WebDriver driver) {
        super(driver);
    }
}
