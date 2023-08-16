package com.disney.qa.tests.disney.apple.ios.regression.anthology;

import com.disney.qa.common.utils.IOSUtils;
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
    private static final String DANCING_WITH_THE_STARS = "Dancing With The Stars";
    private static final String LIVE = "LIVE";
    private static final String WATCH_LIVE = "Watch Live";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72640", "XMOBQA-72646"})
    @Test(description = "Verify Anthology Series - Upcoming", groups = {"Anthology"})
    public void verifyAnthologyUpcoming() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> homePage.isStaticTextLabelPresent(UPCOMING));
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ UPCOMING + " label not found. " + e);
        }
        new IOSUtils().clickNearElement(homePage.getStaticTextByLabelContains(UPCOMING), 0.5, 30);
        String mediaTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(mediaTitle), "Media title was not added");
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

        setAppToHomeScreen(disneyAccount.get());
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> homePage.isStaticTextLabelPresent(LIVE));
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found. " + e);
        }
        sa.assertTrue(homePage.doesAiringBadgeContainLive(), "Airing badge does not contain Live badge on Home");
        new IOSUtils().clickNearElement(homePage.getStaticTextByLabelContains(LIVE), 0.5, 30);
        sa.assertTrue(liveEventModalPage.doesAiringBadgeContainLive(), "Airing badge does not contain Live badge on Live Event Modal");
        liveEventModalPage.clickThumbnailView();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.doesAiringBadgeContainLive(), "Airing badge does not contain live badge on Details Page");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72299"})
    @Test(description = "Verify Anthology Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLivePlayback() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        try {
            fluentWaitNoMessage(getCastedDriver(), 200, 20).until(it -> homePage.isStaticTextLabelPresent(LIVE));
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found. " + e);
        }
        new IOSUtils().clickNearElement(homePage.getStaticTextByLabelContains(LIVE), 0.5, 30);
        homePage.getStaticTextByLabel(WATCH_LIVE).click();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isYouAreLiveButtonPresent(), "'You are live' button was not found");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72303"})
    @Test(description = "Verify Anthology Series - Ended", groups = {"Anthology"})
    public void verifyAnthologyEnded() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        searchPage.getDisplayedTitles().get(0).click();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> detailsPage.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }
        Assert.assertFalse(detailsPage.compareEpisodeNum(), "Expected: Current episode number does not match new episode number.");
    }
}

