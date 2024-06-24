package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEdnaDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusEdnaDOBCollectionPageBase.class)
public class DisneyPlusTabletEdnaDOBCollectionIOSPage extends DisneyPlusEdnaDOBCollectionPageBase {

    public DisneyPlusTabletEdnaDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
