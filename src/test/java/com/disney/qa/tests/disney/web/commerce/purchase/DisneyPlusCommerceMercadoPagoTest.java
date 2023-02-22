package com.disney.qa.tests.disney.web.commerce.purchase;


import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusBillingPage;
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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class DisneyPlusCommerceMercadoPagoTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private boolean isRedemption = false;
    private boolean isFail = false;
    private String paymentProcessingLink = DisneyWebParameters.DISNEY_WEB_MERCADO_PROCESSING_LINK.getValue();
    private List <String>  paypalCountry = Arrays.asList ("MX" , "BR", "CL");

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Monthly Subscription - Mercado Pago", groups = {"BR", "AR", "CL", "CO", "MX","US", TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE, TestGroup.STAR_SMOKE, TestGroup.STAR_COMMERCE_SMOKE})
    public void mercadoMonthly() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);
        String localeToUse = locale.equals(US) ? MX:locale;
        skipTestByProjectLocale(localeToUse, EMPTY_SKIP_COUNTRIES_LIST, STAR_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, STA, MX, "XWEBQAP-52860", "XWEBQAP-52469", "XWEBQAP-52468"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean checkMonthly = true;
        String paymentType = "mercado";
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(localeToUse, isBundle, isRedemption, language, disneyAccount, proxy));

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());

        sa.assertTrue(billingPage.isCreditCardIconVisible(), "Credit card icon is not visible");
        if (paypalCountry.contains(localeToUse)) { 
            sa.assertTrue(billingPage.isPaypalIconVisible(), "Paypal icon is not visible");
        }
        sa.assertTrue(billingPage.isMercadoPagoIconVisible(), "Mercado Pago icon is not visible");

        commercePage.clickMercadoRadioIconId();
        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.handleMercadoPago(localeToUse);
        commercePage.clickBtnReviewSubscriptionCta(localeToUse, paymentType);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, localeToUse, false, false, isMobile());

        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent());

        sa.assertAll();
    }

    @Test(description = "Annual Subscription - Mercado Pago", groups = {"BR", "AR", "CL", "CO", "MX","US", TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE, TestGroup.STAR_SMOKE, TestGroup.STAR_COMMERCE_SMOKE})
    public void mercadoAnnual() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA); 
        String localeToUse = locale.equals(US) ? MX : locale;
        skipTestByProjectLocale(localeToUse, EMPTY_SKIP_COUNTRIES_LIST, STAR_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, MX, "XWEBQAP-52863", "XWEBQAP-52448"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean checkMonthly = false;
        String paymentType = "mercado";
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(localeToUse, isBundle, isRedemption, language, disneyAccount, proxy));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.clickYearlyRadioBtnById();

        commercePage.clickMercadoRadioIconId();
        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        redemptionPage.handleMercadoPago(localeToUse);
        commercePage.clickBtnReviewSubscriptionCta(localeToUse, paymentType);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, localeToUse, false, false, isMobile());

        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent());

        sa.assertAll();
    }

    @Test(description = "Combo+ - Mercado Pago", groups = {"MX", TestGroup.STAR_COMMERCE, TestGroup.STAR_SMOKE, TestGroup.STAR_COMMERCE_SMOKE})
    public void mercadoComboPlus() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, MX, "XWEBQAP-52468"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "mercado";
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickMercadoRadioIconId();

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.handleMercadoPago(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent());

        sa.assertAll();
    }

    @Test(description = "Megabundle - Mercado Pago", groups = {"MX", "AR", "BR", "CL", "CO", TestGroup.STAR_COMMERCE})
    public void megaBundleMercado() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "mercado";
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickMercadoRadioIconId();

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=lionsgateplusbundle");

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.handleMercadoPago(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        commercePage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent());

        sa.assertAll();
    }
}
