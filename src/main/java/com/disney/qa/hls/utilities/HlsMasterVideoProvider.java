package com.disney.qa.hls.utilities;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class HlsMasterVideoProvider extends HlsAssertService implements VideoContentProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public HlsMasterVideoProvider(WebDriver driver) {
        super(driver);
    }

    private HlsFactory hF = new HlsFactory(getDriver());

    protected static final String browser = R.CONFIG.get("browser");

    // USE FOR NON-DRM STREAMS //
    public void newSession(String url) {

        LOGGER.info("Browser being tested: " + browser);
        LOGGER.info("Url: " + url);
        Dimension d = new Dimension(1920,1080);
        driver.manage().window().setSize(d);
        driver.get(url);

        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        String pageStatus = trigger("return document.readyState").toString();

        LOGGER.info("Page Status: " + pageStatus);

        if (!"complete".equals(pageStatus)) {

            for (int x = 0; x <= 5; x++) {
                refresh();
                driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

                if ("complete".equals(pageStatus)) {
                    break;
                }
            }

            Assert.fail("DOM File failed to load after multiple (5) refresh attempts.");

        }

        waitForVideoPlayer(videoPlayerOverlay, 5, 5, 8);
        LOGGER.info("Build Version #: " + getBamHlsVersion());
        Assert.assertEquals("complete", pageStatus
                , String.format("Expected Page to Fully Load, Actual: '%s", pageStatus));

    }

    /**
     * Work in Progress Error Check
     */
    protected void errorCheck() {

        if (alertMessageTxt.isElementNotPresent(5)) {
            LOGGER.info("No Error Present in Media Player. Closing DriverPool.");
        } else {
            LOGGER.info("Media Player Error: " + alertMessageTxt.getText());
        }
    }

    private String grabGuidUrl (String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> guidUrl = restTemplate.getForEntity(url.concat("/startNoRedirect"), String.class);
        String actualUrl = guidUrl.getBody();

        if (actualUrl.isEmpty()) {
            Assert.fail("Guid URL not present...");
        }

        return actualUrl;
    }

    protected boolean waitForVideoPlayer(ExtendedWebElement element, long poll, long timeout, long videoPause) {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(getDriver());
            wait.pollingEvery(poll, TimeUnit.SECONDS);
            wait.withTimeout(timeout, TimeUnit.SECONDS);
            wait.ignoring(NoSuchElementException.class);
            wait.ignoring(TimeoutException.class);
            wait.ignoring(WebDriverException.class);

            Function<WebDriver, Boolean> waitCheck = arg0 -> {
                seekToElement(hF.videoPlayerOverlay);
                element.isElementPresent(5);
                Assert.assertTrue(hF.isMediaPlayerPresent(), "Expected: Media PLAYER present");
                LOGGER.info("Media Player present");
                LOGGER.info("State of Media Player: " + getMediaStateStatus());
                return element.isElementPresent();

            };
            pause(videoPause);
            return wait.until(waitCheck);

        } catch (TimeoutException e) {
            LOGGER.error("Timeout Exception encountered", e);
            return false;
        }
    }

    protected void seekToElement(ExtendedWebElement element) {
        element.scrollTo();
    }


    /**
     * API Trigger Library Get Methods
     **/

    private Object getApiResponse(String apiCall) {
        return trigger(GET_STATUS + apiCall);
    }


    private Object getMediaStateStatus() {
        return getApiResponse(PLAYER + STATE_GET_PLAYER_STATE);
    }

    private String getApiUrlSet(String url) {
        return "var url = '" + url + "';";
    }

    public void getMediaPlayerEnableCaptions() {
        LOGGER.info("Enabling Captions...");
        trigger(PLAYER + ENABLE_CAPTIONS);
    }

    public void getMediaPlayerDisableCaptions() {
        LOGGER.info("Disabling Captions...");
        trigger(PLAYER + DISABLE_CAPTIONS);
    }

    public Object getMediaStateCaptions() {
        LOGGER.info("Captions enabled: " + getApiResponse(PLAYER + HAS_CAPTIONS).toString());
        return getApiResponse(PLAYER + HAS_CAPTIONS).toString();
    }


    /** End API Trigger Library Get Methods **/

    /**
     * API Set Language/Audio Methods
     **/

    public void setAudioTrackLanguage(String language) {
        LOGGER.info("Setting Audio Track Language to: " + language);
        trigger(PLAYER + String.format(AVAILABLE_AUDIO_TRACK_BY_LANGUAGE, language));
    }

    public void setAudioTrackNamePlayback(String name) {
        LOGGER.info("Setting Audio Track Language during playback to: " + name);
        trigger(String.format(AUDIO_TRACK_INITIALIZE, name) +
                PLAYER + String.format(AVAILABLE_AUDIO_TRACK_BY_NAME, AUDIO_TRACK_NAME));
    }

    /** End API Set Language/Audio Methods **/


    /**
     * Api Trigger Library Player State Methods
     **/

    public Object playerMediaStatePlayAsis() {
        return trigger(PLAYER + PLAY);
    }

    private void setMediaPlayerVolume(int volumeLevel) {
        LOGGER.info("Volume set to: " + volumeLevel);
        trigger(PLAYER + ".setVolume(" + volumeLevel + ");");
    }

    public void playerMuteVideo() {
        setMediaPlayerVolume(0);
    }

    private String playerVolumeChangeSet(int volume) {
        return PLAYER + String.format(SET_VOLUME, volume);
    }

    public void playerSeekSet(int offset) {
        trigger(PLAYER + String.format(PLAYER_SEEK, offset));
    }

    public void playerSeekDate() {
        trigger(PLAYER + SEEK_TO_DATE);
    }

    protected void playerSeekDateSpecified(String date) {
        LOGGER.info("Seeking to specified date: " + date);
        trigger(PLAYER + String.format(SEEK_TO_DATE_SPECIFIC, date));
    }

    protected void playerSeekStartPause() {
        LOGGER.info("Seeking to Start and Pausing...");
        trigger(PLAYER + PLAYER_SEEK_TO_START + SPACE + PLAYER + PAUSE);
    }

    protected void playerSeekLive() {
        LOGGER.info("Seeking media PLAYER to live...");
        trigger(PLAYER + SEEK_TO_LIVE);
    }

    protected void playerPause() {
        LOGGER.info("Pausing media PLAYER...");
        trigger(PLAYER + PAUSE);
    }

    public Object noBitrateDropOnSeek(String maxBitrate) {
        LOGGER.info("Allowing playout of video...");
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        waitForVideoPlayerPlayout(35, MEDIA_PLAYER_STATE_PLAYING);
        getMediaStateCurrentTimeSeconds();
        LOGGER.info("Seeking back to 20s...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 20));
        getMediaStateCurrentTimeSeconds();
        Object seek1 = trigger (GET_STATUS + PLAYER + GET_CURRENT_BITRATE);
        LOGGER.info("Seek 1 rate: "+seek1);
        LOGGER.info("Seeking back to 10s...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 10));
        getMediaStateCurrentTimeSeconds();
        Object seek2 = trigger (GET_STATUS + PLAYER + GET_CURRENT_BITRATE);
        LOGGER.info("Seek 2 rate: "+seek2);
        if ((seek1.toString()).contains(maxBitrate) && (seek2.toString()).contains(maxBitrate)) {
            return "__bitrate_did_not_drop";
        } else
            return "__bitrate_dropped";
    }

    protected Object seekFutureSeekPast() {
        LOGGER.info("Allowing playout of video...");
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        waitForVideoPlayerPlayout(10, MEDIA_PLAYER_STATE_PLAYING);
        LOGGER.info("Pausing video to allow buffer for future seek...");
        trigger(PLAYER + PAUSE);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PAUSED,"State is not paused");
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        LOGGER.info("Resuming playback...");
        trigger(PLAYER + PLAY);
        Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_PLAYING, "State is not playing");
        LOGGER.info("Seeking into future...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 15));
        Object ftime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Future time: " + ftime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into past...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 5));
        Object ptime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Past time: " + ptime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("State of Media Player: " + getMediaStateStatus());
        if (getMediaStateStatus().equals(MEDIA_PLAYER_STATE_PLAYING)) {
            return "Media remains in a playing state";
        } else
            return "Media is not in a playing state";
    }

    protected Object seekForwardMultipleTimes() {
        LOGGER.info("Pausing video to allow buffer for future seek...");
        trigger(PLAYER + PAUSE);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PAUSED,"State is not paused");
        waitForVideoPlayer(videoPlayerOverlay,1,31,30);
        LOGGER.info("Resuming playback...");
        trigger(PLAYER + PLAY);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into future to 10 seconds...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 10));
        Object firstTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + firstTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into future to 20 seconds...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 20));
        Object secondTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + secondTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into future to 30 seconds...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 30));
        Object thirdTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + thirdTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("State of Media Player: " + getMediaStateStatus());
        if (getMediaStateStatus().equals(MEDIA_PLAYER_STATE_PLAYING)) {
            return "Media remains in a playing state";
        } else
            return "Media is not in a playing state";
    }

    protected Object seekBackwardMultipleTimes() {
        LOGGER.info("Allowing playout of video...");
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        waitForVideoPlayerPlayout(30, MEDIA_PLAYER_STATE_PLAYING);
        LOGGER.info("Seeking into past to 20 seconds...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 20));
        Object firstTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + firstTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into past to 10 seconds...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 10));
        Object secondTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + secondTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into past to 0 seconds...");
        trigger(PLAYER + String.format(PLAYER_SEEK, 0));
        Object thirdTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + thirdTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("State of Media Player: " + getMediaStateStatus());
        if (getMediaStateStatus().equals(MEDIA_PLAYER_STATE_PLAYING)) {
            return "Media remains in a playing state";
        } else
            return "Media is not in a playing state";
    }

    protected Object seekBackwardMultipleTimesFromTail() {
        LOGGER.info("Allowing video to complete...");
        waitForVideoPlayerEnd(60);
        LOGGER.info("Verifying video playback has completed...");
        Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_ENDED, "State is not ended");
        Object endTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("End time: " + endTime);
        double eTime = (Double) endTime;
        LOGGER.info("Seeking into past to (End time - 100 seconds)...");
        trigger(PLAYER + String.format(PLAYER_SEEK, eTime - 100));
        Object firstTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + firstTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into past to (End time - 200 seconds)...");
        trigger(PLAYER + String.format(PLAYER_SEEK, eTime - 200));
        Object secondTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + secondTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("Seeking into past to (End time - 300 seconds)...");
        trigger(PLAYER + String.format(PLAYER_SEEK, eTime - 300));
        Object thirdTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        LOGGER.info("Current time: " + thirdTime);
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        Assert.assertEquals(getMediaStateStatus(),MEDIA_PLAYER_STATE_PLAYING,"State is not playing");
        LOGGER.info("State of Media Player: " + getMediaStateStatus());
        if (getMediaStateStatus().equals(MEDIA_PLAYER_STATE_PLAYING)) {
            return "Media remains in a playing state";
        } else
            return "Media is not in a playing state";
    }

    protected Object slideResumesAtLive() {
        LOGGER.info("Allowing playout of video...");
        waitForVideoPlayer(videoPlayerOverlay,1,6,5);
        waitForVideoPlayerPlayout(35, MEDIA_PLAYER_STATE_PLAYING);
        LOGGER.info("Pausing video...");
        trigger(PLAYER + PAUSE);
        Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_PAUSED, "State is not paused");
        LOGGER.info("State of Media Player: " + getMediaStateStatus());
        LOGGER.info("Waiting for window to pass...");
        waitForVideoPlayer(videoPlayerOverlay,1,46,45);
        LOGGER.info("Resuming playback...");
        trigger(PLAYER + PLAY);
        Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_PLAYING, "State is not playing");
        LOGGER.info("State of Media Player: " + getMediaStateStatus());
        LOGGER.info("Validating playhead is at live point...");
        Object isLive = getMediaLiveState();
        return isLive.toString();
    }

    public Object keyRotationFastFail() {
        if ("ie".equals(browser) || "safari".equals(browser)) {
            Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_ERROR, "Player did not fast fail");
            return "Test completed";
        } else
            Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_PLAYING, "Player fast failed");
        return "Test completed";
    }

    protected Object keyRotationPlayThroughChange() {
        if ("ie".equals(browser) || "safari".equals(browser)) {
            Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_ERROR, "Player did not fast fail");
            return "Test completed";
        } else {
            LOGGER.info("Allowing playout of video (125s)...");
            waitForVideoPlayerPlayout(125, MEDIA_PLAYER_STATE_PLAYING);
            LOGGER.info("Validating state...");
            Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_PLAYING, "State is not playing");
            return "Test completed";
        }
    }

    protected Object keyRotationPlayThroughChangeAndSeekBack() {
        if ("ie".equals(browser) || "safari".equals(browser)) {
            Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_ERROR, "Player did not fast fail");
            return "Test completed";
        } else {
            LOGGER.info("Allowing playout of video (125s)...");
            waitForVideoPlayerPlayout(125, MEDIA_PLAYER_STATE_PLAYING);
            Object currentTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
            LOGGER.info("Current time: " + currentTime);
            double cTime = (Double) currentTime;
            LOGGER.info("Seeking into past to (Current time - 60 seconds)...");
            trigger(PLAYER + String.format(PLAYER_SEEK, cTime - 60));
            waitForVideoPlayer(videoPlayerOverlay,1,6,5);
            LOGGER.info("Validating state...");
            Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_PLAYING, "State is not playing");
            Object currentTime2 = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
            LOGGER.info("Current time: " + currentTime2);
            double cTime2 = (Double) currentTime2;
            LOGGER.info("Seeking into past to (Current time - 60 seconds)...");
            trigger(PLAYER + String.format(PLAYER_SEEK, cTime2 - 60));
            waitForVideoPlayer(videoPlayerOverlay,1,6,5);
            Object currentTime3 = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
            LOGGER.info("Current time: " + currentTime3);
            LOGGER.info("Validating state...");
            Assert.assertEquals(getMediaStateStatus(), MEDIA_PLAYER_STATE_PLAYING, "State is not playing");
            return "Test completed";
        }
    }

    //This method uses an event listener to capture the volume audio level.
    public Object playerGetVolumeLevelStatus(int volume) {
        Object volumeLevel = trigger(
                OUT_PUT_INITIALIZE + VOLUME_CHANGE_LISTENER + playerVolumeChangeSet(volume) + GET_STATUS + VOLUME_STATE);
        LOGGER.info("Volume level is: " + volumeLevel);
        return volumeLevel.toString();
    }

    protected void mediaPlayerPlayUrlWithSetAudio(String url, String audioLanguage) {
        trigger("var url = '" + url + "';" +
                "var lang = '" + audioLanguage + "';" +
                "var clip = {path:url, initialAudioLanguage:lang};" +
                "var playlist = [clip];" +
                "this_is_not_a_player.play(playlist);");
    }


    /** End Api Trigger Library Player State Methods **/


    /**
     * Start Player Width Control
     **/

    protected void playerWidthIncrease() {
        playerWidthSlider.isElementPresent(5);
        String value = playerWidthSlider.getAttribute("value");
        int ivalue = Integer.parseInt(value);
        LOGGER.info("Slider width value: " + ivalue);
        int width;
        for (width = ivalue; width <= 1920; width += 16) {
            playerWidthSlider.getElement().sendKeys(Keys.ARROW_RIGHT);
            pause(.5);
        }
        waitForVideoPlayer(videoPlayerOverlay,1,31,30);
    }

    protected void playerWidthDecrease() {
        playerWidthSlider.isElementPresent(5);
        String value = playerWidthSlider.getAttribute("value");
        int ivalue = Integer.parseInt(value);
        LOGGER.info("Slider width value: " + ivalue);
        int width;
        for (width = ivalue; width >= 320; width -= 16) {
            playerWidthSlider.getElement().sendKeys(Keys.ARROW_LEFT);
            pause(.5);
        }
        waitForVideoPlayer(videoPlayerOverlay,1,31,30);
    }

    /** End Player Width Control Methods **/

    /**
     * Start Playback Rate Methods
     **/

    protected void setPlaybackRate(String rate) {
        LOGGER.info("Buffering content for 10 seconds...");
        playerPause();
        waitForVideoPlayer(videoPlayerOverlay,1,11,10);
        playerMediaStatePlayAsis();
        LOGGER.info("Setting playback rate...");
        trigger(PLAYER + ".playback.setPlaybackRate(" + rate + ");");
    }

    /**
     * End Playback Rate Methods
     **/
    @Override
    public boolean waitForVideoPlayerPresence() {
        seekToElement(videoPlayerOverlay);
        return waitForVideoPlayer(videoPlayerOverlay, 5, 5, 2);
    }

    private void waitForVideoPlayerPlayout(int length, String state) {
        String status;
        int timeout = 0;

        do {
            status = getMediaStateStatus().toString();
            if (status.equalsIgnoreCase(MEDIA_PLAYER_STATE_BUFFERING)) {
                waitForVideoPlayer(videoPlayerOverlay,1,11,10);
            }
            Assert.assertEquals(status,state,"PLAYER state is not: " + state);
            LOGGER.info("Validating player state during playout: " + status);
            LOGGER.info(String.format("Waiting %s: Current:%d", length, timeout));
            waitForVideoPlayer(videoPlayerOverlay,1,2,1);
            timeout++;
        } while (status.equals(state) && timeout <= length);

    }

    private void waitForVideoPlayerEnd(int length) {
        String status;
        int timeout = 0;

        do {
            status = getMediaStateStatus().toString();
            Assert.assertFalse(status.contains("error"), "The player is in an error state");
            LOGGER.info("Validating player state until completion: " + status);
            LOGGER.info(String.format("Waiting %s: Current:%d", length, timeout));
            waitForVideoPlayer(videoPlayerOverlay,1,2,1);
            timeout++;
        } while (!status.equals(MEDIA_PLAYER_STATE_ENDED) && timeout <= length);
    }

    @Override
    public <T> T getMediaPlayByUrl(String url) {

        if ("ie".equals(browser)) {
            String newLink = grabGuidUrl(url);

            LOGGER.info("Playing IE Video GUID URL: " + newLink);

            return (T) trigger(getApiUrlSet(newLink) +
                    "var clip = {path:url};" +
                    "var playlist = [clip];" +
                    "this_is_not_a_player.play(playlist);");

        } else {
            LOGGER.info("Playing video: " + url);

            return (T) trigger(getApiUrlSet(url) +
                    "var clip = {path:url};" +
                    "var playlist = [clip];" +
                    "this_is_not_a_player.play(playlist);");
        }
    }

    @Override
    public <T> T getMediaPlayByUrl(String url, int offset) {

        if ("ie".equals(browser)) {
            String newLink = grabGuidUrl(url);

            LOGGER.info("Playing IE Video GUID URL: " + newLink);
            LOGGER.info("Offset: " + offset);

            return (T) trigger(getApiUrlSet(newLink) +
                    "var clip = {path:url, startTime: {offset:"+offset+"}};" +
                    "var playlist = [clip];" +
                    "this_is_not_a_player.play(playlist);");

        } else {

            LOGGER.info("Playing Video: " + url);
            LOGGER.info("Offset: " + offset);

            return (T) trigger(getApiUrlSet(url) +
                    "var clip = {path:url, startTime: {offset:"+offset+"}};" +
                    "var playlist = [clip];" +
                    "this_is_not_a_player.play(playlist);");
        }
    }

    @Override
    public <T> T getMediaPlayByUrl(String url, String date) {

        if ("ie".equals(browser)) {
            String newLink = grabGuidUrl(url);

            LOGGER.info("Playing IE Video GUID URL: " + newLink);
            LOGGER.info("Playing Date: " + date);

            return (T) trigger(getApiUrlSet(newLink) +
                    "var clip = {path:url, startTime: {date:'"+date+"'}};" +
                    "var playlist = [clip];" +
                    "this_is_not_a_player.play(playlist);");

        } else {

            LOGGER.info("Playing Video: " + url);
            LOGGER.info("Playing Date: " + date);

            return (T) trigger(getApiUrlSet(url) +
                    "var clip = {path:url, startTime: {date:'"+date+"'}};" +
                    "var playlist = [clip];" +
                    "this_is_not_a_player.play(playlist);");
        }
    }

}
