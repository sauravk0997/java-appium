package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusDOBCollectionPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusDOBCollectionPageBase.class)
public class DisneyPlusTabletDOBCollectionPage extends DisneyPlusDOBCollectionPageBase {
    public DisneyPlusTabletDOBCollectionPage(WebDriver driver) {
        super(driver);
    }
}
