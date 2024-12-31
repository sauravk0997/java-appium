package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.alice.AliceAssert;
import com.disney.alice.AliceAssertion;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.alice.model.RecognitionMetaType;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.openqa.selenium.Dimension;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // Login and open deeplink to movie
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        videoPlayer.waitForVideoToStart();

        // Verify lock icon and click on it
        sa.assertTrue(isLockScreenButtonPresent(),"Lock icon is not present");
        pause(3);
        clickLockScreenButton();

        // Look for locked overlay
        tapOnScreenLocked(1);
        AliceAssertion aliceAssertion = aliceDriver.screenshotAndRecognize();
        aliceAssertion.isLabelPresent(sa, AliceLabels.CLOSED_CAPTIONING.getText());

        sa.assertAll();
    }

    private void tapOnScreenLocked(int numOfTaps) {
        pause(3);
        Dimension size = getDriver().manage().window().getSize();
        tapAtCoordinateNoOfTimes((size.width * 35), (size.height * 50), numOfTaps);
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
