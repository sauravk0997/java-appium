package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.AppStorePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = AppStorePageBase.class)
public class AppStoreIOSPage extends AppStorePageBase {

    public AppStoreIOSPage(WebDriver driver) {
        super(driver);
    }
}
