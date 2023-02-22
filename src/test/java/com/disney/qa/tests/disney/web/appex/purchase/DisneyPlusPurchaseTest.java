package com.disney.qa.tests.disney.web.appex.purchase;


import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.footer.DisneyPlusFooterPage;
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

public class DisneyPlusPurchaseTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();
    private static final ThreadLocal<DisneyPlusCommercePage> commercePage = new ThreadLocal<>();

    private DisneyPlusUserPage page = null;
    private DisneyPlusFooterPage footer = null;
    private DisneyPlusCommercePage purchase = null;

    private static final String privacyPolicyUrlAssertUrl = "legal/privacy-policy";
    private static final String subscriberAgreementAssertUrl = "subscriber-agreement";
    private static final String locale = "US";
    BrowserMobProxy proxy;

    @BeforeMethod()
    public void signUpSetup() {
        page = new DisneyPlusUserPage(getDriver());
        footer = new DisneyPlusFooterPage(getDriver());
        purchase = new DisneyPlusCommercePage(getDriver());
        proxy = page.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, "code", "country"));
        commercePage.set(new DisneyPlusCommercePage(getDriver()));
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void cleanUpTest(ITestResult result) {
        boolean isTestResultSuccess = result.getStatus() == ITestResult.SUCCESS;
        testCleanup(isTestResultSuccess, disneyAccount.get());
        disneyAccount.remove();
    }


    @Test(description = "Purchase - Footer - Home Page")
    public void purchaseHeadersHome(String country) {
        SoftAssert sa = new SoftAssert();

        footer.assertFooterElements(sa);
        footer.clickAllFooterElementsAssert(sa);

        sa.assertAll();
    }


    @Test(description = "Purchase - Footer - Sign Up Page", priority = 2)
    public void purchaseFooterSignUp() {
        SoftAssert sa = new SoftAssert();

        page.clickTrialBtn();
        page.waitForPageToFinishLoading();

        footer.assertFooterElements(sa);
        footer.clickAllFooterElementsAssert(sa);

        sa.assertAll();
    }

    @Test(description = "Purchase - Legal Copy - Sign Up Page", priority = 3)
    public void purchaseLegalSignUp() {
        SoftAssert sa = new SoftAssert();

        page.clickTrialBtn();
        page.waitForPageToFinishLoading();

        purchase.clickLegalCopyLinksAssert(sa, purchase.getPrivacyPolicy1(), privacyPolicyUrlAssertUrl);
        purchase.clickLegalCopyLinksAssert(sa, purchase.getPrivacyPolicy2(), privacyPolicyUrlAssertUrl);
        purchase.clickLegalCopyLinksAssert(sa, purchase.getSubscriberAgreement(), subscriberAgreementAssertUrl);

        sa.assertAll();
    }

    @Test(description = "Purchase - Footer - Password Page", priority = 4)
    public void purchaseFooterPassword() {
        SoftAssert sa = new SoftAssert();

        page.signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), false, false, disneyAccount);
        page.waitForPageToFinishLoading();

        footer.assertFooterElements(sa);
        footer.clickAllFooterElementsAssert(sa);

        sa.assertAll();

    }

    @Test(description = "Purchase - Footer - Billing Page", priority = 5)
    public void purchaseFooterBilling() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        setOverride(page.signUpGeneratedEmailPassword(locale, false, false, language, disneyAccount, proxy));

        //page.waitForElementTimeout(redemption.getSubmitAgreeBtn(), 5);

        footer.assertFooterElements(sa);
        footer.clickAllFooterElementsAssert(sa);

        sa.assertAll();


    }
}
