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

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72385"})
    @Test(description = "Log in - Verify sign up - DOB under 18", groups = {"Onboarding"})
    public void testSignUpDoBUnder18() {
        initialSetup();
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
        initialSetup();
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

}