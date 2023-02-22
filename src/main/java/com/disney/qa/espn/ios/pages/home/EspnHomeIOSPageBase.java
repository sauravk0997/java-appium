package com.disney.qa.espn.ios.pages.home;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;

public class EspnHomeIOSPageBase extends EspnIOSPageBase {


    //Objects

    private static final String HOME_LOGO_ACCESSIBILITY_ID = "SCClubhouseView";





    //Methods

    public EspnHomeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return findExtendedWebElement(MobileBy.AccessibilityId(HOME_LOGO_ACCESSIBILITY_ID))
                .isElementPresent(DELAY);
    }

}
