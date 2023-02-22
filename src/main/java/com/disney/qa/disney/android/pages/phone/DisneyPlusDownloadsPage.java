package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusDownloadsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusDownloadsPageBase.class)
public class DisneyPlusDownloadsPage extends DisneyPlusDownloadsPageBase {

    public DisneyPlusDownloadsPage(WebDriver driver) {
        super(driver);
    }
}
