package com.disney.qa.tests.hls.web.audio;

import com.disney.qa.hls.utilities.HlsAssertService;
import com.disney.qa.hls.utilities.HlsParameter;
import com.disney.qa.hls.web.HlsPlayerLateBoundAudioHelper;
import com.disney.qa.tests.hls.web.HlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class HlsSlideLateBoundAudioTestCMAF extends HlsBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private HlsAssertService hlsAssert;
    private HlsPlayerLateBoundAudioHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerLateBoundAudioHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
        hlsAssert = new HlsAssertService(getDriver());

    }

    @QTestCases(id = "6512")
    @Test(description = "Media - Confirm available audio feeds are returned",  priority = 1)
    public void testConfirmAudioFeeds() {

        SoftAssert sa = new SoftAssert();

        hP.hlsLateBoundConfirmAudioFeeds(HlsPlayerLateBoundAudioHelper.TEST_URL_CMAF_URL_SLIDE_UNENC);

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains("language=en"),
                String.format("Expected audio feeds to contain (language=en), Actual: '%s'", hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_AAC_96K),
                String.format("Expected audio feeds to contain %s, Actual: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_AAC_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains("language=sp"),
                String.format("Expected audio feeds to contain (language=sp), Actual: '%s'", hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_ALTAAC_96K),
                String.format("Expected audio feeds to contain %s, Actual: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_ALTAAC_96K, hlsAssert.getMediaAudioFeeds()));


        sa.assertAll();
    }

    @QTestCases(id = "6513")
    @Test(description = "Media - Confirm default audio track language is 'eng'", priority = 2)
    public void testConfirmDefaultAudioTrack() {
        SoftAssert sa = new SoftAssert();

        hP.hlsLateBoundConfirmAudioFeeds(HlsPlayerLateBoundAudioHelper.TEST_URL_CMAF_URL_SLIDE_UNENC);

        sa.assertEquals(hlsAssert.getMediaAudioFeedTrack(), "en",
                String.format("Expected (en), Actual: %s", hlsAssert.getMediaAudioFeedTrackNoReadout()));

        sa.assertAll();

    }

    @QTestCases(id = "6514")
    @Test(description = "Media - Confirm default audio track is 'en'", priority = 3)
    public void testConfirmDefaultAudioTrackEng() {
        SoftAssert sa = new SoftAssert();

        hP.hlsLateBoundConfirmAudioFeeds(HlsPlayerLateBoundAudioHelper.TEST_URL_CMAF_URL_SLIDE_UNENC);

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains("language=en"),
                String.format("Expected audio feeds to contain (language=en), Actual: '%s'", hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_AAC_96K),
                String.format("Expected audio feeds to contain %s, Actual: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_AAC_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertAll();
    }

    @QTestCases(id = "6515")
    @Test(description = "Media - Confirm video can be started with non-default language", priority = 4)
    public void testConfirmNonDefaultLanguage() {
        SoftAssert sa = new SoftAssert();

        hP.hlsConfirmAudioFeedSetLanguage(HlsPlayerLateBoundAudioHelper.TEST_URL_CMAF_URL_SLIDE_UNENC,"sp");

        sa.assertEquals(hlsAssert.getMediaAudioFeedTrack(), "sp",
                String.format("Expected (sp), Actual: %s", hlsAssert.getMediaAudioFeedTrackNoReadout()));


        sa.assertAll();
    }

    @QTestCases(id = "6516")
    @Test(description = "Media - Confirm language can be changed during playback", priority = 5)
    public void testConfirmLanguageChange() {
        SoftAssert sa = new SoftAssert();

        hP.hlsConfirmLanguageChange(HlsPlayerLateBoundAudioHelper.TEST_URL_CMAF_URL_SLIDE_UNENC, "sp");

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains("language=sp"),
                String.format("Expected audio feeds to contain (language=sp), Actual: '%s'", hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_ALTAAC_96K),
                String.format("Expected audio feeds to contain %s, Actual: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_ALTAAC_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertAll();

    }

    @QTestCases(id = "6517")
    @Test(description = "Media - Confirm language can be changed during playback (NAME)", priority = 6)
    public void testConfirmLateBoundAudioChangeName() {
        SoftAssert sa = new SoftAssert();

        hP.hlsConfirmLanguageNamePlayback(HlsPlayerLateBoundAudioHelper.TEST_URL_CMAF_URL_SLIDE_UNENC, "Alt");

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains("name=Alt"),
                String.format("Expected audio feeds to contain (name=Alt), Actual: '%s'", hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_ALTAAC_96K),
                String.format("Expected audio feeds to contain %s, Actual: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_CMAF_ALTAAC_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertAll();


    }




}