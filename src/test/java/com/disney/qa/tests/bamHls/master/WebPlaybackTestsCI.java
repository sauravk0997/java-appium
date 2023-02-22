package com.disney.qa.tests.bamHls.master;

import com.disney.qa.bamhls.utilities.BamHlsParameter;
import com.disney.qa.bamhls.web.BamHlsPlaybackHelper;
import com.disney.qa.tests.bamHls.bamHlsBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class WebPlaybackTestsCI extends bamHlsBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * All Stream Values in this class are placeholders, values are overridden via custom string values passed in Jenkins by Developers
     */

    @BeforeMethod
    public void baseEvent() {
        LOGGER.info("TESTING CI");
        mP = new BamHlsPlaybackHelper(getDriver());
        mP.newSession(BamHlsParameter.BAMHLS_MASTER_URL.getValue(), 1920, 1080, 5);
        Assert.assertEquals("200", mP.getHttpStatus());
    }

    @Test(description = "Media - CI - Playback (Autoplay)", priority = 1)
    public void testPlayMediaPlayer() {

        mP.playUrlByStream(DRM_CMAF_UNENCRYPTED_ALL, CMAF_UNENCRYPTED_TOS6_LW_EVENT);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

    }

    @Test(description = "Media - CI - Paused", priority = 2)
    public void testPauseMediaPlayer() {

        mP.playUrlByStream(DRM_CMAF_UNENCRYPTED_ALL, CMAF_UNENCRYPTED_TOS6_LW_EVENT);
        mP.pauseVideo(2);

        Assert.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

    }

    @Test(description = "Media - CI - Resume", priority = 3)
    public void testResumeMediaPlayer() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(DRM_CMAF_UNENCRYPTED_ALL, CMAF_UNENCRYPTED_TOS6_LW_EVENT);
        mP.pauseVideo(2);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

        mP.playVideo(5);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertAll();

    }

    @Test(description = "Media - Captions Enabled", priority = 4)
    public void testEnableMediaCaptions() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(DRM_CMAF_UNENCRYPTED_ALL, CMAF_UNENCRYPTED_TOS6_LW_EVENT);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playUrlCaptions(true);

        sa.assertTrue(mP.getCaptionStatus());

        sa.assertAll();

    }

    @Test(description = "Media - Captions Disabled", priority = 5)
    public void testDisableMediaCaptions() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(DRM_CMAF_UNENCRYPTED_ALL, CMAF_UNENCRYPTED_TOS6_LW_EVENT);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playUrlCaptions(false);

        sa.assertFalse(mP.getCaptionStatus());

        sa.assertAll();

    }

    @Test(description = "Media - Confirm seek to start")
    public void testSeekToStart() {

        mP.playSeekToStart(DRM_CMAF_UNENCRYPTED_ALL, CMAF_UNENCRYPTED_TOS6_LW_EVENT);

        float finalStreamAssert = mP.getCurrentStreamTime();

        Assert.assertTrue( 0 <= finalStreamAssert && finalStreamAssert <= 2
                , String.format("Expected Stream Value to Seek to Start, Actual Offset Value: %s", finalStreamAssert));

    }

    @Test(description = "Media - Confirm paused video does not resume after 10 second rewind")
    public void testRewindPause() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(DRM_CMAF_UNENCRYPTED_ALL, CMAF_UNENCRYPTED_TOS6_LW_EVENT);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.playRewindPause(10, 10);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PAUSED), PAUSED,
                mP.assertMessageMediaPlayerStatus(PAUSED));

        sa.assertAll();

    }


}
