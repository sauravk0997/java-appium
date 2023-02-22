package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusMediaPageBase.class)
public class DisneyPlusTabletMediaPage extends DisneyPlusMediaPageBase{

    public DisneyPlusTabletMediaPage(WebDriver driver){
        super(driver);
    }
}
