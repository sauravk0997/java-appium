package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusChangeEmailIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusChangeEmailIOSPageBase.class)
public class DisneyPlusChangeEmailIOSPage extends DisneyPlusChangeEmailIOSPageBase {

    public DisneyPlusChangeEmailIOSPage(WebDriver driver) {
        super(driver);
    }
}
