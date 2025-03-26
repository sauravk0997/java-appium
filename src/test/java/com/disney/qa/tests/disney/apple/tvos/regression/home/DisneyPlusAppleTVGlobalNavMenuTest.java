package com.disney.qa.tests.disney.apple.tvos.regression.home;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.*;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.disney.DisneyEntityIds;

import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVGlobalNavMenuTest extends DisneyPlusAppleTVBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ThreadLocal<List<String>> GLOBAL_NAV = new ThreadLocal<>();
    private ThreadLocal<List<String>> GLOBAL_NAV_TEXT = new ThreadLocal<>();
    private ThreadLocal<List<String>> GLOBAL_NAV_ALICE_LABELS = new ThreadLocal<>();

    private static final String KIDS_DOB = "2018-01-01";
    private static final String KIDS = "Kids";
    private static final String GLOBAL_NAV_NOT_COLLAPSED = "Global Nav menu is not collapsed";
    private static final String GLOBAL_NAV_IS_PRESENT = "Global nav is present";

    public void initDisneyPlusAppleTVGlobalNavMenuTest() {
        DisneyPlusAppleTVHomePage appleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        GLOBAL_NAV.set(Stream.of(DisneyPlusAppleTVHomePage.globalNavigationMenu.values())
                .map(DisneyPlusAppleTVHomePage.globalNavigationMenu::getText)
                .collect(Collectors.toList()));
        GLOBAL_NAV_TEXT.set(appleTVHomePage.getEnumMenuText());
        GLOBAL_NAV_ALICE_LABELS.set(
                Stream.of(AliceLabels.PROFILE_BUTTON.getText(), AliceLabels.HOME_BUTTON_IS_SELECTED.getText(), AliceLabels.SEARCH_ICON.getText(),
                                AliceLabels.WATCHLIST_ICON.getText(), AliceLabels.MOVIES_ICON.getText(), AliceLabels.ORIGINALS_ICON.getText(),
                                AliceLabels.SERIES_ICON.getText(), AliceLabels.SETTINGS_ICON.getText(), AliceLabels.DISNEY_LOGO.getText())
                        .collect(Collectors.toList()));
    }

    @AfterMethod(alwaysRun = true)
    public void clearDisneyPlusAppleTVGlobalNavMenuTest() {
        GLOBAL_NAV.remove();
        GLOBAL_NAV_TEXT.remove();
        GLOBAL_NAV_ALICE_LABELS.remove();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XCDQA-89541", "XCDQA-89543", "XCDQA-89549", "XCDQA-89547", "XCDQA-89545" })
    @Test(description = "Global Navigation > Collapsed State / Expanded State", groups = { TestGroup.HOME, TestGroup.SMOKE, US})
    public void globalNavAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        initDisneyPlusAppleTVGlobalNavMenuTest();

        logIn(getUnifiedAccount());

        // move down to focus on brand tile
        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        LOGGER.info("Opening global nav by clicking Menu button");
        sa.assertTrue(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not expanded after clicking on menu");

        sa.assertTrue(disneyPlusAppleTVHomePage.isDynamicAccessibilityIDElementPresent(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText()),
                "Home is not focused by default -1");

        disneyPlusAppleTVHomePage.clickSelect();
        LOGGER.info("Collapsing Global Nav menu by clicking select");
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavExpanded(),
                "Global Nav menu is not collapsed after clicking select from expanded global nav");

        disneyPlusAppleTVHomePage.moveLeft(1, 1);
        LOGGER.info("Expanding Global Nav menu by moving left");
        sa.assertTrue(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global Nav menu is not expanded after moving left on home page");
        sa.assertTrue(disneyPlusAppleTVHomePage.isAIDElementPresentWithScreenshot(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText()),
                "Home is not focused by default -2");

        disneyPlusAppleTVHomePage.moveRight(1, 1);
        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        LOGGER.info("Collapsing Global Nav menu by moving right");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavExpanded(),
                "Global Nav menu is not collapsed after moving right from expanded global nav");

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        IntStream.range(0, GLOBAL_NAV_TEXT.get().size()).forEach(i -> {
            String menu = GLOBAL_NAV.get().get(i);
            String menuText = GLOBAL_NAV_TEXT.get().get(i);
            disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(menu);
            if (i != 0) {
                LOGGER.info(String.format("checking for %s global nav menu focus", menu));
                sa.assertTrue(disneyPlusAppleTVHomePage.isFocused(disneyPlusAppleTVHomePage.getDynamicAccessibilityId(menu)),
                        String.format("%s is not focused on expanded global nav", menu));
                sa.assertEquals(disneyPlusAppleTVHomePage.getDynamicAccessibilityId(menu).getText().toUpperCase(), menuText.toUpperCase());

            } else {
                LOGGER.info("Checking for profile button focus");
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                sa.assertTrue(disneyPlusAppleTVHomePage.isProfileBtnFocused());
            }
        });

        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, GLOBAL_NAV_ALICE_LABELS.get().toArray(String[]::new));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XCDQA-90916", "XCDQA-90918" })
    @Test(groups = {TestGroup.HOME, US})
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
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());

        initDisneyPlusAppleTVGlobalNavMenuTest();

        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(), getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlock(DisneyEntityIds.END_GAME.getEntityId()));

        List<String> innerPages = Stream.of(
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.MOVIES.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.SERIES.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.ORIGINALS.getText(),
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.SETTINGS.getText())
                .collect(Collectors.toList());
        logIn(getUnifiedAccount());

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.clickRandomBrandTile();
        brandsPage.isOpened();

        LOGGER.info("Validating Global Nav is not present in brand screen");
        sa.assertFalse(homePage.isGlobalNavPresent(), GLOBAL_NAV_IS_PRESENT);

        brandsPage.clickMenuTimes(1, 2);
        sa.assertTrue(homePage.isOpened(), "Not on home page after clicking menu on Brand page");

        //Navigate to Profile tab outside of switch for stability
        homePage.clickMenuTimes(1, 2);
        homePage.clickProfileTab();
        homePage.clickSelect();
        LOGGER.info("Validating Global Nav is not present in profile screen");
        sa.assertFalse(whoIsWatchingPage.isGlobalNavPresent(), GLOBAL_NAV_IS_PRESENT);
        sa.assertTrue(whoIsWatchingPage.isOpened(), "Profile page did not launch");
        whoIsWatchingPage.clickMenuTimes(1, 2);

        IntStream.range(0, innerPages.size()).forEach(i -> {
            String menu = innerPages.get(i);
            homePage.hiddenNavStateOnInnerPages(menu);
            switch (menu.toLowerCase()) {
                case "searchtab":
                    sa.assertFalse(searchPage.isGlobalNavExpanded(), GLOBAL_NAV_NOT_COLLAPSED);
                    sa.assertTrue(searchPage.isOpened(), "Search Page did not launch");
                    break;
                case "watchlistcell":
                    sa.assertFalse(watchListPage.isGlobalNavExpanded(), GLOBAL_NAV_NOT_COLLAPSED);
                    sa.assertTrue(watchListPage.isOpened(), "Watchlist page did not launch");
                    break;
                case "movies":
                    homePage.clickRight();
                    sa.assertFalse(moviesPage.isGlobalNavExpanded(), GLOBAL_NAV_NOT_COLLAPSED);
                    sa.assertTrue(moviesPage.isOpened(), "Movies page did not launch");
                    break;
                case "series":
                    sa.assertFalse(seriesPage.isGlobalNavExpanded(), GLOBAL_NAV_NOT_COLLAPSED);
                    sa.assertTrue(seriesPage.isOpened(), "Series page did not launch");
                    break;
                case "originals":
                    sa.assertFalse(originalsPage.isGlobalNavExpanded(), GLOBAL_NAV_NOT_COLLAPSED);
                    sa.assertTrue(originalsPage.isOpened(), "Originals page did not launch");
                    break;
                case "settingstab":
                    sa.assertFalse(originalsPage.isGlobalNavExpanded(), GLOBAL_NAV_NOT_COLLAPSED);
                    sa.assertTrue(
                            settingsPage.getDynamicCellByLabel(
                                    moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS)).isPresent(),
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
    @Test(groups = {TestGroup.HOME, US})
    public void hiddenStateHeroCarousel() {
        String recommendedForYou = CollectionConstant.getCollectionTitle(CollectionConstant.Collection.RECOMMENDED_FOR_YOU);
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        logIn(getUnifiedAccount());

        homePage.moveLeft(2, 1);
        homePage.isCarouselFocused();
        homePage.clickMenu();
        LOGGER.info("Opening global nav by clicking Menu button");
        sa.assertTrue(homePage.isGlobalNavExpanded(), "Global Nav menu is not expanded after clicking on menu");
        sa.assertTrue(homePage.isAIDElementPresentWithScreenshot(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText()),
                "Home is not focused by default");

        LOGGER.info("Collapsing Global Nav menu by moving right");
        homePage.moveRight(2, 1);
        sa.assertFalse(homePage.isGlobalNavPresent(), "Global Nav menu is present");

        homePage.moveDown(1, 1);
        homePage.navigateToShelf(homePage.getStaticTextByLabelContains(recommendedForYou));
        sa.assertTrue(homePage.getStaticTextByLabel(recommendedForYou).isPresent(),
                "Recommended For You is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67250"})
    @Test(groups = { TestGroup.HOME, TestGroup.SMOKE, US})
    public void verifyGlobalNavKidsSelectedExpanded() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        SoftAssert sa = new SoftAssert();
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        initDisneyPlusAppleTVGlobalNavMenuTest();
        selectAppleUpdateLaterAndDismissAppTracking();
        logInWithoutHomeCheck(getUnifiedAccount());

        sa.assertTrue(new DisneyPlusAppleTVWhoIsWatchingPage(getDriver()).isOpened(),
                "Who's watching page did not launch");
        homePage.clickProfileBtn(KIDS);
        sa.assertTrue(homePage.isKidsHomePageOpen(), "Kids Home page is not open after login");

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavWithClickingMenu();
        IntStream.range(0, GLOBAL_NAV.get().size()).forEach(i -> {
            String menu = GLOBAL_NAV.get().get(i);
            if (i != 0) {
                Assert.assertTrue(homePage.isDynamicAccessibilityIDElementPresent(menu),
                        String.format("%s is not found on expanded global nav", menu));
            } else {
                LOGGER.info("Checking for profile button focus");
                Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
                Assert.assertTrue(homePage.isProfileBtnPresent());
            }
        });
        Assert.assertTrue(homePage.isGlobalNavExpanded(),
                "Global Nav menu is not expanded");
        Assert.assertTrue(homePage.isFocused(homePage.getDynamicAccessibilityId(
                        DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText())),
                "HOME Nav bar selection is not focused/hovered");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67248"})
    @Test(groups = { TestGroup.HOME, TestGroup.SMOKE, US})
    public void verifyGlobalNavKidsSelectedCollapsed() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        SoftAssert sa = new SoftAssert();
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        initDisneyPlusAppleTVGlobalNavMenuTest();
        selectAppleUpdateLaterAndDismissAppTracking();
        logInWithoutHomeCheck(getUnifiedAccount());

        sa.assertTrue(new DisneyPlusAppleTVWhoIsWatchingPage(getDriver()).isOpened(),
                "Who's watching page did not launch");
        homePage.clickProfileBtn(KIDS);
        sa.assertTrue(homePage.isKidsHomePageOpen(), "Kids Home page is not open after login");

        homePage.moveDownFromHeroTileToBrandTile();
        Assert.assertFalse(homePage.isGlobalNavExpanded(),
                "Global Nav menu is not collapsed after clicking select from expanded global nav");

        homePage.clickMenuTimes(1, 2);

        IntStream.range(0, GLOBAL_NAV.get().size()).forEach(i -> {
            String menu = GLOBAL_NAV.get().get(i);
            homePage.navigateToOneGlobalNavMenu(menu);
            boolean focused = homePage.isFocused(homePage.getDynamicAccessibilityId(menu));
            sa.assertTrue(focused,
                    "HOME Nav bar selection is not focused/hovered");

            homePage.clickSelect();
            Assert.assertFalse(homePage.isGlobalNavExpanded(),
                    "Global Nav menu is not collapsed after clicking select from expanded global nav");
            if (focused = true && menu == DisneyPlusAppleTVHomePage.globalNavigationMenu.PROFILE.getText()) {
                homePage.clickMenuTimes(2, 2);
            } else if (focused = true && menu == DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText()) {
                homePage.moveDownFromHeroTileToBrandTile();
            }
            homePage.navigateToGlobalNav();
        });
        sa.assertAll();
    }
}
