package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusDOBCollectionPageBase.class)
public class DisneyPlusDOBCollectionIOSPage extends DisneyPlusDOBCollectionPageBase {
    public DisneyPlusDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
