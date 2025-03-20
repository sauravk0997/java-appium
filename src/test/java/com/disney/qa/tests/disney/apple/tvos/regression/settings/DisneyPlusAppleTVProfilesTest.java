package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.offer.pojos.Partner;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.PROFILE;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVProfilesTest extends DisneyPlusAppleTVBaseTest {
    private static final String PROFILE_NAME = "Test";
    private static final String whoIsWatchingAssertMessage = "Who is watching page is not open after selecting Profile Name from global nav menu";
    private static final String globalNavMenuAssertMessage = "Global Nav menu is present";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90920"})
    @Test(description = "Profiles - Exit from Who's Watching view", groups = {TestGroup.PROFILES, TestGroup.SMOKE, US})
    public void exitFromWhoseWatching() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.openGlobalNavWithClickingMenu();
        homePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        homePage.clickSelect();
        Assert.assertTrue(whoseWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        homePage.clickMenuTimes(1, 1);
        sa.assertTrue(homePage.isOpened(), "Home page is not open after clicking menu on Profile selection page");

        homePage.openGlobalNavWithClickingMenu();
        homePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        homePage.clickSelect();

        Assert.assertTrue(whoseWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        homePage.clickProfileBtn(PROFILE_NAME);
        Assert.assertTrue(homePage.isOpened(), "Home page is not open after selecting a profile");
        sa.assertFalse(homePage.isGlobalNavPresent(), globalNavMenuAssertMessage);
        homePage.moveLeft(2,1); //stop carousel moving
        homePage.isCarouselFocused();
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90924"})
    @Test(description = "Profiles - Exit from Add Profile view", groups = {TestGroup.PROFILES, US})
    public void exitFromAddProfile() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());

        logIn(getUnifiedAccount());

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickAddProfile();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        sa.assertTrue(chooseAvatarPage.isOpened(), "Choose avatar page is not open after clicking add profile");
        disneyPlusAppleTVHomePage.clickMenuTimes(1, 1);
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page is not open after clicking menu on Choose Avatar screen");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-65772"})
    @Test(description = "Profiles - Exit from Select profile to edit view", groups = {TestGroup.PROFILES, US})
    public void exitFromSelectProfileToEdit() {
        SoftAssert sa = new SoftAssert();

        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        logIn(getUnifiedAccount());

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();

        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME));
        disneyPlusAppleTVHomePage.clickMenuTimes(2, 1);
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after clicking menu on edit profile page");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90926"})
    @Test(description = "Profiles - Exit from Edit Profile view", groups = {TestGroup.PROFILES, US})
    public void verifyExitFromEditProfileView() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        logIn(getUnifiedAccount());
        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);

        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME), "Edit mode profile icon is not present");

        disneyPlusAppleTVHomePage.clickMenuTimes(2, 1);
        Assert.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after clicking menu on edit profile page");
        sa.assertFalse(disneyPlusAppleTVHomePage.isGlobalNavPresent(), globalNavMenuAssertMessage);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE_NAME);
        disneyPlusAppleTVHomePage.clickSelect();
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), whoIsWatchingAssertMessage);

        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME), "Edit mode profile icon is not present");
        //Accessibility ID of `Done` button is same as of `edit profile` button.
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), "Who is watching page is not open after clicking Done on edit profile page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-123304"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyAddNewProfileFromHomepageMoreMenu() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());

        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.waitUntilElementIsFocused(whoIsWatchingPage.getUnlockedProfileCell(), FIVE_SEC_TIMEOUT);
        whoIsWatchingPage.moveRight(1, 1);
        whoIsWatchingPage.clickSelect();

        //Go through Choose Avatar screen
        Assert.assertTrue(chooseAvatarPage.isOpened(), "Choose Avatar page was not opened");
        chooseAvatarPage.moveUp(1, 1);
        chooseAvatarPage.clickSelect();

        //Go through profile name input
        Assert.assertTrue(addProfilePage.getEnterProfileNameTitle().isElementPresent(),
                "Enter Profile Name title is not present");
        addProfilePage.clickSelect();
        addProfilePage.enterProfileName(SECONDARY_PROFILE);
        addProfilePage.moveDownUntilElementIsFocused(addProfilePage.getKeyboardDoneButton(), 6);
        addProfilePage.clickSelect();
        addProfilePage.getEnterProfileNameContinueButton().click();

        //Go through birthdate input
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isElementPresent(),
                "Enter Your Birthdate title is not present");
        addProfilePage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(true), Person.ADULT.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();

        //Go through Add Profile page
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        addProfilePage.moveDown(3, 1);
        addProfilePage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isElementPresent(),
                "Select Gender title is not present");
        addProfilePage.clickSelect();
        addProfilePage.clickSaveProfileButton();
        Assert.assertTrue(addProfilePage.getSecondaryButton().isElementPresent(),
                "'Want to add a Profile PIN?' screen is not visible");
        addProfilePage.getSecondaryButton().click();

        //Validate profile was created
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        Assert.assertTrue(whoIsWatchingPage.isProfileIconPresent(SECONDARY_PROFILE),
                "Profile icon cell wasn't displayed for secondary profile");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-97845"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyEnterPasswordScreenUIForProfileCreationLockUserWhileAddProfileFlow() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        SoftAssert sa = new SoftAssert();

        getDefaultCreateUnifiedAccountRequest()
                .setPartner(Partner.DISNEY)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setProfileRestricted(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());
        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()));
        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.waitUntilElementIsFocused(whoIsWatchingPage.getUnlockedProfileCell(),
                DisneyAbstractPage.FIVE_SEC_TIMEOUT);
        whoIsWatchingPage.moveRight(1, 1);
        whoIsWatchingPage.clickSelect();
        sa.assertTrue(passwordPage.isHeaderTextDisplayed(),
                "Enter your password header was not found.");
        sa.assertTrue(passwordPage.getAuthEnterPasswordProfileBody().isPresent(),
                "Add password profile description was not found");
        sa.assertTrue(passwordPage.getPasswordFieldText().equals(passwordPage.getAuthEnterPasswordFieldHintText()),
                "Field Ghost text 'Password' was not found");
        sa.assertTrue(passwordPage.isShowHidePasswordEyeIconPresent(),
                "Show hide password eyc icon was not found");
        sa.assertTrue(passwordPage.getAuthEnterPasswordForgotPassword().isPresent(),
                "Forgot Password button text was not found");
        sa.assertTrue(passwordPage.isContinueButtonPresent(), CONTINUE_BTN_NOT_DISPLAYED);
        passwordPage.enterPasswordToCompleteAuth(getUnifiedAccount().getUserPass());
        sa.assertTrue(chooseAvatarPage.isOpened(), "Choose your avatar screen not open");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-97899"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyAddProfileFlowForProfileCreationLockUser() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());

        getDefaultCreateUnifiedAccountRequest()
                .setPartner(Partner.DISNEY)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setProfileRestricted(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());
        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()));
        logIn(getUnifiedAccount());
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.waitUntilElementIsFocused(whoIsWatchingPage.getUnlockedProfileCell(), DisneyAbstractPage.FIVE_SEC_TIMEOUT);
        whoIsWatchingPage.moveRight(1, 1);
        whoIsWatchingPage.clickSelect();
        Assert.assertTrue(passwordPage.isHeaderTextDisplayed(),
                "Enter your password header was not found.");
        passwordPage.enterPasswordToCompleteAuth(getUnifiedAccount().getUserPass());
        Assert.assertTrue(chooseAvatarPage.isOpened(), "Choose your avatar screen not open");
        chooseAvatarPage.moveUp(1, 1);
        chooseAvatarPage.clickSelect();

        //Go through profile name input
        Assert.assertTrue(addProfilePage.getEnterProfileNameTitle().isPresent(),
                "Enter Profile Name title is not present");
        addProfilePage.clickSelect();
        addProfilePage.enterProfileName(SECONDARY_PROFILE);
        addProfilePage.moveDownUntilElementIsFocused(addProfilePage.getKeyboardDoneButton(), 6);
        addProfilePage.clickSelect();
        addProfilePage.getEnterProfileNameContinueButton().click();

        //Go through birthdate input
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isPresent(),
                "Enter Your Birthdate title is not present");
        addProfilePage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(true), Person.ADULT.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();

        //Go through Add Profile page
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        addProfilePage.moveDown(3, 1);
        addProfilePage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isPresent(),
                "Select Gender title is not present");
        addProfilePage.clickSelect();
        addProfilePage.clickSaveProfileButton();
        Assert.assertTrue(addProfilePage.getSecondaryButton().isPresent(),
                "'Want to add a Profile PIN?' screen is not visible");
        addProfilePage.getSecondaryButton().click();

        //Validate profile was created
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        Assert.assertTrue(whoIsWatchingPage.isProfileIconPresent(SECONDARY_PROFILE),
                "Profile icon cell wasn't displayed for secondary profile");
    }
}
