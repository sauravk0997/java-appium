package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOriginalsIOSPageBase.class)
public class DisneyPlusAppleTVOriginalsPage extends DisneyPlusOriginalsIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"Shorts\"`]")
    private ExtendedWebElement shorts;

    public DisneyPlusAppleTVOriginalsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return DisneyPlusAppleTVCommonPage.isProd() ? shorts.isElementPresent() : getStaticTextByLabelContains("On the Originals screen.").isPresent();
    }
}
