package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusEditProfileIOSPageBase.class)
public class DisneyPlusEditProfileIOSPage extends DisneyPlusEditProfileIOSPageBase {

    public DisneyPlusEditProfileIOSPage(WebDriver driver) {
        super(driver);
    }
}
