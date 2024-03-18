package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.lang.invoke.MethodHandles;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;

public class DisneyPlusMoreMenuProfilesTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String ADULT_DOB = "1923-10-23";
    private static final String THE_CHILD = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";

    private void onboard() {
        setAppToHomeScreen(getAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62628", "XMOBQA-62630"})
    @Test(description = "verify Avatar Selection UI & user's selected Avatar appears", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyAvatarSelection() {
        DisneyPlusMoreMenuIOSPageBase MoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase EditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        onboard();
        MoreMenuIOSPageBase.clickAddProfile();

        // Verify choose avatar page UI
        Assert.assertTrue(chooseAvatarPage.isOpened(), "XMOBQA-62628 - Choose Avatar page was not opened");
        sa.assertTrue(chooseAvatarPage.isSkipButtonPresent(), "XMOBQA-62628 - skip button not present on Choose Avatar page");
        sa.assertTrue(chooseAvatarPage.getBackArrow().isPresent(), "XMOBQA-62628 - back button not present on Choose Avatar page");

        //Choose avatar
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        BufferedImage selectedAvatar = getElementImage(avatars[0]);
        avatars[0].click();

        //Verify that selected avatar appears on Add profile page
        Assert.assertTrue(addProfile.isAddProfilePageOpened(), "XMOBQA-62630 - User was not taken to the 'Add Profiles' page as expected");
        BufferedImage addProfileAvatar = getElementImage(addProfile.getAddProfileAvatar());
        selectedAvatar = getScaledImage(selectedAvatar, addProfileAvatar.getWidth(), addProfileAvatar.getHeight());

        sa.assertTrue(areImagesTheSame(addProfileAvatar, selectedAvatar, 10),
                "XMOBQA-62630 - Avatar Selected was either not displayed or was altered beyond the accepted margin of error");
        //Finish creating profile
        if (getAccount().getProfileLang().equalsIgnoreCase("en")) {
            addProfile.createProfile(SECONDARY_PROFILE, DateHelper.Month.OCTOBER, "23", "1923");
        }
        sa.assertTrue(EditProfileIOSPageBase.isServiceEnrollmentAccessFullCatalogPagePresent(), "Not on serviceEnrollmentAccessFullCatalog page");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        addProfile.clickSecondaryButtonByCoordinates();
        //Verify that selected avatar appears on More menu page
        BufferedImage moreMenuAvatar = getElementImage(MoreMenuIOSPageBase.getProfileAvatar(SECONDARY_PROFILE));
        BufferedImage selectedAvatarCopy = getScaledImage(cloneBufferedImage(selectedAvatar), moreMenuAvatar.getWidth(), moreMenuAvatar.getHeight());

        LOGGER.info("Comparing selected avatar to 'More Menu' display...");
        sa.assertTrue(areImagesTheSame(selectedAvatarCopy, moreMenuAvatar, 10),
                "XMOBQA-62630 - Avatar displayed in the More Menu was either not displayed or was altered beyond the accepted margin of error");
        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62638"})
    @Test(description = "Verify: Edit Profile User can change Avatar", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyEditProfileUserCanChangeAvatar() {
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = new DisneyPlusChooseAvatarIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        ExtendedWebElement[] avatars;

        setAppToHomeScreen(getAccount());
        disneyPlusMoreMenuIOSPageBase.clickMoreTab();
        BufferedImage moreMenuAvatar = getElementImage(disneyPlusMoreMenuIOSPageBase.getProfileAvatar(DEFAULT_PROFILE));

        disneyPlusMoreMenuIOSPageBase.clickEditProfilesBtn();
        disneyPlusEditProfileIOSPageBase.clickEditModeProfile(DEFAULT_PROFILE);
        disneyPlusEditProfileIOSPageBase.getAddProfileAvatar().click();
        chooseAvatarPage.isOpened();
        chooseAvatarPage.verifyChooseAvatarPage();
        avatars = disneyPlusEditProfileIOSPageBase.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[3].click();
        BufferedImage addProfileAvatar = getElementImage(disneyPlusEditProfileIOSPageBase.getAddProfileAvatar());
        BufferedImage moreMenuAvatarCopy = getScaledImage(moreMenuAvatar, addProfileAvatar.getWidth(), addProfileAvatar.getHeight());

        LOGGER.info("Comparing selected avatar to 'Edit Profiles' display...");
        sa.assertFalse(areImagesTheSame(moreMenuAvatarCopy, addProfileAvatar, 10),
                "XMOBQA-62630 - Updated Avatar displayed in the Edit Profiles display was either not displayed or was altered beyond the accepted margin of error");
        disneyPlusEditProfileIOSPageBase.clickSaveProfileButton();

        sa.assertAll();
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62634"})
    @Test(description = "Verify: User cannot select the same avatar for multiple profiles", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyUserCanNotSelectTheSameAvatarForMultipleProfiles() {
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        getAccountApi().addProfile(getAccount(), SECONDARY_PROFILE, ADULT_DOB, getAccount().getProfileLang(), THE_CHILD, false, true);
        disneyPlusMoreMenuIOSPageBase.clickMoreTab();
        disneyPlusMoreMenuIOSPageBase.clickAddProfile();
        ExtendedWebElement[] avatars = disneyPlusEditProfileIOSPageBase.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        for (ExtendedWebElement avatar : avatars) {
            LOGGER.info("Verifying that avatar with label '{}' does not have the same avatar as the previously selected one...", avatar.getText());
            sa.assertNotEquals(avatar.getAttribute("name"), THE_CHILD,
                    "XMOBQA-62634 - The previously selected Avatar was available for selection unexpectedly");
        }
        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61269"})
    @Test(description = "Autoplay toggle is Saved if User saves", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAutoplayToggleIsSaved() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        //Turn ON autoplay
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getAccount().getFirstName());
        editProfile.toggleAutoplayButton("OFF");
        sa.assertTrue(editProfile.isUpdatedToastPresent(), "'Updated' toast was not present");
        sa.assertAll();
    }
    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74793"})
    @Test(description = "Add Profile(Secondary Profile) Age > 18+ defaults to TV-MA", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74794"})
    @Test(description = "Add Profile - (Secondary Profile) Age <18, default to TV-14 and trigger Welch Flow", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66770"})
    @Test(description = "Add Profile - Seventh Profile", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifySeventhProfile() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = new DisneyPlusChooseAvatarIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

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

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61261"})
    @Test(description = "Add Profile - (Secondary Profile) Age <18, default to TV-14 and trigger Welch Flow", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyEditProfileSecondaryProfileUIElements() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase editProfile = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(getAccount(),SECONDARY_PROFILE,ADULT_DOB,getAccount().getProfileLang(),THE_CHILD,false,true);
        setAppToHomeScreen(getAccount());
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        sa.assertTrue(editProfile.isEditTitleDisplayed(),"Edit profile Title is not displayed");
        sa.assertTrue(editProfile.isProfileIconDisplayed(THE_CHILD),"profile icon is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(),"pencil icon is not displayed");
        sa.assertTrue(editProfile.isPersonalInformationSectionDisplayed(),"Personal information section is not as expected");
        sa.assertTrue(editProfile.isPlayBackSettingsSectionDisplayed(),"Playback setting section is not as expected");
        sa.assertTrue(editProfile.isFeatureSettingsSectionDisplayed(),"Feature setting section is not as expected");
        sa.assertTrue(editProfile.isParentalControlSectionDisplayed(),"Parental control section is not as expected");
        sa.assertTrue(editProfile.isMaturityRatingSectionDisplayed(),"Maturity Rating section is not as expected");
        sa.assertTrue(editProfile.isDeleteProfileButtonPresent(),"Delete profile button is not displayed");
        sa.assertTrue(editProfile.getDoneButton().isPresent(),"Done button is not displayed");
        addProfile.updateUserName("updated_profile");
        editProfile.getDoneButton().click();
        moreMenu.clickMoreTab();
        sa.assertTrue(whoIsWatching.getDynamicAccessibilityId("updated_profile").isPresent(), "updated profile name is not displayed");
        whoIsWatching.clickProfile("updated_profile");
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile("updated_profile");
        sa.assertTrue(editProfile.isDeleteProfileButtonPresent(),"Delete profile button is not displayed");
        editProfile.getDeleteProfileButton().click();
        editProfile.clickConfirmDeleteButton();
        sa.assertFalse(whoIsWatching.getDynamicAccessibilityId("updated_profile").isPresent(), "Profile is not deleted");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61261"})
    @Test(description = "Profile Selection Page UI", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyProfileSelectionPageUI() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(getAccount(),SECONDARY_PROFILE,ADULT_DOB,getAccount().getProfileLang(),THE_CHILD,false,true);
        setAppToHomeScreen(getAccount());
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        moreMenu.clickMoreTab();
        sa.assertTrue(moreMenu.isEditProfilesBtnPresent(),"Edit Profile CTA wasn't displayed on who's watching screen");
        sa.assertTrue(moreMenu.isAddProfileButtonPresent(),"Add Profile CTA wasn't displayed on who's watching screen");
        sa.assertTrue(moreMenu.getStaticTextByLabel(SECONDARY_PROFILE).isPresent(),"Profile name wasn't displayed on who's watching screen");
        sa.assertTrue(moreMenu.getStaticTextByLabel(DEFAULT_PROFILE).isPresent(),"Profile name wasn't displayed on who's watching screen");
        sa.assertTrue(whoIsWatching.isProfileIconPresent(DEFAULT_PROFILE),"Profile icon cell wasn't displayed for default profile");
        sa.assertTrue(whoIsWatching.isProfileIconPresent(SECONDARY_PROFILE),"Profile icon cell wasn't displayed for secondary profile");
        sa.assertAll();
    }

}
