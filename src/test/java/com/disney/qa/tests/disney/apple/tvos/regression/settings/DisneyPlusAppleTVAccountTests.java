package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVAccountSharingPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVLoginPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVOneTimePasscodePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVPasswordPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSettingsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
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

    private static final String OOH_SOFT_BLOCK_SCREEN_NOT_DISPLAYED = "OOH Soft Block page headline not displayed";
    private static final String OOH_HARD_BLOCK_SCREEN_NOT_DISPLAYED = "OOH Hard Block page headline not displayed";
    private static final String OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED = "OOH Verify Device screen not displayed";
    private static final String OTP_PAGE_DID_NOT_OPEN = "User not navigated to OTP page";
    private static final String OTP_SUCCESS_MESSAGE_NOT_DISPLAYED =
            "Confirmation page not displayed after entering OTP";
    private static final String CONTINUE_TO_DISNEY_BUTTON_NOT_DISPLAYED = "'Continue To Disney+' button not displayed";
    private static final String SEND_CODE_BUTTON_NOT_DISPLAYED = "Send Code button not displayed";

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
    public void verifyOOHSoftBlockVerification() {
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
                "Log out button not displayed");
        homePage.clickSelect();

        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceHeadlinePresent(),
                OOH_VERIFY_DEVICE_SCREEN_NOT_DISPLAYED);
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyPresent(),
                "OOH Verify Device subcopy not displayed");
        sa.assertTrue(accountSharingPage.isOOHVerifyDeviceSubCopyTwoPresent(),
                "OOH Verify Device subcopy, 'learn more' not displayed");
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceButton().isPresent(),
                "Verify Device button not displayed");
        sa.assertTrue(accountSharingPage.getOOHVerifyDeviceDismissButton().isPresent(),
                "No Thanks/Dismiss button not displayed");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHEnterOtpPagePresent(),
                OTP_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-118365"})
    @Test(groups = {TestGroup.ACCOUNT_SHARING, US})
    public void verifyOOHSoftBlockOTPConfirmationPage() {
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
                "'I'm Away From Home' button not displayed");
        homePage.clickSelect();
        sa.assertTrue(accountSharingPage.isOOHTravelModeScreenHeadlinePresent(),
                "Travel mode 'Confirm you are away from home' screen not displayed");
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
