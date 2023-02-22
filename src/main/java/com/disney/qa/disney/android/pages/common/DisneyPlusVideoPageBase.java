package com.disney.qa.disney.android.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVideoPageBase extends DisneyPlusCommonPageBase {
    private static final String PLAYER_CONTROL_VISIBLE_ERROR_MESSAGE = "Player controls still visible";
    private static final String UP_NEXT_NOT_VISIBLE = "Up next is not visible.";

    @FindBy(id = "video_frame")
    private ExtendedWebElement videoPlayer;

    @FindBy(id = "progressBar")
    private ExtendedWebElement bufferingSpinner;

    @FindBy(id = "ratingId")
    private ExtendedWebElement ratingId;

    @FindBy(id = "ratingAdvisory")
    private ExtendedWebElement ratingAdvisory;

    @FindBy(id = "jumpBackwardButton")
    private ExtendedWebElement jumpBackwardButton;

    @FindBy(id = "playPauseButton")
    private ExtendedWebElement playPauseButton;

    @FindBy(id = "jumpForwardButton")
    private ExtendedWebElement jumpForwardButton;

    @FindBy(id = "remainingTimeTextView")
    private ExtendedWebElement remainingTimeTextView;

    @FindBy(id = "seekBar")
    private ExtendedWebElement seekBar;

    @FindBy(id = "closeIcon")
    private ExtendedWebElement videoBackButton;

    @FindBy(id = "closedCaptions")
    private ExtendedWebElement closedCaptionsButton;

    @FindBy(id = "audioAndSubtitlesContainer")
    private ExtendedWebElement audioAndSubtitleContainer;

    @FindBy(id = "audioRecyclerview")
    private ExtendedWebElement audioList;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/audioRecyclerview']//*[@text='%s']")
    private ExtendedWebElement audioItem;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/audioRecyclerview']//*[@text='%s']" +
            "/preceding-sibling::*[@resource-id='com.disney.disneyplus:id/checkBoxImageView']")
    private ExtendedWebElement audioItemCheckmark;

    @FindBy(id = "subtitleRecyclerview")
    private ExtendedWebElement subtitleList;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/subtitleRecyclerview']//*[@text='%s']")
    private ExtendedWebElement subtitleItem;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/subtitleRecyclerview']//*[@text='%s']" +
            "/preceding-sibling::*[@resource-id='com.disney.disneyplus:id/checkBoxImageView']")
    private ExtendedWebElement subtitleItemCheckmark;

    @FindBy(id = "castButton")
    private ExtendedWebElement chromecastButton;

    @FindBy(id = "mr_chooser_route_icon")
    private ExtendedWebElement chromecastReceiver;

    @FindBy(id = "up_next_progress_image")
    private ExtendedWebElement upNextButton;

    @FindBy(id = "topBarTitle")
    private ExtendedWebElement videoTitle;

    @FindBy(id = "topBarSubtitle")
    private ExtendedWebElement videoSubTitle;

    @FindBy(id = "interstitialCopy")
    private ExtendedWebElement contentAdvisoryMain;

    @FindBy(id = "countdownTimer")
    private ExtendedWebElement contentAdvisoryCountdown;

    @FindBy(id = "contentRatingFragmentContainer")
    private ExtendedWebElement contentRatingOverlayContainer;

    @FindBy(id = "skipIntro")
    private ExtendedWebElement skipIntro;

    @FindBy(id = "skipRecap")
    private ExtendedWebElement skipRecap;

    @FindBy(id = "up_next_back_btn")
    private ExtendedWebElement upNextBackButton;

    @FindBy(id = "up_next_background_image")
    private ExtendedWebElement upNextBackgroundImage;

    @FindBy(id = "up_next_image")
    private ExtendedWebElement upNextImage;

    @FindBy(id = "up_next_title_text")
    private ExtendedWebElement upNextTitle;

    @FindBy(id = "up_next_header_text")
    private ExtendedWebElement upNextHeader;

    @FindBy(id= "up_next_progress_image")
    private ExtendedWebElement upNextProgressImage;

    @FindBy(id = "upNextContainer")
    private ExtendedWebElement upNextContainer;

    @FindBy(id = "content_title")
    private ExtendedWebElement errorCodeTitle;

    @FindBy(id = "positive_button")
    private ExtendedWebElement errorCodeOkButton;

    private double scrubHorizontalPoint;

    public DisneyPlusVideoPageBase (WebDriver driver){
        super(driver);
    }

    private void increaseHorizontalPoint(double amount) {
        scrubHorizontalPoint += amount;
    }

    private void setScrubHorizontalPoint(double value) {
        this.scrubHorizontalPoint = value;
    }

    @Override
    public boolean isOpened(){
        return videoPlayer.isElementPresent();
    }

    public boolean waitForVideoBuffering(){
        try {
            waitUntil(ExpectedConditions.visibilityOfElementLocated(bufferingSpinner.getBy()), LONG_TIMEOUT);
            waitUntil(ExpectedConditions.invisibilityOfElementLocated(bufferingSpinner.getBy()), LONG_TIMEOUT);
        } catch (TimeoutException e){
            throw new TimeoutException(e);
        }
        return true;
    }

    public void displayControlOverlay(){
        LOGGER.info("Activating video player controls...");
        fluentWait(getDriver(), EXTRA_LONG_TIMEOUT, SHORT_TIMEOUT, "Seek bar is present").until(it -> seekBar.isElementNotPresent(ONE_SEC_TIMEOUT));
        videoPlayer.click();
    }

    public void waitForDisplayControlOverlayToDisappear() {
        fluentWait(getDriver(), EXTRA_LONG_TIMEOUT, SHORT_TIMEOUT, "Player controls still displayed").until(it -> playPauseButton.isElementNotPresent(ONE_SEC_TIMEOUT));
    }

    public String getRemainingTime() {
        if(!remainingTimeTextView.isElementPresent(ONE_SEC_TIMEOUT)) {
            displayControlOverlay();
        }
        return remainingTimeTextView.getText();
    }

    public int getRemainingTimeInSeconds(String remainingTime) {
        int hours;
        int minutes;
        int seconds;
        String[] time = remainingTime.split(":");

        if(time.length == 3) {
            hours = Integer.parseInt(time[0]);
            minutes = Integer.parseInt(time[1]);
            seconds = Integer.parseInt(time[2]);

            return hours * 3600 + minutes * 60 + seconds;
        } else {
            minutes = Integer.parseInt(time[0]);
            seconds = Integer.parseInt(time[1]);

            return minutes * 60 + seconds;
        }
    }

    public boolean isRatingIdPresent(){
        return ratingId.isElementPresent();
    }

    public String getRatingId(){
        String[] rating = ratingId.getText().split(" ");
        rating = rating[rating.length-1].split("/");
        return rating[rating.length -1];
    }

    /**
     * @deprecated due to replacing use of generic text element, which has the potential to return ratings information
     * by virtue of it finding the first textView on screen instead of the dedicated title item with built in wait
     * function
    */
     @Deprecated
    public String getTitleText(){
        return genericTextItemType.getText();
    }

    public String getActiveMediaTitle(){
        displayControlOverlay();
        waitUntil(ExpectedConditions.visibilityOfElementLocated(videoTitle.getBy()), 5);
        return videoTitle.getText();
    }

    public String getActiveMediaSubtitle() {
        displayControlOverlay();
        waitUntil(ExpectedConditions.visibilityOfElementLocated(videoSubTitle.getBy()), 5);
        return videoSubTitle.getText();
    }

    public boolean isPlayPauseButtonPresent(){
        return playPauseButton.isElementPresent();
    }

    public boolean isPlayPauseButtonPresent(int timeout){
        return playPauseButton.isElementPresent(timeout);
    }

    public boolean isJumpBackwardsButtonPresent(){
        return jumpBackwardButton.isElementPresent();
    }

    public boolean isJumpForwardButtonPresent(){
        return jumpForwardButton.isElementPresent();
    }

    public boolean isRemainingTimePresent(){
        return remainingTimeTextView.isElementPresent();
    }

    public boolean isUpNextBackButtonElementPresent() { return upNextBackButton.isElementPresent(); }

    public void clickUpNextBackButton() { upNextBackButton.click(); }

    public void clickVideoPlayerFrame() { videoPlayer.click(); }

    public boolean isUpNextImagePresent() { return upNextImage.isElementPresent(); }

    public boolean isUpNextTitlePresent() { return upNextTitle.isElementPresent(); }

    public String getUpNextTitleText() { return upNextTitle.getText(); }

    public boolean isUpNextHeaderPresent() { return upNextHeader.isElementPresent(); }

    public String getUpNextHeaderText() { return upNextHeader.getText(); }

    public boolean isUpNextProgressImageClickable() {return upNextProgressImage.isClickable(); }

    public boolean isUpNextPresent() { return upNextContainer.isElementPresent(); }

    public boolean isUpNextContainerVisible() {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(upNextContainer.getBy()), EXTRA_LONG_TIMEOUT);
    }

    public boolean isUpNextBackgroundImageVisible() {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(upNextBackgroundImage.getBy()), EXTRA_LONG_TIMEOUT);
    }

    public boolean isContentAdvisoryPresent(){
         return contentAdvisoryMain.isElementPresent();
    }

    public boolean waitForContentAdvisoryComplete() {
        try {
            waitUntil(ExpectedConditions.visibilityOfElementLocated(contentAdvisoryMain.getBy()), LONG_TIMEOUT);
            waitUntil(ExpectedConditions.invisibilityOfElementLocated(contentAdvisoryMain.getBy()), LONG_TIMEOUT);
        } catch (TimeoutException e){
            throw new TimeoutException(e);
        }
        return true;
    }

    public String getContentAdvisoryText() {
         return contentAdvisoryMain.getText()
                 .replace("\n", "")
                 .replace("  ", " ");
    }

    public String getContentAdvisoryCountdownText() {
         return contentAdvisoryCountdown.getText();
    }

    public boolean isSeekBarPresent(){
        return seekBar.isElementPresent();
    }

    /**
     * Taps on the scrub bar to the calculated position. Returns the remaining time as a String
     * if needed.
     * @param playbackPercent percentage of playback at which the video should start.
     */
    public String scrubToPlaybackPercentage(double playbackPercent){
        LOGGER.info("Setting video playback to {}% completed...", playbackPercent);
        playbackPercent = Double.parseDouble("." + StringUtils.remove(Double.toString(playbackPercent), "."));
        displayControlOverlay();
        new MobileUtilsExtended().clickElementAtLocation(seekBar, 50, (int) Math.round(playbackPercent * 100));
        String remainingTime = remainingTimeTextView.getText();
        LOGGER.info("Remaining time: {}", remainingTime);
        return remainingTime;
    }

    public void scrubToPercentage(double playbackPercent){
        LOGGER.info("Setting video playback to {}% completed...", playbackPercent);
        playbackPercent = Double.parseDouble("." + StringUtils.remove(Double.toString(playbackPercent), "."));
        displayControlOverlay();
        new MobileUtilsExtended().clickElementAtLocation(seekBar, 50, (int) Math.round(playbackPercent * 100));
    }

    /**
     * Scrubs to exact position on seekbar
     * @param playbackPercent
     */
    public void scrubToAbsolutePercentage(double playbackPercent) {
        LOGGER.info("Setting video playback to {}% completed...", playbackPercent);
        displayControlOverlay();
        new MobileUtilsExtended().clickElementAtAbsoluteLocation(seekBar, 50, playbackPercent);
    }

    public void scrubToUpNext(double initialScrubValue, double incrementalValue){
        MobileUtilsExtended mobileUtils = new MobileUtilsExtended();
        setScrubHorizontalPoint(initialScrubValue);
        fluentWait(getDriver(), (long) (EXTRA_LONG_TIMEOUT * 1.5), SHORT_TIMEOUT, UP_NEXT_NOT_VISIBLE)
                .until(it -> {
                    if(!upNextButton.isElementPresent(DELAY) && scrubHorizontalPoint < 1.0 - incrementalValue) {
                        videoPlayer.click();
                        playPauseButton.click();
                        mobileUtils.clickElementAtLocation(seekBar, 50, (int) Math.round(scrubHorizontalPoint * 100));
                        playPauseButton.click();
                        LOGGER.info("Increasing scrub amount by {}", incrementalValue);
                        increaseHorizontalPoint(incrementalValue);
                    }
                    return upNextButton.isElementPresent(DELAY);
                });
    }

    public boolean waitForContentRatingOverlay() {
        return fluentWait(getDriver(), LONG_TIMEOUT, SHORT_TIMEOUT, UP_NEXT_NOT_VISIBLE)
                .until(it -> contentRatingOverlayContainer.isElementPresent(ONE_SEC_TIMEOUT));
    }

    public boolean isBackButtonPresent(){
        return videoBackButton.isElementPresent();
    }

    public void tapOnPlayPauseButton() {
        displayControlOverlay();
        playPauseButton.click();
    }

    public void closeVideo(){
        displayControlOverlay();
        videoBackButton.click();
    }

    public void skipIntro(){
        displayControlOverlay();
        skipIntro.click();
    }

    public void skipRecap() {
        if(!skipRecap.isElementPresent(DELAY)) {
            displayControlOverlay();
        }
        skipRecap.click();
    }

    public boolean isSkipIntroPresent() { return skipIntro.isElementPresent(); }

    public boolean isChromecastIconPresent(){
        return chromecastButton.isElementPresent();
    }

    public boolean waitForVideoToPlay() {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(videoPlayer.getBy()), LONG_TIMEOUT / 2);
    }

    public void beginCasting(){
        displayControlOverlay();
        chromecastButton.click();
        chromecastReceiver.click();
    }

    public boolean isPlayNextEpisodeBtnPresent() {
        return fluentWait(getDriver(), EXTRA_LONG_TIMEOUT, SHORT_TIMEOUT, UP_NEXT_NOT_VISIBLE).until(it -> upNextButton.isElementPresent(ONE_SEC_TIMEOUT));
    }

    public boolean isPlayNextEpisodeBtnNotPresent() {
        return fluentWait(getDriver(), DELAY, SHORT_TIMEOUT, "Up next is visible").until(it -> !upNextButton.isElementPresent(ONE_SEC_TIMEOUT));
    }

    public void dismissPlayNextEpisodeOverlay() {
        isPlayNextEpisodeBtnPresent();
        videoPlayer.click();
        LOGGER.info("Dismissing play next episode overlay");
    }

    public void clickPlayNextEpisodeBtn(){
        upNextButton.click();
    }

    public void clickJumpBackwardButton() {
        if(!jumpBackwardButton.isElementPresent(DELAY)) {
            displayControlOverlay();
        }
        jumpBackwardButton.click();
    }

    public void clickJumpForwardButton() {
        if(!jumpForwardButton.isElementPresent(DELAY)) {
            displayControlOverlay();
        }
        jumpForwardButton.click();
    }

    public void clickVideoPlayer() {
        videoPlayer.click();
    }

    public void exitBeforeVideoStart(SoftAssert sa) {
        if (!bufferingSpinner.isVisible()) {
            closeVideo();
            sa.assertFalse(isOpened(), "Video Player did not close");
        }
        navigateBack();
        sa.assertFalse(isOpened(), "Video Player did not close");
    }

    public void exitDuringNsa(SoftAssert sa) {
        if (!isContentAdvisoryPresent()) {
            closeVideo();
            sa.assertFalse(isOpened(), "NSA did not close");
        }
        navigateBack();
        sa.assertFalse(isOpened(), "NSA did not close");
    }

    /**
     * Refer to private playVideos() prior to use.
     *
     * @param lengthInSeconds Video playback length (treated as timeout)
     * @param autoPlay Allow next video to autoplay (Autoplay functionality needs to be turned on within Profile settings)
     *
     * Primary purpose: continue playback by clicking on "Play Next Episode" button
     * Steps: set autoPlay to false.
     * When autoplay set to false, super looks for "Play Next Episode" button after every ping and clicks it once it appears.
     * Additional info: "Play Next Episode" button might be tvshow specific and appears after episode conclusion.
     *
     */
    public void playVideo(int lengthInSeconds, boolean autoPlay) {
        playVideos(lengthInSeconds, autoPlay, 0, false, 0);
    }

    /**
     * Refer to private playVideos() prior to use.
     *
     * @param lengthInSeconds Video playback length (treated as timeout)
     * @param autoPlay Allow next video to autoplay (Autoplay functionality needs to be turned on within Profile settings)
     * @param autoPlayLengthInSeconds Auto Play Video length
     * Primary purpose: continue playing videos one after another.
     * Steps: set autoPlay to true, set video playback length for next video
     *
     */
    public void playVideo(int lengthInSeconds, boolean autoPlay, int autoPlayLengthInSeconds) {
        playVideos(lengthInSeconds, autoPlay, autoPlayLengthInSeconds, false, 0);
    }

    /**
     * Refer to private playVideos() prior to use.
     *
     * @param lengthInSeconds Video playback length (treated as timeout)
     * @param autoPlay Allow next video to autoplay (Autoplay functionality needs to be turned on within Profile settings)
     * @param autoPlayLengthInSeconds Auto Play Video length
     * @param fastForwardPercent Scrub position for fast forwarding video playback
     *
     * Primary Purpose: Allows video player testing that do not require the video to play out in full
     */
    public void playVideo(int lengthInSeconds, boolean autoPlay, int autoPlayLengthInSeconds, double fastForwardPercent){
        playVideos(lengthInSeconds, autoPlay, autoPlayLengthInSeconds, true, fastForwardPercent);
    }

    /**
     * Refer to private playVideos() prior to use.
     * @param lengthInSeconds Video playback length (treated as timeout)
     *
     * Assumption: autoplay is allowed
     * Primary purpose: continue playing videos one after another.
     */
    public void playVideo(int lengthInSeconds) {
        playVideos(lengthInSeconds, true, 0, false, 0);
    }

    /**
     * After video starts streaming this function will keep video playing (with additional features).
     * Currently can be done using two approaches (defined in each public methods individually).
     *
     * @param lengthInSeconds Video playback length (can be seen as timeout)
     * @param autoPlay Allow next video to autoplay (Autoplay functionality needs to be turned on within Profile settings)
     * @param fastForward Enables driver to click on the scrubber at the designated playback duration
     * @param scrubPercent Value of desired fast-forward scrubbing (between 0 and 99.9~)
     */
    private void playVideos(int lengthInSeconds, boolean autoPlay, int autoPlayLengthInSeconds, boolean fastForward, double scrubPercent) {
        String contentTitle = getActiveMediaTitle();
        LOGGER.info("Playing content: {}", contentTitle);

        if(fastForward) {
            scrubToPlaybackPercentage(scrubPercent);
        }

        isPlayNextEpisodeBtnPresent();
        LOGGER.info("Video playback completed.");
        try {
            if (autoPlay) {
                LOGGER.info("Proceeding with AutoPlay test...");
                waitUntil(ExpectedConditions.invisibilityOfElementLocated(upNextButton.getBy()), autoPlayLengthInSeconds);
                waitForVideoBuffering();
                if (contentTitle.equals(getActiveMediaTitle())) {
                    Assert.fail("Auto Play was not triggered by the device. Navigation did not take place. No analysis conducted.");
                }
                //Allows the second video to play for a bit for network capture purposes
                TimeUnit.SECONDS.sleep(autoPlayLengthInSeconds);
            } else {
                LOGGER.info("Proceeding with Next Video Button test...");
                clickPlayNextEpisodeBtn();
                waitForVideoBuffering();
                if (contentTitle.equals(getActiveMediaTitle())) {
                    Assert.fail("Next video was not triggered by the device. Navigation did not take place. No analysis conducted.");
                }
                //Allows the second video to play for a bit for network capture purposes
                TimeUnit.SECONDS.sleep(autoPlayLengthInSeconds);
            }
        } catch (InterruptedException interruptedException) {
            LOGGER.info("Shutting down Webdriver");
            Thread.currentThread().interrupt();
            getDriver().quit();
            getDriver().close();
        }
    }

    /**
     * Gets the active video's remaining playback time and allows playback to continue until conclusion
     */
    public void playVideoToCompletion() {
        displayControlOverlay();
        playVideoUntil(getPlaybackCompletionInstant(remainingTimeTextView.getText()));
    }

    /**
     * Allows the video to play until the desired Instant
     * @param remainingTime - The Instant when the loop will terminate
     */
    public void playVideoUntil(Instant remainingTime) {
        LOGGER.info("Allowing playback for {} seconds", Duration.between(Instant.now(), remainingTime).toSeconds());

        while (Instant.now().isBefore(remainingTime)) {
            isOpened();
        }
        LOGGER.info("Playback should be completed. Proceeding with test...");
    }

    /**
     * Returns when a video's playback will be completed as an Instant
     * @param remainingTime - The String displayed in the video player's remaining time spot
     * @return - The Instant datatype the video will end.
     */
    public Instant getPlaybackCompletionInstant(String remainingTime) {
        String[] timesplitter = remainingTime.split(":");
        int hours = 0;
        int minutes = 0;
        int seconds;
        switch (timesplitter.length){
            case 3:
                hours = Integer.parseInt(timesplitter[0]) * 3600;
                minutes = Integer.parseInt(timesplitter[1]) * 60;
                seconds = Integer.parseInt(timesplitter[2]);
                break;
            case 2:
                minutes = Integer.parseInt(timesplitter[0]) * 60;
                seconds = Integer.parseInt(timesplitter[1]);
                break;
            default:
                seconds = Integer.parseInt(timesplitter[0]);
        }
        return Instant.now().plus((hours + minutes + seconds), ChronoUnit.SECONDS);
    }

    public boolean isAudioSubtitleMenuOpen() {
        return audioAndSubtitleContainer.isElementPresent(SHORT_TIMEOUT);
    }

    /**
     * The Audio and Subtitles menu is persistent and must be closed manually.
     * This checks if it's already present before proceeding
     */
    public void openAudioAndSubtitles() {
        if(!isAudioSubtitleMenuOpen()){
            displayControlOverlay();
            closedCaptionsButton.click();
        }
    }

    /**
     * Opens the Audio and Subtitles menu to select the desired Audio option
     * @param option - String of the desired option (Ex. English)
     */
    public void selectAudioOption(String option) {
        setAudioSubtitleOption(audioItem.format(option), audioList);
    }

    /**
     * Opens the Audio and Subtitles menu to select the desired Subtitle option
     * @param option - String of the desired option (Ex. English [CC])
     */
    public void selectSubtitleOption(String option) {
        setAudioSubtitleOption(subtitleItem.format(option), subtitleList);
    }

    /**
     * Opens the Audio and Subtitle menu if it's not already displayed and then makes the desired
     * selection
     * @param option - The option element within the appropriate container due to duplicate values on both
     * @param container - The container to swipe in while looking for it (Scroll does not work with this container for some reason)
     */
    private void setAudioSubtitleOption(ExtendedWebElement option, ExtendedWebElement container) {
        openAudioAndSubtitles();
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        if(!util.swipe(option, container, 10)) {
            util.swipe(option, container, AndroidUtilsExtended.Direction.DOWN, 10);
        }
        option.click();
    }

    public boolean isAudioOptionEnabled(String option) {
        openAudioAndSubtitles();
        new AndroidUtilsExtended().swipe(audioItem.format(option), subtitleList);
        return audioItemCheckmark.format(option).getAttribute("selected").equals("true");
    }

    public boolean isSubtitleOptionEnabled(String option) {
        openAudioAndSubtitles();
        new AndroidUtilsExtended().swipe(subtitleItem.format(option), subtitleList);
        return subtitleItemCheckmark.format(option).getAttribute("selected").equals("true");
    }

    public Map<String, Boolean> verifySeriesMetadata(String contentTitle, String searchApiMetadata) {
        Map<String, Boolean> metadataValidation = new HashMap<>();
        String seasonAndEpisodeRegex = "^\\S\\d:\\S\\d";
        String[] contentMetaData = contentTitle.split(" ", 2);
        String seasonAndEpisodesMetadata = contentMetaData[0];
        String seasonAndEpisodesTitle = contentMetaData[1];

        metadataValidation.put("Series and episode", seasonAndEpisodesMetadata.matches(seasonAndEpisodeRegex));
        metadataValidation.put("Episode title", searchApiMetadata.equals(seasonAndEpisodesTitle));

        LOGGER.info("Metadata validation results: {}", metadataValidation);

        return metadataValidation;
    }

    public void tapOutsidePlayerControls() {
        var dimension = playPauseButton.getSize();
        Point location = playPauseButton.getLocation();

        int x = (int) Math.round(dimension.getWidth() * 0.5);
        int y = dimension.getHeight() - 300;

        new AndroidUtilsExtended().tap(location.getX() + x, location.getY() + y, 0);
    }

    /**
     * @return if remaining time is decrementing in value
     */
    public boolean isInPlayback() {
        fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT, "Loading bar is persistent after + " + LONG_TIMEOUT + " wait")
                .until(it -> bufferingSpinner.isElementNotPresent(ONE_SEC_TIMEOUT));
        fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT, PLAYER_CONTROL_VISIBLE_ERROR_MESSAGE).until(it -> playPauseButton.isElementNotPresent(ONE_SEC_TIMEOUT));
        int initialTimeStamp = getRemainingTimeInSeconds(getRemainingTime());
        fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT, PLAYER_CONTROL_VISIBLE_ERROR_MESSAGE).until(it -> playPauseButton.isElementNotPresent(ONE_SEC_TIMEOUT));
        int remainingTimeStamp = getRemainingTimeInSeconds(getRemainingTime());
        fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT, PLAYER_CONTROL_VISIBLE_ERROR_MESSAGE).until(it -> playPauseButton.isElementNotPresent(ONE_SEC_TIMEOUT));
        LOGGER.info("First time stamp: {}, " + "Second time stamp: {}", initialTimeStamp, remainingTimeStamp);
        return initialTimeStamp > remainingTimeStamp;
    }

    public boolean checkForPlaybackConnectionErrors() {
        return errorCodeTitle.isElementWithTextPresent(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENERIC_PLAYBACK_ERROR.getText()), SHORT_TIMEOUT) ||
                errorCodeTitle.isElementWithTextPresent(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.NETWORK_CONNECTION_ERROR.getText()), SHORT_TIMEOUT) ||
                errorCodeTitle.isElementWithTextPresent(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.NETWORK_EXCEPTION_ERROR.getText()), SHORT_TIMEOUT);
    }

    public void waitForVideoToOpenAndBuffer() {
        Assert.assertTrue(isOpened() && waitForVideoBuffering(),
                "Video Player did not launch");
    }

    public boolean verifyForwardTapAction(int remainingTimeBeforeFwdDoubleTap, int remainingTimeAfterFwdDoubleTap) {
        return remainingTimeBeforeFwdDoubleTap > remainingTimeAfterFwdDoubleTap;
    }

    public boolean verifyRewindTapAction(int remainingTimeBeforeRewindTap, int remainingTimeAfterRewindTap) {
        return remainingTimeAfterRewindTap > remainingTimeBeforeRewindTap;
    }

    public boolean verifyRemainingTimePattern(String currentTime, String pattern) {
        return currentTime.matches(pattern);
    }

    public enum PlayerControls {
        FAST_FORWARD,
        REWIND,
        TAP
    }

    public void tapOnPlayerScreen(PlayerControls playerControls, int numTaps) {
        TouchAction<?> touchAction = new TouchAction<>((MobileDriver<?>) this.getCastedDriver());

        var dimension = videoPlayer.getSize();
        var location = videoPlayer.getLocation();

        int x = 0;
        int y = dimension.getHeight();
        switch (playerControls) {
            case TAP:
            case FAST_FORWARD:
                x = dimension.getWidth() + dimension.getWidth() / 2;
                break;
            case REWIND:
                x = dimension.getWidth() - dimension.getWidth() / 2;
                break;
            default:
                Assert.fail("Undefined player action");
        }

        for (int i = 0; i < numTaps; i++) {
            touchAction.tap(TapOptions.tapOptions().withPosition(
                    PointOption.point(location.getX() + x / 2, location.getY() + y / 2))).release().perform();
        }
    }

    public void tapOnRwdOrFwdButton(PlayerControls playerControls, int numTaps) {
        var touchAction = new TouchAction<>((MobileDriver<?>) this.getCastedDriver());

        Dimension dimension;
        Point location = null;

        int x = 0;
        int y = 0;
        switch (playerControls) {
            case FAST_FORWARD:
                dimension = jumpForwardButton.getSize();
                location = jumpForwardButton.getLocation();

                x = dimension.getWidth() + dimension.getWidth() / 2;
                y = dimension.getHeight();
                break;
            case REWIND:
                dimension = jumpBackwardButton.getSize();
                location = jumpBackwardButton.getLocation();

                x = dimension.getWidth() - dimension.getWidth() / 2;
                y = dimension.getHeight();
                break;
            default:
                Assert.fail("Undefined player action");
        }

        if (location == null) {
            throw new NullPointerException("Forward/Rewind button locations returned null");
        }
        touchAction.tap(TapOptions.tapOptions().withPosition(PointOption.point(location.getX() + x / 2, location.getY() + y / 2))
                .withTapsCount(numTaps)).release().perform();
    }
}
