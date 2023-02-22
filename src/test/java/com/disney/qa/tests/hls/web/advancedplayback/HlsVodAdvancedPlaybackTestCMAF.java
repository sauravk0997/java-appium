package com.disney.qa.tests.hls.web.advancedplayback;

import com.disney.qa.hls.utilities.HlsAssertService;
import com.disney.qa.hls.utilities.HlsParameter;
import com.disney.qa.hls.web.HlsPlayerPlaybackHelper;
import com.disney.qa.tests.hls.web.HlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HlsVodAdvancedPlaybackTestCMAF extends HlsBaseTest{

    private HlsAssertService hlsAssert;
    private HlsPlayerPlaybackHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerPlaybackHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
        hlsAssert = new HlsAssertService(getDriver());
    }

    @QTestCases(id = "6500")
    @Test(description = "Playback - Increase player width", priority = 1)
    public void increasePlayerWidth() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperPlayerWidthIncrease(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);

        Object playerState = hlsAssert.getMediaPlayerStateStatus();

        sa.assertEquals(playerState, HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , playerState));

        sa.assertAll();
    }

    @QTestCases(id = "6501")
    @Test(description = "Playback - Decrease player width", priority = 2)
    public void decreasePlayerWidth() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperPlayerWidthDecrease(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);

        Object playerState = hlsAssert.getMediaPlayerStateStatus();

        sa.assertEquals(playerState, HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , playerState));

        sa.assertAll();
    }

    @QTestCases(id = "6502")
    @Test(description = "Playback - Cycle playback rate", priority = 3)
    public void setPlaybackRate() {
        for (String rate : HlsPlayerPlaybackHelper.getPlayBackRate()) {
            SoftAssert sa = new SoftAssert();

            hP.hlsHelperSetPlaybackRate(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC, rate);

            Object playbackRate = hlsAssert.getMediaPlaybackRate();

            sa.assertEquals(playbackRate, rate
                    , String.format("Expected (__rate: %s), Actual: %s"
                            , rate, playbackRate));

            sa.assertAll();
        }
    }

    @QTestCases(id = "6503")
    @Test(description = "Playback - Reach maximum bitrate < 10 secs", priority = 4)
    public void reachMaximumBitrate() {
        SoftAssert sa = new SoftAssert();

        Object maxBitRate = hP.hlsHelperPlayerReachMaxBitrate(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_EVENT_UNENC, "2515");

        sa.assertEquals(maxBitRate, HlsAssertService.MAX_BITRATE_REACHED,
                String.format("Expected (__max_bitrate_reached), Actual: %s",
                        maxBitRate));

        sa.assertAll();
    }

    @QTestCases(id = "6504")
    @Test(description = "Playback - No bitrate drop on seek", priority = 5)
    public void noBitrateDropOnSeek() {
        SoftAssert sa = new SoftAssert();

        Object noBitrateDropOnSeek = hP.hlsHelperNoBitrateDropOnSeek(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC,"2515");

        sa.assertEquals(noBitrateDropOnSeek,HlsAssertService.BITRATE_NOT_DROPPING
                , String.format("Expected (__bitrate_did_not_drop), Actual: %s"
                        , noBitrateDropOnSeek));

        sa.assertAll();
    }

    @QTestCases(id = "6508")
    @Test(description = "Abuse - Seek Future and seek past", priority = 6)
    public void seekFutureSeekPast() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekFutureSeekPast(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }

    @QTestCases(id = "6509")
    @Test(description = "Abuse - Seek forward multiple times", priority = 7)
    public void seekForwardMultipleTimes() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekForwardMultipleTimes(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }

    @QTestCases(id = "6510")
    @Test(description = "Abuse - Seek backward multiple times", priority = 8)
    public void seekBackwardMultipleTimes() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekBackwardMultipleTimes(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }

    @QTestCases(id = "6511")
    @Test(description = "Abuse - Seek backward multiple times from completed tail", priority = 9)
    public void seekBackwardMultipleTimesFromTail() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekBackwardMultipleTimesFromTailVod(HlsPlayerPlaybackHelper.TEST_URL_CMAF_URL_VOD_UNENC,700);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }
}