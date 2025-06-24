package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusBrandIOSPageBase.class)
public class DisneyPlusAppleTVBrandsPage extends DisneyPlusBrandIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"highEmphasisView\"`]/XCUIElementTypeImage")
    private ExtendedWebElement brandLogoImage;

    public DisneyPlusAppleTVBrandsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public ExtendedWebElement getBrandLogoImage() {
        return brandLogoImage;
    }
}
