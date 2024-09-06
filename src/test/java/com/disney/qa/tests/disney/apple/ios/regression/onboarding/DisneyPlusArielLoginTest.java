package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusArielLoginTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72231"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION })
    public void testLoginDobUnder18() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage = new DisneyPlusEdnaDOBCollectionPageBase(getDriver());
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

        disneyPlusDOBCollectionPageBase.isOpened();
        ednaDOBCollectionPage.enterDOB(Person.MINOR.getMonth(), Person.MINOR.getDay(), Person.MINOR.getYear());
        ednaDOBCollectionPage.tapSaveAndContinueButton();

        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.isOpened(),
                "Contact CS screen did not appear.");
        disneyPlusAccountIsMinorIOSPageBase.clickHelpCenterButton();
        moreMenu.goBackToDisneyAppFromSafari();

        disneyPlusAccountIsMinorIOSPageBase.clickDismissButton();
        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isSignUpButtonDisplayed(),
                "User was not logged out and returned to the Welcome screen");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74265"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION})
    public void testLoginNotEntitledDOBInvalid() {
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage = new DisneyPlusEdnaDOBCollectionPageBase(getDriver());

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        Assert.assertTrue(ednaDOBCollectionPage.isOpened(), "Edna Date of Birth page did not open");
        ednaDOBCollectionPage.enterDOB(Person.OLDERTHAN200.getMonth(), Person.OLDERTHAN200.getDay(), Person.OLDERTHAN200.getYear());
        ednaDOBCollectionPage.tapSaveAndContinueButton();
        Assert.assertTrue(ednaDOBCollectionPage.isEdnaDateOfBirthFormatErrorPresent(),
                "Invalid DOB Message did not appear");
    }
}
