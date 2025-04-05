package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
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
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).isPresent(),
                LOCK_ICON_NOT_PRESENT);
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.LOCK_ICON).click();
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                UNLOCK_ICON_NOT_PRESENT);
        videoPlayer.waitForElementToDisappear(
                videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON),
                DisneyAbstractPage.FIFTEEN_SEC_TIMEOUT);
        Assert.assertFalse(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                "Unlock icon was not automatically dismissed");
        // Click in the screen to make lock control appear
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertTrue(videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.UNLOCK_ICON).isPresent(),
                LOCK_ICON_NOT_PRESENT);
    }
}
