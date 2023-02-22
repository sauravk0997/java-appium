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

public class EspnLiveNoDvrTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    Dimension d = new Dimension(1400, 900);

    String env = Configuration.get(Configuration.Parameter.ENV);
    String espnNetwork = "ESPN+";
    String mlbNetwork = "MLB.tv";

    EspnLoginPage page = null;

    @BeforeTest
    public void beforeTests(){
        page = new EspnLoginPage(getDriver());
        getDriver().manage().window().setSize(d);
        page.redirectUrl(env, "Video");
        page.waitForPageToFinishLoading();
    }

    @Test(description = "Login and Select a Live Video",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9203")
    public void testViewLiveVideo () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        LOGGER.info("URL is: " + espnBasePage.getCurrentUrl());
        page.login(EspnWebParameters.ESPN_WEB_USER.getValue()
                ,EspnWebParameters.ESPN_WEB_PASS.getDecryptedValue());
        page.waitForPageToFinishLoading();
        espnBasePage.openLiveAsset(espnNetwork);
        espnBasePage.selectLiveAsset(mlbNetwork);
        page.waitForPageToFinishLoading();
        sa.assertTrue(espnBasePage.getVideoPlayer().isElementPresent(), "Video Player did not load");
        LOGGER.info("Selected Video: " + espnBasePage.getVideoTitle().getText());
        sa.assertTrue(espnBasePage.getVideoTitle().isElementPresent(), "Video Title not present");

        sa.assertAll();
    }

    @Test(description = "Pause Live Video", dependsOnMethods = "testViewLiveVideo",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9204")
    public void testPauseLiveVideo () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.enterVideoIframe();
        espnBasePage.videoPlayPauseStopPlayer();
        sa.assertTrue(espnBasePage.getVideoPlayPauseStopButton().isElementPresent(), "Video did NOT pause or stop");

        sa.assertAll();
    }

    @Test(description = "Play Live Video", dependsOnMethods = "testPauseLiveVideo" ,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9205")
    public void testPlayLiveVideo () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.videoPlayPauseStopPlayer();
        sa.assertTrue(espnBasePage.getVideoPlayPauseStopButton().isElementPresent(), "Video did NOT play");

        sa.assertAll();
    }

    @Test(description = "Live Video Volume Control", dependsOnMethods = "testPlayLiveVideo",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9206")
    public void testLiveVolumeControl () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.volumeControl();
        sa.assertTrue(espnBasePage.getVideoVolumnControl().isElementPresent(), "Video Control NOT successful");
        espnBasePage.volumeControl();
        sa.assertTrue(espnBasePage.getVideoVolumnControl().isElementPresent(), "Video Control NOT successful");

        espnBasePage.adjustVolumeSlider();
        sa.assertTrue(espnBasePage.getVolumeRange().getAttribute("style").contains("width: 7") |
                espnBasePage.getVolumeRange().getAttribute("style").contains("width: 10") |
                espnBasePage.getVolumeRange().getAttribute("style").contains("width: 6"),"Volume NOT adjusted");

        sa.assertAll();
    }

    @Test(description = "Live Video Share Links", dependsOnMethods = "testLiveVolumeControl",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9207")
    public void testLiveVideoShareLinks () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.videoShareLink();
        sa.assertTrue(espnBasePage.getVideoFacebookShareLink().isElementPresent(), "Facebook share link NOT present");
        sa.assertTrue(espnBasePage.getVideoTwitterShareLink().isElementPresent(), "Twitter share link NOT present");
        sa.assertTrue(espnBasePage.getVideoEmailShareLink().isElementPresent(), "Email share link NOT present");
        espnBasePage.exitVideoShare();

        sa.assertAll();
    }

    @Test(description = "Live Video Broadcast Select", dependsOnMethods = "testLiveVideoShareLinks",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9208")
    public void testLiveVideoBroadcastSelect () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.viewLiveBroadcastOptions();

        sa.assertAll();
    }

    @Test(description = "Live Video Settings", dependsOnMethods = "testLiveVideoBroadcastSelect",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9209")
    public void testLiveVideoSettings () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.openVideoSettings();
        sa.assertTrue(espnBasePage.getVideoGearSettingsAutoPlay().isElementPresent(), "Auto play not found");
        espnBasePage.closedCaptionPresent();

        sa.assertAll();
    }

    @Test(description = "Live Video Full Screen Mode", dependsOnMethods = "testLiveVideoSettings",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9210")
    public void testLiveVideoFullScreen () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.videoEnterFullScreen();
        LOGGER.info("Full Screen = " + espnBasePage.getVideoExitFullScreenButton().getSize());
        espnBasePage.videoExitFullScreen();
        sa.assertTrue(espnBasePage.getVideoEnterFullScreenButton().isElementPresent(), "Video is NOT back to default");

        sa.assertAll();
    }

    @Test(description = "Live Video Scrubber Function", dependsOnMethods = "testLiveVideoFullScreen",  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9211")
    public void testLiveVideoScrubberFunction () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.scrubberSeekLiveVideo();

        sa.assertAll();
    }
}
