package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusBrandIOSPageBase.class)
public class DisneyPlusTabletBrandsIOSPage extends DisneyPlusBrandIOSPageBase {
    public DisneyPlusTabletBrandsIOSPage(WebDriver driver) {
        super(driver);
    }
}
