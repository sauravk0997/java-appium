package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusCreatePasswordIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusCreatePasswordIOSPageBase.class)
public class DisneyPlusAppleTVCreatePasswordPage extends DisneyPlusCreatePasswordIOSPageBase {

    //LOCATORS

    //FUNCTIONS
    public DisneyPlusAppleTVCreatePasswordPage(WebDriver driver) {
        super(driver);
    }

}