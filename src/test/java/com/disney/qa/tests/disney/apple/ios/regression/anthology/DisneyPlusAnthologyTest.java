package com.disney.qa.tests.disney.apple.ios.regression.anthology;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusWhoseWatchingIOSPage;
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72646"})
    @Test(description = "Verify Anthology Series - Upcoming", groups = {"Anthology"})
    public void verifyAnthologyWatchlist() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        homePage.clickSearchIcon();
        search.searchForMedia(DANCING_WITH_THE_STARS);
        search.getDisplayedTitles().get(0).click();
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"xxxxxxxxxx"})
    @Test(description = "Verify Anthology Series - Upcoming", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadge() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);

        SoftAssert sa = new SoftAssert();

//        setAppToHomeScreen(disneyAccount.get());
        signInQA();
        homePage.isOpened();
        homePage.clickSearchIcon();
        search.searchForMedia(DANCING_WITH_THE_STARS);
        search.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        System.out.println(getDriver().getPageSource());
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> detailsPage.isStaticTextLabelPresent(UPCOMING));

        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ UPCOMING + " label not found. " + e);
        }
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertTrue(detailsPage.isStaticTextLabelPresent(UPCOMING), "Upcoming badge was not found.");
        sa.assertAll();
    }

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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72300"})
    @Test(description = "Verify Anthology Live Badge", groups = {"Anthology"})
    public void verifyAnthologyLiveBadge() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModalPage = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

//        setAppToHomeScreen(disneyAccount.get());
        signInQA();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        System.out.println(detailsPage.doesAiringBadgeContainLive());
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> detailsPage.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found. " + e);
        }
        sa.assertTrue(detailsPage.doesAiringBadgeContainLive(), "Airing badge does not contain live badge on Details Page");
//        detailsPage.clickWatchButton();
//        liveEventModalPage.isOpened();
//        sa.assertTrue(liveEventModalPage.doesAiringBadgeContainLive(), "Airing badge does not contain Live badge on Live Event Modal");
//        liveEventModalPage.clickThumbnailView();
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72299"})
    @Test(description = "Verify Anthology Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLiveIndicatorAndPlayback() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

//        setAppToHomeScreen(disneyAccount.get());
        signInQA();
        homePage.clickSearchIcon();
        search.searchForMedia(DANCING_WITH_THE_STARS);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found. " + e);
        }
        sa.assertTrue(details.getLiveProgress().isElementPresent() || details.getLiveProgressTime().isElementPresent(), "Live progress indicator not found.");
        details.clickWatchButton();
        liveEventModal.isOpened();
        liveEventModal.getWatchLiveButton().click();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isYouAreLiveButtonPresent(), "'You are live' button was not found");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73876"})
    @Test(description = "Verify Anthology Series - w", groups = {"Anthology"})
    public void verifyAnthologyEnded() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

//        setAppToHomeScreen(disneyAccount.get());
        signInQA();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        System.out.println(getDriver().getPageSource());
        try {
//            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> detailsPage.isWatchButtonPresent());

            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> detailsPage.getDynamicAccessibilityId("PLAY").isPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }
        Assert.assertFalse(detailsPage.compareEpisodeNum(), "Expected: Current episode number does not match new episode number.");
    }

    private void signInQA() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        handleAlert();
        welcomePage.clickLogInButton();
        loginPage.clickPrimaryButton();
        loginPage.submitEmail("cristina.solmaz+4375@disneyplustesting.com");
        loginPage.clickPrimaryButton();
        passwordPage.submitPasswordForLogin("G0Disney!");
        whoseWatchingPage.clickProfile("Test");
        homePage.isOpened();
    }
}

