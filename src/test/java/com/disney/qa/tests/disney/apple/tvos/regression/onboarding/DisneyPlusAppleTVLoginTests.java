package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.explore.response.Container;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;
import static com.disney.qa.api.disney.DisneyEntityIds.HOME_PAGE;

public class DisneyPlusAppleTVLoginTests extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String EMAIL_INPUT_SCREEN_NOT_LAUNCH_ERROR_MESSAGE = "Email input screen did not launch";
    private static final String NO_EMAIL_INPUT_ERROR_FOUND_ERROR_MESSAGE = "No email input error was found";
    private static final String LOG_IN_SCREEN_NOT_LAUNCH_ERROR_MESSAGE = "Log In password screen did not launch";

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
    @Test(description = "Check for the error message with no email input", groups = {TestGroup.ONBOARDING, US})
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66469"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void registeredEmailTakenToLoginPassword() {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        selectAppleUpdateLaterAndDismissAppTracking();

        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomePage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
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
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        Assert.assertTrue(passcodePage.isOpened(), "Log In password screen did not launch");
        passwordPage.moveDown(1, 1);
        passcodePage.clickLoginWithPassword();

        passwordPage.waitForPresenceOfAnElement(passwordPage.getEnterYourPasswordHeader());
        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
        sa.assertTrue(passwordPage.isEnterYourPasswordHeaderPresent(),
                "Enter your password header was not found.");
        sa.assertTrue(passwordPage.isEnterYourPasswordBodyPresent(getAccount().getEmail()),
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

        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        String passwordGhost = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                PASSWORD.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getAccount().getEmail());

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

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getAccount().getEmail());
        disneyPlusAppleTVLoginPage.moveDown(1,1);

        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.isOpened(), "Log In password screen did not launch");
        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
        disneyPlusAppleTVPasswordPage.clickPassword();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldDisplayed(), "Password field is not displayed");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "KeyboardScreen did not launch");

        disneyPlusAppleTVPasswordPage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(),
                "Log In password screen did not launch after pressing menu from on screen keyboards screen");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90707", "XCDQA-65736"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void passwordEntryEncryptionVerification() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        String encryptedPassword = "••••";
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), "Log In password screen did not launch");

        oneTimePasscodePage.clickLoginWithPassword();
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
        sa.assertTrue(passwordPage.getStaticTextByLabelContains("abcd").isPresent(),
                "XCDQA-90709 - Password was not displayed");
        sa.assertTrue(passwordPage.getShowPassword().isPresent(), "`Show password` was not present.");

        //Click hide password button and it should turn into show password
        passwordPage.getShowPassword().click();
        sa.assertEquals(passwordPage.getSecureTextEntryField().getText(), encryptedPassword,
                "XCDQA-907011 - Password was not encrypted");
        sa.assertTrue(passwordPage.getHidePassword().isPresent(), "`Hide password` was not present.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90697"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyNoInputPasswordError() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage passcodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        Assert.assertTrue(passcodePage.isOpened(), "Log In password screen did not launch");
        passwordPage.moveDown(1, 1);
        passcodePage.clickLoginWithPassword();
        passwordPage.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.IDENTITY, NAVIGATION_BTN_LOG_IN.getText())).click();
        Assert.assertTrue(passwordPage.isAttributeValidationErrorMessagePresent(), "Empty password error did not display");
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66556"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyIncorrectPasswordErrorMessage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);

        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.logInWithPassword(RandomStringUtils.randomAlphabetic(6));
        sa.assertTrue(passwordPage.isInvalidCredentialsDisplayed(), "Invalid Password error not displayed.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66553"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void singleProfileAccountIsTakenToHomePage() {
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logIn(entitledUser);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66554"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void multipleProfileAccountIsTakenToProfileSelection() {
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(entitledUser).profileName("test").language(getLanguage()).avatarId(null).kidsModeEnabled(false).dateOfBirth(null).build());

        logInWithoutHomeCheck(entitledUser);
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90695"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void profileNameRetention() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount())
                .profileName(PROFILE_NAME_SECONDARY)
                .language(getAccount().getProfileLang())
                .avatarId(AVATAR_BUZZ)
                .isStarOnboarded(true)
                .kidsModeEnabled(false)
                .dateOfBirth(DOB_1990).build());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage =
                new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());

        logInWithoutHomeCheck(getAccount());
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        disneyPlusAppleTVWhoIsWatchingPage.clickProfile(PROFILE_NAME_SECONDARY);
        Assert.assertTrue(disneyPlusAppleTVHomePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        disneyPlusAppleTVHomePage.clickMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME_SECONDARY);
        Assert.assertTrue(disneyPlusAppleTVHomePage.isProfileNamePresent(PROFILE_NAME_SECONDARY),
                "Expected Profile is not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91057"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, US})
    public void userLoggingInWithASingleProfileTakesUserToHome() {
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logIn(entitledUser);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68209"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyMultipleProfilesDefaultLanding() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).
                profileName(PROFILE_NAME_SECONDARY).dateOfBirth(ADULT_DOB).language(getAccount().getProfileLang()).
                avatarId(RAYA).kidsModeEnabled(false).isStarOnboarded(true).build());
        String whoIsWatchingTitle = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, CHOOSE_PROFILE_TITLE.getText());
        String editProfileBtn = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, BTN_EDIT_PROFILE.getText());
        String addProfileButtonLabel = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.
                APPLICATION, CREATE_PROFILE.getText());

        logInWithoutHomeCheck(getAccount());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        sa.assertEquals(whoIsWatchingPage.getCollectionHeadlineTitleText(), whoIsWatchingTitle,
                "Collection headline title and Who Is Watching Title are not the same");
        sa.assertTrue(whoIsWatchingPage.getStaticTextByLabelContains(editProfileBtn).isPresent(),
                "Edit Profile button not present");
        sa.assertTrue(whoIsWatchingPage.getTypeCellLabelContains(addProfileButtonLabel).isPresent(),
                "Add profile cell not present");
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getTypeCellLabelContains(getAccount().getFirstName())),
                "First profile is not in focus");
        whoIsWatchingPage.moveRight(1, 1);
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getTypeCellLabelContains(PROFILE_NAME_SECONDARY)),
                "Second profile is not in focus");
        whoIsWatchingPage.moveRight(1, 1);
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getTypeCellLabelContains(addProfileButtonLabel)),
                "Add profile is not in focus");
        whoIsWatchingPage.moveDown(1, 1);
        sa.assertTrue(whoIsWatchingPage.isFocused(whoIsWatchingPage.getEditProfileButton()),
                "Edit profile button is not in focus");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-91065"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyProfileSelectionContentPostLogIn() {
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
