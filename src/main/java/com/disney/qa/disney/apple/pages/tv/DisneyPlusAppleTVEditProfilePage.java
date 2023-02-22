package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusEditProfileIOSPageBase.class)
public class DisneyPlusAppleTVEditProfilePage extends DisneyPlusEditProfileIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"Edit %s's profile.\"`]")
    private ExtendedWebElement dynamicEditProfileIcon;

    @Override
    public boolean isEditModeProfileIconPresent(String username) {
        return dynamicEditProfileIcon.format(username).isElementPresent();
    }

    public DisneyPlusAppleTVEditProfilePage(WebDriver driver) {
        super(driver);
    }
}
