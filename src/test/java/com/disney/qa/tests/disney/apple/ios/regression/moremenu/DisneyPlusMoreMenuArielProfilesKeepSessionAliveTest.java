package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.utils.IOSUtils.DEVICE_TYPE;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;

public class DisneyPlusMoreMenuArielProfilesKeepSessionAliveTest extends DisneyBaseTest {

    private static final String NO_ERROR_DISPLAYED = "error message was not displayed";
    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72463"})
    @Test(description = "Add profile U13, minor authentication", groups = {"Ariel-More Menu-KSA"})
    public void verifyAddProfileU13AuthenticationIncorrectPassword() {
        initialSetup();
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        String invalidPasswordError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_CREDENTIALS_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        //wait for action grant to expire
        passwordPage.keepSessionAlive(15, passwordPage.getHomeNav());
        createKidsProfile();
        passwordPage.submitPasswordWhileLoggedIn("IncorrectPassword!123");
        //Verify that error is shown on screen
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidPasswordError, NO_ERROR_DISPLAYED);
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72470"})
    @Test(description = "Add Profile U13-> Minor Consent Agree", groups = {"Ariel-More Menu-KSA"})
    public void  verifyAddProfileU13MinorConsentAgree() {
        initialSetup();
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        //wait for action grant to expire
        addProfile.keepSessionAlive(15, addProfile.getHomeNav());
        createKidsProfile();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        //Consent screen validation
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertTrue(parentalConsent.validateConsentHeader(), "Consent header text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.validateConsentText(), "Consent text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.verifyPrivacyPolicyLink(), "Privacy Policy Link is not present on Consent screen");
        softAssert.assertTrue(parentalConsent.verifyChildrenPrivacyPolicyLink(), "Children's Privacy Policy Link is not present on Consent screen");
        //TODO: Not able to tap Agree/Decline button using IDs, fix this issue in iOS code(parentalConsent.tapAgreeButton();)
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            softAssert.assertTrue(parentalConsent.validateScrollPopup(), "Alert verbiage doesn't match with the expected dict value");
            parentalConsent.clickAlertConfirm();
            new IOSUtils().scrollDown();
            //Accept parental consent
            new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        }
        //Verify KIDS profile is created
        softAssert.assertTrue(addProfile.isProfilePresent(KIDS_PROFILE), "Newly created profile is not seen on screen");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72470"})
    @Test(description = "Add Profile U13-> Minor Consent Decline", groups = {"Ariel-More Menu-KSA"})
    public void verifyAddProfileU13MinorConsentDecline() {
        initialSetup();
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        //wait for action grant to expire
        addProfile.keepSessionAlive(15, addProfile.getHomeNav());
        createKidsProfile();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        //Decline consent
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        //Verify KIDS profile is created
        softAssert.assertTrue(addProfile.isProfilePresent(KIDS_PROFILE), "Newly created profile is not seen on screen");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72973"})
    @Test(description = "Add Profile U13-> Minor Consent Abandon Flow", groups = {"Ariel-More Menu-KSA"})
    public void verifyAddProfileU13MinorConsentAbandonFlow() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        //wait for action grant to expire
        addProfile.keepSessionAlive(15, addProfile.getHomeNav());
        createKidsProfile();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        //Abandon the flow
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
        homePage.waitForHomePageToOpen();
        moreMenu.clickMoreTab();
        //verify that kids profile is not saved
        softAssert.assertFalse(addProfile.isProfilePresent(KIDS_PROFILE), "KIDS profile was created after abandoning the consent flow");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72869"})
    @Test(description = "Profiles > U13 profile, Password action grant for Welch", groups = {"Ariel-More Menu-KSA"})
    public void verifyU13PasswordGrantForWelch() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        passwordPage.keepSessionAlive(15, passwordPage.getHomeNav());
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        addProfile.clickSaveProfileButton();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            new IOSUtils().scrollDown();
        }
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        softAssert.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "Confirm with your password page was displayed after selecting full catalog");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        softAssert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        softAssert.assertAll();
    }

    private void createKidsProfile() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        addProfile.tapJuniorModeToggle();
        addProfile.clickSaveProfileButton();
    }

    @Maintainer("acadavidcorrea")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72668"})
    @Test(description = "SUF – Password prompt when action grant expires", groups = {"Onboarding"})
    public void testPasswordPromptExpires() {
        initialSetup();
        SoftAssert softAssert = new SoftAssert();
        handleAlert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        login(disneyAccountApi.get().createAccount("US", "en"));

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
        pause(900);


    }
}
