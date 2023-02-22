package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAppLanguageIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusAppLanguageIOSPageBase.class)
public class DisneyPlusAppLanguageIOSPage extends DisneyPlusAppLanguageIOSPageBase {

    public DisneyPlusAppLanguageIOSPage(WebDriver driver) {
        super(driver);
    }
}
