package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIsMinorIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusAccountIsMinorIOSPageBase.class)
public class DisneyPlusAccountIsMinorIOSPage extends DisneyPlusAccountIsMinorIOSPageBase {

    public DisneyPlusAccountIsMinorIOSPage (WebDriver driver) { super(driver); }
}
