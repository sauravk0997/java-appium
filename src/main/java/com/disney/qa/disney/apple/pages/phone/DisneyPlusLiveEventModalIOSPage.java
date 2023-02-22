package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLiveEventModalIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusLiveEventModalIOSPageBase.class)
public class DisneyPlusLiveEventModalIOSPage extends DisneyPlusApplePageBase {

    public DisneyPlusLiveEventModalIOSPage(WebDriver driver) {
        super(driver);
    }
}
