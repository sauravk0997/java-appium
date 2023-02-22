package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountOnHoldIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAccountOnHoldIOSPageBase.class)
public class DisneyPlusTabletAccountOnHoldIOSPage extends DisneyPlusAccountOnHoldIOSPageBase {

    public DisneyPlusTabletAccountOnHoldIOSPage(WebDriver driver) { super(driver); }
}
