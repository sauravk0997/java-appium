package com.disney.qa.tests.disney.web.commerce.account;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.qa.tests.mitm.ProxyCredUtil;
import com.disney.util.TestGroup;
import com.proxy.MitmProxy;
import com.proxy.MitmProxyConfiguration;
import com.proxy.MitmProxyPool;
import com.proxy.Mode;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceBillingHistoryTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void setupTest() throws InterruptedException {
        disneyAccount.set(new DisneyAccount());
        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        MitmProxyConfiguration configuration = MitmProxyConfiguration.builder()
                .listenPort(getProxyPort())
                .headers(disneyPlusBasePage.getHeaders(locale))
                .build();
        if (!locale.equalsIgnoreCase(US)) {
            String country = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
            configuration.setProxyCreds(ProxyCredUtil.getGeoedgeProxy(country));
            configuration.setMode(Mode.UPSTREAM);
        }
        MitmProxy.startProxy(configuration);
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
        MitmProxyPool.tearDownProxy();
    }

    @Test(description = "Unified billing history", groups = {"US","MX","CL","CO","AR","BR", TestGroup.DISNEY_COMMERCE, TestGroup.ARIEL_COMMERCE})
    public void unifiedBillingHistory() throws JSONException, MalformedURLException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(LIVE);

        SoftAssert sa = new SoftAssert();

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(MONTHLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        commercePage.setIgnoreCookie(true);
        commercePage.navigateToMainPage();
        redemptionPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        accountPage.clickOnAccountDropdown(isMobile());
        Assert.assertTrue(accountPage.isBillingHistoryLinkVisible(), "Billing history link is not visible");

        accountPage.clickBillingHistoryCta();
        accountPage.assertUrlContains(sa, PageUrl.BILLING_HISTORY_URL);
        sa.assertTrue(accountPage.isBillingHistoryInvoiceRowPresent(), "Billing history row not present");

        accountPage.clickBillingHistoryInvoiceLink();
        sa.assertTrue(accountPage.isBillingHistoryInvoiceDetailsTitlePresent(), "Invoice details title not present");
        sa.assertTrue(accountPage.isBillingHistoryInvoiceDetailsSubscriptionPresent(), "Invoice details subscription not present");

        sa.assertAll();
    }
}
