package com.disney.qa.espn.ios.pages.scores;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;

public class EspnScoresIOSPageBase extends EspnIOSPageBase {


    //Objects

    private static final String SCORES_TITLE_PREDICATE_LOC = "name == 'Scores' AND type == 'XCUIElementTypeOther'";




    //Methods

    public EspnScoresIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return findExtendedWebElement(MobileBy.iOSNsPredicateString(SCORES_TITLE_PREDICATE_LOC))
                .isElementPresent(DELAY);
    }
}
