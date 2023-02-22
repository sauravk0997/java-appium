package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusOriginalsIOSPageBase.class)
public class DisneyPlusOriginalsIOSPage extends DisneyPlusOriginalsIOSPageBase {

    public DisneyPlusOriginalsIOSPage (WebDriver driver){
        super(driver);
    }
}
