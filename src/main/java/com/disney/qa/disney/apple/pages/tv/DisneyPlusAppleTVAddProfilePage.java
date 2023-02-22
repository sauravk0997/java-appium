package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVAddProfilePage extends DisneyPlusAddProfileIOSPageBase {
    public DisneyPlusAppleTVAddProfilePage(WebDriver driver) {
        super(driver);
    }
}
