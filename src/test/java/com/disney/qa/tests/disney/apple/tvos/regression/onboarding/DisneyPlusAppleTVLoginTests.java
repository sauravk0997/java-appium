package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.alice.labels.AliceLabels.*;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

public class DisneyPlusAppleTVLoginTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90606", "XCDQA-90604"})
    @Test(description = "Email Input screen: Navigate Back", groups = {"Smoke"})
    public void backToWelcomeScreen() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        disneyPlusAppleTVLoginPage.clickMenu();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90608"})
    @Test(description = "Email Input screen: Screen details/appearance", groups = {"Smoke"})
    public void emailInputScreenAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldFocused(), "Email input is not focused by default");
        sa.assertEquals(disneyPlusAppleTVLoginPage.getHeadlineHeaderText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LOGIN_IN_TITLE.getText()));
        sa.assertTrue(disneyPlusAppleTVLoginPage.isContinueButtonDisplayed(), "Continue button is not present");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldDisplayed(), "Email text field is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90610"})
    @Test(description = "Email Input screen: Navigation", groups = {"Smoke"})
    public void emailInputNavigation() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldFocused(), "Email input is not focused by default");
        disneyPlusAppleTVLoginPage.navigateToContinueButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isContinueButtonFocused(), "Continue button is not focused");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90612", "XCDQA-90614"})
    @Test(description = "Email Input screen: on-screen keyboard appearance", groups = {"Smoke"})
    public void emailInputKeyboardAppearance() {
        String tempEmailText = "bcd";
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        disneyPlusAppleTVLoginPage.clickEmailField();
        disneyPlusAppleTVLoginPage.clickEnterNewBtn();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "on screen keyboard is not present");
        disneyPlusAppleTVLoginPage.clickMenu();
        sa.assertFalse(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "on screen keyboard is present");
        disneyPlusAppleTVLoginPage.clickEmailField();
        disneyPlusAppleTVLoginPage.clickEnterNewBtn();
        disneyPlusAppleTVLoginPage.enterTempEmailTextAndClickContinue();

        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch after inputting");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isContinueButtonFocused(), "Continue button is not focused");
        sa.assertEquals(disneyPlusAppleTVLoginPage.getEmailFieldText(), tempEmailText, "Entered text for email did not match");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90600"})
    @Test(description = "Check for the error message with no email input", groups = {"Smoke"})
    public void verifyErrorMessageWithNoEmailInput() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        String noEmailInputError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, ATTRIBUTE_VALIDATION.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        disneyPlusAppleTVLoginPage.clickContinueBtn();
        sa.assertEquals(disneyPlusAppleTVLoginPage.getErrorMessageLabelText(), noEmailInputError);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90602"})
    @Test(description = "Check for error message with invalid email input", groups = {"Smoke"})
    public void verifyErrorMessageWithInvalidEmailInput() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        String noEmailInputError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, ATTRIBUTE_VALIDATION.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen("somethin!^&&#@gmail");
        sa.assertEquals(disneyPlusAppleTVLoginPage.getErrorMessageLabelText(), noEmailInputError);

        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90104"})
    @Test(description = "Verify all the texts on the we couldn't find an account for that email screen", groups = {"Smoke"})
    public void unknownEmailScreenTextVerification() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(apiProvider.get().getUniqueUserEmail());

        List<String> expectedTextList = DisneyPlusAppleTVLoginPage.getUnknownEmailScreenTexts(languageUtils.get());
        IntStream.range(0, expectedTextList.size()).forEach(i -> {
            if (i == 0) {
                sa.assertEquals(disneyPlusAppleTVLoginPage.getActionableAlertTitle(), expectedTextList.get(i));
            } else if (i == 1) {
                sa.assertEquals(disneyPlusAppleTVLoginPage.getActionableAlertMessage(), expectedTextList.get(i));
            } else {
                sa.assertTrue(disneyPlusAppleTVLoginPage.isStaticTextPresentWithScreenShot(expectedTextList.get(i).toUpperCase()),
                        "The following text was not present on unknown email screen" + expectedTextList.get(i).toUpperCase());
            }
        });

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90106"})
    @Test(description = "Verify that user is brought to login with your email screen after pressing try again from unknown email screen", groups = {"Onboarding"})
    public void verifyTryAgainBringsBackToEmailEntry() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        String errorMessage = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LOGIN_INVALID_EMAIL_ERROR.getText());
        String continueBtnText = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_CONTINUE.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(apiProvider.get().getUniqueUserEmail());
        disneyPlusAppleTVLoginPage.clickTryAgainBtn();

        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(),
                "Enter email screen did not launch from unknown email screen");
        sa.assertEquals(disneyPlusAppleTVLoginPage.getErrorMessageLabelText(), errorMessage);
        sa.assertTrue(disneyPlusAppleTVLoginPage.isStaticTextPresentWithScreenShot(continueBtnText),
                "The following text was not found on the button of enter email screen: " + continueBtnText);

        disneyPlusAppleTVLoginPage.clickEmailField();

        sa.assertTrue(disneyPlusAppleTVLoginPage.isEnterNewEmailBtnPresent(),
                "Previously used email screen did not launch");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90108", "XCDQA-90110"})
    @Test(description = "Verify user is taken to sign up screen from unknown email screen and the email field is already filled and finish signing up", groups = {"Smoke"})
    public void verifyUserIsTakenToSignUpFromUnknownUserScreenAndCompleteSignUp() {
        SoftAssert sa = new SoftAssert();
        String uniqueUserEmail = apiProvider.get().getUniqueUserEmail();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVCompletePurchasePage disneyPlusAppleTVCompletePurchasePage = new DisneyPlusAppleTVCompletePurchasePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(uniqueUserEmail);
        disneyPlusAppleTVLoginPage.clickSignUpButtonUnknownEmailScreen();

        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign Up page did not launch");
        sa.assertEquals(disneyPlusAppleTVSignUpPage.getEmailFieldText(), uniqueUserEmail);

        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreatePasswordScreenOpen(),
                "Create password screen did launch from enter your email");
        disneyPlusAppleTVPasswordPage.clickPassword();
        disneyPlusAppleTVPasswordPage.enterPasswordCreatePassword(R.TESTDATA.get("disney_qa_web_generic_pass"));
        disneyPlusAppleTVPasswordPage.moveToContinueBtnKeyboardEntry();
        disneyPlusAppleTVPasswordPage.clickSelect();
        disneyPlusAppleTVPasswordPage.clickSignUp();

        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isOpened(), "Complete Purchase screen did not launch");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90598"})
    @Test(description = "Given user enters a valid registered email then user is taken Log In password screen", groups = {"Smoke"})
    public void registeredEmailTakenToLoginPassword() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount disneyAccount = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyAccount.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90701"})
    @Test(description = "Log In Password screen details verification", groups = {"Smoke"})
    public void passwordScreenDetailsVerification() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        List<String> expectedTexts = DisneyPlusAppleTVPasswordPage.getLogInPasswordScreenTexts(languageUtils.get());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(entitledUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");
        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());

        List<String> actualTexts = disneyPlusAppleTVPasswordPage.getLogInPasswordScreenActualTexts();

        IntStream.range(0, expectedTexts.size()).forEach(i -> sa.assertEquals(actualTexts.get(i), expectedTexts.get(i)));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90703"})
    @Test(description = "Navigation of Log In Password Screen without password entered", groups = {"Smoke"})
    public void passwordScreenNavigation() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        String passwordGhost = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, PASSWORD.getText());
        String forgotPasswordBtnText = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, FORGOT_PASSWORD.getText());
        String logInBtnText = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LOGIN_BTN.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(entitledUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldFocused(), "Password field is not focused on landing");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordText(), passwordGhost);

        disneyPlusAppleTVPasswordPage.clickDown();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isForgotPasswordBtnFocused(), "Forgot password button is not focused");
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, forgotPasswordBtnText, AliceLabels.BUTTON_HOVERED.getText());

        disneyPlusAppleTVPasswordPage.clickDown();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isLogInBtnFocused(), "Log In button is not focused");
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, logInBtnText, AliceLabels.BUTTON_HOVERED.getText());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90705"})
    @Test(description = "On Password Screen selecting enter your password displays on screen keyboard", groups = {"Smoke"})
    public void clickingPasswordFieldLaunchesOnScreenKeyboard() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(entitledUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");

        disneyPlusAppleTVPasswordPage.clickPassword();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldDisplayed(), "Password field is not displayed");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "KeyboardScreen did not launch");

        disneyPlusAppleTVPasswordPage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(),
                "Log In password screen did not launch after pressing menu from on screen keyboards screen");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90707", "XCDQA-90709", "XCDQA-90711"})
    @Test(description = "Verifying hide/show button on the password entry onscreen keyboard", groups = {"Smoke"})
    public void passwordEntryEncryptionVerification() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        String encryptedPassword = "••••";

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(entitledUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");

        disneyPlusAppleTVPasswordPage.clickPassword();

        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "KeyboardScreen did not launch");

        IntStream.range(0, 4).forEach(i -> {
            disneyPlusAppleTVPasswordPage.clickSelect();
            disneyPlusAppleTVPasswordPage.clickRight();
        });
        //Move down to continue button and select it
        disneyPlusAppleTVPasswordPage.moveToContinueBtnKeyboardEntry();
        disneyPlusAppleTVPasswordPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(),
                "Log In password screen did not launch after pressing menu from on screen keyboards screen");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), encryptedPassword);

        //Move to show password button from login button
        disneyPlusAppleTVPasswordPage.moveUp(2, 1);
        disneyPlusAppleTVPasswordPage.moveRight(1, 1);
        disneyPlusAppleTVPasswordPage.clickSelect();

        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), "abcd",
                "XCDQA-90709 - Password was not displayed");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getShowHidePasswordBtnState(), "hide");

        //Click hide password button and it should turn into show password
        disneyPlusAppleTVPasswordPage.clickSelect();

        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), encryptedPassword,
                "XCDQA-907011 - Password was not encrypted");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getShowHidePasswordBtnState(), "show");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90697"})
    @Test(description = "On enter password screen when no input is provided but login is attempted an error should be prompted", groups = {"Smoke"})
    public void noInputPasswordError() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        String emptyPasswordError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_NO_PASSWORD_ERROR.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(entitledUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");

        disneyPlusAppleTVPasswordPage.moveDown(2, 1);
        disneyPlusAppleTVPasswordPage.clickSelect();

        sa.assertEquals(disneyPlusAppleTVPasswordPage.getErrorMessageLabelText(), emptyPasswordError);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90699"})
    @Test(description = "When user enters an incorrect password an appropriate error message is prompted to the user", groups = {"Smoke"})
    public void incorrectPasswordErrorMessage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        String incorrectPasswordError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(entitledUser.getEmail());

        disneyPlusAppleTVPasswordPage.logInWithPassword(RandomStringUtils.randomAlphabetic(6));

        sa.assertEquals(disneyPlusAppleTVPasswordPage.getErrorMessageLabelText(), incorrectPasswordError);

        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90689"})
    @Test(description = "When user enters the correct password then a validating screen is prompted prior to login", groups = {"Smoke"})
    public void validatingScreenPostLogIn() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        DisneyPlusApplePageBase applePageBase = new DisneyPlusApplePageBase(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        logInWithoutHomeCheck(entitledUser);

        //TODO: Temp fix in place. After QCE-1333 is fixed, replace below lines with original Alice validation (QAA-11371).
        sa.assertTrue(applePageBase.doesAttributeEqualTrue(applePageBase.getStaticTextByLabelContains("Validating"), "enabled"));
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, "Validating", AliceLabels.TITLE.getText())
                .isLabelPresent(sa, AliceLabels.LOADING_ANIMATION.getText());
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.LOADING_ANIMATION.getText());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90691"})
    @Test(description = "User logging in with a single profile account will be taken directly to home page", groups = {"Smoke"})
    public void singleProfileAccountIsTakenToHomePage() {
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90693"})
    @Test(description = "User logging in with multiple profile account will be taken Who's Watching", groups = {"Smoke"})
    public void multipleProfileAccountIsTakenToProfileSelection() {
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        disneyAccountApi.addProfile(entitledUser, "test", language, null, false);

        logInWithoutHomeCheck(entitledUser);

        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page did not launch");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90695"})
    @Test(description = "User's profile selected post login is displayed in global nav", groups = {"Smoke"})
    public void profileNameRetention() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        logIn(entitledUser);

        disneyPlusAppleTVHomePage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVHomePage.isHomeBtnPresent(), "Home button is not displayed");

        aliceDriver.screenshotAndRecognize()
                .assertLabelContainsCaption(sa, "Test", AliceLabels.PROFILE_BUTTON.getText());
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90591", "XCDQA-90593"})
    @Test(description = "Verify Restart subscription screen details", groups = "Smoke")
    public void verifyRestartSubscriptionScreenDetails() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusAppleTVRestartSubscriptionPage disneyPlusAppleTVRestartSubscriptionPage = new DisneyPlusAppleTVRestartSubscriptionPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyAccount disneyAccount = disneyAccountApi.createExpiredAccount(ENTITLEMENT_LOOKUP, country, language, SUB_VERSION);

        logInWithoutHomeCheck(disneyAccount);

        sa.assertTrue(disneyPlusAppleTVRestartSubscriptionPage.isOpened(), "Restart subscription screen did not launch");
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());

        disneyPlusAppleTVRestartSubscriptionPage.getRestartSubscriptionScreenDictionaryTexts().forEach(item -> sa.assertTrue(
                disneyPlusAppleTVRestartSubscriptionPage.isDynamicAccessibilityIDElementPresent(item),
                "Following text was not found on restart subscription screen " + item));

        disneyPlusAppleTVRestartSubscriptionPage.moveDown(1, 1);
        disneyPlusAppleTVRestartSubscriptionPage.clickLogoutButtonIfHasFocus();

        sa.assertTrue(disneyPlusAppleTVRestartSubscriptionPage.isLogoutPageOpen(), "LOG OUT menu from restart subscription did not launch");
        sa.assertTrue(disneyPlusAppleTVRestartSubscriptionPage.getLogOutConfirmationButton().isPresent(), "LOG OUT Confirmation Button is not present");
        sa.assertTrue(disneyPlusAppleTVRestartSubscriptionPage.getLogOutCancelButton().isPresent(), "LOG OUT Cancel Button is not present");
        
        disneyPlusAppleTVRestartSubscriptionPage.moveDown(1, 1);
        disneyPlusAppleTVRestartSubscriptionPage.clickCancelAlertBtn();
        sa.assertTrue(disneyPlusAppleTVRestartSubscriptionPage.isOpened(), "Restart subscription screen did not launch");

        disneyPlusAppleTVRestartSubscriptionPage.clickLogoutButtonIfHasFocus();
        sa.assertTrue(disneyPlusAppleTVRestartSubscriptionPage.isLogoutPageOpen(), "LOG OUT menu from restart subscription did not launch");

        disneyPlusAppleTVRestartSubscriptionPage.clickLogoutAlertBtn();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch from restart subscription log out");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90596"})
    @Test(description = "Verify Restart subscription screen details", groups = {"Smoke"})
    public void verifyCompleteSubButtonOnUnSubbedRegisteredAccount() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVCompletePurchasePage disneyPlusAppleTVCompletePurchasePage = new DisneyPlusAppleTVCompletePurchasePage(getDriver());
        DisneyAccount disneyAccount = disneyAccountApi.createAccount(country, language);

        logInWithoutHomeCheck(disneyAccount);

        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isCompleteSubscriptionBtnPresent(), "Complete Sub button not present");
        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isLogOutBtnPresent(), "Log Out button not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91057"})
    @Test(description = "User logging in with a single profile is taken to home page", groups = {"Smoke"})
    public void userLoggingInWithASingleProfileTakesUserToHome() {
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91059", "XCDQA-91061", "XCDQA-91063"})
    @Test(description = "User logging in with an account that has multiple profiles is taken to Who's Watching", groups = {"Smoke"})
    public void userLoggingInWithMultipleProfilesIsTakenToProfileSelection() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        String testProfile = "test";
        disneyAccountApi.addProfile(entitledUser, testProfile, language, null, false);
        String whoIsWatchingTitle = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CHOOSE_PROFILE_TITLE.getText());
        String editProfileBtn = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_EDIT_PROFILE.getText());
        String addProfileBtn = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_PROFILE.getText());

        logInWithoutHomeCheck(entitledUser);

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who's Watching page did not launch");

        sa.assertEquals(disneyPlusAppleTVWhoIsWatchingPage.getCollectionHeadlineTitleText(), whoIsWatchingTitle);

        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, entitledUser.getProfiles().get(0).getProfileName(), ROUND_TILE.getText())
                .assertLabelContainsCaption(sa, testProfile, ROUND_TILE.getText())
                .assertLabelContainsCaption(sa, addProfileBtn, ROUND_TILE.getText());

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isDynamicAccessibilityIDElementPresent(editProfileBtn),
                "The following button text was not found " + editProfileBtn);

        disneyPlusAppleTVWhoIsWatchingPage.clickRight();
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, entitledUser.getProfiles().get(0).getProfileName(), ROUND_TILE_HOVERED.getText());

        disneyPlusAppleTVWhoIsWatchingPage.clickRight();
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, testProfile, ROUND_TILE_HOVERED.getText());

        disneyPlusAppleTVWhoIsWatchingPage.clickRight();
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, addProfileBtn, ROUND_TILE_HOVERED.getText());

        disneyPlusAppleTVWhoIsWatchingPage.clickDown();
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, editProfileBtn, BUTTON_HOVERED.getText());

        sa.assertAll();
    }

    //TODO add avatar recognition once training is complete
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91065", "XCDQA-91067"})
    @Test(description = "Verify the appropriate profile is loaded with the appropriate content after logging in", groups = {"Smoke"})
    public void verifyProfileSelectionContentPostLogIn() throws URISyntaxException, IOException {
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        List<ContentSet> sets = searchApi.getAllSetsInHomeCollection(entitledUser, country, language, "PersonalizedCollection");
        List<String> titles = sets.get(1).getTitles();

        disneyAccountApi.patchProfileAvatar(entitledUser, entitledUser.getProfileId(), R.TESTDATA.get("disney_darth_maul_avatar_id"));

        logIn(entitledUser);

        // Only first five items of the first shelf container are visible on the screen
        IntStream.range(0, 5).forEach(i -> {
            String item = titles.get(i);
            sa.assertTrue(disneyPlusAppleTVHomePage.getDynamicCellByLabel(item).isElementPresent(),
                    String.format("%s asset of %s not found on first row", titles, item));
        });

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        sa.assertTrue(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global navigation is not expanded");

        sa.assertTrue(disneyPlusAppleTVHomePage.isAIDElementPresentWithScreenshot(entitledUser.getProfiles().get(0).getProfileName()));

        sa.assertAll();
    }
}
