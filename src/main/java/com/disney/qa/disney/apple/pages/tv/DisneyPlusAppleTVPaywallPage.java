package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPaywallIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusPaywallIOSPageBase.class)
public class DisneyPlusAppleTVPaywallPage extends DisneyPlusPaywallIOSPageBase {

    public DisneyPlusAppleTVPaywallPage(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "paywallSubscribeStart")
    private ExtendedWebElement paywallSubscribeStart;

    @Override
    public boolean isOpened() { return paywallSubscribeStart.isElementPresent(); }
}
