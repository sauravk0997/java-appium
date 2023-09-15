package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.utils.IOSUtils.DEVICE_TYPE;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_CREDENTIALS_ERROR;

public class DisneyPlusMoreMenuArielProfilesTest extends DisneyBaseTest {
    private static final String KIDS_DOB = "2018-01-01";
    private static final String WRONG_PASSWORD = "local123b456@";
    private static final String NO_ERROR_DISPLAYED = "error message was not displayed";
    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";
    private static final String NINETEEN_EIGHTY = "1980";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72379"})
    @Maintainer("gkrishna1")
    @Test(description = "Existing Profile, Minor U13-Authentication", groups = {"Ariel-More Menu"})
    public void verifyExistingProfileMinorAuth() {
        initialSetup();
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase password = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        String incorrectPasswordError = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());
        onboard();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        pause(3);
        moreMenu.clickMoreTab();
        //01-01-2018
        editProfilePage.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        updateProfilePage.tapSaveButton();
        softAssert.assertTrue(passwordPage.isOpened(), "Password entry modal is not shown after updating the profile");
        passwordPage.submitPasswordWhileLoggedIn(WRONG_PASSWORD);
        softAssert.assertEquals(loginPage.getErrorMessageString(), incorrectPasswordError, NO_ERROR_DISPLAYED);
        password.clickCancelBtn();
        iosUtils.get().terminateApp(buildType.getDisneyBundle());
        iosUtils.get().launchApp(buildType.getDisneyBundle());
        whoIsWatching.clickProfile(KIDS_PROFILE);
        softAssert.assertTrue(updateProfilePage.isOpened(), "'Let's update your profile' page is not opened after abandoning the authentication flow");
        editProfilePage.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        updateProfilePage.tapSaveButton();
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
            parentalConsent.scrollConsentContent(2);
        }
        parentalConsent.tapAgreeButton();
        softAssert.assertTrue(whoIsWatching.isOpened(), "Who is watching page didn't open after clicking on agree button");
        whoIsWatching.clickProfile(KIDS_PROFILE);
        softAssert.assertTrue(whoIsWatching.getDynamicCellByLabel("Mickey and Friends").isElementPresent(), "Kids Home page is not open after login");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72229"})
    @Test(description = " Edit Profile U13, Autoplay & Background video Off", groups = {"Ariel-More Menu"})
    public void verifyU13AutoplayAndBackgroundVideoOff() {
        initialSetup();
        DisneyPlusEditProfileIOSPageBase editProfiles = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase changePassword = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        disneyAccountApi.get().addProfile(disneyAccount.get(),KIDS_PROFILE,KIDS_DOB,disneyAccount.get().getProfileLang(),null,true,false);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfiles.clickEditModeProfile(KIDS_PROFILE);
        softAssert.assertEquals(editProfiles.getAutoplayState(),"Off", "Autoplay and Background video wasn't turned off by default for U13 Profile");
        editProfiles.toggleAutoplayButton("On");
        pause(4);
        changePassword.isHeadlineSubtitlePresent();
        softAssert.assertTrue(changePassword.isPasswordDescriptionPresent());
        changePassword.enterPassword(disneyAccount.get());
        softAssert.assertEquals(editProfiles.getAutoplayState(), "On","After authentication, 'Autoplay' was not turned 'ON' for U13 profile");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72463"})
    @Test(description = "Add profile U13, minor authentication", groups = {"Ariel-More Menu"})
    public void verifyAddProfileU13AuthenticationAbandonFlow() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        createKidsProfile();
        //Abandon the flow after DOB entry
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
        homePage.waitForHomePageToOpen();
        moreMenu.clickMoreTab();
        softAssert.assertFalse(addProfile.isProfilePresent(KIDS_PROFILE), "KIDS profile was created after abandoning the authentication flow");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72463"})
    @Test(description = "Add profile U13, minor authentication", groups = {"Ariel-More Menu"})
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72463"})
    @Test(description = "Add profile U13, minor authentication-Restriction ON", groups = {"Ariel-More Menu"})
    public void verifyAddProfileU13RestrictionONAuthentication() {
        initialSetup();
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        moreMenu.clickMoreTab();
        moreMenu.tapAccountTab();
        //Restrict Profile Creation toggle ON
        moreMenu.clickToggleView();
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        accountPage.isOpened();
        moreMenu.tapBackButton();
        pause(2);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());

        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        addProfile.tapJuniorModeToggle();
        addProfile.clickSaveBtn();
        //User shouldn't see password screen, instead they should directly go to consent screen.
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            new IOSUtils().scrollDown();
        }
        //Accept parental consent
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        softAssert.assertTrue(addProfile.isProfilePresent(KIDS_PROFILE), "Newly created profile is not seen on screen");
        softAssert.assertAll();
    }
    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72470"})
    @Test(description = "Add Profile U13-> Minor Consent Agree", groups = {"Ariel-More Menu"})
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
    @Test(description = "Add Profile U13-> Minor Consent Decline", groups = {"Ariel-More Menu"})
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
    @Test(description = "Add Profile U13-> Minor Consent Abandon Flow", groups = {"Ariel-More Menu"})
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
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72378"})
    @Test(description = "Profiles > Existing Profile U13-> Minor Consent Agree", groups = {"Ariel-More Menu"})
    public void verifyEditProfileU13MinorConsentAgree() {
        initialSetup();
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        onboard();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        //TODO:Bug: IOS-5032 DOB enter screen should be populated here.
        //Once bug is resolved, remove line 177
        moreMenu.clickMoreTab();
        softAssert.assertTrue(updateProfilePage.isOpened(), "Update your profile page is not shown after selecting kids profile");
        editProfilePage.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        updateProfilePage.tapSaveButton();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        //Consent screen validation
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertTrue(parentalConsent.validateConsentHeader(), "Consent header text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.validateConsentText(), "Consent text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.verifyPrivacyPolicyLink(), "Privacy Policy Link is not present on Consent screen");
        softAssert.assertTrue(parentalConsent.verifyChildrenPrivacyPolicyLink(), "Children's Privacy Policy Link is not present on Consent screen");

        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            softAssert.assertTrue(parentalConsent.validateScrollPopup(), "Alert verbiage doesn't match with the expected dict value");
            parentalConsent.clickAlertConfirm();
            new IOSUtils().scrollDown();
            //Accept parental consent
            new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        }
        softAssert.assertTrue(moreMenu.isExitKidsProfileButtonPresent(), "'Exit Kid's Profile' button not enabled.");
        moreMenu.clickHomeIcon();
        softAssert.assertTrue(whoIsWatching.getDynamicCellByLabel("Mickey and Friends").isElementPresent(), "Kids Home page is not open after login");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72378"})
    @Test(description = "Profiles > Existing Profile U13-> Minor Consent Decline", groups = {"Ariel-More Menu"})
    public void verifyEditProfileU13MinorConsentDecline() {
        initialSetup();
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        onboard();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        //TODO:Bug: IOS-5032 DOB enter screen should be populated here.
        //Once bug is resolved, remove line 217
        moreMenu.clickMoreTab();
        editProfilePage.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        updateProfilePage.tapSaveButton();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        //Consent screen validation
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        softAssert.assertTrue(whoIsWatching.getDynamicCellByLabel("Mickey and Friends").isPresent(), "Kids Home page is not open after login");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72470"})
    @Test(description = "Edit Profile U13-> Minor Consent Abandon Flow", groups = {"Ariel-More Menu"})
    public void verifyEditProfileU13MinorConsentAbandonFlow() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        onboard();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        pause(3);
        moreMenu.clickMoreTab();
        editProfilePage.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        updateProfilePage.tapSaveButton();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        //Abandon the flow
        terminateApp(buildType.getDisneyBundle());
        relaunch();
        //Select KIDS profile
        whoIsWatching.clickProfile(KIDS_PROFILE);
        //TODO: Bug created IOS-5038
        softAssert.assertTrue(updateProfilePage.isOpened(), "Update your profile page is not shown after abandoning the consent flow");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72334"})
    @Test(description = "Ads Tier User > Co-viewing > Profile Settings", groups = {"Ariel-More Menu"})
    public void verifyAdTierUserCoViewing() {
        initialSetup(R.CONFIG.get("locale"), R.CONFIG.get("language"), BUNDLE_BASIC);
        setAppToHomeScreen(disneyAccount.get());
        setFlexWelcomeConfig();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);

        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        pause(2);
        editProfilePage.clickEditModeProfile(disneyAccount.get().getFirstName());
        editProfilePage.getGroupWatchAndShareplay().click();
        Assert.assertTrue(editProfilePage.getGroupWatchAndShareplayTooltip().isPresent(), "GroupWatch and Shareplay tooltip is not shown on tapping groupwatch cell");
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72869"})
    @Test(description = "Profiles > U13 profile, Password action grant for Welch", groups = {"Ariel-More Menu"})
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

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69677"})
    @Test(description = "Verify the flows when Profile Creation is restricted", groups = {"Ariel-More Menu"})
    public void verifyProfileCreationRestrictedFunctionality() {
        initialSetup();
        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());

        disneyPlusAccountIOSPageBase.toggleRestrictProfileCreation(IOSUtils.ButtonStatus.ON);

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon toggling 'Restrict Profile Creation'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase1 = new DisneyPlusAccountIOSPageBase(getDriver());
        sa.assertTrue(disneyPlusAccountIOSPageBase1.isRestrictProfileCreationEnabled(),
                "'Restrict Profile Creation' toggle was not enabled after submitting credentials");

        disneyPlusAccountIOSPageBase.getBackArrow().click();
        disneyPlusMoreMenuIOSPageBase.clickAddProfile();

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon clicking 'Add Profile'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());

        try {
            disneyPlusEditProfileIOSPageBase.clickSkipBtn();
            disneyPlusEditProfileIOSPageBase.enterProfileName(RESTRICTED);
            disneyPlusEditProfileIOSPageBase.enterDOB(DateHelper.Month.JANUARY, FIRST, NINETEEN_EIGHTY);
            disneyPlusEditProfileIOSPageBase.chooseGender();
            disneyPlusEditProfileIOSPageBase.clickSaveBtn();
            disneyPlusEditProfileIOSPageBase.clickPrimaryButton();
            disneyPlusEditProfileIOSPageBase.clickSecondaryButtonByCoordinates();

            sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed(RESTRICTED),
                    "Profile created after submitting credentials was not saved");
        } catch (NoSuchElementException e) {
            sa.fail("Could not create a profile after submitting user credentials.");
        }

        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73220"})
    @Test(description = "U13 profile, Password action grant for Welch with RES ON", groups = {"Ariel-More Menu"})
    public void verifyU13RestrictionOnWelchActionGrant() {
        initialSetup();
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        moreMenu.clickMoreTab();
        moreMenu.tapAccountTab();
        //Restrict Profile Creation toggle ON
        moreMenu.clickToggleView();
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());
        accountPage.isOpened();
        moreMenu.tapBackButton();
        pause(2);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        passwordPage.submitPasswordWhileLoggedIn(disneyAccount.get().getUserPass());

        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        addProfile.clickSaveProfileButton();
        //Consent authentication
        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            new IOSUtils().scrollDown();
        }
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel("CONTINUE"), 50, 50);
        //Welch Full catalog access
        new MobileUtilsExtended().clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        Assert.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "'Confirm with your password page' was displayed after selecting full catalog when profile Res was ON");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        Assert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72663"})
    @Test(description = "Kids Profile new copy and rename to Junior Mode", groups = {"Ariel-More Menu"})
    public void verifyJuniorModeCopy() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        disneyAccountApi.get().addProfile(disneyAccount.get(), KIDS_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), null, true, true);
        setAppToHomeScreen(disneyAccount.get());

        whoIsWatching.clickProfile("Test");
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        sa.assertTrue(addProfile.isJuniorModeTextPresent(), "Junior mode text was not present on add profile page");
        addProfile.tapCancelButton();
        addProfile.tapBackButton();
        moreMenu.clickMoreTab();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        moreMenu.clickMoreTab();
        sa.assertEquals(moreMenu.getExitKidsProfileButtonText(),"EXIT JUNIOR MODE", "Exit junior mode verbiage doesn't match the expected value");
        moreMenu.tapExitKidsProfileButton();
        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        new IOSUtils().scrollDown();
        pause(1); //to handle transition
        sa.assertTrue(addProfile.isJuniorModeTextPresent(), "Junior mode text was not present on edit profile page");
        editProfilePage.clickDoneBtn();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72365"})
    @Test(description = "Profiles > Existing Sub->edit gender", groups = {"Ariel-More Menu"})
    public void verifyEditGenderPageUI() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusEditGenderIOSPageBase editGenderPage = initPage(DisneyPlusEditGenderIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(disneyAccount.get().getFirstName());
        editProfilePage.clickGenderButton();

        sa.assertTrue(editGenderPage.isOpened(), "Expected: 'Select Gender' page should be opened");

        editGenderPage.clickGenderDropDown();

        // verify all gender option
        for (DisneyPlusEditGenderIOSPageBase.GenderOption genderItem : DisneyPlusEditGenderIOSPageBase.GenderOption.values()) {
            sa.assertTrue(editGenderPage.isGenderOptionPresent(genderItem),
                    "Expected: " + genderItem + " option should be present");
        }

        editGenderPage.selectGender(DisneyPlusEditGenderIOSPageBase.GenderOption.GENDER_MEN.getGenderOption());
        editGenderPage.tapSaveButton();

        sa.assertTrue(editProfilePage.isUpdatedToastPresent(), "Gender is not updated for user");
        sa.assertAll();
    }

    private void setAppToAccountSettings() {
        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    private void onboard() {
        setAppToHomeScreen(disneyAccount.get());
        disneyAccountApi.get().addProfile(disneyAccount.get(),KIDS_PROFILE,disneyAccount.get().getProfileLang(),null,true);
        pause(3);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
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
