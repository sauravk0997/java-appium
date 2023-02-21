package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;

public class DisneyPlusMoreMenuSettingsTest extends DisneyBaseTest {

    private static final String ADULT_DOB = "1923-10-23";
    private static final String DARTH_MAUL = R.TESTDATA.get("disney_darth_maul_avatar_id");
    private static final String DEFAULT_PROFILE = "Test";
    private static final String KIDS_DOB = "2018-01-01";
    private static final String KIDS_PROFILE = "KIDS";
    private static final String TEST_USER = "Test User";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61653"})
    @Test(description = "Verify: More Menu Page UI", groups = {"More Menu"})
    public void verifyMoreMenuPageUI() {
        setGlobalVariables();
        SoftAssert softAssert = new SoftAssert();
        disneyAccountApi.get().addProfile(disneyAccount.get(),TEST_USER,ADULT_DOB,disneyAccount.get().getProfileLang(),DARTH_MAUL,false,true);
        onboard(TEST_USER);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        softAssert.assertTrue(disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed(TEST_USER)
                        && disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed(DEFAULT_PROFILE),
                "Profile Switcher was not displayed for all profiles");

        for (DisneyPlusMoreMenuIOSPageBase.MoreMenu menuItem : DisneyPlusMoreMenuIOSPageBase.MoreMenu.values()) {
            softAssert.assertTrue(disneyPlusMoreMenuIOSPageBase.isMenuOptionPresent(menuItem),
                    menuItem + " option was not be present");
        }

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61655"})
    @Test(description = "User taps on Profile Switcher", groups = {"More Menu"})
    public void verifyProfileSwitcher() {
        setGlobalVariables();
        List<String> profiles = Arrays.asList("Profile 2", "Profile 3", "Profile 4", "Profile 5", "Profile 6");
        profiles.forEach(profile -> disneyAccountApi.get().addProfile(disneyAccount.get(), profile, disneyAccount.get().getProfileLang(), "5", false));
        onboard(disneyAccount.get().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        disneyPlusMoreMenuIOSPageBase.swipeCells("Profile 3", 12, Direction.LEFT);

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed("Profile 6"),
                "Profile Tray did not swipe");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61657"})
    @Test(description = "User taps on Edit Profiles", groups = {"More Menu"})
    public void verifyEditProfilesDisplay() {
        setGlobalVariables();
        SoftAssert softAssert = new SoftAssert();
        onboard(disneyAccount.get().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        disneyPlusMoreMenuIOSPageBase.clickEditProfilesBtn();

        Assert.assertTrue(disneyPlusEditProfileIOSPageBase.isEditProfilesTitlePresent(),
                "Edit Profiles title was not shown");

        softAssert.assertTrue(disneyPlusEditProfileIOSPageBase.isEditModeProfileIconPresent(DEFAULT_PROFILE),
                "Profiles are not in Edit Mode (pencil shown)");

        Assert.assertTrue(disneyPlusEditProfileIOSPageBase.getTypeButtonByLabel("Done").isElementPresent(),
                "Done button was not displayed");

        disneyPlusEditProfileIOSPageBase.getTypeButtonByLabel("Done").click();

        softAssert.assertTrue(new DisneyPlusWhoseWatchingIOSPageBase(getDriver()).isAccessModeProfileIconPresent(DEFAULT_PROFILE),
                "Profiles did not change to Access Mode (pencil removed)");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61659"})
    @Test(description = "User taps on Watchlist", groups = {"More Menu"})
    public void verifyWatchlist() {
        setGlobalVariables();
        onboard(disneyAccount.get().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicAccessibilityId(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).isElementPresent()
                        && disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "Watchlist was not opened");

        disneyPlusMoreMenuIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isOpened(),
                "User was not returned to the More Menu after closing Watchlist");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61661"})
    @Test(description = "User Taps on App Settings", groups = {"More Menu"})
    public void verifyAppSettings() {
        setGlobalVariables();
        onboard(disneyAccount.get().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicAccessibilityId(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).isElementPresent()
                        && disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "App Settings was not opened");

        disneyPlusMoreMenuIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isOpened(),
                "User was not returned to the More Menu after closing Watchlist");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61663", "XMOBQA-62713"})
    @Test(description = "User taps on Help", groups = {"More Menu"})
    public void verifyHelp() {
        setGlobalVariables();
        onboard(disneyAccount.get().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP.getMenuOption()).click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isHelpWebviewOpen(),
                "'Help' webview was not opened");

        disneyPlusMoreMenuIOSPageBase.clickWebviewDoneBtn();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isOpened(),
                "User was not returned to the More Menu after closing Watchlist");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61665"})
    @Test(description = "User taps on Log Out", groups = {"More Menu"})
    public void verifyLogOut() {
        setGlobalVariables();
        onboard(disneyAccount.get().getFirstName());
        initPage(DisneyPlusMoreMenuIOSPageBase.class).getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT.getMenuOption()).click();

        Assert.assertTrue(new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).isOpened(),
                "User was not logged out and returned to the Welcome screen");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61667"})
    @Test(description = "More Menu View - App Version Number", groups = {"More Menu"})
    public void verifyAppVersionNumber() {
        setGlobalVariables();
        onboard(disneyAccount.get().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isAppVersionDisplayed(),
                "App Version was not displayed");

        Assert.assertEquals(disneyPlusMoreMenuIOSPageBase.getAppVersionText(), "Version: " + new MobileUtilsExtended().getInstalledAppVersionFull(),
                "Displayed App Version was not correct");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61669"})
    @Test(description = "Verify: Simplified Kids More Menu", groups = {"More Menu"})
    public void verifySimplifiedKidsMoreMenu() {
        setGlobalVariables();
        SoftAssert softAssert = new SoftAssert();
        disneyAccountApi.get().addProfile(disneyAccount.get(),KIDS_PROFILE,KIDS_DOB,disneyAccount.get().getProfileLang(),DARTH_MAUL,true,true);
        onboard(KIDS_PROFILE);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        softAssert.assertTrue(disneyPlusMoreMenuIOSPageBase.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST),
                "Watchlist option was not available to a child account");

        softAssert.assertFalse(disneyPlusMoreMenuIOSPageBase.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS),
                "App Settings option was not available to a child account");

        softAssert.assertFalse(disneyPlusMoreMenuIOSPageBase.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LEGAL),
                "Legal option was not available to a child account");

        softAssert.assertFalse(disneyPlusMoreMenuIOSPageBase.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT),
                "Log Out option was not available to a child account");

        softAssert.assertFalse(disneyPlusMoreMenuIOSPageBase.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP),
                "Help option was available to a child account");

        softAssert.assertFalse(disneyPlusMoreMenuIOSPageBase.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT),
                "Account option was available to a child account");

        softAssert.assertAll();
    }

    private void onboard(String profile) {
        setAppToHomeScreen(disneyAccount.get(), profile);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }
}