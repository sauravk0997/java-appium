package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEdnaDOBCollectionPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.alice.labels.AliceLabels.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;
import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;

public class DisneyPlusAppleTVLoginTests extends DisneyPlusAppleTVBaseTest {
    private static final String WELCOME_SCREEN_NOT_LAUNCH_ERROR_MESSAGE = "Welcome screen did not launch";
    private static final String EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE = "Email input screen did not launch";
    private static final String NO_EMAIL_INPUT_ERROR_FOUND_ERROR_MESSAGE = "No email input error was found.";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90606", "XCDQA-90604"})
    @Test(description = "Email Input screen: Navigate Back", groups = {TestGroup.ONBOARDING, TestGroup.SMOKE})
    public void verifyReturnToWelcomeScreen() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        welcomeScreen.clickLogInButton();
        sa.assertTrue(loginPage.isOpened(), EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        loginPage.clickMenu();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90608"})
    @Test(description = "Email Input screen: Screen details/appearance", groups = {TestGroup.ONBOARDING, TestGroup.SMOKE})
    public void emailInputScreenAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldFocused(), "Email input is not focused by default");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isHeadlineHeaderTextPresent());
        sa.assertTrue(disneyPlusAppleTVLoginPage.isContinueButtonDisplayed(), "Continue button is not present");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldDisplayed(), "Email text field is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90610"})
    @Test(description = "Email Input screen: Navigation", groups = {TestGroup.ONBOARDING})
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
    @Test(description = "Email Input screen: on-screen keyboard appearance", groups = {TestGroup.ONBOARDING})
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
    @Test(description = "Check for the error message with no email input", groups = {TestGroup.ONBOARDING})
    public void verifyErrorMessageWithNoEmailInput() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        welcomeScreen.clickLogInButton();
        sa.assertTrue(loginPage.isOpened(), EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        loginPage.clickContinueBtn();
        sa.assertTrue(loginPage.isAttributeValidationErrorMessagePresent(), NO_EMAIL_INPUT_ERROR_FOUND_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90602"})
    @Test(description = "Check for error message with invalid email input", groups = {TestGroup.ONBOARDING})
    public void verifyErrorMessageWithInvalidEmailInput() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen("somethin!^&&#@gmail");
        sa.assertTrue(loginPage.isAttributeValidationErrorMessagePresent(), NO_EMAIL_INPUT_ERROR_FOUND_ERROR_MESSAGE);
        sa.assertAll();
    }

    //TODO this test is not enabled due to Bookworm QAA-16228
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90106"})
    @Test(groups = {TestGroup.ONBOARDING}, enabled = false)
    public void verifyTryAgainBringsBackToEmailEntry() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        String errorMessage = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, LOGIN_INVALID_EMAIL_ERROR.getText());
        String continueBtnText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_CONTINUE.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(DisneyApiCommon.getUniqueEmail());
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
    @Test(description = "Verify user flow from unknown user screen with completed sign up", groups = {TestGroup.ONBOARDING})
    public void verifyUserFlowFromUnknownUserScreenWithCompletedSignUp() {
        SoftAssert sa = new SoftAssert();
        String uniqueUserEmail = DisneyApiCommon.getUniqueEmail();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPageBase = new DisneyPlusEdnaDOBCollectionPageBase(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(uniqueUserEmail);
        disneyPlusAppleTVPasswordPage.clickPassword();
        disneyPlusAppleTVPasswordPage.enterPasswordCreatePassword(R.TESTDATA.getDecrypted("disney_qa_web_d23password"));
        disneyPlusAppleTVPasswordPage.moveToContinueOrDoneBtnKeyboardEntry();
        disneyPlusAppleTVPasswordPage.clickSelect();
        disneyPlusAppleTVPasswordPage.clickSignUp();
        sa.assertTrue(ednaDOBCollectionPageBase.isOpened(), "Edna enforce DOB collection page didn't open after login");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90598"})
    @Test(description = "Given user enters a valid registered email then user is taken Log In password screen", groups = {TestGroup.ONBOARDING})
    public void registeredEmailTakenToLoginPassword() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount disneyAccount = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyAccount.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90701"})
    @Test(description = "Log In Password screen details verification", groups = {TestGroup.ONBOARDING})
    public void verifyPasswordScreenDetails() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), "Welcome screen did not launch");

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        Assert.assertTrue(passwordPage.isOpened(), "Log In password screen did not launch");

        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
        sa.assertTrue(passwordPage.isEnterYourPasswordHeaderPresent(), "Enter your password header was not found.");
        sa.assertTrue(passwordPage.isEnterYourPasswordBodyPresent(getAccount().getEmail()), "Learn more about MyDisney button was not found.");
        sa.assertTrue(passwordPage.isEnterYourPasswordHintPresent(), "Enter your password hint was not found.");
        sa.assertTrue(passwordPage.isCaseSensitiveHintPresent(), "Case Sensitive hint was not found.");
        sa.assertTrue(passwordPage.isForgotPasswordButtonPresent(), "Forgot password button was not found.");
        sa.assertTrue(passwordPage.isLoginNavigationButtonPresent(), "Login navigation button was not found.");
        sa.assertTrue(passwordPage.isLearnMoreAboutMyDisneyButtonPresent(), "Learn more about MyDisney button was not found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90703"})
    @Test(description = "Navigation of Log In Password Screen without password entered", groups = {TestGroup.ONBOARDING})
    public void passwordScreenNavigation() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        String passwordGhost = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, PASSWORD.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getAccount().getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldFocused(), "Password field is not focused on landing");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordText(), passwordGhost);
        disneyPlusAppleTVPasswordPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isLogInBtnFocused(), "Log In button is not focused");
        disneyPlusAppleTVPasswordPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isHavingTroubleLogginInBtnFocused(), "Having trouble logging in button is not focused");
        disneyPlusAppleTVPasswordPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isLearnMoreAboutMyDisneyButtonPresent(), "Learn more about MyDisney button is not focused");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90705"})
    @Test(description = "On Password Screen selecting enter your password displays on screen keyboard", groups = {TestGroup.ONBOARDING})
    public void verifyPasswordOnScreenKeyboard() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getAccount().getEmail());
        disneyPlusAppleTVLoginPage.moveDown(1,1);

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Log In password screen did not launch");
        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        disneyPlusAppleTVPasswordPage.clickPassword();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldDisplayed(), "Password field is not displayed");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "KeyboardScreen did not launch");

        disneyPlusAppleTVPasswordPage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(),
                "Log In password screen did not launch after pressing menu from on screen keyboards screen");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90707", "XCDQA-90709", "XCDQA-90711"})
    @Test(description = "Verifying hide/show button on the password entry onscreen keyboard", groups = {TestGroup.ONBOARDING})
    public void passwordEntryEncryptionVerification() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        String encryptedPassword = "••••";
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), "Welcome screen did not launch");

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        sa.assertTrue(passwordPage.isOpened(), "Log In password screen did not launch");

        passwordPage.clickPassword();
        sa.assertTrue(loginPage.isKeyboardPresent(), "KeyboardScreen did not launch");

        IntStream.range(0, 4).forEach(i -> {
            passwordPage.clickSelect();
            passwordPage.clickRight();
        });
        //Move down to continue button and select it
        passwordPage.moveToContinueOrDoneBtnKeyboardEntry();
        passwordPage.clickSelect();
        sa.assertTrue(passwordPage.isOpened(),
                "Log In password screen did not launch after pressing menu from on screen keyboards screen");
        sa.assertEquals(passwordPage.getSecureTextEntryField().getText(), encryptedPassword);

        //Move to show password button from login button
        passwordPage.moveUp(2, 1);
        passwordPage.moveRight(1, 1);
        passwordPage.getHidePassword().click();
        sa.assertTrue(passwordPage.getStaticTextByLabelContains("abcd").isPresent(), "XCDQA-90709 - Password was not displayed");
        sa.assertTrue(passwordPage.getShowPassword().isPresent(), "`Show password` was not present.");

        //Click hide password button and it should turn into show password
        passwordPage.getShowPassword().click();
        sa.assertEquals(passwordPage.getSecureTextEntryField().getText(), encryptedPassword,
                "XCDQA-907011 - Password was not encrypted");
        sa.assertTrue(passwordPage.getHidePassword().isPresent(), "`Hide password` was not present.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90697"})
    @Test(description = "On enter password screen when no input is provided but login is attempted an error should be prompted", groups = {TestGroup.ONBOARDING})
    public void verifyNoInputPasswordError() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), "Welcome screen did not launch");

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        Assert.assertTrue(passwordPage.isOpened(), "Log In password screen did not launch");

        passwordPage.moveDown(1, 1);
        passwordPage.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).click();
        Assert.assertTrue(passwordPage.isAttributeValidationErrorMessagePresent(), "Empty password error did not display");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90699"})
    @Test(description = "When user enters an incorrect password an appropriate error message is prompted to the user", groups = {TestGroup.ONBOARDING})
    public void verifyIncorrectPasswordErrorMessage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        passwordPage.logInWithPassword(RandomStringUtils.randomAlphabetic(6));
        sa.assertTrue(passwordPage.isInvalidCredentialsDisplayed(), "Invalid Password error not displayed.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90689"})
    @Test(description = "When user enters the correct password then a validating screen is prompted prior to login", groups = {TestGroup.ONBOARDING})
    public void validatingScreenPostLogIn() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        logInWithoutHomeCheck(getAccount());

        disneyPlusAppleTVPasswordPage.waitForPasswordPageToDisappear();
        aliceDriver.screenshotAndRecognize()
                .isLabelPresent(sa, AliceLabels.LOADING_ANIMATION.getText());

        sa.assertTrue(homePage.isOpened(),
                "Home page did not launch for single profile user after logging in");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90691"})
    @Test(description = "User logging in with a single profile account will be taken directly to home page", groups = {TestGroup.ONBOARDING, TestGroup.SMOKE})
    public void singleProfileAccountIsTakenToHomePage() {
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logIn(entitledUser);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90693"})
    @Test(description = "User logging in with multiple profile account will be taken Who's Watching", groups = {TestGroup.ONBOARDING})
    public void multipleProfileAccountIsTakenToProfileSelection() {
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(entitledUser).profileName("test").language(getLanguage()).avatarId(null).kidsModeEnabled(false).dateOfBirth(null).build());

        logInWithoutHomeCheck(entitledUser);

        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page did not launch");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90695"})
    @Test(description = "User's profile selected post login is displayed in global nav", groups = {TestGroup.ONBOARDING})
    public void profileNameRetention() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        logIn(getAccount());

        disneyPlusAppleTVHomePage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVHomePage.isHomeBtnPresent(), "Home button is not displayed");
        disneyPlusAppleTVHomePage.moveUp(2,1);

        aliceDriver.screenshotAndRecognize()
                .assertLabelContainsCaption(sa, "", AliceLabels.PROFILE_BUTTON_HOVERED.getText());
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90591", "XCDQA-90593"})
    @Test(groups = {TestGroup.ONBOARDING}, enabled = false)
    public void verifyRestartSubscriptionScreenDetails() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusAppleTVRestartSubscriptionPage disneyPlusAppleTVRestartSubscriptionPage = new DisneyPlusAppleTVRestartSubscriptionPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyAccount disneyAccount = getAccountApi().createExpiredAccount(ENTITLEMENT_LOOKUP, getCountry(), getLanguage(), "V2");
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
    @Test(groups = {TestGroup.ONBOARDING}, enabled = false)
    public void verifyCompleteSubButtonOnUnSubbedRegisteredAccount() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVCompletePurchasePage disneyPlusAppleTVCompletePurchasePage = new DisneyPlusAppleTVCompletePurchasePage(getDriver());
        DisneyAccount disneyAccount = getAccountApi().createAccount(getCountry(), getLanguage());

        logInWithoutHomeCheck(disneyAccount);

        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isCompleteSubscriptionBtnPresent(), "Complete Sub button not present");
        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isLogOutBtnPresent(), "Log Out button not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91057"})
    @Test(description = "User logging in with a single profile is taken to home page", groups = {TestGroup.ONBOARDING, TestGroup.SMOKE})
    public void userLoggingInWithASingleProfileTakesUserToHome() {
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logIn(entitledUser);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91059", "XCDQA-91061", "XCDQA-91063"})
    @Test(groups = {TestGroup.ONBOARDING}, enabled = false)
    public void userLoggingInWithMultipleProfilesIsTakenToProfileSelection() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        String testProfile = "test";
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(entitledUser).profileName(testProfile).language(getLanguage()).avatarId(null).kidsModeEnabled(false).dateOfBirth(null).build());
        String whoIsWatchingTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CHOOSE_PROFILE_TITLE.getText());
        String editProfileBtn = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_EDIT_PROFILE.getText());
        String addProfileBtn = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_PROFILE.getText());

        logInWithoutHomeCheck(entitledUser);

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who's Watching page did not launch");

        sa.assertEquals(disneyPlusAppleTVWhoIsWatchingPage.getCollectionHeadlineTitleText(), whoIsWatchingTitle);

        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, entitledUser.getProfiles().get(0).getProfileName(), ROUND_TILE_HOVERED.getText())
                .assertLabelContainsCaption(sa, testProfile, ROUND_TILE.getText())
                .assertLabelContainsCaption(sa, addProfileBtn, ROUND_TILE.getText());

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isDynamicAccessibilityIDElementPresent(editProfileBtn),
                "The following button text was not found " + editProfileBtn);

        disneyPlusAppleTVWhoIsWatchingPage.clickRight();
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, testProfile, ROUND_TILE_HOVERED.getText());

        disneyPlusAppleTVWhoIsWatchingPage.clickRight();
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, addProfileBtn, ROUND_TILE_HOVERED.getText());

        disneyPlusAppleTVWhoIsWatchingPage.clickDown();
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, editProfileBtn, BUTTON_HOVERED.getText());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91065"})
    @Test(description = "Verify the appropriate profile is loaded with the appropriate content after logging in", groups = {TestGroup.ONBOARDING})
    public void verifyProfileSelectionContentPostLogIn() throws URISyntaxException, IOException {
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();

        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        ArrayList<Container> collections = disneyBaseTest.getDisneyAPIPage(HOME_PAGE.getEntityId());
        // Recommended collection titles are been listed
        List<String> titles = disneyBaseTest.getContainerTitlesFromApi(collections.get(1).getId(), 50);

        getAccountApi().patchProfileAvatar(getAccount(), getAccount().getProfileId(), R.TESTDATA.get("disney_darth_maul_avatar_id"));

        logIn(getAccount());

        disneyPlusAppleTVHomePage.moveDown(2,1);
        // Only first five items of the first shelf container are visible on the screen
        IntStream.range(0, 5).forEach(i -> {
            String item = titles.get(i);
            sa.assertTrue(disneyPlusAppleTVHomePage.getTypeCellNameContains(item).isElementPresent(),
                    String.format("%s asset of %s not found on first row", titles, item));
        });

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        sa.assertTrue(disneyPlusAppleTVHomePage.isGlobalNavExpanded(), "Global navigation is not expanded");

        sa.assertTrue(disneyPlusAppleTVHomePage.isAIDElementPresentWithScreenshot(getAccount().getProfiles().get(0).getProfileName()));

        sa.assertAll();
    }
}
