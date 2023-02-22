package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusMoreMenuPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusMoreMenuPageBase.class)
public class DisneyPlusTabletMoreMenuPage extends DisneyPlusMoreMenuPageBase {

    public DisneyPlusTabletMoreMenuPage(WebDriver driver) {
        super(driver);
    }
}
