package com.disney.qa.espn.android.pages.media;

import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

import static io.appium.java_client.MobileBy.AndroidUIAutomator;

/**
 * ESPN - Video playback page
 *
 * @author bzayats
 */
public abstract class EspnVideoPageBase extends EspnPageBase {

    //Toolbar
    @FindBy(id = "com.espn.score_center:id/mvpdLogo")
    private ExtendedWebElement logo;

    //video player (ESPN doesn't expose media player controls at this point. Will leave these as is for now)
    @FindBy(id = "video_frame")
    private ExtendedWebElement mediaPlayerFrame;

    @FindBy(id = "com.espn.score_center:id/playPause")
    private ExtendedWebElement mediaPlayerPlayBtn;

    @FindBy(id = "progressBar")
    private ExtendedWebElement mediaPlayerProgressBar;

    @FindBy(id = "com.espn.score_center:id/live_indicator")
    private ExtendedWebElement mediaPlayerLiveIndicator;

    @FindBy(id = "com.espn.score_center:id/controls_pause_button")
    private ExtendedWebElement mediaPlayerPauseBtn;

    @FindBy(id = "com.espn.score_center:id/closedCaptions")
    private ExtendedWebElement mediaPlayerCCBtn;

    @FindBy(id = "com.espn.score_center:id/fullscreenToggle")
    private ExtendedWebElement mediaPlayerMaximizeBtn;

    @FindBy(id = "com.espn.score_center:id/fullscreenToggle")
    private ExtendedWebElement mediaPlayerMinimizeBtn;

    @FindBy(id = "com.espn.score_center:id/seekBar")
    private ExtendedWebElement mediaPlayerSeekBar;

    @FindBy(id = "com.espn.score_center:id/totalTime")
    private ExtendedWebElement mediaPlayerTotalTime;

    @FindBy(id = "com.espn.score_center:id/currentTime")
    private ExtendedWebElement mediaPlayerCurrentTime;

    //Feeds
    @FindBy(id = "com.espn.score_center:id/live_card_watched_button_text")
    private ExtendedWebElement moreLiveBtn;

    @FindBy(id = "com.espn.score_center:id/live_card_recycler_view")
    private ExtendedWebElement feedItem;

    @FindBy(id = "com.espn.score_center:id/cast_controller_container")
    private ExtendedWebElement mediaPlayer;

    public EspnVideoPageBase(WebDriver driver) {
        super(driver);
    }

    /**
     * check if media player controls open
     * @return boolean
     */
    public boolean checkMediaPlayerControlsPresent() {
        List<Boolean> results = new ArrayList<>();

        /* do NOT delete commented out code. I'ts
           commented due to DRM limiting access to video controls
         */
//        activateVideoPlayerControls();
        mediaPlayerFrame.clickIfPresent();
//        results.add(mediaPlayerPlayBtn.isElementPresent());
//        results.add(mediaPlayerCurrentTime.isElementPresent());
//        results.add(mediaPlayerCCBtn.isElementPresent());
        results.add(mediaPlayerSeekBar.isElementPresent());
//        results.add(mediaPlayerTotalTime.isElementPresent());
//        results.add(mediaPlayerMaximizeBtn.isElementPresent());

        LOGGER.info("Results:" + results);

        return results.contains(false);
    }

    /**
     * check if media player controls > scrubber can be moved
     * @return boolean
     */
    public boolean dragMediaPlayerControlsScrubber(int xEnd) {
        String startTime = "";
        String timeAfterSliderIsMoved = "";

        EspnCommonPageBase espnCommonPageBase = initPage(EspnCommonPageBase.class);
//        activateVideoPlayerControls();
        mediaPlayerFrame.clickIfPresent();

        if (mediaPlayerPlayBtn.isElementPresent()) {
            startTime = mediaPlayerTotalTime.getText();
            LOGGER.info("Current playback time: " + startTime);
            espnCommonPageBase.dragToCoords(xEnd);
            timeAfterSliderIsMoved = mediaPlayerTotalTime.getText();
            LOGGER.info("Current playback time: " + timeAfterSliderIsMoved);
        }

        return timeAfterSliderIsMoved.equals(timeAfterSliderIsMoved);
    }

    /**
     * verify that video can be paused/resumed
     * @return boolean
     */
    public boolean pauseResumeVideo() {
        List<Boolean> results = new ArrayList<>();

//        activateVideoPlayerControls();
        mediaPlayerFrame.clickIfPresent();

        if (mediaPlayerPlayBtn.isElementPresent()) {
            mediaPlayerPlayBtn.clickIfPresent();
            results.add(mediaPlayerPlayBtn.isElementPresent());
            mediaPlayerPlayBtn.clickIfPresent();
            results.add(mediaPlayerPlayBtn.isElementPresent());
        }

        return results.contains(false);
    }

    /**
     * check if media player's pause btn present
     * @return boolean
     */
    public boolean mediaPlayerPlayBtn() {
        return mediaPlayerPlayBtn.isElementPresent(LONG_TIMEOUT);
    }

    /**
     * check if media player's present
     * @return boolean
     */
    public boolean isMediaPlayerPresent() {
        return mediaPlayerFrame.isElementPresent(LONG_TIMEOUT);
    }

    /**
     * click close Playing Video
     */
    public void closePlayingVideo() {
        clickNavUpBtn();
    }

    /** Video Player > get video player seekBar **/
    public ExtendedWebElement getMediaPlayerSeekBar() {
        return mediaPlayerSeekBar;
    }

    /** Video Player > wait till spinner is gone **/
    public boolean waitTillProgressBarNotPresent() {
        initPage(EspnCommonPageBase.class).waitUntilNotPresent(mediaPlayerProgressBar);

        return !mediaPlayerProgressBar.isElementPresent(3);
    }

    /**Video Player > Confirm Wait until progress bar is present**/
    public boolean waitUntilProgressBarPresent(){
        initPage(EspnCommonPageBase.class).waitUntilVisible(mediaPlayerProgressBar);

        return mediaPlayerProgressBar.isElementPresent();
    }

    /** Video Player > wait till controls are dismissed **/
    public boolean waitTillControlsNotPresent() {
        initPage(EspnCommonPageBase.class).waitUntilNotPresent(mediaPlayerPlayBtn);

        return !mediaPlayerPlayBtn.isElementPresent(3);
    }

    /** Video Player > get playback's current time **/
    public String getMediaPlayerCurrentTime() {

        return mediaPlayerCurrentTime.getText();
    }

    /** Overload for avoiding Core Appium interruption error **/
    public void activateVideoPlayerControls(){
        double xCoor = 1.09;
        double yCoor = 5.2;

        //TODO: change back to this after QAA-3523 is resolved
//        initPage(EspnCommonPageBase.class).clickCoords(x, y);

        //workaround for QAA-3523
        Dimension scrSize;

        scrSize = mediaPlayerFrame.getSize();

        int x = (int) (scrSize.width / xCoor);
        int y = (int) (scrSize.height / yCoor);

        int count = 0;

        while(count < 5) {
//            MobileUtils.tap(x, y);
            WebDriver drv = getDriver();
            drv.findElement(AndroidUIAutomator("UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).click("+ x +", "+ y +");"));

            if (mediaPlayerPlayBtn.isElementPresent(3)){
                LOGGER.info("Media frame controls visible..");
                break;
            }

            count ++;
            LOGGER.info("Media player controls not visible.. attempt '" + count
                    + "' of 5 ");
        }
    }

    /** Video Player > activate/de-activate video player controls **/
    public void activateVideoPlayerControls(Dimension scrSize){
        double x = 1.09;
        double y = 5.2;

        initPage(EspnCommonPageBase.class).clickCoords(scrSize, x, y);
    }

    /** Video Player > return skipped playback time duration **/
    public boolean isTimeDurationWithinThreshold(int timeThresholdRightBound, String currentTime,
                                                 String newCurrentTime){

        DateTime initialTime  = new DateTime().withTime(Integer.valueOf(extractHours(currentTime)), Integer.valueOf(extractMinutes(currentTime)),
                Integer.valueOf(extractSeconds(currentTime)), 0);
        LOGGER.info("Date time parsed: " + initialTime.toString());
        DateTime newInitialTime  = new DateTime().withTime(Integer.valueOf(extractHours(newCurrentTime)), Integer.valueOf(extractMinutes(newCurrentTime)),
                Integer.valueOf(extractSeconds(newCurrentTime)), 0);
        LOGGER.info("Date time parsed: " + newInitialTime.toString());

        Period changeOfplaybackTime = new Period(initialTime, newInitialTime);
        LOGGER.info("Trip length in seconds: " + changeOfplaybackTime.getSeconds());

        return (Math.abs(changeOfplaybackTime.getSeconds()) <= timeThresholdRightBound);
    }

    /** Video Player > extract seconds from current time string **/
    public String extractSeconds(String timeStamp){

        if (timeStamp.length() > 5){
            return timeStamp.substring(6,8);
        }else{
            return timeStamp.substring(3,5);
        }
    }

    /** Video Player > extract minutes from current time string **/
    public String extractMinutes(String timeStamp){

        if (timeStamp.length() > 5){
            return timeStamp.substring(3,5);
        }else{
            return timeStamp.substring(0,2);
        }
    }

    /** Video Player > extract hours from current time string **/
    public String extractHours(String timeStamp){

        if (timeStamp.length() > 5){
            return timeStamp.substring(0,2);
        }else{
            return "0";
        }
    }

    /** Video Player > verify pause functionality **/
    public boolean verifyPauseFunctionality() {
        String currentTime = "";
        String newCurrentTime = "";

        Dimension scrSize = getDriver().manage().window().getSize();

        EspnCommonPageBase commonPageBase = initPage(EspnCommonPageBase.class);

        commonPageBase.waitUntilNotPresent(mediaPlayerPlayBtn);
        activateVideoPlayerControls(scrSize);
//        mediaPlayerFrame.click();
        mediaPlayerPlayBtn.click();
        currentTime = getMediaPlayerCurrentTime();
        LOGGER.info("Current Time: " + currentTime);
        //explicit wait to check 'pause' in action
        pause(10);
        activateVideoPlayerControls(scrSize);
//        mediaPlayerFrame.click();
        newCurrentTime = getMediaPlayerCurrentTime();
        LOGGER.info("New Current Time: " + newCurrentTime);

        return currentTime.equals(newCurrentTime);
    }

    /** Video Player > verify pause/play functionality **/
    public boolean verifyPausePlayFunctionality(){
        String currentTime = "";
        String newCurrentTime = "";

        Dimension scrSize = getDriver().manage().window().getSize();

        EspnCommonPageBase commonPageBase = initPage(EspnCommonPageBase.class);

        commonPageBase.waitUntilNotPresent(mediaPlayerPlayBtn);
        activateVideoPlayerControls(scrSize);
//        mediaPlayerFrame.clickIfPresent();
        mediaPlayerPlayBtn.click();
        currentTime = getMediaPlayerCurrentTime();
        LOGGER.info("Current Time: " + currentTime);
        mediaPlayerPlayBtn.click();
        //explicit wait to check 'pause' in action
        pause(10);
        activateVideoPlayerControls(scrSize);
//        mediaPlayerFrame.clickIfPresent();
        newCurrentTime = getMediaPlayerCurrentTime();
        LOGGER.info("Unpaused Time: " + newCurrentTime);
        return currentTime.equals(newCurrentTime);
    }

    /** Video Player > check if LIVE indicator is present (should be present in LIVE content only) **/
    public boolean isLiveIndicatorPresent(Dimension d){
        activateVideoPlayerControls(d);
        return mediaPlayerLiveIndicator.isElementPresent();
    }

    /** Video Player > check if open for QE **/
    public boolean isMediaPlayerPresentQE(Long timeOut){
        return mediaPlayerFrame.isElementPresent(timeOut);
    }

    @Override
    public boolean isOpened() {

        return mediaPlayerFrame.isElementPresent();
    }
}

