package com.disney.qa.tests.disney.web.appex.playback;

import com.disney.qa.api.account.DisneyAccountApi;
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
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusPlaybackOptionsTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18837","XWEBQAS-19738","XWEBQAS-18803","XWEBQAS-18801"})
    @Test(description = "Verify Pause and Play Playback", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyPausePlay() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.navigateToMain();
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);
        playbackPage
                .hoverVideoPlayer()
                .clickVideoPlayerPauseBtn()
                .verifyMediaPlayerState(DisneyMediaPlayerCommands.IS_PAUSED,true,softAssert)
                //TODO: add qoe verification here once qoe events are implemented on web
                .waitForSeconds(5);
        playbackPage
                .hoverVideoPlayer()
                .clickVideoPlayerPlayBtn()
                .verifyMediaPlayerState(DisneyMediaPlayerCommands.IS_PLAYING,true,softAssert)
                //TODO: add qoe verification here once qoe events are implemented on web
                .waitForSeconds(5);

        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-24836","XWEBQAS-24838","XWEBQAS-24830"})
    @Test(description = "Verify Subtitle Language", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifySubtitleLanguageChange() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.navigateToMain();
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage.
                clickDetailsPagePlayBtn()
                .waitForSeconds(15);
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

        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-24832","XWEBQAS-24834","XWEBQAS-24830"})
    @Test(description = "Verify Audio Language Change", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyAudioLanguageChange() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.navigateToMain();
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);;
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

        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18869"})
    @Test(description = "Verify FullScreen Toggle", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyFullScreen() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.navigateToMain();
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);
        playbackPage
                .clickVideoPlayer()
                .clickFullScreenButton()
                .waitForSeconds(10); //pausing since entering/exiting full screen takes a few seconds
        softAssert
                .assertTrue(playbackPage.isFullScreenIsVisible());
        playbackPage
                .clickVideoPlayer()
                .clickExitFullScreenButton();
        softAssert
                .assertFalse(playbackPage.isFullScreenIsVisible());
        pause(5);

        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-17363","XWEBQAS-17361"})
    @Test(description = "Verify Video Player Back Button", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifyBackButton() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.navigateToMain();
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);
        playbackPage
                .hoverVideoPlayer()
                .clickVideoPlayerBackArrow()
                .waitForSeconds(10);
        //TODO: Add check that qoe event for playback ended here
        //TODO: Add check that user is no longer in video player here
        softAssert
                .assertFalse(playbackPage.isVideoPlayerIsVisible());

        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18819","XWEBQAS-18823","XWEBQAS-18825"})
    @Test(description = "Verify Seek Forward/Backward 10 Seconds Buttons", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifySeekButtons() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.navigateToMain();
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);
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
        //TODO: Add validation for qoe events for seek and buffering here once qoe is added
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
        //TODO: Add validation for qoe events for seek and buffering here once qoe is added

        checkAssertions(softAssert);
    }

}
