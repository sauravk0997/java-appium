package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.ONLY_MURDERS_IN_THE_BUILDING;
import static com.disney.qa.tests.disney.apple.ios.regression.videoplayer.DisneyPlusVideoUpNextTest.SHORT_SERIES;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.PlayerControl;
import static com.disney.qa.api.disney.DisneyEntityIds.MARVELS;

public class DisneyPlusVideoPlayerControlTest extends DisneyBaseTest {
    protected static final String THE_MARVELS = "The Marvels";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "'Details' page is not shown after closing the video player";
    private static final double SCRUB_PERCENTAGE_TEN = 10;

    @DataProvider(name = "contentType")
    public Object[][] contentType() {
        return new Object[][]{{DisneyPlusApplePageBase.contentType.MOVIE.toString(), "Ice Age"},
                {DisneyPlusApplePageBase.contentType.SERIES.toString(), SHORT_SERIES},
                {DisneyPlusApplePageBase.contentType.EXTRAS.toString(), "Thor: Love and Thunder"}};
    }

    @DataProvider(name = "userType")
    public Object[][] userType() {
        return new Object[][]{{"DISNEY_HULU_NO_ADS_ESPN_WEB"},
                {"DISNEY_VERIFIED_HULU_ESPN_BUNDLE"}};
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66515"})
    @Test(description = "Video Player > Title and Back Button to Close", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION}, dataProvider = "contentType")
    public void verifyTitleAndBackButtonToClose(Object[] content) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        //User taps the back button
        loginAndStartPlayback((String) content[1]);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        //User taps the title
        detailsPage.clickPlayOrContinue();
        videoPlayer.waitForVideoToStart();
        videoPlayer.tapTitleOnPlayer();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertAll();
        //Initiate playback from 'downloads'
        /*detailsPage.getEpisodeToDownload("1","1").click();
        detailsPage.waitForOneEpisodeDownloadToComplete(60, 3);
        homePage.clickDownloadsIcon();
        downloadsPage.tapDownloadedAssetFromListView(SHORT_SERIES);
        downloadsPage.tapDownloadedAsset(SHORT_SERIES);
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        sa.assertTrue(downloadsPage.isContentHeaderPresent(SHORT_SERIES), "'Downloads list view' page didn't open after closing the video player");

        //Initiate playback from 'continue watching' from Home
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        homePage.initiatePlaybackFromContinueWatching(SHORT_SERIES);
        detailsPage.clickContinueButton();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "'Details' page didn't open after closing the video player");*/
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66529"})
    @Test(description = " Video Player > Tap on screen to Rewind", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION})
    public void verifyRewindButtonControlOnPlayer() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SHORT_SERIES);

        videoPlayer.tapForwardButton(3);
        int remainingTimeInPauseMode = videoPlayer.clickPauseButton().getRemainingTime();
        int remainingTimeAfterRwdTapInPauseMode = videoPlayer.tapRewindButton(1).getRemainingTime();
        sa.assertTrue(remainingTimeInPauseMode < remainingTimeAfterRwdTapInPauseMode,
                "Remaining time in pause mode time after rwd tap " + remainingTimeAfterRwdTapInPauseMode +
                        " is not greater than remaining time before rwd tap " + remainingTimeInPauseMode);

        //TODO: IOS-3974 - blocks the below scenario, commenting it out till bug is resolved.
        /*int fastRewindRemainingTime = videoPlayer.tapPlayerScreen(PlayerControl.REWIND, 3).getRemainingTime();
        sa.assertTrue((fastRewindRemainingTime - remainingTimeAfterRwdTapInPauseMode) > 15,
                "Remaining time after, Fast Rewinding the content" + fastRewindRemainingTime +
                        " is not greater than the remaining time after, single rewind" + remainingTimeAfterRwdTapInPauseMode
                );*/
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66529"})
    @Test(description = " Video Player > Tap on screen to Forward", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyForwardButtonControlOnPlayer() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SHORT_SERIES);

        int remainingTimeInPauseMode = videoPlayer.clickPauseButton().getRemainingTime();
        int remainingTimeAfterFwdTapInPauseMode = videoPlayer.tapForwardButton(1).getRemainingTime();
        sa.assertTrue(remainingTimeInPauseMode > remainingTimeAfterFwdTapInPauseMode,
                "Remaining time in pause mode before fwd tap " + remainingTimeInPauseMode +
                        " is not greater than remaining time after fwd tap " + remainingTimeAfterFwdTapInPauseMode);
        //TODO: IOS-3974 - blocks the below scenario, commenting it out till bug is resolved.
        /*int fastForwardRemainingTime = videoPlayer.tapPlayerScreen(PlayerControl.FAST_FORWARD, 3).getRemainingTime();
        sa.assertTrue((remainingTimeAfterFwdTapInPauseMode - fastForwardRemainingTime) > 15,
                "Remaining time after single fwd tap " + remainingTimeAfterFwdTapInPauseMode +
                        " is not greater than remaining time after fast forwarding the content " + fastForwardRemainingTime);*/

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61169"})
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyCloseButtonForDeepLinkingContent() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_debug_video_player_episode_deeplink"), 10);
        detailsPage.clickOpenButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), "Playback didn't start from deep link");
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(10), "Details Page is not shown after closing the player");
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), "Home Page is not shown after closing the Details Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68456"})
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyCloseButtonForDeepLinkingContentMovie() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_debug_video_player_movie_deeplink"), 10);
        detailsPage.clickOpenButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), "Playback didn't start from deep link");
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(10), "Details Page is not shown after closing the player");
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), "Home Page is not shown after closing the Details Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72539"})
    @Test(description = "Video Player > Player Controls UI - Confirm Program Title for Movies / Series / Extras", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION}, dataProvider = "contentType", enabled = false)
    public void verifyProgramTitleOnPlayer(String contentType, String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String playerContentTitle;

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.isOpened(10);
        Assert.assertTrue(detailsPage.getContentTitle().equalsIgnoreCase(content), "We're not on the right content's detail page");
        if (contentType.equalsIgnoreCase(DisneyPlusApplePageBase.contentType.SERIES.toString())) {
            String episodeTitle = detailsPage.getEpisodeContentTitle();
            detailsPage.clickPlayButton().isOpened();
            playerContentTitle = videoPlayer.getTitleLabel();
            sa.assertTrue(videoPlayer.getSubTitleLabel().contains(episodeTitle), "Episode title doesn't match, expected: " + episodeTitle + " but found " + playerContentTitle);
            sa.assertTrue(playerContentTitle.contains(content), "Content title doesn't match");
        } else if (contentType.equalsIgnoreCase(DisneyPlusApplePageBase.contentType.EXTRAS.toString())) {
            detailsPage.clickExtrasTab();
            detailsPage.tapOnFirstContentTitle();
            playerContentTitle = videoPlayer.getTitleLabel();
            sa.assertTrue(playerContentTitle.contains(content), "Content title doesn't match, expected: " + content + " but found " + playerContentTitle);
        } else {
            detailsPage.clickPlayButton().isOpened();
            playerContentTitle = videoPlayer.getTitleLabel();
            sa.assertTrue(playerContentTitle.contains(content), "Content title doesn't match, expected: " + content + " but found " + playerContentTitle);
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66505"})
    @Test(description = "Video Player > Player Controls UI", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyPlayerControlUI() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SHORT_SERIES);
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.PAUSE), "Pause button is not visible on player overlay");
        videoPlayer.clickPauseButton();
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.PLAY), "Play button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.REWIND), "Rewind button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.FAST_FORWARD), "Forward button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.BACK), "Back button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.AIRPLAY), "Airplay Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.AUDIO_SUBTITLE_BUTTON), "Audio subtitle Menu Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isTitleLabelVisible(), "Title label is not visible on player overlay");
        sa.assertTrue(videoPlayer.isCurrentTimeLabelVisible(), "Current time label is not visible on player overlay");
        sa.assertTrue(videoPlayer.isRemainingTimeLabelVisible(), "Remaining time label is not visible on player overlay");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74457"})
    @Test(description = "Hulk - Hulu Video Player - Service Attribution", dataProvider = "userType", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION})
    public void verifyVideoPlayerServiceAttribution(String userType) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(createAccountWithSku(DisneySkuParameters.valueOf(userType), getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.getPlayButton().click();
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisible(), "service attribution wasn't visible when video started");
        sa.assertFalse(videoPlayer.isSeekbarVisible(), "player controls were displayed when video started");
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisibleWithControls(), "service attribution wasn't visible along with controls");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72690"})
    @Test(description = "VOD Player - RW & FW - Play State", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION})
    public void verifyRewindAndForwardButtonControlOnPlayerWhilePlaying() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SHORT_SERIES);

        int remainingTimeBeforeFwd = videoPlayer.getRemainingTime();
        int remainingTimeAfterFwdTapInPlayMode = videoPlayer.tapForwardButton(1).getRemainingTime();
        sa.assertTrue((remainingTimeBeforeFwd - remainingTimeAfterFwdTapInPlayMode) > 10,
                "Remaining time in play mode before fwd tap " + remainingTimeBeforeFwd +
                        " is not greater than remaining time after fwd tap " + remainingTimeAfterFwdTapInPlayMode);

        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_TEN);
        videoPlayer.waitForVideoToStart();
        int remainingTimeBeforeRewind = videoPlayer.getRemainingTime();
        int remainingTimeAfterRewindTapInPlayMode = videoPlayer.tapRewindButton(3).getRemainingTime();
        int remainingTimeDifferenceWhileRewind = remainingTimeAfterRewindTapInPlayMode - remainingTimeBeforeRewind;
        sa.assertTrue(remainingTimeDifferenceWhileRewind <= 30 && remainingTimeDifferenceWhileRewind > 0,
                "Remaining time in play mode time after rewind tap " + remainingTimeAfterRewindTapInPlayMode +
                        " is not greater than remaining time before rewind tap " + remainingTimeBeforeRewind);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66519"})
    @Test(description = "VOD Player Controls - Scrubber Elements", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION})
    public void verifyScrubberElementsOnPlayer() throws URISyntaxException, JsonProcessingException {
        String errorMessage = "not changed after scrub";
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(THE_MARVELS);

        String contentTimeFromUI = videoPlayer.getRemainingTimeInStringWithHourAndMinutes();
        ExploreContent movieApiContent = getApiMovieContent(MARVELS.getEntityId());
        String durationTime = videoPlayer.getHourMinFormatForDuration(movieApiContent.getDurationMs());
        sa.assertTrue(durationTime.equals(contentTimeFromUI), "Scruuber bar not representing total length of current video");

        sa.assertTrue(videoPlayer.isRemainingTimeLabelVisible(), "Time indicator for Remaining time was not found");
        sa.assertTrue(videoPlayer.isCurrentTimeLabelVisible(), "Time indicator for Elapsed time was not found");
        sa.assertTrue(videoPlayer.isSeekbarVisible(), "Scrubber Bar was not found");
        sa.assertTrue(videoPlayer.isRemainingTimeVisibleInCorrectFormat(), "Remaining time is not visible in HH:MM:SS or MM:SS Format");
        sa.assertTrue(videoPlayer.isCurrentTimeVisibleInCorrectFormat(), "Elapsed time is not visible in HH:MM:SS or MM:SS Format");

        int remainingTime = videoPlayer.getRemainingTimeThreeIntegers();
        int elapsedTime = videoPlayer.getCurrentTime();
        int currentPositionOnSeekPlayer = videoPlayer.getCurrentPositionOnPlayer();

        sa.assertTrue(videoPlayer.verifyPlayheadRepresentsCurrentPointOfTime(), "Playhead position not representing the current point in time with respect to the total length of the video");

        //video player already scrubbed 50 % in above method
        int remainingTimeAfterScrub = videoPlayer.getRemainingTime();
        int elapsedTimeAfterScrub = videoPlayer.getCurrentTime();
        int currentPositionAfterScrub = videoPlayer.getCurrentPositionOnPlayer();
        sa.assertTrue(remainingTime > remainingTimeAfterScrub, "Remaining time " + errorMessage);
        sa.assertTrue(elapsedTime < elapsedTimeAfterScrub, "Elapsed time " + errorMessage);
        sa.assertTrue(currentPositionAfterScrub > currentPositionOnSeekPlayer , "Position of seek bar " + errorMessage);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68450"})
    @Test(description = "VOD Player Controls - Backgrounding from the Player Behavior", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION})
    public void verifyVideoPlayerBehaviourAfterBackgroundingApp() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        loginAndStartPlayback(THE_MARVELS);
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_TEN);
        launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        pause(5);
        launchApp(buildType.getDisneyBundle());
        Assert.assertTrue(videoPlayer.isSeekbarVisible(), "Video controls are not displayed");
        Assert.assertTrue(videoPlayer.verifyVideoPaused(), "Video was not paused");
    }

    private void loginAndStartPlayback(String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.clickPlayButton().waitForVideoToStart().isOpened();
    }
}
