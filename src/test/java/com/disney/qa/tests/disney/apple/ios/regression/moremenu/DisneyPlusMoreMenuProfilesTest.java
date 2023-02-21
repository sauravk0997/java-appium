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
    private void onboard() {
        setAppToHomeScreen(disneyAccount.get());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62628", "XMOBQA-62630", "XMOBQA-62634", "XMOBQA-62638"})
    @Test(description = "Verify Avatar Selection", groups = {"More Menu"})
    public void verifyAvatarSelection() {
        setGlobalVariables();
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusEditProfileIOSPageBase disneyPlusEditProfileIOSPageBase = new DisneyPlusEditProfileIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfile = new DisneyPlusAddProfileIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        MobileUtilsExtended utils = new MobileUtilsExtended();
        onboard();
        disneyPlusMoreMenuIOSPageBase.clickAddProfile();

        Assert.assertTrue(addProfile.isChooseAvatarPageOpen(),
                "XMOBQA-62628 - Choose Avatar page was not opened");

        ExtendedWebElement[] avatars = addProfile.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        BufferedImage selectedAvatar = utils.getElementImage(avatars[0]);
        avatars[0].click();

        Assert.assertTrue(addProfile.isAddProfilePageOpened(),
                "XMOBQA-62630 - User was not taken to the 'Add Profiles' page as expected after skipping avatar selection");

        BufferedImage addProfileAvatar = utils.getElementImage(addProfile.getAddProfileAvatar());
        selectedAvatar = utils.getScaledImage(selectedAvatar, addProfileAvatar.getWidth(), addProfileAvatar.getHeight());

        LOGGER.info("Comparing selected avatar to 'Add Profile' display...");
        sa.assertTrue(utils.areImagesTheSame(addProfileAvatar, selectedAvatar, 10),
                "XMOBQA-62630 - Avatar Selected was either not displayed or was altered beyond the accepted margin of error");
        if(disneyAccount.get().getProfileLang().equalsIgnoreCase("en")) {
            addProfile.enterProfileName(SECONDARY_PROFILE);
            addProfile.enterDOB(DateHelper.Month.OCTOBER, "23", "1923");
            addProfile.chooseGender();
            addProfile.clickSaveBtn();
        }
        sa.assertTrue(disneyPlusEditProfileIOSPageBase.isServiceEnrollmentAccessFullCatalogPagePresent(), "Not on serviceEnrollmentAccessFullCatalog page");
        LOGGER.info("Selecting 'Not Now' on 'setting content rating / access to full catalog' page...");
        addProfile.clickSecondaryButtonByCoordinates();

        BufferedImage moreMenuAvatar = utils.getElementImage(disneyPlusMoreMenuIOSPageBase.getProfileAvatar(SECONDARY_PROFILE));
        BufferedImage selectedAvatarCopy = utils.getScaledImage(utils.cloneBufferedImage(selectedAvatar), moreMenuAvatar.getWidth(), moreMenuAvatar.getHeight());

        LOGGER.info("Comparing selected avatar to 'More Menu' display...");
        sa.assertTrue(utils.areImagesTheSame(selectedAvatarCopy, moreMenuAvatar, 10),
                "XMOBQA-62630 - Avatar displayed in the More Menu was either not displayed or was altered beyond the accepted margin of error");

        disneyPlusMoreMenuIOSPageBase.clickEditProfilesBtn();
        moreMenuAvatar = utils.getElementImage(disneyPlusMoreMenuIOSPageBase.getProfileAvatar(SECONDARY_PROFILE));
        selectedAvatarCopy = utils.getScaledImage(selectedAvatarCopy, moreMenuAvatar.getWidth(), moreMenuAvatar.getHeight());

        LOGGER.info("Comparing selected avatar to 'Edit Profiles' display...");
        sa.assertTrue(utils.areImagesTheSame(selectedAvatarCopy, moreMenuAvatar, 10),
                "XMOBQA-62630 - Updated Avatar displayed in the Edit Profiles display was either not displayed or was altered beyond the accepted margin of error");

        disneyPlusEditProfileIOSPageBase.clickEditModeProfile(SECONDARY_PROFILE);
        disneyPlusEditProfileIOSPageBase.getAddProfileAvatar().click();

        avatars = disneyPlusEditProfileIOSPageBase.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        for (ExtendedWebElement avatar : avatars) {
            LOGGER.info("Verifying that avatar with label '{}' does not have the same avatar as the previously selected one...", avatar.getText());
            BufferedImage remainingAvatar = utils.getScaledImage(utils.getElementImage(avatar), selectedAvatar.getWidth(), selectedAvatar.getHeight());
            sa.assertFalse(utils.areImagesTheSame(selectedAvatar, remainingAvatar, 0),
                    "XMOBQA-62634 - The previously selected Avatar was available for selection unexpectedly");
        }

        selectedAvatar = utils.getScaledImage(utils.getElementImage(avatars[0]), addProfileAvatar.getWidth(), addProfileAvatar.getHeight());
        disneyPlusEditProfileIOSPageBase.clickAny(avatars[0]);
        addProfileAvatar = utils.getElementImage(disneyPlusEditProfileIOSPageBase.getAddProfileAvatar());
        selectedAvatar = utils.getScaledImage(selectedAvatar, addProfileAvatar.getWidth(), addProfileAvatar.getHeight());

        LOGGER.info("Comparing newly selected avatar to 'Add Profiles' display...");
        sa.assertTrue(utils.areImagesTheSame(addProfileAvatar, selectedAvatar, 10),
                "XMOBQA-62638 - Newly selected avatar was either not displayed or was modified beyond the expected margin of error");

        disneyPlusEditProfileIOSPageBase.clickDoneBtn();
        disneyPlusApplePageBase.clickMoreTab();
        /*
         * TO-DO: IOS-3186: delete previous line and restore next line when bug is fixed
         * navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        */
        moreMenuAvatar = utils.getElementImage(disneyPlusMoreMenuIOSPageBase.getProfileAvatar(SECONDARY_PROFILE));
        selectedAvatarCopy = utils.getScaledImage(utils.cloneBufferedImage(selectedAvatar), moreMenuAvatar.getWidth(), moreMenuAvatar.getHeight());

        LOGGER.info("Comparing newly selected avatar to 'Who's Watching' display...");
        sa.assertTrue(utils.areImagesTheSame(selectedAvatarCopy, moreMenuAvatar, 10),
                "XMOBQA-62638 - Updated Avatar displayed in the 'Who's Watching' display was either not displayed or was altered beyond the accepted margin of error");

        disneyPlusWhoseWatchingIOSPageBase.clickProfile(disneyAccount.get().getFirstName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuAvatar = utils.getElementImage(disneyPlusMoreMenuIOSPageBase.getProfileAvatar(SECONDARY_PROFILE));
        selectedAvatarCopy = utils.getScaledImage(utils.cloneBufferedImage(selectedAvatar), moreMenuAvatar.getWidth(), moreMenuAvatar.getHeight());

        LOGGER.info("Comparing newly selected avatar to 'More Menu' display...");
        sa.assertTrue(utils.areImagesTheSame(selectedAvatarCopy, moreMenuAvatar, 10),
                "XMOBQA-62638 - Updated Avatar displayed in the More Menu display was either not displayed or was altered beyond the accepted margin of error");

        sa.assertAll();
    }
}
