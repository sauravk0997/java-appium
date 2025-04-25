package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.*;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_STANDARD;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.PROFILE_SETTINGS_KIDPROOF_STATUS_ON;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusNonUSMoreMenuProfilesTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66766"})
    @Test(groups = {TestGroup.MORE_MENU, TestGroup.SMOKE, JP})
    public void verifyAddProfileFlow() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        initialSetup();
        handleAlert();
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(
                        DISNEY_PLUS_STANDARD,
                        JP,
                        getLocalizationUtils().getUserLanguage())));

        //Add Profiles
        List<String> profiles = Arrays.asList("Profile 2", "Profile 3", "Profile 4", "Profile 5", "Profile 6");
        profiles.forEach(profile ->
                getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                        .unifiedAccount(getUnifiedAccount())
                        .profileName(profile)
                        .dateOfBirth(ADULT_DOB)
                        .language(getLocalizationUtils().getUserLanguage())
                        .avatarId("5")
                        .kidsModeEnabled(false)
                        .isStarOnboarded(true)
                        .build()));

        setAppToHomeScreen(getUnifiedAccount());
        handleAlert();
        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        if (homePage.isTravelAlertTitlePresent()) {
            homePage.getTravelAlertOk().click();
        }

        moreMenu.clickMoreTab();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            swipeInContainer(moreMenu.getProfileSelectionCollectionView(), Direction.LEFT, 500);
        }
        whoIsWatching.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "`Choose Avatar` screen was not opened");
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        Assert.assertTrue(addProfile.isOpened(), "'Add Profile' page was not opened");
        addProfile.enterProfileName("Seventh");
        addProfile.clickSaveBtn();
        editProfilePage.waitForUpdatedToastToDisappear();
        //Not now button
        addProfile.clickSecondaryButtonByCoordinates();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            swipeInContainer(moreMenu.getProfileSelectionCollectionView(), Direction.LEFT, 500);
        }
        Assert.assertFalse(moreMenu.isAddProfileButtonPresent(),
                "Add profile button was present when the account had seven profiles on it");
        moreMenu.clickEditProfilesBtn();
        editProfilePage.getDoneButton().click();
        editProfilePage.waitForUpdatedToastToDisappear();
        Assert.assertTrue(whoIsWatching.isOpened(), "who is watching page didn't open");
        Assert.assertFalse(moreMenu.isAddProfileButtonPresent(),
                "Add profile button was present on who is watching " +
                        "screen when the account had seven profiles on it");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66792"})
    @Test(groups = {TestGroup.MORE_MENU, JP})
    public void verifyEditProfileUIPrimaryProfile() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        initialSetup();
        handleAlert();
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(
                        DISNEY_PLUS_STANDARD,
                        JP,
                        getLocalizationUtils().getUserLanguage())));

        setAppToHomeScreen(getUnifiedAccount());
        handleAlert();

        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(DEFAULT_PROFILE);
        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Edit profile Title is not displayed");
        sa.assertTrue(editProfile.getDoneButton().isPresent(SHORT_TIMEOUT), "Done button is not displayed");
        sa.assertTrue(editProfile.getDynamicCellByName(MICKEY_MOUSE).isPresent(), "profile icon is not displayed");
        sa.assertTrue(editProfile.getPrimaryProfileExplainer().isPresent(SHORT_TIMEOUT), "Primary profile explainer is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(SHORT_TIMEOUT), "pencil icon is not displayed");
        sa.assertTrue(editProfile.getTextEntryField().getText().equals(DEFAULT_PROFILE), "Profile name is not displayed");
        sa.assertTrue(editProfile.isPlayBackSettingsSectionDisplayed(), "Playback setting section is not as expected");
        sa.assertTrue(editProfile.isFeatureSettingsSectionDisplayed(), "Feature setting section is not as expected");
        sa.assertTrue(editProfile.isParentalControlHeadingDisplayed(), "Parental control section is not as expected");
        sa.assertTrue(editProfile.isMaturityRatingSectionDisplayed("PG"), "Maturity Rating section is not as expected");
        sa.assertFalse(editProfile.isDeleteProfileButtonPresent(), "Delete profile button is displayed");
        sa.assertFalse(editProfile.getKidProofExitLabel().isPresent(SHORT_TIMEOUT), "Kid proof exit label is displayed");
        sa.assertFalse(editProfile.getKidProofDescription().isPresent(SHORT_TIMEOUT), "Kid proof description is displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73708"})
    @Test(groups = {TestGroup.MORE_MENU, JP})
    public void verifyEditProfileUIKidsProfile() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String onStatusValue = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                PROFILE_SETTINGS_KIDPROOF_STATUS_ON.getText());

        initialSetup();
        handleAlert();
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(
                        DISNEY_PLUS_STANDARD,
                        JP,
                        getLocalizationUtils().getUserLanguage())));

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

        handleAlert();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(KIDS_PROFILE);
        sa.assertTrue(editProfile.isEditTitleDisplayed(), "Edit profile Title is not displayed");
        sa.assertTrue(editProfile.getDoneButton().isPresent(SHORT_TIMEOUT), "Done button is not displayed");
        sa.assertTrue(editProfile.getDynamicCellByName(BABY_YODA).isPresent(), "profile icon is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(SHORT_TIMEOUT), "pencil icon is not displayed");
        sa.assertTrue(editProfile.getTextEntryField().getText().equals(KIDS_PROFILE), "Profile name is not displayed");
        sa.assertTrue(editProfile.isPlayBackSettingsSectionDisplayed(), "Playback setting section is not as expected");
        sa.assertTrue(editProfile.isFeatureSettingsSectionDisplayed(), "Feature setting section is not as expected");
        sa.assertTrue(editProfile.isParentalControlSectionDisplayed(), "Parental control section is not as expected");
        sa.assertTrue(editProfile.getJuniorModeToggleValue().equals(onStatusValue),
                "Junior mode toggle was not present");
        sa.assertTrue(editProfile.getProfilePinHeader().isPresent(), "Profile pin header is not displayed");
        sa.assertTrue(editProfile.getProfilePinDescription().isPresent(), "Profile pin description is not displayed");
        sa.assertTrue(editProfile.isDeleteProfileButtonPresent(), "Delete profile button is displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66776"})
    @Test(groups = {TestGroup.MORE_MENU, JP})
    public void verifyAddProfilePageUI() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        String offInJP = "オフ";
        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(
                        DISNEY_PLUS_STANDARD,
                        JP,
                        getLocalizationUtils().getUserLanguage())));
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getUnifiedAccount());
        handleAlert();
        moreMenu.clickMoreTab();
        whoseWatchingPage.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "`Choose Avatar` screen was not opened.");
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        sa.assertTrue(addProfile.isOpened(), "'Add Profile' page was not opened.");
        sa.assertTrue(addProfile.isWhoIsWatchingAddProfileHeaderPresent(), "Add Profile header was not found");
        sa.assertTrue(addProfile.isProfileNameFieldPresent(), "Profile Name field was not found");
        sa.assertTrue(addProfile.isKidsProfileToggleCellPresent(), " Kids profile toggle was not found");
        sa.assertTrue(addProfile.getKidsProfileToggleCellValue().equalsIgnoreCase(offInJP),
                "Kid profile toggle was not turned off by default");
        sa.assertTrue(addProfile.isSaveBtnPresent(), "Save button was not found");
        sa.assertTrue(addProfile.isCancelButtonPresent(), "Cancel button was not found");
        sa.assertFalse(addProfile.isDateOfBirthFieldPresent(), "Date Of Birth field was found");
        sa.assertFalse(addProfile.isGenderFieldPresent(), "Gender field was found");
        sa.assertAll();
    }
}
