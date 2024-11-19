package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;

import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
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

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.ONLY_MURDERS_IN_THE_BUILDING;
import static com.disney.qa.tests.disney.apple.ios.regression.videoplayer.DisneyPlusVideoUpNextTest.SHORT_SERIES;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.PlayerControl;
import static com.disney.qa.api.disney.DisneyEntityIds.MARVELS;

public class DisneyPlusVideoPlayerControlTest extends DisneyBaseTest {
    protected static final String THE_MARVELS = "The Marvels";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player didn't open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page didn't open";
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

    @DataProvider(name = "userType")
    public Object[][] userType() {
        return new Object[][]{{"DISNEY_HULU_NO_ADS_ESPN_WEB"},
                {"DISNEY_VERIFIED_HULU_ESPN_BUNDLE"}};
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66515"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyTitleAndBackButtonToClose() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        detailsPage.clickPlayButton().waitForVideoToStart().clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        detailsPage.clickPlayOrContinue().waitForVideoToStart().tapTitleOnPlayer();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66529"})
    @Test(description = " Video Player > Tap on screen to Rewind", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(description = " Video Player > Tap on screen to Forward", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
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
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyCloseButtonForDeepLinkingContentSeries() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_debug_video_player_episode_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), "Playback didn't start from deep link");
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT), "Details Page is not shown after closing the player");
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), "Home Page is not shown after closing the Details Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68456"})
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyCloseButtonForDeepLinkingContentMovie() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

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

        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        String contentTitle = detailsPage.getContentTitle();

        Assert.assertTrue(detailsPage.clickPlayButton().isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

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
    @Test(dataProvider = "userType", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyVideoPlayerServiceAttribution(String userType) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(createAccountWithSku(DisneySkuParameters.valueOf(userType),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickPlayButton(TEN_SEC_TIMEOUT);
        videoPlayer.waitForVideoToStart();
        videoPlayer.waitForAdToCompleteIfPresent(5);
        videoPlayer.skipPromoIfPresent();

        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisible(),
                "service attribution wasn't visible when video started");
        sa.assertFalse(videoPlayer.isSeekbarVisible(),
                "player controls were displayed when video started");
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisibleWithControls(),
                "service attribution wasn't visible along with controls");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72690"})
    @Test(description = "VOD Player - RW & FW - Play State", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
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
    @Test(description = "VOD Player Controls - Scrubber Elements", groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyScrubberElementsOnPlayer() throws URISyntaxException, JsonProcessingException {
        String errorMessage = "not changed after scrub";
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(THE_MARVELS);

        String contentTimeFromUI = videoPlayer.getRemainingTimeInStringWithHourAndMinutes();
        ExploreContent movieApiContent = getDisneyApiMovie(MARVELS.getEntityId());
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

        sa.assertTrue(videoPlayer.verifyPlayheadRepresentsCurrentPointOfTime(), "Playhead position not representing the current point in time with respect to the total length of the video");

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

        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        String contentTitle = detailsPage.getContentTitle();
        String episodeTitle = detailsPage.getEpisodeContentTitle();

        Assert.assertTrue(detailsPage.clickPlayButton().isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

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
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        // Assign PG-13 content maturity rating
        getAccountApi().editContentRatingProfileSetting(getAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_PG_13);

        setAppToHomeScreen(getAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_NOT_DISPLAYED_ERROR_MESSAGE);
        // Launch deeplink Dead Pool rated R
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_deadpool_rated_r_deeplink"));
        Assert.assertTrue(homePage.getRatingRestrictionPlaybackMessage().isPresent(),
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71946"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyVideoControlRewindAndForwardWithControlsDown() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        int timeBeforeDoubleTap = 0;
        int timeAfterDoubleTap = 0;
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(THE_MARVELS);
        videoPlayer.waitForVideoToStart();
        videoPlayer.displayVideoController();
        timeBeforeDoubleTap = videoPlayer.getCurrentTime();
        videoPlayer.waitForVideoControlToDisappear();
        videoPlayer.tapPlayerScreen(PlayerControl.REWIND, 2);
        timeAfterDoubleTap = videoPlayer.getCurrentTime();
        LOGGER.info("timeBeforeDoubleTap {} timeAfterDoubleTap {}" , timeBeforeDoubleTap,
                timeAfterDoubleTap);
        sa.assertTrue(timeBeforeDoubleTap <= timeAfterDoubleTap, "Rewind did not work as expected");
        timeBeforeDoubleTap = videoPlayer.getCurrentTime();
        videoPlayer.waitForVideoControlToDisappear();
        videoPlayer.tapPlayerScreen(PlayerControl.FAST_FORWARD, 2);
        timeAfterDoubleTap = videoPlayer.getCurrentTime();
        LOGGER.info("timeAfterDoubleTap {} timeBeforeDoubleTap {}" , timeAfterDoubleTap,
                timeBeforeDoubleTap);
        sa.assertTrue(timeAfterDoubleTap > timeBeforeDoubleTap, "Fast Forward did not work as expected");
        sa.assertAll();
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
