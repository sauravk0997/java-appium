package com.disney.qa.tests.disney.web.commerce.accountstatus;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.DisneyProductData;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.DisneyGlobalUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
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

public class DisneyPlusCommerceAccountHoldTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void setupTest(){
        disneyAccount.set(new DisneyAccount());
    }


    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Payment Change - Credit Card", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void accountHoldCreditCard() throws JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52109"));

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");
        commercePage.clickAccountHoldUpdatePaymentCta();
        commercePage.assertUrlContains(sa, "change-payment-info");
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

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
        DisneyPlusUserPage userPage =  new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Payment Change - PayPal", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void paymentChangePp() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52110"));

        SoftAssert sa = new SoftAssert();
        String paymentType = "paypal";

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");
        commercePage.clickAccountHoldUpdatePaymentCta();

        pause(HALF_TIMEOUT); // Needed to slow down the test
        commercePage.clickPayPalRadioIconById(locale);
        commercePage.assertUrlContains(sa, "change-payment-info");

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        DisneyPlusUserPage userPage =  new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Payment Change - Ideal", groups = {"NL"})
    public void paymentChangeIdeal() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        String paymentType = "ideal";
        boolean isFail = false;

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");
        commercePage.clickAccountHoldUpdatePaymentCta();
        commercePage.assertUrlContains(sa, "change-payment-info");

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        DisneyPlusUserPage userPage =  new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Payment Change - Klarna", groups = {"DE"})
    public void paymentChangeKlarna() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        String paymentType = "klarna";
        boolean isFail = false;

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");
        commercePage.clickAccountHoldUpdatePaymentCta();
        commercePage.assertUrlContains(sa, "change-payment-info");
        pause(HALF_TIMEOUT); // Needed since klarna-submit is clicked before page loads fully

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        DisneyPlusUserPage userPage =  new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Payment Change - Mercado Pago", groups = {"MX"})
    public void paymentChangeMp() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String paymentType = "mercado";
        boolean isFail = false;
        String paymentProcessingLink = DisneyWebParameters.DISNEY_WEB_MERCADO_CHANGING_LINK.getValue();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");
        commercePage.clickAccountHoldUpdatePaymentCta();

        pause(SHORT_TIMEOUT); // Needed to slow down the test

        commercePage.clickMercadoRadioIconId();

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.handleMercadoPago(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
       }
        DisneyPlusUserPage userPage =  new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Payment Change - Cross Product", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void accountHoldCrossProduct() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        String localeToUse = "MX";
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52111"));

        SoftAssert sa = new SoftAssert();
        String currentProject = DisneyGlobalUtils.getProject();
        String partnerToUse = currentProject.equalsIgnoreCase("DIS") ? "star" : "disney";
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);

        DisneyAccountApi accountApi = new DisneyAccountApi(WEB_PLATFORM, ENVIRONMENT, partnerToUse);
        DisneyAccount account = accountApi.createAccountWithBillingHold(COMBO, localeToUse, language, SUB_VERSION_V2_ORDER);
        DisneyAccountApi defaultAccountApi = new DisneyAccountApi(WEB_PLATFORM, ENVIRONMENT, partnerToUse);
        setOverride(defaultAccountApi.overrideLocations(account, localeToUse));
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.clickSubscriberAgreementContinueCta(localeToUse);

        redemptionPage.finishLoginWithPartnerAccount(disneyAccount.get().getUserPass(), localeToUse);

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");
        commercePage.clickAccountHoldUpdatePaymentCta();

        DisneyProductData productData = new DisneyProductData();
        commercePage.assertUrlContains(sa, productData.searchAndReturnProductData("webName"));

        sa.assertAll();
    }

    @Test(description = "Payment Change - Mercado Pago Icon Not Present", groups = {"MX", TestGroup.STAR_COMMERCE}, enabled = false)
    public void paymentChangeMpNotPresent() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);
        //Users with ACCOUNT HOLD can update their payment using MERCADO PAGO as per WEB-6201

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");
        commercePage.clickAccountHoldUpdatePaymentCta();

        commercePage.isMercadoRadioIconIdPresent();
        sa.assertFalse(commercePage.isMercadoPagoIconPresent(), "Mercado Pago Icon is Present");

        sa.assertAll();
    }
   }


