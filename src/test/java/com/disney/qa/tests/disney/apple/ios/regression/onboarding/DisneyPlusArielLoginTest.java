package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.offer.pojos.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;

public class DisneyPlusArielLoginTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72231"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testLoginDobUnder18() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage = new DisneyPlusEdnaDOBCollectionPageBase(getDriver());
        DisneyPlusDOBCollectionPageBase disneyPlusDOBCollectionPageBase = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusAccountIsMinorIOSPageBase disneyPlusAccountIsMinorIOSPageBase = new DisneyPlusAccountIsMinorIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        //Create Disney account without DOB and Gender
        getDefaultCreateUnifiedAccountRequest()
                .setDateOfBirth(null)
                .setGender(null)
                .setPartner(Partner.DISNEY)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()));

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getUnifiedAccount().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(getUnifiedAccount().getUserPass());

        disneyPlusDOBCollectionPageBase.isOpened();
        ednaDOBCollectionPage.enterDOB(Person.MINOR.getMonth(), Person.MINOR.getDay(), Person.MINOR.getYear());
        ednaDOBCollectionPage.tapSaveAndContinueButton();

        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.isOpened(),
                "Contact CS screen did not appear.");
        disneyPlusAccountIsMinorIOSPageBase.clickHelpCenterButton();
        moreMenu.goBackToDisneyAppFromSafari();

        disneyPlusAccountIsMinorIOSPageBase.clickDismissButton();
        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isLogInButtonDisplayed(),
                "User was not logged out and returned to the Welcome screen");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74265"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testLoginNotEntitledDOBInvalid() {
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage = new DisneyPlusEdnaDOBCollectionPageBase(getDriver());

        //Create Disney account without DOB and Gender
        getDefaultCreateUnifiedAccountRequest()
                .setDateOfBirth(null)
                .setGender(null)
                .setPartner(Partner.DISNEY)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
        Assert.assertTrue(ednaDOBCollectionPage.isOpened(), "Edna Date of Birth page did not open");
        ednaDOBCollectionPage.enterDOB(Person.OLDERTHAN200.getMonth(), Person.OLDERTHAN200.getDay(), Person.OLDERTHAN200.getYear());
        ednaDOBCollectionPage.tapSaveAndContinueButton();
        Assert.assertTrue(ednaDOBCollectionPage.isEdnaDateOfBirthFormatErrorPresent(),
                "Invalid DOB Message did not appear");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67749"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void testForgotPasswordOTPPage() {
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(
                DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));

        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();

        loginPage.submitEmail(getUnifiedAccount().getEmail());
        Assert.assertTrue(passwordPage.isPasswordPagePresent(), "Password page did not open");
        Date startTime = getEmailApi().getStartTime();
        passwordPage.clickHavingTroubleLoggingButton();

        String otp = getOTPFromApi(startTime, getUnifiedAccount());
        oneTimePasscodePage.enterOtp(otp);
        oneTimePasscodePage.clickPrimaryButton();
        handleGenericPopup(5,1);
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }
}
