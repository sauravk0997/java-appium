package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.client.requests.CreateUnifiedAccountRequest;
import com.disney.qa.api.offer.pojos.Partner;
import com.disney.qa.api.pojos.UnifiedAccount;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.US;

@Listeners(JocastaCarinaAdapter.class)
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
    public void onScreenNumericKeyboardVerification() {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(
                DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomePage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomePage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        String otp = getOTPFromApi(getUnifiedAccount());
        oneTimePasscodePage.enterOTPCode(otp);
        oneTimePasscodePage.clickMenu();
        Assert.assertTrue(loginPage.isOpened(), LOG_IN_SCREEN_DID_NOT_LAUNCH);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66546"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void verifyEmailResentWithDifferentCode() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(
                DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));

        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(welcomeScreen.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomeScreen.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());
        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);
        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickHavingTroubleLogginInBtn();
        Assert.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");

        String otp = getOTPFromApi(getUnifiedAccount());
        forgotPasswordPage.clickResend();
        sa.assertTrue(forgotPasswordPage.isResentEmailHeaderPresent(), "Resent email header is not present.");
        sa.assertTrue(forgotPasswordPage.isResentEmailBodyPresent(), "Resent email body is not present.");

        String otpTwo = getOTPFromApi(getUnifiedAccount());
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

        UnifiedAccount disneyUser = getUnifiedAccountApi()
                .createAccountForOTP(
                        CreateUnifiedAccountRequest.builder()
                                .country("US")
                                .partner(Partner.DISNEY)
                                .language("en")
                                .build()
                );

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66516"})
    @Test(groups = {TestGroup.ONBOARDING, US})
    public void resettingPasswordTakesUserToHome() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage =  new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(
                DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(welcomeScreenPage.isOpened(), WELCOME_SCREEN_DID_NOT_OPEN);

        welcomeScreenPage.clickLogInButton();
        loginPage.proceedToPasswordScreen(getUnifiedAccount().getEmail());

        Assert.assertTrue(oneTimePasscodePage.isOpened(), ONE_TIME_CODE_SCREEN_DID_NOT_OPEN);

        oneTimePasscodePage.clickLoginWithPassword();
        passwordPage.clickHavingTroubleLogginInBtn();

        sa.assertTrue(forgotPasswordPage.isOpened(), "Forgot password page did not launch");
        String otp = getOTPFromApi(getUnifiedAccount());

        forgotPasswordPage.enterOTP(otp);
        forgotPasswordPage.clickContinueBtnOnOTPPage();
        sa.assertTrue(homePage.isOpened(), "Home page is not open after resetting password");

        sa.assertAll();
    }
}
