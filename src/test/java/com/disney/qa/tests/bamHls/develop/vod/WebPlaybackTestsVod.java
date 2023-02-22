package com.disney.qa.tests.bamHls.develop.vod;

import com.disney.qa.bamhls.utilities.BamHlsParameter;
import com.disney.qa.bamhls.web.BamHlsPlaybackHelper;
import com.disney.qa.tests.bamHls.bamHlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class WebPlaybackTestsVod extends bamHlsBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @BeforeMethod
    public void baseEvent() {
        LOGGER.info("VOD TESTING");
        mP = new BamHlsPlaybackHelper(getDriver());
        mP.newSession(BamHlsParameter.BAMHLS_DEVELOP_URL.getValue(), 1920, 1080, 5);
        Assert.assertEquals("200", mP.getHttpStatus());
    }

    @QTestCases(id = "521")
    @Test(description = "Media - Vod - Playback (Autoplay)", priority = 1)
    public void testPlayMediaPlayer() {

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

    }

    @QTestCases(id = "522")
    @Test(description = "Media - Vod - Paused", priority = 2)
    public void testPauseMediaPlayer() {

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);
        mP.pauseVideo(2);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

    }

    @QTestCases(id = "523")
    @Test(description = "Media - Vod - Resume", priority = 3)
    public void testResumeMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);
        mP.pauseVideo(2);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

        mP.playVideo(3);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertAll();

    }

    @QTestCases(id = "527")
    @Test(description = "Media - Captions Enabled", priority = 4)
    public void testEnableMediaCaptions() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playUrlCaptions(true);

        sa.assertTrue(mP.getCaptionStatus());

        sa.assertAll();

    }

    @QTestCases(id = "528")
    @Test(description = "Media - Captions Disabled", priority = 5)
    public void testDisableMediaCaptions() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playUrlCaptions(false);

        sa.assertFalse(mP.getCaptionStatus());

        sa.assertAll();

    }

    @QTestCases(id = "529")
    @Test(description = "Media - Confirm seek to start", priority = 6)
    public void testSeekToStart() {

        mP.playSeekToStart(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);

        float finalStreamAssert = mP.getCurrentStreamTime();

        Assert.assertTrue( 0 <= finalStreamAssert && finalStreamAssert <= 2
                , String.format("Expected Stream Value to Seek to Start, Actual Offset Value: %s", finalStreamAssert));

    }

    @QTestCases(id = "630")
    @Test(description = "Media - Confirm paused video does not resume after 10 second rewind", priority = 7)
    public void testRewindPause() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playRewindPause(10, 10);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

        sa.assertAll();

    }

    @QTestCases(id = "538")
    @Test(description = "Media - Completed - VOD", priority = 8)
    public void testCompleteMediaPlayer() {

        mP.playUrlByStreamEndVod(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD, 1800, 30);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(ENDED), ENDED,
                mP.assertMessageMediaPlayerStatus(ENDED));

    }

    @QTestCases(id = "541")
    @Test(description = "Media - Confirm seek by seconds", priority = 9)
    public void testSeekBySeconds() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.seekOffsetInt(500);

        float finalStreamAssert = mP.getCurrentStreamTime();

        sa.assertTrue( 500 <= finalStreamAssert && finalStreamAssert < 501
                , String.format("Expected Stream Value is 500, Actual Offset Value: %s", finalStreamAssert));

        sa.assertAll();

    }

    @QTestCases(id = "539")
    @Test(description = "Media - Confirm offset by seconds", priority = 10)
    public void testOffsetBySeconds() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlSetSeekStart(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD, 500);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        float finalStreamAssert = mP.getCurrentStreamTime();

        sa.assertTrue( 500 <= finalStreamAssert && finalStreamAssert < 501
                , String.format("Expected Stream Value is 500, Actual Offset Value: %s", finalStreamAssert));


    }

    @QTestCases(id = "669")
    @Test(description = "Media - Confirm seek by PDT", priority = 9)
    public void testSeekByPDT() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.seekOffsetString("2016-10-18T22:44:55+00:00");

        float finalStreamAssert = mP.getCurrentStreamTime();

        sa.assertTrue( 900 <= finalStreamAssert && finalStreamAssert < 901
                , String.format("Expected Stream Value is 900, Actual Offset Value: %s", finalStreamAssert));

        sa.assertAll();

    }

    @QTestCases(id = "670")
    @Test(description = "Media - Confirm offset by PDT", priority = 10)
    public void testOffsetByPDT() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlSetSeekStartPDT(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_VOD, "2016-10-18T22:44:55+00:00");

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        float finalStreamAssert = mP.getCurrentStreamTime();

        sa.assertTrue( 900 <= finalStreamAssert && finalStreamAssert < 901
                , String.format("Expected Stream Value is 900, Actual Offset Value: %s", finalStreamAssert));


    }


}
