package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEnforceDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusEnforceDOBCollectionPageBase.class)
public class DisneyPlusAppleTVEnforceDOBCollectionPage extends DisneyPlusEnforceDOBCollectionPageBase {

    public DisneyPlusAppleTVEnforceDOBCollectionPage(WebDriver driver) {
        super(driver);
    }
}
