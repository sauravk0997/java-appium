package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.*;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.offer.pojos.*;
import com.disney.qa.api.pojos.*;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.TestGroup;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

import static com.disney.qa.common.DisneyAbstractPage.FIFTEEN_HUNDRED_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BASIC_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusMoreMenuArielProfilesTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String KIDS_DOB = "2018-01-01";
    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";
    private static final String ESPAÑOL = "Español";
    private static final String ENGLISH_US = "English (US)";
    private static final String NEW_PROFILE_NAME = "New Name";
    private static final String OFF = "Off";
    private static final String ON = "On";
    private static final String KIDS_PROFILE_AUTOPLAY_NOT_TURNED_OFF_ERROR_MESSAGE = "Kids profile autoplay was not turned off";
    private static final String KIDS_PROFILE_AUTOPLAY_NOT_TURNED_ON_ERROR_MESSAGE = "Kids profile autoplay was not turned on";
    private static final String UPDATED_TOAST_NOT_FOUND_ERROR_MESSAGE = "Updated toast was not found";
    private static final String JUNIOR_MODE_TEXT_ERROR_MESSAGE = "Junior mode text was not present on edit profile page";
    private static final String TURN_OFF_KIDS_PASSWORD_ERROR_MESSAGE = "Turn off kids profile password body is not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72172"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyU13AutoplayAndNoGenderField() {
        DisneyPlusEditProfileIOSPageBase editProfiles = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase changePassword = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        UnifiedAccount testAccount = getUnifiedAccount();
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(testAccount, DEFAULT_PROFILE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfiles.clickEditModeProfile(KIDS_PROFILE);
        sa.assertTrue(editProfiles.isEditTitleDisplayed(), "Edit profile page not opened");
        sa.assertFalse(editProfiles.isGenderFieldPresent(), "Gender field was found");
        sa.assertEquals(editProfiles.getAutoplayState(),OFF, "Autoplay and Background video wasn't turned off by default for U13 Profile");
        editProfiles.toggleAutoplayButton(ON);
        sa.assertTrue(changePassword.isPasswordDescriptionPresent(), "Password screen was not opened");
        changePassword.enterPasswordNoAccount(INVALID_PASSWORD);
        sa.assertTrue(changePassword.isInvalidPasswordErrorDisplayed(), "Invalid Password error was not displayed");
        changePassword.enterPassword(getUnifiedAccount());
        sa.assertEquals(editProfiles.getAutoplayState(), ON, "After authentication, 'Autoplay' was not turned 'ON' for U13 profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74468"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAddProfileU13AuthenticationAbandonFlow() {
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        createKidsProfile();
        pause(5);
        //Abandon the flow after DOB entry
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72433"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAddProfileU13RestrictionONAuthentication() {
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.tapAccountTab();
        //Restrict Profile Creation toggle ON
        moreMenu.clickToggleView();
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        accountPage.isOpened();
        moreMenu.tapBackButton();
        pause(2);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());

        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        addProfile.tapJuniorModeToggle();
        addProfile.clickSaveBtn();
        //User shouldn't see password screen, instead they should directly go to consent screen.
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            //For iPhone 8 or some other small devices need to scroll more time to read full consent/terms
            parentalConsent.scrollConsentContent(4);
        }
        //Accept parental consent
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        softAssert.assertTrue(addProfile.isProfilePresent(KIDS_PROFILE), "Newly created profile is not seen on screen");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75277"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyEditProfileU13MinorConsentAgree() {
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

        //Consent screen validation
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertTrue(parentalConsent.validateConsentHeader(), "Consent header text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.validateConsentText(), "Consent text doesn't match with the expected dict values");
        softAssert.assertTrue(parentalConsent.verifyPrivacyPolicyLink(), "Privacy Policy Link is not present on Consent screen");
        softAssert.assertTrue(parentalConsent.verifyChildrenPrivacyPolicyLink(), "Children's Privacy Policy Link is not present on Consent screen");

        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            softAssert.assertTrue(parentalConsent.validateScrollPopup(), "Alert verbiage doesn't match with the expected dict value");
            parentalConsent.clickAlertConfirm();
            scrollDown();
            //Accept parental consent
            clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        }
        softAssert.assertTrue(moreMenu.isExitKidsProfileButtonPresent(), "'Exit Kid's Profile' button not enabled.");
        moreMenu.clickHomeIcon();
        softAssert.assertTrue(whoIsWatching.getDynamicCellByLabel("Mickey Mouse and Friends").isElementPresent(), "Kids Home page is not open after login");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75276"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyEditProfileU13MinorConsentDecline() {
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
        editProfilePage.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        //Consent screen validation
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        softAssert.assertTrue(whoIsWatching.getDynamicCellByLabel("Mickey Mouse and Friends").isPresent(), "Kids Home page is not open after login");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74470"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileU13MinorConsentAbandonFlow() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        onboard();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        pause(3);
        moreMenu.clickMoreTab();
        editProfilePage.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        //Abandon the flow
        terminateApp(buildType.getDisneyBundle());
        relaunch();
        //Select KIDS profile
        whoIsWatching.clickProfile(KIDS_PROFILE);
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72334"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAdTierUserCoViewing() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        pause(2);
        editProfilePage.clickEditModeProfile(getUnifiedAccount().getFirstName());
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            swipeUp(400);
        }
        editProfilePage.waitForPresenceOfAnElement(editProfilePage.getSharePlay());
        editProfilePage.getSharePlay().click();
        Assert.assertTrue(editProfilePage.getSharePlayTooltip().isPresent(), "SharePlay tooltip is not shown on tapping on SharePlay cell");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69535"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.PROFILES, TestGroup.SMOKE, US})
    public void verifyProfileCreationRestrictedFunctionality() {
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        setAppToAccountSettings();
        Assert.assertTrue(accountPage.isOpened(), ACCOUNT_PAGE_NOT_DISPLAYED);

        accountPage.toggleRestrictProfileCreation();
        Assert.assertTrue(passwordPage.isOpened(), ENTER_PASSWORD_PAGE_NOT_DISPLAYED);

        passwordPage.enterPassword(getUnifiedAccount());
        editProfilePage.waitForUpdatedToastToDisappear();
        Assert.assertTrue(accountPage.isOpened(), ACCOUNT_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(accountPage.isRestrictProfileCreationValueExpected("1"),
                "'Restrict Profile Creation' toggle was not enabled after submitting credentials");

        accountPage.clickNavBackBtn();
        moreMenuPage.clickAddProfile();
        Assert.assertTrue(passwordPage.isOpened(), ENTER_PASSWORD_PAGE_NOT_DISPLAYED);

        passwordPage.enterPassword(getUnifiedAccount());
        editProfilePage.clickSkipBtn();
        editProfilePage.enterProfileName(RESTRICTED);
        editProfilePage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        editProfilePage.chooseGender();
        editProfilePage.clickSaveBtn();
        editProfilePage.clickPrimaryButton();
        editProfilePage.clickSecondaryButtonByCoordinates();
        Assert.assertTrue(moreMenuPage.isProfileSwitchDisplayed(RESTRICTED),
                "Profile created after submitting credentials was not saved");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73220"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyU13RestrictionOnWelchActionGrant() {
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.tapAccountTab();
        //Restrict Profile Creation toggle ON
        moreMenu.clickToggleView();
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        accountPage.isOpened();
        moreMenu.tapBackButton();
        pause(2);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());

        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        addProfile.clickSaveProfileButton();
        //Consent authentication
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            scrollDown();
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("CONTINUE"), 50, 50);
        //Welch Full catalog access
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        Assert.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "'Confirm with your password page' was displayed after selecting full catalog when profile Res was ON");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        Assert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72683"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyJuniorModeCopy() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount());

        whoIsWatching.clickProfile(DEFAULT_PROFILE);
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
        scrollDown();
        pause(1); //to handle transition
        sa.assertTrue(addProfile.isJuniorModeTextPresent(), "Junior mode text was not present on edit profile page");
        editProfilePage.clickDoneBtn();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75637"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditGenderPageUI() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusEditGenderIOSPageBase editGenderPage = initPage(DisneyPlusEditGenderIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(getUnifiedAccount().getFirstName());
        editProfilePage.clickGenderButton();

        sa.assertTrue(editGenderPage.isOpened(), "Expected: 'Select Gender' page should be opened");

        editGenderPage.clickGenderDropDown();

        // verify all gender option
        editProfilePage.validateOptionsInGenderDropdown();

        editGenderPage.getTypeButtonByLabel(editGenderPage.selectGender(DisneyPlusEditGenderIOSPageBase.GenderOption.GENDER_MEN)).click();
        editGenderPage.tapSaveButton();

        sa.assertTrue(editProfilePage.isUpdatedToastPresent(), "Gender is not updated for user");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72475"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyNoGenderForU13Profiles() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());

        // verify gender field is disabled when you select U13 DOB
        sa.assertFalse(addProfile.isGenderFieldEnabled(),
                "Gender field is enabled for U13 profile");

        addProfile.tapCancelButton();
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.chooseGender();
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());

        // verify gender field is disabled when you selected Gender first then choose the U13 DOB
        sa.assertFalse(addProfile.isGenderFieldEnabled(),
                "Gender field is enabled for U13 profile");

        addProfile.clickSaveBtn();
        //minor consent is shown
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            scrollDown();
        }

        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("CONTINUE"), 50, 50);
        //Welch Full catalog access
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        //minor authentication is prompted
        Assert.assertTrue(passwordPage.isConfirmWithPasswordTitleDisplayed(), "'Confirm with your password page' was displayed after selecting full catalog when profile Res was ON");
        passwordPage.enterPassword(getUnifiedAccount());
        passwordPage.clickSecondaryButtonByCoordinates();
        Assert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72172"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyNoGenderForU18Profiles() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.U18.getMonth(), Person.U18.getDay(), Person.U18.getYear());

        // verify gender field is disabled when you select U18 DOB
        sa.assertFalse(addProfile.isGenderFieldEnabled(),
                "Gender field is enabled for U18 profile");

        addProfile.tapCancelButton();
        avatars[0].click();
        addProfile.enterProfileName(JUNIOR_PROFILE);
        addProfile.chooseGender();
        addProfile.enterDOB(Person.U18.getMonth(), Person.U18.getDay(), Person.U18.getYear());

        // verify gender field is disabled when you selected Gender first then choose the U18 DOB
        sa.assertFalse(addProfile.isGenderFieldEnabled(),
                "Gender field is enabled for U18 profile");

        addProfile.clickSaveBtn();

        //Welch Full catalog access
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        sa.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "Confirm with your password page was displayed after selecting full catalog");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        sa.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72682"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyArielAddProfileJuniorModeUI() {
        setAppToHomeScreen(getUnifiedAccount());
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        addProfile.waitForLoaderToDisappear(TEN_SEC_TIMEOUT);
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        //verify Learn More hyperlink on add profile page
        sa.assertTrue(addProfile.isKidProfileSubCopyPresent(), "Kid Profile sub copy was not present");
        editProfilePage.clickJuniorModeLearnMoreLink();
        sa.assertTrue(moreMenu.isHelpWebviewOpen(), "'Help' web view was not opened");
        pause(3);
        Assert.assertTrue(addProfile.verifyTextOnWebView(JUNIOR_MODE_HELP_CENTER), "User was not navigated to Junior mode help center");
        moreMenu.goBackToDisneyAppFromSafari();
        moreMenu.dismissNotificationsPopUp();
        Assert.assertTrue(addProfile.isAddProfilePageOpened(), "User was not returned to the add profile page after navigating back from safari");
        moreMenu.clickSaveProfileButton();
        //minor consent is shown
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            scrollDown();
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("CONTINUE"), 50, 50);
        //Welch Full catalog access
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        //minor authentication is prompted
        Assert.assertTrue(passwordPage.isConfirmWithPasswordTitleDisplayed(), "'Confirm with your password page' was displayed after selecting full catalog when profile Res was ON");
        passwordPage.enterPassword(getUnifiedAccount());
        passwordPage.clickSecondaryButtonByCoordinates();
        Assert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");

        //verify learn more hyperlink on edit profile page
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        editProfilePage.clickJuniorModeLearnMoreLink();
        sa.assertTrue(moreMenu.isHelpWebviewOpen(), "'Help' web view was not opened");
        Assert.assertTrue(editProfilePage.verifyTextOnWebView(JUNIOR_MODE_HELP_CENTER), "User was not navigated to Junior mode help center");
        moreMenu.goBackToDisneyAppFromSafari();
        moreMenu.dismissNotificationsPopUp();
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72162"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAddProfilePageInlineError() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusEditGenderIOSPageBase editGenderPage = initPage(DisneyPlusEditGenderIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();

        //Verify inline error for add profile page field if any field is empty
        addProfile.clickSaveBtn();
        sa.assertTrue(addProfile.isInlineErrorForProfileFieldPresent(), "Inline error for Profile field if profile field is empty is not present");
        sa.assertTrue(addProfile.isInlineErrorForDOBFieldPresent(), "Inline error for DOB field if DOB field is empty is not present");

        addProfile.enterProfileName(JUNIOR_PROFILE);

        //Verify inline error if user add DOB that is older than 125 year old
        addProfile.enterDOB(Person.OLDERTHAN125.getMonth(), Person.OLDERTHAN125.getDay(), Person.OLDERTHAN125.getYear());
        addProfile.clickSaveBtn();
        sa.assertTrue(addProfile.isInlineErrorForDOBFieldPresent(), "Inline error for DOB field if DOB is older than 125 year old is not present");

        //Verify inline error for gender field if gender field is empty
        addProfile.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        addProfile.clickSaveBtn();
        sa.assertTrue(addProfile.isInlineErrorForGenderFieldPresent(), "Inline error for Gender field if Gender field is empty is not present");

        // verify all gender option and user able to select gender value from dropdown
        addProfile.clickGenderDropDown();
        for (DisneyPlusEditGenderIOSPageBase.GenderOption genderItem : DisneyPlusEditGenderIOSPageBase.GenderOption.values()) {
            sa.assertTrue(editGenderPage.getTypeButtonByLabel(editGenderPage.selectGender(genderItem)).isPresent(),
                    "Expected: " + genderItem + " option should be present");
        }

        editGenderPage.selectGender(DisneyPlusEditGenderIOSPageBase.GenderOption.GENDER_WOMEN);

        addProfile.clickSaveBtn();

        /*Removing Welch Fill catalog access step due to change in requirements
            New additional 18+ profiles →  Default into TV-MA - else (<18), default to TV-14 and trigger Welch.
            https://jira.disneystreaming.com/browse/IOS-8383
            https://jira.disneystreaming.com/browse/PLTFRM-22119
        */

        sa.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "Confirm with your password page was displayed after selecting full catalog");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        sa.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72223"})
    @Test(groups = {TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEnforceDOBAndGenderAccountHolderCollectionScreen() {
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPageBase = initPage(DisneyPlusEdnaDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        //Create Disney account without DOB and Gender
        getDefaultCreateUnifiedAccountRequest()
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setPartner(Partner.DISNEY)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());

        sa.assertTrue(ednaDOBCollectionPageBase.isOpened(), "Edna enforce DOB collection page didn't open after login");
        sa.assertTrue(ednaDOBCollectionPageBase.isEdnaDateOfBirthDescriptionPresent(), "Edna enforce DOB Description is not displayed");

        //Verify Gender collection screen is displayed or not, after DOB collection page if existing account without DOb and Gender
        ednaDOBCollectionPageBase.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        ednaDOBCollectionPageBase.tapSaveAndContinueButton();
        sa.assertTrue(updateProfilePage.isOpened(), "Update profile page is not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72266"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPrimaryProfilesEditProfileDOBGender() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfiles = initPage(DisneyPlusEditProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();

        editProfiles.clickEditModeProfile(getUnifiedAccount().getProfile(DEFAULT_PROFILE).getProfileName());
        sa.assertTrue(editProfiles.isBirthdateHeaderDisplayed(), "Birthdate header is not displayed on the edit profiles screen");
        sa.assertTrue(editProfiles.isBirthdateDisplayed(getUnifiedAccount().getProfile(DEFAULT_PROFILE)),"Birthdate is not displayed on the edit profiles screen");
        sa.assertTrue(editProfiles.isGenderButtonPresent(), "Gender header is not displayed on edit profiles screen");
        sa.assertTrue(editProfiles.isGenderValuePresent(getUnifiedAccount().getProfile(DEFAULT_PROFILE)), "Gender value is not as expected on the edit profiles screen");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67081"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAddProfileBannerForPrimaryProfiles() {
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPageBase = initPage(DisneyPlusEdnaDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = initPage(DisneyPlusAddProfileBannerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

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
        ednaDOBCollectionPageBase.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        ednaDOBCollectionPageBase.waitForPresenceOfAnElement(ednaDOBCollectionPageBase.getSaveAndContinueButton());
        ednaDOBCollectionPageBase.clickElementAtLocation(ednaDOBCollectionPageBase.getSaveAndContinueButton(),
                50, 50);
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        updateProfilePage.chooseGender();
        updateProfilePage.tapSaveButton();

        //Verify add profile banner
        sa.assertTrue(addProfileBanner.isProfileHeaderPresent(), "Add Profile Banner Header is not present");
        sa.assertTrue(addProfileBanner.isProfileSubcopyPresent(), "Add Profile Banner sub copy is not present");
        sa.assertTrue(addProfileBanner.isDismissButtonPresent(), "Dismiss button on add profile banner is not present");
        sa.assertTrue(addProfileBanner.isAddProfileButtonPresent(), "Add profile button on add profile banner is not present");

        //Verify if user dismiss add profile banner, it will be not shown anymore
        addProfileBanner.tapDismissButton();
        sa.assertFalse(addProfileBanner.isProfileHeaderPresent(), "Add Profile Banner Header is present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67085"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAddProfilePageAfterClickingAddProfileButtonOnAddProfileBanner() {
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPageBase = initPage(DisneyPlusEdnaDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = initPage(DisneyPlusAddProfileBannerIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

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
        ednaDOBCollectionPageBase.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        ednaDOBCollectionPageBase.tapSaveAndContinueButton();
        updateProfilePage.chooseGender();
        updateProfilePage.tapSaveButton();
        addProfileBanner.tapAddProfileButton();

        //Verify if user click add profile on banner, add profile page is opened
        sa.assertTrue(chooseAvatarPage.isOpened(), "Choose Avatar Page is not opened");
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        sa.assertTrue(addProfile.isAddProfilePageOpened(), "Add Profile Page is not opened");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74482"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAddProfileBannerIsNotDisplayedForTheMaxAmountOfProfiles() {
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPageBase = initPage(DisneyPlusEdnaDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = initPage(DisneyPlusAddProfileBannerIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int Max = 6;

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

        //Add Max number of profile through API
        for (int i = 0; i < Max; i++) {
            getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                    .unifiedAccount(getUnifiedAccount())
                    .profileName(KIDS_PROFILE + i)
                    .dateOfBirth(KIDS_DOB)
                    .language(getLocalizationUtils().getUserLanguage())
                    .avatarId(BABY_YODA)
                    .kidsModeEnabled(true)
                    .isStarOnboarded(true)
                    .build());
        }

        ednaDOBCollectionPageBase.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        ednaDOBCollectionPageBase.waitForPresenceOfAnElement(ednaDOBCollectionPageBase.getSaveAndContinueButton());
        ednaDOBCollectionPageBase.clickElementAtLocation(ednaDOBCollectionPageBase.getSaveAndContinueButton(), 50, 50);
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        updateProfilePage.chooseGender();
        updateProfilePage.tapSaveButton();
        sa.assertTrue(whoIsWatching.isOpened(), "Who is watching page did not open");
        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        //Verify add profile banner is not displayed if user already have maximum amount of profiles, namely 7
        sa.assertFalse(addProfileBanner.isProfileHeaderPresent(), "Add Profile Banner Header is present");
        sa.assertTrue(homePage.isOpened(), "Home page did not open");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66818"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyEditProfileAppUILanguage() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAppLanguageIOSPageBase appLanguage = initPage(DisneyPlusAppLanguageIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getUnifiedAccount().getFirstName());
        editProfile.clickAppLanguage();

        sa.assertTrue(appLanguage.isOpened(), "App Language screen is not opened");
        sa.assertTrue(appLanguage.isAppLanguageHeaderPresent(), "App Language header is not present");
        sa.assertTrue(appLanguage.isAppLanguageCopyPresent(), "App Language copy is not present");
        sa.assertTrue(appLanguage.getBackButton().isElementPresent(), "Back button is not present");
        sa.assertTrue(appLanguage.isLanguageSelected(ENGLISH_US), "Language selected doesn't have the check mark");
        sa.assertTrue(appLanguage.isLanguageListShownInAlphabeticalOrder(), "Languages are not present in alphabetical order");
        appLanguage.getBackArrow().click();

        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Edit profile page is not opened");
        editProfile.clickAppLanguage();
        sa.assertTrue(appLanguage.isOpened(), "App Language screen is not opened");

        appLanguage.selectLanguage(ESPAÑOL);
        sa.assertTrue(editProfile.isUpdatedToastPresent(), "'Updated' toast was not found");

        editProfile.clickAppLanguage();
        sa.assertTrue(appLanguage.isLanguageSelected(ESPAÑOL), "Language was not changed to Spanish");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69503"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileKidsModeDisableJuniorMode() {
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount());

        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            editProfilePage.swipeUp(FIFTEEN_HUNDRED_SEC_TIMEOUT);
        }
        sa.assertTrue(editProfilePage.isJuniorModeTextPresent(), JUNIOR_MODE_TEXT_ERROR_MESSAGE);

        editProfilePage.getKidProofExitToggleSwitch().click();
        sa.assertTrue(passwordPage.isAuthPasswordKidsProfileBodyDisplayed(), TURN_OFF_KIDS_PASSWORD_ERROR_MESSAGE);

        passwordPage.enterLogInPassword(getUnifiedAccount().getUserPass());
        passwordPage.clickPrimaryButton();
        sa.assertTrue(editProfilePage.isUpdatedToastPresent(), UPDATED_TOAST_NOT_FOUND_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66807"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileDuplicateProfileError() {
        initialSetup();
        handleAlert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount());
        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);

        editProfilePage.enterProfileName(DEFAULT_PROFILE);
        editProfilePage.getDoneButton().click();
        sa.assertTrue(editProfilePage.isDuplicateProfileNameErrorPresent(), "Error `Duplicate Profile Name` is not present");

        editProfilePage.enterProfileName(NEW_PROFILE_NAME);
        editProfilePage.getDoneButton().click();
        homePage.clickMoreTab();
        sa.assertTrue(whoIsWatching.isAccessModeProfileIconPresent(NEW_PROFILE_NAME), "Profile name was not updated to " + NEW_PROFILE_NAME);
        sa.assertAll();
    }

    /*
     * This test is implemented as per https://jira.disneystreaming.com/browse/DMGZ-1710
     * This has bug ticket IOS-10586 as user able to enter and save with more than 12 character
     */
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66811"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMaxProfileCharacterCount() {
        String profileNameWithMaxAllowedCharLimit = "TestTestTest";
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(getUnifiedAccount().getFirstName());
        editProfilePage.enterProfileName(profileNameWithMaxAllowedCharLimit + "X");
        editProfilePage.getDoneButton().click();
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), "Home Page is not opened after saving profile with a new name");
        moreMenu.clickMoreTab();
        Assert.assertTrue(moreMenu.isProfileSwitchDisplayed(profileNameWithMaxAllowedCharLimit + "X"), "Profile name not saved with +Max allowed character limit");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76067"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileKidsAutoplay() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        //Default setting is autoplay off
        setAppToHomeScreen(getUnifiedAccount());
        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        homePage.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        sa.assertEquals(editProfilePage.getAutoplayState(), OFF, "Kids profile autoplay is not turned off by default.");

        switchAndValidateAutoplay(ON, sa, KIDS_PROFILE_AUTOPLAY_NOT_TURNED_ON_ERROR_MESSAGE);
        switchAndValidateAutoplay(OFF, sa, KIDS_PROFILE_AUTOPLAY_NOT_TURNED_OFF_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66822"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProfileSelectionScreenBehaviourBeforeBackgroundLimit() {
        String errorMsg = "profile was not found";
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75275"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void  verifyAddProfileU13MinorConsentAgree() {
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        createKidsProfile();
        //Consent screen validation
        sa.assertTrue(parentalConsent.isConsentHeaderPresent(),
                "Consent header was not present after minor auth");
        sa.assertTrue(parentalConsent.validateConsentHeader(),
                "Consent header text doesn't match with the expected dict values");
        sa.assertTrue(parentalConsent.validateConsentText(),
                "Consent text doesn't match with the expected dict values");
        sa.assertTrue(parentalConsent.verifyPrivacyPolicyLink(),
                "Privacy Policy Link is not present on Consent screen");
        sa.assertTrue(parentalConsent.verifyChildrenPrivacyPolicyLink(),
                "Children's Privacy Policy Link is not present on Consent screen");
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            sa.assertTrue(parentalConsent.validateScrollPopup(),
                    "Alert verbiage doesn't match with the expected dict value");
            parentalConsent.clickAlertConfirm();
            parentalConsent.scrollConsentContent(4);
            //Accept parental consent
            clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("CONTINUE"), 50, 50);
        Assert.assertTrue(parentalConsent.getTypeButtonByLabel(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.BTN_FULL_CATALOG.getText())).isPresent());
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75274"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAddProfileU13MinorConsentDecline() {
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        createKidsProfile();
        Assert.assertTrue(parentalConsent.isConsentHeaderPresent(),
                "Consent header was not present after minor auth");
        //Decline consent
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("CONTINUE"), 50, 50);
        Assert.assertTrue(parentalConsent.getTypeButtonByLabel(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.BTN_FULL_CATALOG.getText())).isPresent());
    }

    private void setAppToAccountSettings() {
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    private void onboard() {
        setAppToHomeScreen(getUnifiedAccount());
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(null)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
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
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        addProfile.clickSaveProfileButton();
    }

    private void switchAndValidateAutoplay(String state, SoftAssert sa, String errorMessage) {
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        editProfilePage.toggleAutoplayButton(state);
        passwordPage.enterPassword(getUnifiedAccount());
        sa.assertTrue(editProfilePage.isUpdatedToastPresent(), UPDATED_TOAST_NOT_FOUND_ERROR_MESSAGE);
        sa.assertEquals(editProfilePage.getAutoplayState(), state, errorMessage);
        editProfilePage.waitForUpdatedToastToDisappear();
        editProfilePage.getDoneButton().click();
        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        sa.assertEquals(editProfilePage.getAutoplayState(), state, errorMessage);
    }
}
