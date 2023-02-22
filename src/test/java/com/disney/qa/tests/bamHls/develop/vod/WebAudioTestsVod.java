package com.disney.qa.tests.bamHls.develop.vod;

import com.disney.qa.bamhls.utilities.BamHlsParameter;
import com.disney.qa.bamhls.web.BamHlsAudioHelper;
import com.disney.qa.tests.bamHls.bamHlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class WebAudioTestsVod extends bamHlsBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @BeforeMethod
    public void baseEvent() {
        LOGGER.info("VOD TESTING");
        mA = new BamHlsAudioHelper(getDriver());
        mA.newSession(BamHlsParameter.BAMHLS_DEVELOP_URL.getValue(), 1920, 1080, 5);
        Assert.assertEquals("200", mA.getHttpStatus());
    }

    @QTestCases(id = "546")
    @Test(description = "Media - Confirm available audio feeds are returned", priority = 1)
    public void testAudioFeeds() {
        SoftAssert sa = new SoftAssert();

        mA.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT);

        sa.assertEquals(mA.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mA.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertTrue(mA.getAudioTrackSelectList().equals(audioStreamsList()),
                mA.assertMessageEquals(AUDIO, mA.getAudioTrackSelectList().toString(), audioStreamsList().toString()));

        sa.assertAll();
    }

    @QTestCases(id = "549")
    @Test(description = "Media - Confirm video can be started with non-default language", priority = 2)
    public void testNonDefaultLanguageStart () {
        SoftAssert sa = new SoftAssert();

        String russian = "rus";

        mA.setPlaySetInitialLanguage(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT, russian);

        sa.assertEquals(mA.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mA.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertEquals(mA.getInitialAudioLanguage(), russian,
                mA.assertMessageEquals(AUDIO, mA.getInitialAudioLanguage(), russian));

        sa.assertAll();
    }

    @QTestCases(id = "551")
    @Test(description = "Media - Confirm language can be changed during playback (NAME)", priority = 3)
    public void testPlaybackLanguageChange() {
        SoftAssert sa = new SoftAssert();

        mA.playUrlByStream(BAM_HLS_WEB_QA_TEST_STREAMS, WWE_TS_EVENT);

        sa.assertEquals(mA.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mA.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertEquals(mA.getCurrentAudioTrack(), ENGLISH_ENG,
                mA.assertMessageEquals(AUDIO, mA.getCurrentAudioTrack(), ENGLISH_ENG));

        mA.setAudioByLanguageUi(ESPANOL_SPA);

        sa.assertEquals(mA.getDesiredMediaPlayerStateStatus(PLAYING), PLAYING,
                mA.assertMessageMediaPlayerStatus(PLAYING));

        sa.assertEquals(mA.getCurrentAudioTrack(), ESPANOL_SPA,
                mA.assertMessageEquals(AUDIO, mA.getCurrentAudioTrack(), ESPANOL_SPA));

        sa.assertAll();
    }
}
