package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusParentalConsentIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusParentalConsentIOSPageBase.class)
public class DisneyPlusTabletParentalConsentIOSPage extends DisneyPlusParentalConsentIOSPageBase {
    public DisneyPlusTabletParentalConsentIOSPage(WebDriver driver) {
        super(driver);
    }
}