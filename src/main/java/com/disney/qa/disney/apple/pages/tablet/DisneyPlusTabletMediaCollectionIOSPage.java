package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusMediaCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusMediaCollectionIOSPageBase.class)
public class DisneyPlusTabletMediaCollectionIOSPage extends DisneyPlusMediaCollectionIOSPageBase {
    public DisneyPlusTabletMediaCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
