package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountOnHoldIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusAccountOnHoldIOSPageBase.class)
public class DisneyPlusAccountOnHoldIOSPage extends DisneyPlusAccountOnHoldIOSPageBase {

    public DisneyPlusAccountOnHoldIOSPage(WebDriver driver) { super(driver); }
}
