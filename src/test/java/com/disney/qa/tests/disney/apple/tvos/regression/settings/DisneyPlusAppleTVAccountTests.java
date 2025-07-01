package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.disney.qa.api.household.pojos.*;
import com.disney.qa.api.pojos.*;

import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SETTINGS;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVAccountTests extends DisneyPlusAppleTVBaseTest {

    private static final String OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED = "OOH Soft Block screen is not displayed";
    private static final String OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED = "OOH Hard Block screen is not displayed";
    private static final String OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED = "OOH Verify Device screen is not displayed";
    private static final String OTP_PAGE_DID_NOT_OPEN = "User not navigated to OTP page";
    private static final String OTP_SUCCESS_MESSAGE_NOT_DISPLAYED =
            "Confirmation page not displayed after entering OTP";
    private static final String CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED = "'Continue To Disney+' button not displayed";
    private static final String SEND_CODE_BUTTON_NOT_DISPLAYED = "Send Code button not displayed";
    private static final String LOG_OUT_BUTTON_NOT_PRESENT = "Log out button not displayed";
    private static final String OOH_VERIFY_BUTTON_NOT_PRESENT = "Verify Device button not displayed";
    private static final String AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED = "'I'm Away From Home' button not displayed";
    private static final String TRAVEL_MODE_MAXED_HEADLINE_NOT_DISPLAYED = "Travel mode maxed headline not displayed";
    private static final String LOG_OUT_CONFIRMATION_NOT_DISPLAYED = "Log out confirmation page did not open";
    private static final String OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED = "Travel mode 'Confirm you are away from home' screen not displayed";
    private static final String UPDATE_HOUSEHOLD_BUTTON_NOT_PRESENT = "Update Household button is not displayed";
    private static final String UPDATE_HOUSEHOLD_BUTTON_IS_NOT_FOCUSSED= "Update Household button is not focussed";
    private static final String UPDATE_HOUSE_SCREEN_NOT_DISPLAYED = "'Update your Disney+ Household' screen not displayed";
    private  static final String HOUSEHOLD_CREATE_ACCOUNT_HEADLINE = "HH create account headline is not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118407"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifySubCopyForExtraMemberAddOnUser() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());

        setAccount(createAccountSharingUnifiedAccounts().getReceivingAccount());
        logIn(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());
        homePage.moveDownFromHeroTile();
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        settingsPage.clickSubscriptionsCell();
        Assert.assertTrue(settingsPage.isExtraMemberSubscriptionDetailTitlePresent(),
                "Extra member subscription detail title not displayed");
        Assert.assertTrue(settingsPage.isExtraMemberSubscriptionDetailSubCopyPresent(),
                "Extra member subscription detail sub copy not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116817"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHSoftBlockVerifyDeviceUIVerification() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();

        UnifiedAccount account = setHouseholdExperience(ExperienceId.SOFT, false);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenHeadlinePresent(),
                OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenSubCopyPresent(),
                "OOH Soft Block page sub copy is not displayed");
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenSubCopyTwoPresent(),
                "OOH Soft Block page sub copy 2 is not displayed");
        sa.assertTrue(accountSharingPage.getOOHSoftBlockContinueButton().isPresent(),
                CONTINUE_BTN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickSelect();

        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyPresent(),
                "OOH Verify Device sub copy is not displayed");
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyTwoPresent(),
                "OOH Verify Device sub copy, 'learn more' is not displayed");
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceButton().isPresent(),
                OOH_VERIFY_BUTTON_NOT_PRESENT);
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceDismissButton().isPresent(),
                "No Thanks/Dismiss button is not displayed");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118365"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHSoftBlockVerifyDeviceOTPConfirmationPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();

        UnifiedAccount account = setHouseholdExperience(ExperienceId.SOFT, true);
        setAccount(account);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenHeadlinePresent(),
                OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();

        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        accountSharingPage.enterOtpOnModal(getOTPFromApi(account));
        sa.assertTrue(accountSharingPage.isOOHConfirmationHeadlinePresent(),
                OTP_SUCCESS_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHConfirmationPageCTA().isPresent(),
                CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED);
        accountSharingPage.getOOHConfirmationPageCTA().click();
        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118363"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHFlaggedTravelModeOTPConfirmationPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();

        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, true);
        setAccount(account);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        accountSharingPage.enterOtpOnModal(getOTPFromApi(account));
        sa.assertTrue(accountSharingPage.isOOHConfirmationHeadlinePresent(),
                OTP_SUCCESS_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHConfirmationPageCTA().isPresent(),
                CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED);
        accountSharingPage.getOOHConfirmationPageCTA().click();
        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118364"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHFlaggedUpdateHouseHoldOTPConfirmationPage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, true);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHUpdateHouseHoldCTA().isPresent(),
                UPDATE_HOUSEHOLD_BUTTON_NOT_PRESENT);
        homePage.clickDown();
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHUpdateHouseHoldHeadlinePresent(),
                UPDATE_HOUSE_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHUpdateHouseHoldSendCodeCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        accountSharingPage.enterOtpOnModal(getOTPFromApi(account));
        sa.assertTrue(accountSharingPage.isOOHConfirmationHeadlinePresent(),
                OTP_SUCCESS_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHConfirmationPageCTA().isPresent(),
                CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED);
        accountSharingPage.getOOHConfirmationPageCTA().click();
        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118367"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHFlaggedTravelModeUI() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();

        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, false);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenSubCopyPresent(),
                "Travel mode screen sub copy not displayed");
        sa.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116857"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyAccountSharingTravelModeMaxedOut() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD_MAX_TRAVEL_NO_CYOS, false);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isFocused(accountSharingPage.getOOHIAmAwayFromHomeCTA()),
                "'I'm Away From Home' button is not focused");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeMaxedHeadlinePresent(),
                TRAVEL_MODE_MAXED_HEADLINE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHTravelModeMaxedSubcopy(),
                "Travel mode screen sub copy not displayed");
        sa.assertTrue(accountSharingPage.getOOHTravelModeMaxedOKCTA().isPresent(),
                "OOH OK button is not present");
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                "OOH Logout button is not present");

        // Click in OK button and verify we are back in the previous screen
        accountSharingPage.getOOHTravelModeMaxedOKCTA().click();
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeMaxedHeadlinePresent(),
                TRAVEL_MODE_MAXED_HEADLINE_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        // Click in logout button and confirm logout in confirmation page
        accountSharingPage.getOOHLogOutButton().click();
        sa.assertTrue(accountSharingPage.isLogoutConfirmationTitlePresent(),
                LOG_OUT_CONFIRMATION_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116861"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyAccountSharingErrorHandling() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        String invalidCode = "111111";
        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, false);
        logIn(account, account.getProfiles().get(0).getProfileName());
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isFocused(accountSharingPage.getOOHIAmAwayFromHomeCTA()),
                "'I'm Away From Home' button is not focused");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenSubCopyPresent(),
                "Travel mode screen sub copy not displayed");
        sa.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        // Enter invalid OTP code and validate error in screen
        accountSharingPage.enterOtpOnModal(invalidCode);
        sa.assertTrue(forgotPasswordPage.isOTPErrorMessagePresent(), "Error message is not present");
        homePage.clickDown();
        accountSharingPage.getResendEmailCopy().click();
        sa.assertTrue(accountSharingPage.getOOHErrorPageHeadline().isPresent(), "Error page headline is not present");
        sa.assertTrue(accountSharingPage.getOOHErrorActivationGenericCopy().isPresent(), "Error activation text is not present");
        accountSharingPage.getOkButton().click();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118369"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyAccountSharingSoftBlockConfirmDevice() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();

        UnifiedAccount account = setHouseholdExperience(ExperienceId.SOFT_NO_CYOS, false);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());
        // Verify Soft Block screen and click in continue
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenHeadlinePresent(),
                OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();

        // Validate verify screen and click in verify device button
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceNoCyosSubCopyPresent(),
                "OOH Verify Device No Cyos not displayed");
        homePage.clickSelect();

        // Verify OTP screen has opened
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        sa.assertTrue(accountSharingPage.isOOHCheckEmailTextPresent(account.getEmail()), "OOH check mail subtext is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118368"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyAccountSharingOTPSupportReasonCodes() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, false);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        sa.assertTrue(accountSharingPage.isFocused(accountSharingPage.getOOHUpdateHouseHoldCTA()),
                UPDATE_HOUSEHOLD_BUTTON_IS_NOT_FOCUSSED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHUpdateHouseHoldHeadlinePresent(),
                UPDATE_HOUSE_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116822"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHHardNoCyosHousehold() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD_NO_CYOS, false);
        logInWithoutHomeCheck(account);
        whoseWatchingPage.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        // Following steps will validate navigation between OOH screens
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHUpdateHouseHoldHeadlinePresent(),
                UPDATE_HOUSE_SCREEN_NOT_DISPLAYED);
        homePage.clickBack();
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        // Click in logout button and verify logout confirmation page
        homePage.moveDown(1, 1);
        homePage.moveRight(1, 1);
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isLogoutConfirmationTitlePresent(),
                LOG_OUT_CONFIRMATION_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.clickBack();
        sa.assertTrue(whoseWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116763"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHHardTravelModeUX() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, true);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        // Validate hard block screen
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        // Click in logout button and validate confirmation page
        homePage.moveDown(1, 1);
        accountSharingPage.getOOHLogOutButton().click();
        sa.assertTrue(accountSharingPage.isLogoutConfirmationTitlePresent(),
                LOG_OUT_CONFIRMATION_NOT_DISPLAYED);
        // Cancel log out and go back to away screen
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        homePage.moveUp(1, 1);
        sa.assertTrue(homePage.isFocused(accountSharingPage.getOOHTravelModeOTPCTA()),
                "Send code button is not focused");
        homePage.clickSelect();

        // Validate OTP
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        accountSharingPage.enterOtpOnModal(getOTPFromApi(account));
        sa.assertTrue(accountSharingPage.isOOHConfirmationHeadlinePresent(),
                OTP_SUCCESS_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHConfirmationPageCTA().isPresent(),
                CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED);
        accountSharingPage.getOOHConfirmationPageCTA().click();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116814"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHHardUpdateUXVerification() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, true);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        homePage.clickSelect();

        // Click in logout button and validate confirmation page
        sa.assertTrue(accountSharingPage.isOOHUpdateHouseHoldHeadlinePresent(),
                UPDATE_HOUSE_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHUpdateHouseHoldSendCodeCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        accountSharingPage.getOOHLogOutButton().click();
        sa.assertTrue(accountSharingPage.isLogoutConfirmationTitlePresent(),
                LOG_OUT_CONFIRMATION_NOT_DISPLAYED);
        // Cancel log out and go back to away screen
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        homePage.moveUp(1, 1);
        sa.assertTrue(homePage.isFocused(accountSharingPage.getOOHTravelModeOTPCTA()),
                "Send code button is not focused");
        homePage.clickSelect();

        // Validate OTP
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        accountSharingPage.enterOtpOnModal(getOTPFromApi(account));
        sa.assertTrue(accountSharingPage.isOOHConfirmationHeadlinePresent(),
                OTP_SUCCESS_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHConfirmationPageCTA().isPresent(),
                CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED);
        accountSharingPage.getOOHConfirmationPageCTA().click();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116762"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHDevicePageSoftBlock() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();

        UnifiedAccount account = setHouseholdExperience(ExperienceId.SOFT, true);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        // Steps to verify the No thanks option
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenHeadlinePresent(),
                OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHSoftBlockContinueButton().isPresent(),
                CONTINUE_BTN_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED);
        homePage.clickDown();
        homePage.clickSelect();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        // Steps to verify device
        homePage.clickDown();
        homePage.openGlobalNavWithClickingMenu();
        homePage.moveUp(2, 1);
        homePage.clickSelect();
        sa.assertTrue(whoseWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);

        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.getOOHSoftBlockContinueButton().isPresent(),
                CONTINUE_BTN_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();

        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        accountSharingPage.enterOtpOnModal(getOTPFromApi(account));
        sa.assertTrue(accountSharingPage.isOOHConfirmationHeadlinePresent(),
                OTP_SUCCESS_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHConfirmationPageCTA().isPresent(),
                CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED);
        accountSharingPage.getOOHConfirmationPageCTA().click();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116823"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHHardBlockNavigation() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD, false);
        logInWithoutHomeCheck(account);
        whoseWatchingPage.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHUpdateHouseHoldHeadlinePresent(),
                UPDATE_HOUSE_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHUpdateHouseHoldSendCodeCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        homePage.clickBack();
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickBack();
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.clickBack();
        sa.assertTrue(whoseWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116818"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyMaxedHHUpdates() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD_MAX_HH, false);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        Assert.assertTrue(accountSharingPage.isOOHHardBlockCreateAccLabelPresent(),
                HOUSEHOLD_CREATE_ACCOUNT_HEADLINE);

        sa.assertTrue(accountSharingPage.isOOHHardBlockSubcopyPresent(),
                "OOH hard block subcopy was not present");
        sa.assertTrue(accountSharingPage.isOOHHardBlockSubcopy2Present(),
                "OOH hard block subcopy 2 was not present");
        sa.assertTrue(accountSharingPage.getOOHUpdateHouseHoldCTA().isPresent(),
                UPDATE_HOUSEHOLD_BUTTON_NOT_PRESENT);
        homePage.moveDown(1, 1);
        Assert.assertTrue(accountSharingPage.isFocused(accountSharingPage.getOOHUpdateHouseHoldCTA()),
                UPDATE_HOUSEHOLD_BUTTON_IS_NOT_FOCUSSED);
        homePage.clickSelect();

        //Verify copies and CTA informing user they can no longer update their household
        sa.assertTrue(accountSharingPage.isOOHUpdateHHMaxedHeadlinePresent(),
                "OOH maxed headline is not displayed");
        sa.assertTrue(accountSharingPage.isOOHUpdateHHMaxedSubcopyPresent(),
                "OOH update maxed subcopy is not displayed");

        //User selects OK CTA
        Assert.assertTrue(accountSharingPage.isOOHUpdateHHMaxedButtonPresent(),
                "OOH update maxed button is not displayed");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHHardBlockCreateAccLabelPresent(),
                HOUSEHOLD_CREATE_ACCOUNT_HEADLINE);

        //verify Logout CTA
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHUpdateHHMaxedHeadlinePresent(),
                "OOH maxed headline is not displayed");
        Assert.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                "OOH Logout button is not present");
        accountSharingPage.getOOHLogOutButton().click();
        sa.assertTrue(accountSharingPage.isLogoutConfirmationTitlePresent(),
                LOG_OUT_CONFIRMATION_NOT_DISPLAYED);
        homePage.clickSelect();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-117641"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyAccountSharingHardNoCyosOverride() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatching = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        UnifiedAccount account = setHouseholdExperience(ExperienceId.HARD_NO_CYOS, false);
        logInWithoutHomeCheck(account);
        whoIsWatching.clickProfile(getUnifiedAccount().getProfiles().get(0).getProfileName());

        Assert.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        Assert.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        Assert.assertTrue(accountSharingPage.getOOHUpdateHouseHoldCTA().isPresent(),
                UPDATE_HOUSEHOLD_BUTTON_NOT_PRESENT);
        Assert.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);

        LOGGER.info("verifying I'm away from home flow");
        homePage.clickSelect();
        Assert.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        Assert.assertTrue(accountSharingPage.getOOHTravelModeOTPCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        Assert.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickBack();
        Assert.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);

        LOGGER.info("verifying update household flow");
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        Assert.assertTrue(accountSharingPage.isOOHUpdateHouseHoldHeadlinePresent(),
                UPDATE_HOUSE_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHUpdateHouseHoldSendCodeCTA().isPresent(),
                SEND_CODE_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickBack();
        Assert.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);

        LOGGER.info("Verifying Logout button 'cancel' flow");
        homePage.moveDown(1, 1);
        homePage.moveRight(1, 1);
        homePage.clickSelect();
        Assert.assertTrue(accountSharingPage.isLogoutConfirmationTitlePresent(),
                LOG_OUT_CONFIRMATION_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        homePage.clickSelect();
        Assert.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);

        LOGGER.info("Verifying Logout button 'logout' flow");
        homePage.clickSelect();
        Assert.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(SHORT_TIMEOUT),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickSelect();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        sa.assertAll();
    }
}
