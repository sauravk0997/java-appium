package com.disney.qa.tests.disney.web.commerce.upgrades;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.commerce.DisneyPlusBillingPage;
import com.disney.qa.disney.web.commerce.DisneyPlusChangeSubscriptionConfirmPage;
import com.disney.qa.disney.web.commerce.DisneyPlusChangeSubscriptionPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusPlansPage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.proxy.MitmProxy;
import com.proxy.MitmProxyConfiguration;
import com.proxy.MitmProxyPool;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceUnifiedUpgradeBundleTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void setupTest() throws InterruptedException {
        disneyAccount.set(new DisneyAccount());
        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());

        MitmProxyConfiguration configuration = MitmProxyConfiguration.builder()
                .listenPort(getProxyPort())
                .headers(disneyPlusBasePage.getHeaders(locale)).build();
        MitmProxy.startProxy(configuration);
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
        MitmProxyPool.tearDownProxy();
    }

    @Test(description = "Bundle - Subscription Details - Credit Card - Stored Payment", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.DISNEY_COMMERCE_SMOKE})
    public void upgradeSubscriptionDetailsCreditCardStored() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52464"));

        SoftAssert sa = new SoftAssert();

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        sa.assertTrue(accountPage.verifyPage(),"Account page didn't load");

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();

        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        sa.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC);

        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());
        changeSubscriptionConfirmPage.enterCVV(countryData.searchAndReturnCountryData(locale, CODE, CVV));
        changeSubscriptionConfirmPage.clickSubmitButton();

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - Subscription Details - PayPal - Stored Payment", groups = {"US", TestGroup.DISNEY_COMMERCE })
    public void upgradeSubscriptionDetailsPayPalStored() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipPayPalTestByProduct();
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        WebDriver driver = getDriver();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(driver);
        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);
        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(driver);

        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        userPage.setIgnoreCookie(true);
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_ADS);
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        Assert.assertTrue(plansPage.verifyPage(), "Plans page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page didn't load");
        billingPage.billingWithPaypal();

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();
        accountPage.clickSwitchSubscriptionBundleUpgrade(locale);

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC);

        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");
        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        redemptionPage.clickCreditSubmitBtnByIdIfPresent();
        redemptionPage.clickStoredPaymentSubmitBtnIfPresent();

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - Account - Credit Card - Stored Payment", groups = {"US", TestGroup.DISNEY_COMMERCE })
    public void upgradeAccountCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        sa.assertTrue(accountPage.verifyPage(),"Account page didn't load");

        accountPage.clickUpgradeToBundleCta();

        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        sa.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM);//NOAH

        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());
        changeSubscriptionConfirmPage.enterCVV(countryData.searchAndReturnCountryData(locale, CODE, CVV));
        changeSubscriptionConfirmPage.clickSubmitButton();

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - Homepage - Credit Card - Stored Payment", groups = {"US", TestGroup.DISNEY_COMMERCE })
    public void upgradeHomepageCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        DisneyPlusChangeSubscriptionConfirmPage confirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();

        if (DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            commercePage.openURL(commercePage.getHomeUrl().concat(DisneyPlusBasePage.DISNEY_WEAPONX_DISABLE));
        }

        userPage.handleBundleCtaOnOlp();
        userPage.typeDplusBaseEmailFieldId(account.getEmail());
        userPage.clickOnSignupContinueButton();
        userPage.typeDplusBasePasswordFieldId(account.getUserPass());
        userPage.clickLoginSubmit();

        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page loaded");
        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC);

        Assert.assertTrue(confirmPage.verifyPage(), "Change subscription confirm page loaded");
        confirmPage.handleStoredPayment(countryData.searchAndReturnCountryData(locale, CODE, CVV));

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle - My Services", groups = {"US", TestGroup.DISNEY_COMMERCE })
    public void bundleMyServices() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(BUNDLE_BASIC, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();

        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        sa.assertTrue(accountPage.verifyPage(),"Account page didn't load");

        commercePage.checkMyServices(sa, accountPage);

        sa.assertAll();
    }

    @Test(description = "Bundle (SASH to NOAH) - Subscription Details - Credit Card - Stored Payment", groups = {US, TestGroup.DISNEY_COMMERCE})
    public void upgradeSashToNoahSubscriptionDetailsCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(BUNDLE_BASIC, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();

        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        sa.assertTrue(accountPage.verifyPage(),"Account page didn't load");

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();

        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        DisneyPlusChangeSubscriptionConfirmPage confirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());

        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page loaded");
        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM);

        Assert.assertTrue(confirmPage.verifyPage(), "Change subscription confirm page loaded");
        confirmPage.handleStoredPayment(countryData.searchAndReturnCountryData(locale, CODE, CVV));

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle (SASH to NOAH) - Subscription Details - PayPal - Stored Payment", groups = {US, TestGroup.DISNEY_COMMERCE}, enabled = false)
    public void upgradeSashToNoahSubscriptionDetailsPayPalStored() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipPayPalTestByProduct();

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String paymentType = "paypal";
        boolean isRedemption = false;

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

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickChangeSubscriptionUpgradeCta();

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleUpgradeCardNotPresent(), false, "upgrade");

        redemptionPage.clickCreditSubmitBtnById();

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle (SASH to NOAH) - Account - Credit Card - Stored Payment", groups = {US, TestGroup.DISNEY_COMMERCE})
    public void upgradeSashToNoahAccountCreditCardStored() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(BUNDLE_BASIC, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();

        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        sa.assertTrue(accountPage.verifyPage(),"Account page didn't load");

        accountPage.clickUpgradeSashToNoahCta();

        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        DisneyPlusChangeSubscriptionConfirmPage confirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());

        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page loaded");
        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM);

        Assert.assertTrue(confirmPage.verifyPage(), "Change subscription confirm page loaded");
        confirmPage.handleStoredPayment(countryData.searchAndReturnCountryData(locale, CODE, CVV));

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle (SASH) - Deeplink - Credit Card", groups = {US, TestGroup.DISNEY_COMMERCE}, enabled = false)
    public void upgradeDeepLinkSash() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByEnv(PROD);

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        commercePage.openURL(commercePage.getHomeUrl().concat("/upgrade?bundleType=sash"));

        userPage.enterLoginFlow(account.getEmail(), account.getUserPass());

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleUpgradeCardPresent(), false, "billing?type=bundle");

        sa.assertEquals(commercePage.getCurrentUrl(), commercePage.getHomeUrl().concat("/upgrade?bundleType=sash"));

        commercePage.clickChangePaymentCtaIfPresent();

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

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }

    @Test(description = "Bundle (NOAH) - Deeplink - Credit Card", groups = {US, TestGroup.DISNEY_COMMERCE}, enabled = false)
    public void upgradeDeepLinkNoah() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByEnv(PROD);

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        commercePage.openURL(commercePage.getHomeUrl().concat("/upgrade?bundleType=noah"));

        userPage.enterLoginFlow(account.getEmail(), account.getUserPass());

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.isBundleUpgradeCardPresent(), false, "billing?type=bundle");

        sa.assertEquals(commercePage.getCurrentUrl(), commercePage.getHomeUrl().concat("/upgrade?bundleType=noah"));

        commercePage.clickChangePaymentCtaIfPresent();

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

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        sa.assertAll();
    }
}
