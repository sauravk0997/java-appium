package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusCreatePasswordIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusCreatePasswordIOSPageBase.class)
public class DisneyPlusCreatePasswordIOSPage extends DisneyPlusCreatePasswordIOSPageBase {
    public DisneyPlusCreatePasswordIOSPage(WebDriver driver) {
        super(driver);
    }
}
