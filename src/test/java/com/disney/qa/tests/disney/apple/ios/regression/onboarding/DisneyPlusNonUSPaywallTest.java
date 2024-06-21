package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPaywallIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusRestartSubscriptionIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusNonUSPaywallTest extends DisneyBaseTest {
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62691"})
    @Test(description = "Paywall is shown to unentitled user after Log In", groups = {"Smoke", TestGroup.PRE_CONFIGURATION })
    public void testLoginWithUnentitledAccount() {
        SoftAssert softAssert = new SoftAssert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        login(getAccountApi().createAccount("US", "en"));

        DisneyPlusWelcomeScreenIOSPageBase paywallPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        softAssert.assertTrue(paywallPageBase.isLogOutButtonDisplayed(),
                "Expected: 'Log out' button should be present");

        softAssert.assertTrue(paywallPageBase.isCompleteSubscriptionButtonDisplayed(),
                "Expected: 'Complete Subscription' button should be present");

        paywallPageBase.clickCompleteSubscriptionButton();

        softAssert.assertTrue(paywallPageBase.isCancelButtonDisplayed(),
                "Expected: 'Cancel' button should be present");

        softAssert.assertTrue(paywallPageBase.isMonthlySubButtonDisplayed(),
                "Expected: Monthly Subscription button should be present");

        softAssert.assertTrue(paywallPageBase.isYearlySubButtonDisplayed(),
                "Expected: Yearly Subscription button should be present");

        softAssert.assertTrue(paywallPageBase.isRestoreButtonDisplayed(),
                "Expected: Restore button should be present");

        paywallPageBase.logOutFromUnentitledAccount();

        softAssert.assertTrue(paywallPageBase.isOpened(), "Expected: After logging out, main paywall page should be opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62193"})
    @Test(description = "Log in - Verify Restart Subscription Paywall UI", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyRestartSubscriptionPaywallUI() {
        SoftAssert softAssert = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusRestartSubscriptionIOSPageBase disneyPlusRestartSubscriptionIOSPageBase = initPage(DisneyPlusRestartSubscriptionIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);

        DisneyAccount expired = getAccountApi().createExpiredAccount("Yearly", "US", "en", "V1");
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(expired);

        disneyPlusRestartSubscriptionIOSPageBase.clickRestartSubscriptionButton();

        softAssert.assertTrue(disneyPlusPaywallIOSPageBase.isYearlySkuButtonPresent(),
                "Yearly SKU button is not displayed.");

        softAssert.assertTrue(disneyPlusPaywallIOSPageBase.isMonthlySkuButtonPresent(),
                "Monthly SKU button is not displayed.");

        softAssert.assertTrue(disneyPlusPaywallIOSPageBase.isPaywallCancelButtonDisplayed(),
                "Cancel button is not displayed.");

        softAssert.assertTrue(disneyPlusPaywallIOSPageBase.isRestartsSubscriptionHeaderDisplayed(),
                "Restart Subscription header is not displayed.");

        softAssert.assertTrue(disneyPlusPaywallIOSPageBase.isRestartsSubscriptionSubHeaderDisplayed(),
                "'You will be billed immediately. Restart anytime.' is not displayed. ");

        aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");

        softAssert.assertAll();
    }
}
