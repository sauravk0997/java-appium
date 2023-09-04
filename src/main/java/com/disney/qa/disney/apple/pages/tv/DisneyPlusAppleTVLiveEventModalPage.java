package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLiveEventModalIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusAppleTVLiveEventModalPage extends DisneyPlusLiveEventModalIOSPageBase {

    @ExtendedFindBy(accessibilityId = "subheadlineLabel")
    private ExtendedWebElement subheadLineLabel;

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

    public boolean isSubheadLineLabelPresent() { return subheadLineLabel.isPresent(); }

    public void clickDetailsButton() {
        detailsButton.click();
    }
}
