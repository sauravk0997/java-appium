package com.disney.qa.tests.disney.apple.tvos.regression.settings;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.CreateUnifiedAccountProfileRequest;
import com.disney.qa.api.offer.pojos.Partner;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileBannerIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEditGenderIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.testng.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.ONE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.PROFILE;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVProfilesTest extends DisneyPlusAppleTVBaseTest {
    private static final String PROFILE_NAME = "Test";
    private static final String whoIsWatchingAssertMessage = "Who is watching page is not open after selecting Profile Name from global nav menu";
    private static final String globalNavMenuAssertMessage = "Global Nav menu is present";
    private static final String ENTER_YOUR_PASSWORD_HEADER_NOT_DISPLAYED = "Enter your password header was not found";
    private static final String ENTER_PROFILE_NAME_TITLE_NOT_DISPLAYED = "Enter Profile Name title is not present";
    private static final String ENTER_YOUR_BIRTHDATE_TITLE_NOT_DISPLAYED = "Enter Your Birthdate title is not present";
    private static final String SELECT_GENDER_TITLE_NOT_DISPLAYED = "Select Gender title is not present";
    private static final String PROFILE_ICON_CELL_NOT_DISPLAYED = "Profile icon cell wasn't displayed for";
    private static final String ADD_PROFILE_PIN_SCREEN_NOT_DISPLAYED =
            "'Want to add a Profile PIN?' screen is not visible";
    private static final String ADD_PROFILE_LABEL = "ADD PROFILE";

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

        sa.assertTrue(chooseAvatarPage.isOpened(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
        disneyPlusAppleTVHomePage.clickMenuTimes(1, 1);
        sa.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
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
        sa.assertTrue(disneyPlusAppleTVEditProfilePage.isEditModeProfileIconPresent(PROFILE_NAME),
                "Edit mode profile icon is not present");
        //Accessibility ID of `Done` button is same as of `edit profile` button.
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        Assert.assertTrue(disneyPlusAppleTVWhoIsWatchingPage.isOpened(),
                "Who is watching page is not open after clicking Done on edit profile page");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-123304"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyAddNewProfileFromHomepageMoreMenu() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        navigateToAddProfileReviewPageFromHomePage(SECONDARY_PROFILE, Person.ADULT);
        addProfilePage.moveDown(3, 1);
        addProfilePage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isElementPresent(),
                SELECT_GENDER_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        addProfilePage.clickSaveProfileButton();
        Assert.assertTrue(addProfilePage.getSecondaryButton().isElementPresent(),
                ADD_PROFILE_PIN_SCREEN_NOT_DISPLAYED);
        addProfilePage.getSecondaryButton().click();

        //Validate profile was created
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        Assert.assertTrue(whoIsWatchingPage.isProfileIconPresent(SECONDARY_PROFILE),
                PROFILE_ICON_CELL_NOT_DISPLAYED + SECONDARY_PROFILE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67258"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.SMOKE, US})
    public void verifyKidsProfileCuratedContent() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        setAccount(getUnifiedAccount());
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        logIn(getUnifiedAccount(), KIDS_PROFILE);
        Assert.assertTrue(homePage.isKidThemeBackgroudUIDisplayed(),
                "UI on home page is not in kid mode theme");
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertFalse(detailsPage.isOpened(), "Adult content detail page displayed for kid profile");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-97901"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyOTPValidationForProfileCreation() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage forgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVChangePasswordPage changePasswordPage = new DisneyPlusAppleTVChangePasswordPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccountForOTP(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM).setProfileRestricted(true)));
        logIn(getUnifiedAccount());

        //Try to add a new profile
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.waitUntilElementIsFocused(whoIsWatchingPage.getUnlockedProfileCell(), FIVE_SEC_TIMEOUT);
        whoIsWatchingPage.clickAddProfile();

        //Validate transition to Enter Your Password screen and select 'Forgot Password' CTA
        Assert.assertTrue(passwordPage.isHeaderTextDisplayed(), ENTER_PASSWORD_PAGE_NOT_DISPLAYED);
        passwordPage.getForgotPasswordButton().click();

        //Fill OTP and create new password
        Assert.assertTrue(forgotPasswordPage.getCheckEmailTitle().isElementPresent(),
                "Forgot Password screen wasn't visible");
        forgotPasswordPage.enterOtpOnModal(getOTPFromApi(getUnifiedAccount()));
        Assert.assertTrue(changePasswordPage.isOpened(),
                "Create New Password screen wasn't opened");
        changePasswordPage.clickPasswordField();
        changePasswordPage.enterPassword("Abc12!");
        changePasswordPage.moveToContinueOrDoneBtnKeyboardEntry();
        changePasswordPage.clickSelect();
        changePasswordPage.clickSave();

        Assert.assertTrue(chooseAvatarPage.isOpened(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
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
                FIVE_SEC_TIMEOUT);
        whoIsWatchingPage.moveRight(1, 1);
        whoIsWatchingPage.clickSelect();
        sa.assertTrue(passwordPage.isHeaderTextDisplayed(),
                ENTER_YOUR_PASSWORD_HEADER_NOT_DISPLAYED);
        sa.assertTrue(passwordPage.getAuthEnterPasswordProfileBody().isPresent(),
                "Add password profile description was not found");
        sa.assertTrue(passwordPage.getPasswordFieldText().equals(passwordPage.getAuthEnterPasswordFieldHintText()),
                "Field Ghost text 'Password' was not found");
        sa.assertTrue(passwordPage.isShowHidePasswordEyeIconPresent(),
                "Show hide password eye icon was not found");
        sa.assertTrue(passwordPage.getAuthEnterPasswordForgotPassword().isPresent(),
                "Forgot Password button text was not found");
        sa.assertTrue(passwordPage.isContinueButtonPresent(), CONTINUE_BTN_NOT_DISPLAYED);
        passwordPage.enterPasswordToCompleteAuth(getUnifiedAccount().getUserPass());
        sa.assertTrue(chooseAvatarPage.isOpened(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
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
        whoIsWatchingPage.waitUntilElementIsFocused(whoIsWatchingPage.getUnlockedProfileCell(), FIVE_SEC_TIMEOUT);
        whoIsWatchingPage.moveRight(1, 1);
        whoIsWatchingPage.clickSelect();
        Assert.assertTrue(passwordPage.isHeaderTextDisplayed(),
                ENTER_YOUR_PASSWORD_HEADER_NOT_DISPLAYED);
        passwordPage.enterPasswordToCompleteAuth(getUnifiedAccount().getUserPass());
        Assert.assertTrue(chooseAvatarPage.isOpened(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
        chooseAvatarPage.moveUp(1, 1);
        chooseAvatarPage.clickSelect();

        //Go through profile name input
        Assert.assertTrue(addProfilePage.getEnterProfileNameTitle().isPresent(),
                ENTER_PROFILE_NAME_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        addProfilePage.enterProfileName(SECONDARY_PROFILE);
        addProfilePage.keyPressTimes(addProfilePage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        addProfilePage.clickSelect();
        addProfilePage.getEnterProfileNameContinueButton().click();

        //Go through birthdate input
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isPresent(),
                ENTER_YOUR_BIRTHDATE_TITLE_NOT_DISPLAYED);
        addProfilePage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(true), Person.ADULT.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();

        //Go through Add Profile page
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        addProfilePage.moveDown(3, 1);
        addProfilePage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isPresent(),
                SELECT_GENDER_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        addProfilePage.clickSaveProfileButton();
        Assert.assertTrue(addProfilePage.getSecondaryButton().isPresent(),
                ADD_PROFILE_PIN_SCREEN_NOT_DISPLAYED);
        addProfilePage.getSecondaryButton().click();

        //Validate profile was created
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        Assert.assertTrue(whoIsWatchingPage.isProfileIconPresent(SECONDARY_PROFILE),
                PROFILE_ICON_CELL_NOT_DISPLAYED + SECONDARY_PROFILE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68211"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyAddNewProfileFromWhoIsWatchingPage() {
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(PROFILE_NAME_SECONDARY)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        logInWithoutHomeCheck(getUnifiedAccount());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.waitUntilElementIsFocused(whoIsWatchingPage.getUnlockedProfileCell(), FIVE_SEC_TIMEOUT);
        whoIsWatchingPage.clickAddProfile();

        //Go through Choose Avatar screen
        Assert.assertTrue(chooseAvatarPage.isOpened(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
        chooseAvatarPage.moveUp(1, 1);
        chooseAvatarPage.clickSelect();

        //Go through profile name input
        Assert.assertTrue(addProfilePage.getEnterProfileNameTitle().isElementPresent(),
                ENTER_PROFILE_NAME_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        addProfilePage.enterProfileName(TERTIARY_PROFILE);
        addProfilePage.keyPressTimes(addProfilePage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        addProfilePage.clickSelect();
        addProfilePage.getEnterProfileNameContinueButton().click();

        //Go through birthdate input
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isElementPresent(),
                ENTER_YOUR_BIRTHDATE_TITLE_NOT_DISPLAYED);
        addProfilePage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(true), Person.ADULT.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();

        //Go through Add Profile page
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        addProfilePage.moveDown(3, 1);
        addProfilePage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isElementPresent(),
                SELECT_GENDER_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        addProfilePage.clickSaveProfileButton();
        Assert.assertTrue(addProfilePage.getSecondaryButton().isElementPresent(),
                ADD_PROFILE_PIN_SCREEN_NOT_DISPLAYED);
        addProfilePage.getSecondaryButton().click();

        //Validate profile was created
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        Assert.assertTrue(whoIsWatchingPage.isProfileIconPresent(TERTIARY_PROFILE),
                PROFILE_ICON_CELL_NOT_DISPLAYED + TERTIARY_PROFILE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-99289"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyLockedProfilePINEntry() {
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVPinPage pinPage = new DisneyPlusAppleTVPinPage(getDriver());

        String incorrectPIN = "1112";

        try {
            getUnifiedAccountApi().updateProfilePin(getUnifiedAccount(),
                    getUnifiedAccount().getProfileId(DEFAULT_PROFILE),
                    PROFILE_PIN);
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }

        logInWithoutHomeCheck(getUnifiedAccount());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatchingPage.clickPinProtectedProfile(DEFAULT_PROFILE);

        pinPage.enterProfilePin(incorrectPIN);
        Assert.assertTrue(pinPage.getProfilePinInvalidErrorMessage().isPresent(),
                "Incorrect Profile pin error message was not displayed");

        pinPage.enterProfilePin(PROFILE_PIN);
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-120608"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.HULU, US})
    public void verifyJuniorModeNoHuluContent() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccount());
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        logIn(getUnifiedAccount(), KIDS_PROFILE);

        // Verify carousel and hulu titles in home
        Assert.assertTrue(homePage.isKidsHomePageOpen(), HOME_PAGE_NOT_DISPLAYED);
        homePage.getKidsCarouselsTV().forEach(element -> sa.assertFalse(element.getText().contains(HULU),
                String.format("%s contains %s", element.getText(), HULU)));
        Assert.assertFalse(homePage.getBrandCell(HULU).isPresent(ONE_SEC_TIMEOUT), "Hulu tile was found on Kids home");
        Assert.assertTrue(homePage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT),
                "Hulu branding was found on Kids' Home page");

        // Validate search
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        sa.assertTrue(searchPage.getTypeCellLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT),
                "Hulu branding was found on Kids' Search page");

        // Verify hulu content search
        searchPage.typeInSearchField(ONLY_MURDERS_IN_THE_BUILDING);
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(ONLY_MURDERS_IN_THE_BUILDING)
                , ONLY_MURDERS_IN_THE_BUILDING + " 'no results found' message not found");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112716"})
    @Test(groups = {TestGroup.PROFILES, US})
    public void verifyAddProfileU18DefaultsToTV14() {
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());
        String defaultRatingExpected = "TV-14";
        String ratingExpected = "TV-MA";

        logIn(getUnifiedAccount());

        //Go through add profile screen
        navigateToAddProfileReviewPageFromHomePage(SECONDARY_PROFILE, Person.U18);

        addProfilePage.clickSaveProfileButton();

        Assert.assertTrue(addProfilePage.verifyHeadlineHeaderText(),
                "Access to full catalog screen was not present");
        Assert.assertTrue(addProfilePage.isMaturityRatingNotNowInfoDisplayed(defaultRatingExpected),
                "The content rating was not TV-14 by default");
        Assert.assertTrue(addProfilePage.isUpdateMaturityRatingActionDisplayed(ratingExpected),
                "Prompt to set content rating was not TV-MA");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-123034"})
    @Test(groups = {TestGroup.PROFILES, LATAM_ANZ})
    public void verifyDOBCollectionForLatamOrANZ() {
        DisneyPlusAppleTVUpdateProfilePage updateProfilePage = new DisneyPlusAppleTVUpdateProfilePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = new DisneyPlusAddProfileBannerIOSPageBase(getDriver());
        DisneyPlusAppleTVChooseAvatarPage appleTVChooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());
        DisneyPlusAppleTVEdnaDOBCollectionPage ednaDOBCollectionPage =
                new DisneyPlusAppleTVEdnaDOBCollectionPage(getDriver());

        // Create account with no DOB and GI
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())
                        .setDateOfBirth(null).setGender(null)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());

        logInWithoutHomeCheck(getUnifiedAccount());

        //Go through birthdate screen
        Assert.assertTrue(ednaDOBCollectionPage.isOpened(), EDNA_DOB_COLLECTION_PAGE_NOT_DISPLAYED);
        ednaDOBCollectionPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(true), Person.ADULT.getYear());
        ednaDOBCollectionPage.getSaveAndContinueButton().click();

        //Go through update profile screen
        Assert.assertTrue(updateProfilePage.isOpened(), UPDATE_PROFILE_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(updateProfilePage.getUpdateProfileTitle().isPresent(), "Update Profile Title is not displayed");
        commonPage.moveDown(2, 1);
        commonPage.clickSelect();
        Assert.assertTrue(updateProfilePage.getStaticTextByLabelContains(updateProfilePage.getGenderPlaceholder()).isPresent(),
                "Gender placeholder is not present");
        commonPage.clickSelect();
        commonPage.moveRight(1, 1);
        updateProfilePage.getSaveProfileBtn().click();

        //Go through add profile for U18 profile
        Assert.assertTrue(addProfileBanner.isProfileHeaderPresent(), "Profile header is not present");
        addProfileBanner.getTypeButtonByLabel(ADD_PROFILE_LABEL).click();
        // Avatar selection and complete U18 profile validations
        Assert.assertTrue(appleTVChooseAvatarPage.getChooseAvatarTitle().isPresent(), "Choose avatar screen was not present");
        commonPage.clickSelect();
        Assert.assertTrue(addProfilePage.getEnterProfileNameTitle().isElementPresent(),
                ENTER_PROFILE_NAME_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        addProfilePage.enterProfileName(JUNIOR_PROFILE);
        addProfilePage.keyPressTimes(addProfilePage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        addProfilePage.clickSelect();
        addProfilePage.getEnterProfileNameContinueButton().click();
        // Add U18 DOB
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isPresent(),
                "Enter your birthdate title is not present for u18 profile");
        addProfilePage.enterDOB(Person.U18.getMonth(), Person.U18.getDay(true), Person.U18.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(isGenderOptionDisabled(), "Gender was enabled for a Junior Profile");
        // Enable Junior Mode and validate DOB was not enabled
        addProfilePage.getKidsProfileToggleCell().click();
        commonPage.moveUp(1, 1);
        Assert.assertTrue(addProfilePage.isFocused(addProfilePage.getChangeAvatarSelectorCell()),
                "DOB was enabled after Junior Mode selection");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116697"})
    @Test(groups = {TestGroup.PROFILES, LATAM_ANZ})
    public void verifyGenderFieldForLatamOrANZJuniorMode() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        logInWithoutHomeCheck(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        navigateToAddProfileReviewPageFromHomePage(JUNIOR_PROFILE, Person.ADULT);
        commonPage.moveDown(3, 1);
        commonPage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isPresent(),
                SELECT_GENDER_TITLE_NOT_DISPLAYED);
        commonPage.clickSelect();
        commonPage.moveDown(1, 1);
        Assert.assertTrue(addProfilePage.isFocused(addProfilePage.getKidsProfileToggleCell()),
                "Junior mode is not focused");
        commonPage.clickSelect();
        commonPage.moveUp(1, 1);
        Assert.assertTrue(addProfilePage.isFocused(addProfilePage.getChangeAvatarSelectorCell()),
                "Gender was enabled after Junior Mode selection");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116695"})
    @Test(groups = {TestGroup.PROFILES, LATAM_ANZ})
    public void verifyGenderFieldForLatamOrANZAddProfileFlow() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());
        DisneyPlusEditGenderIOSPageBase editGenderIOSPageBase = initPage(DisneyPlusEditGenderIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        logInWithoutHomeCheck(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        navigateToAddProfileReviewPageFromHomePage(SECONDARY_PROFILE, Person.ADULT);
        //Go through add profile screen
        commonPage.moveDown(3, 1);
        commonPage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isPresent(),
                SELECT_GENDER_TITLE_NOT_DISPLAYED);
        validateGenderOptions();
        commonPage.moveDown(3, 1);
        Assert.assertTrue(addProfilePage.isFocused(addProfilePage.getTypeCellLabelContains(
                        editGenderIOSPageBase.getGenderLabel(DisneyPlusEditGenderIOSPageBase.GenderOption.GENDER_PREFERNOTTOSAY))),
                "Expected gender was not selected");

        commonPage.clickSelect();
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        addProfilePage.clickSaveProfileButton();
        Assert.assertTrue(addProfilePage.getSecondaryButton().isElementPresent(),
                ADD_PROFILE_PIN_SCREEN_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116674"})
    @Test(groups = {TestGroup.PROFILES, LATAM_ANZ})
    public void verifyDOBFieldForLatamOrANZAddProfileFlow() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        logInWithoutHomeCheck(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        navigateToAddProfileReviewPageFromHomePage(SECONDARY_PROFILE, Person.ADULT);

        // We dont have any identifier currently for DOB field hence verifying with generic locator and below logic
        //if DOB and Gender field is present on page then after moving down 4 time Kids profile toggle cell should be
        // focused
        commonPage.moveDown(2, 1);
        Assert.assertTrue(addProfilePage.isFocused(addProfilePage.getBirthdateSelectorCell()),
                "Date of birth field is not present on Add profile review screen");
        commonPage.moveDown(2, 1);
        Assert.assertTrue(addProfilePage.isFocused(addProfilePage.getKidsProfileToggleCell()),
                "Kids Profile toggle cell not focused");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-116696"})
    @Test(groups = {TestGroup.PROFILES, LATAM_ANZ})
    public void verifyGenderJuniorModeForLatamOrANZEditProfileFlow() {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVEditProfilePage editProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());

        // Add multiple profiles and include one with junior mode enabled
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .language(getLocalizationUtils().getUserLanguage())
                .isStarOnboarded(true)
                .build());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        logInWithoutHomeCheck(getUnifiedAccount());

        Assert.assertTrue(whoseWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoseWatchingPage.clickEditProfile();

        Assert.assertTrue(editProfilePage.isOpened(), EDIT_PROFILE_PAGE_NOT_DISPLAYED);
        commonPage.moveRightUntilElementIsFocused(editProfilePage.getTypeCellLabelContains(KIDS_PROFILE), 3);
        commonPage.clickSelect();
        Assert.assertTrue(editProfilePage.isEditTitleDisplayed(), EDIT_PROFILE_PAGE_NOT_DISPLAYED);
        Assert.assertFalse(editProfilePage.getGenderLabel().isPresent(THREE_SEC_TIMEOUT),
                "Gender field was present in Edit screen for a Junior Mode Profile");
    }

    // Bug related to AU country TVOS-7014
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-123033"})
    @Test(groups = {TestGroup.PROFILES, LATAM_ANZ})
    public void verifyGICollectionForLatamOrANZ() {
        DisneyPlusAppleTVUpdateProfilePage updateProfilePage = new DisneyPlusAppleTVUpdateProfilePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAddProfileBannerIOSPageBase addProfileBanner = new DisneyPlusAddProfileBannerIOSPageBase(getDriver());
        DisneyPlusAppleTVChooseAvatarPage appleTVChooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());

        // Create account with no GI
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())
                        .setGender(null)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        logInWithoutHomeCheck(getUnifiedAccount());

        //Go through update profile screen
        Assert.assertTrue(updateProfilePage.isOpened(), UPDATE_PROFILE_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(updateProfilePage.getUpdateProfileTitle().isPresent(), "Update Profile Title is not displayed");
        commonPage.moveDown(2, 1);
        commonPage.clickSelect();
        Assert.assertTrue(updateProfilePage.getStaticTextByLabelContains(updateProfilePage.getGenderPlaceholder()).isPresent(),
                "Gender placeholder is not present");
        commonPage.clickSelect();
        commonPage.moveRight(1, 1);
        updateProfilePage.getSaveProfileBtn().click();

        //Go through add profile
        Assert.assertTrue(addProfileBanner.isProfileHeaderPresent(), "Profile header is not present");
        addProfileBanner.getTypeButtonByLabel(ADD_PROFILE_LABEL).click();

        Assert.assertTrue(appleTVChooseAvatarPage.getChooseAvatarTitle().isPresent(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
        commonPage.clickSelect();
        Assert.assertTrue(addProfilePage.getEnterProfileNameTitle().isElementPresent(),
                ENTER_PROFILE_NAME_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        // Adult person step
        addProfilePage.enterProfileName(JUNIOR_PROFILE);
        addProfilePage.keyPressTimes(addProfilePage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        addProfilePage.clickSelect();
        addProfilePage.getEnterProfileNameContinueButton().click();
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isPresent(),
                ENTER_YOUR_BIRTHDATE_TITLE_NOT_DISPLAYED);
        addProfilePage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(true), Person.ADULT.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        commonPage.moveDown(3, 1);
        commonPage.clickSelect();
        Assert.assertTrue(addProfilePage.getSelectGenderTitle().isPresent(),
                SELECT_GENDER_TITLE_NOT_DISPLAYED);
        validateGenderOptions();
        // Go back to validate a U18 by navigating back and editing DOB
        commonPage.clickBack();
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
        commonPage.clickBack();
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isPresent(),
                "Enter your birthdate title is not present for u18 profile");
        commonPage.clickBack();
        addProfilePage.getEnterProfileNameContinueButton().click();
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isPresent(),
                "Enter your birthdate title is not present for u18 profile");
        addProfilePage.enterDOB(Person.U18.getMonth(), Person.U18.getDay(true), Person.U18.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);

        Assert.assertTrue(isGenderOptionDisabled(), "Gender was enabled for a U18 Profile");
    }

    public void validateGenderOptions() {
        DisneyPlusEditGenderIOSPageBase editGenderIOSPageBase = initPage(DisneyPlusEditGenderIOSPageBase.class);
        String other = "Other";
        List<DisneyPlusEditGenderIOSPageBase.GenderOption> genderList =
                Stream.of(DisneyPlusEditGenderIOSPageBase.GenderOption.values()).collect(Collectors.toList());
        for (DisneyPlusEditGenderIOSPageBase.GenderOption genderOption : genderList) {
            Assert.assertTrue(editGenderIOSPageBase.getTypeCellLabelContains(
                            editGenderIOSPageBase.getGenderLabel(genderOption)).isPresent(),
                    "Gender " + genderOption + " is not present" );
            // Validate that the third Option is displayed as Other for LATAM
            if (!Arrays.asList(NZ, AU).contains(getLocalizationUtils().getLocale())
                    && genderOption.equals(DisneyPlusEditGenderIOSPageBase.GenderOption.GENDER_NOBINARY)) {
                Assert.assertTrue(editGenderIOSPageBase.getTypeCellLabelContains(other).isPresent(),
                        "Gender option 'Other' is not present for LATAM");
            }
        }
    }

    public boolean isGenderOptionDisabled() {
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());
        addProfilePage.moveDown(3, 1);
        return addProfilePage.isFocused(addProfilePage.getKidsProfileToggleCell());
    }

    private void navigateToAddProfileReviewPageFromHomePage(String profileName, Person dob){
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage chooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        DisneyPlusAppleTVAddProfilePage addProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoseWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavWithClickingMenu();
        homePage.navigateToOneGlobalNavMenu(PROFILE.getText());
        homePage.clickSelect();

        Assert.assertTrue(whoseWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoseWatchingPage.clickAddProfile();

        Assert.assertTrue(chooseAvatarPage.getChooseAvatarTitle().isPresent(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
        commonPage.clickSelect();
        Assert.assertTrue(addProfilePage.getEnterProfileNameTitle().isElementPresent(),
                ENTER_PROFILE_NAME_TITLE_NOT_DISPLAYED);
        addProfilePage.clickSelect();
        addProfilePage.enterProfileName(profileName);
        addProfilePage.keyPressTimes(addProfilePage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        addProfilePage.clickSelect();
        addProfilePage.getEnterProfileNameContinueButton().click();
        Assert.assertTrue(addProfilePage.getEnterYourBirthdateTitle().isPresent(),
                ENTER_YOUR_BIRTHDATE_TITLE_NOT_DISPLAYED);
        addProfilePage.enterDOB(dob.getMonth(), dob.getDay(true), dob.getYear());
        addProfilePage.getEnterDateOfBirthContinueButton().click();
        Assert.assertTrue(addProfilePage.isAddProfileHeaderPresent(), ADD_PROFILE_PAGE_NOT_DISPLAYED);
    }
}
