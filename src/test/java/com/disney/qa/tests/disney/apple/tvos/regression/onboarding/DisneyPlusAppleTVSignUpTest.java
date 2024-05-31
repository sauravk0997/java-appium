package com.disney.qa.tests.disney.apple.tvos.regression.onboarding;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.StringGenerator;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

public class DisneyPlusAppleTVSignUpTest extends DisneyPlusAppleTVBaseTest {

    //TODO this test will be fix when new flows are updated QAA-14797
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90469", "XCDQA-90465"})
    @Test(description = "Email Input screen for Sign Up flow: Details", groups = {"Onboarding", "Smoke"}, enabled = false)
    public void signUpEmailScreenAppearance() {
        String marketingText = ".text";
        String legalText = "content.text";
        List<String> dictionaryList = new ArrayList<>();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        String siteConfig = getLocalizationUtils().getOneIdSiteConfig();
        dictionaryList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, AGREE_AND_CONTINUE_BTN.getText()));
        dictionaryList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, SIGN_UP_TITLE.getText()));
        dictionaryList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, PRIVACY_TERMS_BTN.getText()));
        dictionaryList.add(getContentApiChecker().getMarketingItems(siteConfig, marketingText));
        dictionaryList.add(getLocalizationUtils().getLegalItems(siteConfig, legalText).get(0).asText());
        dictionaryList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, ENTER_EMAIL.getText()));
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        IntStream.range(0, dictionaryList.size()).forEach(index -> {
            if (index < 4) {
                sa.assertEquals(dictionaryList.get(index), disneyPlusAppleTVSignUpPage.getTextFromStaticTextByLabel(dictionaryList.get(index)), dictionaryList.get(index) + " is not present on screen");
            } else if (index == 4) {
                //legalDisclosureView
                sa.assertTrue(disneyPlusAppleTVSignUpPage.islegalDisclosureViewPresent(), "Legal Disclosure view is not found");
            } else {
                sa.assertEquals(disneyPlusAppleTVLoginPage.getEmailFieldText(), dictionaryList.get(index), dictionaryList.get(index) + " is not found");
            }
        });
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isCheckBoxChecked(), "Checkbox was not checked by default");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90471", "XCDQA-90467"})
    @Test(description = "Email Input screen for Sign Up flow: Navigation", groups = {"Onboarding"})
    public void signUpEmailScreenNavigation() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVSignUpPage.clickMenu();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldFocused(), "Email input is not focused by default");
        disneyPlusAppleTVSignUpPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isAgreeAndContinueFocused(), "Continue button is not focused");
        sa.assertAll();
    }

    //TODO this test will be fix when new flows are updated QAA-14798
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90473"})
    @Test(description = "Email Input screen for Sign Up flow: checkbox", groups = {"Onboarding"}, enabled = false)
    public void signUpEmailScreenCheckBox() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVSignUpPage.clickDown();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isCheckBoxFocused(), "checkbox is not focused and checked");
        disneyPlusAppleTVSignUpPage.clickSelect();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isCheckBoxUnChecked(), "checkbox is checked");
        disneyPlusAppleTVSignUpPage.clickSelect();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isCheckBoxFocused(), "checkbox is not focused and checked");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90479", "XCDQA-90477"})
    @Test(description = "Email Input screen for Sign Up flow: on-screen keyboard appearance", groups = {"Onboarding", "Smoke"})
    public void emailInputKeyboardAppearance() {
        String tempEmailText = "somethin!^&&#@gmail";
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.clickEmailAndPressEnterNew();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "on screen keyboard is not present");
        disneyPlusAppleTVLoginPage.clickMenu();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isEmailFieldDisplayed(), "on screen keyboard is present");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(tempEmailText);

        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isAgreeAndContinueFocused(), "Agree and Continue button is not focused");
        sa.assertEquals(disneyPlusAppleTVLoginPage.getEmailFieldText(), tempEmailText, "Entered text for email did not match");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90475"})
    @Test(description = "Email Input screen for Sign Up flow: Previously Used address", groups = {"Onboarding"})
    public void emailInputPreviouslyUsedAddress() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVSignUpPage.waitUntilEmailFieldIsFocused();
        disneyPlusAppleTVSignUpPage.clickSelect();
        disneyPlusAppleTVLoginPage.selectPreviouslyUsedEmails();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch after selecting email");
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isAgreeAndContinueFocused(), "Agree and Continue button is not focused");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90443", "XCDQA-90445"})
    @Test(description = "Email Input screen for Sign Up flow: Error messages", groups = {"Onboarding"})
    public void emailInputErrorMessages() {
        String tempEmailText = "somethin!^&&#@gmail";
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");

        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isErrorMessagePresent(), "Error message is not been displayed");

        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(tempEmailText);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isErrorMessagePresent(), "Error message is not been displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90459", "XCDQA-90455", "XCDQA-90447", "XCDQA-90451", "XCDQA-90457", "XCDQA-90453", "XCDQA-90449"})
    @Test(description = "Email Input screen for Sign Up flow: Legal details", groups = {"Onboarding"}, enabled = false)
    public void legalSignUpEmailScreenDetails() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLegalPage disneyPlusAppleTVLegalPage = new DisneyPlusAppleTVLegalPage(getDriver());
        SoftAssert sa = new SoftAssert();
        String siteConfig = getLocalizationUtils().getOneIdSiteConfig();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");

        disneyPlusAppleTVSignUpPage.clickViewAgreementAndPolicies();
        sa.assertTrue(disneyPlusAppleTVLegalPage.isOpened(), "Legal Page did not launch");
        disneyPlusAppleTVLegalPage.clickMenu();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch after clicking menu on Legal page");
        disneyPlusAppleTVSignUpPage.clickViewAgreementAndPolicies();
        disneyPlusAppleTVLegalPage.areAllLegalDocumentsPresentAndScrollable(siteConfig, sa, getContentApiChecker());
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90461", "XCDQA-90463"})
    @Test(description = "Email Sign Up: Known User Flow", groups = {"Onboarding", "Smoke"})
    public void emailSignUpKnownUserFlow() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(entitledUser.getEmail());
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isOpened(), "Password entry page did not launch");
        disneyPlusAppleTVPasswordPage.logInWithPassword(entitledUser.getUserPass());
        //Wait to handle the expanded validation
        pause(5);
        collapseGlobalNav();
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page did not launch");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90441", "XCDQA-90487", "XCDQA-90489", "XCDQA-90491"})
    @Test(description = "Email Sign Up: valid email Flow, Password Creation: No Input password + invalid password + common password", groups = {"Onboarding"})
    public void emailSignUpValidInput() {
        String validEmail = DisneyApiCommon.getUniqueEmail();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        String noPasswordInputError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, PASSWORD_REQUIRED.getText());
        String invalidPasswordError = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS,
                DictionaryKeys.INVALID_PASSWORD_ENHANCED.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2")));
        String commonPassword = "Test123";

        SoftAssert sa = new SoftAssert();
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(validEmail);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreatePasswordScreenOpen(),
                "Create password screen did launch from enter your email");
        disneyPlusAppleTVPasswordPage.clickSignUp();
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getErrorMessageLabelText(), noPasswordInputError);
        disneyPlusAppleTVPasswordPage.createNewPasswordEntry(StringGenerator.generateWord(5));
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getErrorMessageLabelText(), invalidPasswordError);
        disneyPlusAppleTVPasswordPage.clickBack();
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        disneyPlusAppleTVPasswordPage.createNewPasswordEntry(commonPassword);
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getErrorMessageLabelText(), invalidPasswordError);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90485"})
    @Test(description = "Email Sign Up: valid email takes to paywall screen Flow", groups = {"Onboarding"}, enabled = false)
    public void emailSignUpValidInputTakesToPaywall() {
        String validEmail = DisneyApiCommon.getUniqueEmail();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVCompletePurchasePage disneyPlusAppleTVCompletePurchasePage = new DisneyPlusAppleTVCompletePurchasePage(getDriver());
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(validEmail);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreatePasswordScreenOpen(),
                "Create password screen did launch from enter your email");
        disneyPlusAppleTVPasswordPage.createNewPasswordEntry(StringGenerator.generateWord(5) + StringGenerator.generateNumeric(1));
        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isOpened(),
                "Paywall screen did not launch");
        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isYearlyBtnPresent(),
                "Yearly button is not present on paywall screen");
        sa.assertTrue(disneyPlusAppleTVCompletePurchasePage.isMonthlyBtnPresent(),
                "Monthly button is not present on paywall screen");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90493", "XCDQA-90495"})
    @Test(description = "Email Sign Up: Password Creation: Screen details", groups = {"Onboarding"}, enabled = false)
    public void passwordCreationScreenDetails() {
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        String validEmail = DisneyApiCommon.getUniqueEmail();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());

        List<String> dictionaryList = new ArrayList<>();
        dictionaryList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_PASSWORD_SCREEN_TITLE.getText()));
        dictionaryList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, FAT_FINGER_EMAIL.getText()));
        dictionaryList.add(validEmail);
        SoftAssert sa = new SoftAssert();

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(validEmail);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreatePasswordScreenOpen(),
                "Create password screen did launch from enter your email");

        IntStream.range(0, dictionaryList.size()).forEach(i -> {
            if (i == 0) {
                sa.assertEquals(disneyPlusAppleTVPasswordPage.getHeadlineHeaderText(), dictionaryList.get(i));
            } else {
                sa.assertTrue(disneyPlusAppleTVPasswordPage.isAIDElementPresentWithScreenshot(dictionaryList.get(i)), String.format("%s is not present", dictionaryList.get(i)));
            }
        });
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, CREATE_PASSWORD.getText()), "create password text is not present within text field");
        aliceDriver.screenshotAndRecognize().assertLabelContainsCaption(sa, getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, SIGN_UP.getText()), AliceLabels.BUTTON.getText());
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isPasswordFieldFocused(), "Create Password text field is not focused by default");

        disneyPlusAppleTVPasswordPage.moveDown(1, 1);
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isSignUpBtnFocused(), "Sign up button is not focused after navigating to it");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90497"})
    @Test(description = "Email Sign Up: Password Creation: Screen details", groups = {"Onboarding"})
    public void passwordCreationOnScreenKeyboard() {
        String validEmail = DisneyApiCommon.getUniqueEmail();
        String passReqsEnhanced = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                MY_DISNEY_CREATE_PASSWORD_STRENGTH_HINT.getText()), Map.of("minLength", Integer.parseInt("6"), "charTypes", Integer.parseInt("2")));
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());

        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(validEmail);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreatePasswordScreenOpen(),
                "Create password screen did launch from enter your email");
        disneyPlusAppleTVPasswordPage.clickPassword();
        sa.assertTrue(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "on screen keyboard is not present");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordHintText(), passReqsEnhanced);
        disneyPlusAppleTVSignUpPage.clickMenu();

        sa.assertFalse(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "on screen keyboard is present");
        sa.assertFalse(disneyPlusAppleTVPasswordPage.isPasswordHintTextDisplayed());
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90501", "XCDQA-90503", "XCDQA-90505"})
    @Test(description = "Password Creation: Password Captured and Encrypted", groups = {"Onboarding"})
    public void passwordCapturedAndEncrypted() {
        String expectedPassword = "••••••";
        String randomPassword = StringGenerator.generateWord(5) + StringGenerator.generateNumeric(1);
        String validEmail = DisneyApiCommon.getUniqueEmail();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());

        SoftAssert sa = new SoftAssert();
        selectAppleUpdateLaterAndDismissAppTracking();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(validEmail);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreatePasswordScreenOpen(),
                "Create password screen did launch from enter your email");
        disneyPlusAppleTVPasswordPage.clickPassword();
        disneyPlusAppleTVPasswordPage.enterPasswordCreatePassword(randomPassword);
        disneyPlusAppleTVPasswordPage.moveToContinueOrDoneBtnKeyboardEntry();
        disneyPlusAppleTVPasswordPage.clickSelect();
        sa.assertFalse(disneyPlusAppleTVLoginPage.isKeyboardPresent(), "on screen keyboard is present after entering password and clicking on Continue");
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), expectedPassword);

        disneyPlusAppleTVPasswordPage.clickHideShowPasswordBtn();
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), randomPassword);
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getShowHidePasswordBtnState(), "hide");

        disneyPlusAppleTVPasswordPage.clickHideShowPasswordBtn();
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordFieldText(), expectedPassword);
        sa.assertEquals(disneyPlusAppleTVPasswordPage.getShowHidePasswordBtnState(), "show");

        sa.assertAll();
    }

    //TODO get Alice trained for color recognition for password strength
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90499"})
    @Test(description = "Password Creation: Password Strength", groups = {"Onboarding"})
    public void createPasswordStrengthMeter() {
        selectAppleUpdateLaterAndDismissAppTracking();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        String validEmail = DisneyApiCommon.getUniqueEmail();
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        List<List<String>> passwordStrengthMeterLists = disneyPlusAppleTVPasswordPage.getStrengthMeterVerificationLists();
        List<String> ratingsList = passwordStrengthMeterLists.get(0);
        List<String> passwordList = passwordStrengthMeterLists.get(1);
        List<String> widthList = passwordStrengthMeterLists.get(2);
        List<String> colorsList = passwordStrengthMeterLists.get(3);

        SoftAssert sa = new SoftAssert();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        sa.assertTrue(disneyPlusAppleTVSignUpPage.isOpened(), "Sign up email entry screen did not launch");
        disneyPlusAppleTVLoginPage.selectEnterNewEnterEmailSelectDoneBtn(validEmail);
        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
        sa.assertTrue(disneyPlusAppleTVPasswordPage.isCreatePasswordScreenOpen(),
                "Create password screen did launch from enter your email");
        disneyPlusAppleTVPasswordPage.clickPassword();

        IntStream.range(0, passwordList.size()).forEach(i -> {
            disneyPlusAppleTVPasswordPage.enterPasswordCreatePassword(passwordList.get(i));
            sa.assertEquals(disneyPlusAppleTVPasswordPage.getPasswordStrengthMeterWidth(), Integer.parseInt(widthList.get(i)));
            // for the first password item from passwordList, no meter text is shown
            if (i != 0) {
                String dictionaryKey = ratingsList.get(i);
                sa.assertTrue(disneyPlusAppleTVPasswordPage.isDynamicAccessibilityIDElementPresent(dictionaryKey), String.format("%s is not present", dictionaryKey));
            }
            aliceDriver.screenshotAndRecognize().assertLabelContainsColor(sa, colorsList.get(i), AliceLabels.PASSWORD_STRENGTH_BAR.getText());
        });
        sa.assertAll();
    }
}
