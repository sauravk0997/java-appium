package com.disney.qa.tests.disney.web.commerce.account;


import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.EntitlementHelper;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.DisneyGlobalUtils;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceHorizontalStackingTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    private static final ThreadLocal<DisneyPlusRedemptionPage> redemptionPage = new ThreadLocal<>();

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        redemptionPage.set(new DisneyPlusRedemptionPage(getDriver()));
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Monthly Subscription with Provider - Credit Card - Paused Scenario", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void monthlyPaused() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().entitlements(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getHorizontalStackedPausedEntitlements(locale)).country(locale).build();
        DisneyAccount account = accountApi.createAccount(request);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.isExternalSubscriptionPresent();
        accountPage.clickInternalD2CSubscriptionCtaContains();

        sa.assertTrue(accountPage.isCrossedOutLabelPresent(), "Crossed out label not present");
        sa.assertTrue(accountPage.isPausedLabelPresent(), "Paused label not Present");
        sa.assertTrue(accountPage.isPausedSubscriptionPresent(), "Paused Subscription not present");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription with Mulitiple Providers - Credit Card - Paused Scenario", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void monthlyMultiplePaused() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().entitlements(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getHorizontalStackedPausedEntitlements(locale)).country(locale).build();
        DisneyAccount account = accountApi.createAccount(request);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        commercePage.getModalPrimaryBtn().clickIfPresent(SHORT_TIMEOUT);

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.isExternalSubscriptionPresent();
        accountPage.clickInternalD2CSubscriptionCtaContains();

        sa.assertTrue(accountPage.isCrossedOutLabelPresent(), "Crossed out label not present");
        sa.assertTrue(accountPage.isPausedLabelPresent(), "Paused label not Present");
        sa.assertTrue(accountPage.isPausedSubscriptionPresent(), "Paused Subscription not present");

        sa.assertAll();
    }

    @Test(description = "Monthly Subscription with Provider - Credit Card - Unpaused Scenario", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void monthlyUnpaused() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().entitlements(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getHorizontalStackedPausedEntitlements(locale)).country(locale).build();
        DisneyAccount account = accountApi.createAccount(request);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);
        accountApi.revokeSku(account, request.getEntitlements().get(1).getOffer());

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        sa.assertTrue(accountPage.isUnpausedSubscriptionPresent(), "Unpaused data-testid not present");

        sa.assertAll();
    }

    @Test(description = "Pending Cancel Annual Subscription with Provider - Credit Card - Extended Date", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void annualCancelExtendedDate() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().entitlements(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getHorizontalStackedExtendedDateEntitlements(locale)).country(locale).build();
        DisneyAccount account = accountApi.createAccount(request);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);
        accountApi.revokeSku(account, request.getEntitlements().get(0).getOffer());

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        commercePage.getModalPrimaryBtn().clickIfPresent(SHORT_TIMEOUT);

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        sa.assertTrue(accountPage.isExternalSubscriptionPresent(), "External Subscription not Present");

        accountPage.clickInternalD2CSubscriptionCtaContains();

        sa.assertTrue(accountPage.isPausedLabelPresent(), "Paused label not present");

        sa.assertAll();
    }
}
