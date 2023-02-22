package com.disney.qa.tests.hls.web.playback;

import com.disney.qa.hls.utilities.HlsAssertService;
import com.disney.qa.hls.utilities.HlsParameter;
import com.disney.qa.hls.web.HlsPlayerPlaybackHelper;
import com.disney.qa.tests.hls.web.HlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class HlsEventPlaybackTestDrmCtr extends HlsBaseTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private HlsAssertService hlsAssert;
    private HlsPlayerPlaybackHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerPlaybackHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
        hlsAssert = new HlsAssertService(getDriver());

    }

    @QTestCases(id = "6429")
    @Test(description = "Media = Playback (Autoplay)", priority = 1)
    public void testPlayMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();
    }

    @QTestCases(id = "6430")
    @Test(description = "Media - Pause", priority = 2)
    public void testPauseMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.hlsHelperTestPause();
        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PAUSED
                , String.format("Expected (__media_player_state_playing), Actual: %s", hlsAssert.getMediaPlayerStateStatus()));
        sa.assertAll();
    }

    @QTestCases(id = "6431")
    @Test(description = "Media - Resume", priority = 3)
    public void testResumeMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.hlsHelperTestPause();
        hP.playerMediaStatePlayAsis();
        hP.waitForPlayingState();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , hlsAssert.getMediaPlayerStateStatus()));
        sa.assertAll();
    }

    @QTestCases(id = "6432")
    @Test (description = "Media - Completed", priority = 4)
    public void testCompletedEventSlide() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT_OFFSET);
        hP.hlsHelperWaitForVideoToEnd();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_ENDED
                , String.format("Expected (__media_player_state_ended), Actual: %s", hlsAssert.getMediaPlayerStateStatus()));
        sa.assertAll();
    }

    @QTestCases(id = "6433")
    @Test (description = "Media - Mute", priority = 5)
    public void testMuteMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.playerMuteVideo();

        sa.assertEquals(hP.playerGetVolumeLevelStatus(0), "0","Expected: Volume set to zero");
        sa.assertAll();
    }

    @QTestCases(id = "6434")
    @Test (description = "Media - Unmute", priority = 6)
    public void testUnmuteMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        testMuteMediaPlayer();
        hP.waitForVideoPlayerPresence();

        sa.assertEquals(hP.playerGetVolumeLevelStatus(70), "70","Expected: Volume set to 70)");
        sa.assertAll();
    }

    @QTestCases(id = "6435")
    @Test (description = "Media - Captions Enabled", priority = 7)
    public void testCaptionsEnabled() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.getMediaStateCaptions();
        hP.getMediaPlayerEnableCaptions();

        sa.assertEquals(hlsAssert.getMediaStateCaptionsPresence(), "true",
                String.format("Expected: (true), Actual: %s", hP.getMediaStateCaptions()));
        sa.assertAll();
    }

    @QTestCases(id = "6436")
    @Test (description = "Media - Captions Disabled", priority = 8)
    public void testCaptionsDisabled() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.hlsHelperCaptionsDisabled();
        LOGGER.info("Caption Presence: " + hlsAssert.getMediaStateCaptionsPresence());

        sa.assertEquals(hlsAssert.getMediaStateCaptionsPresence(), "false",
                String.format("Expected: (false) , Actual: %s", hlsAssert.getMediaStateCaptionsPresence()));
        sa.assertAll();
    }

    @QTestCases(id = "6437")
    @Test(description = "Confirm seek to start", priority = 9)
    public void testConfirmSeekToStart() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.hlsHelperConfirmSeekToStart();

        sa.assertEquals(hlsAssert.getMediaStateCurrentTimeSeconds(), "0",
                String.format("Expected current time (0), Actual: %s", hlsAssert.getMediaStateCurrentTimeSeconds()));
        sa.assertAll();
    }

    @QTestCases(id = "6438")
    @Test(description = "Media - Confirm seek to live", priority = 10)
    public void testConfirmSeekToLive() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.hlsHelperConfirmSeekToLive();

        sa.assertEquals(hlsAssert.getMediaLiveState(), "true",
                String.format("Expected: Media state is live. Actual: %s", hlsAssert.getMediaLiveState()));
        sa.assertAll();
    }

    @QTestCases(id = "6439")
    @Test(description = "Media - Confirm paused video does not resume after 10 second rewind", priority = 11)
    public void testConfirmPauseNoResume() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CTR_URL_EVENT);
        hP.hlsHelperConfirmPauseNoResume(10);

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PAUSED,
                String.format("Expected (__media_player_state_paused). Actual: %s", hlsAssert.getMediaPlayerStateStatus()));
        sa.assertAll();
    }
}