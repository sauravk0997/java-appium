package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOneTrustConsentBannerIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusOneTrustConsentBannerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "bannerRejectAllButton")
    protected ExtendedWebElement rejectAllButton;

    public void tapRejectAllButton() {
        rejectAllButton.clickIfPresent();
    }

}
