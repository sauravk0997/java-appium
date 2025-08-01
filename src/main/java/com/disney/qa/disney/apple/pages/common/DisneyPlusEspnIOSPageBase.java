package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.*;
import com.zebrunner.carina.webdriver.locator.*;
import io.appium.java_client.*;
import org.openqa.selenium.*;

public class DisneyPlusEspnIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther" +
            "[`name == \"highEmphasisView\"`]/XCUIElementTypeOther[3]/XCUIElementTypeOther/XCUIElementTypeImage")
    protected ExtendedWebElement espnBrandPage;

    @ExtendedFindBy(accessibilityId = "heroImage")
    protected ExtendedWebElement heroImage;

    public DisneyPlusEspnIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return espnBrandPage.isPresent();
    }

    public ExtendedWebElement getHeroImage() {
        return heroImage;
    }

    public boolean isPageTitlePresent(String sport) {
        return findExtendedWebElement(
                AppiumBy.iOSClassChain(String
                        .format("**/XCUIElementTypeStaticText[`label CONTAINS \"On the %s screen.\"`]",
                                sport))).isPresent();
    }
}