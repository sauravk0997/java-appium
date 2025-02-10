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

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusMoreMenuSettingsTest extends DisneyBaseTest {

    private static final String ADULT_DOB = "1923-10-23";
    private static final String DARTH_MAUL = R.TESTDATA.get("disney_darth_maul_avatar_id");
    private static final String DEFAULT_PROFILE = "Test";
    private static final String KIDS_DOB = "2018-01-01";
    private static final String KIDS_PROFILE = "KIDS";
    private static final String TEST_USER = "Test User";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67276"})
    @Test(description = "Verify: More Menu Page UI", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION,
            TestGroup.SMOKE, US})
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
    @Test(description = "User taps on Profile Switcher", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProfileSwitcher() {
        List<String> profiles = Arrays.asList("Profile 2", "Profile 3", "Profile 4", "Profile 5", "Profile 6");
        profiles.forEach(profile -> getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(profile).language(getAccount().getProfileLang()).avatarId("5").kidsModeEnabled(false).dateOfBirth(null).build()));
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        swipeInContainerTillElementIsPresent(disneyPlusMoreMenuIOSPageBase.getProfileContainer(),
                disneyPlusMoreMenuIOSPageBase.getAddProfileBtn(), 5, Direction.LEFT);
        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isAddProfileButtonPresent());
        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed("Profile 6"),
                "Profile Tray did not swipe");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67280"})
    @Test(description = "User taps on Edit Profiles", groups = {TestGroup.PRE_CONFIGURATION, TestGroup.MORE_MENU, TestGroup.SMOKE, US})
    public void verifyEditProfilesDisplay() {
        SoftAssert softAssert = new SoftAssert();
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        disneyPlusMoreMenuIOSPageBase.clickEditProfilesBtn();

        Assert.assertTrue(disneyPlusEditProfileIOSPageBase.isEditProfilesTitlePresent(),
                "Edit Profiles title was not shown");

        softAssert.assertTrue(disneyPlusEditProfileIOSPageBase.isEditModeProfileIconPresent(DEFAULT_PROFILE),
                "Profiles are not in Edit Mode (pencil shown)");

        Assert.assertTrue(disneyPlusEditProfileIOSPageBase.getTypeButtonByLabel("Done").isElementPresent(),
                "Done button was not displayed");

        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAddProfileBtnPresent(),
                "Add Profile Icon was not displayed");

        softAssert.assertTrue(disneyPlusEditProfileIOSPageBase.isEditProfileImageDisplayed(),
                "Avatar Image was not displayed");

        disneyPlusEditProfileIOSPageBase.getTypeButtonByLabel("Done").click();

        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isOpened(),
                "'Who is Watching Page was not opened");

        softAssert.assertTrue(new DisneyPlusWhoseWatchingIOSPageBase(getDriver()).isAccessModeProfileIconPresent(DEFAULT_PROFILE),
                "Profiles did not change to Access Mode (pencil removed)");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67284"})
    @Test(description = "User Taps on App Settings", groups = {TestGroup.MORE_MENU, TestGroup.SMOKE, US})
    public void verifyAppSettings() {
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(disneyPlusMoreMenuIOSPageBase.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS)).click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.getDynamicAccessibilityId(disneyPlusMoreMenuIOSPageBase.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS)).isElementPresent()
                        && disneyPlusMoreMenuIOSPageBase.getBackArrow().isElementPresent(),
                "App Settings was not opened");

        disneyPlusMoreMenuIOSPageBase.getBackArrow().click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isOpened(),
                "User was not returned to the More Menu after closing Watchlist");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67286"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHelpCenter() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(KIDS_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(null).kidsModeEnabled(true).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount(), getAccount().getFirstName());
        SoftAssert sa = new SoftAssert();
        moreMenu.clickMoreTab();
        //Scenario: Verify Help hyperlink
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP)).click();
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
        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        editProfilePage.clickJuniorModeLearnMoreLink();
        moreMenu.waitForPresenceOfAnElement(moreMenu.getAddressBar());
        sa.assertTrue(moreMenu.isHelpWebviewOpen(), "'Help' web view was not opened");
        sa.assertTrue(editProfilePage.verifyTextOnWebView(JUNIOR_MODE_HELP_CENTER), "User was not navigated to Junior mode help center");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67290"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyLogOut() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        onboard(getAccount().getFirstName());
        welcomePage.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT)).click();
        Assert.assertTrue(welcomePage.isLogInButtonDisplayed(),
                "User was not logged out and returned to the Welcome screen");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61667"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAppVersionNumber() {
        onboard(getAccount().getFirstName());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.isAppVersionDisplayed(),
                "App Version was not displayed");
        String[] capabilitiesAppVersion =  R.CONFIG.get("capabilities.app").split("/");
        Assert.assertEquals(disneyPlusMoreMenuIOSPageBase.getAppVersion(), capabilitiesAppVersion[5],
                "Displayed App Version was not correct");
    }

    private void onboard(String profile) {
        setAppToHomeScreen(getAccount(), profile);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }
}
