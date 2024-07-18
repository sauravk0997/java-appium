package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.StringGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.BTN_CONTINUE;

public class DisneyPlusAppleTVForgotPasswordTests extends DisneyPlusAppleTVBaseTest {

    private static final String MICKEY_MOUSE_PW = "M1ck3yM0us3#$$";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90618"})
    @Test(description = "Pressing menu from forgot password page brings the user back to enter password screen", groups = {"Onboarding"})
    public void menuFromForgotPasswordBringsBackToEnterPassword() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        SoftAssert sa = new SoftAssert();
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(entitledUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Password page did not launch");

        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");

        disneyPlusAppleTVForgotPasswordPage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(),
                "Pressing menu from forgot password page did not take user back to enter password");

        sa.assertAll();
    }
    //TODO this test will be fix when new flows are updated QAA-14761
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90620"})
    @Test(description = "Verify forgot password screen details", groups = {TestGroup.ONBOARDING})
    public void forgotPasswordScreenDetails() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        SoftAssert sa = new SoftAssert();
        List<String> expectedText = disneyPlusAppleTVForgotPasswordPage.getForgotPasswordExpectedScreenTexts(getAccount().getEmail());
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(getAccount().getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

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
        EmailApi emailApi = new EmailApi();
        DisneyAccount disneyOTPAccount = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyOTPAccount.getEmail());
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = emailApi.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();
        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having Trouble Loggin In page did not launch");

        String otp = emailApi.getDisneyOTP(disneyOTPAccount.getEmail(), startTime);
        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        
        disneyPlusAppleTVForgotPasswordPage.clickMenu();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter Password screen did not launch after backing from Having Trouble Loggin In");

        sa.assertAll();
    }


    //TODO this test will be fix when new flows are updated QAA-14767
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90622", "XCDQA-90626"})
    @Test(description = "Verify that the email with the appropriate subject has been sent and use the OTP to proceed to the Home Screen", groups = {TestGroup.ONBOARDING})
    public void otpEntryAndCodeVerification() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        EmailApi emailApi = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = emailApi.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");

        String otp = emailApi.getDisneyOTP(disneyUser.getEmail(), startTime);

        sa.assertNotNull(otp, "OTP email received after time: " + startTime);

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();
        disneyPlusAppleTVForgotPasswordPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page screen did not launch");

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
        EmailApi emailApi = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), "Welcome screen did not launch");

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyUser.getEmail());
        Assert.assertTrue(passwordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = emailApi.getStartTime();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");

        String otp = emailApi.getDisneyOTP(disneyUser.getEmail(), startTime);
        startTime = emailApi.getStartTime();
        forgotPasswordPage.clickResend();
        sa.assertTrue(forgotPasswordPage.isResentEmailHeaderPresent(), "Resent email header is not present.");
        sa.assertTrue(forgotPasswordPage.isResentEmailBodyPresent(), "Resent email body is not present.");

        String otpTwo = emailApi.getDisneyOTP(disneyUser.getEmail(), startTime);
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
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

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
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having trouble loggin in page did not launch");

        disneyPlusAppleTVForgotPasswordPage.enterOTP("223344");
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOTPErrorMessagePresent(), "Error message is not present");

        sa.assertAll();
    }

    //TODO this test will be fix when new flows are updated QAA-14770
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90652"})
    @Test(description = "Enter OTP -> Create password screen -> back -> Check your email screen -> resend OTP -> entering and submitting original OTP should prompt an error", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void verifyUsedOTPCodeDoesNotWorkAfterResending() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        EmailApi emailApi = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        String otpErrorMessage = disneyPlusAppleTVForgotPasswordPage.getOTPErrorMessage();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = emailApi.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = emailApi.getDisneyOTP(disneyUser.getEmail(), startTime);

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        disneyPlusAppleTVForgotPasswordPage.clickBack();
        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Enter password screen did not launch");

        startTime = emailApi.getStartTime();
        disneyPlusAppleTVForgotPasswordPage.clickResend();
        sa.assertEquals(disneyPlusAppleTVForgotPasswordPage.getActionableAlertTitle(), disneyPlusAppleTVForgotPasswordPage.getCheckYourEmailScreenTitle());

        // Wait added to discard the error to get the old code
        pause(15);
        String otpTwo = emailApi.getDisneyOTP(disneyUser.getEmail(), startTime);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90654"})
    @Test(description = "Verify that after using an otp, coming back from create pass screen and attempting to reuse otp will result in an error", groups = {TestGroup.ONBOARDING})
    public void verifyUsedOTPCodeDoesNotWork() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        String email = disneyUser.getEmail();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(email);

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having trouble loggin in page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page did not launch");

        disneyPlusAppleTVHomePage.clickBack();
        disneyPlusAppleTVHomePage.moveDown(5,1);
        disneyPlusAppleTVHomePage.clickSelect();
        disneyPlusAppleTVHomePage.moveDown(6,1);
        disneyPlusAppleTVHomePage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(email);
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();
        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Having trouble loggin in page did not launch");

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOTPErrorMessagePresent(), "Error message is not present");

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
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

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
    //TODO this test will be fix when new flows are updated QAA-14759
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90630", "XCDQA-90632"})
    @Test(description = "Verify create password screen details", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void createPasswordScreenDetails() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());
        String createPasswordScreenFieldText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ENTER_NEW_PASSWORD.getText());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(passwordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        passwordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        forgotPasswordPage.enterOTP(otp);
        forgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(passwordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());

        sa.assertTrue(forgotPasswordPage.isHeadlineHeaderPresent(), "Headline header 'Create new password' not present");
        sa.assertTrue(forgotPasswordPage.isHeadlineSubtitlePresent(), "Headline subtitle not present");
        sa.assertTrue(forgotPasswordPage.isStaticTextPresentWithScreenShot(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, BTN_CONTINUE.getText())), "Continue button is not present");
        sa.assertEquals(passwordPage.getPasswordFieldText(), createPasswordScreenFieldText);

        forgotPasswordPage.clickMenu();

        sa.assertTrue(forgotPasswordPage.isOpened(),
                "Forgot password screen did not launch after pressing menu from Create password screen");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90634"})
    @Test(description = "Verify create password navigation", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void createPasswordScreenNavigation() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());
        String createPasswordScreenFieldText = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ENTER_NEW_PASSWORD.getText());
        AliceDriver aliceDriver = new AliceDriver(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isNumericKeyboardOpen(), "Numeric keyboard did not launch");

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.selectContinueBtnOnKeyboardEntry();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldFocused(), "Create new password field was not focus when page opened");

        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, createPasswordScreenFieldText, AliceLabels.BUTTON_HOVERED.getText());

        disneyPlusAppleTVForgotPasswordPage.moveDown(1, 1);

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isContinueButtonFocused(), "Continue button was not focused after moving down from create new password field");
        sa.assertAll();
    }

    //TODO this test will be fix when new flows are updated QAA-14758
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90636"})
    @Test(description = "Verify create password on screen keyboard", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void createPasswordOnScreenKeyboardVerification() {
        SoftAssert sa = new SoftAssert();
        String passReqsEnhanced = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PASSWORD_REQS_ENHANCED.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2")));
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        disneyPlusAppleTVPasswordPage.clickPassword();

        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordHintText(), passReqsEnhanced, "Password strength hint message did not display");

        disneyPlusAppleTVForgotPasswordPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isStrengthBarPresent(), "Password strength meter did not display");

        disneyPlusAppleTVForgotPasswordPage.clickMenu();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(),
                "Create a new password screen did not launch after pressing menu from on screen keyboard");

        sa.assertFalse(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "Keyboard should not be present on create password screen");

        sa.assertAll();
    }

    //TODO:Missing color check, add that when Alice is trained to recognize strength meter
    //TODO this test will be fix when new flows are updated QAA-14760
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90638", "XCDQA-90640"})
    @Test(description = "Create password screen strength meter", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void createPasswordScreenStrengthMeterVerification() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());
        List<List<String>> passwordStrengthMeterLists = disneyPlusAppleTVPasswordPage.getStrengthMeterVerificationLists();
        List<String> ratingsList = passwordStrengthMeterLists.get(0);
        List<String> passwordList = passwordStrengthMeterLists.get(1);
        List<String> widthList = passwordStrengthMeterLists.get(2);
        String encryptedPassword = "••••••••••••";
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        disneyPlusAppleTVPasswordPage.clickPassword();

        IntStream.range(0, passwordList.size()).forEach(i -> {
            disneyPlusAppleTVPasswordPage.enterNewPassword(passwordList.get(i));
            sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordStrengthMeterWidth(), Integer.parseInt(widthList.get(i)));
            // for the first password item from passwordList, no meter text is shown
            if (i != 0) {
                String dictionaryKey = ratingsList.get(i);
                sa.assertTrue(disneyPlusAppleTVPasswordPage.isAIDElementPresentWithScreenshot(dictionaryKey), String.format("%s is not present", dictionaryKey));
            }
            // Test was failing because password was not cleared between entries. Back out and go back to password page on each PW entry except the last one.
            if (i != (passwordList.size() - 1)) {
                disneyPlusAppleTVPasswordPage.clickMenu();
                disneyPlusAppleTVPasswordPage.clickPassword();
            }
        });

        disneyPlusAppleTVForgotPasswordPage.moveToContinueOrDoneBtnKeyboardEntry();

        disneyPlusAppleTVForgotPasswordPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(),
                "Create a new password screen did not launch after entering valid password");

        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), encryptedPassword);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90679", "XCDQA-90681", "XCDQA-90683"})
    @Test(description = "Empty password submission on create password screen from forgot password", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void createPasswordNoPasswordSubmissionError() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVCreatePasswordPage createPasswordIOSPageBase = new DisneyPlusAppleTVCreatePasswordPage(getDriver());
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());
        String invalidPassword = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2")));

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");

        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        disneyPlusAppleTVPasswordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        sa.assertTrue(disneyPlusAppleTVForgotPasswordPage.isNumericKeyboardOpen(), "Numeric keyboard did not launch");

        disneyPlusAppleTVForgotPasswordPage.enterOTP(otp);
        disneyPlusAppleTVForgotPasswordPage.selectContinueBtnOnKeyboardEntry();

        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        disneyPlusAppleTVPasswordPage.clickContinueBtn();

        sa.assertTrue(createPasswordIOSPageBase.isEmptyPasswordErrorDisplayed());
        List<String> passwords = Stream.of(StringGenerator.generateWord(5), "Test123").collect(Collectors.toList());
        IntStream.range(0, passwords.size()).forEach(i -> {
            disneyPlusAppleTVPasswordPage.clickPassword();
            if (i == 0) {
                disneyPlusAppleTVPasswordPage.enterNewPassword(passwords.get(i));
            } else {
                disneyPlusAppleTVPasswordPage.clearAndEnterNewPassword(passwords.get(i - 1), passwords.get(i));
            }
            disneyPlusAppleTVPasswordPage.selectContinueBtnOnKeyboardEntry();
            disneyPlusAppleTVPasswordPage.clickContinueBtn();
            sa.assertEquals(disneyPlusAppleTVPasswordPage.getErrorMessageLabelText(), invalidPassword);

        });

        sa.assertAll();
    }

    //TODO this test will be fix when new flows are updated QAA-14768
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90685"})
    @Test(description = "Ensure after resetting password user is taken to home screen", groups = {TestGroup.ONBOARDING}, enabled = false)
    public void resettingPasswordTakesUserToHome() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        EmailApi verifyEmail = new EmailApi();
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");

        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(disneyUser.getEmail());

        sa.assertTrue(passwordPage.isOpened(), "Enter password screen did not launch");

        Date startTime = verifyEmail.getStartTime();
        passwordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = verifyEmail.getDisneyOTP(disneyUser.getEmail(), startTime);

        forgotPasswordPage.enterOTP(otp);
        forgotPasswordPage.clickContinueBtnOnOTPPage();

        sa.assertTrue(passwordPage.isCreateNewPasswordScreenOpen(), "Create a new password screen did not launch");

        passwordPage.clickPassword();
        passwordPage.enterNewPassword(MICKEY_MOUSE_PW);
        passwordPage.selectContinueBtnOnKeyboardEntry();
        passwordPage.clickContinueBtn();

        if(homePage.isGlobalNavExpanded()){
            homePage.clickSelect();
        }
        sa.assertTrue(homePage.isOpened(), "Home page is not open after resetting password");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90687"})
    @Test(description = "Fresh login with newly created password takes user to home screen", groups = {TestGroup.ONBOARDING})
    public void newPasswordAllowsSuccessfulLogIn() {
        DisneyAccount disneyUser = getAccountApi().createAccountForOTP(getCountry(), getLanguage());
        String updatedPassword = MICKEY_MOUSE_PW;
        getAccountApi().resetUserPassword(disneyUser, updatedPassword);
        disneyUser.setUserPass(updatedPassword);

        logInTemp(disneyUser);
    }
}
