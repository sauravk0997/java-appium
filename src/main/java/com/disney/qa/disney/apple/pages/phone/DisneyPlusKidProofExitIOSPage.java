package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusKidProofExitIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusKidProofExitIOSPageBase.class)
public class DisneyPlusKidProofExitIOSPage extends DisneyPlusKidProofExitIOSPageBase{

    public DisneyPlusKidProofExitIOSPage(WebDriver driver) {
        super(driver);
    }
}
