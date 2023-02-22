package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusChangeEmailIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusChangeEmailIOSPageBase.class)
public class DisneyPlusTabletChangeEmailIOSPage extends DisneyPlusChangeEmailIOSPageBase {

    public DisneyPlusTabletChangeEmailIOSPage(WebDriver driver) {
        super(driver);
    }
}
