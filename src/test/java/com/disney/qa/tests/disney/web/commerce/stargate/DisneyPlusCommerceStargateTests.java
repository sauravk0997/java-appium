package com.disney.qa.tests.disney.web.commerce.stargate;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.client.responses.account.DisneyFullAccount;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;

import exceptions.DisneyAccountNotFoundException;
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

public class DisneyPlusCommerceStargateTests extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "D+/S+ Megabundle - Monthly user purchases Megabundle", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void monthlyUserBuysMegaBundle() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickMegaBundleUpgradeButton(sa);
        accountPage.clickDplusSubscriberAgreementContinue();

        commercePage.clickChangePaymentCtaIfPresent();
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

        accountPage.isBundleSuccessPage();
        commercePage.assertUrlContains(sa, "bundle-success");
        sa.assertAll();
    }

    @Test(description = "D+/S+ Megabundle - Annual user purchases Megabundle", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void annualUserBuysMegaBundle() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickMegaBundleUpgradeButton(sa);
        accountPage.clickDplusSubscriberAgreementContinue();

        commercePage.clickChangePaymentCtaIfPresent();
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

        accountPage.isBundleSuccessPage();
        commercePage.assertUrlContains(sa, "bundle-success");
        sa.assertAll();
    }

    @Test(description = "D+/S+ Megabundle - Monthly user purchases Megabundle on partner", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void monthlyUserBuysPartnerMegabundle() throws URISyntaxException, IOException, JSONException, DisneyAccountNotFoundException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "credit";
        String type = "complete-purchase";
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        //To create partner account with all oveerides
        DisneyAccountApi partnerAccountApi = getPartnerAccountApi();
        DisneyAccount account = partnerAccountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        partnerAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(account.getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        userPage.clickSubscriberAgreementContinueCta(locale);
        redemptionPage.finishLoginWithPartnerAccount(account.getUserPass(), locale);

        //To override the account in the current platform
        DisneyAccountApi currentAccountApi = getAccountApi();
        DisneyAccount secondAccount = new DisneyAccount();
        DisneyFullAccount fullAccount = getAccountApi().readAccount(account.getEmail(), account.getUserPass());
        String acctId = fullAccount.getAccountId();
        String identityId = fullAccount.getIdentities().get(0).getIdentityId();
        secondAccount.setAccountId(acctId);
        secondAccount.setIdentityPointId(identityId);
        currentAccountApi.overrideLocations(secondAccount, locale);
        currentAccountApi.patchAccountAttributeForLocation(secondAccount, locale, PatchType.IDENTITY);
        currentAccountApi.patchAccountAttributeForLocation(secondAccount, locale, PatchType.ACCOUNT);

        userPage.reloadPageForLocationOverride(locale);
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);
        commercePage.clickSubscriberAgreementContinueCtaIfPresent(locale);
        commercePage.clickChangePaymentCtaIfPresent();
        
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

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, false);

        sa.assertAll();
    }

    @Test(description = "D+/S+ Megabundle - Annual user purchases Megabundle on partner", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void annualUserBuysPartnerMegabundle() throws URISyntaxException, IOException, JSONException, DisneyAccountNotFoundException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "credit";
        String type = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        //To create partner account with all oveerides
        DisneyAccountApi partnerAccountApi = getPartnerAccountApi();
        DisneyAccount account = partnerAccountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        partnerAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(account.getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        userPage.clickSubscriberAgreementContinueCta(locale);
        redemptionPage.finishLoginWithPartnerAccount(account.getUserPass(), locale);

        //To override the account in the current platform
        DisneyAccountApi currentAccountApi = getAccountApi();
        DisneyAccount secondAccount = new DisneyAccount();
        DisneyFullAccount fullAccount = getAccountApi().readAccount(account.getEmail(), account.getUserPass());
        String acctId = fullAccount.getAccountId();
        String identityId = fullAccount.getIdentities().get(0).getIdentityId();
        secondAccount.setAccountId(acctId);
        secondAccount.setIdentityPointId(identityId);
        currentAccountApi.overrideLocations(secondAccount, locale);
        currentAccountApi.patchAccountAttributeForLocation(secondAccount, locale, PatchType.IDENTITY);
        currentAccountApi.patchAccountAttributeForLocation(secondAccount, locale, PatchType.ACCOUNT);

        userPage.reloadPageForLocationOverride(locale);
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);
        commercePage.clickSubscriberAgreementContinueCtaIfPresent(locale);

        commercePage.clickChangePaymentCtaIfPresent();

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

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, false);

        sa.assertAll();
    }

    @Test(description = "D+/S+ Megabundle - Ineligible Combo+ user", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void comboUserMegaBundlePurchase() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(COMBO, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnLogoutWebOrMobile(isMobile());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        setOverride(userPage.signUpEmailPassword(locale, true, false, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        sa.assertTrue(commercePage.isModalPrimaryBtnPresent(), "Error message Unable to purchase Megabundle is not present");

        sa.assertAll();
    }

    @Test(description = "D+/S+ Megabundle - Ineligible Combo+ user on partner site", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void comboUserMegaBundlePartnerPurchase() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyAccountApi accountApi = getPartnerAccountApi();
        DisneyAccount account = accountApi.createAccount(COMBO, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.setMegaBundleTest(disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_MEGABUNDLE_COUNTRY));
        userPage.clickSubscriberAgreementContinueCta(locale);

        redemptionPage.finishLoginWithPartnerAccount(disneyAccount.get().getUserPass(), locale);
        if (!isMobile()) {
            commercePage.handleMatureContentOnboardingFlow(false); 
        }

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnLogoutWebOrMobile(isMobile());

        setOverride(userPage.signUpEmailPassword(locale, true, false, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        sa.assertTrue(commercePage.isModalPrimaryBtnPresent(), "Error message Unable to purchase Megabundle is not present");

        sa.assertAll();
    }

    @Test(description = "Licence plate flow in Stargate Countries", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void licensePlateStargateCountryUser() throws IOException, JSONException, URISyntaxException, IOException, InterruptedException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        String type = "complete-purchase";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account,locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat("/begin"));
        commercePage.generateAndEnterLicensePlateInputField(account);
        commercePage.clickLicensePlateSubmit();

        commercePage.enterLoginFlow(account.getEmail(), account.getUserPass());

        commercePage.isRegularSignUpBtnPresent();
        commercePage.assertUrlContains(sa, type);

        commercePage.clickSignUpNowBtn();
        commercePage.clickSubscriberAgreementContinueCtaIfPresent(locale);
        commercePage.override3DS2Data(proxy, locale);

        sa.assertTrue(commercePage.isMonthlyRadioBtnByIdPresent(), "Standalone Monthly is not present in the billing page");
        sa.assertTrue(commercePage.isYearlyRadioBtnByIdPresent(), "Standalone Yearly is not present in the billing page");
        sa.assertTrue(commercePage.isComboPlusUpsellPresent(), "Combo+ toggle button is not present in the billing page");

        sa.assertAll();
    }

    @Test(description = "Stargate Countries - Billing Page - Non license plate user flow", groups = {"MX", "AR", "BR", "CL", "CO", "EC", "PE", TestGroup.STAR_COMMERCE})
    public void nonLicensePlateStargateBillingPage() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, false, false, language, disneyAccount, proxy));
        commercePage.override3DS2Data(proxy, locale);

        sa.assertTrue(commercePage.isMonthlyRadioBtnByIdPresent(), "Standalone Monthly is not present in the billing page");
        sa.assertTrue(commercePage.isYearlyRadioBtnByIdPresent(), "Standalone Yearly is not present in the billing page");
        sa.assertFalse(commercePage.isComboPlusUpsellPresent(), "Combo+ toggle button is present in the billing page");

        sa.assertAll();
    }

}
