package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusDetailsIOSPage;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusDetailsIOSPageBase.class)
public class DisneyPlusTabletDetailsIOSPage extends DisneyPlusDetailsIOSPage {
    public DisneyPlusTabletDetailsIOSPage(WebDriver driver) {
        super(driver);
    }
}
