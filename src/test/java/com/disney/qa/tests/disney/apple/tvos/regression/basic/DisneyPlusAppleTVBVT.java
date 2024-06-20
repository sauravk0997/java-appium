package com.disney.qa.tests.disney.apple.tvos.regression.basic;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.api.disney.DisneyContentIds.END_GAME;

public class DisneyPlusAppleTVBVT extends DisneyPlusAppleTVBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount user = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(user).profileName(KIDS).dateOfBirth(KIDS_DOB).language(user.getProfileLang()).avatarId(null).kidsModeEnabled(true).isStarOnboarded(true).build());
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(welcome.isOpened(), "Welcome screen did not launch");

        logInWithoutHomeCheck(user);
        sa.assertTrue(whoIsWatching.isOpened(), "Who's Watching not displayed");
        whoIsWatching.clickProfile(TEST);
        collapseGlobalNav();
        sa.assertTrue(homePage.isOpened(), "Home page not displayed");

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH.getText());
        sa.assertTrue(searchPage.isOpened(), "Search page not displayed");

        searchPage.typeInSearchField("endgame");
        searchPage.clickSearchResult(END_GAME.getTitle());
        sa.assertTrue(detailsPage.isOpened(), "Details page not displayed");
        detailsPage.clickPlayButton();
        commonPage.goRightOnPlayerForDuration(3, 3, 30);
        sa.assertTrue(videoPlayerPage.isOpened(), "Video player view not displayed.");

        videoPlayerPage.clickMenuTimes(1, 1);
        detailsPage.isOpened();
        detailsPage.clickWatchlistButton();
        //TODO: TVOS-4096 - App bug play button does not transition to continue button
//        sa.assertTrue(detailsPage.doesContinueButtonExist(), "Continue (play) button not displayed");

        detailsPage.clickMenuTimes(2,1);
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        //TODO: watchlist bug - watchlist accessibility id currently set as "placeholder accessibility title label"
//        sa.assertTrue(watchListPage.isOpened(), "Watchlist page not displayed.");
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
