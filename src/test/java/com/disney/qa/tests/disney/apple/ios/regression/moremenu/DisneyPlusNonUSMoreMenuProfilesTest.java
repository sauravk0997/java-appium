package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

public class DisneyPlusNonUSMoreMenuProfilesTest extends DisneyBaseTest {

    private static final String FIRST = "01";
    private static final String TWENTY_EIGHTEEN = "2018";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66766"})
    @Test(description = "Add Profile Button & Flow (Legacy - No DOB or Gender Collection)", groups = {"NonUS-More Menu"}, enabled = false)
    public void verifyAddProfileFlow() {
        initialSetup();
        handleAlert();
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountFor("JP",  getLocalizationUtils().getUserLanguage()));
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);

        //Add Profiles
        getAccountApi().addProfile(getAccount(), SECONDARY_PROFILE, ADULT_DOB, getLocalizationUtils().getUserLanguage(),
                RAYA, false, true);
        getAccountApi().addProfile(getAccount(), "Third", ADULT_DOB, getLocalizationUtils().getUserLanguage(),
                BABY_YODA, false, true);
        getAccountApi().addProfile(getAccount(), "Fourth", ADULT_DOB, getLocalizationUtils().getUserLanguage(),
                RAYA, false, true);
        getAccountApi().addProfile(getAccount(), "Fifth", ADULT_DOB, getLocalizationUtils().getUserLanguage(),
                BABY_YODA, false, true);
        getAccountApi().addProfile(getAccount(), "Sixth", ADULT_DOB, getLocalizationUtils().getUserLanguage(),
                RAYA, false, true);

        setAppToHomeScreen(getAccount(), DEFAULT_PROFILE);
        handleAlert();
        moreMenu.clickMoreTab();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            swipeInContainer(moreMenu.getProfileSelectionCollectionView(), Direction.LEFT, 500);
        }
        moreMenu.clickAddProfile();
        sa.assertTrue(chooseAvatar.isOpened(), "`Choose Avatar` screen was not opened.");
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        sa.assertTrue(addProfile.isOpened(), "'Add Profile' page was not opened.");
        addProfile.enterProfileName("Seventh");
        addProfile.clickSaveBtn();
        pause(2);
        //Not now button
        addProfile.clickSecondaryButtonByCoordinates();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            swipeInContainer(moreMenu.getProfileSelectionCollectionView(), Direction.LEFT, 500);
        }
        sa.assertFalse(moreMenu.isAddProfileButtonPresent(), "Add profile button was present when the account had seven profiles on it");
        moreMenu.clickEditProfilesBtn();
        editProfilePage.getDoneButton().click();
        pause(2);
        sa.assertTrue(whoIsWatching.isOpened(), "who is watching page didn't open");
        sa.assertFalse(moreMenu.isAddProfileButtonPresent(), "Add profile button was present on who is watching screen when the account had seven profiles on it");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66792"})
    @Test(description = "Edit Profile - UI Elements - Primary Profile (NOT ARIEL)", groups = {"NonUS-More Menu"})
    public void verifyEditProfileUIPrimaryProfile() {
        initialSetup();
        handleAlert();
        setAccount(createAccountFor("JP",  getLocalizationUtils().getUserLanguage()));
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        handleAlert();

        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(DEFAULT_PROFILE);
        sa.assertTrue(editProfile.isEditTitleDisplayed(),"Edit profile Title is not displayed");
        sa.assertTrue(editProfile.getDoneButton().isPresent(SHORT_TIMEOUT),"Done button is not displayed");
        sa.assertTrue(editProfile.getDynamicCellByName(MICKEY_MOUSE).isPresent(),"profile icon is not displayed");
        sa.assertTrue(editProfile.getPrimaryProfileExplainer().isPresent(SHORT_TIMEOUT),"Primary profile explainer is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(SHORT_TIMEOUT),"pencil icon is not displayed");
        sa.assertTrue(editProfile.getTextEntryField().getText().equals(DEFAULT_PROFILE),"Profile name is not displayed");
        sa.assertTrue(editProfile.isPlayBackSettingsSectionDisplayed(),"Playback setting section is not as expected");
        sa.assertTrue(editProfile.isFeatureSettingsSectionDisplayed(),"Feature setting section is not as expected");
        sa.assertTrue(editProfile.isParentalControlHeadingDisplayed(),"Parental control section is not as expected");
        sa.assertTrue(editProfile.isMaturityRatingSectionDisplayed("PG"),"Maturity Rating section is not as expected");
        sa.assertFalse(editProfile.isDeleteProfileButtonPresent(),"Delete profile button is displayed");
        sa.assertFalse(editProfile.getKidProofExitLabel().isPresent(SHORT_TIMEOUT),"Kid proof exit label is displayed");
        sa.assertFalse(editProfile.getKidProofDescription().isPresent(SHORT_TIMEOUT),"Kid proof description is displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73708"})
    @Test(description = "Edit Profile - UI Elements - (Legacy) - Kids Mode Profile", groups = {"NonUS-More Menu"})
    public void verifyEditProfileUIKidsProfile() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        initialSetup();
        handleAlert();
        setAccount(createAccountFor("JP",  getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(getAccount(), KIDS_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA, true, true);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        handleAlert();
        whoIsWatching.clickEditProfile();
        editProfile.clickEditModeProfile(KIDS_PROFILE);
        sa.assertTrue(editProfile.isEditTitleDisplayed(),"Edit profile Title is not displayed");
        sa.assertTrue(editProfile.getDoneButton().isPresent(SHORT_TIMEOUT),"Done button is not displayed");
        sa.assertTrue(editProfile.getDynamicCellByName(BABY_YODA).isPresent(),"profile icon is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(SHORT_TIMEOUT),"pencil icon is not displayed");
        sa.assertTrue(editProfile.getTextEntryField().getText().equals(KIDS_PROFILE),"Profile name is not displayed");
        sa.assertTrue(editProfile.isPlayBackSettingsSectionDisplayed(),"Playback setting section is not as expected");
        sa.assertTrue(editProfile.isFeatureSettingsSectionDisplayed(),"Feature setting section is not as expected");
        sa.assertTrue(editProfile.isParentalControlSectionDisplayed(),"Parental control section is not as expected");
        sa.assertTrue(editProfile.getJuniorModeToggleValue().equals("On"), "Junior mode toggle was not present");
        sa.assertTrue(editProfile.getProfilePinHeader().isPresent(), "Profile pin header is not displayed");
        sa.assertTrue(editProfile.getProfilePinDescription().isPresent(), "Profile pin description is not displayed");
        sa.assertTrue(editProfile.isDeleteProfileButtonPresent(),"Delete profile button is displayed");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66776"})
    @Test(description = "Add Profile UI - (Legacy - No DOB or Gender Collection)", groups = {"NonUS-More Menu"}, enabled = false)
    public void verifyAddProfilePageUI() {
        initialSetup();
        handleAlert();
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountFor("JP",  getLocalizationUtils().getUserLanguage()));
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        handleAlert();
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "`Choose Avatar` screen was not opened.");
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[0].click();
        sa.assertTrue(addProfile.isOpened(), "'Add Profile' page was not opened.");
        sa.assertTrue(addProfile.isAddProfileHeaderPresent(), "Add Profile header was not found");
        sa.assertTrue(addProfile.isProfileNamefieldPresent(), "Profile Name field was not found");
        sa.assertTrue(addProfile.iskidsProfileToggleCellPresent(), " Kids profile toogle was not found");
        sa.assertTrue(addProfile.getkidsProfileToggleCellValue().equalsIgnoreCase("off"), "Kid profile toogle was not turned off by default");
        sa.assertTrue(addProfile.isSaveBtnPresent(), "Save button was not found");
        sa.assertTrue(addProfile.isCancelButtonPresent(), "Cancel button was not found");
        sa.assertFalse(addProfile.isDateOfBirthFieldPresent(), "Date Of Birth field was found");
        sa.assertFalse(addProfile.isGenderFieldPresent(), "Gender field was found");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69535"})
    @Test(description = "Verify the flows when Profile Creation is restricted", groups = {"NonUS More Menu", TestGroup.PRE_CONFIGURATION},enabled = false)
    public void verifyProfileCreationRestrictedFunctionality() {
        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusParentalConsentIOSPageBase parentalConsent = initPage(DisneyPlusParentalConsentIOSPageBase.class);


        disneyPlusAccountIOSPageBase.toggleRestrictProfileCreation(IOSUtils.ButtonStatus.ON);

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon toggling 'Restrict Profile Creation'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(getAccount().getUserPass());

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase1 = new DisneyPlusAccountIOSPageBase(getDriver());
        sa.assertTrue(disneyPlusAccountIOSPageBase1.isRestrictProfileCreationEnabled(),
                "'Restrict Profile Creation' toggle was not enabled after submitting credentials");

        pause(1);
        disneyPlusAccountIOSPageBase.getBackArrow().click();
        disneyPlusMoreMenuIOSPageBase.clickAddProfile();

        Assert.assertTrue(disneyPlusPasswordIOSPageBase.isOpened(),
                "User was not directed to Password entry upon clicking 'Add Profile'");

        disneyPlusPasswordIOSPageBase.submitPasswordWhileLoggedIn(getAccount().getUserPass());

        disneyPlusEditProfileIOSPageBase.clickSkipBtn();
        disneyPlusEditProfileIOSPageBase.enterProfileName(RESTRICTED);
        disneyPlusEditProfileIOSPageBase.enterDOB(DateHelper.Month.JANUARY, FIRST, TWENTY_EIGHTEEN);
        disneyPlusEditProfileIOSPageBase.tapJuniorModeToggle();
        disneyPlusEditProfileIOSPageBase.clickSaveBtn();
        sa.assertTrue(parentalConsent.isConsentHeaderPresent(), "Consent header was not present");
        parentalConsent.scrollConsentContent(2);
        parentalConsent.tapAgreeButton();
        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isProfileSwitchDisplayed(RESTRICTED),
                "Profile created after submitting credentials was not saved");
        sa.assertAll();
    }
    private void setAppToAccountSettings() {
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }
}
