package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.client.requests.*;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_CREDENTIALS_ERROR;

public class DisneyPlusMoreMenuArielProfilesKeepSessionAliveTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String NO_ERROR_DISPLAYED = "error message was not displayed";
    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";

    private static final String WRONG_PASSWORD = "local123b456@";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72433"})
    @Test(description = "Add profile U13, minor authentication", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfileU13AuthenticationIncorrectPassword() {
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        String invalidPasswordError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_CREDENTIALS_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        //wait for action grant to expire
        passwordPage.keepSessionAlive(15, passwordPage.getHomeNav());
        createKidsProfile();
        passwordPage.submitPasswordWhileLoggedIn("IncorrectPassword!123");
        //Verify that error is shown on screen
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), invalidPasswordError, NO_ERROR_DISPLAYED);
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72953"})
    @Test(description = "Profiles > U13 profile, Password action grant for Welch", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyU13PasswordGrantForWelch() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        passwordPage.keepSessionAlive(15, passwordPage.getHomeNav());
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        addProfile.clickSaveProfileButton();
        //Consent authentication
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            parentalConsent.scrollConsentContent(4);
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        softAssert.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "Confirm with your password page was displayed after selecting full catalog");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        softAssert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75277"})
    @Test(description = "Existing Profile, Minor U13-Authentication", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
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
        setAppToHomeScreen(getUnifiedAccount());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true).build());

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
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
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
