package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeDOBCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class)
public class DisneyPlusTabletVerifyAgeDOBCollectionIOSPage extends DisneyPlusVerifyAgeDOBCollectionIOSPageBase {
    public DisneyPlusTabletVerifyAgeDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
