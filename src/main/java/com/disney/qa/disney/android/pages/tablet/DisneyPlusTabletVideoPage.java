package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusVideoPageBase.class)
public class DisneyPlusTabletVideoPage extends DisneyPlusVideoPageBase {

    public DisneyPlusTabletVideoPage(WebDriver driver){
        super(driver);
    }
}
