package com.disney.qa.tests.disney.web.commerce.purchase;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.commerce.DisneyPlusAccountBlockedPage;
import com.disney.qa.disney.web.commerce.DisneyPlusBillingPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusPlansPage;
import com.disney.qa.disney.web.commerce.DisneyPlusSubscriptionDetailsPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.disney.web.entities.PlanCardTypes.SubscriptionPlan;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;

import com.disney.util.disney.ZebrunnerXrayLabels;
import org.json.JSONException;
import org.openqa.selenium.NoSuchWindowException;
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

public class DisneyPlusCommerceArielSignUpTests extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    String data;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        disneyAccount.set(new DisneyAccount());
        data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    private void billingWithCreditCard(WebDriver driver) {
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));
    }

    @Test(description = "Monthly Subscription Standalone With Ads - Credit Card", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void monthlyStandaloneWithAdsCC() throws URISyntaxException, IOException, JSONException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53234"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = false;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        Assert.assertTrue(plansPage.verifyPage(), "Plans page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page didn't load");
        billingWithCreditCard(driver);

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_BASIC.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_BASIC_PROMO.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }


    @Test(description = "Monthly Subscription Standalone No Ads - Credit Card", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void monthlyStandaloneNoAdsCC() throws URISyntaxException, IOException, JSONException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52898", "XWEBQAP-52936", "XWEBQAP-52887", "XWEBQAP-52459"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = false;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_NO_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        Assert.assertTrue(plansPage.verifyPage(), "Plans page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page didn't load");
        sa.assertTrue(billingPage.isCreditCardIconVisible(), "Credit card icon is not visible");
        sa.assertTrue(billingPage.isPaypalIconVisible(), "Paypal icon is not visible");
        sa.assertTrue(billingPage.getSubscribeOptionsCount() == 2, "Subscribe options count is not 2");
        sa.assertTrue(billingPage.isMonthlyToggleChecked(), "Monthly toggle button is not checked");

        billingWithCreditCard(driver);

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription 2P Bundle - Credit Card", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE}, enabled = false)
    public void monthly2pCC() throws URISyntaxException, IOException, JSONException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52902"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = true;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_BUNDLE_2P));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page did not load");
        sa.assertTrue(billingPage.isBundleOfferCardVisble(), "Bundle offer card is not visible");
        sa.assertTrue(billingPage.isBundleUpgradeLinkVisible(), "Bundle downgrade link is not visible");

        billingWithCreditCard(driver);

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_2P.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_2P.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription Bundle With Ads(SASH) - Credit Card", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void bundleWithAdsCC() throws URISyntaxException, IOException, JSONException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52901", "XWEBQAP-52942"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = true;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_BUNDLE_SASH));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page did not load");
        sa.assertTrue(billingPage.isBundleOfferCardVisble(), "Bundle offer card is not visible");
        sa.assertTrue(billingPage.isBundleUpgradeLinkVisible(), "Bundle upgrade link is not visible");

        billingWithCreditCard(driver);

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription Bundle No Ads(NOAH) - Credit Card", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void bundleNoAdsCC() throws URISyntaxException, IOException, JSONException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52900"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = true;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_BUNDLE_NOAH));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page did not load");
        sa.assertTrue(billingPage.isBundleOfferCardVisble(), "Bundle offer card is not visible");
        sa.assertTrue(billingPage.isBundleDowngradeLinkVisible(), "Bundle downgrade link is not visible");

        billingWithCreditCard(driver);

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription Standalone With Ads - Paypal", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void monthlyStandaloneWithAdsPaypal() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(BETA);
        skipTestByEnv(PROD);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52903", "XWEBQAP-52639"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = false;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_ADS));
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

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_BASIC.getPlanName()),
                "The purchase plan is different");

        if(ENVIRONMENT.equalsIgnoreCase(QA) || ENVIRONMENT.equalsIgnoreCase(BETA)){
            sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_BASIC_PROMO.getPrice()),
                    "Plan price does not match purchase price");
        }
        else {
            sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_BASIC.getPrice()),
                    "Plan price does not match purchase price");
        }
        sa.assertAll();
    }

    @Test(description = "Monthly Subscription Bundle No Ads(NOAH) - Paypal", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void bundleNoAdsPaypal() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(BETA);
        skipTestByEnv(PROD);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52904", "XWEBQAP-52941"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = true;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_BUNDLE_NOAH));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page did not load");
        sa.assertTrue(billingPage.isBundleOfferCardVisble(), "Bundle offer card is not visible");
        sa.assertTrue(billingPage.isBundleDowngradeLinkVisible(), "Bundle downgrade link is not visible");

        billingPage.billingWithPaypal();

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Annual Subscription Standalone with No Ads - Credit Card", groups = { "US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void annualStandaloneNoAdsCc() throws URISyntaxException, IOException, JSONException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52899", "XWEBQAP-52937"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        boolean isBundle = false;

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_NO_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        Assert.assertTrue(plansPage.verifyPage(), "Plans page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);

        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page didn't load");
        billingPage.clickAnnualToggleButton();
        sa.assertTrue(billingPage.getSubscribeOptionsCount() == 2, "Subscribe options count is not 2");
        sa.assertTrue(billingPage.isAnnualToggleChecked(), "Annual toggle button is not checked");

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_PREMIUM_YEARLY.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_PLUS_PREMIUM_YEARLY.getPrice()),
                "The plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Verify age gating", groups = { "US", TestGroup.DISNEY_COMMERCE,
            TestGroup.ARIEL_COMMERCE, TestGroup.ARIEL_DOB_COMMERCE })
    public void verifyAgeGating() throws URISyntaxException, IOException, JSONException, NoSuchWindowException {
        
        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52671"));

        DisneyAccountApi accountApi = getAccountApi();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusAccountBlockedPage accountBlockedPage = new DisneyPlusAccountBlockedPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_ADS));

        disneyAccount.get().setEmail(DisneyApiCommon.getUniqueEmail());
        disneyAccount.get().setUserPass(DisneyApiCommon.getCommonPass());
        accountApi.overrideLocations(disneyAccount.get(), locale);

        loginPage.signUpWithCreds(disneyAccount);

        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page didn't load");
        billingPage.billingCcWithDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP),
                WebConstant.UNDER_18_DOB);

        Assert.assertTrue(accountBlockedPage.verifyPage(), "Account blocked page didn't load");
        accountBlockedPage.clickHelpCenter();
        sa.assertTrue(userPage.collectNewTabLandingPageURL().contains(PageUrl.DISNEYPLUS_HELP_CENTER),
                "Help center page did not render");

        accountBlockedPage.clickDismissButton();
        sa.assertTrue(userPage.isDplusBaseLoginBtnPresent(SHORT_TIMEOUT), "Login Button is not present");

        sa.assertAll();
    }
       
    @Test(description = "Restart Subscription - Under 18 user", groups = { "US", TestGroup.DISNEY_COMMERCE, 
            TestGroup.ARIEL_COMMERCE, TestGroup.ARIEL_DOB_COMMERCE })
    public void restartSubscriptionAgeGating() throws JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52672"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        DisneyPlusBasePage basePage = new DisneyPlusBasePage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusAccountBlockedPage accountBlockedPage = new DisneyPlusAccountBlockedPage(driver);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(
                AccountUtils.createExpiredAccountWithoutDob(locale, language, SUB_VERSION_V1));
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickSignUpNowBtn();

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);
        Assert.assertTrue(plansPage.verifyPage(), "Plans page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);

        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale,
                false);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page didn't load");
        billingPage.billingCcWithDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP),
                WebConstant.UNDER_18_DOB);

        Assert.assertTrue(accountBlockedPage.verifyPage(), "Account blocked page didn't load");
        accountBlockedPage.clickHelpCenter();
        sa.assertTrue(basePage.collectNewTabLandingPageURL().contains(PageUrl.DISNEYPLUS_HELP_CENTER),
                "Help center page did not render");
        accountBlockedPage.clickDismissButton();
        sa.assertTrue(basePage.isDplusBaseLoginBtnPresent(SHORT_TIMEOUT));

        sa.assertAll();
    }

    @Test(description = "Standalone premium user is not eligible for basic promo purchase", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void standalonePremiumUserBasicPromoIneligible() throws JSONException, MalformedURLException, URISyntaxException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53292"));

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi disneyAccountApi = getAccountApi();
        DisneyAccount account = disneyAccountApi.createAccount(STANDALONE_MONTHLY_NO_ADS, locale, language, SUB_VERSION_V2_ORDER);
        disneyAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        plansPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        plansPage.clickStandaloneToggleButton();
        Assert.assertTrue(plansPage.isStandalonePlanCardVisible(), "Page not toggled to Standalone plan cards");

        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS)
                .verifyUrlText(sa, PageUrl.SIGNUP);

        loginPage.signUpWithCreds(disneyAccount);
        sa.assertTrue(userPage.isNoPromoButtonPresent(10), "No Promotion button is not visible");
        userPage.verifyUrlText(sa, PageUrl.CAN_NOT_APPLY_PROMOTION);
        userPage.clickNoPromotionButton();
        commercePage.assertSuccessWithoutOverlay(sa, commercePage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Bundle sash user is not eligible for basic promo purchase", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void bundleSashUserBasicPromoIneligible() throws JSONException, MalformedURLException, URISyntaxException {

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53292"));

        SoftAssert sa = new SoftAssert();

        DisneyAccountApi disneyAccountApi = getAccountApi();
        DisneyAccount account = disneyAccountApi.createAccount(BUNDLE_BASIC, locale, language, SUB_VERSION_V2_ORDER);
        disneyAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        plansPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        plansPage.clickStandaloneToggleButton();
        Assert.assertTrue(plansPage.isStandalonePlanCardVisible(), "Page not toggled to Standalone plan cards");

        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS)
                .verifyUrlText(sa, PageUrl.SIGNUP);

        loginPage.signUpWithCreds(disneyAccount);
        sa.assertTrue(userPage.isNoPromoButtonPresent(10), "No Promotion button is not visible");
        userPage.verifyUrlText(sa, PageUrl.CAN_NOT_APPLY_PROMOTION);
        userPage.clickNoPromotionButton();
        userPage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        sa.assertAll();
    }

    @Test(description = "Pending cancel user - Ineligible for promo purchase", groups = { "US", TestGroup.DISNEY_COMMERCE,
                    TestGroup.ARIEL_COMMERCE, TestGroup.ARIEL_DOB_COMMERCE })
    public void pendingCancelIneligiblePromo() throws JSONException, IOException, URISyntaxException {
       
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53290"));

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(BUNDLE_PREMIUM, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        pause(HALF_TIMEOUT);

        accountApi.getSubscriptions(account);
        accountApi.cancelSubscription(account, account.getSubscriptions().get(0).getSubscriptionId());

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        plansPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        plansPage.openURL(plansPage.getHomeUrl().concat(PageUrl.PLANS));
        Assert.assertTrue(plansPage.verifyPage(), "Plans page did not load");

        plansPage.clickStandaloneToggleButton();
        Assert.assertTrue(plansPage.isStandalonePlanCardVisible(), "Page not toggled to Standalone plan cards");

        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS)
                .verifyUrlText(sa, PageUrl.SIGNUP);

        loginPage.signUpWithCreds(disneyAccount);
        sa.assertTrue(userPage.isNoPromoButtonPresent(10), "No Promotion button is not visible");
        userPage.verifyUrlText(sa, PageUrl.CAN_NOT_APPLY_PROMOTION);
        userPage.clickNoPromotionButton();
        userPage.assertSuccessWithoutOverlay(sa, userPage.isAccountDropdownIdPresent(), isMobile());

        accountPage.clickOnAccountDropdown(isMobile());
        sa.assertTrue(accountPage.isRestartSubscriptionBannerPresent(), "Restart subscription banner is not present");

        sa.assertAll();
        }
}
