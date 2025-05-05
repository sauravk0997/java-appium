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

    @Override
    public boolean isOpened() {
        return upNextContentTitleLabel.isElementPresent();
    }

    public boolean isUpNextExtraActionButtonPresent() { return upNextExtraActionButton.isElementPresent(); }

    public void clickUpNextExtraActionButton() { upNextExtraActionButton.clickIfPresent(); }

    public ExtendedWebElement getUpNextPlayButton() {
        return upNextPlayButton;
    }

    public ExtendedWebElement getUpNextContentFooterLabel() {
        return upNextContentFooterLabel;
    }

    public ExtendedWebElement getSeeAllEpisodesButton() {
        return upNextExtraActionButton;
    }

    @Override
    public boolean waitForUpNextUIToAppear() {
        return (fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 0,
                "upNext UI didn't appear on video player")
                .until(it -> upNextPlayButton.isElementPresent(THREE_HUNDRED_SEC_TIMEOUT)));
    }
}
