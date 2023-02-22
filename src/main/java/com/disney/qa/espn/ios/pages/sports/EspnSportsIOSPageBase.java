package com.disney.qa.espn.ios.pages.sports;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;

public class EspnSportsIOSPageBase extends EspnIOSPageBase {


    //Objects

    private static final String SPORTS_TITLE_PREDICATE_LOCATOR = "name == 'Sports' AND type == 'XCUIElementTypeOther'";




    //Methods

    public EspnSportsIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getDriver().findElement(MobileBy.iOSNsPredicateString(SPORTS_TITLE_PREDICATE_LOCATOR))
                .isDisplayed();
    }
}
