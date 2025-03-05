package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.*;
import com.zebrunner.carina.webdriver.locator.*;
import io.appium.java_client.*;
import org.openqa.selenium.*;

public class DisneyPlusEspnIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther" +
            "[`name == \"highEmphasisView\"`]/XCUIElementTypeOther[3]/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement espnBrandPage;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"Replay\"`][1]")
    private ExtendedWebElement replayLabel;

    public DisneyPlusEspnIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return espnBrandPage.isPresent();
    }

    public ExtendedWebElement getReplayLabel() {
        return replayLabel;
    }

    public boolean isSportTitlePresent(String sport) {
        return findExtendedWebElement(
                AppiumBy.iOSClassChain(String
                        .format("**/XCUIElementTypeStaticText[`label CONTAINS \"On the %s screen.\"`]",
                                sport))).isPresent();
    }
}