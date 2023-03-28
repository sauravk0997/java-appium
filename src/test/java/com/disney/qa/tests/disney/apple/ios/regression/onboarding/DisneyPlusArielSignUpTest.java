package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusArielSignUpTest extends DisneyBaseTest {

    private static final String DOB_MINOR = "01/01/2020";
    private static final String DOB_ADULT = "01/01/1983";
    private static final String DOB_INVALID = "01/01/1766";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62247"})
    @Test(description = "Verify onboarding stepper for US based users", groups = {"Ariel"})
    public void verifyOnboardingStepperUS() {
        setGlobalVariables();
        handleAlert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();

        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen");

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());

        Assert.assertTrue(disneyPlusCreatePasswordIOSPageBase.isOpened(),
                "User was not directed to 'Create Password'");

        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB("01/01/1980");
        Assert.assertTrue(initPage(DisneyPlusPaywallIOSPageBase.class).isOpened(),
                "User was not directed to the paywall");
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62022"})
    @Test(description = "Sign Up - Paywall - User taps Cancel", groups = {"Ariel"})
    public void verifyPaywallCancel() {
        setGlobalVariables();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);

        SoftAssert sa = new SoftAssert();
        handleAlert();

        disneyPlusWelcomeScreenIOSPageBase.clickSignUpButton();
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB("01/01/1980");
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

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62020"})
    @Test(description = "Sign Up - Verify Paywall UI", groups = {"Ariel"})
    public void verifyPaywallUI() {
        setGlobalVariables();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        SoftAssert sa = new SoftAssert();
        handleAlert();

        disneyPlusWelcomeScreenIOSPageBase.clickSignUpButton();
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);

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
    @Test(description = "Verify valid password submissions and hide/show button", groups = {"Ariel"})
    public void verifyValidPasswordSubmissions() {
        setGlobalVariables();
        verifySignUpButtonNavigation();
        SoftAssert sa = new SoftAssert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusApplePageBase disneyPlusApplePageBase = initPage(DisneyPlusApplePageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.enterPasswordValue("1234AB!@");
        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "1234AB!@",
                "XMOBQA-62237 - Show/Hide Password did not un-hide the password value");

        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "••••••••",
                "XMOBQA-62237 - Show/Hide Password did not hide the password value");

        disneyPlusApplePageBase.clickPrimaryButton();
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);

        sa.assertTrue(initPage(DisneyPlusPaywallIOSPageBase.class).isOpened(),
                "XMOBQA-62241 - Paywall was not displayed as expected");

        sa.assertAll();
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72385"})
    @Test(description = "Log in - Verify sign up - DOB under 18", groups = {"Onboarding"})
    public void testSignUpDoBUnder18() {
        setGlobalVariables();
        SoftAssert softAssert = new SoftAssert();
        handleAlert();
        DisneyPlusDOBCollectionPageBase disneyPlusDOBCollectionPageBase = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusAccountIsMinorIOSPageBase disneyPlusAccountIsMinorIOSPageBase = new DisneyPlusAccountIsMinorIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());

        disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(disneyAccount.get().getUserPass());

        softAssert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        disneyPlusWelcomeScreenIOSPageBase.clickCompleteSubscriptionButton();

        disneyPlusDOBCollectionPageBase.isOpened();
        disneyPlusDOBCollectionPageBase.enterDOB(DOB_MINOR);
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.isOpened(),
                "Contact CS screen did not appear.");

        softAssert.assertAll();
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72385"})
    @Test(description = "Log in - Verify sign up - DOB Over 18", groups = {"Onboarding"})
    public void testSignUpDOBOver18() {
        setGlobalVariables();
        SoftAssert softAssert = new SoftAssert();
        handleAlert();
        DisneyPlusDOBCollectionPageBase disneyPlusDOBCollectionPageBase = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = new DisneyPlusPaywallIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());

        disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(disneyAccount.get().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(disneyAccount.get().getUserPass());
        softAssert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        disneyPlusWelcomeScreenIOSPageBase.clickCompleteSubscriptionButton();

        disneyPlusDOBCollectionPageBase.isOpened();
        disneyPlusDOBCollectionPageBase.enterDOB(DOB_INVALID);
        softAssert.assertTrue(disneyPlusDOBCollectionPageBase.isInvalidDOBMessageDisplayed(),
                "Invalid DOB Message did not appear.");
        disneyPlusDOBCollectionPageBase.enterDOB(DOB_ADULT);
        //Dismiss error message for paywall - QAA-11256
        disneyPlusPaywallIOSPageBase.dismissPaywallErrorAlert();
        softAssert.assertAll();
    }

    private void verifySignUpButtonNavigation() {
        setGlobalVariables();
        handleAlert();
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
    }
}