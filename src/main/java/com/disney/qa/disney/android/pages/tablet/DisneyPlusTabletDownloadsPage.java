package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusDownloadsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusDownloadsPageBase.class)
public class DisneyPlusTabletDownloadsPage extends DisneyPlusDownloadsPageBase {

    public DisneyPlusTabletDownloadsPage(WebDriver driver) {
        super(driver);
    }
}
