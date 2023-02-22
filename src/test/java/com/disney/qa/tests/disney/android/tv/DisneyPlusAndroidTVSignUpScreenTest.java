package com.disney.qa.tests.disney.android.tv;

import com.disney.alice.AliceAssertion;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionary;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVLoginPage;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.StringGenerator;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVLoginPage.LoginItems.*;
import static com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVLegalPageBase.LegalItems.LEGAL_TITLE;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVSignUpScreenTest extends DisneyPlusAndroidTVBaseTest {

    private ThreadLocal<String> validEmail = new ThreadLocal<>();

    @BeforeMethod
    public void testSetup() {
        validEmail.set(apiProvider.get().getUniqueUserEmail());
    }

    @Test(description = "Verify Error and password field change upon invalid submission", groups = {"smoke"})
    public void createPasswordErrors() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65418", "XCDQA-65420", "XCDQA-65422",
                "XCDQA-67866", "XCDQA-67868", "XCDQA-67870"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1835"));
        SoftAssert sa = new SoftAssert();
        String passwordErrorRegEx = apiProvider.get().getDictionaryItemValue(fullDictionary.get(),
                        DisneyPlusAndroidTVLoginPage.LoginItems.INVALID_PASSWORD_ENHANCED.getText())
                .replaceAll("\\$\\S*", ".*");

        String unexpectedError = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DisneyPlusAndroidTVLoginPage.LoginItems.UNEXPECTED_ERROR.getText());
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        String color = "Red";
        String email = apiProvider.get().getUniqueUserEmail();

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().enterEmail(email);
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        // The keyboard intermittently shows here. Dismiss if needed.
        disneyPlusAndroidTVCommonPage.get().closeKeyboard(3);

        // XCDQA-67866 - XCDQA-65418 - Continue with no text input
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isCreatePasswordPageOpen(), PASSWORD_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().clickPasswordFieldIfNotFocused();
        disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().hideKeyboardIfPresent();
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();
        sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getErrorTextView(), unexpectedError);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isErrorViewPresent(), "Error message should be present.");

        // XCDQA-67870 - XCDQA-65422 - Error message color check
        aliceDriver.screenshotAndRecognize().isColorNamePresent(sa, AliceLabels.ERROR_MESSAGE.getText(), color);

        // XCDQA-67868 - XCDQA-65420 - Continue w/ password requirements not met
        disneyPlusAndroidTVCommonPage.get().openKeyboardWithSelect();
        disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents("EEE");
        disneyPlusAndroidTVCommonPage.get().closeKeyboard();
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        // RegEx match dictionary pattern to displayed text.
        String errorText = disneyPlusAndroidTVLoginPage.get().getErrorTextView();
        sa.assertTrue(errorText.matches(passwordErrorRegEx), "Error text should match pattern derived from dictionary.");
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isErrorViewPresent(), "Error message should be present.");
        aliceDriver.screenshotAndRecognize().isColorNamePresent(sa, AliceLabels.ERROR_MESSAGE.getText(), color);

        checkAssertions(sa);
    }

    @Test(description = "Valid email is taken from sign up to create password page, verify create PW details and default focus", groups = {"smoke"})
    public void createPasswordPageDetails() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65746", "XCDQA-65748", "XCDQA-65752", "XCDQA-65760",
                "XCDQA-66552", "XCDQA-67820", "XCDQA-67872", "XCDQA-67874", "XCDQA-67878"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1831"));
        SoftAssert sa = new SoftAssert();
        String validEmail = apiProvider.get().getUniqueUserEmail();
        String signUpPageTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), SIGN_UP_TITLE.getText());

        List<String> list = new ArrayList<>();
        list.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CREATE_PASSWORD_PAGE_TITLE.getText()));
        list.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CREATE_PASSWORD.getText()));
        list.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), FAT_FINGER_EMAIL.getText()));
        list.add(validEmail);
        list.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), CONTINUE_BTN.getText()));

        // XCDQA-66552 - XCDQA-67820 - User is sent to password page after entering an available email
        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().enterEmail(validEmail);
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        // On some devices the keyboard unexpectedly open at this point, close it.
        disneyPlusAndroidTVCommonPage.get().closeKeyboard();

        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isCreatePasswordPageOpen(), PASSWORD_PAGE_LOAD_ERROR);

        // XCDQA-65746 - XCDQA-67872 - Password page details
        List<String> createPasswordTexts = disneyPlusAndroidTVLoginPage.get().getCreatePasswordPageTexts();

        for (int i = 0; i < list.size(); i++) {
            sa.assertEquals(createPasswordTexts.get(i), list.get(i));
        }

        // XCDQA-65748 - XCDQA-67874 - Previously entered email is visible on password page
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isPasswordFieldFocused(), "Password field should be focused by default");
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isEmailSelectable(), "Email shouldn't be clickable or focusable");
        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.BUTTON_SHOW_PASSWORD.getText());

        // XCDQA-65752 - XCDQA-67878 - Backing out of password page lands user on email sign up page
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isTextElementExactPresent(signUpPageTitle), SIGN_UP_PAGE_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().verifyEmailInput(validEmail), "Email should match previously entered email");

        checkAssertions(sa);
    }

    @Test(description = "Navigation and focus of create a password page")
    public void createPasswordPageNavigation() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65750", "XCDQA-65758", "XCDQA-67876", "XCDQA-67880"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1832"));
        SoftAssert sa = new SoftAssert();

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().enterEmail(validEmail.get());
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        // On some devices the keyboard unexpectedly open at this point, close it.
        disneyPlusAndroidTVCommonPage.get().closeKeyboard();

        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isCreatePasswordPageOpen(), PASSWORD_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().clickPasswordFieldIfNotFocused();

        // On some devices the keyboard unexpectedly open at this point, close it.
        disneyPlusAndroidTVCommonPage.get().closeKeyboard();

        // XCDQA-65758 - XCDQA-67880 - Keyboard appears when selecting password field and disappears when dismissed
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().openKeyboardWithSelect(), "Keyboard should launch");
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().closeKeyboard(), "Keyboard should close");

        // XCDQA-65750 - XCDQA-67876 - Password page navigation
        sa.assertFalse(disneyPlusAndroidTVLoginPage.get().verifyCreatePasswordPageNavigation(),
                "Password page elements should be navigable.");

        checkAssertions(sa);
    }

    @Test(description = "Check for errors on email input field")
    public void emailInputErrors() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67812", "XCDQA-67814", "XCDQA-65400", "XCDQA-65402"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1827"));
        SoftAssert sa = new SoftAssert();
        JsonNode applicationDictionary = getApplicationDictionary(language);
        String emptyEmailError = apiProvider.get().getDictionaryItemValue(applicationDictionary, INVALID_EMAIL.getText());
        String invalidEmail = StringGenerator.generateWord(8);

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);

        // XCDQA-67812 - XCDQA-65400 - Error is shown for entering no email
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();
        sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getErrorTextView(), emptyEmailError);

        // XCDQA-67814 - XCDQA-65402 - Error is shown for invalid email
        disneyPlusAndroidTVLoginPage.get().enterEmail(invalidEmail);
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();
        sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getErrorTextView(), emptyEmailError);

        checkAssertions(sa);
    }

    @Test(description = "Check keyboard visibility and usage")
    public void keyboardCheckEmailField() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67832", "XCDQA-67834", "XCDQA-65344", "XCDQA-65346"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1826"));
        SoftAssert sa = new SoftAssert();

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);

        // XCDQA-67832 - XCDQA-65344 - Onscreen keyboard appears when selecting text field, and disappears when pressing back
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().openKeyboardWithSelect(), "Keyboard should be displayed");
        String keys = disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents(StringGenerator.generateWord(7));
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().closeKeyboard(), "Keyboard should not be displayed");

        // XCDQA-67834 - XCDQA-65346 - Text input on keyboard appears when keyboard is closed
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().verifyEmailInput(keys), "Input from keyboard should match displayed text");

        checkAssertions(sa);
    }

    @Test(description = "Verify keyboard closes properly and ensure hide/show password focus and functionality")
    public void keyboardAndHideShowPassword() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65762", "XCDQA-65764", "XCDQA-65766", "XCDQA-65768", "XCDQA-67886",
                "XCDQA-67888", "XCDQA-67890", "XCDQA-67892"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1834"));
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        DisneyDictionary dict = new DisneyDictionary(fullDictionary.get().toString());
        NavHelper navHelper = new NavHelper(androidTVUtils.getCastedDriver());

        // Proceed through signup to password page
        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().enterEmail(validEmail.get());
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();
        androidTVUtils.hideKeyboardIfPresent();
        disneyPlusAndroidTVLoginPage.get().clickPasswordFieldIfNotFocused();

        // Enter a long password that Alice can recognize
        disneyPlusAndroidTVCommonPage.get().openKeyboardWithSelect();
        pause(1);
        String keys = disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents("TACOTRUCK");
        sa.assertFalse(disneyPlusAndroidTVCommonPage.get().closeKeyboard(), "Keyboard should close");
        disneyPlusAndroidTVLoginPage.get().navigateToHideShowPassword();

        // Keyboard observed getting in the way on Shield Experience 9.1...
        androidTVUtils.hideKeyboardIfPresent();

        // XCDQA-67886 - XCDQA-65762 - Use Alice to ensure password is not visible
        AliceAssertion aliceAssertion = aliceDriver.screenshotAndRecognize();
        aliceAssertion.assertNoLabelContainsCaption(sa, keys, "text_field", "Text field for password should NOT contain: " + keys);

        // XCDQA-67888 - XCDQA-65764 - SHOW/HIDE password description indicates which button is showing
        var desc = androidTVUtils.getContentDescription(disneyPlusAndroidTVLoginPage.get().getHideShowPasswordButtonElement());
        var expectedText = dict.getTextFromKey(DictionaryKeys.SHOW_PASSWORD.getText());
        sa.assertTrue(desc.contains(expectedText), "Password view button description should contain: " + expectedText );

        // XCDQA-67890 - XCDQA-65766 - XCDQA-65768 - XCDQA-67892
        // Use Alice to OCR PW text since the text field returns the PW in clear text whether hidden or not
        navHelper.keyUntilElementDescChanged(() -> disneyPlusAndroidTVLoginPage.get().getHideShowPasswordButtonElement(), AndroidKey.DPAD_CENTER);
        aliceAssertion = aliceDriver.screenshotAndRecognize();
        aliceAssertion.assertLabelContainsCaption(sa, keys, "text_field", "Text field should show visible password: " + keys);
        desc = androidTVUtils.getContentDescription(disneyPlusAndroidTVLoginPage.get().getHideShowPasswordButtonElement());
        expectedText = dict.getTextFromKey(DictionaryKeys.HIDE_PASSWORD.getText());
        sa.assertTrue(desc.contains(expectedText), "Password view button description should contain: " + expectedText );

        checkAssertions(sa);
    }

    @Test(description = "Document page verification and page navigation")
    public void legalPageFromSignUp() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67839", "XCDQA-67841", "XCDQA-67843", "XCDQA-67845", "XCDQA-67849",
                "XCDQA-66005", " XCDQA-66007", "XCDQA-66009", "XCDQA-66011", "XCDQA-66015"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1828"));
        disneyLanguageUtils.get().setLegalDocuments();
        SoftAssert sa = new SoftAssert();
        String legalTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), LEGAL_TITLE.getText());

        Set<String> labelsSet = new LinkedHashSet<>();
        Set<String> documentsSet = new LinkedHashSet<>();

        Map<String, String> legalDocs = disneyLanguageUtils.get().getLegalDocuments();
        legalDocs.keySet().forEach(doc -> documentsSet.add(legalDocs.get(doc)));

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);

        // XCDQA-67839 - XCDQA-66005 - Selecting View Terms & Use button leads to legal page
        disneyPlusAndroidTVLoginPage.get().proceedToLegalPage(R.CONFIG.get("locale").equalsIgnoreCase("kr"));


        // XCDQA-67843 - XCDQA-66009 - Legal page details
        sa.assertEquals(disneyPlusAndroidTVLegalPageBase.get().getLegalScreenTitle(), legalTitle);

        List<String> legalPageBtnTexts = disneyPlusAndroidTVLegalPageBase.get().getAllLegalButtonsText();
        List<String> labels = new ArrayList<>(labelsSet);
        List<String> documents = new ArrayList<>(documentsSet);

        for (int i = 0; i < labels.size(); i++) {
            sa.assertEquals(legalPageBtnTexts.get(i), labels.get(i), "Legal button text should match at index " + i);
            sa.assertEquals(disneyPlusAndroidTVLegalPageBase.get().getDocumentTitle(), labels.get(i), "Page title should match at index " + i);
            sa.assertEquals(disneyPlusAndroidTVLegalPageBase.get().getDocumentText(), documents.get(i), "Page text should match at index " + i);
            sa.assertTrue(disneyPlusAndroidTVLegalPageBase.get().isLegalButtonFocused(i), labels.get(i) + " button should be focused");
            disneyPlusAndroidTVLegalPageBase.get().pressDown(1);
            if (i == 0) {
                sa.assertEquals(disneyPlusAndroidTVLegalPageBase.get().getDocumentTitle(), labels.get(i), "Page title should match at index " + i);
                sa.assertEquals(disneyPlusAndroidTVLegalPageBase.get().getDocumentText(), documents.get(i), "Page text should match at index " + i);
                new AliceDriver(getDriver()).screenshotAndRecognize().assertNoLabelContainsCaption(sa, labels.get(i),
                        AliceLabels.VERTICAL_MENU_ITEM_HOVERED.getText());
                sa.assertEquals(disneyPlusAndroidTVLegalPageBase.get().getLegalScreenTitle(), legalTitle, "Page title should match.");
            }
            disneyPlusAndroidTVLegalPageBase.get().selectFocusedElement();
        }

        // XCDQA-67841 - XCDQA-66007 - Pressing back from the legal page leads to the sign up page
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().openSignUpPageFromLegal(), SIGN_UP_PAGE_LOAD_ERROR);

        checkAssertions(sa);
    }

    @Test(description = "Check the opt-in checkbox on sign up page")
    public void signUpCheckbox() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65342", "XCDQA-67830"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1825"));
        SoftAssert sa = new SoftAssert();

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);

        disneyPlusAndroidTVCommonPage.get().pressTabToMoveToTheNextField();

        // Check below is when email is empty
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpCheckboxChecked(), "Sign up checkbox should be checked");
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpCheckboxFocused(), "Sign up checkbox should be focused");
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertFalse(disneyPlusAndroidTVLoginPage.get().isSignUpCheckboxChecked(), "Sign up checkbox should not be checked");
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpCheckboxFocused(), "Sign up checkbox should not be focused");
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpCheckboxChecked(), "Sign up checkbox should be checked");
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpCheckboxFocused(), "Sign up checkbox should be focused");

        checkAssertions(sa);
    }

    @Test(description = "Email entry page details", groups = {"smoke"})
    public void signUpPageDetails() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65338", "XCDQA-67826"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1824"));
        SoftAssert sa = new SoftAssert();
        String siteConfig = disneyLanguageUtils.get().getOneIdSiteConfig();
        List<String> dictionaryList = new ArrayList<>();
        dictionaryList.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), SIGN_UP_TITLE.getText()));
        dictionaryList.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), ENTER_EMAIL.getText()));
        dictionaryList.add(apiProvider.get().getMarketingItems(siteConfig, MARKETING_TEXT.getText()));
        dictionaryList.add(disneyLanguageUtils.get().getLegalItems(siteConfig, LEGAL_TEXT.getText()).get(0).asText());
        dictionaryList.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), AGREE_AND_CONTINUE_BTN.getText()));
        dictionaryList.add(apiProvider.get().getDictionaryItemValue(fullDictionary.get(), PRIVACY_TERMS_BTN.getText()));

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        List<String> list = disneyPlusAndroidTVLoginPage.get().getSignUpEmailEntryTexts();
        for(int i = 0; i < dictionaryList.size(); i++) {
            sa.assertEquals(list.get(i), dictionaryList.get(i), "Text should match for index" + i);
        }

        checkAssertions(sa);
    }

    @Test(description = "Verify that registered user is taken through login flow from sign up and is able to login")
    public void signUpPageKnownUser() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-67854", "XCDQA-67856", "XCDQA-66152", "XCDQA-66150"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1830"));

        SoftAssert sa = new SoftAssert();
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        DisneyAccount account = getAccountApi().createAccount("Yearly", country, language, "V1");
        String passwordPageTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DisneyPlusAndroidTVLoginPage.LoginItems.PASSWORD_PAGE_TITLE.getText());

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);

        // XCDQA-67854 - XCDQA-66152 - User is sent through log in flow when trying to register with already active email
        disneyPlusAndroidTVLoginPage.get().enterEmail(account.getEmail());
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        // Dodge on-screen keyboard...
        androidTVUtils.hideKeyboardIfPresent();

        sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getPasswordTitleText(), passwordPageTitle);
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(account.getUserPass());

        // XCDQA-67856 - XCDQA-66150 - User is sent to Home after logging in with already active email
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), HOME_PAGE_LOAD_ERROR);

        checkAssertions(sa);
    }

    @Test(description = "Title of sign up page and navigation back to login page and focus of items")
    public void signUpPageTitleAndBackNavigation() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65334", "XCDQA-65336", "XCDQA-65340", "XCDQA-67822",
                "XCDQA-67824", "XCDQA-67828"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1823"));
        String signUpTitle = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), SIGN_UP_TITLE.getText());
        SoftAssert sa = new SoftAssert();

        // XCDQA-65334 - XCDQA-67822 - Start Free Trial button leads to sign up email page
        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isTextElementExactPresent(signUpTitle), signUpTitle + " should be found on sign up page.");

        // XCDQA-65340 - XCDQA-67828 - Sign up page elements are focusable
        sa.assertFalse(disneyPlusAndroidTVLoginPage.get().areSignUpElementsFocused(), "All sign up elements should be focusable.");

        // XCDQA-65336 - XCDQA-67824 - Pressing back from sign up page leads to welcome page
        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().pressBackToOpenWelcomePage(), WELCOME_PAGE_LOAD_ERROR);
        checkAssertions(sa);
    }

    @Test(description = "User with valid email and valid password submission can proceed to paywall", groups = {"smoke"})
    public void signUpToPaywallPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66154", "XCDQA-67858"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1836"));

        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        SoftAssert sa = new SoftAssert();
        String email = apiProvider.get().getUniqueUserEmail();

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().enterEmail(email);
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        // Dodge on-screen keyboard...
        androidTVUtils.hideKeyboardIfPresent();

        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isCreatePasswordPageOpen(), PASSWORD_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get()
                .enterPassword(StringGenerator.generateWord(10) + StringGenerator.generateNumeric(3));
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        disneyPlusAndroidTVLoginPage.get().enterDOB(sa,"01011972" );

        sa.assertTrue(disneyPlusAndroidTVPaywallPage.get().isOpened(), PAYWALL_PAGE_LOAD_ERROR);

        checkAssertions(sa);
    }

    @Test(description = "Verify user is taken to sign up page from unknown email page")
    public void unknownEmailPageToSignUp() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66495"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1837"));
        SoftAssert sa = new SoftAssert();
        String title = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DisneyPlusAndroidTVLoginPage.LoginItems.SIGN_UP_TITLE.getText());

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().enterEmail(validEmail.get());
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();
        disneyPlusAndroidTVLoginPage.get().proceedToSignUpFromUnknownEmail();

        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().isTextElementExactPresent(title), "Sign Up page title should be found");
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().verifyEmailInput(validEmail.get()), "Email in text field should match input");

        checkAssertions(sa);
    }

    //TODO: Train Alice to capture strength meter(levels) and label them appropriately & separate this for android and firetv
    @Test(description = "Verify various password strength meters are being displayed based on password input")
    public void verifyPasswordStrengthMeter() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65754", "XCDQA-65756", "XCDQA-67884"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1833"));

        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        SoftAssert sa = new SoftAssert();

        List<String> list = Stream.of("1", "123456", "aa", "c", "d", "@")
                .collect(Collectors.toList());
        List<DisneyPlusAndroidTVLoginPage.LoginItems> ratingsList = Stream.of(PASSWORD_RATING_FAIR, PASSWORD_RATING_FAIR, PASSWORD_RATING_GOOD, PASSWORD_RATING_GREAT)
                .collect(Collectors.toList());

        disneyPlusAndroidTVWelcomePage.get().proceedToSignUp();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isSignUpPageOpen(), SIGN_UP_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().enterEmail(validEmail.get());
        disneyPlusAndroidTVLoginPage.get().clickAgreeAndContinueButton();

        // Dodge on-screen keyboard...
        androidTVUtils.hideKeyboardIfPresent();

        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isCreatePasswordPageOpen(), PASSWORD_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVLoginPage.get().clickPasswordFieldIfNotFocused();

        boolean isAmazon = AndroidTVUtils.isAmazon();
        // XCDQA-65754 - XCDQA-67884 - XCDQA-65756 - Password strength is displayed and expected appearance is shown
        for (int i = 0; i < list.size(); i++) {
            // We specifically launch the keyboard here for FireTV devices
            if (!new AndroidUtilsExtended().isKeyboardShown()) {
                disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
            }
            disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents(list.get(i));

            // Rating for AndroidTV disappears if keyboard is closed, only viable for FireTV devices
            if (isAmazon) {
                disneyPlusAndroidTVCommonPage.get().closeKeyboardOnscreen(disneyPlusAndroidTVLoginPage.get().getCreatePasswordTitle());
            }

            if (i > 1) {
                sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getStrengthMeterText(),
                        apiProvider.get().getDictionaryItemValue(fullDictionary.get(), ratingsList.get(i - 2).getText()));
            }
        }
        checkAssertions(sa);
    }
}
