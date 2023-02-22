package com.disney.qa.tests.disney.android.mobile.onboarding;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusSignUpPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusCreatePasswordPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAndroidSignUpTest extends BaseDisneyTest {

    private static final String PASSWORD_NULL = "";
    private static final String PASSWORD_LESS_THAN_SIX = "Abc12";
    private static final String PASSWORD_NO_SPECIAL = "abcdefghijk";
    private static final String EMAIL_INVALID_FORMAT_NULL = "";
    private static final String EMAIL_INVALID_FORMAT_NO_AT_SIGN = "blakeautomation2022DisneyStreaming.com";
    private static final String EMAIL_INVALID_FORMAT_NO_DOT_COM = "blakeInvalidFormat@gmail";
    private static final String EMAIL_BLOCKED_DOMAIN_1 = "blakeBlockedDomainEmail@gamil.com";
    private static final String PASSWORD_VALID = "Test1234!";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66555"})
    @Test(description = "Check elements of Sign Up Enter Email page", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpEnterEmailUI() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();

        sa.assertTrue(signUpPageBase.isBackButtonDisplayed(),
                "Back Button Not Displayed.");

        sa.assertTrue(signUpPageBase.isOnboardingStepperTextViewDisplayed(),
                "Stepper Not Displayed.");

        sa.assertTrue(signUpPageBase.isSignupTitleDisplayed(),
                "Sign Up Title Not Displayed.");

        sa.assertTrue(signUpPageBase.isInputHintTextViewDisplayed(),
                "Email Field Title Not Displayed.");

        sa.assertTrue(signUpPageBase.isEditFieldEditTextDisplayed(),
                "Email Field Not Displayed.");

        sa.assertTrue(signUpPageBase.isEditFieldEditTextDisplayed(),
                "Email Field Not Displayed.");

        sa.assertTrue(androidUtils.get().isKeyboardShown(),
                "Keyboard is Not Displayed.");

        androidUtils.get().hideKeyboard();

        commonPageBase.swipeUpOnScreen(1);

        sa.assertTrue(signUpPageBase.isoptInCheckboxesDisplayed(),
                "Check Boxes Not Displayed.");

        sa.assertTrue(signUpPageBase.isOptInTextViewDisplayed(),
                "Marketing Copy Not Displayed.");

        sa.assertTrue(signUpPageBase.isLegaleseTVDisplayed(),
                "Legal Center Not Displayed.");

        sa.assertTrue(signUpPageBase.isStandardButtonContainerDisplayed(),
                "CONTINUE CTA Not Displayed.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66565"})
    @Test(description = "Sign Up - Enter Email - Submit Unregistered Email", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpUnregisteredEmail() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusSubscriberAgreementPageBase subscriberAgreementPageBase =
                initPage(DisneyPlusSubscriberAgreementPageBase.class);
        String newEmail = generateNewUserEmail();
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        signUpPageBase.proceedToPasswordMode(newEmail);

        Assert.assertTrue(
                subscriberAgreementPageBase.isOpened(), "Subscriber agreement page is not displayed");

        commonPageBase.clickStandardButton();

        sa.assertTrue(createPasswordPageBase.isPasswordEntryFieldDisplayed(),
                "Password Entry Field Not Displayed");

        sa.assertTrue(commonPageBase.isTextViewStringDisplayed(newEmail),
                newEmail + " is not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66567"})
    @Test(description = "Sign Up - Enter Email - Submit Invalid Email Format", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpInvalidEmailFormat() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        signUpPageBase.proceedToPasswordMode(EMAIL_INVALID_FORMAT_NULL);

        sa.assertTrue(signUpPageBase.isInputErrorTextViewDisplayed(),
                "Input Error Text View Is Not Displayed");

        signUpPageBase.proceedToPasswordMode(EMAIL_INVALID_FORMAT_NO_AT_SIGN);

        sa.assertTrue(signUpPageBase.isInputErrorTextViewDisplayed(),
                "Input Error Text View Is Not Displayed");

        signUpPageBase.proceedToPasswordMode(EMAIL_INVALID_FORMAT_NO_DOT_COM);

        sa.assertTrue(signUpPageBase.isInputErrorTextViewDisplayed(),
                "Input Error Text View Is Not Displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66575"})
    @Test(description = "Sign Up - Enter Blocked Email Domain", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpBlockedEmailDomain() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusSubscriberAgreementPageBase subscriberAgreementPageBase =
                initPage(DisneyPlusSubscriberAgreementPageBase.class);
        SoftAssert sa = new SoftAssert();

        String subAgreementErrorMessage = "Subscriber agreement screen not displayed";

        welcomePageBase.proceedToSignUp();
        signUpPageBase.proceedToPasswordMode(EMAIL_BLOCKED_DOMAIN_1);

        Assert.assertTrue(subscriberAgreementPageBase.isOpened(), subAgreementErrorMessage);

        commonPageBase.clickStandardButton();
        createPasswordPageBase.submitNewPassword(PASSWORD_VALID);

        sa.assertTrue(
                createPasswordPageBase.isInputErrorDisplayed(), "Password Input Error Message Is Not Shown");

        commonPageBase.clickBackButton();

        Assert.assertTrue(subscriberAgreementPageBase.isOpened(), subAgreementErrorMessage);

        commonPageBase.clickBackButton();

        sa.assertTrue(signUpPageBase.isSignupTitleDisplayed(), "Sign Up Page Title Is Not Displayed");

        sa.assertTrue(commonPageBase.getTextFieldText().equals(EMAIL_BLOCKED_DOMAIN_1),
                "Email Field Is Not Populated with Blocked Email");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66563"})
    @Test(description = "Sign Up - Enter Registered Disney+ Email", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpRegisteredDisneyEmail() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();

        signUpPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());

        sa.assertTrue(loginPageBase.isPasswordScreenOpened(),
                "Log In Enter Password Screen Is Not Displayed");

        sa.assertTrue(androidUtils.get().isKeyboardShown(),
                "Android Keyboard Is Not Displayed");

        commonPageBase.clickBackButton();

        sa.assertTrue(signUpPageBase.isSignupTitleDisplayed(),
                "Sign Up Page Title Is Not Displayed");

        sa.assertTrue(commonPageBase.getTextFieldText().equals(disneyAccount.get().getEmail()),
                "Email Field Is Not Populated with Registered Email");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66581"})
    @Test(description = "Sign Up - Create a Password UI Checks", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpCreatePasswordUI() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusSubscriberAgreementPageBase subscriberAgreementPageBase =
                initPage(DisneyPlusSubscriberAgreementPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        signUpPageBase.proceedToPasswordMode(generateNewUserEmail());

        Assert.assertTrue(
                subscriberAgreementPageBase.isOpened(), "Subscriber agreement page is not displayed");

        commonPageBase.clickStandardButton();

        sa.assertTrue(createPasswordPageBase.isBackButtonDisplayed(),
                "Back Button Is Not Displayed");

        sa.assertTrue(createPasswordPageBase.isOnboardingStepperTextViewDisplayed(),
                "Onboarding Stepper Is Not Displayed");

        sa.assertTrue(createPasswordPageBase.isCreateAPasswordTitleDisplayed(),
                "Create A Password Header Is Not Displayed");

        sa.assertTrue(createPasswordPageBase.isEditTextFieldPresent(),
                "Create Password Text Field Is Not Displayed");

        sa.assertTrue(createPasswordPageBase.isShowHidePasswordIconDisplayed(),
                "Show/Hide Icon Is Not Displayed");

        sa.assertTrue(createPasswordPageBase.isInputDescriptionTextViewDisplayed(),
                "Password Hint Copy (use a minimum ...) Is Not Displayed");

        sa.assertTrue(createPasswordPageBase.isFatFingerHeaderTextViewDisplayed(),
                "Fat Finger Email Text Is Not Displayed");

        androidUtils.get().hideKeyboard();

        sa.assertTrue(createPasswordPageBase.isContinueButtonPresent(),
                "CONTINUE Button Is Not Displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66583"})
    @Test(description = "Check Sign Up - Create Password Invalid Formats", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpInvalidPasswordFormats() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusSubscriberAgreementPageBase subscriberAgreementPageBase =
                initPage(DisneyPlusSubscriberAgreementPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        signUpPageBase.proceedToPasswordMode(generateNewUserEmail());

        Assert.assertTrue(
                subscriberAgreementPageBase.isOpened(), "Subscriber agreement page is not displayed");

        commonPageBase.clickStandardButton();
        createPasswordPageBase.submitNewPassword(PASSWORD_NULL);

        sa.assertTrue(createPasswordPageBase.isInputErrorDisplayed(),
                "Invalid Password Format Is Not Displayed");

        commonPageBase.editTextByClass.click();
        androidUtils.get().pressKeyboardKey(AndroidKey.B);

        sa.assertFalse(
                createPasswordPageBase.isInputErrorDisplayed(),
                "Invalid Password Format Is Displayed After Editing");

        createPasswordPageBase.submitNewPassword(PASSWORD_LESS_THAN_SIX);

        sa.assertTrue(createPasswordPageBase.isInputErrorDisplayed(),
                "Invalid Password Format Is Not Displayed");

        sa.assertEquals(
                createPasswordPageBase.getInputErrorText(),
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                                DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), "6", "2"),
                "Invalid Password Error Not Displayed Or Incorrect For Password Less Than 6 Characters.");

        commonPageBase.editTextByClass.click();
        androidUtils.get().pressKeyboardKey(AndroidKey.DEL);

        sa.assertFalse(
                createPasswordPageBase.isInputErrorDisplayed(),
                "Invalid Password Format Is Displayed After Editing");

        createPasswordPageBase.submitNewPassword(PASSWORD_NO_SPECIAL);

        sa.assertTrue(createPasswordPageBase.isInputErrorDisplayed(),
                "Invalid Password Format Is Not Displayed");

        sa.assertEquals(
                createPasswordPageBase.getInputErrorText(),
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                                DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), "6", "2"),
                "Invalid Password Error Not Displayed Or Incorrect For Password Less Than 6 Characters.");

        commonPageBase.editTextByClass.click();
        androidUtils.get().pressKeyboardKey(AndroidKey.DEL);

        sa.assertFalse(
                createPasswordPageBase.isInputErrorDisplayed(),
                "Invalid Password Format Is Displayed After Editing");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71921"})
    @Test(description =
            "Complete Subscription Page is shown to unentitled user after Sign Up", groups = {"Onboarding", "Sign Up"})
    public void testSignUpCompleteSubscriptionDisplay() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusSignUpPageBase signUpPageBase = initPage(DisneyPlusSignUpPageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusCompletePurchasePageBase completePurchasePageBase =
                initPage(DisneyPlusCompletePurchasePageBase.class);
        DisneyPlusSubscriberAgreementPageBase subscriberAgreementPageBase =
                initPage(DisneyPlusSubscriberAgreementPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        signUpPageBase.proceedToPasswordMode(generateNewUserEmail());

        Assert.assertTrue(
                subscriberAgreementPageBase.isOpened(), "Subscriber agreement page is not displayed");

        commonPageBase.clickStandardButton();
        loginPageBase.registerPassword();
        commonPageBase.clickActionButton();
        commonPageBase.clickConfirmButton();

        sa.assertTrue(completePurchasePageBase.isOpened(),
                "Complete subscription page not displayed");

        Assert.assertTrue(completePurchasePageBase.isCompletePurchaseBtnPresent(),
                "Complete purchase button not displayed");

        sa.assertTrue(completePurchasePageBase.isLogoutBtnPresent(),
                "Log Out Button Not Displayed");

        completePurchasePageBase.clickLogoutBtn();

        sa.assertEquals(
                completePurchasePageBase.getErrorDialogText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOG_OUT_CONFIRMATION_TITLE.getText()),
                "Log Out Confirmation Text Incorrect");

        sa.assertEquals(
                completePurchasePageBase.getConfirmButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGOUT_MODAL.getText()),
                "Log Out Confirmation Button Text Incorrect");

        sa.assertEquals(
                completePurchasePageBase.getCancelButtonText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.CANCEL_BTN_CAPS.getText()),
                "Logout Cancel Button Text Incorrect");

        completePurchasePageBase.clickCancelButton();

        Assert.assertFalse(completePurchasePageBase.isErrorDialogPresent(),
                "Log Out Dialog Was Not Dismissed With Cancel Button");

        completePurchasePageBase.clickLogoutBtn();
        completePurchasePageBase.clickConfirmButton();

        Assert.assertTrue(initPage(DisneyPlusWelcomePageBase.class).isOpened(),
                "User was not returned to the Welcome Screen after Confirming Log Out");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66591"})
    @Test(description = "Sign Up - Create a Valid Password & Land on Paywall", groups = {"Onboarding", "Sign Up"})
    @Maintainer("bwatson")
    public void testSignUpCreateValidPassword() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusPaywallPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusSubscriberAgreementPageBase subscriberAgreementPageBase =
                initPage(DisneyPlusSubscriberAgreementPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        loginPageBase.registerNewEmail(generateNewUserEmail());

        Assert.assertTrue(
                subscriberAgreementPageBase.isOpened(), "Subscriber agreement page is not displayed");

        commonPageBase.clickStandardButton();
        loginPageBase.registerPassword();

        sa.assertTrue(paywallPageBase.isOpened(), "Paywall Is Not Displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66589"})
    @Test(description = "Verify the Password Strength Meter on the Create Password Page", groups = {"Onboarding", "Sign Up"})
    public void testCreatePasswordStrengthMeter() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusSubscriberAgreementPageBase subscriberAgreementPageBase =
                initPage(DisneyPlusSubscriberAgreementPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.proceedToSignUp();
        loginPageBase.registerNewEmail(generateNewUserEmail());

        Assert.assertTrue(
                subscriberAgreementPageBase.isOpened(), "Subscriber agreement page is not displayed");

        commonPageBase.clickStandardButton();

        sa.assertFalse(
                createPasswordPage.isCreatePasswordStrengthMeterFunctioning().containsValue(false),
                "Create Password Strength Meter Is Not Functioning");

        sa.assertAll();
    }
}
