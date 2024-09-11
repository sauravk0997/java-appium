package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusKidProofExitIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusHuluIOSPageBase.class)
public class DisneyPlusTabletKidProofExitIOSPage extends DisneyPlusKidProofExitIOSPageBase {

    public DisneyPlusTabletKidProofExitIOSPage(WebDriver driver) {
        super(driver);
    }
}
