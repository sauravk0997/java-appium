package com.disney.qa.tests.disney.web.appex.playback;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.dgi.validationservices.hora.EventChecklist;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.DisneyMediaPlayerCommands;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.appex.playback.DisneyPlusPlaybackPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.DataMapKeyConstant;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.proxy.MitmProxy;
import com.proxy.MitmProxyConfiguration;
import com.proxy.MitmProxyPool;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.simple.JSONArray;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;

public class DisneyPlusPlaybackQOETest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    public void setupTest() throws InterruptedException {
        disneyAccount.set(new DisneyAccount());
        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        MitmProxyConfiguration configuration = MitmProxyConfiguration.builder()
                .listenPort(getProxyPort())
                .headers(disneyPlusBasePage.getHeaders(locale)).build();
        MitmProxy.startProxy(configuration);
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
        MitmProxyPool.tearDownProxy();
    }

    @Test(description = "Verify StartupActivity Events", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyPQOEStartup() {
        SoftAssert softAssert = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail() , account.getUserPass());
        appExUtil.setPlayerQaMode();


        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert, PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(70);
        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item1.addRequirement("exact","startupActivity","requested");
        checkList.add(item1);
        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item2.addRequirement("exact","startupActivity","fetched");
        checkList.add(item2);
        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item3.addRequirement("exact","startupActivity","masterFetched");
        checkList.add(item3);
        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item4.addRequirement("exact","startupActivity","variantFetched");
        checkList.add(item4);
        EventChecklist item5 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item5.addRequirement("exact","startupActivity","drmCertificateFetched");
        checkList.add(item5);
        EventChecklist item6 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item6.addRequirement("exact","startupActivity","initialized");
        checkList.add(item6);
        EventChecklist item7 = new EventChecklist("urn:dss:event:client:playback:startup:v1");
        item7.addRequirement("exact","startupActivity","ready");
        checkList.add(item7);

        EventChecklist item8 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item8.addRequirement("exact","sampleType","periodic");
        checkList.add(item8);

        EventChecklist item9 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item9.addRequirement("exact","playbackActivity","started");
        checkList.add(item9);

        checkAssertions(softAssert, checkList);
    }

    @Test(description = "Verify PQOE Pause and Resume Events", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyPQOEPauseResume() {
        SoftAssert softAssert = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail() , account.getUserPass());
        appExUtil.setPlayerQaMode();


        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert, PageUrl.SEARCH);

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(40);
        playbackPage
                .hoverVideoPlayer()
                .clickVideoPlayerPauseBtn()
                .verifyMediaPlayerState(DisneyMediaPlayerCommands.IS_PAUSED,true,softAssert)
                .waitForSeconds(5);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","paused");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item2.addRequirement("exact","sampleType","state");
        checkList.add(item2);

        playbackPage
                .hoverVideoPlayer()
                .clickVideoPlayerPlayBtn()
                .verifyMediaPlayerState(DisneyMediaPlayerCommands.IS_PLAYING,true,softAssert)
                .waitForSeconds(5);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact","playbackActivity","resumed");
        item3.addRequirement("exact","cause", "user" );
        checkList.add(item3);

        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item4.addRequirement("exact","sampleType","periodic");
        checkList.add(item4);

        checkAssertions(softAssert, checkList);
    }
    @Test(description = "Verify Seek Events", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyPQOESeek() {
        SoftAssert softAssert = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail() , account.getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(70);

        playbackPage.mediaPlayerSeek(15);


        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","seekStarted");
        item1.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "forward")));
        item1.addRequirement("contains", "cause", new ArrayList<>(Arrays.asList("seek", "scrub")));
        checkList.add(item1);

        checkAssertions(softAssert, checkList);
    }

    @Test(description = "Verify User Ended Playback", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyManualExit() {
        SoftAssert softAssert = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail() , account.getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(70);
        playbackPage
                .hoverVideoPlayer()
                .clickVideoPlayerBackArrow()
                .waitForSeconds(10);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","ended");
        item1.addRequirement("exact","cause", "user" );
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item2.addRequirement("exact","sampleType","state");
        checkList.add(item2);

        softAssert
                .assertFalse(playbackPage.isVideoPlayerIsVisible());

        checkAssertions(softAssert, checkList);
    }

    @Test(description = "Verify Audio Language Change PQOE Event", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyAudioLanguageChange() {
        SoftAssert softAssert = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail() , account.getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(70);;
        playbackPage
                .clickVideoPlayer()
                .hoverHudsonVideoPlayer()
                .clickAudioSubtitlesButton()
                .clickAudioOption(WebConstant.DEUTSCH);
        analyticPause();
        playbackPage
                .clickCloseAudioSubtitleMenu()
                .clickVideoPlayerPlayBtn()
                .waitForSeconds(15);//ensure the video will continue to play for the next step

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item1.addRequirement("exact","sampleType","user");
        item1.addRequirement("exact","playlistAudioLanguage","de");
        item1.addRequirement("exact","playlistAudioName","Deutsch");
        checkList.add(item1);

        checkAssertions(softAssert, checkList);
    }
    @Test(description = "Verify Subtitle Language Change PQOE Event", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifySubtitleLanguageChange() {
        SoftAssert softAssert = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail() , account.getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(70);;
        playbackPage
                .clickVideoPlayer()
                .hoverHudsonVideoPlayer()
                .clickAudioSubtitlesButton()
                .clickSubtitleOption(WebConstant.DEUTSCH);
        analyticPause();
        playbackPage
                .clickCloseAudioSubtitleMenu()
                .clickVideoPlayerPlayBtn()
                .waitForSeconds(15); //ensure the video will continue to play for the next step

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:heartbeat:v1");
        item1.addRequirement("exact","sampleType","user");
        item1.addRequirement("exact","playlistSubtitleLanguage","de");
        item1.addRequirement("exact","playlistSubtitleName","Deutsch");
        checkList.add(item1);

        checkAssertions(softAssert, checkList);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18819","XWEBQAS-18823","XWEBQAS-18825"})
    @Test(description = "Verify Seek Forward/Backward 10 Seconds Buttons", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifySeekButtons() {
        SoftAssert softAssert = new SoftAssert();
        JSONArray checkList = new JSONArray();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail() , account.getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(70);
        playbackPage
                .hoverVideoPlayer()
                .getCurrentTimeValue(DataMapKeyConstant.PROGRESS_PREVIOUS)
                .clickSeekForwardButton()
                .waitForSeconds(7);
        playbackPage
                .getCurrentTimeValue(DataMapKeyConstant.PROGRESS_CURRENT);
        appExUtil
                .verifyValueIncreased
                        (DataMapKeyConstant.PROGRESS_PREVIOUS,DataMapKeyConstant.PROGRESS_CURRENT,
                                "value did not increase")
                .waitForSeconds(7);

        EventChecklist item1 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item1.addRequirement("exact","playbackActivity","seekStarted");
        item1.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "forward")));
        item1.addRequirement("exact","cause","skip");
        checkList.add(item1);

        EventChecklist item2 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item2.addRequirement("exact","playbackActivity","seekEnded");
        item2.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "forward")));
        item2.addRequirement("exact","cause","skip");
        checkList.add(item2);

        playbackPage
                .getCurrentTimeValue(DataMapKeyConstant.PROGRESS_NEW_CURRENT)
                .hoverVideoPlayer()
                .clickSeekBackButton()
                .waitForSeconds(5);
        playbackPage
                .getCurrentTimeValue(DataMapKeyConstant.PROGRESS_END);
        appExUtil
                .verifyValueDecreased
                        (DataMapKeyConstant.PROGRESS_NEW_CURRENT,DataMapKeyConstant.PROGRESS_END,
                                "value did not decrease")
                .waitForSeconds(5);

        EventChecklist item3 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item3.addRequirement("exact","playbackActivity","seekStarted");
        item3.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "backward")));
        item3.addRequirement("exact","cause","skip");
        checkList.add(item3);

        EventChecklist item4 = new EventChecklist("urn:dss:event:client:playback:event:v1");
        item4.addRequirement("exact","playbackActivity","seekEnded");
        item4.addRequirement("contains", "playerSeekDirection", new ArrayList<>(Arrays.asList("same", "backward")));
        item4.addRequirement("exact","cause","skip");
        checkList.add(item4);


        checkAssertions(softAssert, checkList);
    }
}
