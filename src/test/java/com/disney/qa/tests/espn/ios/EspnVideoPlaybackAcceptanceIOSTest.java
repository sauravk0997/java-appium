package com.disney.qa.tests.espn.ios;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchEspnPlusIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchVideoPlayerIOSPageBase;
import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.core.foundation.report.testrail.TestRailCases;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class EspnVideoPlaybackAcceptanceIOSTest extends BaseMobileTest {

    private static final String EXPECTED = "Expected: ";


    @TestRailCases(testCasesId = "1484567")
    @Test(description = "Subscribed User goes into Video Player for VOD")
    public void testVideoPlayerForSubscribedUser() {

        SoftAssert softAssert = new SoftAssert();

        EspnIOSPageBase espnIOSPageBase = initPage(EspnIOSPageBase.class);

        espnIOSPageBase.logInNavigateToWatchPage("QA");

        EspnWatchEspnPlusIOSPageBase espnWatchEspnPlusIOSPageBase =
                initPage(        EspnWatchEspnPlusIOSPageBase.class);

        espnWatchEspnPlusIOSPageBase.playFirstVideo();

        softAssert.assertTrue(initPage(EspnWatchVideoPlayerIOSPageBase.class).isOpened(),
                EXPECTED + "Video player should be opened");

        softAssert.assertAll();

    }




    @TestRailCases(testCasesId = "1484569")
    @Test(description = "Playback Locaitons are Stored for VODs")
    public void testVodPlaybackLocation() {

        SoftAssert softAssert = new SoftAssert();

        EspnIOSPageBase espnIOSPageBase = initPage(EspnIOSPageBase.class);

        espnIOSPageBase.logInNavigateToWatchPage("QA");

        EspnWatchEspnPlusIOSPageBase espnWatchEspnPlusIOSPageBase =
                initPage(EspnWatchEspnPlusIOSPageBase.class);

        espnWatchEspnPlusIOSPageBase.playFirstReplay();

        EspnWatchVideoPlayerIOSPageBase espnWatchVideoPlayerIOSPageBase =
                initPage(EspnWatchVideoPlayerIOSPageBase.class);

        espnWatchVideoPlayerIOSPageBase.watchVideo(10);

        espnWatchVideoPlayerIOSPageBase.pauseVideo();

        initPage(EspnIOSPageBase.class).getTab(EspnIOSPageBase.TabOptions.WATCH);

        espnWatchEspnPlusIOSPageBase.playFirstReplay();

        espnWatchVideoPlayerIOSPageBase.pauseVideo();

        softAssert.assertAll();

    }
}
