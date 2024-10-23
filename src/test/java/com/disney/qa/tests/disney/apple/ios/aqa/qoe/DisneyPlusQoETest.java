package com.disney.qa.tests.disney.apple.ios.aqa.qoe;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.hora.validationservices.EventChecklist;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusQoETest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String MOVIES = "Movies";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XAQA-1578" })
    @Test(description = "Test StartupSequence QoE event - validated by Sdp in checkAssertions method", groups = {TestGroup.PRE_CONFIGURATION, US})
    @Maintainer("isong1")
    public void testQoEStartupSequence() {
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccount account = loginAndStartPlayback();

        pause(10);
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

        checkAssertions(sa, account.getAccountId(), checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XAQA-1577" })
    @Test(description = "Smoke Test Playback QoE events", groups = {TestGroup.PRE_CONFIGURATION, US})
    @Maintainer("isong1")
    public void testQoEPlayback() {
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccount account = loginAndStartPlayback();

        pause(10);
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        videoPlayerPage.clickPauseButton();
        pause(10);
        videoPlayerPage.clickPlayButton();
        pause(10);
        int remainingTimeInPauseMode = videoPlayerPage.getRemainingTime();
        pause(10);
        videoPlayerPage.tapForwardButton(6);
        int remainingTimeAfterFwdTapInPauseMode = videoPlayerPage.getRemainingTime();
        sa.assertTrue(remainingTimeInPauseMode > remainingTimeAfterFwdTapInPauseMode,
                "Remaining time in pause mode before fwd tap " + remainingTimeInPauseMode +
                        " is not greater than remaining time after fwd tap " + remainingTimeAfterFwdTapInPauseMode);
        LOGGER.info("Remaining time in pause mode fwd tap " + remainingTimeInPauseMode +
                "remaining time after fwd tap " + remainingTimeAfterFwdTapInPauseMode);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact", "playbackActivity", "started");
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact", "playbackActivity", "paused");
        item2.addRequirement("exact", "cause", "user");
        checkList.add(item2);
        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact", "playbackActivity", "resumed");
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

        checkAssertions(sa, account.getAccountId(), checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XAQA-1094" })
    @Test(description = "Test Pause/Resume QoE event", groups = {TestGroup.PRE_CONFIGURATION, US})
    @Maintainer("isong1")
    public void testQoEPauseAndResume() {
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();
        JSONArray checkList_resumed = new JSONArray();

        DisneyAccount account = loginAndStartPlayback();

        pause(10);
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        videoPlayerPage.clickPauseButton();
        pause(10);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact", "playbackActivity", "started");
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact", "playbackActivity", "paused");
        item2.addRequirement("exact", "cause", "user");
        checkList.add(item2);
        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("notexact", "playbackActivity", "resumed");
        checkList.add(item3);
        //Check Pause message exist and make sure resume message does not exists by getting qoe events before resuming the video
        checkAssertions(sa, account.getAccountId(), checkList);

        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item4.addRequirement("exact", "playbackActivity", "resumed");
        checkList_resumed.add(item4);
        videoPlayerPage.clickPlayButton();

        pause(10);

        checkAssertions(sa, account.getAccountId(), checkList_resumed);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XAQA-938" })
    @Test(description = "Verify Hearbeat QoE Event", groups = {TestGroup.PRE_CONFIGURATION, US})
    @Maintainer("isong1")
    public void testQoEHeartBeat() {
        SoftAssert sa = new SoftAssert();
        JSONArray checkList = new JSONArray();
        //Log in and start a movie
        DisneyAccount account = loginAndStartPlayback();
        //Enable subtitle
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        videoPlayerPage.tapAudioSubtitleMenu();
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = new DisneyPlusAudioSubtitleIOSPageBase(getDriver());
        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "checkmark was not present for selected lang");
        subtitlePage.chooseSubtitlesLanguage("English [CC]");
        subtitlePage.tapCloseButton();
        //Verify that subtitle is enabled
        videoPlayerPage.isOpened();
        videoPlayerPage.tapAudioSubtitleMenu();
        sa.assertTrue(subtitlePage.verifySelectedAudioIs("English"), "Checkmark was not present for the selected lang");
        sa.assertTrue(subtitlePage.verifySelectedSubtitleLangIs("English [CC]"), "Selected subtitle language is not as expected");
        subtitlePage.tapCloseButton();

        pause(10);
        videoPlayerPage.clickPauseButton();
        pause(10);
        videoPlayerPage.clickPlayButton();
        pause(10);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact", "playbackActivity", "started");
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item2.addRequirement("exact", "sampleType", "user");
        item2.addRequirement("exists", "playlistAudioChannels");
        item2.addRequirement("exists", "playlistAudioCodec");
        item2.addRequirement("exists", "playlistAudioLanguage");
        item2.addRequirement("exists", "playlistAudioName");
        item2.addRequirement("exact", "playlistSubtitleLanguage", "en");
        item2.addRequirement("exact", "playlistSubtitleName", "English [CC]");
        checkList.add(item2);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item3.addRequirement("exact", "sampleType", "state");
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
        item4.addRequirement("exact", "sampleType", "periodic");
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

    private DisneyAccount loginAndStartPlayback() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusSearchIOSPageBase searchPage = new DisneyPlusSearchIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeIOSPage = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyAccount account = getAccount();
        addHoraValidationSku(account);
        Assert.assertTrue(disneyPlusWelcomeIOSPage.isOpened(), "Welcome screen did not launch");
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        //Login Steps
        disneyPlusWelcomeIOSPage.clickLogInButton();
        new DisneyPlusLoginIOSPageBase(getDriver()).fillOutEmailField(account.getEmail());
        hideKeyboard();
        new DisneyPlusPasswordIOSPageBase(getDriver()).submitPasswordForLogin(account.getUserPass());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPagesBase = new DisneyPlusHomeIOSPageBase(getDriver());
        Assert.assertTrue(disneyPlusHomeIOSPagesBase.isOpened(), "Home screen did not launch");
        //Navigate to Movie Page
        disneyPlusHomeIOSPagesBase.clickSearchIcon();
        sa.assertTrue(searchPage.isOpened(), "Search screen not displayed");
        searchPage.clickMoviesTab();
        sa.assertTrue(searchPage.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");
        //Start first movie from the movie page
        searchPage.getDisplayedTitles().get(0).click();
        new DisneyPlusDetailsIOSPageBase(getDriver()).clickPlayButton().waitForVideoToStart();
        Assert.assertTrue(new DisneyPlusVideoPlayerIOSPageBase(getDriver()).isOpened(), "Video Player did not launch");
        return account;
    }
}
