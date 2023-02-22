package com.disney.qa.tests.espn.web.appex;

import com.disney.qa.espn.web.EspnBasePage;
import com.disney.qa.espn.web.EspnLoginPage;
import com.disney.qa.espn.web.EspnWebParameters;
import com.disney.qa.tests.BaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class EspnVodTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    Dimension d = new Dimension(1400, 900);
    String env = Configuration.get(Configuration.Parameter.ENV);
    EspnLoginPage page = null;

    @BeforeTest
    public void beforeTests(){
        page = new EspnLoginPage(getDriver());
        getDriver().manage().window().setSize(d);
        page.redirectUrl(env, "Video");
        page.waitForPageToFinishLoading();
    }

    @Test(description = "Login and Select a Recent Replay Video", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9212")
    public void testViewRecentReplayVideo () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        LOGGER.info("URL is: " + espnBasePage.getCurrentUrl());
        page.login(EspnWebParameters.ESPN_WEB_USER.getValue()
                ,EspnWebParameters.ESPN_WEB_PASS.getDecryptedValue());
        page.waitForPageToFinishLoading();

        pause(10);
        espnBasePage.openVodAsset();
        sa.assertTrue(espnBasePage.getVideoPlayer().isElementPresent(), "Video Player did not load");

        sa.assertAll();
    }

    @Test(description = "Pause Video", dependsOnMethods = "testViewRecentReplayVideo", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9213")
    public void testPauseVideo () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.enterVideoIframe();
        espnBasePage.videoPlayPauseStopPlayer();
        sa.assertTrue(espnBasePage.getVideoPlayPauseStopButton().isElementPresent(), "Video did NOT pause");

        sa.assertAll();
    }

    @Test(description = "Play Video", dependsOnMethods = "testPauseVideo", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9214")
    public void testPlayVideo () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.videoPlayPauseStopPlayer();
        sa.assertTrue(espnBasePage.getVideoPlayPauseStopButton().isElementPresent(), "Video did NOT play");

        sa.assertAll();
    }

    @Test(description = "Volume Control", dependsOnMethods = "testPlayVideo", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9215")
    public void testVolumeControl () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.volumeControl();
        sa.assertTrue(espnBasePage.getVideoVolumnControl().isElementPresent(), "Video Control NOT successful");
        espnBasePage.volumeControl();
        sa.assertTrue(espnBasePage.getVideoVolumnControl().isElementPresent(), "Video Control NOT successful");

        espnBasePage.adjustVolumeSlider();
        sa.assertTrue(espnBasePage.getVolumeRange().getAttribute("style").contains("width: 7") |
                        espnBasePage.getVolumeRange().getAttribute("style").contains("width: 1") |
                        espnBasePage.getVolumeRange().getAttribute("style").contains("width: 9") ,
                    "Volume NOT adjusted");

        sa.assertAll();
    }

    @Test(description = "Video Share Links", dependsOnMethods = "testVolumeControl", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9216")
    public void testVideoShareLinks () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.videoShareLink();
        sa.assertTrue(espnBasePage.getVideoFacebookShareLink().isElementPresent(), "Facebook share link NOT present");
        sa.assertTrue(espnBasePage.getVideoTwitterShareLink().isElementPresent(), "Twitter share link NOT present");
        sa.assertTrue(espnBasePage.getVideoEmailShareLink().isElementPresent(), "Email share link NOT present");
        espnBasePage.exitVideoShare();
        espnBasePage.exitVideoShare();

        sa.assertAll();
    }

    @Test(description = "Video Broadcast Select", dependsOnMethods = "testVideoShareLinks", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9217")
    public void testVideoBroadcastSelect () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.viewBroadcastOptions();

        sa.assertAll();
    }

    @Test(description = "Video Settings", dependsOnMethods = "testVideoBroadcastSelect", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9218")
    public void testVideoSettings () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.openVideoSettings();
        sa.assertTrue(espnBasePage.getVideoGearSettingsAutoPlay().isElementPresent(), "Auto play not found");
        espnBasePage.closedCaptionPresent();

        sa.assertAll();
    }

    @Test(description = "Video Full Screen Mode", dependsOnMethods = "testVideoSettings")
    @QTestCases(id = "9219")
    public void testVideoFullScreen () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.videoEnterFullScreen();
        LOGGER.info("Full Screen = " + espnBasePage.getVideoExitFullScreenButton().getSize());
        espnBasePage.videoExitFullScreen();
        sa.assertTrue(espnBasePage.getVideoEnterFullScreenButton().isElementPresent(), "Video is NOT back to default");

        sa.assertAll();
    }

    @Test(description = "Video Scrubber Function", dependsOnMethods = "testVideoFullScreen")
    @QTestCases(id = "9220")
    public void testVideoScrubberFunction () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.scrubberSeekVideo();
        sa.assertTrue(espnBasePage.getVideoScrubberProgress().getAttribute("style").contains("width: 0.2") |
                        espnBasePage.getVideoScrubberProgress().getAttribute("style").contains("width: 0.5") |
                        espnBasePage.getVideoScrubberProgress().getAttribute("style").contains("width: 0.8"),
                "Scrubber NOT adjusted");

        sa.assertAll();
        page.navigateBack();
        page.logout();
    }
}
