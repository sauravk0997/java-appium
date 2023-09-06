package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusWhoseWatchingIOSPageBase.class)
public class DisneyPlusWhoseWatchingIOSPage extends DisneyPlusWhoseWatchingIOSPageBase {

    public DisneyPlusWhoseWatchingIOSPage(WebDriver driver) {
        super(driver);
    }
}