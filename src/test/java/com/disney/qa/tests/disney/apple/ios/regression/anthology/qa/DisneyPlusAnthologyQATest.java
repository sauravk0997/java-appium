package com.disney.qa.tests.disney.apple.ios.regression.anthology.qa;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLiveEventModalIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;

public class DisneyPlusAnthologyQATest extends DisneyBaseTest {

    //Test constants
    private static final String UPCOMING = "UPCOMING";
    private static final String DANCING_WITH_THE_STARS = "Dancing with the Stars";
    private static final String LIVE = "LIVE";
    private static final String PLAY = "PLAY";
    //private static final String WATCH_LIVE = "Watch Live";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72301"})
    @Test(description = "Verify Anthology Series - Upcoming Badge and Metadata", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadgeAndMetadata() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        SoftAssert sa = new SoftAssert();

//        setAppToHomeScreen(disneyAccount.get());
        QALogin();
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> detailsPage.isStaticTextLabelPresent(UPCOMING));
        } catch (Exception e) {
            skipExecution("Skipping test, "+ UPCOMING + " label not found." + e.getMessage());
        }

        sa.assertTrue(detailsPage.isContentDetailsPagePresent(), "Details page did not open.");
        sa.assertTrue(detailsPage.getAiringBadgeLabel().isElementPresent(), "Airing badge label is not displayed.");
        sa.assertTrue(detailsPage.getUpcomingDateTime().isElementPresent(), "Upcoming Date and Time was not found.");
        sa.assertTrue(detailsPage.getUpcomingTodayBadge().isElementPresent() || detailsPage.getUpcomingBadge().isElementPresent(),
                "Upcoming Today / Upcoming badge is not present");
        sa.assertTrue(detailsPage.getAiringBadgeLabel().isElementPresent(), "Upcoming airing Badge is not present.");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Logo image is not displayed.");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero image is not displayed.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72300"})
    @Test(description = "Verify Anthology Series - Live Badge and Airing Indicator", groups = {"Anthology"})
    public void verifyAnthologyLiveBadge() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

//        setAppToHomeScreen(disneyAccount.get());
        QALogin();
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            skipExecution("Skipping test, "+ LIVE + " label not found, no live content playing." + e.getMessage());
        }

        details.validateLiveProgress(sa);
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not found.");
        sa.assertTrue(details.doesAiringBadgeContainLive(), "Airing badge does not contain live badge on Details Page");

        details.clickQAWatchButton();
        liveEventModal.isOpened();
        sa.assertTrue(liveEventModal.doesAiringBadgeContainLive(), "Airing badge does not contain Live badge on Live Event Modal");
        liveEventModal.validateLiveProgress(sa);
        liveEventModal.clickThumbnailView();
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72299"})
    @Test(description = "Verify Anthology Series - Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLivePlayback() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

//        setAppToHomeScreen(disneyAccount.get());
        QALogin();
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            skipExecution("Skipping test, "+ LIVE + " label not found, no live content playing." + e.getMessage());
        }

        details.clickQAWatchButton();
        liveEventModal.isOpened();
        liveEventModal.getQAWatchLiveButton().click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertTrue(videoPlayer.isYouAreLiveButtonPresent(), "'You are live' button was not found");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73876"})
    @Test(description = "Verify Anthology Series - Ended, Compare episode number", groups = {"Anthology"})
    public void verifyAnthologyEnded() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

//        setAppToHomeScreen(disneyAccount.get());
        QALogin();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> detailsPage.isQAWatchButtonPresent());
        } catch (Exception e) {
            skipExecution("Skipping test, Watch button not found, no live content airing." + e.getMessage());
        }

        Assert.assertFalse(detailsPage.compareEpisodeNum(), "Expected: Current episode number does not match new episode number.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72654"})
    @Test(description = "Verify Anthology Series - No Group Watch During Live Event", groups = {"Anthology"})
    public void verifyAnthologyNoGroupWatchLive() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);

//        setAppToHomeScreen(disneyAccount.get());
        QALogin();
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.isQAWatchButtonPresent());
        } catch (Exception e) {
            skipExecution("Skipping test, Watch button not found, no live content playing." + e.getMessage());
        }

        Assert.assertFalse(details.isGroupWatchButtonDisplayed(), "Group Watch was found during live event.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72297"})
    @Test(description = "Verify Anthology Series - Live Modal", groups = {"Anthology"})
    public void verifyAnthologyLiveModal() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
//        setAppToHomeScreen(disneyAccount.get());
        QALogin();
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.isQAWatchButtonPresent());
        } catch (Exception e) {
            skipExecution("Skipping test, Watch button not found. " + e.getMessage());
        }

        details.clickQAWatchButton();
        liveEventModal.isOpened();
        System.out.println(getDriver().getPageSource());
//        sa.assertTrue(liveEventModal.isTitleLabelPresent(), "Title label not found.");
//        sa.assertTrue(liveEventModal.isSubtitleLabelPresent(), "Subtitle label is not present.");
//        sa.assertTrue(liveEventModal.isThumbnailViewPresent(), "Thumbnail view is not present.");
//        sa.assertTrue(liveEventModal.getQAWatchLiveButton().isPresent(), "Watch live button is not present.");
//        sa.assertTrue(liveEventModal.getQAWatchFromStartButton().isPresent(), "Watch from start button is not present.");

        videoPlayer.compareQAWatchLiveToWatchFromStartTimeRemaining(sa);
        sa.assertAll();
    }

    private void searchAndOpenDWTSDetails() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        homePage.clickSearchIcon();
        search.searchForMedia(DANCING_WITH_THE_STARS);
        search.getDisplayedTitles().get(0).click();
        details.isContentDetailsPagePresent();
    }

    private void QALogin() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);

        welcomePage.isOpened();
        welcomePage.clickLogInButton();
        loginPage.submitEmail("cristina.solmaz+43755@disneyplustesting.com");
        passwordPage.submitPasswordForLogin("G0Disney!");
        homePage.isOpened();
    }
}

