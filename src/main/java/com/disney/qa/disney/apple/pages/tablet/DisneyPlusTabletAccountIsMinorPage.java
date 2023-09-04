package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIsMinorIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAccountIsMinorIOSPageBase.class)

public class DisneyPlusTabletAccountIsMinorPage extends DisneyPlusAccountIsMinorIOSPageBase {

    public DisneyPlusTabletAccountIsMinorPage(WebDriver driver) { super(driver); }
}
