package com.disney.qa.disney.apple.pages.phone;

import org.openqa.selenium.WebDriver;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusOneTrustIOSPageBase.class)
public class DisneyPlusOneTrustIOSPage extends DisneyPlusOneTrustIOSPageBase {
    public DisneyPlusOneTrustIOSPage (WebDriver driver) {
        super(driver);
    }
}

