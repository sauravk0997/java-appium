package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusParentalConsentIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusParentalConsentIOSPageBase.class)
public class DisneyPlusParentalConsentIOSPage extends DisneyPlusParentalConsentIOSPageBase {
    public DisneyPlusParentalConsentIOSPage(WebDriver driver) {
        super(driver);
    }
}
