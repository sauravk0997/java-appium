package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusUpNextIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.PlayerControl;
import com.disney.util.TestGroup;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class DisneyPlusVideoUpNextTest  extends DisneyBaseTest {

    //Test constants
    protected static final String  SHORT_SERIES = "Bluey";
    private static final double PLAYER_PERCENTAGE_FOR_UP_NEXT = 85;
    private static final double PLAYER_PERCENTAGE_FOR_AUTO_PLAY = 95;
    private static final double PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT = 50;

    @DataProvider(name = "autoplay-state")
    public Object[][] autoplayState(){
        return new Object[][] {{"ON"}, {"OFF"}};
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61973"})
    @Test(description = "Verify Up Next UI", groups = {"Video Player", TestGroup.PRE_CONFIGURATION})
    @Maintainer("gkrishna1")
    public void verifyUpNextUI() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        sa.assertTrue(disneyPlusUpNextIOSPageBase.verifyUpNextUI(), "Up Next UI was not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61975"})
    @Test(description = "User Taps play icon", groups = {"Video Player", TestGroup.PRE_CONFIGURATION })
    @Maintainer("gkrishna1")
    public void verifyPlayIconOnUpNextUI() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        //Login
        setAppToHomeScreen(getAccount());
        //Turn off autoplay
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.toggleAutoplay(getAccount().getFirstName(), "OFF");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        //Search and forward the content
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        String nextEpisodesTitle = disneyPlusUpNextIOSPageBase.getNextEpisodeInfo();
        disneyPlusUpNextIOSPageBase.tapPlayIconOnUpNext();
        //Verify that the next episode has started playing
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.doesTitleExists(nextEpisodesTitle),"Next episode didn't play");
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isElementPresent(PlayerControl.PAUSE),"Pause button is not visible on player view");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61979"})
    @Test(description = "User Taps See All Episodes", groups = {"Video Player", TestGroup.PRE_CONFIGURATION })
    @Maintainer("gkrishna1")
    public void verifyUpNextSeeAllEpisodes() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        disneyPlusUpNextIOSPageBase.tapSeeAllEpisodesButton();
        sa.assertTrue(disneyPlusDetailsIOSPageBase.isOpened(),"Tapping on 'See all episodes' didn't take to details page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61981"})
    @Test(description = "User allows autoplay to occur", groups = {"Video Player", TestGroup.PRE_CONFIGURATION })
    @Maintainer("gkrishna1")
    public void verifyAutoPlayOnPlayerView() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Turn ON autoplay
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.toggleAutoplay(getAccount().getFirstName(), "ON");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        //Bring up upnext UI
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        String nextEpisodesTitle = disneyPlusUpNextIOSPageBase.getNextEpisodeInfo();
        //Wait for upnext UI to disappear
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToDisappear();
        //Verify that the next episode has started playing
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.doesTitleExists(nextEpisodesTitle),"Next episode didn't play");
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isElementPresent(PlayerControl.PAUSE),"Pause button is not visible on player view");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62005"})
    @Test(description = "Up Next Logic -Extra content", groups = {"Video Player", TestGroup.PRE_CONFIGURATION })
    @Maintainer("gkrishna1")
    public void VerifyUpNextLogicForExtraContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Search for a series having 'Extras'
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia("The Biggest Little Farm");
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.clickExtrasTab();

        //Initiate playback for "Extra" content from details page
        detailsPage.tapOnFirstContentTitle();
        disneyPlusVideoPlayerIOSPageBase.isOpened();

        //Verify that up next UI doesn't appear on player view
        disneyPlusVideoPlayerIOSPageBase.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT);
        sa.assertFalse(disneyPlusUpNextIOSPageBase.isUpNextViewPresent(),"Up Next UI is shown for extra content");

        //verify that details page is opened once playback is complete.
        sa.assertTrue(detailsPage.isOpened(disneyPlusVideoPlayerIOSPageBase.getRemainingTime()),
                "Control didn't return to the Detail page after 'Extra' content finished playing");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62003"})
    @Test(description = "Up Next Behavior when app gets backgrounded", groups = {"Video Player", TestGroup.PRE_CONFIGURATION}, dataProvider = "autoplay-state")
    @Maintainer("gkrishna1")
    public void verifyUpNextBehaviorWhenAppIsBackgrounded(String autoplayState) {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Turn ON autoplay
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.isOpened();
        editProfile.toggleAutoplay(getAccount().getFirstName(), autoplayState);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        //Bring up upNext UI
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_UP_NEXT);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        sa.assertTrue(disneyPlusUpNextIOSPageBase.verifyUpNextUI(), "Up Next UI was not displayed");
        //This will lock the device for 5 seconds then unlock it
        lockDevice(Duration.ofSeconds(5));
        //After backgrounding the app, video should be paused, and we should see upNext UI
        sa.assertTrue(disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear(), "Up Next UI was not displayed");
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.verifyVideoPaused(),"Play button is not visible on player view, " +
                "video not paused");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61993", "XMOBQA-61165", "XMOBQA-61271"})
    @Test(description = "Autoplay does not autoplay if disabled", groups = {"Video Player", TestGroup.PRE_CONFIGURATION })
    @Maintainer("gkrishna1")
    public void verifyAutoplayDoesNotAutoplayWhenDisabled() {
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Turn ON autoplay
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.toggleAutoplay(getAccount().getFirstName(), "OFF");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);
        //Forward the content
        initiatePlaybackAndScrubOnPlayer(SHORT_SERIES, PLAYER_PERCENTAGE_FOR_AUTO_PLAY);
        int remainingTime = disneyPlusVideoPlayerIOSPageBase.getRemainingTime();
        pause(remainingTime);
        sa.assertTrue(disneyPlusUpNextIOSPageBase.verifyUpNextUI(), "Up Next UI was not displayed");
        //TODO:https://jira.disneystreaming.com/browse/IOS-6617
        //uncomment below line when the bug is resolved
        /*sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isElementPresent(PlayerControl.PAUSE),"Pause button is not visible on player view, " +
                "video not paused when autoplay is OFF");*/
        sa.assertTrue(disneyPlusUpNextIOSPageBase.getNextEpisodeInfo().equalsIgnoreCase("S1:E2 Hospital"), "Next season title is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62519"})
    @Test(description = "Details Page - Bookmarks - Visual Progress Bar - Update after user watches content", groups = {"Video Player", TestGroup.PRE_CONFIGURATION })
    @Maintainer("hpatel7")
    public void verifyProgressBarForAfterUserWatchesContent() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());

        initiatePlaybackAndScrubOnPlayer("Spiderman 3", PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT);
        disneyPlusVideoPlayerIOSPageBase.clickPauseButton();

        int remainingTimeInMinutes = disneyPlusVideoPlayerIOSPageBase.getRemainingTime();
        disneyPlusVideoPlayerIOSPageBase.clickBackButton();

        long hours = remainingTimeInMinutes/60;
        long minutes = remainingTimeInMinutes %  60;
        String durationTime = String.format("%dh %dm",hours, minutes);

        sa.assertTrue(detailsPage.isOpened(), "Detail Page did not open");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button not present after exiting playback");
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Progress bar is not present after exiting playback");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().isPresent(), "Continue watching time remaining is not present");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().getText().contains(durationTime), "Correct remaining time is not reflecting in progress bar");

        detailsPage.clickContinueButton();
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isOpened(), "Video player Page is not opened");
        disneyPlusVideoPlayerIOSPageBase.scrubToPlaybackPercentage(100);
        disneyPlusVideoPlayerIOSPageBase.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Detail Page did not open");
        sa.assertFalse(detailsPage.isContinueButtonPresent(), "Continue button present after completing playback");
        sa.assertFalse(detailsPage.isProgressBarPresent(), "Progress bar is present after completing playback");
        sa.assertAll();
    }

    private void initiatePlaybackAndScrubOnPlayer(String content, double percentage) {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusHomeIOSPageBase.getSearchNav().click();
        disneyPlusSearchIOSPageBase.searchForMedia(content);
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();
        disneyPlusDetailsIOSPageBase.clickPlayButton().isOpened();
        disneyPlusVideoPlayerIOSPageBase.clickPauseButton();
        disneyPlusVideoPlayerIOSPageBase.scrubToPlaybackPercentage(percentage);
        disneyPlusVideoPlayerIOSPageBase.clickPlayButton();
    }
}
