package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLiveEventModalIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusAppleTVLiveEventModalPage extends DisneyPlusLiveEventModalIOSPageBase {

    @ExtendedFindBy(accessibilityId = "watchLiveButton")
    private ExtendedWebElement watchLiveButton;

    @ExtendedFindBy(accessibilityId = "watchFromStartButton")
    private ExtendedWebElement watchFromStartButton;

    @ExtendedFindBy(accessibilityId = "detailsButton")
    private ExtendedWebElement detailsButton;

    public DisneyPlusAppleTVLiveEventModalPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = watchLiveButton.isPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void clickWatchLiveButton() {
        watchLiveButton.clickIfPresent();
    }

    public boolean isWatchLiveButtonPresent() {
        return watchLiveButton.isElementPresent();
    }

    public boolean isWatchFromStartPresent() {
        return watchFromStartButton.isElementPresent();
    }

    public void clickDetailsButton() {
        detailsButton.click();
    }
}
