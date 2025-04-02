package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.*;
import com.disney.config.DisneyConfiguration;
import com.disney.config.DisneyParameters;
import com.disney.qa.api.client.responses.content.ContentSet;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.UnifiedAccount;
import com.disney.qa.common.constant.*;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.remote.MobilePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.*;
import java.util.Set;
import java.util.stream.IntStream;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.CollectionConstant.getCollectionName;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.MICKEY_MOUSE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_CREDENTIALS_ERROR;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusMoreMenuProfilesTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String ADULT_DOB = "1990-10-23";
    private static final String THE_CHILD = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";
    private static final String PROFILE_PIN = "1111";
    private static final String NEW_PROFILE_PIN = "1234";
    private static final String SECONDARY_PROFILE_PIN = "4321";
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String THREE = "3";
    private static final String ESPAÑOL = "Español";
    private static final String MORE_MENU_NOT_DISPLAYED_ERROR = "More Menu is not displayed";
    private static final String DARTH_MAUL = R.TESTDATA.get("disney_darth_maul_avatar_id");
    private static final String KID_PROOF_EXIT_SCREEN_DID_NOT_OPEN = "Kid Proof Exit code screen was not displayed";
    private static final String UPDATED_TOAST_WAS_NOT_DISPLAYED = "'Updated' toast was not displayed";
    private static final String KIDS_PROOF_EXIT_TOGGLE_IS_NOT_ON = "'kids proof exit' toggle is not 'On'";
    private static final String WHO_IS_WATCHING_SCREEN_IS_NOT_DISPLAYED = "Who is watching screen did not open";
    private static final String LIVE_TOGGLE_WAS_NOT_DISPLAYED =
            "Live and unrated section is not found on edit profile screen after multiple swipe";
    private static final String LIVE_TOGGLE_IS_NOT_ON_BY_DEFAULT = "Live toggle is not ON by default";
    private static final String RIGHT = "RIGHT";
    private static final String LEFT = "LEFT";

    private void onboard() {
        setAppToHomeScreen(getUnifiedAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66772"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAddProfileAvatarSelection() {
        DisneyPlusMoreMenuIOSPageBase MoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        onboard();
        MoreMenuIOSPageBase.clickAddProfile();
        Assert.assertTrue(chooseAvatarPage.isOpened(), "Choose Avatar page was not opened");
        sa.assertTrue(chooseAvatarPage.isSkipButtonPresent(), "Skip button not present on Choose Avatar page");
        sa.assertTrue(chooseAvatarPage.getBackArrow().isPresent(), "Back button not present on Choose Avatar page");

        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        BufferedImage selectedAvatar = getElementImage(avatars[0]);
        avatars[0].click();
        Assert.assertTrue(addProfile.isAddProfilePageOpened(), "User was not taken to the 'Add Profiles' page as expected");

        addProfile.enterProfileName(SECONDARY_PROFILE);
        addProfile.chooseGender();
        addProfile.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        addProfile.clickSaveProfileButton();
        addProfile.clickSecondaryButton();
        Assert.assertTrue(MoreMenuIOSPageBase.isOpened(), MORE_MENU_NOT_DISPLAYED_ERROR);

        BufferedImage moreMenuAvatar = getElementImage(MoreMenuIOSPageBase.getProfileAvatar(SECONDARY_PROFILE));
        BufferedImage selectedAvatarCopy = getScaledImage(cloneBufferedImage(selectedAvatar), moreMenuAvatar.getWidth(), moreMenuAvatar.getHeight());
        LOGGER.info("Comparing selected avatar to 'More Menu' display...");
        sa.assertTrue(areImagesTheSame(selectedAvatarCopy, moreMenuAvatar, 15),
                "Avatar displayed in the More Menu was either not displayed or was altered beyond the accepted margin of error");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66772"})
    @Test(description = "Verify: User cannot select the same avatar for multiple profiles", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUserCanNotSelectTheSameAvatarForMultipleProfiles() {
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(THE_CHILD)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        disneyPlusMoreMenuIOSPageBase.clickMoreTab();
        disneyPlusMoreMenuIOSPageBase.clickAddProfile();
        ExtendedWebElement[] avatars = disneyPlusEditProfileIOSPageBase.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        for (ExtendedWebElement avatar : avatars) {
            LOGGER.info("Verifying that avatar with label '{}' does not have the same avatar as the previously selected one...", avatar.getText());
            sa.assertNotEquals(avatar.getAttribute("name"), THE_CHILD,
                    "The previously selected Avatar was available for selection unexpectedly");
        }
        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66778"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUserCanNotSelectTheSameNameForMultipleProfiles() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.waitForPresenceOfAnElement(moreMenu.getAddProfileBtn());
        moreMenu.clickAddProfile();
        chooseAvatar.clickSkipButton();
        addProfile.enterProfileName(DEFAULT_PROFILE);
        addProfile.clickSaveBtn();
        Assert.assertTrue(addProfile.isDuplicateProfileNameErrorPresent(),
                "Error 'Duplicate Profile Name' is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66786"})
    @Test(description = "Autoplay toggle is Saved if User saves", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAutoplayToggleIsSaved() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        //Turn ON autoplay
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getUnifiedAccount().getFirstName());
        editProfile.toggleAutoplayButton("OFF");
        sa.assertTrue(editProfile.isUpdatedToastPresent(), UPDATED_TOAST_WAS_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66794"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileName() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        String updatedUserName = "New Test User";

        setAppToHomeScreen(getUnifiedAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getUnifiedAccount().getFirstName());

        editProfile.enterProfileName(updatedUserName);
        editProfile.clickDoneBtn();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        Assert.assertTrue(whoIsWatching.isProfileIconPresent(updatedUserName),
                "Profile name is not updated as expected");

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76067"})
    @Test(description = "Autoplay toggle is Saved if User saves", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAutoplayToggleForKidsAndAdultProfile() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
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

        setAppToHomeScreen(getUnifiedAccount(), getUnifiedAccount().getFirstName());
        //Verify autoplay is turned on by default for adult profile
        verifyAutoPlayStateForProfile(getUnifiedAccount().getFirstName(), "On", sa);
        //Turn off autoplay for adult profile
        editProfile.toggleAutoplayButton("OFF");
        sa.assertTrue(editProfile.isUpdatedToastPresent(), UPDATED_TOAST_WAS_NOT_DISPLAYED);
        sa.assertTrue(editProfile.getAutoplayState().equals("Off"), "Autoplay wasn't turned Off for primary profile");
        //wait for updated toast to disappear before tapping on done button
        editProfile.waitForUpdatedToastToDisappear();
        editProfile.clickDoneBtn();
        //Verify autoplay is turned off for adult profile
        verifyAutoPlayStateForProfile(getUnifiedAccount().getFirstName(), "Off", sa);
        editProfile.clickDoneBtn();
        //Verify autoplay is turned OFF by default for kids profile
        verifyAutoPlayStateForProfile(KIDS_PROFILE, "Off", sa);
        //Turn on autoplay for adult profile
        editProfile.toggleAutoplayButton("ON");
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        sa.assertTrue(editProfile.isUpdatedToastPresent(), UPDATED_TOAST_WAS_NOT_DISPLAYED);
        editProfile.waitForUpdatedToastToDisappear();
        editProfile.clickDoneBtn();
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74601"})
    @Test(description = "Add Profile(Secondary Profile) Age > 18+ defaults to TV-MA", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyProfileDefaultsToTVMA() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase editProfile = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        onboard();
        moreMenu.clickAddProfile();
        //Choose avatar
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        //Finish creating profile
        addProfile.createProfile(SECONDARY_PROFILE, DateHelper.Month.OCTOBER, "23", "1955");
        //Verify ServiceEnrollment pin page
        sa.assertTrue(editProfile.isServiceEnrollmentSetPINPresent(), "ServiceEnrollment set pin page is not shown");
        sa.assertTrue(editProfile.isProfilePinDescriptionDisplayed(), "Profile pin description is not displayed");
        sa.assertTrue(editProfile.isProfilePinActionDisplayed(), "Profile pin action is not displayed");
        sa.assertTrue(editProfile.isProfilePinReminderDisplayed(), "Profile pin reminder is not displayed");
        LOGGER.info("Selecting 'Not Now' on pin settings page");
        addProfile.clickSecondaryButtonByCoordinates();
        //Verify rating is displayed in account's page
        moreMenu.clickEditProfilesBtn();
        pause(2);
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(RATING_MATURE), "profile rating is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74602"})
    @Test(description = "Add Profile - (Secondary Profile) Age <18, default to TV-14 and trigger Welch Flow", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySecondaryProfileU18DefaultsToTV14() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase editProfile = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        onboard();
        moreMenu.clickAddProfile();
        //Choose avatar
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        //Finish creating profile
        addProfile.enterProfileName(SECONDARY_PROFILE);
        addProfile.enterDOB(Person.U18.getMonth(), Person.U18.getDay(), Person.U18.getYear());
        addProfile.clickSaveBtn();
        //Verify Welch flow is triggered
        sa.assertTrue(editProfile.isServiceEnrollmentAccessFullCatalogPagePresent(), "Welch flow wasn't displayed on screen after creating the U18 profile");
        sa.assertTrue(addProfile.isMaturityRatingNotNowInfoDisplayed(), "Maturity rating not now info wasn't displayed");
        sa.assertTrue(addProfile.isUpdateMaturityRatingActionDisplayed(), "Update your maturity rating info wasn't displayed");
        sa.assertTrue(addProfile.verifyHeadlineHeaderText(), "Set your content rating to max rating text isn't displayed");
        //select 'Not Now' button
        addProfile.clickSecondaryButtonByCoordinates();
        //Verify TV-14 is displayed in account's page
        moreMenu.clickEditProfilesBtn();
        pause(2);
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(RATING_TV14), "U18 profile rating is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66770"})
    @Test(description = "Add Profile - Seventh Profile", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySeventhProfile() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = new DisneyPlusChooseAvatarIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        List<String> profiles = Arrays.asList("Profile 2", "Profile 3", "Profile 4", "Profile 5", "Profile 6");
        profiles.forEach(profile ->
                getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                        .unifiedAccount(getUnifiedAccount())
                        .profileName(profile)
                        .dateOfBirth(ADULT_DOB)
                        .language(getLocalizationUtils().getUserLanguage())
                        .avatarId(RAYA)
                        .kidsModeEnabled(false)
                        .isStarOnboarded(true).build())
        );

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            swipeInContainer(moreMenu.getProfileSelectionCollectionView(), Direction.LEFT, 500);
        }
        moreMenu.clickAddProfile();
        sa.assertTrue(chooseAvatar.isOpened(), "`Choose Avatar` screen was not opened.");

        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        sa.assertTrue(addProfile.isOpened(), "'Add Profile' page was not opened.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73707"})
    @Test(description = "Add Profile - (Secondary Profile) Age <18, default to TV-14 and trigger Welch Flow", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyEditProfileSecondaryProfileUIElements() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase editProfile = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(THE_CHILD)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount());
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Edit profile Title is not displayed");
        sa.assertTrue(editProfile.isProfileIconDisplayed(THE_CHILD), "profile icon is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(), "pencil icon is not displayed");
        sa.assertTrue(editProfile.isPersonalInformationSectionDisplayed(), "Personal information section is not as expected");
        sa.assertTrue(editProfile.isPlayBackSettingsSectionDisplayed(), "Playback setting section is not as expected");
        sa.assertTrue(editProfile.isFeatureSettingsSectionDisplayed(), "Feature setting section is not as expected");
        sa.assertTrue(editProfile.isParentalControlSectionDisplayed(), "Parental control section is not as expected");
        sa.assertTrue(editProfile.isMaturityRatingSectionDisplayed("TV-MA"), "Maturity Rating section is not as expected");
        sa.assertTrue(editProfile.isDeleteProfileButtonPresent(), "Delete profile button is not displayed");
        sa.assertTrue(editProfile.getDoneButton().isPresent(), "Done button is not displayed");
        addProfile.updateUserName("updated_profile");
        editProfile.getDoneButton().click();
        moreMenu.clickMoreTab();
        sa.assertTrue(whoIsWatching.getDynamicAccessibilityId("updated_profile").isPresent(), "updated profile name is not displayed");
        whoIsWatching.clickProfile("updated_profile");
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile("updated_profile");
        sa.assertTrue(editProfile.isDeleteProfileButtonPresent(), "Delete profile button is not displayed");
        editProfile.getDeleteProfileButton().click();
        editProfile.clickConfirmDeleteButton();
        sa.assertFalse(whoIsWatching.getDynamicAccessibilityId("updated_profile").isPresent(), "Profile is not deleted");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66836"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyForgotPin() {
        DisneyPlusPinIOSPageBase pinPage = new DisneyPlusPinIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase homePage = new DisneyPlusHomeIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        String incorrectPasswordError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());

        try {
            getUnifiedAccountApi().updateProfilePin(getUnifiedAccount(),
                    getUnifiedAccount().getProfileId(DEFAULT_PROFILE), PROFILE_PIN);
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }
        setAppToHomeScreen(getUnifiedAccount());
        sa.assertTrue(whoIsWatching.isPinProtectedProfileIconPresent(DEFAULT_PROFILE), "Pin protected profile was not found.");
        whoIsWatching.clickPinProtectedProfile(DEFAULT_PROFILE);

        //Confirm forgot PIN? is present
        sa.assertTrue(pinPage.getForgotPinButton().isPresent(), "Forgot Pin button not found");

        //Enter in wrong password
        pinPage.getForgotPinButton().click();
        pinPage.enterPasswordNoAccount("M0us3#M1ck3y");
        sa.assertEquals(passwordPage.getErrorMessageString(), incorrectPasswordError, "'We couldn't log you in' error message did not display for wrong password entered.");

        //Enter correct password
        passwordPage.enterPassword(getUnifiedAccount());

        //Validate buttons and text present on Forgot PIN page
        sa.assertTrue(pinPage.isOpened(), "Profile pin title label was not found.");
        sa.assertTrue(pinPage.getPinCheckBox().isPresent(), "Pin check box was not found.");
        sa.assertTrue(pinPage.getLimitAccessMessaging(DEFAULT_PROFILE).isPresent(), "Profile pin limit access messaging not found.");
        sa.assertTrue(pinPage.getCancelButton().isPresent(), "Cancel button was not found.");
        sa.assertTrue(pinPage.getSaveButton().isPresent(), "Save button was not found.");

        //Validate cancel button action
        pinPage.clearPin();
        pinPage.clickProfilePin();
        pinPage.enterProfilePin(NEW_PROFILE_PIN);
        pinPage.getCancelButton().click();
        sa.assertTrue(pinPage.isOpened(), "Did not return to profile pin page");

        pinPage.getForgotPinButton().click();
        sa.assertTrue(pinPage.getAccountPasswordRequiredMessaging().isPresent(), "'To retrieve pin, account password required' messaging not found.");

        //Enter in new pin and save
        passwordPage.enterPassword(getUnifiedAccount());
        sa.assertTrue(pinPage.isOpened(), "Pin page was not opened after entering password.");
        pinPage.clearPin();
        pinPage.clickProfilePin();
        pinPage.enterProfilePin(NEW_PROFILE_PIN);
        pinPage.getSaveButton().click();
        sa.assertTrue(whoIsWatching.isPinProtectedProfileIconPresent(DEFAULT_PROFILE), "Pin protected profile icon not present.");

        //Enter in new pin and home screen appears
        whoIsWatching.clickPinProtectedProfile(DEFAULT_PROFILE);
        pinPage.clickProfilePin();
        pinPage.enterProfilePin(NEW_PROFILE_PIN);
        sa.assertTrue(homePage.isOpened(), "Home page did not open");

        //refresh app to present who's watching page
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));

        //Remove profile pin via forgot PIN
        whoIsWatching.isOpened();
        whoIsWatching.clickPinProtectedProfile(DEFAULT_PROFILE);
        pinPage.getForgotPinButton().click();
        passwordPage.enterPassword(getUnifiedAccount());
        pinPage.getPinCheckBox().click();
        pinPage.getSaveButton().click();
        sa.assertFalse(whoIsWatching.isPinProtectedProfileIconPresent(DEFAULT_PROFILE), "Pin protected profile was found.");

        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        sa.assertTrue(homePage.isOpened(), "Home page did not open.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66764"})
    @Test(description = "Profile Selection Page UI", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProfileSelectionPageUI() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(THE_CHILD)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount());

        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        moreMenu.clickMoreTab();
        sa.assertTrue(moreMenu.isEditProfilesBtnPresent(), "Edit Profile CTA wasn't displayed on who's watching screen");
        sa.assertTrue(moreMenu.isAddProfileButtonPresent(), "Add Profile CTA wasn't displayed on who's watching screen");
        sa.assertTrue(moreMenu.getStaticTextByLabel(SECONDARY_PROFILE).isPresent(), "Profile name wasn't displayed on who's watching screen");
        sa.assertTrue(moreMenu.getStaticTextByLabel(DEFAULT_PROFILE).isPresent(), "Profile name wasn't displayed on who's watching screen");
        sa.assertTrue(whoIsWatching.isProfileIconPresent(DEFAULT_PROFILE), "Profile icon cell wasn't displayed for default profile");
        sa.assertTrue(whoIsWatching.isProfileIconPresent(SECONDARY_PROFILE), "Profile icon cell wasn't displayed for secondary profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66796"})
    @Test(description = "Profiles > Edit Profile - Delete Profile UI", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileDeleteProfileUI() {
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isEditProfilesBtnPresent(), "Edit Profiles button was not found.");

        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            editProfile.swipeUp(500);
        }
        sa.assertTrue(editProfile.isDeleteProfileButtonPresent(), "Delete button is not present on Edit profile.");

        editProfile.getDeleteProfileButton().click();
        sa.assertTrue(editProfile.getDeleteProfileCancelButton().isPresent(), "Delete Profile cancel button was not found.");
        sa.assertTrue(editProfile.getDeleteProfileDeleteButton().isPresent(), "Delete Profile delete button was not found.");
        sa.assertTrue(editProfile.getDeleteProfileTitle(SECONDARY_PROFILE).isPresent(), "Delete Profile page load (title and copy) was not found");
        sa.assertTrue(editProfile.getDeleteProfileCopy().isPresent(), "Delete Profile copy was not found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66839"})
    @Test(description = "Profiles > Profile PIN - Profile Access", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProfilePinProfileAccess() {
        SoftAssert sa = new SoftAssert();
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = new DisneyPlusPinIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase homePage = new DisneyPlusHomeIOSPageBase(getDriver());
        try {
            getUnifiedAccountApi().updateProfilePin(getUnifiedAccount(), getUnifiedAccount().getProfileId(DEFAULT_PROFILE),
                    PROFILE_PIN);
            getUnifiedAccountApi().updateProfilePin(getUnifiedAccount(), getUnifiedAccount().getProfileId(SECONDARY_PROFILE), SECONDARY_PROFILE_PIN);
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }
        setAppToHomeScreen(getUnifiedAccount());
        sa.assertTrue(whoIsWatching.isPinProtectedProfileIconPresent(DEFAULT_PROFILE), "Pin protected profile was not found.");
        whoIsWatching.clickPinProtectedProfile(DEFAULT_PROFILE);
        //Validate UI
        sa.assertTrue(pinPage.isOpened(), "'Enter your profile PIN` title was not found.");
        sa.assertTrue(pinPage.getCancelButton().isPresent(), "Cancel button was not found.");
        sa.assertTrue(pinPage.getPinInputField().isPresent(), "Input field is not present.");
        sa.assertTrue(pinPage.isPinProtectedProfileIconPresent(DEFAULT_PROFILE), "Pin protected profile was not found on Profile PIN Page.");
        sa.assertTrue(pinPage.getForgotPinButton().isPresent(), "Forgot Pin? button was not found.");

        //Validate incorrect pin error
        SecureRandom random = new SecureRandom();
        int min = 1000;
        int max = 9999;
        IntStream.range(0, 10).forEach(i -> {
            pinPage.clickProfilePin();
            pinPage.enterProfilePin(String.valueOf(random.nextInt(max - min + 1) + min));
            sa.assertTrue(pinPage.getProfilePinInvalidErrorMessage().isPresent(), "Profile PIN Invalid Error message was not found.");
            pinPage.clearPin();
        });

        //Validate correct pin
        pinPage.clickProfilePin();
        pinPage.enterProfilePin(PROFILE_PIN);
        sa.assertTrue(homePage.isOpened(), "After entering profile pin, home page did not open.");

        //Validate selecting pin protected profile from More Menu
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickPinProtectedProfile(SECONDARY_PROFILE);
        sa.assertTrue(pinPage.isOpened(), "Profile pin page was not opened after clicking secondary pin protected profile.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69538"})
    @Test(description = "Ariel: Profiles - Edit Profile - Maturity Rating Slider", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileMaturityRatingSlider() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase editProfile = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusContentRatingIOSPageBase contentRatingPage = new DisneyPlusContentRatingIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(DEFAULT_PROFILE);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(RATING_MATURE), "profile rating is not as expected");
        editProfile.getContentRatingHeader().click();
        passwordPage.enterPassword(getUnifiedAccount());
        sa.assertTrue(contentRatingPage.isOpened(), "Content rating page was not opened");
        sa.assertTrue(contentRatingPage.isContentRatingDisplyed(RATING_R), RATING_R + " rating was not displayed");
        sa.assertTrue(contentRatingPage.isContentRatingDisplyed(RATING_MATURE), RATING_MATURE + " rating was not displayed");
        sa.assertTrue(contentRatingPage.verifyLastContentRating(RATING_MATURE), RATING_MATURE + " rating was not displayed at last");

        //User select Rating R
        contentRatingPage.selectContentRating(RATING_R);
        contentRatingPage.clickSaveButton();
        sa.assertTrue(editProfile.isUpdatedToastPresent(), UPDATED_TOAST_WAS_NOT_DISPLAYED);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(RATING_R), "profile rating is not as expected");

        editProfile.waitForUpdatedToastToDisappear();
        //User select Rating TV-MA
        editProfile.getContentRatingHeader().click();
        passwordPage.enterPassword(getUnifiedAccount());
        contentRatingPage.selectContentRating(RATING_MATURE);
        contentRatingPage.clickSaveButton();
        sa.assertTrue(editProfile.isUpdatedToastPresent(), UPDATED_TOAST_WAS_NOT_DISPLAYED);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(RATING_MATURE), "profile rating is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66837"})
    @Test(description = "Profiles > Manage Profile PIN", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyManageProfilePIN() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = new DisneyPlusPinIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        try {
            getUnifiedAccountApi().updateProfilePin(getUnifiedAccount(), getUnifiedAccount().getProfileId(DEFAULT_PROFILE), PROFILE_PIN);
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }
        setAppToHomeScreen(getUnifiedAccount());

        //Verify pin protected profile
        whoIsWatching.clickPinProtectedProfile(DEFAULT_PROFILE);
        sa.assertTrue(pinPage.isOpened(), "Pin title was not found.");
        sa.assertTrue(pinPage.getPinInputField().isPresent(), "Pin input field was not found.");
        sa.assertTrue(pinPage.isPinProtectedProfileIconPresent(DEFAULT_PROFILE), "Pin Protected profile avatar and lock was not found.");
        sa.assertTrue(pinPage.getPinCancelButton().isPresent(), "Pin cancel button was not found.");
        sa.assertTrue(pinPage.getForgotPinButton().isPresent(), "Forgot Pin button was not found.");

        //Verify existing profile - no profile pin
        pinPage.getPinCancelButton().click();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        editProfile.swipeUp(500);
        editProfile.getEditProfilePinSettingLabel().click();
        passwordPage.enterPassword(getUnifiedAccount());

        //Verify clicking pin checkbox
        sa.assertTrue(pinPage.getPinCheckBox().isPresent(), "Checked checkbox was not found.");
        pinPage.getPinCheckBox().click();
        sa.assertTrue(pinPage.getLimitAccessMessaging(SECONDARY_PROFILE).isPresent(), "Profile pin limit access messaging not found.");
        sa.assertTrue(pinPage.getPinInputField().isPresent(), "Pin input field was not found.");
        sa.assertTrue(pinPage.getKeyboardByPredicate().isPresent(), "Keyboard did not pop up.");

        //Verify after each pin number entered
        pinPage.enterProfilePin(ONE);
        sa.assertTrue(pinPage.isPinFieldNumberPresent(ONE), ONE + " was not inputted into field.");
        sa.assertTrue(pinPage.getPinInputField().isPresent(), "Pin input field was not found.");

        pinPage.enterProfilePin(TWO);
        sa.assertTrue(pinPage.isPinFieldNumberPresent(TWO), TWO + " was not inputted into field.");
        sa.assertTrue(pinPage.getPinInputField().isPresent(), "Pin input field was not found.");

        pinPage.enterProfilePin(THREE);
        sa.assertTrue(pinPage.isPinFieldNumberPresent(THREE), THREE + " was not inputted into field.");
        sa.assertTrue(pinPage.getPinInputField().isPresent(), "Pin input field was not found.");

        //Validate save after only three numbers entered
        pinPage.getSaveButton().click();
        sa.assertTrue(pinPage.getProfilePinMissingErrorMessage().isPresent(), "Profile PIN missing error message was not found");
        sa.assertTrue(pinPage.getPinInputField().isPresent(), "Pin input field was not found.");

        //Verify error message not present after new number entered
        pinPage.enterProfilePin("4");
        sa.assertTrue(pinPage.getProfilePinMissingErrorMessage().isElementNotPresent(SHORT_TIMEOUT), "Profile PIN missing error message was found");

        //Validate clicking cancel button
        pinPage.getCancelButton().click();
        sa.assertTrue(editProfile.getEditProfilePinSettingLabel().isPresent(), "Did not return to Edit Profile screen, Profile pin setting label not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69511"})
    @Test(description = "PCON > Kid-Proof Exit Settings > Toggle UI and Logic", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyKidProofExitSettings() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
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

        setAppToHomeScreen(getUnifiedAccount(), getUnifiedAccount().getFirstName());

        editProfile.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(KIDS_PROFILE);
        //Verify line items for kid proof exit
        sa.assertTrue(editProfile.getKidProofExitLabel().isPresent(), "Kids proof exit label wasn't present");
        sa.assertTrue(editProfile.getKidProofDescription().isPresent(), "Kids proof exit description wasn't present");
        //Kid proof exit is off by default
        sa.assertTrue(editProfile.getKidProofExitToggleValue().equals("Off"), "kids exit toggle value is not Off by default");
        editProfile.toggleKidsProofExit();
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        sa.assertTrue(editProfile.getKidProofExitToggleValue().equals("On"), KIDS_PROOF_EXIT_TOGGLE_IS_NOT_ON);
        editProfile.waitForUpdatedToastToDisappear();
        //Turn off junior mode toggle
        editProfile.toggleJuniorMode();
        passwordPage.submitPasswordWhileLoggedIn(getUnifiedAccount().getUserPass());
        sa.assertTrue(editProfile.getJuniorModeToggleValue().equals("Off"), "Profile is not converted to General Audience(non-primary)");
        //Verify kids proof exit is not tappable
        editProfile.toggleKidsProofExit();
        sa.assertTrue(editProfile.getKidProofExitToggleValue().equals("On"), "Kid proof exit toggle value was not retained from previous setting");
        //Change non-primary(general audience) profile back to kids profile
        editProfile.toggleJuniorMode();
        sa.assertTrue(editProfile.isUpdatedToastPresent(), UPDATED_TOAST_WAS_NOT_DISPLAYED);
        sa.assertTrue(editProfile.getKidProofExitToggleValue().equals("On"), "Kid proof exit toggle value was not retained from previous setting");
        editProfile.waitForUpdatedToastToDisappear();
        editProfile.clickDoneBtn();
        //Create new kids profile
        createKidsProfile();
        editProfile.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(JUNIOR_PROFILE);
        sa.assertTrue(editProfile.getKidProofExitToggleValue().equals("Off"), "kids exit toggle value is not Off by default");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66809"})
    @Test(description = "Profiles - Edit Profile - Saving an empty Name Error", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyEditProfileSavingEmptyNameError() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount(), getUnifiedAccount().getFirstName());
        editProfile.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(DEFAULT_PROFILE);
        editProfile.enterProfileName("");
        editProfile.clickDoneBtn();
        sa.assertTrue(editProfile.isEmptyProfileNameErrorDisplayed(), "Empty profile name error is not displayed");
        //Keys.SPACE is not working as expected
        editProfile.enterProfileName("     ");
        editProfile.clickDoneBtn();
        sa.assertTrue(editProfile.isEmptyProfileNameErrorDisplayed(), "Empty profile name error is not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66780"})
    @Test(description = "Edit Profile - All Characters allowed for Profile name", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyEditProfileAllCharacters() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(DEFAULT_PROFILE);
        String allCharacters = "\ud83d\ude3b!@\u24E912\uD83D\uDC9A3WA\ud83d\ude06"; //u codes left to right: cat heart emoji, z circle symbol, green heart emoji, laughing emoji
        editProfile.enterProfileName(allCharacters);
        editProfile.clickDoneBtn();
        sa.assertTrue(homePage.isOpened(), "After clicking 'Done' to save new profile name, not returned to Home.");

        homePage.clickMoreTab();
        moreMenu.isOpened();
        sa.assertTrue(moreMenu.getProfileCell(allCharacters, false).isPresent(), allCharacters + " profile name was not found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68341"})
    @Test(description = "Localization - UI Languages & Ability to Change Language", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUIAppAbilityToChangeLanguage() {
        String spanishLanguageCode = "es";
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
        appLanguage.selectLanguage(ESPAÑOL);

        getLocalizationUtils().setLanguageCode(spanishLanguageCode);
        DisneyLocalizationUtils disneyLocalizationUtils = new DisneyLocalizationUtils(getCountry(), getLocalizationUtils().getUserLanguage(), MobilePlatform.IOS,
                DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                DISNEY);
        disneyLocalizationUtils.setDictionaries(getConfigApi().getDictionaryVersions());

        String editProfileInSpanish = disneyLocalizationUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT_PROFILE_TITLE_2.getText());
        String doneInSpanish = disneyLocalizationUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT_PROFILE_DONE_BUTTON.getText());
        sa.assertTrue(editProfile.getStaticTextByLabel(editProfileInSpanish).isPresent(), "UI language for Edit Profile page is not updated after language change");
        sa.assertTrue(editProfile.getStaticTextByLabel(doneInSpanish).isPresent(), "UI language For Done button on Edit Profile page is not updated after language change");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66790"})
    @Test(description = "Edit Profile - Tap Edit Profile", groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyTapEditProfile() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        moreMenu.clickMoreTab();

        sa.assertTrue(moreMenu.isEditProfilesBtnPresent(), "Edit Profiles button is not present.");
        moreMenu.clickEditProfilesBtn();
        sa.assertTrue(whoIsWatching.isEditProfileButtonDisplayed(), "Edit Profile button is not present.");

        editProfile.clickEditModeProfile(getUnifiedAccount().getFirstName());
        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Tapping on edit profile did not open profile to edit.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66806"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEditProfileChangeAvatar() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        List<ContentSet> avatarSets = getAvatarSets(getAccount());
        int lastSetId = avatarSets.size() - 1;
        String lastSetAvatarId = "";
        String avatarSetName = "";
        try {
            lastSetAvatarId = avatarSets.get(lastSetId).getAvatarIds().get(0);
        } catch (IndexOutOfBoundsException e) {
            Assert.fail("Index out of bounds: " + e);
        }
        try {
            avatarSetName = avatarSets.get(1).getSetName();
        } catch (IndexOutOfBoundsException e) {
            Assert.fail("Index out of bounds: " + e);
        }
        setAppToHomeScreen(getAccount());
        moreMenu.clickMoreTab();
        BufferedImage originalAvatar = getElementImage(moreMenu.getProfileAvatar(DEFAULT_PROFILE));
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getUnifiedAccount().getFirstName());
        editProfile.getAddProfileAvatar().click();

        //validate back arrow, Choose Avatar screen title and avatar headers
        sa.assertTrue(chooseAvatar.getBackArrow().isPresent(), "Back arrow was not present.");
        sa.assertTrue(chooseAvatar.getChooseAvatarTitle().isPresent(), "Choose Avatar title was not present.");
        for (int i = 0; i < chooseAvatar.getHeaderTitlesInView().size(); i++) {
            //First avatar set name returned is "Default" which does not exist as avatar header title
            sa.assertTrue(chooseAvatar.getStaticTextByLabelContains(avatarSets.get(i + 1).getSetName()).isPresent(),
                    "Avatar header was not found.");
        }

        //validate back arrow function
        chooseAvatar.getBackArrow().click();
        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Not returned back to Edit Profile screen.");
        editProfile.getAddProfileAvatar().click();

        //validate scrolling
        Assert.assertTrue(chooseAvatar.isOpened(), "Choose Avatar page was not opened.");
        sa.assertTrue(chooseAvatar.isCollectionViewScrollableHorizontally(0, 2), "Not able to horizontally scroll in collection.");
        sa.assertTrue(chooseAvatar.isCollectionViewScreenScrollableVertically(chooseAvatar.getStaticTextByLabelContains(avatarSetName),
                        chooseAvatar.getStaticTextByLabelContains(avatarSets.get(lastSetId).getSetName()), null),
                "Not able to vertically scroll Choose Avatar screen.");

        //validate select new avatar
        swipePageTillElementTappable(chooseAvatar.getTypeCellNameContains(lastSetAvatarId), 2, null, Direction.UP, 1000);
        chooseAvatar.getTypeCellNameContains(lastSetAvatarId).click();
        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Not returned back to Edit Profile screen.");
        editProfile.getDoneButton().click();
        moreMenu.clickMoreTab();
        BufferedImage updatedAvatar = getElementImage(moreMenu.getProfileAvatar(DEFAULT_PROFILE));
        sa.assertTrue(areImagesDifferent(originalAvatar, updatedAvatar), "Avatar images are the same.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67787"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMoreMenuSimplifiedJuniorProfile() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), JUNIOR_PROFILE);
        moreMenu.clickMoreTab();

        // Elements that should be present on screen
        sa.assertTrue(moreMenu.getProfileAvatar(JUNIOR_PROFILE).isPresent(SHORT_TIMEOUT),
                "Avatar is not present");
        sa.assertTrue(moreMenu.getStaticTextByLabel(JUNIOR_PROFILE).isPresent(),
                "Junior Mode name was not present on profile page");
        sa.assertEquals(moreMenu.getExitKidsProfileButtonText(), "EXIT JUNIOR MODE",
                "Exit Junior Mode text is not present");
        sa.assertTrue(moreMenu.isAppVersionDisplayed(), "App Version is not present");
        sa.assertTrue(moreMenu.getDynamicCellByLabel(
                        moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).isPresent(SHORT_TIMEOUT),
                "Watchlist Menu is not present");
        sa.assertTrue(moreMenu.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LEGAL),
                "Legal Menu is not present");

        // Elements that should not be present on screen
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS),
                "App Settings Menu is present");
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT),
                "Account Menu is present");
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP),
                "Help Menu is present");
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT),
                "Log Out Menu is present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75399"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyKidProofExitJuniorProfileCloseButton() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusKidProofExitIOSPageBase kidProofExitIOSPageBase = new DisneyPlusKidProofExitIOSPageBase(getDriver());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        configureKidsProfileProofExit();

        whoIsWatching.clickProfile(KIDS_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.tapExitKidsProfileButton();
        // Validates title text from Kid Proof Exit Screen
        Assert.assertTrue(kidProofExitIOSPageBase.isKidProofDialogTitleDisplayed(), KID_PROOF_EXIT_SCREEN_DID_NOT_OPEN);
        kidProofExitIOSPageBase.getCloseButton().click();
        // Validates that Kid Proof Exit Screen has been closed
        Assert.assertTrue(moreMenu.getStaticTextByLabel(KIDS_PROFILE).isPresent(),
                "Junior Profile screen was not open");
        Assert.assertEquals(moreMenu.getExitKidsProfileButtonText(), "EXIT JUNIOR MODE",
                "Exit Junior Mode option is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75396"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyKidProofExitJuniorProfileUpdateSettings() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(KIDS_PROFILE);
        // Validates if Kid-Proof-Exit option is present
        Assert.assertTrue(editProfile.getKidProofExitLabel().isPresent(), "Kids Proof Exit label was not present");
        // Toggle ON Kid-Proof-Exit option and verify that it is updated and Junior Mode remains the same
        editProfile.toggleKidsProofExit();
        passwordPage.enterPassword(getUnifiedAccount());
        editProfile.waitForUpdatedToastToDisappear();
        Assert.assertEquals(editProfile.getJuniorModeToggleValue(), "On", "Profile is not converted to General Audience");
        Assert.assertEquals(editProfile.getKidProofExitToggleValue(), "On", "Kids Proof Exit option is not toggled ON");
        // Toggle OFF Junior Mode option and it should not update kids toggle option
        editProfile.toggleJuniorMode();
        passwordPage.enterPassword(getUnifiedAccount());
        editProfile.waitForUpdatedToastToDisappear();
        Assert.assertEquals(editProfile.getJuniorModeToggleValue(), "Off", "Junior Mode is not toggled OFF");
        Assert.assertEquals(editProfile.getKidProofExitToggleValue(), "On", "Kids Proof Exit option is not toggled ON");
        // Toggle ON Junior Mode option and assert that Kid-Proof-Exit option remains the same
        editProfile.toggleJuniorMode();
        editProfile.waitForUpdatedToastToDisappear();
        Assert.assertEquals(editProfile.getJuniorModeToggleValue(), "On", "Profile is not converted to General Audience");
        Assert.assertEquals(editProfile.getKidProofExitToggleValue(), "On", "Kids Proof Exit option is not toggled ON");
        // Toggle OFF Junior Mode option and validate OFF status
        editProfile.toggleJuniorMode();
        passwordPage.enterPassword(getUnifiedAccount());
        editProfile.waitForUpdatedToastToDisappear();
        Assert.assertEquals(editProfile.getJuniorModeToggleValue(), "Off", "Junior Mode is not toggled OFF");
        // Try to toggle Kid-Proof-Exit option and validate if it is disabled
        editProfile.toggleKidsProofExit();
        Assert.assertEquals(editProfile.getKidProofExitToggleValue(), "On", "Kids Proof Exit toggle was not disabled");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75397"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyKidProofExitJuniorProfileScreenIncorrectCode() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusKidProofExitIOSPageBase kidProofExitIOSPageBase = initPage(DisneyPlusKidProofExitIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        String incorrectCode = "1234";
        configureKidsProfileProofExit();

        whoIsWatching.clickProfile(KIDS_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.tapExitKidsProfileButton();
        // Validates title text from Kid Proof Exit Screen
        Assert.assertTrue(kidProofExitIOSPageBase.isKidProofDialogTitleDisplayed(), KID_PROOF_EXIT_SCREEN_DID_NOT_OPEN);
        // Enter 4 digits to get error message incorrect code
        kidProofExitIOSPageBase.getCodeInputField().type(incorrectCode);
        Assert.assertTrue(kidProofExitIOSPageBase.isKidProofIncorrectCodeErrorMessageDisplayed(), "Incorrect code error message not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75398"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyKidProofExitJuniorProfileCorrectCode() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusKidProofExitIOSPageBase kidProofExitIOSPageBase = initPage(DisneyPlusKidProofExitIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        configureKidsProfileProofExit();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.tapExitKidsProfileButton();
        // Validates title text from Kid Proof Exit Screen
        Assert.assertTrue(kidProofExitIOSPageBase.isKidProofDialogTitleDisplayed(), KID_PROOF_EXIT_SCREEN_DID_NOT_OPEN);
        // Enter correct code and validates screen expected
        String code = kidProofExitIOSPageBase.parseExitDigitsCode();
        kidProofExitIOSPageBase.getCodeInputField().type(code);

        Assert.assertTrue(whoIsWatching.isOpened(), WHO_IS_WATCHING_SCREEN_IS_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66832"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyKidProofExitJuniorProfileScreenUI() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusKidProofExitIOSPageBase kidProofExitIOSPageBase = initPage(DisneyPlusKidProofExitIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        configureKidsProfileProofExit();

        whoIsWatching.clickProfile(KIDS_PROFILE);
        moreMenu.clickMoreTab();
        // Click on Exit Kids Profile and validates that screen elements
        moreMenu.tapExitKidsProfileButton();
        Assert.assertTrue(kidProofExitIOSPageBase.isKidProofDialogTitleDisplayed(), KID_PROOF_EXIT_SCREEN_DID_NOT_OPEN);
        Assert.assertTrue(kidProofExitIOSPageBase.getCloseButton().isPresent(), "Close button is not present");
        Assert.assertTrue(kidProofExitIOSPageBase.getDoorIcon().isPresent(), "Door Icon was not present");
        Assert.assertTrue(kidProofExitIOSPageBase.getCodeText().isPresent(), "Code of 4 digit was not generated");
        Assert.assertTrue(kidProofExitIOSPageBase.getKeyboardByPredicate().isPresent(), "Keyboard did not pop up");
        Assert.assertTrue(kidProofExitIOSPageBase.getCodeInputField().isPresent(), "Digits text field is not present");

        // Navigate back and validates that Kid Proof Exit Screen has been closed
        kidProofExitIOSPageBase.getCloseButton().click();
        Assert.assertEquals(moreMenu.getExitKidsProfileButtonText(), "EXIT JUNIOR MODE",
                "Exit Junior Mode option is not present");
        // Click on Exit Kids Profile and validates screen elements
        moreMenu.tapExitKidsProfileButton();
        Assert.assertTrue(kidProofExitIOSPageBase.isKidProofDialogTitleDisplayed(), KID_PROOF_EXIT_SCREEN_DID_NOT_OPEN);
        Assert.assertTrue(kidProofExitIOSPageBase.getCodeInputField().isPresent(), "Digits text field is not present");
        // Enter correct code and validates screen expected
        String code = kidProofExitIOSPageBase.parseExitDigitsCode();
        kidProofExitIOSPageBase.getCodeInputField().type(code);
        Assert.assertTrue(whoIsWatching.isOpened(), "Who is watching page did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67783"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorModeProfileKidsCollection() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), KIDS_PROFILE);
        homePage.clickDynamicCollectionOrContent(2, 2);
        Assert.assertTrue(brandPage.isOpened(), "Brand/Collection page is not open");
        Assert.assertTrue(brandPage.getBackButton().isPresent(), "Back button is not present");
        Assert.assertTrue(brandPage.isKidThemeBackgroudUIDisplayed(),
                "UI on collection page is not in kid mode theme");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75395"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyKidProofExitToggleOffFlow() {
        String ON = "On";
        String OFF = "Off";
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .kidProofExitEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(KIDS_PROFILE);
        Assert.assertTrue(editProfile.getKidProofExitToggleValue().equals(ON), KIDS_PROOF_EXIT_TOGGLE_IS_NOT_ON);

        editProfile.toggleKidsProofExit();
        editProfile.enterPassword(getUnifiedAccount());
        Assert.assertTrue(editProfile.isUpdatedToastPresent(), UPDATED_TOAST_WAS_NOT_DISPLAYED);
        Assert.assertTrue(editProfile.isEditTitleDisplayed(), "Edit profile screen did not open");
        Assert.assertTrue(editProfile.getKidProofExitToggleValue().equals(OFF),
                "'kids proof exit' toggle is not 'Off'");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75500"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProfileContentMaturityRatingRestriction() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String contentUnavailableError = "content-unavailable";
        String contentUnavailableHeader = "No Titles Available";

        //set lower rating
        List<String> ratingSystemValues = getUnifiedAccount().getProfile(DEFAULT_PROFILE).getAttributes()
                .getParentalControls().getMaturityRating().getRatingSystemValues();

        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(), ratingSystemValues.get(0));

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), "Home page did not open");
        launchDeeplink(R.TESTDATA.get("disney_prod_loki_collection_deeplink"));
        Assert.assertTrue(homePage.getStaticTextByLabel(contentUnavailableHeader).isPresent(),
                "Rating Restriction message Header not displayed");
        Assert.assertTrue(homePage.getStaticTextByLabelContains(contentUnavailableError).isPresent(),
                "Rating Restriction message was not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72350"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProfileMinorConsentUI() {
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(null)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(null)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), KIDS_PROFILE);

        updateProfilePage.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        sa.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
        sa.assertTrue(parentalConsent.verifyPrivacyPolicyLink(),
                "Privacy Policy Link is not present on Consent screen");
        sa.assertTrue(parentalConsent.validateConsentText(), "Consent text doesn't match with the expected values");
        sa.assertTrue(parentalConsent.isMinorDisclaimerPresent(), "Consent disclaimer not found");
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            LOGGER.info("Scrolling down to view all of Minor Consent Page");
            parentalConsent.scrollConsentContent(4);
        }
        sa.assertTrue(parentalConsent.isAgreeButtonPresent(), "Agree button is not present");
        sa.assertTrue(parentalConsent.isDeclineButtonPresent(), "Decline button is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67785"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileNavigationBarAlignment() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), KIDS_PROFILE);
        homePage.waitForHomePageToOpen();
        // Creating HashSet to store unique elements distance
        Set<Integer> distanceSet = new HashSet<>();
        List<ExtendedWebElement> navElements = addNavigationBarElements();

        for (int i = 0; i < navElements.size() - 1; i++)
            distanceSet.add(getDistanceBetweenElements(navElements.get(i), navElements.get(i + 1)));

        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            // All tabs in iPhone will be equally spaced, therefore HashSet will have just one entry
            Assert.assertEquals(distanceSet.size(), 1, "Junior mode navigation menu is not aligned in handset");
        } else if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            // In iPads, two tabs are on the right and other 2 are left aligned. Therefore there will be 2 unique distances, hence HashSet will have 2 entries.
            Assert.assertEquals(distanceSet.size(), 2,
                    "Junior mode navigation menu is not correctly aligned in tablet");
            validateElementPositionAlignment(moreMenu.getHomeNav(), LEFT);
            validateElementPositionAlignment(moreMenu.getSearchNav(), LEFT);
            validateElementPositionAlignment(moreMenu.getDownloadNav(), RIGHT);
            validateElementPositionAlignment(moreMenu.getMoreMenuTab(), RIGHT);
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72466"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProfileValidateUpdate() {
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        String dismissCatalog = "NOT NOW";
        String cancelButton = "Cancel";
        SoftAssert sa = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(MICKEY_MOUSE)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), SECONDARY_PROFILE);
        Assert.assertTrue(updateProfilePage.isOpened(),
                "'Let's update your profile' page is not opened");

        // Validate the update profile UI
        sa.assertTrue(updateProfilePage.doesUpdateProfileTitleExist(),
                "Profile title is not present");
        sa.assertTrue(updateProfilePage.isCompleteProfileDescriptionPresent(),
                "Profile subtitle is not present");
        sa.assertTrue(updateProfilePage.isProfileNameFieldPresent(), "Profile Name field is not present");
        sa.assertTrue(editProfile.getDynamicCellByName(MICKEY_MOUSE).isPresent(), "Profile icon is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(), "Pencil icon is not displayed");
        sa.assertTrue(updateProfilePage.isDateOfBirthFieldPresent(), "DOB field is not present");
        sa.assertTrue(updateProfilePage.isGenderFieldPresent(), "Gender field is not present");
        sa.assertTrue(updateProfilePage.isGenderFieldEnabled(),
                "Gender dropdown is not enabled");
        updateProfilePage.clickGenderDropDown();
        updateProfilePage.validateOptionsInGenderDropdown();
        updateProfilePage.getTypeButtonContainsLabel(cancelButton).click();

        // Select DOB and gender
        updateProfilePage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        updateProfilePage.chooseGender();
        updateProfilePage.tapSaveButton();
        // Dismiss full catalog message
        updateProfilePage.getStaticTextByLabelContains(dismissCatalog).click();
        Assert.assertTrue(homePage.isOpened(), "Home page did not open");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78840"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLiveAndUnratedToggleEditProfileScreen() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String ON = "On";
        String OFF = "Off";
        int swipeCount = 3;
        int duration = 500;

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(DARTH_MAUL)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), SECONDARY_PROFILE);
        homePage.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        Assert.assertTrue(swipe(editProfile.getProfileSettingLiveUnratedHeader(), Direction.UP, swipeCount, duration),
                LIVE_TOGGLE_WAS_NOT_DISPLAYED);
        sa.assertTrue(editProfile.getProfileSettingLiveUnratedDesc().isPresent(),
                "Live unrated description is not present");
        sa.assertTrue(editProfile.getProfileSettingLiveUnratedHelpLink().isPresent(),
                "Live unrated hyper link is not present");

        //Toggle is ON by default
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), ON,
                LIVE_TOGGLE_IS_NOT_ON_BY_DEFAULT);

        // Turn Toggle OFF
        editProfile.tapLiveAndUnratedToggle();
        editProfile.waitForUpdatedToastToDisappear();
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), OFF,
                "Live toggle did not turn OFF after tapping on toggle");

        //Turn toggle ON
        editProfile.tapLiveAndUnratedToggle();
        editProfile.enterPassword(getUnifiedAccount());
        editProfile.waitForUpdatedToastToDisappear();
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), ON,
                "Live toggle did not turn On after tapping on toggle");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78841"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLiveAndUnratedToggleJuniorMode() {
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String OFF = "Off";
        int swipeCount = 3;
        int duration = 500;

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(DARTH_MAUL)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
        homePage.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(JUNIOR_PROFILE);
        Assert.assertTrue(swipe(editProfile.getProfileSettingLiveUnratedHeader(), Direction.UP, swipeCount, duration),
                LIVE_TOGGLE_WAS_NOT_DISPLAYED);

        //Live toggle should be disabled
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), OFF,
                "Live toggle was not defaulted to OFF for kid's profile");
        editProfile.tapLiveAndUnratedToggle();
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), OFF,
                "Live toggle was tappable for kids mode");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78842"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLiveAndUnratedContentJuniorMode() {
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        int swipeCount = 3;
        int duration = 500;
        String setTitle, setContentTitle;

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(DARTH_MAUL)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        com.disney.qa.api.explore.response.Set espnUpcomingSet =
                getExploreAPISet(getCollectionName(CollectionConstant.Collection.LIVE_AND_UPCOMING_FROM_ESPN), 50);

        if (espnUpcomingSet == null) {
            throw new SkipException("Skipping test, not able to get the 'Live and unrated' set data from the explore api");
        }

        try {
            setTitle = espnUpcomingSet.getVisuals().getName();
            setContentTitle = espnUpcomingSet.getItems().get(0).getVisuals().getTitle();
        } catch (Exception e) {
            throw new SkipException("Skipping test, not able to get the 'Live and unrated' content from the explore " +
                    "api " + e);
        }

        setAppToHomeScreen(getUnifiedAccount(), JUNIOR_PROFILE);

        // Verify that live and unrated content is not shown
        LOGGER.info("'Live and unrated' content under test -> set Name: {} and title: {}", setTitle, setContentTitle);
        Assert.assertFalse(swipe(homePage.getStaticTextByLabel(setTitle), Direction.UP, swipeCount, duration),
                "'Live and unrated' collection header is found on kid's profile");
        Assert.assertFalse(swipe(homePage.getStaticTextByLabel(setContentTitle), Direction.DOWN, swipeCount, duration),
                "'Live and unrated' title was found on kid's profile");

        homePage.clickMoreTab();
        moreMenu.tapExitKidsProfileButton();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(JUNIOR_PROFILE);
        Assert.assertTrue(swipe(editProfile.getProfileSettingLiveUnratedHeader(), Direction.UP, swipeCount, duration),
                LIVE_TOGGLE_WAS_NOT_DISPLAYED);

        //verify tooltip
        editProfile.getProfileSettingLiveUnratedHeader().click();
        Assert.assertTrue(editProfile.getProfileSettingLiveUnratedTooltip().isPresent(),
                "Live and unrated toggle Tool tip for junior mode was not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78839"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLiveAndUnratedToggleUI() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String ON = "On";
        int swipeCount = 3;
        int duration = 500;

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(DARTH_MAUL)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());
        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);

        //Toggle is ON by default for secondary profile
        homePage.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        Assert.assertTrue(swipe(editProfile.getProfileSettingLiveUnratedHeader(), Direction.UP, swipeCount, duration),
                LIVE_TOGGLE_WAS_NOT_DISPLAYED);

        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), ON, LIVE_TOGGLE_IS_NOT_ON_BY_DEFAULT +
                " for secondary profile");
        editProfile.clickDoneBtn();

        //Toggle is ON by default for primary profile
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(DEFAULT_PROFILE);
        Assert.assertTrue(swipe(editProfile.getProfileSettingLiveUnratedHeader(), Direction.UP, swipeCount, duration),
                LIVE_TOGGLE_WAS_NOT_DISPLAYED);
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), ON, LIVE_TOGGLE_IS_NOT_ON_BY_DEFAULT +
                " for primary profile");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77686"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLiveAndUnratedToggleOff() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        String ON = "On";
        String OFF = "Off";
        int swipeCount = 3;
        int duration = 500;
        String setTitle, setContentTitle;

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        com.disney.qa.api.explore.response.Set espnUpcomingSet =
                getExploreAPISet(getCollectionName(CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING), 50);

        if (espnUpcomingSet == null) {
            throw new SkipException("Skipping test, not able to get the 'Live and unrated' set data from the explore api");
        }

        try {
            setTitle = espnUpcomingSet.getVisuals().getName();
            setContentTitle = espnUpcomingSet.getItems().get(0).getVisuals().getTitle();
        } catch (Exception e) {
            throw new SkipException("Skipping test, not able to get the 'Live and unrated' content from the explore " +
                    "api" + e);
        }

        // Verify that live and unrated content is shown
        LOGGER.info("'Live and unrated' content under test -> set Name: {} and title: {}", setTitle, setContentTitle);
        Assert.assertTrue(swipe(homePage.getStaticTextByLabel(setTitle), Direction.UP, swipeCount, duration),
                "'live and unrated' collection is not found on home page");
        Assert.assertTrue(homePage.getStaticTextByLabel(setContentTitle).isPresent(), "not able to find live title");

        //Toggle is ON by default for secondary profile
        homePage.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(DEFAULT_PROFILE);
        Assert.assertTrue(swipe(editProfile.getProfileSettingLiveUnratedHeader(), Direction.UP, swipeCount, duration),
                LIVE_TOGGLE_WAS_NOT_DISPLAYED);

        //Toggle is ON by default
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), ON,
                LIVE_TOGGLE_IS_NOT_ON_BY_DEFAULT);

        // Turn Toggle OFF
        editProfile.tapLiveAndUnratedToggle();
        editProfile.waitForUpdatedToastToDisappear();
        Assert.assertEquals(editProfile.getLiveAndUnratedToggleState(), OFF,
                "Live toggle did not turn OFF after tapping on toggle");
        editProfile.clickDoneBtn();
        homePage.waitForHomePageToOpen();

        // Verify that live and unrated content is not shown
        Assert.assertFalse(swipe(homePage.getStaticTextByLabel(setTitle), Direction.UP, swipeCount, duration),
                "Live and unrated collection was present after turning off the live toggle");

        Assert.assertFalse(swipe(homePage.getStaticTextByLabel(setContentTitle), Direction.DOWN, swipeCount, duration),
                "Live content title was present after turning off the live toggle");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78050"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.ACCOUNT_SHARING, TestGroup.PRE_CONFIGURATION, US})
    public void verifyExtraMemberCanNotSeeOwnersProfile() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);

        setAccount(createAccountSharingUnifiedAccounts().getReceivingAccount());
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickMoreTab();
        moreMenuPage.clickEditProfilesBtn();

        Assert.assertTrue(editProfilePage.isOpened(), EDIT_PROFILE_PAGE_NOT_DISPLAYED);
        Assert.assertEquals(editProfilePage.getQuantityOfProfileCells(), 1,
                "Number of profile cells wasn't equal to 1");
        Assert.assertFalse(editProfilePage.getAddProfileBtn().isElementPresent(FIVE_SEC_TIMEOUT),
                "Add Profile button is present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77236"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.ACCOUNT_SHARING, TestGroup.PRE_CONFIGURATION, US})
    public void verifyExtraMemberCanUpdateProfile() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusEditGenderIOSPageBase editGenderPage = initPage(DisneyPlusEditGenderIOSPageBase.class);

        //Get possible avatars from Unified Search API and save the ID for the first avatar from the last set
        List<ContentSet> avatarSets = getAvatarSets(getUnifiedAccount());
        String lastSetFirstAvatarId = "";
        try {
            lastSetFirstAvatarId = avatarSets.get(avatarSets.size() - 1).getAvatarIds().get(0);
        } catch (IndexOutOfBoundsException e) {
            Assert.fail("Index out of bounds: " + e);
        }

        setAccount(createAccountSharingUnifiedAccounts().getReceivingAccount());
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickMoreTab();
        BufferedImage originalAvatar = getElementImage(moreMenuPage.getProfileAvatar(DEFAULT_PROFILE));
        moreMenuPage.clickEditProfilesBtn();

        Assert.assertTrue(editProfilePage.isOpened(), EDIT_PROFILE_PAGE_NOT_DISPLAYED);
        editProfilePage.clickEditModeProfile(getUnifiedAccount().getFirstName());

        //Update profile's name
        editProfilePage.enterProfileName(EXTRA_MEMBER_PROFILE);

        //Update profile's avatar
        editProfilePage.getAddProfileAvatar().click();
        swipePageTillElementTappable(
                chooseAvatarPage.getTypeCellNameContains(lastSetFirstAvatarId),
                10,
                null,
                Direction.UP,
                1000);
        chooseAvatarPage.getTypeCellNameContains(lastSetFirstAvatarId).click();

        //Update profile's gender and save all changes
        editProfilePage.clickGenderButton();
        editGenderPage.clickGenderDropDown();
        String desiredGender = editGenderPage.selectGender(DisneyPlusEditGenderIOSPageBase.GenderOption.GENDER_MEN);
        editGenderPage.getTypeButtonByLabel(desiredGender).click();
        editGenderPage.tapSaveButton();
        editProfilePage.getDoneButton().click();

        //Go from Home to More Options and validate avatar and name changed
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        moreMenuPage.clickMoreTab();
        Assert.assertTrue(moreMenuPage.isOpened(), MORE_MENU_NOT_DISPLAYED);
        Assert.assertTrue(moreMenuPage.getStaticTextByLabel(EXTRA_MEMBER_PROFILE).isElementPresent(),
                "Extra Member profile is not visible");
        BufferedImage updatedAvatar = getElementImage(moreMenuPage.getProfileAvatar(EXTRA_MEMBER_PROFILE));
        Assert.assertTrue(areImagesDifferent(originalAvatar, updatedAvatar),
                "Avatar image is not updated");

        //Go to edit profile to validate gender changed
        moreMenuPage.clickEditProfilesBtn();
        Assert.assertTrue(editProfilePage.isOpened(), EDIT_PROFILE_PAGE_NOT_DISPLAYED);
        editProfilePage.clickEditModeProfile(EXTRA_MEMBER_PROFILE);
        Assert.assertTrue(editProfilePage.isGenderButtonPresent(), "Gender button is not present");
        Assert.assertTrue(editProfilePage.getStaticTextByLabel(desiredGender).isElementPresent(),
                "Gender is not updated");
    }

    private List<ExtendedWebElement> addNavigationBarElements() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        List<ExtendedWebElement> navElements = new ArrayList<>();
        navElements.add(moreMenu.getHomeNav());
        navElements.add(moreMenu.getSearchNav());
        navElements.add(moreMenu.getDownloadNav());
        navElements.add(moreMenu.getMoreMenuTab());
        return navElements;
    }

    private List<ContentSet> getAvatarSets(UnifiedAccount account) {
        List<ContentSet> avatarSets = getUnifiedSearchApi()
                .getAllSetsInAvatarCollection(account, getCountry(), getLanguage());
        if (avatarSets.isEmpty()) {
            throw new NoSuchElementException("No avatar sets were found");
        } else {
            return avatarSets;
        }
    }

    private List<ContentSet> getAvatarSets(DisneyAccount account) {
        List<ContentSet> avatarSets = getSearchApi().getAllSetsInAvatarCollection(account, getCountry(), getLanguage());
        if (avatarSets.isEmpty()) {
            throw new SkipException("Skipping test, no avatar sets were found.");
        } else {
            return avatarSets;
        }
    }

    private void verifyAutoPlayStateForProfile(String profile, String autoPlayState, SoftAssert sa) {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        editProfile.clickMoreTab();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(profile);
        sa.assertTrue(editProfile.getAutoplayState().equals(autoPlayState), "autoplay state wasn't saved for profile" + profile + ":" + autoPlayState);
    }

    private void createKidsProfile() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        addProfile.enterProfileName(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.MINOR.getMonth(), Person.MINOR.getDay(), Person.MINOR.getYear());
        addProfile.tapJuniorModeToggle();
        addProfile.clickSaveProfileButton();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            parentalConsent.scrollConsentContent(4);
        }
        clickElementAtLocation(parentalConsent.getTypeButtonByLabel("AGREE"), 50, 50);
    }

    private void configureKidsProfileProofExit() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(KIDS_PROFILE);
        // Validates kids proof exit option and toggle it
        Assert.assertTrue(editProfile.getKidProofExitLabel().isPresent(), "Kids Proof Exit label was not present");
        editProfile.toggleKidsProofExit();
        passwordPage.enterPassword(getUnifiedAccount());
        editProfile.waitForUpdatedToastToDisappear();
        editProfile.clickDoneBtn();
    }
}
