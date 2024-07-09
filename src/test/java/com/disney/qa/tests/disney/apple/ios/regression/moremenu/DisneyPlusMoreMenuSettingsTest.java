package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.SkipException;
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67276"})
    @Test(description = "Verify: More Menu Page UI", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyMoreMenuPageUI() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        DisneyAccount account = getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(TEST_USER).dateOfBirth(ADULT_DOB).language(getAccount().getProfileLang()).avatarId(DARTH_MAUL).kidsModeEnabled(false).isStarOnboarded(true).build());
        setAppToHomeScreen(account, TEST_USER);

        softAssert.assertTrue(homePage.getMoreMenuTab().isPresent(), "Profile Icon is not displayed");
        if(R.CONFIG.get(DEVICE_TYPE).equals(TABLET)){
            softAssert.assertTrue(homePage.isProfileNameDisplayed(TEST_USER), "Profile Name is not displayed");
        }

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        softAssert.assertTrue(moreMenuPage.isProfileSwitchDisplayed(TEST_USER)
                        && moreMenuPage.isProfileSwitchDisplayed(DEFAULT_PROFILE),
                "Profile Switcher was not displayed for all profiles");

        for (DisneyPlusMoreMenuIOSPageBase.MoreMenu menuItem : DisneyPlusMoreMenuIOSPageBase.MoreMenu.values()) {
            softAssert.assertTrue(moreMenuPage.isMenuOptionPresent(menuItem),
                    menuItem + " option was not be present");
        }
        Assert.assertTrue(moreMenuPage.isAppVersionDisplayed(),
                "App Version was not displayed");

        //verify that Profile Selector/Switch does NOT scroll if user scroll above element
        scrollUp();
        softAssert.assertTrue(moreMenuPage.isProfileSwitchDisplayed(TEST_USER)
                        && moreMenuPage.isProfileSwitchDisplayed(DEFAULT_PROFILE),
                "Profile Switcher was not displayed for all profiles");

        //verify if profile has pin lock then lock icon is displayed under profile name
        moreMenuPage.clickHomeIcon();
        try {
            getAccountApi().updateProfilePin(account, account.getProfileId(DEFAULT_PROFILE), "1234");
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }
        moreMenuPage.clickMoreTab();
        softAssert.assertTrue(moreMenuPage.isPinLockOnProfileDisplayed(DEFAULT_PROFILE), "Lock icon under the profile name is not displayed");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61655"})
    @Test(description = "User taps on Profile Switcher", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyProfileSwitcher() {
        List<String> profiles = Arrays.asList("Profile 2", "Profile 3", "Profile 4", "Profile 5", "Profile 6");
        profiles.forEach(profile -> getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(profile).language(getAccount().getProfileLang()).avatarId("5").kidsModeEnabled(false).dateOfBirth(null).build()));
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        disneyPlusMoreMenuIOSPageBase.swipeCells("Profile 3", 12, Direction.LEFT);

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed("Profile 6"),
                "Profile Tray did not swipe");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67280"})
    @Test(description = "User taps on Edit Profiles", groups = {TestGroup.PRE_CONFIGURATION, TestGroup.MORE_MENU, TestGroup.SMOKE})
    public void verifyEditProfilesDisplay() {
        SoftAssert softAssert = new SoftAssert();
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase disneyPlusChooseAvatarIOSPageBase = new DisneyPlusChooseAvatarIOSPageBase(getDriver());
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

        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAddProfileBtnPresent(),
                "Add Profile Icon was not displayed");

        disneyPlusWhoseWatchingIOSPageBase.clickAddProfile();

        Assert.assertTrue(disneyPlusChooseAvatarIOSPageBase.isOpened(),
                "Choose Avatar Page was not displayed");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67284"})
    @Test(description = "User Taps on App Settings", groups = {TestGroup.MORE_MENU, TestGroup.SMOKE})
    public void verifyAppSettings() {
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicAccessibilityId(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).isElementPresent()
                        && disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "App Settings was not opened");

        disneyPlusMoreMenuIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isOpened(),
                "User was not returned to the More Menu after closing Watchlist");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67286"})
    @Test(description = "Help Center > Open in New Browser", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyHelpCenter() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(KIDS_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(null).kidsModeEnabled(true).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount(), getAccount().getFirstName());
        SoftAssert sa = new SoftAssert();
        moreMenu.clickMoreTab();
        //Scenario: Verify Help hyperlink
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP.getMenuOption()).click();
        sa.assertTrue(moreMenu.isHelpWebviewOpen(), "'Help' webview was not opened");
        moreMenu.goBackToDisneyAppFromSafari();
        moreMenu.dismissNotificationsPopUp();
        Assert.assertTrue(moreMenu.isOpened(), "User was not returned to the More Menu after navigating back from safari");
        //Scenario: Verify Info hyperlink
        moreMenu.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(getAccount().getFirstName());
        editProfilePage.selectInfoHyperlink();
        sa.assertTrue(moreMenu.isHelpWebviewOpen(), "'Help' web view was not opened");
        sa.assertTrue(editProfilePage.verifyTextOnWebView(DISNEY_PLUS_HELP_CENTER),"User was not navigated to Disney plus help center");
        moreMenu.goBackToDisneyAppFromSafari();
        moreMenu.dismissNotificationsPopUp();
        editProfilePage.clickDoneBtn();
        //Scenario: Verify Learn More on Kids profile
        moreMenu.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        editProfilePage.clickJuniorModeLearnMoreLink();
        sa.assertTrue(moreMenu.isHelpWebviewOpen(), "'Help' web view was not opened");
        sa.assertTrue(editProfilePage.verifyTextOnWebView(JUNIOR_MODE_HELP_CENTER), "User was not navigated to Junior mode help center");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67290"})
    @Test(description = "User taps on Log Out", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifyLogOut() {
        onboard(getAccount().getFirstName());
        initPage(DisneyPlusMoreMenuIOSPageBase.class).getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT.getMenuOption()).click();

        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isSignUpButtonDisplayed(),
                "User was not logged out and returned to the Welcome screen");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61667"})
    @Test(description = "More Menu View - App Version Number", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAppVersionNumber() {
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isAppVersionDisplayed(),
                "App Version was not displayed");

        Assert.assertEquals(disneyPlusMoreMenuIOSPageBase.getAppVersionText(), "Version: " + "2.23.0.59750",
                "Displayed App Version was not correct");
    }
    
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66835"})
    @Test(description = "Verify: Simplified Kids More Menu", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION})
    public void verifySimplifiedKidsMoreMenu() {
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(KIDS_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(DARTH_MAUL).kidsModeEnabled(true).isStarOnboarded(true).build());
        onboard(KIDS_PROFILE);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        //Validate watchlist and app version are present
        sa.assertTrue(moreMenu.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST),
                "Watchlist option was not found on a child account");
        sa.assertTrue(moreMenu.isAppVersionDisplayed(), "App version number was not found on a child account");

        //Validate app settings, logout, help, account and legal are not present
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS),
                "App Settings option was found on a child account");
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT),
                "Log Out option was found on a child account");
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP),
                "Help option was found on a child account");
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT),
                "Account option was found on a child account");
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LEGAL),
                "Legal option was found on a child account");
        sa.assertAll();
    }

    private void onboard(String profile) {
        setAppToHomeScreen(getAccount(), profile);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }
}
