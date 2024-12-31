package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.alice.AliceAssertion;
import com.disney.alice.AliceDriver;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
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
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        SoftAssert sa = new SoftAssert();
        String overlayLocked = "closed_captioning";

        // Login and open deeplink to movie
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(isLockScreenButtonPresent(),"Lock icon is not present");
        pause(3);
        clickLockScreenButton();

        // Look for locked overlay
        pause(3);
        clickElementAtLocation(videoPlayer.getPlayerView(), 10, 50);
        AliceAssertion aliceAssertion = aliceDriver.screenshotAndRecognize();
        aliceAssertion.getMetaData().forEach(item -> item.getCaption());
        aliceAssertion.isLabelPresent(sa, overlayLocked);
        aliceAssertion.isLabelPresent(sa, "CLOSED_CAPTIONING");
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
}
