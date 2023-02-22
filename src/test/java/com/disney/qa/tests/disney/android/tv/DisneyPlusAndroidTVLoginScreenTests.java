package com.disney.qa.tests.disney.android.tv;

import com.disney.alice.AliceAssertion;
import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.api.dictionary.DisneyDictionary;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVLoginPage;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.StringGenerator;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVLoginPage.LoginItems.UNKNOWN_EMAIL_TRY_AGAIN_BTN;
import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVLoginScreenTests extends DisneyPlusAndroidTVBaseTest {

    private final ThreadLocal<String> validEmail = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        validEmail.set(new DisneyApiProvider().getUniqueUserEmail());
    }

    @Test(description = "Verify hide/show button password functionality via login flow")
    public void hideShowPasswordBtnLoginFlow() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66481", "XCDQA-66483", "XCDQA-66485", "XCDQA-66487"));
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100829"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1842"));
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        DisneyDictionary dict = new DisneyDictionary(fullDictionary.get().toString());
        NavHelper navHelper = new NavHelper(androidTVUtils.getCastedDriver());

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isEnterPasswordPageOpen(), PASSWORD_PAGE_LOAD_ERROR);

        // Need more than three dots and easy to OCR text to get Alice to cooperate....
        // "XXXXXXXXX" no longer works. But TACOTRUCK does. ¯\_(ツ)_/¯
        String keys = disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents("TACOTRUCK");
        disneyPlusAndroidTVLoginPage.get().navigateToHideShowPassword();

        // Keyboard observed getting in the way on Shield Experience 9.1...
        androidTVUtils.hideKeyboardIfPresent();

        // Use Alice to see hidden password dots...
        AliceAssertion aliceAssertion = aliceDriver.screenshotAndRecognize();
        aliceAssertion.assertNoLabelContainsCaption(sa, keys, "text_field", "Text field for password should NOT contain: " + keys);

        // SHOW/HIDE password description indicates which button is showing.
        var desc = androidTVUtils.getContentDescription(disneyPlusAndroidTVLoginPage.get().getHideShowPasswordButtonElement());
        var expectedText = dict.getTextFromKey(DictionaryKeys.SHOW_PASSWORD.getText());
        sa.assertTrue(desc.contains(expectedText), "Password view button description should contain: " + expectedText);

        // Use Alice to OCR PW text since the text field returns the PW in clear text whether hidden or not.
        // Nav helper to ensure hide/show button state is changed before screenshot is taken.
        navHelper.keyUntilElementDescChanged(() -> disneyPlusAndroidTVLoginPage.get().getHideShowPasswordButtonElement(), AndroidKey.DPAD_CENTER);
        aliceAssertion = aliceDriver.screenshotAndRecognize();
        aliceAssertion.assertLabelContainsCaption(sa, keys, "text_field", "Text field should show visible password: " + keys);
        desc = androidTVUtils.getContentDescription(disneyPlusAndroidTVLoginPage.get().getHideShowPasswordButtonElement());
        expectedText = dict.getTextFromKey(DictionaryKeys.HIDE_PASSWORD.getText());
        sa.assertTrue(desc.contains(expectedText), "Password view button description should contain: " + expectedText);

        checkAssertions(sa);
    }

    @Test(description = "Login with an account that was never entitled to invoke complete purchase flow")
    public void inactiveSubscriptionLogin() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66558"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1844"));
        SoftAssert sa = new SoftAssert();

        DisneyAccount account = disneyAccountApi.get().createAccount(country, language.split("_")[0]);
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(account.getEmail());
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(account.getUserPass());

        Assert.assertTrue(disneyPlusAndroidTVCompletePurchasePageBase.get().isOpened(), "Compete purchase page should launch");

        checkAssertions(sa);
    }

    @Test(description = "Verify email entry page details via login flow and keyboard verification", groups = {"smoke"})
    public void loginScreenVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66481", "XCDQA-66483", "XCDQA-66485", "XCDQA-66487"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1839"));
        SoftAssert sa = new SoftAssert();
        JsonNode applicationDictionary = getApplicationDictionary(language);
        List<String> loginTexts = Stream.of(DisneyPlusAndroidTVLoginPage.LoginItems.LOGIN_TITLE.getText(),
                DisneyPlusAndroidTVLoginPage.LoginItems.ENTER_EMAIL.getText(), DisneyPlusAndroidTVLoginPage.LoginItems.CONTINUE_BTN.getText())
                .collect(Collectors.toList());

        // XCDQA-66481 - Log in button leads to log in page
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();

        // XCDQA-66485 - Log in page details
        for (int i = 0; i < loginTexts.size(); i++) {
            sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getLoginEmailEntryTexts().get(i),
                    apiProvider.get().getDictionaryItemValue(applicationDictionary, loginTexts.get(i)),
                    String.format("Actual: %s Expected: %s", disneyPlusAndroidTVLoginPage.get().getLoginEmailEntryTexts().get(i),
                            apiProvider.get().getDictionaryItemValue(applicationDictionary, loginTexts.get(i))));
        }
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isEmailFieldFocused(), "Email field should be focused");
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isDisneyPlusLogoPresent(), "Disney Logo should be present");

        // XCDQA-66487 - Text can be entered in log in page email field
        String keys = disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents(StringGenerator.generateEmail());
        disneyPlusAndroidTVCommonPage.get().closeKeyboard();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().verifyEmailInput(keys), "Entered text should match text field");

        // XCDQA-66483 - Pressing back from Log in page leads to Welcome page
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);

        checkAssertions(sa);
    }

    @Test(description = "Verify an error is presented when attempting to continue with no email given")
    public void noEmailLoginError() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100833"));
        SoftAssert sa = new SoftAssert();
        String errorLabel = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DisneyPlusAndroidTVLoginPage.LoginItems.INVALID_EMAIL.getText());

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();

        disneyPlusAndroidTVLoginPage.get().clickStandardButton();

        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isInputErrorVisible(), "Continuing sign up without an email should present an error");
        sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getInputErrorText(), errorLabel);

        checkAssertions(sa);
    }

    @Test(description = "Verify password page details and default state via login flow", groups = {"smoke"})
    public void passwordPageDetailsLoginFlow() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65734", "XCDQA-66469"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1841"));
        SoftAssert sa = new SoftAssert();
        String passwordLabel = apiProvider.get().getDictionaryItemValue(fullDictionary.get(), DisneyPlusAndroidTVLoginPage.LoginItems.CURRENT_PASSWORD.getText());

        // XCDQA-66469 - Entering a valid email sends user to password page
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isEnterPasswordPageOpen(), PASSWORD_PAGE_LOAD_ERROR);

        List<String> list = Stream.of(DisneyPlusAndroidTVLoginPage.LoginItems.PASSWORD_PAGE_TITLE.getText(), DisneyPlusAndroidTVLoginPage.LoginItems.PASSWORD_GHOST.getText(),
                DisneyPlusAndroidTVLoginPage.LoginItems.LOGIN_BTN.getText(), DisneyPlusAndroidTVLoginPage.LoginItems.FORGOT_PASSWORD_BTN.getText()).collect(Collectors.toList());

        // XCDQA-65734 - Password page details
        for (int i = 0; i < list.size(); i++) {
            sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getPasswordPageLoginTexts().get(i),
                    apiProvider.get().getDictionaryItemValue(fullDictionary.get(), list.get(i)));
        }

        sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getPasswordLabelText(), passwordLabel);
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isDisneyPlusLogoPresent(), "Disney Logo should be present");
        new AliceDriver(getDriver()).screenshotAndRecognize().isLabelPresent(sa, AliceLabels.BUTTON_SHOW_PASSWORD.getText());
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isPasswordFieldFocused(), "Password input field should be focused");

        checkAssertions(sa);
    }

    @Test(description = "Verify unknown email page errors by going back to enter email page and email edit/update")
    public void unknownEmailScreenVerification() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66489", "XCDQA-66491", "XCDQA-66493", "XCDQA-66497"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1840"));
        SoftAssert sa = new SoftAssert();

        List<String> pageTextsExpected = Stream.of(DisneyPlusAndroidTVLoginPage.LoginItems.UNKNOWN_EMAIL_PAGE_TITLE.getText(),
                DisneyPlusAndroidTVLoginPage.LoginItems.UNKNOWN_EMAIL_SUBTEXT.getText(), UNKNOWN_EMAIL_TRY_AGAIN_BTN.getText(),
                DisneyPlusAndroidTVLoginPage.LoginItems.UNKNOWN_EMAIL_BTN_SIGN_UP.getText()).collect(Collectors.toList());

        sa.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), WELCOME_PAGE_LOAD_ERROR);

        // Some or all partner devices won't have signup button present thus removing anything signup related
        boolean isSignUpBtnPresent = disneyPlusAndroidTVWelcomePage.get().isLoginButtonPresent();

        if (!isSignUpBtnPresent) {
            pageTextsExpected.remove(1);
        }

        // XCDQA-66489 - Unknown email submission
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().enterEmail(validEmail.get());
        disneyPlusAndroidTVCommonPage.get().pressTab();
        disneyPlusAndroidTVCommonPage.get().getAndroidTVUtilsInstance().hideKeyboardIfPresent();
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();

        // XCDQA-66491 - Unknown email details
        // Some or all partner devices won't have signup button present thus removing anything signup related
        List<String> unknownEmailPageTexts = disneyPlusAndroidTVLoginPage.get().getUnknownEmailPageTexts();
        if (!isSignUpBtnPresent) {
            unknownEmailPageTexts.remove(1);
        }

        for (int i = 0; i < pageTextsExpected.size() - 1; i++) {
            sa.assertEquals(unknownEmailPageTexts.get(i),
                    apiProvider.get().getDictionaryItemValue(fullDictionary.get(), pageTextsExpected.get(i)));
        }

        if (isSignUpBtnPresent) {
            sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getTierTwoButtonText(),
                    apiProvider.get().getDictionaryItemValue(fullDictionary.get(), pageTextsExpected.get(pageTextsExpected.size() - 1)));
        }

        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isTryAgainBtnFocused(), "Try again button should be focused");

        // XCDQA-66489 - Unknown email submission
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().verifyEmailInput(validEmail.get()), "Email should not change");
        sa.assertEquals(disneyPlusAndroidTVLoginPage.get().getErrorTextView(), apiProvider.get().getDictionaryItemValue(fullDictionary.get(),
                DisneyPlusAndroidTVLoginPage.LoginItems.UNKNOWN_EMAIL_ERROR.getText()));

        // XCDQA-66497 - Unknown email edit email after error
        String keys = disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents(StringGenerator.generateWord(7));
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().verifyEmailInput(validEmail.get() + keys), "Unable to update/edit email");

        checkAssertions(sa);
    }

    @Test(description = "Verify focus and keyboard on password page via login flow")
    public void verifyFocusAndKeyboardLoginPasswordPage() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-65738", "XCDQA-65740", "XCDQA-65742", "XCDQA-65744"));
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1843"));
        SoftAssert sa = new SoftAssert();

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        String getGhostText = disneyPlusAndroidTVLoginPage.get().getPasswordText();

        // XCDQA-65740 - Onscreen keyboard appears when selected
        sa.assertTrue(disneyPlusAndroidTVCommonPage.get().openKeyboardWithSelect(), "Keyboard should launch");
        disneyPlusAndroidTVCommonPage.get().typeUsingKeyEvents(StringGenerator.generateWord(5));
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().closeKeyboardEnterPasswordPage(), "Keyboard should close");

        // XCDQA-65742 - Ghost text disappears when entering text
        sa.assertNotEquals(disneyPlusAndroidTVLoginPage.get().getPasswordText(), getGhostText, "Ghost text should not be present");

        // XCDQA-65738 - Forgot password button is focusable
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().navigateAndFocusForgotPasswordBtn(), "Forgot password button should be focused");

        // XCDQA-65740 - Pressing back from password page returns to email log in page
        disneyPlusAndroidTVCommonPage.get().pressBackTimes(1);
        sa.assertTrue(disneyPlusAndroidTVLoginPage.get().isLoginPageOpened(), LOGIN_PAGE_LOAD_ERROR);

        checkAssertions(sa);
    }
}
