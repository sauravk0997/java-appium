package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusVerifyAgeIOSPageBase.class)
public class DisneyPlusTabletVerifyAgeIOSPage extends DisneyPlusVerifyAgeIOSPageBase {
    public DisneyPlusTabletVerifyAgeIOSPage(WebDriver driver) { super(driver);}
}
