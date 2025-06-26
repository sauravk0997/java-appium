package com.disney.qa.disney.apple.pages.tv;

import com.disney.config.DisneyParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCompletePurchaseIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusCompletePurchaseIOSPageBase.class)
public class DisneyPlusAppleTVCompletePurchasePage extends DisneyPlusCompletePurchaseIOSPageBase {

    @ExtendedFindBy(accessibilityId = "restoreButton")
    private ExtendedWebElement restorePurchaseBtn;

    public DisneyPlusAppleTVCompletePurchasePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = restorePurchaseBtn.isElementPresent();
        return isPresent;
    }
}
