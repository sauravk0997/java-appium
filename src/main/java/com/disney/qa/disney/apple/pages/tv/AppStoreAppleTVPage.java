package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.AppStorePageBase;
import com.disney.qa.disney.apple.pages.phone.AppStoreIOSPage;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = AppStorePageBase.class)
public class AppStoreAppleTVPage extends AppStoreIOSPage {

    public AppStoreAppleTVPage(WebDriver driver) {
        super(driver);
    }
}
