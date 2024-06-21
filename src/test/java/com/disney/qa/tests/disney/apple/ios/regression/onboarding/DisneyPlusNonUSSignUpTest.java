package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusNonUSSignUpTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62247"})
    @Test(description = "Verify onboarding stepper for US based users", groups = {"NonUS-Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyOnboardingStepperUS() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen");
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        Assert.assertTrue(disneyPlusCreatePasswordIOSPageBase.isOpened(),
                "User was not directed to 'Create Password'");
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");
        Assert.assertTrue(initPage(DisneyPlusPaywallIOSPageBase.class).isOpened(),
                "User was not directed to the paywall");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62022"})
    @Test(description = "Sign Up - Paywall - User taps Cancel", groups = {"NonUS-Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyPaywallCancel() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);

        SoftAssert sa = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickSignUpButton();
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");
        disneyPlusPaywallIOSPageBase.clickPaywallCancelButton();

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isFinishLaterHeaderPresent(),
                "Finish later header is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isFinishLaterTextPresent(),
                "Finish later text is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isResumeButtonPresent(),
                "RESUME button is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isFinishLaterButtonPresent(),
                "FINISH LATER button is not displayed.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62020"})
    @Test(description = "Sign Up - Verify Paywall UI", groups = {"NonUS-Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyPaywallUI() {
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);

        SoftAssert sa = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickSignUpButton();
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isYearlySkuButtonPresent(),
                "Yearly SKU button is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isMonthlySkuButtonPresent(),
                "Monthly SKU button is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isPaywallCancelButtonDisplayed(),
                "Cancel button is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isStartStreamingTextDisplayed(),
                "Start Streaming Text is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isCancelAnytimeTextDisplayed(),
                "Cancel anytime text is not displayed.");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.restoreBtn.isElementPresent(),
                "Restore Purchase button is not displayed.");

        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, "disney_logo");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62237", "XMOBQA-62241"})
    @Test(description = "Verify valid password submissions and hide/show button", groups = {"NonUS-Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyValidPasswordSubmissions() {
        verifySignUpButtonNavigation();
        SoftAssert sa = new SoftAssert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusApplePageBase disneyPlusApplePageBase = initPage(DisneyPlusApplePageBase.class);

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.enterPasswordValue("1234AB!@");
        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "1234AB!@",
                "XMOBQA-62237 - Show/Hide Password did not un-hide the password value");

        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "••••••••",
                "XMOBQA-62237 - Show/Hide Password did not hide the password value");

        disneyPlusApplePageBase.clickPrimaryButton();

        sa.assertTrue(initPage(DisneyPlusPaywallIOSPageBase.class).isOpened(),
                "XMOBQA-62241 - Paywall was not displayed as expected");

        sa.assertAll();
    }

    private void verifySignUpButtonNavigation() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
    }
}
