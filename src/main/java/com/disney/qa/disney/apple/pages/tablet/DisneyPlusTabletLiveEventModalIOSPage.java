package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusLiveEventModalIOSPageBase;

import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusLiveEventModalIOSPageBase.class)
public class DisneyPlusTabletLiveEventModalIOSPage extends DisneyPlusLiveEventModalIOSPageBase {

    public DisneyPlusTabletLiveEventModalIOSPage(WebDriver driver) {
        super(driver);
    }
}
