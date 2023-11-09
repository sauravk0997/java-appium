package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusHuluIOSPageBase.class)
public class DisneyPlusTabletHuluIOSPage extends DisneyPlusHuluIOSPageBase {

    public DisneyPlusTabletHuluIOSPage(WebDriver driver) {
        super(driver);
    }
}
