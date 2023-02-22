package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusContentMetadataPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusContentMetadataPageBase.class)
public class DisneyPlusContentMetadataPage extends DisneyPlusContentMetadataPageBase {
    public DisneyPlusContentMetadataPage(WebDriver driver) {
        super(driver);
    }
}
