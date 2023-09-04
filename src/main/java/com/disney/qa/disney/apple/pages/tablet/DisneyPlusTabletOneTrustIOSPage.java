package com.disney.qa.disney.apple.pages.tablet;

import org.openqa.selenium.WebDriver;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusOneTrustIOSPageBase.class)
public class DisneyPlusTabletOneTrustIOSPage extends DisneyPlusOneTrustIOSPageBase {
    public DisneyPlusTabletOneTrustIOSPage (WebDriver driver) {
        super(driver);
    }
}

