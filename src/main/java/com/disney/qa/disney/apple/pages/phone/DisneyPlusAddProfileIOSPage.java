package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusAddProfileIOSPageBase.class)
public class DisneyPlusAddProfileIOSPage extends DisneyPlusAddProfileIOSPageBase {

    public DisneyPlusAddProfileIOSPage(WebDriver driver) {
        super(driver);
    }
}
