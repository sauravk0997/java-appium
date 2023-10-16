package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEnforceDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusEnforceDOBCollectionPageBase.class)
public class DisneyPlusTabletEnforceDOBCollectionIOSPage extends DisneyPlusEnforceDOBCollectionPageBase {

    public DisneyPlusTabletEnforceDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
