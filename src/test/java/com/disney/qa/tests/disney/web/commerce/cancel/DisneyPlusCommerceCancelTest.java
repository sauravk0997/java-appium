package com.disney.qa.tests.disney.web.commerce.cancel;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusLoginPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.commerce.*;
import com.disney.qa.disney.web.commerce.modal.DisneyPlusSubscriptionRestartModal;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.disney.web.entities.PlanCardTypes.PlanCancelSwitchCard;
import com.disney.qa.disney.web.entities.PlanCardTypes.SubscriptionPlan;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class DisneyPlusCommerceCancelTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @BeforeMethod(alwaysRun = true)
    public void setupTest() throws InterruptedException{
        disneyAccount.set(new DisneyAccount());
        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        MitmProxyConfiguration configuration = MitmProxyConfiguration.builder()
                .listenPort(getProxyPort())
                .headers(disneyPlusBasePage.getHeaders(locale))
                .build();
        MitmProxy.startProxy(configuration);
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
        MitmProxyPool.tearDownProxy();
    }

    private void billingWithCreditCard(DisneyPlusBillingPage billingPage, String zipCode) {
        String creditCard = billingPage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(ENVIRONMENT), locale, false);

        Assert.assertTrue(billingPage.verifyPage(), "Billing page didn't load");
        billingPage.billingCcWithoutDob(billingPage.getCreditCardName(locale), creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                countryData.searchAndReturnCountryData(locale, CODE, zipCode));
    }

    @Test(description = "One Step Cancel & Restart - Standalone Monthly with Ads", groups = { "US",
            TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void oneStepCancelRestartStandaloneAds() throws URISyntaxException, IOException, JSONException, NoSuchAlgorithmException {
        skipTestByEnv(QA);
        
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52905", "XWEBQAP-52753",
                "XWEBQAP-52537", "XWEBQAP-52935"));

        SoftAssert sa = new SoftAssert();
        WebDriver driver = getDriver();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        PlanCancelSwitchCard cancelSwitchCard = PlanCancelSwitchCard.DISNEY_PLUS_PREMIUM;

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCancelSwitchPage cancelSwitchPage = new DisneyPlusCancelSwitchPage(driver);
        DisneyPlusCancelSuccessPage cancelSuccessPage = new DisneyPlusCancelSuccessPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_STANDALONE_ADS));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        commercePage.clickSignUpNowBtn();

        DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
        Assert.assertTrue(plansPage.verifyPage(), "Plan select page didn't load");
        plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS);

        billingWithCreditCard(billingPage, ZIP_CA);

        commercePage.assertPlansPurchase(sa, locale, isMobile(), false);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CStandaloneWithAdsSubscriptionCta();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription Details page didn't load");
        subscriptionDetailsPage.clickSubscriptionDetailsCancelSubscriptionButton();

        Assert.assertTrue(cancelSwitchPage.verifyPage(), "Cancel Switch page didn't load");
        sa.assertTrue(cancelSwitchPage.getPlanTitle(cancelSwitchCard).equals(cancelSwitchCard.getPlanTitle()), "Plan title do no match");
        sa.assertEquals(cancelSwitchPage.getPlanDescription(), cancelSwitchCard.getCancelSwitchDescription(),
                "Plan descriptions do no match");
        sa.assertTrue(cancelSwitchPage.getPlanPrice().equals(cancelSwitchCard.getPlanPrice()), "Plan price do no match");
        cancelSwitchPage
                .clickCancelSubscriptionButton()
                .waitForPageToFinishLoading();

        Assert.assertTrue(cancelSuccessPage.verifyPage(), "Cancel Success page didn't load");
        cancelSuccessPage.selectRandomSurveyOption();
        cancelSuccessPage.clickSubmitButton();

        sa.assertTrue(cancelSuccessPage.isThanksForFeedbackLabelVisible(), "Thanks for feedback label not visible");

         accountPage.openURL(accountPage.getHomeUrl().concat("/account"), EXPLICIT_TIMEOUT);
         Assert.assertTrue(accountPage.verifyPage(), "Account page didn't load");

         sa.assertTrue(accountPage.isRestartSubscriptionBannerPresent(), "Restart subscription banner is not present");
         sa.assertTrue(accountPage.getAccountPageSubscriptionDetails().contains(WebConstant.ACCOUNT_PAGE_CANCEL_COPY), "Subscription is not cancelled");

         accountPage.clickInternalD2CSubscriptionCtaContains();
         Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription Details Page didn't load");
         sa.assertTrue(subscriptionDetailsPage.isRestartSubscriptionButtonVisible(), " Restart Subscription Button is not visible");

         subscriptionDetailsPage.clickBackToAccountLink();
         Assert.assertTrue(accountPage.verifyPage(), "Account page didn't load");

         accountPage.clickRestartSubscriptionButton();
         accountPage.clickRestartModalButton();

         pause(15); //Restart Subscription takes some time

         sa.assertTrue(accountPage.isInternalD2CStandaloneWithAdsCtaVisible(), "Disney+ Basic (Monthly) subscription is not visible");
         
         sa.assertAll();
    }

    @Test(description = "Multi-Step Cancel - Keep Subscription - Bundle Monthly with Ads", groups = { "US",
            TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void multiStepCancelBundleAds() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52534", "XWEBQAP-52750"));

        SoftAssert sa = new SoftAssert();
        WebDriver driver = getDriver();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        PlanCancelSwitchCard cancelSwitchCard = PlanCancelSwitchCard.DISNEY_PLUS_BASIC;

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCancelSwitchPage cancelSwitchPage = new DisneyPlusCancelSwitchPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_BUNDLE_SASH));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        billingWithCreditCard(billingPage, ZIP);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), true, false);

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalBundleMonthlyD2CSubscriptionCta();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription Details page didn't load");
        subscriptionDetailsPage.clickSubscriptionDetailsCancelSubscriptionButton();

        Assert.assertTrue(cancelSwitchPage.verifyPage(), "Cancel Switch page didn't load");
        sa.assertTrue(cancelSwitchPage.getPlanTitle(cancelSwitchCard).equals(cancelSwitchCard.getPlanTitle()), "Plan title do no match");
        sa.assertEquals(cancelSwitchPage.getPlanDescription(), cancelSwitchCard.getCancelSwitchDescription(),
                "Plan descriptions do no match");
        sa.assertTrue(cancelSwitchPage.getPlanPrice().equals(cancelSwitchCard.getPlanPrice()), "Plan price do no match");

        cancelSwitchPage.clickKeepSubscriptionButton();
        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription details page didn't load");

         sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPlanName()),
         "The purchase plan is different");
         sa.assertTrue(subscriptionDetailsPage.getSubscriptionDetailsText().contains(SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPrice()),
         "Plan price does not match purchase price");
         
        sa.assertAll();
    }

    @Test(description = "Multi-Step Cancel & Restart - Bundle Monthly with No Ads", groups = { "US",
            TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE })
    public void multiStepCancelRestartBundleNoAds() throws URISyntaxException, IOException, JSONException, NoSuchAlgorithmException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52535", "XWEBQAP-52748",
                "XWEBQAP-52537", "XWEBQAP-52935"));

        SoftAssert sa = new SoftAssert();
        WebDriver driver = getDriver();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        PlanCancelSwitchCard cancelSwitchCard = PlanCancelSwitchCard.DISNEY_BUNDLE_TRIO_BASIC;

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(driver);
        DisneyPlusCancelRecommendationPage cancelRecommendationPage = new DisneyPlusCancelRecommendationPage(driver);
        DisneyPlusCancelSwitchPage cancelSwitchPage = new DisneyPlusCancelSwitchPage(driver);
        DisneyPlusCancelSuccessPage cancelSuccessPage = new DisneyPlusCancelSuccessPage(driver);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusLoginPage loginPage = new DisneyPlusLoginPage(driver);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(driver);
        DisneyPlusSubscriptionRestartModal subscriptionRestartModal = new DisneyPlusSubscriptionRestartModal(driver);

        userPage.setIgnoreCookie(true);
        userPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        setOverride(userPage.redirectToSignUpQsp(PageUrl.SIGNUP_TYPE_BUNDLE_NOAH));
        AccountUtils.createAccountViaApi(locale, disneyAccount, getAccountApi());

        loginPage.signUpWithCreds(disneyAccount);

        billingWithCreditCard(billingPage, ZIP);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), true, false);
      
        accountPage.clickOnAccountDropdown(isMobile());
        commercePage.waitForPageToFinishLoading();
        accountPage.clickInternalD2CBundleMonthlyNoAdsSubscriptionCta();

        Assert.assertTrue(subscriptionDetailsPage.verifyPage(), "Subscription Details page didn't load");
        subscriptionDetailsPage.clickSubscriptionDetailsCancelSubscriptionButton();

        Assert.assertTrue(cancelSwitchPage.verifyPage(), "Cancel Switch page didn't load");
        sa.assertTrue(cancelSwitchPage.getPlanTitle(cancelSwitchCard).equals(cancelSwitchCard.getPlanTitle()), "Plan title do no match");
        sa.assertEquals(cancelSwitchPage.getPlanDescription(), cancelSwitchCard.getCancelSwitchDescription(),
                "Plan descriptions do no match");

        Assert.assertTrue(cancelSwitchPage.getPlanPrice().equals(cancelSwitchCard.getPlanPrice()), "Plan price do no match");
        cancelSwitchPage.clickContinueToCancelButton();

        Assert.assertTrue(cancelRecommendationPage.verifyPage(), "Cancel Recommendation page didn't load");
        sa.assertTrue(cancelRecommendationPage.isRecommendationLinkVisible(), "There are no recommendation links shown on the Cancel page");
        cancelRecommendationPage.clickCancelSubscriptionButton();

        Assert.assertTrue(cancelSuccessPage.verifyPage(), "Cancel Success page didn't load");
        cancelSuccessPage.selectRandomSurveyOption();
        cancelSuccessPage.clickSubmitButton();

        sa.assertTrue(cancelSuccessPage.isThanksForFeedbackLabelVisible(),
                "Thanks for feedback label not visible");

        cancelSuccessPage.clickRestartSubscriptionButton();
        Assert.assertTrue(subscriptionRestartModal.verifyModal(), "Subscription Restart Modal did not load");

        subscriptionRestartModal.clickModalPrimaryButton();
        Assert.assertTrue(accountPage.verifyPage(), "Account page didn't load");

        sa.assertTrue(accountPage.isInternalD2CBundleMonthlyNoAdsCtaVisible(), "Disney+ Bundle (monthly) with no ads subscription is not visible");

        sa.assertAll();
    }

    @Test(description = "Verify plan switch doesn't show for D+ annual user in cancel flow", groups = {"US", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void VerifyPlanSwitchNotShownOnAnnualUserInCancelFlow() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52932"));

        WebDriver driver = getDriver();
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi disneyAccountApi = getAccountApi();
        DisneyAccount account = disneyAccountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        disneyAccountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(driver);
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(driver);
        DisneyPlusSubscriptionDetailsPage subscriptionDetailPage = new DisneyPlusSubscriptionDetailsPage(driver);
        DisneyPlusCancelRecommendationPage cancelRecommendationPage = new DisneyPlusCancelRecommendationPage(driver);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        Assert.assertTrue(subscriptionDetailPage.verifyPage(), "Subscription Details page didn't load");
        subscriptionDetailPage
                .clickSubscriptionDetailsCancelSubscriptionButton()
                .waitForPageToFinishLoading();

        Assert.assertTrue(cancelRecommendationPage.verifyPage(), "Cancel Recommendation page didn't load");
        sa.assertTrue(cancelRecommendationPage.isRecommendationLinkVisible(), "Recommendation link is not visible");

        String href = cancelRecommendationPage.getRecommendationLinkHref();
        String detailsPageAltText = cancelRecommendationPage.getRecommendationLinkAltText().split("\\w*")[0].toLowerCase();

        sa.assertTrue(href.contains(detailsPageAltText), "Href doesn't contain details page alt text");

        cancelRecommendationPage.clickRecommendationLink();
        commercePage.verifyUrlText(sa, href);

        sa.assertAll();
    }
}
