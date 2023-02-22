package com.disney.qa.tests.disney.web.commerce.upgrades;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceUpgradesBundleTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private boolean isRedemption = false;
    private String paymentProcessingLink = DisneyWebParameters.DISNEY_WEB_MERCADO_PROCESSING_LINK.getValue();

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Bundle - Subscription Details - Credit Card - Stored Payment", groups = {"MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void upgradeSubscriptionDetailsCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);
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
            cvv,
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, true, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();
        accountPage.clickSwitchSubscriptionBundleUpgrade(locale);
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        redemptionPage.handleStoredPaymemt(cvv);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - Subscription Details - PayPal - Stored Payment", groups = {"MX", "BO", "BR", "CL", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void upgradeSubscriptionDetailsPayPalStored() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickPayPalRadioIconById(locale);

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, true, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();
        accountPage.clickSwitchSubscriptionBundleUpgrade(locale);
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        redemptionPage.clickCreditSubmitBtnByIdIfPresent();
        redemptionPage.clickStoredPaymentSubmitBtnIfPresent();

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - Subscription Details - Mercado Pago - Change Payment", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void upgradeSubscriptionDetailsMercadoPagoStored() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isFail = false;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickMercadoRadioIconId();

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        String taxId = redemptionPage.searchAndReturnZipTaxId(locale);

        redemptionPage.handleMercadoPago(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            commercePage.assertPaymentProccesing(sa, isFail, commercePage.isPaymentProcessingSpinnerPresent(), commercePage.isBillingErrorBannerPresent());
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();
        accountPage.clickSwitchSubscriptionBundleUpgrade(locale);
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.clickChangePaymentCtaIfPresent();
        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        commercePage.clickMercadoRadioIconId();

        commercePage.handleTaxIdBillingFields(locale, taxId);
        redemptionPage.handleMercadoPago(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long

        if (!DisneyPlusBasePage.ENVIRONMENT.equalsIgnoreCase("QA")) {
            commercePage.openURL(commercePage.getHomeUrl().concat(paymentProcessingLink));
            redemptionPage.reloadMercadoPaymentPage();
            pause(EXPLICIT_TIMEOUT); // Needed since payment processing takes long
        }
        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - Subscription Details - Credit Card - Change Payment", groups = {"MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void upgradeSubscriptionDetailsCreditCardChange() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";
        boolean isMegaBundleCountry = disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.patchStarOnboardingStatus(account, false);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, true, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();
        if (isMegaBundleCountry) {
            accountPage.clickSwitchSubscriptionBundleUpgrade(locale);
        }        
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        pause(SHORT_TIMEOUT); //needed for some countries to load only during changePayment

        commercePage.clickChangePaymentCtaIfPresent();
        commercePage.override3DS2Data(proxy, locale);

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

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - Account - Credit Card - Stored Payment", groups = {"MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE, TestGroup.STAR_SMOKE, TestGroup.STAR_COMMERCE_SMOKE})
    public void upgradeAccountCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, MX, "XWEBQAP-52467"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);
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
            cvv,
            redemptionPage.searchAndReturnZipTaxId(locale),
            locale
        );

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickUpgradeToBundleCta();
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        redemptionPage.handleStoredPaymemt(cvv);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        accountPage.isBundleSuccessPage();
        commercePage.assertUrlContains(sa, "bundle-success");

        sa.assertAll();
    }

    @Test(description = "Bundle - Homepage - Credit Card - Stored Payment", groups = {"MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void upgradeHomepageCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isActivation = false;
        String paymentType = "credit";

        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
            .country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, false, disneyAccount, proxy));

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
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

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, true, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnLogoutWebOrMobile(isMobile());

        if (DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            commercePage.openURL(commercePage.getHomeUrl().concat(DisneyPlusBasePage.DISNEY_WEAPONX_DISABLE));
        } else {
            userPage.isSignupCtasPresent();
        }

        setOverride(userPage.signUpEmailPassword(locale, true, isActivation, account.getEmail(), account.getUserPass(), language, true, disneyAccount, proxy));

        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);
        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        redemptionPage.handleStoredPaymemt(cvv);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);

        if (locale.equalsIgnoreCase("US")) {
            commercePage.assertSuccessOverlay(sa, commercePage.isPurchaseSuccessSuperBundle(), commercePage.isPurchaseSuccessConfirmBtnPresent(), false, locale, isMobile());
        } else {
            commercePage.assertSuccessOverlay(sa, commercePage.isPurchaseSuccessComboPlusSuccess(), commercePage.isPurchaseSuccessConfirmBtnPresent(), false, locale, isMobile());
        }

        sa.assertAll();
    }

    @Test(description = "Bundle - My Services", groups = {"MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void bundleMyServices() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

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

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        commercePage.checkMyServices(sa, accountPage);

        sa.assertAll();
    }

    @Test(description = "Bundle - Sign Up - Credit Card - Change Payment", groups = {"MX", "AR", "BO", "BR", "CL", "CO", "CR", "DO", "EC", "GT", "HN", "NI", "PA", "PE", "PY", "SV", "UY", TestGroup.STAR_COMMERCE})
    public void upgradeSignUpCreditCardChanged() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.patchStarOnboardingStatus(account, false);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.signUpEmailPassword(locale, isBundle, false, account.getEmail(), account.getUserPass(), language, true, disneyAccount, proxy.get());

        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        commercePage.clickChangePaymentCtaIfPresent();

        commercePage.assertUrlContains(sa, "upgrade");

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
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

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, false);

        sa.assertAll();
    }
}
