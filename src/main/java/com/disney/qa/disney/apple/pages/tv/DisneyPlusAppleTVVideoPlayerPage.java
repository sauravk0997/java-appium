package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVideoPlayerIOSPageBase.class)
public class DisneyPlusAppleTVVideoPlayerPage extends DisneyPlusVideoPlayerIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusAppleTVVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilDetailsPageIsLoadedFromTrailer(long timeout, int polling) {
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        fluentWait(getDriver(), timeout, polling, "Details page did not load after " + timeout)
                .until(it -> disneyPlusAppleTVDetailsPage.isOpened());
    }

    /**
     * Waits for content to end in player until Up Next End Card present.
     * Returns the object of DisneyPlusAppleTVVideoPlayerPage.
     * @param timeout
     * @param polling
     */
    public DisneyPlusAppleTVVideoPlayerPage waitForTvosContentToEnd(long timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Up Next End Card did not load after " + timeout).until(it -> isUpNextHeaderPresent());
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    /**
     * Waits for trailer to end in player until video player is not open.
     * Returns the object of DisneyPlusAppleTVVideoPlayerPage.
     * @param timeout
     * @param polling
     */
    public DisneyPlusAppleTVVideoPlayerPage waitForTvosTrailerToEnd(long timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Trailer did not end after " + timeout).until(it -> !isOpened());
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    public void pauseAndPlayVideo() {
        LOGGER.info("Pause video..");
        clickSelect();
        pause(1);
        LOGGER.info("Play video..");
        clickSelect();
    }

    @Override
    public String getTitleLabel() {
        LOGGER.info("Pause/play player to see title..");
        clickSelect();
        return titleLabel.getText();
    }

    @Override
    public ExtendedWebElement getServiceAttributionLabel() {
        fluentWait(getDriver(), TWENTY_FIVE_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Service Attribution is not visible")
                .until(it -> getStaticTextByName(SERVICE_ATTRIBUTION).isPresent(ONE_SEC_TIMEOUT));
        return getStaticTextByNameContains(SERVICE_ATTRIBUTION);
    }

    /**
     * Retrieves the remaining time on the seekbar and converts it to seconds
     * Seek bar must be visible beforehand. You can use "DisneyPlusAppleTVCommonPage" .clickDown(1) for this
     *
     * @return Playback remaining time in seconds
     */
    public int getRemainingTimeInSeconds() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(timeRemainingLabel.getBy()), FIVE_SEC_TIMEOUT);
        String[] remainingTimeParts = timeRemainingLabel.getText().replace("-", "").split(":");
        int remainingTimeInSec;

        if (remainingTimeParts.length == 3) {
            int hours = Integer.parseInt(remainingTimeParts[0]);
            int minutes = Integer.parseInt(remainingTimeParts[1]);
            int sec = Integer.parseInt(remainingTimeParts[2]);
            remainingTimeInSec = (hours * 60 * 60) + minutes * 60 + sec;
        } else if (remainingTimeParts.length == 2) {
            int minutes = Integer.parseInt(remainingTimeParts[0]);
            int sec = Integer.parseInt(remainingTimeParts[1]);
            remainingTimeInSec = minutes * 60 + sec;
        } else {
            remainingTimeInSec = Integer.parseInt(remainingTimeParts[0]);
        }
        LOGGER.info("Playback time remaining {} seconds...", remainingTimeInSec);
        return remainingTimeInSec;
    }
}
