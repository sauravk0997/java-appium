package com.disney.qa.tests.disney.web.commerce.changepayment;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceChangePaymentTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    private boolean isRedemption = false;
    private String paymentProcessingLink = DisneyWebParameters.DISNEY_WEB_MERCADO_CHANGING_LINK.getValue();

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Credit Card", groups = {"US", "AD", "AG", "AI", "AL", "AR", "AS", "AT", "AU", "AW", "AX", "BA", "BB", "BE", "BG", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CC", "CH", "CK", "CL", "CO", "CR", "CW", "CX", "CZ", "DE", "DK", "DM", "DO", "EC", "EE", "ES", "FI", "FK", "FO", "FR", "GB", "GD", "GF", "GG", "GI", "GL", "GP", "GR", "GS", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "IE", "IM", "IO", "IS", "IT", "JE", "JM", "JP", "KN", "KR", "KY", "LC", "LI", "LT", "LU", "LV", "MC", "ME", "MF", "MH", "MK", "MP", "MQ", "MS", "MT", "MU", "MX", "NC", "NF", "NI", "NL", "NO", "NU", "NZ", "PA", "PE", "PF", "PL", "PM", "PN", "PR", "PT", "PY", "RE", "RO", "RS", "RU", "SE", "SG", "SH", "SI", "SJ", "SK", "SK", "SM", "SR", "SV", "SX", "TC", "TF", "TK", "TR", "TT", "TW", "UM", "UY", "VA", "VC", "VE", "VG", "VI", "WF", "YT", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.STAR_SMOKE, TestGroup.STAR_COMMERCE_SMOKE})
    public void changePaymentCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52116", "XWEBQAP-52875"));

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();
        commercePage.override3DS2Data(proxy.get(), locale);

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

        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentCreditCard(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "PayPal", groups = {"AD", "AG", "AI", "AL", "AS", "AT", "AU", "AW", "BB", "BE", "BG", "BL", "BM", "BO", "BR", "BS", "BZ", "CA", "CH", "CK", "CL", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "EE", "ES", "FI", "FK", "FO", "FR", "GB", "GD", "GF", "GG", "GI", "GL", "GP", "GR", "GS", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KN", "KY", "LC", "LI", "LT", "LU", "LV", "MC", "ME", "MF", "MH", "MK", "MP", "MQ", "MS", "MT", "MU", "MX", "NC", "NF", "NI", "NL", "NO", "NU", "NZ", "PA", "PE", "PF", "PL", "PM", "PN", "PR", "PT", "PY", "RE", "RO", "RU", "SE", "SG", "SH", "SI", "SJ", "SK", "SM", "SR", "SV", "TC", "TT", "TW", "UM", "US", "UY", "VA", "VC", "VE", "VG", "VI", "WF", "YT", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void changePaymentPayPal() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipPayPalTestByProduct();

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52117"));

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();

        pause(SHORT_TIMEOUT); // Needed to load the page fully
        commercePage.clickPayPalRadioIconById(locale);
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy.get());
        skipPayPalTest(shouldSkip);

        pause(LONG_TIMEOUT); // Needed since it takes a bit of time
        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentPayPal(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "Ideal", groups = {"NL"})
    public void changePaymentIdeal() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "ideal";
        boolean isFail = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

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

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentIdeal(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "Klarna", groups = {"DE"})
    public void changePaymentKlarna() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        String paymentType = "klarna";
        boolean isBundle = false;
        boolean isFail = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

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
        pause(HALF_TIMEOUT); // Need sometime for the challenge frame to appear
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();

        pause(HALF_TIMEOUT); // Needed since klarna-submit is clicked before page loads fully

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentKlarna(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "Mercado Pago", groups = {"BR", "AR", "CL", "CO", "MX", TestGroup.STAR_COMMERCE})
    public void changePaymentMercado() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isFail = false;

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

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();

        commercePage.clickMercadoRadioIconId();

        redemptionPage.handleMercadoPago(locale);

        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            redemptionPage.reloadMercadoPaymentPage();
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }

        accountPage.clickOnAccountDropdownIfNotAccountPage(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, commercePage.isAccountDropdownIdPresent(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "Bundle/Combo+ - Credit Card", groups = {"US", "MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void changePaymentComboCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52118"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

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
        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();
        commercePage.override3DS2Data(proxy, locale);

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

        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentCreditCard(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "Bundle/Combo+ - PayPal", groups = {"US", "MX", "BO", "BR", "CL", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void changePaymentComboPayPal() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52119"));

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(COMBO, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();
        pause(SHORT_TIMEOUT); // Need a second to load PayPal
        commercePage.clickPayPalRadioIconById(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        pause(TIMEOUT); // Needed since it takes a bit of time
        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentPayPal(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "Bundle/Combo+ - Mercado Pago", groups = {"BR", "AR", "CL", "CO", "MX", TestGroup.STAR_COMMERCE})
    public void changePaymentComboMercado() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isFail = false;
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(COMBO, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();

        commercePage.clickMercadoRadioIconId();

        redemptionPage.handleMercadoPago(locale);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            redemptionPage.reloadMercadoPaymentPage();
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }

        if (!isMobile()) {
            commercePage.clickStartStreamingBtnKey();
        }

        accountPage.clickOnAccountDropdownIfNotAccountPage(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, commercePage.isAccountDropdownIdPresent(), "subscription?product=");

        sa.assertAll();
    }

    @Test(description = "Megabundle - Credit Card", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void changePaymentMegaBundleCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "credit";
        
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
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
        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=lionsgateplusbundle");

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, true, isMobile());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();

        if (accountPage.isClickSubscriptionDetailsChangePaymentBtnNotPresent()) {
            throw new SkipException("Skipping test due to Change button not appearing.");
        }

        accountPage.clickSubscriptionDetailsChangePaymentBtn();
        commercePage.override3DS2Data(proxy, locale);

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

        accountPage.clickInternalD2CSubscriptionCtaContainsIfPresent();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isLastPaymentCreditCard(), "subscription?product=");

        sa.assertAll();
    }
}
