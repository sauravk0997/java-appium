package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyplusLegalIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyplusLegalIOSPageBase.class)
public class DisneyPlusTabletLegalIOSPage extends DisneyplusLegalIOSPageBase {

    public DisneyPlusTabletLegalIOSPage(WebDriver driver) {
        super(driver);
    }
}
