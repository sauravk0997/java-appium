package com.disney.qa.tests.disney.web.commerce.account;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.EntitlementHelper;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.commerce.*;
import com.disney.qa.disney.web.commerce.modal.DisneyPlusFirstTimeLoginModal;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.disney.util.disney.DisneyGlobalUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.proxy.MitmProxyPool;
import com.zebrunner.agent.core.annotation.TestLabel;
import net.lightbody.bmp.BrowserMobProxy;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

public class DisneyPlusCommerceAccountFlowsTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    ThreadLocal<VerifyEmail> verifyEmail = new ThreadLocal<>();

    private String redemptionWithoutPayGiftCardCode = DisneyWebParameters.DISNEY_QA_WEB_REDEMPTION_RWOP_GIFT_CODE.getValue();
    Boolean isRedemption = false;

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

    @Test(description = "Verify Account Page", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void verifyAccountPageElements() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52864"));

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(getDriver());
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickOnEmailChange(0);
        accountPage.isEnterPasscodeSubmitButtonPresent();
        accountPage.verifyUrlText(sa, "account/enter-passcode");

        accountPage.clickOnPasscodeCancelButtonKey();

        accountPage.clickOnPasswordChange(0);
        accountPage.isEnterPasscodeSubmitButtonPresent();
        accountPage.verifyUrlText(sa, "account/enter-passcode");

        accountPage.clickOnPasscodeCancelButtonKey();

        if (commercePage.isUSCountry(locale)) {
            sa.assertTrue(accountPage.isMonthlyInternalD2CSubscriptionCtaPresent(), "Subscription not present");
            accountPage.clickInternalD2CStandaloneWithAdsSubscriptionCta();
            subscriptionDetailsPage.verifyPage();

            subscriptionDetailsPage.clickBackToAccountLink();

            sa.assertTrue(accountPage.isBillingHistoryLinkVisible(), "Billing history link is not visible");
            sa.assertTrue(accountPage.isUpgradeToBundleCardVisible(), "Upgrade to bundle card is not visible");

            SeleniumUtils utils = new SeleniumUtils(getDriver());
            utils.scrollToBottom();
            waitForSeconds(SHORT_TIMEOUT);
            accountPage.clickOnRestrictProfileCreationToggle();
            commercePage.typeDplusBasePasswordFieldId(account.getUserPass());
            commercePage.clickPasswordContinueLoginBtn();

            waitForSeconds(SHORT_TIMEOUT);

            sa.assertTrue(accountPage.isRestrictProfileCreationToggleEnabled(), "Restrict profile creation toggle is not enabled");
        }
        sa.assertAll();
    }

    @Test(description = "Log Out Of All Devices", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void logOutAllDevices() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickLogOutAllDevicesButton();
        accountPage.getLogOutAllDevicesCurrentPass().type(account.getUserPass());
        accountPage.verifyUrlText(sa, "account/log-out-devices");
        accountPage.clickLogOutAllDevicesContinue();
        sa.assertTrue(accountPage.isDplusBaseLoginBtnPresent(TIMEOUT), "Login btn not present");

        sa.assertAll();
    }

    @Test(description = "Banner For Unverified User", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void bannerForUnverfiedUser() throws IOException, JSONException, URISyntaxException, InterruptedException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String previousDay = disneyApiCommon.formatDateForQuery(DateTime.now().minusDays(1).toString());
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount dAccount = getAccountApi().createAccount("Yearly", locale, language, "V1");
        setOverride(getAccountApi().overrideLocations(dAccount, locale));

        JSONObject identityObject = new JSONObject();
        identityObject.put("createdAt", previousDay);
        identityObject.put("emailVerified", false);

        JSONObject attributeObject = new JSONObject();
        attributeObject.put("dssIdentityCreatedAt", previousDay);
        attributeObject.put("emailVerified", false);
        attributeObject.put("userVerified", false);

        getAccountApi().patchIDPIdentity(dAccount, identityObject);
        pause(HALF_TIMEOUT);
        getAccountApi().patchAccount(dAccount, attributeObject, PatchType.ACCOUNT);
        sa.assertFalse(getAccountApi().getAccountIdentityBody(dAccount).getAttributes().getEmailVerified(), "Identity Email Verified was not false");

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(dAccount.getEmail(), dAccount.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(commercePage.isErrorOnAccountPresent(), "Error not present");

        sa.assertAll();
    }

    @Test(description = "CST (No Pay) Subscription", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void cstSubscription() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, "CST");
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        sa.assertTrue(accountPage.isInternalNopaySubscriptionPresent(), "Subscription not present");

        sa.assertAll();
    }

    @Test(description = "D2C Subscription", groups = {"US", "AD", "AG", "AI", "AL", "AR", "AS", "AT", "AU", "AW", "AX", "BA", "BB", "BE", "BG", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CC", "CH", "CK", "CL", "CO", "CR", "CW", "CX", "CZ", "DE", "DK", "DM", "DO", "EC", "EE", "ES", "FI", "FK", "FO", "FR", "GB", "GD", "GF", "GG", "GI", "GL", "GP", "GR", "GS", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "IE", "IM", "IO", "IS", "IT", "JE", "JM", "JP", "KN", "KR", "KY", "LC", "LI", "LT", "LU", "LV", "MC", "ME", "MF", "MH", "MK", "MP", "MQ", "MS", "MT", "MU", "MX", "NC", "NF", "NI", "NL", "NO", "NU", "NZ", "PA", "PE", "PF", "PL", "PM", "PN", "PR", "PT", "PY", "RE", "RO", "RS", "RU", "SE", "SG", "SH", "SI", "SJ", "SK", "SM", "SR", "SV", "SX", "TC", "TF", "TK", "TR", "TT", "TW", "UM", "UY", "VA", "VC", "VE", "VG", "VI", "WF", "YT",
            TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void d2cSubscription() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52865"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
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
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(accountPage.isMonthlyInternalD2CSubscriptionCtaPresent(), "Subscription not present");

        if (commercePage.isUSCountry(locale)) {
            accountPage.clickInternalD2CStandaloneWithAdsSubscriptionCta();

            DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(getDriver());
            subscriptionDetailsPage.verifyPage();
            sa.assertTrue(subscriptionDetailsPage.isSubscriptionPlanSectionVisible(), "Subscription plan section is not visible");
            sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_PLUS_BASIC_PROMO.getPlanName()),
                    "The purchase plan is incorrect");
            sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_PLUS_BASIC_PROMO.getPrice()),
                    "The plan price is incorrect");
            sa.assertTrue(subscriptionDetailsPage.isCardDetailsSectionVisible(), "Card section details is not visible");
            sa.assertTrue(subscriptionDetailsPage.isSubscriptionDetailsChangePaymentButtonVisible(), "Change payment button is not visible");
            sa.assertTrue(subscriptionDetailsPage.isPaymentMethodVisible(), "Payment method is not visible");
            sa.assertTrue(subscriptionDetailsPage.isPaymentCardVisible(), "Payment card is not visible");
            sa.assertTrue(subscriptionDetailsPage.isNextBillingDateVisible(), "Next billing Date is not visible");
            sa.assertTrue(subscriptionDetailsPage.isLastPaymentVisible(), "Last payment date is not visible");
            sa.assertTrue(subscriptionDetailsPage.isLastPaymentPriceVisible(), "Last payment price is not visible");
            sa.assertTrue(subscriptionDetailsPage.getLastPaymentPrice().contains(PlanCardTypes.SubscriptionPlan.DISNEY_PLUS_BASIC.getPrice().substring(0,0)),
                    "The last purchase price is empty");
            sa.assertTrue(subscriptionDetailsPage.isSubscriptionDetailsCancelSubscriptionButtonVisible(), "Cancel subscription button is not visible");
        }
        sa.assertAll();
    }

    @Test(description = "External Subscription - Verizon/Telmex", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void externalSubscription() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).build();
        request.addSku(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getExternalEntitlement());
        DisneyAccount account = getAccountApi().createAccount(request);

        getAccountApi().overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(accountPage.isExternalSubscriptionPresent(), "Subscription not present");

        sa.assertAll();
    }


    @Test(description = "External Subscription - IAP", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void externalSubscriptionIap() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        DisneyOffer offer = accountApi.fetchOffer(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getIapEntitlement());
        accountApi.entitleAccountV1(account, offer);

        getAccountApi().overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(accountPage.isExternalAppleSubscriptionPresent(), "Subscription not present");

        sa.assertAll();
    }

    @Test(description = "Gift Card Subscription", groups = {"US"}, enabled = false)
    public void giftCardSubscription() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(redemptionWithoutPayGiftCardCode);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, true, language, disneyAccount, proxy));

        commercePage.clickStartStreamingBtnKey();

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(accountPage.isInternalMonthlyNopaySubscriptionPresent(), "Subscription not present");

        sa.assertAll();
    }

    @Test(description = "Stacked Subscription (Double Billed)", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void stackedSubscription() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().entitlements(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getStackedDoubleBilledEntitlements(locale)).country("US").build();
        DisneyAccount account = accountApi.createAccount(request);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(commercePage.isDplusAlertElementPresent(), "Alert element not present on account page");

        sa.assertAll();
    }

    @Test(description = "Grace Billing - Payment Failed", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void graceBillingPaymentFailed() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean checkStarz = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccountWithBillingGrace(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        if (isMobile()) {
            DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
            commercePage.assertMobileUserGetApp(sa, locale, checkStarz);
            accountPage.clickOnAccountDropdown(isMobile());
            sa.assertTrue(commercePage.isErrorOnAccountPresent(), "Error not present");
        } else {
            sa.assertTrue(commercePage.isModalPrimaryBtnPresent(), "Modal not present");
            commercePage.clickModalPrimaryBtn();

            String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);
            redemptionPage.enterPurchaseFlowBillingInfo(
                commercePage.getCreditCardName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale), locale);
            redemptionPage.clickCreditSubmitBtnById();

            DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
            accountPage.clickOnAccountDropdown(isMobile());
            accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();
    
            commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentCreditCard(), "subscription?product=");
        }
        sa.assertAll();
    }

    @Test(description = "Billing History", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void billingHistory() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        ThreadLocal<DisneyAccount> dAccount = new ThreadLocal<>();
        setOverride(userPage.createEmailAndLogin(locale, isBundle, isRedemption, language, dAccount, null));
        accountPage.clickOnAccountDropdown(isMobile());

        List<String> invoiceStatuses = getAccountApi().checkInvoiceStatus(dAccount.get());
        if (invoiceStatuses.contains("SETTLED")) {
            accountPage.clickBillingHistoryCta();
            pause(SHORT_TIMEOUT);
            accountPage.assertUrlContains(sa, PageUrl.BILLING_HISTORY_URL);
            sa.assertTrue(accountPage.isBillingHistoryInvoiceRowPresent(), "Billing history row not present");
        } else {
            sa.assertTrue(invoiceStatuses.contains("SENT_FOR_SETTLE"), "Invoice not sent for settlement");
        }

        sa.assertAll();
    }

    @Test(description = "Billing History - Invoice Details", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void billingHistoryInvoiceDetails() throws JSONException, IOException, URISyntaxException, InterruptedException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        ThreadLocal<DisneyAccount> dAccount = new ThreadLocal<>();
        setOverride(userPage.createEmailAndLogin(locale, isBundle, isRedemption, language, dAccount, null));

        accountPage.clickOnAccountDropdown(isMobile());

        List<String> invoiceStatuses = getAccountApi().checkInvoiceStatus(dAccount.get());
        if (invoiceStatuses.contains("SETTLED")) {
            accountPage.clickBillingHistoryCta();
            pause(SHORT_TIMEOUT);
            accountPage.assertUrlContains(sa, PageUrl.BILLING_HISTORY_URL);
            accountPage.clickBillingHistoryInvoiceLink();
            sa.assertTrue(accountPage.isBillingHistoryInvoiceDetailsTitlePresent(), "Invoice details title not present");
            sa.assertTrue(accountPage.isBillingHistoryInvoiceDetailsSubscriptionPresent(), "Invoice details subscription not present");
        } else {
            sa.assertTrue(invoiceStatuses.contains("SENT_FOR_SETTLE"), "Invoice not sent for settlement");
        }
        sa.assertAll();
    }

    @Test(description = "Billing History - eInvoice Check", groups = {"TW"})
    public void billingHistoryEInvoiceCheck() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        ThreadLocal<DisneyAccount> dAccount = new ThreadLocal<>();
        setOverride(userPage.createEmailAndLogin(locale, isBundle, isRedemption, language, dAccount, proxy));
        commercePage.override3DS2Data(proxy, locale);

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        List<String> invoiceStatuses = getAccountApi().checkInvoiceStatus(dAccount.get());
        if (invoiceStatuses.contains("SETTLED")) {
            accountPage.clickBillingHistoryCta();
            sa.assertTrue(accountPage.isBillingHistoryEInvoiceCopyPresent(), "E Invoice Copy not present");
        } else {
            sa.assertTrue(invoiceStatuses.contains("SENT_FOR_SETTLE"), "Invoice not sent for settlement");
        }

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAP-52344"})
    @Test(description = "Delete account", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void deleteAccount() throws URISyntaxException, JSONException, IOException, InterruptedException, MalformedURLException {
        //To do - WEBQAA-228
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccountForOTP(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        pause(HALF_TIMEOUT);

        accountApi.getSubscriptions(account);
        accountApi.cancelSubscription(account,account.getSubscriptions().get(0).getSubscriptionId());

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        accountPage.clickDeleteAccountCta();
        commercePage.clickContinueDeleteAccountCta();
        commercePage.generateAndEnterOtp(disneyAccount.get().getEmail(), verifyEmail, startTime);
        commercePage.clickEnterPasscodeSubmitBt();
        commercePage.clickDeleteAccountConfirmBtn();
        commercePage.clickDeleteAccountSuccessBtn();

        if(isMobile()) {
            commercePage.assertMobileUserGetApp(sa, locale, false);
        }
        else {
            commercePage.assertUrlContains(sa, "/home");
        }
        sa.assertAll();
    }

    @Test(description = "Archived Account - Error Message on Sign Up", groups = {"US","MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void archivedAccountSignUp() throws URISyntaxException, JSONException, IOException, InterruptedException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        String currentUrl = userPage.getCurrentUrl();

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
            .country(locale).language(language).isArchived(true).build();
            disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
       
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail()); //This is not for a partner account. Just reusing the method.
        userPage.clickSubscriberAgreementContinueCta(locale);
        redemptionPage.finishLoginWithPartnerAccount(disneyAccount.get().getUserPass(), locale);

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        sa.assertTrue(accountPage.isArchivedAccountContainerPresent(), "Archived account error message is not present");
        accountPage.assertUrlContains(sa, PageUrl.ARCHIVED_ACCOUNT);

        accountPage.clickArchivedAccountCta();
        sa.assertTrue(userPage.verifyLoginBtn(), "Login Button not Present on " + currentUrl);

        sa.assertAll();
    }

    @Test(description = "Identity Unification - Standalone Sign Up", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.IDENTITY_UNIFICATION})
    public void identityUnificationStandalone() throws JSONException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        // TODO: WEBQAA-325 - Add Xray Test Ids
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, ""));

        SoftAssert sa = new SoftAssert();

        boolean checkMonthly = true;

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        DisneyPlusFirstTimeLoginModal firstTimeLoginModal = new DisneyPlusFirstTimeLoginModal(getDriver());
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        AccountUtils.createHuluPartnerAccount(disneyAccount);

        loginPage.openURL(loginPage.getHomeUrl().concat(PageUrl.LOGIN));
        Assert.assertTrue(loginPage.verifyPage(), "Login page did not load");

        loginPage.enterEmail(disneyAccount.get().getEmail())
                .clickLoginContinueButton();
        Assert.assertTrue(firstTimeLoginModal.verifyModal(), "First Time Login Modal did not load");

        firstTimeLoginModal.clickModalPrimaryButton();
        loginPage.verifyUrlText(sa, PageUrl.LOGIN_MARKETING_OPT_IN);
        Assert.assertTrue(loginPage.isMarketingOptInVisible(), "Login page marketing opt-in checkbox did not load");

        loginPage.clickSignupContinueButton();
        Assert.assertTrue(userPage.isEmailSeemsFamiliarCopyVisible(), "User page did not load");

        userPage.enterPassword(disneyAccount.get().getUserPass())
                .clickPasswordContinueButton();

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);
       
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));
    
        sa.assertAll();
    }
}
