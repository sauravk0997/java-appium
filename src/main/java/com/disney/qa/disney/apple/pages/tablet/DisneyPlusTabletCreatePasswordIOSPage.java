package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.phone.DisneyPlusCreatePasswordIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusCreatePasswordIOSPage.class)
public class DisneyPlusTabletCreatePasswordIOSPage extends DisneyPlusCreatePasswordIOSPage {
    public DisneyPlusTabletCreatePasswordIOSPage(WebDriver driver) {
        super(driver);
    }
}
