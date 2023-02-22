package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusDOBCollectionPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusDOBCollectionPageBase.class)
public class DisneyPlusDOBCollectionPage extends DisneyPlusDOBCollectionPageBase {
    public DisneyPlusDOBCollectionPage(WebDriver driver) {
        super(driver);
    }
}
