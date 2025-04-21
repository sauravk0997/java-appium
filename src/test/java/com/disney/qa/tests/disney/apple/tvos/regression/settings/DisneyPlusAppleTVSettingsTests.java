package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVLegalPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSettingsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVSettingsTests extends DisneyPlusAppleTVBaseTest {
    private static final String SETTINGS_PAGE_NOT_DISPLAYED = "Settings page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68157"})
    @Test(groups = {TestGroup.SMOKE, US})
    public void verifyLogout() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        Assert.assertTrue(settingsPage.isOpened(), SETTINGS_PAGE_NOT_DISPLAYED);
        homePage.moveDownUntilElementIsFocused(settingsPage.getLogOutCell(), 8);
        // Log out and validate welcome screen is open
        settingsPage.getLogOutCell().click();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68357"})
    @Test(groups = {TestGroup.SMOKE, US})
    public void verifyLegalPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVLegalPage legalPage = new DisneyPlusAppleTVLegalPage(getDriver());
        String disneyTerms = getLocalizationUtils().getLegalHeaders().iterator().next();

        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        Assert.assertTrue(settingsPage.isOpened(), SETTINGS_PAGE_NOT_DISPLAYED);
        // Navigate and select Legal option and validate options
        homePage.moveDownUntilElementIsFocused(legalPage.getLegalOption(), 8);
        legalPage.getLegalOption().click();

        Assert.assertTrue(legalPage.isOpened(), "Legal page did not open");
        legalPage.verifyLegalHeaders();
        // Validate first option is focused and opened
        Assert.assertTrue(legalPage.isFocused(legalPage.getTypeButtonByLabel(disneyTerms)), "First option is not focused");
        legalPage.verifyLegalOptionExpanded(disneyTerms);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68140"})
    @Test(groups = {TestGroup.SMOKE, US})
    public void verifyGlobalMenuOptions() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchlistPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVMoviesPage moviesPage = new DisneyPlusAppleTVMoviesPage(getDriver());
        DisneyPlusAppleTVSeriesPage seriesPage = new DisneyPlusAppleTVSeriesPage(getDriver());
        DisneyPlusAppleTVOriginalsPage originalsPage = new DisneyPlusAppleTVOriginalsPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        Assert.assertTrue(whoseWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoseWatchingPage.clickBack();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        homePage.openGlobalNavAndSelectOneMenu(HOME.getText());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        Assert.assertTrue(watchlistPage.isOpened(), WATCHLIST_PAGE_NOT_DISPLAYED);

        homePage.clickLeft();
        homePage.navigateToOneGlobalNavMenu(MOVIES.getText());
        homePage.clickSelect();
        Assert.assertTrue(moviesPage.isOpened(), "Movies page was not open");
        homePage.openGlobalNavAndSelectOneMenu(SERIES.getText());
        Assert.assertTrue(seriesPage.isOpened(), "Series page was not open");
        homePage.openGlobalNavAndSelectOneMenu(ORIGINALS.getText());
        Assert.assertTrue(originalsPage.isOpened(), ORIGINALS_PAGE_NOT_DISPLAYED);
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        Assert.assertTrue(settingsPage.isOpened(), SETTINGS_PAGE_NOT_DISPLAYED);
    }
}
