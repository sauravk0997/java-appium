package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTrustIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusOneTrustIOSPageBase.class)
public class DisneyPlusOneTrustIOSPage extends DisneyPlusOneTrustIOSPageBase {
    public DisneyPlusOneTrustIOSPage (WebDriver driver) {
        super(driver);
    }
}

