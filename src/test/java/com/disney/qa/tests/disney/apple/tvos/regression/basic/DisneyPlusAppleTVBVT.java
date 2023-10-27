package com.disney.qa.tests.disney.apple.tvos.regression.basic;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.api.disney.DisneyContentIds.END_GAME;

public class DisneyPlusAppleTVBVT extends DisneyPlusAppleTVBaseTest {

    //Test constants
    private static final String KIDS = "Kids";
    private static final String TEST = "Test";
    private static final String KIDS_DOB = "2018-01-01";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106585"})
    @Test(description = "tvOS Basic Verification Test", groups = {"BVT"})
    public void tvOSBVT() {
        DisneyPlusAppleTVWelcomeScreenPage welcome = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVMoviesPage moviesPage = new DisneyPlusAppleTVMoviesPage(getDriver());
        DisneyPlusAppleTVSeriesPage seriesPage = new DisneyPlusAppleTVSeriesPage(getDriver());
        DisneyPlusAppleTVOriginalsPage originalsPage = new DisneyPlusAppleTVOriginalsPage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount user = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        disneyAccountApi.addProfile(user, KIDS, KIDS_DOB, user.getProfileLang(), null, true, true);
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(welcome.isOpened(), "Welcome screen did not launch");

        logInWithoutHomeCheck(user);
        sa.assertTrue(whoIsWatching.isOpened(), "Who's Watching not displayed");
        whoIsWatching.clickProfile(TEST);
        if (homePage.isGlobalNavExpanded()) {
            LOGGER.warn("Menu was opened before landing. Closing menu.");
            homePage.clickSelect();
        }
        sa.assertTrue(homePage.isOpened(), "Home page not displayed");

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH.getText());
        sa.assertTrue(searchPage.isOpened(), "Search page not displayed");

        searchPage.typeInSearchField("endgame");
        searchPage.clickSearchResult(END_GAME.getTitle());
        sa.assertTrue(detailsPage.isOpened(), "Details page not displayed");
        detailsPage.clickPlayButton();
        videoPlayerPage.waitForVideoToStart();
        sa.assertTrue(videoPlayerPage.isOpened(), "Video player view not displayed.");

        videoPlayerPage.clickMenuTimes(1, 1);
        detailsPage.isOpened();
        detailsPage.clickWatchlistButton();
        sa.assertTrue(detailsPage.doesContinueButtonExist(), "Continue (play) button not displayed");

        detailsPage.clickMenuTimes(2,1);
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        sa.assertTrue(watchListPage.isOpened());
        sa.assertTrue(watchListPage.areWatchlistTitlesDisplayed(END_GAME.getTitle()));

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.MOVIES.getText());
        sa.assertTrue(moviesPage.isOpened());

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.SERIES.getText());
        sa.assertTrue(seriesPage.isOpened(), "Series page not displayed");

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.ORIGINALS.getText());
        sa.assertTrue(originalsPage.isOpened(), "Originals page not displayed");

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.SETTINGS.getText());
        sa.assertTrue(settingsPage.isOpened(), "Settings Page not displayed");

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.PROFILE.getText());
        whoIsWatching.isOpened();
        whoIsWatching.clickProfile(KIDS);
        if (!homePage.isGlobalNavPresent()) {
            homePage.openGlobalNavWithClickingMenu();
        }
        sa.assertTrue(homePage.isProfileNamePresent(KIDS), "Profile 'Kids' not present");
        sa.assertAll();
    }
}
