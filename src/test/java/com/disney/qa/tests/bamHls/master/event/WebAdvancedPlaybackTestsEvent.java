package com.disney.qa.tests.bamHls.master.event;

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
import java.util.ArrayList;
import java.util.Arrays;

public class WebAdvancedPlaybackTestsEvent extends bamHlsBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @BeforeMethod
    public void baseEvent() {
        LOGGER.info("EVENT TESTING");
        mP = new BamHlsPlaybackHelper(getDriver());
        mP.newSession(BamHlsParameter.BAMHLS_MASTER_URL.getValue(), 1920, 1080, 5);
        Assert.assertEquals("200", mP.getHttpStatus());
    }

    @QTestCases(id = "613")
    @Test(description = "Player - Player reaches top bitrate in less than a second", priority = 1)
    public void testBitRateTop() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertEquals(mP.getCurrentBitRateUi(), BITRATE_1546,
                mP.assertMessageEquals(BITRATE, mP.getCurrentBitRateUi(), BITRATE_1546));

        sa.assertAll();

    }

    @QTestCases(id = "614")
    @Test(description = "Player - Bitrate remains static when seeking video", priority = 2)
    public void testBitRateStaticSeek() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertEquals(mP.getCurrentBitRateUi(), BITRATE_1546,
                mP.assertMessageEquals(BITRATE, mP.getCurrentBitRateUi(), BITRATE_1546));

        mP.playPauseVideo(20);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertEquals(mP.getCurrentBitRateUi(), BITRATE_1546,
                mP.assertMessageEquals(BITRATE, mP.getCurrentBitRateUi(), BITRATE_1546));


        sa.assertAll();
    }

    @QTestCases(id = "621")
    @Test(description = "Player - Seek multiple times backward", priority = 3)
    public void testSeekBackwardsMultiple() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.waitForVideoPause(40, true);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.seekOffsetInt(30);

        sa.assertTrue(30 <= mP.getCurrentStreamTime() && mP.getCurrentStreamTime() <= 30.9 ,
                mP.assertStreamRangeTrue("30", mP.getCurrentStreamTime()));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.seekOffsetInt(20);

        sa.assertTrue(20 <= mP.getCurrentStreamTime() && mP.getCurrentStreamTime() <= 20.9 ,
                mP.assertStreamRangeTrue("20", mP.getCurrentStreamTime()));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.seekOffsetInt(10);

        sa.assertTrue(10 <= mP.getCurrentStreamTime() && mP.getCurrentStreamTime() <= 10.9 ,
                mP.assertStreamRangeTrue("10", mP.getCurrentStreamTime()));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.seekOffsetInt(0);

        sa.assertTrue(0 <= mP.getCurrentStreamTime() && mP.getCurrentStreamTime() <= 0.09 ,
                mP.assertStreamRangeTrue("0", mP.getCurrentStreamTime()));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));



        sa.assertAll();
    }

    @QTestCases(id = "622")
    @Test(description = "Player - Seek multiple times away from completed tail", priority = 4)
    public void testSeekAwayTail() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStreamEnd(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT_1736, 65);

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(ENDED), ENDED,
                mP.assertMessageMediaPlayerStatus(ENDED));

        float firstAssertValue = mP.getCurrentStreamTime();
        mP.seekBackwardCurrentOffset(10);

        sa.assertTrue(firstAssertValue > mP.getCurrentStreamTime(),
                mP.assertTrueMessage(Float.toString(firstAssertValue),  Float.toString(mP.getCurrentStreamTime())));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        float secondAssertValue = mP.getCurrentStreamTime();
        mP.seekBackwardCurrentOffset(10);

        sa.assertTrue(secondAssertValue > mP.getCurrentStreamTime(),
                mP.assertTrueMessage(Float.toString(secondAssertValue),  Float.toString(mP.getCurrentStreamTime())));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        float thirdAssertValue = mP.getCurrentStreamTime();
        mP.seekBackwardCurrentOffset(10);

        sa.assertTrue(thirdAssertValue > mP.getCurrentStreamTime(),
                mP.assertTrueMessage(Float.toString(thirdAssertValue),  Float.toString(mP.getCurrentStreamTime())));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        float fourthAssertValue = mP.getCurrentStreamTime();
        mP.seekBackwardCurrentOffset(10);

        sa.assertTrue(fourthAssertValue > mP.getCurrentStreamTime(),
                mP.assertTrueMessage(Float.toString(fourthAssertValue),  Float.toString(mP.getCurrentStreamTime())));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));


        sa.assertAll();

    }

    @QTestCases(id = "631")
    @Test(description = "Player - Short chunks do not impact playback", priority = 5)
    public void testShortChunksNoPlaybackImpact() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, ESPN_SHORT_CHUNK_TS_SILK_EVENT);

        float startTime = mP.getCurrentStreamTime();

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.waitForVideoPause(60, true);

        float endTime = mP.getCurrentStreamTime();

        LOGGER.info(mP.getMediaPlayerStatus());

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));
        sa.assertTrue(startTime < endTime);

        sa.assertAll();

    }

    @QTestCases(id = "671")
    @Test(description = "Player - Playback rate can be adjusted without crashing player", priority = 6)
    public void testPlaybackRateAdjust() {
        SoftAssert sa = new SoftAssert();

        ArrayList<String> assertArray = new ArrayList<>(Arrays.asList("1", "4", "1", "2", "1"));
        ArrayList<String> dataArray = new ArrayList<>();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT);

        dataArray.add(mP.getPlaybackRate());

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.waitForVideoPause(10, true);
        mP.seekVideoRewind(10);

        LOGGER.info("Increasing bitrate to 4x");
        mP.playSetPlaybackRate(4);

        dataArray.add(mP.getPlaybackRate());

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        LOGGER.info("Waiting for Video to return to 1x bitrate");

        mP.waitForVideoPause(5, true);

        dataArray.add(mP.getPlaybackRate());

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.waitForVideoPause(10, true);
        mP.seekVideoRewind(5);

        LOGGER.info("Increasing bitrate to 2x");

        mP.playSetPlaybackRate(2);

        dataArray.add(mP.getPlaybackRate());

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        LOGGER.info("Waiting for Video to return to 1x bitrate");

        mP.waitForVideoPause(15, true);

        dataArray.add(mP.getPlaybackRate());

        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertEquals(dataArray, assertArray,
                "Playback rate is not equal to state at assertion.");

        sa.assertAll();

    }

    @QTestCases(id = "672")
    @Test(description = "Player - Playback rates greater than 1x revert to 1x when end of buffer is reached", priority = 7)
    public void testPlaybackRateRevert() {
        SoftAssert sa = new SoftAssert();

        mP.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT);

        sa.assertEquals(mP.getPlaybackRate(), "1",
                mP.assertMessageEquals(PLAYBACK, mP.getPlaybackRate(), "1"));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.waitForVideoPause(10, true);
        mP.seekVideoRewind(10);

        LOGGER.info("Increasing bitrate to 4x");
        mP.playSetPlaybackRate(4);

        sa.assertEquals(mP.getPlaybackRate(), "4",
                mP.assertMessageEquals(PLAYBACK, mP.getPlaybackRate(), "4"));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        LOGGER.info("Waiting for Video to return to 1x bitrate");

        mP.waitForVideoPause(5, true);

        LOGGER.info(String.format("Current bitrate: %s", mP.getCurrentBitRateUi()));

        sa.assertEquals(mP.getPlaybackRate(), "1",
                mP.assertMessageEquals(PLAYBACK, mP.getPlaybackRate(), "1"));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        mP.waitForVideoPause(10, true);

        LOGGER.info("Increasing bitrate to 2x");
        mP.playSetPlaybackRate(2);

        sa.assertEquals(mP.getPlaybackRate(), "2",
                mP.assertMessageEquals(PLAYBACK, mP.getPlaybackRate(), "2"));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        LOGGER.info("Waiting for Video to return to 1x bitrate");

        mP.waitForVideoPause(5, true);

        LOGGER.info(String.format("Current bitrate: %s", mP.getCurrentBitRateUi()));

        sa.assertEquals(mP.getPlaybackRate(), "1",
                mP.assertMessageEquals(PLAYBACK, mP.getPlaybackRate(), "1"));
        sa.assertEquals(mP.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mP.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertAll();

    }


}

