package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPaywallIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "restoreButton")
    public ExtendedWebElement restoreBtn;

    public DisneyPlusPaywallIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return restoreBtn.isPresent();
    }
}
