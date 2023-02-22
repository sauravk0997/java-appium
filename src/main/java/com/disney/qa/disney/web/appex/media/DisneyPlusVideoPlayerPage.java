package com.disney.qa.disney.web.appex.media;

import com.disney.qa.disney.web.DisneyMediaPlayerCommands;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.TestContext;
import com.disney.qa.disney.web.entities.WebConstant;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVideoPlayerPage extends DisneyPlusBasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(css = "div.btm-media-player")
    private ExtendedWebElement videoPlayer;

    @FindBy(xpath = "//*[contains(@id,'hudson-player')]")
    private ExtendedWebElement videoHudsonPlayer;

    @FindBy(xpath = "//*[contains(@class,'back-arrow')]")
    private ExtendedWebElement videoPlayerBackArrow;

    @FindBy(xpath = "//button[@aria-label='Play' and contains(@class, 'play-icon')]")
    private ExtendedWebElement videoPlayerPlayBtn;

    @FindBy(xpath = "//button[@aria-label='Pause' and contains(@class, 'pause-icon')]")
    private ExtendedWebElement videoPlayerPauseBtn;

    @FindBy(css = ".progress-bar .slider-container")
    private ExtendedWebElement sliderValueNow;

    @FindBy(xpath = "//label[contains(@for, 'subtitleTrackPicker') and contains(text(), '%s')]")
    private ExtendedWebElement subtitleLanguageButton;

    @FindBy(xpath = "//label[contains(@for, 'audioTrackPicker') and contains(text(), '%s')]")
    private ExtendedWebElement audioLanguageButton;

    @FindBy(css = ".exit-fullscreen-icon")
    private ExtendedWebElement exitFullScreenIcon;

    @FindBy(xpath = "//button[@aria-label= '%s']")
    private ExtendedWebElement dynamicAriaLabel;

    @FindBy(css = ".milestones")
    private ExtendedWebElement adMilestone;

    @FindBy(css = ".milestone-marker")
    private ExtendedWebElement adMilestoneMarker;

    @FindBy(css = ".overlay_interstitials__badge")
    private ExtendedWebElement adLabelBadge;

    @FindBy(css = ".overlay_interstitials__content_interaction_link")
    private ExtendedWebElement adLabelInteraction;


    private enum VideoPlayerActions {
        AUDIO_SUBTITLE_SETTINGS_BUTTON("Audio and subtitles menu"),
        CLOSE_SUBTITLE_AUDIO_MENU_BUTTON("Press back on your remote to close the audio and subtitles menu."),
        FULL_SCREEN_BUTTON("Full Screen"),
        EXIT_FULL_SCREEN_BUTTON("Exit Full Screen"),
        SEEK_BACK_10_BUTTON("Skip back 10 seconds"),
        SEEK_FORWARD_10_BUTTON("Skip ahead 10 seconds"),
        VOLUME_BUTTON("Mute");

        String names;

        VideoPlayerActions(String names) {
            this.names = names;
        }

        public String getNames() {
            return names;
        }
    }

    public DisneyPlusVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    //State booleans

    public boolean isVideoPlayerPresent() {
        waitFor(videoPlayer);
        return videoPlayer.isElementPresent();
    }

    public boolean isVideoHudsonPlayerPresent() {
        waitFor(videoHudsonPlayer);
        return videoHudsonPlayer.isElementPresent();
    }

    public boolean isVideoPlayerBackArrowPresent() {
        waitFor(videoPlayerBackArrow);
        return videoPlayerBackArrow.isElementPresent();
    }

    public boolean isVideoPlayerPlayBtnPresent() {
        return videoPlayerPlayBtn.isElementPresent();
    }

    public boolean isVideoPlayerPauseBtnPresent() {
        return videoPlayerPauseBtn.isElementPresent();
    }

    //Hover Methods

    public DisneyPlusVideoPlayerPage hoverVideoPlayer() {
        waitFor(videoPlayer);
        LOGGER.info("Video player is on");
        videoPlayer.hover();
        return this;
    }

    public DisneyPlusVideoPlayerPage hoverHudsonVideoPlayer() {
        waitFor(videoHudsonPlayer);
        videoHudsonPlayer.hover();
        return this;
    }

    public void hoverVideoPlayerBackArrow() {
        waitFor(videoPlayerBackArrow);
        videoPlayerBackArrow.hover();
    }

    //Click Methods

    public DisneyPlusVideoPlayerPage clickVideoPlayer() {
        waitFor(videoPlayer);
        videoPlayer.click();
        return this;
    }

    public boolean isVideoPlayerIsVisible() {
        LOGGER.info("Clicking Play Button....");
        return videoPlayer.isVisible();
    }

    public void clickHudsonVideoPlayer() {
        waitFor(videoHudsonPlayer);
        videoHudsonPlayer.click();
    }

    public DisneyPlusVideoPlayerPage clickVideoPlayerBackArrow() {
        waitFor(videoPlayerBackArrow);
        videoPlayerBackArrow.click();
        return this;
    }

    public DisneyPlusVideoPlayerPage clickVideoPlayerPlayBtn() {
        getMediaPlayerState();
        LOGGER.info("Clicking Play Button....");
        isVideoPlayerPlayBtnPresent();
        safeClickOnVideoPlayerPlayBtn();
        return this;
    }

    public DisneyPlusVideoPlayerPage safeClickOnVideoPlayerPlayBtn() {
        LOGGER.info("Safe click Play Button");
        if(BROWSER.equalsIgnoreCase("firefox"))
            videoPlayerPlayBtn.clickByJs();
        else
            videoPlayerPlayBtn.click();
        return this;
    }

    public DisneyPlusVideoPlayerPage clickVideoPlayerPauseBtn() {
        getMediaPlayerState();
        LOGGER.info("Clicking Pause Button....");
        videoPlayerPauseBtn.click();
        getMediaPlayerState();
        return this;
    }

    public DisneyPlusVideoPlayerPage clickAudioSubtitlesButton() {
        LOGGER.info("Clicking Audio/Subtitles....");
        dynamicAriaLabel.format(VideoPlayerActions.AUDIO_SUBTITLE_SETTINGS_BUTTON.getNames()).click();
        return this;
    }

    public DisneyPlusVideoPlayerPage clickSubtitleOption(String subtitleLanguage) {
        LOGGER.info("Clicking {} Subtitles...", subtitleLanguage);
        subtitleLanguageButton.format(subtitleLanguage).clickByJs();
        return this;
    }

    public DisneyPlusVideoPlayerPage clickAudioOption(String audioLanguage) {
        LOGGER.info("Clicking {} Audio : ",  audioLanguage);
        audioLanguageButton.format(audioLanguage).clickByJs();
        return this;
    }

    public DisneyPlusVideoPlayerPage  clickCloseAudioSubtitleMenu() {
        LOGGER.info("Closing Audio/Subtitles Menu...");
        dynamicAriaLabel.format(VideoPlayerActions.CLOSE_SUBTITLE_AUDIO_MENU_BUTTON.getNames()).click();
        return this;
    }

    public DisneyPlusVideoPlayerPage  clickFullScreenButton() {
        LOGGER.info("Opening Full Screen...");
        dynamicAriaLabel.format(VideoPlayerActions.FULL_SCREEN_BUTTON.getNames()).click();
        return this;
    }

    public boolean  isFullScreenIsVisible() {
        LOGGER.info("verifying player Full Screen...");
        return exitFullScreenIcon.isPresent();
    }

    public DisneyPlusVideoPlayerPage  clickExitFullScreenButton() {
        LOGGER.info("Exiting Full Screen...");
        dynamicAriaLabel.format(VideoPlayerActions.EXIT_FULL_SCREEN_BUTTON.getNames()).click();
        return this;
    }

    public DisneyPlusVideoPlayerPage  clickSeekForwardButton() {
        LOGGER.info("Skipping ahead 10 seconds...");
        dynamicAriaLabel.format(VideoPlayerActions.SEEK_FORWARD_10_BUTTON.getNames()).click();
        return this;
    }

    public DisneyPlusVideoPlayerPage  clickSeekBackButton() {
        LOGGER.info("Rewinding 10 seconds...");
        dynamicAriaLabel.format(VideoPlayerActions.SEEK_BACK_10_BUTTON.getNames()).clickByJs();
        return this;
    }

    public DisneyPlusVideoPlayerPage  getCurrentTimeValue(String storedValue) {
        LOGGER.info("Getting time...");
        int a = Integer.parseInt(sliderValueNow.getAttribute("aria-valuenow"));
        TestContext.putvalue(storedValue,a);
        LOGGER.info("value stored is : {}", a);
        return this;
    }

    public String getMediaPlayerObject() {
        return DisneyMediaPlayerCommands.RETURN.getValue() + DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() + DisneyMediaPlayerCommands.PLAYER_STATE.getValue();
    }

    public String getMediaPlayerState() {
        LOGGER.info("Retrieving Media Player State");
        String playbackState = "Invalid Media State Retrieved";
        boolean isBuffering = Boolean.parseBoolean(String.valueOf(trigger(getMediaPlayerObject() + DisneyMediaPlayerCommands.IS_BUFFERING.getValue())));
        boolean isEmpty = Boolean.parseBoolean(String.valueOf(trigger(getMediaPlayerObject() + DisneyMediaPlayerCommands.IS_EMPTY.getValue())));
        boolean isError = Boolean.parseBoolean(String.valueOf(trigger(getMediaPlayerObject() + DisneyMediaPlayerCommands.IS_ERROR.getValue())));
        boolean isFinished = Boolean.parseBoolean(String.valueOf(trigger(getMediaPlayerObject() + DisneyMediaPlayerCommands.IS_FINISHED.getValue())));
        boolean isPaused = Boolean.parseBoolean(String.valueOf(trigger(getMediaPlayerObject() + DisneyMediaPlayerCommands.IS_PAUSED.getValue())));
        boolean isPlaying = Boolean.parseBoolean(String.valueOf(trigger(getMediaPlayerObject() + DisneyMediaPlayerCommands.IS_PLAYING.getValue())));

        if (isBuffering) {
            playbackState = "Buffering";
        }
        if (isEmpty) {
            playbackState = "Empty";
        }
        if (isError) {
            playbackState = "Error";
        }
        if (isFinished) {
            playbackState = "Finished";
        }
        if (isPaused) {
            playbackState = "Paused";
        }
        if (isPlaying) {
            playbackState = "Playing";
        }

        LOGGER.info(String.format("Media Playback State: %s", playbackState));
        return playbackState;
    }

    public DisneyPlusVideoPlayerPage verifyMediaPlayerState(DisneyMediaPlayerCommands playerState, boolean expectedState, SoftAssert sa) {
        LOGGER.info("verifying Media Player State is : {}" , playerState);
        boolean actualPlaybackState = Boolean.parseBoolean(String.valueOf(trigger(getMediaPlayerObject() + playerState.getValue())));
        String finalPlaybackState = String.valueOf(actualPlaybackState);
        switch (playerState) {
            case IS_BUFFERING:
            case IS_EMPTY:
            case IS_ERROR:
            case IS_FINISHED:
            case IS_PAUSED:
            case IS_PLAYING:
                sa.assertEquals(actualPlaybackState,expectedState,"required playback state isn't present");
                LOGGER.info("Actual Playback State for: {} is {}", playerState, finalPlaybackState);
                break;
            default:
                LOGGER.error("Invalid State: {}" , playerState);
                break;
        }
        return this;
    }

    public boolean  isAdElementPresent() {
        LOGGER.info("checking for ad elements while paused...");
        return (adMilestone.isElementPresent() && adMilestoneMarker.isElementPresent());
    }

    public DisneyPlusVideoPlayerPage  verifyBadgeElementPresent() {
        LOGGER.info("checking for ad badge while paused...");
        adLabelBadge.assertElementWithTextPresent(WebConstant.AD_BADGE);
        return this;
    }

    public DisneyPlusVideoPlayerPage  verifyInteractionElementPresent() {
        LOGGER.info("checking for interaction label while paused...");
        adLabelInteraction.assertElementWithTextPresent(WebConstant.AD_INTERACTION_BADGE);
        return this;
    }

    // Getters

    /**
     * Grab content id key displayed on url on videoplayback
     * @return 36 Character Key
     */
    public String getUrlContentKey() {
        String url = getCurrentUrl();
        String key = url.substring(Math.max(0, url.length()-36));
        LOGGER.info("Content id key grabbed from url: " + key);
        return key;
    }

    /**
     *
     * Refer to private playVideos() prior to use.
     *
     * @param lengthInSeconds Video playback length (treated as timeout)
     * @param autoPlay Allow next video to autoplay (Autoplay functionality needs to be turned on within Profile settings)
     *
     * Primary purpose: continue playback by clicking on "Play Next Episode" button
     *
     * Steps: set autoPlay to false.
     *
     * When autoplay set to false, super looks for "Play Next Episode" button after every ping and clicks it once it appears.
     *
     * Additional info: "Play Next Episode" button might be tvshow specific and appears after episode conclusion.
     *
     */
    public void playVideo(int lengthInSeconds, boolean autoPlay) {
        playVideos(lengthInSeconds, autoPlay, 0);
    }

    /**
     *
     * Refer to private playVideos() prior to use.
     *
     * @param lengthInSeconds Video playback length (treated as timeout)
     * @param autoPlay Allow next video to autoplay (Autoplay functionality needs to be turned on within Profile settings)
     * @param autoPlayLengthInSeconds Auto Play Video length
     *
     * Primary purpose: continue playing videos one after another.
     *
     * Steps: set autoPlay to true, set video playback length for next video
     *
     */
    public void playVideo(int lengthInSeconds, boolean autoPlay, int autoPlayLengthInSeconds) {
        playVideos(lengthInSeconds, autoPlay, autoPlayLengthInSeconds);
    }

    /**
     *
     * Refer to private playVideos() prior to use.
     *
     * @param lengthInSeconds Video playback length (treated as timeout)
     *
     * Assumption: autoplay is allowed
     *
     * Primary purpose: continue playing videos one after another.
     *
     */
    public void playVideo(int lengthInSeconds) {
        playVideos(lengthInSeconds, true, 0);
    }

    /**
     *
     * After video starts streaming this function will keep video playing (with additional features).
     *
     * Currently can be done using two approaches (defined in each public methods individually).
     *
     * @param lengthInSeconds Video playback length (can be seen as timeout)
     * @param autoPlay Allow next video to autoplay (Autoplay functionality needs to be turned on within Profile settings)
     *
     */
    private void playVideos(int lengthInSeconds, boolean autoPlay, int autoPlayLengthInSeconds) {
        String currentContent;
        int maxAttempts = 5;
        do {
            currentContent = getDriver().getCurrentUrl();
            if(!currentContent.contains("/video")) {
                LOGGER.info("Waiting for video to start...");
                pause(SHORT_TIMEOUT);
            } else break;
        } while (maxAttempts-- > 0);
        LOGGER.info("Playing content: " + currentContent);
        DisneyPlusBaseDetailsPage disneyPlusBaseDetailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        for (int timeElapsed = SHORT_TIMEOUT; timeElapsed <= lengthInSeconds; timeElapsed += SHORT_TIMEOUT) {
            try {
                TimeUnit.SECONDS.sleep(SHORT_TIMEOUT);
                LOGGER.info("Elapsed time: " + timeElapsed + " seconds");

                //ping webdriver
                getDriver().getCurrentUrl();

                if(autoPlay && !currentContent.equalsIgnoreCase(getDriver().getCurrentUrl())) {
                    LOGGER.info("Playing next video for " + autoPlayLengthInSeconds + " seconds");
                    //keep short
                    TimeUnit.SECONDS.sleep(autoPlayLengthInSeconds);
                    break;
                } else if(!autoPlay && disneyPlusBaseDetailsPage.isPlayNextEpisodeBtnPresent(5)) {
                    disneyPlusBaseDetailsPage.clickPlayNextEpisodeBtn();
                    break;
                }
            } catch (InterruptedException interruptedException) {
                LOGGER.info("Shutting down Webdriver");
                Thread.currentThread().interrupt();
                getDriver().quit();
                getDriver().close();
            }
        }
    }

}
