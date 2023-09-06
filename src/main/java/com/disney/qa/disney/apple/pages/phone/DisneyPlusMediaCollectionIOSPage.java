package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusMediaCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusMediaCollectionIOSPageBase.class)
public class DisneyPlusMediaCollectionIOSPage extends DisneyPlusMediaCollectionIOSPageBase {
    public DisneyPlusMediaCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
