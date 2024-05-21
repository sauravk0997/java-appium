package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVerifyAgeIOSPageBase.class)
public class DisneyPlusAppleTVVerifyAgePage extends DisneyPlusVerifyAgeIOSPageBase {

    //LOCATORS

    //FUNCTIONS
    public DisneyPlusAppleTVVerifyAgePage(WebDriver driver) {
        super(driver);
    }

}