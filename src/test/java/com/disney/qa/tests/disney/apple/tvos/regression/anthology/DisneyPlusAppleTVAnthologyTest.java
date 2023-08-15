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
    public void verifyAnthologyUpcoming() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
//        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
//        logIn(entitledUser);
        signInQA();

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        searchPage.clickSearchResult(DANCING_WITH_THE_STARS.getTitle());
        detailsPage.isAnthologyTitlePresent();
        detailsPage.addToWatchlist();
        detailsPage.clickMenuTimes(1,1);
        pause(1); //from transition to search bar
        searchPage.clickMenuTimes(1,1);
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        sa.assertTrue(watchListPage.areWatchlistTitlesDisplayed(DANCING_WITH_THE_STARS.getTitle()), "Dancing With The Stars was not added to watchlist.");
        System.out.println(getDriver().getPageSource());
        watchListPage.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).click();
        sa.assertTrue(detailsPage.isOpened(), DANCING_WITH_THE_STARS.getTitle() + " details page did not load.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106657"})
    @Test(description = "Verify Anthology Search", groups = {"Anthology"})
    public void verifyAnthologySearch() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusApplePageBase applePage = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());

        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
//        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
//        logIn(entitledUser);
        signInQA();

        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.isOpened();
        searchPage.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        sa.assertTrue(searchPage.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).isElementPresent(), "Dancing with the Stars title not found in search results.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106001"})
    @Test(description = "Verify Anthology Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLiveModalAndPlayback() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModalPage = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
//        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
//        logIn(entitledUser);
        signInQA();

        homePage.checkIfElementAttributeFound(homePage.getStaticTextByLabel(LIVE), "enabled");
        homePage.clickAnthologyCarousel(LIVE);
        liveEventModalPage.isOpened();
        sa.assertTrue(liveEventModalPage.isWatchFromStartPresent(), "Watch from start is not present");
        sa.assertTrue(liveEventModalPage.isWatchLiveButtonPresent(), "Watch Live button not present");
        liveEventModalPage.clickDetailsButton();
        sa.assertTrue(detailsPage.isWatchButtonPresent(), "Watch button is not present.");
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
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
//        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
//        logIn(entitledUser);
        signInQA();

        homePage.checkIfElementAttributeFound(homePage.getStaticTextByLabel(LIVE), "enabled");
        sa.assertTrue(homePage.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Home");
        homePage.clickAnthologyCarousel(LIVE);
        sa.assertTrue(liveEventModalPage.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Live Event Modal");
        liveEventModalPage.clickDetailsButton();
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Details Page");
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
//        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
//        logIn(entitledUser);
        signInQA();

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

    private void signInQA() {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        welcomePage.isOpened();
        welcomePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.proceedToLocalizedPasswordScreen("cristina.solmaz+4375@disneyplustesting.com");
        passwordPage.logInWithPasswordLocalized("G0Disney!");
        whoIsWatchingPage.clickProfile("Test");
    }
}
