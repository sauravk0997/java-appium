package com.disney.qa.tests.disney.web.commerce.earlyAccess;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.commerce.DisneyPlusBillingPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusDefaultProfilePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.proxy.MitmProxy;
import com.proxy.MitmProxyConfiguration;
import com.proxy.MitmProxyPool;
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

public class DisneyPlusCommerceEarlyAccess extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
        MitmProxyPool.tearDownProxy();
    }

    @Test(description = "Unentitled - Never had EA", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaUnentitledNeverHadEA() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52124"));

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";
        boolean isBundle = false;
        boolean isPartner = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        skipEATest(commercePage.handleEAUrl());

        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        loginPage.signUpWithCreds(disneyAccount);
        commercePage.override3DS2Data(proxy, locale);

        commercePage.assertEAElements(sa, commercePage.isEAStandalonePricePresent(), commercePage.isEAOfferPricePresent());

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Unentitled - Never had EA - Abandoner", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaUnentitledNeverHadEAAbandoner() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52125"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";
        String expectedUrl = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        skipEATest(commercePage.handleEAUrl());

        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        loginPage.signUpWithCreds(disneyAccount);
        commercePage.override3DS2Data(proxy, locale);
        commercePage.assertEAElements(sa, commercePage.isEAStandalonePricePresent(), commercePage.isEAOfferPricePresent());

        commercePage.goToCompleteOrRestartPurchase(sa, expectedUrl);

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);
        //TODO : Uncomment/remove the assertion - WEBQAA-364
        //commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), true);
        commercePage.override3DS2Data(proxy, locale);

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
        countryData.searchAndReturnCountryData(locale, CODE, EXP),
        countryData.searchAndReturnCountryData(locale, CODE, CVV),
        countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        sa.assertAll();
    }

    @Test(description = "Unentitled - Had EA", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaUnentitledHadEA() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52126"));

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";
        // QAA-8598
        // String expectedUrl = "restart-subscription";
        // boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyOffer eaSku = commercePage.getEaSkus(locale);
        DisneyAccountApi api = getAccountApi();

        DisneyAccount account = api.createExpiredAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        api.entitleAccountV1(account, api.lookupOfferToUse(locale, YEARLY));
        api.entitleAccountV1(account, eaSku);
        setOverride(api.overrideLocations(account, locale));
        disneyAccount.set(account);

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        skipEATest(commercePage.handleEAUrl());

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());
        // QAA-8598
        // commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);
        commercePage.override3DS2Data(proxy, locale);
        commercePage.clickChangePaymentCtaIfPresent();

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), "home");

        sa.assertAll();
    }

    @Test(description = "Entitled - No EA", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaEntitledNoEA() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52127"));

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        skipEATest(commercePage.handleEAUrl());

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());
        commercePage.override3DS2Data(proxy, locale);
        commercePage.clickChangePaymentCtaIfPresent();

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        sa.assertTrue(commercePage.isEAConfirmationPresent(), "EA not purchased");

        sa.assertAll();
    }

    @Test(description = "Entitled - No EA - Multiple Profiles", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaEntitledNoEAMultiProfile() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52128"));

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.addProfile(account, "Test", language, null, false);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        skipEATest(commercePage.handleEAUrl());

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());
        DisneyPlusDefaultProfilePage defaultProfilePage = new DisneyPlusDefaultProfilePage(getDriver());
        defaultProfilePage.clickOnPrimaryProfileOnEditPage();
        commercePage.override3DS2Data(proxy, locale);
        commercePage.clickChangePaymentCtaIfPresent();

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        sa.assertTrue(commercePage.isEAConfirmationPresent(), "EA not purchased");

        sa.assertAll();
    }

    @Test(description = "Entitled - No EA - Stored Payment", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaEntitledNoEAStoredPayment() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52129"));

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";
        boolean isBundle = false;
        boolean isRedemption = false;
        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
            commercePage.getCreditCardName(locale),
            creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            cvv,
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.clickStartStreamingBtnKey();

        skipEATest(commercePage.handleEAUrl());

        redemptionPage.handleStoredPaymemt(cvv);

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        sa.assertTrue(commercePage.isEAConfirmationPresent(), "EA not purchased");

        sa.assertAll();
    }

    @Test(description = "Entitled - Has EA", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaEntitledHasEA() throws MalformedURLException, JSONException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52130"));

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyOffer eaSku = commercePage.getEaSkus(locale);

        DisneyAccountApi api = getAccountApi();
        DisneyAccount account = api.createAccount(locale, language);
        api.entitleAccountV2(account, api.lookupOfferToUse(locale, YEARLY));
        api.entitleAccountV1(account, eaSku);
        setOverride(api.overrideLocations(account, locale));
        disneyAccount.set(account);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        skipEATest(commercePage.handleEAUrl());

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        sa.assertTrue(commercePage.isEAConfirmationPresent(), "EA not purchased");

        sa.assertAll();
    }

    @Test(description = "Entitled - Billing Hold - No EA", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaEntitledBillingHoldNoEA() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52131"));

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi api = getAccountApi();
        DisneyAccount account = api.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        api.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        skipEATest(commercePage.handleEAUrl());

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());

        commercePage.clickAccountHoldUpdatePaymentCta();
        commercePage.override3DS2Data(proxy, locale);

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), "home");

        sa.assertAll();
    }

    @Test(description = "Entitled - Has EA - Invoice Check (One Invoice)", groups = {"US"}, enabled = false)
    public void eaEntitledHasEAInvoiceCheck() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52132"));

        SoftAssert sa = new SoftAssert();
        boolean checkStarz = true;
        boolean isBundle = false;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        skipEATest(commercePage.handleEAUrl());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, true, language, disneyAccount, proxy));

        commercePage.assertEAElements(sa, commercePage.isEAStandalonePricePresent(), commercePage.isEAOfferPricePresent());
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

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertSuccessOverlay(sa, commercePage.isPurchaseSuccessConfirmBtnPresent(), commercePage.isEaPurchaseSuccessConfirmBtn(), checkStarz, locale, false);

        commercePage.clickStartStreamingBtnKey();

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickBillingHistoryCta();

        accountPage.clickBillingHistoryInvoiceLink();
        sa.assertTrue(accountPage.isBillingHistoryInvoiceDetailsTitlePresent(), "Invoice details title not present");
        sa.assertTrue(accountPage.isBillingHistoryInvoiceDetailsSubscriptionTwoPresent(), "Invoice details subscription 2 not present");

        sa.assertAll();
    }

    @Test(description = "Entitled - Has EA - Invoice Check (Two Invoices)", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void eaEntitledHasEAInvoiceTwoCheck() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(BETA);
        skipTestByEnv(PROD);
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52133"));

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";
        String completePurchase = "complete-purchase";
        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        MitmProxyConfiguration configuration = MitmProxyConfiguration.builder()
                .listenPort(getProxyPort())
                .headers(new DisneyPlusCommercePage(getDriver()).getHeaders(locale)).build();
        MitmProxy.startProxy(configuration);
        commercePage.navigateToMainPage();

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, false);

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.clickStartStreamingBtnKey();

        skipEATest(commercePage.handleEAUrl());

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.handleStoredPaymemt(cvv);

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        commercePage.isEAConfirmationPresent();

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickBillingHistoryCta();
        accountPage.assertUrlContains(sa, PageUrl.BILLING_HISTORY_URL);

        sa.assertTrue(accountPage.isBillingHistoryInvoiceRowTwoPresent(), "Invoice row 2 not present");

        sa.assertAll();
    }
}

