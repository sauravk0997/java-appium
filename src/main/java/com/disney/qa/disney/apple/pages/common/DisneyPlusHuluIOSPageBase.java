package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.common.constant.CollectionConstant;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHuluIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "brandLandingView")
    protected ExtendedWebElement brandLandingView;

    public DisneyPlusHuluIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        ExtendedWebElement studiosLabel = staticTextByLabel.format("Studios and Networks");
        swipePageTillElementPresent(studiosLabel, 3, brandLandingView, Direction.UP, 500);
        return studiosLabel.isPresent();
    }
}