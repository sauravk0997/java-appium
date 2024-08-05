package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusArielSignUpTest extends DisneyBaseTest {

    private static final String DOB_MINOR = "01/01/2020";
    private static final String DOB_ADULT = "01/01/1983";
    private static final String DOB_INVALID = "01/01/1766";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72232"})
    @Test(description = "Log in - Verify sign up - DOB under 18", groups = {TestGroup.ONBOARDING, TestGroup.SIGN_UP, TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testSignUpDoBUnder18() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusAccountIsMinorIOSPageBase accountIsMinorPage = new DisneyPlusAccountIsMinorIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());

        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_MINOR);
        sa.assertTrue(accountIsMinorPage.isOpened(),
                "Contact CS screen did not appear.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74265"})
    @Test(description = "Log in - Verify sign up - Invalid DOB", groups = {TestGroup.ONBOARDING, TestGroup.SIGN_UP, TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testSignUpDOBInvalid() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_INVALID);
        sa.assertTrue(dobCollectionPage.isInvalidDOBMessageDisplayed(),
                "Invalid DOB Message did not appear.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72231"})
    @Test(description = "Log in - Verify login - DOB under 18", groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testLoginDobUnder18() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusDOBCollectionPageBase disneyPlusDOBCollectionPageBase = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusAccountIsMinorIOSPageBase disneyPlusAccountIsMinorIOSPageBase = new DisneyPlusAccountIsMinorIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(getAccount().getUserPass());

        softAssert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        disneyPlusWelcomeScreenIOSPageBase.clickCompleteSubscriptionButton();

        disneyPlusDOBCollectionPageBase.isOpened();
        disneyPlusDOBCollectionPageBase.enterDOB(DOB_MINOR);
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.isOpened(),
                "Contact CS screen did not appear.");
        disneyPlusAccountIsMinorIOSPageBase.clickHelpCenterButton();
        moreMenu.goBackToDisneyAppFromSafari();

        disneyPlusAccountIsMinorIOSPageBase.clickDismissButton();
        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isSignUpButtonDisplayed(),
                "User was not logged out and returned to the Welcome screen");
        softAssert.assertAll();
    }

}