package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEdnaDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusEdnaDOBCollectionPageBase.class)
public class DisneyPlusAppleTVEdnaDOBCollectionPage extends DisneyPlusEdnaDOBCollectionPageBase {

    public DisneyPlusAppleTVEdnaDOBCollectionPage(WebDriver driver) {
        super(driver);
    }
}
