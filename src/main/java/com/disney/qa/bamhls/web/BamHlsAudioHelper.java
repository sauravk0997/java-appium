package com.disney.qa.bamhls.web;

import com.disney.qa.bamhls.utilities.BamHlsMasterVideoProvider;
import org.openqa.selenium.WebDriver;

public class BamHlsAudioHelper extends BamHlsMasterVideoProvider {

    public BamHlsAudioHelper(WebDriver driver) {
        super(driver);
    }

    public void playUrlByStream(String streamUrlLocal, String labelLocal) {
        waitForVideoPlayer(2, 2, 3);
        loadStreamUrlLabel(streamUrlLocal, labelLocal);
        playVideoClick();
        waitForVideoPlayer(2,2,3);
    }

    public void setPlaySetInitialLanguage(String streamUrlLocal, String labelLocal, String language) {
        waitForVideoPlayer(2, 2, 3);
        loadStreamUrlLabel(streamUrlLocal, labelLocal);
        setInitialAudioLanguage(language);
        playVideoClick();
        waitForVideoPlayer(2,2,2);

    }

    public void setAudioByLanguageUi(String language) {
        waitForVideoPlayer(2, 2, 2);
        setAudioTrackUiBtn(language);
        waitForVideoPlayer(2,2,2);

    }
}
