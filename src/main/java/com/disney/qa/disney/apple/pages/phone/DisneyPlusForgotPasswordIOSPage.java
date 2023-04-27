package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusForgotPasswordIOSPage extends DisneyPlusOneTimePasscodeIOSPageBase {
    public DisneyPlusForgotPasswordIOSPage(WebDriver driver) {
        super(driver);
    }
}
