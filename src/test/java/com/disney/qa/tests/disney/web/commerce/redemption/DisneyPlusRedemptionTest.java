package com.disney.qa.tests.disney.web.commerce.redemption;


import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.EntitlementHelper;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusCreateProfilePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusConsentCollectionPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.disney.util.disney.DisneyGlobalUtils;
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

public class DisneyPlusRedemptionTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private boolean isRedemption = true;
    private boolean isBundle = false;
    private boolean isPartner = false;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Without Pay", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void redemptionWithoutPay() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithoutPay());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        if (locale.equalsIgnoreCase(US)) {
            DisneyPlusConsentCollectionPage consentCollectionPage = new DisneyPlusConsentCollectionPage(getDriver());
            consentCollectionPage.verifyPage();
            consentCollectionPage.enterConsentDob(WebConstant.ADULT_DOB);
        }

        DisneyPlusCreateProfilePage createProfilePage = new DisneyPlusCreateProfilePage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, createProfilePage.isProfileBtnVisible());

        sa.assertAll();

    }

    @Test(description = "With Pay - Credit Card", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
        public void redemptionWithPayAllCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        loginPage.signUpWithCreds(disneyAccount);
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

        commercePage.assertUrlContains(sa, "redemptionCode=" + new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);
        
        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "With Pay - PayPal", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void redemptionWithPayAllPayPal() throws MalformedURLException, JSONException, URISyntaxException, IOException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String paymentType = "paypal";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
        commercePage.override3DS2Data(proxy, locale);

        commercePage.clickPayPalRadioIconById(locale);

        commercePage.assertUrlContains(sa, "redemptionCode=" + new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        commercePage.removeTestHeader(proxy);
        commercePage.clickPayPalSubmitBtnById();

        boolean shouldSkip = commercePage.paypalTransactionHandler(locale);
        skipPayPalTest(shouldSkip);

        commercePage.handleSubmitOnPaypal();
        commercePage.switchWindow();

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    /**
     * Disabled until new redemption code is generated
     */
    @Test(description = "With Pay - Ideal", groups = "NL", enabled = false)
    public void redemptionWithPayIdeal() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isFail = false;
        String paymentType = "ideal";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());

        commercePage.assertUrlContains(sa, "redemptionCode=" + new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    /**
     * Disabled until new redemption code is generated
     */
    @Test(description = "With Pay - Klarna", groups = "DE", enabled = false)
    public void redemptionWithPayKlarna() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isFail = false;
        String paymentType = "klarna";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());

        commercePage.assertUrlContains(sa, "redemptionCode=" + new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "With Pay - Invalid Code", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void redemptionWithPayInvalidCode() {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode("invalid");

        sa.assertTrue(redemptionPage.isInputErrorPresent(),
                "Input Error not present after invalid redemption code");

        sa.assertAll();
    }

    @Test(description = "With Pay - Cannot Apply Promotion", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void redemptionWithPayCannotApply() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());

        sa.assertTrue(redemptionPage.isBtnCannotApplyPromotionPresent(),
                "Cannot Apply Promotion Ok Button not present");

       commercePage.assertUrlContains(sa, "cannot-apply-promotion");

        sa.assertAll();
    }

    @Test(description = "With Pay - Continue Redemption", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void redemptionWithPayContinueRedemption() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.isRegularSignUpBtnPresent();

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.openURL(redemptionPage.getHomeUrl().concat(String.format("/redeem?redemptionCode=%s", new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale))));

        sa.assertTrue(redemptionPage.isSpeedBumpContinuePresent(),
                String.format("Speed-bump-continue data-testid not present on %s", redemptionPage.getCurrentUrl()));

        commercePage.assertUrlContains(sa, "continue-redemption?");

        redemptionPage.clickSpeedBumpContinue();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        sa.assertTrue(userPage.isCreditRadioLabelPresent());

        sa.assertAll();
    }

    @Test(description = "With Pay - Lapsed User", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void redemptionWithpayLapsedUser() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createExpiredAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/redeem"));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterRedemptionCode(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());
        if (DisneyPlusBasePage.IS_DISNEY) {
            commercePage.clickSubscriberAgreementContinueCta(locale);
        }

        commercePage.assertUrlContains(sa, "redemptionCode=" + new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));
        commercePage.clickChangePaymentCta();

        redemptionPage.enterPurchaseFlowBillingInfo(
            commercePage.getCreditCardName(locale),
            creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        commercePage.assertUrlContains(sa, "redemptionCode=" + new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getMultiUseRedemptionCodeWithPay(locale));

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }
}
