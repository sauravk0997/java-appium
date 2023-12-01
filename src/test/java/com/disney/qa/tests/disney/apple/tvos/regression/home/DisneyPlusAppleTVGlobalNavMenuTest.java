package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.disney.DisneyContentIds;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DisneyPlusAppleTVGlobalNavMenuTest extends DisneyPlusAppleTVBaseTest {

    private List<String> globalNav;
    private List<String> globalNavText;
    private List<String> globalNavAliceLabels;

    private static final String KIDS_DOB = "2018-01-01";
    private static final String KIDS = "Kids";
    private static final String WATCHLIST_REF_TYPE_MOVIES = "programId";

    @BeforeMethod
    public void beforeMethod() {
        globalNav = Stream.of(DisneyPlusAppleTVHomePage.globalNavigationMenu.values())
                .map(DisneyPlusAppleTVHomePage.globalNavigationMenu::getText)
                .collect(Collectors.toList());
        globalNavText = Stream.of(DisneyPlusAppleTVHomePage.globalNavigationMenuText.values())
                .map(DisneyPlusAppleTVHomePage.globalNavigationMenuText::getText)
                .collect(Collectors.toList());
        globalNavAliceLabels = Stream.of(AliceLabels.PROFILE_BUTTON.getText(), AliceLabels.HOME_BUTTON_IS_SELECTED.getText(), AliceLabels.SEARCH_ICON.getText(),
                AliceLabels.WATCHLIST_ICON.getText(), AliceLabels.MOVIES_ICON.getText(), AliceLabels.ORIGINALS_ICON.getText(),
                AliceLabels.SERIES_ICON.getText(), AliceLabels.SETTINGS_ICON.getText(), AliceLabels.DISNEY_LOGO.getText())
                .collect(Collectors.toList());
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89541", "XCDQA-89543", "XCDQA-89549", "XCDQA-89547", "XCDQA-89545"})
    @Test(description = "Global Navigation > Collapsed State / Expanded State", groups = {"Home", "Smoke"})
    public void globalNavAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        logInTemp(entitledUser);

        // move down to focus on brand tile
        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        LOGGER.info("Opening global nav by clicking Menu button");
        sa.assertTrue(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not expanded after clicking on menu");
        sa.assertTrue(disneyPlusAppleTVHomePage.isDynamicAccessibilityIDElementPresent(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText()), "Home is not focused by default -1");
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, DisneyPlusAppleTVHomePage.globalNavigationMenuText.HOME.getText().toUpperCase(), AliceLabels.VERTICAL_MENU_ITEM_HOVERED_VERT_SEPARATOR.getText());

        disneyPlusAppleTVHomePage.clickSelect();
        LOGGER.info("Collapsing Global Nav menu by clicking select");
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not collapsed after clicking select from expanded global nav");

        disneyPlusAppleTVHomePage.moveLeft(1, 1);
        LOGGER.info("Expanding Global Nav menu by moving left");
        sa.assertTrue(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not expanded after moving left on home page");
        sa.assertTrue(disneyPlusAppleTVHomePage.isAIDElementPresentWithScreenshot(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText()), "Home is not focused by default -2");

        disneyPlusAppleTVHomePage.moveRight(1, 1);
        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        LOGGER.info("Collapsing Global Nav menu by moving right");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not collapsed after moving right from expanded global nav");
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        IntStream.range(0, globalNavText.size()).forEach(i -> {
            String menu = globalNav.get(i);
            String menuText = globalNavText.get(i);
            disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(menu);
            if (i != 0) {
                LOGGER.info(String.format("checking for %s global nav menu focus", menu));
                sa.assertTrue(disneyPlusAppleTVHomePage.isFocused(disneyPlusAppleTVHomePage.getDynamicAccessibilityId(menu)), String.format("%s is not focused on expanded global nav", menu));
                sa.assertEquals(disneyPlusAppleTVHomePage.getDynamicAccessibilityId(menu).getText().toUpperCase(), menuText.toUpperCase());
                aliceDriver.screenshotAndRecognize().assertLabelContainsCaptionCaseInsensitive(sa, menuText.toUpperCase(), AliceLabels.VERTICAL_MENU_ITEM_HOVERED_VERT_SEPARATOR.getText());
            } else {
                LOGGER.info("Checking for profile button focus");
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                sa.assertTrue(disneyPlusAppleTVHomePage.isProfileBtnFocused());
            }
        });
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, globalNavAliceLabels.toArray(String[]::new));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91703", "XCDQA-91701"})
    @Test(description = "Global Nav Menu appearance - kids mode", groups ={"Home", "Smoke"})
    public void globalNavAppearanceKidsProfile() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        SoftAssert sa = new SoftAssert();

        disneyAccountApi.addProfile(entitledUser, KIDS, KIDS_DOB, entitledUser.getProfileLang(), null, true, true);
        selectAppleUpdateLaterAndDismissAppTracking();
        logInWithoutHomeCheck(entitledUser);

        sa.assertTrue(new DisneyPlusAppleTVWhoIsWatchingPage(getDriver()).isOpened(), "Who's watching page did not launch");
        disneyPlusAppleTVHomePage.clickProfileBtn(KIDS);
        sa.assertTrue(disneyPlusAppleTVHomePage.isKidsHomePageOpen(), "Kids Home page is not open after login");

        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        IntStream.range(0, globalNav.size()).forEach(i -> {
            String menu = globalNav.get(i);
            if (i != 0) {
                sa.assertTrue(disneyPlusAppleTVHomePage.isDynamicAccessibilityIDElementPresent(menu), String.format("%s is not found on expanded global nav", menu));
            } else {
                LOGGER.info("Checking for profile button focus");
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                sa.assertTrue(disneyPlusAppleTVHomePage.isProfileBtnPresent());
            }
        });
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, globalNavAliceLabels.toArray(String[]::new));
        sa.assertTrue(disneyPlusAppleTVHomePage.isFocused(disneyPlusAppleTVHomePage.getDynamicAccessibilityId(
                DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText())), "HOME Nav bar selection is not focused/hovered");

        disneyPlusAppleTVHomePage.clickSelect();
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not collapsed after clicking select from expanded global nav");
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, globalNavAliceLabels.toArray(String[]::new));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90916", "XCDQA-90918"})
    @Test(description = "Hidden Nav state - inner pages", groups = {"Home"})
    public void hiddenNavState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVBrandsPage brandsPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVMoviesPage moviesPage = new DisneyPlusAppleTVMoviesPage(getDriver());
        DisneyPlusAppleTVSeriesPage seriesPage = new DisneyPlusAppleTVSeriesPage(getDriver());
        DisneyPlusAppleTVOriginalsPage originalsPage = new DisneyPlusAppleTVOriginalsPage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());

        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        searchApi.addToWatchlist(entitledUser, DisneyContentIds.END_GAME.getContentType(), DisneyContentIds.END_GAME.getContentId());
        List<String> innerPages = Stream.of(
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.MOVIES.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.SERIES.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.ORIGINALS.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.SETTINGS.getText())
                .collect(Collectors.toList());
        logInTemp(entitledUser);

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.clickRandomBrandTile();
        brandsPage.isOpened();

        sa.assertFalse(homePage.isGlobalNavPresent(), "Global Nav menu is present");

        brandsPage.clickMenuTimes(1, 1);
        sa.assertTrue(homePage.isHomeContentViewPresent(), "Not on home page after clicking menu on Brand page");

        //Navigate to Profile tab outside of switch for stability
        homePage.clickMenuTimes(1,1);
        homePage.clickProfileTab();
        homePage.clickSelect();
        sa.assertFalse(whoIsWatchingPage.isGlobalNavPresent(), "Global Nav menu is present.");
        sa.assertTrue(whoIsWatchingPage.isOpened(),"Profile page did not launch");
        whoIsWatchingPage.clickMenuTimes(1,1);

        IntStream.range(0, innerPages.size()).forEach(i -> {
            String menu = innerPages.get(i);
            homePage.hiddenNavStateOnInnerPages(menu);
            switch (menu.toLowerCase()) {
                case "search":
                    sa.assertFalse(searchPage.isGlobalNavPresent(), "Global Nav menu is present");
                    sa.assertTrue(searchPage.isOpened(), "Search Page did not launch");
                    break;
                case "watchlist":
                    sa.assertFalse(watchListPage.isGlobalNavExpanded(), "Global Nav menu is not collapsed");
                    sa.assertTrue(watchListPage.isOpened(), "Watchlist page did not launch");
                    break;
                case "movies":
                    sa.assertFalse(moviesPage.isGlobalNavExpanded(), "Global Nav menu is not collapsed");
                    sa.assertTrue(moviesPage.isOpened(), "Movies page did not launch");
                    break;
                case "series":
                    sa.assertFalse(seriesPage.isGlobalNavExpanded(), "Global Nav menu is not collapsed");
                    sa.assertTrue(seriesPage.isOpened(), "Series page did not launch");
                    break;
                case "originals":
                    sa.assertFalse(originalsPage.isGlobalNavExpanded(), "Global Nav menu is not collapsed");
                    sa.assertTrue(originalsPage.isOpened(), "Originals page did not launch");
                    break;
                case "settingsTab":
                    sa.assertFalse(settingsPage.isGlobalNavPresent(), "Global Nav menu is present.");
                    sa.assertTrue(settingsPage.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).isElementPresent(),
                        "Settings page did not launch");
                    break;

                default:
                    LOGGER.info("Invalid global nav menu");
                    break;
            }
        });
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90928"})
    @Test(description = "Hidden state - Hero Carousel", groups = {"Home"})
    public void hiddenStateHeroCarousel() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logInTemp(entitledUser);

        disneyPlusAppleTVHomePage.moveRight(1, 1);

        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.BANNER_HOVERED.getText());
        disneyPlusAppleTVHomePage.clickMenu();
        LOGGER.info("Opening global nav by clicking Menu button");
        sa.assertTrue(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not expanded after clicking on menu");
        sa.assertTrue(disneyPlusAppleTVHomePage.isAIDElementPresentWithScreenshot(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText()), "Home is not focused by default");
        disneyPlusAppleTVHomePage.moveRight(1, 1);
        LOGGER.info("Collapsing Global Nav menu by moving right");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), "Global Nav menu is present");
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
        sa.assertAll();
    }
}