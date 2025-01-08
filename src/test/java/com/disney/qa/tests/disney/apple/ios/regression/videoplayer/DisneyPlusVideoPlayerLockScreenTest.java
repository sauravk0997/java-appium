package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusVideoPlayerLockScreenTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73738"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackLockControlsTooltip() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        // Login and open deeplink to movie and validate lock controls tooltip
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        videoPlayer.waitForVideoToStart();
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        videoPlayer.waitForVideoLockTooltipToAppear();
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        sa.assertFalse(videoPlayer.getLockScreenToolTip().isPresent(), "Video player tooltip is still present");
        videoPlayer.clickBackButton();
        detailsPage.getBackButton().click();
        detailsPage.clickHomeIcon();
        // Open deeplink to another content and validate lock controls
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        videoPlayer.waitForVideoToStart();
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        sa.assertFalse(videoPlayer.getLockScreenToolTip().isPresent(), "Video player tooltip is present");

        sa.assertAll();
    }
}
