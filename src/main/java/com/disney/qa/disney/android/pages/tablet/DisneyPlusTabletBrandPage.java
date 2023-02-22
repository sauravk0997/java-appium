package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusBrandPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusBrandPageBase.class)
public class DisneyPlusTabletBrandPage extends DisneyPlusBrandPageBase{

    public DisneyPlusTabletBrandPage(WebDriver driver){
        super(driver);
    }
}
