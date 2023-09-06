package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAppSettingsIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusAppSettingsIOSPage;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusAppSettingsIOSPageBase.class)
public class DisneyPlusAppleTVAppSettingsPage extends DisneyPlusAppSettingsIOSPage {
    public DisneyPlusAppleTVAppSettingsPage(WebDriver driver) {
        super(driver);
    }
}
