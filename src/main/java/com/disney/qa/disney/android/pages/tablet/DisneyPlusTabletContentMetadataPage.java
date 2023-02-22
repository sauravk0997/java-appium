package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusContentMetadataPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusContentMetadataPageBase.class)
public class DisneyPlusTabletContentMetadataPage extends DisneyPlusContentMetadataPageBase {
    public DisneyPlusTabletContentMetadataPage(WebDriver driver) {
        super(driver);
    }
}
