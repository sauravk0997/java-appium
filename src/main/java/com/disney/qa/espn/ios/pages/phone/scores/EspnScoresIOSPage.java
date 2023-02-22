package com.disney.qa.espn.ios.pages.phone.scores;

import com.disney.qa.espn.ios.pages.scores.EspnScoresIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnScoresIOSPageBase.class)
public class EspnScoresIOSPage extends EspnScoresIOSPageBase {
    public EspnScoresIOSPage(WebDriver driver) {
        super(driver);
    }
}
