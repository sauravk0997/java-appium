package com.disney.qa.tests.disney.web.commerce.activation;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceSkyActivationTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    private boolean isPartner = true;
    private boolean isBundle = false;
    private final String disneyActivationTokenUrl = DisneyWebParameters.DISNEY_PROD_ACTIVATION_TOKEN_URL.getValue();
    private final String disneyIncomingActivationUrl = DisneyWebParameters.DISNEY_INCOMING_ACTIVATION_URL.getValue();

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }


    @Test(description = "Incoming Activation - Sky", groups = {"GB"})
    public void incomingActivationSky() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        String provider = "skyuk";

        DisneyAccountApi accountApi = getAccountApi();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        String token = commercePage.getJwtActivationToken(disneyActivationTokenUrl, account.getEmail(), provider);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getActivationUrl(disneyIncomingActivationUrl, token, provider));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(account.getEmail(), account.getUserPass());

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);

        sa.assertAll();
    }

    @Test(description = "Incoming Activation - Sky - Account created on D+", groups = {"GB"})
    public void incomingActivationSkyAccountOnDisney() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        String email = DisneyApiCommon.getUniqueEmail();
        String password = DisneyApiCommon.getCommonPass();
        String provider = "skyuk";

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPasswordActivation(locale, language,false, false, disneyAccount, proxy));

        String token = commercePage.getJwtActivationToken(disneyActivationTokenUrl, email, provider);
        commercePage.openURL(commercePage.getActivationUrl(disneyIncomingActivationUrl, token, provider));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(email, password);

        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), isBundle, isPartner);
        disneyAccount.set(commercePage.setAndGetUIGeneratedAccount(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass()));


        sa.assertAll();
    }

}
