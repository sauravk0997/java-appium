package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.offer.pojos.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.account.AccountBlockReasons;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusLoginTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String NO_ERROR_DISPLAYED = "error message was not displayed";
    public static final String COMPLETE_PROFILE_PAGE_NOT_DISPLAYED = "Complete Profile Page is not displayed";
    public static final String HOME_PAGE_NOT_DISPLAYED = "Home page is not displayed";
    public static final String DISNEY_PLUS_LOGO_NOT_DISPLAYED = "Disney+ Logo is not displayed";
    public static final String DOB_PAGE_NOT_DISPLAYED = "DOB Collection Page is not displayed";
    public static final String LOG_OUT_BTN_NOT_DISPLAYED = "Log Out Button is not displayed";
    public static final String MYDISNEY_LOGO_NOT_DISPLAYED = "myDisney Logo is not displayed";
    public static final String BACK_ARROW_NOT_DISPLAYED = "Back Arrow is not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72745"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void testLogInScreen() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        String step1Label = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem
                        (DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_STEPPER_TEXT.getText()),
                Map.of("current_step", "1"));
        String learnMoreBody = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_LEARN_MORE_BODY.getText()), Map.of("link_1", "and more"));

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        softAssert.assertTrue(loginPage.isBackButtonPresent(), BACK_ARROW_NOT_DISPLAYED);
        softAssert.assertTrue(loginPage.isDisneyLogoDisplayed(), DISNEY_PLUS_LOGO_NOT_DISPLAYED);
        softAssert.assertTrue(loginPage.isMyDisneyLogoDisplayed(), MYDISNEY_LOGO_NOT_DISPLAYED);
        softAssert.assertTrue(loginPage.getStaticTextByLabel(step1Label).isPresent(), "STEP 1 text should be displayed");
        softAssert.assertTrue(loginPage.isEnterEmailHeaderDisplayed(), "'Enter your email to continue' text should be displayed");
        softAssert.assertTrue(loginPage.isEmailFieldDisplayed(), "Email field should be present");
        softAssert.assertTrue(loginPage.isEnterEmailBodyDisplayed(), "Log in to Disney+ with your MyDisney account should display or Email Body should display");
        softAssert.assertTrue(loginPage.continueButtonPresent(), "Continue (primary) button should be present");
        softAssert.assertTrue(loginPage.isLearnMoreHeaderDisplayed(), "'Disney+ is part of The Walt Disney Family of Companies' text should be displayed");
        softAssert.assertTrue(loginPage.getStaticTextByLabel(learnMoreBody).isPresent(), "'MyDisney lets you seamlessly log in to services' text should be displayed");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72744"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void testPasswordScreen() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getUnifiedAccount().getEmail());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isBackButtonPresent(), BACK_ARROW_NOT_DISPLAYED);
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isDisneyLogoDisplayed(), DISNEY_PLUS_LOGO_NOT_DISPLAYED);
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isMyDisneyLogoDisplayed(), MYDISNEY_LOGO_NOT_DISPLAYED);
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isStepLabelDisplayed("2"), "stepper should be displayed");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isHeaderTextDisplayed(), "Header text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isPasswordEntryFieldDisplayed(), "Password field should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isCaseSensitiveHintPresent(), "Password hint text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isShowPasswordIconDisplayed(), "Show Password button should be present");
        softAssert.assertTrue(disneyPlusChangeEmailIOSPageBase.isLearnMoreAboutMyDisney(), "Learn more about my disney Link should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isForgotPasswordButtonPresent(), "One Time Code Link should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isLoginButtonDisplayed(), "Login button should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isEnterYourPasswordBodyPresent(getUnifiedAccount().getEmail()), "Password body with email and edit link should be present");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67222"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testValidEmailLogin() {
        SoftAssert softAssert = new SoftAssert();

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        new DisneyPlusLoginIOSPageBase(getDriver()).submitEmail(getUnifiedAccount().getEmail());
        softAssert.assertTrue(new DisneyPlusPasswordIOSPageBase(getDriver()).isPasswordPagePresent(), "Password page should have opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62046"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testValidPasswordOneProfile() {
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase homePage = new DisneyPlusHomeIOSPageBase(getDriver());

        welcomeScreen.clickLogInButton();
        login(getUnifiedAccount());
        handleGenericPopup(5, 1);
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75711"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testValidPasswordMultipleProfiles() {
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatchingPage = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        welcomeScreen.clickLogInButton();
        login(getUnifiedAccount());
        handleGenericPopup(5, 1);
        Assert.assertTrue(whoIsWatchingPage.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67218"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testInvalidEmailFormat() {
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        SoftAssert softAssert = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.fillOutEmailField("a");
        disneyPlusLoginIOSPageBase.clickContinueBtn();
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isAttributeValidationErrorMessagePresent(), NO_ERROR_DISPLAYED);

        disneyPlusLoginIOSPageBase.fillOutEmailField("emailWithoutTLD@gmail");
        disneyPlusLoginIOSPageBase.clickContinueBtn();
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isAttributeValidationErrorMessagePresent(), NO_ERROR_DISPLAYED);

        disneyPlusLoginIOSPageBase.fillOutEmailField("");
        disneyPlusLoginIOSPageBase.clickContinueBtn();
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isAttributeValidationErrorMessagePresent(), NO_ERROR_DISPLAYED);

        disneyPlusLoginIOSPageBase.fillOutEmailField("notAnEmail");
        disneyPlusLoginIOSPageBase.clickContinueBtn();
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isAttributeValidationErrorMessagePresent(), NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67236"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void testInvalidPassword() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        String invalidCreds = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_PASSWORD_LOGIN_ERROR.getText());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getUnifiedAccount().getEmail());
        disneyPlusPasswordIOSPageBase.enterLogInPassword("wrongUserPass" + "\n");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.getDynamicAccessibilityId(invalidCreds).isPresent(), NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67234"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void testEmptyPassword() {
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.enterLogInPassword("");
        passwordPage.getLoginButton().click();
        Assert.assertTrue(passwordPage.isAttributeValidationErrorMessagePresent(), NO_ERROR_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67230"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testShowHidePassword() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        StringBuilder hiddenPassword = new StringBuilder();
        String userPassword = getUnifiedAccount().getUserPass();
        hiddenPassword.append("•".repeat(userPassword.length()));

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getUnifiedAccount().getEmail());
        disneyPlusPasswordIOSPageBase.typePassword(getUnifiedAccount().getUserPass());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isShowPasswordIconDisplayed());
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getHidePasswordText(), hiddenPassword.toString());
        disneyPlusPasswordIOSPageBase.clickShowPasswordIcon();
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getShowPasswordText(), userPassword);
        disneyPlusPasswordIOSPageBase.clickHidePasswordIcon();
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getHidePasswordText(), hiddenPassword.toString());

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66764"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testChooseProfiles() {
        String kidProfile = "kidProfile";
        String profile3 = "profile 3";
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());
        SoftAssert softAssert = new SoftAssert();

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(kidProfile)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(profile3)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(getUnifiedAccount());
        pause(5);
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);

        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isHeaderTextDisplayed(), "Header text was not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(DEFAULT_PROFILE), "Profile name or image not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(kidProfile), "kid profile name or image not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(profile3), "additional adult name or image not displayed");
        softAssert.assertFalse(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent("DOESNT EXIST"), "profile displayed that should not.");

        disneyPlusWhoseWatchingIOSPageBase.clickProfile(DEFAULT_PROFILE);
        softAssert.assertTrue(new DisneyPlusHomeIOSPageBase(getDriver()).isOpened(), HOME_PAGE_NOT_DISPLAYED);

        // TODO add check that the correct profile loaded
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67266"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMinorLogInBlocked() throws InterruptedException {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusAccountIsMinorIOSPageBase cssPage = initPage(DisneyPlusAccountIsMinorIOSPageBase.class);
        String blockedHeader = "Sorry, you're not eligible to use this service.";
        String blockedDescriptionPart1 = "Please visit our Help Center to find out more or for further assistance if you no longer wish to have a MyDisney account.";
        String blockedDescriptionPart2 = "If you think this is an error, please contact Customer Support.";

        getUnifiedAccountApi().patchAccountBlock(getUnifiedAccount(), AccountBlockReasons.MINOR);

        welcomeScreen.clickLogInButton();
        login(getUnifiedAccount());
        Assert.assertTrue(cssPage.getStaticTextByName(blockedHeader).isPresent(),
                "CSS Header not displayed");
        sa.assertTrue(cssPage.getStaticTextByLabelContains(blockedDescriptionPart1).isPresent(),
                "CSS Description part 1 is not displayed");
        sa.assertTrue(cssPage.getStaticTextByLabelContains(blockedDescriptionPart2).isPresent(),
                "CSS Description part 2 is not displayed");
        sa.assertTrue(cssPage.getHelpCenterButton().isPresent(),
                "Help Center Button not present");
        sa.assertTrue(cssPage.getDismissButton().isPresent(),
                "Dismiss Button not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72163"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testLogInEntitledDOBCollectionOver18() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage =
                initPage(DisneyPlusEdnaDOBCollectionPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusAddProfileBannerIOSPageBase addProfileBannerPage = initPage(DisneyPlusAddProfileBannerIOSPageBase.class);
        DisneyPlusEnforceDOBCollectionPageBase enforceDOBCollectionPage =
                initPage(DisneyPlusEnforceDOBCollectionPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);

        String stepperDict = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.ONBOARDING_STEPPER.getText());

        //Create Disney account without DOB and Gender
        getDefaultCreateUnifiedAccountRequest()
                .setDateOfBirth(null)
                .setGender(null)
                .setPartner(Partner.DISNEY)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()));
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
        Assert.assertTrue(ednaDOBCollectionPage.isOpened(), DOB_PAGE_NOT_DISPLAYED);

        //Element Validations
        sa.assertTrue(loginPage.isDisneyLogoDisplayed(), DISNEY_PLUS_LOGO_NOT_DISPLAYED);
        sa.assertTrue(loginPage.isMyDisneyLogoDisplayed(), MYDISNEY_LOGO_NOT_DISPLAYED);
        sa.assertTrue(loginPage.getStaticTextByLabel(getLocalizationUtils().formatPlaceholderString(
                        stepperDict, Map.of("current_step", 3, "total_steps", 5))).isElementPresent(),
                "'STEP 3 OF 5' should be displayed");
        sa.assertTrue(ednaDOBCollectionPage.isEdnaDateOfBirthDescriptionPresent(), "DOB Sub Copy not displayed");
        sa.assertTrue(ednaDOBCollectionPage.isEdnaBirthdateLabelDisplayed(), "Birthdate label not displayed");
        sa.assertTrue(ednaDOBCollectionPage.isLogOutBtnDisplayed(), LOG_OUT_BTN_NOT_DISPLAYED);

        //Close and Reopen
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
        handleAlert();
        Assert.assertTrue(enforceDOBCollectionPage.isOpened(), DOB_PAGE_NOT_DISPLAYED);

        //Save DOB
        dobCollectionPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        dobCollectionPage.clickConfirmBtn();
        Assert.assertTrue(addProfilePage.isGenderFieldPresent(), COMPLETE_PROFILE_PAGE_NOT_DISPLAYED);

        //Finish Flow -> Log Out -> Log In -> DOB Collection not shown after Saving
        addProfilePage.chooseGender();
        updateProfilePage.tapSaveButton();
        addProfileBannerPage.tapDismissButton();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT);
        moreMenuPage.waitForLoaderToDisappear(FORTY_FIVE_SEC_TIMEOUT);
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72314"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLearnMoreModal() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginIOSPageBase = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordIOSPageBase = initPage(DisneyPlusPasswordIOSPageBase.class);

        String learnMoreHeader = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_LEARN_MORE_HEADER.getText());
        String learnMoreBody = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_LEARN_MORE_BODY.getText()), Map.of("link_1", "and more"));

        welcomeScreen.clickLogInButton();
        loginIOSPageBase.submitEmail(getUnifiedAccount().getEmail());
        sa.assertTrue(passwordIOSPageBase.isPasswordPagePresent(), "Password page did not open");
        Assert.assertTrue(passwordIOSPageBase.getLearnMoreAboutMyDisney().isPresent(),
                "Learn more about my disney Link is not displayed");
        passwordIOSPageBase.getLearnMoreAboutMyDisney().click();
        sa.assertTrue(passwordIOSPageBase.getStaticTextByLabel(learnMoreHeader).isPresent(),
                "'Disney+ is part of The Walt Disney Family of Companies' text should be displayed");
        sa.assertTrue(passwordIOSPageBase.getStaticTextByLabel(learnMoreBody).isPresent(),
                "'MyDisney lets you seamlessly log in to services' text should be displayed");

        passwordIOSPageBase.getLearnMoreAboutMyDisney().click();
        sa.assertFalse(passwordIOSPageBase.getStaticTextByLabel(learnMoreHeader).isPresent(TEN_SEC_TIMEOUT),
                "Learn more modal did not close");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75996"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.PRE_CONFIGURATION, US})
    public void verifyLogOutModalForDOBCollection() {
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreenPage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDobCollectionPage = initPage(DisneyPlusEdnaDOBCollectionPageBase.class);

        // Create Disney account without DOB and Gender
        setAccount(getUnifiedAccountApi().createAccount(
                getDefaultCreateUnifiedAccountRequest()
                        .setDateOfBirth(null)
                        .setGender(null)
                        .setPartner(Partner.DISNEY)
                        .setCountry(getLocalizationUtils().getLocale())
                        .setAddDefaultEntitlement(true)
                        .setLanguage(getLocalizationUtils().getUserLanguage())));

        welcomeScreenPage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());

        Assert.assertTrue(ednaDobCollectionPage.isOpened(), EDNA_DOB_COLLECTION_PAGE_NOT_DISPLAYED);
        ednaDobCollectionPage.tapLogOutButton();
        Assert.assertTrue(ednaDobCollectionPage.getLogoutModalHeader().isPresent(), LOGOUT_MODAL_NOT_DISPLAYED);

        ednaDobCollectionPage.getCancelButton().click();
        Assert.assertTrue(ednaDobCollectionPage.isOpened(), EDNA_DOB_COLLECTION_PAGE_NOT_DISPLAYED);
        Assert.assertFalse(ednaDobCollectionPage.getLogoutModalHeader().isPresent(FIVE_SEC_TIMEOUT),
                "Logout modal is still displayed");
    }
}
