package com.disney.qa.tests.disney.windows10;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.windows10.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyWindowsPrivacyAndComplianceTest extends DisneyWindowsBaseTest {

    private static final String errorMessageGlobalNav = "Did not land on right screen after global menu navigation: %s";
    DisneyWindowsWelcomePage disneyWindowsWelcomePage;
    DisneyWindowsHomePage disneyWindowsHomePage;
    DisneyWindowsAccountPage disneyWindowsAccountPage;
    DisneyWindowsHelpPage disneyWindowsHelpPage;
    DisneyWindowsLegalPage disneyWindowsLegalPage;
    DisneyWindowsMoviesPage disneyWindowsMoviesPage;
    DisneyWindowsOriginalsPage disneyWindowsOriginalsPage;
    DisneyWindowsSearchPage disneyWindowsSearchPage;
    DisneyWindowsSeriesPage disneyWindowsSeriesPage;
    DisneyWindowsSettingsPage disneyWindowsSettingsPage;
    DisneyWindowsWatchlistPage disneyWindowsWatchlistPage;
    DisneyWindowsWhoseWatchingPage disneyWindowsWhoseWatchingPage;
    DisneyWindowsCommonPage disneyWindowsCommonPage;
    DisneyWindowsDetailsPage disneyWindowsDetailsPage;
    DisneyWindowsVideoPlayerPage disneyWindowsVideoPlayerPage;

    @Test
    public void privacyAndComplianceTest() {
        disneyWindowsWelcomePage = new DisneyWindowsWelcomePage(getDriver());
        disneyWindowsHomePage = new DisneyWindowsHomePage(getDriver());
        disneyWindowsAccountPage = new DisneyWindowsAccountPage(getDriver());
        disneyWindowsHelpPage = new DisneyWindowsHelpPage(getDriver());
        disneyWindowsLegalPage = new DisneyWindowsLegalPage(getDriver());
        disneyWindowsMoviesPage = new DisneyWindowsMoviesPage(getDriver());
        disneyWindowsOriginalsPage = new DisneyWindowsOriginalsPage(getDriver());
        disneyWindowsSearchPage = new DisneyWindowsSearchPage(getDriver());
        disneyWindowsSeriesPage = new DisneyWindowsSeriesPage(getDriver());
        disneyWindowsSettingsPage = new DisneyWindowsSettingsPage(getDriver());
        disneyWindowsWatchlistPage = new DisneyWindowsWatchlistPage(getDriver());
        disneyWindowsWhoseWatchingPage = new DisneyWindowsWhoseWatchingPage(getDriver());
        disneyWindowsCommonPage = new DisneyWindowsCommonPage(getDriver());
        disneyWindowsDetailsPage = new DisneyWindowsDetailsPage(getDriver());
        disneyWindowsVideoPlayerPage = new DisneyWindowsVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        accountApi.get().addProfile(disneyAccount.get(), "Automation", language, null, null, false, false);
        sa.assertTrue(disneyWindowsWelcomePage.isOpened(), "Welcome screen did not launch");
        login();
        sa.assertTrue(disneyWindowsWhoseWatchingPage.isOpened(), "Who is watching screen did not launch after login");
        disneyWindowsWhoseWatchingPage.selectDefaultProfile();
        sa.assertTrue(disneyWindowsHomePage.isOpened(), "Home screen did not launch after login");
        disneyWindowsHomePage.clickNewToDisneyContent();
        disneyWindowsSeriesPage.seriesDetailsCheck();
        globalNavMenuChecks(sa);
        legalScreens();
        helpAndAccountScreens();
        videoPlayAddRemoveWatchlist(sa);
        addEditProfile(sa);
        kidsModeCheck();
        logout();
        sa.assertTrue(disneyWindowsWelcomePage.isOpened(), "Welcome screen did not launch");

        sa.assertAll();
    }

    private void kidsModeCheck() {
        disneyWindowsHomePage.getProfile().click();
        disneyWindowsWhoseWatchingPage.selectProfileByNamePresent("kids");
        pause(5);
        UniversalUtils.captureAndUpload(getDriver());
        disneyWindowsHomePage.getProfile().click();
        disneyWindowsWhoseWatchingPage.selectDefaultProfile();
    }

    private void globalNavMenuChecks(SoftAssert sa) {
        int bound = disneyWindowsHomePage.globalNavMenu().size();
        for (int i = 0; i < bound; i++) {
            String text = disneyWindowsHomePage.globalNavMenu().get(i).getText();
            disneyWindowsHomePage.globalNavMenu().get(i).click();
            pause(5);
            UniversalUtils.captureAndUpload(getDriver());
            switch (text.toLowerCase()) {
                case "series":
                    sa.assertTrue(disneyWindowsSeriesPage.isSeriesPresent(), String.format(errorMessageGlobalNav, text));
                    break;
                case "movies":
                    sa.assertTrue(disneyWindowsMoviesPage.isMovieTitlePresent(), String.format(errorMessageGlobalNav, text));
                    break;
                case "originals":
                    sa.assertTrue(disneyWindowsOriginalsPage.isOriginalsPresent(), String.format(errorMessageGlobalNav, text));
                    break;
                case "home":
                    sa.assertTrue(disneyWindowsHomePage.isPixarPresent(), String.format(errorMessageGlobalNav, text));
                    break;
                case "search":
                    sa.assertTrue(disneyWindowsSearchPage.isSearchBoxPresent(), String.format(errorMessageGlobalNav, text));
                    break;
                case "settings":
                    sa.assertTrue(disneyWindowsSettingsPage.isAppSettingsPresent(), String.format(errorMessageGlobalNav, text));
                    break;
                case "watchlist":
                    sa.assertTrue(disneyWindowsWatchlistPage.isWatchlistPresent(), String.format(errorMessageGlobalNav, text));
                    break;
                default:
                    sa.assertTrue(disneyWindowsWhoseWatchingPage.isAddProfilePresent(), String.format(errorMessageGlobalNav, text));
                    disneyWindowsCommonPage.pressBack(1, 5);
                    break;
            }
        }
    }

    private void legalScreens() {
        disneyWindowsHomePage.getSettings().click();
        disneyWindowsSettingsPage.isOpened();
        disneyWindowsSettingsPage.getLegalButton().click();
        disneyWindowsLegalPage.isOpened();
        int legalPageLength = disneyWindowsLegalPage.legalMenu().size();
        for (int i = 0; i < legalPageLength; i++) {
            disneyWindowsLegalPage.legalMenu().get(i).click();
            pause(5);
            UniversalUtils.captureAndUpload(getDriver());
        }
    }

    private void helpAndAccountScreens() {
        disneyWindowsCommonPage.pressBack(1, 5);
        disneyWindowsSettingsPage.getHelpButton().click();
        disneyWindowsHelpPage.isOpened();
        UniversalUtils.captureAndUpload(getDriver());
        disneyWindowsCommonPage.pressBack(1, 5);
        disneyWindowsSettingsPage.getAccountButton().click();
        disneyWindowsAccountPage.isOpened();
        UniversalUtils.captureAndUpload(getDriver());
        disneyWindowsCommonPage.pressBack(2, 5);
        UniversalUtils.captureAndUpload(getDriver());
    }

    private void logout() {
        disneyWindowsHomePage.isPixarPresent();
        disneyWindowsHomePage.getSettings().click();
        disneyWindowsSettingsPage.isAppSettingsPresent();
        disneyWindowsSettingsPage.getLogOutButton().click();
        disneyWindowsWelcomePage.isOpened();
    }

    private void videoPlayAddRemoveWatchlist(SoftAssert sa) {
        disneyWindowsHomePage.getMovies().click();
        disneyWindowsMoviesPage.isOpened();
        disneyWindowsMoviesPage.select101DalmatiansMovie();
        disneyWindowsDetailsPage.isOpened();
        disneyWindowsDetailsPage.clickAddToWatchlist();
        sa.assertTrue(disneyWindowsDetailsPage.isRemoveWatchlistButtonPresent(), "content not added to watchlist");
        UniversalUtils.captureAndUpload(getDriver());
        disneyWindowsDetailsPage.removeFromWatchlist();
        UniversalUtils.captureAndUpload(getDriver());
        sa.assertTrue(disneyWindowsDetailsPage.isAddWatchlistButtonPresent(), "content not removed from watchlist");
        disneyWindowsDetailsPage.clickPlay();
        pause(15);
        //TODO commented the below line, have to fix enabling CC
        //disneyWindowsVideoPlayerPage.selectEnglishCC();
        UniversalUtils.captureAndUpload(getDriver());
        disneyWindowsCommonPage.pressBack(2, 5);
    }

    private void addEditProfile(SoftAssert sa) {
        String profileName = "profile1";
        String auto = "auto";
        disneyWindowsHomePage.getProfile().click();
        disneyWindowsWhoseWatchingPage.isOpened();
        disneyWindowsWhoseWatchingPage.addProfile(profileName);
        disneyWindowsWhoseWatchingPage.isOpened();
        sa.assertTrue(disneyWindowsWhoseWatchingPage.isProfileByNamePresent(profileName), "new profile is not added");
        disneyWindowsWhoseWatchingPage.editProfile(profileName, auto);
        disneyWindowsWhoseWatchingPage.isOpened();
        sa.assertTrue(disneyWindowsWhoseWatchingPage.isProfileByNamePresent(auto), "profile is not edited");
        disneyWindowsWhoseWatchingPage.deleteProfile(auto);
        disneyWindowsWhoseWatchingPage.isOpened();
        sa.assertFalse(disneyWindowsWhoseWatchingPage.isProfileByNamePresent(auto), "profile is not deleted");
        disneyWindowsWhoseWatchingPage.selectDefaultProfile();
    }
}
