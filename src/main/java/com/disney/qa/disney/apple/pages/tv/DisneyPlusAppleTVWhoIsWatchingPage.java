package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
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
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isOpened;
    }
}
