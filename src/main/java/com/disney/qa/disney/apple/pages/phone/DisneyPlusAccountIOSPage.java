package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAccountIOSPageBase.class)
public class DisneyPlusAccountIOSPage extends DisneyPlusAccountIOSPageBase {

    public DisneyPlusAccountIOSPage(WebDriver driver) {
        super(driver);
    }
}
