package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.*;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.offer.pojos.Partner;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.stream.IntStream;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.*;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVLoginTests extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE = "Email input screen did not launch";
    private static final String NO_EMAIL_INPUT_ERROR_FOUND_ERROR_MESSAGE = "No email input error was found";
    private static final String LOG_IN_SCREEN_NOT_LAUNCH_ERROR_MESSAGE = "Log In password screen did not launch";
    private static final String ONE_TIME_PASSCODE_NOT_LAUNCH_ERROR_MESSAGE = "One-time passcode screen did not launch";
    private static final String KEYBOARD_SCREEN_NOT_LAUNCH_ERROR_MESSAGE = "Keyboard Screen did not launch";
    private static final String LOGIN_SCREEN_NOT_LAUNCH_AFTER_PRESS_MENU_ERROR_MESSAGE =
            "Log In password screen did not launch after pressing menu from on screen keyboards screen";
    private static final String PASSWORD_NOT_ENCRYPTED_ERROR_MESSAGE = "Password was not encrypted";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66483"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void verifyReturnToWelcomeScreen() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomeScreen.clickLogInButton();
        sa.assertTrue(loginPage.isOpened(), EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        loginPage.clickMenu();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66485"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void emailInputScreenAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldFocused(), "Email input is not focused by default");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isHeadlineHeaderTextPresent());
        sa.assertTrue(disneyPlusAppleTVLoginPage.isContinueButtonDisplayed(), "Continue button is not present");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldDisplayed(), "Email text field is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66481"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void emailInputNavigation() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isOpened(), "Email input screen did not launch");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldFocused(), "Email input is not focused by default");
        disneyPlusAppleTVLoginPage.navigateToContinueButton();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isContinueButtonFocused(), "Continue button is not focused");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90612", "XCDQA-90614"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void emailInputKeyboardAppearance() {
        String tempEmailText = "bcd";
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
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
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyErrorMessageWithNoEmailInput() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomeScreen.clickLogInButton();
        sa.assertTrue(loginPage.isOpened(), EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        loginPage.clickContinueBtn();
        sa.assertTrue(loginPage.isAttributeValidationErrorMessagePresent(), NO_EMAIL_INPUT_ERROR_FOUND_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66489"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyErrorMessageWithInvalidEmailInput() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen("somethin!^&&#@gmail");
        sa.assertTrue(loginPage.isAttributeValidationErrorMessagePresent(), NO_EMAIL_INPUT_ERROR_FOUND_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66493"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void verifyTryAgainWhenInvalidEmailInput() {
        String unknownEmail = "abc@po.com";
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(unknownEmail);
        Assert.assertTrue(loginPage.getTryAgainButton().isPresent(), "Try Again button not displayed");
        loginPage.clickTryAgainBtn();
        Assert.assertTrue(loginPage.isOpened(), EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        Assert.assertTrue(loginPage.isEmailFieldFocused(), "Email input is not focused by default");
        Assert.assertTrue(loginPage.getStaticTextByLabel(unknownEmail).isPresent(),
                "Previously entered email is not displayed in email text field");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66469"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void registeredEmailTakenToLoginPassword() {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();

        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomePage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), "One time passcode page did not launch");

        oneTimePasscodePage.clickLoginWithPassword();
        Assert.assertTrue(passwordPage.isOpened(), LOG_IN_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-65734"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyPasswordScreenDetails() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage passcodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        Assert.assertTrue(passcodePage.isOpened(), "Log In password screen did not launch");
        passwordPage.moveDown(1, 1);
        passcodePage.clickLoginWithPassword();

        passwordPage.waitForPresenceOfAnElement(passwordPage.getEnterYourPasswordHeader());
        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
        sa.assertTrue(passwordPage.isEnterYourPasswordHeaderPresent(),
                "Enter your password header was not found.");
        sa.assertTrue(passwordPage.isEnterYourPasswordBodyPresent(getUnifiedAccount().getEmail()),
                "Log in to Disney+ with your MyDisney account text was not found.");
        sa.assertTrue(passwordPage.isEnterYourPasswordHintPresent(), "Enter your password hint was not found.");
        sa.assertTrue(passwordPage.isCaseSensitiveHintPresent(), "Case Sensitive hint was not found.");
        sa.assertTrue(passwordPage.isHavingTroubleLoggingInPresent(),
                "Having Trouble Logging In button was not found.");
        sa.assertTrue(passwordPage.isLoginNavigationButtonPresent(), "Login navigation button was not found.");
        sa.assertTrue(passwordPage.isLearnMoreAboutMyDisneyButtonPresent(),
                "Learn more about MyDisney button was not found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90703"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void passwordScreenNavigation() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage =
                new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage =
                new DisneyPlusAppleTVOneTimePasscodePage(getDriver());


        String passwordGhost = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                PASSWORD.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());

        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.isOpened(), "Log In password screen did not launch");
        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldFocused(),
                "Password field is not focused on landing");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordText(), passwordGhost);
        disneyPlusAppleTVPasswordPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isLogInBtnFocused(), "Log In button is not focused");
        disneyPlusAppleTVPasswordPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isHavingTroubleLogginInBtnFocused(),
                "Having trouble logging in button is not focused");
        disneyPlusAppleTVPasswordPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isLearnMoreAboutMyDisneyButtonPresent(),
                "Learn more about MyDisney button is not focused");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-65740"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyPasswordOnScreenKeyboard() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        disneyPlusAppleTVLoginPage.moveDown(1,1);

        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.isOpened(), "Log In password screen did not launch");
        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
        disneyPlusAppleTVPasswordPage.clickPassword();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldDisplayed(), "Password field is not displayed");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "KeyboardScreen did not launch");

        disneyPlusAppleTVPasswordPage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(),
                LOGIN_SCREEN_NOT_LAUNCH_AFTER_PRESS_MENU_ERROR_MESSAGE);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-65736"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyPasswordShowHideButtons() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        String encryptedPassword = "••••";
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_NOT_LAUNCH_ERROR_MESSAGE);

        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickPassword();
        Assert.assertTrue(loginPage.isKeyboardPresent(), KEYBOARD_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);

        IntStream.range(0, 4).forEach(i -> {
            passwordPage.clickSelect();
            passwordPage.clickRight();
        });
        //Move down to continue button and select it
        passwordPage.moveToContinueOrDoneBtnKeyboardEntry();
        passwordPage.clickSelect();
        Assert.assertTrue(passwordPage.isOpened(),
                LOGIN_SCREEN_NOT_LAUNCH_AFTER_PRESS_MENU_ERROR_MESSAGE);

        //Move to show password button from login button
        passwordPage.moveUp(2, 1);
        passwordPage.moveRight(1, 1);
        passwordPage.getHidePassword().click();
        Assert.assertTrue(passwordPage.getStaticTextByLabelContains("abcd").isPresent(),
                "Password did not displayed");
        Assert.assertTrue(passwordPage.getShowPassword().isPresent(), "`Show password` was not present.");

        //Click hide password button should turn into show password
        passwordPage.getShowPassword().click();
        Assert.assertTrue(passwordPage.getHidePassword().isPresent(), "`Hide password` was not present.");
        Assert.assertEquals(passwordPage.getSecureTextEntryField().getText(), encryptedPassword,
                PASSWORD_NOT_ENCRYPTED_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90707"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyPasswordCapturedAndEncrypted() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        String encryptedPassword = "••••";
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_PASSCODE_NOT_LAUNCH_ERROR_MESSAGE);

        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickPassword();
        Assert.assertTrue(loginPage.isKeyboardPresent(), KEYBOARD_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);

        IntStream.range(0, 4).forEach(i -> {
            passwordPage.clickSelect();
            passwordPage.clickRight();
        });
        //Move down to continue button and select it
        passwordPage.moveToContinueOrDoneBtnKeyboardEntry();
        passwordPage.clickSelect();
        Assert.assertTrue(passwordPage.isOpened(), LOG_IN_SCREEN_NOT_LAUNCH_ERROR_MESSAGE);
        Assert.assertEquals(passwordPage.getSecureTextEntryField().getText(), encryptedPassword,
                PASSWORD_NOT_ENCRYPTED_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90697"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyNoInputPasswordError() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage passcodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        Assert.assertTrue(passcodePage.isOpened(), "Log In password screen did not launch");
        passwordPage.moveDown(1, 1);
        passcodePage.clickLoginWithPassword();
        passwordPage.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).click();
        Assert.assertTrue(passwordPage.isAttributeValidationErrorMessagePresent(), "Empty password error did not display");
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66556"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void verifyIncorrectPasswordErrorMessage() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.logInWithPassword(RandomStringUtils.randomAlphabetic(6));
        Assert.assertTrue(passwordPage.isInvalidCredentialsDisplayed(), "Invalid Password error not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66553"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void singleProfileAccountIsTakenToHomePage() {
        logIn(getUnifiedAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91057"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void userLoggingInWithASingleProfileTakesUserToHome() {
        logIn(getUnifiedAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66554"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void multipleProfileAccountIsTakenToProfileSelection() {
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        logInWithoutHomeCheck(getUnifiedAccount());
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90695"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void profileNameRetention() {
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(PROFILE_NAME_SECONDARY)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage =
                new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());

        logInWithoutHomeCheck(getUnifiedAccount());
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        disneyPlusAppleTVWhoIsWatchingPage.clickProfile(PROFILE_NAME_SECONDARY);
        Assert.assertTrue(disneyPlusAppleTVHomePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        disneyPlusAppleTVHomePage.clickMenu();
        Assert.assertTrue(disneyPlusAppleTVHomePage.isProfileNamePresent(PROFILE_NAME_SECONDARY),
                "Expected Profile is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68209"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyMultipleProfilesDefaultLanding() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(PROFILE_NAME_SECONDARY)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        String whoIsWatchingTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, CHOOSE_PROFILE_TITLE.getText());
        String editProfileBtn = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, BTN_EDIT_PROFILE.getText());
        String addProfileButtonLabel = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, CREATE_PROFILE.getText());

        logInWithoutHomeCheck(getUnifiedAccount());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.moveRight(1, 1);
        Assert.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getTypeCellLabelContains(PROFILE_NAME_SECONDARY)),
                "Secondary profile is not focused");

        whoIsWatchingPage.clickSelect();
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);

        sa.assertEquals(whoIsWatchingPage.getCollectionHeadlineTitleText(), whoIsWatchingTitle,
                "Collection headline title and Who Is Watching Title are not the same");
        sa.assertTrue(whoIsWatchingPage.getStaticTextByLabelContains(editProfileBtn).isPresent(),
                "Edit Profile button not present");
        sa.assertTrue(whoIsWatchingPage.getTypeCellLabelContains(addProfileButtonLabel).isPresent(),
                "Add profile cell not present");
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getTypeCellLabelContains(PROFILE_NAME_SECONDARY)),
                "Second profile is not in focus after returning to Profile Selection");

        whoIsWatchingPage.moveLeft(1, 1);
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getTypeCellLabelContains(
                getUnifiedAccount().getFirstName())), "First profile is not in focus");

        whoIsWatchingPage.moveRight(2, 1);
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getTypeCellLabelContains(addProfileButtonLabel)),
                "Add profile is not in focus");
        whoIsWatchingPage.moveDown(1, 1);
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getEditProfileButton()),
                "Edit profile button is not in focus");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106115"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyContactCustomerServiceUI() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVEdnaDOBCollectionPage ednaDOBCollectionPageBase =
                new DisneyPlusAppleTVEdnaDOBCollectionPage(getDriver());
        DisneyPlusAppleTVAccountIsMinorPage accountIsMinorPage =
                new DisneyPlusAppleTVAccountIsMinorPage(getDriver());

        // Create Disney account without DOB and Gender
        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setPartner(Partner.DISNEY)
                .setLanguage(getLocalizationUtils().getUserLanguage())));
        logInWithoutHomeCheck(getUnifiedAccount());

        // Enter under 18 DOB. Forcing user to be blocked and redirected to contact customer service screen
        Assert.assertTrue(ednaDOBCollectionPageBase.isOpened(), EDNA_DOB_COLLECTION_PAGE_NOT_DISPLAYED);
        ednaDOBCollectionPageBase.enterDOB(
                Person.AGE_17.getMonth(), Person.AGE_17.getDay(true), Person.AGE_17.getYear());
        ednaDOBCollectionPageBase.getSaveAndContinueButton().click();

        Assert.assertTrue(accountIsMinorPage.isOpened(), ACCOUNT_IS_MINOR_PAGE_NOT_DISPLAYED);
        sa.assertTrue(accountIsMinorPage.isBodyTextValid(),
                "Body for account blocked screen is not displayed");
        sa.assertTrue(accountIsMinorPage.getDismissButton().isPresent(),
                "Dismiss button is not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-111553"})
    @Test(groups = {TestGroup.ONBOARDING, CA})
    public void verifyOneTrustConsentBanner() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVOneTrustConsentBannerIOSPage oneTrustConsentPage =
                new DisneyPlusAppleTVOneTrustConsentBannerIOSPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSettingsPage settingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        logInWithoutHomeCheck(getUnifiedAccount());

        // Validate One Trust consent page
        Assert.assertTrue(oneTrustConsentPage.isAllowAllButtonPresent(),
                "Accept All button is not present");
        Assert.assertTrue(oneTrustConsentPage.isRejectAllButtonPresent(),
                "Reject All button is not present");
        Assert.assertTrue(oneTrustConsentPage.isCustomizedChoicesButtonPresent(),
                "Customize Choices button is not present");

        // Tap 'Accept All' button and validate banner is not present
        oneTrustConsentPage.tapAcceptAllButton();
        Assert.assertFalse(oneTrustConsentPage.getTvOsBannerAllowAllButton().isPresent(FIVE_SEC_TIMEOUT),
                "One Trust consent banner is present");

        // Log out to welcome screen
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SETTINGS.getText());
        Assert.assertTrue(settingsPage.isOpened(), SETTINGS_PAGE_NOT_DISPLAYED);
        homePage.moveDownUntilElementIsFocused(settingsPage.getLogOutCell(), 8);
        settingsPage.getLogOutCell().click();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        // Create new account and login with it
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        logInWithoutHomeCheck(getUnifiedAccount());

        // Select "Reject All". Validate One Trust consent doesn't shows anymore and user is redirected to homepage
        Assert.assertTrue(oneTrustConsentPage.isRejectAllButtonPresent(),
                "Reject All button is not present");
        oneTrustConsentPage.tapRejectAllButton();
        Assert.assertFalse(oneTrustConsentPage.getTvOsBannerAllowAllButton().isPresent(FIVE_SEC_TIMEOUT),
                "One Trust consent banner is present");
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }
}
