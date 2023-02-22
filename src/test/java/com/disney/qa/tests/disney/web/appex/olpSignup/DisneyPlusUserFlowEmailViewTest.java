package com.disney.qa.tests.disney.web.appex.olpSignup;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.pojo.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusUserFlowEmailViewTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> accountApi = new ThreadLocal<>();
    private static final ThreadLocal<DisneyApiProvider> apiProvider = new ThreadLocal<>();
    ThreadLocal<com.disney.qa.api.pojos.DisneyAccount> disneyAccount = new ThreadLocal<>();


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        apiProvider.set(new DisneyApiProvider());
        accountApi.set(new DisneyAccount());
    }

    @QTestCases(id = "44529")
    @Test(description = "Welcome Page Review", priority = 1, groups = {TestGroup.DISNEY_APPEX, "AG","AI","AR","AT","AU","AW","BB","BE","BL","BM","BO","BQ","BR","BS","BZ","CA","CH","CL","CO","CR","CW","DE","DK","DM","DO","EC","ES","FI","FK","FR","GB","GD","GF","GG","GL","GP","GS","GT","GY","HN","HT","IE","IM","IS","IT","JE","JM","JP","KR","KN","KY","LC","LU","MC","MF","MQ","MS","MU","MX","NC","NI","NL","NO","NZ","PA","PE","PR","PT","PY","RE","SE","SG","SR","SV","TC","TT","US","UY","VC","VE","VG","VI","WF","YT"})
    public void welcomePageReview() {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        String currentUrl = userPage.getCurrentUrl();

        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        sa.assertTrue(userPage.verifyLoginBtn(),
                "Login Button not Present on " + currentUrl);
        sa.assertTrue(userPage.getBaseLogo().isElementPresent(),
                "Disney Logo not Present on " + currentUrl);

        sa.assertTrue(userPage.isSignupCtasPresent(),
                "signup ctas not Present on " + currentUrl);

        sa.assertTrue(userPage.isContentBrandsPresent(),
                "Brand Logos not Present on " + currentUrl);
        sa.assertTrue(userPage.isFooterIdPresent(),
                "Footer not Present on " + currentUrl);

        if (userPage.isUSCountry(locale)) {
                sa.assertTrue(userPage.isSuperBundlePresent(), "Super Bundle not Present on " + currentUrl);
        }

        checkAssertions(sa);
    }

    @QTestCases(id = "44538")
    @Test(description = "Sign Up Review", priority = 2, groups = {TestGroup.DISNEY_APPEX, "AG","AI","AR","AT","AU","AW","BB","BE","BL","BM","BO","BQ","BR","BS","BZ","CA","CH","CL","CO","CR","CW","DE","DK","DM","DO","EC","ES","FI","FK","FR","GB","GD","GF","GG","GL","GP","GS","GT","GY","HN","HT","IE","IM","IS","IT","JE","JM","KN","KY","LC","LU","MC","MF","MQ","MS","MU","MX","NC","NI","NL","NO","NZ","PA","PE","PR","PT","PY","RE","SE","SG","SR","SV","TC","TT","US","UY","VC","VE","VG","VI","WF","YT"})
    public void signUpReview() {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        userPage.handleStandaloneCtasOnOlp();

        sa.assertTrue(userPage.getDssLoginWrapperId().isElementPresent(),
        "Login Wrapper not present on " + userPage.getCurrentUrl());

        sa.assertTrue(userPage.isSignUpLoginContinueBtnPresent(),
                "Sign up title not present on " + userPage.getCurrentUrl());

        new SeleniumUtils(getDriver()).scroll(0,100);

        sa.assertTrue(userPage.getOnboardingStepper().isElementPresent(),
                "First step for onboarding Stepper not Present " + userPage.getCurrentUrl());

        checkAssertions(sa);
    }

    @QTestCases(id = "44539")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19720"})
    @Test(description = "Marketing proceed with opt-in", priority = 3, groups = {TestGroup.DISNEY_APPEX})
    public void emailOptInMarketing() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        String signUpEmail = DisneyApiCommon.getUniqueEmail();

        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        userPage.handleStandaloneCtasOnOlp();
        userPage.typeEmailFieldId(signUpEmail);

        sa.assertTrue(userPage.isSignUpMarketingOptInChecked(),
                "Marketing Tick Box is Unchecked");

        userPage.clickOnSignupContinueButton();

        sa.assertTrue(userPage.getDplusBasePasswordFieldId().isElementPresent(),
                "Enter Password Header not Present after clicking agree and continue.");

        checkAssertions(sa);
    }

    @QTestCases(id = "44540")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19722"})
    @Test(description = "Marketing proceed with opt-out", priority = 4, groups = {TestGroup.DISNEY_APPEX})
    public void emailOptOutMarketing() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        String signUpEmail = DisneyApiCommon.getUniqueEmail();

        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        userPage.handleStandaloneCtasOnOlp();
        userPage.typeEmailFieldId(signUpEmail);
        userPage.clickMarketingOptInCheckBox();

        sa.assertFalse(userPage.isSignUpMarketingOptInChecked(),
                "Marketing Tick Box is still checked when trying to opt out");

        userPage.clickOnSignupContinueButton();

        sa.assertTrue(userPage.getDplusBasePasswordFieldId().isElementPresent(),
                "Enter Password Header not Present after clicking agree and continue.");

        checkAssertions(sa);
    }

    @QTestCases(id = "44557")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19724"})
    @Test(description = "Terms and Conditions link", priority = 5 , groups = {TestGroup.DISNEY_APPEX})
    public void emailTermsConditions() {
        SoftAssert sa = new SoftAssert();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        userPage.handleStandaloneCtasOnOlp();

        sa.assertTrue(userPage.isSubscriberAgreementBtnPresent(),
                "Subscriber Agreement Link not Present");

        userPage.clickSubscriberAgreementBtn();

        sa.assertTrue(userPage.getLegalCenterTitle().isElementPresent(),
                "Legal Center Title not present on " + userPage.getCurrentUrl());

        checkAssertions(sa);
    }

    @QTestCases(id = "44559")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19726"})
    @Test(description = "Privacy Policy link", priority = 6 , groups = {TestGroup.DISNEY_APPEX})
    public void emailPrivacyPolicy() {
        SoftAssert sa = new SoftAssert();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        userPage.handleStandaloneCtasOnOlp();
        analyticPause();
        sa.assertTrue(userPage.isPrivacyLinkPresent(),
                "Privacy Link Not Present");

        userPage.clickPrivacyLink();

        sa.assertTrue(userPage.getLegalCenterTitle().isElementPresent(),
                "Legal Center Title not present on " + userPage.getCurrentUrl());

        checkAssertions(sa);
    }

    @QTestCases(id = "44560")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19728"})
    @Test(description = "Sign Up Email Entry - Invalid Email Error Message", priority = 7, groups = {TestGroup.DISNEY_APPEX})
    public void emailError() {
        SoftAssert sa = new SoftAssert();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        userPage.handleStandaloneCtasOnOlp();
        userPage.typeEmailFieldId("");

        userPage.clickOnSignupContinueButton();

        sa.assertTrue(userPage.isInvalidEmailMsgPresent(),
                "Invalid Email Message Not Present");

       checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-30891"})
    @Test(description = "Login - Verify Login Elements", groups = {TestGroup.DISNEY_APPEX})
    public void verifyLoginElements() {
        SoftAssert sa = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        loginPage.clickDplusBaseLoginBtn();

        sa.assertTrue(loginPage.isDisneyLogoIdPresent(),
                "Disney Logo not present on email page");

        sa.assertTrue(loginPage.isDplusLoginEmailTextPresent(),
                "Log in with your email text not present on email page");

        sa.assertTrue(loginPage.isDplusBaseEmailFieldIdPresent(),
                "Email text box not present on email page");

        sa.assertTrue(loginPage.isSignUpLoginContinueBtnPresent(),
                "Continue button not present on email page");

        sa.assertTrue(loginPage.isDplusNewToDisneyParagraphPresent(),
                "New to Disney paragraph not present on email page");

        sa.assertTrue(loginPage.isDplusSignUpLinkPresent(),
                "Sign up link not present on email page");

        sa.assertTrue(loginPage.isEmailGhostFillPresent(),
                "Email ghost fill not visible on email page");

        loginPage.typeDplusBaseEmailFieldId("Test");

        sa.assertFalse(loginPage.isEmailGhostFillPresent(),
                "Email ghost fill is still visible on email page");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-30890"})
    @Test(description = "Login - Verify Existing Account", groups = {TestGroup.DISNEY_APPEX, "US"})
    public void verifyExistingAccountLogin() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        loginPage
                .clickDPlusBaseLoginButton()
                .enterEmail(ProfileConstant.MAIN_TEST)
                .clickSignUpLoginContinueButton();
        softAssert
                .assertTrue(loginPage.isErrorCodeSixPresent(),
                "Error code six not present on email page");
        loginPage
                .enterEmail(disneyAccount.get().getEmail().substring(1))
                .clickSignUpLoginContinueButton();
        softAssert
                .assertTrue(loginPage.isEmailNotFoundMessagePresent(),
                "We couldn't find an account not present on email page");
        loginPage
                .clickTryAgainButton();
        softAssert
                .assertTrue(loginPage.isUnknownEmailErrorPresent(),
                "Unknown email error message not present on email page");
        loginPage
                .enterEmail(disneyAccount.get().getEmail())
                .clickSignUpLoginContinueButton();
        softAssert
                .assertTrue(loginPage.isLoginButtonPresent(),
                "Login Button not present on password page");

        softAssert.assertAll();
    }
}
