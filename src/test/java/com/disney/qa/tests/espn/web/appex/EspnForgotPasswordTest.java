package com.disney.qa.tests.espn.web.appex;

import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.espn.web.EspnForgotPasswordPage;
import com.disney.qa.espn.web.EspnWebParameters;
import com.disney.qa.tests.BaseTest;
import com.disney.util.TestGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.Date;

//smallem//

public class EspnForgotPasswordTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String pwdURL = EspnWebParameters.ESPN_WEB_PWD_URL.getValue();

    private String userGmail = EspnWebParameters.ESPN_WEB_GMAIL_USER.getValue();
    private String passwordGmail = EspnWebParameters.ESPN_WEB_GMAIL_PASS.getDecryptedValue();
    private String emailSubject = "Your ESPN Account Passcode";

    @BeforeTest
    public void beforeTests() {
        EspnForgotPasswordPage.open(getDriver(), pwdURL);
    }

    @AfterTest
    public void afterTests() {
        quitDrivers();
    }

    @Test(description = "Forgot Password Test, Check that an email can be sent to reset password", groups = {TestGroup.ESPN_APPEX})
    public void testEspnForgotPassword() {

        EspnForgotPasswordPage espnForgotPasswordPage = new EspnForgotPasswordPage(getDriver());

        VerifyEmail verifyEmail = new VerifyEmail();
        SoftAssert sa = new SoftAssert();
        LOGGER.info("Url:http://www.espn.com/");
        sa.assertTrue(espnForgotPasswordPage.getLoginIcon().isElementPresent(),  "Expected - LoginIcon is displayed on Homepage");
        espnForgotPasswordPage.getLoginIcon().click();
        sa.assertTrue(espnForgotPasswordPage.getLoginButton().isPresent(),  "Expected - LoginButton is clickable");
        espnForgotPasswordPage.getLoginButton().click();

        getDriver().switchTo().frame(espnForgotPasswordPage.getiFrame().getElement());
        sa.assertTrue(espnForgotPasswordPage.gethelpSignIn().isElementPresent(),  "Expected - Help signing in button is present");
        espnForgotPasswordPage.gethelpSignIn().click();
        sa.assertTrue(espnForgotPasswordPage.getUserID().isVisible(),  "Expected - Email or Username field is displayed" );
        espnForgotPasswordPage.getUserID().type("eplus.webqa@gmail.com");
        sa.assertTrue(espnForgotPasswordPage.getContinue().clickIfPresent(),  "Expected - Continue button is present and clickable");
        espnForgotPasswordPage.getContinue().clickByJs();

        pause(10);
        Date startTime = verifyEmail.getStartTime();

        verifyEmail.scanEspnEmail(userGmail, passwordGmail, emailSubject, startTime);

        sa.assertAll();



    }

}
