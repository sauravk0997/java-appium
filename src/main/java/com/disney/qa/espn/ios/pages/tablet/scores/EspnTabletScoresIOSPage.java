package com.disney.qa.espn.ios.pages.tablet.scores;

import com.disney.qa.espn.ios.pages.phone.scores.EspnScoresIOSPage;
import com.disney.qa.espn.ios.pages.scores.EspnScoresIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnScoresIOSPageBase.class)
public class EspnTabletScoresIOSPage extends EspnScoresIOSPage {
    public EspnTabletScoresIOSPage(WebDriver driver) {
        super(driver);
    }
}
