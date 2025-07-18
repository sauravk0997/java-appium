package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusMoreMenuAccountSettingsTest extends DisneyBaseTest {
    private static final String NEW_PASSWORD = "TestPass1234!";
    private static final String ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED = "One time passcode screen is not displayed";
    private static final String CHANGE_EMAIL_SCREEN_DID_NOT_OPEN = "'Change Email' screen did not open";
    private static final String MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN =
            "Manage your MyDisney account overlay didn't open";

    @BeforeMethod
    public void handleAlert() {
        super.handleAlert();
    }

    public void setAppToAccountSettings() {
        setAppToHomeScreen(getUnifiedAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67128"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAccountDisplay() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        String manageDeviceText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.DEVICE_MANAGEMENT_BUTTON_LABEL.getText());
        String accountSectionEditProfileLinkText = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.ACCOUNT_EDIT_PROFILE.getText());

        setAppToAccountSettings();

        accountPage.waitForAccountPageToOpen();
        sa.assertTrue(accountPage.getNavBackArrow().isElementPresent(), BACK_BUTTON_NOT_DISPLAYED);
        sa.assertTrue(accountPage.getStaticTextByLabelContains(getUnifiedAccount().getEmail()).isPresent(),
                "User Email address was not displayed");
        sa.assertTrue(accountPage.getManageMyAccountCell().isPresent(),
                "Manage with MyDisney link was not displayed");
        sa.assertTrue(
                accountPage.getStaticTextByLabelContains(
                        getLocalizationUtils().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.HIDDEN_PASSWORD.getText()
                        )).isPresent(), "User Password (hidden) was not displayed");
        sa.assertTrue(
                accountPage.getStaticTextByLabel(
                        getLocalizationUtils().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.SUBSCRIPTIONS_TITLE.getText()
                        )).isPresent(), "Subscriptions header not displayed");
        sa.assertTrue(accountPage.isSubscriptionCellPresent(), "Subscription cell was not displayed");
        sa.assertTrue(accountPage.isAccessAndSecurityTextPresent(), "Access & Security text was not displayed");
        sa.assertTrue(accountPage.getManageDevicesText().equals(manageDeviceText),
                "Manage Devices text was not displayed");
        sa.assertTrue(
                accountPage.getStaticTextByLabel(
                        getLocalizationUtils().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.ACCOUNT_SETTINGS_HEADER.getText()
                        )).isPresent(), "Settings header not displayed");
        sa.assertTrue(accountPage.isRestrictProfilesContainerPresent(),
                "Restrict Profile Creation container was not displayed");
        sa.assertTrue(accountPage.isEditProfilesLinkPresent(), "Edit Profiles link was not displayed");
        sa.assertTrue(accountPage.getEditProfileLink().getText().equals(accountSectionEditProfileLinkText),
                "Edit Profiles text was not displayed");
        accountPage.swipe(accountPage.getAccountManagementTextElement());
        sa.assertTrue(accountPage.isAccountManagementLinkPresent(),
                "Account Management link was not displayed");
        sa.assertTrue(accountPage.isAccountManagementTextPresent(),
                "Account Management text was not displayed");

        accountPage.clickNavBackBtn();
        sa.assertTrue(moreMenuPage.isOpened(), MORE_MENU_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67132"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifySubscriptionDetails_DirectBillingWeb() {
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM)));

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isSubscriptionMessageDisplayed(),
                "Direct Billing Web subscription message was not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67144", "XMOBQA-67150", "XMOBQA-75512"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangePasswordUI() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.getEditPasswordButton().click();
        String otp = getOTPFromApi(getUnifiedAccount());

        sa.assertTrue(oneTimePasscodePage.isOpened(),
                "OTP entry page was not opened");

        oneTimePasscodePage.enterOtpValue(otp);

        sa.assertTrue(changePasswordPage.isOpened(),
                "Change Password screen was not opened");

        sa.assertTrue(changePasswordPage.isPasswordSubtitlePresent(),
                "Change Password subtitle was not displayed");

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesTitlePresent(),
                "Logout All Devices title was not displayed");

        sa.assertTrue(changePasswordPage.isSaveAndContinueBtnPresent(),
                "Save & Continue button was not displayed");

        sa.assertTrue(changePasswordPage.isCancelBtnPresent(),
                "Cancel button was not displayed");

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesUnchecked(),
                "Logout All Devices was not unchecked by default");

        changePasswordPage.clickLogoutAllDevices();

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesChecked(),
                "Logout All Devices was not checked");

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesPasswordCopyDisplayed(),
                "Logout All Devices password text was not displayed");

        changePasswordPage.submitNewPasswordValue("invalid" + "\n");

        sa.assertTrue(changePasswordPage.isInvalidPasswordErrorDisplayed(),
                "Invalid Password error was not displayed");

        changePasswordPage.clickHeader();
        changePasswordPage.clickCancelBtn();

        sa.assertTrue(accountPage.isOpened(),
                "User was not directed back to Account Settings after cancelling the password change");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67152"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangePasswordWithoutLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase changePasswordPage =
                initPage(DisneyPlusChangePasswordIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.getEditPasswordButton().click();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), "One time passcode screen is not displayed");

        String otp = getOTPFromApi(getUnifiedAccount());
        oneTimePasscodePage.enterOtpValueDismissKeys(otp);
        Assert.assertTrue(changePasswordPage.isChooseNewPasswordPageOpen(),
                "Choose new password page did not open");
        changePasswordPage.submitNewPasswordValue(NEW_PASSWORD);
        changeEmailPage.getKeyboardDoneButton().clickIfPresent();
        changeEmailPage.clickBackToDisneyBtn();
        Assert.assertTrue(accountPage.isOpened(),
                "User was not directed back to 'Account Settings' after changing their password");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68917"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangePasswordWithLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.getEditPasswordButton().click();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), "One time passcode screen is not displayed");
        String otp = getOTPFromApi(getUnifiedAccount());
        oneTimePasscodePage.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(changePasswordPage.isChooseNewPasswordPageOpen(),
                "Choose new password page did not open");
        changePasswordPage.clickLogoutAllDevices();
        getUnifiedAccount().setUserPass(NEW_PASSWORD);
        changePasswordPage.submitNewPasswordValue(NEW_PASSWORD);
        changePasswordPage.getKeyboardDoneButton().clickIfPresent();

        changeEmailPage.clickLogoutBtn();
        Assert.assertTrue(welcomePage.isOpened(),
                "User was not logged out and returned to the Welcome screen after submitting the new password");

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(),
                "User was not able to log in successfully with the new password");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67134"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailUI() {
        DisneyPlusOneTimePasscodeIOSPageBase otpPage = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase accountPage = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = new DisneyPlusChangeEmailIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());
        Assert.assertTrue(accountPage.isOpened(), ACCOUNT_PAGE_NOT_DISPLAYED);
        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();

        String otp = getOTPFromApi(getUnifiedAccount());

        Assert.assertTrue(otpPage.isOpened(), "OTP entry page was not opened");

        otpPage.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(changeEmailPage.isOpened(), CHANGE_EMAIL_SCREEN_DID_NOT_OPEN);

        hideKeyboard();

        sa.assertTrue(changeEmailPage.getMyDisneyLogo().isPresent(), "MyDisney logo not present");
        sa.assertTrue(changeEmailPage.isHeadlineSubtitlePresent(), "'Change Email' subtitle was not displayed");
        sa.assertTrue(changeEmailPage.isCurrentEmailShown(getUnifiedAccount().getEmail()),
                "'Change Email' display of user email was not shown");
        sa.assertTrue(changeEmailPage.isNewEmailHeaderPresent(),
                "'Change Email' text entry header was not displayed");
        sa.assertTrue(changeEmailPage.getTextEntryField().isPresent(), "'Email field' not present");
        Assert.assertTrue(changeEmailPage.isLogoutAllDevicesUnchecked(),
                "'Change Email' device logout checkbox was not unchecked by default");
        sa.assertTrue(changeEmailPage.isLogoutOfAllDevicesTextPresent(), "'Logout of all devices' text not present");
        changeEmailPage.clickLogoutAllDevices();
        sa.assertTrue(changeEmailPage.isLogoutAllDevicesChecked(), "'Logout All Devices' was not checked");
        sa.assertTrue(changeEmailPage.getSaveAndContinueButton().isPresent(),
                "'Save and Continue' button not present");
        sa.assertTrue(changeEmailPage.getCancelButton().isPresent(), "Cancel button not present");
        sa.assertTrue(changeEmailPage.isLearnMoreAboutMyDisney(), "'Learn more about MyDisney' text not present");

        changeEmailPage.submitNewEmailAddress("invalid");

        sa.assertTrue(changeEmailPage.isChangeEmailFormatErrorDisplayed(),
                "'Invalid Email' error was not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-70695"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailWithoutLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage =
                initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        Assert.assertTrue(accountPage.isOpened(), ACCOUNT_PAGE_NOT_DISPLAYED);
        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);

        String otp = getOTPFromApi(getUnifiedAccount());

        oneTimePasscodePage.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(changeEmailPage.isOpened(), CHANGE_EMAIL_SCREEN_DID_NOT_OPEN);
        String newEmail = generateGmailAccount();
        getUnifiedAccount().setEmail(newEmail);
        oneTimePasscodePage.getTextEntryField().click();
        changeEmailPage.submitNewEmailAddress(newEmail);
        Assert.assertTrue(changeEmailPage.isConfirmationPageOpen(),
                "User was not directed to Confirmation Page");
        Assert.assertTrue(changeEmailPage.isNewEmailShownOnSuccessPage(newEmail),
                "New Email is not displayed on success page");

        changeEmailPage.clickBackToDisneyBtn();
        Assert.assertTrue(accountPage.isOpened(),
                "User was not returned to Account Settings after submitting the new email");
        Assert.assertTrue(accountPage.isChangeLinkPresent(newEmail),
                "The User's new email address was not displayed as expected");

        accountPage.clickMoreTab();
        moreMenuPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT);

        Assert.assertTrue(welcomePage.isOpened(),
                "User was not logged out and returned to the Welcome screen after logout");
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(),
                "User was not able to log in successfully with the new email");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68921"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailWithLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage =
                initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();

        String otp = getOTPFromApi(getUnifiedAccount());
        oneTimePasscodePage.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(changeEmailPage.isOpened(), "'Change Email' screen was not opened");

        hideKeyboard();

        changeEmailPage.clickLogoutAllDevices();
        String newEmail = generateGmailAccount();
        getUnifiedAccount().setEmail(newEmail);

        changeEmailPage.submitNewEmailAddress(newEmail);
        changeEmailPage.clickLogoutBtn();
        Assert.assertTrue(welcomePage.isOpened(),
                "User was not logged out and returned to the Welcome screen after submitting the new email");

        setAppToHomeScreen(getUnifiedAccount());

        Assert.assertTrue(homePage.isOpened(),
                "User was not able to log in successfully with the new email");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67138"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailSelectCancel() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);

        setAppToAccountSettings(getUnifiedAccount());
        accountPage.waitForAccountPageToOpen();
        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();

        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);
        changeEmailPage.clickCancelBtn();

        Assert.assertTrue(accountPage.isOpened(),
                "User was not returned to the Account page after cancelling new email submission");
        Assert.assertTrue(accountPage.getStaticTextByLabelContains(getUnifiedAccount().getEmail())
                        .isPresent(),
                "User's email didn't stay same after cancelling new email submission");
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75206"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailIncorrectOTPInlineError() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        String incorrectOTP = "888888";

        setAppToAccountSettings(getUnifiedAccount());
        accountPage.waitForAccountPageToOpen();
        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();

        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);
        oneTimePasscodePage.enterOtp(incorrectOTP);
        oneTimePasscodePage.clickPrimaryButton();
        Assert.assertTrue(oneTimePasscodePage.isOtpIncorrectErrorPresent(),
                "Inline error message for OTP is not displayed");
        Assert.assertTrue(oneTimePasscodePage.isOpened(),
                "Screen transitioned away from the One time passcode screen");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67140"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailCancelOnEmailPage() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);
        String otp = getOTPFromApi(getUnifiedAccount());
        oneTimePasscodePage.enterOtpValueDismissKeys(otp);
        Assert.assertTrue(changeEmailPage.isOpened(), CHANGE_EMAIL_SCREEN_DID_NOT_OPEN);
        changeEmailPage.clickCancelBtn();
        Assert.assertTrue(accountPage.isOpened(), "User is not taken back to the account page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75208"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void  testAccountSettingsEditProfileButton() {
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        String doneButtonLabel = "Done";

        setAppToAccountSettings(getUnifiedAccount());
        accountPage.waitForAccountPageToOpen();
        Assert.assertTrue(accountPage.isEditProfilesLinkPresent(), "Edit Profiles link was not displayed");

        accountPage.tapEditProfilesLink();
        Assert.assertTrue(editProfilePage.isEditProfilesTitlePresent(), "Edit profile screen is not displayed");
        Assert.assertTrue(editProfilePage.isEditModeProfileIconPresent(DEFAULT_PROFILE),
                "Profiles are not in Edit Mode (pencil shown)");
        Assert.assertTrue(editProfilePage.isEditProfileImageDisplayed(),
                "Avatar Image was not displayed");
        Assert.assertTrue(editProfilePage.getTypeButtonByLabel(doneButtonLabel).isElementPresent(),
                "Done button was not displayed");
        Assert.assertTrue(whoIsWatchingPage.isAddProfileBtnPresent(),
                "Add Profile Icon was not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67160"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAccountsCommunicationSettings() {
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.waitForAccountPageToOpen();
        accountPage.swipe(accountPage.getAccountManagementTextElement());
        Assert.assertTrue(accountPage.isAccountManagementLinkPresent(),
                "Account Management link was not displayed");
        accountPage.tapAccountManagementLink();
        accountPage.waitForPresenceOfAnElement(accountPage.getWebviewUrlBar());
        Assert.assertTrue(accountPage.isAccountManagementFAQWebViewDisplayed(),
                "Account Management FAQ web page did not open");
        relaunch();
        Assert.assertTrue(accountPage.isOpened(),
                "User was not returned to the Account page after navigating back from webview");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75207"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyChangePasswordInCorrectOTPInlineError() {
        String incorrectOTP = "123456";
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.getEditPasswordButton().click();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);
        oneTimePasscodePage.enterOtp(incorrectOTP);
        oneTimePasscodePage.clickPrimaryButton();
        Assert.assertTrue(oneTimePasscodePage.isOtpIncorrectErrorPresent(),
                "Inline error message for OTP is not displayed");
        Assert.assertTrue(oneTimePasscodePage.isOpened(),
                "Screen transitioned away from the One time passcode screen");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67146"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyChangePasswordCancelButtonOnOTPScreen() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        setAppToAccountSettings(getUnifiedAccount());
        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.getEditPasswordButton().click();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);
        oneTimePasscodePage.clickCancelBtn();
        Assert.assertTrue(accountPage.isOpened(),
                "Account page not opened after clicking Cancel button on OTP screen");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75515"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEmailChangeErrorWhenEmailNotFormatted() {
        String emailWithoutAtSymbol = "qait.disneystreaminggmail.com";
        String emailWithoutDot = "qaitdisneystreaminggmail";
        String emailNotFormattedErrorMessage = "Email Properly not formatted error message not displayed";
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);
        String otp = getOTPFromApi(getUnifiedAccount());
        oneTimePasscodePage.enterOtpValueDismissKeys(otp);
        Assert.assertTrue(changeEmailPage.isOpened(), CHANGE_EMAIL_SCREEN_DID_NOT_OPEN);
        changeEmailPage.submitNewEmailAddress(emailWithoutAtSymbol);
        sa.assertTrue(changeEmailPage.isChangeEmailFormatErrorDisplayed(), emailNotFormattedErrorMessage);

        changeEmailPage.submitNewEmailAddress(emailWithoutDot);
        sa.assertTrue(changeEmailPage.isChangeEmailFormatErrorDisplayed(), emailNotFormattedErrorMessage);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75516"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyChangeEmailErrorUseExistingEmail() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        setAppToAccountSettings(getUnifiedAccount());

        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(accountPage.waitForManageMyDisneyAccountOverlayToOpen(getUnifiedAccount()),
                MANAGE_MYDISNEY_ACCOUNT_OVERLAY_DID_NOT_OPEN);
        accountPage.tapEditEmailButton();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_SCREEN_IS_NOT_DISPLAYED);
        String otp = getOTPFromApi(getUnifiedAccount());
        oneTimePasscodePage.enterOtpValueDismissKeys(otp);
        Assert.assertTrue(changeEmailPage.isOpened(), CHANGE_EMAIL_SCREEN_DID_NOT_OPEN);
        changeEmailPage.submitNewEmailAddress(getUnifiedAccount().getEmail());
        Assert.assertTrue(changeEmailPage.isAlreadyInUseEmailErrorMessageDisplayed(),
                "Already In Use Email Error message not displayed");
    }

    //Helper methods

    private void setAppToAccountSettings(UnifiedAccount account) {
        setAppToHomeScreen(account);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }
}
