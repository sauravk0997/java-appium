package com.disney.qa.tests.disney.web.commerce.purchase;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceIdealTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();
    private String localeToUse = "NL";

    private String paymentType = "ideal";
    private String completePurchase = "complete-purchase";
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

    @Test(description = "Monthly Subscription - Ideal", priority = 1, groups = {"NL","US", TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void monthly() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52860"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;
        boolean isFail = false;

        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(localeToUse, isBundle, false, language, disneyAccount, proxy));

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        commercePage.assertPurchaseWithSuccessOverlay(sa, localeToUse, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Annual Subscription - Ideal", groups = {"NL","US", TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void annual() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52944"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = false;
        boolean isBundle = false;
        boolean isFail = false;
        
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(localeToUse, isBundle, false, language, disneyAccount, proxy));

        commercePage.clickYearlyRadioBtnById();
        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        commercePage.assertPurchaseWithSuccessOverlay(sa, localeToUse, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription - Ideal - Failure", priority = 1, groups = {"NL"})
    public void monthlyFailure() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;
        boolean isFail = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        com.disney.qa.api.pojos.DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());

        sa.assertAll();
    }

}
