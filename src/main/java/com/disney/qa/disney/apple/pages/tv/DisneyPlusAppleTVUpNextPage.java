package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpNextIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusUpNextIOSPageBase.class)
public class DisneyPlusAppleTVUpNextPage extends DisneyPlusUpNextIOSPageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "upNextContentFooterLabel")
    private ExtendedWebElement upNextContentFooterLabel;

    @ExtendedFindBy(accessibilityId = "upNextContentDescription")
    private ExtendedWebElement upNextContentDescription;

    @ExtendedFindBy(accessibilityId = "upNextContentTitleLabel")
    private ExtendedWebElement upNextContentTitleLabel;

    @ExtendedFindBy(accessibilityId = "upNextSubheaderLabel")
    private ExtendedWebElement upNextSubheaderLabel;

    @ExtendedFindBy(accessibilityId = "upNextLogoImage")
    private ExtendedWebElement upNextLogoImage;

    @ExtendedFindBy(accessibilityId = "upNextHeroImage")
    private ExtendedWebElement upNextHeroImage;

    @ExtendedFindBy(accessibilityId = "upNextExtraActionButton")
    public ExtendedWebElement upNextExtraActionButton;

    @ExtendedFindBy(accessibilityId = "upNextPlayButton")
    private ExtendedWebElement upNextPlayButton;

    //FUNCTIONS
    public DisneyPlusAppleTVUpNextPage(WebDriver driver) {
        super(driver);
    }

    public boolean isUpNextExtraActionButtonPresent() { return upNextExtraActionButton.isElementPresent(); }

    public void clickUpNextExtraActionButton() { upNextExtraActionButton.clickIfPresent(); }

    public ExtendedWebElement getUpNextPlayButton() {
        return upNextPlayButton;
    }
}
