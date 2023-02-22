package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusVideoPlayerIOSPageBase.class)
public class DisneyPlusVideoPlayerIOSPage extends DisneyPlusVideoPlayerIOSPageBase {
    public DisneyPlusVideoPlayerIOSPage(WebDriver driver) {
        super(driver);
    }
}
