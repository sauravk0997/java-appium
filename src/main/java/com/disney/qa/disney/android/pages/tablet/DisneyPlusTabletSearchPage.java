package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusSearchPageBase.class)
public class DisneyPlusTabletSearchPage extends DisneyPlusSearchPageBase {

    public DisneyPlusTabletSearchPage(WebDriver driver){
        super(driver);
    }
}
