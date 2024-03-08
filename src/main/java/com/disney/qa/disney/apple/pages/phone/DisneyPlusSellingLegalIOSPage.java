package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyplusSellingLegalIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyplusSellingLegalIOSPageBase.class)
public class DisneyPlusSellingLegalIOSPage extends DisneyplusSellingLegalIOSPageBase {

    public DisneyPlusSellingLegalIOSPage(WebDriver driver) {
        super(driver);
    }
}
