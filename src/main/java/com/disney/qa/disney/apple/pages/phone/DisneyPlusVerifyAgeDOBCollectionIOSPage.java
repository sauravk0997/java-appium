package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeDOBCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class)
public class DisneyPlusVerifyAgeDOBCollectionIOSPage extends DisneyPlusVerifyAgeDOBCollectionIOSPageBase {
    public DisneyPlusVerifyAgeDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
