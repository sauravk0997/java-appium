package com.disney.qa.tests.disney.web.appex.playback;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusVideoPlayerPage;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.playback.DisneyPlusPlaybackPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.HARUtils;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusPlaybackTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }


    @Test(description = "Verify Series Playback with next episode", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX","JP"})
    public void verifySeriesPlayNextEpisode() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1513"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText( softAssert, PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);

        int duration =  playbackPage.getDurationTime().intValue();

        softAssert
                .assertTrue(playbackPage.mediaSeekToEnd(duration), "Video was not scrubbed to the end");
        softAssert
                .assertTrue(playbackPage.getPlayNextButton().isElementPresent(),
                "Expected play-next-episode button to be present");

        //Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "Verify Movie Playback with next episode", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX, TestGroup.DISNEY_SMOKE,"US", "MX", "JP"})
    public void verifyMoviePlayNextEpisode() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1514"));
        skipTestByEnv(QA);
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        DisneyPlusVideoPlayerPage videoPlayerPage = new DisneyPlusVideoPlayerPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert, PageUrl.SEARCH);
        homepageSearchPage
                .enterMovieOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);

        int duration =  playbackPage.getDurationTime().intValue();

        String firstMovieContentKey = videoPlayerPage.getUrlContentKey();

        softAssert
                .assertTrue(playbackPage.mediaSeekToEnd(duration), "Video was not scrubbed to the end");

        waitForSeconds(10); //must pause to achieve true ending of video

        softAssert
                .assertTrue(playbackPage.getPlayNextButton().isElementPresent(),
                "Expected play-next-episode button to be present");

        playbackPage.
                clickPlayNextEpisode();
        waitForSeconds(5);//ensure video continues to play to get content key

        String playNextMovieContentKey = videoPlayerPage.getUrlContentKey();

        softAssert
                .assertNotEquals(firstMovieContentKey, playNextMovieContentKey,
                String.format("First movie content key: '%s' Play-next movie content key: '%s'", firstMovieContentKey, playNextMovieContentKey));

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions( softAssert);
    }
    @Test(description = "Verify Deeplink Interaction Event", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX,"US", "MX", "JP"})
    public void verifyDeeplinkInteraction() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1707"));
        SoftAssert softAssert = new SoftAssert();
        String videoDeepLink = R.TESTDATA.get("disney_prod_video_player_movie_deeplink");

        videoDeepLink = videoDeepLink.replace("www", "stage-web");

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        getDriver().get(videoDeepLink);
        waitForSeconds(15);
        if (horaEnabled()) {
            softAssert.assertTrue(HARUtils.harContainsValue(proxy.get(), HARUtils.RequestDataType.POST_DATA, "\"interactionType\":\"deeplink\""));
        }
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-30774"})
    @Test(description = "Verify Series Playback", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX,"US", "MX","JP"})
    public void verifySeriesPlaybackPage() throws InterruptedException {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1708"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert, PageUrl.SEARCH);
        homepageSearchPage
                .enterSeriesOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);

        int duration =  playbackPage.getDurationTime().intValue();

        playbackPage
                .pauseMediaPlayer()
                .waitForSeconds(5);
        playbackPage
                .playMediaPlayer()
                .waitForSeconds(5);
        softAssert
                .assertTrue(playbackPage.mediaSeekToEnd(duration), "Video was not scrubbed to the end");
        softAssert
                .assertTrue(playbackPage.getPlayNextButton().isElementPresent(),
                "Expected play-next-episode button to be present");

        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-30774"})
    @Test(description = "Verify Movie Playback", groups = {TestGroup.DISNEY_APPEX,TestGroup.PLAYER_APPEX,"US", "MX", "JP"})
    public void verifyMoviePlaybackPage() throws InterruptedException {
        skipTestByEnv(QA);
        
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1706"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusPlaybackPage playbackPage = new DisneyPlusPlaybackPage(getDriver());
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        appExUtil.setPlayerQaMode();

        homepageSearchPage
                .clickOnSearchBar()
                .verifyUrlText(softAssert,PageUrl.SEARCH);
        homepageSearchPage
                .enterMovieOnSearchBar();
        detailsPage
                .clickDetailsPagePlayBtn()
                .waitForSeconds(15);

        int duration =  playbackPage.getDurationTime().intValue();

        playbackPage
                .pauseMediaPlayer()
                .waitForSeconds(5);
        playbackPage
                .playMediaPlayer()
                .waitForSeconds(5);
        softAssert
                .assertTrue(playbackPage.mediaSeekToEnd(duration), "Video was not scrubbed to the end");
        waitForSeconds(10);
        softAssert
                .assertTrue(playbackPage.getPlayNextButton().isElementPresent(),
                "Expected play-next-movie button to be present");
        playbackPage
                .clickPlayNextEpisode()
                .waitForSeconds(5);

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

}
