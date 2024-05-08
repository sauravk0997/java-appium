package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVideoPlayerIOSPageBase extends DisneyPlusApplePageBase {
    protected static final String WATCH_LIVE_TIME_REMAINING = "watchLiveTimeRemaining";
    protected static final String WATCH_FROM_START_TIME_REMAINING = "watchFromStartTimeRemaining";
    protected static final String LIVE_VIDEO_NOT_PLAYING_ERROR_MESSAGE = "Live video is not playing";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "ucp.playerView")
    protected ExtendedWebElement playerView;

    @ExtendedFindBy(accessibilityId = "ucp.currentPosition")
    protected ExtendedWebElement seekBar;

    @ExtendedFindBy(accessibilityId = "currentTimeMarker")
    protected ExtendedWebElement currentTimeMarker;

    @ExtendedFindBy(accessibilityId = "ucp.durationLabel")
    protected ExtendedWebElement timeRemainingLabel;
    @FindBy(name = "titleLabel")
    protected ExtendedWebElement titleLabel;
    @ExtendedFindBy(accessibilityId = "ucp.currentTimeLabel")
    private ExtendedWebElement currentTimeLabel;
    @ExtendedFindBy(accessibilityId = "ucp.fastRewind")
    private ExtendedWebElement rewindButton;
    @ExtendedFindBy(accessibilityId = "ucp.fastForward")
    private ExtendedWebElement forwardButton;
    @FindBy(xpath = "//*[@name='ucp.playerView']/following-sibling::*//XCUIElementTypeImage")
    private ExtendedWebElement ucpLoadSpinner;
    @ExtendedFindBy(accessibilityId = "audioSubtitleMenuButton")
    private ExtendedWebElement audioSubtitleMenuButton;
    @ExtendedFindBy(accessibilityId = "GCKUICastButton")
    private ExtendedWebElement chromecastButton;
    @ExtendedFindBy(accessibilityId = "ucp.audioOutput")
    private ExtendedWebElement airplayButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \"%s\"`]")
    private ExtendedWebElement currentlyPlayingTitle;
    @ExtendedFindBy(accessibilityId = "brandImageView")
    private ExtendedWebElement brandImageView;
    @ExtendedFindBy(accessibilityId = "advisoryLabel")
    private ExtendedWebElement advisoryLabel;
    @ExtendedFindBy(accessibilityId = "skipIntroButton")
    private ExtendedWebElement skipIntroButton;
    @ExtendedFindBy(accessibilityId = "restartButton")
    private ExtendedWebElement restartButton;
    @ExtendedFindBy(accessibilityId = "youAreLiveButton")
    private ExtendedWebElement youAreLiveButton;
    @FindBy(name = "subtitleLabel")
    private ExtendedWebElement subtitleLabel;

    @FindBy(name = "serviceAttributionLabel")
    private ExtendedWebElement serviceAttributionLabel;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"%s\"`]/XCUIElementTypeImage")
    private ExtendedWebElement networkWatermarkLogo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$type == 'XCUIElementTypeStaticText' and label = 'Ad'$]/XCUIElementTypeOther[12]/XCUIElementTypeStaticText")
    protected ExtendedWebElement adRemainingTime;

    //FUNCTIONS

    public DisneyPlusVideoPlayerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return playerView.isPresent();
    }

    public ExtendedWebElement getPlayButton() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PLAY.getText()));
    }

    public ExtendedWebElement getPauseButton() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PAUSE.getText()));
    }

    public ExtendedWebElement getBackButton() {
        return dynamicBtnFindByName.format("buttonBack");
    }

    public ExtendedWebElement getElementFor(PlayerControl control) {
        switch (control) {
            case AIRPLAY:
                return airplayButton;
            case AUDIO_SUBTITLE_BUTTON:
                return audioSubtitleMenuButton;
            case BACK:
                return getBackButton();
            case CHROMECAST:
                return chromecastButton;
            case FAST_FORWARD:
                return forwardButton;
            case PLAY:
                return getPlayButton();
            case PAUSE:
                return getPauseButton();
            case RESTART:
                return restartButton;
            case REWIND:
                return rewindButton;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is an invalid player control", control));
        }
    }

    public boolean isElementPresent(PlayerControl control) {
        displayVideoController();
        return getElementFor(control).isElementPresent();
    }

    public boolean isTitleLabelVisible() {
        displayVideoController();
        return titleLabel.isElementPresent();
    }

    public boolean isSeekbarVisible() {
        return seekBar.isPresent();
    }

    public boolean isServiceAttributionLabelVisible() {
        return (fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 0, "Service attribution didn't appear on video player")
                .until(it -> serviceAttributionLabel.isPresent(LONG_TIMEOUT)));
    }

    public boolean isServiceAttributionLabelVisibleWithControls() {
        displayVideoController();
        return serviceAttributionLabel.isPresent();
    }

    public boolean isCurrentTimeLabelVisible() {
        displayVideoController();
        return currentTimeLabel.isPresent();
    }

    public boolean isRemainingTimeLabelVisible() {
        displayVideoController();
        return timeRemainingLabel.isPresent();
    }

    public boolean verifyVideoPaused() {
        displayVideoController();
        return getPlayButton().isElementPresent();
    }

    public DisneyPlusVideoPlayerIOSPageBase waitForVideoToStart() {
        LOGGER.info("Waiting for video buffering to complete...");
        waitUntil(ExpectedConditions.visibilityOfElementLocated(ucpLoadSpinner.getBy()), 30);
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(ucpLoadSpinner.getBy()), 30);
        LOGGER.info("Buffering completed.");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase displayVideoController() {
        LOGGER.info("Activating video player controls...");
        //Check is due to placement of PlayPause, which will pause the video if clicked
        Dimension size = getDriver().manage().window().getSize();
        tapAtCoordinateNoOfTimes((size.width * 35), (size.height * 50), 1);
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, HALF_TIMEOUT, "Seek bar is present").until(it -> !seekBar.isPresent(ONE_SEC_TIMEOUT));
        int attempts = 0;
        do {
            clickElementAtLocation(playerView, 35, 50);
        } while (attempts++ < 5 && !seekBar.isElementPresent(SHORT_TIMEOUT));
        if (attempts == 6) {
            Assert.fail("Seek bar was present and attempts exceeded over 5.");
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickPauseButton() {
        displayVideoController();
        getPauseButton().click();
        LOGGER.info("Pause button on player view clicked");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickPlayButton() {
        //TODO: work around due to bug IOS-6425
        if (!getPlayButton().isElementPresent()) {
            displayVideoController();
        }
        getPlayButton().click();
        LOGGER.info("Play button on player view clicked");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusDetailsIOSPageBase clickBackButton() {
        displayVideoController();
        getBackButton().click();
        return initPage(DisneyPlusDetailsIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase tapRewindButton(int times) {
        displayVideoController();
        while (times > 0) {
            rewindButton.click();
            times--;
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase tapForwardButton(int times) {
        displayVideoController();
        while (times > 0) {
            forwardButton.click();
            times--;
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase tapPlayerScreen(PlayerControl control, int times) {
        Dimension dimension = playerView.getSize();
        if (dimension == null) {
            throw new NullPointerException("Player dimensions returned null");
        }
        int x = dimension.getWidth();
        int y = dimension.getHeight();
        int xOffset = 0;
        switch (control) {
            case REWIND:
                xOffset = (int) (x - Math.round(x * 0.8));
                break;
            case FAST_FORWARD:
                xOffset = (int) (x - Math.round(x * 0.2));
                break;
            default:
                throw new IllegalArgumentException("Undefined player action");
        }
        int yOffset = y / 2;
        tapAtCoordinateNoOfTimes(xOffset, yOffset, times);
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusDetailsIOSPageBase tapTitleOnPlayer() {
        displayVideoController();
        titleLabel.click();
        return initPage(DisneyPlusDetailsIOSPageBase.class);
    }

    /**
     * Scrubs on the seek bar to the given percentage. Returns the object of
     * DisneyPlusVideoPlayerIOSPageBase.
     *
     * @param playbackPercent
     */
    public DisneyPlusVideoPlayerIOSPageBase scrubToPlaybackPercentage(double playbackPercent) {
        LOGGER.info("Setting video playback to {}% completed...", playbackPercent);
        displayVideoController();
        Point currentTimeMarkerLocation = currentTimeMarker.getLocation();
        int seekBarWidth = seekBar.getSize().getWidth();
        int destinationX = (int) (seekBarWidth * Double.parseDouble("." + (int) Math.round(playbackPercent * 100)));
        displayVideoController();
        dragAndDropElement(currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(), destinationX, currentTimeMarkerLocation.getY(), 3);
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    /**
     * Scrubs on the seek bar to the given percentage for playback with ads. Returns the object of
     * DisneyPlusVideoPlayerIOSPageBase.
     *
     * @param playbackPercent
     */
    public DisneyPlusVideoPlayerIOSPageBase scrubPlaybackWithAdsPercentage(double playbackPercent) {
        LOGGER.info("Setting video playback to {}% completed..", playbackPercent);
        displayVideoController();
        Point currentTimeMarkerLocation = currentTimeMarker.getLocation();
        int seekBarWidth = seekBar.getSize().getWidth();
        int destinationX = (int) (seekBarWidth * Double.parseDouble("." + (int) Math.round(playbackPercent * 100)));
        displayVideoController();
        scrollFromTo(currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(), destinationX, currentTimeMarkerLocation.getY());
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    /**
     * Verifies if the given episode title is playing
     *
     * @param episodeName
     */

    public boolean doesTitleExists(String episodeName) {
        waitForVideoToStart();
        displayVideoController();
        return currentlyPlayingTitle.format(episodeName).isPresent();
    }

    public void seekOnPlayer() {
        pause(5);
        displayVideoController();
        longPressAndHoldElement(currentTimeMarker, 20);
    }

    public int getCurrentPositionOnPlayer() {
        displayVideoController();
        return currentTimeMarker.getLocation().getX();
    }

    /**
     * Opens the player overlay, reads remaining time on the seekbar
     * and converts it to seconds
     *
     * @return Playback remaining time in seconds
     */
    public int getRemainingTime() {
        displayVideoController();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        int remainingTimeInSec = (Integer.parseInt(remainingTime[0]) * -60) + (Integer.parseInt(remainingTime[1]));
        LOGGER.info("Playback time remaining {} seconds...", remainingTimeInSec);
        return remainingTimeInSec;
    }

    public void tapAudioSubtitleMenu() {
        fluentWait(getDriver(), LONG_TIMEOUT, HALF_TIMEOUT, "subtitle menu overlay didn't open")
                .until(it -> {
                    displayVideoController();
                    audioSubtitleMenuButton.click();
                    return initPage(DisneyPlusAudioSubtitleIOSPageBase.class).isOpened();
                });
    }

    public boolean isSkipIntroButtonPresent() {
        return skipIntroButton.isElementPresent();
    }

    public boolean isYouAreLiveButtonPresent() {
        displayVideoController();
        return youAreLiveButton.isElementPresent();
    }

    /**
     * Waits for content to end in player until getRemainingTime isn't greater than 0 and polling
     * Returns the object of DisneyPlusVideoPlayerIOSPageBase.
     *
     * @param timeout
     * @param polling
     */
    public DisneyPlusVideoPlayerIOSPageBase waitForContentToEnd(int timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Content did not end after " + timeout).until(it -> getRemainingTime() == 0);
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    /**
     * Waits for trailer to end in player until video player is not open.
     * Returns the object of DisneyPlusVideoPlayerIOSPageBase.
     *
     * @param timeout
     * @param polling
     */
    public DisneyPlusVideoPlayerIOSPageBase waitForTrailerToEnd(int timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Trailer did not end after " + timeout).until(it -> !isOpened());
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public String getSubTitleLabel() {
        displayVideoController();
        return subtitleLabel.getText();
    }

    public String getTitleLabel() {
        displayVideoController();
        return titleLabel.getText();
    }

    public int getX() {
        Dimension dimension = playerView.getSize();
        return dimension.getWidth();
    }

    public int getY() {
        Dimension dimension = playerView.getSize();
        return dimension.getHeight();
    }

    public boolean isAdBadgeLabelPresent(int...timeout) {
        int waitTime = 10;
        if (timeout.length > 0) {
            waitTime = timeout[0];
        }
        String adLabel = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.AD_BADGE_LABEL.getText());
        return getDynamicAccessibilityId(adLabel).isPresent(waitTime);
    }

    /**
     * Opens the player overlay, reads remaining time that has 3 integers
     * (hours, minutes, seconds) on the seekbar and converts it to seconds
     *
     * @return Playback remaining time in seconds
     */

    public int getRemainingTimeThreeIntegers() {
        displayVideoController();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        int remainingTimeInSec = (Integer.parseInt(remainingTime[0]) * -60) * 60 + Integer.parseInt(remainingTime[1]) * 60 + (Integer.parseInt(remainingTime[2]));
        LOGGER.info("Playback time remaining {} seconds...", remainingTimeInSec);
        return remainingTimeInSec;
    }

    public void compareWatchLiveToWatchFromStartTimeRemaining(SoftAssert sa) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModalPage = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        Map<String, Integer> params = new HashMap<>();

        liveEventModalPage.getWatchLiveButton().click();
        sa.assertTrue(isOpened(), LIVE_VIDEO_NOT_PLAYING_ERROR_MESSAGE);
        displayVideoController();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        List<String> timeRemaining = List.of(remainingTime);
        if (timeRemaining.size() == 3) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTime());
        }
        clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");

        detailsPage.clickWatchButton();
        liveEventModalPage.getWatchFromStartButton().click();
        sa.assertTrue(isOpened(), LIVE_VIDEO_NOT_PLAYING_ERROR_MESSAGE);
        if (timeRemaining.size() == 3) {
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTime());
        }
        sa.assertTrue(params.get(WATCH_FROM_START_TIME_REMAINING) > params.get(WATCH_LIVE_TIME_REMAINING),
                "Watch from start did not return to beginning of live content.");
        params.clear();
    }

    /**
     * Below are QA env specific methods for DWTS Anthology.
     * To be deprecated when DWTS Test Streams no longer available on QA env (QAA-12244).
     */
    public void compareQAWatchLiveToWatchFromStartTimeRemaining(SoftAssert sa) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModalPage = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        Map<String, Integer> params = new HashMap<>();

        liveEventModalPage.getQAWatchLiveButton().click();
        pause(10); //transition
        displayVideoController();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        List<String> timeRemaining = List.of(remainingTime);
        if (timeRemaining.size() == 3) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_LIVE_TIME_REMAINING, getRemainingTime());
        }
        clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");

        detailsPage.clickQAWatchButton();
        liveEventModalPage.getQAWatchFromStartButton().click();
        pause(10); //transition
        if (timeRemaining.size() == 3) {
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTimeThreeIntegers());
        } else if (timeRemaining.size() == 2) {
            params.put(WATCH_FROM_START_TIME_REMAINING, getRemainingTime());
        }
        sa.assertTrue(params.get(WATCH_FROM_START_TIME_REMAINING) > params.get(WATCH_LIVE_TIME_REMAINING),
                "Watch from start did not return to beginning of live content.");
        params.clear();
    }

    public ExtendedWebElement getNetworkWatermarkLogo(String network) {
        return format(networkWatermarkLogo, network);
    }

    public boolean isNetworkWatermarkLogoPresent(String network) {
        return getNetworkWatermarkLogo(network).isElementPresent();
    }

    public boolean isNetworkWatermarkIsNotLogoPresent(String network) {
        return getNetworkWatermarkLogo(network).isElementNotPresent(2);
    }

    public ExtendedWebElement getPlayerView() {
        return playerView;
    }

    public DisneyPlusVideoPlayerIOSPageBase validateResumeTimeRemaining(SoftAssert sa) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        scrubToPlaybackPercentage(30);
        int scrubbedTimeRemaining = getRemainingTime();
        clickBackButton();
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present after exiting video player.");
        detailsPage.clickContinueButton();
        waitForVideoToStart();
        sa.assertTrue(scrubbedTimeRemaining > getRemainingTime(),
                "Returned to play-head position before scrubbed to 30% completed, resume did not work.");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase validateResumeTimeThreeIntegerRemaining(SoftAssert sa) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        scrubToPlaybackPercentage(30);
        int scrubbedTimeRemaining = getRemainingTimeThreeIntegers();
        clickBackButton();
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button is not present after exiting video player.");
        detailsPage.clickContinueButton();
        waitForVideoToStart();
        sa.assertTrue(scrubbedTimeRemaining > getRemainingTimeThreeIntegers(),
                "Returned to play-head position before scrubbed to 30% completed, resume did not work.");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase verifyVideoPlaying(SoftAssert sa) {
        int previousTimeRemaining = getRemainingTime();
        pause(10);
        sa.assertTrue(previousTimeRemaining > getRemainingTime(),
                "Video is not playing, new time remaining is not less than previous time remaining");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase verifyThreeIntegerVideoPlaying(SoftAssert sa) {
        int previousTimeRemaining = getRemainingTimeThreeIntegers();
        pause(10);
        sa.assertTrue(previousTimeRemaining > getRemainingTimeThreeIntegers(),
                "Video is not playing, new time remaining is not less than previous time remaining");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public int getBeginningTime() {
        displayVideoController();
        String[] currentTime = currentTimeLabel.getText().split(":");
        int currentTimeInSec = Integer.parseInt(currentTime[1]);
        LOGGER.info("Playback currently at {} seconds...", currentTimeInSec);
        return currentTimeInSec;
    }

    public int getCurrentTime() {
        displayVideoController();
        String[] currentTime = currentTimeLabel.getText().split(":");
        int currentTimeInSec = 0;
        if (currentTime.length > 2) {
            currentTimeInSec = (Integer.parseInt(currentTime[0]) * 60) * 60 + Integer.parseInt(currentTime[1]) * 60 + (Integer.parseInt(currentTime[2]));
        } else {
            currentTimeInSec = (Integer.parseInt(currentTime[0]) * 60) + (Integer.parseInt(currentTime[1]));
        }
        LOGGER.info("Playback currently at {} seconds...", currentTimeInSec);
        return currentTimeInSec;
    }

    public DisneyPlusVideoPlayerIOSPageBase verifyVideoPlayingFromBeginning(SoftAssert sa) {
        sa.assertTrue(getBeginningTime() < 60,
                "Video is not playing from the beginning.");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public String getRemainingTimeInStringWithHourAndMinutes() {
        int remainingTimeInMinutes = getRemainingTime();
        long hours = remainingTimeInMinutes / 60;
        long minutes = remainingTimeInMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    public String getRestartButtonStatus() {
        displayVideoController();
        return restartButton.getAttribute(Attributes.ENABLED.getAttribute());
    }

    public void clickRestartButton() {
        displayVideoController();
        restartButton.click();
    }

    public DisneyPlusVideoPlayerIOSPageBase waitForAdToComplete(int timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Ad did not end after " + timeout).until(it -> !isAdBadgeLabelPresent());
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public int getAdRemainingTimeInSeconds() {
        displayVideoController();
        String adTime = isAdBadgeLabelPresent()?adRemainingTime.getText():"0:00";
        String[] remainingTime = adTime.split(":");
        int remainingTimeInSec = (Integer.parseInt(remainingTime[0]) * -60) + (Integer.parseInt(remainingTime[1]));
        LOGGER.info("Ad Playback time remaining {} seconds...", remainingTimeInSec);
        return remainingTimeInSec;
    }

    public void waitForAdToCompleteIfPresent(int polling) {
        ExtendedWebElement adTimeBadge = staticTextLabelContains.format(":");
        int remainingTime;
        if (isAdBadgeLabelPresent() && adTimeBadge.isPresent()) {
            String[] adTime = adTimeBadge.getText().split(":");
            remainingTime = (Integer.parseInt(adTime[0]) * 60) + (Integer.parseInt(adTime[1]));
            LOGGER.info("Ad Playback time remaining {} seconds...", remainingTime);
            fluentWait(getDriver(), remainingTime, polling, "Ad did not end after " + remainingTime).until(it -> !isAdBadgeLabelPresent());
        } else {
            LOGGER.info("No ad time badge detected, continuing with test..");
        }
    }

    public boolean isAdBadgeLabelPresentWhenControlDisplay() {
        String adLabel = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.AD_BADGE_LABEL.getText());
        displayVideoController();
        return getDynamicAccessibilityId(adLabel).isElementPresent();
    }

    public boolean isRemainingTimeVisibleInCorrectFormat() {
        displayVideoController();
        return validateTimeFormat(timeRemainingLabel.getText().replace("-", ""));
    }

    public boolean isCurrentTimeVisibleInCorrectFormat() {
        displayVideoController();
        return validateTimeFormat(currentTimeLabel.getText());
    }

    public boolean validateTimeFormat(String time) {
        Pattern timePatternInHHMMSS = Pattern.compile("^([0-1][\\d]|2[0-3]):[0-5][\\d]:[0-5][\\d]$");
        Pattern timePatternInHMMSS = Pattern.compile("^[\\d]:[0-5][\\d]:[0-5][\\d]$");
        Pattern timePatternInMMSS = Pattern.compile("^[0-5][\\d]:[0-5][\\d]$");
        Pattern timePatternInMSS = Pattern.compile("^[\\d]:[0-5][\\d]$");
        if (timePatternInHHMMSS.matcher(time).matches()) {
            LOGGER.info("Content time is displayed HH:MM:SS format");
            return true;
        } else if (timePatternInHMMSS.matcher(time).matches()) {
            LOGGER.info("Content time is displayed H:MM:SS format");
            return true;
        } else if (timePatternInMMSS.matcher(time).matches()) {
            LOGGER.info("Content time is displayed in MM:SS format");
            return true;
        } else if (timePatternInMSS.matcher(time).matches()) {
            LOGGER.info("Content time is displayed in M:SS format");
            return true;
        } else {
            LOGGER.info("Content time is not displayed in correct format");
            return false;
        }
    }

    /**
     * To verify Playhead represents current time with respect to the total length of the video,
     * we are scruubing playhead to 50% and verifying with Half of seekbar width plus/minus 20
     */
    public boolean verifyPlayheadRepresentsCurrentPointOfTime() {
        displayVideoController();
        int seekBarWidth = seekBar.getSize().getWidth();

        scrubToPlaybackPercentage(50);
        waitForVideoToStart();
        int currentPositionOnSeekPlayerAfterScrub = getCurrentPositionOnPlayer();
        int expectedPosition = (seekBarWidth / 2);
        return ((expectedPosition - 20) < currentPositionOnSeekPlayerAfterScrub && currentPositionOnSeekPlayerAfterScrub < (expectedPosition + 20));
    }

    public enum PlayerControl {
        AIRPLAY,
        AUDIO_SUBTITLE_BUTTON,
        BACK,
        CHROMECAST,
        FAST_FORWARD,
        PLAY,
        PAUSE,
        RESTART,
        REWIND
    }

    public void validateRatingsOnPlayer(String rating, String ratingsDictionaryKey, SoftAssert sa, DisneyPlusDetailsIOSPageBase detailsPage) {
        detailsPage.getPlayButton().click();
        sa.assertTrue(isRatingPresent(ratingsDictionaryKey), rating + " Rating was not found on movie video player.");
        clickBackButton();
    }

    public ExtendedWebElement getSkipPromoButton() {
        return getTypeButtonContainsLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BTN_SKIP_PROMO.getText()));
    }

    public void skipPromoIfPresent() {
        getSkipPromoButton().clickIfPresent(SHORT_TIMEOUT);
    }

    public void waitForAdGracePeriodToEnd() {
        int gracePeriod = getRemainingTime() - FORTY_FIVE_SEC_TIMEOUT;
        LOGGER.info("Waiting for playback to move pass {} seconds grace period ", FORTY_FIVE_SEC_TIMEOUT);
        fluentWait(getDriver(), LONG_TIMEOUT, HALF_TIMEOUT, "Playback unable to pass ad grace period").
                until(it -> getRemainingTime() < gracePeriod);
    }
}
