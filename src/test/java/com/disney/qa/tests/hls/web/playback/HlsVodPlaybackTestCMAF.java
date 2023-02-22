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

public class HlsVodPlaybackTestCMAF extends HlsBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private HlsAssertService hlsAssert;
    private HlsPlayerPlaybackHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerPlaybackHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
        hlsAssert = new HlsAssertService(getDriver());

    }

    @QTestCases(id = "6484")
    @Test(description = "Media = Playback (Autoplay)", priority = 1)
    public void testPlayMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format(HlsAssertService.PLAYING_ASSERT
                        , hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();
    }

    @QTestCases(id = "6485")
    @Test(description = "Media - Pause", priority = 2)
    public void testPauseMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        hP.hlsHelperTestPause();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PAUSED
                , String. format(HlsAssertService.PLAYING_ASSERT, hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();

    }

    @QTestCases(id = "6486")
    @Test(description = "Media - Resume", priority = 3)
    public void testResumeMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        hP.hlsHelperTestPause();
        hP.playerMediaStatePlayAsis();
        hP.waitForPlayingState();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format(HlsAssertService.PLAYING_ASSERT
                        , hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();

    }

    @QTestCases(id = "6488")
    @Test (description = "Media - Mute", priority = 4)
    public void testMuteMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        hP.playerMuteVideo();


        sa.assertEquals(hP.playerGetVolumeLevelStatus(0), "0",
                "Expected: Volume set to zero");

        sa.assertAll();

    }

    @QTestCases(id = "6489")
    @Test (description = "Media - Unmute", priority = 5)
    public void testUnmuteMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        testMuteMediaPlayer();
        hP.waitForVideoPlayerPresence();

        sa.assertEquals(hP.playerGetVolumeLevelStatus(70), "70",
                "Expected: Volume set to 70)");

        sa.assertAll();
    }

    @QTestCases(id = "6490")
    @Test (description = "Media - Captions Enabled", priority = 6)
    public void testCaptionsEnabled() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        hP.getMediaStateCaptions();
        hP.getMediaPlayerEnableCaptions();

        sa.assertEquals(hlsAssert.getMediaStateCaptionsPresence(), "true",
                String.format("Expected: (true), Actual: %s", hP.getMediaStateCaptions()));
        sa.assertAll();
    }

    @QTestCases(id = "6491")
    @Test (description = "Media - Captions Disabled", priority = 7)
    public void testCaptionsDisabled() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        hP.hlsHelperCaptionsDisabled();

        LOGGER.info("Caption Presence: " + hlsAssert.getMediaStateCaptionsPresence());
        sa.assertEquals(hlsAssert.getMediaStateCaptionsPresence(), "false",
                String.format("Expected: (false) , Actual: %s", hlsAssert.getMediaStateCaptionsPresence()));

        sa.assertAll();

    }

    @QTestCases(id = "6492")
    @Test(description = "Confirm seek to start", priority = 8)
    public void testConfirmSeekToStart() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        hP.hlsHelperConfirmSeekToStart();

        sa.assertEquals(hlsAssert.getMediaStateCurrentTimeSeconds(), "0",
                String.format("Expected current time (0), Actual: %s", hlsAssert.getMediaStateCurrentTimeSeconds()));

    }

    @QTestCases(id = "6494")
    @Test(description = "Media - Confirm paused video does not resume after 10 second rewind", priority = 10)
    public void testConfirmPauseNoResume() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperTestPlayUrl(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);
        hP.hlsHelperConfirmPauseNoResume(10);


        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), HlsAssertService.MEDIA_PLAYER_STATE_PAUSED,
                String.format("Expected (__media_player_state_paused). Actual: %s", hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();
    }

    @QTestCases(id = "6496")
    @Test(description = "Media - Confirm offset by seconds - VOD", priority = 11)
    public void testConfirmOffsetSeconds() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperConfirmOffsetSeconds(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC, 60);

        sa.assertTrue(hlsAssert.getMediaStateCurrentAssertRange() >= 60 && hlsAssert.getMediaStateCurrentAssertRange() <= 61,
                String.format(HlsAssertService.TIME_60_ASSERT, hlsAssert.getMediaStateCurrentTimeSeconds()));

        sa.assertAll();
    }

    @QTestCases(id = "6497")
    @Test(description = "Media - Confirm offset by date - VOD", priority = 12)
    public void testConfirmOffsetDate()  {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperConfirmOffsetDate(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC, "2018-05-07T00:01:00+00:00");

        sa.assertTrue(hlsAssert.getMediaStateCurrentAssertRange() >= 60 && hlsAssert.getMediaStateCurrentAssertRange() <= 61,
                String.format(HlsAssertService.TIME_60_ASSERT, hlsAssert.getMediaStateCurrentTimeSeconds()));

        sa.assertAll();

    }

    @QTestCases(id = "6498")
    @Test(description = "Media - Confirm seek by seconds - VOD", priority = 13)
    public void testConfirmSeekSeconds() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperConfirmSeekSeconds(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC, 60);

        sa.assertTrue(hlsAssert.getMediaStateCurrentAssertRange() >= 60 && hlsAssert.getMediaStateCurrentAssertRange() <= 61,
                String.format(HlsAssertService.TIME_60_ASSERT, hlsAssert.getMediaStateCurrentTimeSeconds()));

        sa.assertAll();

    }

    @QTestCases(id = "6499")
    @Test(description = "Media - Confirm seek by date", priority = 14)
    public void testConfirmSeekDate() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperConfirmSeekDate(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC, "2018-05-07T00:01:00+00:00");

        sa.assertTrue(hlsAssert.getMediaStateCurrentAssertRange() >= 60 && hlsAssert.getMediaStateCurrentAssertRange() <= 61,
                String.format(HlsAssertService.TIME_60_ASSERT, hlsAssert.getMediaStateCurrentTimeSeconds()));

        sa.assertAll();
    }

    @QTestCases(id = "6495")
    @Test(description = "Completed", priority = 15)
    public void testConfirmCompleted() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperConfirmOffsetSecondsRun(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC, 670);
        hP.hlsHelperWaitForVideoToEnd();

        sa.assertEquals(hlsAssert.getMediaPlayerStateStatus(), "__media_player_state_ended"
                , String.format("Expected media player state (__media_player_state_ended), Actual: %s", hlsAssert.getMediaPlayerStateStatus()));

        sa.assertAll();

    }

}
