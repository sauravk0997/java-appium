package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVForgotPasswordPageBase;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVForgotPasswordTests extends DisneyPlusAndroidTVBaseTest {

    ThreadLocal<VerifyEmail> verifyEmail = new ThreadLocal<>();
    private ThreadLocal<DisneyAccount> disneyUser = new ThreadLocal<>();
    private static final String MICKEY_MOUSE_PW = "M1ck3yM0us3#";
    private static final String EMAIL_SUBJECT = "Your one-time passcode";

    @BeforeMethod
    public void testSetup() {
        verifyEmail.set(new VerifyEmail());
        disneyUser.set(disneyAccountApi.get().createAccountForOTP(country, language));
    }

    @Test(description = "Verify OTP screen details", groups = {"smoke"})
    public void oneTimePasscodeScreenDetailsVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66513"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1845"));
        SoftAssert sa = new SoftAssert();
        List<String> expectedTexts = DisneyPlusAndroidTVForgotPasswordPageBase.ForgotPasswordItems.screenTexts;

        disneyPlusAndroidTVWelcomePage.get().isSignUpBtnPresent();
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        disneyPlusAndroidTVLoginPage.get().clickForgotPassword();
        disneyPlusAndroidTVForgotPasswordPageBase.get().hideKeyboardOnOTPLanding(false);
        List<String> actualTexts = disneyPlusAndroidTVForgotPasswordPageBase.get().forgotPasswordScreenTexts();

        IntStream.range(0, expectedTexts.size()).forEach(index -> {
            String expectedText = expectedTexts.get(index).equals(DisneyPlusAndroidTVForgotPasswordPageBase.ForgotPasswordItems.BODY_TEXT.getText()) ?
                    apiProvider.get().replaceValue(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), expectedTexts.get(index)), entitledUser.get().getEmail()) :
                    apiProvider.get().getDictionaryItemValue(fullDictionary.get(), expectedTexts.get(index));

            sa.assertEquals(actualTexts.get(index), expectedText);
        });

        sa.assertTrue(disneyPlusAndroidTVForgotPasswordPageBase.get().isDisneyPlusLogoPresent(),
                "Disney Logo should be present");

        List<String> pinCodeGhost = disneyPlusAndroidTVForgotPasswordPageBase.get().getPinCodeGhost();

        IntStream.range(0, 5).forEach(i -> sa.assertEquals(pinCodeGhost.get(i),"0"));

        checkAssertions(sa);
    }

    @Test(description = "Reset the password via the OTP flow and login with the updated password")
    public void resetPasswordAndLogin() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66515", "XCDQA-66521"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1846"));
        SoftAssert sa = new SoftAssert();

        Date startTime = verifyEmail.get().getStartTime();
        String changePW = "M1ck3yM0us3$";

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(disneyUser.get().getEmail());

        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isEnterPasswordPageOpen(), "Enter password page should be visible after selecting login using password.");

        disneyPlusAndroidTVLoginPage.get().proceedToOTPPageUsingRemote();
        String otp = verifyEmail.get().getDisneyOTP(disneyUser.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        disneyUser.get().setUserPass(changePW);
        disneyPlusAndroidTVForgotPasswordPageBase.get().enterOtp(otp);
        disneyPlusAndroidTVForgotPasswordPageBase.get().hideKeyboardOnOTPLanding(true);

        sa.assertEquals(disneyPlusAndroidTVForgotPasswordPageBase.get().getInputText(), otp);

        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(disneyUser.get().getUserPass());

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), "Discover/Home page should be visible after resetting password");

        new AndroidUtilsExtended().clearAppCache();
        login(disneyUser.get());

        checkAssertions(sa);
    }

    @Test(description = "Click on resend email button, verify resend email screen texts and OK button functionality ")
    public void resendEmailScreenVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66544", "XCDQA-66546", "XCDQA-66548"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1847"));
        SoftAssert sa = new SoftAssert();

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        disneyPlusAndroidTVLoginPage.get().clickForgotPassword();
        disneyPlusAndroidTVForgotPasswordPageBase.get().hideKeyboardOnOTPLanding(false);
        disneyPlusAndroidTVForgotPasswordPageBase.get().moveToResendEmailBtnAndSelect();
        List<String> actualTexts = disneyPlusAndroidTVForgotPasswordPageBase.get().resendEmailTexts();

        IntStream.range(0, actualTexts.size()).forEach(i -> {
            String expectedText = apiProvider.get().getDictionaryItemValue(fullDictionary.get(),
                    DisneyPlusAndroidTVForgotPasswordPageBase.ForgotPasswordItems.resendEmailScreenTexts.get(i));
            sa.assertEquals(actualTexts.get(i), expectedText);
        });

        disneyPlusAndroidTVForgotPasswordPageBase.get().clickOkBtn();
        sa.assertTrue(disneyPlusAndroidTVForgotPasswordPageBase.get().isOTPScreenOpened(), "OTP screen did not open");

        checkAssertions(sa);
    }

    @Test(description = "Check same OTP is resent within 15 minutes and a different one after 15 minutes")
    public void verifyOTPEmailResent() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66550"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1848"));
        SoftAssert sa = new SoftAssert();

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(disneyUser.get().getEmail());
        Date startTime = verifyEmail.get().getStartTime();
        disneyPlusAndroidTVLoginPage.get().clickForgotPassword();
        String otp = verifyEmail.get().getDisneyOTP(disneyUser.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        startTime = verifyEmail.get().getStartTime();
        disneyPlusAndroidTVForgotPasswordPageBase.get().hideKeyboardOnOTPLanding(false);
        disneyPlusAndroidTVForgotPasswordPageBase.get().clickResendEmailBtn();
        String resentOTP = verifyEmail.get().getDisneyOTP(disneyUser.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);

        Assert.assertEquals(otp, resentOTP);

        new AndroidUtilsExtended().clearAppCache();
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(disneyUser.get().getEmail());
        startTime = verifyEmail.get().getStartTime();
        disneyPlusAndroidTVLoginPage.get().clickForgotPassword();
        String beforeFifteenOTP = verifyEmail.get().getDisneyOTP(disneyUser.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        disneyPlusAndroidTVForgotPasswordPageBase.get().hideKeyboardOnOTPLanding(false);

        int count = 0;
        while (count < 36) {
            Assert.assertTrue(disneyPlusAndroidTVForgotPasswordPageBase.get().isOTPScreenOpened(), "OTP Screen isn't open");
            getDriver();
            pause(25);
            count++;
        }
        startTime = verifyEmail.get().getStartTime();
        disneyPlusAndroidTVForgotPasswordPageBase.get().clickResendEmailBtn();
        String afterFifteenOTP = verifyEmail.get().getDisneyOTP(disneyUser.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);

        Assert.assertNotEquals(beforeFifteenOTP, afterFifteenOTP);

        checkAssertions(sa);
    }

    @AfterMethod
    public void cleanUp() {
        disneyAccountApi.get().resetUserPassword(disneyUser.get(), MICKEY_MOUSE_PW);
    }
}
