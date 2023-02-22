package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpNextIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusUpNextIOSPageBase.class)
public class DisneyPlusTabletUpNextIOSPage extends DisneyPlusUpNextIOSPageBase {
    public DisneyPlusTabletUpNextIOSPage(WebDriver driver) { super(driver);}
}
