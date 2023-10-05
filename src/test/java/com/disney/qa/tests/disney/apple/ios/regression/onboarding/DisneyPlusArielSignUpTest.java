package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusArielSignUpTest extends DisneyBaseTest {

    private static final String DOB_MINOR = "01/01/2020";
    private static final String DOB_ADULT = "01/01/1983";
    private static final String DOB_INVALID = "01/01/1766";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74264"})
    @Test(description = "Log in - Verify sign up - DOB under 18", groups = {"Onboarding"})
    public void testSignUpDoBUnder18() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        handleAlert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusAccountIsMinorIOSPageBase accountIsMinorPage = new DisneyPlusAccountIsMinorIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());

        disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(disneyAccount.get().getEmail());
        passwordPage.submitPasswordForLogin(disneyAccount.get().getUserPass());

        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_MINOR);
        sa.assertTrue(accountIsMinorPage.isOpened(),
                "Contact CS screen did not appear.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72385"})
    @Test(description = "Log in - Verify sign up - DOB Over 18", groups = {"Onboarding"})
    public void testSignUpDOBOver18() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        handleAlert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusPaywallIOSPageBase paywallPage = new DisneyPlusPaywallIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());

        disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(disneyAccount.get().getEmail());
        passwordPage.submitPasswordForLogin(disneyAccount.get().getUserPass());
        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);
        sa.assertFalse(dobCollectionPage.isInvalidDOBMessageDisplayed(), "Adult DOB was not accepted.");
        //Dismiss error message for paywall - QAA-12552
        paywallPage.dismissNotificationsPopUp();
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74265"})
    @Test(description = "Log in - Verify sign up - Invalid DOB", groups = {"Onboarding"})
    public void testSignUpDOBInvalid() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        handleAlert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());

        disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(disneyAccount.get().getEmail());
        passwordPage.submitPasswordForLogin(disneyAccount.get().getUserPass());
        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_INVALID);
        sa.assertTrue(dobCollectionPage.isInvalidDOBMessageDisplayed(),
                "Invalid DOB Message did not appear.");
        sa.assertAll();
    }
}