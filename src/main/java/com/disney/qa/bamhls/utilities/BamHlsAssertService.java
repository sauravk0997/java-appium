package com.disney.qa.bamhls.utilities;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.function.Function;

public class BamHlsAssertService extends BamHlsFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public BamHlsAssertService(WebDriver driver) {
        super(driver);
    }

    //Returns state of Media Player Object
    public String getMediaPlayerStatus() {
        return trigger(RETURN + STREAM + PLAYBACK_STATE).toString();
    }

    //Retrieves Boolean value of true or false if player is Live
    public boolean isPlayerLive() {
        return Boolean.parseBoolean(trigger(RETURN + STREAM + IS_LIVE).toString());
    }

    //Retrieves playback rate
    public String getPlaybackRate() {
        return trigger(RETURN + STREAM + PLAYBACK_RATE).toString();
    }

    //Assertion to explicitly listen and assert a state specified in the status parameter, will fail after 5 cycles.
    public String getDesiredMediaPlayerStateStatus(String status) {

        int x;
        String mediaStatus = "";

        for (x = 0; x <= 5; x++) {

                if (getMediaPlayerStatus().equalsIgnoreCase(status)) {
                    mediaStatus = status;
                    break;

                }  else  {

                    LOGGER.info(String.format("Waiting due to undesired status: %s, Expected: %s", getMediaPlayerStatus(), status));
                    waitForVideoPlayer(2, 2, 2);
                    LOGGER.info(String.format("Media Player Status after pause: %s", getMediaPlayerStatus()));
                    mediaStatus = getMediaPlayerStatus();
                    x++;

                }  if (mediaStatus.equalsIgnoreCase(NOT_READY)) {

                    Assert.fail("Media Player State in NOT_READY State");
                }

            }

        LOGGER.info(String.format("Media State Returned for Assertion: %s", mediaStatus));
        return mediaStatus;

    }

    //Retrieves and returns HTTP status of current driver URL
    public String getHttpStatus() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> getUrl = restTemplate.getForEntity(driver.getCurrentUrl(), String.class);
        HttpStatus httpResponse = getUrl.getStatusCode();
        LOGGER.info(String.format("HTTP - GET - on %s, Actual: '%s'", driver.getCurrentUrl(), httpResponse));
        return httpResponse.toString();
    }

    //Assert to confirm the expected media player status
    public String assertMessageMediaPlayerStatus(String expected) {


        if (SEEKING.equalsIgnoreCase(getMediaPlayerStatus()) || BUFFERING.equalsIgnoreCase(getMediaPlayerStatus())){

            LOGGER.info("Media Player not in desired state. Pausing video for 4 seconds and checking again...");
            waitForVideoPlayer(2, 2, 4);
            expected = getMediaPlayerStatus();
            LOGGER.info(String.format("Media State Status: %s", expected));

        } else if (null == getMediaPlayerStatus()){

            Assert.fail(String.format("Null value found for media player state: %s", getMediaPlayerStatus()));

        }

        return String.format("Expected Media Player State Status (%s), Actual: %s ", expected, getMediaPlayerStatus());
    }

    //Generic Assert message for assertEquals assertions
    public String assertMessageEquals(String object ,String actual, String expected) {
        return String.format("Expected %s: '%s', Actual: '%s'", object, expected, actual);

    }

    //Generic Assert Message for assert stream time assertions
    public String assertStreamRangeTrue(String expected, float actual) {
        return String.format("Stream Time not within range: %s, actual: %s", expected, actual);
    }

    //Generic Assert Message for assertTrue
    public String assertTrueMessage(String expected, String actual) {
        return String.format("Condition not met, Expected: %s, Actual: %s", expected, actual);
    }

    //Method to ping state of video player, videoPause parameter is a long variable for seconds to pause before continuing with the waitCheck
    public boolean waitForVideoPlayer(int poll, int timeout, long videoPause) {

        try {
            FluentWait<WebDriver> wait = new FluentWait<>(getDriver());
            wait.pollingEvery(Duration.ofSeconds(poll));
            wait.withTimeout(Duration.ofSeconds(timeout));
            wait.ignoring(NoSuchElementException.class);
            wait.ignoring(TimeoutException.class);
            wait.ignoring(WebDriverException.class);

            Function<WebDriver, Boolean> waitCheck = arg0 -> {
                videoPlayerOverlay.scrollTo();
                Assert.assertTrue(videoPlayerOverlay.isElementPresent(5), "Expected: Media PLAYER to be present");
                LOGGER.info("Media Player present");
                LOGGER.info(String.format("State of Media Player: %s", getMediaPlayerStatus()));
                return videoPlayerOverlay.isElementPresent();

            };
            pause(videoPause);
            return wait.until(waitCheck);

        } catch (TimeoutException e) {
            LOGGER.error("Timeout Exception encountered", e);
            return false;
        }
    }


}
