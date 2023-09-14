package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.email.EmailApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;

public class DisneyPlusForgotPasswordTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62044"})
    @Test(description = "Log in - Verify Forgot Password Link",groups = {"Onboarding"})
    public void testForgotPasswordLink() {
        initialSetup();
        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        SoftAssert softAssert = new SoftAssert();
        handleAlert();
        EmailApi verifyEmail = new EmailApi();
        String subject = "Your one-time passcode";
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        initPage(DisneyPlusLoginIOSPageBase.class).submitEmail(disneyAccount.get().getEmail());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isForgotPasswordLinkDisplayed(), "Forgot password link not displayed");
        Date startTime = verifyEmail.getStartTime();
        disneyPlusPasswordIOSPageBase.clickForgotPasswordLink();
        softAssert.assertTrue(initPage(DisneyPlusOneTimePasscodeIOSPageBase.class).isOpened(), "Forgot password page not opened");
        String otp = verifyEmail.getDisneyOTP(disneyAccount.get().getEmail(), startTime);
        softAssert.assertTrue(otp.length() > 0);
        softAssert.assertAll();
    }
}