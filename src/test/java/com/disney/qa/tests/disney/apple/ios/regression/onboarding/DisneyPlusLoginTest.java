package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
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

public class DisneyPlusLoginTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String NO_ERROR_DISPLAYED = "error message was not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72745"})
    @Test(description = "Log In - Verify Login Screen UI", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testLogInScreen() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = new DisneyPlusSignUpIOSPageBase(getDriver());

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isBackButtonPresent(), "Back Arrow should be present");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isDisneyLogoDisplayed(), "Disney+ logo image should be displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isMyDisneyLogoDisplayed(), "MyDisney logo image should be displayed");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isStep1LabelDisplayed(), "STEP 1 text should be displayed");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isEnterEmailHeaderDisplayed(), "'Enter your email to continue' text should be displayed");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isEmailFieldDisplayed(), "Email field should be present");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isEnterEmailBodyDisplayed(), "Log in to Disney+ with your MyDisney account should display or Email Body should display");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.continueButtonPresent(), "Continue (primary) button should be present");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isLearnMoreHeaderDisplayed(), "'Disney+ is part of The Walt Disney Family of Companies' text should be displayed");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isLearnMoreBodyDisplayed(), "'MyDisney lets you seamlessly log in to services' text should be displayed");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72744"})
    @Test(description = "Log In - Verify Password Screen UI", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testPasswordScreen() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isBackArrowDisplayed(), "Expected: Back Arrow should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isDisneyLogoDisplayed(), "Expected: Disney+ logo image should be displayed");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isHeaderTextDisplayed(), "Expected: Header text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isPasswordFieldDisplayed(), "Expected: Password field should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isPasswordHintTextDisplayed(), "Expected: Password hint text should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isForgotPasswordLinkDisplayed(), "Expected: Forgot password link should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isShowPasswordIconDisplayed(), "Expected: Show Password button should be present");
        softAssert.assertTrue(disneyPlusPasswordIOSPageBase.isLoginButtonDisplayed(), "Expected: Login button should be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67222"})
    @Test(description = "Log in - Verify valid email", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testValidEmailLogin() {
        SoftAssert softAssert = new SoftAssert();

        new DisneyPlusWelcomeScreenIOSPageBase(getDriver()).clickLogInButton();
        new DisneyPlusLoginIOSPageBase(getDriver()).submitEmail(getAccount().getEmail());
        softAssert.assertTrue(new DisneyPlusPasswordIOSPageBase(getDriver()).isPasswordPagePresent(), "Password page should have opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62046"})
    @Test(description = "Log in - Verify valid password - One Profile", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testValidPasswordOneProfile() {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = new DisneyPlusHomeIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        disneyPlusPasswordIOSPageBase.submitPasswordForLogin(getAccount().getUserPass());
        softAssert.assertTrue(disneyPlusHomeIOSPageBase.isOpened(), "Home page should have been opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75711"})
    @Test(description = "Log in - Verify valid password - Multiple Profiles", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
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
    @Test(description = "Log in - Verify invalid email formats - One Character, No Top Level Domain, No Email, Invalid Email", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
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
    @Test(description = "Log in - Verify invalid password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67218"})
    @Test(description = "Log in - Verify empty password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testEmptyPassword() {
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.enterLogInPassword("");
        passwordPage.getLoginButton().click();
        Assert.assertTrue(passwordPage.isAttributeValidationErrorMessagePresent(), NO_ERROR_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67230"})
    @Test(description = "Log in - Verify Show and Hide Password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
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
    @Test(description = "Log in - Unknown email - try again", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION}, enabled = false)
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

    //TODO: QAA-14561
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68436"})
    @Test(description = "Log in - Unknown email - sign up", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void testEmailNoAccountSignUp() {
        String noEmailError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN_INVALID_EMAIL_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = new DisneyPlusSignUpIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        String newEmail = "thisEmailDoesntExist2@disenystreaming.com";
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(newEmail);

        softAssert.assertEquals(disneyPlusLoginIOSPageBase.getErrorMessageString(), noEmailError, NO_ERROR_DISPLAYED);
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isNoAccountAlertSubtextDisplayed(), "No Account alert subtext was not displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isTryAgainAlertButtonDisplayed(), "try again button was not displayed");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.isSignUpAlertButtonDisplayed(), "sign up button was not displayed");

        disneyPlusLoginIOSPageBase.clickAlertSignUpButton();

        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(), "sign up page did not load");
        softAssert.assertTrue(disneyPlusSignUpIOSPageBase.isEmailFieldDisplayed(), "email field was not displayed");
        softAssert.assertEquals(disneyPlusSignUpIOSPageBase.getEmailFieldText(), newEmail, "Email field was not prefilled with email address");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66764"})
    @Test(description = "Verify Choose Profiles Screen", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
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
//        disneyPlusWhoseWatchingIOSPageBase.dismissAppTrackingPopUp();

        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isHeaderTextDisplayed(), "Header text was not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(DEFAULT_PROFILE), "Profile name or image not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(kidProfile), "kid profile name or image not displayed");
        softAssert.assertTrue(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent(profile3), "additional adult name or image not displayed");
        softAssert.assertFalse(disneyPlusWhoseWatchingIOSPageBase.isAccessModeProfileIconPresent("DOESNT EXIST"), "profile displayed that should not.");

        disneyPlusWhoseWatchingIOSPageBase.clickProfile(DEFAULT_PROFILE);
        softAssert.assertTrue(new DisneyPlusHomeIOSPageBase(getDriver()).isOpened(),
                "Expected - Home page should be opened after selecting profile");

        // TODO add check that the correct profile loaded
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67250"})
    @Test(description = "Log In - Complete Subscription UI", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testNotEntitledAccount() {
        DisneyAccount nonActiveAccount = getAccountApi().createAccount("US", "en");
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusCompleteSubscriptionIOSPageBase disneyPlusCompleteSubscriptionIOSPageBase = new DisneyPlusCompleteSubscriptionIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(nonActiveAccount);

        // Complete subscription page loads
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getHeroImage().isPresent(), "hero not present");
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getPrimaryText().isPresent(), "primary text not present");
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getSecondaryText().isPresent(), "secondary text not present");
        softAssert.assertTrue(disneyPlusCompleteSubscriptionIOSPageBase.getCompleteSubscriptionButton().isPresent(), "button not present");
        //TODO:https://jira.disneystreaming.com/browse/QCE-1253
        //aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67254"})
    @Test(description = "Log in - expired account", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testExpiredAccount() {
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusRestartSubscriptionIOSPageBase disneyPlusRestartSubscriptionIOSPageBase = new DisneyPlusRestartSubscriptionIOSPageBase(getDriver());

        DisneyAccount expired = getAccountApi().createExpiredAccount("Yearly", "US", "en", "V2");
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(expired);

        // Restart Subscription Page loads
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getHeroImage().isPresent(), "hero not present");
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getPrimaryText().isPresent(), "primary text not present");
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getSecondaryText().isPresent(), "secondary text not present");
        softAssert.assertTrue(disneyPlusRestartSubscriptionIOSPageBase.getRestartSubscriptionButton().isPresent(), "button not present");
        //TODO:https://jira.disneystreaming.com/browse/QCE-1253
        //aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67236"})
    @Test(description = "Log in - Incorrect Password", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testIncorrectPassword() {
        String invalidPasswordError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_PASSWORD_LOGIN_ERROR.getText());
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusLoginIOSPageBase disneyPlusLoginIOSPageBase = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase disneyPlusPasswordIOSPageBase = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        disneyPlusLoginIOSPageBase.submitEmail(getAccount().getEmail());
        disneyPlusPasswordIOSPageBase.enterLogInPassword("incorrectPassword123" + "\n");
        softAssert.assertTrue(disneyPlusLoginIOSPageBase.getDynamicAccessibilityId(invalidPasswordError).isPresent(), NO_ERROR_DISPLAYED);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62679"})
    @Test(description = "Log in - Verify Account on Hold", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
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

        softAssert.assertTrue(disneyPlusAccountOnHoldIOSPageBase.getLogoutButton().isPresent(), "Logout button not present");
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
    @Test(description = "Log in - Verify Minor User is blocked from logging in", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION })
    public void verifyMinorLogInBlocked() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusAccountIsMinorIOSPageBase disneyPlusAccountIsMinorIOSPageBase = initPage(DisneyPlusAccountIsMinorIOSPageBase.class);

        DisneyAccount minorAccount = getAccountApi().createAccount("Yearly", "US", "en", "V1");
        getAccountApi().patchAccountBlock(minorAccount, AccountBlockReasons.MINOR);

        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(minorAccount);

        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getNotEligibleHeader().isPresent(), "Account Ineligibility Header not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getNotEligibleSubText().isPresent(), "Account Ineligibility Text not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getHelpCenterButton().isPresent(), "Help Center Button not present");
        softAssert.assertTrue(disneyPlusAccountIsMinorIOSPageBase.getDismissButton().isPresent(), "Dismiss Button not present");

        softAssert.assertAll();
    }
}
