package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyplusLegalIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyplusLegalIOSPageBase.class)
public class DisneyPlusLegalIOSPage extends DisneyplusLegalIOSPageBase {

    public DisneyPlusLegalIOSPage(WebDriver driver) {
        super(driver);
    }
}
