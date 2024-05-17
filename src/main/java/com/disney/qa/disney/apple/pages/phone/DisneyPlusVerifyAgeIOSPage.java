package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusVerifyAgeIOSPageBase.class)
public class DisneyPlusVerifyAgeIOSPage extends DisneyPlusVerifyAgeIOSPageBase {
    public DisneyPlusVerifyAgeIOSPage(WebDriver driver) {
        super(driver);
    }
}
