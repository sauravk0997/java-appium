package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;

public class DisneyPlusMoreMenuArielProfilesTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String KIDS_DOB = "2018-01-01";
    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";
    private static final String ESPAÑOL = "Español";
    private static final String ENGLISH_US = "English (US)";
    private static final String NEW_PROFILE_NAME = "New Name";

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72229"})
    @Test(description = " Edit Profile U13, Autoplay & Background video Off", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyU13AutoplayAndBackgroundVideoOff() {
        DisneyPlusEditProfileIOSPageBase editProfiles = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChangePasswordIOSPageBase changePassword = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getAccount());
        getAccountApi().addProfile(getAccount(),KIDS_PROFILE,KIDS_DOB,getAccount().getProfileLang(),null,true,false);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfiles.clickEditModeProfile(KIDS_PROFILE);
        softAssert.assertEquals(editProfiles.getAutoplayState(),"Off", "Autoplay and Background video wasn't turned off by default for U13 Profile");
        editProfiles.toggleAutoplayButton("On");
        pause(4);
        changePassword.isHeadlineSubtitlePresent();
        softAssert.assertTrue(changePassword.isPasswordDescriptionPresent());
        changePassword.enterPassword(getAccount());
        softAssert.assertEquals(editProfiles.getAutoplayState(), "On","After authentication, 'Autoplay' was not turned 'ON' for U13 profile");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74470"})
    @Test(description = "Add profile U13, minor authentication", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfileU13AuthenticationAbandonFlow() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();
        setAppToHomeScreen(getAccount());
        createKidsProfile();
        pause(5);
        //Abandon the flow after DOB entry
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72463"})
    @Test(description = "Add profile U13, minor authentication-Restriction ON", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfileU13RestrictionONAuthentication() {
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert softAssert = new SoftAssert();

        setAppToHomeScreen(getAccount());
        moreMenu.clickMoreTab();
        moreMenu.tapAccountTab();
        //Restrict Profile Creation toggle ON
        moreMenu.clickToggleView();
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
        accountPage.isOpened();
        moreMenu.tapBackButton();
        pause(2);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());

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

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72378"})
    @Test(description = "Profiles > Existing Profile U13-> Minor Consent Agree", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72378"})
    @Test(description = "Profiles > Existing Profile U13-> Minor Consent Decline", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74468"})
    @Test(description = "Edit Profile U13-> Minor Consent Abandon Flow", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyEditProfileU13MinorConsentAbandonFlow() {
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
        editProfilePage.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        //Abandon the flow
        terminateApp(buildType.getDisneyBundle());
        relaunch();
        //Select KIDS profile
        whoIsWatching.clickProfile(KIDS_PROFILE);
        //TODO: Bug created IOS-5038
        softAssert.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present after minor auth");
        softAssert.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72334"})
    @Test(description = "Ads Tier User > Co-viewing > Profile Settings", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAdTierUserCoViewing() {
        setAccount(getAccountApi().createAccount(getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_BASIC),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V1));
        setAppToHomeScreen(getAccount());
        //setFlexWelcomeConfig();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);

        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        pause(2);
        editProfilePage.clickEditModeProfile(getAccount().getFirstName());
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            swipeUp(400);
        }
        editProfilePage.waitForPresenceOfAnElement(editProfilePage.getSharePlay());
        editProfilePage.getSharePlay().click();
        Assert.assertTrue(editProfilePage.getSharePlayTooltip().isPresent(), "SharePlay tooltip is not shown on tapping on SharePlay cell");
    }

    @Maintainer("mboulogne1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69677"})
    @Test(description = "Verify the flows when Profile Creation is restricted", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyProfileCreationRestrictedFunctionality() {
        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());

        disneyPlusAccountIOSPageBase.toggleRestrictProfileCreation(IOSUtils.ButtonStatus.ON);

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon toggling 'Restrict Profile Creation'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(getAccount().getUserPass());

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase1 = new DisneyPlusAccountIOSPageBase(getDriver());
        sa.assertTrue(disneyPlusAccountIOSPageBase1.isRestrictProfileCreationEnabled(),
                "'Restrict Profile Creation' toggle was not enabled after submitting credentials");

        disneyPlusAccountIOSPageBase.getBackArrow().click();
        disneyPlusMoreMenuIOSPageBase.clickAddProfile();

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon clicking 'Add Profile'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(getAccount().getUserPass());

        try {
            disneyPlusEditProfileIOSPageBase.clickSkipBtn();
            disneyPlusEditProfileIOSPageBase.enterProfileName(RESTRICTED);
            disneyPlusEditProfileIOSPageBase.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72682"})
    @Test(description = "Profiles - Add Profile - Kids Profile / Junior Mode UI", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfileJuniorProfileUI() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(KIDS_PROFILE);
        addProfile.enterDOB(Person.MINOR.getMonth(), Person.MINOR.getDay(), Person.MINOR.getYear());
        addProfile.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        sa.assertTrue(addProfile.isJuniorModeTextPresent(), "Junior mode text was not present on add profile page");
        sa.assertTrue(addProfile.isKidProfileSubCopyPresent(), "Profile sub copy is not present");
        sa.assertTrue(editProfilePage.isLearnMoreLinkPresent(), "learn more hyper link is not found");
        addProfile.tapJuniorModeToggle();
        addProfile.clickSaveProfileButton();
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            parentalConsent.scrollConsentContent(4);
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
        sa.assertTrue(addProfile.isProfilePresent(KIDS_PROFILE), "Newly created profile is not seen on screen");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73220"})
    @Test(description = "U13 profile, Password action grant for Welch with RES ON", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyU13RestrictionOnWelchActionGrant() {
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        moreMenu.clickMoreTab();
        moreMenu.tapAccountTab();
        //Restrict Profile Creation toggle ON
        moreMenu.clickToggleView();
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());
        accountPage.isOpened();
        moreMenu.tapBackButton();
        pause(2);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        passwordPage.submitPasswordWhileLoggedIn(getAccount().getUserPass());

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
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        Assert.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "'Confirm with your password page' was displayed after selecting full catalog when profile Res was ON");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        Assert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72663"})
    @Test(description = "Kids Profile new copy and rename to Junior Mode", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyJuniorModeCopy() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(getAccount(), KIDS_PROFILE, KIDS_DOB, getAccount().getProfileLang(), null, true, true);
        setAppToHomeScreen(getAccount());

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
        scrollDown();
        pause(1); //to handle transition
        sa.assertTrue(addProfile.isJuniorModeTextPresent(), "Junior mode text was not present on edit profile page");
        editProfilePage.clickDoneBtn();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72365"})
    @Test(description = "Profiles > Existing Sub->edit gender", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyEditGenderPageUI() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusEditGenderIOSPageBase editGenderPage = initPage(DisneyPlusEditGenderIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(getAccount().getFirstName());
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72482"})
    @Test(description = "Profiles > Add profile, No Gender for U13 Profiles", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyNoGenderForU13Profiles() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
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
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        //minor authentication is prompted
        Assert.assertTrue(passwordPage.isConfirmWithPasswordTitleDisplayed(), "'Confirm with your password page' was displayed after selecting full catalog when profile Res was ON");
        passwordPage.enterPassword(getAccount());
        passwordPage.clickSecondaryButtonByCoordinates();
        Assert.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72817"})
    @Test(description = "Profiles > Add profile, No Gender for U18 Profiles", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyNoGenderForU18Profiles() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
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
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        sa.assertFalse(passwordPage.isConfirmWithPasswordTitleDisplayed(), "Confirm with your password page was displayed after selecting full catalog");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        passwordPage.clickSecondaryButtonByCoordinates();
        sa.assertTrue(passwordPage.getHomeNav().isPresent(), "Home page was not displayed after selecting not now");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72664"})
    @Test(description = " Profiles > Kids Profile new copy", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyLearnMoreLinkForKidsProfile() {
        setAccount(getAccountApi().createAccount(getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_BASIC),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V1));
        setAppToHomeScreen(getAccount());
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
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
        addProfile.clickSaveBtn();
        //minor consent is shown
        if ("Phone".equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            scrollDown();
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("DECLINE"), 50, 50);
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("CONTINUE"), 50, 50);
        //Welch Full catalog access
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())), 50, 50);
        //minor authentication is prompted
        Assert.assertTrue(passwordPage.isConfirmWithPasswordTitleDisplayed(), "'Confirm with your password page' was displayed after selecting full catalog when profile Res was ON");
        passwordPage.enterPassword(getAccount());
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72311"})
    @Test(description = "Profiles > Add profile, DOB and gender AAW", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAddProfilePageInlineError() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusEditGenderIOSPageBase editGenderPage = initPage(DisneyPlusEditGenderIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
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
            sa.assertTrue(editGenderPage.isGenderOptionPresent(genderItem),
                    "Expected: " + genderItem + " option should be present");
        }

        editGenderPage.selectGender(DisneyPlusEditGenderIOSPageBase.GenderOption.GENDER_WOMEN.getGenderOption());

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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72366"})
    @Test(description = "Profiles > Existing Subscribers, enforce DOB Account Holder collection", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyEnforceDOBAndGenderAccountHolderCollectionScreen() {
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEnforceDOBCollectionPageBase enforceDOBCollectionPage = initPage(DisneyPlusEnforceDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        //Create Disney account without DOB and Gender
        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());

        sa.assertTrue(enforceDOBCollectionPage.isOpened(), "Enforce DOB collection page didn't open after login");

        //Verify if user terminate app without saving DOB, app will keep showcasing Enforce DOB screen
        terminateApp(buildType.getDisneyBundle());
        launchApp(buildType.getDisneyBundle());
        sa.assertTrue(enforceDOBCollectionPage.isOpened(), "Enforce DOB collection page didn't open after login");
        sa.assertTrue(enforceDOBCollectionPage.isDateOfBirthDescriptionPresent(), "Enforce DOB Description is not displayed");

        //Verify Gender collection screen is displayed or not, after DOB collection page if existing account without DOb and Gender
        enforceDOBCollectionPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        enforceDOBCollectionPage.clickPrimaryButton();
        sa.assertTrue(updateProfilePage.isOpened(), "Update profile page is not displayed");
        sa.assertAll();
    }
    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72348"})
    @Test(description = "Ariel: Profiles - Edit Profile - Primary Profile - DOB & Gender", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyPrimaryProfilesEditProfileDOBGender() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfiles = initPage(DisneyPlusEditProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();

        editProfiles.clickEditModeProfile(getAccount().getProfile(DEFAULT_PROFILE).getProfileName());
        sa.assertTrue(editProfiles.isBirthdateHeaderDisplayed(), "Birthdate header is not displayed on the edit profiles screen");
        sa.assertTrue(editProfiles.isBirthdateDisplayed(getAccount().getProfile(DEFAULT_PROFILE)),"Birthdate is not displayed on the edit profiles screen");
        sa.assertTrue(editProfiles.isGenderButtonPresent(), "Gender header is not displayed on edit profiles screen");
        sa.assertTrue(editProfiles.isGenderValuePresent(getAccount().getProfile(DEFAULT_PROFILE)), "Gender value is not as expected on the edit profiles screen");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72335"})
    @Test(description = "Profiles > Existing Subs -> Add Profile Banner for Primary Profiles", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAddProfileBannerForPrimaryProfiles() {
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEnforceDOBCollectionPageBase enforceDOBCollectionPage = initPage(DisneyPlusEnforceDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = initPage(DisneyPlusAddProfileBannerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        //Create Disney account without DOB and Gender
        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        enforceDOBCollectionPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        enforceDOBCollectionPage.clickPrimaryButton();
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74481"})
    @Test(description = "Profiles > Existing Subs -> Add Profile flow through Add Banner for Primary Profiles", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAddProfilePageAfterClickingAddProfileButtonOnAddProfileBanner() {
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEnforceDOBCollectionPageBase enforceDOBCollectionPage = initPage(DisneyPlusEnforceDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = initPage(DisneyPlusAddProfileBannerIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        //Create Disney account without DOB and Gender
        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        enforceDOBCollectionPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        enforceDOBCollectionPage.clickPrimaryButton();
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74482"})
    @Test(description = "Profiles > Existing Subs -> Add Profile Banner for Max profiles", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAddProfileBannerIsNotDispalyedForTheMaxAmountOfProfiles() {
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusEnforceDOBCollectionPageBase enforceDOBCollectionPage = initPage(DisneyPlusEnforceDOBCollectionPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = initPage(DisneyPlusAddProfileBannerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int Max = 6;

        //Create Disney account without DOB and Gender
        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());

        //Add Max number of profile through API
        for(int i=0;i<Max;i++){
            getAccountApi().addProfile(getAccount(),KIDS_PROFILE,getAccount().getProfileLang(),null,true);
        }

        enforceDOBCollectionPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        enforceDOBCollectionPage.clickPrimaryButton();
        updateProfilePage.chooseGender();
        updateProfilePage.tapSaveButton();
        //Verify add profile banner is not displayed if user already have maximum amount of profiles, namely 7
        sa.assertFalse(addProfileBanner.isProfileHeaderPresent(), "Add Profile Banner Header is present");
        sa.assertAll();
    }

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61284"})
    @Test(description = "Ariel: Profiles - Edit Profile - App UI Language", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyEditProfileAppUILanguage() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAppLanguageIOSPageBase appLanguage = initPage(DisneyPlusAppLanguageIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getAccount().getFirstName());
        editProfile.clickAppLanguage();

        sa.assertTrue(appLanguage.isOpened(), "App Language screen is not opened");
        sa.assertTrue(appLanguage.isAppLanguageHeaderPresent(), "App Language header is not present");
        sa.assertTrue(appLanguage.isAppLanguageCopyPresent(), "App Language copy is not present");
        sa.assertTrue(appLanguage.getBackButton().isElementPresent(), "Back button is not present");
        sa.assertTrue(appLanguage.isLanguageSelected(ENGLISH_US), "Language selected doesn't have the check mark");
        sa.assertTrue(appLanguage.isLanguageListShownInAlphabeticalOrder(), "Languages are not present in alphabetical order");
        pressByElement(appLanguage.getBackArrow(), 1);

        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Edit profile page is not opened");
        editProfile.clickAppLanguage();
        sa.assertTrue(appLanguage.isOpened(), "App Language screen is not opened");

        appLanguage.selectLanguage(ESPAÑOL);
        sa.assertTrue(editProfile.isUpdatedToastPresent(), "'Updated' toast was not found");

        editProfile.clickAppLanguage();
        sa.assertTrue(appLanguage.isLanguageSelected(ESPAÑOL), "Language was not changed to Spanish");
        sa.assertAll();
    }

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69503"})
    @Test(description = "Ariel: Profiles - Edit Profile - Kids Mode - Require Password to Disable Junior Mode Profile", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyEditProfileKidsModeDisableJuniorMode() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);

        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(getAccount(), KIDS_PROFILE, KIDS_DOB, getAccount().getProfileLang(), null, true, true);
        setAppToHomeScreen(getAccount());

        whoIsWatching.clickProfile("Test");
        moreMenu.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            editProfilePage.swipeUp(1500);
        }
        sa.assertTrue(editProfilePage.isJuniorModeTextPresent(), "Junior mode text was not present on edit profile page");

        editProfilePage.getKidProofExitToggleSwitch().click();
        sa.assertTrue(passwordPage.isAuthPasswordKidsProfileBodyDisplayed(), "Password Kids profile body is not displayed");

        passwordPage.typePassword(getAccount().getUserPass());
        passwordPage.clickPrimaryButton();
        sa.assertTrue(editProfilePage.isUpdatedToastPresent(), "'Updated' toast was not present");
        sa.assertAll();
    }

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66807"})
    @Test(description = "Edit Profile - Duplicate Profile Name Error", groups = {"Ariel-More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyEditProfileDuplicateProfileError() {
        String DARTH_MAUL = R.TESTDATA.get("disney_darth_maul_avatar_id");
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(getAccount(), SECONDARY_PROFILE, ADULT_DOB, getAccount().getProfileLang(), DARTH_MAUL, false, true);

        setAppToHomeScreen(getAccount());
        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);

        editProfilePage.enterProfileName(DEFAULT_PROFILE);
        editProfilePage.clickSaveBtn();
        sa.assertTrue(editProfilePage.isErrorDuplicateProfileNamePresent(), "Error `Duplicate Profile Name` is not present");

        editProfilePage.enterProfileName(NEW_PROFILE_NAME);
        editProfilePage.clickSaveBtn();
        homePage.clickMoreTab();
        sa.assertTrue(whoIsWatching.isAccessModeProfileIconPresent(NEW_PROFILE_NAME), "Profile name was not updated to " + NEW_PROFILE_NAME);
        sa.assertAll();
    }

    private void setAppToAccountSettings() {
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    private void onboard() {
        setAppToHomeScreen(getAccount());
        getAccountApi().addProfile(getAccount(),KIDS_PROFILE,getAccount().getProfileLang(), BABY_YODA,true);
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
