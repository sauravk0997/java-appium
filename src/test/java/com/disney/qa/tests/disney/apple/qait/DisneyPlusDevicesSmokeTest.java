package com.disney.qa.tests.disney.apple.qait;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAudioSubtitleIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;


public class DisneyPlusDevicesSmokeTest extends DisneyBaseTest {

    protected static final String BLUEY_SERIES = "Bluey";

    @Test(groups = {TestGroup.QAIT, TestGroup.PRE_CONFIGURATION})
    public void testWelcomePage() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());

        softAssert.assertTrue(isFooterTabPresent(DisneyPlusApplePageBase.FooterTabs.HOME),
                "Expected: Home button should be present in nav bar");

        disneyPlusHomeIOSPageBase.getHomeNav().click();

        softAssert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
                "Expected: Home Screen should be opened");

        softAssert.assertAll();
    }

    @Test(groups = {TestGroup.QAIT, TestGroup.PRE_CONFIGURATION})
    public void testPlayback() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusHomeIOSPageBase.getSearchNav().click();
        disneyPlusSearchIOSPageBase.searchForMedia(BLUEY_SERIES);
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();
        disneyPlusDetailsIOSPageBase.clickPlayButton();
        pause(10);
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isOpened(), "Video Player did not launch");
        sa.assertAll();
    }
}
