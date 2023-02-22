package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusPaywallPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusPaywallPageBase.class)
public class DisneyPlusPaywallPage extends DisneyPlusPaywallPageBase{

    public DisneyPlusPaywallPage(WebDriver driver){
        super(driver);
    }
}
