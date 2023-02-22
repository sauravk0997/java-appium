package com.disney.qa.tests.disney.web.commerce.purchase;


import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusBillingPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusPlansPage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommercePayPalTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private String paymentType = "paypal";
    private String completePurchase = "complete-purchase";
    private boolean isRedemption = false;
    private boolean isPartner = false;

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Verify PayPal Button", groups = {"AD", "AG", "AI", "AL", "AS", "AT", "AU", "AW", "BB", "BE", "BG", "BL", "BM", "BO", "BR", "BS", "BZ", "CA", "CH", "CK", "CL", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "EE", "ES", "FI", "FK", "FO", "FR", "GB", "GD", "GF", "GG", "GI", "GL", "GP", "GR", "GS", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KN", "KY", "LC", "LI", "LT", "LU", "LV", "MC", "ME", "MF", "MH", "MK", "MP", "MQ", "MS", "MT", "MU", "MX", "NC", "NF", "NI", "NL", "NO", "NU", "NZ", "PA", "PE", "PF", "PL", "PM", "PN", "PR", "PT", "PY", "RE", "RO", "RU", "SE", "SG", "SH", "SI", "SJ", "SK", "SM", "SR", "SV", "TC", "TT", "TW", "UM", "US", "UY", "VA", "VC", "VE", "VG", "VI", "WF", "YT", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void  paypalVerify() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52097"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);

        commercePage.clickPayPalRadioIconById(locale);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        sa.assertTrue(commercePage.isPayPalSubmitBtnByIdPresent(), "PayPal submit button not present");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription - PayPal", groups = {"AD", "AG", "AI", "AL", "AS", "AT", "AU", "AW", "BB", "BE", "BG", "BL", "BM", "BO", "BR", "BS", "BZ", "CA", "CH", "CK", "CL", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "EE", "ES", "FI", "FK", "FO", "FR", "GB", "GD", "GF", "GG", "GI", "GL", "GP", "GR", "GS", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KN", "KY", "LC", "LI", "LT", "LU", "LV", "MC", "ME", "MF", "MH", "MK", "MP", "MQ", "MS", "MT", "MU", "MX", "NC", "NF", "NI", "NL", "NO", "NU", "NZ", "PA", "PE", "PF", "PL", "PM", "PN", "PR", "PT", "PY", "RE", "RO", "RU", "SE", "SG", "SH", "SI", "SJ", "SK", "SM", "SR", "SV", "TC", "TT", "TW", "UM", "US", "UY", "VA", "VC", "VE", "VG", "VI", "WF", "YT", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void  monthly() throws URISyntaxException, JSONException, MalformedURLException, IOException {
        skipPayPalTestByProduct();

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52859"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);
        commercePage.override3DS2Data(proxy, locale);

        commercePage.clickPayPalRadioIconById(locale);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        if (locale.equals("CA") && isMobile()) {
            pause(TIMEOUT);// Need some time for payment processing only for mobile test in Canada . May be can re-check and remove later.
        }

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Annual Subscription - PayPal", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void annual() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52862"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = false;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        if (locale.equalsIgnoreCase(WebConstant.US)) {
            commercePage.clickSignUpNowBtn();
            DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
            plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);
        } else {
            commercePage.clickCompleteAndRestartPurchaseSignUpNowBtn(locale);
            commercePage.clickSubscriberAgreementContinueCta(locale);
        }
        commercePage.clickYearlyRadioBtnById();
        commercePage.clickPayPalRadioIconById(locale);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "D+ Superbundle (SASH) / S+ Combo+ - PayPal", priority = 3, groups = {"US", "MX", "BO", "BR", "CL", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void superBundleSash() throws URISyntaxException, JSONException, MalformedURLException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-520908"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickPayPalRadioIconById(locale);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Superbundle (NOAH) - PayPal", priority = 3, groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void superbundleNoah() throws URISyntaxException, JSONException, MalformedURLException {
        skipPayPalTestByProduct();

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52099"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickPayPalRadioIconById(locale);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleUpgradeCardPresent(), false, "billing?type=bundle");

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        commercePage.clickBundleUpgradeCardLink();

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleOfferCardDowngradePresent(), false, "billing?bundleType=noah");

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription - PayPal - Blocked", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void paymentBlocked() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52100"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);

        commercePage.clickPayPalRadioIconById(locale);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        boolean shouldSkip = commercePage.handleFullPayPal(commercePage.getBlockedPaypalCreditAccount(locale), proxy);
        skipPayPalTest(shouldSkip);

        sa.assertTrue(commercePage.isDplusAlertElementPresent(),
                "Alert Element not Present when transaction is blocked on: " + commercePage.getCurrentUrl());

        sa.assertAll();
    }

    @Test(description = "Deeplink - PayPal", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void deepLink() throws JSONException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-522101"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(commercePage.getDeeplinkUrl(locale)));

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        if (locale.equalsIgnoreCase(WebConstant.US) && !DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            sa.assertEquals(commercePage.getCurrentUrl(), commercePage.getHomeUrl().concat(commercePage.getDeeplinkUrl(locale)));
        }
        commercePage.clickPayPalRadioIconById(locale);
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "D+/S+ Megabundle - PayPal", groups = {"MX", "BR", "CL", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void megaBundlePaypal() throws URISyntaxException, JSONException, MalformedURLException {
        //TODO: Change skips once launched and make this test enabled as Paypal is having issues working in QA - QAA-9840
        //skipTest(QA);
        //skipPayPalTestByProduct();
        skipTestByEnv(PROD);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickPayPalRadioIconById(locale);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=megabundle");

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }
}
