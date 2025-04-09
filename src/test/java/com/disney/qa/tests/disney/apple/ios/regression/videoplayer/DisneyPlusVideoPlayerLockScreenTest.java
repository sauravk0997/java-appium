package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusUpNextIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Listeners;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusVideoPlayerLockScreenTest extends DisneyBaseTest {

    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";
    private static final String UNLOCK_ICON_NOT_PRESENT = "Unlock icon is not present";
    private static final String LOCK_ICON_NOT_PRESENT = "Lock icon is not present";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73738"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackLockControlsTooltip() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        // Login and open deeplink to movie and validate lock controls tooltip
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertTrue(videoPlayer.waitForVideoLockTooltipToAppear(), "Video controls tooltip did not appear");
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        sa.assertFalse(videoPlayer.getLockScreenToolTip().isPresent(), "Video player tooltip is still present");
        videoPlayer.clickBackButton();
        detailsPage.getBackButton().click();
        detailsPage.clickHomeIcon();
        // Open deeplink to another content and validate lock controls
        launchDeeplink(R.TESTDATA.get("disney_prod_series_loki_first_episode_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        sa.assertFalse(videoPlayer.getLockScreenToolTip().isPresent(), "Video player tooltip is present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74131"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackDismissLockedOverlay() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        // Login and open deeplink to movie and validate lock controls
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        // Click in the screen to make lock control appear
        Assert.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON),
                LOCK_ICON_NOT_PRESENT);
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).click();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        // Click in the screen to make unlock control disappear
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertFalse(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                "Unlock icon was not hidden");
        // Click in the screen to make unlock control appear
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73814"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackLockedOverlayAutoDismiss() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        // Login and open deeplink to movie and validate lock controls
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        // Click in the screen to make lock control appear and lock screen
        Assert.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON),
                LOCK_ICON_NOT_PRESENT);
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).click();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        videoPlayer.waitForElementToDisappear(
                videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON),
                DisneyAbstractPage.TEN_SEC_TIMEOUT);
        Assert.assertFalse(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                "Unlock icon was not automatically dismissed");
        // Click in the screen to make lock control appear
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                LOCK_ICON_NOT_PRESENT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74161"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyExitingAppReturnToUnlocked() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        // Login and open deeplink to movie and validate lock controls
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        // Click in the screen to make lock control appear and lock screen
        Assert.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON),
                LOCK_ICON_NOT_PRESENT);
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).click();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        // Terminate app and relaunch
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
        // Validate playback is not locked
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertFalse(videoPlayer.getElementFor(
                DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                "Playback is in locked state after exiting the app");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74162"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDetailsUpNextUnlock() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusUpNextIOSPageBase upNext = initPage(DisneyPlusUpNextIOSPageBase.class);
        int percentage = 97;
        // Login and open deeplink to movie and validate lock controls
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        // Scrub to up next UI and lock screen
        videoPlayer.scrubToPlaybackPercentage(percentage);
        videoPlayer.waitForVideoControlToDisappear();
        Assert.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON),
                LOCK_ICON_NOT_PRESENT);
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).click();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        upNext.waitForUpNextUIToAppear();
        Assert.assertTrue(upNext.isOpened(), "Up next UI not present");
        Assert.assertTrue(upNext.getSeeDetailsButton().isPresent(), "See details button is not present");
        // Click Details button and validate video player is closed
        upNext.getSeeDetailsButton().click();
        Assert.assertFalse(videoPlayer.isOpened(), "Video player continued open after clicking Details button");
        // Open video player and validate it is not locked
        launchDeeplink(R.TESTDATA.get("disney_prod_content_mulan_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertFalse(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                "Playback screen was locked after reopening the video player");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73793"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUnlockedTapLock() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        // Login and open deeplink to movie and validate lock controls
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        // Lock playback screen
        Assert.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON),
                LOCK_ICON_NOT_PRESENT);
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).click();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        videoPlayer.waitForElementToDisappear(
                videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON),
                DisneyAbstractPage.TEN_SEC_TIMEOUT);
        // Long tap in unlock button and verify it is unlocked
        videoPlayer.clickUnlockButton();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).isPresent(),
                LOCK_ICON_NOT_PRESENT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73740"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyTapScreenLockedOverlay() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String tapAndHold = "Tap and hold to unlock";

        // Login and open deeplink to movie and validate lock controls
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        // Click in the screen to make lock control appear and lock screen
        Assert.assertTrue(videoPlayer.isElementPresent(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON),
                LOCK_ICON_NOT_PRESENT);
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).click();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);

        videoPlayer.waitForElementToDisappear(
                videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON),
                DisneyAbstractPage.TEN_SEC_TIMEOUT);

        // Click in the screen to validate locked overlay
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        Assert.assertTrue(videoPlayer.getTypeOtherContainsLabel(tapAndHold).isPresent(),
                "Tap and hold text is not present");
        videoPlayer.waitForElementToDisappear(
                videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON),
                DisneyAbstractPage.TEN_SEC_TIMEOUT);

        // Validate unlock icon is at the bottom and center
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        validateElementPositionAlignment(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON), CENTER_POSITION);
        videoPlayer.waitForElementToDisappear(
                videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON),
                DisneyAbstractPage.TEN_SEC_TIMEOUT);
        // Click again in the screen to get height position
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        validateElementExpectedHeightPosition(
                videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON), BOTTOM);
    }
}
