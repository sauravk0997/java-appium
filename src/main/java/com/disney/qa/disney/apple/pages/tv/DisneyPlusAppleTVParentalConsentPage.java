package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusParentalConsentIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusParentalConsentIOSPageBase.class)
public class DisneyPlusAppleTVParentalConsentPage extends DisneyPlusParentalConsentIOSPageBase{

    public DisneyPlusAppleTVParentalConsentPage(WebDriver driver) {
        super(driver);
    }
}
