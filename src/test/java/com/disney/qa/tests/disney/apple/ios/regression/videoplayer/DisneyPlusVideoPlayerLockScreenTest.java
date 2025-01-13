package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusVideoPlayerLockScreenTest extends DisneyBaseTest {

    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73738"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackLockControlsTooltip() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        // Login and open deeplink to movie and validate lock controls tooltip
        setAppToHomeScreen(getAccount());
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
}
