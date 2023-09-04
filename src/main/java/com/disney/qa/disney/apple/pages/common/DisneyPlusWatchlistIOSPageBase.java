package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusWatchlistIOSPageBase extends DisneyPlusApplePageBase{

    @ExtendedFindBy(accessibilityId = "badgeContentView")
    protected ExtendedWebElement badgeContentView;

    public ExtendedWebElement getBadgeContentView() {
        return badgeContentView;
    }

    public DisneyPlusWatchlistIOSPageBase(WebDriver driver) {
        super(driver);
    }
}
