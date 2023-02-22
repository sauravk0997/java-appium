package com.disney.qa.tests.disney.android.mobile.onboarding;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAndroidLoginTest extends BaseDisneyTest {

    private static final String EMAIL_UNREGISTERED = "jeffInvalidTest@thisIsFake.ru";
    private static final String EMAIL_UNREGISTERED_EDITED = "editedJeffInvalidTest@thisIsFake.ru";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68436"})
    @Test(description = "Verify unknown email modal", groups = {"Onboarding", "Log In"})
    public void testUnknownEmailModal() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1649"));

        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusWelcomePageBase.class);
        SoftAssert sa = new SoftAssert();

        String invalidEmailErrorMessage = "Invalid Email error not displayed";
        String unregisteredEmailErrorMessage = "Unregistered email to be the same one originally entered";

        welcomePageBase.continueToLogin();

        sa.assertTrue(loginPageBase.isOpened(), "Log in enter email page not displayed.");

        loginPageBase.proceedToPasswordMode(EMAIL_UNREGISTERED);

        sa.assertTrue(commonPageBase.isErrorDialogPresent(), "Unregistered email error not displayed");

        String expectedError =
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.LOGIN_NO_ACCOUNT_SUB_TEXT.getText()),
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.LOGIN_NO_ACCOUNT_SUB_COPY_LINK_1_TEXT.getText()));

        sa.assertEquals(commonPageBase.getErrorMessageText(), expectedError, invalidEmailErrorMessage);

        loginPageBase.dismissError();

        sa.assertEquals(commonPageBase.getEditTextFieldText(), EMAIL_UNREGISTERED, unregisteredEmailErrorMessage);

        sa.assertEquals(
                loginPageBase.getInputErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGIN_INVALID_EMAIL_ERROR.getText()),
                invalidEmailErrorMessage);

        loginPageBase.proceedToPasswordMode(EMAIL_UNREGISTERED);

        sa.assertTrue(commonPageBase.isErrorDialogPresent(), invalidEmailErrorMessage);

        sa.assertEquals(commonPageBase.getErrorMessageText(), expectedError, invalidEmailErrorMessage);

        commonPageBase.tapAwayToCloseModal();

        sa.assertEquals(commonPageBase.getEditTextFieldText(), EMAIL_UNREGISTERED, unregisteredEmailErrorMessage);

        sa.assertEquals(
                loginPageBase.getInputErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGIN_INVALID_EMAIL_ERROR.getText()),
                invalidEmailErrorMessage);

        loginPageBase.proceedToPasswordMode(EMAIL_UNREGISTERED_EDITED);

        sa.assertTrue(commonPageBase.isErrorDialogPresent(), "Unregistered email modal to be displayed");

        sa.assertEquals(commonPageBase.getErrorMessageText(), expectedError, invalidEmailErrorMessage);

        commonPageBase.clickCancelButton();
        loginPageBase.isSignUpScreenDisplayed();
        commonPageBase.getEditTextFieldText();

        sa.assertEquals(commonPageBase.getEditTextFieldText(), EMAIL_UNREGISTERED_EDITED,
                "Unregistered email to be the edited one entered");

        androidUtils.get().hideKeyboard();
        loginPageBase.pressBackCarrotBtn();
        welcomePageBase.continueToLogin();
        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());

        sa.assertEquals(
                loginPageBase.getPasswordTitleText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.ENTER_YOUR_PASSWORD.getText()),
                "Password title does not match expected dictionary key");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67212"})
    @Test(description = "Verify login screen UI", groups = {"Onboarding", "Log In"})
    public void testLoginEmailScreenUI() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1687"));

        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.continueToLogin();

        Assert.assertTrue(loginPageBase.isOpened(), "Login page not displayed.");

        sa.assertTrue(loginPageBase.isLogoDisplayed(), "Disney Plus logo not displayed");

        sa.assertTrue(loginPageBase.isBackButtonDisplayed(), "Back arrow not displayed");

        sa.assertEquals(
                loginPageBase.getEmailTitleText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGIN_IN_TITLE.getText()),
                "Login title not displayed");

        sa.assertTrue(
                disneyPlusCommonPageBase.isTextViewStringDisplayed(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.BTN_CONTINUE.getText())),
                "Continue button not displayed");

        androidUtils.get().hideKeyboard();

        sa.assertTrue(loginPageBase.isSignUpButtonDisplayed(), "Sign Up link not displayed");

        loginPageBase.pressBackCarrotBtn();

        sa.assertTrue(welcomePageBase.isOpened(), "Welcome screen not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67224"})
    @Test(description = "Verify enter password screen", groups = {"Onboarding", "Log In"})
    public void testPasswordScreenElements() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1651"));

        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        SoftAssert sa = new SoftAssert();

        welcomePageBase.continueToLogin();

        loginPageBase.proceedToPasswordMode(disneyAccount.get().getEmail());

        sa.assertEquals(loginPageBase.getPasswordTitleText(), languageUtils.get().getDictionaryItem
                        (DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ENTER_YOUR_PASSWORD.getText()),
                "Password title not displayed");

        sa.assertTrue(loginPageBase.isLogoDisplayed(), "Disney Plus logo not displayed");

        sa.assertTrue(loginPageBase.isBackButtonDisplayed(), "Back arrow not displayed");

        sa.assertTrue(loginPageBase.isEditTextFieldPresent(), "Password text entry field not displayed");

        sa.assertTrue(loginPageBase.isShowPasswordButtonDisplayed(), "Show Password button not displayed");

        sa.assertTrue(loginPageBase.isForgotPasswordDisplayed(), "Forgot password button not displayed");

        checkAssertions(sa);
    }
}
