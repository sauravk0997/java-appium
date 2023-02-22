package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusDetailsIOSPageBase.class)
public class DisneyPlusAppleTVDetailsPage extends DisneyPlusDetailsIOSPageBase {

    @ExtendedFindBy(accessibilityId = "trailerButton")
    private ExtendedWebElement trailerButton;

    @ExtendedFindBy(accessibilityId = "contentSummaryView")
    private ExtendedWebElement contentSummaryView;

    public DisneyPlusAppleTVDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = DisneyPlusAppleTVCommonPage.isProd() ? logoImage.isElementPresent() : playButton.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isLogoPresent() {
        return logoImage.isElementPresent();
    }

    public boolean isTrailerButtonPresent() {
        return trailerButton.isElementPresent();
    }

    public boolean isContentSummaryView() { return contentSummaryView.isElementPresent(); }
    public boolean isBriefDescriptionPresent(String text) {
        ExtendedWebElement briefDesc = getDynamicAccessibilityId(text);
        return briefDesc.isElementPresent() && briefDesc.getText().chars().count() <= 120;
    }

    public void clickTrailerButton() {
        trailerButton.click();
    }

    public boolean isAnthologyTitlePresent() {
        return getStaticTextByLabel("Dancing with the Stars").isElementPresent();
    }

    @Override
    public DisneyPlusAppleTVVideoPlayerPage clickWatchButton() {
        watchButton.click();
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    @Override
    public boolean compareEpisodeNum() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        isOpened();
        moveDown(2,1);
        String currentEpisodeNum = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        moveUp(2,2);
        clickWatchButton();
        new DisneyPlusAppleTVLiveEventModalPage(getDriver()).clickWatchLiveButton();
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.waitForContentToEnd(350, 20); //Enters playback ~5 min till end
        clickMenuTimes(1,1);
        isOpened();
        moveDown(2,1);
        String recentlyPlayedEpisode = getParsedString(getDynamicXpathContainsName(titleLabel.toString()), "0", ". ");
        return currentEpisodeNum.contains(recentlyPlayedEpisode);
    }
}
