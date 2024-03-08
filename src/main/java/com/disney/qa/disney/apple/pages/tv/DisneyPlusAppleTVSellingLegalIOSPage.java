package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyplusSellingLegalIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyplusSellingLegalIOSPageBase.class)
public class DisneyPlusAppleTVSellingLegalIOSPage extends DisneyplusSellingLegalIOSPageBase {

    public DisneyPlusAppleTVSellingLegalIOSPage(WebDriver driver) {
        super(driver);
    }
}
