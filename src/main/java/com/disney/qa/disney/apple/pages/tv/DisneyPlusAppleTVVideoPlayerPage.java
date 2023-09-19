package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public boolean isWatchingLivePresent() {
        return watchingLive.isPresent();
    }

    @Override
    public String getTitleLabel() {
        LOGGER.info("Pause/play player to see title..");
        clickSelect();
        return titleLabel.getText();
    }

    @Override
    public void compareWatchLiveToWatchFromStartTimeRemaining(SoftAssert sa) {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        Map<String, Integer> params = new HashMap<>();

        details.clickWatchButton();
        liveEventModal.getWatchLiveButton().click();
        sa.assertTrue(isOpened(), LIVE_VIDEO_NOT_PLAYING_ERROR_MESSAGE);
        pauseAndPlayVideo();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        List<String> timeRemaining = List.of(remainingTime);
        if (timeRemaining.size() == 3) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTime());
        }
        clickBackButton();
        sa.assertTrue(details.isOpened(), "Details page did not open");

        clickBackButton();
        details.isOpened();
        details.clickWatchButton();
        liveEventModal.getWatchFromStartButton().click();
        sa.assertTrue(isOpened(), LIVE_VIDEO_NOT_PLAYING_ERROR_MESSAGE);
        pauseAndPlayVideo();
        if (timeRemaining.size() == 3) {
            getRemainingTimeThreeIntegers();
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTime());
        }
        sa.assertTrue(params.get(WATCH_FROM_START_TIME_REMAINING) > params.get(WATCH_LIVE_TIME_REMAINING),
                "Watch from start did not return to beginning of live content.");
        params.clear();
    }

    @Override
    public void compareQAWatchLiveToWatchFromStartTimeRemaining(SoftAssert sa) {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        Map<String, Integer> params = new HashMap<>();

        liveEventModal.getQAWatchLiveButton().click();
        pause(2); //transition
        sa.assertTrue(isOpened(), "Live video is not playing");
        pauseAndPlayVideo();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        List<String> timeRemaining = List.of(remainingTime);
        if (timeRemaining.size() == 3) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTime());
        }
        clickMenuTimes(1,1);
        sa.assertTrue(details.isOpened(), "Details page did not open");

        details.clickQAWatchButton();
        liveEventModal.getQAWatchFromStartButton().click();
        pause(3); //transition
        sa.assertTrue(isOpened(), "Live video is not playing");
        pauseAndPlayVideo();
        if (timeRemaining.size() == 3) {
            getRemainingTimeThreeIntegers();
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTime());
        }
        sa.assertTrue(params.get(WATCH_FROM_START_TIME_REMAINING) > params.get(WATCH_LIVE_TIME_REMAINING),
                "Watch from start did not return to beginning of live content.");
        params.clear();
    }
}
