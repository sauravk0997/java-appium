package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.phone.DisneyPlusVideoPlayerIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusVideoPlayerIOSPage.class)
public class DisneyPlusTabletVideoPlayerIOSPage extends DisneyPlusVideoPlayerIOSPage {
    public DisneyPlusTabletVideoPlayerIOSPage(WebDriver driver) {
        super(driver);
    }
}
