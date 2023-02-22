package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusEditorialPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusEditorialPageBase.class)
public class DisneyPlusTabletEditorialPage extends DisneyPlusEditorialPageBase {

    public DisneyPlusTabletEditorialPage(WebDriver driver){
        super(driver);
    }
}
