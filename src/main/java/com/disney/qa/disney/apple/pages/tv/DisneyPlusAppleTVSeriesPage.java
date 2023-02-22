package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVSeriesPage extends DisneyPlusApplePageBase {
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Featured\"`][1]")
    private ExtendedWebElement featuredBtn;

    public DisneyPlusAppleTVSeriesPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return featuredBtn.isElementPresent();
    }
}
