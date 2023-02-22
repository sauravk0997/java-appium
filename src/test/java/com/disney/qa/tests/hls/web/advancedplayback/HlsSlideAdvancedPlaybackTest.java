package com.disney.qa.tests.hls.web.advancedplayback;

import com.disney.qa.hls.utilities.HlsAssertService;
import com.disney.qa.hls.utilities.HlsParameter;
import com.disney.qa.hls.web.HlsPlayerPlaybackHelper;
import com.disney.qa.tests.hls.web.HlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HlsSlideAdvancedPlaybackTest extends HlsBaseTest{

    private HlsAssertService hlsAssert;
    private HlsPlayerPlaybackHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerPlaybackHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
        hlsAssert = new HlsAssertService(getDriver());
    }

    @QTestCases(id = "6400")
    @Test(description = "Playback - Increase player width", priority = 1)
    public void increasePlayerWidth() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperPlayerWidthIncrease(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);

        Object playerState = hlsAssert.getMediaPlayerStateStatus();

        sa.assertEquals(playerState, HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , playerState));

        sa.assertAll();
    }

    @QTestCases(id = "6401")
    @Test(description = "Playback - Decrease player width", priority = 2)
    public void decreasePlayerWidth() {
        SoftAssert sa = new SoftAssert();

        hP.hlsHelperPlayerWidthDecrease(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);

        Object playerState = hlsAssert.getMediaPlayerStateStatus();

        sa.assertEquals(playerState, HlsAssertService.MEDIA_PLAYER_STATE_PLAYING
                , String.format("Expected (__media_player_state_playing), Actual: %s"
                        , playerState));

        sa.assertAll();
    }

    @QTestCases(id = "6402")
    @Test(description = "Playback - Cycle playback rate", priority = 3)
    public void setPlaybackRate() {
        for (String rate : HlsPlayerPlaybackHelper.getPlayBackRate()) {
            SoftAssert sa = new SoftAssert();

            hP.hlsHelperSetPlaybackRate(HlsPlayerPlaybackHelper.TEST_URL_SLIDE, rate);

            Object playbackRate = hlsAssert.getMediaPlaybackRate();

            sa.assertEquals(playbackRate, rate
                    , String.format("Expected (__rate: %s), Actual: %s"
                            , rate, playbackRate));

            sa.assertAll();
        }
    }

    @QTestCases(id = "6403")
    @Test(description = "Playback - Reach maximum bitrate < 10 secs", priority = 4)
    public void reachMaximumBitrate() {
        SoftAssert sa = new SoftAssert();

        Object maxBitRate = hP.hlsHelperPlayerReachMaxBitrate(HlsPlayerPlaybackHelper.TEST_URL_SLIDE, "1546");

        sa.assertEquals(maxBitRate, HlsAssertService.MAX_BITRATE_REACHED,
                String.format("Expected (__max_bitrate_reached), Actual: %s",
                        maxBitRate));

        sa.assertAll();
    }

    @QTestCases(id = "6404")
    @Test(description = "Playback - No bitrate drop on seek", priority = 5)
    public void noBitrateDropOnSeek() {
        SoftAssert sa = new SoftAssert();

        Object noBitrateDropOnSeek = hP.hlsHelperNoBitrateDropOnSeek(HlsPlayerPlaybackHelper.TEST_URL_SLIDE,"1546");

        sa.assertEquals(noBitrateDropOnSeek,HlsAssertService.BITRATE_NOT_DROPPING
                , String.format("Expected (__bitrate_did_not_drop), Actual: %s"
                        , noBitrateDropOnSeek));

        sa.assertAll();
    }

    @QTestCases(id = "6405")
    @Test(description = "Playback - Passed sliding window resumes at live point", priority = 6)
    public void slideResumesAtLive() {
        SoftAssert sa = new SoftAssert();

        Object slideResumesAtLive = hP.hlsHelperSlideResumesAtLive(HlsPlayerPlaybackHelper.TEST_URL_SLIDE_WIDTH60);

        sa.assertEquals(slideResumesAtLive,"true"
                , String.format("Expected (true), Actual: %s"
                        , slideResumesAtLive));

        sa.assertAll();
    }

    @QTestCases(id = "6408")
    @Test(description = "Abuse - Seek Future and seek past", priority = 7)
    public void seekFutureSeekPast() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekFutureSeekPast(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }

    @QTestCases(id = "6409")
    @Test(description = "Abuse - Seek forward multiple times", priority = 8)
    public void seekForwardMultipleTimes() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekForwardMultipleTimes(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }

    @QTestCases(id = "6410")
    @Test(description = "Abuse - Seek backward multiple times", priority = 9)
    public void seekBackwardMultipleTimes() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekBackwardMultipleTimes(HlsPlayerPlaybackHelper.TEST_URL_SLIDE);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }

    @QTestCases(id = "6411")
    @Test(description = "Abuse - Seek backward multiple times from completed tail", priority = 10)
    public void seekBackwardMultipleTimesFromTail() {
        SoftAssert sa = new SoftAssert();

        Object testResult = hP.hlsHelperPlayerSeekBackwardMultipleTimesFromTail(HlsPlayerPlaybackHelper.TEST_URL_SLIDE_OFFSET);

        sa.assertEquals(testResult, "Media remains in a playing state"
                , String.format("Expected (__Media remains in a playing state), Actual: %s"
                        , testResult));

        sa.assertAll();
    }
}