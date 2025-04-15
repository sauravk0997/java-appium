package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.explore.response.Visuals;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.apache.commons.lang3.time.StopWatch;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.temporal.ValueRange;
import java.util.concurrent.TimeUnit;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.*;
import static com.disney.qa.api.disney.DisneyEntityIds.MARVELS;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusVideoPlayerControlTest extends DisneyBaseTest {

    protected static final String THE_MARVELS = "The Marvels";
    private static final double SCRUB_PERCENTAGE_TEN = 10;

    @DataProvider(name = "contentType")
    public Object[][] contentType() {
        return new Object[][]{
                {DisneyPlusApplePageBase.contentType.MOVIE.toString(),
                        R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink")},
                {DisneyPlusApplePageBase.contentType.SERIES.toString(),
                        R.TESTDATA.get("disney_prod_series_detail_deeplink")}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66515"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyTitleAndBackButtonToClose() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.clickPlayButton().waitForVideoToStart().clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.clickPlayOrContinue().waitForVideoToStart().tapTitleOnPlayer();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66529"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRewindAndForwardButtonControlOnPlayerWhilePaused() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SERIES_BLUEY);

        videoPlayer.tapForwardButton(3);
        int remainingTimeInPauseMode = videoPlayer.clickPauseButton().getRemainingTime();
        int remainingTimeAfterRwdTapInPauseMode = videoPlayer.tapRewindButton(1).getRemainingTime();
        sa.assertTrue(remainingTimeInPauseMode < remainingTimeAfterRwdTapInPauseMode,
                "Remaining time in pause mode time after Rewind tap " + remainingTimeAfterRwdTapInPauseMode +
                        " is not greater than remaining time before Rewind tap " + remainingTimeInPauseMode);

        videoPlayer.clickPlayButton();
        remainingTimeInPauseMode = videoPlayer.clickPauseButton().getRemainingTime();
        int remainingTimeAfterFastForwardTapInPauseMode = videoPlayer.tapRewindButton(4).getRemainingTime();
        sa.assertTrue(remainingTimeInPauseMode > remainingTimeAfterFastForwardTapInPauseMode,
                "Remaining time in pause mode time after Forward tap " + remainingTimeAfterFastForwardTapInPauseMode +
                        " is not less than remaining time before Forward tap " + remainingTimeInPauseMode);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61169"})
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyCloseButtonForDeepLinkingContentSeries() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_debug_video_player_episode_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT), "Details Page is not shown after closing the player");
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), "Home Page is not shown after closing the Details Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68456"})
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyCloseButtonForDeepLinkingContentMovie() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_debug_video_player_movie_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), "Playback didn't start from deep link");
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT), "Details Page is not shown after closing the player");
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), "Home Page is not shown after closing the Details Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66505"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyVideoPlayerControlsUIMovies() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        String contentTitle = detailsPage.getContentTitle();

        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

        sa.assertTrue(videoPlayer.getTitleLabel().contains(contentTitle),
                "Content title doesn't match from the detail's content title");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.BACK), "Back button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.AIRPLAY),
                "Airplay Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.LOCK_ICON),
                "Lock Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.AUDIO_SUBTITLE_BUTTON),
                "Audio subtitle Menu Button is not visible on player overlay");

        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.PAUSE), "Pause button is not visible on player overlay");
        videoPlayer.clickPauseButton();
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.PLAY), "Play button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.REWIND), "Rewind button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.FAST_FORWARD), "Forward button is not visible on player overlay");

        sa.assertTrue(videoPlayer.isCurrentTimeMarkerVisible(),
                "Current time marker is not visible on player overlay");
        sa.assertTrue(videoPlayer.isCurrentTimeLabelVisible(),
                "Current time label is not visible on player overlay");
        sa.assertTrue(videoPlayer.isRemainingTimeLabelVisible(),
                "Remaining time label is not visible on player overlay");

        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.RESTART),
                "Restart button is not visible on player overlay");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74457"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyVideoPlayerServiceAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        String seriesDeliBoys = "Deli Boys";
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(seriesDeliBoys);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.clickPlayButton(TEN_SEC_TIMEOUT);
        Assert.assertTrue(videoPlayer.getServiceAttribution().getText().equals(HULU_SERVICE_ATTRIBUTION_MESSAGE));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72690"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRewindAndForwardButtonControlOnPlayerWhilePlaying() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SERIES_BLUEY);

        int remainingTimeBeforeFwd = videoPlayer.getRemainingTime();
        int remainingTimeAfterFwdTapInPlayMode = videoPlayer.tapForwardButton(1).getRemainingTime();
        sa.assertTrue((remainingTimeBeforeFwd - remainingTimeAfterFwdTapInPlayMode) > 10,
                "Remaining time in play mode before fwd tap " + remainingTimeBeforeFwd +
                        " is not greater than remaining time after fwd tap " + remainingTimeAfterFwdTapInPlayMode);

        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_TEN);
        videoPlayer.waitForVideoToStart();
        int remainingTimeBeforeRewind = videoPlayer.getRemainingTime();
        int remainingTimeAfterRewindTapInPlayMode = videoPlayer.tapRewindButton(4).getRemainingTime();
        int remainingTimeDifferenceWhileRewind = remainingTimeAfterRewindTapInPlayMode - remainingTimeBeforeRewind;
        sa.assertTrue(remainingTimeDifferenceWhileRewind <= 40 && remainingTimeDifferenceWhileRewind > 0,
                "Remaining time in play mode time after rewind tap " + remainingTimeAfterRewindTapInPlayMode +
                        " is not greater than remaining time before rewind tap " + remainingTimeBeforeRewind);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66519"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyScrubberElementsOnPlayer() {
        String errorMessage = "not changed after scrub";
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(THE_MARVELS);

        String contentTimeFromUI = videoPlayer.getRemainingTimeInStringWithHourAndMinutes();
        ExploreContent movieApiContent = getMovieApi(MARVELS.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String durationTime = videoPlayer.getHourMinFormatForDuration(movieApiContent.getDurationMs());
        sa.assertTrue(durationTime.equals(contentTimeFromUI), "Scrubber bar not representing total length of current video");
        sa.assertTrue(videoPlayer.isRemainingTimeLabelVisible(), "Time indicator for Remaining time was not found");
        sa.assertTrue(videoPlayer.isCurrentTimeLabelVisible(), "Time indicator for Elapsed time was not found");
        sa.assertTrue(videoPlayer.isSeekbarVisible(), "Scrubber Bar was not found");
        sa.assertTrue(videoPlayer.isRemainingTimeVisibleInCorrectFormat(), "Remaining time is not visible in HH:MM:SS or MM:SS Format");
        sa.assertTrue(videoPlayer.isCurrentTimeVisibleInCorrectFormat(), "Elapsed time is not visible in HH:MM:SS or MM:SS Format");

        int remainingTime = videoPlayer.getRemainingTimeThreeIntegers();
        int elapsedTime = videoPlayer.getCurrentTime();
        int currentPositionOnSeekPlayer = videoPlayer.getCurrentPositionOnPlayer();
        sa.assertTrue(videoPlayer.verifyPlayheadRepresentsCurrentPointOfTime(),
                "Playhead position not representing the current point in time with respect to the total length of the video");

        //video player already scrubbed 50 % in above method
        int remainingTimeAfterScrub = videoPlayer.getRemainingTime();
        int elapsedTimeAfterScrub = videoPlayer.getCurrentTime();
        int currentPositionAfterScrub = videoPlayer.getCurrentPositionOnPlayer();
        sa.assertTrue(remainingTime > remainingTimeAfterScrub, "Remaining time " + errorMessage);
        sa.assertTrue(elapsedTime < elapsedTimeAfterScrub, "Elapsed time " + errorMessage);
        sa.assertTrue(currentPositionAfterScrub > currentPositionOnSeekPlayer, "Position of seek bar " + errorMessage);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68450"})
    @Test(description = "VOD Player Controls - Backgrounding from the Player Behavior", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyVideoPlayerBehaviourAfterBackgroundingApp() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setPictureInPictureConfig(DISABLED);
        loginAndStartPlayback(THE_MARVELS);
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_TEN);
        videoPlayer.waitForVideoToStart();
        runAppInBackground(5);
        Assert.assertTrue(videoPlayer.isSeekbarVisible(), "Video controls are not displayed");
        Assert.assertTrue(videoPlayer.verifyVideoPaused(), "Video was not paused");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72539"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyVideoPlayerControlsUISeries() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        String contentTitle = detailsPage.getContentTitle();
        String episodeTitle = detailsPage.getEpisodeContentTitle();

        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

        sa.assertTrue(videoPlayer.getSubTitleLabel().contains(episodeTitle),
                "Episode title doesn't match from the detail's episode title");
        sa.assertTrue(videoPlayer.getTitleLabel().contains(contentTitle),
                "Content title doesn't match from the detail's content title");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.BACK), "Back button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.AIRPLAY),
                "Airplay Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.LOCK_ICON),
                "Lock Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.AUDIO_SUBTITLE_BUTTON),
                "Audio subtitle Menu Button is not visible on player overlay");

        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.PAUSE), "Pause button is not visible on player overlay");
        videoPlayer.clickPauseButton();
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.PLAY), "Play button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.REWIND), "Rewind button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.FAST_FORWARD), "Forward button is not visible on player overlay");

        sa.assertTrue(videoPlayer.isCurrentTimeMarkerVisible(),
                "Current time marker is not visible on player overlay");
        sa.assertTrue(videoPlayer.isCurrentTimeLabelVisible(),
                "Current time label is not visible on player overlay");
        sa.assertTrue(videoPlayer.isRemainingTimeLabelVisible(),
                "Remaining time label is not visible on player overlay");

        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.RESTART),
                "Restart button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.NEXT_EPISODE),
                "Next episode button is not visible on player overlay");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66834"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, TestGroup.VIDEO_PLAYER, US})
    public void testDeeplinkContentMaturityRestriction() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String RATING_PG_13 = "PG-13";
        String HOME_NOT_DISPLAYED_ERROR_MESSAGE = "Home page is not displayed";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        // Assign PG-13 content maturity rating
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_PG_13);

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_NOT_DISPLAYED_ERROR_MESSAGE);
        // Launch deeplink Dead Pool rated R
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_deadpool_rated_r_deeplink"));
        Assert.assertTrue(homePage.getUnavailableContentError().isPresent(),
                "Rating playback message error is not present");
        homePage.clickAlertConfirm();
        Assert.assertTrue(homePage.isOpened(), HOME_NOT_DISPLAYED_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66517"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDisplayingTheDuration() {
        SoftAssert sa = new SoftAssert();
        String videoPlayerErrorMsg = "Video player remaining time format is not as expected for ";
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        loginAndStartPlayback(THE_MARVELS);

        videoPlayer.displayVideoController();
        String remainingTime = videoPlayer.getTimeRemainingLabelText();
        sa.assertTrue(videoPlayer.validateTimeFormat(remainingTime),
                videoPlayerErrorMsg + remainingTime);

        videoPlayer.scrubToPlaybackPercentage(50);
        videoPlayer.displayVideoController();
        String remainingTimeAfterFirstScrub = videoPlayer.getTimeRemainingLabelText();
        sa.assertTrue(videoPlayer.validateTimeFormat(remainingTimeAfterFirstScrub),
                videoPlayerErrorMsg + remainingTimeAfterFirstScrub);

        videoPlayer.scrubToPlaybackPercentage(96);
        videoPlayer.displayVideoController();
        String remainingTimeAfterSecondScrub = videoPlayer.getTimeRemainingLabelText();
        sa.assertTrue(videoPlayer.validateTimeFormat(remainingTimeAfterSecondScrub),
                videoPlayerErrorMsg + remainingTimeAfterSecondScrub);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66509"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyVideoControlBringUpAndDismissControls() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(THE_MARVELS);

        videoPlayer.waitForVideoToStart();
        // Tap anywhere on the video player and validate video controls
        videoPlayer.displayVideoController();
        sa.assertTrue(videoPlayer.getPauseButton().isPresent(SHORT_TIMEOUT),
                "Video player controls are not displayed after tapping the player screen");
        // Wait for video controls to disappear and validate them
        videoPlayer.waitForVideoControlToDisappear();
        sa.assertFalse(videoPlayer.getPauseButton().isPresent(SHORT_TIMEOUT),
                "Video player controls are not automatically dismissed");
        // Tap anywhere to activate video controls and tap again to validate that video controls are dismissed
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50, 2);
        sa.assertFalse(videoPlayer.getPauseButton().isPresent(),
                "Video player controls are not dismissed");
        // Prepare for pause action and validate video controls are up
        videoPlayer.clickPauseButton();
        sa.assertTrue(videoPlayer.verifyVideoPaused(),
                "Video player did not paused");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71946"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyVideoControlRewindAndForwardWithControlsDown() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        int timeBeforeDoubleTap = 0;
        int timeAfterDoubleTap = 0;
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(THE_MARVELS);
        videoPlayer.waitForVideoToStart();

        // Double tap to fast forward and validate time
        timeBeforeDoubleTap = videoPlayer.getCurrentTime();
        videoPlayer.waitForVideoControlToDisappear();
        videoPlayer.tapPlayerScreen(PlayerControl.FAST_FORWARD, 2);
        timeAfterDoubleTap = videoPlayer.getCurrentTime();
        LOGGER.info("timeAfterDoubleTap {} timeBeforeDoubleTap {}" , timeAfterDoubleTap,
                timeBeforeDoubleTap);
        sa.assertTrue(timeAfterDoubleTap > timeBeforeDoubleTap, "Fast Forward did not work as expected");
        // Double tap and rewind three times to make sure the rewind is working and compare against current time
        timeBeforeDoubleTap = videoPlayer.getCurrentTime();
        videoPlayer.waitForVideoControlToDisappear();
        videoPlayer.tapPlayerScreen(PlayerControl.REWIND, 2);
        videoPlayer.tapPlayerScreen(PlayerControl.REWIND, 2);
        videoPlayer.tapPlayerScreen(PlayerControl.REWIND, 2);
        timeAfterDoubleTap = videoPlayer.getCurrentTime();
        LOGGER.info("timeBeforeDoubleTap {} timeAfterDoubleTap {}" , timeBeforeDoubleTap,
                timeAfterDoubleTap);
        sa.assertTrue( timeAfterDoubleTap < timeBeforeDoubleTap, "Rewind did not work as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74453"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.HULU, TestGroup.VIDEO_PLAYER, US})
    public void testNetworkWatermarkUserInterrupted() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        StopWatch stopWatch = new StopWatch();
        int uiLatency = 30;
        String network = "FX";
        String entitySeries = "entity-6bf318d8-f506-4e7f-a58f-0c5cc09b6c90";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.waitForHomePageToOpen();

        //Get one percent of the first episode duration
        ExploreContent seriesApiContent = getSeriesApi(entitySeries,
                DisneyPlusBrandIOSPageBase.Brand.HULU);
        Visuals episodeDetails = seriesApiContent.getSeasons().get(0).getItems().get(0).getVisuals();
        int minimumNetworkEpisodeLogoDuration = (int)Math.round((episodeDetails.getDurationMs() / 1000) * .01);

        // Launch deeplink for FX content and start to play
        launchDeeplink(R.TESTDATA.get("hulu_prod_series_pose_deeplink"));
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        // Start timer
        stopWatch.start();
        // Validate that content rating overlay and FX logo are present
        sa.assertTrue(videoPlayer.isContentRatingOverlayPresent(), "Rating overlay is not present");
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), "Network watermark is not present");
        // Wait for video controls to disappear and click on the screen to validate FX logo is not present
        videoPlayer.waitForVideoControlToDisappear();
        videoPlayer.clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        sa.assertFalse(videoPlayer.getTypeOtherContainsLabel(network).isPresent(2), "Network watermark is present");
        // Wait for network watermark to disappear and validate time after stop timer
        videoPlayer.waitForVideoControlToDisappear();
        sa.assertTrue(videoPlayer.waitForNetworkWatermarkLogoToDisappear(network), "Watermark network is present");
        stopWatch.stop();
        long totalTime = stopWatch.getTime(TimeUnit.SECONDS);
        LOGGER.info("totalTime {}, minimumNetworkEpisodeLogoDuration {}", totalTime, minimumNetworkEpisodeLogoDuration);
        int playDuration = ((int)totalTime - minimumNetworkEpisodeLogoDuration);
        ValueRange range = ValueRange.of(0, uiLatency);
        sa.assertTrue(range.isValidIntValue(playDuration), "Network watermark was not displayed within expected duration");

        sa.assertAll();
    }

    private void loginAndStartPlayback(String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        searchPage.getDynamicAccessibilityId(content).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
    }
}
