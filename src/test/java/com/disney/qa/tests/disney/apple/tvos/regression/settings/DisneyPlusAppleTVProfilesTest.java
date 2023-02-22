package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVEditProfilePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWhoIsWatchingPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAppleTVProfilesTest extends DisneyPlusAppleTVBaseTest {
    private static final String PROFILE_NAME = "Test";
    private static final String whoIsWatchingAssertMessage = "Who is watching page is not open after selecting Profile Name from global nav menu";
    private static final String homePageAssertMessage = "Home page is not open after login";
    private static final String globalNavMenuAssertMessage = "Global Nav menu is present";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90920"})
    @Test(description = "Profiles - Exit from Who's Watching view", groups = {"Smoke"})
    public void exitFromWhoseWatching() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getCastedDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        logIn(entitledUser);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVHomePage.clickMenuTimes(1, 1);
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after clicking menu on Profile selection page");
        UniversalUtils.captureAndUpload(getCastedDriver());

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVHomePage.clickProfileBtn(PROFILE_NAME);
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after selecting a profile");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);
        disneyPlusAppleTVHomePage.moveUp(1,1);
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.BANNER_HOVERED.getText());
        aliceDriver.uploadAliceScreenshots();

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90924"})
    @Test(description = "Profiles - Exit from Add Profile view", groups = {"Smoke"})
    public void exitFromAddProfile() {
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        logIn(entitledUser);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickAddProfile();
        UniversalUtils.captureAndUpload(getCastedDriver());

        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isChooseAvatarPageOpen(), "Choose avatar page is not open after clicking add profile");
        disneyPlusAppleTVHomePage.clickMenuTimes(1, 1);
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page is not open after clicking menu on Choose Avatar screen");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);
        UniversalUtils.captureAndUpload(getCastedDriver());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90922"})
    @Test(description = "Profiles - Exit from Select profile to edit view", groups = {"Smoke"})
    public void exitFromSelectProfileToEdit() {
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        logIn(entitledUser);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        UniversalUtils.captureAndUpload(getCastedDriver());

        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME));
        disneyPlusAppleTVHomePage.clickMenuTimes(1, 1);
        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after clicking menu on edit profile page");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90926"})
    @Test(description = "Profiles - Exit from Edit Profile view", groups = {"Smoke"})
    public void exitFromEditProfileView() {
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        logIn(entitledUser);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        UniversalUtils.captureAndUpload(getCastedDriver());

        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME), "Edit mode profile icon is not present");
        disneyPlusAppleTVHomePage.clickMenuTimes(1, 1);
        UniversalUtils.captureAndUpload(getCastedDriver());

        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after clicking menu on edit profile page");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        UniversalUtils.captureAndUpload(getCastedDriver());

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME), "Edit mode profile icon is not present");
        //Accessibility ID of `Done` button is same as of `edit profile` button.
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        UniversalUtils.captureAndUpload(getCastedDriver());
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page is not open after clicking Done on edit profile page");

        sa.assertAll();
    }
}
