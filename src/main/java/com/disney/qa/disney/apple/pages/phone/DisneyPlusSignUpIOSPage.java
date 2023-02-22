package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusSignUpIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusSignUpIOSPageBase.class)
public class DisneyPlusSignUpIOSPage extends DisneyPlusSignUpIOSPageBase {
    public DisneyPlusSignUpIOSPage(WebDriver driver) {
        super(driver);
    }
}
