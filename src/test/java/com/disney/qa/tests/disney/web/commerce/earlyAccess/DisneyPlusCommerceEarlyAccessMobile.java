package com.disney.qa.tests.disney.web.commerce.earlyAccess;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.commerce.DisneyPlusBillingPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
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

public class DisneyPlusCommerceEarlyAccessMobile extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private String getAppUrl = "get-app";

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Unentitled - Never had EA", groups = { "US", TestGroup.DISNEY_COMMERCE })
    public void eaMobileUnentitledNeverHadEA()
            throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52134"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        skipEATest(commercePage.handleEAUrl());

        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        loginPage.signUpWithCreds(disneyAccount);
        commercePage.override3DS2Data(proxy, locale);
        commercePage.assertEAElements(sa, commercePage.isEAStandalonePricePresent(),
                commercePage.isEAOfferPricePresent());

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.isGetAppEAPagePresent();
        commercePage.assertUrlContains(sa, getAppUrl);

        sa.assertAll();
    }

    @Test(description = "Unentitled - Had EA", groups = { "US", TestGroup.DISNEY_COMMERCE })
    public void eaMobileUnentitledHadEA() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52135"));

        SoftAssert sa = new SoftAssert();
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
        // commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl,
        // isBundle);
        commercePage.override3DS2Data(proxy, locale);
        commercePage.clickChangePaymentCtaIfPresent();

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.isGetAppEAPagePresent();
        commercePage.assertUrlContains(sa, getAppUrl);

        sa.assertAll();
    }

    @Test(description = "Entitled - No EA", groups = { "US", TestGroup.DISNEY_COMMERCE })
    public void eaMobileEntitledNoEA() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52136"));

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        DisneyAccountApi api = getAccountApi();
        DisneyAccount account = api.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        api.overrideLocations(account, locale);
        disneyAccount.set(account);

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        skipEATest(commercePage.handleEAUrl());

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.override3DS2Data(proxy, locale);
        commercePage.clickChangePaymentCtaIfPresent();

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale),
                billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                        false),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.isGetAppEAPagePresent();
        commercePage.assertUrlContains(sa, getAppUrl);

        sa.assertAll();
    }

    @Test(description = "Entitled - No EA - Multiple Profiles", groups = { "US", TestGroup.DISNEY_COMMERCE })
    public void eaMobileEntitledNoEAMultiProfile()
            throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52137"));

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi api = getAccountApi();
        DisneyAccount account = api.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        api.addProfile(account, "Test", language, null, false);
        api.overrideLocations(account, locale);
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

        commercePage.isGetAppEAPagePresent();
        commercePage.assertUrlContains(sa, getAppUrl);

        sa.assertAll();
    }

    @Test(description = "Entitled - Has EA", groups = { "US", TestGroup.DISNEY_COMMERCE })
    public void eaMobileEntitledHasEA() throws MalformedURLException, JSONException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52138"));

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

        commercePage.isGetAppEAPagePresent();
        commercePage.assertUrlContains(sa, getAppUrl);

        sa.assertAll();
    }

    @Test(description = "Entitled - Billing Hold - No EA", groups = { "US", TestGroup.DISNEY_COMMERCE })
    public void eaMobileEntitledBillingHoldNoEA()
            throws MalformedURLException, JSONException, IOException, URISyntaxException {
        handleEASkips(false);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52139"));

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

        commercePage.isGetAppEAPagePresent();
        commercePage.assertUrlContains(sa, getAppUrl);

        sa.assertAll();
    }

}
