package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusMoreMenuPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusMoreMenuPageBase.class)
public class DisneyPlusMoreMenuPage extends DisneyPlusMoreMenuPageBase {

    public DisneyPlusMoreMenuPage(WebDriver driver) {
        super(driver);
    }
}
