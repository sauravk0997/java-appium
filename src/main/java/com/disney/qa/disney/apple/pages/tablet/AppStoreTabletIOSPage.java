package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.AppStorePageBase;
import com.disney.qa.disney.apple.pages.phone.AppStoreIOSPage;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = AppStorePageBase.class)
public class AppStoreTabletIOSPage extends AppStoreIOSPage {

    public AppStoreTabletIOSPage(WebDriver driver) {
        super(driver);
    }
}
