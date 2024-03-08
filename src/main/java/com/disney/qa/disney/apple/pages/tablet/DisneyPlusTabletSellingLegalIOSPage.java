package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyplusSellingLegalIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyplusSellingLegalIOSPageBase.class)
public class DisneyPlusTabletSellingLegalIOSPage extends DisneyplusSellingLegalIOSPageBase {

    public DisneyPlusTabletSellingLegalIOSPage(WebDriver driver) {
        super(driver);
    }
}
