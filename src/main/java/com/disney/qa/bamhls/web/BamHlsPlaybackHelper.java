package com.disney.qa.bamhls.web;

import com.disney.qa.bamhls.utilities.BamHlsMasterVideoProvider;
import org.openqa.selenium.WebDriver;

public class BamHlsPlaybackHelper extends BamHlsMasterVideoProvider {

    public BamHlsPlaybackHelper(WebDriver driver) {
        super(driver);
    }

    public void playUrlByStream(String streamUrlLocal, String labelLocal) {
        waitForVideoPlayer(2, 2, 3);
        loadStreamUrlLabel(streamUrlLocal, labelLocal);
        playVideoClick();
        waitForVideoPlayer(2,2,3);
    }

    public void playPauseVideo(int pauseTime) {
        waitForVideoPlayer(2, 2, 2);
        waitForVideoPause(30, false);
        waitForVideoPlayer(2, 2, 0);
        seekVideoForward(pauseTime);
        waitForVideoPlayer(2,2,1);

    }

    public void playUrlByStreamEnd(String streamUrlLocal, String labelLocal, int length) {
        waitForVideoPlayer(2, 2, 3);
        loadStreamUrlLabel(streamUrlLocal, labelLocal);
        playVideoClick();
        waitForVideoPlayer(2,2,3);
        waitForVideoPlayerEnd(length);
    }

    public void playUrlByStreamEndVod(String streamUrl, String label, int streamStart, int length) {
        waitForVideoPlayer(2,2,3);
        setStartTimeField(streamStart);
        loadStreamUrlLabel(streamUrl, label);
        playVideoClick();
        waitForVideoPlayer(2,2,2);
        waitForVideoPlayerEnd(length);

    }

    public void playUrlSetSeekStart(String streamUrl, String label, int startTime) {
        waitForVideoPlayer(2, 2, 2);
        setStartTimeField(startTime);
        loadStreamUrlLabel(streamUrl, label);
        playVideoClick();
        waitForVideoPlayer(2,2,2);

    }

    public void playUrlSetSeekStartPDT(String streamUrl, String label, String startTime) {
        waitForVideoPlayer(2, 2, 2);
        setStartTimePDTField(startTime);
        loadStreamUrlLabel(streamUrl, label);
        playVideoClick();
        waitForVideoPlayer(2,2,2);

    }

    public void playSeekToStart(String streamUrlLocal, String labelLocal) {
        playUrlByStream(streamUrlLocal, labelLocal);
        playVideoClick();
        waitForVideoPause(10, true);
        seekOffsetInt(0);
        pauseVideo(1);

    }

    public void playRewindPause(int pauseTime, int rewindTime) {
        waitForVideoPause(pauseTime, false);
        waitForVideoPlayer(2, 2, 2);
        pauseVideo(0);
        seekVideoRewind(rewindTime);
        waitForVideoPlayer(2, 2, 2);

    }

    public void playUrlSeekTolive(String streamUrlLocal, String labelLocal) {
        playUrlByStream(streamUrlLocal, labelLocal);
        playVideoClick();
        waitForVideoPlayer(2, 2, 3);
        seekToLive();

    }

    public void playSetPlaybackRate(double playbackRate) {
        setPlaybackRate(playbackRate);
        waitForVideoPlayer(2,2,0);
    }

}
