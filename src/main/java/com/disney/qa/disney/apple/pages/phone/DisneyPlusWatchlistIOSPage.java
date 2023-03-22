package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWatchlistIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusWatchlistIOSPageBase.class)
public class DisneyPlusWatchlistIOSPage extends DisneyPlusWatchlistIOSPageBase {

    public DisneyPlusWatchlistIOSPage(WebDriver driver) {
        super(driver);
    }
}