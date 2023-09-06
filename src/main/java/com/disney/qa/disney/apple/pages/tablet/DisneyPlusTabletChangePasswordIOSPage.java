package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusChangePasswordIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusChangePasswordIOSPageBase.class)
public class DisneyPlusTabletChangePasswordIOSPage extends DisneyPlusChangePasswordIOSPageBase {

    public DisneyPlusTabletChangePasswordIOSPage(WebDriver driver) {
        super(driver);
    }
}
