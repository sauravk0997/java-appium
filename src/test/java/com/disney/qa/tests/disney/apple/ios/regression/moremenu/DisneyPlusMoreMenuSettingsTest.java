package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusMoreMenuSettingsTest extends DisneyBaseTest {

    private static final String ADULT_DOB = "1923-10-23";
    private static final String DEFAULT_PROFILE = "Test";
    private static final String KIDS_DOB = "2018-01-01";
    private static final String KIDS_PROFILE = "KIDS";
    private static final String TEST_USER = "Test User";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67276"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyMoreMenuPageUI() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(TEST_USER)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());
        setAppToHomeScreen(getUnifiedAccount(), TEST_USER);

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
            getUnifiedAccountApi().updateProfilePin(getUnifiedAccount(),
                    getUnifiedAccount().getProfileId(DEFAULT_PROFILE), "1234");
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
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        List<String> profiles = Arrays.asList("Profile 2", "Profile 3", "Profile 4", "Profile 5", "Profile 6");
        profiles.forEach(profile ->
                getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                        .unifiedAccount(getUnifiedAccount())
                        .profileName(profile)
                        .dateOfBirth(ADULT_DOB)
                        .language(getLocalizationUtils().getUserLanguage())
                        .avatarId("5")
                        .kidsModeEnabled(false)
                        .build()));

        onboard(getUnifiedAccount().getFirstName());

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
        onboard(getUnifiedAccount().getFirstName());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67286"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHelpCenter() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), getUnifiedAccount().getFirstName());
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
        editProfilePage.clickEditModeProfile(getUnifiedAccount().getFirstName());
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
//        moreMenu.waitForPresenceOfAnElement(moreMenu.getAddressBar());
        sa.assertTrue(moreMenu.isHelpWebviewOpen(), "'Help' web view was not opened");
        sa.assertTrue(editProfilePage.verifyTextOnWebView(JUNIOR_MODE_HELP_CENTER), "User was not navigated to Junior mode help center");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67290"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyLogOut() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        onboard(getUnifiedAccount().getFirstName());
        welcomePage.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT)).click();
        Assert.assertTrue(welcomePage.isLogInButtonDisplayed(),
                "User was not logged out and returned to the Welcome screen");
    }

    private void onboard(String profile) {
        setAppToHomeScreen(getUnifiedAccount(), profile);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }
}
