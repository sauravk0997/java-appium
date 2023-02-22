package com.disney.qa.espn.ios.pages.watch;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;

public class EspnWatchOriginalsIOSPageBase extends EspnWatchIOSPageBase {


    //Objects





    //Methods

    public EspnWatchOriginalsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return "1".equals(findExtendedWebElement(MobileBy.AccessibilityId(ORIGINALS_TAB_ACCESSIBILITY_ID))
                .getAttribute("value"));
    }

}
