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

public class HlsSlidePlaybackTest extends HlsBaseTest{

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private HlsAssertService hlsAssert;
    private HlsPlayerPlaybackHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerPlaybackHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
        hlsAssert = new HlsAssertService(getDriver());

    }

    @QTestCases(id = "6355")
    @Test(description = "Media = Playback (Autoplay)", priority = 1)
    public void testPlayMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();
    }

    @QTestCases(id = "6356")
    @Test(description = "Media - Pause", priority = 2)
    public void testPauseMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);
        hP.hlsHelperTestPause();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PAUSED
                , String. format("Expected (__media_player_state_playing), Actual: %s", hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();

    }

    @QTestCases(id = "6357")
    @Test(description = "Media - Resume", priority = 3)
    public void testResumeMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);
        hP.hlsHelperTestPause();
        hP.playerMediaStatePlayAsis();
        hP.waitForPlayingState();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();

    }

    @QTestCases(id = "6358")
    @Test (description = "Media - Completed", priority = 4)
    public void testCompletedSlide() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE_OFFSET);
        hP.hlsHelperWaitForVideoToEnd();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_ENDED
                , String.format("Expected (__media_player_state_ended), Actual: %s", hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();

    }



    @QTestCases(id = "6359")
    @Test (description = "Media - Mute", priority = 5)
    public void testMuteMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);
        hP.playerMuteVideo();


        sa.assertEquals(hP.playerGetVolumeLevelStatus(0), "0",
                "Expected: Volume set to zero");

        sa.assertAll();

    }

    @QTestCases(id = "6360")
    @Test (description = "Media - Unmute", priority = 6)
    public void testUnmuteMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);
        testMuteMediaPlayer();
        hP.waitForVideoPlayerPresence();

        sa.assertEquals(hP.playerGetVolumeLevelStatus(70), "70",
                "Expected: Volume set to 70)");

        sa.assertAll();
    }

    @QTestCases(id = "6361")
    @Test (description = "Media - Captions Enabled", priority = 7)
    public void testCaptionsEnabled() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);
        hP.getMediaStateCaptions();
        hP.getMediaPlayerEnableCaptions();

        sa.assertEquals(hlsAssert.getMediaStateCaptionsPresence(), "true",
                String.format("Expected: (true), Actual: %s", hP.getMediaStateCaptions()));
        sa.assertAll();
    }

    @QTestCases(id = "6362")
    @Test (description = "Media - Captions Disabled", priority = 8)
    public void testCaptionsDisabled() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);
        hP.hlsHelperCaptionsDisabled();

        LOGGER.info("Caption Presence: " + hlsAssert.getMediaStateCaptionsPresence());
        sa.assertEquals(hlsAssert.getMediaStateCaptionsPresence(), "false",
                String.format("Expected: (false) , Actual: %s", hlsAssert.getMediaStateCaptionsPresence()));

        sa.assertAll();

    }

    @QTestCases(id = "6365")
    @Test(description = "Media - Confirm paused video does not resume after 10 second rewind", priority = 9)
    public void testConfirmPauseNoResume() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);
        hP.hlsHelperConfirmPauseNoResume(10);


        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PAUSED,
                String.format("Expected (__media_player_state_paused). Actual: %s", hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();
    }

}
