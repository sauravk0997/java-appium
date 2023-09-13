package com.disney.qa.disney.apple.pages.common;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVideoPlayerIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS

    @ExtendedFindBy(accessibilityId = "ucp.playerView")
    protected ExtendedWebElement playerView;

    @ExtendedFindBy(accessibilityId = "ucp.currentPosition")
    protected ExtendedWebElement seekBar;

    @ExtendedFindBy(accessibilityId = "currentTimeMarker")
    protected ExtendedWebElement currentTimeMarker;

    @ExtendedFindBy(accessibilityId = "ucp.durationLabel")
    private ExtendedWebElement timeRemainingLabel;

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

    @FindBy(xpath = "//XCUIElementTypeOther[@name='ratingLabel']/XCUIElementTypeStaticText")
    private ExtendedWebElement ratingLabelText;

    @FindBy(xpath = "//XCUIElementTypeOther[@name='advisoryLabel']/XCUIElementTypeStaticText")
    private ExtendedWebElement advisoryLabelText;

    @ExtendedFindBy(accessibilityId = "ratingLabel")
    private ExtendedWebElement ratingLabel;

    @FindBy(xpath = "//XCUIElementTypeOther[@name='reasonLabel']/XCUIElementTypeStaticText")
    private ExtendedWebElement reasonLabelText;

    @ExtendedFindBy(accessibilityId = "skipIntroButton")
    private ExtendedWebElement skipIntroButton;

    @ExtendedFindBy(accessibilityId = "restartButton")
    private ExtendedWebElement restartButton;

    @ExtendedFindBy(accessibilityId = "youAreLiveButton")
    private ExtendedWebElement youAreLiveButton;

    @FindBy(name = "titleLabel")
    protected ExtendedWebElement titleLabel;

    @FindBy(name = "subtitleLabel")
    private ExtendedWebElement subtitleLabel;

    private ExtendedWebElement backButton = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.BACK_BTN.getText()));

    //FUNCTIONS

    public DisneyPlusVideoPlayerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return playerView.isPresent();
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

    public ExtendedWebElement getPlayButton() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PLAY.getText()));
    }

    public ExtendedWebElement getPauseButton() {
        return getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PAUSE.getText()));
    }

    public ExtendedWebElement getElementFor(PlayerControl control) {
        switch (control) {
            case AIRPLAY:
                return airplayButton;
            case AUDIO_SUBTITLE_BUTTON:
                return audioSubtitleMenuButton;
            case BACK:
                return backButton;
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
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(seekBar.getBy()), 15);
        int attempts = 0;
        do {
            new MobileUtilsExtended().clickElementAtLocation(playerView, 35, 50);
        } while (attempts++ < 10 && !seekBar.isElementPresent(SHORT_TIMEOUT));
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
       if(!getPlayButton().isElementPresent()) {
           displayVideoController();
       }
        getPlayButton().click();
        LOGGER.info("Play button on player view clicked");
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public DisneyPlusDetailsIOSPageBase clickBackButton() {
        displayVideoController();
        backButton.click();
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
                xOffset = (int) (x - Math.round(x * 0.68));
                break;
            case FAST_FORWARD:
                xOffset = (int) (x - Math.round(x * 0.33));
                break;
            default:
                throw new IllegalArgumentException("Undefined player action");
        }
        int yOffset = y / 2;
        new IOSUtils().tapAtCoordinateNoOfTimes(xOffset, yOffset, times);
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
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
        new IOSUtils().dragAndDropElement(currentTimeMarkerLocation.getX(), currentTimeMarkerLocation.getY(), destinationX, currentTimeMarkerLocation.getY(),3);
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
        new IOSUtils().longPressAndHoldElement(currentTimeMarker, 20);
    }

    /**
     * Opens the player overlay, reads remaining time on the seekbar
     * and converts it to seconds
     *
     * @return Play back remaining time in seconds
     */
    public int getRemainingTime() {
        displayVideoController();
        String[] remainingTime = timeRemainingLabel.getText().split(":");
        int remainingTimeInSec = (Integer.parseInt(remainingTime[0]) * -60) + (Integer.parseInt(remainingTime[1]));
        LOGGER.info("Playback time remaining {} seconds...", remainingTimeInSec);
        return remainingTimeInSec;
    }

    public void tapAudioSubTitleMenu() {
        displayVideoController();
        pause(1);
        audioSubtitleMenuButton.click();
    }

    public boolean isSkipIntroButtonPresent() { return skipIntroButton.isElementPresent(); }

    public boolean isYouAreLiveButtonPresent() {
        displayVideoController();
        return youAreLiveButton.isElementPresent();
    }

    /**
     * Waits for content to end in player until getRemainingTime isn't greater than 0 and polling
     * Returns the object of DisneyPlusVideoPlayerIOSPageBase.
     * @param timeout
     * @param polling
     */
    public DisneyPlusVideoPlayerIOSPageBase waitForContentToEnd(int timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Content did not end after " + timeout).until(it ->  getRemainingTime() == 0);
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    /**
     * Waits for trailer to end in player until video player is not open.
     * Returns the object of DisneyPlusVideoPlayerIOSPageBase.
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

    public String getAdvisoryLabelText() {
        return advisoryLabelText.getText();
    }

    public String getRatingLabelText() {
        return ratingLabelText.getText();
    }

    public String getRatingReasonText() {
        return reasonLabelText.getText();
    }

    public boolean isAdBadgeLabelPresent() {
        String adLabel = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.AD_BADGE_LABEL.getText());
        return getDynamicAccessibilityId(adLabel).isElementPresent();
    }

    public void compareWatchLiveToWatchFromStartTimeRemaining(SoftAssert sa) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModalPage = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        Map<String, Integer> params = new HashMap<>();

        detailsPage.clickWatchButton();
        liveEventModalPage.getWatchLiveButton().click();
        sa.assertTrue(isOpened(), "Live video is not playing");
        params.put("watchLiveTimeRemaining", getRemainingTime());
        clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");

        clickBackButton();
        detailsPage.isOpened();
        detailsPage.clickWatchButton();
        liveEventModalPage.getWatchFromStartButton().click();
        sa.assertTrue(isOpened(), "Live video is not playing");
        params.put("watchFromStartTimeRemaining", getRemainingTime());
        sa.assertTrue(params.get("watchLiveTimeRemaining") < params.get("watchFromStartTimeRemaining"), "Watch from start did not return to beginning of live content.");
        params.clear();
    }
}
