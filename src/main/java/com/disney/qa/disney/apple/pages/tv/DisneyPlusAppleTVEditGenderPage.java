package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEditGenderIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusEditGenderIOSPageBase.class)
public class DisneyPlusAppleTVEditGenderPage extends DisneyPlusEditGenderIOSPageBase {

    public DisneyPlusAppleTVEditGenderPage(WebDriver driver) {
        super(driver);
    }
}
