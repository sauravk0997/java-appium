package com.disney.qa.tests.disney.apple.ios.qoeplayback;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.hora.validationservices.EventChecklist;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import com.disney.qa.hora.validationservices.HoraValidator;
import org.testng.asserts.SoftAssert;

public class DisneyPlusQoETest extends DisneyBaseTest {
//    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal();
    private static final String MOVIES = "Movies";


    private void loginAndStartPlayback(String content) {

    }
        private void checkAssertions(SoftAssert softAssert, String accountId, JSONArray checkList) {
        if (horaEnabled()) {
            LOGGER.info(accountId);
            HoraValidator hv = new HoraValidator(accountId);
            hv.assertValidation(softAssert);
            hv.checkListForPQOE(softAssert, checkList);
        }
        softAssert.assertAll();
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10318"})
    @Test(description = "Test StartupSequence QoE event - validated by Sdp in checkAssertions method")
    @Maintainer("isong1")
    public void testQoEStartupSequence(ITestContext context) {
//      xaqa-1578;
        initialSetup();
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPagesBase = new DisneyPlusHomeIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = new DisneyPlusSearchIOSPageBase(getDriver());
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = new DisneyPlusDetailsIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeIOSPAge = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        handleAlert();

        DisneyAccount account = disneyAccount.get();
        addHoraValidationSku(account);
//        disneyAccount.set(account);
        Assert.assertTrue(disneyPlusWelcomeIOSPAge.isOpened(), "Welcome screen did not launch");

        UniversalUtils.captureAndUpload(getCastedDriver());
        disneyPlusWelcomeIOSPAge.clickLogInButton();
//        disneyPlusLoginIOSPageBase.submitEmail(account.getEmail());
        disneyPlusLoginIOSPageBase.fillOutEmailField(account.getEmail());
        disneyPlusLoginIOSPageBase.clickPrimaryButton();

//        if(disneyPlusLoginIOSPageBase.isPrimaryButtonPresent()){
//            disneyPlusLoginIOSPageBase.clickPrimaryButton();
//        }
        pause(3);
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(account.getUserPass());
        Assert.assertTrue(disneyPlusHomeIOSPagesBase.isOpened(), "Home screen did not launch");
        pause(10);
        disneyPlusHomeIOSPagesBase.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search screen not displayed");
        searchPage.clickMoviesTab();
        sa.assertTrue(searchPage.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");

        searchPage.getDisplayedTitles().get(0).click();

        detailsIOSPageBase.clickPlayButton().waitForVideoToStart();
        Assert.assertTrue(videoPlayerPage.isOpened(), "Video Player did not launch");

        pause(20);
        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item1.addRequirement("exact", "startupActivity", "requested");
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item2.addRequirement("exact", "startupActivity", "fetched");
        checkList.add(item2);
        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item3.addRequirement("exact", "startupActivity", "masterFetched");
        checkList.add(item3);
        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item4.addRequirement("exact", "startupActivity", "variantFetched");
        checkList.add(item4);
        EventChecklist item6 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item6.addRequirement("exact", "startupActivity", "initialized");
        checkList.add(item6);
        EventChecklist item7 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item7.addRequirement("exact", "startupActivity", "ready");
        checkList.add(item7);
        EventChecklist item8 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item8.addRequirement("exact", "playbackActivity", "started");
        checkList.add(item8);
//        checkAssertions(sa);
//        sa.assertAll();
        checkAssertions(sa,account.getAccountId(), checkList);
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-11002"})
    @Test(description = "Smoke Test Playback QoE events")
    @Maintainer("isong1")
    public void testQoEPlayback(ITestContext context) {
//      xaqa-1577;
        initialSetup();
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPagesBase = new DisneyPlusHomeIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = new DisneyPlusSearchIOSPageBase(getDriver());
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = new DisneyPlusDetailsIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeIOSPAge = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        handleAlert();

        DisneyAccount account = disneyAccount.get();
              //  getAccountApi().createAccount("Yearly", locale, language, "V1");
        addHoraValidationSku(account);
        Assert.assertTrue(disneyPlusWelcomeIOSPAge.isOpened(), "Welcome screen did not launch");
        UniversalUtils.captureAndUpload(getCastedDriver());
        disneyPlusWelcomeIOSPAge.clickLogInButton();
        disneyPlusLoginIOSPageBase.fillOutEmailField(account.getEmail());
        disneyPlusLoginIOSPageBase.clickPrimaryButton();
//        disneyPlusLoginIOSPageBase.submitEmail(account.getEmail());
//        if(disneyPlusLoginIOSPageBase.isPrimaryButtonPresent()){
//            disneyPlusLoginIOSPageBase.clickPrimaryButton();
//        }
        pause(3);
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(account.getUserPass());
        Assert.assertTrue(disneyPlusHomeIOSPagesBase.isOpened(), "Home screen did not launch");
        disneyPlusHomeIOSPagesBase.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search screen not displayed");
        searchPage.clickMoviesTab();
        sa.assertTrue(searchPage.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");

        searchPage.getDisplayedTitles().get(0).click();

        detailsIOSPageBase.clickPlayButton().waitForVideoToStart();
        Assert.assertTrue(videoPlayerPage.isOpened(), "Video Player did not launch");

        pause(10);
        videoPlayerPage.clickPauseButton();
        pause(10);
        videoPlayerPage.clickPlayButton();
        pause(10);
        int remainingTimeInPauseMode = videoPlayerPage.getRemainingTime();
        pause(10);
        videoPlayerPage.tapForwardButton(6);
        int remainingTimeAfterFwdTapInPauseMode = videoPlayerPage.getRemainingTime();
        sa.assertTrue( remainingTimeInPauseMode > remainingTimeAfterFwdTapInPauseMode,
                "Remaining time in pause mode before fwd tap " + remainingTimeInPauseMode +
                        " is not greater than remaining time after fwd tap " + remainingTimeAfterFwdTapInPauseMode);
        LOGGER.info("Remaining time in pause mode fwd tap " + remainingTimeInPauseMode +
                "remaining time after fwd tap " + remainingTimeAfterFwdTapInPauseMode);


        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity", "started" );
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","paused");
        item2.addRequirement("exact","cause", "user" );
        checkList.add(item2);
        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact","playbackActivity","resumed");
        checkList.add(item3);
        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item4.addRequirement("exists", "activitySessionId");
        item4.addRequirement("exists", "audioBitrate");
        item4.addRequirement("exists", "cdnName");
        item4.addRequirement("exists", "contentKeys");
        item4.addRequirement("exists", "cpSessionId");
        item4.addRequirement("exists", "cpVideoIndex");
        item4.addRequirement("exists", "experiments");
        item4.addRequirement("exists", "experimentKeys");
        item4.addRequirement("exists", "maxAllowedVideoBitrate");
        item4.addRequirement("exists", "networkType");
        item4.addRequirement("exists", "pageViewId");
        item4.addRequirement("exists", "periodType");
        item4.addRequirement("exists", "playbackActivity");
        item4.addRequirement("exists", "playbackSessionId");
        item4.addRequirement("exists", "playheadPosition");
        item4.addRequirement("exists", "productType");
        item4.addRequirement("exists", "streamUrl");
        item4.addRequirement("exists", "videoBitrate");
        checkList.add(item4);

//        checkAssertions(sa);
//        sa.assertAll();
        checkAssertions(sa, account.getAccountId(), checkList);
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"QAA-10324"})
    @Test(description = "Test Pause/Resume QoE even")
    @Maintainer("isong1")
    public void testQoeEPauseAndResume(ITestContext context) {
//       xaqa-1094;
        initialSetup();
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();
        JSONArray checkList_resumed = new JSONArray();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPagesBase = new DisneyPlusHomeIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = new DisneyPlusSearchIOSPageBase(getDriver());
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = new DisneyPlusDetailsIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeIOSPAge = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        handleAlert();

        DisneyAccount account = disneyAccount.get();
        addHoraValidationSku(account);
//        disneyAccount.set();
        Assert.assertTrue(disneyPlusWelcomeIOSPAge.isOpened(), "Welcome screen did not launch");

        UniversalUtils.captureAndUpload(getCastedDriver());
        disneyPlusWelcomeIOSPAge.clickLogInButton();
        disneyPlusLoginIOSPageBase.fillOutEmailField(account.getEmail());
        disneyPlusLoginIOSPageBase.clickPrimaryButton();
//        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
//        if(disneyPlusLoginIOSPageBase.isPrimaryButtonPresent()){
//            disneyPlusLoginIOSPageBase.clickPrimaryButton();
//        }
        pause(3);
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(account.getUserPass());
        Assert.assertTrue(disneyPlusHomeIOSPagesBase.isOpened(), "Home screen did not launch");
        disneyPlusHomeIOSPagesBase.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search screen not displayed");
        searchPage.clickMoviesTab();
        sa.assertTrue(searchPage.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");

        searchPage.getDisplayedTitles().get(0).click();

        detailsIOSPageBase.clickPlayButton().waitForVideoToStart();
        Assert.assertTrue(videoPlayerPage.isOpened(), "Video Player did not launch");

        pause(10);
        videoPlayerPage.clickPauseButton();
        pause(10);


        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity", "started" );
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","paused");
        item2.addRequirement("exact","cause", "user" );
        checkList.add(item2);
        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("notexact","playbackActivity","resumed");
        HoraValidator hv = new HoraValidator(account.getAccountId());
        checkList.add(item3);
        //Check Pause message exist and make sure resume message does not exists by getting qoe events before resuming the video
        hv.checkListForPQOE(sa, checkList);

        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item4.addRequirement("exact","playbackActivity","resumed");
        checkList_resumed.add(item4);
        videoPlayerPage.clickPlayButton();

        pause(10);

        checkAssertions(sa, account.getAccountId(), checkList_resumed);
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XAQA-938"})
    @Test(description = "Test StartupSequence QoE event - validated by Sdp in checkAssertions method")
    @Maintainer("isong1")
    public void testQoEHeartBeat(ITestContext context) {
//       XAQA-938;
        initialSetup();
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPagesBase = new DisneyPlusHomeIOSPageBase(getDriver());
        DisneyPlusSearchIOSPageBase searchPage = new DisneyPlusSearchIOSPageBase(getDriver());
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        DisneyPlusDetailsIOSPageBase detailsIOSPageBase = new DisneyPlusDetailsIOSPageBase(getDriver());
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = new DisneyPlusAudioSubtitleIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeIOSPAge = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        handleAlert();

        DisneyAccount account = disneyAccount.get();
        addHoraValidationSku(account);
        Assert.assertTrue(disneyPlusWelcomeIOSPAge.isOpened(), "Welcome screen did not launch");

        UniversalUtils.captureAndUpload(getCastedDriver());
        disneyPlusWelcomeIOSPAge.clickLogInButton();
        disneyPlusLoginIOSPageBase.fillOutEmailField(account.getEmail());
        disneyPlusLoginIOSPageBase.clickPrimaryButton();
//        disneyPlusLoginIOSPageBase.submitEmail(account.getEmail());
        //Safety for login process where it does not proceed to Passwordpage
//        if(disneyPlusLoginIOSPageBase.isPrimaryButtonPresent()){
//            disneyPlusLoginIOSPageBase.clickPrimaryButton();
//        }
        pause(3);
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(account.getUserPass());
        Assert.assertTrue(disneyPlusHomeIOSPagesBase.isOpened(), "Home screen did not launch");
        disneyPlusHomeIOSPagesBase.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search screen not displayed");
        searchPage.clickMoviesTab();
        sa.assertTrue(searchPage.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");

        searchPage.getDisplayedTitles().get(0).click();

        detailsIOSPageBase.clickPlayButton().waitForVideoToStart();
        Assert.assertTrue(videoPlayerPage.isOpened(), "Video Player did not launch");
        videoPlayerPage.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        subtitlePage.chooseSubtitlesLanguage("English [CC]");
        subtitlePage.tapCloseButton();

        videoPlayerPage.isOpened();
        videoPlayerPage.tapAudioSubTitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "Checkmark was not present for the selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("English [CC]"), "Selected subtitle language is not as expected");
        subtitlePage.tapCloseButton();

        pause(10);
        videoPlayerPage.clickPauseButton();
        pause(10);
        videoPlayerPage.clickPlayButton();
        pause(10);


        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity", "started" );
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item2.addRequirement("exact","sampleType", "user" );
        item2.addRequirement("exists", "playlistAudioChannels");
        item2.addRequirement("exists", "playlistAudioCodec");
        item2.addRequirement("exists", "playlistAudioLanguage");
        item2.addRequirement("exists", "playlistAudioName");
        item2.addRequirement("exact","playlistSubtitleLanguage","en");
        item2.addRequirement("exact","playlistSubtitleName","English [CC]");
        checkList.add(item2);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item3.addRequirement("exact","sampleType","state");
        item3.addRequirement("exists", "activitySessionId");
        item3.addRequirement("exists", "audioBitrate");
        item3.addRequirement("exists", "bufferSegmentDuration");
        item3.addRequirement("exists", "cdnName");
        item3.addRequirement("exists", "contentKeys");
        item3.addRequirement("exists", "cpSessionId");
        item3.addRequirement("exists", "cpVideoIndex");
        item3.addRequirement("exists", "experimentKeys");
        item3.addRequirement("exists", "experiments");
        item3.addRequirement("exists", "maxAllowedVideoBitrate");
        item3.addRequirement("exists", "mediaBytesDownloaded");
        item3.addRequirement("exists", "mediaDownloadTotalCount");
        item3.addRequirement("exists", "mediaDownloadTotalTime");
        item3.addRequirement("exists", "mediaTitle");
        item3.addRequirement("exists", "networkType");
        item3.addRequirement("exists", "pageViewId");
        item3.addRequirement("exists", "partner");
        item3.addRequirement("exists", "periodType");
        item3.addRequirement("exists", "playbackDuration");
        item3.addRequirement("exists", "playbackSessionId");
        item3.addRequirement("exists", "playbackState");
        item3.addRequirement("exists", "playheadPosition");
        item3.addRequirement("exists", "productType");
        item3.addRequirement("exists", "subscriptionId");
        item3.addRequirement("exists", "totalVst");
        item3.addRequirement("exists", "videoBitrate");
        item3.addRequirement("exists", "videoStartTimestamp");
        checkList.add(item3);
        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item4.addRequirement("exact","sampleType","periodic");
        item4.addRequirement("exists", "activitySessionId");
        item4.addRequirement("exists", "audioBitrate");
        item4.addRequirement("exists", "bufferSegmentDuration");
        item4.addRequirement("exists", "cdnName");
        item4.addRequirement("exists", "contentKeys");
        item4.addRequirement("exists", "cpSessionId");
        item4.addRequirement("exists", "cpVideoIndex");
        item4.addRequirement("exists", "experimentKeys");
        item4.addRequirement("exists", "experiments");
        item4.addRequirement("exists", "maxAllowedVideoBitrate");
        item4.addRequirement("exists", "mediaBytesDownloaded");
        item4.addRequirement("exists", "mediaDownloadTotalCount");
        item4.addRequirement("exists", "mediaDownloadTotalTime");
        item4.addRequirement("exists", "mediaTitle");
        item4.addRequirement("exists", "networkType");
        item4.addRequirement("exists", "pageViewId");
        item4.addRequirement("exists", "partner");
        item4.addRequirement("exists", "periodType");
        item4.addRequirement("exists", "playbackDuration");
        item4.addRequirement("exists", "playbackSessionId");
        item4.addRequirement("exists", "playbackState");
        item4.addRequirement("exists", "playheadPosition");
        item4.addRequirement("exists", "productType");
        item4.addRequirement("exists", "subscriptionId");
        item4.addRequirement("exists", "totalVst");
        item4.addRequirement("exists", "videoBitrate");
        item4.addRequirement("exists", "videoStartTimestamp");
        checkList.add(item4);

        checkAssertions(sa, account.getAccountId(), checkList);
    }
}
