package com.disney.qa.tests.hls.web.audio;


import com.disney.qa.hls.utilities.HlsAssertService;
import com.disney.qa.hls.utilities.HlsParameter;
import com.disney.qa.hls.web.HlsPlayerLateBoundAudioHelper;
import com.disney.qa.tests.hls.web.HlsBaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class HlsVodLateBoundAudioTest extends HlsBaseTest{

    private HlsAssertService hlsAssert;
    private HlsPlayerLateBoundAudioHelper hP;

    @BeforeMethod()
    public void baseEvent() {
        hP = new HlsPlayerLateBoundAudioHelper(getDriver());
        hP.newSession(HlsParameter.HLS_DEV_MASTER_URL.getValue());
        hlsAssert = new HlsAssertService(getDriver());
    }

    @QTestCases(id = "6371")
    @Test(description = "Media - Confirm available audio feeds are returned", priority = 1)
    public void testConfirmAudioFeeds() {
        SoftAssert sa = new SoftAssert();

        hP.hlsLateBoundConfirmAudioFeeds(HlsPlayerLateBoundAudioHelper.TEST_URL_VOD);

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_POR_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_POR_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_POR_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_POR_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_DEU_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_DEU_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_DEU_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_DEU_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_JPN_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_JPN_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_JPN_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_JPN_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_ITA_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_ITA_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_ITA_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_ITA_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_FRE_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_FRE_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_FRE_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_FRE_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertAll();
    }

    @QTestCases(id = "6372")
    @Test(description = "Media - Confirm default audio track language is 'eng'", priority = 2)
    public void testConfirmDefaultAudioTrack() {
        SoftAssert sa = new SoftAssert();

        hP.hlsLateBoundConfirmAudioFeeds(HlsPlayerLateBoundAudioHelper.TEST_URL_VOD);

        sa.assertEquals(hlsAssert.getMediaAudioFeedTrack(), "eng",
                String.format("Expected (eng), Actual: %s", hlsAssert.getMediaAudioFeedTrackNoReadout()));

        sa.assertAll();
    }

    @QTestCases(id = "6373")
    @Test(description = "Media - Confirm default audio track is 'eng'", priority = 3)
    public void testConfirmDefaultAudioTrackEng() {
        SoftAssert sa = new SoftAssert();

        hP.hlsLateBoundConfirmAudioFeeds(HlsPlayerLateBoundAudioHelper.TEST_URL_VOD);

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_ENG_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertAll();
    }

    @QTestCases(id = "6374")
    @Test(description = "Media - Confirm video can be started with non-default language", priority = 4)
    public void testConfirmNonDefaultlanguage() {
        SoftAssert sa = new SoftAssert();

        hP.hlsConfirmAudioFeedSetLanguage(HlsPlayerLateBoundAudioHelper.TEST_URL_VOD,"rus");

        sa.assertEquals(hlsAssert.getMediaAudioFeedTrack(), "rus",
                String.format("Expected (rus), Actual: %s", hlsAssert.getMediaAudioFeedTrackNoReadout()));

        sa.assertAll();
    }

    @QTestCases(id = "6375")
    @Test(description = "Media - Confirm language can be changed during playback", priority = 5)
    public void testConfirmLanguageChange() {
        SoftAssert sa = new SoftAssert();

        hP.hlsConfirmLanguageChange(HlsPlayerLateBoundAudioHelper.TEST_URL_VOD, "rus");

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_RUS_96K, hlsAssert.getMediaAudioFeeds()));


        sa.assertAll();
    }

    @QTestCases(id = "6376")
    @Test(description = "Media - Confirm language can be changed during playback (NAME)", priority = 6)
    public void testConfirmLanguageEspanol() {
        SoftAssert sa = new SoftAssert();

        hP.hlsConfirmLanguageNamePlayback(HlsPlayerLateBoundAudioHelper.TEST_URL_VOD, "Español (fox deportes)");

        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_48K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_48K, hlsAssert.getMediaAudioFeeds()));
        sa.assertTrue(hlsAssert.getMediaAudioFeeds().contains(HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_96K),
                String.format("Expected LBA Audio feeds to contain %s , Actual full output: '%s'", HlsPlayerLateBoundAudioHelper.HLS_LBA_SPA_96K, hlsAssert.getMediaAudioFeeds()));

        sa.assertAll();

    }
}