package com.disney.qa.tests.espn.web;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.oneId.CreateEspnAccountRequest;
import com.disney.qa.api.client.responses.account.espn.items.EspnFullAccount;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.DisneySkuParameters;
import com.disney.qa.api.disney.pojo.DisneyAccount;
import com.disney.qa.api.pojos.ESPNEntitlement;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.espn.web.EspnPurchasePage;
import com.disney.qa.espn.web.EspnWebParameters;
import com.disney.qa.tests.BaseTest;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.Date;

/** Created by hrobbins on 02/14/19
 * Updated by hrobbins on 03/12/2020
 * Updated by smallem on 10/16/2020
 */

public class EspnPurchaseTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private String monthlyBaseUrl = EspnWebParameters.ESPN_WEB_BASE_PURCHSE_DIRECT_URL.getValue() + EspnWebParameters.ESPN_WEB_BASE_MONTHLY_DIRECT_URL.getValue();
    private String annualBaseUrl = EspnWebParameters.ESPN_WEB_BASE_PURCHSE_DIRECT_URL.getValue() + EspnWebParameters.ESPN_WEB_BASE_ANNUAL_DIRECT_URL.getValue();
    private String UFCURL = EspnWebParameters.ESPN_WEB_BASE_PURCHSE_DIRECT_URL.getValue() + EspnWebParameters.ESPN_WEB_UFC_DIRECT_URL.getValue();
    private String UFCUpgradeURL = EspnWebParameters.ESPN_WEB_BASE_PURCHSE_DIRECT_URL.getValue() + EspnWebParameters.ESPN_WEB_UFC_UPGRADE_DIRECT_URL.getValue();
    private String MLBURL = EspnWebParameters.ESPN_WEB_BASE_PURCHSE_DIRECT_URL.getValue() + EspnWebParameters.ESPN_WEB_MLB_BUNDLE_DIRECT_URL.getValue();
    private String subManagementURL = EspnWebParameters.ESPN_WEB_QA_SUB_MANAGEMENT_URL.getValue();
    private String userGmail = EspnWebParameters.ESPN_WEB_GMAIL_USER.getValue();
    private String password = EspnWebParameters.ESPN_WEB_GMAIL_PASS.getDecryptedValue();
    private String superBundlePass = DisneyWebParameters.DISNEY_QA_WEB_GENERIC_PASS.getDecryptedValue();
    private String firstName = "Web";
    private String lastName = "QA Automation";
    private String zip = "12345";
    private String mastercard = EspnWebParameters.MASTERCARD_CREDIT_CARD.getValue();
    private String cvv = EspnWebParameters.CVV.getValue();
    private String visa = EspnWebParameters.VISA_CREDIT_CARD.getValue();
    private String discover = EspnWebParameters.DISCOVER_CREDIT_CARD.getValue();
    private String amex = EspnWebParameters.AMEX_CREDIT_CARD.getValue();
    private String amexCVV = EspnWebParameters.AMEX_CVV.getValue();
    private String monthlyPrice = "6.99";
    private String annualPrice = "69.99";
    private String mlbMonthlyBundlePrice = "29.98";
    private String mlbAnnualBundlePrice = "74.98";
    private String mlbMonthlyAddOnPrice = "24.99";
    private String mlbAnnualAddOnPrice = "24.99";
    private String ufcBundlePrice = "99.98";
    private String ufcPpvPrice = "74.99";
    private String welcomeEmailSubject = "Welcome to ESPN";
    private String purchaseEmailSubject = "[t proof|T] [ORDERCONFIRM]Thank you for your order";
    private String country = "US";
    private String language = "en";
    private String paymentUpdateSuccess = "Success! Your payment details are updated With credit card";

    DisneyAccount superBundleUser;
    EspnPurchasePage espnPurchase;
    VerifyEmail email;
    String baseEmailAddress;
    Date startTime;

    @BeforeMethod
    public void beforeMethod() {
        espnPurchase = new EspnPurchasePage(getDriver());
        email = new VerifyEmail();
        baseEmailAddress = espnPurchase.uniqueEmail(userGmail);
        startTime = email.getStartTime();

    }

    @QTestCases(id = "47403")
    @Test(description = "Base Tier Monthly Purchase")
    public void purchaseBaseMonthly() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(monthlyPrice), "Monthly price could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        pause(10);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");

        espnPurchase.updatePayment(discover, firstName, lastName, zip, cvv);
        sa.assertTrue(espnPurchase.getUpdatePaymentMsg().getText().contains(paymentUpdateSuccess), "Payment update failed");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the Success message");
        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());

        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");

        LOGGER.info("SWID for Base Tier Monthly user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @Test(description = "Base Tier Monthly Purchase plus PPV add-on")
    public void purchaseBaseMonthlyWithPPVAddOn() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(monthlyPrice), "Monthly price could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        espnPurchase.addOnPPV();
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");

        LOGGER.info("SWID for Base Tier Monthly user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "69656")
    @Test(description = "Base Tier Monthly Purchase with Annual Upgrade")
    public void purchaseBaseMonthlyWithAnnualUpgrade() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(monthlyPrice), "Monthly price could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(annualPrice), "Monthly to Annual upgrade flow did not load");
        espnPurchase.changeButton(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");
        espnPurchase.addOnPPV();
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Monthly to Annual upgrade was not successful");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.updatePayment(visa, firstName, lastName, zip, cvv);
        sa.assertTrue(espnPurchase.getUpdatePaymentMsg().getText().contains(paymentUpdateSuccess), "Payment update failed");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the Success message");
        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for Base Tier Monthly with Annual Upgrade user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();
    }

    @QTestCases(id = "47405")
    @Test(description = "Base Tier Annual Purchase")
    public void purchaseBaseAnnual() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(annualPrice), "Annual price could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        espnPurchase.addOnPPV();
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(),  "Unable to locate the UFC logo in sub-management");

        espnPurchase.updatePayment(amex, firstName, lastName, zip, amexCVV);
        sa.assertTrue(espnPurchase.getUpdatePaymentMsg().getText().contains(paymentUpdateSuccess), "Payment update failed");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the Success message");
        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for Base Tier Annual user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "47406")
    @Test(description = "MLB Bundle Monthly Purchase", enabled=false)
    public void purchaseMLBMonthlyBundle() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyBundlePrice), "MLB Tile for bundle on purchase flow could not be found");
        espnPurchase.purchaseFlow(amex, firstName, lastName, zip, amexCVV);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        espnPurchase.addOnPPV();
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");

        LOGGER.info("SWID for MLB Bundle Monthly user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "69657")
    @Test(description = "MLB Bundle Monthly Purchase with Annual Upgrade", enabled=false)
    public void purchaseMLBMonthlyBundleWithAnnualUpgrade() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyBundlePrice), "MLB Tile for bundle on purchase flow could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        espnPurchase.addOnPPV();
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(annualPrice), "Monthly to Annual upgrade flow did not load");
        espnPurchase.storedPayment();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Monthly to Annual upgrade was not successful");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for MLB Bundle Monthly with Annual Upgrade user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "47407")
    @Test(description = "MLB Bundle Annual Purchase", enabled=false)
    public void purchaseMLBAnnualBundle() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        // Note : Effective March, 2021 re-add radio button option for MLB
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbAnnualBundlePrice), "E+ Tile for bundle on purchase flow could not be found");
        espnPurchase.purchaseFlow(mastercard, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        espnPurchase.addOnPPV();
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.updatePayment(visa, firstName, lastName, zip, cvv);
        sa.assertTrue(espnPurchase.getUpdatePaymentMsg().getText().contains(paymentUpdateSuccess), "Payment update failed");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the Success message");
        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for MLB Bundle Annual user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "47408")
    @Test(description = "MLB Add-on Monthly Purchase", enabled=false)
    public void purchaseMLBAddOnMonthly() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(monthlyPrice), "Monthly price could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");
        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyAddOnPrice), "MLB add-on monthly price could not be found");
        espnPurchase.storedPayment();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");
        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");

        LOGGER.info("SWID for MLB Add-on Monthly user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "69658")
    @Test(description = "MLB Add-on Monthly Purchase with Annual Upgrade", enabled=false)
    public void purchaseMLBAddOnMonthlyWithAnnualUpgrade() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(monthlyPrice), "Monthly price could not be found");
        espnPurchase.purchaseFlow(amex, firstName, lastName, zip, amexCVV);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");
        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyAddOnPrice), "MLB add-on monthly price could not be found");
        espnPurchase.storedPayment();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        espnPurchase.addOnPPV();
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(annualPrice), "Monthly to Annual upgrade flow did not load");
        espnPurchase.storedPayment();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Monthly to Annual upgrade was not successful");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for MLB Add-on Monthly with Annual Upgrade user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "47409")
    @Test(description = "MLB Add-on Annual Purchase", enabled=false)
    public void purchaseMLBAddOnAnnual() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(annualPrice), "Annual price could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");
        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
       // Note : Effective March, 2021 re-add radio button option for MLB
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbAnnualAddOnPrice), "MLB add-on annual price on purchase flow could not be found");
        espnPurchase.storedPayment();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for MLB Add-on Annual user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "47410")
    @Test(description = "UFC Bundle")
    public void purchaseUFCBundle() {
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        Assert.assertTrue(espnPurchase.getUFCBundlePrice().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().equals(ufcBundlePrice), "UFC Bundle price could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.updatePayment(visa, firstName, lastName, zip, cvv);
        sa.assertTrue(espnPurchase.getUpdatePaymentMsg().getText().contains(paymentUpdateSuccess), "Payment update failed");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the Success message");
        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC Bundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "47411")
    @Test(description = "Base Tier Monthly + UFC PPV Add-on")
    public void purchaseUFCBaseTierMonthly(){
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(monthlyPrice), "Monthly price could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");
        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, password);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcPpvPrice), "UFC Add-on price could not be found");
        espnPurchase.changeButton(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrading to an annual plan"), "Monthly user did not receive Upgrade to Annual Message");

        LOGGER.info("SWID for UFC + Monthly Add-on user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "69659")
    @Test(description = "Base Tier Monthly with Annual Upgrade + UFC PPV Add-on")
    public void purchaseUFCUpgrade(){
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(monthlyPrice), "Monthly price could not be found");
        espnPurchase.purchaseFlow(amex, firstName, lastName, zip, amexCVV);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");
        espnPurchase.pageSetUp(UFCUpgradeURL);
        espnPurchase.signIn(baseEmailAddress, password);
        sa.assertTrue(espnPurchase.getMonthlyToAnnualUpgradeMsg().getText().contains("upgrade your existing"), "Monthly user did not receive Upgrade to Annual Message");
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcBundlePrice), "UFC Add-on price could not be found");
        espnPurchase.changeButton(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC + Monthly Add-on with Annual Upgrade user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "47412")
    @Test(description = "Base Tier Annual + UFC PPV Add-on")
    public void purchaseBaseTierAnnualandUFCAddon(){
        SoftAssert sa = new SoftAssert();

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.createNewAccount(firstName, lastName, baseEmailAddress, password);
        email.scanEspnEmail(baseEmailAddress, password, welcomeEmailSubject, startTime);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(annualPrice), "Annual price could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");
        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, password);
        Assert.assertTrue(espnPurchase.getPurchasePrice(0).isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcPpvPrice), "UFC Add-on price could not be found");
        espnPurchase.changeButton(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getEspnLogo().isPresent(), "Unable to locate the E+ logo in sub-management");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, password);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC + Annual Add-on user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89595")
    @Test(description = "D+ Bundle User + E+ Monthly Product Block")
    public void disneyPlusBundleWithEspnMonthlyBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().email(baseEmailAddress).firstName(firstName).lastName(lastName).region(country).build();
        request.addEntitlement(new ESPNEntitlement());
        accountApi.createESPNAccount(request);

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);
        sa.assertAll();
    }

    @QTestCases(id = "89596")
    @Test(description = "D+ Bundle User + E+ Annual Product Block")
    public void disneyPlusBundleWithEspnAnnualBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().email(baseEmailAddress).firstName(firstName).lastName(lastName).region(country).build();
        request.addEntitlement(new ESPNEntitlement());
        accountApi.createESPNAccount(request);

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89597")
    @Test(description = "D+ Bundle User + MLB Monthly Add-on + Product Block", enabled=false)
    public void disneyPlusBundleWithMlbMonthly(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().email(baseEmailAddress).firstName(firstName).lastName(lastName).region(country).build();
        request.addEntitlement(new ESPNEntitlement());
        accountApi.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyAddOnPrice), "MLB add-on monthly price on purchase flow could not be found");
        espnPurchase.purchaseFlow(mastercard, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");


        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress,superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Monthly MLB Add-on is:" + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89598")
    @Test(description = "D+ Bundle User + MLB Annual Add-on + Product Block", enabled=false)
    public void disneyPlusBundleWithMlbAnnual() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().email(baseEmailAddress).firstName(firstName).lastName(lastName).region(country).build();
        request.addEntitlement(new ESPNEntitlement());
        accountApi.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        // Note : Effective March, 2021 re-add radion button option for MLB
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbAnnualAddOnPrice), "MLB add-on annual price on purchase flow could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Annual MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89599")
    @Test(description = "D+ Bundle User + UFC Add-on + E+ Base Tier Product Block")
    public void disneyPlusBundleWithUfcAddon(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().email(baseEmailAddress).firstName(firstName).lastName(lastName).region(country).build();
        request.addEntitlement(new ESPNEntitlement());
        accountApi.createESPNAccount(request);

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getTierPrice().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcPpvPrice), "UFC Add-on price could not be found");
        espnPurchase.purchaseFlow(amex, firstName, lastName, zip, amexCVV);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC + Annual Add-on user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89600")
    @Test(description = "Hulu Bundle User w/Ads + E+ Monthly Product Block")
    public void huluBundleSashWithEspnMonthlyBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89601")
    @Test(description = "Hulu Bundle User w/Ads + E+ Annual Product Block")
    public void huluBundleSashWithEspnAnnualBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89602")
    @Test(description = "Hulu Bundle User w/Ads + E+ Base Tier Product Block + MLB Bundle Add-on", enabled=false)
    public void huluBundleSashWithMlbMonthly(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyAddOnPrice), "MLB add-on monthly price on purchase flow could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");


        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress,superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Monthly MLB Add-on is:" + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89603")
    @Test(description = "Hulu Bundle User w/Ads + MLB Annual Add-on + Product Block", enabled=false)
    public void huluBundleSashWithMlbAnnual() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        // Note : Effective March, 2021 re-add radion button option for MLB
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbAnnualAddOnPrice), "MLB add-on annual price on purchase flow could not be found");
        espnPurchase.purchaseFlow(mastercard, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Annual MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89604")
    @Test(description = "Hulu Bundle User w/Ads + E+ Base Tier Product Block + UFC Add-on")
    public void huluBundleSashWithUfcBundle(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getTierPrice().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcPpvPrice), "UFC Add-on price could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC + Annual Add-on user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89605")
    @Test(description = "Hulu Bundle User No Ads + E+ Monthly Product Block")
    public void huluBundleNoahWithEspnMonthlyBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89606")
    @Test(description = "Hulu Bundle User No Ads + E+ Annual Product Block")
    public void huluBundleNoahWithEspnAnnualBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89607")
    @Test(description = "Hulu Bundle User No Ads + E+ Base Tier Product Block + MLB Bundle Add-on", enabled=false)
    public void huluBundleNoahWithMlbMonthly(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyAddOnPrice), "MLB add-on monthly price on purchase flow could not be found");
        espnPurchase.purchaseFlow(amex, firstName, lastName, zip, amexCVV);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");


        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress,superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Monthly MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89608")
    @Test(description = "Hulu Bundle User No Ads + MLB Annual Add-on + Product Block", enabled=false)
    public void huluBundleNoahWithMlbAnnual() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        // Note : Effective March, 2021 re-add radion button option for MLB
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbAnnualAddOnPrice), "MLB add-on annual price on purchase flow could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Annual MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89609")
    @Test(description = "Hulu Bundle User No Ads + E+ Base Tier Product Block + UFC Add-on")
    public void huluBundleNoahWithUfcBundle(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getTierPrice().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcPpvPrice), "UFC Add-on price could not be found");
        espnPurchase.purchaseFlow(mastercard, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC + Annual Add-on user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89610")
    @Test(description = "Hulu Bundle User Live w/Ads + E+ Monthly Product Block")
    public void huluBundleLiveSashWithEspnMonthlyBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89611")
    @Test(description = "Hulu Bundle User Live w/Ads + E+ Annual Product Block")
    public void huluBundleLiveSashWithEspnAnnualBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = " 89612")
    @Test(description = "Hulu Bundle User Live w/Ads + E+ Base Tier Product Block + MLB Bundle Add-on", enabled=false)
    public void huluBundleLiveSashWithMlbMonthly(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyAddOnPrice), "MLB add-on monthly price on purchase flow could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");


        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress,superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Monthly MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89613")
    @Test(description = "Hulu Bundle User Live w/Ads + MLB Annual Add-on + Product Block", enabled=false)
    public void huluBundleLiveSashWithMlbAnnual() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        // Note : Effective March, 2021 re-add radion button option for MLB
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbAnnualAddOnPrice), "MLB add-on annual price on purchase flow could not be found");
        espnPurchase.purchaseFlow(amex, firstName, lastName, zip, amexCVV);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is:" + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Annual MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89614")
    @Test(description = "Hulu Bundle User Live w/Ads + E+ Base Tier Product Block + UFC Add-on")
    public void huluBundleLiveSashWithUfcBundle(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getTierPrice().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcPpvPrice), "UFC Add-on price could not be found");
        espnPurchase.purchaseFlow(visa, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC + Annual Add-on user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89615")
    @Test(description = "Hulu Bundle User Live No Ads + E+ Monthly Product Block")
    public void huluBundleLiveNoahWithEspnMonthlyBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89616")
    @Test(description = "Hulu Bundle User Live No Ads + E+ Annual Product Block")
    public void huluBundleLiveNoahWithEspnAnnualBlock(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubPage();
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        LOGGER.info("SWID for D+ SuperBundle user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);

        sa.assertAll();

    }

    @QTestCases(id = "89617")
    @Test(description = "Hulu Bundle User Live No Ads + E+ Base Tier Product Block + MLB Bundle Add-on", enabled=false)
    public void huluBundleLiveNoahWithMlbMonthly(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbMonthlyAddOnPrice), "MLB add-on monthly price on purchase flow could not be found");
        espnPurchase.purchaseFlow(mastercard, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");


        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress,superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Monthly MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89618")
    @Test(description = "Hulu Bundle User Live No Ads + MLB Annual Add-on + Product Block", enabled=false)
    public void huluBundleLiveNoahWithMlbAnnual() {
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getMlbCardLogo().isPresent(), "Purchase flow did not load");
        // Note : Effective March, 2021 re-add radion button option for MLB
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(mlbAnnualAddOnPrice), "MLB add-on annual price on purchase flow could not be found");
        espnPurchase.purchaseFlow(discover, firstName, lastName, zip, cvv);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");
        sa.assertTrue(espnPurchase.getMlbLogo().isPresent(), "Unable to locate the MLB logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(MLBURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for D+ ser with Annual MLB Add-on is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

    @QTestCases(id = "89619")
    @Test(description = "Hulu Bundle User Live No Ads + E+ Base Tier Product Block + UFC Add-on")
    public void huluBundleLiveNoahWithUfcBundle(){
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi api = new DisneyAccountApi("browser", "QA", "espn");
        CreateEspnAccountRequest request = CreateEspnAccountRequest.builder().firstName(firstName).lastName(lastName).email(baseEmailAddress).region(country).password(superBundlePass).build();
        ESPNEntitlement entitlement = ESPNEntitlement.builder().sku(com.disney.qa.api.utils.DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH).subVersion("V1").build();
        request.addEntitlement(entitlement);
        api.createESPNAccount(request);

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        espnPurchase.updateAccount(firstName, lastName);
        getDriver().navigate().refresh();
        Assert.assertTrue(espnPurchase.getTierPrice().isPresent(), "Purchase flow did not load");
        sa.assertTrue(espnPurchase.getTierPrice().getText().contains(ufcPpvPrice), "UFC Add-on price could not be found");
        espnPurchase.purchaseFlow(amex, firstName, lastName, zip, amexCVV);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("thank-you"), "Not on Success page");

        espnPurchase.pageSetUp(subManagementURL);
        espnPurchase.verifySubManagement(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("subscription"), "Sub-Management failed to load");
        sa.assertTrue(espnPurchase.getUfcLogo().isPresent(), "Unable to locate the UFC logo in sub-management");
        sa.assertTrue(espnPurchase.getSuperbundleLogo().isPresent(), "Unable to locate the Superbundle logo in sub-management");

        espnPurchase.pageSetUp(monthlyBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(annualBaseUrl);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        espnPurchase.pageSetUp(UFCURL);
        espnPurchase.signIn(baseEmailAddress, superBundlePass);
        LOGGER.info("Current URL is: " + espnPurchase.getCurrentUrl());
        sa.assertTrue(espnPurchase.getCurrentUrl().contains("blockage"), "User is not receiving product blockage");

        LOGGER.info("SWID for UFC + Annual Add-on user is: " + espnPurchase.getValueOfCookieNamed("SWID"));

        email.scanEspnEmail(baseEmailAddress, password, purchaseEmailSubject, startTime);


        sa.assertAll();

    }

}

