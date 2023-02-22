package com.disney.qa.tests.disney.web.commerce.planswitch;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.commerce.*;
import com.disney.qa.disney.web.commerce.modal.DisneyPlusPlanSwitchModal;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.disney.web.entities.PlanCardTypes.PlanCancelSwitchCard;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.proxy.MitmProxy;
import com.proxy.MitmProxyConfiguration;
import com.proxy.MitmProxyPool;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommercePlanSwitchTest extends DisneyPlusBaseTest {

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

    @Test(description = "Bundle No Ads User to Standalone Ads monthly", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void bundleNoAdsToStandaloneAds() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52646"));

        DisneyAccountApi disneyAccountApi = getAccountApi();
        DisneyAccount account = disneyAccountApi.createAccount(BUNDLE_PREMIUM, locale, language, SUB_VERSION_V2_ORDER);
        disneyAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.goToSwitchPlanFromSubscription(isMobile());

        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");

        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC), "Trio basic card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_BASIC), "D+ basic card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM), "D+ premium card is not visible");

        sa.assertFalse(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY), "Annual card is visible");
        changeSubscriptionPage.clickExpandAnnualPlans();
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY), "D+ premium annual card is not visible");

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_BASIC);

        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());
        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");

        sa.assertTrue(changeSubscriptionConfirmPage.getPlanTitle().contains(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_BASIC.getPlanTitle()),
                "Plan card title is different");
        sa.assertTrue(changeSubscriptionConfirmPage.getPlanPrice().contains(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_BASIC.getPlanPrice()),
                "Plan card price is different");

        sa.assertTrue(changeSubscriptionConfirmPage.getTotalDueToday().contains("$0.00"), "Total due amount is not $0.00");
        sa.assertFalse(changeSubscriptionConfirmPage.isCVVFieldVisible(), "CVV field is visible");

        changeSubscriptionConfirmPage.clickSubmitButton();

        waitForSeconds(30); //Plan switching is very slow.

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account", isMobile());

        sa.assertTrue(accountPage.isDowngradePendingChangePromptVisible(), "Pending change prompt is not visible");

        sa.assertAll();
    }

    @Test(description = "D+ No Ads Monthly User to Bundle Ads - Change CC", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void standaloneNoAdsToBundleAdsChangeCC() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        WebDriver driver = getDriver();
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52627"));

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(driver);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_NO_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        Assert.assertTrue(plansPage.verifyPage(), "Plan select page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);

        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale, false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), false, false);

        accountPage.goToSwitchPlanFromSubscription(isMobile());

        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");

        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM), "D+ trio premium card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC), "D+ trio basic card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_BASIC), "D+ basic card is not visible");

        sa.assertFalse(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY), "Annual card is visible");
        changeSubscriptionPage.clickExpandAnnualPlans();
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY), "D+ premium annual card is not visible");

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC);

        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");

        sa.assertTrue(changeSubscriptionConfirmPage.getPlanTitle().contains(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC.getPlanTitle()),
        "Plan card title is different");
        sa.assertTrue(changeSubscriptionConfirmPage.getPlanPrice().contains(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC.getPlanPrice()),
        "Plan card price is different");

        sa.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");
        sa.assertFalse(changeSubscriptionConfirmPage.getTotalDueToday().contains("$0.00"), "Total due amount is $0.00");

        changeSubscriptionConfirmPage.clickChangeCC();

        billingPage.verifyPage();
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");
        changeSubscriptionConfirmPage.enterCVV(countryData.searchAndReturnCountryData(locale, CODE, CVV));
        changeSubscriptionConfirmPage.clickSubmitButton();

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        commercePage.clickBundleSuccessContinueButton();
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "D+ No Ads Monthly User to D+ Ads from the cancel flow", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void standaloneNoAdsToStandaloneAdsFromCancel() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        WebDriver driver = getDriver();
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52532", "XWEBQAP-52752"));

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);
        DisneyPlusCancelSwitchPage cancelSwitchPage = new DisneyPlusCancelSwitchPage(driver);
        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(driver);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_NO_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        Assert.assertTrue(plansPage.verifyPage(), "Plan select page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_NO_ADS);

        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale, false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), false, false);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCta();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription Details page didn't load");
        subscriptionDetailsPage.clickSubscriptionDetailsCancelSubscriptionButton();

        Assert.assertTrue(cancelSwitchPage.verifyPage(), "Cancel Switch page didn't load");

        sa.assertTrue(cancelSwitchPage.getPlanTitle(PlanCancelSwitchCard.DISNEY_PLUS_BASIC)
                .equals(PlanCancelSwitchCard.DISNEY_PLUS_BASIC.getPlanTitle()), "Plan title didn't match");
        sa.assertTrue(cancelSwitchPage.getPlanDescription().size() == 2, "Plan card description count is wrong");
        sa.assertEquals(cancelSwitchPage.getPlanDescription(), PlanCancelSwitchCard.DISNEY_PLUS_BASIC.getCancelSwitchDescription(),
                "Plan descriptions do no match");
        sa.assertTrue(cancelSwitchPage.getPlanPrice().equals(PlanCancelSwitchCard.DISNEY_PLUS_BASIC.getPlanPrice()), "Plan price is wrong");

        cancelSwitchPage.clickSwitchPlanButton();

        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");
        sa.assertTrue(changeSubscriptionConfirmPage.getTotalDueToday().contains("$0.00"), "Total due amount is not $0.00");
        changeSubscriptionConfirmPage.clickSubmitButton();

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account", isMobile());
        sa.assertTrue(accountPage.isDowngradePendingChangePromptVisible(), "Pending change prompt is not visible");

        sa.assertAll();
    }

    @Test(description = "Bundle Ads User to Standalone No Ads Paypal", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void bundleAdsToStandaloneNoAdsPaypal() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52643"));

        SoftAssert sa = new SoftAssert();
        WebDriver driver = getDriver();
        boolean isBundle = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(driver);
        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_BUNDLE_SASH));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        billingPage.billingWithPaypal();

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.goToSwitchPlanFromSubscription(isMobile());

        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM), "D+ trio premium card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM), "D+ premium card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_BASIC), "D+ basic card is not visible");

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM);

        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");

        sa.assertTrue(changeSubscriptionConfirmPage.getPlanTitle().contains(PlanCardTypes.PlanCancelSwitchCard.DISNEY_PLUS_PREMIUM.getPlanTitle()),
                "Plan card title is different");
        sa.assertTrue(changeSubscriptionConfirmPage.getPlanPrice().contains(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM.getPlanPrice()),
                "Plan card price is different");

        sa.assertFalse(changeSubscriptionConfirmPage.isCVVFieldVisible(), "CVV is shown on Paypal Screen");
        sa.assertFalse(changeSubscriptionConfirmPage.getTotalDueToday().contains("$0.00"), "Total due amount is $0.00");

        changeSubscriptionConfirmPage.clickSubmitButton();
        waitForSeconds(EXPLICIT_TIMEOUT);

        commercePage.assertSuccessWithoutOverlay(sa, accountPage.isUpgradeToAnnualCardNotPresent(), "account", isMobile());

        sa.assertTrue(accountPage.isRestartSubscriptionBannerPresent(), "Restart subscription banner is not present");
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Standalone Ads User to Bundle No Ads Paypal", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void standAloneAdsToBundleNoAdsPaypal() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52632"));

        SoftAssert sa = new SoftAssert();
        WebDriver driver = getDriver();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(driver);
        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(driver);
        Assert.assertTrue(plansPage.verifyPage(), "Plan select page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS);

        billingPage.billingWithPaypal();

        commercePage.assertPlansPurchase(sa, locale, isMobile(), isBundle);

        accountPage.goToSwitchPlanFromSubscription(isMobile());

        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM), "D+ trio premium card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC), "D+ trio  basic card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM), "D+ premium card is not visible");

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM);

        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");

        sa.assertTrue(changeSubscriptionConfirmPage.getPlanTitle().contains(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM.getPlanTitle()),
                "Plan card title is different");
        sa.assertTrue(changeSubscriptionConfirmPage.getPlanPrice().contains(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM.getPlanPrice()),
                "Plan card price is different");

        sa.assertFalse(changeSubscriptionConfirmPage.isCVVFieldVisible(), "CVV is shown on Paypal Screen");
        sa.assertFalse(changeSubscriptionConfirmPage.getTotalDueToday().contains("$0.00"), "Total due amount is $0.00");

        changeSubscriptionConfirmPage.clickSubmitButton();
        waitForSeconds(EXPLICIT_TIMEOUT);

        commercePage.assertBundleUpgradePurchaseSuccess(sa);

        commercePage.clickBundleSuccessContinueButton();
        accountPage.clickInternalD2CSubscriptionCtaContains();

        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);
        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");

        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPrice()),
                "Plan price does not match purchase price");

        sa.assertAll();
    }

    @Test(description = "Verify all links on plan switch L3 page", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void verifyLinksOnL3ChangeSwitchPage() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53016"));

        DisneyAccountApi disneyAccountApi = getAccountApi();
        DisneyAccount account = disneyAccountApi.createAccount(BUNDLE_PREMIUM, locale, language, SUB_VERSION_V2_ORDER);
        disneyAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        DisneyPlusPlanSwitchModal changeSubscriptionModal = new DisneyPlusPlanSwitchModal(getDriver());

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        accountPage.goToSwitchPlanFromSubscription(isMobile());
        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");

        changeSubscriptionPage.clickBundleTermsLink();
        sa.assertFalse(changeSubscriptionPage.collectBundleTermsPopupContent().isEmpty(), "The expected content is empty");

        changeSubscriptionPage.clickHelpCenterLinkInBundleTermsPopup();
        sa.assertTrue(changeSubscriptionPage.collectNewTabLandingPageURL().contains(PageUrl.DISNEY_PLUS_HELP_CENTER), "Disney Help center url didn't load");
        changeSubscriptionModal.clickModalCloseButton();

        changeSubscriptionPage.clickAFewShowsThatPlayAdsLink();
        sa.assertFalse(changeSubscriptionPage.collectAFewShowsThatPlayAdsPopupContent().isEmpty(), "There is no content displayed");

        changeSubscriptionPage.clickHuluHelpCenterLink();
        sa.assertTrue(changeSubscriptionPage.collectNewTabLandingPageURL().contains(PageUrl.HULU_HELP_CENTER_NO_ADS_EXCEPTIONS), "Hulu help center link didn't load");
        changeSubscriptionModal.clickModalCloseButton();

        sa.assertAll();
    }

    @Test(description = "Verify all links on confirm L4 page", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void verifyLinksOnL4ChangeSwitchPage() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-53017"));

        DisneyAccountApi disneyAccountApi = getAccountApi();
        DisneyAccount account = disneyAccountApi.createAccount(BUNDLE_PREMIUM, locale, language, SUB_VERSION_V2_ORDER);
        disneyAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        DisneyPlusPlanSwitchModal changeSubscriptionModal = new DisneyPlusPlanSwitchModal(getDriver());
        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        accountPage.goToSwitchPlanFromSubscription(isMobile());
        changeSubscriptionPage
                .clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_BASIC)
                .waitForPageToFinishLoading();
        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");

        changeSubscriptionConfirmPage.clickLearnMoreLink();
        sa.assertFalse(changeSubscriptionConfirmPage.collectLearnMoreDialogContent().isEmpty(), "The lean more does not have content");
        changeSubscriptionModal.clickModalCloseButton();

        changeSubscriptionConfirmPage.clickShowOrHideSummaryOfChangesLink();
        sa.assertTrue(changeSubscriptionConfirmPage.collectChangeSubscriptionConfirmSummaryContent().contains("Hide summary of changes"), "The show summary of changes does not display right info ");

        changeSubscriptionConfirmPage.clickShowOrHideSummaryOfChangesLink();
        sa.assertTrue(changeSubscriptionConfirmPage.collectChangeSubscriptionConfirmSummaryContent().contains("Show summary of changes"), "The hide summary of changes does not display right info ");

        changeSubscriptionConfirmPage.clickSubscriberAgreementLink();
        sa.assertTrue(changeSubscriptionConfirmPage.collectNewTabLandingPageURL().contains(PageUrl.DISNEY_PLUS_LEGAL_SUBSCRIBER_AGREEMENT), "Subscriber agreement link didn't load correctly");

        sa.assertAll();
    }

    @Test(description = "Standalone Ads monthly to annual", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void verifyStandaloneMonthlyToStandaloneAnnual() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);
        
        SoftAssert sa = new SoftAssert();
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52632"));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        DisneyPlusChangeSubscriptionPage changeSubscriptionPage = new DisneyPlusChangeSubscriptionPage(getDriver());
        DisneyPlusChangeSubscriptionConfirmPage changeSubscriptionConfirmPage = new DisneyPlusChangeSubscriptionConfirmPage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(getDriver());
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(getDriver());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);
        commercePage.clickSignUpNowBtn();

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        Assert.assertTrue(plansPage.verifyPage(), "Plan select page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS);


        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale, false);

        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, ZIP));

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), false, false);

        accountPage.goToSwitchPlanFromSubscription(isMobile());

        Assert.assertTrue(changeSubscriptionPage.verifyPage(), "Change subscription page didn't load");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_PREMIUM), "D+ trio premium card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_BUNDLE_TRIO_BASIC), "D+ trio  basic card is not visible");
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM), "D+ premium card is not visible");

        sa.assertFalse(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY), "Annual card is visible");
        changeSubscriptionPage.clickExpandAnnualPlans();
        sa.assertTrue(changeSubscriptionPage.isPlanCardButtonVisible(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY), "Annual card is not visible");

        changeSubscriptionPage.clickPlanCardButton(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY)
                .waitForPageToFinishLoading();

        Assert.assertTrue(changeSubscriptionConfirmPage.verifyPage(), "Change subscription confirm page didn't load");

        sa.assertTrue(changeSubscriptionConfirmPage.getPlanTitle().contains(PlanCardTypes.PlanCancelSwitchCard.DISNEY_PLUS_PREMIUM.getPlanTitle()),
        "Plan card title is different");
        sa.assertTrue(changeSubscriptionConfirmPage.getPlanPrice().contains(PlanCardTypes.PlanSwitchCard.DISNEY_PLUS_PREMIUM_YEARLY.getPlanPrice()),
        "Plan card price is different");

        changeSubscriptionConfirmPage.handleStoredPayment(countryData.searchAndReturnCountryData(locale, CODE, CVV)).waitForPageToFinishLoading();

        waitForSeconds(EXPLICIT_TIMEOUT);
        sa.assertTrue(accountPage.isDisneyPlusAnnualPlanDisplayed(), "Expecting Disney+ annual plan is displayed");
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");

        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_PLUS_PREMIUM_YEARLY.getPlanName()),
                "The purchase plan is different");
        sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(PlanCardTypes.SubscriptionPlan.DISNEY_PLUS_PREMIUM_YEARLY.getPrice()),
                "The plan price does not match purchase price");

        sa.assertAll();
    }
}
