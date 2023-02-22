package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusOriginalsIOSPageBase.class)
public class DisneyPlusTabletOriginalsIOSPage extends DisneyPlusOriginalsIOSPageBase {
    public DisneyPlusTabletOriginalsIOSPage(WebDriver driver) {
        super(driver);
    }
}
