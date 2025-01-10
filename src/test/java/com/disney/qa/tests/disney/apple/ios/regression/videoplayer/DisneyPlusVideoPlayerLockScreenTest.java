package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
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
        SoftAssert sa = new SoftAssert();

        // Login and open deeplink to movie and validate lock controls tooltip
        setAppToHomeScreen(getAccount());
        openDeeplinkAndPlayContent("disney_prod_movie_detail_dr_strange_deeplink");

        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        Assert.assertTrue(videoPlayer.waitForVideoLockTooltipToAppear(), "Video controls tooltip did not appear");
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        sa.assertFalse(videoPlayer.getLockScreenToolTip().isPresent(), "Video player tooltip is still present");
        videoPlayer.clickBackButton();
        detailsPage.getBackButton().click();
        detailsPage.clickHomeIcon();
        // Open deeplink to another content and validate lock controls
        openDeeplinkAndPlayContent("disney_prod_series_detail_loki_deeplink");
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        sa.assertFalse(videoPlayer.getLockScreenToolTip().isPresent(), "Video player tooltip is present");

        sa.assertAll();
    }

    private void openDeeplinkAndPlayContent(String deeplink) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        launchDeeplink(R.TESTDATA.get(deeplink));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();
    }
}
