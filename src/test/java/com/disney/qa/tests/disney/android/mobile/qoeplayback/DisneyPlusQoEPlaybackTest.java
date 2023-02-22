package com.disney.qa.tests.disney.android.mobile.qoeplayback;

import com.disney.qa.api.dgi.validationservices.hora.EventChecklist;
import com.disney.qa.disney.android.pages.common.DisneyPlusMediaPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.HARUtils;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;

public class DisneyPlusQoEPlaybackTest extends BaseDisneyTest {

    public static final String DISNEY_PROD_SERIES_DEEPLINK = "disney_prod_series_landing_page_deeplink";
    public static final String DISNEY_PROD_MOVIES_DEEPLINK = "disney_prod_movies_landing_page_deeplink";
    public static final String SERIES_DUCKTALES = "/ducktales/tc6CG7H7lhCE";
    public static final String SERIES_THESIMPSONS = "/the-simpsons/3ZoBZ52QHb4x";
    public static final String SERIES_THEMANDALORIAN = "/themandalorian/3jLIGMDYINqD";
    public static final String MOVIE_PETER_PAN = "/peter-pan/20a1PB36VPVF";
    public static final String MOVIE_DUMBO = "/dumbo/3lAXKmGUOnjh";

    @BeforeMethod(alwaysRun = true)
    public void startProxyAndDictionaries() {
        if (!isConfigLocaleUS() || horaEnabled()) {
            startProxyAndRestart();
        }
    }

    private void testSetup(Boolean autoPlay) {
        if(!autoPlay) {
            try {
                accountApi.get().patchProfileAutoplayStatus(disneyAccount.get(), false);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Something went wrong with patching the account");
            }
        }

        login(disneyAccount.get(), false);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10318"})
    @Test(description = "Test StartupSequence QoE event - validated by Sdp in checkAssertions method", groups = {TestGroup.QOE_PLAYBACK})
    public void testStartupSequenceQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1661","XAQA-1578","XAQA-2208",ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(true);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_DUMBO);
        mediaPageBase.dismissPopup();
        mediaPageBase.getPlayButton().click();
        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        pause(30);

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-11002"})
    @Test(description = "Smoke Test Playback QoE events", groups = {TestGroup.QOE_PLAYBACK})
    public void testPlaybackSmokeQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1577","XAQA-1257","XAQA-1107","XAQA-1106", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        videoPageBase.tapOnPlayPauseButton();

        analyticPause();

        Assert.assertTrue(videoPageBase.isSkipIntroPresent(),
                "Playback did not pause");

        videoPageBase.clickVideoPlayerFrame();
        videoPageBase.tapOnPlayPauseButton();

        analyticPause();

        videoPageBase.clickJumpForwardButton();
        pause(20);
        videoPageBase.clickJumpBackwardButton();
        pause(20);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity", "started" );
        item1.addRequirement("exact","partner", "disney");
        item1.addRequirement("exact","mediaTitle", "DuckTales - s1e1 - d73ccb4c-5805-4062-ae6d-6d33d264893e");
        item1.addRequirement("exact","productType", "VOD");
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","paused");
        item2.addRequirement("exact","cause", "user" );
        item2.addRequirement("exact","partner", "disney");
        item2.addRequirement("exact","mediaTitle", "DuckTales - s1e1 - d73ccb4c-5805-4062-ae6d-6d33d264893e");
        item2.addRequirement("exact","productType", "VOD");
        checkList.add(item2);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact","playbackActivity","resumed");
        item3.addRequirement("exact","cause", "user" );
        item3.addRequirement("exact","partner", "disney");
        item3.addRequirement("exact","mediaTitle", "DuckTales - s1e1 - d73ccb4c-5805-4062-ae6d-6d33d264893e");
        item3.addRequirement("exact","productType", "VOD");
        checkList.add(item3);

        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item4.addRequirement("exact","playbackActivity","seekStarted");
        item4.addRequirement("contains","cause", new ArrayList<>(Arrays.asList("seek", "skip", "scrub")));
        item4.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "forward")));
        item4.addRequirement("class","seekDistance",java.lang.Long.class);
        item4.addRequirement("exact","partner", "disney");
        item4.addRequirement("exact","mediaTitle", "DuckTales - s1e1 - d73ccb4c-5805-4062-ae6d-6d33d264893e");
        item4.addRequirement("exact","productType", "VOD");
        checkList.add(item4);

        EventChecklist item5 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item5.addRequirement("exact","playbackActivity","seekStarted");
        item5.addRequirement("contains","cause", new ArrayList<>(Arrays.asList("seek", "skip", "scrub")));
        item5.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "backward")));
        item5.addRequirement("class","seekDistance",java.lang.Long.class);
        item5.addRequirement("exact","partner", "disney");
        item5.addRequirement("exact","mediaTitle", "DuckTales - s1e1 - d73ccb4c-5805-4062-ae6d-6d33d264893e");
        item5.addRequirement("exact","productType", "VOD");
        checkList.add(item5);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10324"})
    @Test(description = "Test Pause/Resume QoE event", groups = {TestGroup.QOE_PLAYBACK})
    public void testPauseResumeQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1662","XAQA-1094", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        videoPageBase.tapOnPlayPauseButton();

        analyticPause();

        Assert.assertTrue(videoPageBase.isSkipIntroPresent(),
                "Playback did not pause");

        videoPageBase.clickVideoPlayerFrame();

        videoPageBase.tapOnPlayPauseButton();

        analyticPause();

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","paused");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","resumed");
        item2.addRequirement("exact","cause", "user" );
        checkList.add(item2);

        checkAssertions(sa, checkList);
    }
    
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10323"})
    @Test(description = "Test Quick Seek rebuffering not induced QoE events", groups = {TestGroup.QOE_PLAYBACK})
    public void testQuickSeekQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1667","XAQA-1101", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(true);
        JSONArray checkList = new JSONArray();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        videoPageBase.clickJumpForwardButton();
        pause(20);
        videoPageBase.clickJumpBackwardButton();
        pause(20);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","seekStarted");
        item1.addRequirement("exact","cause", "skip" );
        item1.addRequirement("exact","playerSeekDirection","forward");
        item1.addRequirement("class","seekDistance",java.lang.Long.class);
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","seekEnded");
        item2.addRequirement("exact","cause", "skip" );
        item2.addRequirement("exact","playerSeekDirection","forward");
        item2.addRequirement("class","seekDistance",java.lang.Long.class);
        item2.addRequirement("class","seekSize",java.lang.Long.class);
        checkList.add(item2);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact","playbackActivity","seekStarted");
        item3.addRequirement("exact","cause", "skip" );
        item3.addRequirement("exact","playerSeekDirection","backward");
        item3.addRequirement("class","seekDistance",java.lang.Long.class);
        checkList.add(item3);

        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item4.addRequirement("exact","playbackActivity","seekEnded");
        item4.addRequirement("exact","cause", "skip" );
        item4.addRequirement("exact","playerSeekDirection","backward");
        item4.addRequirement("class","seekDistance",java.lang.Long.class);
        item4.addRequirement("class","seekSize",java.lang.Long.class);
        checkList.add(item4);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10323"})
    @Test(description = "Test Skip Intro Seek rebuffering not induced QoE events", groups = {TestGroup.QOE_PLAYBACK})
    public void testSkipIntroSeekQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1667","XAQA-1101", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(true);
        JSONArray checkList = new JSONArray();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        sa.assertTrue(videoPageBase.isSkipIntroPresent(),
                "Skip Intro button is not displayed");
        videoPageBase.skipIntro();

        pause(30);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","seekStarted");
        item1.addRequirement("exact","cause", "skip" );
        item1.addRequirement("exact","playerSeekDirection","forward");
        item1.addRequirement("class","seekDistance",java.lang.Long.class);
        item1.addRequirement("class","seekSize",java.lang.Long.class);
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","seekEnded");
        item2.addRequirement("exact","cause", "skip" );
        item2.addRequirement("exact","playerSeekDirection","forward");
        item2.addRequirement("class","seekDistance",java.lang.Long.class);
        item2.addRequirement("class","seekSize",java.lang.Long.class);
        checkList.add(item2);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact","playbackActivity","resumed");
        item3.addRequirement("exact","cause", "user" );
        checkList.add(item3);

        checkAssertions(sa, checkList);
    }

        @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10323"})
    @Test(description = "Test Skip Recap Seek rebuffering not induced QoE events", groups = {TestGroup.QOE_PLAYBACK})
    public void testSkipRecapSeekQoeEventRebufferingNotInduced() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1667","XAQA-1101", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_THEMANDALORIAN);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        videoPageBase.scrubToPercentage(95);
        videoPageBase.isPlayNextEpisodeBtnPresent();
        videoPageBase.clickPlayNextEpisodeBtn();

        videoPageBase.skipRecap();

        pause(20);

            EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
            item1.addRequirement("exact","playbackActivity","paused");
            item1.addRequirement("exact","cause", "user" );
            checkList.add(item1);

            EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
            item2.addRequirement("exact","playbackActivity","seekStarted");
            item2.addRequirement("exact","cause", "skip" );
            item2.addRequirement("exact","playerSeekDirection","forward");
            item2.addRequirement("class","seekDistance",java.lang.Long.class);
            item2.addRequirement("class","seekSize",java.lang.Long.class);
            checkList.add(item2);

            EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
            item3.addRequirement("exact","playbackActivity","seekEnded");
            item3.addRequirement("exact","cause", "skip" );
            item3.addRequirement("exact","playerSeekDirection","forward");
            item3.addRequirement("class","seekDistance",java.lang.Long.class);
            item3.addRequirement("class","seekSize",java.lang.Long.class);
            checkList.add(item3);

            EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
            item4.addRequirement("exact","playbackActivity","resumed");
            item4.addRequirement("exact","cause", "user" );
            checkList.add(item4);

            checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10323"})
    @Test(description = "Test Drag To Seek rebuffering not induced QoE events", groups = {TestGroup.QOE_PLAYBACK})
    public void testDragToSeekQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1667","XAQA-1101", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(true);
        JSONArray checkList = new JSONArray();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        videoPageBase.scrubToPlaybackPercentage(50);

        pause(20);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","paused");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","seekStarted");
        item2.addRequirement("exact","cause", "seek" );
        item2.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "forward")));
        item2.addRequirement("class","seekDistance",java.lang.Long.class);
        checkList.add(item2);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact","playbackActivity","seekEnded");
        item3.addRequirement("exact","cause", "seek" );
        item2.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "forward")));
        item3.addRequirement("class","seekDistance",java.lang.Long.class);
        item3.addRequirement("class","seekSize",java.lang.Long.class);
        checkList.add(item3);

        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item4.addRequirement("exact","playbackActivity","resumed");
        item4.addRequirement("exact","cause", "user" );
        checkList.add(item4);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10326"})
    @Test(description = "Test EOS confirm cause = playedToEnd then AutoPlayed next episode QoE event", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testEndPlaybackAutoplayQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1668","XAQA-1123", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(true);
        JSONArray checkList = new JSONArray();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_THESIMPSONS);

        mediaPageBase.startPlayback();

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        String firstEpisode = videoPageBase.getActiveMediaTitle();

        videoPageBase.scrubToPercentage(95);
        videoPageBase.playVideoToCompletion();

        sa.assertFalse(videoPageBase.getActiveMediaTitle().equals(firstEpisode),
                "Autoplay did not play the next episode");

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","ended");
        item1.addRequirement("exact","cause", "playedToEnd" );
        checkList.add(item1);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10326"})
    @Test(description = "Test EOS confirm cause = playedToEnd then user clicked PlayUpNext QoE event", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testEndPlaybackUpNextQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1669","XAQA-1124", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);

        mediaPageBase.startPlayback();

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        videoPageBase.scrubToPercentage(92);
        videoPageBase.isPlayNextEpisodeBtnPresent();
        videoPageBase.clickPlayNextEpisodeBtn();
        videoPageBase.exitBeforeVideoStart(sa);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","ended");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-11202"})
    @Test(description = "Test EOS confirm cause = playback initiated then user clicked Player Back Arrow to Title Page", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testEndPlaybackByReturnToTitlePageQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1122", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);

        mediaPageBase.startPlayback();

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        pause(90);

        videoPageBase.closeVideo();

        pause(10);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","paused");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","ended");
        item2.addRequirement("exact","cause", "user" );
        checkList.add(item2);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10329"})
    @Test(description = "Test EBVS confirm cause = user when close player prior to playback starts QoE event", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testCloseVideoBeforePlaybackQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1670","XAQA-1242", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);

        mediaPageBase.startPlayback();

        Assert.assertTrue(videoPageBase.isOpened(),
                "Video Player did not launch");

        videoPageBase.exitBeforeVideoStart(sa);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","ended");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10329"})
    @Test(description = "Test EBVS confirm cause = user when click Up Next to next episode but exits before playback starts QoE event", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testEndBeforeVideoStartPlaybackUpNextQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1672","XAQA-1119", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);

        mediaPageBase.startPlayback();

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        videoPageBase.scrubToPercentage(92);
        videoPageBase.isPlayNextEpisodeBtnPresent();
        videoPageBase.clickPlayNextEpisodeBtn();
        videoPageBase.exitBeforeVideoStart(sa);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","ended");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10329"})
    @Test(description = "Test EBVS confirm cause = user when EBVS during NSA Warning QoE event", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testEndBeforeVideoStartPlaybackNsaQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1673","XAQA-1120", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_PETER_PAN);

        mediaPageBase.startPlayback();

        sa.assertTrue(videoPageBase.isContentAdvisoryPresent(), "Content Advisory screen is not displayed");

        analyticPause();

        videoPageBase.exitDuringNsa(sa);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","ended");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-11215"})
    @Test(description = "Test confirm Heartbeat event contains totalVst when asset has NSA Warning QoE event", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testNsaWarningPlaybackQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE,
                "XAQA-1673","XAQA-1316", ANALYTICS_SMOKE_TEST));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_PETER_PAN);

        mediaPageBase.startPlayback();

        sa.assertTrue(videoPageBase.isContentAdvisoryPresent(), "Content Advisory screen is not displayed");

        videoPageBase.waitForContentAdvisoryComplete();

        sa.assertFalse(videoPageBase.isContentAdvisoryPresent(), "Content Advisory screen did not complete");

        analyticPause();

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item1.addRequirement("class", "totalVst", java.lang.Long.class);
        checkList.add(item1);

        checkAssertions(sa, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-11379"})
    @Test(description = "Test heartbeat after playback begins QoE event", groups = {TestGroup.QOE_PLAYBACK})
    @Maintainer("jdemelle")
    public void testHeartbeatPlaybackStartedQoeEvent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-864"));
        SoftAssert sa = new SoftAssert();
        testSetup(false);
        JSONArray checkList = new JSONArray();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened() && videoPageBase.waitForVideoBuffering(),
                "Video Player did not launch");

        analyticPause();

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        checkList.add(item1);

        checkAssertions(sa, checkList);
    }
}
