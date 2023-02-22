package com.disney.qa.tests.disney.web.commerce.activation;

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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class StarPlusCommerceIzziActivationTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    private String provider = "izzi_mx";
    private static final String beginUrl = "/begin";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "B2B2C Izzi Combo Purchase", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void comboPurchaseIzzi() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).build();
        request.addSku(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getExternalEntitlement(provider));
        DisneyAccount account = getAccountApi().createAccount(request);

        getAccountApi().overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickUpgradeToBundleCta();
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.isModalPrimaryBtnPresent();
        sa.assertTrue(commercePage.isModalPrimaryBtnPresent(), "Purchase modal not present");
        commercePage.clickModalPrimaryBtn();

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

        commercePage.assertSuperbundlePageElements(sa, commercePage.getBundleOfferCardTest(), commercePage.getBundleCardTest(), false, "billing?type=bundle");

        redemptionPage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        accountPage.isBundleSuccessPage();
        commercePage.assertUrlContains(sa, "bundle-success");

        sa.assertAll();
    }

    @Test(description = "B2B2C Izzi Combo Purchase - Goto Partner Site", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void comboPurchaseIzziPartnerSite() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).build();
        request.addSku(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getExternalEntitlement(provider));
        DisneyAccount account = getAccountApi().createAccount(request);

        getAccountApi().overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickUpgradeToBundleCta();
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.isModalPrimaryBtnPresent();
        sa.assertTrue(commercePage.isModalPrimaryBtnPresent(), "Purchase modal not present");
        commercePage.clickModalSecondaryBtn();

        commercePage.assertUrlContains(sa, "izzi");

        sa.assertAll();
    }
    @Test(description = "B2B2C Izzi Combo Purchase - License Plate flow", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void comboPurchaseIzziLicencePlate() throws MalformedURLException, JSONException, URISyntaxException, InterruptedException, IOException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).build();
        request.addSku(new EntitlementHelper(getAccountApi(), DisneyGlobalUtils.getProject()).getExternalEntitlement(provider));
        DisneyAccount account = getAccountApi().createAccount(request);

        getAccountApi().overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.openURL(commercePage.getHomeUrl().concat(beginUrl));
        commercePage.generateAndEnterLicensePlateInputField(account);
        commercePage.clickLicensePlateSubmit();

        commercePage.typeDplusBaseEmailFieldId(account.getEmail());
        commercePage.clickSignUpLoginContinueBtn();
        commercePage.typeDplusBasePasswordFieldId(account.getUserPass());
        commercePage.clickDplusBaseLoginFlowBtn();

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());

        accountPage.clickUpgradeToBundleCta();
        commercePage.clickSubscriberAgreementContinueCtaOnUpgrade(locale);

        commercePage.isBillingPagePresent();
        sa.assertFalse(commercePage.isModalPrimaryBtnPresent(), "Purchase modal is present");

        sa.assertAll();
    }
}
