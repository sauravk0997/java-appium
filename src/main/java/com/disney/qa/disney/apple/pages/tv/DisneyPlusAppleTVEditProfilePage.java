package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
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

    @ExtendedFindBy(accessibilityId = "autoplayToggleCell")
    protected ExtendedWebElement autoplayToggleCell;

    public ExtendedWebElement getAutoplayToggleCell() {
        return autoplayToggleCell;
    }

    public DisneyPlusAppleTVEditProfilePage(WebDriver driver) {
        super(driver);
    }
}
