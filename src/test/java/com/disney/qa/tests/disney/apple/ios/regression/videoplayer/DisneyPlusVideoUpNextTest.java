package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.config.*;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusUpNextIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.PlayerControl;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.*;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.*;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusVideoUpNextTest extends DisneyBaseTest {

    //Test constants
    protected static final String SHORT_SERIES = "Bluey";
    private static final String IRON_MAN = "Iron Man";
    private static final double PLAYER_PERCENTAGE_FOR_UP_NEXT = 90;
    private static final double PLAYER_PERCENTAGE_FOR_AUTO_PLAY = 95;
    private static final double PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT = 50;
    private static final String REGEX_UPNEXT_SERIES_TITLE = "Season %s Episode %s %s";
    private static final double PLAYER_PERCENTAGE_FOR_UP_NEXT_SHORT_SERIES = 80;

    @DataProvider(name = "autoplay-state")
    public Object[][] autoplayState(){
        return new Object[][] {{"ON"}, {"OFF"}};
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67648"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyPlayIconOnUpNextUI() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        //Login
        setAppToHomeScreen(getAccount());
        //Turn off autoplay
        toggleAutoPlay("OFF");
        //Search and forward the content
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        String nextEpisodesTitle = disneyPlusUpNextIOSPageBase.getNextEpisodeInfo();
        disneyPlusUpNextIOSPageBase.tapPlayIconOnUpNext();
        //Verify that the next episode has started playing
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.doesTitleExists(nextEpisodesTitle),"Next episode didn't play");
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isElementPresent(PlayerControl.PAUSE),"Pause button is not visible on player view");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67652"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyUpNextSeeAllEpisodes() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        disneyPlusUpNextIOSPageBase.tapSeeAllEpisodesButton();
        sa.assertTrue(disneyPlusDetailsIOSPageBase.isOpened(),"Tapping on 'See all episodes' didn't take to details page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67654"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyAutoPlayOnPlayerView() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Turn ON autoplay
        toggleAutoPlay("ON");
        //Bring up upnext UI
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT_SHORT_SERIES);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        String nextEpisodesTitle = disneyPlusUpNextIOSPageBase.getNextEpisodeInfo();
        //Wait for upnext UI to disappear
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToDisappear();
        //Verify that the next episode has started playing
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.getSubTitleLabel().contains(nextEpisodesTitle),
                "Next episode didn't play");
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isElementPresent(PlayerControl.PAUSE),
                "Pause button is not visible on player view");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67672"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUpNextLogicForExtraContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Search for a series having 'Extras'
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(IRON_MAN);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.clickExtrasTab();

        //Initiate playback for "Extra" content from details page
        detailsPage.tapOnFirstContentTitle();
        disneyPlusVideoPlayerIOSPageBase.isOpened();

        //Verify that up next UI doesn't appear on player view
        disneyPlusVideoPlayerIOSPageBase.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT);
        sa.assertFalse(disneyPlusUpNextIOSPageBase.isUpNextViewPresent(),"Up Next UI is shown for extra content");

        //verify that details page is opened once playback is complete.
        sa.assertTrue(detailsPage.isDetailPageOpened(disneyPlusVideoPlayerIOSPageBase.getRemainingTime()),
                "Control didn't return to the Detail page after 'Extra' content finished playing");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67676","XMOBQA-72610"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, US}, dataProvider = "autoplay" +
            "-state", enabled = false)
    public void verifyUpNextBehaviorWhenAppIsBackgrounded(String autoplayState) {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Turn ON autoplay
        toggleAutoPlay(autoplayState);
        //Bring up upNext UI
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        sa.assertTrue(disneyPlusUpNextIOSPageBase.verifyUpNextUI(), "Up Next UI was not displayed");
        //This will lock the device for 5 seconds then unlock it
        lockDevice(Duration.ofSeconds(5));
        //After backgrounding the app, video should be paused, and we should see upNext UI
        sa.assertTrue(disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear(), "Up Next UI was not displayed");
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.verifyVideoPaused(),"Play button is not visible on player view, " +
                "video not paused");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67656"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAutoplayDoesNotAutoplayWhenDisabled() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Turn ON autoplay
        toggleAutoPlay("OFF");
        //Forward the content
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_AUTO_PLAY);
        int remainingTime = disneyPlusVideoPlayerIOSPageBase.getRemainingTime();
        pause(remainingTime);
        sa.assertTrue(disneyPlusUpNextIOSPageBase.verifyUpNextUI(), "Up Next UI was not displayed");
        //TODO:https://jira.disneystreaming.com/browse/IOS-6617
        //uncomment below line when the bug is resolved
        /*sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isElementPresent(PlayerControl.PAUSE),"Pause button is not visible on player view, " +
                "video not paused when autoplay is OFF");*/
        sa.assertTrue(disneyPlusUpNextIOSPageBase.getNextEpisodeInfo().equalsIgnoreCase("S1:E2 Hospital"), "Next season title is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72685"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyUpNextEpisodeIsDownloadedWifiOn() {
        int first = 1;
        int second = 2;
        int pollingInSeconds = 5;
        int timeoutInSeconds = 90;
        String one = "1";
        String two = "2";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusUpNextIOSPageBase upNextPage = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));
        detailsPage.isOpened();
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeUp(2500);
        }
        //Download episode
        swipePageTillElementPresent(detailsPage.getEpisodeToDownload(one, two), 2,
                detailsPage.getContentDetailsPage(), Direction.UP, 1200);
        detailsPage.getEpisodeToDownload(one, two).click();
        String secondEpisodeTitle = detailsPage.getEpisodeTitleLabel(second)
                .getText().split("\\.")[1];
        detailsPage.waitForOneEpisodeDownloadToComplete(timeoutInSeconds, pollingInSeconds);

        //Play Bluey season 1
        detailsPage.getEpisodeTitleLabel(first).click();
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.getSkipIntroButton().click();
        videoPlayerPage.clickPauseButton();
        videoPlayerPage.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_AUTO_PLAY);

        //Wait for upnext UI to disappear
        upNextPage.waitForUpNextUIToDisappear();
        //Verify that the next episode has started playing
        videoPlayerPage.waitForVideoToStart();
        String secondSubtitle = videoPlayerPage.getSubTitleLabel();
        Assert.assertTrue(secondSubtitle.contains(secondEpisodeTitle), "Second episode for series didn't play");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67684"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUpNextContentRatingOverlay() {
        DisneyPlusUpNextIOSPageBase upNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT_SHORT_SERIES);
        upNextIOSPageBase.waitForUpNextUIToAppear();
        upNextIOSPageBase.tapPlayIconOnUpNext();
        Assert.assertTrue(videoPlayerIOSPageBase.isContentRatingOverlayPresent(), "Content Rating overlay not displayed");
        Assert.assertTrue(videoPlayerIOSPageBase.waitForContentRatingOverlayToDisappear(), "Content rating overlay " +
                "didn't dismiss");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67646"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.UP_NEXT, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUpNextAutoPlayUIWhenEnabled() {
        String seriesContentTitle = "Loki";
        DisneyPlusUpNextIOSPageBase upNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        ExploreContent seriesApiContent = getSeriesApi(R.TESTDATA.get("disney_prod_loki_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String episodeTitle = "";
        String seasonNumber = "";
        try {
            episodeTitle = seriesApiContent.getSeasons().get(0).getItems().get(1).getVisuals().getEpisodeTitle();
            seasonNumber = seriesApiContent.getSeasons().get(0).getItems().get(1).getVisuals().getSeasonNumber();
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }

        if(episodeTitle == null || seasonNumber == null){
            throw new SkipException("Skipping test, failed to get episodeTitles or seasonNumber from the api");
        }

        String upNextTitlePlaceHolder = String.format(REGEX_UPNEXT_SERIES_TITLE, seasonNumber, "2", episodeTitle);

        //Enable autoplay
        toggleAutoPlay("ON");
        initiatePlaybackAndScrubOnPlayer(seriesContentTitle, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        upNextIOSPageBase.waitForUpNextUIToAppear();
        sa.assertTrue(upNextIOSPageBase.verifyUpNextUI(), "Up Next UI was not displayed");
        sa.assertTrue(upNextIOSPageBase.getStaticTextByLabel(upNextTitlePlaceHolder).isPresent(),
                "Up Next meta data title not displayed");
        sa.assertTrue(upNextIOSPageBase.isNextEpisodeHeaderPresent(), "Next Episode Header is not displayed");
        sa.assertTrue(videoPlayer.getBackButton().isPresent(), "Back button is not found");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72263"})
    @Test(groups = {TestGroup.SERIES, TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeriesPlaybackNextEpisode() {
        String apiSecondEpisodeTitle;
        int firstSeasonIndex = 0;
        int secondEpisodeIndex = 1;

        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        ExploreContent seriesApiContent = getSeriesApi(
                R.TESTDATA.get("disney_prod_loki_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        try {
            apiSecondEpisodeTitle = seriesApiContent.getSeasons()
                    .get(firstSeasonIndex)
                    .getItems()
                    .get(secondEpisodeIndex)
                    .getVisuals()
                    .getEpisodeTitle();
        } catch (Exception e) {
            throw new SkipException("Skipping test, next episode title is not found" + e.getMessage());
        }
        setAppToHomeScreen(getAccount());

        //Turn OFF autoplay
        toggleAutoPlay("OFF");

        // Deeplink series episode
        launchDeeplink(R.TESTDATA.get("disney_prod_series_loki_first_episode_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        videoPlayer.waitForVideoToStart();

        //Tap on Next episode and verify that the next episode has started playing
        videoPlayer.getElementFor(PlayerControl.NEXT_EPISODE).click();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getSubTitleLabel().contains(apiSecondEpisodeTitle),
                "Next episode didn't play");
    }

    private void initiatePlaybackAndScrubOnPlayer(String content, double percentage) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), "Video Player did not open");
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickPauseButton();
        videoPlayer.scrubToPlaybackPercentage(percentage);
        videoPlayer.clickPlayButton();
    }

    private void toggleAutoPlay(String value) {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.toggleAutoplay(getAccount().getFirstName(), value.toUpperCase());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
    }
}
