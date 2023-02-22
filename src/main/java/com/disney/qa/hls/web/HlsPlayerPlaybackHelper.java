package com.disney.qa.hls.web;

import com.disney.qa.hls.utilities.HlsMasterVideoProvider;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;

public class HlsPlayerPlaybackHelper extends HlsMasterVideoProvider{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // TS STREAMS //
    public static final String TEST_URL_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String TEST_URL_SLIDE = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String TEST_URL_VOD = "https://lw.bamgrid.com/2.0/hls/vod/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";

    // TS STREAMS WITH OFFSET //
    public static final String TEST_URL_SLIDE_OFFSET = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/offset-1630/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String TEST_URL_EVENT_OFFSET = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset-1630/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String TEST_URL_EVENT_OFFSET_IE = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset-1620/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";

    // TS STREAM WITH SLIDE //
    public static final String TEST_URL_SLIDE_WIDTH60 = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/width60/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";

    // DRM KEY ROTATION - CTR //
    public static final String TEST_URL_CTR_URL_EVENT_KR = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ms02/hls/ext/tos_kr/ctr/master.m3u8";
    public static final String TEST_URL_CTR_URL_SLIDE_KR = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ms02/hls/ext/tos_kr/ctr/master.m3u8";
    public static final String TEST_URL_CTR_URL_VOD_KR = "https://lw.bamgrid.com/2.0/hls/vod/bam/ms02/hls/ext/tos_kr/ctr/master.m3u8";

    // DRM KEY ROTATION - CBCS //
    public static final String TEST_URL_CBCS_URL_EVENT_KR = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ms02/hls/ext/tos_kr/cmaf_cbcs/master.m3u8";
    public static final String TEST_URL_CBCS_URL_SLIDE_KR = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ms02/hls/ext/tos_kr/cmaf_cbcs/master.m3u8";
    public static final String TEST_URL_CBCS_URL_VOD_KR = "https://lw.bamgrid.com/2.0/hls/vod/bam/ms02/hls/ext/tos_kr/cmaf_cbcs/master.m3u8";

    // CMAF STREAMS //
    public static final String TEST_URL_CMAF_URL_EVENT_UNENC = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";
    public static final String TEST_URL_CMAF_URL_SLIDE_UNENC = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";
    public static final String TEST_URL_CMAF_URL_VOD_UNENC = "https://lw.bamgrid.com/2.0/hls/vod/nocookie/profile/default/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";

    // CMAF STREAMS WITH OFFSET //
    public static final String TEST_URL_CMAF_URL_EVENT_UNENC_OFFSET = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";
    public static final String TEST_URL_CMAF_URL_SLIDE_UNENC_OFFSET = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";

    // CMAF STREAM WITH SLIDE //
    public static final String TEST_URL_CMAF_URL_SLIDE_UNENC_WIDTH60 = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/width60/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";

    // DRM STREAMS - CTR //
    public static final String TEST_URL_CTR_URL_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ms02/hls/ext/tos5/ctr/master_1aud.m3u8";
    public static final String TEST_URL_CTR_URL_SLIDE = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ms02/hls/ext/tos5/ctr/master_1aud.m3u8";
    public static final String TEST_URL_CTR_URL_VOD = "https://lw.bamgrid.com/2.0/hls/vod/bam/ms02/hls/ext/tos5/ctr/master_1aud.m3u8";

    // DRM STREAMS - CTR WITH OFFSET //
    public static final String TEST_URL_CTR_URL_EVENT_OFFSET = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/ctr/master_1aud.m3u8";
    public static final String TEST_URL_CTR_URL_SLIDE_OFFSET = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/ctr/master_1aud.m3u8";

    // DRM STREAMS - CTR + SILK //
    public static final String TEST_URL_CTR_SILK_URL_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ms02/hls/ext/tos5/ctr/master_w_silk_cmaf1aud.m3u8";
    public static final String TEST_URL_CTR_SILK_URL_SLIDE = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ms02/hls/ext/tos5/ctr/master_w_silk_cmaf1aud.m3u8";
    public static final String TEST_URL_CTR_SILK_URL_VOD = "https://lw.bamgrid.com/2.0/hls/vod/bam/ms02/hls/ext/tos5/ctr/master_w_silk_cmaf1aud.m3u8";

    // DRM STREAMS - CTR + SILK WITH OFFSET //
    public static final String TEST_URL_CTR_SILK_URL_EVENT_OFFSET = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/ctr/master_w_silk_cmaf1aud.m3u8";
    public static final String TEST_URL_CTR_SILK_URL_SLIDE_OFFSET = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/ctr/master_w_silk_cmaf1aud.m3u8";

    // DRM STREAMS - CTR + SILK WITH SLIDE //
    public static final String TEST_URL_CTR_SILK_URL_SLIDE_WIDTH60 = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/width60/bam/ms02/hls/ext/tos5/ctr/master_w_silk_cmaf1aud.m3u8";

    // DRM STREAMS - CBCS + SILK //
    public static final String TEST_URL_CBCS_SILK_URL_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ms02/hls/ext/tos5/cmaf_cbcs/master_w_silk_cmaf1aud.m3u8";
    public static final String TEST_URL_CBCS_SILK_URL_SLIDE = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ms02/hls/ext/tos5/cmaf_cbcs/master_w_silk_cmaf1aud.m3u8";
    public static final String TEST_URL_CBCS_SILK_URL_VOD = "https://lw.bamgrid.com/2.0/hls/vod/bam/ms02/hls/ext/tos5/cmaf_cbcs/master_w_silk_cmaf1aud.m3u8";

    // DRM STREAMS - CBCS + SILK WITH OFFSET //
    public static final String TEST_URL_CBCS_SILK_URL_EVENT_OFFSET = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/cmaf_cbcs/master_w_silk_cmaf1aud.m3u8";
    public static final String TEST_URL_CBCS_SILK_URL_SLIDE_OFFSET = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/offset-670/bam/ms02/hls/ext/tos5/cmaf_cbcs/master_w_silk_cmaf1aud.m3u8";

    // DRM STREAMS - CBCS + SILK WITH SLIDE //
    public static final String TEST_URL_CBCS_SILK_URL_SLIDE_WIDTH60 = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/width60/bam/ms02/hls/ext/tos5/cmaf_cbcs/master_w_silk_cmaf1aud.m3u8";

    private static final String[] PLAY_BACK_RATE = {"0.25","0.50","0.75","1","2","4","8"};
    private static final String SEEKDATE ="2016-10-18T23:24:57+00:00";

    public HlsPlayerPlaybackHelper(WebDriver driver) { super(driver); }


    /** Use this class to override methods from the HlsMasterVideoProvider **/

    @Override
    public boolean waitForVideoPlayerPresence() {
        seekToElement(videoPlayerOverlay);
        return waitForVideoPlayer(videoPlayerOverlay, 5, 5, 2);

    }

    /** Methods for Test Recycling **/

    public static String[] getPlayBackRate() {
        return PLAY_BACK_RATE;
    }

    public void hlsHelperTestPlayUrl(String url) {

        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        waitForPlayingState();
    }

    public void hlsHelperTestPause() {

        waitForVideoPlayerPresence();
        playerPause();
        waitForVideoPlayerPresence();
    }

    public void hlsHelperCaptionsDisabled() {

        getMediaStateCaptions();
        waitForVideoPlayerPresence();
        getMediaPlayerEnableCaptions();
        getMediaStateCaptions();
        getMediaPlayerDisableCaptions();
        waitForVideoPlayerPresence();
        getMediaStateCaptions();
    }

    public void hlsHelperConfirmSeekToStart() {

        waitForVideoPlayerPresence();
        playerSeekDateSpecified(SEEKDATE);
        waitForVideoPlayerPresence();
        playerSeekStartPause();
        waitForVideoPlayerPresence();
    }

    public void hlsHelperConfirmSeekToLive() {

        waitForVideoPlayerPresence();
        playerSeekLive();
        waitForVideoPlayerPresence();
        playerPause();

    }

    public void hlsHelperConfirmPauseNoResume(int pauseTime) {

        waitForVideoPlayer(videoPlayerOverlay, 5, 5, pauseTime);
        playerPause();
        waitForVideoPlayerPresence();
        playerSeekSet(0);
        waitForVideoPlayerPresence();
    }

    public void hlsHelperConfirmOffsetSeconds(String url, int seconds) {

        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url, seconds);
        waitForPlayingState();
    }

    public void hlsHelperConfirmOffsetSecondsRun(String url, int seconds) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url, seconds);
        waitForVideoPlayerPresence();

    }

    public void hlsHelperConfirmOffsetDate(String url, String seekDate) {

        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url, seekDate);
        waitForPlayingState();
    }

    public void hlsHelperConfirmSeekSeconds(String url, int seek) {

        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        playerPause();
        waitForVideoPlayer(videoPlayerOverlay,1,4,3);
        playerSeekSet(seek);
    }

    public void hlsHelperConfirmSeekDate(String url, String seekDate) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        waitForVideoPlayer(videoPlayerOverlay,1,4,3);
        playerSeekDateSpecified(seekDate);
    }

    public Object hlsHelperPlayerReachMaxBitrate(String url, String maxBitrate) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return getMediaMaxBitrateTime(maxBitrate);
    }

    public void hlsHelperPlayerWidthIncrease(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        playerWidthIncrease();
    }

    public void hlsHelperPlayerWidthDecrease(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        playerWidthDecrease();
    }

    public void hlsHelperSetPlaybackRate(String url, String rate) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        setPlaybackRate(rate);
    }

    public Object hlsHelperNoBitrateDropOnSeek(String url, String maxBitrate) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return noBitrateDropOnSeek(maxBitrate);
    }

    public Object hlsHelperPlayerSeekFutureSeekPast(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return seekFutureSeekPast();
    }

    public Object hlsHelperPlayerSeekForwardMultipleTimes(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return seekForwardMultipleTimes();
    }

    public Object hlsHelperPlayerSeekBackwardMultipleTimes(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return seekBackwardMultipleTimes();
    }

    public Object hlsHelperPlayerSeekBackwardMultipleTimesFromTail(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return seekBackwardMultipleTimesFromTail();
    }

    public Object hlsHelperPlayerSeekBackwardMultipleTimesFromTailVod(String url, int seconds) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url, seconds);
        waitForVideoPlayerPresence();
        return seekBackwardMultipleTimesFromTail();
    }

    public Object hlsHelperSlideResumesAtLive(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return slideResumesAtLive();
    }

    public Object hlsHelperPlayerKeyRotationFastFail(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return keyRotationFastFail();
    }

    public Object hlsHelperPlayerKeyRotationPlayThroughChange(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return keyRotationPlayThroughChange();
    }

    public Object hlsHelperPlayerKeyRotationPlayThroughChangeAndSeekBack(String url) {
        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        return keyRotationPlayThroughChangeAndSeekBack();
    }

    public void waitForPlayingState() {
        String status = getMediaPlaybackStateStatus().toString();
        int timeout = 0;

        while (!status.equals(MEDIA_PLAYER_PLAYBACK_STATE_PLAYING)) {

            status = getMediaPlaybackStateStatus().toString();

            LOGGER.info("Waiting for playing state, current status: " + status);
            timeout++;
            pause(.2);

            if (status == MEDIA_PLAYER_PLAYBACK_STATE_PLAYING) {

                Assert.assertTrue(status.equals(MEDIA_PLAYER_PLAYBACK_STATE_PLAYING));
                LOGGER.info("Media player is playing");

            } else if (timeout == 25){

                Assert.fail("Video player taking too long to start playback...");
            }
        }
    }

    public void hlsHelperWaitForVideoToEnd() {
        String status = getMediaPlayerStateStatus().toString();
        waitForVideoPlayerPresence();
        int timeout = 0;

        while (status.equals(MEDIA_PLAYER_STATE_PLAYING) || status.equals(MEDIA_PLAYER_STATE_BUFFERING)) {

            waitForVideoPlayer(videoPlayerOverlay, 2, 2, 5);
            status = getMediaPlayerStateStatus().toString();

            LOGGER.info("Waiting for media to end, current status: " + status);
            timeout++;

            if (status == MEDIA_PLAYER_STATE_ENDED) {

                Assert.assertTrue(status.equals(MEDIA_PLAYER_STATE_ENDED));
                LOGGER.info("Media player test ended");

            } else if (timeout == 15){

                Assert.fail("Video player taking too long to end, ending manually...");
            }
        }

    }
}