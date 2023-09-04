package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusEditProfileIOSPageBase.class)
public class DisneyPlusTabletEditProfileIOSPage extends DisneyPlusEditProfileIOSPageBase {
    public DisneyPlusTabletEditProfileIOSPage(WebDriver driver) {
        super(driver);
    }

}
