package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpdateProfileIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusUpdateProfileIOSPageBase.class)
public class DisneyPlusTabletUpdateProfileIOSPage extends DisneyPlusUpdateProfileIOSPageBase {
    public DisneyPlusTabletUpdateProfileIOSPage(WebDriver driver) {
        super(driver);
    }
}
