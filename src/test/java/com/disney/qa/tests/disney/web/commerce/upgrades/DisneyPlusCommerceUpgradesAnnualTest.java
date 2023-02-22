package com.disney.qa.tests.disney.web.commerce.upgrades;


import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;

import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceUpgradesAnnualTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    private boolean isRedemption = false;

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Monthly To Annual - Account Details - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE}, enabled =false)
    public void upgradeAnnualAccountCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

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

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.isUpgradeToAnnualCard();
        accountPage.clickUpgradeToAnnualCta();
        commercePage.isCreditSubmitBtnIdPresent();
        redemptionPage.clickCreditSubmitBtnById();

        userPage.isAccountDropdownIdPresent();
        pause(HALF_TIMEOUT); // Needed to ensure page is fully loaded
        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account");

        sa.assertAll();
    }

    @Test(description = "Monthly To Annual - Account Details - PayPal", groups = {"US", TestGroup.DISNEY_COMMERCE}, enabled =false)
    public void upgradeAnnualAccountPayPal() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.clickPayPalRadioIconById(locale);
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.isUpgradeToAnnualCard();
        accountPage.clickUpgradeToAnnualCta();
        commercePage.isCreditSubmitBtnIdPresent();
        redemptionPage.clickCreditSubmitBtnById();

        userPage.isAccountDropdownIdPresent();
        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account");

        sa.assertAll();
    }


    @Test(description = "Monthly To Annual - Account Details - Ideal", groups = {"NL"}, enabled = false)
    public void upgradeAnnualAccountIdeal() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "ideal";
        Boolean isFail = false;
        String completePurchase = "complete-purchase";

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

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.isUpgradeToAnnualCard();
        accountPage.clickUpgradeToAnnualCta();
        commercePage.handleWorldPayTransaction(paymentType, isFail);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();
        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account");

        sa.assertAll();
    }

    @Test(description = "Monthly To Annual - Account Details - Klarna", groups = {"DE"}, enabled = false)
    public void upgradeAnnualAccountKlarna() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "klarna";
        Boolean isFail = false;
        String completePurchase = "complete-purchase";

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

        commercePage.handleWorldPayTransaction(paymentType, isFail);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.isUpgradeToAnnualCard();
        accountPage.clickUpgradeToAnnualCta();
        commercePage.handleWorldPayTransaction(paymentType, isFail);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();
        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account");

        sa.assertAll();
    }

    @Test(description = "Monthly To Annual - Subscription Details - Credit Card - Stored Payment", groups = {"US", TestGroup.DISNEY_COMMERCE}, enabled =false)
    public void upgradeSubscriptionDetailsCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";
        String cvv = countryData.searchAndReturnCountryData(locale, CODE, CVV);
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

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();
        accountPage.clickChangeSubscriptionAnnualUpgradeCta();

        redemptionPage.handleStoredPaymemt(cvv);

        userPage.isAccountDropdownIdPresent();
        pause(SHORT_TIMEOUT); // Needed for the component to update prior
        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account");

        sa.assertAll();
    }

    @Test(description = "Monthly To Annual - Subscription Details - Credit Card - Change Payment", groups = {"US", TestGroup.DISNEY_COMMERCE}, enabled =false)
    public void upgradeSubscriptionDetailsCreditCardChange() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

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

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();
        accountPage.clickChangeSubscriptionAnnualUpgradeCta();
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

        userPage.isAccountDropdownIdPresent();
        pause(SHORT_TIMEOUT); // Needed for the component to update prior
        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account");

        sa.assertAll();
    }
}
