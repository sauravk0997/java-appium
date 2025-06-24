package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVerifyAgeDOBCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class)
public class DisneyPlusAppleTVVerifyAgeDOBCollectionPage extends DisneyPlusVerifyAgeDOBCollectionIOSPageBase {

    public DisneyPlusAppleTVVerifyAgeDOBCollectionPage(WebDriver driver) {
        super(driver);
    }

}