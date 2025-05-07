package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.gmail.exceptions.GMailUtilsException;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.CONTINUE_BTN_NOT_DISPLAYED;
import static com.disney.qa.common.constant.IConstantHelper.HOME_PAGE_NOT_DISPLAYED;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.IConstantHelper.WELCOME_SCREEN_NOT_DISPLAYED;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SETTINGS;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVAccountTests extends DisneyPlusAppleTVBaseTest {

    private static final String OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED = "OOH Soft Block screen not displayed";
    private static final String OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED = "OOH Hard Block screen not displayed";
    private static final String OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED = "OOH Verify Device screen not displayed";
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
    private static final String UPDATE_HOUSEHOLD_BUTTON_NOT_PRESENT = "Update Household button not displayed";
    private static final String UPDATE_HOUSE_SCREEN_NOT_DISPLAYED = "'Update your Disney+ Household' screen not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118407"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifySubCopyForExtraMemberAddOnUser() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());

        setAccount(createAccountSharingUnifiedAccounts().getReceivingAccount());
        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
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
        String email = "accountsharingsofttest@disneyplustesting.com";
        String password = "Test1234!";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);

        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenHeadlinePresent(),
                OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenSubCopyPresent(),
                "OOH Soft Block page subcopy not displayed");
        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenSubCopyTwoPresent(),
                "OOH Soft Block page subcopy 2 not displayed");
        sa.assertTrue(accountSharingPage.getOOHSoftBlockContinueButton().isPresent(),
                CONTINUE_BTN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickSelect();

        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyPresent(),
                "OOH Verify Device subcopy not displayed");
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyTwoPresent(),
                "OOH Verify Device subcopy, 'learn more' not displayed");
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceButton().isPresent(),
                OOH_VERIFY_BUTTON_NOT_PRESENT);
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceDismissButton().isPresent(),
                "No Thanks/Dismiss button not displayed");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118365"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHSoftBlockVerifyDeviceOTPConfirmationPage() {
        String email = "qait.disneystreaming+1744102243522aebcdisneystreaming@gmail.com";
        String password = "M1ck3yM0us3#";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);

        sa.assertTrue(accountSharingPage.isOOHSoftBlockScreenHeadlinePresent(),
                OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();

        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        accountSharingPage.enterOtpOnModal(getOTPFromApi(email));
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
        String email = "qait.disneystreaming+1744104491527109cdisneystreaming@gmail.com";
        String password = "M1ck3yM0us3#";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);

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
        accountSharingPage.enterOtpOnModal(getOTPFromApi(email));
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
        String email = "qait.disneystreaming+1744104491527109cdisneystreaming@gmail.com";
        String password = "M1ck3yM0us3#";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);

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
        accountSharingPage.enterOtpOnModal(getOTPFromApi(email));
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
        String email = "qait.disneystreaming+1744104491527109cdisneystreaming@gmail.com";
        String password = "M1ck3yM0us3#";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);

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

        String email = "testtravelmaxed@disneyplustesting.com";
        String password = "Test1234!";

        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);
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
        String email = "testerrorhandling2@disneyplustesting.com";
        String password = "Test1234!";
        String invalidCode = "111111";
        String errorMessage = "Sorry, we could not connect you to Disney+ using the passcode you provided. " +
                "Please re-enter your passcode and try again. " +
                "If the problem persists, visit the Disney+ Help Centre (error code 21).";
        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);
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
        sa.assertTrue(accountSharingPage.getStaticTextByLabelContains(errorMessage).isPresent(),
                "Error message is not present");
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

        String email = "testconfirmdevice@disneyplustesting.com";
        String password = "Test1234!";

        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);
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
        sa.assertTrue(accountSharingPage.isOOHCheckEmailTextPresent(email), "OOH check mail subtext is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118368"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyAccountSharingOTPSupportReasonCodes() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());

        String email = "testhardsupport@disneyplustesting.com";
        String password = "Test1234!";

        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        homePage.moveDown(1, 1);
        sa.assertTrue(accountSharingPage.isFocused(accountSharingPage.getOOHUpdateHouseHoldCTA()),
                "Update Household button not focused");
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
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());

        String email = "testhardnocyos@disneyplustesting.com";
        String password = "Test1234!";

        SoftAssert sa = new SoftAssert();
        loginWithAccountSharingUser(email, password);
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHIAmAwayFromHomeCTA().isPresent(),
                AWAY_FROM_HOME_BUTTON_NOT_DISPLAYED);
        // Following steps will validate navigation between OOH screens
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                OOH_CONFIRM_AWAY_SCREEN_NOT_DISPLAYED);
        homePage.clickBack();
        sa.assertTrue(accountSharingPage.isOOHHardBlockScreenHeadlinePresent(),
                OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED);
        // Click in logout button and confirm logout in confirmation page
        homePage.moveDown(1, 1);
        homePage.moveRight(1, 1);
        sa.assertTrue(accountSharingPage.getOOHLogOutButton().isPresent(),
                LOG_OUT_BUTTON_NOT_PRESENT);
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isLogoutConfirmationTitlePresent(),
                LOG_OUT_CONFIRMATION_NOT_DISPLAYED);
        homePage.clickSelect();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116763"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHHardTravelModeUX() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVAccountSharingPage accountSharingPage = new DisneyPlusAppleTVAccountSharingPage(getDriver());
        SoftAssert sa = new SoftAssert();
        String email = "qait.disneystreaming+1799211931388caeedisneystreaming@gmail.com";
        String password = "Test1234!";
        loginWithAccountSharingUser(email, password);

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
        accountSharingPage.enterOtpOnModal(getOTPFromApi(email));
        sa.assertTrue(accountSharingPage.isOOHConfirmationHeadlinePresent(),
                OTP_SUCCESS_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.getOOHConfirmationPageCTA().isPresent(),
                CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED);
        accountSharingPage.getOOHConfirmationPageCTA().click();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    private void loginWithAccountSharingUser(String email, String password) {
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage passcodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomeScreen.clickLogInButton();
        loginPage.proceedToLocalizedPasswordScreen(email);
        Assert.assertTrue(passcodePage.isOpened(), "Log In password screen did not launch");
        passcodePage.clickLoginWithPassword();
        passwordPage.logInWithPasswordLocalized(password);
    }

    //TODO Once QP-4003 and QP-4001 fixed and API available to create HouseHold account, Need to remove this method,
    //we can use existing getOTPFromApi method from BaseTest class
    public String getOTPFromApi(String email) {
        int emailAPILatency = 10;
        try {
            String firstOTP = getEmailApi().getDisneyOTP(email);
            pause(emailAPILatency);
            String secondOTP = getEmailApi().getDisneyOTP(email);

            if (!secondOTP.equals(firstOTP)) {
                LOGGER.info("First and second OTP doesn't match, firstOTP: {}, secondOTP: {}", firstOTP, secondOTP);
                return secondOTP;
            } else {
                LOGGER.info("First and second OTP match, returning first OTP: {}", firstOTP);
                return firstOTP;
            }
        } catch (GMailUtilsException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
