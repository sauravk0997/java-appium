package com.disney.qa.tests.disney.android.mobile.onboarding;

import com.disney.exceptions.OTPRetrievalException;
import com.disney.qa.api.account.AccountBlockReasons;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_EMAIL_ERROR;

public class DisneyPlusAndroidArielLoginTest extends BaseDisneyTest {

    private static final String EMAIL_SUBJECT = "Your one-time passcode";
    private static final String INVALID_PIN_CODE = "123456";
    private static final String NEW_PASSWORD = "G0D1sn3yQ@";
    private static final String INVALID_PASSWORD = "BAD PASSWORD";
    private static final String NEW_EMAIL = "AndroidQATest@disneystreaming.com";
    private static final String INVALID_EMAIL_FORMAT = "InvalidEmailFormat@";
    private static final String HIDDEN_PASSWORD = "••••••••••";
    private static final String DOB_OVER_18 = "01012000";

    ThreadLocal<VerifyEmail> verifyEmail = new ThreadLocal<>();

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67739"})
    @Test(description = "Verify UI elements of the OTP display during Login", groups = {"Onboarding", "ArielLogIn"})
    public void testOneTimePasscodeInterfaceElements() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1652"));

        DisneyPlusOneTimePasscodePageBase otpPage = openOtpDisplay();
        SoftAssert sa = new SoftAssert();

        androidUtils.get().hideKeyboard();

        Assert.assertTrue(otpPage.isOpened(), "OTP page was not displayed");

        sa.assertTrue(otpPage.isGenericBackButtonPresent(), "Back Button was not displayed");

        sa.assertTrue(otpPage.isOnboardingDisneyLogoPresent(), "Disney+ logo was not displayed");

        sa.assertEquals(
                otpPage.getTitleText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CHECK_EMAIL_TITLE.getText()),
                "OTP Title did not match dictionary key");

        sa.assertEquals(
                otpPage.getMessageText(),
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.CHECK_EMAIL_COPY.getText()),
                        disneyAccount.get().getEmail()),
                "OTP message did not match dictionary key");

        sa.assertTrue(otpPage.isPinEntryFieldPresent(), "Pin entry field was not displayed");

        sa.assertTrue(otpPage.isStandardButtonPresent(), "Continue button not displayed");

        sa.assertEquals(
                otpPage.getResendEmailText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.RESEND_EMAIL_COPY.getText()),
                "Resend email text does not match dictionary key");

        sa.assertEquals(
                otpPage.getResendButtonText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.RESEND_EMAIL_COPY_2.getText()),
                "Resend button text does not match dictionary key");

        sa.assertTrue(otpPage.doesPinEntryActivateKeyboard(), "Virtual keyboard not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67743"})
    @Test(description = "Verify email generation and UI elements for Resend", groups = {"Onboarding", "ArielLogIn"})
    public void testResendButtonFunctionality() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1653"));

        SoftAssert sa = new SoftAssert();

        createAccount();
        DisneyPlusOneTimePasscodePageBase otpPage = openOtpDisplay();
        androidUtils.get().hideKeyboard();
        pause(30);
        Date startTime = verifyEmail.get().getStartTime();
        otpPage.clickResendButton();

        try {
            verifyEmail.get().getDisneyOTP(
                    disneyAccount.get().getEmail(),
                    EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        } catch (OTPRetrievalException otpe) {
            Assert.fail("Resend button did not generate a new OTP email");
        }

        sa.assertTrue(otpPage.isErrorPresent(), "Dialog box was not displayed");

        sa.assertEquals(
                otpPage.getErrorDialogText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.EMAIL_RESEND_TITLE.getText()),
                "Dialog header text was incorrect.");

        sa.assertEquals(
                otpPage.getErrorMessageText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.EMAIL_RESEND_SUBTITLE.getText()),
                "Dialog message text was incorrect");

        sa.assertEquals(
                otpPage.getCancelButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.BTN_HELP_CENTER.getText()),
                "Help center button text does not match dictionary key");

        sa.assertEquals(
                otpPage.getConfirmButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.BTN_OK.getText()),
                "OK button text does not match dictionary key");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67747"})
    @Test(description = "Verify Forgot Password flow sends a confirmation email", groups = {"Onboarding", "ArielLogIn"})
    public void testValidPinEntry() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1655"));

        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        SoftAssert sa = new SoftAssert();

        createAccount();
        DisneyPlusOneTimePasscodePageBase otpPage = openOtpDisplay();

        Date startTime = verifyEmail.get().getStartTime();

        String otp =
                verifyEmail.get().getDisneyOTP(
                        disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(),
                        EMAIL_SUBJECT, startTime);

        otpPage.clickPinEntry();
        createPasswordPage.editTextByClass.type(otp);
        androidUtils.get().hideKeyboard();
        otpPage.clickStandardButton();

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordOpened(),
                "Create new password screen not displayed");

        createPasswordPage.getEditTextByClass().click();
        createPasswordPage.getEditTextByClass().type(NEW_PASSWORD);
        androidUtils.get().hideKeyboard();
        createPasswordPage.clickStandardButton();

        sa.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(), "Discovery screen not displayed");

        checkAssertions(sa);

        startTime = verifyEmail.get().getStartTime();

        verifyEmail.get().scanEmail(
                disneyAccount.get().getEmail(),
                disneyAccount.get().getUserPass(),
                "Your password has been updated", startTime);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67749"})
    @Test(description = "Log In - Forgot Password Flow - Create a new password page UI", groups = {"Onboarding", "ArielLogIn"})
    public void testLogInForgotPasswordCreateNewPasswordUI() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1656"));

        DisneyPlusOneTimePasscodePageBase oneTimePasscodePageBase = initPage(DisneyPlusOneTimePasscodePageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        createAccount();

        Date startTime = verifyEmail.get().getStartTime();

        openOtpDisplay();
        oneTimePasscodePageBase.clickPinEntry();
        commonPageBase.editTextByClass.click();
        commonPageBase.editTextByClass.type(
                verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(),
                        EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime));

        androidUtils.get().hideKeyboard();
        oneTimePasscodePageBase.clickStandardButton();

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordOpened(),
                "User was not directed to create their new password.");

        sa.assertTrue(createPasswordPage.isDisneyLogoDisplayed(),
                "Disney+ logo was not shown.");

        sa.assertEquals(
                createPasswordPage.getCreateNewPasswordTitleText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CREATE_NEW_PASSWORD.getText()),
                "Create new password title does not match dictionary key");

        sa.assertTrue(verifyCreateNewPasswordPrompt(createPasswordPage.getMessageText()),
                "Create new password message does not match hardcoded reference");

        sa.assertTrue(createPasswordPage.isPasswordEntryFieldDisplayed(),
                "New password text entry was not displayed.");

        sa.assertEquals(
                createPasswordPage.getPasswordInstructionsText(),
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.PASSWORD_REQS_ENHANCED.getText()), "6", "2"),
                "Password instructions do not match dictionary key");

        sa.assertTrue(
                oneTimePasscodePageBase.isTextViewStringDisplayed(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.BTN_CONTINUE.getText())),
                "Continue button is not displayed");

        createPasswordPage.enterNewPassword(NEW_PASSWORD);

        sa.assertTrue(createPasswordPage.isStrengthMeterDisplayed(),
                "Password strength meter was not displayed");

        sa.assertTrue(createPasswordPage.isShowHidePasswordIconDisplayed(),
                "Show/Hide password graphic is not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67751"})
    @Test(description = "Verify invalid password, forgot password flow", groups = {"Onboarding", "ArielLogIn"})
    public void verifyInvalidPasswordForgotPasswordFlow() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1657"));

        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        SoftAssert sa = new SoftAssert();

        createAccount();
        DisneyPlusOneTimePasscodePageBase otpPage = openOtpDisplay();

        Date startTime = verifyEmail.get().getStartTime();

        String otp =
                verifyEmail.get().getDisneyOTP(
                        disneyAccount.get().getEmail(),
                        EmailApi.getOtpAccountPassword(),
                        EMAIL_SUBJECT, startTime);

        otpPage.clickPinEntry();
        createPasswordPage.editTextByClass.type(otp);
        androidUtils.get().hideKeyboard();
        otpPage.clickStandardButton();

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordOpened(),
                "User was not directed to create their new password.");

        androidUtils.get().hideKeyboard();
        createPasswordPage.clickStandardButton();

        sa.assertEquals(
                createPasswordPage.getInputErrorText(),
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                                DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), "6", "2"),
                "Invalid Password error was either not displayed or incorrect for empty submission.");

        createPasswordPage.enterNewPassword("badpassword");

        sa.assertFalse(createPasswordPage.isInputErrorDisplayed(),
                "Error was not removed when typing a new password.");

        androidUtils.get().hideKeyboard();
        createPasswordPage.clickStandardButton();

        sa.assertEquals(
                createPasswordPage.getInputErrorText(),
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                                DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()),"6", "2"),
                "Invalid Password error was either not displayed or incorrect for populated submission.");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67753"})
    @Test(description = "Verifies the display functions during new password flow", groups = {"Onboarding", "ArielLogIn"})
    public void verifyShowHidePasswordFunctions() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1658"));

        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        SoftAssert sa = new SoftAssert();

        createAccount();
        DisneyPlusOneTimePasscodePageBase otpPage = openOtpDisplay();

        Date startTime = verifyEmail.get().getStartTime();
        String otp =
                verifyEmail.get().getDisneyOTP(
                        disneyAccount.get().getEmail(),
                        EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);

        otpPage.clickPinEntry();
        createPasswordPage.editTextByClass.type(otp);
        androidUtils.get().hideKeyboard();
        otpPage.clickStandardButton();

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordOpened(),
                "Create password screen not displayed");

        createPasswordPage.enterNewPassword(NEW_PASSWORD);
        createPasswordPage.getShowPwdToggle().click();

        sa.assertTrue(createPasswordPage.getEditTextByClass().getText().equals(NEW_PASSWORD),
                "Enabling Show Password did not display the entered password");

        createPasswordPage.getShowPwdToggle().click();

        sa.assertEquals(createPasswordPage.getEditTextByClass().getText(), "••••••••••",
                "Enabling Show Password did not hide the entered password");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67755"})
    @Test(description = "Verify the color of the Strength Meter at various stages", groups = {"Onboarding", "ArielLogIn"})
    public void verifyPasswordStrengthMeterDisplay() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1660"));

        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        SoftAssert sa = new SoftAssert();

        createAccount();
        DisneyPlusOneTimePasscodePageBase otpPage = openOtpDisplay();

        Date startTime = verifyEmail.get().getStartTime();
        String otp =
                verifyEmail.get().getDisneyOTP(
                        disneyAccount.get().getEmail(),
                        EmailApi.getOtpAccountPassword(),
                        EMAIL_SUBJECT,
                        startTime);

        otpPage.clickPinEntry();
        createPasswordPage.editTextByClass.type(otp);
        androidUtils.get().hideKeyboard();
        otpPage.clickStandardButton();

        Assert.assertTrue(createPasswordPage.isCreateNewPasswordOpened(),
                "User was not directed to create their new password.");

        createPasswordPage.enterNewPassword("1");

        sa.assertTrue(createPasswordPage.isStatusBarRed(),
                "Password Strength meter was not RED for a Poor password (Strength Score: 1)");

        createPasswordPage.enterNewPassword("1234!@");

        sa.assertTrue(createPasswordPage.isStatusBarRed(),
                "Password Strength meter was not RED for a Poor password (Strength Score: 2)");

        createPasswordPage.enterNewPassword("Test3r!@");

        sa.assertTrue(createPasswordPage.isStatusBarOrange(),
                "Password Strength meter was not ORANGE for a Fair password (Strength Score: 3)");

        createPasswordPage.enterNewPassword("Test3r!@D");

        sa.assertTrue(createPasswordPage.isStatusBarOrange(),
                "Password Strength meter was not ORANGE for a Fair password (Strength Score: 4)");

        createPasswordPage.enterNewPassword(NEW_PASSWORD);

        sa.assertTrue(createPasswordPage.isStatusBarGreen(),
                "Password Strength meter was not GREEN for a Good password (Strength Score: 5)");

        createPasswordPage.enterNewPassword(NEW_PASSWORD + "W00");

        sa.assertTrue(createPasswordPage.isStatusBarGreen(),
                "Password Strength meter was not GREEN for a Good password (Strength Score: 6)");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67741"})
    @Test(description = "Verify the error dialog for an invalid PIN", groups = {"Onboarding", "ArielLogIn"})
    public void testIncorrectPinEntry() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1654"));

        DisneyPlusOneTimePasscodePageBase oneTimePasscodePageBase = initPage(DisneyPlusOneTimePasscodePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        SoftAssert sa = new SoftAssert();

        createAccount();
        openOtpDisplay();
        loginPageBase.editTextByClass.click();
        loginPageBase.editTextByClass.type(INVALID_PIN_CODE);
        androidUtils.get().hideKeyboard();
        oneTimePasscodePageBase.clickStandardButton();

        sa.assertTrue(oneTimePasscodePageBase.isPinErrorTextDisplayed(), "Pin error text was not displayed.");

        sa.assertEquals(
                oneTimePasscodePageBase.getPinErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                        DictionaryKeys.INVALID_PASSCODE.getText()),
                "Invalid pin error text does not match dictionary key");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67236"})
    @Test(description = "Validate correct error message for incorrect password", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("scho2")
    public void verifyIncorrectPasswordInput() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);

        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());

        createPasswordPage.enterNewPassword(INVALID_PASSWORD);
        createPasswordPage.clickStandardButton();

        Assert.assertEquals(
                createPasswordPage.getInputErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                        DictionaryKeys.INVALID_CREDENTIALS_ERROR.getText()),
                "Incorrect Password text does not match dictionary key");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67234"})
    @Test(description = "Validate error message upon null password entry", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("scho2")
    public void verifyNullPasswordInput() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);

        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());

        createPasswordPage.enterNewPassword(null);
        createPasswordPage.clickStandardButton();

        Assert.assertEquals(
                createPasswordPage.getInputErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                        DictionaryKeys.INVALID_NO_PASSWORD_ERROR.getText()),
                "No Password error text does not match dictionary key");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67240"})
    @Test(description = "Verify Sign Up UI", groups = {"Onboarding", "ArielLogIn"})
    public void testLoginScreenTapSignup() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusSignUpPageBase signupPageBase = initPage(DisneyPlusSignUpPageBase.class);
        SoftAssert sa = new SoftAssert();

        sa.assertTrue(welcomePageBase.isLoginButtonPresent(), "Login button not displayed");

        welcomePageBase.continueToLogin();

        sa.assertTrue(loginPageBase.isOpened(), " Login screen not displayed.");

        sa.assertEquals(loginPageBase.getTextFieldText(),"", "Text entry field is not empty");

        loginPageBase.editTextByClass.type(NEW_EMAIL);
        androidUtils.get().hideKeyboard();

        sa.assertTrue(loginPageBase.isSignUpButtonDisplayed(), "Signup button not displayed");

        loginPageBase.clickSignupButton();

        Assert.assertTrue(signupPageBase.isOpened(), "Signup screen not displayed");

        sa.assertTrue(signupPageBase.isGenericBackButtonPresent(), "Back arrow not displayed");

        sa.assertTrue(signupPageBase.isSignupTitleDisplayed(), "Signup title not displayed");

        sa.assertEquals(
                signupPageBase.getSignupTitleText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.SIGN_UP_TITLE.getText()),
                "Signup title to be displayed correctly");

        sa.assertTrue(signupPageBase.isEditTextFieldPresent(), "Text entry field not displayed");

        androidUtils.get().pressBack();

        sa.assertTrue(signupPageBase.isStandardButtonPresent(), "Continue button not displayed");

        sa.assertTrue(signupPageBase.isoptInCheckboxesDisplayed(), "OPT in checkboxes not displayed ");

        sa.assertEquals(signupPageBase.getTextFieldText(), NEW_EMAIL, "Email was not pulled from log in page");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67218"})
    @Test(description = "Verify invalid email format error message", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("ckim1")
    public void verifyInvalidEmailFormatErrorMessage() {
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.continueToLogin();

        loginPageBase.proceedToPasswordMode(INVALID_EMAIL_FORMAT);

        sa.assertEquals(
                disneyPlusCommonPageBase.invalidEmailInputErrorMessage(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        INVALID_EMAIL_ERROR.getText()),
                "Invalid email error text does not match dictionary key");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67230"})
    @Test(description = "Tap on Show & Hide Password Icon", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("scho2")
    public void verifyTapOnShowAndHidePasswordIcon() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);

        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());
        createPasswordPage.enterNewPassword(NEW_PASSWORD);

        sa.assertEquals(createPasswordPage.getEditTextByClass().getText(), HIDDEN_PASSWORD,
                "Password is displayed");

        sa.assertTrue(
                createPasswordPage.isShowHideExpectedStatus(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DictionaryKeys.SHOW_PASSWORD.getText())),
                "Show Password graphic is not displayed");

        createPasswordPage.getShowPwdToggle().click();

        sa.assertTrue(
                createPasswordPage.isShowHideExpectedStatus(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DictionaryKeys.HIDE_PASSWORD.getText())),
                "Hide icon is not flipped to show icon");

        sa.assertEquals(createPasswordPage.getEditTextByClass().getText(), NEW_PASSWORD,
                "Password is not visible");

        createPasswordPage.getShowPwdToggle().click();

        sa.assertEquals(createPasswordPage.getEditTextByClass().getText(),HIDDEN_PASSWORD,
                "Password is still visible");

        sa.assertTrue(
                createPasswordPage.isShowHideExpectedStatus(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                DictionaryKeys.SHOW_PASSWORD.getText())),
                "Show Password graphic is not displayed");

        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-70773"})
    @Test(description = "Log In Existing Star+ Account - No Tied Account Forced Prompt", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("bwatson")
    public void testLogInExistingStarAccountForcedPrompt() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        SoftAssert sa = new SoftAssert();

        accountApi.set(
                new DisneyAccountApi(
                        PLATFORM,
                        DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                        "Star"));

        disneyAccount.set(
                accountApi.get().createExpiredAccount("Star", "BR", "pt", "V1"));

        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());

        commonPageBase.bottomClickView.click();

        sa.assertFalse(commonPageBase.isErrorDialogPresent(), "Error prompt is displayed");

        sa.assertEquals(
                loginPageBase.getInputErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGIN_NO_ACCOUNT.getText()),
                "Log in page email error text does not match dictionary key");

        loginPageBase.clickStandardButton();

        sa.assertEquals(
                commonPageBase.getErrorDialogText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGIN_EMAIL_LEGAL_PROMPT_HEADER.getText()),
                "Prompt description main header text does not match dictionary key");

        sa.assertEquals(
                commonPageBase.getErrorMessageText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGIN_EMAIL_LEGAL_PROMPT_BODY.getText()),
                "Prompt description body text does not match dictionary key");

        loginPageBase.clickLegalPositiveButton();

        sa.assertFalse(commonPageBase.isErrorDialogPresent(), "Forced prompt is displayed");

        sa.assertTrue(signUpPageBase.isOpened(), "Sign up page is not displayed");

        sa.assertTrue(commonPageBase.getTextFieldText().equals(disneyAccount.get().getEmail()),
                "Sign Up Email Text Field Is Not Populated with Star+ Email");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67222"})
    @Test(description = "Login with valid email and reach password screen", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("jdemelle")
    public void testLoginValidEmail() {
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);

        welcomePageBase.clickLoginButton();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());

        Assert.assertTrue(loginPageBase.isPasswordScreenOpened(), "Password screen is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67266"})
    @Test(description = "Login with minor blocked email", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("amostafa")
    public void testLoginMinorBlockedEmail() throws InterruptedException {
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusHelpCenterPageBase helpCenterPageBase = initPage(DisneyPlusHelpCenterPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusContactCustomerServicePageBase customerPageBase =
                initPage(DisneyPlusContactCustomerServicePageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.clickLoginButton();

        getAccountApi().patchAccountBlock(disneyAccount.get(), AccountBlockReasons.MINOR);

        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());
        loginPageBase.logInWithPassword(disneyAccount.get().getUserPass());

        sa.assertTrue(customerPageBase.isTitleDisplayed(), "CSS title is not displayed.");

        sa.assertTrue(customerPageBase.isSubtitleDisplayed(), "CSS subtitle is not displayed.");

        sa.assertTrue(
                commonPageBase.isTextViewStringDisplayed(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.BTN_HELP_CENTER.getText())),
                "Help center text is not displayed");

        sa.assertTrue(
                commonPageBase.isTextViewStringDisplayed(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.DISMISS_BTN.getText())),
                "Dismiss text is not displayed");

        customerPageBase.clickHelpCenter();

        sa.assertTrue(helpCenterPageBase.getURLText(), "Help center URL is incorrect");

        androidUtils.get().pressBack();

        sa.assertTrue(customerPageBase.isTitleDisplayed(), "CSS title is not displayed.");

        customerPageBase.clickDismiss();

        sa.assertTrue(welcomePageBase.isOpened());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67238"})
    @Test(description = "Unentitled user complete subscription flow", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("bwatson")
    public void testSubmitValidPassword() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCompletePurchasePageBase completePageBase = initPage(DisneyPlusCompletePurchasePageBase.class);

        disneyAccount.set(accountApi.get().createAccount("US", "en"));
        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());
        loginPageBase.logInWithPassword(disneyAccount.get().getUserPass());

        Assert.assertTrue(completePageBase.isOpened(), "Complete subscription is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67250"})
    @Test(description = "Complete subscription page is displayed to unentitled user", groups = {"Onboarding", "ArielLogIn"})
    public void testCompleteSubscriptionDisplay() {
        DisneyPlusCompletePurchasePageBase completePurchasePageBase =
                initPage(DisneyPlusCompletePurchasePageBase.class);

        SoftAssert sa = new SoftAssert();

        boolean supported = languageUtils.get().isSelectedLanguageSupported();
        boolean purchaseAvailable =
                initPage(DisneyPlusWelcomePageBase.class)
                        .isPurchaseAvailable(languageUtils.get()
                                .getNoPurchaseSubtextValues(supported));

        login(
                accountApi.get().createAccount(languageUtils.get().getLocale(),
                        languageUtils.get().getUserLanguage()), false);

        sa.assertTrue(completePurchasePageBase.isOpened(), "Complete subscription page not displayed");

        if (purchaseAvailable) {
            sa.assertTrue(completePurchasePageBase.isCompletePurchaseBtnPresent(),
                    "Complete purchase button not displayed");
        } else {
            sa.assertFalse(completePurchasePageBase.isCompletePurchaseBtnPresent(),
                    "Complete purchase button should not be displayed");
        }

        Assert.assertTrue(completePurchasePageBase.isLogoutBtnPresent(),
                "Logout button not displayed");

        completePurchasePageBase.clickLogoutBtn();

        sa.assertEquals(
                completePurchasePageBase.getErrorDialogText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOG_OUT_CONFIRMATION_TITLE.getText()),
                "Log out confirmation text does not match dictionary key");

        sa.assertEquals(
                completePurchasePageBase.getConfirmButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGOUT_MODAL.getText()),
                "Logout confirmation button text does not match dictionary key");

        sa.assertEquals(
                completePurchasePageBase.getCancelButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CANCEL_BTN_CAPS.getText()),
                "Logout cancel button text does not match dictionary key");

        completePurchasePageBase.clickCancelButton();

        Assert.assertFalse(completePurchasePageBase.isErrorDialogPresent(), "Logout dialog is displayed");

        completePurchasePageBase.clickLogoutBtn();
        completePurchasePageBase.clickConfirmButton();

        Assert.assertTrue(initPage(DisneyPlusWelcomePageBase.class).isOpened(),
                "User was not returned to the welcome screen after confirming logout");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67254"})
    @Test(description = "Restart subscription page is displayed", groups = {"Onboarding", "ArielLogIn"})
    @Maintainer("bwatson")
    public void testRestartSubscriptionDisplay() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusPaywallPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusCompletePurchasePageBase completePurchasePageBase =
                initPage(DisneyPlusCompletePurchasePageBase.class);
        DisneyPlusPlanSelectPageBase planSelectPageBase = initPage(DisneyPlusPlanSelectPageBase.class);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(
                accountApi.get().createExpiredAccount(
                        "Disney", "US", "en", "V1"));

        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());
        loginPageBase.logInWithPassword(disneyAccount.get().getUserPass());

        sa.assertTrue(completePurchasePageBase.isMainDescriptionPresent(),
                "Description main is not displayed");

        sa.assertEquals(
                completePurchasePageBase.getDescriptionMainText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.RESTART_SUB_COPY.getText()),
                "Description main text does not match dictionary key");

        sa.assertTrue(completePurchasePageBase.isSubDescriptionPresent(), "Description sub is not displayed");

        sa.assertEquals(
                completePurchasePageBase.getDescriptionSubText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.RESTART_SUB_COPY_2.getText()),
                "Description sub text is incorrect");

        sa.assertTrue(
                initPage(DisneyPlusCommonPageBase.class).isTextViewStringDisplayed(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.RESTART_SUBSCRIPTION.getText())),
                "Restart subscription cta text incorrect");

        commonPageBase.clickStandardButton();

        planSelectPageBase.isOpened();
        planSelectPageBase.clickPlanSelect(0);

        sa.assertTrue(paywallPageBase.isOpened());
        
        paywallPageBase.clickCancelBtn();
        paywallPageBase.clickPositiveBtn();

        sa.assertTrue(completePurchasePageBase.isLogoutBtnPresent(),
                "Restart subscription button not displayed");

        completePurchasePageBase.clickLogoutBtn();

        sa.assertEquals(
                completePurchasePageBase.getErrorDialogText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOG_OUT_CONFIRMATION_TITLE.getText()),
                "Log out confirmation text does not match dictionary key");

        sa.assertEquals(
                completePurchasePageBase.getConfirmButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGOUT_MODAL.getText()),
                "Logout confirmation button text does not match dictionary key");

        sa.assertEquals(
                completePurchasePageBase.getCancelButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CANCEL_BTN_CAPS.getText()),
                "Logout cancel button text does not match dictionary key");

        completePurchasePageBase.clickCancelButton();

        sa.assertFalse(completePurchasePageBase.isErrorDialogPresent(), "Logout dialog is displayed");

        completePurchasePageBase.clickLogoutBtn();
        completePurchasePageBase.clickConfirmButton();

        sa.assertTrue(welcomePageBase.isOpened(), "Welcome screen not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72223"})
    @Test(description = "Ariel Onboarding - Log In - Not Entitled - DOB Collection", groups = {"Onboarding", "ArielLogIn"})
    public void testLogInNotEntitledDOBCollectionScreen() {
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);

        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        SoftAssert sa = new SoftAssert();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());

        disneyAccount.set(accountApi.get().createAccount(createDisneyAccountRequest));

        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(createDisneyAccountRequest.getEmail());
        loginPageBase.logInWithPassword(createDisneyAccountRequest.getPassword());

        sa.assertFalse(dobCollectionPageBase.isNonEntitledDOBCollectionDisplayed().containsValue(false),
                "Not Entitled DOB Collection Page Elements Are Not All Displayed");

        hideKeyboard();

        sa.assertEquals(
                dobCollectionPageBase.getDOBTitleText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.DATE_OF_BIRTH_TITLE.getText()),
                "DOB Title text does not match dictionary key");

        sa.assertEquals(
                dobCollectionPageBase.getDOBCancelBtnText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CANCEL_LABEL.getText()),
                "DOB Cancel text does not match dictionary key");

        sa.assertEquals(
                dobCollectionPageBase.getDOBSubCopyText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.DATE_OF_BIRTH_DESCRIPTION.getText()),
                "DOB Sub Copy text does not match dictionary key");

        sa.assertEquals(
                dobCollectionPageBase.getDOBInputLabelText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.DATE_OF_BIRTH_LABEL.getText()),
                "DOB Input Label text does not match dictionary key");

        sa.assertEquals(
                dobCollectionPageBase.getDOBContinueBtnText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.BTN_DATE_OF_BIRTH_CONTINUE.getText()),
                "DOB Collection Confirm CTA text does not match dictionary key");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72163"})
    @Test(description = "Ariel Onboarding - Log In - ENTITLED - DOB Collection", groups = {"Onboarding", "ArielLogIn"})
    public void testLogInEntitledDOBCollectionScreen() {
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusCompleteProfilePageBase completeProfilePageBase = initPage(DisneyPlusCompleteProfilePageBase.class);
        DisneyPlusHelpCenterPageBase helpCenterPageBase = initPage(DisneyPlusHelpCenterPageBase.class);

        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        SoftAssert sa = new SoftAssert();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage())
                .setAddDefaultEntitlement(true);

        disneyAccount.set(accountApi.get().createAccount(createDisneyAccountRequest));

        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(createDisneyAccountRequest.getEmail());
        loginPageBase.logInWithPassword(createDisneyAccountRequest.getPassword());

        sa.assertFalse(dobCollectionPageBase.isNonEntitledDOBCollectionDisplayed().containsValue(false),
                "BASIC DOB Collection Page Elements Are Not All Displayed");

        hideKeyboard();

        sa.assertTrue(dobCollectionPageBase.isAccountHolderEmailDisplayed(), "Account Holder Email: label is not displayed");

        String str = languageUtils.get().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.ACCOUNT_HOLDER_EMAIL.getText()).replaceAll(createDisneyAccountRequest.getEmail(), "");

        sa.assertEquals(
                str,
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.ACCOUNT_HOLDER_EMAIL.getText()),
                "Account Holder Email Text is not correct");
        
        sa.assertTrue(dobCollectionPageBase.isDOBDisclaimerTextDisplayed(), "DOB Disclaimer Text is not displayed");

        dobCollectionPageBase.clickDisneyPlusHelpCenterLink();

        sa.assertTrue(helpCenterPageBase.getURLText(), "Help Center URL Text is not displayed");

        androidUtils.get().pressBack();

        sa.assertTrue(dobCollectionPageBase.isDOBDisclaimerTextDisplayed(), "DOB Collection Page is displayed after visiting Help Center and pressing back");

        closeAppForRelaunch();
        activityAndPackageLaunch();
        sa.assertTrue(dobCollectionPageBase.isDOBTitleDisplayed(), "DOB Page is not open after relaunch");

        dobCollectionPageBase.submitDOBValue(DOB_OVER_18);

        loginPageBase.logInWithPassword(createDisneyAccountRequest.getPassword());

        sa.assertTrue(completeProfilePageBase.isChooseProfileHeaderTitleTextDisplayed(), "Complete Profile Page is not displayed");

        sa.assertAll();
    }

    /**
     * Simple repeated code to proceed to the Forgot Password OTP page used
     * in each test within this class.
     * @return - Returns the initialized OTP page factory.
     */
    private DisneyPlusOneTimePasscodePageBase openOtpDisplay() {
        DisneyPlusOneTimePasscodePageBase otpPage = initPage(DisneyPlusOneTimePasscodePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);

        welcomePageBase.continueToLogin().proceedToPasswordMode(disneyAccount.get().getEmail());
        loginPageBase.waitForPasswordFieldToBeDisplayed();
        androidUtils.get().hideKeyboard();
        loginPageBase.clickForgotPassword();

        Assert.assertTrue(otpPage.isOpened(),
                "OTP page was not opened as expected");

        return otpPage;
    }

    private void createAccount() {
        disneyAccount.set(
                accountApi.get().createAccountForOTP(
                        languageUtils.get().getLocale(),
                        languageUtils.get().getUserLanguage()));

        verifyEmail.set(new VerifyEmail());
    }

    /**
     * Currently the string values are hardcoded for Ariel/US
     * Only available en or es languages
     */
    private boolean verifyCreateNewPasswordPrompt(String newPasswordPrompt) {
        String enCreateNewPassword = "This replaces the password you use to log into your accounts for all of the " +
                "services of The Walt Disney Family of Companies, including Hulu and ESPN+. Learn more.";
        String esCreateNewPassword = "Debes usar este correo electrónico y contraseña para iniciar sesión en las " +
                "cuentas de todos tus servicios favoritos de la Familia de Compañías Walt Disney, incluidos Disney+, " +
                "Hulu e ESPN+. Más información.";

        if (languageUtils.get().getUserLanguage().equals("en")) {
            return enCreateNewPassword.equals(newPasswordPrompt);
        } else {
            return esCreateNewPassword.equals(newPasswordPrompt);
        }
    }
}