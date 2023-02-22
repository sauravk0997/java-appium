package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.tests.disney.apple.ios.regression.videoplayer.DisneyPlusVideoUpNextTest.SHORT_SERIES;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.PlayerControl;

public class DisneyPlusVideoPlayerControlTest extends DisneyBaseTest {
    @DataProvider(name = "contentType")
    public Object[][] contentType() {
        return new Object[][]{{DisneyPlusApplePageBase.contentType.MOVIE.toString(), "Ice Age"},
                {DisneyPlusApplePageBase.contentType.SERIES.toString(), SHORT_SERIES},
                {DisneyPlusApplePageBase.contentType.EXTRAS.toString(), "Thor: Love and Thunder"}};
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61163"})
    @Test(description = "Video Player > User taps to close Video Player", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifyCloseButtonControlOnPlayer() {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        //Initiate playback from the 'details' page
        loginAndStartPlayback(SHORT_SERIES);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "'Details' page is not shown closing the video player");

        //Initiate playback from 'downloads'
        detailsPage.startDownload();
        detailsPage.waitForDownloadToComplete();
        homePage.clickDownloadsIcon();
        downloadsPage.tapDownloadedAssetFromListView(SHORT_SERIES);
        downloadsPage.tapDownloadedAsset("Play Bluey");
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        sa.assertTrue(downloadsPage.isContentHeaderPresent(SHORT_SERIES), "'Downloads list view' page didn't open after closing the video player");

        //Initiate playback from 'continue watching' from Home
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        homePage.initiatePlaybackFromContinueWatching();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        sa.assertTrue(homePage.isOpened(), "'Home' page didn't open after closing the video player");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61185"})
    @Test(description = " Video Player > Tap on screen to Rewind", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifyRewindButtonControlOnPlayer() {
        setGlobalVariables();
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61179"})
    @Test(description = " Video Player > Tap on screen to Forward", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifyForwardButtonControlOnPlayer() {
        setGlobalVariables();
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SHORT_SERIES);

        int remainingTimeInPauseMode = videoPlayer.clickPauseButton().getRemainingTime();
        int remainingTimeAfterFwdTapInPauseMode = videoPlayer.tapForwardButton(1).getRemainingTime();
        sa.assertTrue( remainingTimeInPauseMode > remainingTimeAfterFwdTapInPauseMode,
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
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifyCloseButtonForDeepLinkingContent() {
        setGlobalVariables();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(disneyAccount.get());
        new IOSUtils().launchDeeplink(true, R.TESTDATA.get("disney_debug_video_player_episode_deeplink"), 10);
        detailsPage.clickOpenButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), "Playback didn't start from deep link");
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(10), "Details Page is not shown after closing the player");
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), "Home Page is not shown after closing the Details Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72788"})
    @Test(description = "Video Player > User taps to close Video Player from Deeplink", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifyCloseButtonForDeepLinkingContentMovie() {
        setGlobalVariables();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(disneyAccount.get());
        new IOSUtils().launchDeeplink(true, R.TESTDATA.get("disney_debug_video_player_movie_deeplink"), 10);
        detailsPage.clickOpenButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), "Playback didn't start from deep link");
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(10), "Details Page is not shown after closing the player");
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), "Home Page is not shown after closing the Details Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61195"})
    @Test(description = "Video Player > Player Controls UI - Confirm Program Title for Movies / Series / Extras", groups = {"Video Player"}, dataProvider = "contentType")
    @Maintainer("gkrishna1")
    public void verifyProgramTitleOnPlayer(String contentType, String content) {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String playerContentTitle;

        setAppToHomeScreen(disneyAccount.get());
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
            sa.assertTrue(playerContentTitle.contains(episodeTitle), "Episode title doesn't match, expected: " + episodeTitle + " but found " + playerContentTitle);
            sa.assertTrue(videoPlayer.getSubTitleLabel().contains(content), "Content title doesn't match");
        } else if (contentType.equalsIgnoreCase(DisneyPlusApplePageBase.contentType.EXTRAS.toString())) {
            detailsPage.clickExtrasButton();
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61191"})
    @Test(description = "Video Player > Player Controls UI", groups = {"Video Player"})
    @Maintainer("gkrishna1")
    public void verifyPlayerControlUI() {
        setGlobalVariables();
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
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.CHROMECAST), "Chromecast Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.AUDIO_SUBTITLE_BUTTON), "Audio subtitle Menu Button is not visible on player overlay");
        sa.assertTrue(videoPlayer.isTitleLabelVisible(), "Title label is not visible on player overlay");
        sa.assertTrue(videoPlayer.isCurrentTimeLabelVisible(), "Current time label is not visible on player overlay");
        sa.assertTrue(videoPlayer.isRemainingTimeLabelVisible(), "Remaining time label is not visible on player overlay");
        sa.assertAll();
    }

    private void loginAndStartPlayback(String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(disneyAccount.get());
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.clickPlayButton().waitForVideoToStart().isOpened();
    }
}
