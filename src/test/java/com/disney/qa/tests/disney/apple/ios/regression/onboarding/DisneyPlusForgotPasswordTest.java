package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.email.EmailApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;

public class DisneyPlusForgotPasswordTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62044"})
    @Test(description = "Log in - Verify Forgot Password Link",groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testForgotPasswordLink() {
        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        SoftAssert softAssert = new SoftAssert();
        EmailApi emailApi = new EmailApi();
        String subject = "Your one-time passcode";
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        initPage(DisneyPlusLoginIOSPageBase.class).submitEmail(getAccount().getEmail());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isForgotPasswordLinkDisplayed(), "Forgot password link not displayed");
        Date startTime = emailApi.getStartTime();
        disneyPlusPasswordIOSPageBase.clickForgotPasswordLink();
        softAssert.assertTrue(initPage(DisneyPlusOneTimePasscodeIOSPageBase.class).isOpened(), "Forgot password page not opened");
        String otp = emailApi.getDisneyOTP(getAccount().getEmail(), startTime);
        softAssert.assertTrue(otp.length() > 0);
        softAssert.assertAll();
    }
}
