package com.disney.qa.tests.disney.web.commerce.onboarding;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceOnboardingTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Docomo Onboarding User", groups = {"JP"})
    public void docomoOnboarding() throws URISyntaxException, IOException, JSONException, InterruptedException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi api = getAccountApi();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).language(language).isStarOnboarded(false).build();
        DisneyOffer offer = api.lookupOfferToUse(locale, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(offer).subVersion("V2-ORDER").build();
        request.addEntitlement(entitlement);
        disneyAccount.set(api.createAccount(request));

        //Patch Object
        JSONObject identityObject = new JSONObject();
        JSONObject onboardingObject = new JSONObject();
        JSONArray legalAssertionsArray = new JSONArray();
        legalAssertionsArray.put("gtou_dplus-sub-jp_ppv2_proxy");
        onboardingObject.put("eligibleForOnboarding", true);

        identityObject.put("marketingPreferences", onboardingObject);
        identityObject.put("legalAssertions", legalAssertionsArray);

        // patch call
        api.patchIDPIdentity(disneyAccount.get(), identityObject);
        sa.assertTrue(api.getAccountIdentityBody(disneyAccount.get()).getAttributes().getMarketingPreferences().getEligibleForOnboarding(), "eligibleForOnboarding was not true");
        setOverride(api.overrideLocations(disneyAccount.get(), locale));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        sa.assertTrue(commercePage.isDocomoContinueButton(), "Docomo button is not present");
        commercePage.assertUrlContains(sa, "getupdates");

        commercePage.clickDocomoContinueButton();

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), false, false);

        sa.assertAll();
    }

    @Test(description = "Error SignUp Email - OptIn Checkbox Not Checked", groups = {"KR"})
    public void purchasCheckBoxNotChecked() throws URISyntaxException, IOException, JSONException, InterruptedException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.handleStandaloneCtasOnOlp();

        String newEmail = DisneyApiCommon.getUniqueEmail();
        userPage.typeDplusBaseEmailFieldId(newEmail);

        //Both checkboxes are unchecked
        userPage.clickSignUpLoginContinueBtn();
        sa.assertTrue(userPage.isOptInErrorMessagePresent(), "Opt in error message not shown");

        //Check first checkbox, error message should remain
        userPage.clickCheckBoxOptIn();
        sa.assertTrue(userPage.isOptInErrorMessagePresent(), "Opt in error message not shown");

        //Uncheck first checkbox and check second checkbox, error to remain
        userPage.clickCheckBoxOptIn();
        userPage.clickCheckBoxThirdPartyProxy();
        sa.assertTrue(userPage.isOptInErrorMessagePresent(), "Opt in error message not shown");

        sa.assertAll();
    }
}
