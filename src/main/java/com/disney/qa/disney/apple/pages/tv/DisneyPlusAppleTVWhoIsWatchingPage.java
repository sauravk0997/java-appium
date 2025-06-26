package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusWhoseWatchingIOSPageBase.class)
public class DisneyPlusAppleTVWhoIsWatchingPage extends DisneyPlusWhoseWatchingIOSPageBase {

    public DisneyPlusAppleTVWhoIsWatchingPage(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "profileSelection")
    private ExtendedWebElement profileSelection;

    @Override
    public boolean isOpened() {
        boolean isOpened = profileSelection.isElementPresent();
        return isOpened;
    }
}
