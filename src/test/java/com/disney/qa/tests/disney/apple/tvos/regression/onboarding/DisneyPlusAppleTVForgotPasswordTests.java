package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
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

public class DisneyPlusAppleTVForgotPasswordTests extends DisneyPlusAppleTVBaseTest {
    private static final String ONE_TIME_CODE_SCREEN_DID_NOT_OPEN = "One time code screen did not open";
    private static final String WELCOME_SCREEN_DID_NOT_OPEN = "Welcome screen did not launch";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90618"})
    @Test(groups = {TestGroup.ONBOARDING})
    public void menuFromForgotPasswordBringsBackToEnterPassword() {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomePage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getAccount().getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodePage.getLoginButtonWithPassword().click();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodePage.clickMenu();
        Assert.assertTrue(loginPage.isOpened(),
                "Pressing menu from one time passcode page did not take user back to login page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90620"})
    @Test(description = "Verify forgot password screen details", groups = {TestGroup.ONBOARDING})
    public void forgotPasswordScreenDetails() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        SoftAssert sa = new SoftAssert();
        List<String> expectedText = disneyPlusAppleTVForgotPasswordPage.getForgotPasswordExpectedScreenTexts(getAccount().getEmail());
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getAccount().getEmail());

        Assert.assertTrue(oneTimePasscodeIOSPageBase.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");

        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());

        expectedText.forEach(item -> sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isStaticTextPresentWithScreenShot(item),
                "The following text was not present on forgot password screen " + item));

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOTPPlaceholderPresent(), "OTP placeholder is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90624"})
    @Test(description = "verify on screen numeric keyboard functionality", groups = {TestGroup.ONBOARDING})
    public void onScreenNumericKeyboardVerification() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        Date startTime = getEmailApi().getStartTime();
        DisneyAccount disneyOTPAccount = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyOTPAccount.getEmail());
        Assert.assertTrue(oneTimePasscodeIOSPageBase.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);
        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();
        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having Trouble Loggin In page did not launch");

        String otp = getEmailApi().getDisneyOTP(disneyOTPAccount.getEmail(), startTime);
        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);

        disneyPlusAppleTVForgotPasswordPage.clickMenu();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter Password screen did not launch after backing from Having Trouble Loggin In");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90628"})
    @Test(description = "Email is resent with a different code and user is taken to new email sent page", groups = {TestGroup.ONBOARDING})
    public void verifyEmailResentWithDifferentCode() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        Date startTime = getEmailApi().getStartTime();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyUser.getEmail());
        Assert.assertTrue(oneTimePasscodeIOSPageBase.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);
        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");

        String otp = getEmailApi().getDisneyOTP(disneyUser.getEmail(), startTime);
        startTime = getEmailApi().getStartTime();
        forgotPasswordPage.clickResend();
        sa.assertTrue(forgotPasswordPage.isResentEmailHeaderPresent(), "Resent email header is not present.");
        sa.assertTrue(forgotPasswordPage.isResentEmailBodyPresent(), "Resent email body is not present.");

        String otpTwo = getEmailApi().getDisneyOTP(disneyUser.getEmail(), startTime);
        sa.assertEquals(otp, otpTwo, "Original and second OTPs do not match one another.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90644"})
    @Test(description = "Attempting to continue without entering a code should result in an error", groups = {TestGroup.ONBOARDING})
    public void noCodeEntryError() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(oneTimePasscodeIOSPageBase.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having trouble loggin in page did not launch");

        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOTPErrorMessagePresent(), "Error message is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90646"})
    @Test(description = "Attempting to continue entering a wrong code should result in an error", groups = {TestGroup.ONBOARDING})
    public void invalidCodeEntryError() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(oneTimePasscodeIOSPageBase.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having trouble loggin in page did not launch");

        disneyPlusAppleTVForgotPasswordPage.enterOTP("223344");
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOTPErrorMessagePresent(), "Error message is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90652"})
    @Test(description = "Enter OTP -> Create password screen -> back -> Check your email screen -> resend OTP -> entering and submitting original OTP should prompt an error", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void verifyUsedOTPCodeDoesNotWorkAfterResending() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        Date startTime = getEmailApi().getStartTime();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        String otpErrorMessage = disneyPlusAppleTVForgotPasswordPage.getOTPErrorMessage();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = getEmailApi().getDisneyOTP(disneyUser.getEmail(), startTime);
        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        disneyPlusAppleTVForgotPasswordPage.clickBack();
        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Enter password screen did not launch");

        startTime = getEmailApi().getStartTime();
        disneyPlusAppleTVForgotPasswordPage.clickResend();
        sa.assertEquals(disneyPlusAppleTVForgotPasswordPage.getActionableAlertTitle(), disneyPlusAppleTVForgotPasswordPage.getCheckYourEmailScreenTitle());

        // Wait added to discard the error to get the old code
        pause(15);
        String otpTwo = getEmailApi().getDisneyOTP(disneyUser.getEmail(), startTime);
        //select ok on resend OTP email screen
        disneyPlusAppleTVForgotPasswordPage.clickSelect();

        sa.assertNotEquals(otp, otpTwo, "Same OTP was sent after being used");

        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isNumericKeyboardOpen(), "Numeric keyboard did not launch");

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.selectContinueBtnOnKeyboardEntry();

        sa.assertEquals(disneyPlusAppleTVForgotPasswordPage.getErrorMessageLabelText(), otpErrorMessage);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90650"})
    @Test(description = "Verify that 15 minutes after receiving OTP it doesn't work anymore", groups = {TestGroup.ONBOARDING})
    public void otpDoesNotWorkAfterFifteenMinutes() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(oneTimePasscodeIOSPageBase.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        Date startTime = getEmailApi().getStartTime();
        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = getEmailApi().getDisneyOTP(disneyUser.getEmail(), startTime);

        AtomicInteger count = new AtomicInteger(0);
        IntStream.range(0, 60).forEach(i -> {
            pause(15);
            count.addAndGet(15);
            Assert.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(),
                    String.format("Forgot password screen closed after %d seconds elapsed.", count.get()));
        });

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOtpIncorrectErrorPresent());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90685"})
    @Test(groups = {TestGroup.ONBOARDING})
    public void resettingPasswordTakesUserToHome() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodeIOSPageBase =  new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());

        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyUser.getEmail());

        Assert.assertTrue(oneTimePasscodeIOSPageBase.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        Date startTime = verifyEmail.getStartTime();
        oneTimePasscodeIOSPageBase.getLoginButtonWithPassword().click();
        passwordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        forgotPasswordPage.enterOTP(otp);
        forgotPasswordPage.clickContinueBtnOnOTPPage();
        sa.assertTrue(homePage.isOpened(), "Home page is not open after resetting password");

        sa.assertAll();
    }
}
