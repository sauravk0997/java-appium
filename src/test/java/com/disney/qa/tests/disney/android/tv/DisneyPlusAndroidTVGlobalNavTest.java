package com.disney.qa.tests.disney.android.tv;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.disney.android.pages.common.DisneyPlusMoreMenuPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSearchPageBase;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVProfilePageBase;
import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVSettingsPageBase;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.*;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.DIS;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.STA;

public class DisneyPlusAndroidTVGlobalNavTest extends DisneyPlusAndroidTVBaseTest {
    private static final String APP_LANDING_ERROR = "Expected - App to land on Discover page";
    private static final String MENU_DISPLAYED_ERROR = "Menu is displayed";

    @Test(groups = {"smoke"})
    public void BackBtnExpandsGlobalNav() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66764", "XCDQA-66768", "XCDQA-66770"));
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100420"));
        SoftAssert sa = new SoftAssert();

        login(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVDiscoverPage.get().focusHeroTile();
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), GLOBAL_NAV_COLLAPSE_ERROR);

        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(), GLOBAL_NAV_LOAD_ERROR);

        if (isStar()) {
            navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getShelfItem(), AndroidKey.DPAD_RIGHT);
        } else {
            disneyPlusAndroidTVCommonPage.get().focusBrandTile("disney");
        }

        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isGlobalNavMainMenuHidden(), GLOBAL_NAV_COLLAPSE_ERROR);
        sa.assertAll();
    }

    @Test(description = "Global Menu: Selecting BACK Exits App", groups = {"smoke"})
    public void backBtnExitsApp() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66766"));
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());

        disneyPlusAndroidTVDiscoverPage.get().focusHeroTile();
        disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus();
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), GLOBAL_NAV_LOAD_ERROR);
        // Press back to exit app
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        Assert.assertFalse(isAppRunning(), "App should not be running. Should be on Android Home screen");

        sa.assertAll();
    }

    @Test(description = "Selecting Global Navigation Items takes user to the correct screen")
    public void selectingGlobalNavOptions() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66772", "XCDQA-66746"));
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100419", "XCDQA-100421", "XCDQA-100422", "XCDQA-100424"));
        SoftAssert sa = new SoftAssert();
        JsonNode applicationDictionary = getApplicationDictionary(language);
        String appSettingsTitle = apiProvider.get().getDictionaryItemValue(applicationDictionary, DisneyPlusMoreMenuPageBase.AppSettingsList.SETTINGS_PAGE_HEADER.getText());
        String moviesTitle = apiProvider.get().getDictionaryItemValue(applicationDictionary, DisneyPlusSearchPageBase.ScreenTitles.MOVIES.getText());
        String seriesTitle = apiProvider.get().getDictionaryItemValue(applicationDictionary, DisneyPlusSearchPageBase.ScreenTitles.SERIES.getText());
        String watchlistTitle = apiProvider.get().getDictionaryItemValue(applicationDictionary, DisneyPlusMoreMenuPageBase.MenuList.WATCHLIST.getText());
        String profileSelectionTitle = apiProvider.get().getDictionaryItemValue(applicationDictionary, DisneyPlusAndroidTVProfilePageBase.ProfileItems.PROFILE_SELECTION_TITLE.getText());
        String searchTitle = isStar() ? "Search by title or team" : apiProvider.get().getDictionaryItemValue(applicationDictionary, DisneyPlusSearchPageBase.ScreenTitles.SEARCH_BAR_PLACEHOLDER.getText());

        Map<String, String> map = new HashMap<>();
        map.put("Settings", appSettingsTitle);
        map.put("Movies", moviesTitle);
        map.put("Series", seriesTitle);
        map.put("Watchlist", watchlistTitle);
        map.put("Profile", profileSelectionTitle);
        map.put("Search", searchTitle);

        login(entitledUser.get());

        map.forEach((key, value) -> {
            disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus();
            disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(key);
            sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isTextElementPresent(value), "Text should be present: " + value);

            if (key.equals("Profile")) {
                sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
                sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isGlobalNavCollapsed(), "Global nav thumbnails should not be present" + value);
                sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), "Menu should not be displayed on page : " + value);
                disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
                sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
            } else {
                sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isGlobalNavCollapsed(), "Global nav should be collapsed on: " + value);
                sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), "Global nav should not be expanded on: " + value);
            }
        });
      
        disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus();
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect("Home");
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isGlobalNavFullyCollapsedOnHome(), "Global nav shouldn't be visible at all");
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), "Global nav should not be expanded when focus is on hero carousel");
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().pressDownFromHeroCarousel(), "Global nav is not collapsed on home page");
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), "Global nav should not be expanded when focus moves on brand tile");
        disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus();
        if (isStar()) {
            disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect("ESPN");
            sa.assertTrue(starPlusAndroidTVESPNPageBase.get().isOpened(), "ESPN Page should be opened");
        } else {
            disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect("Originals");
            sa.assertTrue(disneyPlusAndroidTVOriginalsPageBase.get().isOpened(), ORIGINALS_PAGE_LOAD_ERROR);
        }
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isGlobalNavCollapsed(), GLOBAL_NAV_COLLAPSE_ERROR);
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), "Global nav should not be expanded on Originals/ESPN page");

        sa.assertAll();
    }

    @Test()
    public void hiddenGlobalMenu() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66774", "XCDQA-66776"));
        SoftAssert sa = new SoftAssert();
        Random rand = new Random();
        JsonNode applicationDictionary = getApplicationDictionary(language);
        String appSettingsTitle = apiProvider.get().getDictionaryItemValue(applicationDictionary,
            DisneyPlusMoreMenuPageBase.AppSettingsList.SETTINGS_PAGE_HEADER.getText());

        login(entitledUser.get());
        disneyPlusAndroidTVCommonPage.get().pressDownFromHeroCarousel();

        int randomIndex = rand.ints(0, 5).findFirst().getAsInt();
        disneyPlusAndroidTVCommonPage.get().pressRight(randomIndex);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isBrandTileFocused(randomIndex),
                String.format("Brand tile at index %s should be focused", randomIndex));
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVBrandPage.get().isOpened(), BRAND_PAGE_LOAD_ERROR);
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(),
                MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), APP_LANDING_ERROR);
        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(SETTINGS,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        EnumSet<DisneyPlusAndroidTVSettingsPageBase.Settings> settings = EnumSet.allOf(DisneyPlusAndroidTVSettingsPageBase.Settings.class);
        settings.remove(DisneyPlusAndroidTVSettingsPageBase.Settings.PROFILES);
        settings.remove(DisneyPlusAndroidTVSettingsPageBase.Settings.APP_SETTINGS);
        settings.remove(DisneyPlusAndroidTVSettingsPageBase.Settings.LOGOUT);
        for (DisneyPlusAndroidTVSettingsPageBase.Settings setting : settings ) {
            disneyPlusAndroidTVSettingsPageBase.get().selectGenericContentElement(setting.getText());
            sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(),
                    MENU_DISPLAYED_ERROR);
            disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
            sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isElementWithTextPresent(appSettingsTitle),
                    "Expected - '" + appSettingsTitle + "' header to be displayed");
        }

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.PROFILE,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVDiscoverPage.get().selectGenericContentDescElement(apiProvider.get().getDictionaryItemValue(applicationDictionary,
                DisneyPlusAndroidTVProfilePageBase.ProfileItems.EDIT_PROFILE_BTN.getText()));
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVDiscoverPage.get().clickOnGenericTextExactElement(apiProvider.get().getDictionaryItemValue(applicationDictionary,
                DisneyPlusAndroidTVProfilePageBase.ProfileItems.CREATE_PROFILE.getText()));
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isSkipBtnPresent(), "Profile Icon selection screen should open");
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isOpened(), SEARCH_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVSearchPage.get().selectCollectionFromFirstRow();
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVSearchPage.get().isOpened(), SEARCH_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.ORIGINALS,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVOriginalsPageBase.get().isOpened(), ORIGINALS_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().selectMediaItem(true);
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVOriginalsPageBase.get().isOpened(), ORIGINALS_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SERIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().selectMediaItem(false);
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVSeriesPageBase.get().isOpened(), SERIES_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES,
                disneyPlusAndroidTVCommonPage.get().openGlobalNavAndFocus());
        sa.assertTrue(disneyPlusAndroidTVMoviesPageBase.get().isOpened(), MOVIES_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().selectMediaItem(false);
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), MENU_DISPLAYED_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVMoviesPageBase.get().isOpened(), MOVIES_PAGE_LOAD_ERROR);

        sa.assertAll();
    }

    @Test(description = "Verify global nav is collapsed, current page thumb is highlighted and persistent indicator is present")
    public void globalNavNonInteractiveWhenCollapsed() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66744", "XCDQA-66748"));
        SoftAssert sa = new SoftAssert();
        disneyPlusAndroidTVCommonPage.get().setTakeScreenshot(true);

        login(entitledUser.get());
        for (int i = 0; i < 5 ; i++) {
            disneyPlusAndroidTVCommonPage.get().pressDown(1);
            sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isGlobalNavCollapsed(),
                    "Global nav should be collapsed after scrolling " + i);
            sa.assertFalse(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(),
                    "Menu should display after scrolling " + i);
        }

        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isCollapsedNavThumbnailHighlighted(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME),
                "Home thumbnail should be highlighted or indicator is present");
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().getText().equals(""),
                "Home global should not display text while collapsed");

        sa.assertAll();
    }

    @Test
    public void globalNavExpandedState() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66750", "XCDQA-66756", "XCDQA-66758", "XCDQA-66754"));
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        login(entitledUser.get());
        navHelper.keyUntilElementVisible(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible and focused on Home.");
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN, SETTINGS.toString());
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), GLOBAL_NAV_LOAD_ERROR);
        
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.HOME_BUTTON_IS_SELECTED.getText())
                .assertLabelContainsCaptionCaseInsensitive(sa, HOME.getText(), AliceLabels.VERTICAL_MENU_ITEM_SELECTED.getText())
                .assertLabelContainsCaptionCaseInsensitive(sa, SETTINGS.getText(), AliceLabels.VERTICAL_MENU_ITEM_HOVERED_VERT_SEPARATOR.getText())
                .assertNoLabelContainsCaption(sa, "Disney", AliceLabels.RECTANGLE_TILE.getText());
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().isOpened(), SETTINGS_PAGE_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVSettingsPageBase.get().isSettingsOptionFocused(0, disneyPlusAndroidTVSettingsPageBase.get().getAllSettings()), "App Settings not focused");
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isGlobalNavCollapsed(), GLOBAL_NAV_COLLAPSE_ERROR);

        sa.assertAll();
    }

    @Test(description = "Open navbar with left presses and then verify persistent indicator")
    public void globalNavExpandedUsingLeft() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66752", "XCDQA-66760", "XCDQA-66762"));
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100420"));
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 3);
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        login(entitledUser.get());
        if (!isStar()) {
            disneyPlusAndroidTVCommonPage.get().pressDownFromHeroCarousel();
            sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isBrandTileFocused(0),
                    "Disney brand should be focused");
            aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.HOME_BUTTON_IS_SELECTED.getText());
        }

        disneyPlusAndroidTVCommonPage.get().pressRight(randomNumber);
        disneyPlusAndroidTVCommonPage.get().pressLeft(randomNumber + 1);

        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isMenuDisplayed(), GLOBAL_NAV_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isNavSelectionFocused(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME),
                "Home menu should be focused on global nav bar");
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isHomeGlobalNavHomeFocused(), "Home is not focused on global nav");

        if (!isStar()) {
            NavHelper navHelper = new NavHelper(getCastedDriver());
            navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getBrandTileList(0).get(0), AndroidKey.DPAD_RIGHT);
            sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isBrandTileFocused(0),
                    "Disney brand tile not focused after press right and collapsing global nav");
        }
        sa.assertAll();
    }
}
