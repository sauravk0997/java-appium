package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusOneTimePasscodeIOSPage extends DisneyPlusOneTimePasscodeIOSPageBase {

    public DisneyPlusOneTimePasscodeIOSPage (WebDriver driver) {
        super(driver);
    }
}
