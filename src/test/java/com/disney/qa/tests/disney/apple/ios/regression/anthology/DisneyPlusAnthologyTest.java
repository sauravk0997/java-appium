package com.disney.qa.tests.disney.apple.ios.regression.anthology;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

public class DisneyPlusAnthologyTest extends DisneyBaseTest {

    //Test constants
    private static final String UPCOMING = "UPCOMING";
    private static final String DANCING_WITH_THE_STARS = "Dancing with the Stars";
    private static final String LIVE = "LIVE";
    private static final String WATCH_LIVE = "Watch Live";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72640"})
    @Test(description = "Verify Anthology Series - Search", groups = {"Anthology"})
    public void verifyAnthologySearch() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        String[] firstDisplayTitle = searchPage.getDisplayedTitles().get(0).getText().split(",");
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DANCING_WITH_THE_STARS + " details page did not open.");
        sa.assertTrue(firstDisplayTitle[0].equalsIgnoreCase(detailsPage.getMediaTitle()), "Search result title does not match Details page media title.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72646"})
    @Test(description = "Verify Anthology Series - Upcoming", groups = {"Anthology"})
    public void verifyAnthologyWatchlist() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        String mediaTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(mediaTitle), "Media title was not added.");
        moreMenu.getDynamicCellByLabel(mediaTitle).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72301"})
    @Test(description = "Verify Anthology Series - Upcoming", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadge() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> detailsPage.isStaticTextLabelPresent(UPCOMING));
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ UPCOMING + " label not found. " + e);
        }

        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
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
    @Test(description = "Verify Anthology Live Badge and Airing Indicator", groups = {"Anthology"})
    public void verifyAnthologyLiveBadge() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found. " + e);
        }

        sa.assertTrue(details.getLiveNowBadge().isElementPresent(), "Live Now badge was not found.");
        sa.assertTrue(details.getLiveProgress().isElementPresent() || details.getLiveProgressTime().isElementPresent(),
                "Live progress indicator not found.");
        sa.assertTrue(details.doesAiringBadgeContainLive(), "Airing badge does not contain live badge on Details Page");

        details.clickWatchButton();
        liveEventModal.isOpened();
        sa.assertTrue(liveEventModal.doesAiringBadgeContainLive(), "Airing badge does not contain Live badge on Live Event Modal");
        liveEventModal.clickThumbnailView();
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72299"})
    @Test(description = "Verify Anthology Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLivePlayback() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found. " + e);
        }

        details.clickWatchButton();
        liveEventModal.isOpened();
        liveEventModal.getWatchLiveButton().click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertTrue(videoPlayer.isYouAreLiveButtonPresent(), "'You are live' button was not found");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73876"})
    @Test(description = "Verify Anthology Series - Ended, Episode number different", groups = {"Anthology"})
    public void verifyAnthologyEnded() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> detailsPage.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }
        Assert.assertFalse(detailsPage.compareEpisodeNum(), "Expected: Current episode number does not match new episode number.");
    }

    private void searchAndOpenDWTSDetails() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        homePage.clickSearchIcon();
        search.searchForMedia(DANCING_WITH_THE_STARS);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
    }
}

