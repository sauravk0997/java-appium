package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEditGenderIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusEditGenderIOSPageBase.class)
public class DisneyPlusEditGenderIOSPage extends DisneyPlusEditGenderIOSPageBase {
    public DisneyPlusEditGenderIOSPage(WebDriver driver) {
        super(driver);
    }
}
