package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

public class DisneyPlusHuluIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusHuluIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "182f4b9d-2a66-4243-8056-bb2687c18bdc")
    private ExtendedWebElement networkCollectionView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"ABC, Select for details on this title.\"`]")
    private ExtendedWebElement networkCollection;

    @Override
    public boolean isOpened() {
        return networkCollection.isPresent();
    }
}