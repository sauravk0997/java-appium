package com.disney.qa.tests.espn.web.appex;

import com.disney.qa.espn.web.EspnPurchasePage;
import com.disney.qa.espn.web.EspnWebParameters;
import com.disney.qa.tests.BaseTest;
import com.disney.util.TestGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class EspnUfcPpvPaywallTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final static String ESPN_UFC_URL = EspnWebParameters.ESPN_WEB_PROD_LANDING_UFC_PPV_URL.getValue();
    private final static String PROD_BASE_USER = EspnWebParameters.ESPN_WEB_PROD_QE_ESPN_BASE_EMAIL.getValue();
    private final static String PROD_PPV_ENT_USER = EspnWebParameters.ESPN_WEB_PROD_QE_ESPN_BASE_PPV_EMAIL.getValue();
    private final static String PROD_BASE_USER_PASS = EspnWebParameters.ESPN_WEB_PROD_QE_ESPN_BASE_PWD.getDecryptedValue();
    private final static String firstName = "QE";
    private final static String lastName = "Automation";


    @Test(description = "Verify PPV paywall is present", groups = {TestGroup.ESPN_APPEX})
    public void baseEntitlementUfcPpvPaywall() {

        EspnPurchasePage espnPurchasePage = new EspnPurchasePage(getDriver());
        SoftAssert sa = new SoftAssert();

        espnPurchasePage.pageSetUp(ESPN_UFC_URL);
        espnPurchasePage.buyNow();
        espnPurchasePage.signIn(PROD_BASE_USER, PROD_BASE_USER_PASS);
        sa.assertTrue(espnPurchasePage.getPaywallBody(0).isElementPresent(), "Paywall did not load");
        LOGGER.info("Paywall Body:\n" + espnPurchasePage.getPaywallBody(0));
        sa.assertTrue(espnPurchasePage.getUFCPurchasePrice().getText().contains("$59.99"), "UFC Add-on price could not be found");
        LOGGER.info(String.format("Session Token: %s", espnPurchasePage.getValueOfCookieNamed("ESPN-ONESITE.WEB-PROD.token")));

        sa.assertAll();

    }

    @Test(description = "Unregistered user is presented paywall", groups = {TestGroup.ESPN_APPEX})
    public void unregisteredUserPpvPaywall() {

        EspnPurchasePage espnPurchasePage = new EspnPurchasePage(getDriver());
        SoftAssert sa = new SoftAssert();

        espnPurchasePage.pageSetUp(ESPN_UFC_URL);
        espnPurchasePage.buyNow();
        espnPurchasePage.createNewAccount(firstName, lastName, espnPurchasePage.uniqueYopmail(PROD_BASE_USER), PROD_BASE_USER_PASS);
        sa.assertTrue(espnPurchasePage.getPaywallBody(0).isElementPresent(), "Paywall did not load");
        LOGGER.info("Paywall Body:\n" + espnPurchasePage.getPaywallBody(0));
        sa.assertTrue(espnPurchasePage.getUFCBundlePrice().getText().contains("79.99"), "UFC Add-on price could not be found");
        LOGGER.info(String.format("Session Token: %s", espnPurchasePage.getValueOfCookieNamed("ESPN-ONESITE.WEB-PROD.token")));

        sa.assertAll();

    }

    @Test(description = "Verify paywall not displayed to user with PPV entitlement", groups = {TestGroup.ESPN_APPEX})
    public void paywallNotPresent() {

        EspnPurchasePage espnPurchasePage = new EspnPurchasePage(getDriver());
        SoftAssert sa = new SoftAssert();

        espnPurchasePage.pageSetUp(ESPN_UFC_URL);
        espnPurchasePage.buyNow();
        espnPurchasePage.signInWithUfcPpvEntitlements(PROD_PPV_ENT_USER, PROD_BASE_USER_PASS);
        sa.assertTrue(espnPurchasePage.getWelcomeBackHeading().getText().contains("Welcome Back"), "User was not logged in");
        sa.assertTrue(espnPurchasePage.getGetStartedButton().isElementPresent(), "User was not logged in");
        LOGGER.info(String.format("Session Token: %s", espnPurchasePage.getValueOfCookieNamed("ESPN-ONESITE.WEB-PROD.token")));

        sa.assertAll();
    }
}
