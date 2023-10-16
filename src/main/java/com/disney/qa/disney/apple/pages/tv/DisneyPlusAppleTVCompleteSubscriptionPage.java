package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCompletePurchaseIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusCompletePurchaseIOSPageBase.class)
public class DisneyPlusAppleTVCompleteSubscriptionPage extends DisneyPlusCompletePurchaseIOSPageBase {
    public DisneyPlusAppleTVCompleteSubscriptionPage(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == 'customButton'`]")
    private ExtendedWebElement completeSubscriptionBtn;

    @Override
    public boolean isOpened() {
        boolean isPresent = completeSubscriptionBtn.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isCompleteSubBtnFocused() {
        boolean isFocused = isFocused(completeSubscriptionBtn);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isFocused;
    }
}
