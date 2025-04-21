package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.disney.qa.common.constant.IConstantHelper.PHONE;
import static com.disney.qa.common.constant.IConstantHelper.TABLET;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVideoPlayerIOSPageBase extends DisneyPlusApplePageBase {
    private static final double SCRUB_PERCENTAGE_TEN = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String BROADCAST_COLLECTION = "broadcastCollectionView";

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "ucp.playerView")
    protected ExtendedWebElement playerView;

    @ExtendedFindBy(accessibilityId = "ucp.currentPosition")
    protected ExtendedWebElement seekBar;

    @ExtendedFindBy(accessibilityId = "currentTimeMarker")
    protected ExtendedWebElement currentTimeMarker;
    @FindBy(name = "serviceAttributionLabel")
    protected ExtendedWebElement serviceAttributionLabel;
    @ExtendedFindBy(accessibilityId = "serviceAttributionLabel")
    protected ExtendedWebElement serviceAttribution;
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
    @ExtendedFindBy(accessibilityId = "lockPlayerControlsButton")
    private ExtendedWebElement iconPinUnlocked;
    @ExtendedFindBy(accessibilityId = "unlockPlayerControlsButton")
    private ExtendedWebElement iconPinLocked;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name CONTAINS \"%s\"`]")
    private ExtendedWebElement currentlyPlayingTitle;
    @ExtendedFindBy(accessibilityId = "brandImageView")
    private ExtendedWebElement brandImageView;
    @ExtendedFindBy(accessibilityId = "skipIntroButton")
    private ExtendedWebElement skipIntroButton;
    @FindBy(name = "subtitleLabel")
    private ExtendedWebElement subtitleLabel;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`label == \"%s\"`]/XCUIElementTypeImage")
    private ExtendedWebElement networkWatermarkLogo;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"adPod\"`]")
    private ExtendedWebElement adPod;
    @ExtendedFindBy(accessibilityId = "adCountdownLabel")
    private ExtendedWebElement adTimeBadge;
    @ExtendedFindBy(accessibilityId = "ratingLabel")
    private ExtendedWebElement contentRatingOverlayLabel;
    @ExtendedFindBy(accessibilityId = "contentRatingInfoView")
    private ExtendedWebElement contentRatingInfoView;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[$type='XCUIElementTypeStaticText' AND label CONTAINS " +
            "'%s'$]/**/XCUIElementTypeButton")
    private ExtendedWebElement feedOptionCheckmark;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$name == 'ucp.playerView'$]/" +
            "**/XCUIElementTypeImage[`name == 'loader'`]")
    private ExtendedWebElement loaderImage;



    public static final String NEGATIVE_STEREOTYPE_INTERSTITIAL_MESSAGE_PART1 = "This program includes negative " +
          "depictions and/or mistreatment of people or cultures. These stereotypes were wrong then and are wrong now. Rather than remove this content, we want to acknowledge its harmful impact, learn from it and spark conversation to create a more inclusive future together.";
    public static final String NEGATIVE_STEREOTYPE_INTERSTITIAL_MESSAGE_PART2 = "Disney is committed to creating " +
            "stories with inspirational and aspirational themes that reflect the rich diversity of the human experience around the globe.";
    private static final String NEGATIVE_STEREOTYPE_COUNTDOWN_MESSAGE = "YOUR VIDEO WILL START IN";

    //FUNCTIONS

    public DisneyPlusVideoPlayerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return playerView.isPresent();
    }

    public ExtendedWebElement getPlayButton() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PLAY.getText()));
    }

    public ExtendedWebElement getPauseButton() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PAUSE.getText()));
    }

    public ExtendedWebElement getBackButton() {
        return dynamicBtnFindByName.format("buttonBack");
    }

    public ExtendedWebElement getBroadcastMenu() {
        String broadcastMenuLabel = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                DictionaryKeys.BROADCAST_MENU.getText());
        return dynamicBtnFindByLabel.format(broadcastMenuLabel);
    }

    public ExtendedWebElement getSkipRecapButton() {
        return getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SKIP_RECAP.getText()));
    }

    public ExtendedWebElement getContentRatingInfoView() {
        return contentRatingInfoView;
    }

    public ExtendedWebElement getBroadcastCollectionView() {
        return getDynamicAccessibilityId(BROADCAST_COLLECTION);
    }

    public ExtendedWebElement getElementFor(PlayerControl control) {
        switch (control) {
            case AIRPLAY:
                return airplayButton;
            case AUDIO_SUBTITLE_BUTTON:
                return audioSubtitleMenuButton;
            case BACK:
                return getBackButton();
            case BROADCAST_MENU:
                return getBroadcastMenu();
            case CHROMECAST:
                return chromecastButton;
            case FAST_FORWARD:
                return forwardButton;
            case NEXT_EPISODE:
                return getNextEpisodeButton();
            case PLAY:
                return getPlayButton();
            case PAUSE:
                return getPauseButton();
            case RESTART:
                return getRestartButton();
            case REWIND:
                return rewindButton;
            case LOCK_ICON:
                return iconPinUnlocked;
            case UNLOCK_ICON:
                return iconPinLocked;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is an invalid player control", control));
        }
    }

    public boolean isElementPresent(PlayerControl control) {
        waitForPresenceOfAnElement(playerView);
        displayVideoController();
        return getElementFor(control).isElementPresent();
    }

    public ExtendedWebElement getSeekbar() {
        return seekBar;
    }

    public boolean isSeekbarVisible() {
        return seekBar.isPresent();
    }

    public ExtendedWebElement getSkipIntroButton() {
        return skipIntroButton;
    }

    public ExtendedWebElement getServiceAttributionLabel(){
        return serviceAttributionLabel;
    }

    public ExtendedWebElement getServiceAttribution() {
        return serviceAttribution;
    }

    public ExtendedWebElement getSubtitleLabelElement() {
        return subtitleLabel;
    }

    public boolean isServiceAttributionLabelVisible() {
        try {
            return fluentWait(getDriver(), TWENTY_FIVE_SEC_TIMEOUT, ONE_SEC_TIMEOUT,
                    "Service attribution didn't appear on video player")
                    .until(it -> getServiceAttributionLabel().isPresent(ONE_SEC_TIMEOUT));
        } catch (Exception e) {
            LOGGER.info(String.format("Service Attribution Label not found - %s", e.getMessage()));
            return false;
        }
    }

    public boolean isServiceAttributionLabelVisibleWithControls() {
        displayVideoController();
        return getServiceAttributionLabel().isPresent();
    }

    public boolean isCurrentTimeLabelVisible() {
        displayVideoController();
        return currentTimeLabel.isPresent();
    }

    public String getTimeRemainingLabelText() {
        String remainingTime = timeRemainingLabel.getText();
        if (remainingTime != null && !remainingTime.isEmpty()) {
            return remainingTime.replace("-", "");
        } else {
            throw new NoSuchElementException("Time remaining label is not found on video player");
        }
    }

    public boolean isRemainingTimeLabelVisible() {
        displayVideoController();
        return timeRemainingLabel.isPresent();
    }
    public boolean isCurrentTimeMarkerVisible() {
        displayVideoController();
        return currentTimeMarker.isPresent();
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

    public DisneyPlusVideoPlayerIOSPageBase waitForVideoToStart(int timeout, int polling) {
        LOGGER.info("Checking for loading spinner...");
        try {
            fluentWait(getDriver(), timeout, polling, "Loading spinner is not visible")
                    .until(it -> ucpLoadSpinner.isElementPresent());
        } catch (TimeoutException timeoutException) {
            LOGGER.info("Loading spinner not detected and skipping wait");
        }
        LOGGER.info("Loading spinner detected and waiting for animation to complete");
        fluentWait(getDriver(), timeout, polling, "Loading spinner is still visible")
                .until(it -> ucpLoadSpinner.isElementNotPresent(timeout));
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase displayVideoController() {
        LOGGER.info("Activating video player controls...");
        //Check is due to placement of PlayPause, which will pause the video if clicked
        Dimension size = getDriver().manage().window().getSize();
        tapAtCoordinateNoOfTimes((size.width * 35), (size.height * 50), 1);
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, FIVE_SEC_TIMEOUT, "Seek bar is present").until(it -> !seekBar.isPresent(ONE_SEC_TIMEOUT));
        int attempts = 0;
        do {
            clickElementAtLocation(playerView, 35, 50);
        } while (attempts++ < 5 && !seekBar.isElementPresent(THREE_SEC_TIMEOUT));
        if (attempts == 6) {
            Assert.fail("Seek bar was present and attempts exceeded over 5.");
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusVideoPlayerIOSPageBase clickPauseButton() {
        ExtendedWebElement pauseButton = getPauseButton();
        displayVideoController();
        pauseButton.click();
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
        List<ExtendedWebElement> backButtonList = findExtendedWebElements(getBackButton().getBy());
        if (!backButtonList.isEmpty()) {
            for (ExtendedWebElement backButton : backButtonList) {
                if (backButton.isElementPresent(ONE_SEC_TIMEOUT)) {
                    backButton.click();
                    break;
                }
            }
        } else {
            throw new NoSuchElementException("Back button was not found on video player");
        }
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

    public boolean isTitleLabelDisplayed() {
        return titleLabel.isPresent();
    }

    public boolean isSubTitleLabelDisplayed() {
        return subtitleLabel.isPresent();
    }

    public DisneyPlusDetailsIOSPageBase tapTitleOnPlayer() {
        displayVideoController();
        titleLabel.click();
        return initPage(DisneyPlusDetailsIOSPageBase.class);
    }

    public DisneyPlusDetailsIOSPageBase tapSubtitleOnPlayer() {
        displayVideoController();
        subtitleLabel.click();
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
        int destinationX = seekBar.getLocation().getX() +
                (int)(seekBar.getSize().getWidth() * Double.parseDouble("." + (int) Math.round(playbackPercent * 100)));
        displayVideoController();
        Point currentTimeMarkerLocation = currentTimeMarker.getLocation();
        LOGGER.info("Scrubbing from X:{},Y:{} to X:{},Y:{}",
                currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(),
                destinationX, currentTimeMarkerLocation.getY());
        scrollFromTo(currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(),
                destinationX, currentTimeMarkerLocation.getY());
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
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE) || R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            displayVideoController();
        } else {
            new DisneyPlusAppleTVCommonPage(getDriver()).clickDown(1);
        }
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        int remainingTimeInSec =
                (Math.abs(Integer.parseInt(remainingTime[0])) * 60) + (Integer.parseInt(remainingTime[1]));
        LOGGER.info("Playback time remaining {} seconds...", remainingTimeInSec);
        return remainingTimeInSec;
    }

    /**
     * Opens the player overlay, reads remaining time that has 3 integers
     * (hours, minutes, seconds) on the seekbar and converts it to seconds
     *
     * @return Playback remaining time in seconds
     */

    public int getRemainingTimeThreeIntegers() {
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE) || R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            displayVideoController();
        }
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

    /**
     * Opens the player overlay, reads remaining time that has 3 integers
     * (hours, minutes, seconds) on the seekbar and converts only hour and mins to seconds
     * to match the details page duration
     * @return Playback remaining time in seconds
     */
    public int getRemainingHourAndMinInSeconds() {
        displayVideoController();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        int remainingTimeInSec = (Integer.parseInt(remainingTime[0]) * -60) * 60
                + Integer.parseInt(remainingTime[1]) * 60;
        LOGGER.info("Playback time remaining {} seconds...", remainingTimeInSec);
        return remainingTimeInSec;
    }

    /**
     * Opens the player overlay, reads remaining time from seek bar taking into account there could be titles where
     * hours unit might not be present and returns the total quantity of remaining minutes using hours and minutes
     * @return Playback remaining time in minutes
     */
    public int getRemainingTimeInMinutes() {
        displayVideoController();
        String[] remainingTimeParts = timeRemainingLabel.getText().replace("-", "").split(":");

        if (remainingTimeParts.length == 2) {
            return Integer.parseInt(remainingTimeParts[0]);
        } else if (remainingTimeParts.length == 3) {
            int hours = Integer.parseInt(remainingTimeParts[0]);
            int minutes = Integer.parseInt(remainingTimeParts[1]);
            return (hours * 60) + minutes;
        } else {
            throw new IllegalArgumentException(String.format("Invalid time format: %s", timeRemainingLabel.getText()));
        }
    }

    public String getRemainingTimeInStringWithHourAndMinutes() {
        int remainingTimeInMinutes = getRemainingTime();
        long hours = remainingTimeInMinutes / 60;
        long minutes = remainingTimeInMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    public String getRemainingTimeInDetailsFormatString() {
        int remainingTimeInSeconds = getRemainingTimeThreeIntegers();
        if(remainingTimeInSeconds > 3600) {
            long hours = remainingTimeInSeconds / 60;
            long minutes = remainingTimeInSeconds % 60;
            return String.format("%dh %dm", hours, minutes);
        } else {
            long minutes = remainingTimeInSeconds / 60;
            return String.format("%dm", minutes);
        }
    }
    
    public void tapAudioSubtitleMenu() {
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT, "subtitle menu overlay didn't open")
                .until(it -> {
                    displayVideoController();
                    audioSubtitleMenuButton.click();
                    return initPage(DisneyPlusAudioSubtitleIOSPageBase.class).isOpened();
                });
    }

    public boolean isSkipIntroButtonPresent() {
        return skipIntroButton.isElementPresent();
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
        int waitTime = timeout.length > 0 ? timeout[0] : FIFTEEN_SEC_TIMEOUT;
        return fluentWait(getDriver(), waitTime, THREE_SEC_TIMEOUT, "Ad badge label not present")
                .until(it -> isAdBadgePresent(ONE_SEC_TIMEOUT));
    }

    public ExtendedWebElement getAdBadge() {
        String adLabel = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.AD_BADGE_LABEL.getText());
        return getStaticTextByLabel(adLabel);
    }

    public boolean isAdBadgeLabelPresentWhenControlDisplay() {
        displayVideoController();
        return getAdBadge().isElementPresent();
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
                "Video is not playing from the beginning");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public ExtendedWebElement getNextEpisodeButton() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.BTN_NEXT_EPISODE.getText()));
    }

    public ExtendedWebElement getRestartButton() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.VIDEO_PLAYER_RESTART_BUTTON.getText()));
    }

    public String getRestartButtonStatus() {
        displayVideoController();
        return getRestartButton().getAttribute(Attributes.ENABLED.getAttribute());
    }

    public void clickRestartButton() {
        displayVideoController();
        getRestartButton().click();
    }

    public ExtendedWebElement getAdRemainingTime() {
        fluentWait(getDriver(), TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Ad not displayed")
                .until(it -> adTimeBadge.isPresent());
        if (adTimeBadge.isPresent()) {
            return adTimeBadge;
        } else {
            throw new NoSuchElementException("Ad remaining time was not found");
        }
    }

    public boolean isAdTimeDurationPresent() {
        return adTimeBadge.isPresent();
    }

    public boolean isAdTimeDurationPresentWithVideoControls() {
        displayVideoController();
        return adTimeBadge.isPresent();
    }

    public void waitForAdToCompleteIfPresent(int polling) {
        if (isAdBadgeLabelPresent()) {
            int remainingTime = getAdRemainingTimeInSeconds();
            fluentWait(getDriver(), remainingTime, polling, "Ad did not end after " + remainingTime)
                    .until(it -> getAdBadge().isElementNotPresent(ONE_SEC_TIMEOUT));
        } else {
            LOGGER.info("No ad time badge detected, continuing with test..");
        }
    }

    public int getAdRemainingTimeInSeconds() {
        String[] adTime = adTimeBadge.getText().split(":");
        int remainingTime = (Integer.parseInt(adTime[0]) * 60) + (Integer.parseInt(adTime[1]));
        LOGGER.info("Ad Playback time remaining {} seconds...", remainingTime);
        return remainingTime;
    }

    public String getAdRemainingTimeInString() {
        String adTime = getAdRemainingTime().getText();
        LOGGER.info("Ad Playback time remaining {} string...", adTime);
        return adTime;
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
        Pattern timePatternInHHMMSS = Pattern.compile("^([1-9][\\d]):[0-5][\\d]:[0-5][\\d]$");
        Pattern timePatternInHMMSS = Pattern.compile("^[1-9]:[0-5][\\d]:[0-5][\\d]$");
        Pattern timePatternInMMSS = Pattern.compile("^[1-5][\\d]:[0-5][\\d]$");
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
     * we are scrubbing playhead to 50% and verifying with Half of seekbar width plus/minus 75
     */
    public boolean verifyPlayheadRepresentsCurrentPointOfTime() {
        displayVideoController();
        int seekBarWidth = seekBar.getSize().getWidth();
        scrubToPlaybackPercentage(50);
        waitForVideoToStart();
        int currentPositionOnSeekPlayerAfterScrub = getCurrentPositionOnPlayer();
        int expectedPosition = (seekBarWidth / 2);
        return ((expectedPosition - 75) < currentPositionOnSeekPlayerAfterScrub &&
                currentPositionOnSeekPlayerAfterScrub < (expectedPosition + 75));
    }

    public enum PlayerControl {
        AIRPLAY,
        AUDIO_SUBTITLE_BUTTON,
        BACK,
        BROADCAST_MENU,
        CHROMECAST,
        FAST_FORWARD,
        LOCK_ICON,
        NEXT_EPISODE,
        PLAY,
        PAUSE,
        RESTART,
        REWIND,
        UNLOCK_ICON
    }

    public void validateRatingsOnPlayer(String rating, SoftAssert sa, DisneyPlusDetailsIOSPageBase detailsPage) {
        detailsPage.getPlayButton().click();
        skipPromoIfPresent(FIFTEEN_SEC_TIMEOUT);
        sa.assertTrue(isRatingPresent(rating), rating + " Rating was not found on video player");
        waitForVideoToStart();
        scrubToPlaybackPercentage(SCRUB_PERCENTAGE_TEN);
        waitForVideoToStart();
        clickBackButton();
    }

    public ExtendedWebElement getSkipPromoButton() {
        return getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BTN_SKIP_PROMO.getText()));
    }

    public void skipPromoIfPresent(long... timeout) {
        long waitTime = timeout.length > 0 ? timeout[0] : THREE_SEC_TIMEOUT;
        getSkipPromoButton().clickIfPresent(waitTime);
    }

    public void waitForAdGracePeriodToEnd(int remainingTime) {
        int gracePeriod = remainingTime - FORTY_FIVE_SEC_TIMEOUT;
        LOGGER.info("Waiting for playback to move pass {} seconds grace period ", FORTY_FIVE_SEC_TIMEOUT);
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT, "Playback unable to pass ad grace period").
                until(it -> getRemainingTime() < gracePeriod);
    }

    public void waitingForR21PauseTimeOutToEnd(int waitTime, int polling) {
        LOGGER.info("Waiting for R21 Pause timeout to end");
        fluentWait(getDriver(), waitTime, polling, "Video player is visible after R21 Pause timeout")
                .until(it -> !getPlayerView().isPresent());
    }

    public boolean waitUntilRemainingTimeLessThan(int waitTime, int polling, int expectedRemainingTimeInSec) {
        return fluentWait(getDriver(), waitTime, polling,
                "Video player remaining time not matched with expected remaining time")
                .until(it -> isRemainingTimeLessThanExpected(expectedRemainingTimeInSec));
    }

    public boolean isRemainingTimeLessThanExpected(int expectedRemainingTimeInSec) {
        int remainingtime = getRemainingTime();
        return remainingtime <= expectedRemainingTimeInSec;
    }

    public boolean isAdPodPresent() {
        return adPod.isPresent();
    }

    public boolean isContentRatingOverlayPresent() {
        return fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Content Rating is not visible")
                .until(it -> contentRatingOverlayLabel.isElementPresent(THREE_SEC_TIMEOUT));
    }

    public boolean waitForContentRatingOverlayToDisappear() {
        return fluentWait(getDriver(), FIVE_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Content Rating is visible")
                .until(it -> !contentRatingOverlayLabel.isElementPresent());
    }

    public boolean isCrossingAdBoundaryMessagePresent() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.ALERT_MESSAGE_CROSSING_AD_BOUNDARY.getText())).isPresent();
    }

    public void waitForVideoControlToDisappear() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        fluentWait(getDriver(), FIVE_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Player controls still displayed")
                .until(it -> videoPlayer.getElementFor(PlayerControl.FAST_FORWARD).isElementNotPresent(ONE_SEC_TIMEOUT));
    }

    public boolean waitForVideoLockTooltipToAppear() {
        return fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT,
                "Player controls lock tooltip did not appear")
                .until(it -> getLockScreenToolTip().isPresent(ONE_SEC_TIMEOUT));
    }

    public ExtendedWebElement getLockScreenToolTip() {
        return getTextElementValue(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PLAYER_CONTROLS_LOCK_TOOLTIP.getText()));
    }

    public boolean isConcurrencyMessageErrorPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                DictionaryKeys.STREAMCONCURRENCY.getText())).isPresent();
    }

    public boolean isConcurrencyTitleErrorPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                DictionaryKeys.STREAMCONCURRENCY_TITLE.getText())).isPresent();
    }

    public boolean isConcurrencyCTAButtonPresent() {
        return getTypeButtonContainsLabel(getCtaButtonDismiss().getText()).isPresent();
    }

    public ExtendedWebElement getCtaButtonDismiss() {
        return getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                DictionaryKeys.DISMISS_BTN.getText()));
    }

    public boolean waitForNetworkWatermarkLogoToDisappear(String network) {
        return fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, ONE_SEC_TIMEOUT, "Network Watermark Logo is present")
                .until(it -> getNetworkWatermarkLogo(network).isElementNotPresent(ONE_SEC_TIMEOUT));
    }

    public boolean isAdBadgePresent(int timeout) {
        return getAdBadge().isPresent(timeout);
    }

    public boolean isAdBadgeLabelNotPresent() {
        return fluentWait(getDriver(), TEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Ad badge label was present")
                .until(it -> getAdBadge().isElementNotPresent(ONE_SEC_TIMEOUT));
    }

    public boolean waitForDeleteAndPlayButton() {
        try {
            return fluentWait(getDriver(), ONE_HUNDRED_TWENTY_SEC_TIMEOUT, ONE_SEC_TIMEOUT,
                    "Delete and play button is not present")
                    .until(it -> getStaticTextByLabelContains(getLocalizationUtils()
                            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                    DictionaryKeys.BTN_DELETE_PLAY_NEXT.getText())).isPresent());
        } catch (TimeoutException e) {
            LOGGER.info("Exception occurred attempting to wait for delete and play button");
            return false;
        }
    }

    public List<String> getBroadcastTargetFeedOptionText() {
        List<String> feedOptionText = new ArrayList<>();
        List<ExtendedWebElement> feedCell =
                findExtendedWebElements(collectionCellNoRow.format(BROADCAST_COLLECTION).getBy());
        feedCell.forEach(targetFeed -> feedOptionText.add(targetFeed.getText().split(",")[0].trim().toUpperCase()));
        return feedOptionText;
    }

    public List<String> getBroadcastLanguageOptionText() {
        List<String> languageOptionText = new ArrayList<>();
        List<ExtendedWebElement> feedCell =
                findExtendedWebElements(collectionCellNoRow.format(BROADCAST_COLLECTION).getBy());
        feedCell.forEach(targetFeed -> languageOptionText.add(targetFeed.getText().split(",")[1].trim()));
        return languageOptionText;
    }

    public List<String> getExpectedBroadcastLanguageOptions() {
        List<String> broadcastsAudioOptions = new ArrayList<>();
        Pattern pattern = Pattern.compile("BROADCASTS_NAME_.*");
        for (DictionaryKeys key : DictionaryKeys.values()) {
            Matcher matcher = pattern.matcher(key.name());
            if (matcher.matches()) {
                broadcastsAudioOptions.add(getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.MEDIA, key.getText()));
            }
        }
        return broadcastsAudioOptions;
    }

    public String selectAndGetBroadcastFeedOption() {
        String selectedOption = null;
        List<ExtendedWebElement> feedCell =
                findExtendedWebElements(collectionCellNoRow.format(BROADCAST_COLLECTION).getBy());
        if (feedCell.size() > 1) {
            selectedOption = feedCell.get(1).getText().trim();
            feedCell.get(1).click();
        }
        return selectedOption;
    }

    public boolean isFeedOptionSelected(String feedOption) {
        return feedOptionCheckmark.format(feedOption).getAttribute(Attributes.LABEL.getAttribute()).equals("checkmark");
    }

    public ExtendedWebElement getTimeRemainingLabel() {
        return timeRemainingLabel;
    }

    public ExtendedWebElement getTitleVideoLabel() {
        return titleLabel;
    }

    public void clickUnlockButton() {
        if (getElementFor(PlayerControl.UNLOCK_ICON).isElementNotPresent(ONE_SEC_TIMEOUT)) {
            clickElementAtLocation(getPlayerView(), 10, 50);
        }
        longTap(iconPinLocked);
    }
}
