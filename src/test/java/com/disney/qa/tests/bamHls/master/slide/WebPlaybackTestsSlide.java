package com.disney.qa.tests.bamHls.master.slide;

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

public class WebPlaybackTestsSlide extends bamHlsBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @BeforeMethod
    public void baseEvent() {
        LOGGER.info("SLIDE TESTING");
        mP = new BamHlsPlaybackHelper(getDriver());
        mP.newSession(BamHlsParameter.BAMHLS_DEVELOP_URL.getValue(), 1920, 1080, 5);
        Assert.assertEquals("200", mP.getHttpStatus());
    }

    @QTestCases(id = "521")
    @Test(description = "Media - Slide - Playback (Autoplay)", priority = 1)
    public void testPlayMediaPlayer() {

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

    }

    @QTestCases(id = "522")
    @Test(description = "Media - Slide - Paused", priority = 2)
    public void testPauseMediaPlayer() {

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE);
        mP.pauseVideo(2);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

    }

    @QTestCases(id = "523")
    @Test(description = "Media - Slide - Resume", priority = 3)
    public void testResumeMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE);
        mP.pauseVideo(2);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

        mP.playVideo(3);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertAll();

    }

    @QTestCases(id = "524")
    @Test(description = "Completed - Slide", priority = 4)
    public void testCompleteMediaPlayer() {

        mP.playUrlByStreamEnd(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE_1736, 65);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(ENDED), ENDED,
                mP.assertMessageMediaPlayerStatus(ENDED));

    }

    @QTestCases(id = "527")
    @Test(description = "Media - Captions Enabled", priority = 5)
    public void testEnableMediaCaptions() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playUrlCaptions(true);

        sa.assertTrue(mP.getCaptionStatus());

        sa.assertAll();

    }

    @QTestCases(id = "528")
    @Test(description = "Media - Captions Disabled", priority = 6)
    public void testDisableMediaCaptions() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playUrlCaptions(false);

        sa.assertFalse(mP.getCaptionStatus());

        sa.assertAll();

    }

    @QTestCases(id = "530")
    @Test(description = "Media - Confirm seek to live", priority = 7)
    public void testSeektoLive() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlSeekTolive(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertTrue(mP.isPlayerLive());

        sa.assertAll();

    }

    @QTestCases(id = "630")
    @Test(description = "Media - Confirm paused video does not resume after 10 second rewind", priority = 8)
    public void testRewindPause() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_SLIDE);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playRewindPause(10, 10);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

        sa.assertAll();

    }




}
