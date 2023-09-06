package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPinIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusPinIOSPageBase.class)
public class DisneyPlusTabletPinIOSPage extends DisneyPlusPinIOSPageBase {
    public DisneyPlusTabletPinIOSPage(WebDriver driver) {
        super(driver);
    }
}
