package com.disney.qa.disney.apple.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
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
