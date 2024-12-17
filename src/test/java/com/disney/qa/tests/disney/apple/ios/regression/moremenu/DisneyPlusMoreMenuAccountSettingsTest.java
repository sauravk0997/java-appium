package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.account.*;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;

import static com.disney.qa.common.constant.IConstantHelper.US;


public class DisneyPlusMoreMenuAccountSettingsTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String NEW_PASSWORD = "TestPass1234!";
    private static final String MONTHLY = "Monthly";
    private static final String YEARLY = "Yearly";
    private static final String GOOGLE_URL = "accounts.google.com";
    private static final String HULU_URL = "auth.hulu.com";
    private static final String ROKU_URL = "my.roku.com";
    private static final String AMAZON_URL = "amazon.com";
    private static final String MERCADOLIBRE_URL = "mercadolibre.com";
    private DisneyEntitlement disneyEntitlements;
    private static final String EDIT_ICON = "editIcon";

    @BeforeMethod
    public void handleAlert() {
        super.handleAlert();
    }

    @DataProvider(name = "disneyPlanTypes")
    public Object[][] disneyPlanTypes() {
        return new Object[][]{
                {MONTHLY}, {YEARLY}
        };
    }

    public void setAppToAccountSettings() {
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67128"})
    @Test(description = "Verify the Account submenu display elements are present", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAccountDisplay() {
        setAppToAccountSettings(getAccount());
        SoftAssert sa = new SoftAssert();
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        accountPage.waitForAccountPageToOpen();
        sa.assertTrue(accountPage.getBackArrow().isElementPresent(), "Back arrow was not displayed");
        sa.assertTrue(accountPage.getStaticTextByLabel(getAccount().getEmail()).isPresent(),
                "User Email address was not displayed");
        sa.assertTrue(accountPage.getManageWithMyDisneyButton().isPresent(),
                "Manage with MyDisney link was not displayed");
        sa.assertTrue(
                accountPage.getStaticTextByLabel(
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
        sa.assertTrue(accountPage.isManageDevicesTextPresent(), "Manage Devices text was not displayed");
        sa.assertTrue(
                accountPage.getStaticTextByLabel(
                        getLocalizationUtils().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.ACCOUNT_SETTINGS_HEADER.getText()
                        )).isPresent(), "Settings header not displayed");
        sa.assertTrue(accountPage.isRestrictProfilesContainerPresent(),
                "Restrict Profile Creation container was not displayed");
        sa.assertTrue(accountPage.isEditProfilesLinkPresent(), "Edit Profiles link was not displayed");
        sa.assertTrue(accountPage.isEditProfilesTextPresent(), "Edit Profiles text was not displayed");
        accountPage.swipe(accountPage.getAccountManagementTextElement());
        sa.assertTrue(accountPage.isAccountManagementLinkPresent(),
                "Account Management link was not displayed");
        sa.assertTrue(accountPage.isAccountManagementTextPresent(),
                "Account Management text was not displayed");

        accountPage.getBackArrow().click();
        sa.assertTrue(moreMenuPage.isOpened(), "More Menu page was not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61571"})
    @Test(description = "Verify that the correct description for D+ Premium displayed", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifySubscriptionDetailsDisneyPlus() {
        setAppToAccountSettings(getAccount());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isDisneyPlusPremiumSubscriptionPresent(),
                "D+ Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openDisneyPlusPremiumWebView();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61573"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifySubscriptionDetails_LegacyDisneyBundle() {
        setAccount(createAccountWithSku(
                        DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                        getLocalizationUtils().getLocale(),
                        getLocalizationUtils().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        setAppToAccountSettings();
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(disneyPlusAccountIOSPageBase.isBamtechBundleSubscriptionTitlePresent(),
                "Legacy Disney Bundle subscription title was not displayed");
        sa.assertTrue(disneyPlusAccountIOSPageBase.isBamtechBundleSubscriptionMessagePresent(),
                "Legacy Disney Bundle subscription message was not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61575"})
    @Test(description = "Verify that the correct description for Hulu Bundle displayed", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySubscriptionDetails_HuluBundle() {
        setAccount(createAccountWithSku(DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isHuluBundleSubscriptionMessagePresent(),
                "Hulu Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openHuluBundleWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(HULU_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75401"})
    @Test(description = "Verify that the correct description for Google displayed", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySubscriptionDetails_GooglePlay() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_GOOGLE_YEARLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isGoogleSubscriptionTitlePresent(),
                "Google Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isGoogleSubscriptionMessagePresent(),
                "Google Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openGoogleWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(GOOGLE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75402"})
    @Test(description = "Verify that the correct description for Roku displayed", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySubscriptionDetails_Roku() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_ROKU_YEARLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isRokuSubscriptionTitlePresent(),
                "Roku Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isRokuSubscriptionMessagePresent(),
                "Roku Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openRokuWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(ROKU_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75403"})
    @Test(description = "Verify that the correct description for Amazon displayed", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySubscriptionDetails_Amazon() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_AMAZON_YEARLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isAmazonSubscriptionTitlePresent(),
                "Amazon Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isAmazonSubscriptionMessagePresent(),
                "Amazon Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openAmazonWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(AMAZON_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61613", "XMOBQA-66500"})
    @Test(description = "Verify that the correct description for Mercado Libre displayed", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySubscriptionDetails_MercadoLibre() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_MERCADOLIBRE_MX_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMercadolibreSubscriptionTitlePresent(),
                "Mercado Libre Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMercadoLibreSubscriptionMessagePresent(),
                "Mercado Libre Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openMercadoLibreWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(MERCADOLIBRE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67166", "XMOBQA-75307", "XMOBQA-67142"})
    @Test(description = "Verify the 'Unverified email badge is displayed in the More Menu and Account submenu", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyUnverifiedAccountFunctions() throws URISyntaxException, InterruptedException {
        SoftAssert sa = new SoftAssert();
        //Builds a DisneyAccount object with existing credentials that are already configured for test needs
        DisneyProfile profile = new DisneyProfile();
        profile.setProfileName("Profile");
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        DisneyOffer offer = getAccountApi().lookupOfferToUse("monthly");
        request.addEntitlement(new DisneyEntitlement().setOffer(offer));
        DisneyAccount unverifiedAccount = getAccountApi().createAccount(request);
        getAccountApi().patchAccountVerified(unverifiedAccount, false, PatchType.ACCOUNT, null);
        //Add pause cause patch takes a little while
        pause(15);
        setAccount(unverifiedAccount);

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);

        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        if (homePage.getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FOOTER_MANAGE_PREFERENCE.getText())).isPresent()) {
            sa.assertTrue(oneTrustPage.isOpened(), "One trust page did not open.");
            oneTrustPage.tapCloseButton();
        }
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isAccountUnverifiedBadgeDisplayed(),
                "XMOBQA-62564 - Unverified Account badge was not displayed");

        disneyPlusMoreMenuIOSPageBase.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);

        sa.assertTrue(disneyPlusAccountIOSPageBase.isVerifyAccountHeaderPresent(),
                "XMOBQA-62566 - Verify Account header was not displayed");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isVerifyAccountLinkPresent(),
                "XMOBQA-62566 - Verify Account link was not present");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(getAccount().getEmail()),
                "XMOBQA-62570 - Change Email link was not displayed");

        sa.assertFalse(disneyPlusAccountIOSPageBase.isChangeLinkActive(getAccount().getEmail()),
                "XMOBQA-62570 - Change Email link was not disabled");

        disneyPlusAccountIOSPageBase.clickVerifyAccountLink();

        sa.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "XMOBQA-62568 -  OTP Page was not opened");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67144", "XMOBQA-67150", "XMOBQA-75512"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangePasswordUI() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        DisneyAccount otpAccount = getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage());
        setAppToAccountSettings(otpAccount);

        Date startTime = getEmailApi().getStartTime();
        accountPage.clickManageWithMyDisneyButton();
        accountPage.getDynamicRowButtonLabel(EDIT_ICON, 2).click();
        String otp = getOTPFromApi(startTime, otpAccount);

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

        DisneyAccount otpAccount = getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage());
        setAppToAccountSettings(otpAccount);

        Date startTime = getEmailApi().getStartTime();
        accountPage.clickChangePasswordCell();
        accountPage.getDynamicRowButtonLabel(EDIT_ICON, 2).click();

        String otp = getOTPFromApi(startTime, otpAccount);

        oneTimePasscodePage.enterOtp(otp);
        oneTimePasscodePage.clickPrimaryButton();
        changePasswordPage.submitNewPasswordValue(NEW_PASSWORD);
        changeEmailPage.getKeyboardDoneButton().clickIfPresent();
        changeEmailPage.clickBackToDisneyBtn();
        Assert.assertTrue(accountPage.isOpened(),
                "User was not directed back to 'Account Settings' after changing their password");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67134", "XMOBQA-70695"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailUI() {
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        DisneyAccount otpAccount = getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage());
        setAppToAccountSettings(otpAccount);

        disneyPlusApplePageBase.clickManageWithMyDisneyButton();
        Date startTime = getEmailApi().getStartTime();
        disneyPlusAccountIOSPageBase.clickEditEmail(otpAccount.getEmail());

        String otp = getOTPFromApi(startTime, otpAccount);

        Assert.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "XMOBQA-61553 - OTP entry page was not opened");

        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(disneyPlusChangeEmailIOSPageBase.isOpened(),
                "XMOBQA-61551 - 'Change Email' screen was not opened");

        hideKeyboard();

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isHeadlineSubtitlePresent(),
                "XMOBQA-61551 - 'Change Email' subtitle was not displayed");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isCurrentEmailShown(otpAccount.getEmail()),
                "XMOBQA-61551 - 'Change Email' display of user email was not shown");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isNewEmailHeaderPresent(),
                "XMOBQA-61551 - 'Change Email' text entry header was not displayed");

        Assert.assertTrue(disneyPlusChangeEmailIOSPageBase.isLogoutAllDevicesUnchecked(),
                "XMOBQA-61551 - 'Change Email' device logout checkbox was not unchecked by default");

        disneyPlusChangeEmailIOSPageBase.clickLogoutAllDevices();

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isLogoutAllDevicesChecked(),
                "XMOBQA-61551 - 'Logout All Devices' was not checked");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isLearnMoreAboutMyDisney(),
                "XMOBQA-61551 - 'Logout All Devices' password text was not displayed");

        disneyPlusChangeEmailIOSPageBase.submitNewEmailAddress("invalid");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isAttributeValidationErrorMessagePresent(),
                "XMOBQA-61551 - 'Invalid Email' error was not displayed");

        disneyPlusChangeEmailIOSPageBase.clickCancelBtn();

        sa.assertTrue(disneyPlusAccountIOSPageBase.isOpened(),
                "XMOBQA-61555 - User was not returned to the Account Settings page after cancelling new email submission");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-70695"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailWithoutLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);

        DisneyAccount otpAccount = getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage());
        setAppToAccountSettings(otpAccount);

        accountPage.clickManageWithMyDisneyButton();
        Date startTime = getEmailApi().getStartTime();
        accountPage.clickEditEmail(otpAccount.getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), "One time passcode screen is not displayed");

        String otp = getOTPFromApi(startTime, otpAccount);

        oneTimePasscodePage.enterOtpValueDismissKeys(otp);

        String newEmail = generateGmailAccount();
        oneTimePasscodePage.getTextEntryField().click();
        changeEmailPage.submitNewEmailAddress(newEmail);
        Assert.assertTrue(changeEmailPage.isConfirmationPageOpen(),
                "User was not directed to Confirmation Page");

        changeEmailPage.clickBackToDisneyBtn();
        Assert.assertTrue(accountPage.isOpened(),
                "User was not returned to Account Settings after submitting the new email");
        Assert.assertTrue(accountPage.isChangeLinkPresent(newEmail),
                "The User's new email address was not displayed as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67140"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailSelectCancel() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);

        setAppToAccountSettings(getAccount());
        accountPage.waitForAccountPageToOpen();
        accountPage.clickManageWithMyDisneyButton();
        Assert.assertTrue(waitUntil(ExpectedConditions.visibilityOfElementLocated(
                accountPage.getStaticTextByLabelContains(getAccount().getEmail()).getBy()), 15),
                "Manage your MyDisney account overlay didn't open");
        accountPage.tapEditEmailButton();

        Assert.assertTrue(oneTimePasscodePage.isOpened(), "One time passcode screen is not displayed");
        changeEmailPage.clickCancelBtn();

        Assert.assertTrue(accountPage.isOpened(),
                "User was not returned to the Account page after cancelling new email submission");
        Assert.assertTrue(accountPage.getStaticTextByLabelContains(getAccount().getEmail())
                        .isPresent(),
                "User's email didn't stay same after cancelling new email submission");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-70695"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void testChangeEmailWithLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =
                initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage =
                initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);

        DisneyAccount otpAccount = getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage());
        setAppToAccountSettings(otpAccount);

        accountPage.clickManageWithMyDisneyButton();
        Date startTime = getEmailApi().getStartTime();
        accountPage.clickEditEmail(otpAccount.getEmail());

        String otp = getOTPFromApi(startTime, otpAccount);
        oneTimePasscodePage.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(changeEmailPage.isOpened(),
                "'Change Email' screen was not opened");

        hideKeyboard();

        changeEmailPage.clickLogoutAllDevices();
        String newEmail = generateGmailAccount();
        otpAccount.setEmail(newEmail);

        changeEmailPage.submitNewEmailAddress(newEmail);
        changeEmailPage.clickLogoutBtn();
        Assert.assertTrue(welcomePage.isOpened(),
                "User was not logged out and returned to the Welcome screen after submitting the new email");

        setAppToHomeScreen(otpAccount, otpAccount.getProfiles().get(0).getProfileName());

        Assert.assertTrue(disneyPlusHomeIOSPageBase.isOpened(),
                "User was not able to log in successfully with the new email");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75208"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void  testAccountSettingsEditProfileButton() {
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        String doneButtonLabel = "Done";

        setAppToAccountSettings(getAccount());
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
        setAppToAccountSettings(getAccount());

        accountPage.waitForAccountPageToOpen();
        accountPage.swipe(accountPage.getAccountManagementTextElement());
        Assert.assertTrue(accountPage.isAccountManagementLinkPresent(),
                "Account Management link was not displayed");
        accountPage.getAccountManagementLink().click();
        accountPage.waitForPresenceOfAnElement(accountPage.getWebviewUrlBar());
        Assert.assertTrue(accountPage.isAccountManagementFAQWebViewDisplayed(),
                "Account Management FAQ web page did not open");
        relaunch();
        Assert.assertTrue(accountPage.isOpened(),
                "User was not returned to the Account page after navigating back from webview");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66501"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyIAPBillingHoldWithPartnerSub() {
        SoftAssert sa = new SoftAssert();
        String firstName = "Test";
        String lastName = "User";
        List<DisneyOrder> billingOrder = new LinkedList<>();
        List<DisneySkuParameters> iapOnHold = Arrays.asList(DisneySkuParameters.DISNEY_IAP_GOOGLE_MONTHLY, DisneySkuParameters.DISNEY_IAP_GOOGLE_YEARLY,
                DisneySkuParameters.DISNEY_IAP_AMAZON_MONTHLY, DisneySkuParameters.DISNEY_IAP_AMAZON_YEARLY,
                DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, DisneySkuParameters.DISNEY_IAP_APPLE_YEARLY,
                DisneySkuParameters.DISNEY_IAP_ROKU_MONTHLY, DisneySkuParameters.DISNEY_IAP_ROKU_YEARLY);
        List<DisneySkuParameters> activeProviders = Arrays.asList(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE,
                DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE);

        iapOnHold.forEach(iap -> activeProviders.forEach(provider -> {
            LOGGER.info("Entitlement combo: {}/{}", iap, provider);
            billingOrder.add(DisneyOrder.SET_BILLING_HOLD);
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(generateGmailAccount())
                    .language(getLocalizationUtils().getUserLanguage())
                    .country(getLocalizationUtils().getLocale()).build();

            createDisneyAccountRequest.addSku(iap);
            createDisneyAccountRequest.setOrderSettings(billingOrder);
            getAccount().getOrderSettings().clear();

            try {
                disneyEntitlements.setOffer(getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE));
                disneyEntitlements.setSubVersion(SUBSCRIPTION_V2);
                getSubscriptionApi().entitleAccount(getAccount(), Arrays.asList(disneyEntitlements));

                performIapHoldProviderSubscriptionCheck(sa, provider, iap);
            } catch (URISyntaxException | JSONException e) {
                LOGGER.info("Something went wrong with generating the account with entitlements {}/{}", iap, provider, e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        sa.assertAll();
    }

    //Helper methods

    private void setAppToAccountSettings(DisneyAccount account) {
        setAppToHomeScreen(account);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    /**
     * Performs the Paused Entitlement check by checking for the passed URL value for the given provider.
     * Checks both the Partner and Direct subscription tab navigation.
     *
     * @param url - The expected URL (fragment, whole path is not required) when opening the webview
     * @return - The soft assertion in the test for which it was called.
     */
    private SoftAssert performPausedEntitlementCheck(DisneySkuParameters sku, String url, DisneyPlusPaywallIOSPageBase.PlanType planName, SoftAssert sa) {
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        setAppToAccountSettings();
        if (!disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent()) {
            logout();
            setAppToAccountSettings();
        }
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent(),
                "Stacked Subscription header text was not displayed. Unable to verify Paused status.");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isDirectBillingPausedSubscriptionDisplayed(planName),
                "Direct Billing 'Paused' subscription was not displayed");

        disneyPlusAccountIOSPageBase.openBillingProvider(sku);
        pause(4);
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                String.format("Browser webview did not open on navigation for the following SKU: %s", sku));
        pause(2);
        sa.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(url),
                String.format("Webview did not open to the expected URL: %s", url));

        relaunch();
        disneyPlusAccountIOSPageBase.clickPausedDirectBillingContainer(planName);
        sa.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                String.format("Webview did not open to the expected URL: %s", DISNEY_URL));
        return sa;
    }

    /**
     * Performs the Held Entitlement check by checking for the the presence of the active provider and the lack of
     * display for the held IAP provider.
     */
    private void performIapHoldProviderSubscriptionCheck(SoftAssert sa, DisneySkuParameters activeProvider, DisneySkuParameters heldIapService) {
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        try {
            setAppToAccountSettings();

            sa.assertTrue(disneyPlusAccountIOSPageBase.isSingleSubHeaderPresent(),
                    "Single Subscription header text was not displayed");

            sa.assertFalse(disneyPlusAccountIOSPageBase.isBillingProviderCellPresent(heldIapService, 5),
                    "Held IAP subscription cell was displayed unexpectedly for sku: " + heldIapService);

            sa.assertTrue(disneyPlusAccountIOSPageBase.isBillingProviderCellPresent(activeProvider),
                    "Active Partner cell was not displayed for sku: " + activeProvider);

            logout();
        } catch (NoSuchElementException e) {
            LOGGER.error("There was a navigation error. Iteration was not completed");
            sa.fail(String.format("Something went wrong with navigation for the account with entitlement combo: %s/%s", activeProvider, heldIapService), e);
            restart();
        }
    }
}
