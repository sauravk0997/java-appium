package com.disney.qa.tests.disney.web.commerce.portability;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAddProfilePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusConsentCollectionPage;
import com.disney.qa.disney.web.commerce.DisneyPlusPortabilityPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommercePortabilityTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    // Currently this test is extremely flaky and since we are skipping Live testing, the test should not run at the moment
    @Test(description = "EU Login (Non Launched EU Country)", groups = {"US", TestGroup.DISNEY_COMMERCE}, enabled = false)
    public void portabilityNonLaunch() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByAppVersion(LIVE);

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "en-GB", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "CY"));

        SoftAssert sa = new SoftAssert();
        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup("CY", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(true);

        sa.assertFalse(portabilityPage.isLoginPageSignUpCtaPresent(),
                String.format("Login Sign Up CTA present on: %s, on eu login page", portabilityPage.getCurrentUrl()));

        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        sa.assertTrue(userPage.isForgotPasswordIdPresent(),
                String.format("'Forgot password' not visible on: %s", portabilityPage.getCurrentUrl()));

        portabilityPage.clickDplusBaseLogoId();
        portabilityPage.assertFalseLogoRedirect(sa);
        portabilityPage.assertUrlContains(sa, "/eu/");

        sa.assertAll();
    }

    // Currently we don't have a country that supports the splash page, the test cannot be run at this moment
    @Test(description = "Splash Redirect (CDN Gating)", groups = "US", enabled = false)
    public void portabilitySplashRedirect() {
        skipTestByEnv(QA);
        skipTestByAppVersion(LIVE);

        SoftAssert sa = new SoftAssert();
        if (!DisneyPlusBasePage.BRANCH.equalsIgnoreCase("LIVE")) {
            skipTestByEnv(DisneyPlusBasePage.ENVIRONMENT);
        }

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup("GR", CODE, COUNTRY);

        portabilityPage.assertUrlContains(sa, "gr");
        portabilityPage.assertPreviewUrl(sa);
        portabilityPage.assertEuLoginId(sa);

        sa.assertAll();
    }

    //Will enable once pre-sale
    @Test(description = "Presale User Service Unavailable", groups = "US", enabled = false)
    public void portabilityPreSaleUnavailable() {
        SoftAssert sa = new SoftAssert();

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup("DE", CODE, COUNTRY);

        portabilityPage.assertPreviewUrl(sa);
        portabilityPage.assertEuLoginId(sa);

        sa.assertAll();
    }

    // Currently this test is extremely flaky and since we are skipping Live testing, the test should not run at the moment
    @Test(description = "NL User in CY (Non Launched Country) Portability Active", groups = {"US", TestGroup.DISNEY_COMMERCE}, enabled = false)
    public void portabilityActiveGr() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByAppVersion(LIVE);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "en-GB", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "CY"));

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup("CY", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(true);

        portabilityPage.assertUrlContains(sa, "eu");
        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlContains(sa, "eu");
        portabilityPage.assertUrlLanguage(sa, language);

        portabilityPage.openURL(portabilityPage.getHomeUrl().concat("/en-gb/portability"));
        portabilityPage.isPortabilityTitleTextPresent();

        sa.assertFalse(portabilityPage.isCreditSubmitBtnIdClickable(),
                "Credit Submit Button is present on portability");

        sa.assertAll();
    }

    @Test(description = "NL User in FR (Launched Country) Portability Active", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void portabilityActiveFr() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "fr-FR", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "FR"));
        getAccountApi().patchAccountAttributeForLocation(disneyAccount.get(), "NL", PatchType.ACCOUNT);

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.setIgnoreCookie(true);
        portabilityPage.handlePortabilityStartup("FR", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(false);

        portabilityPage.assertUrlNotContains(sa, "eu");

        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlContains(sa, "eu");
        portabilityPage.assertUrlLanguage(sa, "fr-fr");

        portabilityPage.openURL(portabilityPage.getHomeUrl().concat("/fr-fr/portability"));
        portabilityPage.isPortabilityTitleTextPresent();

        sa.assertFalse(portabilityPage.isCreditSubmitBtnIdClickable(),
                "Credit Submit Button is present on portability");
        sa.assertTrue(portabilityPage.isCreditSubmitBtnIdVisible(),
                "Credit Submit Button is not visible on portability");

        sa.assertAll();
    }

    @Test(description = "AW (NL Territory) User in FR (Launched Country) Portability Active", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void portabilityActiveIm() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "AW", "en-GB", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "FR"));
        getAccountApi().patchAccountAttributeForLocation(disneyAccount.get(), "NL", PatchType.ACCOUNT);

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.setIgnoreCookie(true);
        portabilityPage.handlePortabilityStartup("FR", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(false);

        portabilityPage.assertUrlNotContains(sa, "eu");

        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlContains(sa, "eu");
        portabilityPage.assertUrlLanguage(sa, "en-gb");

        portabilityPage.openURL(portabilityPage.getHomeUrl().concat("/en-gb/portability"));
        portabilityPage.isPortabilityTitleTextPresent();

        sa.assertFalse(portabilityPage.isCreditSubmitBtnIdClickable(),
                "Credit Submit Button is clickable on portability");
        sa.assertTrue(portabilityPage.isCreditSubmitBtnIdVisible(),
                "Credit Submit Button is not visible on portability");

        sa.assertAll();
    }

    @Test(description = "NL User in FR (Paid with IAP) (No Payment Country)", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void portabilityNlFrNoPaymentCountry() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "nl", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "FR"));
        getAccountApi().patchAccountAttributeForLocation(disneyAccount.get(), "NL", PatchType.ACCOUNT);

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.setIgnoreCookie(true);
        portabilityPage.handlePortabilityStartup("FR", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(false);

        portabilityPage.assertUrlNotContains(sa, "/eu");

        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlContains(sa, "/eu");
        portabilityPage.assertUrlLanguage(sa, "nl-nl");

        portabilityPage.openURL(portabilityPage.getHomeUrl().concat("/nl-nl/portability"));
        portabilityPage.waitForPageToFinishLoading();
        portabilityPage.isPortabilityTitleTextPresent();

        sa.assertFalse(portabilityPage.isCreditSubmitBtnIdClickable(),
                "Credit Submit Button is clickable on portability");
        sa.assertTrue(portabilityPage.isCreditSubmitBtnIdVisible(),
                "Credit Submit Button is not visible on portability");

        sa.assertAll();
    }

    @Test(description = "NL User in NL (Paid with IAP) (No Payment Country)", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void portabilityNlNlNoPaymentCountry() throws URISyntaxException, JSONException, IOException {
        skipTestByEnv(QA);
        DisneyAccountApi api = getAccountApi();

        SoftAssert sa = new SoftAssert();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country("NL").patchLocations(false).build();
        com.disney.qa.api.pojos.DisneyAccount account = api.createAccount(request);

        account.setCountryCode("NL");
        api.entitleAccount(account, api.lookupOfferToUse("NL", "Yearly"), "V2-ORDER");
        api.patchStarOnboardingStatus(account, true);
        setOverride(api.overrideLocations(account, "NL"));

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.setIgnoreCookie(true);
        portabilityPage.handlePortabilityStartup("NL", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(false);

        portabilityPage.assertUrlNotContains(sa, "/eu");

        portabilityPage.typeDplusBaseEmailFieldId(account.getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(account.getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlNotContains(sa, "/eu");

        portabilityPage.openURL(portabilityPage.getHomeUrl().concat("/nl-nl/portability"));
        portabilityPage.waitForPageToFinishLoading();

        portabilityPage.isPortabilityTitleTextPresent();
        sa.assertTrue(portabilityPage.isCreditSubmitBtnIdPresent(),
                "Credit Submit Button is not present on portability");
        sa.assertTrue(portabilityPage.isCreditSubmitBtnIdClickable(),
                "Credit Submit Button is not clickable on portability");

        sa.assertAll();
    }

    @Test(description = "NL User in NL", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void portabilityNlUser() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "nl", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "NL"));

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.setIgnoreCookie(true);
        portabilityPage.handlePortabilityStartup("NL", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(false);
        portabilityPage.isSignUpLoginContinueBtnPresent(); //Need this to ensure login page is fully loaded
        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlNotContains(sa, "eu");
        portabilityPage.assertUrlContains(sa, "home");

        sa.assertAll();
    }

    @Test(description = "NL User in US", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void portabilityNlUserUs() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "nl", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "US"));

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup("US", CODE, COUNTRY);
        portabilityPage.clickDplusBasePreviewEuLoginId(false);
        portabilityPage.isSignUpLoginContinueBtnPresent(); //Need this to ensure login page is fully loaded
        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlNotContains(sa, "eu");
        portabilityPage.assertUrlContains(sa, "home");

        sa.assertAll();
    }

    @Test(description = "NL User in US with location override set to FR", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void portabilityNlUsOverrideFr() throws URISyntaxException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", "NL", "nl", "V2-ORDER"));
        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), "FR"));
        getAccountApi().patchAccountAttributeForLocation(disneyAccount.get(), "NL", PatchType.ACCOUNT);

        DisneyPlusPortabilityPage portabilityPage = new DisneyPlusPortabilityPage(getDriver());
        portabilityPage.handlePortabilityStartup("US", CODE, COUNTRY);
        portabilityPage.assertUrlNotContains(sa, "eu");

        portabilityPage.clickDplusBasePreviewEuLoginId(false);
        portabilityPage.isSignUpLoginContinueBtnPresent(); //Need this to ensure login page is fully loaded
        portabilityPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        portabilityPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        portabilityPage.clickDplusBaseLoginFlowBtn();
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isAccountDropdownIdPresent();

        portabilityPage.assertUrlContains(sa, "nl-nl");
        portabilityPage.assertUrlContains(sa, "eu");
        portabilityPage.assertUrlContains(sa, "home");

        sa.assertAll();
    }

}
