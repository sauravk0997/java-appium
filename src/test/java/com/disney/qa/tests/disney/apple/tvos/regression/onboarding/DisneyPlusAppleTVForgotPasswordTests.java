package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.gmail.exceptions.GMailUtilsException;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusAppleTVForgotPasswordTests extends DisneyPlusAppleTVBaseTest {
    private static final String ONE_TIME_CODE_SCREEN_DID_NOT_OPEN = "One time code screen did not open";
    private static final String WELCOME_SCREEN_DID_NOT_OPEN = "Welcome screen did not launch";
    private static final String LOG_IN_SCREEN_DID_NOT_LAUNCH = "Log In screen did not launch";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66523"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void menuFromForgotPasswordBringsBackToEnterPassword() {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomePage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodePage.clickMenu();
        Assert.assertTrue(loginPage.isOpened(),
                "Pressing menu from one time passcode page did not take user back to login page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66513"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void forgotPasswordScreenDetails() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        SoftAssert sa = new SoftAssert();
        List<String> expectedText = disneyPlusAppleTVForgotPasswordPage.getForgotPasswordExpectedScreenTexts(getUnifiedAccount().getEmail());
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());

        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");

        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());

        expectedText.forEach(item -> sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isStaticTextPresentWithScreenShot(item),
                "The following text was not present on forgot password screen " + item));

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOTPPlaceholderPresent(), "OTP placeholder is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90624"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void onScreenNumericKeyboardVerification() throws GMailUtilsException {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        DisneyAccount disneyOTPAccount = getAccountApi().createAccountForOTP(getCountry(), getLanguage());
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomePage.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyOTPAccount.getEmail());
        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        String otp = getEmailApi().getDisneyOTP(disneyOTPAccount.getEmail());
        oneTimePasscodePage.enterOTPCode(otp);
        oneTimePasscodePage.clickMenu();
        Assert.assertTrue(loginPage.isOpened(), LOG_IN_SCREEN_DID_NOT_LAUNCH);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66546"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyEmailResentWithDifferentCode() throws GMailUtilsException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyUser.getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);
        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");

        String otp = getEmailApi().getDisneyOTP(disneyUser.getEmail());
        forgotPasswordPage.clickResend();
        sa.assertTrue(forgotPasswordPage.isResentEmailHeaderPresent(), "Resent email header is not present.");
        sa.assertTrue(forgotPasswordPage.isResentEmailBodyPresent(), "Resent email body is not present.");

        String otpTwo = getEmailApi().getDisneyOTP(disneyUser.getEmail());
        sa.assertEquals(otp, otpTwo, "Original and second OTPs do not match one another.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90644"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void noCodeEntryError() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having trouble loggin in page did not launch");

        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOtpIncorrectErrorPresent(), "Error message is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90646"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void invalidCodeEntryError() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having trouble loggin in page did not launch");

        disneyPlusAppleTVForgotPasswordPage.enterOTP("223344");
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOtpIncorrectErrorPresent(), "Error message is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66522"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void otpDoesNotWorkAfterFifteenMinutes() throws GMailUtilsException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = getEmailApi().getDisneyOTP(disneyUser.getEmail());

        AtomicInteger count = new AtomicInteger(0);
        IntStream.range(0, 60).forEach(i -> {
            pause(15);
            count.addAndGet(15);
            Assert.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(),
                    String.format("Forgot password screen closed after %d seconds elapsed.", count.get()));
        });

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickAgreeAndContinue();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOtpIncorrectErrorPresent());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66516"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void resettingPasswordTakesUserToHome() throws GMailUtilsException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        EmailApi verifyEmail = getEmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail());

        forgotPasswordPage.enterOTP(otp);
        forgotPasswordPage.clickContinueBtnOnOTPPage();
        sa.assertTrue(homePage.isOpened(), "Home page is not open after resetting password");

        sa.assertAll();
    }
}
