package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.common.DisneyPlusPaywallPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusPaywallPageBase.class)
public class DisneyPlusTabletPaywallPage extends DisneyPlusPaywallPageBase{

    public DisneyPlusTabletPaywallPage(WebDriver driver){
        super(driver);
    }
}
