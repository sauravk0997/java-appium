package com.disney.qa.tests.disney.web.commerce.crossProductSignup;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.responses.account.DisneyFullAccount;
import com.disney.qa.api.client.responses.identity.DisneyIdentity;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceCrossProductSignupTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    private String localeToUse = "MX";

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Existing Partner Account", groups = {"MX", "US", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void existingPartner() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52120"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);

        DisneyAccountApi accountApi = getPartnerAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, localeToUse, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, localeToUse);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.clickSubscriberAgreementContinueCta(localeToUse);

        redemptionPage.finishLoginWithPartnerAccount(disneyAccount.get().getUserPass(), localeToUse);

        if (DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            commercePage.clickCompletePurchaseMegaBuyNowBtn();
        } else {
        commercePage.clickCompleteAndRestartPurchaseBaseCta(localeToUse, "complete-purchase", isBundle);
        }
        commercePage.clickSubscriberAgreementContinueCtaIfPresent(localeToUse);

        sa.assertTrue(commercePage.isBillingOrUpgradePagePresent(), "Billing page not shown");

        sa.assertAll();
    }

    @Test(description = "Existing Partner Account With Security Flag", groups = {"MX", "US", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void existingPartnerWithFlag() throws URISyntaxException, IOException, JSONException, InterruptedException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52121"));

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);
    
        DisneyAccountApi accountApi = getPartnerAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, localeToUse, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, localeToUse);
        disneyAccount.set(account);

        // patch call
        JSONObject identityObject = new JSONObject();
        JSONObject attributeObject = new JSONObject();

        identityObject.put("securityFlagged", true);
        attributeObject.put("securityFlagged", true);

        accountApi.patchIDPIdentity(disneyAccount.get(), identityObject);
        pause(HALF_TIMEOUT);
        accountApi.patchAccount(disneyAccount.get(), attributeObject, PatchType.ACCOUNT);

        DisneyFullAccount fullAccount = accountApi.getAccountBody(disneyAccount.get());
        sa.assertTrue(fullAccount.getAttributes().getSecurityFlagged(),"Account Security Flagged was not true");

        // Verify Account Attribute
        DisneyIdentity identity = accountApi.getAccountIdentityBody(disneyAccount.get());
        sa.assertTrue(identity.getAttributes().getSecurityFlagged(), "Identity Security Flagged was not true");

        apiProvider.get().setPartner("default");
        setOverride(accountApi.overrideLocations(disneyAccount.get(), localeToUse));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail());

        sa.assertTrue(commercePage.isDplusBaseEmailFieldIdPresent(), "Email field not shown");

        sa.assertAll();
    }

    @Test(description = "Shared Payment Consent - No Thanks", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void sharedPaymentConsentNoThanks() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);
    
        DisneyAccountApi accountApi = getPartnerAccountApi();
        CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().country(localeToUse).language(language).build();
        createDisneyAccountRequest.addOrderSetting(DisneyOrder.SET_IS_NOT_SHARED);
        createDisneyAccountRequest.addOrderSetting(DisneyOrder.SET_IS_DEFAULT_PAYMENT_METHOD);
        DisneyOffer offer = accountApi.lookupOfferToUse(localeToUse, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(offer).subVersion("V2-ORDER").build();
        createDisneyAccountRequest.addEntitlement(entitlement);
        DisneyAccount account = accountApi.createAccount(createDisneyAccountRequest);
        accountApi.overrideLocations(account, localeToUse);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.clickSubscriberAgreementContinueCta(localeToUse);

        redemptionPage.finishLoginWithPartnerAccount(disneyAccount.get().getUserPass(), localeToUse);

        commercePage.clickCompleteAndRestartPurchaseBaseCta(localeToUse, "complete-purchase", isBundle);
        commercePage.clickSubscriberAgreementContinueCtaIfPresent(localeToUse);

        sa.assertTrue(commercePage.isBillingOrUpgradePagePresent(), "Billing page not shown");
        sa.assertTrue(commercePage.isModalPrimaryBtnPresent(), "Payment concent modal not shown");
        commercePage.clickModalSecondaryBtn();

        sa.assertTrue(redemptionPage.isBillingCardNameIdPresent(), "Billing name not shown");

        sa.assertAll();
    }

    @Test(description = "Shared Payment Consent - Consent", groups = {"MX", "US", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void sharedPaymentConsent() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52122"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);
       
        DisneyAccountApi accountApi = getPartnerAccountApi();
        CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().country(localeToUse).language(language).build();
        createDisneyAccountRequest.addOrderSetting(DisneyOrder.SET_IS_NOT_SHARED);
        createDisneyAccountRequest.addOrderSetting(DisneyOrder.SET_IS_DEFAULT_PAYMENT_METHOD);
        DisneyOffer offer = accountApi.lookupOfferToUse(localeToUse, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(offer).subVersion("V2-ORDER").build();
        createDisneyAccountRequest.addEntitlement(entitlement);
        DisneyAccount account = accountApi.createAccount(createDisneyAccountRequest);
        accountApi.overrideLocations(account, localeToUse);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.clickSubscriberAgreementContinueCta(localeToUse);

        redemptionPage.finishLoginWithPartnerAccount(disneyAccount.get().getUserPass(), localeToUse);

        if (DisneyPlusBasePage.DPLUS_SPECIAL_OFFER_ENABLED) {
            commercePage.clickCompletePurchaseMegaBuyNowBtn();
        } else {
        commercePage.clickCompleteAndRestartPurchaseBaseCta(localeToUse, "complete-purchase", isBundle);
        }
        commercePage.clickSubscriberAgreementContinueCtaIfPresent(localeToUse);

        sa.assertTrue(commercePage.isBillingOrUpgradePagePresent(), "Billing page not shown");
        sa.assertTrue(commercePage.isModalPrimaryBtnPresent(), "Payment concent modal not shown");

        commercePage.clickModalPrimaryBtn();

        sa.assertTrue(commercePage.isChangePaymentCtaPresent(), "Change payment not shown");
        sa.assertTrue(commercePage.isStoredPaymentSubmitBtnPresent(), "Stored payment not shown");

        sa.assertAll();
    }

    @Test(description = "Existing Combo+ Account", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void existingComboUser() throws URISyntaxException, IOException, JSONException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52123"));

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(localeToUse, CODE, COUNTRY);

        DisneyAccountApi accountApi = getPartnerAccountApi();
        DisneyAccount account = accountApi.createAccount(COMBO, localeToUse, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, localeToUse);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.startLoginWithPartnerAccount(disneyAccount.get().getEmail());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.clickSubscriberAgreementContinueCta(localeToUse);

        redemptionPage.finishLoginWithPartnerAccount(disneyAccount.get().getUserPass(), localeToUse);
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, true, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(accountPage.isExternalBamtechSubscriptionPresent(), "External Platform Subscription not present");

        sa.assertAll();
    }

}
