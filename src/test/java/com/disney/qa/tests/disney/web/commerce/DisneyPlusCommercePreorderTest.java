package com.disney.qa.tests.disney.web.commerce;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommercePreorderTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        boolean isTestResultSuccess = result.getStatus() == ITestResult.SUCCESS;
        testCleanup(isTestResultSuccess, disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Preorder - Successful Purchase - Credit Card", groups = {"DK", "FI", "NO", "SE", "BE", "PT"})
    public void preorderCreditCardSuccess() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.getCastedDriver().get(commercePage.getPreOrderUrl(locale));

        sa.assertTrue(commercePage.isEuPrivacyAgreementPresent(),
                String.format("EU Privacy Agreement not present on: %s", commercePage.getCurrentUrl()));

        commercePage.typeDplusBaseEmailFieldId(account.getEmail());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.typeDplusBasePasswordFieldId(account.getUserPass());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
            commercePage.getCreditCardName(locale),
            creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        sa.assertTrue(commercePage.isTermsTextPresent(),
                String.format("Terms Text not present on billing page, url: %s", commercePage.getCurrentUrl()));

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        sa.assertTrue(commercePage.isBrandImagePresent(),
                "Brand Image not present after credit card submission");
        sa.assertTrue(commercePage.getCurrentUrl().contains("success"),
                String.format("Current url does not contain `success`, actual: %s", commercePage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "Preorder - Payment Blockage - Credit Card - Another Country", groups = {"DK", "FI", "NO", "SE", "BE", "PT"})
    public void preorderBlockedCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.getCastedDriver().get(commercePage.getPreOrderUrl(locale));

        sa.assertTrue(commercePage.isEuPrivacyAgreementPresent(),
                String.format("EU Privacy Agreement not present on: %s", commercePage.getCurrentUrl()));

        commercePage.typeDplusBaseEmailFieldId(account.getEmail());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.typeDplusBasePasswordFieldId(account.getUserPass());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
            commercePage.getCreditCardName(locale),
            creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        sa.assertTrue(commercePage.isTermsTextPresent(),
                String.format("Terms Text not present on billing page, url: %s", commercePage.getCurrentUrl()));

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        sa.assertTrue(commercePage.isPaymentBlockedBtnIdPresent(),
                "Payment Blocked Button not present");
        sa.assertTrue(commercePage.getCurrentUrl().contains("payment-blocked"),
                String.format("Current url does not contain `payment-blocked`, actual: %s", commercePage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "Preorder - Successful Purchase - PayPal", groups = {"DK", "FI", "NO", "SE", "BE", "PT"})
    public void preOrderPayPalSuccess() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.getCastedDriver().get(commercePage.getPreOrderUrl(locale));

        String initialWindow = getWindowHandle(getDriver());

        sa.assertTrue(commercePage.isEuPrivacyAgreementPresent(),
                String.format("EU Privacy Agreement not present on: %s", commercePage.getCurrentUrl()));

        commercePage.typeDplusBaseEmailFieldId(account.getEmail());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.typeDplusBasePasswordFieldId(account.getUserPass());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.clickPayPalRadioIconById(locale);
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickPayPalSubmitBtnById();

        boolean shouldSkip = commercePage.paypalTransactionHandler(locale);
        skipPayPalTest(shouldSkip);
        commercePage.clickPayPalPurchaseContinueBtnById();

        getDriver().switchTo().window(initialWindow);

        sa.assertTrue(commercePage.isBrandImagePresent(),
                "Brand Image not present after credit card submission");
        sa.assertTrue(commercePage.getCurrentUrl().contains("success"),
                String.format("Current url does not contain `success`, actual: %s", commercePage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "Preorder - Payment Blockage - PayPal - Another Country", groups = {"DK", "FI", "NO", "SE", "BE", "PT"})
    public void preOrderPayPalBlocked() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.getCastedDriver().get(commercePage.getPreOrderUrl(locale));

        String initialWindow = getWindowHandle(getDriver());

        sa.assertTrue(commercePage.isEuPrivacyAgreementPresent(),
                String.format("EU Privacy Agreement not present on: %s", commercePage.getCurrentUrl()));

        commercePage.typeDplusBaseEmailFieldId(account.getEmail());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.typeDplusBasePasswordFieldId(account.getUserPass());
        commercePage.clickGenericValueSubmitBtn();
        commercePage.clickPayPalRadioIconById(locale);
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickPayPalSubmitBtnById();

        commercePage.paypalTransactionHandler("US");
        commercePage.clickPayPalPurchaseContinueBtnById();
        commercePage.clickPayPalPurchaseContinueBtnById();
        getDriver().switchTo().window(initialWindow);

        sa.assertTrue(commercePage.isErrorProcessingPresent(),
                "Error Processing message not present");

        sa.assertAll();
    }

    @Test(description = "Credit Card - LATAM", groups = "BR")
    public void  monthlyLatam() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        SoftAssert sa = new SoftAssert();
        String successUrl = "disneyplus.com/success";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.typeDplusBaseEmailFieldId(account.getEmail());
        userPage.clickSignUpLoginContinueBtn();
        // Need to pause here due to instability - requested data-testid creation for agree & continue button
        pause(HALF_TIMEOUT);
        userPage.clickGenericTypeSubmitBtn();
        userPage.getDplusBasePasswordFieldId().type(account.getUserPass());
        userPage.clickLoginSubmit();
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
            commercePage.getCreditCardName(locale),
            creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        redemptionPage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        String successUrlFull = commercePage.getFullUrl(TIMEOUT, successUrl);

        sa.assertTrue(commercePage.getCurrentUrl().contains(successUrlFull));

        sa.assertAll();
    }
}
