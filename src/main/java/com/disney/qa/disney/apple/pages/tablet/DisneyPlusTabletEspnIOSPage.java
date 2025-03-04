package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.*;
import com.zebrunner.carina.utils.factory.*;
import org.openqa.selenium.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusEspnIOSPageBase.class)
public class DisneyPlusTabletEspnIOSPage extends DisneyPlusEspnIOSPageBase {
    public DisneyPlusTabletEspnIOSPage(WebDriver driver) {
        super(driver);
    }
}