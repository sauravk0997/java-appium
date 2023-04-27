package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpNextIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusUpNextIOSPageBase.class)
public class DisneyPlusUpNextIOSPage extends DisneyPlusUpNextIOSPageBase {
    public DisneyPlusUpNextIOSPage(WebDriver driver) { super(driver);}
}
