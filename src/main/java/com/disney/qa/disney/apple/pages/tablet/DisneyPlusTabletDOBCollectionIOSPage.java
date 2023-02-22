package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDOBCollectionPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusDOBCollectionPageBase.class)
public class DisneyPlusTabletDOBCollectionIOSPage extends DisneyPlusDOBCollectionPageBase {
    public DisneyPlusTabletDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
