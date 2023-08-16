package com.disney.qa.tests.disney.apple.tvos.regression.anthology;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.api.disney.DisneyContentIds.DANCING_WITH_THE_STARS;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;

public class DisneyPlusAppleTVAnthologyTest extends DisneyPlusAppleTVBaseTest {

    //Test constants
    private static final String UPCOMING = "UPCOMING";
    private static final String LIVE = "LIVE";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106662"})
    @Test(description = "Verify Anthology Upcoming", groups = {"Anthology"})
    public void verifyAnthologyWatchlist() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        logIn(entitledUser);

        searchForDWTS();
        detailsPage.addToWatchlist();
        detailsPage.clickMenuTimes(1,1);
        pause(1); //from transition to search bar
        searchPage.clickMenuTimes(1,1);
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        sa.assertTrue(watchListPage.areWatchlistTitlesDisplayed(DANCING_WITH_THE_STARS.getTitle()), "Dancing With The Stars was not added to watchlist.");

        watchListPage.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).click();
        sa.assertTrue(detailsPage.isOpened(), DANCING_WITH_THE_STARS.getTitle() + " details page did not load.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106657"})
    @Test(description = "Verify Anthology Search", groups = {"Anthology"})
    public void verifyAnthologySearch() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());

        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        logIn(entitledUser);

        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.isOpened();
        searchPage.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        sa.assertTrue(searchPage.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).isElementPresent(), "Dancing with the Stars title not found in search results.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105996"})
    @Test(description = "Verify Anthology Series - Upcoming Badge and Metadata", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadgeAndMetadata() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        logIn(entitledUser);
        searchForDWTS();

        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
//        sa.assertTrue(detailsPage.getAiringBadgeLabel().isElementPresent(), "Airing badge label is not displayed.");
//        sa.assertTrue(detailsPage.getUpcomingDateTime().isElementPresent(), "Upcoming Date and Time was not found.");
//        sa.assertTrue(detailsPage.getUpcomingTodayBadge().isElementPresent() || detailsPage.getUpcomingBadge().isElementPresent(),
//                "Upcoming Today / Upcoming badge is not present");
//        sa.assertTrue(detailsPage.getAiringBadgeLabel().isElementPresent(), "Upcoming airing Badge is not present.");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Logo image is not displayed.");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero image is not displayed.");
        sa.assertAll();
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106001"})
    @Test(description = "Verify Anthology Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLivePlayback() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModalPage = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        logIn(entitledUser);

        searchForDWTS();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> detailsPage.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }
        detailsPage.clickWatchButton();
        liveEventModalPage.clickWatchLiveButton();
        videoPlayerPage.waitForVideoToStart();
        sa.assertTrue(videoPlayerPage.isOpened(), "Live video is not playing");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105997"})
    @Test(description = "Verify Anthology Live Badge", groups = {"Anthology"})
    public void verifyAnthologyLiveBadge() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModalPage = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        logIn(entitledUser);


        searchForDWTS();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> detailsPage.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }
        sa.assertTrue(detailsPage.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Details Page");
        detailsPage.clickWatchButton();
        sa.assertTrue(liveEventModalPage.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Live Event Modal");
        liveEventModalPage.clickDetailsButton();
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110033"})
    @Test(description = "Verify Anthology Ended, Compare episode number", groups = {"Anthology"})
    public void verifyAnthologyEnded() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        logIn(entitledUser);

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        searchPage.clickSearchResult(DANCING_WITH_THE_STARS.getTitle());
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> detailsPage.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }
        sa.assertFalse(detailsPage.compareEpisodeNum(), "Episode number are the same");
        sa.assertAll();
    }

    private void searchForDWTS() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.isOpened();
        searchPage.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        searchPage.clickSearchResult(DANCING_WITH_THE_STARS.getTitle());
        detailsPage.isOpened();
    }
}
