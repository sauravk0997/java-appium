package com.disney.qa.tests.disney.web.commerce.freetrialfraud;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
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

public class DisneyPlusCommerceFreeTrialFraudCreditCardTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private String paymentType = "credit";
    private String completePurchase = "complete-purchase";

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Credit Card", groups = {"BR", "AR", "CL", "CO", "MX", "PE", "EC", "UY", "PA", "CR", TestGroup.STAR_COMMERCE}, enabled = false)
    public void freeTrialFraudCreditCard() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, true);

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
        commercePage.clickStoredPaymentSubmitBtn();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();
        sa.assertTrue(accountPage.isLastPaymentNoFreeTrialPresent(), "Free trial present");

        sa.assertAll();
    }

    @Test(description = "Credit Card - Change Payment To FTF", groups = {"BR", "AR", "CL", "CO", "MX", "PE", "EC", "UY", "PA", "CR", TestGroup.STAR_COMMERCE}, enabled = false)
    public void freeTrialFraudCreditCardChangedToFTF() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, true);

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

        commercePage.clickChangePaymentCta();

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
        commercePage.clickStoredPaymentSubmitBtn();
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();
        sa.assertTrue(accountPage.isLastPaymentNoFreeTrialPresent(), "Free trial present");

        sa.assertAll();
    }

    @Test(description = "Credit Card - Change Payment To Whitelisted", groups = {"BR", "AR", "CL", "CO", "MX", "PE", "EC", "UY", "PA", "CR"}, enabled = false)
    public void freeTrialFraudCreditCardChangedToWhitelisted() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, true);

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

        commercePage.clickChangePaymentCta();

        creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

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
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();
        sa.assertTrue(accountPage.isLastPaymentNoFreeTrialPresent(), "Free trial present");

        sa.assertAll();
    }

}
