package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

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
}
