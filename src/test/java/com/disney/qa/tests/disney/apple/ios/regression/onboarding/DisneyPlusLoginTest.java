package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import static com.disney.qa.common.utils.IOSUtils.DEVICE_TYPE;

import com.disney.util.TestGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.alice.AliceDriver;
import com.disney.qa.api.account.AccountBlockReasons;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIsMinorIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountOnHoldIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCompleteSubscriptionIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusRestartSubscriptionIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSignUpIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;

import java.lang.invoke.MethodHandles;

public class DisneyPlusLoginTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String NO_ERROR_DISPLAYED = "error message was not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62030", "XMOBQA-62032", "XMOBQA-62689"})
    @Test(description = "Log In - Verify Login Screen UI", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testLogInScreen() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isBackArrowDisplayed(), "Expected: Back Arrow should be present");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isEmailFieldDisplayed(), "Expected: Email field should be present");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isPrimaryButtonPresent(), "Expected: Continue (primary) button should be present");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isSignUpButtonDisplayed(), "Expected: Sign Up button should be present");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isDisneyLogoDisplayed(), "Expected: Disney+ logo image should be displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isLoginTextDisplayed(), "Expected: 'Log in with your email' text should be displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isNewToDPlusTextDisplayed(), "Expected: 'New to Disney+?' text should be displayed");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62038"})
    @Test(description = "Log In - Verify Password Screen UI", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testPasswordScreen() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isBackArrowDisplayed(), "Expected: Back Arrow should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isDisneyLogoDisplayed(), "Expected: Disney+ logo image should be displayed");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isHeaderTextDisplayed(), "Expected: Header text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isPasswordFieldDisplayed(), "Expected: Password field should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isPasswordHintTextDisplayed(), "Expected: Password hint text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isForgotPasswordLinkDisplayed(), "Expected: Forgot password link should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isShowPasswordIconDisplayed(), "Expected: Show Password button should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isLoginButtonDisplayed(), "Expected: Login button should be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62036"})
    @Test(description = "Log in - Verify valid email", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testValidEmailLogin() {
        SoftAssert softAssert = new SoftAssert();

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        new DisneyPlusLoginIOSPageBase(getDriver()).submitEmail(disneyAccount.get().getEmail());
        softAssert.assertTrue(new DisneyPlusPasswordIOSPageBase(getDriver()).isOpened(), "Password page should have opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62046"})
    @Test(description = "Log in - Verify valid password - One Profile", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testValidPasswordOneProfile() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = new DisneyPlusHomeIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(disneyAccount.get().getUserPass());
        softAssert.assertTrue(disneyPlusHomeIOSPageBase.isOpened(), "Home page should have been opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62046"})
    @Test(description = "Log in - Verify valid password - Multiple Profiles", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testValidPasswordMultipleProfiles() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());

        disneyAccountApi.get().addProfile(disneyAccount.get(), "Prof2", disneyAccount.get().getProfileLang(), null, false);
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(disneyAccount.get().getUserPass());
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isOpened(), "Who's Watching page should have been opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62034", "XMOBQA-62048"})
    @Test(description = "Log in - Verify invalid email format", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testInvalidEmailFormat() {
        String invalidEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.ATTRIBUTE_VALIDATION.getText());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        SoftAssert softAssert = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail("notAnEmail");
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidEmailError, NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62050"})
    @Test(description = "Log in - Verify invalid email format - One Character", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testInvalidEmailFormatOneChar() {
        String invalidEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_EMAIL_ERROR.getText());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        SoftAssert softAssert = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail("a");
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidEmailError, NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62050"})
    @Test(description = "Log in - Verify invalid email format - No TLD", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testInvalidEmailFormatNoTopLevelDomain() {
        String invalidEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_EMAIL_ERROR.getText());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        SoftAssert softAssert = new SoftAssert();
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail("emailWithoutTLD@gmail");
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidEmailError, NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62052"})
    @Test(description = "Log in - Verify login no email", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testLoginNoEmail() {
        String invalidEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_EMAIL_ERROR.getText());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        SoftAssert softAssert = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail("");
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidEmailError, NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62040"})
    @Test(description = "Log in - Verify invalid password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testInvalidPassword() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        String invalidCreds = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_CREDENTIALS_ERROR.getText());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin("wrongUserPass");
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getErrorMessageString(), invalidCreds, NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62056"})
    @Test(description = "Log in - Verify empty password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testEmptyPassword() {
        String noPasswordError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_NO_PASSWORD_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin("");
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getErrorMessageString(), noPasswordError, NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62042"})
    @Test(description = "Log in - Verify Show and Hide Password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testShowHidePassword() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        StringBuilder hiddenPassword = new StringBuilder();
        String userPassword = disneyAccount.get().getUserPass();
        hiddenPassword.append("•".repeat(userPassword.length()));

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.typePassword(disneyAccount.get().getUserPass());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isShowPasswordIconDisplayed());
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getPasswordText(), hiddenPassword.toString());
        disneyPlusPasswordIOSPageBase.clickShowPasswordIcon();
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getPasswordText(), userPassword);
        disneyPlusPasswordIOSPageBase.clickHidePasswordIcon();
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getPasswordText(), hiddenPassword.toString());

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62070"})
    @Test(description = "Log in - Unknown email - try again", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testEmailNoAccountTryAgain() {
        String noEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN_INVALID_EMAIL_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        String newEmail = "thisEmailDoesntExist@disenystreaming.com";
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(newEmail);

        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), noEmailError, NO_ERROR_DISPLAYED);
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isTryAgainAlertButtonDisplayed(), "try again button was not displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isSignUpAlertButtonDisplayed(), "sign up button was not displayed");
        disneyPlusLoginIOSPageBase.clickAlertTryAgainButton();
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), noEmailError, NO_ERROR_DISPLAYED);
        disneyPlusLoginIOSPageBase.fillOutEmailField(disneyAccount.get().getEmail());
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62072"})
    @Test(description = "Log in - Unknown email - sign up", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testEmailNoAccountSignUp() {
        String noEmailError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN_INVALID_EMAIL_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = new DisneyPlusSignUpIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        String newEmail = "thisEmailDoesntExist2@disenystreaming.com";
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(newEmail);

        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), noEmailError, NO_ERROR_DISPLAYED);
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isNoAccountAlertSubtextDisplayed(), "No Account alert subtext was not displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isTryAgainAlertButtonDisplayed(), "try again button was not displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isSignUpAlertButtonDisplayed(), "sign up button was not displayed");

        disneyPlusLoginIOSPageBase.clickAlertSignUpButton();

        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(), "sign up page did not load");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isEmailFieldDisplayed(), "email field was not displayed");
        softAssert.assertEquals(disneyPlusSignUpIOSPageBase.getEmailFieldText(), newEmail, "Email field was not prefilled with email address");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62074", "XMOBQA-62076"})
    @Test(description = "Verify Choose Profiles Screen", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testChooseProfiles() {
        String kidProfile = "kidProfile";
        String profile3 = "profile 3";
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());

        disneyAccountApi.get().addProfile(disneyAccount.get(), kidProfile, "en", "90e6cc76-b849-5301-bf2a-d8ddb200d07b", true);
        disneyAccountApi.get().addProfile(disneyAccount.get(), profile3, "en", "b4515c3a-d9a9-57e4-b2be-a793104c0839", false);

        SoftAssert softAssert = new SoftAssert();
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(disneyAccount.get());
//        disneyPlusWhoseWatchingIOSPageBase.dismissAppTrackingPopUp();

        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isHeaderTextDisplayed(), "Header text was not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(DEFAULT_PROFILE), "Profile name or image not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(kidProfile), "kid profile name or image not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(profile3), "additional adult name or image not displayed");
        softAssert.assertFalse(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent("DOESNT EXIST"), "profile displayed that should not.");

        disneyPlusWhoseWatchingIOSPageBase.clickProfile(DEFAULT_PROFILE);
        softAssert.assertTrue(new DisneyPlusHomeIOSPageBase(getDriver()).isOpened(),
                "Expected - Home page should be opened after selecting profile");

        // TODO add check that the correct profile loaded
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62066"})
    @Test(description = "Log in - Test non-entitled account", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testNotEntitledAccount() {
        DisneyAccount nonActiveAccount = disneyAccountApi.get().createAccount("US", "en");
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusCompleteSubscriptionIOSPageBase disneyPlusCompleteSubscriptionIOSPageBase = new DisneyPlusCompleteSubscriptionIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(nonActiveAccount);

        // Complete subscription page loads
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getHeroImage().isPresent(), "hero not present");
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getPrimaryText().isPresent(), "primary text not present");
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getSecondaryText().isPresent(), "secondary text not present");
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getCompleteSubscriptionButton().isPresent(), "button not present");
        //TODO:https://jira.disneystreaming.com/browse/QCE-1253
        //aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62062, XMOBQA-62195"})
    @Test(description = "Log in - expired account", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testExpiredAccount() {
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusRestartSubscriptionIOSPageBase disneyPlusRestartSubscriptionIOSPageBase = new DisneyPlusRestartSubscriptionIOSPageBase(getDriver());

        DisneyAccount expired = disneyAccountApi.get().createExpiredAccount("Yearly", "US", "en", "V1");
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(expired);

        // Restart Subscription Page loads
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getHeroImage().isPresent(), "hero not present");
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getPrimaryText().isPresent(), "primary text not present");
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getSecondaryText().isPresent(), "secondary text not present");
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getRestartSubscriptionButton().isPresent(), "button not present");
        //TODO:https://jira.disneystreaming.com/browse/QCE-1253
        //aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");

        softAssert.assertAll();
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62058"})
    @Test(description = "Log in - Incorrect Password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testIncorrectPassword() {
        String invalidPasswordError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_CREDENTIALS_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin("incorrectPassword123");
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidPasswordError, NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62679"})
    @Test(description = "Log in - Verify Account on Hold", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyAccountOnHold() {
        SoftAssert softAssert = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusAccountOnHoldIOSPageBase disneyPlusAccountOnHoldIOSPageBase = initPage(DisneyPlusAccountOnHoldIOSPageBase.class);

        DisneyAccount accountWithBillingHold = disneyAccountApi.get().createAccountWithBillingHold("Yearly", "US", "en", "V2");
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(accountWithBillingHold);

        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getLogoutButton().isPresent(), "Logout button not present");
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getAccountHoldTitle().isPresent(), "Account Hold Title not present");
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getAccountHoldSubText().isPresent(), "Account Hold Subtext not present");
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getUpdatePaymentButton().isPresent(), "Update Payment Button not present");
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getRefreshButton().isPresent(), "Refresh Button not present");
        //QCE-1253 Causes below to fail on iPhone. Otherwise test passes on iPad.
        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            LOGGER.info("Tablet");
            aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");
        }

        softAssert.assertAll();
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62082"})
    @Test(description = "Log in - Verify Minor User is blocked from logging in", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyMinorLogInBlocked() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusAccountIsMinorIOSPageBase disneyPlusAccountIsMinorIOSPageBase = initPage(DisneyPlusAccountIsMinorIOSPageBase.class);

        DisneyAccount minorAccount = disneyAccountApi.get().createAccount("Yearly", "US", "en", "V1");
        disneyAccountApi.get().patchAccountBlock(minorAccount, AccountBlockReasons.MINOR);

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(minorAccount);

        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getNotEligibleHeader().isPresent(), "Account Ineligibility Header not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getNotEligibleSubText().isPresent(), "Account Ineligibility Text not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getHelpCenterButton().isPresent(), "Help Center Button not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getDismissButton().isPresent(), "Dismiss Button not present");

        softAssert.assertAll();
    }
}