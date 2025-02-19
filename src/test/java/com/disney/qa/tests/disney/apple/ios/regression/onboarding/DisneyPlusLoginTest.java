package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusAddProfileBannerIOSPage;
import com.disney.util.TestGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.alice.AliceDriver;
import com.disney.qa.api.account.AccountBlockReasons;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.disney.qa.common.DisneyAbstractPage.FORTY_FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.US;

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
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isBackButtonPresent(), BACK_ARROW_NOT_DISPLAYED);
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isDisneyLogoDisplayed(), DISNEY_PLUS_LOGO_NOT_DISPLAYED);
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isMyDisneyLogoDisplayed(), MYDISNEY_LOGO_NOT_DISPLAYED);
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isStep2LabelDisplayed(), "stepper should be displayed");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isHeaderTextDisplayed(), "Header text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isPasswordEntryFieldDisplayed(), "Password field should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isCaseSensitiveHintPresent(), "Password hint text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isShowPasswordIconDisplayed(), "Show Password button should be present");
        softAssert.assertTrue(disneyPlusChangeEmailIOSPageBase.isLearnMoreAboutMyDisney(), "Learn more about my disney Link should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isForgotPasswordButtonPresent(), "One Time Code Link should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isLoginButtonDisplayed(), "Login button should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isEnterYourPasswordBodyPresent(getAccount().getEmail()), "Password body with email and edit link should be present");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67222"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testValidEmailLogin() {
        SoftAssert softAssert = new SoftAssert();

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        new DisneyPlusLoginIOSPageBase(getDriver()).submitEmail(getAccount().getEmail());
        softAssert.assertTrue(new DisneyPlusPasswordIOSPageBase(getDriver()).isPasswordPagePresent(), "Password page should have opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62046"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testValidPasswordOneProfile() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = new DisneyPlusHomeIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(getAccount().getUserPass());
        softAssert.assertTrue(disneyPlusHomeIOSPageBase.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75711"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testValidPasswordMultipleProfiles() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName("Prof2").language(getAccount().getProfileLang()).avatarId(null).kidsModeEnabled(false).dateOfBirth(null).build());
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(getAccount().getUserPass());
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isOpened(), "Who's Watching page should have been opened");

        softAssert.assertAll();
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
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        disneyPlusPasswordIOSPageBase.enterLogInPassword("wrongUserPass" + "\n");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.getDynamicAccessibilityId(invalidCreds).isPresent(), NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67234"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void testEmptyPassword() {
        DisneyPlusLoginIOSPageBase loginPage =  initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
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
        String userPassword = getAccount().getUserPass();
        hiddenPassword.append("•".repeat(userPassword.length()));

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        disneyPlusPasswordIOSPageBase.typePassword(getAccount().getUserPass());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isShowPasswordIconDisplayed());
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getHidePasswordText(), hiddenPassword.toString());
        disneyPlusPasswordIOSPageBase.clickShowPasswordIcon();
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getShowPasswordText(), userPassword);
        disneyPlusPasswordIOSPageBase.clickHidePasswordIcon();
        softAssert.assertEquals(disneyPlusPasswordIOSPageBase.getHidePasswordText(), hiddenPassword.toString());

        softAssert.assertAll();
    }

    //TODO: QAA-14561
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68436"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void testEmailNoAccountTryAgain() {
        String noEmailError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN_INVALID_EMAIL_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        String newEmail = "thisEmailDoesntExist@disenystreaming.com";
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(newEmail);

        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), noEmailError, NO_ERROR_DISPLAYED);
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isTryAgainAlertButtonDisplayed(), "try again button was not displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isSignUpAlertButtonDisplayed(), "sign up button was not displayed");
        disneyPlusLoginIOSPageBase.clickAlertTryAgainButton();
        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), noEmailError, NO_ERROR_DISPLAYED);
        disneyPlusLoginIOSPageBase.fillOutEmailField(getAccount().getEmail());
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66764"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testChooseProfiles() {
        String kidProfile = "kidProfile";
        String profile3 = "profile 3";
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusWhoseWatchingIOSPageBase disneyPlusWhoseWatchingIOSPageBase = new DisneyPlusWhoseWatchingIOSPageBase(getDriver());

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(kidProfile).language("en").avatarId("90e6cc76-b849-5301-bf2a-d8ddb200d07b").kidsModeEnabled(true).dateOfBirth(null).build());
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(profile3).language("en").avatarId("b4515c3a-d9a9-57e4-b2be-a793104c0839").kidsModeEnabled(false).dateOfBirth(null).build());

        SoftAssert softAssert = new SoftAssert();
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(getAccount());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62679"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAccountOnHold() {
        SoftAssert softAssert = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusAccountOnHoldIOSPageBase disneyPlusAccountOnHoldIOSPageBase = initPage(DisneyPlusAccountOnHoldIOSPageBase.class);

        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        request.setLanguage(getLanguage());
        request.setCountry(getCountry());
        request.setSkus(Arrays.asList(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY));
        List<DisneyOrder> orderList = new LinkedList();
        orderList.add(DisneyOrder.SET_BILLING_HOLD);
        request.setOrderSettings(orderList);
        DisneyAccount accountWithBillingHold = getAccountApi().createAccount(request);

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(accountWithBillingHold);

        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getLogoutButton().isPresent(), LOG_OUT_BTN_NOT_DISPLAYED);
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getAccountHoldTitle().isPresent(), "Account Hold Title not present");
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getAccountHoldSubText().isPresent(), "Account Hold Subtext not present");
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getUpdatePaymentButton().isPresent(), "Update Payment Button not present");
        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getRefreshButton().isPresent(), "Refresh Button not present");
        //QCE-1253 Causes below to fail on iPhone. Otherwise test passes on iPad.
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(TABLET)) {
            LOGGER.info("Tablet");
            aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");
        }

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67266"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMinorLogInBlocked() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusAccountIsMinorIOSPageBase disneyPlusAccountIsMinorIOSPageBase = initPage(DisneyPlusAccountIsMinorIOSPageBase.class);

        DisneyAccount minorAccount = getAccountApi().createAccount("Yearly", "US", "en", "V1");
        getAccountApi().patchAccountBlock(minorAccount, AccountBlockReasons.MINOR);

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(minorAccount);
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getNotEligibleHeader().isPresent(),
                "Account Ineligibility Header not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getNotEligibleDescription().isPresent(),
                "Account Ineligibility Text not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getHelpCenterButton().isPresent(),
                "Help Center Button not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getDismissButton().isPresent(),
                "Dismiss Button not present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72163"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testLogInEntitledDOBCollectionOver18() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage =
                new DisneyPlusEdnaDOBCollectionPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusAddProfileIOSPageBase addProfilePage = new DisneyPlusAddProfileIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase homePage = new DisneyPlusHomeIOSPageBase(getDriver());
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = new DisneyPlusUpdateProfileIOSPageBase(getDriver());
        DisneyPlusAddProfileBannerIOSPage addProfileBannerPage = new DisneyPlusAddProfileBannerIOSPage(getDriver());
        DisneyPlusEnforceDOBCollectionPageBase enforceDOBCollectionPage =
                new DisneyPlusEnforceDOBCollectionPageBase(getDriver());
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        String stepperDict = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.ONBOARDING_STEPPER.getText());

        //Create Disney account without DOB
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setAddDefaultEntitlement(true)
                .setLanguage(getLocalizationUtils().getUserLanguage());
        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
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
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
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

        String learnMoreHeader= getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_LEARN_MORE_HEADER.getText());
        String learnMoreBody = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        DictionaryKeys.MY_DISNEY_LEARN_MORE_BODY.getText()), Map.of("link_1", "and more"));

        welcomeScreen.clickLogInButton();
        loginIOSPageBase.submitEmail(getAccount().getEmail());
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
}
