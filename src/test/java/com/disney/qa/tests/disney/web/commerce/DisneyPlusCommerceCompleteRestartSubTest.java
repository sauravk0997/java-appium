package com.disney.qa.tests.disney.web.commerce;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;

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
import java.util.ArrayList;
import java.util.Arrays;

public class DisneyPlusCommerceCompleteRestartSubTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private boolean isRedemption = false;
    private boolean isPartner = false;
    SoftAssert sa;

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
        sa = new SoftAssert();
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Base - Complete Subscription - Credit Card", groups = {"US", "MX", "NL", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void commerceCompleteBaseCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, new ArrayList<>(Arrays.asList("US", "NL")) , DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53406"));

        boolean isBundle = false;
        String paymentType = "credit";
        String type = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
            .country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);
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

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "SuperBundle/Combo+ - Complete Subscription - Credit Card", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void commerceCompleteSuperCreditCard() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        boolean isBundle = true;
        String paymentType = "credit";
        String type = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
        commercePage.goToCompleteOrRestartPurchase(sa, type);
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);
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

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Base - Complete Subscription - Paypal", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void commerceCompleteBasePayPal() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        boolean isBundle = false;
        String paymentType = "paypal";
        String type = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);

        commercePage.clickPayPalRadioIconById(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Superbundle/Combo + - Complete Subscription - Paypal", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void commerceCompleteBundlePayPal() throws URISyntaxException, JSONException, MalformedURLException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        boolean isBundle = true;
        String paymentType = "paypal";
        String type = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
        commercePage.goToCompleteOrRestartPurchase(sa, type);
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);

        commercePage.clickPayPalRadioIconById(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Base - Complete Subscription - Ideal", groups = "NL")
    public void commerceCompleteBaseIdeal() throws MalformedURLException, JSONException, URISyntaxException{
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        boolean isFail = false;
        boolean isBundle = false;
        String paymentType = "ideal";
        String expectedUrl = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.goToCompleteOrRestartPurchase(sa, expectedUrl);

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Base - Complete Subscription - Klarna", groups = "DE")
    public void commerceCompleteBaseKlarna() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        boolean isFail = false;
        boolean isBundle = false;
        String paymentType = "klarna";
        String expectedUrl = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.goToCompleteOrRestartPurchase(sa, expectedUrl);

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Base - Restart Subscription - Credit Card", groups = {"US", "MX", "NL", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void commerceRestartBaseCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, new ArrayList<>(Arrays.asList("US", "NL")) , DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53407"));

        boolean isBundle = false;
        String paymentType = "credit";
        String expectedUrl = "restart-subscription";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createExpiredAccount(YEARLY,locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());

        commercePage.clickChangePaymentCtaIfPresent();

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

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Base - Restart Subscription - PayPal", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void commerceRestartBasePayPal() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        boolean isBundle = false;
        String paymentType = "paypal";
        String expectedUrl = "restart-subscription";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createExpiredAccount(YEARLY,locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.clickChangePaymentCtaIfPresent();

        commercePage.clickPayPalRadioIconById(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Base - Restart Subscription - Ideal", groups = "NL")
    public void commerceRestartBaseIdeal() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        boolean isFail = false;
        boolean isBundle = false;
        String paymentType = "ideal";
        String expectedUrl = "restart-subscription";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY,locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.goToCompleteOrRestartPurchase(sa, expectedUrl);

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Base - Restart Subscription - Klarna", groups = "DE")
    public void commerceRestartBaseKlarna() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        boolean isFail = false;
        boolean isBundle = false;
        String paymentType = "klarna";
        String expectedUrl = "restart-subscription";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY,locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.goToCompleteOrRestartPurchase(sa, expectedUrl);

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Megabundle - Complete Subscription - Credit Card", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void commerceCompleteMegaBundleCreditCard() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        boolean isBundle = true;
        String paymentType = "credit";
        String type = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
        commercePage.goToCompleteOrRestartPurchase(sa, type);
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);
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

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }
}
