package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_CREDENTIALS_ERROR;

public class DisneyPlusMoreMenuArielProfilesKeepSessionAliveTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String NO_ERROR_DISPLAYED = "error message was not displayed";
    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";

    private static final String WRONG_PASSWORD = "local123b456@";

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72433"})
    @Test(description = "Add profile U13, minor authentication", groups = {"Ariel-More Menu-KSA", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfileU13AuthenticationIncorrectPassword() {
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        String invalidPasswordError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_CREDENTIALS_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getAccount());
        //wait for action grant to expire
        passwordPage.keepSessionAlive(15, passwordPage.getHomeNav());
        createKidsProfile();
        passwordPage.submitPasswordWhileLoggedIn("IncorrectPassword!123");
        //Verify that error is shown on screen
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidPasswordError, NO_ERROR_DISPLAYED);
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75275"})
    @Test(description = "Add Profile U13-> Minor Consent Agree", groups = {"Ariel-More Menu-KSA", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void  verifyAddProfileU13MinorConsentAgree() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getAccount());
        //wait for action grant to expire
        addProfile.keepSessionAlive(15, addProfile.getHomeNav());
        createKidsProfile();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
        //Consent screen validation
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertTrue(parentalConsent.validateConsentHeader(), "Consent header text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.validateConsentText(), "Consent text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.verifyPrivacyPolicyLink(), "Privacy Policy Link is not present on Consent screen");
        softAssert.assertTrue(parentalConsent.verifyChildrenPrivacyPolicyLink(), "Children's Privacy Policy Link is not present on Consent screen");
        //TODO: Not able to tap Agree/Decline button using IDs, fix this issue in iOS code(parentalConsent.tapAgreeButton();)
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            softAssert.assertTrue(parentalConsent.validateScrollPopup(), "Alert verbiage doesn't match with the expected dict value");
            parentalConsent.clickAlertConfirm();
            parentalConsent.scrollConsentContent(4);
            //Accept parental consent
            clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        }
        //Verify KIDS profile is created
        softAssert.assertTrue(addProfile.isProfilePresent(KIDS_PROFILE), "Newly created profile is not seen on screen");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75274"})
    @Test(description = "Add Profile U13-> Minor Consent Decline", groups = {"Ariel-More Menu-KSA", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfileU13MinorConsentDecline() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getAccount());
        //wait for action grant to expire
        addProfile.keepSessionAlive(15, addProfile.getHomeNav());
        createKidsProfile();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        //Decline consent
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        //Verify KIDS profile is created
        softAssert.assertTrue(addProfile.isProfilePresent(KIDS_PROFILE), "Newly created profile is not seen on screen");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74470"})
    @Test(description = "Add Profile U13-> Minor Consent Abandon Flow", groups = {"Ariel-More Menu-KSA", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfileU13MinorConsentAbandonFlow() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getAccount());
        //wait for action grant to expire
        addProfile.keepSessionAlive(15, addProfile.getHomeNav());
        createKidsProfile();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72953"})
    @Test(description = "Profiles > U13 profile, Password action grant for Welch", groups = {"Ariel-More Menu-KSA", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyU13PasswordGrantForWelch() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(getAccount());
        passwordPage.keepSessionAlive(15, passwordPage.getHomeNav());
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        addProfile.clickSaveProfileButton();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            parentalConsent.scrollConsentContent(4);
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        softAssert.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "Confirm with your password page was displayed after selecting full catalog");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        softAssert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75277"})
    @Maintainer("gkrishna1")
    @Test(description = "Existing Profile, Minor U13-Authentication", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyExistingProfileMinorAuth() {
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase password = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);

        SoftAssert softAssert = new SoftAssert();
        String incorrectPasswordError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());
        setAppToHomeScreen(getAccount());
        getAccountApi().addProfile(getAccount(),KIDS_PROFILE,getAccount().getProfileLang(),null,true);

        //wait for 15 min to expire the current action grant
        addProfile.keepSessionAlive(15, addProfile.getHomeNav());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(KIDS_PROFILE);
        pause(3);
        moreMenu.clickMoreTab();
        //01-01-2018
        editProfilePage.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        softAssert.assertTrue(passwordPage.isOpened(), "Password entry modal is not shown after updating the profile");
        passwordPage.submitPasswordWhileLoggedIn(WRONG_PASSWORD);
        softAssert.assertEquals(loginPage.getErrorMessageString(), incorrectPasswordError, NO_ERROR_DISPLAYED);
        password.clickCancelButton();
        terminateApp(buildType.getDisneyBundle());
        launchApp(buildType.getDisneyBundle());
        whoIsWatching.clickProfile(KIDS_PROFILE);
        softAssert.assertTrue(updateProfilePage.isOpened(), "'Let's update your profile' page is not opened after abandoning the authentication flow");
        editProfilePage.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
            parentalConsent.scrollConsentContent(2);
        }
        parentalConsent.tapAgreeButton();
        softAssert.assertTrue(whoIsWatching.isOpened(), "Who is watching page didn't open after clicking on agree button");
        whoIsWatching.clickProfile(KIDS_PROFILE);
        softAssert.assertTrue(whoIsWatching.getDynamicCellByLabel("Mickey Mouse and Friends").isElementPresent(), "Kids Home page is not open after login");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66822"})
    @Test(description = "Profiles - Who's Watching - User does not see Profile Selection if returning before Two Hour Background Limit", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyProfileSelectionScreenBehaviourBeforeBackgroundLimit() {
        String errorMsg = "profile was not found";
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(getAccountApi().createAccount(getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_BASIC),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2));
        getAccountApi().addProfile(getAccount(), JUNIOR_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA,
                true, true);

        setAppToHomeScreen(getAccount(), DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(JUNIOR_PROFILE), JUNIOR_PROFILE + " " + errorMsg);
        moreMenu.clickSearchIcon();
        launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        pause(5);
        launchApp(buildType.getDisneyBundle());
        sa.assertTrue(searchPage.isOpened(), "Search page did not open");
        sa.assertFalse(whoIsWatching.isOpened(), "Who is watching screen/page was opened");
        searchPage.clickMoreTab();
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(JUNIOR_PROFILE), JUNIOR_PROFILE + " " + errorMsg);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(DEFAULT_PROFILE), DEFAULT_PROFILE + " " + errorMsg);
        LOGGER.info("If Other profile then default profile means Kids profile selected, then Exit Junior mode button should be displayed on more menu page");
        sa.assertFalse(moreMenu.isExitKidsProfileButtonPresent(), DEFAULT_PROFILE + " profile was not selected");
        sa.assertAll();
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
}
