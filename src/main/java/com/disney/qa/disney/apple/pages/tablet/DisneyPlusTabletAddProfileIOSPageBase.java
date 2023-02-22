package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAddProfileIOSPageBase.class)
public class DisneyPlusTabletAddProfileIOSPageBase extends DisneyPlusAddProfileIOSPageBase {
    public DisneyPlusTabletAddProfileIOSPageBase(WebDriver driver) {
        super(driver);
    }
}
