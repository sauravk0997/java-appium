package com.disney.qa.tests.disney.web.appex.olpSignup;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import net.lightbody.bmp.BrowserMobProxy;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;


public class DisneyPlusUserFlowPasswordViewTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal();

    BrowserMobProxy proxy;

    private String creditCardSpoof = "4111111111111111";
    private String passwordNumberSpoof = "1234567689";
    private String locale = "US";
    private static final String PASSWORD_ALERT_MSG = "Please enter your current password.";

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @QTestCases(id = "44766")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20214"})
    @Test(description = "Password - Design Review", priority = 1 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordDesignReview() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);

        softAssert
                .assertTrue(userPage.isDisneyLogoIdPresent(),
                "Disney Logo not present on password page");
        softAssert
                .assertTrue(userPage.getDplusBasePasswordFieldId().isElementPresent(),
                "Enter a Password copy not present");
        softAssert
                .assertTrue(userPage.isPasswordFieldPresent(),
                "Password Field not Present");
        softAssert
                .assertTrue(userPage.isPasswordFieldShowLblPresent(),
                "Show field not present");
        softAssert
                .assertTrue(userPage.isPasswordHelpPresent(),
                "Minimum of 6 characters copy not present");
        softAssert
                .assertTrue(userPage.isPasswordSubmitBtnPresent(),
                "Continue Button not present");

        softAssert.assertAll();
    }

    @QTestCases(id = "44767")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20216"})
    @Test(description = "Password - Show/Hide toggle", priority = 2 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordVisibilityToggle() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        String password = userPage.retrieveCurrentTime();

        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);
        userPage.enterPassword(password);
        softAssert
                .assertTrue(userPage.isPasswordFieldShowLblPresent(),
                "Show Label is not present");
        softAssert
                .assertTrue(userPage.isPasswordFieldEncrypted(),
                "Password field is not encrypted when show label is present");

        userPage
                .clickShowPasswordLbl();
        softAssert
                .assertNotEquals(userPage.getDplusBasePasswordAtribute("type"), "password",
                "Type attribute not `text` after toggling visibility");

        softAssert.assertAll();
    }

    @QTestCases(id = "44784")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20220"})
    @Test(description = "Password - Abandoning Restarts Flow", priority = 4 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordAbandonRestartFlow() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);

        softAssert
                .assertTrue(userPage.isDisneyLogoIdPresent(),
                "Disney Logo not present on password page");
        softAssert
                .assertTrue(userPage.getDplusBasePasswordFieldId().isElementPresent(),
                "Enter Password Field Not Present");

        userPage
                .clickDisneyLogoId();
        softAssert
                .assertFalse(userPage.getDplusBasePasswordFieldId().isElementPresent(),
                "Enter Password Field Not Present");

        userPage
                .signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);
        softAssert
                .assertTrue(userPage.isDisneyLogoIdPresent(),
                "Disney Logo not present on password page");
        softAssert
                .assertTrue(userPage.getDplusBasePasswordFieldId().isElementPresent(),
                "Enter Password Field Not Present");

        softAssert.assertAll();
    }

    @QTestCases(id = "44991")
    @Test(description = "Password - Error Messages - Too Short", priority = 5 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordErrorTooShort() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);

        userPage
                .enterPassword(userPage.createRandomAlphabeticString(3));
        userPage
                .clickLoginSubmit();
        softAssert
                .assertTrue(userPage.isPasswordAlertPresent(),
                "Password Alert Message not present after clicking continue");

        softAssert.assertAll();
    }

    @QTestCases(id = "44992")
    @Test(description = "Password - Error Messages - Too Long", priority = 6 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordErrorTooLong() {
        SoftAssert softAssert = new SoftAssert();

        String randomString = RandomStringUtils.randomAlphanumeric(257);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);

        userPage
                .enterPassword(randomString + passwordNumberSpoof);
        userPage
                .clickLoginSubmit();
        softAssert
                .assertTrue(userPage.isPasswordAlertPresent(),
                "Password Alert Message not present after clicking continue");

        softAssert.assertAll();
    }

    @QTestCases(id = "44768")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20218"})
    @Test(description = "Password - Continue Button", priority = 3 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordContinueBtn() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        proxy = userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, "code", "country"));
        userPage.signUpGeneratedEmailPassword(locale, false, false, language, disneyAccount, proxy);

        softAssert
                .assertTrue(userPage.isDisneyLogoIdPresent(),
                "Disney Logo did not load under billing");
        softAssert
                .assertTrue(userPage.isCreditIconPresent(),
                "Credit Card Icon is not present after creating password");

        String currentUrl = userPage.getCurrentUrl();
        softAssert.assertTrue(currentUrl.contains("billing"),
                String.format("Url Redirect did not proceed to billing, Current URL: %s", currentUrl));

        softAssert.assertAll();
    }

    @QTestCases(id = "44948")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20224"})
    @Test(description = "Password - Error Messages - Blank password", priority = 7 , groups = {TestGroup.DISNEY_APPEX}) //fix
    public void passwordErrorBlankPassword() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);
        waitForSeconds(2);
        userPage
                .clickPasswordContinueLoginBtn();
        softAssert
                .assertTrue(userPage.isPasswordAlertPresent(),
                "Password Alert Message not present after clicking continue on password creation.");

        String passwordAlertText = userPage.getPasswordAlert().getText();
        PAGEFACTORY_LOGGER.info("text"+ userPage.getPasswordAlert().getText());
        softAssert
                .assertTrue(passwordAlertText.contains("Please enter your current password"),
                "Enter a password alert not visible after clicking continue on password creation.");
        softAssert
                .assertTrue(passwordAlertText.equalsIgnoreCase(PASSWORD_ALERT_MSG),
                String.format("Password alert message does not match: '%s' '%s'", PASSWORD_ALERT_MSG, passwordAlertText));

        softAssert.assertAll();
    }

    @QTestCases(id = "44990")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20222"})
    @Test(description = "Password - Error Messages - Must contain non-number/non-letter", priority = 8 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordErrorNonNumberLetter() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);

        userPage
                .enterPassword(passwordNumberSpoof);
        userPage
                .clickLoginSubmit();
        softAssert
                .assertTrue(userPage.isPasswordAlertPresent(),
                "Password Alert Message not present after clicking continue");

        softAssert.assertAll();
    }

    @QTestCases(id = "44993")
    @Test(description = "Password - Error Messages - Cannot Be Credit Card Number", priority = 9 , groups = {TestGroup.DISNEY_APPEX})
    public void passwordErrorCreditCard() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        userPage.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);

        userPage
                .enterPassword(creditCardSpoof);
        userPage
                .clickLoginSubmit();
        softAssert
                .assertTrue(userPage.isPasswordAlertPresent(),
                "Password Alert Message not present after clicking continue");

        softAssert.assertAll();
    }
}
