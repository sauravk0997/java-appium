package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVideoPlayerIOSPageBase.class)
public class DisneyPlusAppleTVVideoPlayerPage extends DisneyPlusVideoPlayerIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == 'Restart'`]")
    private ExtendedWebElement restartBtn;
    @ExtendedFindBy(accessibilityId = "seekTimeLabel")
    protected ExtendedWebElement seekTimeLabel;

    public DisneyPlusAppleTVVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getRestartBtn() {
        return restartBtn;
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
        clickPlay();
        return titleLabel.getText();
    }

    @Override
    public DisneyPlusVideoPlayerIOSPageBase displayVideoController() {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        LOGGER.info("Activating video player controls...");
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, FIVE_SEC_TIMEOUT, "Seek bar is present")
                .until(it -> !seekBar.isPresent(ONE_SEC_TIMEOUT));
        int attempts = 0;
        do {
            commonPage.clickDown(2);
        } while (attempts++ < 5 && !seekBar.isElementPresent(ONE_SEC_TIMEOUT));
        if (attempts == 6) {
            Assert.fail("Seek bar was present and attempts exceeded over 5.");
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public boolean isThumbnailAlignedWithTheSeekBar() {
        int seekBarLeftXCoordinate = seekBar.getLocation().getX();
        int thumbnailLeftXCoordinate = thumbnailView.getLocation().getX();

        return  seekBarLeftXCoordinate == thumbnailLeftXCoordinate;
    }

    public boolean isThumbnailAlignedWithTheEndOfTheSeekBar() {
        int seekBarRightXCoordinate = seekBar.getLocation().getX() + seekBar.getSize().getWidth();
        int thumbnailRightXCoordinate = thumbnailView.getLocation().getX() + thumbnailView.getSize().getWidth();

        return  seekBarRightXCoordinate == thumbnailRightXCoordinate;
    }

    @Override
    public int getCurrentTime() {
        String[] currentTimeTokens = seekTimeLabel.getText().split(":");
        int currentTimeInSec;
        if (currentTimeTokens.length > 2) {
            currentTimeInSec = Integer.parseInt(currentTimeTokens[0]) * 3600
                    + Integer.parseInt(currentTimeTokens[1]) * 60
                    + (Integer.parseInt(currentTimeTokens[2]));
        } else {
            currentTimeInSec = (Integer.parseInt(currentTimeTokens[0]) * 60) + (Integer.parseInt(currentTimeTokens[1]));
        }
        LOGGER.info("Playback currently at '{}' seconds", currentTimeInSec);
        return currentTimeInSec;
    }

    public DisneyPlusVideoPlayerIOSPageBase scrubToPlaybackPercentage(double playbackPercent) {
        LOGGER.info("Setting video playback to {}% completed...", playbackPercent);
        clickPlay();
        int destinationX = seekBar.getLocation().getX() +
                (int)(seekBar.getSize().getWidth() * Double.parseDouble("." + (int) Math.round(playbackPercent * 100)));
        clickDown();
        Point currentTimeMarkerLocation = seekTimeLabel.getLocation();
        LOGGER.info("Scrubbing from X:{},Y:{} to X:{},Y:{}",
                currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(),
                destinationX, currentTimeMarkerLocation.getY());
        clickRight();
        swipeLeft(currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(),
                destinationX, currentTimeMarkerLocation.getY(), 500);
        scrollFromTo(currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(),
                destinationX, currentTimeMarkerLocation.getY());
        return initPage(DisneyPlusAppleTVVideoPlayerPage.class);
    }
}
