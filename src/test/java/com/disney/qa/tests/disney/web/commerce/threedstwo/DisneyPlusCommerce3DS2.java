package com.disney.qa.tests.disney.web.commerce.threedstwo;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
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

public class DisneyPlusCommerce3DS2 extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private String paymentType = "credit";
    private String completePurchase = "complete-purchase";
    private boolean isPartner = false;
    public static final String THREEDSTWO_SUCCESSFUL_CHALLENGE_NAME = "3DS_V2_CHALLENGE_IDENTIFIED";
    public static final String THREEDSTWO_UNSUCCESSFUL_CHALLENGE_NAME = "3DS_V2_CHALLENGE_UNKNOWN_IDENTITY";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Successful Challenge and Purchase", groups = {"FR", "AT", "IT"})
    public void threeDsTwoSuccessfulChallenge() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);
        
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
            THREEDSTWO_SUCCESSFUL_CHALLENGE_NAME,
            creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        if (!commercePage.isThreeDsTwoIframePresent()) {
            throw new SkipException("Skipping test due to 3DS2 not showing up for this country");
        } else {
            commercePage.clickChallengeFormSubmitBtn(locale);
            commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);
        }
        
        sa.assertAll();
    }

    @Test(description = "Challenge Failed and Failed Purchase", groups = {"FR", "AT", "IT"})
    public void threeDsTwoUnsuccessfulChallenge() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
            THREEDSTWO_UNSUCCESSFUL_CHALLENGE_NAME,
            creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        
        if (!commercePage.isThreeDsTwoIframePresent()) {
            throw new SkipException("Skipping test due to 3DS2 not showing up for this country");
        } else {
            commercePage.clickChallengeFormSubmitBtn(locale);
            sa.assertTrue(commercePage.isErrorProcessingPresent(),
                "Error Processing message not present");
        }

        sa.assertAll();
    }

}