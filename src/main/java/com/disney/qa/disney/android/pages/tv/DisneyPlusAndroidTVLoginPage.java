package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusDiscoverPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusLoginPageBase;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage.getTextViewInElement;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:CallToDeprecatedMethod" })
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusLoginPageBase.class)
public class DisneyPlusAndroidTVLoginPage extends DisneyPlusLoginPageBase {

    @FindBy(id = "registerTitle")
    private ExtendedWebElement createPasswordTitle;

    @FindBy(id = "registerEmailTextView")
    private ExtendedWebElement createPasswordStaticEmail;

    @FindBy(id = "agreeContinueButton")
    private ExtendedWebElement disclosureAgreeAndContinue;

    @FindBy(id = "disclosureTitle")
    private ExtendedWebElement disclosureTitle;

    @FindBy(id = "fatFingerHeaderTextView")
    private ExtendedWebElement fatFingerHeader;

    @FindBy(xpath = "//android.widget.TextView[@resource-id = 'com.disney.disneyplus:id/standardButtonText' and @text = '%s']")
    private ExtendedWebElement genericStandardButtonText;

    @FindBy(id = "legalese_tv")
    private ExtendedWebElement legalTerms;

    @FindBy(id = "loginButton")
    private ExtendedWebElement loginBtn;

    @FindBy(id = "loginTitle")
    private ExtendedWebElement loginTitle;

    @FindBy(id = "meterProgressBar")
    private ExtendedWebElement meterProgressBar;

    @FindBy(id = "passwordInputText")
    private ExtendedWebElement passwordEntryForm;

    @FindBy(id = "passwordRestrictionLayout")
    private ExtendedWebElement passwordRestrictionLayout;

    @FindBy(id = "passwordRestrictionText")
    private ExtendedWebElement passwordRestrictionText;

    @FindBy(id = "loginPasswordRoot")
    private ExtendedWebElement passwordRoot;

    @FindBy(id = "passwordStrength")
    private ExtendedWebElement passwordStrengthMeter;

    @FindBy(id = "meterTextView")
    private ExtendedWebElement passwordStrengthMeterText;

    @FindBy(id = "passwordTitle")
    private ExtendedWebElement passwordTitle;

    @FindBy(id = "inputDescriptionTextView")
    private ExtendedWebElement passwordTopLabel;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/titleTextSwitcher']/android.widget.TextView")
    private ExtendedWebElement proceedButton;

    @FindBy(id = "interstitialButtonPrimary")
    private ExtendedWebElement restartSubButton;

    @FindBy(id = "interstitialDescriptionMain")
    private ExtendedWebElement restartSubScreenTitle;

    @FindBy(id = "inputShowPwdImageView")
    private ExtendedWebElement showPasswordBtn;

    @FindBy(id = "optInCheckbox")
    private ExtendedWebElement signUpCheckBox;

    @FindBy(id = "signUpTitle")
    private ExtendedWebElement signUpTitle;

    @FindBy(id = "termsButton")
    private ExtendedWebElement termsButton;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id = 'com.disney.disneyplus:id/termsButton']/*/*/*")
    private ExtendedWebElement termsButtonExtended;
    private AndroidTVUtils androidTVUtils;

    public DisneyPlusAndroidTVLoginPage(WebDriver driver) {
        super(driver);
        androidTVUtils = new AndroidTVUtils(getDriver());
    }

    public boolean areSignUpElementsFocused() {
        List<Boolean> list = new ArrayList<>();
        list.add(androidTVUtils.isElementFocused(editTextField));
        androidTVUtils.pressTab();
        androidTVUtils.hideKeyboardIfPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        list.add(androidTVUtils.isElementFocused(signUpCheckBox));
        androidTVUtils.pressDown();
        list.add(androidTVUtils.isElementFocused(continueLoadingButton));
        androidTVUtils.pressDown();
        list.add(androidTVUtils.isElementFocused(termsButton));

        return list.contains(false);
    }

    public void clickForgotPassword() {
        forgotPasswordButton.click();
    }

    public void clickLoginBtn() {
        loginBtn.click();
    }

    public void clickOnTermsButton() {
        termsButton.click();
    }

    public void clickPasswordField() {
        editTextField.click();
    }

    public void clickPasswordFieldIfNotFocused() {
        if (!androidTVUtils.isFocused(editTextField)) {
            editTextField.click();
            androidTVUtils.hideKeyboardIfPresent();
        }
    }

    public void enterDOB(SoftAssert sa, String dob) {
        NavHelper nav = new NavHelper(getCastedDriver());
        nav.waitUntilTrue(() -> findExtendedWebElement(dateOfBirthContainer.getBy()).isVisible(),15,1);
        sa.assertTrue(isDOBContainerDisplayed(), "Date of birth page entry page should be shown.");

        androidTVUtils.sendInput(dob);
        UniversalUtils.captureAndUpload(getCastedDriver());
        clickAgreeAndContinueButton();
    }

    public void clickRestartSubscriptionButton() {
        restartSubButton.click();
    }

    public boolean closeKeyboardEnterPasswordPage() {
        DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, 2, "Unable to hide keyboard")
                .until(it -> {
                    androidTVUtils.pressBack();
                    return editTextField.isElementPresent(ONE_SEC_TIMEOUT);
                });
        return true;
    }

    public void enterEmail(String text) {
        editTextField.type(text);
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void enterPassword(String password) {
        editTextField.type(password);
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public List<String> getCreatePasswordPageTexts() {
        List<String> list = new ArrayList<>();
        list.add(createPasswordTitle.getText());
        list.add(textViewEditText.getText());
        list.add(fatFingerHeader.getText());
        list.add(createPasswordStaticEmail.getText());
        list.add(proceedButton.getText());

        return list;
    }

    public ExtendedWebElement getCreatePasswordTitle() {
        return createPasswordTitle;
    }

    public BufferedImage getErrorImage() {
        return new UniversalUtils().getElementImage(errorTextView);
    }

    public String getErrorTextView() {
        String text = errorTextView.getText().replaceAll("\\n", " ");
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public ExtendedWebElement getHideShowPasswordButtonElement() {
        return showPasswordBtn;
    }

    public BufferedImage getHideShowPasswordImage() {
        try { // Wait here for the image to load once updated
            TimeUnit.SECONDS.sleep(SHORT_TIMEOUT);
        } catch (Exception e) {
            LOGGER.error("Interrupted with Exception: " + e);
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
        return new UniversalUtils().getElementImage(showPasswordBtn);
    }

    public BufferedImage getImage() {
        return new UniversalUtils().getElementImage(meterProgressBar);
    }

    public List<String> getLoginEmailEntryTexts() {
        List<String> textList = Stream.of(loginTitle.getText(), textViewEditText.getText()).collect(Collectors.toList());
        textList.add(String.valueOf(getTextViewInElement(standardButton)));
        return textList;
    }

    public BufferedImage getPasswordInputImage() {
        return new UniversalUtils().getElementImage(editTextField);
    }

    public String getPasswordLabelText() {
        return passwordTopLabel.getText();
    }

    public List<String> getPasswordPageLoginTexts() {
        List<ExtendedWebElement> list = findExtendedWebElements(standardButton.getBy());
        List<String> textList = Stream.of(passwordTitle.getText(), textViewEditText.getText()).collect(Collectors.toList());
        list.forEach(item -> textList.add(String.valueOf(getTextViewInElement(item))));
        return textList;
    }

    public String getPasswordPageTitle() {
        return loginTitle.getText();
    }

    public String getPasswordText() {
        String text = editTextField.getText();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public ExtendedWebElement getSignUpTitle() {
        return signUpTitle;
    }

    public String getStrengthMeterText() {
        return passwordStrengthMeterText.getText();
    }

    public List<String> getSignUpEmailEntryTexts() {
        List<String> list = new ArrayList<>();
        list.add(signUpTitle.getText());
        list.add(textViewEditText.getText());
        list.add(signUpCheckBox.getText());
        list.add(legalTerms.getText());
        list.add(proceedButton.getText());
        list.add(termsButtonExtended.getText());

        return list;
    }

    public List<String> getUnknownEmailPageTexts() {
        return Stream.of(tierTwoTitle.getText(), tierTwoSubtitle.getText(), tierTwoButtonOne.getText())
                .collect(Collectors.toList());
    }

    public BufferedImage enterPasswordAndReturnElementImage(String text) {
        editTextField.type(text);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return new AndroidUtilsExtended().getElementImage(passwordRestrictionLayout);
    }

    public boolean isContinueBtnFocused() {
        return androidTVUtils.isElementFocused(continueLoadingButton);
    }

    public boolean isCreatePasswordPageOpen() {
        boolean isPresent = createPasswordTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isDisclosurePageOpen() {
        return disclosureTitle.isElementPresent();
    }

    public boolean isEmailFieldFocused() {

        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(editTextField);
    }

    // TODO: Move this to a utils class if this method is commonly used in other test cases
    public boolean isEmailSelectable() {
        return createPasswordStaticEmail.getAttribute("clickable").equals("false") &&
                createPasswordStaticEmail.getAttribute("focusable").equals("false");
    }

    public boolean isEnterPasswordPageOpen() {
        return passwordRoot.isElementPresent();
    }

    public boolean isHideShowPasswordBtnFocused() {
        return androidTVUtils.isFocused(showPasswordBtn);
    }

    public boolean isLoginPageOpened() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return loginTitle.isElementPresent();
    }

    public boolean isPasswordFieldFocused() {
        return new AndroidTVUtils(getCastedDriver()).isElementFocused(editTextField);
    }

    public boolean isSignUpCheckboxChecked() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return signUpCheckBox.isChecked();
    }

    public boolean isSignUpCheckboxFocused() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(signUpCheckBox);
    }

    public boolean isSignUpPageOpen() {
        boolean isPresent = signUpCheckBox.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isStrengthMeterPresent() {
        boolean isPresent = meterProgressBar.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isRestartSubscriptionPageOpened() {
        return restartSubScreenTitle.isElementPresent();
    }

    public boolean isTryAgainBtnFocused() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return androidTVUtils.isElementFocused(tierTwoButtonOne);
    }

    public boolean isUnknownUserPageOpened() {
        return tierTwoSecondButton.isElementPresent();
    }

    public void handleDisclosurePage(ExtendedWebElement element) {
        if (!element.isElementPresent(DELAY)) {
            DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, ONE_SEC_TIMEOUT, "Unable to focus Agree and Continue button")
                    .until(it -> {
                        androidTVUtils.pressDown();
                        return androidTVUtils.isFocused(disclosureAgreeAndContinue);
                    });
            androidTVUtils.pressSelect();
        }
    }

    @Override
    public DisneyPlusDiscoverPageBase logInWithPassword(String password) {
        editTextField.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        editTextField.type(password);
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressTab, 2, 1);
        UniversalUtils.captureAndUpload(getCastedDriver());
        androidTVUtils.pressSelect();
        if (new AndroidUtilsExtended().isKeyboardShown()) {
            androidTVUtils.hideKeyboardIfPresent();
            androidTVUtils.pressSelect();
        }
        return initPage(DisneyPlusDiscoverPageBase.class);
    }

    public boolean navigateAndFocusForgotPasswordBtn() {
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressTab, 2, 1);
        androidTVUtils.pressDown();
        UniversalUtils.captureAndUpload(getCastedDriver());
        androidTVUtils.hideKeyboardIfPresent();

        return androidTVUtils.isElementFocused(forgotPasswordButton);
    }

    public void navigateToHideShowPassword() {
        androidTVUtils.pressTab();
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public boolean openSignUpPageFromLegal() {
        androidTVUtils.pressBack();
        boolean isTrue = editTextField.isElementPresent() && signUpTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isTrue;
    }

    public void proceedToLegalPage(boolean isKR) {
        androidTVUtils.pressTab();
        androidTVUtils.keyPressTimes(AndroidTVUtils::pressDown, isKR ? 3 : 2, 0);

        androidTVUtils.pressSelect();
    }

    public void selectSignUp(boolean isKR) {
        androidTVUtils.pressTab();
        if (isKR) {
            androidTVUtils.pressDown();
            androidTVUtils.pressSelect();
            androidTVUtils.pressDown();
            androidTVUtils.pressSelect();
            androidTVUtils.pressDown();
            androidTVUtils.pressDown();
        } else {
            androidTVUtils.keyPressTimes(AndroidTVUtils::pressDown, 1, 0);
        }
        androidTVUtils.pressSelect();
    }

    public void proceedToSignUpFromUnknownEmail() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        tierTwoSecondButton.click();
    }

    public void proceedToOTPPageUsingRemote() {
        androidTVUtils.pressTab();
        androidTVUtils.pressTab();
        androidTVUtils.pressDown();
        androidTVUtils.pressSelect();
    }

    @Override
    public DisneyPlusLoginPageBase proceedToPasswordMode(String username) {
        NavHelper navHelper = new NavHelper(androidTVUtils.getCastedDriver());
        UniversalUtils.captureAndUpload(getCastedDriver());
        editTextField.isElementPresent();
        editTextField.type(username);
        UniversalUtils.captureAndUpload(getCastedDriver());

        // Escape from edit box and dismiss keyboard.
        androidTVUtils.pressTab();
        androidTVUtils.hideKeyboardIfPresent();

        // Dodge spam opt-in
        if(disclosureTitle.isVisible(1)) {
            navHelper.keyUntilElementFocused(()-> disclosureAgreeAndContinue, AndroidKey.DPAD_DOWN);
        }
        else {
            navHelper.keyUntilElementFocused(()->continueLoadingButton, AndroidKey.DPAD_DOWN);
        }

        UniversalUtils.captureAndUpload(getCastedDriver());
        androidTVUtils.pressSelect();
        if (!passwordRoot.isElementPresent(DELAY))
            androidTVUtils.pressEnter();
        return initPage(DisneyPlusLoginPageBase.class);
    }

    public void signUpPageErrorHandler() {
        if (!signUpCheckBox.isElementPresent(10)) {
            if (!contentTitle.isElementPresent(10)) {
                return;
            }
            positiveButton.click();
        }
        new DisneyPlusAndroidTVWelcomePage(getDriver()).proceedToSignUp();
    }

    public boolean verifyCreatePasswordPageNavigation() {
        List<Boolean> list = new ArrayList<>();
        list.add(androidTVUtils.isElementFocused(editTextField));
        androidTVUtils.pressTab();
        androidTVUtils.hideKeyboardIfPresent();
        list.add(androidTVUtils.isElementFocused(showPasswordBtn));
        UniversalUtils.captureAndUpload(getCastedDriver());
        androidTVUtils.pressDown();
        list.add(androidTVUtils.isElementFocused(continueLoadingButton));

        return list.contains(false);
    }

    public boolean verifyEmailInput(String text) {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return editTextField.getText().equals(text);
    }

    public boolean verifyStandardButtonText(String text) {
        return genericStandardButtonText.format(text).isElementPresent();
    }

    // Log In, Sign Up, and Password pages
    public enum LoginItems {
        AGREE_AND_CONTINUE_BTN("btn_agree_continue"),
        CASE_SENSITIVE_PASSWORD_LABEL("case_sensitive"),
        CONTINUE_BTN("btn_continue"),
        CREATE_PASSWORD("create_password"),
        CREATE_PASSWORD_PAGE_TITLE("create_password_title"),
        CURRENT_PASSWORD("current_password"),
        ENTER_EMAIL("enter_email"),
        FAT_FINGER_EMAIL("fat_finger_email_copy"),
        FORGOT_PASSWORD_BTN("btn_login_forgot_password"),
        INVALID_EMAIL("invalid_email"),
        INVALID_PASSWORD("invalidpassword"),
        INVALID_PASSWORD_ENHANCED("invalidpassword_enhanced"),
        UNEXPECTED_ERROR("unexpectederror"),
        LEGAL_TEXT("content.text"),
        LOGIN_BTN("btn_login"),
        LOGIN_TITLE("log_in_title"),
        PASSWORD_GHOST("password"),
        PASSWORD_RATING_GOOD("password_rating_good"),
        PASSWORD_RATING_GREAT("password_rating_great"),
        PASSWORD_RATING_FAIR("password_rating_fair"),
        PASSWORD_REQUIREMENTS("password_reqs"),
        PASSWORD_PAGE_TITLE("enter_your_password"),
        PRIVACY_TERMS_BTN("btn_terms_privacy"),
        MARKETING_TEXT(".text"),
        SIGN_UP_TITLE("sign_up_title"),
        UNKNOWN_EMAIL_BTN_SIGN_UP("sign_up_caps"),
        UNKNOWN_EMAIL_ERROR("log_in_email_error_no_account"),
        UNKNOWN_EMAIL_PAGE_TITLE("log_in_noaccount"),
        UNKNOWN_EMAIL_SUBTEXT("log_in_noaccount_subcopy"),
        UNKNOWN_EMAIL_TRY_AGAIN_BTN("btn_try_again");

        private String item;

        LoginItems(String item) {
            this.item = item;
        }

        public String getText() {
            return this.item;
        }
    }
}
