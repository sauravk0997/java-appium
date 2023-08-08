package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusOneTrustIOSPageBase.class)
public class DisneyPlusTabletOneTrustIOSPage extends DisneyPlusOneTrustIOSPageBase {
    public DisneyPlusTabletOneTrustIOSPage (WebDriver driver) {
        super(driver);
    }
}

