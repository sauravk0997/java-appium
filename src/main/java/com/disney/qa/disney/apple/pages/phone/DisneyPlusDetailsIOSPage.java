package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusDetailsIOSPageBase.class)
public class DisneyPlusDetailsIOSPage extends DisneyPlusDetailsIOSPageBase {
    public DisneyPlusDetailsIOSPage(WebDriver driver) {
        super(driver);
    }
}
