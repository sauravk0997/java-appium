package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;

public class DisneyPlusMoreMenuProfilesTest extends DisneyBaseTest {

    private static final String TEST = "Test";
    private static final String ADULT_DOB = "1923-10-23";
    private static final String THE_CHILD = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";

    private void onboard() {
        setAppToHomeScreen(disneyAccount.get());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62628", "XMOBQA-62630"})
    @Test(description = "verify Avatar Selection UI & user's selected Avatar appears", groups = {"More Menu"})
    public void verifyAvatarSelection() {
        setGlobalVariables();
        DisneyPlusMoreMenuIOSPageBase MoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase EditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        MobileUtilsExtended utils = new MobileUtilsExtended();
        onboard();
        MoreMenuIOSPageBase.clickAddProfile();

        // Verify choose avatar page UI
        Assert.assertTrue(chooseAvatarPage.isOpened(), "XMOBQA-62628 - Choose Avatar page was not opened");
        sa.assertTrue(chooseAvatarPage.isSkipButtonPresent(), "XMOBQA-62628 - skip button not present on Choose Avatar page");
        sa.assertTrue(chooseAvatarPage.getBackArrow().isPresent(), "XMOBQA-62628 - back button not present on Choose Avatar page");

        //Choose avatar
        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        BufferedImage selectedAvatar = utils.getElementImage(avatars[0]);
        avatars[0].click();

        //Verify that selected avatar appears on Add profile page
        Assert.assertTrue(addProfile.isAddProfilePageOpened(), "XMOBQA-62630 - User was not taken to the 'Add Profiles' page as expected");
        BufferedImage addProfileAvatar = utils.getElementImage(addProfile.getAddProfileAvatar());
        selectedAvatar = utils.getScaledImage(selectedAvatar, addProfileAvatar.getWidth(), addProfileAvatar.getHeight());

        sa.assertTrue(utils.areImagesTheSame(addProfileAvatar, selectedAvatar, 10),
                "XMOBQA-62630 - Avatar Selected was either not displayed or was altered beyond the accepted margin of error");
        //Finish creating profile
        if (disneyAccount.get().getProfileLang().equalsIgnoreCase("en")) {
            addProfile.createProfile(SECONDARY_PROFILE, DateHelper.Month.OCTOBER, "23", "1923");
        }
        sa.assertTrue(EditProfileIOSPageBase.isServiceEnrollmentAccessFullCatalogPagePresent(), "Not on serviceEnrollmentAccessFullCatalog page");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        addProfile.clickSecondaryButtonByCoordinates();
        //Verify that selected avatar appears on More menu page
        BufferedImage moreMenuAvatar = utils.getElementImage(MoreMenuIOSPageBase.getProfileAvatar(SECONDARY_PROFILE));
        BufferedImage selectedAvatarCopy = utils.getScaledImage(utils.cloneBufferedImage(selectedAvatar), moreMenuAvatar.getWidth(), moreMenuAvatar.getHeight());

        LOGGER.info("Comparing selected avatar to 'More Menu' display...");
        sa.assertTrue(utils.areImagesTheSame(selectedAvatarCopy, moreMenuAvatar, 10),
                "XMOBQA-62630 - Avatar displayed in the More Menu was either not displayed or was altered beyond the accepted margin of error");
        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62638"})
    @Test(description = "Verify: Edit Profile User can change Avatar", groups = {"More Menu"})
    public void verifyEditProfileUserCanChangeAvatar() {
        setGlobalVariables();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = new DisneyPlusChooseAvatarIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        ExtendedWebElement[] avatars;
        MobileUtilsExtended utils = new MobileUtilsExtended();

        setAppToHomeScreen(disneyAccount.get());
        disneyPlusMoreMenuIOSPageBase.clickMoreTab();
        BufferedImage moreMenuAvatar = utils.getElementImage(disneyPlusMoreMenuIOSPageBase.getProfileAvatar(TEST));

        disneyPlusMoreMenuIOSPageBase.clickEditProfilesBtn();
        disneyPlusEditProfileIOSPageBase.clickEditModeProfile(TEST);
        disneyPlusEditProfileIOSPageBase.getAddProfileAvatar().click();
        chooseAvatarPage.isOpened();
        chooseAvatarPage.verifyChooseAvatarPage();
        avatars = disneyPlusEditProfileIOSPageBase.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        avatars[3].click();
        BufferedImage addProfileAvatar = utils.getElementImage(disneyPlusEditProfileIOSPageBase.getAddProfileAvatar());
        BufferedImage moreMenuAvatarCopy = utils.getScaledImage(moreMenuAvatar, addProfileAvatar.getWidth(), addProfileAvatar.getHeight());

        LOGGER.info("Comparing selected avatar to 'Edit Profiles' display...");
        sa.assertFalse(utils.areImagesTheSame(moreMenuAvatarCopy, addProfileAvatar, 10),
                "XMOBQA-62630 - Updated Avatar displayed in the Edit Profiles display was either not displayed or was altered beyond the accepted margin of error");
        disneyPlusEditProfileIOSPageBase.clickSaveProfileButton();

        sa.assertAll();
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62634"})
    @Test(description = "Verify: User cannot select the same avatar for multiple profiles", groups = {"More Menu"})
    public void verifyUserCanNotSelectTheSameAvatarForMultipleProfiles() {
        setGlobalVariables();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        disneyAccountApi.get().addProfile(disneyAccount.get(), SECONDARY_PROFILE, ADULT_DOB, disneyAccount.get().getProfileLang(), THE_CHILD, false, true);
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
}