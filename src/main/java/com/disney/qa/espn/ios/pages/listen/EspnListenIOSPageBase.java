package com.disney.qa.espn.ios.pages.listen;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;

public class EspnListenIOSPageBase extends EspnIOSPageBase {


    //Objects

    private static final String LISTEN_TITLE_PREDICATE_LOC = "name == 'Listen' AND type == 'XCUIElementTypeOther'";




    //Methods

    public EspnListenIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getDriver().findElement(MobileBy.iOSNsPredicateString(LISTEN_TITLE_PREDICATE_LOC))
                .isDisplayed();
    }
}
