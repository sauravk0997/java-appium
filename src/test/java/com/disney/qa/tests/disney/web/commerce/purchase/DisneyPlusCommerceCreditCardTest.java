package com.disney.qa.tests.disney.web.commerce.purchase;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.disney.DisneyHttpHeaders;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusBillingPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusPlansPage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAddProfilePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.disney.web.entities.WebConstant;
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
import java.time.LocalDate;

public class DisneyPlusCommerceCreditCardTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    private String paymentType = "credit";
    private boolean isRedemption = false;
    private boolean isPartner = false;
    private String completePurchase = "complete-purchase";
    private String vermontZip = "05030";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Monthly Subscription - Credit Card", groups = {"US", "AD", "AG", "AI", "AL", "AR", "AS", "AT", "AU", "AW", "AX", "BA", "BB", "BE", "BG", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CC", "CH", "CK", "CL", "CO", "CR", "CW", "CX", "CZ", "DE", "DK", "DM", "DO", "EC", "EE", "ES", "FI", "FK", "FO", "FR", "GB", "GD", "GF", "GG", "GI", "GL", "GP", "GR", "GS", "GT", "GU", "GY", "HK", "HN", "HR", "HT", "HU", "IE", "IM", "IO", "IS", "IT", "JE", "JM", "JP", "KN", "KR", "KY", "LC", "LI", "LT", "LU", "LV", "MC", "ME", "MF", "MH", "MK", "MP", "MQ", "MS", "MT", "MU", "MX", "NC", "NF", "NI", "NL", "NO", "NU", "NZ", "PA", "PE", "PF", "PL", "PM", "PN", "PR", "PT", "PY", "RE", "RO", "RS", "RU", "SE", "SG", "SH", "SI", "SJ", "SK", "SM", "SR", "SV", "SX", "TC", "TF", "TK", "TR", "TT", "TW", "UM", "UY", "VA", "VC", "VE", "VG", "VI", "WF", "YT", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE,TestGroup.STAR_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE, TestGroup.STAR_COMMERCE_SMOKE})
    public void monthly() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52858", "XWEBQAP-52447"));

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

    @Test(description = "Annual Subscription - Credit Card", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void annual() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52861"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = false;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        if (locale.equalsIgnoreCase(WebConstant.US)) {
            commercePage.clickSignUpNowBtn();
            DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
            plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);
        } else {
            commercePage.clickCompleteAndRestartPurchaseSignUpNowBtn(locale);
            commercePage.clickSubscriberAgreementContinueCta(locale);
        }
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.clickYearlyRadioBtnById();

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

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

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "D+ Superbundle (SASH) / S+ Combo+ - Credit Card", groups = {"US", "MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.STAR_SMOKE, TestGroup.STAR_COMMERCE_SMOKE})
    public void superBundleSash() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52462", "XWEBQAP-52459", "XWEBQAP-52449"));

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

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Superbundle (NOAH) - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void superBundleNoah() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52465"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;

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

        commercePage.clickBundleUpgradeCardLink();

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleOfferCardDowngradePresent(), false, "billing?bundleType=noah");

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Superbundle Redirect Hulu Sash User - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void superBundleRedirectHuluUser() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52083"));

        SoftAssert sa = new SoftAssert();
        boolean isActivation = false;
        boolean isBundle = true;
        disneyAccount.get().setEmail(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_USER.getValue());
        disneyAccount.get().setUserPass(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_PASS.getDecryptedValue());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        sa.assertTrue(userPage.isRedirectHuluSubscriberHeaderPresent(),
                "Hulu redirect header not shown");
        sa.assertTrue(userPage.isRedirectHuluSubscriberButtonPresent(),
                "Hulu redirect button not shown");

        sa.assertAll();
    }

    @Test(description = "Superbundle Ineligible (SASH only) - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void superBundleIneligibleSash() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isActivation = false;
        boolean isBundle = true;
        disneyAccount.get().setEmail(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_SASH_USER.getValue());
        disneyAccount.get().setUserPass(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_SASH_PASS.getDecryptedValue());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        pause(TIMEOUT);
        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleUpgradeCardNotPresent(), false, "billing?type=bundle");

        sa.assertAll();
    }

    @Test(description = "Superbundle Redirect - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void superBundleRedirect() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52085"));

        SoftAssert sa = new SoftAssert();
        boolean isActivation = false;
        boolean isBundle = true;
        disneyAccount.get().setEmail(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_USER.getValue());
        disneyAccount.get().setUserPass(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_PASS.getDecryptedValue());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        sa.assertTrue(userPage.isRedirectHuluSubscriberHeaderPresent(),
                "Hulu redirect header not shown");
        sa.assertTrue(userPage.isRedirectHuluSubscriberButtonPresent(),
                "Hulu redirect button not shown");
        sa.assertAll();
    }

    @Test(description = "Superbundle Redirect Page - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void superBundleRedirectPage() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52086"));

        SoftAssert sa = new SoftAssert();
        boolean isActivation = false;
        boolean isBundle = true;
        disneyAccount.get().setEmail(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_2_USER.getValue());
        disneyAccount.get().setUserPass(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_2_PASS.getDecryptedValue());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        sa.assertTrue(userPage.isRedirectHuluSubscriberHeaderPresent(),
                "Hulu redirect header not shown");
        sa.assertTrue(userPage.isRedirectHuluSubscriberButtonPresent(),
                "Hulu redirect button not shown");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription - Credit Card - Blocked ", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void paymentBlocked() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52087"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
        commercePage.override3DS2Data(proxy, locale);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
                commercePage.getCreditCardName(locale),
                commercePage.getBlockedCreditCardNumber(locale),
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale),
                locale
        );

        redemptionPage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertCreditCardPaymentBlocked(sa);

        sa.assertAll();
    }

    @Test(description = "Error - Credit Card", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    private void error() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52088"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
        commercePage.override3DS2Data(proxy, locale);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        if (locale.equalsIgnoreCase("US") && !DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            creditCard = "5460 8357 8210 5440";
        } else if (locale.equalsIgnoreCase("MX") && !DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")){
            creditCard = "5364 8155 8780 4709";
        }

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
                redemptionPage.getCardHolderErrorName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale),
                locale
        );

        redemptionPage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        sa.assertTrue(commercePage.isErrorProcessingPresent(),
                "Error Processing alert not present");

        sa.assertAll();
    }

    @Test(description = "Deeplink - Credit Card", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void deepLink() throws JSONException, IOException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52089"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
            .country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(commercePage.getDeeplinkUrl(locale)));

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        if (locale.equalsIgnoreCase(WebConstant.US)) {
            commercePage.isCreditSubmitBtnIdPresent();

            if (!DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
                sa.assertEquals(commercePage.getCurrentUrl(), commercePage.getHomeUrl().concat(commercePage.getDeeplinkUrl(locale)));
            }
        }
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
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Deeplink - Superbundle (NOAH) - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void superBundleDeeplinkNoah() throws URISyntaxException, JSONException, IOException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52090"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(DisneyWebParameters.DISNEY_WEB_SUPERBUNDLE_NOAH_DEEPLINK_URL.getValue()));

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
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

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleOfferCardDowngradePresent(), true, "billing?bundleType=noah");

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Annual Subscription - Credit Card - Vermont (Annual Opt In)", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void vermontMonthly() throws JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52094"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickSignUpNowBtn();

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.clickYearlyRadioBtnById();

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        redemptionPage.enterPurchaseFlowBillingInfo(
                commercePage.getCreditCardName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                vermontZip,
                locale
        );
        if (!DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            commercePage.getAnnualOptInCheckbox().click();
        }

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription - Credit Card - TDA Enabled", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE,TestGroup.STAR_SMOKE})
    public void monthlyTda() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52102"));

        SoftAssert sa = new SoftAssert();
        boolean checkMonthly = true;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.removeTestHeader(proxy);
        proxy.addHeader(DisneyHttpHeaders.BAMTECH_TDA_ENABLE, "true");
        proxy.addHeader(DisneyHttpHeaders.BAMTECH_TDA_DURATION, "10000");

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setIgnoreCookie(true);
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        if (!locale.equalsIgnoreCase(WebConstant.US)) {
            commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);
        }
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

        pause(EXPLICIT_TIMEOUT); // Needed for TDA

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        if (isMobile()) {
            commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);
            commercePage.openURL(commercePage.getHomeUrl().concat("/account"));
        } else {
            if (!locale.equalsIgnoreCase("MX")) {
                addProfilePage.updateProfile();
            }
            commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false,
                    isMobile());
            accountPage.clickOnAccountDropdown(isMobile());
        }
        sa.assertTrue(accountPage.isMonthlyInternalD2CSubscriptionCtaNotPresent(), "Subscription is present");

        sa.assertAll();
    }

    @Test(description = "Bundle Upsell - Credit Card", groups = {"GT"})
    public void bundleupsell() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);
        if (DisneyPlusCommercePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            skipTestByEnv(BETA);
            skipTestByEnv(PROD);
        }

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());

        redemptionPage.clickBundleUpsellToggle();

        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

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

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), true, isPartner);

        sa.assertAll();
    }

    @Test(description = "Billing Opt In Error Verification", groups = {"KR"})
    public void billingOptInErrorVerification() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
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

        //uncheck both checkboxes.
        commercePage.clickCheckBoxElectronicPaymentConsent();
        commercePage.clickCheckBoxThirdPartyProvisionConsent();
        commercePage.clickCreditSubmitBtnById();
        sa.assertTrue(commercePage.isOptInErrorMessagePresent(), "Opt in error message not present");

        //check only first checkbox and verify.
        commercePage.clickCheckBoxElectronicPaymentConsent();
        commercePage.clickCreditSubmitBtnById();
        sa.assertTrue(commercePage.isOptInErrorMessagePresent(), "Opt in error message not present");

        //uncheck first checkbox and check second one and then verify.
        commercePage.clickCheckBoxElectronicPaymentConsent();
        commercePage.clickCheckBoxThirdPartyProvisionConsent();
        commercePage.clickCreditSubmitBtnById();
        sa.assertTrue(commercePage.isOptInErrorMessagePresent(), "Opt in error message not present");

        sa.assertAll();
    }

    @Test(description = "Billing Date Of Birth Error Verification", groups = {"KR"})
    public void billingDateOfBirthErrorVerification() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
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

        //Verify wrong day
        LocalDate date = redemptionPage.generateRandomDateWithBillingAge(locale).plusDays(1);
        redemptionPage.enterDateOfBirth(date);
        sa.assertTrue(redemptionPage.isDateDropdownErrorMessagePresent(), "As Day is wrong, Date dropdown error message should be present.");

        //Verify wrong month : The month verification can be skipped for December, as plus 1 month will go to next year, which is not in the list
        if (date.getMonthValue() != 12) {
            date = redemptionPage.generateRandomDateWithBillingAge(locale).plusMonths(1);
            redemptionPage.enterDateOfBirth(date);
            sa.assertTrue(redemptionPage.isDateDropdownErrorMessagePresent(), "As Month is wrong, Date dropdown error message should be present.");
            sa.assertTrue(redemptionPage.isDayDisabled(), "As Month is wrong, Date dropdown should be disabled.");
        }

        sa.assertAll();
    }

    @Test(description = "D+/S+ Megabundle - Credit Card", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void megaBundleCreditCard() throws URISyntaxException, JSONException, IOException, MalformedURLException {
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
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, false, language, disneyAccount, proxy));
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

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), isBundle, "billing?type=lionsgateplusbundle");

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Shop Disney - Credit Card - Standalone", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.SHOP_DISNEY})
    public void shopDisneyStandaloneCreditCard() throws URISyntaxException, JSONException, IOException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52598"));

        SoftAssert sa = new SoftAssert();

        boolean checkMonthly = true;
        boolean isBundle = false;
        boolean isRedemption = true; // Flag is required to force only signing up for a new account

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.openURL(commercePage.getHomeUrl().concat(PageUrl.SHOP_DISNEY_FROZEN).concat(WebConstant.STANDARD));

        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);

        commercePage.assertBillingPageElements(sa, commercePage.isMonthlyRadioBtnByIdChecked(), commercePage.isYearlyRadioBtnByIdChecked(), checkMonthly);

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            countryData.searchAndReturnCountryData(locale, CODE, ZIP));
        
        commercePage.assertSuccessOverlay(sa, commercePage.isPurchaseSuccessNewUser(), commercePage.isPurchaseSuccessRedirectBtnPresent(), false, locale, isMobile());
        commercePage.clickBackToShopDisneyBtn(isMobile());
        commercePage.verifyUrlText(sa, PageUrl.SHOP_DISNEY_FROZEN_PRODUCT);

        sa.assertAll();
    }

    @Test(description = "Shop Disney - Credit Card - Super Bundle", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.SHOP_DISNEY})
    public void shopDisneySuperBundleCreditCard() throws URISyntaxException, JSONException, IOException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52598"));

        SoftAssert sa = new SoftAssert();

        boolean isBundle = true;
        boolean isRedemption = true; // Flag is required to force only signing up for a new account

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.openURL(commercePage.getHomeUrl().concat(PageUrl.SHOP_DISNEY_FROZEN).concat(WebConstant.BUNDLE));

        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, PageUrl.BILLING_TYPE_BUNDLE);

        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
            countryData.searchAndReturnCountryData(locale, CODE, EXP),
            countryData.searchAndReturnCountryData(locale, CODE, CVV),
            countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.assertSuccessOverlay(sa, commercePage.isPurchaseSuccessSuperBundle(), commercePage.isPurchaseSuccessRedirectBtnPresent(), false, locale, isMobile());
        commercePage.clickBackToShopDisneyBtn(isMobile());
        commercePage.verifyUrlText(sa, PageUrl.SHOP_DISNEY_FROZEN_PRODUCT);

        sa.assertAll();
    }
}
