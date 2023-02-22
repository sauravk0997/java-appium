package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCompletePurchaseIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
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
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isCompleteSubBtnFocused() {
        boolean isFocused = isFocused(completeSubscriptionBtn);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isFocused;
    }
}
