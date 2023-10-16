package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEditGenderIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusEditGenderIOSPageBase.class)
public class DisneyPlusTabletEditGenderIOSPage extends DisneyPlusEditGenderIOSPageBase {

    public DisneyPlusTabletEditGenderIOSPage(WebDriver driver) {
        super(driver);
    }
}
