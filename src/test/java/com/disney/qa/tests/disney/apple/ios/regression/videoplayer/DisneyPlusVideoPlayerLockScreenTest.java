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
import static com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.*;

public class DisneyPlusVideoPlayerLockScreenTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74131"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackLockScreen() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        // Login and open deeplink to movie
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(isLockScreenButtonPresent(),
                "Lock icon is not present");
        pause(5);
        clickLockScreenButton();
        System.out.println(getDriver().getPageSource());
        Assert.assertTrue(getScreenLockedText(),"Screen is not locked");


        sa.assertAll();
    }

    private boolean isLockScreenButtonPresent() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        return videoPlayer.getElementFor(PlayerControl.LOCK_ICON).isElementPresent();
    }

    private void clickLockScreenButton() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        videoPlayer.getElementFor(PlayerControl.LOCK_ICON).click();
    }

    private boolean getScreenLockedText() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        // videoPlayer.displayVideoController();
        clickElementAtLocation(videoPlayer.getPlayerView(), 50, 50);
        return videoPlayer.getPlayerControlScreenLockedPresent().isElementPresent();
        //.getStaticTextByLabelContains
        // ("SCREEN
        // LOCKED").isPresent();
    }
}
