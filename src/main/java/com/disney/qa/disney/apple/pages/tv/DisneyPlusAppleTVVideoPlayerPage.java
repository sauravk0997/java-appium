package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVideoPlayerIOSPageBase.class)
public class DisneyPlusAppleTVVideoPlayerPage extends DisneyPlusVideoPlayerIOSPageBase {

    public DisneyPlusAppleTVVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "contentRatingInfoView")
    private ExtendedWebElement contentRatingInfoView;

    @ExtendedFindBy(accessibilityId = "Watching live")
    private ExtendedWebElement watchingLive;

    public void waitUntilDetailsPageIsLoadedFromTrailer(long timeout, int polling) {
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        fluentWait(getDriver(), timeout, polling, "Details page did not load after " + timeout)
                .until(it -> disneyPlusAppleTVDetailsPage.isOpened());
    }

    public DisneyPlusAppleTVVideoPlayerPage waitForContentToEnd(long timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Up Next End Card did not load after " + timeout).until(it -> isUpNextHeaderPresent());
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    public void pauseAndPlayVideo() {
        LOGGER.info("Pause video..");
        clickSelect();
        pause(1);
        LOGGER.info("Play video..");
        clickSelect();
    }
    public boolean isWatchingLivePresent() {
        return watchingLive.isPresent();
    }

    @Override
    public String getTitleLabel() {
        LOGGER.info("Pausing playback to see video player title..");
        clickSelect();
        return titleLabel.getText();
    }
}
