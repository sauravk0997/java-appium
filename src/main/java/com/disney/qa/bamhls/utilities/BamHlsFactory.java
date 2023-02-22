package com.disney.qa.bamhls.utilities;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class BamHlsFactory extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public BamHlsFactory(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "suiteSelection")
    protected ExtendedWebElement suiteSelection;

    @FindBy(xpath = "//div//select[@id='suiteSelection']//option[@value='%s']")
    protected ExtendedWebElement getSuiteSelectionOption;

    @FindBy(id = "videoSelection")
    protected ExtendedWebElement videoSelection;

    @FindBy(xpath = "//select[@id='videoSelection']//option[@value='%s']")
    protected ExtendedWebElement videoSelectionOption;

    @FindBy(xpath = "//td[@id='audiotrackselect']//button")
    protected List <ExtendedWebElement> audioTrackSelectList;

    @FindBy(xpath = "//td[@id='audiotrackselect']//button[contains(text() , \"%s\")]")
    protected ExtendedWebElement audioTrackSelect;

    @FindBy(xpath = "//td[@id='audiotrackselect']//button[contains(@style, 'lightgreen')]")
    protected ExtendedWebElement audioTrackSelected;

    @FindBy(id = "playbackRate")
    protected ExtendedWebElement playbackRateList;

    @FindBy(xpath = "//button[@title='Detach -> Switch']")
    protected ExtendedWebElement switchVideoBtn;

    @FindBy(id = "video")
    protected ExtendedWebElement videoPlayerOverlay;

    @FindBy(id = "videoSelectionLocal")
    protected ExtendedWebElement videoSelectionLocal;

    @FindBy(id = "videoUrl")
    protected ExtendedWebElement videoUrlField;

    @FindBy(id = "restart")
    protected ExtendedWebElement seekToStartBtn;

    @FindBy(id = "startTime")
    protected ExtendedWebElement startTimeField;

    @FindBy(id = "seekTo")
    protected ExtendedWebElement seekTimeField;

    @FindBy(id = "//button[@onclick='seek()']")
    protected ExtendedWebElement seekTimeGoBtn;

    @FindBy(id = "startPdt")
    protected ExtendedWebElement startTimePDTField;

    @FindBy(id = "seekToPdt")
    protected ExtendedWebElement seekTimePDTField;

    @FindBy(id = "offsetSeekValue")
    protected ExtendedWebElement offsetSeekValue;

    @FindBy(id = "seekBack")
    protected ExtendedWebElement seekBack;

    @FindBy(id = "seekForward")
    protected ExtendedWebElement seekForward;

    @FindBy(id = "collapsible-initparams")
    protected ExtendedWebElement startUpPropCollapse;

    @FindBy(id = "initAudioLang")
    protected ExtendedWebElement initAudioLanguage;

    @FindBy (id = "bitrates")
    protected ExtendedWebElement bitRates;

    @FindBy(xpath = "//td[@id='bitrates']//button")
    protected List <ExtendedWebElement> bitRatesSelectList;

    @FindBy(xpath = "//td[@id='bitrates']//button[contains(@style, 'lightgreen')]")
    protected ExtendedWebElement bitRatesSelected;


    protected static final String PLAYING = "PLAYING";
    protected static final String PAUSED = "PAUSED";
    protected static final String BUFFERING = "BUFFERING";
    protected static final String ENDED = "ENDED";
    protected static final String NOT_READY = "NOTREADY";
    protected static final String SEEKING = "SEEKING";

    protected static final String RETURN = "return ";
    protected static final String STREAM = "stream";

    protected static final String PLAYBACK_STATE = ".playbackState";
    protected static final String PLAYBACK_RATE = ".playbackRate";
    protected static final String VIDEO_WIDTH = ".videoWidth";
    protected static final String VIDEO_HEIGHT = ".videoHeight";
    protected static final String CURRENT_TIME = ".currentTime";
    protected static final String PAUSE_VIDEO = "pauseVideo();";

    protected static final String PLAY = ".play();";
    protected static final String SEEK = ".seek(%s);";
    protected static final String SEEK_BY_PDT = ".seekByPdt(new Date('%s'));";
    protected static final String SEEK_TO_LIVE = ".seekToLive();";
    protected static final String CURRENT_TIME_SEEK_FORWARD = ".currentTime += %s;";
    protected static final String CURRENT_TIME_SEEK_BACKWARD = ".currentTime -= %s;";
    protected static final String IS_LIVE = ".isLive";
    protected static final String LOAD_SOURCE = ".loadSource(%s);";
    protected static final String PLAYBACK_RANGE = ".playbackRange";
    protected static final String END = ".end";

    protected static final String ARE_SUBTITLES_ENABLED = ".areSubtitlesEnabled";
    protected static final String ARE_SUBTITLES_ENABLED_TRUE = ".areSubtitlesEnabled = true;";
    protected static final String ARE_SUBTITLES_ENABLED_FALSE = ".areSubtitlesEnabled = false;";

    protected static final String AUDIOTRACKS = ".audioTracks";
    protected static final String AUDIO_IS_MUTED = ".isMuted";
    protected static final String INITIAL_AUDIO_LANGUAGE = ".initialAudioLanguage";
    protected static final String MUTE = ".isMuted = true";
    protected static final String UNMUTE = ".isMuted = false";



    /** Method Helpers to play Streams**/

    //Stable method to select suite by Json
    public void selectSuite(String testJson) {

        Select suiteSelectionJson = new Select(getDriver().findElement(suiteSelection.getBy()));
        suiteSelectionJson.selectByValue(testJson);

    }

    //Stable method to select video by Url
    public void selectVideo(String videoUrl) {

        Select videoSelectionUrl = new Select(getDriver().findElement(videoSelection.getBy()));
        videoSelectionUrl.selectByValue(videoUrl);

    }

    //Sets the initial Audio Langauge before Playback
    public void setInitialAudioLanguage(String language) {

        startUpPropCollapse.isElementPresent();
        startUpPropCollapse.click();
        initAudioLanguage.isElementPresent();
        initAudioLanguage.type(language);
        initAudioLanguage.isElementWithTextPresent(language, 3);

    }

    public ExtendedWebElement getVideoSelection(String url) {
        return videoSelectionOption.format(url);
    }


    /** Methods for base video player functions **/

    //Set the seek value via integer
    protected String setSeekInt(int seekValue) {

        return String.format(SEEK, seekValue);
    }

    //Set the seek value via pdt
    protected String setSeekPDT(String seekValue) {

        return String.format(SEEK_BY_PDT, seekValue);
    }

    //Set the seek forward value
    protected String setCurrentSeekForward(int seekValue) {
        return String.format(CURRENT_TIME_SEEK_FORWARD, seekValue);
    }

    //Set the seek backward value
    protected String setCurrentSeekBackward(int seekValue) {
        return String.format(CURRENT_TIME_SEEK_BACKWARD, seekValue);
    }

    //Defines the audio track in UI for interaction
    public ExtendedWebElement setAudioTrackUi(String language) {
        LOGGER.info(String.format("Track set: %s", language));
        return audioTrackSelect.format(language);
    }

    //Seek to Live State
    protected void seekToLive()  {
        LOGGER.info("Seeking to Live...");
        trigger(STREAM + SEEK_TO_LIVE);
    }

    //Pass a URL String value to a method that can trigger video playback without clicking on any UI elements
    protected String setPlayerUrlSource(String testUrl) {
        return String.format(LOAD_SOURCE, testUrl);
    }

    //Retrieve Current Stream Time Value
    public float getCurrentStreamTime() {
        return Float.parseFloat(trigger(RETURN + STREAM + CURRENT_TIME).toString());
    }

    //Retrieve Audio Stream
    public String getInitialAudioLanguage() {
        return (trigger(RETURN + STREAM + INITIAL_AUDIO_LANGUAGE).toString());
    }

    //Retrieve Audio Streams as String
    public String getMediaAudioStreams() {

        LOGGER.info("Audio Retrieved: " + trigger(RETURN + STREAM + AUDIOTRACKS).toString());

        return (trigger(RETURN + STREAM + AUDIOTRACKS).toString());
    }

    //Retrieve Maximum Stream Time Range
    public float getStreamRange() {
        return Float.parseFloat(trigger(RETURN + STREAM + PLAYBACK_RANGE + END).toString());
    }

    //Retrieve Current Audio Track Selected in UI
    public String getCurrentAudioTrack() {
        return audioTrackSelected.getText();
    }



    //Retrieve List of Available Audio Streams
    public List <String> getAudioTrackSelectList() {

        int iterator;
        int audioTrackSize = audioTrackSelectList.size();
        List <String> languageList = new ArrayList<>();

        if (audioTrackSize > 0) {

            for (iterator = 0; audioTrackSize >= iterator; iterator++ ) {

                if (audioTrackSize == iterator) {

                    break;
                }

                String nameValue = audioTrackSelectList.get(iterator).getText();

                languageList.add(nameValue);

            }

        }

        return languageList;
    }

    //Retrieve List of Available Audio Streams
    public List <String> getBitRatesList() {

        int iterator;
        int audioTrackSize = bitRatesSelectList.size();
        List <String> bitRatesList = new ArrayList<>();

        if (audioTrackSize > 0) {

            for (iterator = 0; audioTrackSize >= iterator; iterator++ ) {

                if (audioTrackSize == iterator) {

                    break;
                }

                String nameValue = bitRatesSelectList.get(iterator).getText();

                bitRatesList.add(nameValue);

            }

        }

        return bitRatesList;
    }



}
