package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpdateProfileIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusUpdateProfileIOSPageBase.class)
public class DisneyPlusUpdateProfileIOSPage extends DisneyPlusUpdateProfileIOSPageBase {
    public DisneyPlusUpdateProfileIOSPage(WebDriver driver) {
        super(driver) ;
    }
}
