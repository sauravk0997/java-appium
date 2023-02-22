package com.disney.qa.tests.disney.web.commerce.activation;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceCanalActivationTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    private boolean isPartner = true;
    private boolean isBundle = false;
    private final String disneyActivationTokenUrl = DisneyWebParameters.DISNEY_PROD_ACTIVATION_TOKEN_URL.getValue();
    private final String disneyIncomingActivationUrl = DisneyWebParameters.DISNEY_INCOMING_ACTIVATION_URL.getValue();

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }


    @Test(description = "Incoming Activation - Canal", groups = {"FR"})
    public void incomingActivationCanal() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        String provider = "canal";

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        String token = commercePage.getJwtActivationToken(disneyActivationTokenUrl, disneyAccount.get().getEmail(), provider);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getActivationUrl(disneyIncomingActivationUrl, token, provider));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Incoming Activation - Canal - Account created on D+", groups = {"FR"})
    public void incomingActivationCanalAccountOnDisney() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        String provider = "canal";

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        disneyAccount.get().setEmail(DisneyApiCommon.getUniqueEmail());
        disneyAccount.get().setUserPass(DisneyApiCommon.getCommonPass());

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        String token = commercePage.getJwtActivationToken(disneyActivationTokenUrl, disneyAccount.get().getEmail(), provider);
        commercePage.openURL(commercePage.getActivationUrl(disneyIncomingActivationUrl, token, provider));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        redemptionPage.clickSignUpLoginContinueBtn();
        redemptionPage.clickSubscriberAgreementContinueCta(locale);
        redemptionPage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        redemptionPage.clickPasswordContinueLoginBtn();
        
        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), true, false);

        sa.assertAll();
    }
}
