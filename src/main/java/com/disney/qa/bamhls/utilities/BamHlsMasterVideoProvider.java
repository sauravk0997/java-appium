package com.disney.qa.bamhls.utilities;

import com.qaprosoft.carina.core.foundation.utils.R;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class BamHlsMasterVideoProvider extends BamHlsAssertService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public BamHlsMasterVideoProvider(WebDriver driver) {
        super(driver);
    }

    protected static final String BROWSER = R.CONFIG.get("browser");
    private static final String TEST_ENVIRONMENT = R.CONFIG.get("env");
    private static final String TEST_HARNESS_URL = R.CONFIG.get("url");
    private static final String STREAM_VALUES = R.CONFIG.get("custom_string");
    private static final String STREAM_LABEL = R.CONFIG.get("custom_string2");


    //Starts WebDriver to open browser and load test url
    public void newSession(String localTestHarness, int width, int height, int timeoutSeconds) {
        initialTestSetup(width, height);
        setTestHarnessUrl(localTestHarness);
        checkIfPageLoaded(timeoutSeconds);
    }

    //Universal Test Setup Method for all BAMHLS Harness Versions
    private void initialTestSetup(int width, int height) {
        LOGGER.info("Browser: " + BROWSER);
        LOGGER.info("Environment: " + TEST_ENVIRONMENT);
        Dimension d = new Dimension(width, height);
        driver.manage().window().setSize(d);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }

    //This method loads the appropriate test harness url depending on the test environment set in config
    private void setTestHarnessUrl(String localTestHarness) {
        LOGGER.info("Setting Test Harness depending on environment....");
        LOGGER.info(String.format("Environment selected: %s", TEST_ENVIRONMENT));

        switch (TEST_ENVIRONMENT.toUpperCase()) {
            case "LOCAL":
                LOGGER.info("Testing Locally....");
                driver.get(localTestHarness);
                break;
            case "MASTER":
                LOGGER.info(String.format("Testing Master Harness: %s", BamHlsParameter.BAMHLS_MASTER_URL.getValue()));
                driver.get(BamHlsParameter.BAMHLS_MASTER_URL.getValue());
                break;
            case "DEV":
                LOGGER.info(String.format("Testing Development Harness: %s", BamHlsParameter.BAMHLS_DEVELOP_URL.getValue()));
                driver.get(BamHlsParameter.BAMHLS_DEVELOP_URL.getValue());
                break;
            case "CIDEV":
                LOGGER.info("Testing against Developer Test values...");
                driver.get(BamHlsParameter.BAMHLS_DEVELOP_URL.getValue());
                break;
            case "CIMASTER":
                LOGGER.info("Testing against Master Test values...");
                driver.get(BamHlsParameter.BAMHLS_MASTER_URL.getValue());
                break;
            default:
                Assert.fail("No Environment Chosen");
                break;
        }
    }

    //This method verifies that the Dom file loaded, fails test if it does not
    private void checkIfPageLoaded(int timeoutSeconds) {

        int x = 0;

        String pageStatus = trigger("return document.readyState").toString();
        String complete = "complete";

        LOGGER.info("Page Status: " + pageStatus);

        while (!complete.equalsIgnoreCase(pageStatus) || x >= timeoutSeconds) {

            LOGGER.info("Refreshing Page....");
            refresh();
            x++;

        }

        Assert.assertEquals(complete, pageStatus,
                String.format("Expected pageStatus (complete), Actual: '%s'", pageStatus));

    }

    //Method to play Stream. Handles different test environment and plays desired stream based on environment.

    public void loadStreamUrlLabel(String streamUrlLocal, String labelLocal) {

        String testJson = "";
        String testLabel = "";
        String developerValue = STREAM_VALUES;
        String developerLabel = STREAM_LABEL;

        if (TEST_ENVIRONMENT.equalsIgnoreCase("local")) {
            LOGGER.info("Local Testing....");
            testJson = streamUrlLocal;
            testLabel = labelLocal;
        } else if (TEST_ENVIRONMENT.equalsIgnoreCase("dev") || TEST_ENVIRONMENT.equalsIgnoreCase("master")) {
            LOGGER.info("Development Testing...");
            if (TEST_HARNESS_URL.contains("development")) {
                LOGGER.info(String.format("Testing bamHls Development Harness: %s", TEST_HARNESS_URL));
            } else if (TEST_HARNESS_URL.contains("master")) {
                LOGGER.info(String.format("Testing MDRM Master Harness: %s", TEST_HARNESS_URL));
            }
            testJson = streamUrlLocal;
            testLabel = labelLocal;
        } else if (TEST_ENVIRONMENT.equalsIgnoreCase("cimaster") || TEST_ENVIRONMENT.equalsIgnoreCase("cidev")) {
            LOGGER.info("Developer Triggered Custom Job...");
            LOGGER.info(String.format("Development Test Suite: %s", developerValue));
            LOGGER.info(String.format("Development Label: %s", developerLabel));
            testJson = developerValue;
            testLabel = developerLabel;
        } else {
            Assert.fail(String.format("Invalid Test Environment set: %s", TEST_ENVIRONMENT));
        }

        suiteSelection.isElementPresent();
        selectSuite(testJson);
        LOGGER.info(String.format("Test Suite Selected: %s", testJson));

        waitForVideoPlayer(2, 2, 2);

        videoSelection.isElementPresent();
        selectVideo(testLabel);
        LOGGER.info(String.format("Label: %s", testLabel));

        grabGuidUrl(testLabel);

    }

    //Clicks switchVideo element to play video
    public void playVideoClick() {
        waitForVideoPlayer(2, 2, 2);
        switchVideoBtn.isElementPresent();
        switchVideoBtn.click();
        waitForVideoPlayer(2, 2, 2);
    }

    private void grabGuidUrl(String testLabel) {

        if (BROWSER.equalsIgnoreCase("ie")
                && TEST_ENVIRONMENT.equalsIgnoreCase("MASTER")
                || TEST_ENVIRONMENT.equalsIgnoreCase("CIMASTER")) {
            RestTemplate restTemplate = new RestTemplate();

            LOGGER.info("Internet Explorer detected, attempting to grab GUID URL redirect....");

            ResponseEntity<String> guidUrl = restTemplate.getForEntity(testLabel.concat("/startNoRedirect"), String.class);
            String actualUrl = guidUrl.getBody();

            LOGGER.info(String.format("URL Retrieved: %s", actualUrl));
            videoUrlField.type(actualUrl);

        }

    }

    //Returns playback state of the media player - READY, PAUSED, BUFFERING, ERROR
    protected String getPlaybackState() {
        return trigger(RETURN + STREAM + PLAYBACK_STATE).toString();
    }

    public String getLanguageArraySpecified(String language) {

        Iterator audioTracksArray = getAudioTrackSelectList().iterator();
        String valueAssert = "";

        while (audioTracksArray.hasNext()) {

            valueAssert = audioTracksArray.next().toString();

            if (valueAssert.equalsIgnoreCase(language)) {

                break;

            }

    }

        return valueAssert;
    }

    //Grabs Current Bit rate displayed on page
    public String getCurrentBitRateUi() {

        return bitRatesSelected.getText();
    }

    //Sets Audio Track in UI interface with click
    protected void setAudioTrackUiBtn(String audioTrack) {

        setAudioTrackUi(audioTrack).click();
    }

    protected void setPlaybackRate (double playbackRate) {
        LOGGER.info(String.format("Setting Playback Rate: %s", playbackRate));
        trigger(STREAM + PLAYBACK_RATE + " = " + playbackRate);
    }

    //Set start time before video is loaded
    protected void setStartTimeField(int length) {

        LOGGER.info(String.format("Setting start time before playing stream. Seek: %s", Integer.toString(length)));
        startTimeField.isElementPresent(3);
        startTimeField.type(Integer.toString(length));
        startTimeField.isElementWithTextPresent(Integer.toString(length));

    }

    //Seek Time PDT format
    protected void setStartTimePDTField(String startTimePDT) {

        LOGGER.info(String.format("Setting start time before playing stream. Seek: %s", startTimePDT));
        startTimePDTField.isElementPresent(3);
        startTimePDTField.type(startTimePDT);
        startTimePDTField.isElementWithTextPresent(startTimePDT);

    }

    //Seek offset by Integer
    public void seekOffsetInt(int offsetValue) {
        LOGGER.info(String.format("Seeking: %s", offsetValue));
        trigger(RETURN + STREAM + setSeekInt(offsetValue));
        LOGGER.info(String.format("Current Stream Time: %s", getCurrentStreamTime()));
    }

    //Seek offset by string
    public void seekOffsetString(String offsetValue) {
        LOGGER.info(String.format("Seeking: %s", offsetValue));
        trigger(RETURN + STREAM + setSeekPDT(offsetValue));
        LOGGER.info(String.format("Current Stream Time: %s", getCurrentStreamTime()));
    }

    //Seeks offset backwards by integer value in parameter
    public void seekVideoRewind(int offset) {

        offsetSeekValue.isElementPresent();
        offsetSeekValue.type(Integer.toString(offset));
        seekBack.isElementPresent();

        LOGGER.info(String.format("Current Stream Time: %s", getCurrentStreamTime()));
        LOGGER.info(String.format("Rewinding offset by %ss", offset));

        seekBack.click();
        LOGGER.info(String.format("Current Offset after Rewind: %s", getCurrentStreamTime()));

    }

    //Seeks offset forward by integer value in parameter
    public void seekVideoForward(int offset) {


        offsetSeekValue.isElementPresent();
        offsetSeekValue.type(Integer.toString(offset));
        LOGGER.info("Seek passed: " + offsetSeekValue.getText());
        seekForward.isElementPresent();

        LOGGER.info(String.format("Current Stream Time: %s", getCurrentStreamTime()));
        LOGGER.info(String.format("Forwarding offset by %ss", offset));

        seekForward.click();
        LOGGER.info(String.format("Current Offset after Forward: %s", getCurrentStreamTime()));


    }

    //Seeks forward from current offset by integer value in parameter
    public void seekForwardCurrentOffset(int offset) {

        LOGGER.info(String.format("Current Time: %s", getCurrentStreamTime()));
        LOGGER.info(String.format("Seeking forward %s", offset));
        trigger(STREAM + setCurrentSeekForward(offset));

    }

    //Seeks backward from current offset by integer value in parameter
    public void seekBackwardCurrentOffset(int offset) {

        LOGGER.info(String.format("Current Time: %s", getCurrentStreamTime()));
        LOGGER.info(String.format("Seeking backward %s", offset));
        trigger(STREAM + setCurrentSeekBackward(offset));

    }

    //Pause Video
    public void pauseVideo(int pause) {
        LOGGER.info("Pausing video...");
        trigger(PAUSE_VIDEO);
        waitForVideoPlayer(2, 2, pause);
    }

    //Play Video
    public void playVideo(int pause) {
        LOGGER.info("Playing video...");
        trigger(STREAM + PLAY);
        waitForVideoPlayer(2, 2, pause);
    }

    //Get Caption State - Converts Object to Boolean for boolean method assertion
    public boolean getCaptionStatus() {
        boolean captionStatus = Boolean.parseBoolean(trigger(RETURN + STREAM + ARE_SUBTITLES_ENABLED).toString());
        LOGGER.info(String.format("Are Captions Enabled: %s", captionStatus));
        return captionStatus;
    }

    //Enable Captions
    public void enableCaptions() {
        LOGGER.info("Enabling Captions...");
        trigger(STREAM + ARE_SUBTITLES_ENABLED_TRUE);
    }

    //Disable Captions
    public void disableCaptions() {
        LOGGER.info("Disabling Captions...");
        trigger(STREAM + ARE_SUBTITLES_ENABLED_FALSE);
    }

    //Method to enable or disable captions, pass true or false to enable or disable
    public void playUrlCaptions(boolean status) {

        if (status) {
            enableCaptions();
        } else {
            disableCaptions();
        }
        waitForVideoPlayer(2, 2, 1);

    }

    //Loop to wait for the video player to end
    protected void waitForVideoPlayerEnd(int length) {

        String status;
        int timeout = 0;

        do {
            status = getPlaybackState();
            Assert.assertFalse(status.equalsIgnoreCase("error"), "The player is in an error state");
            LOGGER.info(String.format("Validating player state until completion: %s", status));
            LOGGER.info(String.format("Waiting %ss: Time Elapsed: %ss", length, timeout));
            waitForVideoPlayer(1, 2, 1);
            timeout++;

        } while (!status.equalsIgnoreCase("ended") && timeout <= length);

    }

    //Wait for the video player to play stream. Pause time is specified amount of seconds to let stream play. Boolean rangeRestriction if needed to wait regardless of stream range.
    public void waitForVideoPause(int pauseTime, boolean noRangeRestriction) {

        float currentStreamTime = getCurrentStreamTime();

        waitForVideoPlayer(2, 2, 0);

        float initialStreamTime = getCurrentStreamTime();
        LOGGER.info(String.format("Current Offset time: %s", initialStreamTime));

        float timeCaptured = getCurrentStreamTime();
        LOGGER.info(String.format("Offset Captured before wait: %s", timeCaptured));

        float streamRange = getStreamRange();

        LOGGER.info("Stream Range: " + streamRange);

        if (pauseTime >= 0 && pauseTime <= streamRange || noRangeRestriction ) {
            LOGGER.info(String.format("Letting video play for %ss", pauseTime));
            waitForVideoPlayer(2, 2, pauseTime);
            currentStreamTime = getCurrentStreamTime();

        } else {
            Assert.fail(String.format("Offset Range Value incompatible with stream. Pause specified: %ss, StreamRange: %ss", pauseTime, streamRange));
        }

        LOGGER.info(String.format("Initial Stream Time: %s", initialStreamTime));
        LOGGER.info(String.format("Current Stream Time: %s", currentStreamTime));

    }


}